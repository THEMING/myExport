package com.xtionframe.export.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xtionframe.export.entity.Constant;
import com.xtionframe.export.entity.ExportTask;
import com.xtionframe.export.util.DBUtil;
import com.xtionframe.export.util.ThreadPoolUtil;

@RestController
@RequestMapping("/export")
public class ExportService {
	


	@Autowired
	private  HttpServletRequest request;
	
	@RequestMapping(value="/export",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String test(@RequestBody Map<String,Object> map){
		String connectUrls = MapUtils.getString(map, "connectUrls","");
		String queryProcedure = MapUtils.getString(map, "queryProcedure","");
		Map<String,String> params = MapUtils.getMap(map, "execParams",null);
		String resultProcedure = MapUtils.getString(map, "resultProcedure","");
		
		if(connectUrls.isEmpty()){
			return "缺少参数connectUrls";
		}
		if(queryProcedure.isEmpty()){
			return "缺少参数queryProcedure";
		}
		if(params==null){
			return "缺少参数execParams";
		}
		if(resultProcedure.isEmpty()){
			return "缺少参数resultProcedure";
		}
		
		ExportTask et = new ExportTask(connectUrls,queryProcedure,params,resultProcedure,request);
		ThreadPoolUtil.putInPool(et);
		
		return "success";
	}
	
	@RequestMapping(value="/download/{fileName}",method = RequestMethod.GET)   
	public ResponseEntity<byte[]> download(@PathVariable String fileName) throws IOException {   
	    HttpHeaders headers = new HttpHeaders();   
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
	    headers.setContentDispositionFormData("attachment", new String((fileName+Constant.EXCEL).getBytes("gbk"),"iso-8859-1"));   
	    
	    byte[] buffer = null;   
        try {   
            File file = new File(request.getRealPath(Constant.EXPORT_PATH_FOLDER)+File.separatorChar+fileName+Constant.EXCEL);   
            FileInputStream fis = new FileInputStream(file);   
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);   
            byte[] b = new byte[1000];   
            int n;   
            while ((n = fis.read(b)) != -1) {   
                bos.write(b, 0, n);   
            }   
            fis.close();   
            bos.close();   
            buffer = bos.toByteArray();   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        }   

	    return new ResponseEntity<byte[]>(buffer,   
	                                      headers, HttpStatus.CREATED);   
	}  
}
