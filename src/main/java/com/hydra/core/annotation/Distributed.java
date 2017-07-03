package com.hydra.core.annotation;

import com.hydra.core.common.DistributedStrategyEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Distributed {
	/**
	 * 任务分发策略
	 * @return
	 */
	DistributedStrategyEnum strategy() default DistributedStrategyEnum.PARALLEL;

	/**
	 * 分发数量
	 * @return
	 */
	int number() default 1;

}
