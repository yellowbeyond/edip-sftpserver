package com.cib.edip.edipsftpserver.configs

import java.io.{FileInputStream, FileNotFoundException}
import java.net.URL

import com.cib.edip.edipsftpserver.utils.Helpers
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

object ConfigParseYAML{

  def loadConfig[T](fileName:String,_class:Class[T]):Option[T]= {

    if (Helpers.checkNotNull(fileName)) {

      val yaml = new Yaml(new Constructor(_class))
      val url: URL = classOf[Yaml].getClassLoader.getResource(fileName)
      if (url != null) {
        //val _class=classOf[T]
        val obj:T = yaml.load(new FileInputStream(url.getFile))
        if(Helpers.checkNotNull(obj)) return Some(obj)

      }else{
        throw new FileNotFoundException("can not load yaml config file:"+fileName)
      }

    }

    None
  }

  //ConfigParseYAML.loadConfig[ClassFormatError]("")



}
