/**
 * TestMain.java
 * author: yujiakui
 * 2017年9月19日
 * 下午4:01:11
 */
package com.ctfin.framework.drm.common.test;

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
		System.setProperty("zk.server.addr", "127.0.0.1:2181");
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(
				"com.ctfin.framework.drm");


		while (true) {
			Thread.sleep(10);
		}

	}

}
