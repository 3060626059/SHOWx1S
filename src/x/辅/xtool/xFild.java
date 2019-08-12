package x.辅.xtool;

import java.io.File;
import java.util.ArrayList;

public class xFild {
	/**
	 * 内部类
	 * 
	 * 不想在方法外部创建数据容器
	 * 
	 * 不想把数据放在外部
	 * 
	 * 方法自己调用自己,自己发布任务给自己
	 * 
	*/
  public static ArrayList<File> xFileS1(File f,String keyword) {
	  
	  ArrayList<File> xArrayList=new ArrayList<File>(89);
	  
	  class x循环{
		 public void x循环s(File xFile) {
			 if (xFile.isDirectory()) {
				File[] xfS = xFile.listFiles();
				for (File file : xfS) {
					if (file.isFile()) {
						if (file.getName().contains(keyword)) {
							xArrayList.add(file);
						}
					}else {
						x循环s(file);
					}
				}
			}
			 
		}
		 
	  }
	  
	  x循环 x循环=new x循环();
	  x循环.x循环s(f);

	return xArrayList;
  }
  
  public static ArrayList<String> xFileS2(File f,String keyword) {
	  ArrayList<String> xArrayList=new ArrayList<String>(89);
	  
	  class x循环{
		 public void x循环s(File xFile) {
			if (xFile.isDirectory()) {
				File[] xfS = xFile.listFiles();
				for (File file:xfS) {
					if (file.isFile()) {
						if (file.getName().contains(keyword)) {
							xArrayList.add(file.toString());
						}
					}else {
						x循环s(file);
					}
				}
			}
		}
	  }
	  
	  x循环 x循环=new x循环();
	  x循环.x循环s(f);
	  
	  return xArrayList;	
  }
  
}
