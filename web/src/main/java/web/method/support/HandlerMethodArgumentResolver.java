package web.method.support;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(Parameter parameter);
    Object resolveArgument(Parameter parameter, HttpServletRequest request);
}
