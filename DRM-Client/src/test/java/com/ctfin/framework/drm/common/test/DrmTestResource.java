/**
 * DrmWebInfoCndResource.java author: yujiakui 2017年9月19日 下午3:59:34
 */
package com.ctfin.framework.drm.common.test;

import com.ctfin.framework.drm.client.DrmResourceManager;
import com.ctfin.framework.drm.common.annotation.DrmClassResource;
import com.ctfin.framework.drm.common.annotation.DrmFieldResource;
import org.springframework.stereotype.Component;

/**
 * @author yujiakui
 *
 * 下午3:59:34
 */
@Component
@DrmClassResource
public class DrmTestResource implements DrmResourceManager {

  @DrmFieldResource
  private String drm = "helloworld";

  @Override
  public void onUpdate(String filed,Object before, Object after) {
    System.out.println("filed update,"+"before:" + before + ",after:" + after);
  }

}
