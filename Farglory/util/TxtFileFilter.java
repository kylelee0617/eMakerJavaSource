package Farglory.util ;
import  java.io.* ;
/**
 * 2013/07/19 KC �ϥ�xlsFileFilter��g�ϥ�
 */
public  class  TxtFileFilter  extends  javax.swing.filechooser.FileFilter {
		// �P�_�ɦW���O�_�����ɦW Txt
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
		// ��ܦb�ɮ׹�ܤ�����ɮ������y�z�r��
		public  String  getDescription( ) {
				return  "TXT File (*.txt) "; 
		}
}
