package com.xtionframe.export.service;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class test {

	@RequestMapping(value="/test",method = RequestMethod.POST)
	public String test(@RequestBody Map<String,String> map){
		return MapUtils.getString(map, "test");
	}
}
