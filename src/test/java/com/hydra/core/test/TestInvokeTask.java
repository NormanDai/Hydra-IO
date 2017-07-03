package com.hydra.core.test;

import com.hydra.core.schedule.EnvironmentParams;
import com.hydra.core.annotation.*;
import com.hydra.core.common.DistributedStrategyEnum;
import com.hydra.core.common.ExpressionMeasureEnum;
import com.hydra.core.common.ExpressionStrategyEnum;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 测试类
 * @author Administrator
 *
 */
@Task("testInvokeTask")
public class TestInvokeTask {



	@Executor("executorA")
	@Expression(strategy = ExpressionStrategyEnum.TIMING, measure = ExpressionMeasureEnum.MINUTE,factor = "15")
	@Distributed(strategy = DistributedStrategyEnum.SHARDING,number = 2)
	public String executorA(EnvironmentParams context){
		String jobName = context.getJobName();
		int invokeIndex = context.getInvokeIndex();
		String str = jobName + " - " + invokeIndex + " running at " + new Date();
		System.out.println(str);
		return str;
	}


	@Join("testInvokeTask@executorA")
	@Executor("executorB")
	public String executorB(EnvironmentParams context, String values){

		String jobName = context.getJobName();
		int invokeIndex = context.getInvokeIndex();
		String str = jobName + " - " + invokeIndex + " running at " + new Date() + " param is:[" + values + " ]" ;
		System.out.println(str);
		return jobName;
	}

	@Join("testInvokeTask@executorB")
	@Executor("executorC")
	public String executorC(EnvironmentParams context, String values){
		String jobName = context.getJobName();
		int invokeIndex = context.getInvokeIndex();
		String str = jobName + " - " + invokeIndex + " running at " + new Date() + " param is:[" + values + " ]" ;
		System.out.println(str);
		return jobName;
	}

	public void executorE(){


	}

	public static void main(String[] strings){
		Class[] pat = new Class[2];
		pat[0] = EnvironmentParams.class;
		pat[1] = String.class;
		try {
			Method method = TestInvokeTask.class.getMethod("executorE", null);


			System.out.println(method.getName());
			System.out.println(method.getReturnType());
			Class<?>[] parameterTypes = method.getParameterTypes();
			System.out.println(parameterTypes.length);
			for (int i = 0; i < parameterTypes.length ; i++) {
				//System.out.println(parameterTypes[i].getTypeName());
			}


		}catch (Exception exception){
			exception.printStackTrace();
		}

	}



}
