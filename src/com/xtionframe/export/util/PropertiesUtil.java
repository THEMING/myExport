package com.xtionframe.export.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.springframework.core.io.ClassPathResource;

/**
 * @author guan.jianming
 * @description 配置文件工具类，具有缓存配置文件的功能
 */
public class PropertiesUtil {
	//缓存配置文件列表的集合对象
	private static Map properties = null;
	
	/**
	 * @author guan.jianming
	 * @description 获取配置文件，并将其放入缓存内
	 * @param fileName
	 * @return
	 */
	private static Properties getProperties(String fileName){
		if(properties==null){
			properties = new ConcurrentHashMap();
		}
		if(properties.containsKey(fileName)){
			return (Properties) MapUtils.getObject(properties, fileName);
		}else{
			try { 
				Properties property = new Properties();
				InputStream is = new FileInputStream(new ClassPathResource(fileName).getFile());
				property.load(new BufferedReader(new InputStreamReader(is, "utf-8")));
				is.close();
				properties.put(fileName, property);
				return property;
			} catch (IOException e1) {   
				   e1.printStackTrace();   
			}
		}
		return null;
	}
	
	/**
	 * @author guan.jianming
	 * @description 根据配置文件名和属性名称获取值
	 * @param fileName
	 * @param propertyName
	 * @return
	 */
	public static String getPropertyValue(String fileName,String propertyName,String defaultValue){
		return getProperties(fileName).getProperty(propertyName,defaultValue==null?"":defaultValue);
	}

}
