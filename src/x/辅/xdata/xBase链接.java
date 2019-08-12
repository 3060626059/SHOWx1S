package x.辅.xdata;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class xBase链接 {
	
	private Connection xConnection=null;
	private Statement xStatement=null;
	private String dbF=null;    //路径
	
	private boolean x状态=false;    //文件 是否 属于数据库     只在清单初始化中被操作
	public static ArrayList<String> x错误容器=new ArrayList<String>(610);    //名字不好,能做多好呢
	
	
	
	public xBase链接(){}
	public xBase链接(File file) {
		super();
		x更换链接(file);
	}
	public xBase链接(String dbF) {
		super();
		x更换链接(dbF);
	}
	public void x更换链接(File file) {
		this.dbF=file.toString();
		x链接初始化();
	}
	public void x更换链接(String string) {
		this.dbF=string;
		x链接初始化();
	}
	
	private void x链接初始化() {
		    if (xConnection!=null) {
				      try {
						xConnection.close();
					       } catch (SQLException e) {}
			}
		    if (xStatement!=null) {
				       try {
						  xStatement.close();
					        } catch (SQLException e) {}
			}
		    
		    String tmpF="jdbc:sqlite:"+dbF;
		    
		    try {
				Class.forName("org.sqlite.JDBC");
				xConnection=DriverManager.getConnection(tmpF);
			} catch (ClassNotFoundException e) {} 
		      catch (SQLException e) {
				System.out.println("无效 路径/数据库 :"+e.getMessage()+"\n"+dbF);
			  }   
		    
	}
	
	private void x清单初始化(boolean xboolean) {
		if (xConnection==null) {
			x链接初始化();
		}
		if (xStatement!=null) {
			try {
				xStatement.close();
			} catch (SQLException e) {
			}
			xStatement=null;
		}
		
		try {
			xConnection.setAutoCommit(xboolean);//设置是否自动生效
			xStatement=xConnection.createStatement();
			ResultSet tmpResultSet = xStatement.executeQuery("select * from sqlite_master");
			                tmpResultSet.close();
			x状态=true;
		   } catch (SQLException e) {
			//必须进行一次设计操作,才会出现真实效果
			x状态=false;//告诉后面方法,不需要再调用清单初始化. 文件并不是数据库
			x错误容器.add(dbF);
			try {
				xStatement.close();   //为了后面代码不乱来,直接关闭
				xStatement=null;
			     } catch (SQLException e1) {}
		}
		
	}
	
	public ResultSet x数据库查询(String SQL) {
		//我担心会有浪费资源的嫌疑
		//每次插入语句都重新创建清单,只插一句也要重新创建清单
		ResultSet resultSet=null;
		x清单初始化(false);
		if (x状态) {
			try {
				resultSet=xStatement.executeQuery(SQL);
			     } catch (SQLException e) {return null;}
			return resultSet;
		}else {
			return null;
		}
		
	}
	public void x数据库插入(String SQL) {
		x清单初始化(true);
		//初始化后的状态就一定不是前一个连接的状态
		//属于本次连接的效果
		if (x状态) {
			try {
				xStatement.execute(SQL);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	public void x数据库插入(ArrayList<String> SQL) {
		try {
			x清单初始化(false);
			
			if (x状态) {
				for (String string : SQL) {
					xStatement.addBatch(string);
				}
				xStatement.executeBatch();
				xConnection.commit();
				xStatement.clearBatch();
				xConnection.setAutoCommit(true);
			}
			
		} catch (SQLException e) {}
	}
	public void close() {
		if (xStatement!=null) {
			try {
				xStatement.close();
			} catch (SQLException e) {}
		}
		if (xConnection!=null) {
			try {
				xConnection.close();
			} catch (SQLException e) {}
		}
	}
	
	/**          构造方法1       构造方法2
	 *                  |                     |
	 *                  |                     |
	 *            更换链接1       更换链接2
	 *                        \        /
	 *                          \    /
	 *                            \/
	 *                          dbF
	 *                            |
	 *                            |
	 *              Connection连接操作-------无法通过异常 被动得到失败状态
	 *                            
	 *                            
	 *                    xStatement初始化
	 *                            |
	 *                            |
	 *                            Connection连接操作
	 *                            
	 *                             
	 *                查询     插入1     插入2                                          
	 *                   \          |          /                          
	 *                     \        |        / 
	 *                       \      |      /  
	 *                         \    |    /
	 *                xStatement初始化----------无法通过异常 被动得到失败状态//没有实际操作之前不会触发异常
	 *                                                                                                         \\这个想法(通过异常判断是否属于数据库的文件)
	 *                                                                                                            是不可靠
	 *                
	 *             
	 *            
	 *            
	 * 
	 * */
	
	
	/*  为什么要重新设计数据库连接类能
	 * 
	 *   因为前面的连接类多多少少有缺陷,直接修改好麻烦
	 *   直接写个新的
	 *   
	 *   x数据库链接  :  我要的基本功能都完成,命名也有规范
	 *                          缺点,对于不成功的连接应该有一些操作与留存记录
	 *   xdb  :  实在是过于完美,我只要功能,不想要完美
	 * 
	 * 
	 * 
	 * */
	
	
	/*
	 *   样子 : 是 xdb 命名  
	 *             添加功能  错误连接记录
	 *   
	 *   结构 : x数据库链接
	 * 
	 *   大部分数据库操作是在 Statement 上的
	 *   一部分是在Connection
	 *   
	 *   分开操作
	 *   参数:|  1路径
	 *          |  2连接标志
	 *          |  3"错误连接列表" 容器
	 *          |
	 *    以后补充
	 *   
	 *   
	 *          传入连接   
	 *              ||
	 *              ||
	 *         Connection操作{连接状态:成功/失败}
	 *                     ||
	 *                     ||
	 *                Statement操作{开关}
	 *                          ||
	 *                          ||
	 *                         1.查询
	 *                         2.插入
	 *                         
	 *                         
	 * **/
	
	
}
