package com.example.multidatasource;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DynamicDataSource {
    LoadedDataSources name() default LoadedDataSources.PRIMARY;
}
