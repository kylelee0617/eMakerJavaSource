package Sale.Sale05M274;

import jcx.db.talk;
import jcx.jform.bNotify;

public class AfterMod extends bNotify {
  public void actionPerformed(String value) throws Throwable {
    // ����槹 Transaction ��,�|���楻�q�{��
    // �i�ΥH�H�oEmail�q���άO�۰ʦA�B�z�۩wTransaction
    // Start �ק���:20100625 ���u�s��:B3774
    // Sale.Sale05M27401 exeFun274 = new Sale.Sale05M27401();
    Sale05M27401_New exeFun274 = new Sale05M27401_New();
    // End �ק���:20100625 ���u�s��:B3774
    String stringBarCode = getValue("BarCode").trim();
    //
    if (!exeFun274.blnIsDispatch(stringBarCode)) { // �����o��~�i�H�ק綠�夺�e
      String stringContractNo = getValue("ContractNo").trim();
      String stringCompanyCd = getValue("CompanyCd").trim(); // �ק���:20120517 ���u�s��:B3774
      // Start �ק���:20120517 ���u�s��:B3774
      // String stringDescript = exeFun274.getDescript(stringContractNo);
      String stringDescript = exeFun274.getDescript(stringContractNo, stringCompanyCd);
      // End �ק���:20120517 ���u�s��:B3774
      exeFun274.doModifyDoc1M030(stringBarCode, stringDescript);
      getButton("btnLoadDocData").doClick();
    }
    // Start �ק���:20091224 ���u�s��:B3774
    // Start �ק���:20100108 ���u�s��:B3774
    String stringStatus = getValue("Status").trim();
    // Start �ק���:20100317 ���u�s��:B3774
    // if("N".equals(stringStatus)){
    // Start �ק���:20151123 ���u�s��:B3774
    // if("N".equals(stringStatus) || "A".equals(stringStatus)){
    if ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)) {
      // End �ק���:20151123 ���u�s��:B3774
      // End �ק���:20100317 ���u�s��:B3774
      // End �ק���:20100108 ���u�s��:B3774
      talk dbSale = getTalk("" + get("put_dbSale"));
      String stringContractNo = getValue("ContractNo").trim();
      String stringCompanyCd = getValue("CompanyCd").trim(); // �ק���:20120517 ���u�s��:B3774
      String stringProjectID1 = getValue("ProjectID1").trim();
      String stringContractDate = getValue("ContractDate").trim();
      String stringCashInDate = getValue("CashInDate").trim();
      String stringSql = "";
      String retData[][] = null;
      stringSql = "select HouseCar, Position " + "from Sale05M278 " +
      // Start �ק���:20120517 ���u�s��:B3774
      // "where ContractNo='"+stringContractNo+"'";
          "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
      // End �ק���:20120517 ���u�s��:B3774
      retData = dbSale.queryFromPool(stringSql);
      for (int intRow = 0; intRow < retData.length; intRow++) {
        stringSql = "UPDATE A_Sale " + "SET ContrDate=convert(datetime , '" + stringContractDate + "' , 21), " + "DateRange=convert(datetime , '" + stringCashInDate + "' , 21) "
            + "WHERE ID1=(SELECT ID1 " + "FROM A_Sale " +
            // Start �ק���:20100412 ���u�s��:B3774
            // "WHERE ProjectID1='"+stringProjectID1+"' ";
            "WHERE ProjectID1='" + (stringProjectID1.equals("H38A") ? "H38" : stringProjectID1) + "' ";
        // End �ק���:20100412 ���u�s��:B3774
        if ("House".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Position'" + "AND Position='" + retData[intRow][1] + "')";
        } else if ("Car".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Car'" + "AND Car='" + retData[intRow][1] + "')";
        }
        dbSale.execFromPool(stringSql);
      }
    } // �ק���:20100108 ���u�s��:B3774
    // End �ק���:20091224 ���u�s��:B3774
    //
    getButton("btnReSizeFileNameField").doClick(); // �ק���:20091225 ���u�s��:B3774
    //
    // Start �ק���:20091127 ���u�s��:B3774
    setValue("ContractNoDB", getValue("ContractNo").trim());
    setValue("CompanyCdDB", getValue("CompanyCd").trim()); // �ק���:20120517 ���u�s��:B3774
    setValue("ContractDateDB", getValue("ContractDate").trim());
    // End �ק���:20091127 ���u�s��:B3774
    setValue("TransferDateDB", getValue("TransferDate").trim()); // �ק���:20150811 ���u�s��:B3774

    // �~������LOG
    getButton("RenewRelated").doClick(); // 21-05 Kyle : ��s�D�n�Ȥ�P���p�H

    setValue("text11", "�ק�");
    getButton("AML").doClick();

    getButton("CheckRiskNew2021").doClick(); // �p�⭷�I��

    return;
  }

  public String getInformation() {
    return "---------------update_trigger()----------------";
  }
}
