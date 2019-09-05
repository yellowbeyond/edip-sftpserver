package com.cib.edip.client
import com.mastfrog.marshallers.netty.NettyContentMarshallers
import com.mastfrog.netty.http.client.State
import io.netty.buffer.ByteBuf
import io.netty.util.CharsetUtil

class RestClientTest[T] extends AbstractReceiver[T]  {

  private val contentMarshallers: NettyContentMarshallers = new NettyContentMarshallers().withStrings()


  override def getMarshaller(): NettyContentMarshallers = {

    return this.contentMarshallers

  }

  override def doReceive(content: ByteBuf): Unit = {

    //val m:Map[String,_]=this.string2Map(content,this.getMarshaller())

    //println(m.toString())

    val str:String = this.byte2String(content,this.getMarshaller(),CharsetUtil.UTF_8)

    return str

  }

}

object RestClientTest {

  private val restClientTest: RestClientTest[State[_]]= new RestClientTest[State[_]]

  def main(args:Array[String]):Unit={
    RestClient.doGet("http://www.baidu.com",restClientTest)

  }

  def getDefaultState():RestClientTest[State[_]] = {

    if (restClientTest != None) {
      return restClientTest
    }else{

      return None.get
    }

  }

}

