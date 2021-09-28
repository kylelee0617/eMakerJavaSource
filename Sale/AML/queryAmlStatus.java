package Sale.AML;

import jcx.db.talk;
import jcx.jform.bTransaction;

/**
 * 回傳值為 true 表示執行接下來的資料庫異動或查詢
   回傳值為 false 表示接下來不執行任何指令
   傳入值 value 為 "新增","查詢","修改","刪除","列印","PRINT" (列印預覽的列印按鈕),"PRINTALL" (列印預覽的全部列印按鈕) 其中之一
 * @author B04391
 *
 */

public class queryAmlStatus extends bTransaction{
  talk dbSale = null;
  talk db400CRM = null;
  
  public boolean action(String value)throws Throwable{
    dbSale = getTalk("Sale");
    db400CRM = getTalk("400CRM");
    
    if( "查詢".equals(value) ) {
      String customNo = getQueryValue("customNo").trim();
      String customName = getQueryValue("customName").trim();
      
      if("".equals(customNo) && "".equals(customName)) {
        messagebox("錯誤 : 查詢條件為空");
        return false;
      }
      
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ");
      sb.append("A.CUSTOMERID , A.CUSTOMERNAME , A.BIRTHDAY ,  MODIFIERNO , MODIFIEDDATE , L.CONTROLCLASSIFICATIONNAME , L.CONTROLCLASSIFICATIONCODE , C.NOTECONTECT ");
      sb.append("FROM CRCLNAPF A,CRCLNCPF C,CRCLCLPF L ");
      sb.append("WHERE A.CONTROLLISTNAMECODE=C.CONTROLLISTNAMECODE AND C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE ");
      if( !"".equals(customNo) ) {
        sb.append("AND A.CUSTOMERID='").append(customNo).append("' ");
      }
      if( !"".equals(customName) ) {
        sb.append("AND A.CUSTOMERNAME='").append(customName).append("' ");
      }
      sb.append("ORDER BY CUSTOMERID asc");
      
      String[][] customerInfo = db400CRM.queryFromPool(sb.toString());
      setTableData("AmlStatus",customerInfo);
      message("共 " + customerInfo.length + " 筆結果!!");
      
//      for(int i=0 ; i<customerInfo.length ; i++) {
//        String[] info = customerInfo[i];
//        for(int j=0 ; j<info.length ; j++) {
//          System.out.println(info[j] + "--");
//        }
//      }
      
      setValue("customNo", customNo);
      setValue("customName", customName);
      
      return false;
    }
    
    
    return false;
  }
  public String getInformation(){
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
