package top.kwseeker.jwtservlet.servlet;

import net.minidev.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import top.kwseeker.jwtservlet.dao.UserMapper;
import top.kwseeker.jwtservlet.pojo.User;
import top.kwseeker.jwtservlet.util.MD5Util;
import top.kwseeker.jwtservlet.util.MybatisUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//用户注册将用户名密码等传入到mysql
@WebServlet(urlPatterns = "/servlet/register", loadOnStartup = 1)
public class RegisterServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Post request: /servlet/register");

        JSONObject outputMSg=new JSONObject();

        //TODO: 为了保证安全，前端传密码前也要先加密一次
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        SqlSession sqlSession = MybatisUtil.getSqlSession("mybatis-config.xml");
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        if(userName==null || password==null || "".equals(userName) || "".equals(password)) {
            outputMSg.put("success", false);
            outputMSg.put("msg", "用户名和密码输入格式错误");
        } else if(userMapper.countUserByName(userName) > 0) {
            outputMSg.put("success", false);
            outputMSg.put("msg", "该用户名已注册");
        } else {
            //MD5加密
            String passwordMD5 = MD5Util.MD5EncodeUtf8(password);
            userMapper.insertUser(new User(userName, passwordMD5));

            outputMSg.put("success", true);
            outputMSg.put("msg", "注册成功");
        }

        try {
            response.setContentType("text/html;charset=UTF-8;");
            PrintWriter out = response.getWriter();
            out.println(outputMSg.toJSONString());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
