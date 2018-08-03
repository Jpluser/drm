/**
 * TestDrmList.java
 * author: yujiakui
 * 2018年1月8日
 * 下午3:26:25
 */
package com.ctfin.framework.drm.admin.test;

import com.ctfin.framework.drm.common.DrmObject;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.service.DrmService;

/**
 * @author yujiakui
 *
 *         下午3:26:25
 *
 */
public class TestDrmList {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "127.0.0.1:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm");
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		List<String> strings = drmService.listAllApps();
		for(String app:strings){
			List<DrmObject> drmObjects = drmService.listAllDrmInfo(app);
			System.out.println(drmObjects);

		}


		Thread.sleep(200);
	}

}
