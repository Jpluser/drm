/**
 * TestDrm.java
 * author: yujiakui
 * 2018年1月2日
 * 上午10:31:07
 */
package com.ctfin.framework.drm.admin.test;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ctfin.framework.drm.admin.model.DrmConfRequestParam;
import com.ctfin.framework.drm.admin.service.DrmService;
import com.google.common.collect.Lists;

/**
 * @author yujiakui
 *
 *         上午10:31:07
 *
 */
public class TestDrm {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("zk.server.addr", "127.0.0.1:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm");
		DrmConfRequestParam drmConfRequestParam = new DrmConfRequestParam();
		drmConfRequestParam.setApplicationName("yjk");
		drmConfRequestParam.setClassName("com.ctfin.framework.drm.common.test.DrmWebInfoCndResource");
		drmConfRequestParam.setDrmValue("hellohyb");
		drmConfRequestParam.setFieldName("testDrmValue");
		//drmConfRequestParam.setDrmRequestUrl(Lists.newArrayList("172.16.255.123", "127.0.0.1"));
		DrmService drmService = annotationConfigApplicationContext.getBean(DrmService.class);
		// drmService.push(drmConfRequestParam);
		drmService.push(drmConfRequestParam);

		Thread.sleep(200);
	}

}
