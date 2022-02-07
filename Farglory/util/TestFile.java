package Farglory.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestFile {
  public static void main(String[] args) {
    File file = new File("D:\\oldDir");
    listDir(file); // 列出當前路徑下的所有檔案以及資料夾路徑
  }

  /**
   * 列出當前路徑下的所有檔案路徑
   * 
   * @param file
   */
  public static void listDir(File file) {
    String newPath = "D:\\newDir";
    if (file.isDirectory()) { // 是一個目錄
      // 列出目錄中的全部內容
      File results[] = file.listFiles();
      if (results != null) {
        for (int i = 0; i < results.length; i++) {
          listDir(results[i]); // 繼續一次判斷
        }
      }
    } else { // 是檔案
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
    // file.delete(); //刪除!!!!! 根目錄,慎操作
    // 獲取完整路徑
    System.out.println(file);
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