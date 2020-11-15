package io.kimmking.spring02;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Aop2 {
    
    @Pointcut(value="execution(* io.kimmking.*.Klass.*dong(..))")
    public void point(){
        
    }
    
    @Before(value="point()")
    public void before(){
        System.out.println("========>begin klass dong...");
    }
    
    @AfterReturning(value = "point()")
    public void after(){
        System.out.println("========>after klass dong...");
    }
    
    @Around("point()")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable{
        System.out.println("========>around begin klass dong");
        joinPoint.proceed();
        System.out.println("========>around after klass dong");
        
    }
    
}
