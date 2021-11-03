package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.BeanUtils;
import context.ApplicationContext;
import context.ConfigurableWebApplicationContext;
import context.WebApplicationContext;
import context.support.XmlWebApplicationContext;

import java.io.IOException;

public abstract class FrameworkServlet extends HttpServlet {
    public static final String DEFAULT_NAMESPACE_SUFFIX = "-servlet";

    private WebApplicationContext webApplicationContext;

    public final void init() {
        initServletBean();
    }

    protected final void initServletBean(){
        this.webApplicationContext = initWebApplicationContext();
    }

    protected WebApplicationContext initWebApplicationContext(){

        WebApplicationContext wac = createWebApplicationContext();

        onRefresh(wac);
        return wac;
    }

    protected void onRefresh(ApplicationContext context) {
        // For subclasses: do nothing by default.
    }

    protected WebApplicationContext createWebApplicationContext(){
        Class<?> contextClass = XmlWebApplicationContext.class;
        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }

    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac){
        wac.setNamespace(getNamespace());
        wac.refresh();
    }

    public String getNamespace() {
        return  getServletName() + DEFAULT_NAMESPACE_SUFFIX;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new ServletException("processrequest error",e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected final void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        doService(request, response);
    }

    protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
