package com.cib.edip.edipsftpserver.utils

import java.util
import java.util.{Map, UUID}

import com.alibaba.fastjson.serializer.SerializeFilter
import com.alibaba.fastjson.{JSON, TypeReference}

object Helpers {

  def checkNotNull(any: Any): Boolean ={

    if(any!=null) return true

    false

  }

  def checkNotNullAndEmpty(any: Any): Boolean ={

    if(any!=null){

      if(any.isInstanceOf[java.util.Map[_,_]] & !any.asInstanceOf[java.util.Map[_,_]].isEmpty){

        return true

      }
    }

    false

  }

  def castMapToJSON(map: java.util.Map[_,_]): Option[String] = {
    if (checkNotNullAndEmpty(map)) {
    val rs: String = JSON.toJSONString(map, new Array[SerializeFilter](0))


      return Some(rs)

  }

    None
  }

  def castJSONToMap(json: String):java.util.Map[_,_] ={
    if(checkNotNull(json)){
      val jsonObj=JSON.parseObject(json,
      new TypeReference[util.Map[String, Any]](){})

      return jsonObj


    }
    None.get
  }


  def genUUID(): String ={
    UUID.randomUUID.toString
  }

}
