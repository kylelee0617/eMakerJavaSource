package Farglory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestFile {
  public static void main(String[] args) {
    File file = new File("D:\\oldDir");
    listDir(file); // �C�X��e���|�U���Ҧ��ɮץH�θ�Ƨ����|
  }

  /**
   * �C�X��e���|�U���Ҧ��ɮ׸��|
   * 
   * @param file
   */
  public static void listDir(File file) {
    String newPath = "D:\\newDir";
    if (file.isDirectory()) { // �O�@�ӥؿ�
      // �C�X�ؿ������������e
      File results[] = file.listFiles();
      if (results != null) {
        for (int i = 0; i < results.length; i++) {
          listDir(results[i]); // �~��@���P�_
        }
      }
    } else { // �O�ɮ�
      String fileStr = (file.getName()).toString();
      String fileFormat = "JPG,PNG,MP4";
      String suffixStr = "";
      if (null != fileStr && !"".equals(fileStr)) {
        suffixStr = fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length());
        if (fileFormat.indexOf(suffixStr.toUpperCase()) != -1) {
          newPath = newPath + "/" + fileStr;
          copyFile(file, newPath);
        }
      }
    }
    // file.delete(); //�R��!!!!! �ڥؿ�,�V�ާ@
    // ���������|
    System.out.println(file);
  }

  /**
   * �����ɮר���w�ؿ�
   * 
   * @param oldFile
   * @param newPath
   */
  public static void copyFile(File oldFile, String newPath) {
    try {
      if (oldFile.isFile()) {
        FileInputStream input = new FileInputStream(oldFile);
        FileOutputStream output = new FileOutputStream(newPath);
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = input.read(b)) != -1) {
          output.write(b, 0, len);
        }
        output.flush();
        output.close();
        input.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}