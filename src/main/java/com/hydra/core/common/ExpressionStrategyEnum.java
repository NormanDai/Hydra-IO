package com.hydra.core.common;

/**
 * 表达式解析策略
 */
public enum ExpressionStrategyEnum {
    /**
     * 标准模式：只执行一次
     */
    STANDARD,
    /**
     * 定时模式：以时间日期为单位定时执行
     */
    TIMING,
    /**
     * 间隔模式：以时间为单位间隔执行
     */
    INTERVAL;
}
