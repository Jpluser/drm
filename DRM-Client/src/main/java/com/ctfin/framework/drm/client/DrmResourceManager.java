/**
 * DrmResourceManager.java author: yujiakui 2017年9月19日 上午8:40:49
 */
package com.ctfin.framework.drm.client;

/**
 * @author yujiakui
 *
 *         上午8:40:49
 *
 *         资源管理接口：具体的更新行为是通过反射进行
 */
public interface DrmResourceManager {
  void onUpdate(String filedName,Object before, Object after);
}
