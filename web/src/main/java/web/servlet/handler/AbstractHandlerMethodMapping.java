package web.servlet.handler;

import aop.AopUtils;
import beans.factory.InitializingBean;
import core.MethodIntrospector;
import web.method.HandlerMethod;
import web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHandlerMethodMapping<T> extends AbstractHandlerMapping implements InitializingBean {

    private final MappingRegistry mappingRegistry = new MappingRegistry();


    @Override
    public void afterPropertiesSet() {
        initHandlerMethods();
    }

    protected abstract boolean isHandler(Class<?> beanType);


    protected void initHandlerMethods() {
        String[] beanNames = obtainApplicationContext().getBeanNamesForType(Object.class);
        for (String beanName : beanNames) {
            
                Class<?> beanType = null;
                try {
                    beanType = obtainApplicationContext().getType(beanName);
                }
                catch (Throwable ignored) {
                    
                }
                if (beanType != null && isHandler(beanType)) {
                    detectHandlerMethods(beanName);
                }
            
        }
    }

    protected void detectHandlerMethods(final Object handler) {
        Class<?> handlerType = (handler instanceof String ?
                obtainApplicationContext().getType((String) handler) : handler.getClass());

        if (handlerType != null) {
            final Class<?> userType = handlerType;
            Map<Method, T> methods = MethodIntrospector.selectMethods(userType,
                    method -> {
                        try {
                            return getMappingForMethod(method, userType);
                        }
                        catch (Throwable ex) {
                            throw new IllegalStateException("Invalid mapping on handler class [" +
                                    userType.getName() + "]: " + method, ex);
                        }
                    });
          
            for (Map.Entry<Method, T> entry : methods.entrySet()) {
                Method invocableMethod = entry.getKey();
                T mapping = entry.getValue();
                registerHandlerMethod(obtainApplicationContext().getBean((String) handler), invocableMethod, mapping);
            }
        }
    }

    protected void registerHandlerMethod(Object handler, Method method, T mapping) {
        this.mappingRegistry.register(mapping, handler, method);
    }

    protected abstract T getMappingForMethod(Method method, Class<?> handlerType);

    protected abstract String getMappingPathPatterns(T mapping);



    class MappingRegistry{
        private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap<>();

        private final Map<String, T> urlLookup = new LinkedHashMap<>();

        public void register(T mapping, Object handler, Method method) {

            HandlerMethod handlerMethod = new HandlerMethod(handler, method);


            this.mappingLookup.put(mapping, handlerMethod);

            String url = getMappingPathPatterns(mapping);
            this.urlLookup.put(url, mapping);

        }

        public T getMappingsByUrl(String urlPath) {
            return this.urlLookup.get(urlPath);
        }

        public Map<T, HandlerMethod> getMappings() {
            return this.mappingLookup;
        }
    }

    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        String lookupPath = request.getServletPath();
        T mapping = this.mappingRegistry.getMappingsByUrl(lookupPath);

        HandlerMethod handlerMethod = this.mappingRegistry.getMappings().get(mapping);

        return handlerMethod;

    }

}
