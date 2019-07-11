package ynu.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import ynu.enums.InterceptorLevel;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})//type���Ը����ע��,method���Ը���������ע��
@Retention(RetentionPolicy.RUNTIME)//������ʱ��Ч��������ʱ������
public @interface AuthInterceptor {
	
	  /**
     * �������ؼ���Ĭ��Ϊ�û���������
     *
     * @return {@link InterceptorLevel}
     */
    InterceptorLevel value() default InterceptorLevel.USER;
}
