package com.hydra.core.common;

/**
 * 分发策略
 */
public enum DistributedStrategyEnum {
    /**
     * 分布式 分片
     */
    SHARDING,
    /**
     * 本地并行
     */
    PARALLEL;

}
