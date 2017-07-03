package com.hydra.core.schedule;


public interface TaskDescParser<T extends TaskDescription>{

    /**
     * 解析作业描述：并将其转换成体格排班对象
     * @param t
     */
    void parser(T t);
}
