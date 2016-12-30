package com.yztc.core.literouter.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Activity RequestCode Annotation
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 16/10/21 11:30]
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestCode {
    int value();
}


//Retention：注释类型的注释要保留多久
//Target：指示注释类型所适用的程序元素的种类


