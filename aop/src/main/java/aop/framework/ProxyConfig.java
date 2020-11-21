package aop.framework;

public class ProxyConfig {
    private boolean proxyTargetClass = false;

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    public boolean isProxyTargetClass() {
        return this.proxyTargetClass;
    }

    public void copyFrom(ProxyConfig other) {
        this.proxyTargetClass = other.proxyTargetClass;
    }
}
