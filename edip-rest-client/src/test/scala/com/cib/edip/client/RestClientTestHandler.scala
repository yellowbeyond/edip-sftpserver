package com.cib.edip.client

import io.netty.handler.codec.http.{HttpHeaders, HttpResponseStatus}


class RestClientTestHandler[T](override val `type`: Class[T]) extends AbstractRestResponseHandler[T](`type`) {

  //private val contentMarshallers: NettyContentMarshallers = new NettyContentMarshallers().withStrings()

  private val sb:StringBuffer=new StringBuffer()


  override def receive(status: HttpResponseStatus, headers: HttpHeaders, obj: T): Unit = {

    if(obj!=None&obj.isInstanceOf[T]){
      sb.append(obj.asInstanceOf[T].toString)
    }

    println(sb)
  }

}

object RestClientTestHandler {

  private val restClientTestHandler: RestClientTestHandler[String]= new RestClientTestHandler[String](classOf[String])

  def main(args:Array[String]):Unit={
    try{
      RestClient.doGet[String]("http://www.baidu.com",restClientTestHandler)

    }catch {
      case e:Exception=>println(e)
    }

  }

}

