package dev.ss.world.event.eventapi;

import java.lang.annotation.*;

@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ShellEvent {
    byte value() default 2;
}
