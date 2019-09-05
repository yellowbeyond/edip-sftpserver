package com.cib.edip.edipsftpserver;

import ai.houyi.dorado.springboot.DoradoSpringBootApplication;
import com.cib.edip.edipsftpserver.fork.ForkSftpServer;
import com.cib.edip.edipsftpserver.fork.ProcessInfo;
import org.springframework.boot.SpringApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

@DoradoSpringBootApplication
public class EdipSftpserverApplication {

	private static final Logger LOG = LoggerFactory.getLogger(EdipSftpserverApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(EdipSftpserverApplication.class, args);

		ForkSftpServer fss=new ForkSftpServer();
		HashMap<String,String> _args=new HashMap<String,String>();
		_args.put("-p","2009");
		_args.put("-d","/tmp");
		_args.put("-n","server-1");
		String uuid=UUID.randomUUID().toString();
		_args.put("-k",uuid);

		HashMap<String,String> env=new HashMap<String,String>();

		try{
			ProcessInfo pi=fss.startProcess(Class.forName("com.cib.edip.edipsftpserver.sftpd.SftpServer"),_args,env,"./target/*:./target/lib/*","server_test");
			/*if(){

			}
*/


		}catch (ClassNotFoundException e){
			e.printStackTrace();

		}
	}





}
