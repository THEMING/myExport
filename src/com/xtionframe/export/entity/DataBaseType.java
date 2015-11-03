/*
 * Copyright (c) 2014 by XuanWu Wireless Technology Co.Ltd. 
 *             All rights reserved                         
 */
package com.xtionframe.export.entity;

/**
 * @author <a href="hw86xll@163.com">Wei.Huang</a>
 * @Date 2014年6月19日
 * @Version 1.0.0
 */
public enum DataBaseType {
	SqlServer {
		@Override
		public String getDriverName() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:sqlserver://";
		}

		@Override
		public int getValue() {
			return 1;
		}
	},
	SqlServer2000 {

		@Override
		public String getDriverName() {
			return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:microsoft:sqlserver://";
		}
		
		@Override
		public int getValue() {
			return 10;
		}
	},
	Oracle {
		@Override
		public String getDriverName() {
			return "oracle.jdbc.driver.OracleDriver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:oracle:thin:@";
		}

		@Override
		public int getValue() {
			return 6;
		}
	},
	Postgresql {
		@Override
		public String getDriverName() {
			return "org.postgresql.Driver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:postgresql://";
		}

		@Override
		public int getValue() {
			return 9;
		}
	},
	Mysql {
		@Override
		public String getDriverName() {
			return "com.mysql.jdbc.Driver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:mysql://";
		}

		@Override
		public int getValue() {
			return 8;
		}
	},
	DB2 {
		@Override
		public String getDriverName() {
			return "com.ibm.db2.jcc.DB2Driver";
		}

		@Override
		public String getDriverURL() {
			return "jdbc:db2://";
		}

		@Override
		public int getValue() {
			return 7;
		}
	};

	/**
	 * 得到数据库类型的整数值
	 * 
	 * @return
	 */
	public abstract int getValue();

	/**
	 * 得到数据库类型的驱动类名称
	 * 
	 * @return
	 */
	public abstract String getDriverName();

	/**
	 * 得到数据库类型的连接URL
	 * 
	 * @return
	 */
	public abstract String getDriverURL();

	/**
	 * 根据值得到数据库的类型
	 * 
	 * @param value
	 * @return
	 */
	public static DataBaseType getType(int value) {
		for (DataBaseType type : DataBaseType.values()) {
			if (type.getValue() == value)
				return type;
		}
		throw new IllegalArgumentException("Illegal db type:" + value);
	}
}
