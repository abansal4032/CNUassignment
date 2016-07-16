package com.cnu2016.assignment04;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;



@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    public LoggerInterceptor() {

    }

    @Autowired
    private AWSQueueService awsQueueService;

    private static Logger logger = LoggerFactory.getLogger(LoggerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        long time = System.currentTimeMillis();
        logger.info(request.getMethod() + " " + request.getRequestURL().toString() + " called ");
        request.setAttribute("Timestamp",time);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        Map<String, String> queueMessage = new HashMap<String, String>();
        Date date = new Date();
        long time = System.currentTimeMillis();
        long requestTime = (long)request.getAttribute("Timestamp");
        long responseTime = time - requestTime;
        queueMessage.put("Time to respond",responseTime + "");
        queueMessage.put("Timestamp",date.toString());
        String url = request.getRequestURI();
        queueMessage.put("Url",url);
        Map<String, String> parameters = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        String temp = "";
        while (headerNames.hasMoreElements()) {
            String temp2 = "";
            String temp3 = (String) headerNames.nextElement();
            temp2 += temp3 + ":";
            String key = temp3;
            temp2 += request.getHeader(key) + ",";
            String value = request.getHeader(key);
            temp += temp2;
        }
        queueMessage.put("Parameters", temp);
        Integer responseCode = response.getStatus();
        queueMessage.put("Response Code",responseCode.toString());
        String ipAddress = request.getRemoteAddr();
        queueMessage.put("IpAddress",ipAddress);
        String json = new ObjectMapper().writeValueAsString(queueMessage);
        awsQueueService.sendMessage(json);
        logger.info(request.getMethod() + " " + request.getRequestURL().toString() + " executed with status " + response.getStatus());

    }
}
