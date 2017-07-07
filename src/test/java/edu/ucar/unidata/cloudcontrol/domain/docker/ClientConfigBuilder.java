package edu.ucar.unidata.cloudcontrol.domain.docker;

import java.util.Date;

public class ClientConfigBuilder {

    private ClientConfig clientConfig;

    public ClientConfigBuilder() {
        clientConfig = new ClientConfig();
    }

    public ClientConfigBuilder id(int id) {
        clientConfig.setId(id);
        return this;
    }

    public ClientConfigBuilder dockerHost(String dockerHost) {
        clientConfig.setDockerHost(dockerHost);
        return this;
    }

    public ClientConfigBuilder dockerCertPath(String dockerCertPath) {
        clientConfig.setDockerCertPath(dockerCertPath);
        return this;
    }

    public ClientConfigBuilder dockerTlsVerify(int dockerTlsVerify) {
        clientConfig.setDockerTlsVerify(dockerTlsVerify);
        return this;
    }

    public ClientConfigBuilder createdBy(String createdBy) {
        clientConfig.setCreatedBy(createdBy);
        return this;
    }

    public ClientConfigBuilder lastUpdatedBy(String lastUpdatedBy) {
        clientConfig.setLastUpdatedBy(lastUpdatedBy);
        return this;
    }

    public ClientConfigBuilder dateCreated(Date dateCreated) {
        clientConfig.setDateCreated(dateCreated);
        return this;
    }

    public ClientConfigBuilder dateModified(Date dateModified) {
        clientConfig.setDateModified(dateModified);
        return this;
    }

    public ClientConfig build() {
        return clientConfig;
    }
}
