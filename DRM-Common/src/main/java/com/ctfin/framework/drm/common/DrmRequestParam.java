/**
 * DrmRequestParam.java
 * author: yujiakui
 * 2017年9月19日
 * 上午9:54:35
 */
package com.ctfin.framework.drm.common;

import com.ctfin.framework.drm.common.model.DrmOperationEnum;
import java.io.Serializable;

/**
 * @author yujiakui
 *
 *         上午9:54:35
 *
 *         drm推送的请求参数
 */
public class DrmRequestParam implements Cloneable, Serializable {

	/** serial id */
	private static final long serialVersionUID = -2940277243382576657L;

	/** 类名 */
	private String className;

	/** 域名 */
	private String fieldName;

	/** drm推送的值 */
	private Object drmValue;
	/**
	 * 资源操作类型
	 */
	private DrmOperationEnum operation = DrmOperationEnum.PUT;
	public DrmRequestParam() {
	}

	public DrmRequestParam(String className, String fieldName) {
		this.className = className;
		this.fieldName = fieldName;
	}

	public DrmRequestParam(String className, String fieldName, Object drmValue) {
		this.className = className;
		this.fieldName = fieldName;
		this.drmValue = drmValue;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("DrmRequestParam{");
		stringBuilder.append("className=").append(className).append(",");
		stringBuilder.append("fieldName=").append(fieldName).append(",");
		stringBuilder.append("drmValue=").append(drmValue).append(",");
		stringBuilder.append("operation=").append(operation).append("}");
		return stringBuilder.toString();
	}

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the drmValue
	 */
	public Object getDrmValue() {
		return drmValue;
	}

	/**
	 * @param drmValue
	 *            the drmValue to set
	 */
	public void setDrmValue(Object drmValue) {
		this.drmValue = drmValue;
	}

	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	public DrmOperationEnum getOperation() {
		return operation;
	}

	public void setOperation(DrmOperationEnum operation) {
		this.operation = operation;
	}
}
