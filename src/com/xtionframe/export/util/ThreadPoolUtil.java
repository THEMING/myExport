package com.xtionframe.export.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.xtionframe.export.entity.Constant;

import java.lang.Runnable;

public class ThreadPoolUtil {
	
	private static ThreadPoolTaskExecutor poolTaskExecutor = null;
	
	public static ThreadPoolTaskExecutor getTheThreadPool(){
		if(poolTaskExecutor == null){
			ThreadPoolTaskExecutor poolTaskExecutorTemp = new ThreadPoolTaskExecutor();  
			//线程池所使用的缓冲队列  
			poolTaskExecutorTemp.setQueueCapacity(Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.QUEUE_CAPACITY,"100")));  
			//线程池维护线程的最少数量  
			poolTaskExecutorTemp.setCorePoolSize(Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.CORE_POOL_SIZE,"5")));  
			//线程池维护线程的最大数量  
			poolTaskExecutorTemp.setMaxPoolSize(Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.MAX_POOL_SIZE,"10")));  
			//线程池维护线程所允许的空闲时间  
			poolTaskExecutorTemp.setKeepAliveSeconds(30000);  
			poolTaskExecutorTemp.initialize();  
			poolTaskExecutor = poolTaskExecutorTemp;
		}
		return poolTaskExecutor;
	}
	
	public static void putInPool(Runnable clazz){
		getTheThreadPool().execute(clazz);
	}
	
	public static void printActiveCount(){
		System.out.println("========poolTaskExecutor.getActiveCount()==================="+poolTaskExecutor.getActiveCount());
	}
}
