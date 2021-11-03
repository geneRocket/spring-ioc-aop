package web.servlet.mvc.condition;

public interface RequestCondition <T>{
    T combine(T other);
}
