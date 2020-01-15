package com.video.vip.util;

import java.lang.annotation.*;

/**
 * @author wxn
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AllowNull {
    String value() default "";
}
