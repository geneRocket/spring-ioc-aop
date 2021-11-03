package web.servlet.mvc.method;

import web.servlet.mvc.condition.RequestCondition;

public class RequestMappingInfo implements RequestCondition<RequestMappingInfo> {
    private final String path;

    public RequestMappingInfo(String path) {
        this.path = path;
    }

    public RequestMappingInfo combine(RequestMappingInfo other) {
        String paths = this.path+other.path;
        return new RequestMappingInfo(paths);
    }

    public String getPath() {
        return path;
    }
}
