[TOC]

#  edip-sftpserver

项目包含以下组件

- edip-sftpsever-core:基本核心组件和工具组件。
- edip-sftpserver-sftpd：sftp协议服务端组件，可alone方式独立运行，基于[Mina-sshd](https://github.com/apache/mina-sshd)实现SFTP核心功能。
- edip-rest-client：基于Rest的http客户端组件，采用netty构建底层http请求-响应处理机制。
- edip-rest-server ：基于spring boot的rest服务端组件，采用[Dorado](https://github.com/javagossip/dorado)作为rest的服务组件，采用netty构建底层http请求-响应处理机制。



基于Apache SSH的功能加强版SFTP服务端和客户端，支持restful的Agent+Instance模式，也支持单一进程的single模式。所包含组件：

- [Spring Boot](https://github.com/timboudreau/netty-http-client)

- [Netty-http-client](https://github.com/timboudreau/netty-http-client)

- [Dorado](https://github.com/javagossip/dorado)

- [Mina-sshd](https://github.com/apache/mina-sshd)



## sftpd


