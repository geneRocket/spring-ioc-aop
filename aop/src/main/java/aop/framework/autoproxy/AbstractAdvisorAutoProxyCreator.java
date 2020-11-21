package aop.framework.autoproxy;

import aop.Advisor;
import aop.AopUtils;
import beans.factory.BeanFactory;
import beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;

public abstract class AbstractAdvisorAutoProxyCreator extends  AbstractAutoProxyCreator {

    protected Object[] getAdvicesAndAdvisorsForBean(
            Class<?> beanClass, String beanName,Object targetSource) {

        List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName,targetSource);
        if (advisors.isEmpty()) {
            return DO_NOT_PROXY;
        }
        return advisors.toArray();
    }

    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName, Object target) {
        List<Advisor> candidateAdvisors = findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        if (!eligibleAdvisors.isEmpty()) {
            //eligibleAdvisors = sortAdvisors(eligibleAdvisors);
        }
        return eligibleAdvisors;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
            throw new IllegalArgumentException(
                    "AdvisorAutoProxyCreator requires a ConfigurableListableBeanFactory: " + beanFactory);
        }
        initBeanFactory((ConfigurableListableBeanFactory) beanFactory);
    }

    protected List<Advisor> findAdvisorsThatCanApply(
            List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

        return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);

    }

    protected abstract void initBeanFactory(ConfigurableListableBeanFactory beanFactory);

    protected abstract List<Advisor> findCandidateAdvisors();
}
