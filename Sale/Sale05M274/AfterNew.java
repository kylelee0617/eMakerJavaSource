package Sale.Sale05M274;

import java.util.Hashtable;

import jcx.db.talk;
import jcx.jform.bNotify;
import jcx.util.convert;
import jcx.util.datetime;

public class AfterNew extends bNotify {
  public void actionPerformed(String value) throws Throwable {
    // ����槹 Transaction ��,�|���楻�q�{��
    // �i�ΥH�H�oEmail�q���άO�۰ʦA�B�z�۩wTransaction
    talk dbSale = getTalk("" + get("put_dbSale"));
    String stringSql = "";
    String retData[][] = null;
    String stringProjectID1 = getValue("ProjectID1").trim();
    String stringContractDate = convert.ac2roc(getValue("ContractDate").trim().replaceAll("/", ""));
    String stringContractNo = getValue("ContractNo").trim();
    String stringCompanyCd = getValue("CompanyCd").trim(); // �ק���:20120517 ���u�s��:B3774
    //
    stringSql = "select count(ContractNo) " + "from Sale05M274_FLOWC_HIS " +
    // Start �ק���:20120517 ���u�s��:B3774
    // "where ContractNo='"+stringContractNo+"'";
        "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
    // and �ק���:20120517 ���u�s��:B3774
    retData = dbSale.queryFromPool(stringSql);
    if (Integer.parseInt(retData[0][0]) > 1) {
      return;
    }
    //
    talk dbFE3D = getTalk("" + get("put_dbFE3D"));
    talk dbDoc = getTalk("" + get("put_dbDoc"));
    // Start �ק���:20100625 ���u�s��:B3774
    // Sale.Sale05M27401 exeFun274 = new Sale.Sale05M27401();
    Sale05M27401_New exeFun274 = new Sale05M27401_New();
    // End �ק���:20100625 ���u�s��:B3774
    Sale.Sale05M22701 exeFun227 = new Sale.Sale05M22701();
    //
    int intProjEndPosition = 0;
    if (stringProjectID1.length() > 4) {
      intProjEndPosition = 8;
      // Start �ק���:20100203 ���u�s��:B3774
    } else if (stringProjectID1.length() == 4) {
      intProjEndPosition = 7;
      // End �ק���:20100203 ���u�s��:B3774
      // Start �ק���:20110511 ���u�s��:B3774
      // }else{
    } else if (stringProjectID1.length() == 3) {
      // End �ק���:20110511 ���u�s��:B3774
      // Start �ק���:20100203 ���u�s��:B3774
      // intProjEndPosition = 7;
      intProjEndPosition = 6;
      // End �ק���:20100203 ���u�s��:B3774
      // Start �ק���:20110511 ���u�s��:B3774
    } else {
      intProjEndPosition = 5;
      // End �ק���:20110511 ���u�s��:B3774
    }
    //
    int intKindDay = 0;
    stringSql = "select KindDay " +
    // Start �ק���:20110406 ���u�s��:B3774
    // "from Doc1M010 "+
        "from Doc1M011 " +
        // End �ק���:20110406 ���u�s��:B3774
        "where KindNo='58'";
    retData = dbDoc.queryFromPool(stringSql);
    if (retData.length > 0) {
      intKindDay = Integer.parseInt(retData[0][0]);
    }
    String stringBarCode = getValue("BarCode").trim(); // 1
    String stringCDate = datetime.getToday("yy/mm/dd");
    // Start �ק���:20100204 ���u�s��:B3774
    // Start �ק���:20100603 ���u�s��:B3774
    String stringCTime = datetime.getTime("h:m:s");
    // String stringCTime = datetime.getTime("pm/am h�Im��s��").substring(0,2)+"
    // "+datetime.getTime("h:m:s");
    // End �ק���:20100603 ���u�s��:B3774
    // End �ק���:20100204 ���u�s��:B3774
    String stringEDateTime = datetime.getTime("YYYY/mm/dd h:m:s"); // 4
    String stringPreFinDate = convert.FormatedDate(convert.roc2ac(datetime.dateAdd(stringCDate.replaceAll("/", ""), "d", intKindDay)), "/");
    String stringKindNo = "58";
    String stringKindNoD = stringKindNo; // �ק���:20100625 ���u�s��:B3774
    // Start �ק���:20120517 ���u�s��:B3774
    // String stringComNo = getValue("CompanyCd").trim(); // 7
    String stringComNo = stringCompanyCd; // 7
    // End �ק���:20120517 ���u�s��:B3774
    String stringDepartNo = stringContractNo.substring(0, intProjEndPosition);
    // Start �ק���:20100203 ���u�s��:B3774
    stringSql = "select DEPT_CD " + "from FE3D01 " + "where DEPT_CD='" + stringDepartNo + "'";
    retData = dbFE3D.queryFromPool(stringSql);
    if (retData.length == 0) {
      stringSql = "select RTRIM(DEPT_CD) " + "from FE3D01 " + "where DEPT_CD='" + stringDepartNo.substring(0, stringDepartNo.length() - 1) + "'";
      retData = dbFE3D.queryFromPool(stringSql);
      if (retData.length > 0) {
        stringDepartNo = retData[0][0];
      }
    }
    // End �ק���:20100203 ���u�s��:B3774
    //
    String stringEmployeeNo = getUser();
    String stringDocNo1 = stringDepartNo; // 10
    // Start �ק���:20100621 ���u�s��:B3774
    // String stringDocNo2 = stringContractNo.substring(intProjEndPosition,
    // intProjEndPosition+stringContractDate.length());
    String stringDocNo2 = datetime.getToday("yymm");
    // End �ק���:20100621 ���u�s��:B3774
    String stringDocNo3 = "";
    String stringDocNo = ""; // 13
    // Start �ק���:20120517 ���u�s��:B3774
    // String stringDescript = exeFun274.getDescript(stringContractNo);
    String stringDescript = exeFun274.getDescript(stringContractNo, stringCompanyCd);
    // End �ק���:20120517 ���u�s��:B3774
    String stringOriEmployeeName = exeFun227.getEmpName(stringEmployeeNo);
    String stringLastDepart = stringDepartNo; // 16
    String stringLastDateTime = stringEDateTime;
    String stringInOut = "";
    String stringProjectID = ""; // 19
    stringProjectID1 = "";
    String stringCostID = "";
    String stringRealMoney = ""; // 22
    String stringRemark = "";
    String stringRecordNo = "";
    // �B�z����y����
    // Start �ק���:20170515 ���u�s��:B3774
    if ("CS".equals(stringComNo)) {
      stringComNo = "20";
    }
    // End �ק���:20170515 ���u�s��:B3774
    //
    // Start �ק���:20100120 ���u�s��:B3774
    stringSql = "select isnull(right(convert(char(4),1001+max(DocNo3)),3),'001') " + "from Doc1M030 " + "where KindNo ='58' " +
    // Start �ק���:20100621 ���u�s��:B3774
        "and ComNo='" + stringComNo + "' " + "and hand = 'N' " +
        // End �ק���:20100621 ���u�s��:B3774
        "and DocNo1='" + stringDocNo1 + "' " + "and DocNo2='" + stringDocNo2 + "'";
    retData = dbDoc.queryFromPool(stringSql);
    stringDocNo3 = retData[0][0];
    // End �ק���:20100120 ���u�s��:B3774
    stringDocNo = stringDocNo1 + stringDocNo2 + stringDocNo3;
    //
    exeFun274.doInsertDoc1M030(stringBarCode, stringCDate, stringCTime, // 1
        // Start �ק���:20100625 ���u�s��:B3774
        // stringEDateTime, stringPreFinDate, stringKindNo, // 4
        stringEDateTime, stringPreFinDate, stringKindNo, stringKindNoD, // 4
        // End �ק���:20100625 ���u�s��:B3774
        stringComNo, stringDepartNo, stringEmployeeNo, // 8
        stringDocNo1, stringDocNo2, stringDocNo3, // 11
        stringDocNo, stringDescript, stringOriEmployeeName, // 14
        stringLastDepart, stringLastDateTime, stringInOut, // 17
        stringProjectID, stringProjectID1, stringCostID, // 20
        stringRealMoney, stringRemark, stringRecordNo); // 23
    //
    exeFun274.doInsertDoc1M040(stringBarCode, stringCDate, stringCTime, stringEDateTime, stringDepartNo, stringEmployeeNo);
    //
    String stringStatus = getValue("Status").trim();
    if ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)) {
      stringProjectID1 = getValue("ProjectID1").trim();
      stringContractDate = getValue("ContractDate").trim();
      String stringCashInDate = getValue("CashInDate").trim();
      stringSql = "select HouseCar, Position " + "from Sale05M278 " +
          "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
      retData = dbSale.queryFromPool(stringSql);
      for (int intRow = 0; intRow < retData.length; intRow++) {
        stringSql = "UPDATE A_Sale " + "SET ContrDate=convert(datetime , '" + stringContractDate + "' , 21), " + "DateRange=convert(datetime , '" + stringCashInDate + "' , 21) "
            + "WHERE ID1=(SELECT ID1 " + "FROM A_Sale " +
            "WHERE ProjectID1='" + (stringProjectID1.equals("H38A") ? "H38" : stringProjectID1) + "' ";
        if ("House".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Position'" + "AND Position='" + retData[intRow][1] + "')";
        } else if ("Car".equals(retData[intRow][0])) {
          stringSql = stringSql + "AND HouseCar='Car'" + "AND Car='" + retData[intRow][1] + "')";
        }
        dbSale.execFromPool(stringSql);
      }
    }
    
    // �~�����v�ˮ�
    getButton("RenewRelated").doClick();  //21-05 Kyle : ��s�D�n�Ȥ�P���p�H
    
    setValue("text11", "�s�W");
    getButton("AML").doClick();
    
    getButton("CheckRiskNew2021").doClick(); // �p�⭷�I��

    // Start �ק���:20091223 ���u�s��:B3774
    Hashtable hash = new Hashtable();
    hash.put("table17.CmpContractNo", getValue("ContractNo").trim());
    action(2, hash);
    //
    getButton("btnReSizeFileNameField").doClick(); // �ק���:20091225 ���u�s��:B3774
    //
    // End �ק���:20091223 ���u�s��:B3774
    return;
  }

  public String getInformation() {
    return "---------------\u627f\u8fa6.Notify()----------------";
  }
}
