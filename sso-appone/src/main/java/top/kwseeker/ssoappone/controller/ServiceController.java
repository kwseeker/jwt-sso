package top.kwseeker.ssoappone.controller;

import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 针对每一个前端来的请求均设置Token校验
 *
 * 首先判断header中是否带token,
 *      没有token, 转到sso-authorizor登录界面
 *          登录成功的话，返回到业务界面
 *      有token, 解析token验证是否有效
 *          有效，继续执行业务逻辑；
 *          失效，转到sso-authorizor登录界面
 */
@RestController
@RequestMapping("/serverone")
public class ServiceController {
    private final static Logger logger = LoggerFactory.getLogger(ServiceController.class);
    private final static String AuthorizorURI = "http://localhost:8080";

    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        try {
//            response.sendRedirect(AuthorizorURI + "/authorizor/login");   //failed: 只能重定向发送get请求
//            request.getRequestDispatcher("AuthorizorURI + /authorizor/login").forward(request, response); //failed: 只能此应用中转发
            //finally
            response.sendRedirect(AuthorizorURI + "login.html");    //重定向到前端页面，前端页面发送Post请求到后端，请求token
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/register")
    public void register(HttpServletRequest request, HttpServletResponse response) {
        try {
//            response.sendRedirect(AuthorizorURI + "/authorizor/register");
//            request.getRequestDispatcher("/authorizor/register").forward(request, response);
            //finally
            response.sendRedirect(AuthorizorURI + "register.html");    //重定向到前端页面，前端页面发送Post请求到后端，请求token
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/serviceone")
    public String offerSomeService(HttpServletRequest request, HttpServletResponse response){

        logger.info("in offerSomeService() ...");

        JSONObject jsonObject = (JSONObject) request.getAttribute("data");
        String username = (String) jsonObject.get("uid");

        return "Hello " + username + ", serverone offer service serviceone ..." ;
    }
}
