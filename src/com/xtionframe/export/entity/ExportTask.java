package com.xtionframe.export.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xtionframe.export.util.DBUtil;
import com.xtionframe.export.util.POIUtil;
import com.xtionframe.export.util.PropertiesUtil;
import com.xtionframe.export.util.WebInfoUtil;

public class ExportTask implements Runnable {
	
	private String connectUrls = null;
	
	private String queryProcedure = null;
	
	/**
	 * 所有入参
	 */
	private Map<String,String> params = null;
	
	private String resultProcedure = null;
	
	/**
	 * 入参的key值数组
	 */
	private String[] keys = null;
	
	private String fileName = null;
	
	private String filePath = null;
	
	/**
	 * excel的标题数组
	 */
	private String[] titles = null;
	
	private String ip = null;
	
	private int port = 0;

	public ExportTask(String connectUrls, String queryProcedure, Map<String,String> params,
			String resultProcedure, HttpServletRequest request) {
		this.connectUrls = connectUrls;
		
		
		
		this.params = new HashMap<String,String>();
		Object[] keys = params.keySet().toArray();
		this.keys = new String[keys.length];
		for(int i=0;i<keys.length;i++){//把map里所有key转成小写，与执行的存储过程保持一致
			String key = keys[i].toString();
			this.keys[i] = key.toLowerCase();
			this.params.put(key.toLowerCase(), MapUtils.getString(params, key,""));
		}
		this.queryProcedure = handleSql(queryProcedure.toLowerCase(), this.params);
		this.resultProcedure = resultProcedure.toLowerCase();
		this.fileName = MapUtils.getString(params, "modulename","导出文件")+"_"+new SimpleDateFormat(Constant.TIMESTAMP_FORMAT_yyyyMMddHHmmssSSS).format(new Date())+Constant.EXCEL;
		this.ip = request.getServerName();
		this.port = request.getServerPort();
		this.filePath = request.getRealPath(Constant.EXPORT_PATH_FOLDER)+File.separatorChar+this.fileName;
	}
	
//	public static String getrealFilePath(String fileName){
//		return request.getRealPath(Constant.EXPORT_PATH_FOLDER)+File.separatorChar+fileName;
//	}

	@Override
	public void run() {
		long start = System.currentTimeMillis();
		System.out.println("======================start======================="+start+"====="+filePath);
		try{
			JdbcTemplate jt = DBUtil.getReadJdbcTemplate(connectUrls);
			long countStart = System.currentTimeMillis();
			int count = getCount(jt,queryProcedure);//总记录数
			System.out.println("=========================查总数据！====================总用时===="+((System.currentTimeMillis()-countStart)/1000)+"秒");
			if(count==0){
				throw new Exception("没有能导出的数据！");
			}
			
			int pageCount = Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PAGE_COUNT,"1000"));//分页查询时每页数
			double num = Math.ceil((double)count/(double)pageCount);
			
			SXSSFWorkbook wb = null;
			FileInputStream fis = null;
			
			
			for(int i=0;i<num;i++){//总共要查询数据库的次数
				System.out.println("=================总共="+num+"=次===================这是第===="+(i+1)+"次");
				long listStart = System.currentTimeMillis();
				List<Map<String,Object>> list = query(jt,queryProcedure,pageCount,i+1);
				System.out.println("=========================查这次数据！====================总用时===="+((System.currentTimeMillis()-listStart)/1000)+"秒");
				if(list!=null&&list.size()!=0){
					if(this.titles==null){//设置标题数组，方便以后使用
						Map<String,Object> map = list.get(0);
						Object[] os = map.keySet().toArray();
						this.titles = new String[os.length];
						for(int j=0;j<os.length;j++){
							this.titles[j] = os[j].toString();
						}
					}
					
					File file = new File(filePath);
					if(!file.exists()){
						File filee = POIUtil.getStream(filePath, this.titles);
						fis = new FileInputStream(filee);
						XSSFWorkbook wbb = new XSSFWorkbook(fis);
						wb = new SXSSFWorkbook(wbb,Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PAGE_COUNT,"1000")));
						wb.setCompressTempFiles(true);
					}
					
					
					
					long exportStart = System.currentTimeMillis();
//					POIUtil.exportPerpare(list, filePath,this.titles);
					POIUtil.exportPerpareShit(list, wb,this.titles);
					System.out.println("=========================写入数据！====================总用时===="+((System.currentTimeMillis()-exportStart)/1000)+"秒");
				}
			}
			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);
			wb.dispose();
