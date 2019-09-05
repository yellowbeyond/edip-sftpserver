package com.cib.edip.client

import com.mastfrog.netty.http.client.ResponseHandler
import io.netty.handler.codec.http.{HttpHeaders, HttpResponseStatus}


abstract class  AbstractRestResponseHandler[T](override val `type`: Class[T])
  extends ResponseHandler[T](`type`) {

  override def receive(status: HttpResponseStatus, obj: T): Unit = {
  }

  override def receive(status: HttpResponseStatus, headers: HttpHeaders, obj: T): Unit = {
  }

  override def receive(obj: T): Unit = {
  }


}

