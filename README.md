# drm
## drm动态资源管理


drm设计和说明文档地址：https://www.jianshu.com/p/d6cd29ab54a7


## 基于Zookeeper的DRM框架使用说明
### 1.	背景介绍
随着系统复杂性增加和灵活化性，配置化的需求，需要能够动态改变程序运行轨迹，在不同的场景下程序运行不同轨迹，说直白点就是能够动态改变程序中对象对应的属性值，根据推送的不同属性值来路由不同的处理逻辑，使的系统能够灵活路由。
### 2.	框架设计说明
#### 2.1 框架需要解决的问题
在设计drm框架的时候，需要考虑以下几个问题：
* （1）	持久化问题：就是在Client重启的时候，应该如何保证Client能够重新接收到属于持久化标记的推送信息？去服务器端拉取？还是其他？
* （2）	特定机器和全部机器推送的问题：如何实现对单台或者多台单独推送的问题？如何实现对所有的机器广播的问题？
* （3）	推送操作日志记录问题：记录推送的历史
* （4）	定时推送的问题
* （5）	简化Client实现：使得使用方方便的接入使用
#### 2.2 总体框架
 
Drm整体设计框架是利用Zookeeper作为配置中心的思想，通过Zookeeper提供的watch机制来实现Client对推送信息的监听，通过动态创建ZK目录层次结构来实现动态配置信息的推送，对应的目录结构设计如下：
/CTFIN/DRM/系统名/类名/属性名/
/CTFIN/DRM/系统名/类名/属性名/persist   (本级节点是全局持久化的节点)
/CTFIN/DRM/系统名/类名/属性名/clientip        (本级节点是临时不持久化的节点，当客户端机器上线时创建下线时删除)

* 1.客户端上线时先扫描所有标注有@DrmFieldResource的Field
* 2.如果persist级节点不存在则持久化创建一个节点并将当前值设置到节点
* 3.如果persist级节点存在则取节点值设置到Field上
* 4.如果clientip级节点不存在则创建一个临时节点并将值设置为persist级节点上的值
* 5.如果clientip级节点存在则将值设置为persist级节点上的值（理论上说不会出现，因为机器下线后节点删除了）

优化：
 
管理后台通过推送（创建对应的ZK node节点）将要推送到Client的信息推送的配置中心ZK，即是在Zk上创建对应的目录结构，Client通过监听ZK对应节点的Child和Data 变更信息来实现Client对应的配置信息动态修改，为了进一步简化客户端的操作，定义了一套注解（DrmClassResource，DrmFieldResource）用于标记哪个类对应的哪个属性需要通过drm动态推送。
##### 2.2.1 管理推送端
对应的主要类图如下：
 
优化：
 
其中接口DrmConfRequestParamPersist是需要使用方自己实现对应的推送日志持久化操作的。
#### 2.2.2 DRM client客户端
客户端对应的主要类图如下：
 
类DrmResourceParserFactory是整个Drm Client的核心，其主要的功能如下：
* （1）对应的init方法中，其会遍历整个Spring Context找到有注解@DrmClassResource的类，并对这样的类进行校验，校验点有是否有属性有@DrmFieldResource注解和对应的类是否实现了DrmResourceManager接口（不进行判断了，可以实现这个接口也可以不实现）。
* （2）根据Drm推送的信息ClassName + fieldname 找到对应的实体Bean，在使用反射将对应的drmValue设置到被@DrmFieldResource之前，会调用对应实体bean的beforeUpdate方法，然后再进行反射设置值，值设置完成之后还会调用实体bean的afterUpdate方法。
     类ZkChangeMonitor是Client启动的时候自动注册和加载对应的perisist对应的内容和注册对应的监听器。
### 3.	使用说明
#### 3.1 DRM admin使用
步骤：
* （1）增加maven依赖
```xml
<dependency>
  <groupId>com.ctfin.framework</groupId>
  <artifactId>drm</artifactId>
  <version>1.0.0</version>
</dependency>
```

* （2）实现接口DrmConfRequestParamPersist完成对应的推送日志记录操作
* （3）在配置文件中增加zkclient地址配置：
如properties文件中增加zk.server.addr=192.168.1.202:2181
如yaml文件中增加zk.server.addr: 192.168.1.202:2181
* （4）调用DrmService对应的push方法即可
* （5）增加Component 扫描路径：com.ctfin.framework.drm.server
#### 3.2 DRM Client使用
步骤：
* （1）增加maven依赖
```xml
<dependency>
			<groupId>com.ctfin.framework</groupId>
			<artifactId>drm</artifactId>
			<version>1.0.0</version>
</dependency>
```

* （2）在配置文件中增加如下属性：
```properties
applicationName=应用程序的名称
zk.server.addr=zk对应的地址和端口

```


* （3）注解需要推送的类和属性（注意注解和需要实现的接口DrmResourceManager—）接口可以实现也可以不实现，不做强制要求）
 
* （4）增加Component 扫描的路径：com.ctfin.framework.drm.common
 

