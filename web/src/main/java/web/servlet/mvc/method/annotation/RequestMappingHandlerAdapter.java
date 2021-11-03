package web.servlet.mvc.method.annotation;

import context.support.ApplicationObjectSupport;
import web.method.HandlerMethod;
import web.method.annotation.RequestParamMethodArgumentResolver;
import web.method.support.HandlerMethodArgumentResolver;
import web.servlet.HandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Parameter;

public class RequestMappingHandlerAdapter extends ApplicationObjectSupport implements HandlerAdapter {
    HandlerMethodArgumentResolver argumentResolver = new RequestParamMethodArgumentResolver();
    RequestResponseBodyMethodProcessor requestResponseBodyMethodProcessor=new RequestResponseBodyMethodProcessor();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerMethod;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        invokeHandlerMethod(request, response, (HandlerMethod) handler);
    }

    protected void  invokeHandlerMethod(HttpServletRequest request,
                                               HttpServletResponse response, HandlerMethod handlerMethod) throws Exception{
        Object []args=getMethodArgumentValues(request,handlerMethod);

        Object returnValue = handlerMethod.getMethod().invoke(handlerMethod.getBean(),args);
        this.requestResponseBodyMethodProcessor.handleReturnValue(returnValue,response);

    }

    private Object[] getMethodArgumentValues(HttpServletRequest request,HandlerMethod handlerMethod){
        Parameter[] parameters=handlerMethod.getMethod().getParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];

            if (this.argumentResolver.supportsParameter(parameter)) {
                args[i] = this.argumentResolver.resolveArgument(
                        parameter, request);
            }
            if (args[i] == null) {
                throw new IllegalStateException("Could not resolve method parameter at index ");
            }
        }
        return args;
    }


}
