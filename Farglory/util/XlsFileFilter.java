package Farglory.util ;
import  java.io.* ;
public  class  XlsFileFilter  extends  javax.swing.filechooser.FileFilter {
		// 判斷檔名中是否有副檔名 xls
		public  boolean  accept(File  obj) {
				String  file  =  ""  ;
				if(obj.getPath(  ).lastIndexOf('.')  >  0) {
						file  =  obj.getPath( ).substring(obj.getPath( ).lastIndexOf('.')+1).toLowerCase( ) ;
				}
				if(file  !=  "") {
						return  file.equals("xls")  ;
				} else {
						return  obj.isDirectory( )  ;
				}
		
		}
		// 顯示在檔案對話方塊的檔案類型描述字串
		public  String  getDescription( ) {
				return  "XLS File (*.xls) " ;
		}
}
