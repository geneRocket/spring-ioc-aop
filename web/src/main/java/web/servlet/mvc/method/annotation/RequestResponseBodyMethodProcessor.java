package web.servlet.mvc.method.annotation;

import web.servlet.mvc.method.support.HandlerMethodReturnValueHandler;

import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;

import java.io.IOException;


public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {
    private final Gson gson;

    public RequestResponseBodyMethodProcessor() {
        this.gson = new Gson();
    }

    public void handleReturnValue(Object returnValue, HttpServletResponse response){
        try {
            response.setHeader("Content-Type", "application/json");
            this.gson.toJson(returnValue, response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
