import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
		String filePath = "";
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		List<Map> list = new ArrayList<Map>();//导出的数据
		Workbook wb = new XSSFWorkbook(fis);//excel文件对象
		Sheet sheet = wb.createSheet();//excel文件内sheet对象
		/*
		 * 遍历导出的数据，追加到excel文件对象中
		 * */
		list.forEach(map -> {
			Row row = sheet.createRow(sheet.getLastRowNum());//追加一行
			Cell cell = row.createCell(row.getRowNum());//创建一个单元格
			cell.setCellValue(MapUtils.getString(map, ""));//设置单元格内容
		});
		
		FileOutputStream out = new FileOutputStream(filePath);
		wb.write(out);//把excel对象的数据写入硬盘
		out.close();
	}
}
