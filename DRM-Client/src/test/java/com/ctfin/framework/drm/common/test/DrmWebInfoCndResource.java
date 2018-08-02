/**
 * DrmWebInfoCndResource.java author: yujiakui 2017年9月19日 下午3:59:34
 */
package com.ctfin.framework.drm.common.test;

import com.ctfin.framework.drm.client.DrmResourceManager;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.common.annotation.DrmClassResource;
import com.ctfin.framework.drm.common.annotation.DrmFieldResource;

/**
 * @author yujiakui
 *
 * 下午3:59:34
 */
@Component
@DrmClassResource
public class DrmWebInfoCndResource implements DrmResourceManager {

  @DrmFieldResource
  private String testDrmValue = "helloworld";

  @Override
  public void onUpdate(String filed,Object before, Object after) {
    System.out.println("filed:"+filed+" update,"+"before:" + before + ",after:" + after);
  }

}
