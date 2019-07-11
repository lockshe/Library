package ynu.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ynu.enums.InterceptorLevel;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})//type可以给类加注解,method可以给方法加上注解
@Retention(RetentionPolicy.RUNTIME)//在运行时有效（即运行时保留）
public @interface AuthInterceptor {
	
	  /**
     * 定义拦截级别，默认为用户级别拦截
     *
     * @return {@link InterceptorLevel}
     */
    InterceptorLevel value() default InterceptorLevel.USER;
}
