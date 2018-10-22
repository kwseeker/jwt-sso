package top.kwseeker.ssoapptwo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/servertwo")
public class ServiceController {
    private final static Logger logger = LoggerFactory.getLogger(ServiceController.class);
    private final static String AuthorizorURI = "http://localhost:8080";

    @GetMapping
    public String hello() {
        return "Hello";
    }

//    @PostMapping("/login")
//    public void login(HttpServletResponse response) {
//        try {
//            response.sendRedirect(AuthorizorURI + "/authorizor/login");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @PostMapping("/register")
//    public void register(HttpServletResponse response) {
//        try {
//            response.sendRedirect(AuthorizorURI + "/authorizor/register");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    @GetMapping("/serviceone")
//    public String offerSomeService(HttpServletRequest request, HttpServletResponse response){
//
//        logger.info("in offerSomeService() ...");
//
//        JSONObject jsonObject = (JSONObject) request.getAttribute("data");
//        String username = (String) jsonObject.get("uid");
//
//        return "Hello " + username + ", serverone offer service serviceone ..." ;
//    }
}

