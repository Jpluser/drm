/**
 * DrmServiceImpl.java author: yujiakui 2017年12月27日 上午9:21:37
 */
package com.ctfin.framework.drm.admin.service;

import com.alibaba.fastjson.JSON;
import com.ctfin.framework.drm.common.DrmRequestParam;
import com.ctfin.framework.drm.common.model.ZkPathConstants;
import com.ctfin.framework.drm.common.zk.ZkClientFetch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ctfin.framework.drm.admin.DrmConfRequestParamPersist;
import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 * 上午9:21:37
 *
 * 推送请求服务
 */
@Component
public class DrmServiceImpl implements DrmService {

  private static final Logger logger = LoggerFactory.getLogger(DrmServiceImpl.class);

  @Autowired
  private ZkClientFetch zkClientFetch;

  @Autowired
  private DrmConfRequestParamPersist drmConfRequestParamPersist;


  @Override
  public boolean push(DrmConfRequestParam drmConfRequestParam) {
    List<String> reqUrls = drmConfRequestParam.getDrmRequestUrl();
    ZkClient zkClient = zkClientFetch.getZkClient();
    if (!CollectionUtils.isEmpty(reqUrls)) {
      // 处理特定机器的推送，要注意对于推送all的情况，其会同时更新对应的机器field的值
      handleReqUrlDrm(drmConfRequestParam, reqUrls, zkClient);
    } else {
      // 如果所有的都推送，则处理所有都推送的场景，对于推送是all的场景，还要覆盖推送所有的特定机器的
      handleAllMachineDrm(drmConfRequestParam, zkClient);
    }

    // 保存推送的日志：记录对应的推送流水
    // 记录推送的日志--记录推送信息
    drmConfRequestParamPersist.save(drmConfRequestParam);

    return true;
  }

  @Override
  public Object getClientDrmValue(DrmConfRequestParam drmConfRequestParam) {
    if (CollectionUtils.isEmpty(drmConfRequestParam.getDrmRequestUrl())
        || drmConfRequestParam.getDrmRequestUrl().size() != 1) {
      logger.error("getClientDrmValue parameter error!:{}", JSON.toJSONString(drmConfRequestParam));
      return null;
    }
    ZkClient zkClient = zkClientFetch.getZkClient();
    handleReqUrlDrm(drmConfRequestParam, drmConfRequestParam.getDrmRequestUrl(),
        zkClient);
    //TODO 有可能需要等待
    DrmRequestParam data = zkClient.readData(
        drmConfRequestParam.getDataPath() + ZkPathConstants.PATH_SEP + drmConfRequestParam
            .getDrmRequestUrl().get(0));
    return data == null ? null : data.getDrmValue();
  }

  /**
   * 处理reqUrl对应的drm推送，如/CTFIN/DRM/特定机器/类名/属性名/persist或者nopersist
   */
  private void handleReqUrlDrm(DrmConfRequestParam drmConfRequestParam, List<String> reqUrls,
      ZkClient zkClient) {
    for (String reqUrl : reqUrls) {
      String reqUrlFieldpath = StringUtils.join(
          Lists.newArrayList(ZkPathConstants.ROOT_PATH,
              drmConfRequestParam.getApplicationName(),
              drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName(), reqUrl),
          ZkPathConstants.PATH_SEP);
      reqUrlFieldpath = reqUrlFieldpath.replace(".", ZkPathConstants.PATH_SEP);
      DrmRequestParam drmRequestParam = new DrmRequestParam(drmConfRequestParam.getClassName(),
          drmConfRequestParam.getFieldName(), drmConfRequestParam.getDrmValue());
      drmRequestParam.setOperation(drmConfRequestParam.getOperation());
      if (zkClient.exists(reqUrlFieldpath)) {
        zkClient.writeData(reqUrlFieldpath, drmRequestParam);
      } else {
        zkClient.createEphemeral(reqUrlFieldpath, drmRequestParam);
      }
    }
  }

