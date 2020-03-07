package com.example.multidatasource;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DynamicDataSourceAspect {
    public static Logger log = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    @Pointcut("@annotation(com.example.multidatasource.DynamicDataSource)")
    public void dynamicDataSource() {
    }

    @Around("dynamicDataSource()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DynamicDataSource dynamicDataSource = method.getAnnotation(DynamicDataSource.class);
        if (dynamicDataSource == null) {
            DynamicRoutingDataSource.setDataSource(LoadedDataSources.PRIMARY);
            log.info("DynamicRoutingDataSource to {}", LoadedDataSources.PRIMARY);
        } else {
            DynamicRoutingDataSource.setDataSource(dynamicDataSource.name());
            log.info("DynamicRoutingDataSource to {}", dynamicDataSource.name());
        }
        try {
            point.proceed();
        } finally {
            DynamicRoutingDataSource.clearDataSource();
            log.info("cleared datasource");
        }

        return null;
    }
}
