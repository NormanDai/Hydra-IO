package com.hydra.core.flow;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskFlowGraph extends TaskFlowNode{

    private TaskFlowNode initExecutorNode;


    public TaskFlowNode getHeader(){
        return initExecutorNode;
    }

}
