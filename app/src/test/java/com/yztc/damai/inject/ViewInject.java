package com.yztc.damai.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wanggang on 2017/1/6.
 */

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewInject {
    int value() default -1;
}

//定义的一个注解  @interface
//Target ->表明应用在什么地方
//        1.CONSTRUCTOR:用于描述构造器
//        2.FIELD:用于描述域
//        3.LOCAL_VARIABLE:用于描述局部变量
//        4.METHOD:用于描述方法
//        5.PACKAGE:用于描述包
//        6.PARAMETER:用于描述参数
//        7.TYPE:用于描述类、接口(包括注解类型) 或enum声明

//Retention ->注解的生命周期
//        1.SOURCE:在源文件中有效（即源文件保留）   标识
//        2.CLASS:在class文件中有效（即class保留） 编译时处理
//        3.RUNTIME:在运行时有效（即运行时保留）    运行时处理

