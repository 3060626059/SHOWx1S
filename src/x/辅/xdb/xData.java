package x.辅.xdb;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;



import com.alibaba.fastjson.JSONObject;

public class xData {
	/****
	 * 处理数据,防止重复,hashSet容器,传入结果集
	 * 
	 * 
	 * 有两个方法部分功能是相同的
	 * 
	 * 是否需要把相同的一部分独立出来 [是]
	 * 
	 * 
	 * 会出问题的地方太多,一个人无法保证
	 * 
	 * 
	 * 
	 * */
	protected static HashSet<JSONObject> xHashSet容器;

	
	
	
	public xData() {
		super();
		xHashSet容器=new HashSet<JSONObject>(89);//34+55
	}
	
	public xData(int array初始化长度) {
		super();
		xHashSet容器=new HashSet<JSONObject>(array初始化长度);//指定长度
	}
	
	public void xAddResultSet结果追加(ResultSet xResultSet) {
		if (xResultSet!=null) {
			ArrayList<JSONObject> array = xResultSet转化JSON(xResultSet);
			
			for (JSONObject jsonObject : array) {
				xHashSet容器.add(jsonObject);
			}
			
			try {
				xResultSet.close();
			} catch (SQLException e) {}
		}
	}
	private void xDifferenceSet差集(ResultSet xResultSet) {
		ArrayList<JSONObject> xARRAY = xResultSet转化JSON(xResultSet);
		for (JSONObject xjson : xARRAY) {
			xHashSet容器.remove(xjson);
		}
	}
	
	protected ArrayList<JSONObject> xResultSet转化JSON(ResultSet xResultSet){
		ArrayList<JSONObject> xArrayList=new ArrayList<JSONObject>(34);
		ResultSetMetaData xMetaData;
		try {
			 xMetaData = xResultSet.getMetaData();	
			 while (xResultSet.next()) {
				 JSONObject xJson=new JSONObject();
				for (int i = 1; i <= xMetaData.getColumnCount(); i++) {
					//("又是这个0啊");
					//还有 这个等于号
					//她是从一开始取,i 小于 getColumnName 的话
					//会漏掉一列
					String xName=xMetaData.getColumnName(i);
					Object xData = xResultSet.getObject(xName);
					//不进行判空
					xJson.put(xName, xData);
				}
				xArrayList.add(xJson);
			}
			 
		} catch (SQLException e) {}	
		

		return xArrayList;
	}
	
 	public void xRemoveDuplicate重复移除(ResultSet xResultSet) {
		xDifferenceSet差集(xResultSet);
		try {
			xResultSet.close();
		} catch (SQLException e) {
		}
	}
	public ArrayList<String> xSQL语法(String xTable名字) {

		
		ArrayList<String> xArrayList语法=new ArrayList<String>(89);
		
		StringBuilder xtmp=new StringBuilder();
		
		for ( JSONObject xjson1:xHashSet容器) {
		     Iterator<String> xKeyValuse = xjson1.keySet().iterator();
			 Iterator<String> xKeyData = xjson1.keySet().iterator();
			
			xtmp.append("insert into "+xTable名字+" (");
			
			         while (xKeyValuse.hasNext()) {
			        	 String xkey = xKeyValuse.next();
							if (xKeyValuse.hasNext()) {
								xtmp.append(xkey+",");
							}
							
							if (!xKeyValuse.hasNext()) {
								//是最后的数值
								xtmp.append(xkey+")");
							}
						}
						
			          xtmp.append(" Values(");
			
			         while (xKeyData.hasNext()) {
						    //从json取出键相对应的值
			        	    //非null需要加 ''
			        	    //不能多次调用next,会导致迭代器越界
			        	 String xkey = xKeyData.next();
			        	 Object xjsonValues = xjson1.get(xkey);
			        	 
			        	      if (xKeyData.hasNext()) {
			        	    	  //不到最后
			        	    	  if (xjsonValues!=null) {
										xtmp.append("'");
										xtmp.append(xjsonValues);
										xtmp.append("',");
									}else {
										xtmp.append(xjsonValues);
										xtmp.append(",");
									}
								}
						      if (!xKeyData.hasNext()) {
						    	  //这个主要是尾部部分的处理
						    	  if (xjsonValues!=null) {
										xtmp.append("'");
										xtmp.append(xjsonValues);
										xtmp.append("')");
									}else {
										xtmp.append(xjsonValues);
										xtmp.append(")");
									}
							}
					}
			
			
		xArrayList语法.add(xtmp.toString());
		xtmp.delete(0, xtmp.length());
			
			
		}
		
		return xArrayList语法;
	}
	
	@Override
	public String toString() {

         StringBuilder xtmp=new StringBuilder();
		
		for ( JSONObject xjson1:xHashSet容器) {

			 Iterator<String> xKeyData = xjson1.keySet().iterator();
			
			
			         while (xKeyData.hasNext()) {
						    //从json取出键相对应的值
			        	    //非null需要加 ''
			        	 String xkey = xKeyData.next();
			        	 Object xjsonValues = xjson1.get(xkey);
			        	 
			        	      if (xKeyData.hasNext()) {
			        	    	  //不到最后
			        	    	  if (xjsonValues!=null) {
			        	    		    xtmp.append(xkey);
										xtmp.append(" : ");
										xtmp.append(xjsonValues);
										xtmp.append("    ");
									}else {
										xtmp.append(xkey);
									    xtmp.append(xjsonValues);
										xtmp.append("    ");
									}
								}
						      if (!xKeyData.hasNext()) {
						    	  //这个主要是尾部部分的处理
						    	  if (xjsonValues!=null) {
						    		    xtmp.append(xkey);
										xtmp.append(" : ");
										xtmp.append(xjsonValues);
										xtmp.append("\n");
									}else {
										xtmp.append(xKeyData.next());
										xtmp.append(xjsonValues);
										xtmp.append("\n");
									}
							}
					}
			
			
		}
		
		
		
		return xtmp.toString();
	}
	
	
	
	
	
	
	
}
