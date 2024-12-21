package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 定义切点表达式，在Mapper层中有AutoFill注解的方法定义为切点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     *  前置增强，实现公共字段自动填充
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始公共字段自动填充");
//        获取到当前的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上额度注解对象
        OperationType operationType = annotation.value(); //获得数据库操作类型

//        获取到当前方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return;
        }
        Object entity = args[0]; //entity ->实体

//        准备要赋值的数据
        LocalDateTime createTime = LocalDateTime.now();
        LocalDateTime updateTime = LocalDateTime.now();
        Long createUser = BaseContext.getCurrentId();
        Long updateUser = BaseContext.getCurrentId();
//        根据不同的数据库操作类型，通过反射为数据赋值
        if (operationType == OperationType.UPDATE){
            Class<?> entityClass = entity.getClass();
            Method setUpdateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            setUpdateTime.invoke(entity,updateTime);
            setUpdateUser.invoke(entity,updateUser);
        }else if (operationType == OperationType.INSERT){
            Class<?> entityClass = entity.getClass();
            Method setUpdateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            Method setUpdateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
            Method setCreateUser = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setCreateTime = entityClass.getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            setUpdateTime.invoke(entity,updateTime);
            setUpdateUser.invoke(entity,updateUser);
            setCreateTime.invoke(entity,createTime);
            setCreateUser.invoke(entity,createUser);

        }
    }
}
