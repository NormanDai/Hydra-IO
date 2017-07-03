package com.hydra.core.flow;

import com.hydra.core.schedule.NormalTaskDescParser;
import com.hydra.core.schedule.Schedule;
import com.hydra.core.container.EnvironmentContext;
import com.hydra.core.wrapper.WrapperBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务流解析
 */
public class TaskFlowParser {

    public static final String CONTXT_FLW_GRAPH = "flowGraph";

    /**
     * 图解析
     */
    public void parser(){
        /**  获取上下文容器*/
        EnvironmentContext environmentContext = EnvironmentContext.getInstance();
        Map<String,Schedule> environmentSchedules = environmentContext.getObject(NormalTaskDescParser.CONTXT_SCHEDUS, Map.class);
        Map<String,TaskFlowGraph> flowGraphMap = new ConcurrentHashMap<String, TaskFlowGraph>();
        for(Map.Entry<String,Schedule> entry : environmentSchedules.entrySet()){

            TaskFlowGraph graph = new TaskFlowGraph();
            graph.setLabName(entry.getKey());
            TaskFlowNode headerNode = new TaskFlowNode();
            headerNode.setLabName(entry.getKey());
            graph.setInitExecutorNode(headerNode);
            setNextNodes(headerNode);
            flowGraphMap.put(entry.getKey(),graph);
        }
        environmentContext.addBean(CONTXT_FLW_GRAPH,flowGraphMap);
    }

    private  void setNextNodes(TaskFlowNode node){
        if(null != node){
            EnvironmentContext environmentContext = EnvironmentContext.getInstance();
            List<WrapperBean> environmentWrappers = environmentContext.getObject(NormalTaskDescParser.CONTXT_WRAPERS, List.class);
            for (WrapperBean wpBean : environmentWrappers) {
                if(null != wpBean.getJoinWrapper() && wpBean.getJoinWrapper().getName().equals(node.getLabName())){
                    TaskFlowNode childNode = new TaskFlowNode();
                    childNode.setLabName(wpBean.getTaskWrapper().getName()+"@"+wpBean.getExecutorWrapper().getValue());
                    node.getNextNodes().add(childNode);
                    setNextNodes(childNode);
                }
            }
        }
    }



}
