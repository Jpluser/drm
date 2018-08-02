package com.ctfin.framework.drm.common.model;

import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

public class FieldModel<T> {
  private T bean;
  private Field field;

  public FieldModel(T bean, Field field) {
    this.bean = bean;
    this.field = field;
  }

  public T getBean() {
    return bean;
  }

  public void setBean(T bean) {
    this.bean = bean;
  }

  public Field getField() {
    return field;
  }

  public void setField(Field field) {
    this.field = field;
  }

  public Object getValue(){
    return ReflectionUtils.getField(field,bean);
  }
}
