package web.servlet;

import context.ApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends FrameworkServlet{


    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";


    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doDispatch(request, response);
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpServletRequest processedRequest = request;

        HandlerExecutionChain mappedHandler = null;
        mappedHandler = getHandler(processedRequest);
        if (mappedHandler == null) {
            noHandlerFound(processedRequest, response);
            return;
        }
        HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
        ha.handle(processedRequest, response, mappedHandler.getHandler());

    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter ha : this.handlerAdapters) {
                if (ha.supports(handler)) {
                    return ha;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
                "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }

    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);

    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping hm : this.handlerMappings) {
                HandlerExecutionChain handler = hm.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }

    protected void initStrategies(ApplicationContext context) {

        initHandlerMappings(context);
        initHandlerAdapters(context);

    }

    private void initHandlerMappings(ApplicationContext context){
        Map<String, HandlerMapping> matchingBeans =context.getBeansOfType(HandlerMapping.class);
        if (!matchingBeans.isEmpty()){
            this.handlerMappings = new ArrayList<>(matchingBeans.values());
        }

    }

    private void initHandlerAdapters(ApplicationContext context){
        Map<String, HandlerAdapter> matchingBeans = context.getBeansOfType(HandlerAdapter.class);
        if (!matchingBeans.isEmpty()) {
            this.handlerAdapters = new ArrayList<>(matchingBeans.values());
        }
    }
}