//			out.flush();
			out.close();
			
			JdbcTemplate writeJt = DBUtil.getWriteJdbcTemplate(connectUrls);
			writeResult(writeJt,resultProcedure,this.params,"http://"+this.ip+":"+this.port+"/export/download/"+fileName,"");
		
		}catch(Exception e){
			JdbcTemplate writeJt = DBUtil.getWriteJdbcTemplate(connectUrls);
			writeResult(writeJt,resultProcedure,this.params,"",e.getMessage());
		}
		
		
		System.out.println("=========================导出成功！====================总用时===="+((System.currentTimeMillis()-start)/1000)+"秒");
	}
	
	private static void writeResult(JdbcTemplate jt,String resultProcedure,Map<String,String> map,String filePath,String msg){
		System.out.println("======================================filePath======================="+filePath);
		String sql = handleSql(resultProcedure,map).replace(Constant.AT+Constant.EXCEL_FILE_PATH, "'"+filePath+"'").replace(Constant.AT+Constant.ERROR_INFO, "'"+msg+"'");
		jt.execute(sql);
	}
	
	/**
	 * @author guan.jianming
	 * @description 处理传入的查询sql，把@参数替换成传入的参数，后面三个参数特殊处理 exec test_test @userNumber,@iscount,@pageindex,@pagesize 替换成 exec test_test 636985,@iscount,@pageindex,@pagesize
	 * @param queryProcedure 
	 * @return
	 */
	private static String handleSql(String queryProcedure,Map<String,String> map){
		String inputParams = queryProcedure.substring(queryProcedure.indexOf(Constant.AT));//变成 @userNumber,@iscount,@pageindex,@pagesize
		String[] params = inputParams.toLowerCase().replace(Constant.AT,"").split(Constant.Comma);
		for(String param:params){
			if(exceptSomeParams(param)){
				queryProcedure = queryProcedure.replace(Constant.AT+param, "'"+MapUtils.getString(map, param.trim(),"")+"'");//从入参中查找值替换到存储过程中，没找到的用""代替
			}
		}
		return queryProcedure;
	}
	
	/**
	 * @author guan.jianming
	 * @description 如果参数是iscount、pageindex、pagesize、url、errorinfo则忽略
	 * @param param
	 * @return
	 */
	private static Boolean exceptSomeParams(String param){
		if(Constant.ISCOUNT.equals(param)||Constant.PAGEINDEX.equals(param)||Constant.PAGESIZE.equals(param)
				||Constant.EXCEL_FILE_PATH.equals(param)||Constant.ERROR_INFO.equals(param)){
			return false;
		}
		
		return true;
	}
	
	/**
	 * @author guan.jianming
	 * @description 执行查询数据
	 * @param jt
	 * @param queryProcedure
	 * @param pageCount
	 * @param pageIndex
	 * @return
	 */
	public static List<Map<String,Object>> query(JdbcTemplate jt,String queryProcedure,int pageCount,int pageIndex){
		String sql = queryProcedure.replace(Constant.AT+Constant.ISCOUNT, Constant.ISNOTCOUNT_VALUE)
				.replace(Constant.AT+Constant.PAGEINDEX, pageIndex+"")
				.replace(Constant.AT+Constant.PAGESIZE, pageCount+"");
		return jt.queryForList(sql);
	}
	
	/**
	 * @author guan.jianming
	 * @description 执行查询总记录数
	 * @param jt
	 * @param queryProcedure
	 * @return
	 */
	private int getCount(JdbcTemplate jt,String queryProcedure){
		
		String sql = queryProcedure.replace(Constant.AT+Constant.ISCOUNT, Constant.ISCOUNT_VALUE).replace(Constant.AT+Constant.PAGEINDEX, "''").replace(Constant.AT+Constant.PAGESIZE, "''");
		Map<String,Object> map = jt.queryForMap(sql);
		return MapUtils.getIntValue(map, Constant.COUNT,0);
	}
	
	public static void main(String[] args) {
		String queryProcedure = "exec test_test @usernumber , @iscount ,@pageindex,@pagesize";
		Map map = new HashMap();
		map.put("usernumber", 111);
		System.out.println(handleSql(queryProcedure,map));
	}

}
