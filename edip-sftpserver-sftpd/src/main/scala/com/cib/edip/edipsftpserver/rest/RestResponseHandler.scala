package com.cib.edip.edipsftpserver.rest

import java.util

import com.cib.edip.client.AbstractRestResponseHandler
import com.cib.edip.edipsftpserver.fork.ForkSftpServer
import org.slf4j.{Logger, LoggerFactory}

class RestResponseHandler[T](override val `type`: Class[T]) extends AbstractRestResponseHandler[T](`type`) {

  protected val LOG = LoggerFactory.getLogger(classOf[RestResponseHandler[T]])
  override def handleByMap[A](obj: util.Map[A, _]): Unit = {

  }
}
