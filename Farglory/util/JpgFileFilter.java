package Farglory.util ;
import  java.io.* ;
/**
 * 2012/10/12 KC �ϥ�xlsFileFilter��g�ϥ�
 */
public  class  JpgFileFilter  extends  javax.swing.filechooser.FileFilter {
		// �P�_�ɦW���O�_�����ɦW jpg
		public  boolean  accept(File  obj) {
				String  file  =  ""  ;
				if(obj.getPath(  ).lastIndexOf('.')  >  0) {
						file  =  obj.getPath( ).substring(obj.getPath( ).lastIndexOf('.')+1).toLowerCase( ) ;
				}
				if(file  !=  "") {
						return  file.equals("jpg")  ;
				} else {
						return  obj.isDirectory( )  ;
				}
		}
		// ��ܦb�ɮ׹�ܤ�����ɮ������y�z�r��
		public  String  getDescription( ) {
				return  "JPG File (*.jpg) "; 
		}
}
