package com.hydra.core.schedule;


public interface Schedule {
	
	/**
	 * 下次执行时间
	 * @return
	 */
	public long nextInvokeTimeByLong();
	
	/**
	 * 是否可以执行
	 * @return
	 */
	public boolean whetherReady();

}
