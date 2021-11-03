package core.util;

public class AntPathMatcher {
    public boolean isPattern(String path) {
        return (path.indexOf('*') != -1 || path.indexOf('?') != -1);
    }
    public boolean match(String pattern, String path){
        return true;
    }


}
