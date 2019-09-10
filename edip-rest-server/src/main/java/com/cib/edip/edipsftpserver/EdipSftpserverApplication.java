package com.cib.edip.edipsftpserver;

import ai.houyi.dorado.springboot.DoradoSpringBootApplication;
import com.cib.edip.edipsftpserver.fork.ForkSftpServer;
import com.cib.edip.edipsftpserver.fork.ProcessInfo;
import com.cib.edip.edipsftpserver.sftpd.SftpServer;
import org.springframework.boot.SpringApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DoradoSpringBootApplication
public class EdipSftpserverApplication {

    private static final Logger LOG = LoggerFactory.getLogger(EdipSftpserverApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EdipSftpserverApplication.class, args);

        ForkSftpServer fss = new ForkSftpServer();
        HashMap<String, String> _args = new HashMap<String, String>();
        _args.put("-p", "2009");
        _args.put("-d", "/tmp");
        _args.put("-n", "server-1");
        String uuid = UUID.randomUUID().toString();
        _args.put("-k", uuid);
        _args.put("-r","http://localhost:8081/info/register-server");

        //_args.put("-Dlog4j.configuration=file:src/main/resources/log4j.properties"," ");


        HashMap<String, String> env = new HashMap<String, String>();

        try {
            ProcessInfo pi = fss.startProcess(Class.forName("com.cib.edip.edipsftpserver.sftpd.SftpServer"), _args, env, "./target/*:./target/lib/*", "server_test");
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
