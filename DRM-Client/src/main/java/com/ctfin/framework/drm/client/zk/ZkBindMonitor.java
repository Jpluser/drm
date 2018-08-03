/**
 * ZkBindMonitor.java author: yujiakui 2018年1月3日 下午4:36:46
 */
package com.ctfin.framework.drm.client.zk;

import com.ctfin.framework.drm.client.DrmResourceParseFactory;
import com.ctfin.framework.drm.common.DrmObject;
import com.ctfin.framework.drm.common.model.FieldModel;
import com.ctfin.framework.drm.common.zk.ZkClientFetch;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.common.model.ZkPathConstants;
import com.ctfin.framework.drm.common.utils.LogUtils;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

/**
 * @author yujiakui
 *
 * 下午4:36:46
 *
 * zk 绑定监控
 */
@Component
public class ZkBindMonitor implements InitializingBean {

  /**
   * logger
   */
  private final static Logger LOGGER = LoggerFactory.getLogger(ZkBindMonitor.class);

  @Autowired
  private ZkClientFetch zkClientFetch;

  @Autowired
  private ZkFieldDataListenerImpl zkFieldDataListenerImpl;

  @Autowired
  private DrmResourceParseFactory drmResourceParseFactory;

  @Value("${applicationName}")
  private String applicationName;

  /*
   * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    // 解析drm注解
    drmResourceParseFactory.parseDrmAnnotation();
    // 绑定监听和启动初始化
    bindMonitorAndStartInit();
  }

  /**
   * 绑定监控和启动初始化
   */
  public void bindMonitorAndStartInit() {
		/*	// 获取spring上下文的应用程序名称，如果不为空则设置对应的应用程序名为spring应用程序名称
			if (StringUtils.isNotEmpty(applicationContext.getApplicationName())) {
				applicationName = applicationContext.getApplicationName();
			}*/
    if (StringUtils.isEmpty(applicationName)) {
      throw new RuntimeException("DRM 要求对应的applicationName不能为空");
    }
    // 处理 ALL和特定 机器对应的路径：如果不存在则创建，如果存在则判断是否是持久化，如果不是则不进行处理
    handleMachinePath();
  }

  /**
   * 处理所有的机器对应的路径：如：/CTFIN/DRM/应用程序名称/all
   */
  private void handleMachinePath() {
    // zkClient客户端
    ZkClient zkClient = zkClientFetch.getZkClient();
    // 获取所有的需要drm推送的属性
    Table<String, String, FieldModel> drmFieldTable = drmResourceParseFactory.getParseResultTable();
    for (Cell<String, String, FieldModel> drmEle : drmFieldTable.cellSet()) {
      handleField(zkClient, drmEle.getValue());
    }
  }

  /**
   * 处理特定机器的字段
   */
  private void handleField(ZkClient zkClient, FieldModel fieldModel) {
    Field field=fieldModel.getField();
    String className = field.getDeclaringClass().getName();
    String fieldName = field.getName();
    String zkIpFieldPath=getZkFieldPath(field);
    String persistPath = zkIpFieldPath + ZkPathConstants.PATH_SEP + ZkPathConstants.PERSIST;
    String localPath = zkIpFieldPath + ZkPathConstants.PATH_SEP + getLocalIp();
    DrmObject drmPersistFieldValue = new DrmObject(className, fieldName);
    if (zkClient.exists(persistPath)) {
      // 持久化路径存在
      drmPersistFieldValue = zkClient.readData(persistPath);
      handleDrmValue(drmPersistFieldValue);
    } else {
      zkClient.createPersistent(persistPath,true);
      drmPersistFieldValue.setDrmValue(fieldModel.getValue());
      try {
        zkClient.writeData(persistPath, drmPersistFieldValue,0);
      }catch (Exception e){
       if(e instanceof  KeeperException.BadVersionException){
         LOGGER.warn("write data but bad version ignore! path:{},value:{}",persistPath,drmPersistFieldValue);
       }else throw e;
      }
    }
    //localPath中存放的是临时的节点
    if (!zkClient.exists(localPath)) {
      zkClient.createEphemeral(localPath, drmPersistFieldValue);
    }else {
      zkClient.writeData(localPath, drmPersistFieldValue);
      LOGGER.warn("ephemeral path data has rewrite! path:{},data:{}",localPath,drmPersistFieldValue);
    }
    zkClient.subscribeDataChanges(persistPath, zkFieldDataListenerImpl);
    zkClient.subscribeDataChanges(localPath, zkFieldDataListenerImpl);
  }


  /**
   * 处理drm推送的值
   */
  private void handleDrmValue(DrmObject drmObject) {
    if (drmObject != null && drmObject.getDrmValue() != null) {
      drmResourceParseFactory.resetFieldValue(drmObject);
    }
  }

  /**
   * 获取zk对应的应用程序地址路径 例如： /CTFIN/DRM/应用程序名称/
   */
  public String getZkApplicationPath() {
    StringBuilder stringBuilder = new StringBuilder(ZkPathConstants.ROOT_PATH);
    stringBuilder.append(ZkPathConstants.PATH_SEP);
    stringBuilder.append(applicationName);
    stringBuilder.append(ZkPathConstants.PATH_SEP);
    return stringBuilder.toString();
  }

  public String getZkFieldPath(Field field){
    String zkApplicationPath = getZkApplicationPath();
    String className = field.getDeclaringClass().getName();
    String fieldName = field.getName();
    String zkIpFieldPath = zkApplicationPath + className + ZkPathConstants.PATH_SEP + fieldName;
    return zkIpFieldPath.replace(".", ZkPathConstants.PATH_SEP);
  }

  /**
   * 获取本机对应的ip地址
   */
  public String getLocalIp() {
    // 获取当前机器的ip地址：
    String ipAddr = "ERROR_IP";
    try {
      InetAddress localAddress = InetAddress.getLocalHost();
      ipAddr = localAddress.getHostAddress();
    } catch (UnknownHostException e) {
      LogUtils.error(LOGGER, "获取当前机器对应的ip地址失败errorMsg={0}", e.getMessage());
    }
    return ipAddr;
  }

}
