package top.kwseeker.sso.authorizer.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.sso.authorizer.dao.UserMapper;
import top.kwseeker.sso.authorizer.domain.jwt.JWTAuth;
import top.kwseeker.sso.authorizer.pojo.User;
import top.kwseeker.sso.authorizer.util.JWTUtil;
import top.kwseeker.sso.authorizer.util.MD5Util;
import top.kwseeker.sso.authorizer.vo.ResponseVO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 此应用用于认证以及生成JWT令牌
 * <p>
 * 1) 获取登录请求的用户名和密码；
 * 2）使用Mybatis从MySQL读取对应用户名的密码比较；登录失败的话，继续留在login页面
 * 3）生成JWT令牌,并返回
 */
@Slf4j
@RestController
@RequestMapping("/authorizer")
public class UserController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private JWTAuth jwtAuth;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseVO login(HttpServletRequest request, HttpServletResponse response) {
        ResponseVO responseVO;
        log.info("in controller method login() ...");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordMD5 = MD5Util.MD5EncodeUtf8(password);
        log.info("login: {}, {}", username, password);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return new ResponseVO(false, "username and password could not be blank");
        }

        User user = userMapper.selectUserByName(username);
        if (user != null && passwordMD5.equals(user.getPassword())) {
            //生成JWT令牌
            Map<String, Object> payload = new HashMap<>();
            Date date = new Date();
            payload.put("uid", username);       //用户名
            payload.put("iat", date.getTime()); //生成的时间
            payload.put("ext", date.getTime() + 1000 * 60 * 30);    //过期时间30分钟
            String token = JWTUtil.createToken(payload);

            responseVO = new ResponseVO(true, "login succeed", token);
        } else {
            responseVO = new ResponseVO(false, "username or password error");
        }
        return responseVO;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseVO register(HttpServletRequest request, HttpServletResponse response) {
        log.info("in controller method register() ...");
        //TODO: 为了保证安全，前端传密码前也要先加密一次
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        ResponseVO responseVO;
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            responseVO = new ResponseVO(false, "username or password could not be blank");
        } else if (userMapper.countUserByName(username) > 0) {
            responseVO = new ResponseVO(false, "user is already registered");
        } else {
            //MD5加密
            String passwordMD5 = MD5Util.MD5EncodeUtf8(password);
            userMapper.insertUser(new User(username, passwordMD5));
            responseVO = new ResponseVO(true, "register succeed");
        }
        return responseVO;
    }
}
