/**
 * TestDrmList.java
 * author: yujiakui
 * 2018年1月8日
 * 下午3:26:25
 */
package com.ctfin.framework.drm.admin.test;

import com.ctfin.framework.drm.common.DrmRequestParam;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
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
		System.setProperty("zk.server.addr", "172.16.9.184:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm");
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		List<String> strings = drmService.listAllApps();
		for(String app:strings){
			List<DrmRequestParam> drmRequestParams = drmService.listAllDrmInfo(app);
			System.out.println(drmRequestParams);

		}


		Thread.sleep(200);
	}

}
