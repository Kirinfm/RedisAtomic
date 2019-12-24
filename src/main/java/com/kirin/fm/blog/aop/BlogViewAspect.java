package com.kirin.fm.blog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class BlogViewAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.kirin.fm.blog.controller.IndexController.getBlog(..))")
    public void getBlogDetail() {
    }

    @AfterReturning(value = "getBlogDetail()", returning = "response")
    public void countBlogView(JoinPoint joinPoint, Object response) {
        if (null == ((ResponseEntity) response).getBody()) {
            return;
        }
        String blogId = joinPoint.getArgs()[0].toString();
        RedisAtomicLong entityIdCounter =
                new RedisAtomicLong(blogId, redisTemplate.getConnectionFactory());
        entityIdCounter.getAndIncrement();
        entityIdCounter.expire(24 * 60 * 60, TimeUnit.SECONDS);
    }
}
