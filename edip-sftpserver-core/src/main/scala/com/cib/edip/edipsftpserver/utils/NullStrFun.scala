package com.cib.edip.edipsftpserver.utils

class NullStringFun extends Function0[String] () {

  override def apply: String = null

}

object NullStringFun {

  val instance:NullStringFun=new NullStringFun

  def get(): NullStringFun ={
    instance

  }
}
