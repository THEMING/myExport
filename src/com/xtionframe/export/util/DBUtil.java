package com.xtionframe.export.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.xtionframe.export.entity.PoolDataSourceConfig;
import com.xtionframe.export.entity.PoolDataSourceConfig.PoolType;

public class DBUtil {
	private static Map<String, JdbcTemplate> factoryMap = new ConcurrentHashMap<String, JdbcTemplate>();
	
	public static void buildSessionFactory(String urls){
		List<PoolDataSourceConfig> configs = UrlUtil.createPoolDataSourceConfigList(urls);
		for(PoolDataSourceConfig config : configs){	
			BasicDataSource entDataSource = new BasicDataSource();
			entDataSource.setDriverClassName(config.getDriverClassName());
			entDataSource.setUrl(config.getUrl());
			entDataSource.setUsername(config.getUserName());
			entDataSource.setPassword(config.getPassword());
			entDataSource.setValidationQuery(config.getValidationQuery());
			if(config.getConnectionProperties()!=null&&!config.getConnectionProperties().isEmpty())
				entDataSource.setConnectionProperties(config.getConnectionProperties());
			entDataSource.setTestOnBorrow(true);
			entDataSource.setMaxActive(config.getMaxActive());
			entDataSource.setValidationQueryTimeout(config.getValidationQueryTimeout());
			entDataSource.setRemoveAbandoned(true);
			entDataSource.setDefaultAutoCommit(true);
		
			String key = tran2Key(urls, config.getPoolType());
			JdbcTemplate jt = new JdbcTemplate(entDataSource);
			factoryMap.put(key, jt);
	    }
	}
	
	private static String tran2Key(String urls,PoolType poolType){
		StringBuilder key = new StringBuilder();
		key.append(urls).append("_");
		key.append(poolType.getIndex());
		return key.toString();
	}
	
	public static JdbcTemplate getReadJdbcTemplate(String urls){
		buildSessionFactory(urls);
		Object jt = MapUtils.getObject(factoryMap, tran2Key(urls,PoolType.READ),null);
		if(jt!=null){
			return (JdbcTemplate)jt;
		}
		return null;
	}
	
	public static JdbcTemplate getWriteJdbcTemplate(String urls){
		buildSessionFactory(urls);
		Object jt = MapUtils.getObject(factoryMap, tran2Key(urls,PoolType.WRITE),null);
		if(jt!=null){
			return (JdbcTemplate)jt;
		}
		return null;
	}

}
