package com.hydra.core.annotation;

import com.hydra.core.common.ExpressionMeasureEnum;
import com.hydra.core.common.ExpressionStrategyEnum;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Expression {

	/**  执行模式*/
	ExpressionStrategyEnum strategy() default ExpressionStrategyEnum.STANDARD;
	/**  执行单位*/
	ExpressionMeasureEnum measure() default ExpressionMeasureEnum.STANDARD;
	/**  执行因子*/
	String factor() default "";


}
