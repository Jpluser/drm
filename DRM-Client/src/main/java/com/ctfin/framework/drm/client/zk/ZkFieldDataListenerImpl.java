/**
 * ZkFieldDataListenerImpl.java author: yujiakui 2018年1月3日 下午5:19:47
 */
package com.ctfin.framework.drm.client.zk;

import com.ctfin.framework.drm.client.DrmResourceParseFactory;
import com.ctfin.framework.drm.common.DrmObject;
import com.ctfin.framework.drm.common.model.DrmOperationEnum;
import com.ctfin.framework.drm.common.model.FieldModel;
import com.ctfin.framework.drm.common.zk.ZkClientFetch;
import org.I0Itec.zkclient.IZkDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ctfin.framework.drm.common.utils.LogUtils;

/**
 * @author yujiakui
 *
 *         下午5:19:47
 *
 */
@Component
public class ZkFieldDataListenerImpl implements IZkDataListener {

  /** logger */
  private final static Logger LOGGER = LoggerFactory.getLogger(ZkFieldDataListenerImpl.class);

  @Autowired
  private ZkClientFetch zkClientFetch;
  @Autowired
  private DrmResourceParseFactory drmResourceParseFactory;


  @Override
  public void handleDataChange(String dataPath, Object data) throws Exception {
    LogUtils.info(LOGGER, "handleDataChange dataPath={0},data={1}", dataPath, data);
    if (data instanceof DrmObject) {
      DrmObject drmObject = (DrmObject) data;
      if (drmObject.getOperation() == DrmOperationEnum.GET) {
        FieldModel fieldModel = drmResourceParseFactory.getParseResultTable()
            .get(drmObject.getClassName(), drmObject.getFieldName());
        Object value = fieldModel.getValue();
        drmObject.setDrmValue(value);
        drmObject.setOperation(DrmOperationEnum.PUT);
        zkClientFetch.getZkClient().writeData(dataPath, drmObject);
        LOGGER.info("reload filed:{},value:{} to server", dataPath, value);
      } else {
        parseAndresetFieldValue(drmObject);
      }
    }
  }

  /**
   * 解析对应的路径获得对应的类名和属性名，然后将对应的值推送
   *
   * @param drmObject
   */
  private void parseAndresetFieldValue(DrmObject drmObject) {
    drmResourceParseFactory.resetFieldValue(drmObject);
  }


  @Override
  public void handleDataDeleted(String dataPath) throws Exception {
    LogUtils.info(LOGGER, "handleDataDeleted dataPath={0}", dataPath);
  }

}
