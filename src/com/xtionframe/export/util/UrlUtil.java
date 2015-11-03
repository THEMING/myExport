package com.xtionframe.export.util;

import java.util.ArrayList;
import java.util.List;

import com.xtionframe.export.entity.DataBaseType;
import com.xtionframe.export.entity.PoolDataSourceConfig;
import com.xtionframe.export.entity.PoolDataSourceConfig.PoolType;

public class UrlUtil {
	
	public static List<PoolDataSourceConfig> createPoolDataSourceConfigList(String urls){
		List<PoolDataSourceConfig> configs = new ArrayList<PoolDataSourceConfig>();
		String[] addressArr = urls.split("\\|\\|");
		String[] writeAddress = addressArr[0].split("\\;");
		String[] readAddress = addressArr[1].split("\\;");
		 
		configs.add(createPoolDataSourceConfig(readAddress, PoolType.READ));
		configs.add(createPoolDataSourceConfig(writeAddress, PoolType.WRITE));
	
		return configs;
	}
	
	private static PoolDataSourceConfig createPoolDataSourceConfig(String[] address, PoolType poolType){
		PoolDataSourceConfig config = new PoolDataSourceConfig();
		config.setMaxActive(5);
		config.setUrl(address[0]);
		config.setPoolType(poolType);
		config.setValidationQueryTimeout(15);
		config.setUserName(address[1].replace("userid=", ""));
		config.setPassword(address[2].replace("password=", ""));
		setDriverClassName(config.getUrl(), config);
		
		String validationQuery = "select 1";
		if(address.length > 3) {
			if(address[3]!=null&&!address[3].isEmpty()){
				validationQuery = address[3];	
			}
		}
		config.setValidationQuery(validationQuery);
		
		return config;
	}
	
	private static void setDriverClassName(String jdbcUrl, PoolDataSourceConfig config) {
		if(jdbcUrl.indexOf(DataBaseType.Postgresql.getDriverURL()) != -1){
			config.setDbType(DataBaseType.Postgresql);
			config.setDriverClassName(DataBaseType.Postgresql.getDriverName());
		} else if(jdbcUrl.indexOf(DataBaseType.SqlServer.getDriverURL()) != -1){
			config.setDbType(DataBaseType.SqlServer);
			config.setDriverClassName(DataBaseType.SqlServer.getDriverName());
			if(!jdbcUrl.contains("DatabaseName")){
				String[] urls = jdbcUrl.split("/");
				config.setUrl(urls[0]+"//"+urls[2]+";DatabaseName="+urls[3]);
			}
		} else if(jdbcUrl.indexOf(DataBaseType.SqlServer2000.getDriverURL()) != -1){
			config.setDbType(DataBaseType.SqlServer2000);
			config.setDriverClassName(DataBaseType.SqlServer2000.getDriverName());
			if(!jdbcUrl.contains("DatabaseName")){
				String[] urls = jdbcUrl.split("/");
				config.setUrl(urls[0]+"//"+urls[2]+";DatabaseName="+urls[3]);
			}
		} else if(jdbcUrl.indexOf(DataBaseType.Oracle.getDriverURL()) != -1){
			config.setDbType(DataBaseType.Oracle);
			config.setDriverClassName(DataBaseType.Oracle.getDriverName());
		} else if(jdbcUrl.indexOf(DataBaseType.Mysql.getDriverURL()) != -1) {
			config.setDbType(DataBaseType.Mysql);
			config.setDriverClassName(DataBaseType.Mysql.getDriverName());
		} else{
			throw new IllegalArgumentException("Illegal jdbc url:" + jdbcUrl);			
		}	

	}

}
