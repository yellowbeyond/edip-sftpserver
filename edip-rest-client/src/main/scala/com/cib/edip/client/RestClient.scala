package com.cib.edip.client



import com.mastfrog.netty.http.client.{HttpClient, ResponseFuture, ResponseHandler, State}
import com.mastfrog.util.thread.Receiver
import java.util.concurrent.TimeUnit
import com.mastfrog.url.URL


object RestClient {

  def doGet(url:String,c:Receiver[State[_]]): Boolean = {


    val client: HttpClient = HttpClient.builder.followRedirects.build

    if(client.!=(None)){
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

    if(client!=(None)){

      val urlObj:URL=URL.parse(url)

      if(urlObj!=None){

        if(receiver != None){
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



  def doPost(url:String,restResponseHandler: ResponseHandler[Map[String,_]]): Boolean = {


    val client: HttpClient = HttpClient.builder.followRedirects.build

    if(client != None){
      client.get().setURL(url).execute(restResponseHandler)
      return true
    }else{
      return false
    }
  }

}
