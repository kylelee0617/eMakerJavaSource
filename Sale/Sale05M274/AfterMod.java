package Sale.Sale05M274;

import jcx.db.talk;
import jcx.jform.bNotify;

public class AfterMod extends bNotify {
  public void actionPerformed(String value) throws Throwable {
    // 當執行完 Transaction 時,會執行本段程式
    // 可用以寄發Email通知或是自動再處理自定Transaction
    // Start 修改日期:20100625 員工編號:B3774
    // Sale.Sale05M27401 exeFun274 = new Sale.Sale05M27401();
    Sale05M27401_New exeFun274 = new Sale05M27401_New();
    // End 修改日期:20100625 員工編號:B3774
    String stringBarCode = getValue("BarCode").trim();
    //
    if (!exeFun274.blnIsDispatch(stringBarCode)) { // 未收發文才可以修改公文內容
      String stringContractNo = getValue("ContractNo").trim();
      String stringCompanyCd = getValue("CompanyCd").trim(); // 修改日期:20120517 員工編號:B3774
      // Start 修改日期:20120517 員工編號:B3774
      // String stringDescript = exeFun274.getDescript(stringContractNo);
      String stringDescript = exeFun274.getDescript(stringContractNo, stringCompanyCd);
      // End 修改日期:20120517 員工編號:B3774
      exeFun274.doModifyDoc1M030(stringBarCode, stringDescript);
      getButton("btnLoadDocData").doClick();
    }
    // Start 修改日期:20091224 員工編號:B3774
    // Start 修改日期:20100108 員工編號:B3774
    String stringStatus = getValue("Status").trim();
    // Start 修改日期:20100317 員工編號:B3774
    // if("N".equals(stringStatus)){
    // Start 修改日期:20151123 員工編號:B3774
    // if("N".equals(stringStatus) || "A".equals(stringStatus)){
    if ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)) {
      // End 修改日期:20151123 員工編號:B3774
      // End 修改日期:20100317 員工編號:B3774
      // End 修改日期:20100108 員工編號:B3774
      talk dbSale = getTalk("" + get("put_dbSale"));
      String stringContractNo = getValue("ContractNo").trim();
      String stringCompanyCd = getValue("CompanyCd").trim(); // 修改日期:20120517 員工編號:B3774
      String stringProjectID1 = getValue("ProjectID1").trim();
      String stringContractDate = getValue("ContractDate").trim();
      String stringCashInDate = getValue("CashInDate").trim();
      String stringSql = "";
      String retData[][] = null;
      stringSql = "select HouseCar, Position " + "from Sale05M278 " +
      // Start 修改日期:20120517 員工編號:B3774
      // "where ContractNo='"+stringContractNo+"'";
          "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
      // End 修改日期:20120517 員工編號:B3774
      retData = dbSale.queryFromPool(stringSql);
      for (int intRow = 0; intRow < retData.length; intRow++) {
        stringSql = "UPDATE A_Sale " + "SET ContrDate=convert(datetime , '" + stringContractDate + "' , 21), " + "DateRange=convert(datetime , '" + stringCashInDate + "' , 21) "
            + "WHERE ID1=(SELECT ID1 " + "FROM A_Sale " +
            // Start 修改日期:20100412 員工編號:B3774
            // "WHERE ProjectID1='"+stringProjectID1+"' ";
            "WHERE ProjectID1='" + (stringProjectID1.equals("H38A") ? "H38" : stringProjectID1) + "' ";
        // End 修改日期:20100412 員工編號:B3774
        if ("House".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Position'" + "AND Position='" + retData[intRow][1] + "')";
        } else if ("Car".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Car'" + "AND Car='" + retData[intRow][1] + "')";
        }
        dbSale.execFromPool(stringSql);
      }
    } // 修改日期:20100108 員工編號:B3774
    // End 修改日期:20091224 員工編號:B3774
    //
    getButton("btnReSizeFileNameField").doClick(); // 修改日期:20091225 員工編號:B3774
    //
    // Start 修改日期:20091127 員工編號:B3774
    setValue("ContractNoDB", getValue("ContractNo").trim());
    setValue("CompanyCdDB", getValue("CompanyCd").trim()); // 修改日期:20120517 員工編號:B3774
    setValue("ContractDateDB", getValue("ContractDate").trim());
    // End 修改日期:20091127 員工編號:B3774
    setValue("TransferDateDB", getValue("TransferDate").trim()); // 修改日期:20150811 員工編號:B3774

    // 洗錢防制LOG
    getButton("RenewRelated").doClick(); // 21-05 Kyle : 更新主要客戶與關聯人

    setValue("text11", "修改");
    getButton("AML").doClick();

    getButton("CheckRiskNew2021").doClick(); // 計算風險值

    return;
  }

  public String getInformation() {
    return "---------------update_trigger()----------------";
  }
}
