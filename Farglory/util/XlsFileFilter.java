package Farglory.util ;
import  java.io.* ;
public  class  XlsFileFilter  extends  javax.swing.filechooser.FileFilter {
		// �P�_�ɦW���O�_�����ɦW xls
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
		// ��ܦb�ɮ׹�ܤ�����ɮ������y�z�r��
		public  String  getDescription( ) {
				return  "XLS File (*.xls) " ;
		}
}
