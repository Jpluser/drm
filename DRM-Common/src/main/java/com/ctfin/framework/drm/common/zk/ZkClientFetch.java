/**
 * ZkClientFetch.java author: yujiakui 2017年12月27日 下午2:14:29
 */
package com.ctfin.framework.drm.common.zk;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yujiakui
 *
 * 下午2:14:29
 *
 * zookeeper对应的client获取器
 */
@Component
public class ZkClientFetch implements InitializingBean {

  @Value("${zk.server.addr}")
  private String serverAddrInfo;
  /**
   * zookeeper 客户端
   */
  private ZkClient zkClient;

  /**
   * session 超时
   */
  private final static int SESSION_TIMEOUT = 3000;

  /**
   * 链接 超时
   */
  private final static int CONNECTION_TIMEOUT = 3000;

  /**
   * 创建zk客户端
   */
  public ZkClient createZkClient(String serverAddrInfo) {
    zkClient = new ZkClient(serverAddrInfo, SESSION_TIMEOUT, CONNECTION_TIMEOUT,
        new MyZkSerializer());
    return zkClient;
  }

  /**
   * @return the zkClient
   */
  public ZkClient getZkClient() {
    return zkClient;
  }

  /**
   * @param zkClient the zkClient to set
   */
  public void setZkClient(ZkClient zkClient) {
    this.zkClient = zkClient;
  }

  public String getServerAddrInfo() {
    return serverAddrInfo;
  }

  public void setServerAddrInfo(String serverAddrInfo) {
    this.serverAddrInfo = serverAddrInfo;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    createZkClient(serverAddrInfo);
  }
}
