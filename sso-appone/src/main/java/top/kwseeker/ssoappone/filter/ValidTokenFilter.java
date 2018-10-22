package top.kwseeker.ssoappone.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import top.kwseeker.ssoappone.util.JWTUtil;
import top.kwseeker.ssoappone.util.TokenState;
import top.kwseeker.ssoappone.vo.ResponseVO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Order(1)
@WebFilter(filterName = "ValidTokenFilter", urlPatterns = "/*")
public class ValidTokenFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(ValidTokenFilter.class);
    private final static String TAG = "<Lee Filter> ";

    @Override
    public void init(FilterConfig config) throws ServletException {
        logger.info(TAG + "ValidTokenFilter init ...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.info(TAG + "Request filtered to validate token status ...");
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        //不需要认证保护的URI直接放行
        String uri = httpServletRequest.getRequestURI();
        if(uri.endsWith("/login") || uri.endsWith("/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        //需要认证保护的URI获取头部中的token, 对token进行校验
        logger.info(TAG + "validate token, requestUri: " + httpServletRequest.getRequestURI());
        String token = httpServletRequest.getHeader("token");   //获取头部中token字段
        Map<String, Object> resultMap = JWTUtil.validToken(token);
        TokenState state = TokenState.getTokenState((String) resultMap.get("state"));
        switch (state) {
            case VALID:
                //获取payload data
                logger.info("token valid ...");
                httpServletRequest.setAttribute("data", resultMap.get("data"));
                filterChain.doFilter(request, response);
                break;
            case EXPIRED:
            case INVALID:
                //失败返回json格式错误信息
                logger.info("token invalid ...");
                ResponseVO responseVO = new ResponseVO(false, "token is invalid or expired");

                ObjectMapper mapper = new ObjectMapper();
                String responseVoJson = mapper.writeValueAsString(responseVO);
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("text/html; charset=UTF-8");
                PrintWriter writer = httpServletResponse.getWriter();
                writer.print(responseVoJson);
                writer.close();
                break;
        }
    }

    @Override
    public void destroy() {
    }
}
