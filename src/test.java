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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class test {

	public static void main(String[] args) throws IOException {
		
		
		List<Map> list = createList();//导出的数据
		
		for(int i=0;i<999;i++){
			String filePath = "C:\\Users\\THEMING\\Desktop\\myExport.xlsx";
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			Workbook wb = new XSSFWorkbook(fis);//excel文件对象
			Sheet sheet = wb.createSheet();//excel文件内sheet对象
			/*
			 * 遍历导出的数据，追加到excel文件对象中
			 * */
			int j=0;
			for(Map map:list){
				Row row = sheet.createRow(j++);//追加一行
//				Cell cell = row.createCell(row.getRowNum());//创建一个单元格
//				cell.setCellValue(MapUtils.getString(map, ""));//设置单元格内容
				createCell(row,map,"sort",0);
				createCell(row,map,"q",1);
				createCell(row,map,"w",2);
				createCell(row,map,"e",3);
				createCell(row,map,"r",4);
				createCell(row,map,"t",5);
				createCell(row,map,"y",6);
				createCell(row,map,"u",7);
			}
			
			FileOutputStream out = new FileOutputStream(filePath);
			wb.write(out);//把excel对象的数据写入硬盘
			out.close();
			
			System.out.println(i);
		}
		
	}
	
	private static void createCell(Row row,Map map,String key,int j){
		Cell cell = row.createCell(j);//创建一个单元格
		cell.setCellValue(MapUtils.getString(map, key));//设置单元格内容
	}
	
	private static List<Map> createList(){
		List<Map> list = new ArrayList<Map>();
		Map map = new HashMap();
		map.put("q", "q");
		map.put("w", "w");
		map.put("e", "e");
		map.put("r", "r");
		map.put("t", "t");
		map.put("y", "y");
		map.put("u", "u");
		for(int i=0;i<20000;i++){
			map.put("sort", i);
			list.add(map);
		}
		
		return list;
	}
}
