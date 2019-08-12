package x.主;

import java.io.File;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import x.辅.xdata.xBase链接;
import x.辅.xdb.xData;
import x.辅.xtool.xFild;

public class 测试6 {

	/*
	 * 测试 Xbase 功能是否正常
	 * 
	 * 测试完毕 符合设计
	 * 
	 * 集数据
	 * 
	 * 查看数据
	 * 
	 * 2019年5月19日 13:28:13
	 * 
	 * 
	 */
	public static void main(String[] args) {
		String xSQL_sqlite_master="select * from sqlite_master";
		String xSQL_wifiConfig="select * from wifiConfig";
		String xSQL_wifiConfig_ssid_psk="select ssid,psk from wifiConfig";
		
		ArrayList<File> xarray = xFild.xFileS1(
				new File("C:\\Users\\lenovo\\Desktop\\手机备份\\HuaweiBackup\\backupFiles\\"), 
				"wifiConfig");

		//File xFileOUT = new File("C:\\Users\\lenovo\\Desktop\\wifiConfig.vbm");
		File xFileOUT=new File("C:\\Users\\lenovo\\Desktop\\手机备份\\HuaweiBackup\\backupFiles\\2019-04-06_21-39-13\\wifiConfig.db");
		
		xData x=new xData();
		
		xBase链接 xBase链接=new xBase链接();
		
		       //找文件
				for (int i = 0; i < xarray.size(); i++) {
					File xfile = xarray.get(i);
					xBase链接.x更换链接(xfile);
					x.xAddResultSet结果追加( xBase链接.x数据库查询(xSQL_wifiConfig) );
				}
			
				xBase链接.x更换链接(xFileOUT);
				xBase链接.x数据库插入("create table if not exists wifiConfig (hiddenSSID TEXT, GateWay TEXT, Port TEXT, DNS1 TEXT, ssid TEXT, wep_key0 TEXT, Host TEXT, DNS2 TEXT, key_mgmt TEXT, ExclusionList TEXT, IPAddress TEXT, NetworkPrefixLength TEXT, psk TEXT )");
				x.xRemoveDuplicate重复移除(xBase链接.x数据库查询(xSQL_wifiConfig));
				xBase链接.x数据库插入(x.xSQL语法("wifiConfig"));
				          
				           ResultSet tmpresultset = xBase链接.x数据库查询(xSQL_wifiConfig_ssid_psk);
				           
				           ResultSetMetaData metadata=null; 
				           try {
							    metadata= tmpresultset.getMetaData();
							    String xColmn[]=new String[metadata.getColumnCount()];
							    for (int i = 1; i <= xColmn.length; i++) {
									xColmn[i-1]=metadata.getColumnName(i);
									//System.out.print(xColmn[i-1]+"     ");
								}
							    while (tmpresultset.next()) {
							    	System.out.println();
									for (String string : xColmn) {
										Object obj = tmpresultset.getObject(string);
										System.out.print(obj+"     ");
									   }
								}
							    
							    tmpresultset.close();
						      } catch (SQLException e) {}
				       
			    xBase链接.close();
				
				
				
				
				
			
	}

}
