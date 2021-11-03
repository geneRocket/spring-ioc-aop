package web.method.annotation;

import web.bind.annotation.RequestParam;
import web.method.support.HandlerMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getAnnotation(RequestParam.class)!=null;
    }

    @Override
    public Object resolveArgument(Parameter parameter, HttpServletRequest request) {
        RequestParam requestParamAnnotation= parameter.getAnnotation(RequestParam.class);
        String fieldName = requestParamAnnotation.name();
        String value=request.getParameter(fieldName);

        return value;
    }
}
