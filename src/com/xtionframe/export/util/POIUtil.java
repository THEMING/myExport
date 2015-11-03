package com.xtionframe.export.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.xtionframe.export.entity.Constant;

public class POIUtil {
	
	private static String PER_SHEET_MAX_ROW_NUM = "1000000";
	
	public static void exportPerpare(List<Map<String,Object>> list,String fileName, String[] titles){
		int count = list.size();
		int perWriteIntoExcel = Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PER_WRITE_INTO_EXCEL,"100000"));
		double num = Math.ceil((double)count/(double)perWriteIntoExcel);
		for(int i=0;i<num;i++){
			long exportStart = System.currentTimeMillis();
			export(list.subList(i*perWriteIntoExcel, (i+1)*perWriteIntoExcel>count?count:((i+1)*perWriteIntoExcel)),fileName,titles);
			System.out.println("=========================单次写入处理！====================总用时===="+((System.currentTimeMillis()-exportStart)/1000)+"秒");
		}
	}
	
	public static void exportPerpareShit(List<Map<String,Object>> list,SXSSFWorkbook wb, String[] titles){
		int count = list.size();
		int perWriteIntoExcel = Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PER_WRITE_INTO_EXCEL,"100000"));
		double num = Math.ceil((double)count/(double)perWriteIntoExcel);
		for(int i=0;i<num;i++){
			long exportStart = System.currentTimeMillis();
			exportShit(list.subList(i*perWriteIntoExcel, (i+1)*perWriteIntoExcel>count?count:((i+1)*perWriteIntoExcel)),wb,titles);
			System.out.println("=========================单次写入处理！====================总用时===="+((System.currentTimeMillis()-exportStart)/1000)+"秒");
		}
	}
	
	public static void export(List<Map<String,Object>> list,String fileName, String[] titles){
		
		long getStreamStart = System.currentTimeMillis();
		File file = getStream(fileName,titles);
		System.out.println("=========================生成文件！====================总用时===="+((System.currentTimeMillis()-getStreamStart)/1000)+"秒");
		Workbook wb = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			wb = new XSSFWorkbook(fis);
			fos = new FileOutputStream(file,true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			FileUtil.closeInputStrem(fis);
			FileUtil.closeOutputStrem(fos);
		}
		
		int sheetNum = wb.getNumberOfSheets();
		Sheet sheet = wb.getSheetAt(sheetNum-1);
		int rowNum = sheet.getLastRowNum()+1;
		int perSheetMaxRow = Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PER_SHEET_MAX_ROW,PER_SHEET_MAX_ROW_NUM));//excel文件里每个sheet最大行数
		
		for(Map<String,Object> map:list){//循环数据，追加到excel文件
			if(rowNum>perSheetMaxRow){//当当前sheet页的行数已经超过设定的最大行数，则新增一个sheet，设置标题，重置当前行数
				long createASheetStart = System.currentTimeMillis();
				sheet = createASheet(wb,titles);
				System.out.println("=========================新增sheet设置标题！====================总用时===="+((System.currentTimeMillis()-createASheetStart)/1000)+"秒");
				rowNum = sheet.getLastRowNum()+1;//重置当前行数
				
			}
//			System.out.println("================================正在追加第"+rowNum+"行==========================="+rowNum);
			Row row = sheet.createRow(rowNum);//追加一行
			
			for(int i=0;i<titles.length;i++){
				Cell cell = row.createCell(i);
				cell.setCellValue(MapUtils.getString(map, titles[i],""));
			}
			
			rowNum++;//当前行数+1
		}
		
		try {
			wb.write(fos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			FileUtil.closeInputStrem(fis);
			FileUtil.closeOutputStrem(fos);
		}
	}
	
