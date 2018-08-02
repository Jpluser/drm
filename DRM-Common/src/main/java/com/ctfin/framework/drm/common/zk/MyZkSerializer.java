/**
 * MyZkSerializer.java author: yujiakui 2018年1月19日 下午1:59:29
 */
package com.ctfin.framework.drm.common.zk;

import com.alibaba.fastjson.JSON;
import com.ctfin.framework.drm.common.DrmRequestParam;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yujiakui
 *
 *         下午1:59:29
 *
 */
public class MyZkSerializer implements ZkSerializer {

  private static final Logger logger = LoggerFactory.getLogger(MyZkSerializer.class);

  @Override
  public byte[] serialize(Object data) throws ZkMarshallingError {
    try {
      return JSON.toJSONString(data).getBytes("UTF-8");
    } catch (Exception e) {
      logger.error("ZkSerializer serialize error:{}",JSON.toJSONString(data),e);
    }
    return null;
  }

  @Override
  public Object deserialize(byte[] bytes) throws ZkMarshallingError {
    try {
      return JSON.parseObject(new String(bytes, "UTF-8"), DrmRequestParam.class);
    } catch (Exception e) {
      logger.error("ZkSerializer deserialize error",e);
    }
    return null;
  }

}
