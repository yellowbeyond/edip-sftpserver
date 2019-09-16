package com.cib.edip.edipsftpserver;

import com.cib.edip.edipsftpserver.fork.ProcessInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cib.edip.edipsftpserver.config.Config;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SftpServerContext implements ApplicationContextAware {

    private final static SftpServerContext sftpServerContext=new SftpServerContext();
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SftpServerContext.applicationContext == null) {
            SftpServerContext.applicationContext = applicationContext;
        }
        System.out.println("---------------------------------------------------------------------");

        System.out.println("---------------------------------------------------------------------");

        System.out.println("---------------me.shijunjie.util.SpringUtil------------------------------------------------------");

        System.out.println("========ApplicationContext配置成功,在普通类可以通过调用SpringUtils.getAppContext()获取applicationContext对象,applicationContext="+SftpServerContext.applicationContext+"========");

        System.out.println("---------------------------------------------------------------------");
    }

    @Autowired
    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public static SftpServerContext getInstance(){
        return SftpServerContext.sftpServerContext;

    }

    private Map<String,ProcessInfo> registerServers=new HashMap<String,ProcessInfo>();

    public Map<String, ProcessInfo> getRegisterServers() {
        return registerServers;
    }

    public void setRegisterServers(Map<String, ProcessInfo> registerServers) {
        this.registerServers = registerServers;
    }

    public void registerServer(String uuid, ProcessInfo pi){
        if(!isServerRegisted(uuid)){
            this.registerServers.put(uuid,pi);
        };

    }

    public ProcessInfo getRegisterServer(String uuid){
        if(this.isServerRegisted(uuid)){
            return (ProcessInfo) this.registerServers.get(uuid);
        }

        return null;

    }

    public boolean isServerRegisted(String uuid){
        if(this.registerServers.containsKey(uuid)){
            return true;
        }

        return false;

    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }




}
