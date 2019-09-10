package com.cib.edip.client

import com.google.common.net.MediaType
import java.nio.charset.Charset

import com.mastfrog.marshallers.netty.NettyContentMarshallers
import com.mastfrog.netty.http.client.State
import com.mastfrog.util.thread.Receiver
import io.netty.buffer.ByteBuf
import io.netty.handler.codec.http.HttpContent
import io.netty.util.CharsetUtil


/**
 * The abstract Receiver instance for user to invoke.
 */
abstract class AbstractReceiver[T] extends Receiver[T]{

  //private[client] var marshallers = null


  /**
   * invoek by ResponseFuture to receive response data from server and abstract the content to bytes[].
   *
   * @param obj                 the state from the server response
   *
   */

  @Override
  def receive(obj:T ) {

    if (obj.isInstanceOf[T]) {

      val sta: State[Any] = obj.asInstanceOf[State[Any]]

      if (sta.get().isInstanceOf[HttpContent]) {

        //this.string2Map(sta.get().asInstanceOf[HttpContent].content(),this.getMarshaller)}
        //val bb = state.get().asInstanceOf[HttpContent].content()

        // val bf = new Array[Byte](bb.readableBytes())

        //bb.getBytes(bb.readerIndex(),bf)
        //this.doReceive(bf)
        //this.receiveString(new String(bf,CharsetUtil.UTF_8))
        //String s = new String(bytes, CharsetUtil.UTF_8);

        this.doReceive(sta.get().asInstanceOf[HttpContent].content())


      }
    }
  }


  /**
   * offer the handler method for sub class to use
   * @param content                   the content bytes[] from the server response
   *
   */
  def doReceive(content: ByteBuf): Unit

  //abstract def receiveMap(string:String): Unit

  /**
   * transform the bytes[] to String
   *
   * @param bf                   the content bytes[] from the server response
   * @param charset                   the Charset which bytes[] transform to
   * @return The String create from {@content contetn } by { @charset charset}
   */
  def byte2String(bf:ByteBuf,marshaller:NettyContentMarshallers,charset:Charset):String = {
    if(charset!= null & bf != null){
      return marshaller.read(new String().getClass,null)
    }

    ""//else new String(content,CharsetUtil.UTF_8)
  }


  /**
   * transform the bytes[] to Map
   *
   *
   * @return The String create from {@content contetn } by { @charset charset}
   */
  def byte2Map(bf:ByteBuf,marshaller:NettyContentMarshallers): Map[String,Any] = {
    if (bf != null) {

        return marshaller.read(Map.getClass,bf,null).asInstanceOf[Map[String,Any]]

    }

    return null

   }


  def getMarshaller():NettyContentMarshallers



}
