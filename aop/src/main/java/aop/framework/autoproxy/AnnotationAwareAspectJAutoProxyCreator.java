package aop.framework.autoproxy;

import aop.Advisor;
import aop.annotation.aspectj.BeanFactoryAspectJAdvisorsBuilder;
import beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;

public class AnnotationAwareAspectJAutoProxyCreator extends AbstractAdvisorAutoProxyCreator {
    private BeanFactoryAspectJAdvisorsBuilder aspectJAdvisorsBuilder;

    protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        this.aspectJAdvisorsBuilder =
                new BeanFactoryAspectJAdvisorsBuilder(beanFactory);
    }

    protected List<Advisor> findCandidateAdvisors() {
        return aspectJAdvisorsBuilder.buildAspectJAdvisors();
    }

}
