package context.support;

import beans.BeansException;
import core.io.ClassPathResource;
import core.io.Resource;

public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext {
    private Resource configResources;

    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        super();
        this.configResources = new ClassPathResource(configLocation);
        refresh();
    }

    protected Resource getConfigResources() {
        return this.configResources;
    }


}
