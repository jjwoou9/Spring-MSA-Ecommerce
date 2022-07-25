package com.amp.secondservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/second-service")
public class SecondServiceController {

    Environment env;

    @Autowired
    public SecondServiceController(Environment env){
        this.env = env;
    }

    @GetMapping("/welcome")
    public  String welcome(){
        return "Welcome to the Second Service.";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header){
        log.info(header);

        return "First Second Message.";
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port={}", request.getServerPort());
        return String.format("Second Service Check on PORT %s", env.getProperty("local.server.port"));
    }
}
