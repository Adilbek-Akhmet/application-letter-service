package soft.application.common;

import org.springframework.boot.logging.LogLevel;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface LogAround {

    /**
     * Param to override default method start log message
     */
    String startMsg() default "";

    /**
     * Param to override default method end log message
     */
    String endMsg() default "";

    /**
     * Param to override default method error log message
     */
    String errMsg() default "";

    /**
     * Log level in aspect
     */
    LogLevel logLevel() default LogLevel.INFO;

    /**
     * if true, intercepted method's argument names and values will be logged in startMsg()
     */
    boolean logArgs() default false;

}
