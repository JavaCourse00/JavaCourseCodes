package io.kimmking.rpcfx.demo.provider;

public class ServiceProviderDesc {

    private String host;
    private Integer port;
    private String serviceClass;

    // group
    // version

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(String serviceClass) {
        this.serviceClass = serviceClass;
    }
}
