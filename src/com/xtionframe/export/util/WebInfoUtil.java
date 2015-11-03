package com.xtionframe.export.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author guan.jianming
 * 获取web应用相关信息工具类
 */
public class WebInfoUtil {
	
	public static HttpServletRequest getRequest(){
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	static String ip = null;
	
	/**
	 * @author guan.jianming
	 * @description 获取服务器名称 例如http://localhost:8080/xtionframe-web/api/sfa/salearea/exportSaleArea中的localhost
	 * @return
	 */
	public static String getIp(){
		return getRequest().getServerName();
	}
	
	/**
	 * @author guan.jianming
	 * @description 获取应用名 例如http://localhost:8080/xtionframe-web/api/sfa/salearea/exportSaleArea中的/xtionframe-web
	 * @return
	 */
	public static String getContextPath(){
		return getRequest().getContextPath();
	}
	
	/**
	 * @author guan.jianming
	 * @description 获取服务名 例如http://localhost:8080/xtionframe-web/api/sfa/salearea/exportSaleArea中的/api/sfa
	 * @return
	 */
	public static String getServletPath(){
		return getRequest().getServletPath();
	}
	
	/**
	 * @author guan.jianming
	 * @description 获取端口 例如http://localhost:8080/xtionframe-web/api/sfa/salearea/exportSaleArea中的8080
	 * @return
	 */
	public static int getServerPort(){
		return getRequest().getServerPort();
	}
	
	/**
	 * @author guan.jianming
	 * @description 获取端口 例如http://localhost:8080/xtionframe-web/api/sfa/salearea/exportSaleArea中的/salearea/exportSaleArea
	 * @return
	 */
	public static String getPathInfo(){
		return getRequest().getPathInfo();
	}
	
	/**
	 * @author guan.jianming
	 * @description 获取应用部署在服务器里的绝对路径 
	 * 例如E:\Mything\jetty-distribution-9.2.6.v20141205\jetty-distribution-9.2.6.v20141205\webapps\xtionframe-web
	 * 此方法用于生成文件时获取文件保存的绝对路径
	 * @param subPath subPath可空
	 * @return
	 */
	public static String getRealPath(String subPath){
		return getRequest().getRealPath(subPath);
//		return "E:\\Mything\\jetty-distribution-9.2.6.v20141205\\jetty-distribution-9.2.6.v20141205\\webapps\\root\\data\\temp";
//		return getRequest().getSession().getServletContext().getRealPath(subPath);
	}

}
