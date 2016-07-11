package com.cnu2016.assignment04;

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


@Component
public class LoggerInterceptor extends HandlerInterceptorAdapter {

    public LoggerInterceptor() {

    }

    @Autowired
    private AWSQueueService awsQueueService;

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        Map<String, String> queueMessage = new HashMap<String, String>();
        Date date = new Date();
        queueMessage.put("Timestamp",date.toString());
        String url = request.getRequestURI();
        queueMessage.put("Url",url);
        Map<String, String> parameters = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            parameters.put(key, value);
        }
        queueMessage.put("Parameters", parameters.toString());
        Integer responseCode = response.getStatus();
        queueMessage.put("Response Code",responseCode.toString());
        String ipAddress = request.getRemoteAddr();
        queueMessage.put("IpAddress",ipAddress);
        awsQueueService.sendMessage(queueMessage.toString());

    }
}