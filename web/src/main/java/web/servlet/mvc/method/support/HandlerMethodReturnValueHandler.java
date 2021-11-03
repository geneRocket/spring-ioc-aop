package web.servlet.mvc.method.support;

import javax.servlet.http.HttpServletResponse;

public interface HandlerMethodReturnValueHandler {
    void handleReturnValue(Object returnValue, HttpServletResponse response);
}
