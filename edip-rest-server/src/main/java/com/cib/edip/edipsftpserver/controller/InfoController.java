package com.cib.edip.edipsftpserver.controller;


import ai.houyi.dorado.rest.annotation.Controller;
import ai.houyi.dorado.rest.annotation.GET;
import ai.houyi.dorado.rest.annotation.Path;

import java.util.HashMap;
import java.util.Map;



@Controller
@Path(value="/info")
public class InfoController {

    @GET
    public Map getInfo() {
       Map hmp=new HashMap<String,Object>();
       hmp.put("Server_name","edip_sftp_center");

       return hmp;
    }


}
