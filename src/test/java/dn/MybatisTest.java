package dn;

import dn.domain.Post;
import dn.domain.User;
import dn.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WxcommunityApplication.class)
public class MybatisTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void queryUser() {
        List<User> users = userMapper.queryUserList();
        System.out.println(users);
    }

    @Test
    public void queryPost() {
        List<Post> posts = userMapper.queryPostList();
        System.out.println(posts);
    }
}
