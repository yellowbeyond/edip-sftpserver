package com.cib.edip.client




import java.util

import com.mastfrog.netty.http.client.{HttpClient, ResponseFuture, ResponseHandler, State}
import com.mastfrog.util.thread.Receiver
import com.google.common.net.MediaType
import java.util.concurrent.TimeUnit

import com.cib.edip.edipsftpserver.utils.Helpers
import com.mastfrog.url.URL
import org.slf4j.LoggerFactory


class RestClient{



}


object RestClient {

  private val LOG = LoggerFactory.getLogger(classOf[RestClient])

  def doGet(url:String,c:Receiver[State[_]]): Boolean = {


    val client: HttpClient = HttpClient.builder.followRedirects.build

    if(client!=(null)){
      val rf: ResponseFuture=client.get().setURL(url).execute()

      rf.await(5, TimeUnit.SECONDS).throwIfError

      return true
    }else{
      return false

    }
  }

  /**
   * invoek by ResponseFuture to receive response data from server and abstract the content to bytes[].
   *
   * @param url           the state from the server response
   * @param responseHandler           the state from the server response
   *
   * @return Boolean
   *
   */
  def doGet[A](url:String,responseHandler:ResponseHandler[A]): Unit = {


    return doGet(url,responseHandler,None.get)
  }

  /**
   * do handle the get the http request.
   *
   * @param url                       the url the http server to access
   * @param responseHandler           the response handler to handle the response
   * @param receiver                  the receiver register for listen the http response event
   *
   * @return Boolean                  return if the get request sent OK
   *
   */
  def doGet[A](url:String,responseHandler:ResponseHandler[A],receiver:Receiver[State[_]]): Unit  = {


    val client: HttpClient = HttpClient.builder.followRedirects.build

    if(client!=(null)){

      val urlObj:URL=URL.parse(url)

      if(urlObj!=null){

        if(receiver != null){
          client.get().setURL(urlObj).execute(responseHandler).onAnyEvent(receiver)
        }else{
          client.get().setURL(urlObj).execute(responseHandler)
        }


      }else{
        throw new IllegalArgumentException("parse url String failure")
      }

    }else{
      throw new NullPointerException("arg url can't be null")
    }
  }



  /**
   * do handle the get the http request.
   *
   * @param url                       the url the http server to access
   * @param responseHandler           the response handler to handle the response
   * @param receiver                  the receiver register for listen the http response event
   *
   * @return Boolean                  return if the get request sent OK
   *
   */
  def doPost[A](url:String,body:util.Map[String,_],responseHandler:ResponseHandler[A],receiver:Receiver[State[_]]): Unit  = {


    val client: HttpClient = HttpClient.builder.followRedirects.build

    if(client!=null){


      val urlObj:URL=URL.parse(url)


      if(Helpers.checkNotNull(urlObj)) {



        if (Helpers.checkNotNullAndEmpty(body)) {
        //val body: util.HashMap[String,Any]= new util.HashMap[String,Any]



        if (Helpers.checkNotNull(receiver)) {
          client.post().setURL(urlObj).setBody(body, MediaType.JSON_UTF_8).execute(responseHandler).onAnyEvent(receiver)
        } else {
          client.post().setURL(urlObj).setBody(body, MediaType.JSON_UTF_8).execute(responseHandler)
        }
      }else{
          throw new NullPointerException("arg body is nul or empty")
        }

      }else{
        throw new IllegalArgumentException("parse url String failure")
      }

    }else{
      throw new NullPointerException("arg url can't be null")
    }
  }

}
