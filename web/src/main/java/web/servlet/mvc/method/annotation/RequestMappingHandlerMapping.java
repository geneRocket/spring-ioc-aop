package web.servlet.mvc.method.annotation;

import strereotype.Controller;
import web.bind.annotation.RequestMapping;
import web.servlet.mvc.method.RequestMappingInfo;
import web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class RequestMappingHandlerMapping extends RequestMappingInfoHandlerMapping {

    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = createRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
        }
        return info;
    }

    private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = element.getAnnotation(RequestMapping.class);
        if(requestMapping==null){
            return null;
        }
        return createRequestMappingInfo(requestMapping);
    }

    protected RequestMappingInfo createRequestMappingInfo(
            RequestMapping requestMapping) {

        return new RequestMappingInfo(requestMapping.path());

    }

    @Override
    protected boolean isHandler(Class<?> beanType) {
        return beanType.isAnnotationPresent(Controller.class) || beanType.isAnnotationPresent(RequestMapping.class);
    }


}
