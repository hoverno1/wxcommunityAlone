package dn.controller;

import dn.domain.Post;
import dn.domain.User;
import dn.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MybatisController {
    @Autowired
    private UserMapper userMapper;

    private String picPlace;
    private String picPlace2;
    private String aid;
    private String avatar;
    private String disease;
    private String diseaseContent;
    private final String prefixImg="https://my.plantdisrecogn.com/wxcommunity/image/";

    //获取author_login表中数据
    @RequestMapping("/queryUser")
    @ResponseBody
    public List<User> queryLogin() {
        List<User> users = userMapper.queryUserList();
        return users;
    }

    //获取author_post表中数据
    @RequestMapping("/queryPost")
    @ResponseBody
    public List<Post> queryPost() {
        List<Post> posts = userMapper.queryPostList();
        return posts;
    }

    //寻找图片所在位置
    @Configuration
    public class WebMvcConfiguration extends WebMvcConfigurerAdapter {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/image/**").addResourceLocations("file:C:/Users/Administrator/Desktop/wxcommuitypic/");
            super.addResourceHandlers(registry);
        }
    }

    //获取前端数据aid,将图片存入author_login表中
    @RequestMapping("/postAid")
    @ResponseBody
    public void postAid(HttpServletRequest request) {
        String aid = request.getParameter("aid");
        System.out.println(aid);
        User u = new User();
        u.setAid(Integer.parseInt(aid));
        u.setAvatar(prefixImg+picPlace);
        userMapper.insertAvatar(u);
    }

    //获取author_login表中数据aid返回给前端
    @RequestMapping("/queryAid")
    @ResponseBody
    public int queryAid(HttpServletRequest request) {
        String author = request.getParameter("username");
        //System.out.println(author);
        User u = userMapper.queryAid(author);
        return u.getAid();
    }

    //根据前端传来的数据aid以及要发布的详情信息（不包含图片），将这些信息存储到相对应的aid的数据库中
    //要注意需要将author_login中的avatar头像复制到对应的author_post中
    @RequestMapping("/publishPost")
    @ResponseBody
    public int publishPost(HttpServletRequest request) {
        aid = null;
        disease = null;
        diseaseContent = null;
        aid = request.getParameter("aid");
        disease = request.getParameter("disease");
        diseaseContent = request.getParameter("diseaseContent");
        //System.out.println(aid + "===" + disease + "===" + diseaseContent);
        if (aid.equals("null") || disease.equals("null") || diseaseContent.equals("null")) {
            return 0;
        } else {
            return 1;
        }
    }

    //根据前端传来的数据aid以及要发布的详情信息（包含图片），将这些信息存储到相对应的aid的数据库中
    //要注意需要将author_login中的avatar头像复制到对应的author_post中
    //并将发布内容存入数据库
    @RequestMapping(value = "/publishPostPic", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public int publishPostPic(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");

        //realPath填写电脑文件夹路径
        String realPath = "C:\\Users\\Administrator\\Desktop\\wxcommuitypic";

        //格式化时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String nowTime = sdf.format(new Date().getTime());

        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
        String originalLastName = multipartFile.getOriginalFilename();
        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));

        //拼接：名字+时间戳+后缀
        String picName = picFirstName + "." + nowTime + picLastName;
        try {
            File dir = new File(realPath);
            //如果文件目录不存在，创建文件目录
            if (!dir.exists()) {
                dir.mkdir();
                //System.out.println("创建文件目录成功：" + realPath);
            }
            File file = new File(realPath, picName);
            //获取真正的用户名，并调用方法存入
            picPlace2 = file.getName();
            multipartFile.transferTo(file);
            //System.out.println("添加图片成功！");
            //将发布内容存入数据库
            //查询头像信息
            //System.out.println(aid);
            User u1 = userMapper.queryAvatar(aid);
            //System.out.println(u1);
            avatar = u1.getAvatar();
            //System.out.println(avatar);
            //信息存入数据库
            Post post = new Post();
            post.setAvatar(avatar);
            post.setDate(nowTime);
            post.setDetail(diseaseContent);
            post.setImgSrc(prefixImg+picPlace2);
            post.setDisease(disease);
            post.setAid(aid);
            userMapper.insertPost(post);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //向数据库录入用户注册信息（用户名，密码，状态），aid自动增加生成
    @RequestMapping("/Login")
    @ResponseBody
    public int Login(HttpServletRequest request) {
        //flag是注册成功与否标志 0失败 1成功
        int flag = 0;
        String frontUsername = request.getParameter("username");
        String frontPassword = request.getParameter("password");
        //System.out.println(frontUsername + "===" + frontPassword);
        if (frontUsername == "" || frontPassword == "") {
            //System.out.println("两个数据为空");
            return 0;
        }
        if(userMapper.testName(frontUsername)!=0) return 0;
        //封装一个包含信息的user对象
        User u = new User(null, frontUsername, frontPassword, 0, "");
        //将此user对象注入到数据库中
        userMapper.insertUser(u);
        //信息名为flag 1：有此用户信息 0：无此用户信息
        return 1;
    }

    //向数据库录入用户注册信息（头像）
    @RequestMapping(value = "/LoginPic", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public int uploadPicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MultipartHttpServletRequest req = (MultipartHttpServletRequest) request;

        //对应前端的upload的name参数"file"
        MultipartFile multipartFile = req.getFile("file");

        //realPath填写电脑文件夹路径
        String realPath = "C:\\Users\\Administrator\\Desktop\\wxcommuitypic";

        //格式化时间戳
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String nowTime = sdf.format(new Date().getTime());

        //裁剪用户id
        String originalFirstName = multipartFile.getOriginalFilename();
        String picFirstName = originalFirstName.substring(0, originalFirstName.indexOf("."));

        //取得图片的格式后缀
        String originalLastName = multipartFile.getOriginalFilename();
        String picLastName = originalLastName.substring(originalLastName.lastIndexOf("."));
        //拼接：名字+时间戳+后缀
        String picName = picFirstName + "." + nowTime + picLastName;
        //System.out.println(picName);
        try {
            File dir = new File(realPath);
            //如果文件目录不存在，创建文件目录
            if (!dir.exists()) {
                dir.mkdir();
                System.out.println("创建文件目录成功：" + realPath);
            }
            File file = new File(realPath, picName);
            //获取真正的用户名，并调用方法存入
            picPlace = file.getName();
            multipartFile.transferTo(file);
            //System.out.println("添加图片成功！");
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //获取检验前端返回的登陆信息
    @RequestMapping("/Test")
    @ResponseBody
    public int Test(HttpServletRequest request) {
        String frontUsername = request.getParameter("username");
        String frontPassword = request.getParameter("password");
        //System.out.println(frontUsername + "===" + frontPassword);
        List<User> users = queryLogin();
        int flag = 0;
        //遍历users,查找是否有此用户
        for (User user : users) {
            if (frontUsername.equals(user.getAuthor()) && frontPassword.equals(user.getPassword())) {
                //有就改变status状态，返回确认信息
                user.setStatus(1);
                //1就是flag的值
                return 1;
            }
        }
        //无就返回否定信息
        //信息名为flag 1：有此用户信息 0：无此用户信息
        return flag;
    }
}
