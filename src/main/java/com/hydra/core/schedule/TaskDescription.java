package com.hydra.core.schedule;

import com.hydra.core.wrapper.WrapperBean;

import java.util.List;

public interface TaskDescription {

    /**
     * 抽取描述信息
     * @param
     */
     void extractDescription();

     List<WrapperBean> getWrappers();

}
