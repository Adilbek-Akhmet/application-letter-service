package soft.application.common;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;

/**
 * Method log aspect
 */
@Slf4j
@Aspect
@Component
public class LogAroundAspect {

    @Around("@annotation(LogAround)")
    public Object pointCut(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        LogAround ann = methodSignature.getMethod().getAnnotation(LogAround.class);
        String mthdName =  methodSignature.getMethod().getName();

        StringBuilder startMsgBuilder = new StringBuilder(!ann.startMsg().isEmpty() ? ann.startMsg() : "'{}()' method execution started");

        if (ann.logArgs()) {
            Parameter[] parameters = methodSignature.getMethod().getParameters();

            if (parameters != null && parameters.length > 0) {
                startMsgBuilder.append(". Arguments: ");

                for (int i = 0; i < parameters.length; ++i) {
                    String paramName = parameters[i].getName();
                    Object paramVal = joinPoint.getArgs()[i];

                    String paramLog = String.format("{%s = %s}", paramName, paramVal != null ? paramVal.toString() : "nil");
                    startMsgBuilder.append(paramLog);

                    if (i != parameters.length - 1) {
                        startMsgBuilder.append(" | ");
                    }
                }
            }
        }

        log(ann.logLevel(), startMsgBuilder.toString(), mthdName);

        try {
            Object proceed = joinPoint.proceed();
            log(ann.logLevel(), !ann.endMsg().isEmpty() ? ann.endMsg() : "'{}()' method execution succeeded in {}ms", mthdName, (System.currentTimeMillis() - startTime));
            return proceed;
        } catch (Throwable e) {
            log(ann.logLevel(), !ann.errMsg().isEmpty() ? ann.errMsg() : "'{}()' method execution failed in {}ms with err: {}", mthdName, (System.currentTimeMillis() - startTime), e.getMessage());
            throw e;
        }
    }

    private void log(LogLevel level, String pattern, Object ...args) {
        switch (level) {
            case INFO:
                log.info(pattern, args);
                break;
            case WARN:
                log.warn(pattern, args);
                break;
            case DEBUG:
                log.debug(pattern, args);
                break;
            case ERROR:
                log.error(pattern, args);
                break;
            case TRACE:
                log.trace(pattern, args);
                break;
            default:
                log.info(pattern, args);
        }
    }

}

