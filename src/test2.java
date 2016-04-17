import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class test2 {

	public static void main(String[] args) throws IOException {
		
		
		
		String filePath = "C:\\Users\\THEMING\\Desktop\\myExport.xlsx";//文件路径
		SXSSFWorkbook wb = null;//excel文件对象
		
		for(int i=0;i<3;i++){//虚拟导入3个sheet
			
			if(wb==null){
				wb = new SXSSFWorkbook(100);//excel文件对象，设置常驻内存的记录数为100
				wb.setCompressTempFiles(true);//压缩临时文件
			}
			
			Sheet sheet = wb.createSheet();//excel文件内sheet对象
			/*
			 * 遍历导出的数据，追加到excel文件对象中
			 * */
			for(int j=0;j<500000;j++){//虚拟导入500000行
				Row row = sheet.createRow(j++);//追加一行
				for(int k=0;k<16383;k++){//虚拟导入16383列
					Cell cell = row.createCell(k);//创建一个单元格
					cell.setCellValue(k);
				}
			}
		}
		
		FileOutputStream out = new FileOutputStream(filePath);
		wb.write(out);//把excel对象的数据写入硬盘
		out.close();
		
	}
	
//	private static void createCell(Row row,Map map,String key,int j){
//		Cell cell = row.createCell(j);//.setCellValue(MapUtils.getString(map, key));//创建一个单元格
//		cell.setCellValue(MapUtils.getString(map, key));//设置单元格内容
//		cell = null;
//	}
//	
//	private static List<Map> createList(){
//		List<Map> list = new ArrayList<Map>();
//		Map map = new HashMap();
//		map.put("q", "q");
//		map.put("w", "w");
//		map.put("e", "e");
//		map.put("r", "r");
//		map.put("t", "t");
//		map.put("y", "y");
//		map.put("u", "u");
//		for(int i=0;i<500000;i++){
//			map.put("sort", i);
//			list.add(map);
//		}
//		
//		return list;
//	}
}
