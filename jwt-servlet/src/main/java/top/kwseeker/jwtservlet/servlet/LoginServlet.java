package top.kwseeker.jwtservlet.servlet;

import net.minidev.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import top.kwseeker.jwtservlet.dao.UserMapper;
import top.kwseeker.jwtservlet.jwt.Jwt;
import top.kwseeker.jwtservlet.util.MybatisUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//使用注解 @WebServlet 代替 web.xml 中的 servlet类和访问路径的映射
@WebServlet(urlPatterns="/servlet/login",loadOnStartup=1)
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 5285600116871825644L;

    /**
     * 校验用户名密码
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userName=request.getParameter("userName");
        String password =request.getParameter("password");
        JSONObject resultJSON=new JSONObject();

        //用户名密码校验成功后，生成token返回客户端
        SqlSession sqlSession = MybatisUtil.getSqlSession("mybatis-config.xml");
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
//        if("admin".equals(userName)&&"123".equals(password)){
        if(userMapper.countUserByName(userName) > 0 &&
                !password.equals(userMapper.selectUserByName(userName).getPassword())){
            //生成token
            Map<String , Object> payload=new HashMap<String, Object>();
            Date date=new Date();
            payload.put("uid", "admin");//用户ID
            payload.put("iat", date.getTime());//生成时间
            payload.put("ext",date.getTime()+1000*60*60);//过期时间1小时
            String token=Jwt.createToken(payload);

            resultJSON.put("success", true);
            resultJSON.put("msg", "登陆成功");
            resultJSON.put("token", token);

        }else{
            resultJSON.put("success", false);
            resultJSON.put("msg", "用户名密码不对");
        }
        //输出结果
        output(resultJSON.toJSONString(), response);
    }

    public void output(String jsonStr,HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8;");
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        out.flush();
        out.close();
    }
}
