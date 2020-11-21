import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LogAspect{

	@Pointcut("execution(* *.test(..))")
	public void test() {
	}

	@Before("test()")
	public void test_before() {
		System.out.println("test bfeore");
	}

	@After("test()")
	public void test_after() {
		System.out.println("test after");
	}
}
