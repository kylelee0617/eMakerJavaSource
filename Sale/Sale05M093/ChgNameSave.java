package Sale.Sale05M093;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;
import Farglory.util.KUtils;
import Farglory.util.MLPUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;
import jcx.util.operation;

public class ChgNameSave extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    System.out.println("chk==>" + getUser() + " , value==>換戶存檔");
    if (getUser() != null && getUser().toUpperCase().equals("B9999")) {
      messagebox("換戶存檔權限不允許!!!");
      return value;
    }

    MLPUtils mlpUtils = new MLPUtils();
    KUtils kUtil = new KUtils();
    talk dbSale = getTalk("Sale");
    int RecordNo = 0;
    String stringSQL = "";
    String stringCustomName = "";
    String stringCustomNo = "";
    String stringPercent = "";
    String stringPercentSum = "0";
    String stringOrderNo = getValue("OrderNo").trim();
    String stringAddress = "";
    String stringTel = "";
    String stringTrxDate = getValue("TrxDate").trim();
    String stringProjectID1 = getValue("ProjectID1").trim();
    String stringDocNo = "";
    String stringEDate = "";
    String stringChangeName = "";
    String[][] tb1_string = getTableData("table1");
    String[][] tb2_string = getTableData("table2");
    String[][] tb5_string = getTableData("table5");
    String[][] tb6_string = getTableData("table6");
    String[][] retSale05M080 = null;
    String[][] retSale05M086 = null;
    String[][] retSale05M087 = null;
    String[][] retSale05M091 = null;
    String[][] retSale05M091Ben = null;
    String[][] retSale05M091Agent = null;
    boolean flag1 = false;
    boolean flag2 = false;// 實質受益人
    boolean flag3 = false;// 代理人
    Vector vectorSql = new Vector();
    Vector vectorCustomNo = new Vector();
    JTable jtable2 = getTable("table2");
    int intcount = 0;
    String paperNo = getValue("PaperNo") == null ? "" : getValue("PaperNo").trim();
    String majorID = getValue("MajorID") == null ? "" : getValue("MajorID").trim();
    String majorName = getValue("MajorName").trim();

    // 20200212 kyle >> key: customName , value: customNo
    Map tb2Map = new HashMap();

    // 檢核
    // 換名日期
    if (stringTrxDate.length() == 0) {
      message("[換名日期] 不可空白");
      return value;
    }
    stringTrxDate = convert.replace(stringTrxDate, "/", "");
    if (!check.isACDay(stringTrxDate)) {
      message("[換名日期] 格式錯誤(YYYY/MM/DD)。");
      return value;
    } else {
      setValue("TrxDate", convert.FormatedDate(stringTrxDate, "/"));
    }
    // 日期小於換名日期之資料未開立發票時，顯示訊息，不允動作
    stringSQL = "SELECT  DocNo  FROM  Sale05M086  WHERE  OrderNo  =  '" + stringOrderNo + "' ";
    retSale05M086 = dbSale.queryFromPool(stringSQL);
    for (int intNo = 0; intNo < retSale05M086.length; intNo++) {
      stringDocNo = retSale05M086[intNo][0].trim();
      //
      System.out.println("");
      stringSQL = "SELECT  EDate  FROM  Sale05M080 WHERE  DocNo  =  '" + stringDocNo + "' ";
      retSale05M080 = dbSale.queryFromPool(stringSQL);
      if (retSale05M080.length == 0) {
        message("[使用編號] 不存在於 [收款單(Sale05M080] 中。");
        return value;
      }
      //
      stringEDate = convert.replace(retSale05M080[0][0].trim(), "/", "");
      // System.out.println((intNo+1)+"--------------------"+stringEDate+"-----------------"+datetime.subDays1(stringTrxDate,
      // stringEDate)) ;
      if (datetime.subDays1(stringTrxDate, stringEDate) > 0) {
        // 日期小於換名日期時，判斷是否開立發票
        stringSQL = "SELECT  InvoiceNo  FROM  Sale05M087  WHERE  DocNo  =  '" + stringDocNo + "' ";
        retSale05M087 = dbSale.queryFromPool(stringSQL);
        if (retSale05M087.length == 0) {
          message("[收款單編號] 為 " + stringDocNo + " [收款日期] 為 " + stringEDate + " 之資料未開立轉發票。");
          return value;
        }
      }
    }
    stringTrxDate = getValue("TrxDate").trim();

    // 表格2
    talk dbDoc = getTalk("Doc");
    String stringNationality = "";
    String stringZIP = "";
    String stringCity = "";
    String stringTown = "";
    String stringAuditorship = ""; // 20090401 增加身份
    // 20191224 配合洗錢防制更新新增欄位
    String strCountryName = "";
    String strBirthday = "";
    String strMajorName = "";
    String strPositionName = "";
    String strCellphone = "";
    String strTel2 = "";
    String streMail = "";
    String strIsBlackList = "";
    String strIsControlList = "";
    String strIsLinked = "";
    String stringSql = "";
    String[][] retTown = null;
    for (int i = 0; i < tb2_string.length; i++) {
      stringChangeName = ("" + getValueAt("table2", i, "ChangeName")).trim();
//      stringAuditorship = ("" + getValueAt("table2", i, "auditorship")).trim();
      stringNationality = ("" + getValueAt("table2", i, "Nationality")).trim();
      strCountryName = ("" + getValueAt("table2", i, "CountryName")).trim();
      stringCustomNo = ("" + getValueAt("table2", i, "CustomNo")).trim();
      stringCustomName = ("" + getValueAt("table2", i, "CustomName")).trim();
      stringPercent = ("" + getValueAt("table2", i, "Percentage")).trim();
      strBirthday = ("" + getValueAt("table2", i, "Birthday")).trim();
      strMajorName = ("" + getValueAt("table2", i, "MajorName")).trim();
      strPositionName = ("" + getValueAt("table2", i, "PositionName")).trim();
      stringZIP = ("" + getValueAt("table2", i, "ZIP")).trim();
      stringCity = ("" + getValueAt("table2", i, "City")).trim();
      stringTown = ("" + getValueAt("table2", i, "Town")).trim();
      stringAddress = ("" + getValueAt("table2", i, "Address")).trim();
      strCellphone = ("" + getValueAt("table2", i, "Cellphone")).trim();
      stringTel = ("" + getValueAt("table2", i, "Tel")).trim();
      strTel2 = ("" + getValueAt("table2", i, "Tel2")).trim();
      streMail = ("" + getValueAt("table2", i, "eMail")).trim();
      strIsBlackList = ("" + getValueAt("table2", i, "IsBlackList")).trim();
      strIsControlList = ("" + getValueAt("table2", i, "IsControlList")).trim();
      strIsLinked = ("" + getValueAt("table2", i, "IsLinked")).trim();
      String engName = getValueAt("table2", i, "EngName").toString().trim();
      String engNo = getValueAt("table2", i, "EngNo").toString().trim();
      String countryName2 = getValueAt("table2", i, "CountryName2").toString().trim();

      if ("1".equals(stringChangeName)) {

        // ID空白
        if ( StringUtils.isBlank(stringCustomNo) && StringUtils.isBlank(engNo) ) {
          message("[統編/身份證號] 或 [護照證件號] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }

        // 訂戶姓名空白
        if ( StringUtils.isBlank(stringCustomName) && StringUtils.isBlank(engName) ) {
          message("[訂戶姓名] 或 [護照證件名] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }

        // TODO: 萊斯
        // 萊斯Start
        String orderDate = kUtil.getOrderDateByOrderNo(stringOrderNo);
        String indCode = kUtil.getIndustryCodeByMajorName(strMajorName);
        String funcName = getFunctionName();
        String recordType = "客戶資料";
        String processType = "query18";
        String amlText = stringProjectID1 + "," + stringOrderNo + "," + orderDate + "," + funcName + "," + recordType + "," + stringCustomNo + "," + stringCustomName + ","
            + strBirthday + "," + indCode + "," + strCountryName + "," + countryName2 + "," + engName + "," + processType;
        setValue("AMLText", amlText);
        getButton("BtCustAML").doClick();
        String amlRS = getValue("AMLText").trim();
        if (!"".equals(amlRS)) {
          messagebox(amlRS);
          return value;
        }
        // 萊斯END

        flag1 = true;
        // 客戶
        if ("".equals(stringNationality)) {
          message("[國籍] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }

        // 法人時檢核實受人是否空白
        System.out.println("tb5_string.length==>" + tb5_string.length);
        if (stringCustomNo.length() == 8) {
          if (tb5_string.length == 0) {
            messagebox("更新客戶資料包含法人，請指定其實質受益人!!");
            return value;
          }
          // 該法人是否存在
          boolean flag91Ben = true;
          if (tb5_string.length > 0) {
            for (int e = 0; e < tb5_string.length; e++) {
              String strCustomNo = "";
              strCustomNo = ("" + getValueAt("table5", e, "CustomNo")).trim();
              if (stringCustomName.equals(strCustomNo)) { // 這邊實受人的CustomNo，裝的其實是CustomName
                flag91Ben = false;
                break;
              }
            }
            if (flag91Ben) {
              message("法人[" + stringCustomName + "]，請指定其實質受益人!!");
              return value;
            }
          }
        }
        if ("1".equals(stringNationality)) {
          // 本國人
          if (stringCustomNo.length() != 8 && stringCustomNo.length() != 10) {
            message("[統編/身分證號] 長度錯誤!");
            jtable2.setRowSelectionInterval(i, i);
            return value;
          }
          if (stringCustomNo.length() == 8 && check.isCoId(stringCustomNo) == false) {
            message("[統編/身分證號] 統一編號錯誤!");
            jtable2.setRowSelectionInterval(i, i);
            return value;
          }
          if (stringCustomNo.length() == 10 && check.isID(stringCustomNo) == false) {
            message("[統編/身分證號] 身分證號錯誤!");
            jtable2.setRowSelectionInterval(i, i);
            return value;
          }
        }

        // 重覆
        if (vectorCustomNo.indexOf(stringCustomNo) != -1) {
          message("該筆客戶:" + stringCustomName + " 資料重覆。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        vectorCustomNo.add(stringCustomNo);

        // 比例
        if ("".equals(stringPercent)) {
          message("[比例%] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        if (!check.isFloat(stringPercent, "5,2")) {
          message("[比例%] 必須為數字。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        System.out.println("stringPercent:" + stringPercent);
        if (Float.parseFloat(stringPercent) < 1) {
          message("[比例%] 不得小於 1 %。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        stringPercentSum = operation.floatAdd(stringPercentSum, stringPercent, 2);
        System.out.println("stringPercentSum:" + stringPercentSum);

        if ("".equals(stringCity)) {
          message("[縣市] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        if ("".equals(stringTown)) {
          message("[鄉鎮]  不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        stringSql = " SELECT  ZIP FROM  Town b  WHERE  Coun   IN  (SELECT  Coun  FROM  City  WHERE  CounName='" + stringCity + "') " + " AND  TownName  =  '" + stringTown + "' ";
        retTown = dbDoc.queryFromPool(stringSql);
        if (retTown.length == 0) {
          messagebox("[縣市][鄉鎮] 關係不正確。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }

        if ("".equals(stringAddress)) {
          message("[地址] 不可為空白。");
          jtable2.setRowSelectionInterval(i, i);
          return value;
        }
        // kyle 20200212 - 特殊需求: 將table2[][]轉MAP
        tb2Map.put(stringCustomName, stringCustomNo);
      }
    }
    // table2 end

    System.out.println("stringPercentSum=====>" + stringPercentSum);
    if (Float.parseFloat(stringPercentSum) != 100) {
      message("[比例%] 合計須為 100 %。");
      // jtable2.setRowSelectionInterval(i, i) ;
      return value;
    }

    // System.out.println("flag1"+flag1);
    if (flag1 == false) {
      message("請至少勾選一筆換名");
      return value;
    }

    // TODO: 萊斯
    // 20200620 Kyle : 受益人欄位洗錢檢核
    getButton("CheckBensAML18").doClick();
    if (StringUtils.isNotBlank(getValue("AMLText"))) return value;

    // 異動資料庫
    // 客戶資料
    stringSQL = "SELECT  max(RecordNo) " + " FROM  Sale05M091 " + " WHERE  OrderNo  =  '" + stringOrderNo + "' ";
    retSale05M091 = dbSale.queryFromPool(stringSQL);
    RecordNo = Integer.parseInt(retSale05M091[0][0]) + 1;
    //
    String stringCustomNoNew = "";
    String stringCustomNewName = "";
    String stringEmail = "";
    for (int i = 0; i < tb1_string.length; i++) {
      stringCustomNo = ("" + getValueAt("table1", i, "CustomNo")).trim();
      stringCustomName = ("" + getValueAt("table1", i, "CustomName")).trim();
      //
      stringSQL = " UPDATE Sale05M091 SET " + "  StatusCd = 'C', " + "  TrxDate = '" + stringTrxDate + "' " + " WHERE OrderNo  =  '" + stringOrderNo + "' " + " and  CustomNo  =  '"
          + stringCustomNo + "' " + " and  ISNULL(StatusCd,'')='' ";
      System.out.println(i + "------------------UPDATE  Sale05M091----------" + stringSQL);
      vectorSql.add(stringSQL);

      stringSQL = " INSERT  " + " INTO Sale05M093" + " ( " + " ProjectID1," + " OrderNo," + " RecordNo," + " TrxDate," + " CustomNo, " + " CustomName, " + " CustomNoNew, "
          + " CustomNewName, " + " DiscountOpen, " + " PaperNo, " + " MajorID, " + " MajorName " + " ) " + " VALUES " + " ( " + " '" + getValue("ProjectID1").trim() + "', " + " '"
          + stringOrderNo + "', " + " N'" + (i + 1) + "', " + " N'" + stringTrxDate + "', " + " N'" + stringCustomNo + "', " + " N'" + stringCustomName + "', ";
      System.out.println("tb2_string.length: " + tb2_string.length);
      System.out.println("tb1_string.length: " + tb1_string.length);
      if (tb2_string.length < tb1_string.length) {
        stringCustomNoNew = ("" + getValueAt("table2", 0, "CustomNo")).trim();
        stringCustomNewName = ("" + getValueAt("table2", 0, "CustomName")).trim();
      } else {
        stringCustomNoNew = ("" + getValueAt("table2", i, "CustomNo")).trim();
        stringCustomNewName = ("" + getValueAt("table2", i, "CustomName")).trim();
      }
      stringSQL = stringSQL + " N'" + stringCustomNoNew + "' , " + " N'" + stringCustomNewName + "' , " + " '', ";
      stringSQL = stringSQL + "'" + paperNo + "','" + majorID + "','" + majorName + "') ";
      System.out.println(i + "------------------INSERT  Sale05M093----------" + stringSQL);
      vectorSql.add(stringSQL);

    }

    // 新增91
    // 客戶資料
    for (int i = 0; i < tb2_string.length; i++) {
      stringChangeName = ("" + getValueAt("table2", i, "ChangeName")).trim();
//      stringAuditorship = ("" + getValueAt("table2", i, "auditorship")).trim();
      stringNationality = ("" + getValueAt("table2", i, "Nationality")).trim();
      strCountryName = ("" + getValueAt("table2", i, "CountryName")).trim();
      stringCustomNo = ("" + getValueAt("table2", i, "CustomNo")).trim();
      stringCustomName = ("" + getValueAt("table2", i, "CustomName")).trim();
      stringPercent = ("" + getValueAt("table2", i, "Percentage")).trim();
      strBirthday = ("" + getValueAt("table2", i, "Birthday")).trim();
      strMajorName = ("" + getValueAt("table2", i, "MajorName")).trim();
      strPositionName = ("" + getValueAt("table2", i, "PositionName")).trim();
      stringZIP = ("" + getValueAt("table2", i, "ZIP")).trim();
      stringCity = ("" + getValueAt("table2", i, "City")).trim();
      stringTown = ("" + getValueAt("table2", i, "Town")).trim();
      stringAddress = ("" + getValueAt("table2", i, "Address")).trim();
      strCellphone = ("" + getValueAt("table2", i, "Cellphone")).trim();
      stringTel = ("" + getValueAt("table2", i, "Tel")).trim();
      strTel2 = ("" + getValueAt("table2", i, "Tel2")).trim();
      streMail = ("" + getValueAt("table2", i, "eMail")).trim();
      strIsBlackList = ("" + getValueAt("table2", i, "IsBlackList")).trim();
      strIsControlList = ("" + getValueAt("table2", i, "IsControlList")).trim();
      strIsLinked = ("" + getValueAt("table2", i, "IsLinked")).trim();
      String engName = getValueAt("table2", i, "EngName").toString().trim();
      String engNo = getValueAt("table2", i, "EngNo").toString().trim();
      String countryName2 = getValueAt("table2", i, "CountryName2").toString().trim();

      // System.out.println("i2"+tb2_string[i][2]);
      if ("1".equals(stringChangeName)) {
        stringSQL = "INSERT  INTO Sale05M091  (OrderNo,RecordNo,auditorship,Nationality,CountryName, CustomNo,CustomName,Percentage,Birthday,MajorName, "
            + "PositionName,ZIP,City,Town,Address,Cellphone,Tel,Tel2,eMail,IsBlackList,IsLinked,IsControlList,TrxDateDown, EngNo, EngName, CountryName2) " + "VALUES ('"
            + stringOrderNo + "'," + "N'" + RecordNo + "', N'A'," + "N'" + stringNationality + "'," + "N'" + strCountryName + "'," + "N'" + stringCustomNo + "'," + "N'"
            + stringCustomName + "'," + "N'" + stringPercent + "'," + "N'" + strBirthday + "'," + "N'" + strMajorName + "'," + "N'" + strPositionName + "', " + "N'" + stringZIP
            + "', " + "N'" + stringCity + "' ," + "N'" + stringTown + "' ," + "N'" + stringAddress + "' ," + "N'" + strCellphone + "' ," + "N'" + stringTel + "' ," + "N'" + strTel2
            + "' ," + "N'" + streMail + "' , " + "N'" + strIsBlackList + "' ," + "N'" + strIsLinked + "' ," + "N'" + strIsControlList + "' ," + "N'" + stringTrxDate + "', " + "N'"
            + engNo + "', N'" + engName + "', N'" + countryName2 + "' ) ";
        vectorSql.add(stringSQL);
        RecordNo = RecordNo + 1;
      }
    }

    // 實受 + 代理人共用欄位
    String trxDate = getValue("TrxDate").trim();

    // 實受人
    String strNew91BenNo = "";
    String strNew91BenName = "";
    String str91BenCustomNo = "";
    String str91BenNo = "";
    String str91BenName = "";
    String str91BenBirthday = "";
    String str91BenCountryName = "";
    String str91BenHoldType = "";
    String str91BenIsBlackList = "";
    String str91BenIsControlList = "";
    String str91BenIsLinked = "";

    // 20200226 kyle : 要求改成像客戶換名那樣保留資料並用StatusCd='C'標記
    // stringSQL= "DELETE FROM Sale05M091Ben WHERE OrderNo = '" +
    // getValue("OrderNo") + "'" ;
    stringSQL = "UPDATE Sale05M091Ben set StatusCd = 'C', TrxDate = '" + trxDate + "' WHERE OrderNo = '" + getValue("OrderNo") + "'";
    vectorSql.add(stringSQL);

    System.out.println("tb5_string.length=====>>" + tb5_string.length);
    if (tb5_string.length > 0) {
      stringSQL = "select ISNULL(max(RecordNo), 0)+1 from Sale05M091Ben where orderNo = '" + getValue("OrderNo").trim() + "' ";
      String[][] retMax = dbSale.queryFromPool(stringSQL);
      int maxRecordNo = Integer.parseInt(retMax[0][0].trim());

      for (int j = 0; j < tb5_string.length; j++) {
        str91BenNo = ("" + getValueAt("table5", j, "BenNo")).trim();
        str91BenName = ("" + getValueAt("table5", j, "BenName")).trim();
        str91BenBirthday = ("" + getValueAt("table5", j, "Birthday")).trim();
        str91BenCountryName = ("" + getValueAt("table5", j, "CountryName")).trim();
        str91BenHoldType = ("" + getValueAt("table5", j, "HoldType")).trim();
        str91BenIsBlackList = ("" + getValueAt("table5", j, "IsBlackList")).trim();
        str91BenIsControlList = ("" + getValueAt("table5", j, "IsControlList")).trim();
        str91BenIsLinked = ("" + getValueAt("table5", j, "IsLinked")).trim();
        String getMapKey = getValueAt("table5", j, "CustomNo").toString().trim();
        int recordNo = j + maxRecordNo;
        stringCustomNo = tb2Map.get(getMapKey) == null ? "" : tb2Map.get(getMapKey).toString();
        System.out.println("tb5_string.stringCustomNo=====>>" + stringCustomNo);
        if ("".equals(stringCustomNo)) {
          messagebox("實質受益人 [" + getMapKey + "] 找不到對應客戶");
          message("請確認更新客戶名單及對應實質受益人是否正確!!");
          return value;
        }

        str91BenBirthday = convert.replace(str91BenBirthday, "/", "");

        stringSQL = " INSERT INTO Sale05M091Ben (OrderNo,RecordNo,CustomNo,BenName,BCustomNo,Birthday,CountryName,HoldType,IsBlackList,IsControlList,IsLinked ) " + " VALUES   ( "
            + "'" + getValue("OrderNo") + "'," + "N'" + recordNo + "'," + "N'" + stringCustomNo + "'," + "N'" + str91BenName + "'," + "N'" + str91BenNo + "'," + "N'"
            + str91BenBirthday + "'," + "N'" + str91BenCountryName + "'," + "N'" + str91BenHoldType + "'," + "N'" + str91BenIsBlackList + "'," + "N'" + str91BenIsControlList + "',"
            + "N'" + str91BenIsLinked + "' " + " ) ";
        System.out.println(j + "------------------INSERT  Sale05M091Ben----------" + stringSQL);
        vectorSql.add(stringSQL);
      }
    }

    // 代理人
    String strNew91ACustomNo = "";
    String strNew91AgentName = "";
    String str91OrderNo = "";
    String str91CustomNo = "";
    String str91ACustomNo = "";
    String str91AgentName = "";
    String str91AgentCountryName = "";
    String str91AgentRel = "";
    String str91AgentReason = "";
    String str91AgentIsBlackList = "";
    String str91AgentIsControlList = "";
    String str91AgentIsLinked = "";

    // 20200226 kyle : 要求改成像客戶換名那樣保留資料並用StatusCd='C'標記
    // stringSQL= "DELETE FROM Sale05M091Agent WHERE OrderNo = '" +
    // getValue("OrderNo") + "'" ;
    stringSQL = "UPDATE Sale05M091Agent set StatusCd = 'C', TrxDate = '" + trxDate + "' WHERE OrderNo = '" + getValue("OrderNo") + "'";
    vectorSql.add(stringSQL);

    if (tb6_string.length > 0) {
      stringSQL = "select ISNULL(max(RecordNo), 0)+1 from Sale05M091Agent where orderNo = '" + getValue("OrderNo").trim() + "' ";
      String[][] retMax = dbSale.queryFromPool(stringSQL);
      int maxRecordNo = Integer.parseInt(retMax[0][0].trim());

      for (int k = 0; k < tb6_string.length; k++) {
        str91OrderNo = ("" + getValueAt("table6", k, "OrderNo")).trim();
        str91CustomNo = ("" + getValueAt("table6", k, "CustomNo")).trim();
        str91ACustomNo = ("" + getValueAt("table6", k, "ACustomNo")).trim();
        str91AgentName = ("" + getValueAt("table6", k, "AgentName")).trim();
        str91AgentCountryName = ("" + getValueAt("table6", k, "CountryName")).trim();
        str91AgentRel = ("" + getValueAt("table6", k, "AgentRel")).trim();
        str91AgentReason = ("" + getValueAt("table6", k, "AgentReason")).trim();
        str91AgentIsBlackList = ("" + getValueAt("table6", k, "IsBlackList")).trim();
        str91AgentIsControlList = ("" + getValueAt("table6", k, "IsControlList")).trim();
        str91AgentIsLinked = ("" + getValueAt("table6", k, "IsLinked")).trim();
        String aIdent = getValueAt("table6", k, "AIdent").toString().trim();

        String getMapKey = getValueAt("table6", k, "CustomNo").toString().trim();
        int recordNo = k + maxRecordNo;
        stringCustomNo = tb2Map.get(getMapKey) == null ? "" : tb2Map.get(getMapKey).toString();
        System.out.println("tb5_string.stringCustomNo=====>>" + stringCustomNo);
        if ("".equals(stringCustomNo)) {
          messagebox("代理人 [" + getMapKey + "] 找不到對應客戶");
          message("請確認更新客戶名單及對應代理人是否正確!!");
          return value;
        }
        stringSQL = " INSERT INTO Sale05M091Agent (OrderNo,RecordNo,CustomNo,AgentName,ACustomNo,CountryName,AgentRel,AgentReason,IsBlackList,IsControlList,IsLinked,AIdent ) "
            + " VALUES   (    " + "N'" + getValue("OrderNo") + "'," + "N'" + recordNo + "'," + "N'" + stringCustomNo + "'," + "N'" + str91AgentName + "'," + "N'" + str91ACustomNo
            + "'," + "N'" + str91AgentCountryName + "'," + "N'" + str91AgentRel + "'," + "N'" + str91AgentReason + "'," + "N'" + str91AgentIsBlackList + "', " + "N'"
            + str91AgentIsControlList + "'," + "N'" + str91AgentIsLinked + "', N'"+aIdent+"' ) ";
        System.out.println(k + "------------------INSERT  Sale05M091Agent----------" + stringSQL);
        vectorSql.add(stringSQL);
      }
    }
    if (vectorSql.size() > 0) {
      dbSale.execFromPool((String[]) vectorSql.toArray(new String[0]));
      getButton("button3").setVisible(true);
      // getButton("button4").setVisible(true); //20200212 kyle mod:
      // 刪除會造成實質受益人偏移，估計USER用不到這個功能先隱藏
    }
    //
    stringSQL = "SELECT * FROM Sale05M091 " + " WHERE OrderNo = '" + getValue("OrderNo") + "'" + " and isnull(StatusCd,'') = '' ";
    String[][] ret91 = dbSale.queryFromPool(stringSQL);
    System.out.println("stringSQL==>" + stringSQL);
    System.out.println("ret91.length==>" + ret91.length);
    if (ret91.length == 0) {
      stringSQL = "UPDATE  Sale05M091 SET " + "  StatusCd = '', " + "  TrxDate = '' " + " WHERE OrderNo = '" + getValue("OrderNo") + "'" + " and CustomNo = '" + stringCustomNo
          + "' ";
      dbSale.execFromPool(stringSQL);
      stringSQL = "Delete from  Sale05M093  " + " WHERE OrderNo = '" + getValue("OrderNo") + "'" + " and CustomNo = '" + stringCustomNo + "' ";
      dbSale.execFromPool(stringSQL);
      getButton("button3").setVisible(false);
      // getButton("button4").setVisible(true); //20200212 kyle mod:
      // 刪除會造成實質受益人偏移，估計USER用不到這個功能先隱藏
    }

    // TODO: 萊斯
    System.out.println("洗錢防治相關-------------------------------------S");
    // 21-05 Kyle : 更新主要客戶與關聯人
    getButton("RenewRelated").doClick();

    // 21-05 Kyle : 查詢客戶風險值
    getButton("CheckRiskNew").doClick();

    // AML
    setValue("actionText", "存檔");
    getButton("AML").doClick();
    System.out.println("洗錢防治相關-------------------------------------E");

    messagebox("換名成功");
    return value;
  }

  public String getInformation() {
    return "---------------button1(button1).defaultValue()----------------";
  }
}
