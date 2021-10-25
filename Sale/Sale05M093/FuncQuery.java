package Sale.Sale05M093;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;

import Farglory.util.KSqlUtils;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class FuncQuery extends bTransaction {
  public boolean action(String value) throws Throwable {
    if (value.length() <= 0) {
      return false;
    }
    
    KSqlUtils ksUtil = new KSqlUtils();
    String logText = "���W��Ƭd��";
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), logText + ":Start");
    
    message("");
    getButton("button2").setVisible(false);
    getButton("button3").setVisible(false);
    getButton("button4").setVisible(false);
    
    talk dbSale = getTalk("" + get("put_dbSale"));
    String stringTrxDate = getValue("TrxDate").trim();
    String stringOrderNo = getValue("OrderNo").trim();
    String stringProjectID1 = getValue("ProjectID1").trim();
    
    
    String lwk_sqlstmt = "SELECT CustomNo, CustomNoNew, DiscountOpen FROM Sale05M093 WHERE  OrderNo  =  '" + stringOrderNo + "'  AND  TrxDate  =  '" + stringTrxDate + "' ";
    String[][] retSale05M093 = dbSale.queryFromPool(lwk_sqlstmt);
    String[][] ret1 = new String[(retSale05M093.length)][22];
    if (retSale05M093.length <= 0) {
      message("�L���W���");
      return false;
    }
    if (retSale05M093[0][2].equals("")) {
      getButton("button3").setVisible(true);
      getButton("button4").setVisible(true);
    }
    
    int i = 0;
    String[][] retSale05M091 = null;
    for (int k = 0; k < retSale05M093.length; k++) {
      lwk_sqlstmt = "SELECT distinct top 1 OrderNo, RecordNo, '', Nationality, CountryName2, EngNo, EngName, CountryName, CustomNo,  CustomName, Percentage, "
          + "Birthday, MajorName, PositionName, ZIP, City, Town, Address, Cellphone, Tel, Tel2, eMail, IsBlackList, IsControlList, IsLinked "
          + "FROM Sale05M091 "
          + "WHERE OrderNo  =  '" + stringOrderNo + "' AND  CustomNo  =  '" + retSale05M093[k][0] + "' AND TrxDate  =  '" + stringTrxDate + "' "
          + " AND  StatusCd  =  'C' ";
      retSale05M091 = dbSale.queryFromPool(lwk_sqlstmt);
      retSale05M091[0][1] = "" + (k + 1); // No
      ret1[i] = retSale05M091[0];
      i = i + 1;
    }
    setTableData("table1", ret1);

    Vector vectorTable2Data = new Vector();
    String[] ret2 = new String[9];
    
    //OrderNo, RecordNo, '', Nationality, CountryName2, EngNo, EngName, CountryName, CustomNo,  CustomName, Percentage, Birthday, MajorName, PositionName, ZIP, City, Town, Address, 
    //Cellphone, Tel, Tel2, eMail, IsBlackList, IsControlList, IsLinked
    
    lwk_sqlstmt = "SELECT OrderNo, RecordNo, '1', Nationality, CountryName2, EngNo, EngName, CountryName, CustomNo, CustomName, Percentage, Birthday, MajorName, PositionName, ZIP, City, Town, "
        + "Address, Cellphone, Tel, Tel2, eMail, IsBlackList, IsControlList, IsLinked, TrxDate "
        + "FROM Sale05M091 WHERE  OrderNo  =  '" + stringOrderNo + "' "
        + " AND TrxDate  >  '" + stringTrxDate + "' ORDER  BY  TrxDate ";
    retSale05M091 = dbSale.queryFromPool(lwk_sqlstmt);
    
    if (retSale05M091.length == 0) {
      lwk_sqlstmt = "SELECT OrderNo, RecordNo, '1', Nationality, CountryName2, EngNo, EngName, CountryName, CustomNo, CustomName, Percentage, Birthday, MajorName, PositionName, ZIP, City, Town, "
          + "Address, Cellphone, Tel, Tel2, eMail, IsBlackList, IsControlList, IsLinked, TrxDate "
          + " FROM  Sale05M091 WHERE  OrderNo  =  '" + stringOrderNo + "' "
          + " AND  ISNULL(TrxDate,'')  =  '' ORDER  BY  TrxDate ";
      retSale05M091 = dbSale.queryFromPool(lwk_sqlstmt);
    }
    
    String stringTrxDateL = "";
    for (int intNo = 0; intNo < retSale05M091.length; intNo++) {
      ret2 = new String[retSale05M091[intNo].length];
      if (intNo != 0 && !stringTrxDateL.equals(retSale05M091[intNo][25].trim())) {
        break;
      }
      stringTrxDateL = retSale05M091[intNo][25].trim();
      //
      retSale05M091[intNo][1] = "" + (intNo + 1); //NO
      ret2 = retSale05M091[intNo];
      vectorTable2Data.add(ret2);
    }
    setTableData("table2", (String[][]) vectorTable2Data.toArray(new String[0][0]));
    
    //������
    String stringSQL = "select  *  from  SALE05M098   where  ProjectID1  =  '" + stringProjectID1 + "'  and  OrderNo  =  '" + stringOrderNo + "' "
        + " and  TrxDate  =  '" + stringTrxDate + "' ";
    String retSale05M098[][] = dbSale.queryFromPool(stringSQL);
    setTableData("table3", retSale05M098);
    
    //�o��
    stringSQL = "select  *  from  SALE05M099   where  ProjectID1  =  '" + stringProjectID1 + "'  and  OrderNo  =  '" + stringOrderNo + "'  and  TrxDate  =  '"
        + stringTrxDate + "' ";
    String retSale05M099[][] = dbSale.queryFromPool(stringSQL);
    setTableData("table4", retSale05M099);
    
    // �����q�H
    String str091BenSql = " SELECT  RecordNo,(SELECT TOP 1 a.CustomName FROM Sale05M091 a WHERE a.CustomNo=b.CustomNo) AS CustomName, BenName, BCustomNo,Birthday, CountryName, HoldType, IsBlackList,IsControlList,IsLinked "
        + " FROM Sale05M091Ben b WHERE orderNo = '" + getValue("OrderNo") + "' AND  (StatusCd ='' or StatusCd is null) ORDER BY RecordNo";
    String retSale05M091Ben[][] = dbSale.queryFromPool(str091BenSql);
    String[][] ret5 = new String[(retSale05M091Ben.length)][12];
    if (retSale05M091Ben.length > 0) {
      for (int a = 0; a < retSale05M091Ben.length; a++) {
        ret5[a][0] = getValue("OrderNo").trim(); // OrderNo
        ret5[a][1] = "" + (a + 1); // No
        ret5[a][2] = retSale05M091Ben[a][1].trim(); // �q��m�W
        ret5[a][3] = retSale05M091Ben[a][2].trim(); // ����H�m�W
        ret5[a][4] = retSale05M091Ben[a][3].trim(); // �����Ҹ�
        ret5[a][5] = retSale05M091Ben[a][4].trim(); // �ͤ�
        ret5[a][6] = retSale05M091Ben[a][5].trim(); // ��O
        ret5[a][7] = retSale05M091Ben[a][6].trim(); // ��H�O
        ret5[a][8] = retSale05M091Ben[a][7].trim(); // �¦W��
        ret5[a][9] = retSale05M091Ben[a][8].trim(); // ���ަW��
        ret5[a][10] = retSale05M091Ben[a][9].trim(); // �Q�`���Y�H
      }
      setTableData("table5", ret5);
    }
    
    // �N�z�H
    String str091AgentSql = "SELECT RecordNo, (SELECT TOP 1 a.CustomName FROM Sale05M091 a WHERE a.CustomNo=b.CustomNo) AS CustomName,AgentName, ACustomNo, CountryName, AgentRel,AgentReason,IsBlackList,IsControlList,IsLinked "
        + "FROM Sale05M091Agent b WHERE orderNo = '" + getValue("OrderNo") + "' AND  (StatusCd ='' or StatusCd is null) ORDER BY RecordNo";
    String retSale05M091Agent[][] = dbSale.queryFromPool(str091AgentSql);
    String[][] ret6 = new String[(retSale05M091Agent.length)][12];
    if (retSale05M091Agent.length > 0) {
      for (int b = 0; b < retSale05M091Agent.length; b++) {
        ret6[b][0] = getValue("OrderNo").trim(); // OrderNo
        ret6[b][1] = "" + (b + 1); // No
        ret6[b][2] = retSale05M091Agent[b][1].trim(); // �q��m�W
        ret6[b][3] = retSale05M091Agent[b][2].trim(); // �N�z�H�m�W
        ret6[b][4] = retSale05M091Agent[b][3].trim(); // �����Ҹ�
        ret6[b][5] = retSale05M091Agent[b][4].trim(); // ��O
        ret6[b][6] = retSale05M091Agent[b][5].trim(); // ���Y
        ret6[b][7] = retSale05M091Agent[b][6].trim(); // �N�z��]
        ret6[b][8] = retSale05M091Agent[b][7].trim(); // �¦W��
        ret6[b][9] = retSale05M091Agent[b][8].trim(); // ���ަW��
        ret6[b][10] = retSale05M091Agent[b][9].trim(); // �Q�`���Y�H
      }
      setTableData("table6", ret6);
      setValue("nonAgent2", "0");
    }
    
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), logText + ":END");
    
    return false;
  }

  public String getInformation() {
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
