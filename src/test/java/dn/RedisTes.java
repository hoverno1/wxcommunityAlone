package dn;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dn.domain.User;
import dn.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WxcommunityApplication.class)
public class RedisTes {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() throws JsonProcessingException {
        //1.从redis中获取数据  数据形式：json
        String userListJson = redisTemplate.boundValueOps("user.queryUserList").get();
        //2.判断redis中是否存在数据
        if (userListJson == null) {
            //3.1如果数据不存在，从数据库MySQL中查询
            List<User> users = userMapper.queryUserList();
            //3.2将获取的数据存储到redis中
            //将list转换为json字符串格式  使用Jackson来进行转换
            ObjectMapper objectMapper = new ObjectMapper();
            userListJson = objectMapper.writeValueAsString(users);
            //存储数据进redis
            redisTemplate.boundValueOps("user.queryUserList").set(userListJson);
            System.out.println("=====从数据库MySQL中获取=====");
        } else {
            System.out.println("=====从数据库redis中获取=====");
        }
        //4.如果数据存在，获取redis数据（此处直接打印）
        System.out.println(userListJson);
    }
}
