package com.cib.edip.edipsftpserver;

import ai.houyi.dorado.springboot.DoradoSpringBootApplication;
import com.cib.edip.edipsftpserver.fork.ForkSftpServer;
import com.cib.edip.edipsftpserver.fork.ProcessInfo;
import com.cib.edip.edipsftpserver.sftpd.SftpServer;
import com.cib.edip.edipsftpserver.config.Config;

import com.cib.edip.edipsftpserver.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

@DoradoSpringBootApplication
public class EdipSftpserverApplication {

    private static final Logger LOG = LoggerFactory.getLogger(EdipSftpserverApplication.class);

    public static String SFTPD_SERVER_HOME_ENV="EDIP_SFTP_SERVER_HOME";
    public static String SFTPD_HOME_ENV="EDIP_SFTPD_HOME";

    public static void main(String[] args) {
        SpringApplication.run(EdipSftpserverApplication.class, args);
        Config config=SftpServerContext.getConfig();
        ForkSftpServer fss = new ForkSftpServer();
        HashMap<String, String> _args = new HashMap<String, String>();
        _args.put("-p", Helpers.checkNotNull(config.getPort())?config.getPort().toString():"2009");
        _args.put("-d", Helpers.checkNotNull(config.getRootDir())?config.getRootDir():"/tmp");
        _args.put("-n", Helpers.checkNotNull(config.getServerName())?config.getServerName():"server-1");
        String uuid = UUID.randomUUID().toString();
        _args.put("-k", uuid);
        _args.put("-r",null);
        _args.put("-url",Helpers.checkNotNull(config.getRegisterServerUrl())?config.getRegisterServerUrl():"http://localhost:8081/info/register-server");
        _args.put("--HostKey="+(Helpers.checkNotNull(config.getHostKey())?config.getHostKey():"/keys/edip-sftpserver-host-rsa-key"),null);
        //_args.put("-Dlog4j.configuration=file:src/main/resources/log4j.properties"," ");



        //LOG.debug("======================"+System.getenv("EDIP_SFTP_SERVER_HOME"));


        HashMap<String, String> env = new HashMap<String, String>();

        if(Helpers.checkNotNull((System.getenv(EdipSftpserverApplication.SFTPD_SERVER_HOME_ENV)))) {

            env.put(EdipSftpserverApplication.SFTPD_HOME_ENV,System.getenv(EdipSftpserverApplication.SFTPD_SERVER_HOME_ENV) );
        }

        try {
            ProcessInfo pi = fss.startProcess(Class.forName(Helpers.checkNotNull(config.getMainClass())?config.getMainClass():"com.cib.edip.edipsftpserver.sftpd.SftpServer"), _args, env,
                    Helpers.checkNotNull(config.getClassPath())?System.getenv(EdipSftpserverApplication.SFTPD_SERVER_HOME_ENV)+ java.io.File.separator+config.getClassPath():"./target/*:./target/lib/*", "server_test");
			/*if(){

			}

*/
			LOG.debug(pi.getReturnInfo().toString());
            SftpServerContext.getInstance().registerServer(uuid + "-" +pi.getReturnInfo().get(SftpServer.ReturnInfoConstant.SFTPD_SERVER_PID).toString(), pi);
            //System.out.println("ddddd");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
    }


}