public static void exportShit(List<Map<String,Object>> list,SXSSFWorkbook wb, String[] titles){
		
//		long getStreamStart = System.currentTimeMillis();
//		File file = getStream(fileName,titles);
//		System.out.println("=========================生成文件！====================总用时===="+((System.currentTimeMillis()-getStreamStart)/1000)+"秒");
//		Workbook wb = null;
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//		try {
//			fis = new FileInputStream(file);
//			wb = new XSSFWorkbook(fis);
//			fos = new FileOutputStream(file,true);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			FileUtil.closeInputStrem(fis);
//			FileUtil.closeOutputStrem(fos);
//		}
		
		int sheetNum = wb.getNumberOfSheets();
		Sheet sheet = wb.getSheetAt(sheetNum-1);
		int rowNum = sheet.getLastRowNum()+1;
		int perSheetMaxRow = Integer.parseInt(PropertiesUtil.getPropertyValue("export.properties", Constant.PER_SHEET_MAX_ROW,PER_SHEET_MAX_ROW_NUM));//excel文件里每个sheet最大行数
		
		for(Map<String,Object> map:list){//循环数据，追加到excel文件
			if(rowNum>perSheetMaxRow){//当当前sheet页的行数已经超过设定的最大行数，则新增一个sheet，设置标题，重置当前行数
				long createASheetStart = System.currentTimeMillis();
				sheet = createASheet(wb,titles);
				System.out.println("=========================新增sheet设置标题！====================总用时===="+((System.currentTimeMillis()-createASheetStart)/1000)+"秒");
				rowNum = sheet.getLastRowNum()+1;//重置当前行数
				
			}
//			System.out.println("================================正在追加第"+rowNum+"行==========================="+rowNum);
			Row row = sheet.createRow(rowNum);//追加一行
			
			for(int i=0;i<titles.length;i++){
				Cell cell = row.createCell(i);
				cell.setCellValue(MapUtils.getString(map, titles[i],""));
			}
			
			rowNum++;//当前行数+1
		}
		
//		try {
//			wb.write(fos);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//			FileUtil.closeInputStrem(fis);
//			FileUtil.closeOutputStrem(fos);
//		}
	}
	
	public static File getStream(String fileName,String[] titles){
		File file = new File(fileName);
		if(!file.exists()){
			try {
				Workbook wb = new XSSFWorkbook();
				createASheet(wb,titles);
				FileOutputStream fileOut = new FileOutputStream(fileName);//,true);
			    wb.write(fileOut);
			    fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		
		return file;
	}
	
	public static Sheet createASheet(Workbook wb,String[] titles){
		Sheet sheet = wb.createSheet();//新增一个sheet
		Row row = sheet.createRow(0);
		for(int i=0;i<titles.length;i++){//设定标题头
			Cell cell = row.createCell(i);
			cell.setCellValue(titles[i]);
		}
		
		return sheet;
	}
	
	public static void main(String[] args) {
//		List<String> a = new ArrayList<String>();
//		a.add("1");
//		a.add("2");
//		a.add("3");
//		a.add("4");
//		a.add("5");
//		List<String> b = a.subList(0,9);
//		System.out.println(a.size());
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("E:\\jetty-distribution-8.1.16.v20140903\\webapps\\root\\export\\人员模块_20151103093701013_new.xlsx"),true);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			File file = new File("E:\\jetty-distribution-8.1.16.v20140903\\webapps\\root\\export\\人员模块_20151103093701013.xlsx");
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = new XSSFWorkbook(fis);
//			FileOutputStream fos = new FileOutputStream(new File("E:\\jetty-distribution-8.1.16.v20140903\\webapps\\root\\export\\人员模块_20151103093701013_new.xlsx"),true);
//			byte[] b = new byte[1000];   
//            int n;   
//            while ((n = fis.read(b)) != -1) {   
//            	fos.write(b, 0, n);   
//            } 
			int i = wb.getNumberOfSheets();
			Sheet sheet = wb.getSheetAt(i-1);
			Row row = sheet.createRow(sheet.getLastRowNum());
			Cell cell = row.createCell(0);
			cell.setCellValue("11");
			wb.write(fos);
            
            fis.close();   
            fos.flush();
//            fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			File file = new File("E:\\jetty-distribution-8.1.16.v20140903\\webapps\\root\\export\\人员模块_20151103093701013_new.xlsx");
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = new XSSFWorkbook(fis);
//			FileOutputStream fos = new FileOutputStream(new File("E:\\jetty-distribution-8.1.16.v20140903\\webapps\\root\\export\\人员模块_20151103093701013_new.xlsx"),true);
//			byte[] b = new byte[1000];   
//            int n;   
//            while ((n = fis.read(b)) != -1) {   
//            	fos.write(b, 0, n);   
//            } 
			int i = wb.getNumberOfSheets();
			Sheet sheet = wb.getSheetAt(i-1);
			Row row = sheet.createRow(sheet.getLastRowNum());
			Cell cell = row.createCell(0);
			cell.setCellValue("11");
			wb.write(fos);
            
            fis.close();   
            
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try{
			fos.flush();
            fos.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("end");
		
		
	}

}