  /**
   * 处理所有机器的drm推送：生成对应的路径：/CTFIN/DRM/应用程序名称/ALL/类名/属性名/persist或者nopersist
   */
  private void handleAllMachineDrm(DrmConfRequestParam drmConfRequestParam, ZkClient zkClient) {
    String zkFieldPath = StringUtils.join(
        Lists.newArrayList(ZkPathConstants.ROOT_PATH,
            drmConfRequestParam.getApplicationName(),
            drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName()),
        ZkPathConstants.PATH_SEP);
    zkFieldPath = zkFieldPath.replace(".", ZkPathConstants.PATH_SEP);
    List<String> children = zkClient.getChildren(zkFieldPath);
    for (String child : children) {
      String path = zkFieldPath + ZkPathConstants.PATH_SEP + child;
      if (ZkPathConstants.PERSIST.equalsIgnoreCase(child)) {
        if (!zkClient.exists(path)) {
          zkClient.createPersistent(path, true);
        }
      } else {
        if (!zkClient.exists(path)) {
          zkClient.createEphemeral(path);
        }
      }
      DrmRequestParam drmRequestParam = new DrmRequestParam(drmConfRequestParam.getClassName(),
          drmConfRequestParam.getFieldName(), drmConfRequestParam.getDrmValue());
      zkClient.writeData(path, drmRequestParam);
    }
  }

  /* (non-Javadoc)
   * @see com.ctfin.framework.drm.admin.service.DrmService#delete(com.ctfin.framework.drm.admin.model.DrmConfRequestParam)
   */
  @Override
  public boolean delete(DrmConfRequestParam drmConfRequestParam) {
    try {
      ZkClient zkClient = zkClientFetch.getZkClient();
      String zkFieldPath = StringUtils.join(
          Lists.newArrayList(ZkPathConstants.ROOT_PATH,
              drmConfRequestParam.getApplicationName(),
              drmConfRequestParam.getClassName(), drmConfRequestParam.getFieldName()),
          ZkPathConstants.PATH_SEP);
      zkFieldPath = zkFieldPath.replace(".", ZkPathConstants.PATH_SEP);
      if (CollectionUtils.isEmpty(drmConfRequestParam.getDrmRequestUrl())) {
        zkClient.deleteRecursive(zkFieldPath);
      } else {
        for (String client : drmConfRequestParam.getDrmRequestUrl()) {
          zkClient.deleteRecursive(zkFieldPath + ZkPathConstants.PATH_SEP + client);
        }
      }
    } catch (Throwable e) {
      logger.error("delete drmConfRequestParam:{} error!", drmConfRequestParam);
      return false;
    }
    return true;
  }


  /* (non-Javadoc)
   * @see com.ctfin.framework.drm.admin.service.DrmService#listAllDrmInfo()
   */
  @Override
  public List<DrmRequestParam> listAllDrmInfo(String app) {
    List<DrmRequestParam> drmRequestParams = Lists.newArrayList();
    deepListDrmInfo(drmRequestParams, ZkPathConstants.ROOT_PATH + ZkPathConstants.PATH_SEP + app);
    Collections.sort(drmRequestParams, new Comparator<DrmRequestParam>() {
      @Override
      public int compare(DrmRequestParam o1, DrmRequestParam o2) {
        return (o1.getClassName() + o1.getFieldName())
            .compareTo(o2.getClassName() + o2.getFieldName());
      }
    });
    return drmRequestParams;
  }

  @Override
  public List<String> listAllApps() {
    ZkClient zkClient = zkClientFetch.getZkClient();
    return zkClient.getChildren(ZkPathConstants.ROOT_PATH);
  }

  private void deepListDrmInfo(List<DrmRequestParam> drmRequestParams, String path) {
    ZkClient zkClient = zkClientFetch.getZkClient();
    List<String> children = zkClient.getChildren(path);
    if (CollectionUtils.isEmpty(children)) {
      if(path.endsWith(ZkPathConstants.PATH_SEP+ZkPathConstants.PERSIST)){
        //只看持久化的数据
        DrmRequestParam drm = zkClient.readData(path);
        if (drm != null) {
          drmRequestParams.add(drm);
        }
      }
    } else {
      for (String child : children) {
        deepListDrmInfo(drmRequestParams, path + ZkPathConstants.PATH_SEP + child);
      }
    }
  }

}
