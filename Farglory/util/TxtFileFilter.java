package Farglory.util ;
import  java.io.* ;
/**
 * 2013/07/19 KC 使用xlsFileFilter改寫使用
 */
public  class  TxtFileFilter  extends  javax.swing.filechooser.FileFilter {
		// 判斷檔名中是否有副檔名 Txt
		public  boolean  accept(File  obj) {
				String  file  =  ""  ;
				if(obj.getPath(  ).lastIndexOf('.')  >  0) {
						file  =  obj.getPath( ).substring(obj.getPath( ).lastIndexOf('.')+1).toLowerCase( ) ;
				}
				if(file  !=  "") {
						return  file.equals("txt")  ;
				} else {
						return  obj.isDirectory( )  ;
				}
		}
		// 顯示在檔案對話方塊的檔案類型描述字串
		public  String  getDescription( ) {
				return  "TXT File (*.txt) "; 
		}
}
