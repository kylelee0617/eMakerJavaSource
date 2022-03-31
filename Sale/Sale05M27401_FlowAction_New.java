package Sale;

import jcx.jform.bProcFlow;
import jcx.db.*;
import jcx.util.*;
import javax.swing.JTable;
import java.text.DecimalFormat;

public class Sale05M27401_FlowAction_New extends bProcFlow {
  public boolean action(String value) throws Throwable {
    // 回傳值為 true 表示執行接下來的流程處理
    // 回傳值為 false 表示接下來不執行任何流程處理
    // 傳入值 value 為 "送呈"、"核准審核結束"、退承辦"...等
    Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil();
    talk dbSale = getTalk("Sale");
    talk dbFE5D = getTalk("FE5D");
    talk dbFE3D = getTalk("" + get("put_dbFE3D"));
    talk dbDoc = getTalk("" + get("put_dbDoc"));
    talk dbFED1 = getTalk("" + get("put_dbFED1"));
    talk dbAS400 = getTalk("JGENLIB");
    String stringState = getState();
    String stringSql = "";
    String retData[][] = null;
    String retSales[][] = null;
    String stringStatus = getValue("Status");
    String stringProjectID1 = getValue("ProjectID1").trim();
    String stringContractNo = getValue("ContractNo").trim();
    String stringCompanyCd = getValue("CompanyCd").trim();
    String stringContractSerialNo = getValue("ContractSerialNo").trim();
    String stringNotifyUser = getValue("NotifyUser").trim();
    String stringGetBackDocStatus = getValue("GetBackDocStatus").trim();
    String stringContractDate = getValue("ContractDate").trim();
    boolean blnHas01 = false;
    String aryCompanyCd[][] = null;
    String retFED1023[][] = null;
    String stringErrorMSGTemp = "";
    String stringErrorMSG = "";
    String stringOrderDate = "";
    boolean blnHasCSSales = false;
    boolean bln289AllCompany = false;
    boolean bln289HasCS = false;
    boolean bln289HasZ3 = false;
    boolean bln289HasZ0 = false;
    String stringIsSFlow1 = (String) get("put_stringIsSFlow1");
    String stringDestState = "";
    String stringPerson = "";
    if ("Y".equals(stringIsSFlow1)) {
      if ("送呈".equals(value)) {
        if ("承辦".equals(stringState)) {
          setDestState("營業經辦");
        } else if ("行銷".equals(stringState)) {
          setDestState("營業部");
        } else if ("營業經辦".equals(stringState)) {
          setDestState("財務經辦");
        }
      } else if ("送呈 ".equals(value)) {
        if ("承辦".equals(stringState)) {
          setDestState("財務經辦");
        }
      }
    }
    if ("退承辦".equals(value)) {
      if (getMemo().trim().length() == 0) {
        message("請輸入退回意見!");
        return false;
      }
      setDestState("承辦");
    } else if ("退營業".equals(value)) {
      if ("營業部".equals(stringState) || "營業_出文".equals(stringState) || "開發_出文".equals(stringState) || "財務經辦".equals(stringState) || "財務代銷".equals(stringState)
          || "財務_出文".equals(stringState) || "法務_出文".equals(stringState) || "不動產".equals(stringState) || "會計".equals(stringState) || "營業結案".equals(stringState)) {
        setDestState("營業經辦");
      }
    } else if ("退行銷".equals(value)) {
      if ("Y".equals(stringIsSFlow1)) {
        if ("營業經辦".equals(stringState) || "財務經辦".equals(stringState) || "財務代銷".equals(stringState) || "營業_出文".equals(stringState) || "開發_出文".equals(stringState)
            || "財務_出文".equals(stringState) || "法務_出文".equals(stringState) || "不動產".equals(stringState) || "會計".equals(stringState) || "營業結案".equals(stringState)) {
          setDestState("承辦");
        } else if ("營業部".equals(stringState) || "開發部".equals(stringState) || "財務室".equals(stringState)) {
          setDestState(stringState.substring(0, 2) + "_出文");
        }
      } else {
        if ("營業_出文".equals(stringState) || "開發_出文".equals(stringState) || "財務_出文".equals(stringState) || "法務_出文".equals(stringState) || "不動產".equals(stringState)
            || "會計".equals(stringState) || "營業結案".equals(stringState)) {
          setDestState("承辦");
        } else if ("營業部".equals(stringState) || "開發部".equals(stringState) || "財務室".equals(stringState)) {
          setDestState(stringState.substring(0, 2) + "_出文");
        }
      }
    } else if ("作廢存檔".equals(value)) {
      // 作廢日期
      String stringVoidDate = getValue("VoidDate").trim();
      if (stringVoidDate.length() == 0) {
        message("[作廢日期] 不可為空白!");
        getcLabel("VoidDate").requestFocus();
        return false;
      }
      if (stringVoidDate.replaceAll("/", "").length() != 8) {
        message("[作廢日期] 格式錯誤(YYYY/MM/DD) !");
        getcLabel("VoidDate").requestFocus();
        return false;
      }
      stringVoidDate = exeUtil.getDateAC(stringVoidDate, "作廢日期");
      if (stringVoidDate.length() != 10) {
        message(stringVoidDate);
        getcLabel("VoidDate").requestFocus();
        return false;
      }
      if (datetime.subDays1(getToday("YYYYmmdd"), stringVoidDate.replaceAll("/", "")) < 0) {
        message("作廢日期必須小於等於今天!");
        getcLabel("VoidDate").requestFocus();
        return false;
      }
      setValue("VoidDate", stringVoidDate);
      // 作廢原因
      String stringVoidReason = getValue("VoidReason").trim();
      if (stringVoidReason.length() == 0) {
        message("[作廢原因] 不可為空白!");
        getcLabel("VoidReason").requestFocus();
        return false;
      } else if ("6".equals(stringVoidReason)) {
        // 選其它備註必填
        String stringVoidRemark = getValue("VoidRemark").trim();
        if (stringVoidRemark.length() == 0) {
          message("[作廢原因]為其它，則[備註] 不可為空白!");
          getcLabel("VoidRemark").requestFocus();
          return false;
        }
      }
    }

    setValue("ActionName", value);

    if (("承辦".equals(stringState) && ("送呈".equals(value) || "送呈 ".equals(value))) || ("內業業管".equals(stringState) && "送呈".equals(value))
        || ("行銷".equals(stringState) && "送呈".equals(value))) {
      stringSql = "SELECT HLCompanyCd FROM Sale05M289 T1 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' "
          + "AND HLCompanyCd='CS' AND EXISTS(SELECT T92.SaleFlag FROM Sale05M274 T274, Sale05M278 T278, Sale05M092 T92 WHERE T274.ContractNo=T278.ContractNo "
          + "AND T274.CompanyCd=T278.CompanyCd AND T278.OrderNo=T92.OrderNo AND T278.HouseCar=T92.HouseCar AND T278.Position=T92.Position "
          + "AND T274.ContractNo=T1.ContractNo AND T274.CompanyCd=T1.CompanyCd AND ISNULL(T92.SaleFlag,'')='')";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length > 0) {
        stringSql = "SELECT F_INP_TIME FROM Sale05M274_FLOWC WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' "
            + "AND SUBSTRING(F_INP_TIME,1,8)>='20120808'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          stringSql = "SELECT SalesNo, (CASE WHEN SUBSTRING(T295.SalesNo,1,1)='A' THEN 'A0'+SUBSTRING(T295.SalesNo,2,3) "
              + "WHEN SUBSTRING(T295.SalesNo,1,1)='C' THEN 'C0'+SUBSTRING(T295.SalesNo,2,3) ELSE 'B'+T295.SalesNo END) SalesFullNo, T90.Orderdate, T295.SalesName "
              + "FROM Sale05M295 T295, Sale05M090 T90 WHERE T295.OrderNo=T90.OrderNo AND T295.ContractNo='" + stringContractNo + "' AND T295.CompanyCd='"
              + stringCompanyCd + "' ORDER BY T295.SalesNo";
          retData = dbSale.queryFromPool(stringSql);
          for (int intNo = 0; intNo < retData.length; intNo++) {
            stringOrderDate = convert.add0(convert.ac2roc(retData[intNo][2].replaceAll("/", "")), "7");
            stringSql = "SELECT EMP_NO FROM FE3D74 WHERE INSUR_KIND='1' AND FIRM_NO='84703052' AND EMP_NO='" + retData[intNo][1] + "' AND '"
                + stringOrderDate + "' BETWEEN REGISTER_DATE AND (CASE WHEN ISNULL(CANCEL_DATE,'')='' THEN '9991231' ELSE CANCEL_DATE END)";
            retSales = dbFE3D.queryFromPool(stringSql);
            if (retSales.length > 0) {
              blnHasCSSales = true;
              break;
            }
          }
          if (stringContractNo.equals("033H90A1010808001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1011031003")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H90A1000904001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H48A990425002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1011130007")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1011130006")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033M61A1011205001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H48A981204002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1011228001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H90A1000917002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1020107002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1020127001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H48A990524001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1020309001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020316001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020316002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020407003")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020408003")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020407001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020407002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1020410001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H116A1020416001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020714001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020728001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020819001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H90A1000830002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H102A1010510001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020907001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1020907002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H85A1000808001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H85A1020919001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1021003002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H90A1000829001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1021013001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H90A1010808001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1021102001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021108001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021110001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H110A1021116001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021118001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021118002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021118003")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021120001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021120002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021122002")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021123001")) {
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021126001")) { // Mei 1021127 A03F11
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021122001")) { // Mei 1021127 A03F05
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("035H85A1000725001")) { // Mei 1021203 A06F19
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021111001")) { // Mei 1021203 A03F08
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021128001")) { // Mei 1021203 A08F05
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021111002")) { // Mei 1021203 A08F12
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021124001")) { // Mei 1021203 A07F09
            blnHasCSSales = true;
          }
          if (stringContractNo.equals("033H105A1021206001")) { // Mei 1021210 A05F10
            blnHasCSSales = true;
          }
        }
      }
    }
    stringSql = "SELECT * FROM Sale05M274 T274 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' "
        + "AND (SELECT COUNT(*) FROM Sale05M289 WHERE ContractNo=T274.ContractNo AND CompanyCd=T274.CompanyCd)="
        + "(SELECT COUNT(*) FROM Sale05M289 , A_COM WHERE Sale05M289.ComNo=A_COM.Com_No AND Sale05M289.ContractNo=T274.ContractNo AND Sale05M289.CompanyCd=T274.CompanyCd AND ISNULL(A_COM.COMPANY_CD,'')!='')";
    retData = dbSale.queryFromPool(stringSql);
    if (retData.length > 0) {
      bln289AllCompany = true;
    }
    stringSql = "SELECT * FROM Sale05M289 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' AND HLCompanyCd='CS'";
    retData = dbSale.queryFromPool(stringSql);
    if (retData.length > 0) {
      bln289HasCS = true;
    }
    stringSql = "SELECT * FROM Sale05M289 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' AND HLCompanyCd='Z3'";
    retData = dbSale.queryFromPool(stringSql);
    if (retData.length > 0) {
      bln289HasZ3 = true;
    }
    stringSql = "SELECT * FROM Sale05M289 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "' AND HLCompanyCd='Z0'";
    retData = dbSale.queryFromPool(stringSql);
    if (retData.length > 0) {
      bln289HasZ0 = true;
    }
    if ("H75A".equals(stringProjectID1) || "H92A".equals(stringProjectID1) || "H102A".equals(stringProjectID1)) {
      stringSql = "SELECT DISTINCT ComNo FROM Sale05M289 , A_COM WHERE Sale05M289.ComNo=A_COM.Com_No AND Sale05M289.ContractNo='" + stringContractNo + "' "
          + "AND CompanyCd='" + stringCompanyCd + "' AND ISNULL(A_COM.COMPANY_CD,'')='' ORDER BY ComNo";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        stringPerson = "NULL";
      } else {
        for (int intRow = 0; intRow < retData.length; intRow++) {
          stringPerson = stringPerson.length() == 0 ? "'3','" + retData[intRow][0] + "'" : stringPerson + ",'" + retData[intRow][0] + "'";
        }
      }
    }
    if ("承辦".equals(stringState) && "送呈 ".equals(value)) {
      String stringFE5DSql = "";
      String retFE5DData[][] = null;
      String stringObjectCd = getValue("OBJECT_CD").trim();
      if (stringObjectCd.length() == 0) {
        message("請輸入客戶代號!");
        return false;
      }
      // 回寫 FE5D34
      stringSql = "select OrderNo from Sale05M275_New where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' and Position='"
          + (stringProjectID1.equals("H28") ? stringObjectCd : stringObjectCd.substring(0, 6)) + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        message("資料錯誤!");
        return false;
      } else {
        String stringDEPT_CD = "";
        String stringDEPT_CD_1 = "";
        String stringOrderNo = "";
        if ("A1".equals(stringProjectID1)) {
          stringDEPT_CD = "A01";
          stringDEPT_CD_1 = "A";
        } else if ("A2".equals(stringProjectID1)) {
          stringDEPT_CD = "A02";
          stringDEPT_CD_1 = "A";
        } else if ("H28".equals(stringProjectID1)) {
          stringDEPT_CD = "H28";
          stringDEPT_CD_1 = "A";
        } else if ("M48".equals(stringProjectID1)) {
          stringDEPT_CD = "M48";
          stringDEPT_CD_1 = "A";
        } else {
          stringDEPT_CD = stringProjectID1.substring(0, stringProjectID1.length() - 1);
          stringDEPT_CD_1 = stringProjectID1.substring(stringProjectID1.length() - 1, stringProjectID1.length());
        }
        if (bln289HasZ3) {
          stringDEPT_CD_1 = "C";
        }
        if (bln289HasZ0) {
          stringDEPT_CD_1 = "T";
        }
        stringOrderNo = retData[0][0];
        stringFE5DSql = "DELETE FROM FE5D34 WHERE DEPT_CD='" + stringDEPT_CD + "' AND DEPT_CD_1='" + stringDEPT_CD_1 + "' AND OBJECT_CD='" + stringObjectCd
            + "' AND ORDERNO='" + stringOrderNo + "'";
        dbFE5D.execFromPool(stringFE5DSql);
        stringFE5DSql = "INSERT INTO FE5D34(DEPT_CD, DEPT_CD_1, OBJECT_CD, ORDERNO, CONTRACTNO, LAST_USER, LAST_YMD) VALUES('" + stringDEPT_CD + "', '" + stringDEPT_CD_1
            + "', '" + stringObjectCd + "', '" + stringOrderNo + "', '" + stringContractNo + "', '" + getUser().toUpperCase() + "', '"
            + datetime.getToday("yymmdd") + "')";
        dbFE5D.execFromPool(stringFE5DSql);
      }
    }
    if ("內業業管".equals(stringState) && "送呈".equals(value)) {
      JTable jtable10 = getTable("table10");
      String stringAmtPercent = "0";
      for (int intRow = 0; intRow < jtable10.getRowCount(); intRow++) {
        stringAmtPercent = operation.floatAdd(stringAmtPercent, "" + getValueAt("table10", intRow, "AMT_PERCENT"), 4);
      }
      if (operation.compareTo(stringAmtPercent, "100") == 1) {
        message("拆款表百分比合計不等於100%!");
        return false;
      }
    }
    if (("內業業管".equals(stringState) || "行銷".equals(stringState)) && "送呈".equals(value)) {
      stringErrorMSG = "";
      // 檢核資料是否與購屋證明單一致
      stringSql = "select T278.Position, (case when convert(money,T278.PingSu)!=convert(money,T92.PingSu) then '坪數' else '' end) PingSu, "
          + "(case when convert(money,T278.DealMoney)!=convert(money,T92.DealMoney) then '售價' else '' end) DealMoney, "
          + "(case when convert(money,T278.GiftMoney)!=convert(money,T92.GiftMoney) then '佣金利息外贈送' else '' end) GiftMoney, "
          + "(case when convert(money,T278.CommMoney)!=convert(money,T92.CommMoney) then '佣金' else '' end) CommMoney, "
          + "(case when convert(money,T278.CommMoney1)!=convert(money,T92.CommMoney1) then '中原佣金' else '' end) CommMoney1, " + // 修改日期:20151022 員工編號:B3774
          "(case when convert(money,T278.ViMoney)!=convert(money,T92.ViMoney) then '贈送利息' else '' end) ViMoney, "
          + "(case when convert(money,T278.PureMoney)!=convert(money,(T92.DealMoney-T92.GiftMoney-T92.CommMoney-T92.CommMoney1-T92.ViMoney)) then '淨售' else '' end) PureMoney "
          + "from Sale05M278 T278, Sale05M092 T92 where T278.OrderNo=T92.OrderNo and T278.HouseCar=T92.HouseCar and T278.Position=T92.Position "
          + "and T278.ContractNo='" + stringContractNo + "' and T278.CompanyCd='" + stringCompanyCd + "' order by T278.OrderNo, T278.RecordNo";
      retData = dbSale.queryFromPool(stringSql);
      for (int intRow = 0; intRow < retData.length; intRow++) {
        stringErrorMSGTemp = "";
        for (int intCol = 1; intCol <= 6; intCol++) {
          if (retData[intRow][intCol].length() > 0) {
            stringErrorMSGTemp = stringErrorMSGTemp.length() == 0 ? (retData[intRow][0] + "：" + retData[intRow][intCol]) : stringErrorMSGTemp + "、" + retData[intRow][intCol];
          }
        }
        if (stringErrorMSGTemp.length() > 0) {
          stringErrorMSG = stringErrorMSG.length() == 0 ? stringErrorMSGTemp : (stringErrorMSG + "\n" + stringErrorMSGTemp);
        }
      }
      if (stringErrorMSG.length() > 0) {
        messagebox("以下資料與購屋證明單不一致：\n" + stringErrorMSG);
        return false;
      }
      // 檢核售價是否一致(棟樓資料、拆款表頁籤、拆款表表格)
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(DealMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))=CONVERT(decimal(20,4),(H_HL_Money+C_HL_Money))";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的售價合計和拆款表頁籤的房土總價+車位總價不一致!");
        return false;
      }
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(DealMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))="
          + "CONVERT(decimal(20,4),(select SUM(HL_Money) from Sale05M283_New where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的售價合計和拆款表表格的合約總價合計不一致!");
        return false;
      }
      // 檢核銀貸金額是否一致(棟樓資料、拆款表頁籤、拆款表表格、特約事項)
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(LoanMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))=CONVERT(decimal(20,4),(H_HL_LoanMoney+C_HL_LoanMoney))";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的銀貸金額合計和拆款表頁籤的房土銀貸+車位銀貸不一致!");
        return false;
      }
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(LoanMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))="
          + "CONVERT(decimal(20,4),(select ISNULL(SUM(HL_Money),0) from Sale05M283_New where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd and ITEMLS_CD IN ('BANK','HOME2','HOMEA')))";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的銀貸金額合計和拆款表表格的銀貸金額合計不一致!");
        return false;
      }
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(LoanMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))=CONVERT(decimal(20,4),LoanMoney)";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的銀貸金額合計和特約事項頁籤的銀貸金額不一致!");
        return false;
      }
      // 檢核贈送金額是否一致(棟樓資料、特約事項頁籤、贈送明細表格)
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select SUM(GiftMoney+ViMoney) from Sale05M278 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))=CONVERT(decimal(20,4),GiftMoney)";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的贈送金額合計和特約事項頁籤的贈送金額不一致!");
        return false;
      }
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(select ISNULL(SUM(TotalAmt),0) from Sale05M305 where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))=CONVERT(decimal(20,4),GiftMoney)";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("棟樓資料頁籤的贈送金額合計和贈送明細表格的總金額合計不一致!");
        return false;
      }
      // 檢核已收金額是否一致(拆款表頁籤、拆款表表格)
      stringSql = "select ContractNo from Sale05M274 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
          + "and CONVERT(decimal(20,4),(H_HL_ReceiveMoney+C_HL_ReceiveMoney))="
          + "CONVERT(decimal(20,4),(select SUM(HL_ReceiveMoney) from Sale05M283_New where ContractNo=Sale05M274.ContractNo and CompanyCd=Sale05M274.CompanyCd))";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        messagebox("拆款表頁籤的房土已收+拆款表頁籤和拆款表表格的已收金額合計不一致!");
        return false;
      }
    }
    stringSql = "select CmpCompanyCd from Sale05M294 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "'";
    retData = dbSale.queryFromPool(stringSql);
    if (retData.length > 0) {
      aryCompanyCd = new String[retData.length][4];
    }
    for (int intRow = 0; intRow < retData.length; intRow++) {
      if ("01".equals(retData[intRow][0])) {
        blnHas01 = true;
      }
      aryCompanyCd[intRow][0] = retData[intRow][0];
      stringSql = "select substring(COMPANY_NAME,1,4) from FED1023 where COMPANY_CD='" + retData[intRow][0] + "'";
      retFED1023 = dbFED1.queryFromPool(stringSql);
      if (retFED1023.length > 0) {
        aryCompanyCd[intRow][1] = retFED1023[0][0];
      }
    }
    if ("營業經辦".equals(stringState) && ("N".equals(stringStatus) || "NA".equals(stringStatus)) && !"退承辦".equals(value) && !"作廢存檔".equals(value) && !"退行銷".equals(value)) {
      String stringFE5DSql = "";
      String retFE5DData[][] = null;
      String stringObjectCd = getValue("OBJECT_CD").trim();
      if (stringObjectCd.length() == 0) {
        message("請輸入客戶代號!");
        return false;
      }
      String stringDeptCd = "";
      String stringDeptCd1 = "";
      String stringDeptCdAll = "";
      if ("A1".equals(stringProjectID1)) {
        stringDeptCd = "A01";
        stringDeptCd1 = "A";
        stringDeptCdAll = "A01A";
      } else if ("A2".equals(stringProjectID1)) {
        stringDeptCd = "A02";
        stringDeptCd1 = "A";
        stringDeptCdAll = "A02A";
      } else if ("H28".equals(stringProjectID1)) {
        stringDeptCd = "H28";
        stringDeptCd1 = "A";
        stringDeptCdAll = "H28A";
      } else if ("M48".equals(stringProjectID1)) {
        stringDeptCd = "M48";
        stringDeptCd1 = "A";
        stringDeptCdAll = "M48A";
      } else {
        stringDeptCd = stringProjectID1.substring(0, stringProjectID1.length() - 1);
        stringDeptCd1 = stringProjectID1.substring(stringProjectID1.length() - 1, stringProjectID1.length());
        stringDeptCdAll = stringProjectID1;
      }
      if ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H48".equals(stringDeptCd) || "H63".equals(stringDeptCd) || "H66".equals(stringDeptCd)
          || "H73".equals(stringDeptCd) || "H75".equals(stringDeptCd) || "H82".equals(stringDeptCd) || "H86".equals(stringDeptCd) || "H89".equals(stringDeptCd)
          || "H90".equals(stringDeptCd) || "H92".equals(stringDeptCd) || "H95".equals(stringDeptCd) || "H96".equals(stringDeptCd) || "H98".equals(stringDeptCd)
          || "H99".equals(stringDeptCd) || "H100".equals(stringDeptCd) || "M61".equals(stringDeptCd)
          || ("H102".equals(stringDeptCd) && (bln289AllCompany || bln289HasCS || bln289HasZ3)) || "H103".equals(stringDeptCd) || "H105".equals(stringDeptCd)
          || "H108".equals(stringDeptCd) || "H101".equals(stringDeptCd)) { // 要分開檢核01及CS
        for (int intRow = 0; intRow < aryCompanyCd.length; intRow++) {
          if ("01".equals(aryCompanyCd[intRow][0])) {
            if ("E2AI".equals(stringDeptCd)) {
              stringDeptCd1 = "I";
            } else {
              stringDeptCd1 = "A";
              if ("H101B".equals(stringProjectID1)) {
                stringDeptCd1 = "B";
              }
            }
          } else if ("CS".equals(aryCompanyCd[intRow][0])) {
            stringDeptCd1 = "T";
            if ("H101B".equals(stringProjectID1)) {
              stringDeptCd1 = "S";
            }
          } else if ("Z3".equals(aryCompanyCd[intRow][0])) {
            stringDeptCd1 = "C";
          } else if ("Z0".equals(aryCompanyCd[intRow][0]) && ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H89".equals(stringDeptCd))) {
            stringDeptCd1 = "T";
          }
          System.out.println("1stringDeptCd1=====>" + stringDeptCd1);
          stringFE5DSql = "SELECT OBJECT_ID, CONVERT(decimal(16,0),SALE_AMT) SALE_AMT, CONVERT(decimal(16,0),SELF_AMT) SELF_AMT, "
              + "CONVERT(decimal(16,0),BANK_AMT) BANK_AMT, CONVERT(decimal(16,0),CAR_SALE_AMT) CAR_SALE_AMT, CONVERT(decimal(16,0),CAR_SELF_AMT) CAR_SELF_AMT, "
              + "CONVERT(decimal(16,0),CAR_BANK_AMT) CAR_BANK_AMT, MAIL_ADDR, TEL_NO, EMAIL, SALE_PERSON, TRUST FROM FE5D05 "
              + "WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND RTRIM(DEPT_CD_1)='" + stringDeptCd1 + "' AND OBJECT_CD='" + stringObjectCd + "'";
          retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
          if (retFE5DData.length == 0) {
            message("在客服系統找不到" + aryCompanyCd[intRow][1] + "客戶代號");
            return false;
          }
          stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(DealMoney*10000)),0) DealMoney from Sale05M289 where ContractNo='" + stringContractNo + "' "
              + "and CompanyCd='" + stringCompanyCd + "' and HouseCar='House' ";
          if (("H75A".equals(stringProjectID1) || "H92A".equals(stringProjectID1) || "H102A".equals(stringProjectID1)) && "01".equals(aryCompanyCd[intRow][0])) {
            stringSql = stringSql + "and (HLCompanyCd='01' OR ComNo IN (" + stringPerson + "))";
          } else {
            stringSql = stringSql + "and HLCompanyCd='" + aryCompanyCd[intRow][0] + "'";
          }
          retData = dbSale.queryFromPool(stringSql);
          if (!retFE5DData[0][1].equals(retData[0][0])) {
            message("合約會審." + aryCompanyCd[intRow][1] + ".房售價不等於客服系統房售價");
          }
          stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(DealMoney*10000)),0) DealMoney from Sale05M289 where ContractNo='" + stringContractNo + "' "
              + "and CompanyCd='" + stringCompanyCd + "' and HouseCar='Car' ";
          if (("H75A".equals(stringProjectID1) || "H92A".equals(stringProjectID1) || "H102A".equals(stringProjectID1)) && "01".equals(aryCompanyCd[intRow][0])) {
            stringSql = stringSql + "and (HLCompanyCd='01' OR ComNo IN (" + stringPerson + "))";
          } else {
            stringSql = stringSql + "and HLCompanyCd='" + aryCompanyCd[intRow][0] + "'";
          }
          retData = dbSale.queryFromPool(stringSql);
          if (!retFE5DData[0][4].equals(retData[0][0])) {
            message("合約會審." + aryCompanyCd[intRow][1] + ".車售價不等於客服系統車售價");
          }
        }
      } else {
        System.out.println("2stringDeptCd1=====>" + stringDeptCd1);
        stringFE5DSql = "SELECT OBJECT_ID, CONVERT(decimal(16,0),SALE_AMT) SALE_AMT, CONVERT(decimal(16,0),SELF_AMT) SELF_AMT, "
            + "CONVERT(decimal(16,0),BANK_AMT) BANK_AMT, CONVERT(decimal(16,0),CAR_SALE_AMT) CAR_SALE_AMT, CONVERT(decimal(16,0),CAR_SELF_AMT) CAR_SELF_AMT, "
            + "CONVERT(decimal(16,0),CAR_BANK_AMT) CAR_BANK_AMT, MAIL_ADDR, TEL_NO, EMAIL, SALE_PERSON, TRUST FROM FE5D05 "
            + "WHERE RTRIM(DEPT_CD)+DEPT_CD_1='" + stringDeptCdAll + "' AND OBJECT_CD='" + stringObjectCd + "'";
        retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
        if (retFE5DData.length == 0) {
          message("在客服系統找不到客戶代號");
          return false;
        }
        stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(DealMoney*10000)),0) DealMoney from Sale05M278 where ContractNo='" + stringContractNo + "' "
            + "and CompanyCd='" + stringCompanyCd + "' and HouseCar='House'";
        retData = dbSale.queryFromPool(stringSql);
        if (!retFE5DData[0][1].equals(retData[0][0])) {
          message("合約會審.房售價不等於客服系統房售價");
          return false;
        }
        if (!stringContractNo.equals("033H601A1050807001")) {
          stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(DealMoney*10000)),0) DealMoney from Sale05M278 where ContractNo='" + stringContractNo + "' "
              + "and CompanyCd='" + stringCompanyCd + "' and HouseCar='Car'";
          retData = dbSale.queryFromPool(stringSql);
          if (!retFE5DData[0][4].equals(retData[0][0])) {
            message("合約會審.車售價不等於客服系統車售價");
            return false;
          }
        }
      }
    }
    if ("營業經辦".equals(stringState) && !"作廢存檔".equals(value)) {
      String stringObjectCd = getValue("OBJECT_CD").trim();
      if (stringObjectCd.length() == 0) {
        message("請輸入客戶代號!");
        return false;
      }
      stringSql = "select OrderNo from Sale05M275_New where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' and Position='"
          + (stringProjectID1.equals("H28") ? stringObjectCd : stringObjectCd.substring(0, 6)) + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        message("資料錯誤!");
        return false;
      } else {
        stringSql = "select OrderNo from Sale05M091 where OrderNo='" + retData[0][0] + "' and Nationality='1' and len(CustomNo)=8";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          String stringFE5DSql = "";
          String retFE5DData[][] = null;

          String stringDeptCd = "";
          String stringDeptCd1 = "";
          String stringDeptCdAll = "";
          //
          if ("A1".equals(stringProjectID1)) {
            stringDeptCd = "A01";
            stringDeptCd1 = "A";
            stringDeptCdAll = "A01A";
          } else if ("A2".equals(stringProjectID1)) {
            stringDeptCd = "A02";
            stringDeptCd1 = "A";
            stringDeptCdAll = "A02A";
          } else if ("H28".equals(stringProjectID1)) {
            stringDeptCd = "H28";
            stringDeptCd1 = "A";
            stringDeptCdAll = "H28A";
          } else if ("M48".equals(stringProjectID1)) {
            stringDeptCd = "M48";
            stringDeptCd1 = "A";
            stringDeptCdAll = "M48A";
          } else {
            stringDeptCd = stringProjectID1.substring(0, stringProjectID1.length() - 1);
            stringDeptCd1 = stringProjectID1.substring(stringProjectID1.length() - 1, stringProjectID1.length());
            stringDeptCdAll = stringProjectID1;
          }
          if ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H48".equals(stringDeptCd) || "H63".equals(stringDeptCd) || "H66".equals(stringDeptCd)
              || "H73".equals(stringDeptCd) || "H75".equals(stringDeptCd) || "H82".equals(stringDeptCd) || "H86".equals(stringDeptCd) || "H89".equals(stringDeptCd)
              || "H90".equals(stringDeptCd) || "H92".equals(stringDeptCd) || "H95".equals(stringDeptCd) || "H96".equals(stringDeptCd) || "H98".equals(stringDeptCd)
              || "H99".equals(stringDeptCd) || "H100".equals(stringDeptCd) || "M61".equals(stringDeptCd)
              || ("H102".equals(stringDeptCd) && (bln289AllCompany || bln289HasCS || bln289HasZ3)) || "H103".equals(stringDeptCd) || "H105".equals(stringDeptCd)
              || "H108".equals(stringDeptCd) || "H101".equals(stringDeptCd)) { // 要分開檢核01及CS
            for (int intRow = 0; intRow < aryCompanyCd.length; intRow++) {
              if ("01".equals(aryCompanyCd[intRow][0])) {
                if ("E2AI".equals(stringDeptCd)) {
                  stringDeptCd1 = "I";
                } else {
                  stringDeptCd1 = "A";
                  if ("H101B".equals(stringProjectID1)) {
                    stringDeptCd1 = "B";
                  }
                }
              } else if ("CS".equals(aryCompanyCd[intRow][0])) {
                stringDeptCd1 = "T";
                if ("H101B".equals(stringProjectID1)) {
                  stringDeptCd1 = "S";
                }
              } else if ("Z3".equals(aryCompanyCd[intRow][0])) {
                stringDeptCd1 = "C";
              } else if ("Z0".equals(aryCompanyCd[intRow][0]) && ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H89".equals(stringDeptCd))) {
                stringDeptCd1 = "T";
              }
              stringFE5DSql = "SELECT OBJECT_TYPE FROM FE5D05 WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND RTRIM(DEPT_CD_1)='" + stringDeptCd1 + "' "
                  + "AND OBJECT_CD='" + stringObjectCd + "'";
              retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
              if (retFE5DData.length == 0) {
                message("在客服系統找不到" + aryCompanyCd[intRow][1] + "客戶代號");
                return false;
              } else if (!"2".equals(retFE5DData[0][0])) {
                message("營利事業有錯!");
                return false;
              }
            }
          } else {
            stringFE5DSql = "SELECT OBJECT_TYPE FROM FE5D05 WHERE RTRIM(DEPT_CD)+DEPT_CD_1='" + stringDeptCdAll + "' AND OBJECT_CD='" + stringObjectCd + "'";
            retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
            if (retFE5DData.length == 0) {
              message("在客服系統找不到客戶代號");
              return false;
            } else if (!"2".equals(retFE5DData[0][0])) {
              message("營利事業有錯!");
              return false;
            }
          }
        }
      }
    }
    // 只要有01每關都要輸入內控簽核人、經辦都要檢核金額；其他只要經辦及會計檢核金額
    if ((stringState.indexOf("_出文") == -1 && ((blnHas01 && !"不動產".equals(stringState) && !"財務代銷".equals(stringState)
        && !(("承辦".equals(stringState) && ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)))))
        || (!("退承辦".equals(value) || "退營業".equals(value) || "退行銷".equals(value) || "作廢存檔".equals(value))
            && ("營業經辦".equals(stringState) || "開發經辦".equals(stringState) || "財務經辦".equals(stringState) || "會計".equals(stringState)))))
        || "作廢存檔".equals(value)) {
      showDialog("合約會審資料檢核", "", true, false, 350, 250, 280, 245);
      if ("Y".equals(getValue("IsCancelCheck"))) {
        return false;
      }
    }
    if ("營業經辦".equals(stringState) && ("N".equals(stringStatus) || "NA".equals(stringStatus)) && !"退承辦".equals(value) && !"作廢存檔".equals(value) && !"退行銷".equals(value)) {
      String stringFE5DSql = "";
      String strFE5D53Sql = "";
      String strFE5D53_91BenSql = "";
      String strFE5D53_91AgentSql = "";
      String retFE5DData[][] = null;
      String stringObjectCd = getValue("OBJECT_CD").trim();
      String stringDeptCd = "";
      String stringDeptCd1 = "";
      String stringDeptCdAll = "";
      if ("A1".equals(stringProjectID1)) {
        stringDeptCd = "A01";
        stringDeptCd1 = "A";
        stringDeptCdAll = "A01A";
      } else if ("A2".equals(stringProjectID1)) {
        stringDeptCd = "A02";
        stringDeptCd1 = "A";
        stringDeptCdAll = "A02A";
      } else if ("H28".equals(stringProjectID1)) {
        stringDeptCd = "H28";
        stringDeptCd1 = "A";
        stringDeptCdAll = "H28A";
      } else if ("M48".equals(stringProjectID1)) {
        stringDeptCd = "M48";
        stringDeptCd1 = "A";
        stringDeptCdAll = "M48A";
      } else {
        stringDeptCd = stringProjectID1.substring(0, stringProjectID1.length() - 1);
        stringDeptCd1 = stringProjectID1.substring(stringProjectID1.length() - 1, stringProjectID1.length());
        stringDeptCdAll = stringProjectID1;
      }
      if ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H48".equals(stringDeptCd) || "H63".equals(stringDeptCd) || "H66".equals(stringDeptCd)
          || "H73".equals(stringDeptCd) || "H75".equals(stringDeptCd) || "H82".equals(stringDeptCd) || "H86".equals(stringDeptCd) || "H89".equals(stringDeptCd)
          || "H90".equals(stringDeptCd) || "H92".equals(stringDeptCd) || "H95".equals(stringDeptCd) || "H96".equals(stringDeptCd) || "H98".equals(stringDeptCd)
          || "H99".equals(stringDeptCd) || "H100".equals(stringDeptCd) || "M61".equals(stringDeptCd)
          || ("H102".equals(stringDeptCd) && (bln289AllCompany || bln289HasCS || bln289HasZ3)) || "H103".equals(stringDeptCd) || "H105".equals(stringDeptCd)
          || "H108".equals(stringDeptCd) || "H101".equals(stringDeptCd)) { // 要分開檢核01及CS
        for (int intRow = 0; intRow < aryCompanyCd.length; intRow++) {
          if ("01".equals(aryCompanyCd[intRow][0])) {
            if ("E2AI".equals(stringDeptCd)) {
              stringDeptCd1 = "I";
            } else {
              stringDeptCd1 = "A";
              if ("H101B".equals(stringProjectID1)) {
                stringDeptCd1 = "B";
              }
            }
          } else if ("CS".equals(aryCompanyCd[intRow][0])) {
            stringDeptCd1 = "T";
            if ("H101B".equals(stringProjectID1)) {
              stringDeptCd1 = "S";
            }
          } else if ("Z3".equals(aryCompanyCd[intRow][0])) {
            stringDeptCd1 = "C";
          } else if ("Z0".equals(aryCompanyCd[intRow][0]) && ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H89".equals(stringDeptCd))) {
            stringDeptCd1 = "T";
          }
          System.out.println("3stringDeptCd1=====>" + stringDeptCd1);
          stringFE5DSql = "SELECT OBJECT_ID, CONVERT(decimal(16,0),SALE_AMT) SALE_AMT, CONVERT(decimal(16,0),SELF_AMT) SELF_AMT, "
              + "CONVERT(decimal(16,0),BANK_AMT) BANK_AMT, CONVERT(decimal(16,0),CAR_SALE_AMT) CAR_SALE_AMT, CONVERT(decimal(16,0),CAR_SELF_AMT) CAR_SELF_AMT, "
              + "CONVERT(decimal(16,0),CAR_BANK_AMT) CAR_BANK_AMT, MAIL_ADDR, TEL_NO, EMAIL, SALE_PERSON, TRUST FROM FE5D05 "
              + "WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND RTRIM(DEPT_CD_1)='" + stringDeptCd1 + "' AND OBJECT_CD='" + stringObjectCd + "'";
          retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
          // 回寫 FE5D05
          stringFE5DSql = "UPDATE FE5D05 SET APPOINT_DATE='"
              + ((datetime.subDays1(getToday("YYYYmmdd"), "" + get("put_use100YearDate")) < 0) ? convert.ac2roc(getValue("ContractDate").trim().replaceAll("/", ""))
                  : convert.add0(convert.ac2roc(getValue("ContractDate").trim().replaceAll("/", "")), "7"))
              + "' ";
          if (retFE5DData[0][7].trim().length() == 0 || retFE5DData[0][0].trim().length() == 0 || retFE5DData[0][8].trim().length() == 0
              || retFE5DData[0][9].trim().length() == 0) {
            stringSql = "select TOP 1 RTRIM(ZIP)+RTRIM(City)+RTRIM(Town)+RTRIM(Address), CustomNo, Tel, eMail from Sale05M277 where ContractNo='" + stringContractNo
                + "' and CompanyCd='" + stringCompanyCd + "'";
            retData = dbSale.queryFromPool(stringSql);
            if (retData.length > 0) {
              if (retFE5DData[0][7].trim().length() == 0) {
                stringFE5DSql = stringFE5DSql + ", MAIL_ADDR='" + retData[0][0] + "' ";
              }
              if (retFE5DData[0][0].trim().length() == 0) {
                stringFE5DSql = stringFE5DSql + ", OBJECT_ID='" + retData[0][1] + "' ";
              }
              if (retFE5DData[0][8].trim().length() == 0) {
                stringFE5DSql = stringFE5DSql + ", TEL_NO='" + retData[0][2] + "' ";
              }
              if (retFE5DData[0][9].trim().length() == 0) {
                stringFE5DSql = stringFE5DSql + ", EMAIL='" + retData[0][3] + "' ";
              }
            }
          }
          if (retFE5DData[0][10].trim().length() == 0) {
            stringSql = "select distinct SalesName from Sale05M295 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "'";
            retData = dbSale.queryFromPool(stringSql);
            if (retData.length > 0) {
              String stringSalesName = "";
              for (int intNo = 0; intNo < retData.length; intNo++) {
                stringSalesName = stringSalesName.length() == 0 ? retData[intNo][0] : (stringSalesName + "," + retData[intNo][0]);
              }
              stringSalesName = convert.StrToByte(stringSalesName);
              stringSalesName = stringSalesName.length() > 20 ? stringSalesName.substring(0, 20) : stringSalesName;
              stringSalesName = convert.ByteToStr(stringSalesName);
              stringFE5DSql = stringFE5DSql + ", SALE_PERSON='" + stringSalesName + "' ";
            }
          }
          stringSql = "select TOP 1 ISNULL(IsTrust,'N') IsTrust from Sale05M275_New where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd
              + "'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            stringFE5DSql = stringFE5DSql + ", TRUST='" + retData[0][0] + "' ";
          }
          stringSql = "select ISNULL((select Top 1 PingSu from Sale05M278 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
              + "and HouseCar='House' order by OrderNo, RecordNo),0) PingSu, ISNULL(ListPrice,0) ListPrice, "
              + "ISNULL(GiftMoneyCommMoney,0) GiftMoneyCommMoney, ISNULL(FloorPrice,0) FloorPrice from(select CONVERT(decimal(16,0),SUM(ListPrice*10000)) ListPrice, "
              + "CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)) GiftMoneyCommMoney, "
              + "CONVERT(decimal(16,0),SUM(FloorPrice*10000)) FloorPrice from Sale05M289 where ContractNo='" + stringContractNo + "' and CompanyCd='"
              + stringCompanyCd + "' and HouseCar='House' and HLCompanyCd='" + aryCompanyCd[intRow][0] + "') TempData";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            stringFE5DSql = stringFE5DSql + ", CONTRACT_CD='" + stringContractSerialNo + "' , SALE_UNIT=" + retData[0][0] + ", APPOINT_AMT=" + retData[0][1] + ", GIFT_AMT="
                + retData[0][2] + ", RESERVE_AMT=" + retData[0][3] + " ";
            stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(ListPrice*10000)),0) ListPrice, "
                + "ISNULL(CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)),0) GiftMoneyCommMoney, "
                + "ISNULL(CONVERT(decimal(16,0),SUM(FloorPrice*10000)),0) FloorPrice from Sale05M289 where ContractNo='" + stringContractNo + "' and CompanyCd='"
                + stringCompanyCd + "' and HouseCar='Car' and HLCompanyCd='" + aryCompanyCd[intRow][0] + "'";
            retData = dbSale.queryFromPool(stringSql);
            if (retData.length > 0) {
              stringFE5DSql = stringFE5DSql + ", CAR_CONTRACT_CD='" + stringContractSerialNo + "' , CAR_APPOINT_AMT=" + retData[0][0] + ", CAR_GIFT_AMT=" + retData[0][1]
                  + ", CAR_RESERVE_AMT=" + retData[0][2] + " ";
            }
          } else {
            stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(ListPrice*10000)),0) ListPrice, "
                + "ISNULL(CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)),0) GiftMoneyCommMoney, "
                + "ISNULL(CONVERT(decimal(16,0),SUM(FloorPrice*10000)),0) FloorPrice from Sale05M289 where ContractNo='" + stringContractNo + "' and CompanyCd='"
                + stringCompanyCd + "' and HouseCar='Car' and HLCompanyCd='" + aryCompanyCd[intRow][0] + "'";
            retData = dbSale.queryFromPool(stringSql);
            if (retData.length > 0) {
              stringFE5DSql = stringFE5DSql + ", CAR_CONTRACT_CD='" + stringContractSerialNo + "' , CAR_APPOINT_AMT=" + retData[0][0] + ", CAR_GIFT_AMT=" + retData[0][1]
                  + ", CAR_RESERVE_AMT=" + retData[0][2] + ", CONTRACT_CD='' ";
            }
          }
          stringFE5DSql = stringFE5DSql + "WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND DEPT_CD_1='" + stringDeptCd1 + "' AND OBJECT_CD='" + stringObjectCd + "'";
          aryCompanyCd[intRow][2] = stringFE5DSql;
        }
      } else {
        System.out.println("4stringDeptCd1=====>" + stringDeptCd1);
        stringFE5DSql = "SELECT OBJECT_ID, CONVERT(decimal(16,0),SALE_AMT) SALE_AMT, CONVERT(decimal(16,0),SELF_AMT) SELF_AMT, "
            + "CONVERT(decimal(16,0),BANK_AMT) BANK_AMT, CONVERT(decimal(16,0),CAR_SALE_AMT) CAR_SALE_AMT, CONVERT(decimal(16,0),CAR_SELF_AMT) CAR_SELF_AMT, "
            + "CONVERT(decimal(16,0),CAR_BANK_AMT) CAR_BANK_AMT, MAIL_ADDR, TEL_NO, EMAIL, SALE_PERSON, TRUST FROM FE5D05 "
            + "WHERE RTRIM(DEPT_CD)+DEPT_CD_1='" + stringDeptCdAll + "' AND OBJECT_CD='" + stringObjectCd + "'";
        retFE5DData = dbFE5D.queryFromPool(stringFE5DSql);
        // 回寫 FE5D05
        stringFE5DSql = "UPDATE FE5D05 SET APPOINT_DATE='"
            + ((datetime.subDays1(getToday("YYYYmmdd"), "" + get("put_use100YearDate")) < 0) ? convert.ac2roc(getValue("ContractDate").trim().replaceAll("/", ""))
                : convert.add0(convert.ac2roc(getValue("ContractDate").trim().replaceAll("/", "")), "7"))
            + "' ";
        if (retFE5DData[0][7].trim().length() == 0 || retFE5DData[0][0].trim().length() == 0 || retFE5DData[0][8].trim().length() == 0 || retFE5DData[0][9].trim().length() == 0) {
          stringSql = "select TOP 1 RTRIM(ZIP)+RTRIM(City)+RTRIM(Town)+RTRIM(Address), CustomNo, Tel, eMail from Sale05M277 where ContractNo='" + stringContractNo + "' "
              + "and CompanyCd='" + stringCompanyCd + "'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            if (retFE5DData[0][7].trim().length() == 0) {
              stringFE5DSql = stringFE5DSql + ", MAIL_ADDR='" + retData[0][0] + "' ";
            }
            if (retFE5DData[0][0].trim().length() == 0) {
              stringFE5DSql = stringFE5DSql + ", OBJECT_ID='" + retData[0][1] + "' ";
            }
            if (retFE5DData[0][8].trim().length() == 0) {
              stringFE5DSql = stringFE5DSql + ", TEL_NO='" + retData[0][2] + "' ";
            }
            if (retFE5DData[0][9].trim().length() == 0) {
              stringFE5DSql = stringFE5DSql + ", EMAIL='" + retData[0][3] + "' ";
            }
          }
        }
        if (retFE5DData[0][10].trim().length() == 0) {
          stringSql = "select distinct SalesName from Sale05M295 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            String stringSalesName = "";
            for (int intNo = 0; intNo < retData.length; intNo++) {
              stringSalesName = stringSalesName.length() == 0 ? retData[intNo][0] : (stringSalesName + "," + retData[intNo][0]);
            }
            stringSalesName = convert.StrToByte(stringSalesName);
            stringSalesName = stringSalesName.length() > 20 ? stringSalesName.substring(0, 20) : stringSalesName;
            stringSalesName = convert.ByteToStr(stringSalesName);
            stringFE5DSql = stringFE5DSql + ", SALE_PERSON='" + stringSalesName + "' ";
          }
        }
        stringSql = "select TOP 1 ISNULL(IsTrust,'N') IsTrust from Sale05M275_New where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd
            + "'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          stringFE5DSql = stringFE5DSql + ", TRUST='" + retData[0][0] + "' ";
        }
        stringSql = "select ISNULL((select Top 1 PingSu from Sale05M278 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
            + "and HouseCar='House' order by OrderNo, RecordNo),0) PingSu, ISNULL(ListPrice,0) ListPrice, "
            + "ISNULL(GiftMoneyCommMoney,0) GiftMoneyCommMoney, ISNULL(FloorPrice,0) FloorPrice from(select CONVERT(decimal(16,0),SUM(ListPrice*10000)) ListPrice, "
            + "CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)) GiftMoneyCommMoney, "
            + "CONVERT(decimal(16,0),SUM(FloorPrice*10000)) FloorPrice from Sale05M278 where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd
            + "' and HouseCar='House') TempData";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          stringFE5DSql = stringFE5DSql + ", CONTRACT_CD='" + stringContractSerialNo + "' , SALE_UNIT=" + retData[0][0] + ", APPOINT_AMT=" + retData[0][1] + ", GIFT_AMT="
              + retData[0][2] + ", RESERVE_AMT=" + retData[0][3] + " ";
          stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(ListPrice*10000)),0) ListPrice, "
              + "ISNULL(CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)),0) GiftMoneyCommMoney, "
              + "ISNULL(CONVERT(decimal(16,0),SUM(FloorPrice*10000)),0) FloorPrice from Sale05M278 where ContractNo='" + stringContractNo + "' and CompanyCd='"
              + stringCompanyCd + "' and HouseCar='Car'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            stringFE5DSql = stringFE5DSql + ", CAR_CONTRACT_CD='" + stringContractSerialNo + "' , CAR_APPOINT_AMT=" + retData[0][0] + ", CAR_GIFT_AMT=" + retData[0][1]
                + ", CAR_RESERVE_AMT=" + retData[0][2] + " ";
          }
        } else {
          stringSql = "select ISNULL(CONVERT(decimal(16,0),SUM(ListPrice*10000)),0) ListPrice, "
              + "ISNULL(CONVERT(decimal(16,0),SUM(GiftMoney*10000+CommMoney*10000+CommMoney1*10000+ViMoney*10000)),0) GiftMoneyCommMoney, "
              + "ISNULL(CONVERT(decimal(16,0),SUM(FloorPrice*10000)),0) FloorPrice from Sale05M278 where ContractNo='" + stringContractNo + "' and CompanyCd='"
              + stringCompanyCd + "' and HouseCar='Car'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            stringFE5DSql = stringFE5DSql + ", CAR_CONTRACT_CD='" + stringContractSerialNo + "' , CAR_APPOINT_AMT=" + retData[0][0] + ", CAR_GIFT_AMT=" + retData[0][1]
                + ", CAR_RESERVE_AMT=" + retData[0][2] + ", CONTRACT_CD='' ";
          }
        }
        stringFE5DSql = stringFE5DSql + "WHERE RTRIM(DEPT_CD)+DEPT_CD_1='" + stringDeptCdAll + "' AND OBJECT_CD='" + stringObjectCd + "'";
      }
      // 回寫 FE5D34
      stringSql = "select OrderNo from Sale05M275_New where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' and Position='"
          + (stringProjectID1.equals("H28") ? stringObjectCd : stringObjectCd.substring(0, 6)) + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length == 0) {
        message("資料錯誤!");
        return false;
      } else {
        if ("E02".equals(stringDeptCd) || "E2AI".equals(stringDeptCd) || "H48".equals(stringDeptCd) || "H63".equals(stringDeptCd) || "H66".equals(stringDeptCd)
            || "H73".equals(stringDeptCd) || "H75".equals(stringDeptCd) || "H82".equals(stringDeptCd) || "H86".equals(stringDeptCd) || "H89".equals(stringDeptCd)
            || "H90".equals(stringDeptCd) || "H92".equals(stringDeptCd) || "H95".equals(stringDeptCd) || "H96".equals(stringDeptCd) || "H98".equals(stringDeptCd)
            || "H99".equals(stringDeptCd) || "H100".equals(stringDeptCd) || "M61".equals(stringDeptCd)
            || ("H102".equals(stringDeptCd) && (bln289AllCompany || bln289HasCS || bln289HasZ3)) || "H103".equals(stringDeptCd) || "H105".equals(stringDeptCd)
            || "H108".equals(stringDeptCd) || "H101".equals(stringDeptCd)) { // 要分開回寫FE5D05 01及CS
          for (int intRow = 0; intRow < aryCompanyCd.length; intRow++) {
            dbFE5D.execFromPool(aryCompanyCd[intRow][2]); // 回寫FE5D05的
          }
        } else {
          dbFE5D.execFromPool(stringFE5DSql); // 回寫FE5D05的
        }
        // 更新/新增FE5D53
        String orderNo = "";
        // 091
        String strCountry = ""; // 國籍
        String strJobClassification = "";// 行業別
        String strJobTitle = "";// 職稱
        String strRiskValue = "";// 風險值
        // 090
        String strMoneySource = "";// 資金來源
        String strTradePurpose = "";// 交易目的
        // FE5D05
        String strObjectFullName = "";
        String strObjectID = "";

        String strCompareResult = "";// 比對結果

        String strCustomNo = "";
        String strCustomName = "";

        stringSql = "SELECT TOP 1 CustomNo, CustomName FROM Sale05M277 WHERE ContractNo='" + stringContractNo + "' AND CompanyCd='" + stringCompanyCd + "'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          strCustomNo = retData[0][0].trim();
          strCustomName = retData[0][1].trim();
        }
        stringSql = " SELECT TOP 1 CountryName,MajorName,PositionName,RiskValue FROM Sale05M091 WHERE OrderNo LIKE '%" + stringDeptCdAll + "%'and CustomNo = '" + strCustomNo
            + "' and CustomName = '" + strCustomName + "' ";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          strCountry = retData[0][0].trim();
          strJobClassification = retData[0][1].trim();
          strJobTitle = retData[0][2].trim();
          strRiskValue = retData[0][3].trim();
        }
        // 國籍轉碼
        stringSql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + strCountry + "'";
        retData = dbAS400.queryFromPool(stringSql);
        if (retData.length > 0) {
          strCountry = retData[0][0].trim();// 國家代碼
        } else {
          strCountry = "";
        }
        // 職業別轉碼
        stringSql = " SELECT CZ02 FROM PDCZPF WHERE CZ01='INDUSTRY' AND CZ02<> '' AND CZ09='" + strJobClassification + "' ";
        retData = dbAS400.queryFromPool(stringSql);
        if (retData.length > 0) {
          strJobClassification = retData[0][0].trim();
        } else {
          strJobClassification = "";
        }
        // 職稱轉碼
        stringSql = "SELECT PositionCD,PName FROM A_Position WHERE PName='" + strJobTitle + "'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          strJobTitle = retData[0][0].trim();// 職稱代碼
        } else {
          strJobTitle = "";
        }
        // 資金來源, 交易目的
        stringSql = "SELECT M90.ORDERNO, M90.FundsSrc ,M90.UseTYPE  FROM Sale05M090 M90,Sale05M092 M92 WHERE M90.ORDERNO = M92.ORDERNO AND M90.ProjectID1 = '" + stringDeptCd
            + stringDeptCd1 + "' AND M92.Position ='" + stringObjectCd + "'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          String strOrderNo = retData[0][0].trim();
          strMoneySource = retData[0][1].trim();
          strTradePurpose = retData[0][2].trim();

          stringSql = "SELECT TOP 1 CountryName,MajorName,PositionName,IsBlackList,IsControlList,IsLinked  FROM Sale05M091 WHERE OrderNo='" + strOrderNo + "'";
          retData = dbSale.queryFromPool(stringSql);
          if (retData.length > 0) {
            String isBlack = retData[0][3].trim();
            String isControll = retData[0][4].trim();
            String isLinked = retData[0][5].trim();
            if ("Y".equals(isBlack)) {// 比對結果
              strCompareResult = strCompareResult + "符合疑似黑名單,";
            } else {
              strCompareResult = strCompareResult + "不符合疑似黑名單,";
            }
            if ("Y".equals(isControll)) {
              strCompareResult = strCompareResult + "符合控管名單,";
            } else {
              strCompareResult = strCompareResult + "不符合控管名單,";
            }
            if ("Y".equals(isLinked)) {
              strCompareResult = strCompareResult + "為利害關係人,";
            } else {
              strCompareResult = strCompareResult + "非利害關係人,";
            }
          }
        }
        // 資金來源轉碼 怕JAVA不支援用IF寫
        if ("薪資".equals(strMoneySource)) {
          strMoneySource = "1";
        } else if ("出售不動產".equals(strMoneySource)) {
          strMoneySource = "2";
        } else if ("繼承財產".equals(strMoneySource)) {
          strMoneySource = "3";
        } else if ("商業經營獲利".equals(strMoneySource)) {
          strMoneySource = "4";
        } else if ("股匯市投資".equals(strMoneySource)) {
          strMoneySource = "5";
        } else if ("二等親內血/姻親出資".equals(strMoneySource)) {
          strMoneySource = "6";
        } else {
          strMoneySource = "7";
        }
        // 交易目的
        if ("首購置產".equals(strTradePurpose)) {
          strTradePurpose = "1";
        } else if ("換屋置產".equals(strTradePurpose)) {
          strTradePurpose = "2";
        } else if ("工作需求".equals(strTradePurpose)) {
          strTradePurpose = "3";
        } else if ("休閒觀光".equals(strTradePurpose)) {
          strTradePurpose = "4";
        } else if ("投資理財".equals(strTradePurpose)) {
          strTradePurpose = "5";
        } else if ("退休養老".equals(strTradePurpose)) {
          strTradePurpose = "6";
        } else {
          strTradePurpose = "7";
        }
        // 基本資料
        // 判斷05是否有資料沒有就新增不然就更新
        stringSql = "SELECT DEPT_CD,DEPT_CD_1, OBJECT_CD,OBJECT_TYPE,OBJECT_FULL_NAME,OBJECT_ID,OBJECT_BIRTHDAY FROM FE5D05  WHERE RTRIM(DEPT_CD)='" + stringDeptCd
            + "' AND OBJECT_CD='" + stringObjectCd + "' ";
        retData = dbFE5D.queryFromPool(stringSql);
        if (retData.length > 0) {
          for (int a = 0; a < retData.length; a++) {
            String strObjCD1 = retData[a][1].trim();
            String strObjType = retData[a][3].trim();
            String strObjName = retData[a][4].trim();
            String strObjID = retData[a][5].trim();
            String strObjBDay = retData[a][6].trim();
            stringSql = " SELECT * FROM FE5D53 WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND DEPT_CD_1 = '" + strObjCD1 + "' AND OBJECT_CD='" + stringObjectCd
                + "' AND OBJECT_TYPE_CD = '2'  ";
            String[][] ret53 = dbFE5D.queryFromPool(stringSql);
            if (ret53.length > 0) {// 更新
              strFE5D53Sql = "UPDATE FE5D53 SET COUNTRY = '" + strCountry + "', JOB_CLASSIFICATION = '" + strJobClassification + "', JOB_TITLE='" + strJobTitle
                  + "', RISK_RETURN = '" + strRiskValue + "' , COMPARE_RESULT = '" + strCompareResult + "', MONEY_SOURCE ='" + strMoneySource + "', TRADE_PURPOSE = '"
                  + strTradePurpose + "'  WHERE DEPT_CD = '" + stringDeptCd + "'  AND OBJECT_CD = '" + stringObjectCd + "' AND OBJECT_TYPE_CD = '2' AND OBJECT_ID = '" + strObjID
                  + "' ";
            } else {// 新增
              strFE5D53Sql = " INSERT INTO FE5D53 (DEPT_CD, DEPT_CD_1, OBJECT_CD, OBJECT_TYPE, OBJECT_TYPE_CD,OBJECT_NAME,OBJECT_ID,OBJECT_BIRTHDAY, COUNTRY, JOB_CLASSIFICATION, JOB_TITLE, MONEY_SOURCE, TRADE_PURPOSE, COMPARE_RESULT ,REGISTER_PLACE,REGISTER_DATE,CAREER,DESIGNATION,TEL_NO,MAIL_ADDR,RISK_RETURN)  "
                  + " VALUES ( '" + stringDeptCd + "','" + strObjCD1 + "','" + stringObjectCd + "','" + strObjType + "','2','" + strObjName + "','" + strObjID + "','" + strObjBDay
                  + "','" + strCountry + "','" + strJobClassification + "','" + strJobTitle + "','" + strMoneySource + "','" + strTradePurpose + "','" + strCompareResult
                  + "',' ',' ',' ',' ',' ',' ','" + strRiskValue + "');";
            }
            dbFE5D.execFromPool(strFE5D53Sql);
          }
        }
        // 其他案別未更新05 補更新
        // 銷售人, 訂價, 底價, 通訊地址, 身分證, 實質受益人
        // 此合約會審資料 案別 棟樓別 更新FED05
        // 1 取公司代碼 CompanyCd
        String strCompanyCd = "";
        String strSaleSql = "";
        String strOrderNo = "";
        String strAllCustomName = "";
        String strAllSalesName = "";
        String strObjectId = "";
        String strChairman = "";
        String strContactMan = "";
        String strMailAddr = "";

        String[][] retInvoice;

        // 取全名
        strSaleSql = "SELECT OrderNo FROM Sale05M275_New WHERE  ContractNo='" + stringContractNo + "'";
        String[][] retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          strOrderNo = retCustomName[0][0].trim();
        }
        strSaleSql = "SELECT CustomName FROM Sale05M091  WHERE OrderNo = '" + strOrderNo + "' AND StatusCd = '' ";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          for (int a = 0; a < retCustomName.length; a++) {
            if ("".equals(strAllCustomName)) {
              strAllCustomName = retCustomName[a][0].trim();
            } else {
              strAllCustomName = strAllCustomName + "," + retCustomName[a][0].trim();
            }
          }
        }
        // 取身分證號, 姓名, 地址
        strSaleSql = "SELECT TOP 1 CustomNo,CustomName,ZIP,City,Town, Address FROM Sale05M091  WHERE OrderNo = '" + strOrderNo
            + "' AND StatusCd = '' ORDER BY RecordNo,Percentage ";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          strObjectId = retCustomName[0][0].trim();
          strChairman = retCustomName[0][1].trim();
          strContactMan = retCustomName[0][1].trim();
          strMailAddr = retCustomName[0][2].trim() + retCustomName[0][3].trim() + retCustomName[0][4].trim() + retCustomName[0][5].trim();
        }
        // 取銷售人
        strSaleSql = "SELECT SalesName FROM Sale05M295 WHERE ContractNo='" + stringContractNo + "'";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          for (int a = 0; a < retCustomName.length; a++) {
            if ("".equals(strAllSalesName)) {
              strAllSalesName = retCustomName[a][0].trim();
            } else {
              strAllSalesName = strAllSalesName + "," + retCustomName[a][0].trim();
            }
          }
        }
        // 全名/業務名 長度處理
        if (strAllCustomName.length() > 19) {
          strAllCustomName = strAllCustomName.substring(0, 19);
        }
        if (strAllSalesName.length() > 9) {
          strAllSalesName = strAllSalesName.substring(0, 9);
        }
        if (strMailAddr.length() > 24) {
          strMailAddr = strMailAddr.substring(0, 24);
        }
        // 更新共用資料
        strFE5D53Sql = "UPDATE FE5D05 SET OBJECT_FULL_NAME='" + strAllCustomName + "', OBJECT_ID='" + strObjectId + "', MAIL_ADDR = '" + strMailAddr + "', SALE_PERSON='"
            + strAllSalesName + "'  WHERE DEPT_CD = '" + stringDeptCd + "' AND OBJECT_CD = '" + stringObjectCd + "' ";
        dbFE5D.execFromPool(strFE5D53Sql);
        // 更新訂價底價
        String strListPrice = "";
        String strFloorPrice = "";
        // 取棟樓資料
        strSaleSql = "SELECT ListPrice , FloorPrice  FROM Sale05M278 WHERE ContractNo  ='" + stringContractNo + "' AND HouseCar = 'House' ";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          strListPrice = retCustomName[0][0].trim();
          strFloorPrice = retCustomName[0][1].trim();
        }
        double dubListPrice = 0;
        double dubFloorPrice = 0;
        // FIX 若單買車位為空防呆
        if (!"".equals(strListPrice)) {
          dubListPrice = Double.parseDouble(strListPrice);
        }
        if (!"".equals(strFloorPrice)) {
          dubFloorPrice = Double.parseDouble(strFloorPrice);
        }
        dubListPrice = dubListPrice * 10000;
        dubFloorPrice = dubFloorPrice * 10000;
        DecimalFormat df = new DecimalFormat("0");
        strListPrice = df.format(dubListPrice);
        strFloorPrice = df.format(dubFloorPrice);
        // update FE5D05
        strFE5D53Sql = "UPDATE FE5D05 SET APPOINT_AMT='" + strListPrice + "', RESERVE_AMT= '" + strFloorPrice + "'   WHERE DEPT_CD = '" + stringDeptCd + "' AND OBJECT_CD = '"
            + stringObjectCd + "' AND DEPT_CD_1='A' ";
        dbFE5D.execFromPool(strFE5D53Sql);
        // 實益受益人
        String str91BenNo = "";
        String str91BenName = "";
        String str91BenBirthday = "";
        String str91BenCountryName = "";
        String str91BenIsBlackList = "";
        String str91BenIsControlList = "";
        String str91BenIsLinked = "";
        strSaleSql = "SELECT BCustomNo,BenName,Birthday,CountryName,IsBlackList,IsControlList,IsLinked FROM Sale05M091Ben WHERE OrderNo = '" + strOrderNo + "' ";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          for (int c = 0; c < retCustomName.length; c++) {
            str91BenNo = retCustomName[c][0].trim();
            str91BenName = retCustomName[c][1].trim();
            str91BenBirthday = retCustomName[c][2].trim();
            str91BenCountryName = retCustomName[c][3].trim();
            str91BenIsBlackList = retCustomName[c][4].trim();
            str91BenIsControlList = retCustomName[c][5].trim();
            str91BenIsLinked = retCustomName[c][6].trim();

            String str91BenCompareResult = "";
            // 西元年轉民國年
            if (str91BenBirthday.length() > 0) {
              String yyyy = str91BenBirthday.substring(0, 4);
              String mm = str91BenBirthday.substring(4, 6);
              String dd = str91BenBirthday.substring(6, 8);
              int intyyyy = Integer.parseInt(yyyy);
              intyyyy = intyyyy - 1911;
              str91BenBirthday = intyyyy + mm + dd;
            }
            // 國籍轉碼
            stringSql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + str91BenCountryName + "'";
            retData = dbAS400.queryFromPool(stringSql);
            if (retData.length > 0) {
              strCountry = retData[0][0].trim();// 國家代碼
            } else {
              strCountry = "";
            }
            str91BenCompareResult = "";
            if ("Y".equals(str91BenIsBlackList)) {// 比對結果
              str91BenCompareResult = str91BenCompareResult + "符合疑似黑名單,";
            } else {
              str91BenCompareResult = str91BenCompareResult + "不符合疑似黑名單,";
            }
            if ("Y".equals(str91BenIsControlList)) {
              str91BenCompareResult = str91BenCompareResult + "符合控管名單,";
            } else {
              str91BenCompareResult = str91BenCompareResult + "不符合控管名單,";
            }
            if ("Y".equals(str91BenIsLinked)) {
              str91BenCompareResult = str91BenCompareResult + "為利害關係人";
            } else {
              str91BenCompareResult = str91BenCompareResult + "非利害關係人";
            }
            // 判斷05是否有資料沒有就新增不然就更新
            stringSql = "SELECT DEPT_CD,DEPT_CD_1, OBJECT_CD,OBJECT_TYPE FROM FE5D05  WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND OBJECT_CD='" + stringObjectCd + "' ";
            retData = dbFE5D.queryFromPool(stringSql);
            if (retData.length > 0) {
              for (int d = 0; d < retData.length; d++) {
                String strObjCD1 = retData[d][1].trim();
                String strObjType = retData[d][3].trim();
                // 原53是否存在
                stringSql = " SELECT * FROM FE5D53 WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND DEPT_CD_1 = '" + strObjCD1 + "' AND OBJECT_CD='" + stringObjectCd
                    + "' AND OBJECT_TYPE_CD = '1'  AND OBJECT_ID = '" + str91BenNo + "' ";
                String[][] ret53 = dbFE5D.queryFromPool(stringSql);
                if (ret53.length > 0) {// 更新
                  strFE5D53Sql = "UPDATE FE5D53 SET COUNTRY = '" + strCountry + "', OBJECT_NAME = '" + str91BenName + "' , OBJECT_ID = '" + str91BenNo + "' , OBJECT_BIRTHDAY = '"
                      + str91BenBirthday + "', COMPARE_RESULT = '" + str91BenCompareResult + "'  WHERE DEPT_CD = '" + stringDeptCd + "'  AND DEPT_CD_1 = '" + strObjCD1
                      + "' AND OBJECT_CD='" + stringObjectCd + "' AND OBJECT_TYPE_CD = '1'   AND OBJECT_ID = '" + str91BenNo + "' ";
                } else {// 新增
                  strFE5D53Sql = " INSERT INTO FE5D53 (DEPT_CD, DEPT_CD_1, OBJECT_CD, OBJECT_TYPE, OBJECT_TYPE_CD,OBJECT_NAME,OBJECT_ID,OBJECT_BIRTHDAY, COUNTRY, JOB_CLASSIFICATION, JOB_TITLE, MONEY_SOURCE, TRADE_PURPOSE, COMPARE_RESULT ,REGISTER_PLACE,REGISTER_DATE,CAREER,DESIGNATION,TEL_NO,MAIL_ADDR,RISK_RETURN)  "
                      + " VALUES ( '" + stringDeptCd + "','" + strObjCD1 + "','" + stringObjectCd + "','" + strObjType + "','1','" + str91BenName + "','" + str91BenNo + "','"
                      + str91BenBirthday + "','" + strCountry + "',' ',' ',' ',' ','" + str91BenCompareResult + "',' ',' ',' ',' ',' ',' ',' ');";
                }
                dbFE5D.execFromPool(strFE5D53Sql);
              }
            }
          }
        }
        // 指定第三人
        String str91AgentNo = "";
        String str91AgentName = "";
        String str91AgentCountryName = "";
        String str91AgentIsBlackList = "";
        String str91AgentIsControlList = "";
        String str91AgentIsLinked = "";
        strSaleSql = "SELECT ACustomNo,AgentName,CountryName,IsBlackList,IsControlList,IsLinked FROM Sale05M091Agent WHERE OrderNo = '" + strOrderNo + "' ";
        retCustomName = dbSale.queryFromPool(strSaleSql);
        if (retCustomName.length > 0) {
          for (int e = 0; e < retCustomName.length; e++) {
            str91AgentNo = retCustomName[e][0].trim();
            str91AgentName = retCustomName[e][1].trim();
            str91AgentCountryName = retCustomName[e][2].trim();
            str91AgentIsBlackList = retCustomName[e][3].trim();
            str91AgentIsControlList = retCustomName[e][4].trim();
            str91AgentIsLinked = retCustomName[e][5].trim();

            String str91AgentCompareResult = "";
            // 國籍轉碼
            stringSql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + str91AgentCountryName + "'";
            retData = dbAS400.queryFromPool(stringSql);
            if (retData.length > 0) {
              strCountry = retData[0][0].trim();// 國家代碼
            } else {
              strCountry = "";
            }
            str91AgentCompareResult = "";
            if ("Y".equals(str91AgentIsBlackList)) {// 比對結果
              str91AgentCompareResult = str91AgentCompareResult + "符合疑似黑名單,";
            } else {
              str91AgentCompareResult = str91AgentCompareResult + "不符合疑似黑名單,";
            }
            if ("Y".equals(str91AgentIsControlList)) {
              str91AgentCompareResult = str91AgentCompareResult + "符合控管名單,";
            } else {
              str91AgentCompareResult = str91AgentCompareResult + "不符合控管名單,";
            }
            if ("Y".equals(str91AgentIsLinked)) {
              str91AgentCompareResult = str91AgentCompareResult + "為利害關係人";
            } else {
              str91AgentCompareResult = str91AgentCompareResult + "非利害關係人";
            }
            // 判斷05是否有資料沒有就新增不然就更新
            stringSql = "SELECT DEPT_CD,DEPT_CD_1, OBJECT_CD,OBJECT_TYPE FROM FE5D05  WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND OBJECT_CD='" + stringObjectCd + "' ";
            retData = dbFE5D.queryFromPool(stringSql);
            if (retData.length > 0) {
              for (int f = 0; f < retData.length; f++) {
                String strObjCD1 = retData[f][1].trim();
                String strObjType = retData[f][3].trim();
                // 原53是否存在
                stringSql = " SELECT * FROM FE5D53 WHERE RTRIM(DEPT_CD)='" + stringDeptCd + "' AND DEPT_CD_1 = '" + strObjCD1 + "' AND OBJECT_CD='" + stringObjectCd
                    + "' AND OBJECT_TYPE_CD = '3'  AND OBJECT_ID = '" + str91AgentNo + "' ";
                String[][] ret53 = dbFE5D.queryFromPool(stringSql);
                if (ret53.length > 0) {// 更新
                  strFE5D53Sql = "UPDATE FE5D53 SET COUNTRY = '" + strCountry + "', OBJECT_NAME = '" + str91AgentName + "' , OBJECT_ID = '" + str91AgentNo
                      + "' , COMPARE_RESULT = '" + str91AgentCompareResult + "'  WHERE DEPT_CD = '" + stringDeptCd + "'  AND DEPT_CD_1 = '" + strObjCD1 + "' AND OBJECT_CD='"
                      + stringObjectCd + "' AND OBJECT_TYPE_CD = '3'   AND OBJECT_ID = '" + str91AgentNo + "' ";
                } else {// 新增
                  strFE5D53Sql = " INSERT INTO FE5D53 (DEPT_CD, DEPT_CD_1, OBJECT_CD, OBJECT_TYPE, OBJECT_TYPE_CD,OBJECT_NAME,OBJECT_ID,OBJECT_BIRTHDAY, COUNTRY, JOB_CLASSIFICATION, JOB_TITLE, MONEY_SOURCE, TRADE_PURPOSE, COMPARE_RESULT ,REGISTER_PLACE,REGISTER_DATE,CAREER,DESIGNATION,TEL_NO,MAIL_ADDR,RISK_RETURN)  "
                      + " VALUES ( '" + stringDeptCd + "','" + strObjCD1 + "','" + stringObjectCd + "','" + strObjType + "','3','" + str91AgentName + "','" + str91AgentNo
                      + "',' ','" + strCountry + "',' ',' ',' ',' ','" + str91AgentCompareResult + "',' ',' ',' ',' ',' ',' ',' ');";
                }
                dbFE5D.execFromPool(strFE5D53Sql);
              }
            }
          }
        }
        String stringDEPT_CD = "";
        String stringDEPT_CD_1 = "";
        String stringOrderNo = "";
        if ("A1".equals(stringProjectID1)) {
          stringDEPT_CD = "A01";
          stringDEPT_CD_1 = "A";
        } else if ("A2".equals(stringProjectID1)) {
          stringDEPT_CD = "A02";
          stringDEPT_CD_1 = "A";
        } else if ("H28".equals(stringProjectID1)) {
          stringDEPT_CD = "H28";
          stringDEPT_CD_1 = "A";
        } else if ("M48".equals(stringProjectID1)) {
          stringDEPT_CD = "M48";
          stringDEPT_CD_1 = "A";
        } else {
          stringDEPT_CD = stringProjectID1.substring(0, stringProjectID1.length() - 1);
          stringDEPT_CD_1 = stringProjectID1.substring(stringProjectID1.length() - 1, stringProjectID1.length());
        }
        if (bln289HasZ3) {
          stringDEPT_CD_1 = "C";
        }
        if (bln289HasZ0) {
          stringDEPT_CD_1 = "T";
        }
        stringOrderNo = retData[0][0];
        stringFE5DSql = "DELETE FROM FE5D34 WHERE DEPT_CD='" + stringDEPT_CD + "' AND DEPT_CD_1='" + stringDEPT_CD_1 + "' AND OBJECT_CD='" + stringObjectCd
            + "' AND ORDERNO='" + stringOrderNo + "'";
        dbFE5D.execFromPool(stringFE5DSql);
        stringFE5DSql = "INSERT INTO FE5D34(DEPT_CD, DEPT_CD_1, OBJECT_CD, ORDERNO, CONTRACTNO, LAST_USER, LAST_YMD) VALUES('" + stringDEPT_CD + "', '" + stringDEPT_CD_1
            + "', '" + stringObjectCd + "', '" + stringOrderNo + "', '" + stringContractNo + "', '" + getUser().toUpperCase() + "', '"
            + datetime.getToday("yymmdd") + "')";
        dbFE5D.execFromPool(stringFE5DSql);
      }
    }
    String stringSigner = getValue("Signer").trim();
    if (blnHas01 && (("承辦".equals(stringState) && ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus))) || "財務代銷".equals(stringState))) {
      stringSigner = getUser().toUpperCase();
    }
    // 寫內控簽核人
    if (blnHas01) {
      stringSql = "update Sale05M274_FLOWC_HIS set Signer='" + stringSigner + "' where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd
          + "' and F_INP_TIME=(select top 1 F_INP_TIME from Sale05M274_FLOWC_HIS where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd
          + "' order by F_INP_TIME desc)";
      addToTransaction(stringSql);
    }
    // 寫審核
    stringSql = "update Sale05M274_FLOWC_HIS set ApplyOrReject='" + value + "' where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
        + "and F_INP_TIME=(select top 1 F_INP_TIME from Sale05M274_FLOWC_HIS where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "' "
        + "order by F_INP_TIME desc)";
    addToTransaction(stringSql);
    // 結案後由行銷取回，承辦要設為取回通知者
    if ("營業_出文".equals(stringState) && "END".equals(stringGetBackDocStatus)) {
      stringSql = "update Sale05M274_FLOWC set F_INP_ID='" + stringNotifyUser + "', F_INP_INFO='|[USER],[APPROVE],;|' where ContractNo='" + stringContractNo + "' "
          + "and CompanyCd='" + stringCompanyCd + "'";
      addToTransaction(stringSql);
    }
    // 營業結案 - 要做公文追蹤結案的處理
    if ("營業結案".equals(stringState) && "核准審核結束".equals(value)) {
      String stringBarCode = getValue("BarCode").trim();
      String stringLastDepart = "";
      String stringLastDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
      String stringDocStatus = "3";
      String stringDeptCd = "";
      String stringCDate = datetime.getToday("yy/mm/dd");
      String stringCTime = datetime.getTime("h:m:s");
      String stringEDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
      String stringDepartNo = "";
      String stringEmployeeNo = getUser().toUpperCase();
      //
      stringSql = "SELECT substring(dept_cd,1,3) FROM FE3D05 where emp_no='" + getUser().toUpperCase() + "'";
      retData = dbFE3D.queryFromPool(stringSql);
      if (retData.length > 0) {
        stringDeptCd = retData[0][0];
      }
      stringLastDepart = stringDeptCd + (stringProjectID1.length() > 4 ? stringProjectID1.substring(0, 4) : stringProjectID1);
      stringDepartNo = stringLastDepart;
      stringSql = "select ID1, EDateTime from Doc1M040 where BarCode='" + stringBarCode + "' order by EDateTime";
      retData = dbDoc.queryFromPool(stringSql);
      if (retData.length == 0) {
        message("無對應的Doc1M040資料!");
        return false;
      }
      //
      long longCheckSecond = exeUtil.getSubSecond(stringLastDateTime, retData[retData.length - 1][1].trim());
      String stringCheckDateTime = exeUtil.getSecondToTime(longCheckSecond);
      stringSql = "update Doc1M040 set CheckDateTime='" + stringCheckDateTime + "', CheckSecond=" + longCheckSecond + " where ID1="
          + retData[retData.length - 1][0].trim();
      dbDoc.execFromPool(stringSql);
      // 修改Doc1M030
      stringSql = "update Doc1M030 set DocClose='Y', LastDepart='" + stringLastDepart + "', LastDateTime='" + stringLastDateTime + "', DocStatus='"
          + stringDocStatus + "' where BarCode='" + stringBarCode + "'";
      dbDoc.execFromPool(stringSql);
      // 新增Doc1M040
      stringSql = "INSERT Doc1M040 (BarCode,             CDate,              CTime, EDateTime,          DepartNo,        EmployeeNo, "
          + "CheckDateTime, CheckSecond, DocStatus)  VALUES ( '" + stringBarCode + "',  '" + stringCDate + "',  '" + stringCTime + "',  '" + stringEDateTime
          + "',  '" + stringDepartNo + "',  '" + stringEmployeeNo + "',  '結案',   null,  '" + stringDocStatus + "') ";
      dbDoc.execFromPool(stringSql);
      stringSql = "SELECT MAX(ID1) FROM Doc1M040 WHERE BarCode='" + stringBarCode + "'";
      retData = dbDoc.queryFromPool(stringSql);
      if (retData.length > 0) {
        stringSql = "INSERT INTO DOC1CM040 (DI_AL_SERIAL, DI_AL_DATETIME) VALUES(" + retData[0][0] + ", GETDATE())";
        dbDoc.execFromPool(stringSql);
      }
    }
    if ("作廢存檔".equals(value)) {
      stringSql = "update Sale05M274 set IsVoid='Y', VoidUser='" + getUser().toUpperCase() + "', VoidSysDateTime='" + datetime.getTime("YYYY/mm/dd h:m:s") + "' "
          + "where ContractNo='" + stringContractNo + "' and CompanyCd='" + stringCompanyCd + "'";
      addToTransaction(stringSql);
      message("作廢存檔成功!");
    } else {
      setValue("VoidDate", "");
      setValue("VoidReason", "");
      setValue("VoidRemark", "");
    }
    return true;
  }

  public String getInformation() {
    return "---------------(Sale05M27401)簽核按鈕動作.preProcess()----------------";
  }
}