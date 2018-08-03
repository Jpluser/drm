package com.ctfin.framework.drm.common.model;

import java.util.Date;

public class DrmClients {

  private String ip;
  private Date createTime;
  private Date modifyTime;

  public DrmClients() {
  }

  public DrmClients(String ip, Date createTime, Date modifyTime) {
    this.ip = ip;
    this.createTime = createTime;
    this.modifyTime = modifyTime;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }
}
