package web.servlet;

public class HandlerExecutionChain {
    private final Object handler;

    public HandlerExecutionChain(Object handler) {
        this.handler = handler;
    }

    public Object getHandler() {
        return handler;
    }
}
