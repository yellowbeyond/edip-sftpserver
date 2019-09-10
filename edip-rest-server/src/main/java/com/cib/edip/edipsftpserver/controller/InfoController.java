package com.cib.edip.edipsftpserver.controller;


import ai.houyi.dorado.rest.annotation.*;
import com.cib.edip.edipsftpserver.Info;
import com.cib.edip.edipsftpserver.SftpServerContext;
import com.cib.edip.edipsftpserver.fork.ProcessInfo;
import com.cib.edip.edipsftpserver.sftpd.SftpServer;
import com.cib.edip.edipsftpserver.utils.Helpers;
import com.cib.edip.edipsftpserver.utils.NullStringFun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Function0;

import java.util.HashMap;
import java.util.Map;



@Controller
@Path("/info")
public class InfoController {
    private static final Logger LOG = LoggerFactory.getLogger(InfoController.class);

    @GET
    public Map<String,ProcessInfo> getInfo() {
       //Map hmp=new HashMap<String,Object>();
       //hmp.put("Server_name","edip_sftp_center");



       return SftpServerContext.getInstance().getRegisterServers();
    }

    @POST
    @Path("/register-server")
    public String registerServer(Map<String,Object> map) {

        LOG.debug(map.toString());

        String rsuuid=map.get(SftpServer.ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY).toString()+"-"
                +map.get(SftpServer.ReturnInfoConstant.SFTPD_SERVER_PID).toString();

        LOG.debug("RegisterServers:"+rsuuid+SftpServerContext.getInstance().getRegisterServers().toString());

        if(SftpServerContext.getInstance().isServerRegisted(rsuuid)){
            ProcessInfo pi=SftpServerContext.getInstance().getRegisterServer(rsuuid);

            if(Helpers.checkNotNullAndEmpty(pi.getReturnInfo())){
                //new Integer(pi.getReturnInfo().get(SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS)).intValue();

               if((int)pi.getReturnInfo().get(SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS)== SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS_UNREGISTERED
                  & pi.getReturnInfo().get(SftpServer.ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY).toString()
                       .equals(map.get(SftpServer.ReturnInfoConstant.SFTPD_SERVER_SECURITY_KEY).toString())){
                   pi.getReturnInfo().put(SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS, SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS_REGISTERED);
                   pi.getReturnInfo().put(SftpServer.ReturnInfoConstant.SFTPD_SERVER_TOKEN,Helpers.genUUID());
                   pi.getReturnInfo().put(SftpServer.ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS,SftpServer.ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS_SUCCESS);

                   return Helpers.castMapToJSON(pi.getReturnInfo()).getOrElse(NullStringFun.get());

                   //return pi.getReturnInfo();
              }
            }
        }else{

            Map<String,Integer> rm=new HashMap<String,Integer>();
            rm.put(SftpServer.ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS,SftpServer.ReturnInfoConstant.SERVER_REGISTER_RESPONSE_STATUS_FAILURE);


            return  Helpers.castMapToJSON(rm).getOrElse(NullStringFun.get());
        }

       /* map.put(SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS, SftpServer.ReturnInfoConstant.SFTP_REGISTER_STATUS_REGISTERED);
        map.put(SftpServer.ReturnInfoConstant.SFTPD_SERVER_TOKEN,Helpers.genUUID());

        System.out.println("====================out"+Helpers.castMapToJSON(map).getOrElse(NullStringFun.get()));
        return Helpers.castMapToJSON(map).getOrElse(NullStringFun.get());*/

       return null;
    }


}
