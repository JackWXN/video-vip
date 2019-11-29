package com.video.vip.basics.util.basics;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 获取spring流程上下文
 * @author wxn
 */
@Component
public class MyApplicationContextUtil implements ApplicationContextAware {
	
	private static ApplicationContext context;
	
	private static Executor executor;

	@Override
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext contex) throws BeansException {
		MyApplicationContextUtil.context=contex;
	} 
	
	public static ApplicationContext getContext(){
		return context;
	}
	
	{
		 executor =  new ThreadPoolExecutor(50,130, 30, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(100000),new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	/**
	 * 
	 * publishEvent:(监听). <br/>
	 *
	 * @author yajun.han
	 * @param event
	 * @since JDK 1.7
	 */
	public static void publishEvent(final ApplicationEvent event) {
        executor.execute(new Runnable() {
        	@Override
            public void run() {  
            	context.publishEvent(event); 
            }  
        });  
    }
}
