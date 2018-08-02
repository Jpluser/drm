/**
 * TestMain.java
 * author: yujiakui
 * 2017年9月19日
 * 下午4:01:11
 */
package com.ctfin.framework.drm.common.test;

import com.ctfin.framework.drm.client.DrmResourceParseFactory;
import com.ctfin.framework.drm.common.DrmRequestParam;
import com.ctfin.framework.drm.common.DrmRequestResult;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author yujiakui
 *
 *         下午4:01:11
 *
 */
public class TestMain {

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("applicationName", "yjk");
		System.setProperty("zk.server.addr", "172.16.9.184:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm");


		while (true) {
			Thread.sleep(10);
		}

	}

}
