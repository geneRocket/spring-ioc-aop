package web.servlet.mvc.method;

import strereotype.Controller;
import web.bind.annotation.RequestMapping;
import web.servlet.handler.AbstractHandlerMethodMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public abstract class RequestMappingInfoHandlerMapping extends AbstractHandlerMethodMapping<RequestMappingInfo> {




    protected String getMappingPathPatterns(RequestMappingInfo info) {
        return info.getPath();
    }

    protected RequestMappingInfo getMatchingMapping(RequestMappingInfo info, HttpServletRequest request) {
        return info;
    }
}
