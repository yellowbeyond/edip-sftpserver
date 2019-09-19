package com.cib.edip.edipsftpserver.fork;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cib.edip.edipsftpserver.sftpd.SftpServer;
import com.cib.edip.edipsftpserver.utils.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForkSftpServer implements JavaFork {

    private static final Logger LOG = LoggerFactory.getLogger(ForkSftpServer.class);


    @Override
    public ProcessInfo startProcess(Class mainClass, Map<String,String> args, Map<String,String> env,String cp,String serverName) {
        ProcessBuilder pb = null;
        Process p = null;
        String line, errline = null;
        BufferedReader stdout, errout = null;
        ProcessInfo pi;

        try {

            pb = new ProcessBuilder("java");
            //pb.directory(new File("/vagrant/workspace/project/test_kfk/edip-sftpserver/edip-sftpserver-sftpd/target"));  //切换到工作目录
            //pb.command(new String[]{"java", "-jar", "edip-sftpserver-sftpd-0.0.1-SNAPSHOT.jar", "-p", "2003", "-d", "/tmp"});
            if(cp!=null){
                pb.command().add("-cp");
                pb.command().add(cp);
            }

            LOG.debug("fork classpath:"+cp);

            pb.command().add("-Dlog4j.configuration="+(Helpers.checkNotNull(System.getProperty("log4j.configuration"))?
                    System.getProperty("log4j.configuration"):"file:///vagrant/workspace/project/test_kfk/edip-sftpserver/edip-rest-server/src/main/resources/log4j-sftpd.properties"));
            pb.command().add(mainClass.getName());

           // pb.command().add("-Dlog4j.configuration=file://src/main/resources/log4j.properties");

            this.setArgs(pb,args);
            this.setEnv(pb,env);
            //pb.command().add("2///>///&1");

            p = pb.start();


            pi=new ProcessInfo(p);


           // pb.redirectError();

            stdout = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            errout = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            while((line = stdout.readLine())!=null) {
                if (line != null) {
                    LOG.info(line);
                    Matcher matcher=Pattern.compile("\\[SftpServer\\sReturn\\sInfo\\]\\:(\\{.*\\})").matcher(line);

                    if(matcher.find()){

                        pi.setReturnInfo(JSON.parseObject(matcher.group(1), new TypeReference<Map<String, Object>>(){}));

                        break;

                    }
                }
            }

           if(errout.ready()) {
                errline = errout.readLine();
                if(errline!=null){
                    LOG.error(errline);
                }
            }

            //while((line = stdout.readLine())!=null){
           // while(stdout.ready()) {

            //}
            /*else{
                break;
            }


             Thread.sleep(1);


            }*/
            stdout.close();
            errout.close();

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    @Override
    public void killProcess(Process p) {
        if(p!=null){
            p.destroy();
        }

    }

    private void setArgs(ProcessBuilder pb, Map<String,String> args){
        args.forEach((k,v)->{
            pb.command().add(k);

            if(Helpers.checkNotNull(v)) {
                pb.command().add(v);
            }

            LOG.debug("========================"+k+"<==========>"+v);
        });


    }

    private void setEnv(ProcessBuilder pb, Map<String,String> env){
        Map<String,String> env_or =pb.environment();

        env_or.putAll(env);


    }
}
