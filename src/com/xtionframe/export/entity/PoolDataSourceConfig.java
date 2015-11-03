/*
 * Copyright (c) 2014 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xtionframe.export.entity;


/**
 * 数据库连接池参数配置
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2014年6月18日
 * @Version 1.0.0
 */
public class PoolDataSourceConfig {
	
	public enum PoolType{
		READ(1), WRITE(2), READ_AND_WRITE(3);
		
		private final int index;
		
		private PoolType(int index){
			this.index = index;
		}
		
		public int getIndex(){
			return index;
		}
		
		public static PoolType getType(int index){
			switch(index){
			case 1:
				return READ;
			case 2:
				return WRITE;
			case 3:
				return READ_AND_WRITE;
			default:
				return READ_AND_WRITE;
			}
		}
	}
	
	private int maxActive;
	private int maxIdle;
	
	private boolean testOnBorrow;
	
	private String ip;
	private String dbName;
	
	private String url;
	private String userName;
	private String password;
	private String driverClassName;
	private String validationQuery;
	private String connectionProperties;
	
	private int validationQueryTimeout;
	
	private PoolType poolType;
	private DataBaseType dbType;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getValidationQuery() {
		return validationQuery;
	}

	public void setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
	}

	public String getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(String connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getValidationQueryTimeout() {
		return validationQueryTimeout;
	}

	public void setValidationQueryTimeout(int validationQueryTimeout) {
		this.validationQueryTimeout = validationQueryTimeout;
	}

	public PoolType getPoolType() {
		return poolType;
	}

	public void setPoolType(PoolType poolType) {
		this.poolType = poolType;
	}

	public DataBaseType getDbType() {
		return dbType;
	}

	public void setDbType(DataBaseType dbType) {
		this.dbType = dbType;
	}
}
