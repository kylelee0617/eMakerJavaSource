package Farglory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jcx.jform.sproc;

public class FileControl extends sproc{
  public String getDefaultValue(String value)throws Throwable{
    return value;
  }
  
  public static void main(String[] args) {
  }
  
  public static List getFiles2(File file) {
    List rsList = new ArrayList();  
    listDir2(file, rsList);
    return rsList;
  }
  
  public static void listDir2(File file, List rsList) {
    boolean isDir = false;
    if (file.isDirectory()) { // 是一個目錄
      System.out.println("是目錄");
      // 列出目錄中的全部內容
      File results[] = file.listFiles();
      if (results != null) {
        for (int i = 0; i < results.length; i++) {
          listDir2(results[i], rsList); // 繼續一次判斷
        }
      }
    }
    
    // 獲取完整路徑
    if(file.isFile()) {
      rsList.add(file);
    }
  }

  /**
   * 拷貝檔案到指定目錄
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