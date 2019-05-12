package com.wk.proxy.springAOP.spring3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class Advices {
    //前置通知
    public void before(JoinPoint joinPoint){
        System.out.println("----------------before-------------------------");
        System.out.println("methos is:"+joinPoint.getSignature()+", arguments :"+joinPoint.getArgs()+", target:"+
        joinPoint.getTarget());
    }
    //后置通知
    public void after(JoinPoint jp){
        System.out.println("----------------after-------------------------");
    }
    //返回值通知
    public void afterReturing(JoinPoint joinPoint,Object result){
        System.out.println("----------------afterReturing-------------------------");
        System.out.println("this result is: "+result);
    }

    //抛出异常通知
    //在方法出现异常时会执行的代码，可以访问到异常对象
    //可以指定在出现特定异常时再执行通知代码
    public void afterThrowing(JoinPoint joinPoint,Exception exp){
        System.out.println("----------------afterThrowing-----------------the exceptio is:"+exp.getMessage());

    }

    //环绕通知
    /**
     *  环绕通知需要携带ProceedingJoinPoint类型的参数
     *  环绕通知类似于动态代理的全过程，ProceedingJoinPoint类型的参数可以决定是否执行目标方法
     *  而且环绕通知必须有返回值，返回值为目标方法的返回值
     */
    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
        System.out.println("----------环绕开始-------------------------");
        Object result = pjd.proceed();
        System.out.println("----------环绕结束-------------------------");
        return result;
    }
}
