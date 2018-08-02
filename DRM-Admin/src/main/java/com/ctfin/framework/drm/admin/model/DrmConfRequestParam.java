/**
 * DrmConfRequestParam.java author: yujiakui 2017年9月19日 下午1:46:42
 */
package com.ctfin.framework.drm.admin.model;

import com.ctfin.framework.drm.common.DrmRequestParam;
import com.ctfin.framework.drm.common.model.DrmOperationEnum;
import com.ctfin.framework.drm.common.model.ZkPathConstants;
import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

/**
 * @author yujiakui
 *
 *         下午1:46:42
 *
 *         drm配置请求参数
 */
public class DrmConfRequestParam extends DrmRequestParam {

  /** serial id */
  private static final long serialVersionUID = -4391448172258037621L;

  /** drm请求对应的url地址:如果推送所有的则对应的url列表中存放的就是all */
  private List<String> drmRequestUrl;

  /** 应用程序的名称 */
  private String applicationName;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("DrmConfRequestParam{");
    stringBuilder.append("drmRequestUrl=[").append(drmRequestUrl).append("],");
    stringBuilder.append("applicationName=").append(applicationName).append(",");
    stringBuilder.append("className=").append(getClassName()).append(",");
    stringBuilder.append("fieldName=").append(getFieldName()).append(",");
    stringBuilder.append("drmValue=").append(getDrmValue());
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  @Override
  public DrmConfRequestParam clone() throws CloneNotSupportedException {
    DrmConfRequestParam drmConfRequestParam = (DrmConfRequestParam) super.clone();
    drmConfRequestParam.setApplicationName(applicationName);
    drmConfRequestParam.setClassName(getClassName());
    drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList(drmRequestUrl));
    drmConfRequestParam.setDrmValue(getDrmValue());
    drmConfRequestParam.setFieldName(getFieldName());
    return drmConfRequestParam;
  }

  public String getDataPath(){
    String reqUrlFieldpath = StringUtils.join(
        Lists.newArrayList(ZkPathConstants.ROOT_PATH,
            applicationName,
            getClassName(), getFieldName()),
        ZkPathConstants.PATH_SEP);
    return reqUrlFieldpath.replace(".", ZkPathConstants.PATH_SEP);
  }

  /**
   * @return the drmRequestUrl
   */
  public List<String> getDrmRequestUrl() {
    return drmRequestUrl;
  }

  /**
   * @param drmRequestUrl
   *            the drmRequestUrl to set
   */
  public void setDrmRequestUrl(List<String> drmRequestUrl) {
    this.drmRequestUrl = drmRequestUrl;
  }

  /**
   * @return the applicationName
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * @param applicationName
   *            the applicationName to set
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

}
