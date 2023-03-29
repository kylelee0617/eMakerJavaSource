package Farglory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jcx.jform.sproc;

public class FileControl extends sproc {
  public String getDefaultValue(String value) throws Throwable {
    return value;
  }

  public static void main(String[] args) {
    File file = new File("D:\\oldDir");

    File testFile = new File("G:\\kyleTest\\Transfer\\Trans144");
    List aa = getFiles2(testFile);
    System.out.println("aa:" + aa.size());
  }

  public static List getFiles2(File file) {
    List rsList = new ArrayList();
    listDir2(file, rsList);
    return rsList;
  }

  public static void listDir2(File file, List rsList) {
    boolean isDir = false;
    
    if (file.isDirectory()) { // �O�@�ӥؿ�
      System.out.println("�ؿ�...");
      // �C�X�ؿ������������e
      File results[] = file.listFiles();
      if (results != null) {
        for (int i = 0; i < results.length; i++) {
          //listDir2(results[i], rsList); // �~��@���P�_
          rsList.add(results[i]);
        }
      }
    } else if (file.isFile()) {
      rsList.add(file);
    }
    
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