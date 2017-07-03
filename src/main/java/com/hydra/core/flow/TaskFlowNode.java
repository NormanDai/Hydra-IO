package com.hydra.core.flow;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TaskFlowNode {

    private String labName;

    private List<TaskFlowNode> nextNodes = new ArrayList<TaskFlowNode>();


    public void addAfter(TaskFlowNode node){
        nextNodes.add(node);
    }

    public TaskFlowNode getIndex(int index){
        return nextNodes.get(index);
    }

}
