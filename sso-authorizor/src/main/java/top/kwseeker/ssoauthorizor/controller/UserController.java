package top.kwseeker.ssoauthorizor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.kwseeker.ssoauthorizor.dao.UserMapper;
import top.kwseeker.ssoauthorizor.pojo.User;
import top.kwseeker.ssoauthorizor.util.JWTUtil;
import top.kwseeker.ssoauthorizor.util.MD5Util;
import top.kwseeker.ssoauthorizor.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 此应用用于认证以及生成JWT令牌
 *
 * 1) 获取登录请求的用户名和密码；
 * 2）使用Mybatis从MySQL读取对应用户名的密码比较；登录失败的话，继续留在login页面
 * 3）生成JWT令牌,并返回
 */
@RestController
@RequestMapping("/authorizor")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final static String TAG = "<Lee UserController>";

    @Autowired
    UserMapper userMapper;

    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/login")
    public ResponseVO login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("in controller method login() ...");

        String userName=request.getParameter("userName");
        String password =request.getParameter("password");
        logger.info(TAG + "login: {}, {}", userName, password);
        ResponseVO responseVO;
        if(userMapper.countUserByName(userName) > 0 &&
                !password.equals(userMapper.selectUserByName(userName).getPassword())) {
            //生成token
            Map<String, Object> payload = new HashMap<>();
            Date date = new Date();
            payload.put("uid", userName);   //用户名
            payload.put("iat", date.getTime()); //生成的时间
            payload.put("ext", date.getTime() + 1000*60*30);    //过期时间0.5小时
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
        logger.info("in controller method register() ...");
        //TODO: 为了保证安全，前端传密码前也要先加密一次
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        ResponseVO responseVO;
        if(userName==null || password==null || "".equals(userName) || "".equals(password)) {
            responseVO = new ResponseVO(false, "username or password should not be empty");
        } else if(userMapper.countUserByName(userName) > 0) {
            responseVO = new ResponseVO(false, "user is already registered");
        } else {
            //MD5加密
            String passwordMD5 = MD5Util.MD5EncodeUtf8(password);
            userMapper.insertUser(new User(userName, passwordMD5));
            responseVO = new ResponseVO(true, "register succeed");
        }
        return responseVO;
    }
}
