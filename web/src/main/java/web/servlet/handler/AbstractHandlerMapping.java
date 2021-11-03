package web.servlet.handler;

import context.support.ApplicationObjectSupport;
import web.servlet.HandlerExecutionChain;
import web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractHandlerMapping extends ApplicationObjectSupport implements HandlerMapping {
    public final HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        Object handler = getHandlerInternal(request);

        if (handler == null) {
            return null;
        }

        HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

        return executionChain;
    }

    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain ?
                (HandlerExecutionChain) handler : new HandlerExecutionChain(handler));


        return chain;
    }

    protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;

}
