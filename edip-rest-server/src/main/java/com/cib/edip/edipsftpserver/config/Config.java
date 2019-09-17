package com.cib.edip.edipsftpserver.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftpd")
public class Config {
    private Integer port;
    private String rootDir;
    private String serverName;
    private String registerServerUrl;
    private boolean registerServer;
    private String classPath;
    private String mainClass;


    @Getter
    public String getMainClass() {
        return mainClass;
    }
    @Setter
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }



    @Getter
    public String getClassPath() {
        return classPath;
    }
    @Setter
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    @Getter
    public Integer getPort() {
        return port;
    }

    @Setter
    public void setPort(Integer port) {
        this.port = port;
    }

    @Getter
    public String getRootDir() {
        return rootDir;
    }

    @Setter
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    @Getter
    public String getServerName() {
        return serverName;
    }

    @Setter
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }


    @Getter
    public String getRegisterServerUrl() {
        return registerServerUrl;
    }

    @Setter
    public void setRegisterServerUrl(String registerServerUrl) {
        this.registerServerUrl = registerServerUrl;
    }

    public boolean isRegisterServer() {
        return registerServer;
    }

    @Setter
    public void setRegisterServer(boolean registerServer) {
        this.registerServer = registerServer;
    }




}
