package com.netease.mc.modSS.mod;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Info {
    String name();
    
    String description();
    
    Category category();
    
    int keybind() default 0;
}
