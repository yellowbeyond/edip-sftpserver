package com.cib.edip.edipsftpserver.sftpd.config;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import scala.Function0;



public class Sftpd{

    private Integer port;
    private String rootDir;
    private String serverName;
    private String registerServerPath;

    @Setter
    @Getter
    public Integer getPort() {
        return port;
    }

    @Setter
    @Getter
    public void setPort(Integer port) {
        this.port = port;
    }

    @Setter
    @Getter
    public String getRootDir() {
        return rootDir;
    }

    @Setter
    @Getter
    public void setRootDir(String rootDir) {
        this.rootDir = rootDir;
    }

    @Setter
    @Getter
    public String getServerName() {
        return serverName;
    }

    @Setter
    @Getter
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Setter
    @Getter
    public String getRegisterServerPath() {
        return registerServerPath;
    }

    @Setter
    @Getter
    public void setRegisterServerPath(String registerServerPath) {
        this.registerServerPath = registerServerPath;
    }


}
