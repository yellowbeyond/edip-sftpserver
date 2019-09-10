package com.cib.edip.client

import com.cib.edip.edipsftpserver.utils.Helpers
import com.mastfrog.netty.http.client.ResponseHandler
import io.netty.handler.codec.http.{HttpHeaders, HttpResponseStatus}
import org.slf4j.LoggerFactory


abstract class  AbstractRestResponseHandler[T](override val `type`: Class[T])
  extends ResponseHandler[T](`type`) {

  private val LOG = LoggerFactory.getLogger(classOf[AbstractRestResponseHandler[T]])

  override def receive(status: HttpResponseStatus, obj: T): Unit = {
  }

  override def receive(status: HttpResponseStatus, headers: HttpHeaders, obj: T): Unit = {
  }

  override def receive(obj: T): Unit = {


    if(Helpers.checkNotNull(obj) && obj.isInstanceOf[T]){


      LOG.debug(obj.getClass.toString)
      if(isKeyTypeMap[String](obj)){


        handleByMap[String](obj.asInstanceOf[java.util.Map[String,Any]])

      }

    }
  }

  def isKeyTypeMap[A](obj: T): Boolean ={
    if(obj.isInstanceOf[java.util.Map[A,_]]) return true
    false
  }

  def handleByMap[A]( obj: java.util.Map[A,_]): Unit


}

