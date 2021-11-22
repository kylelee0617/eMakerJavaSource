package Doc.Doc6M010;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import cLabel;
import Farglory.util.FargloryUtil;
import Doc.Doc2M010;

public class FormLoad extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    Doc.Doc2M010 exeFun = new Doc.Doc2M010();
    FargloryUtil exeUtil = new FargloryUtil();
    boolean booleanSource = getFunctionName().indexOf("Doc5M") == -1;
    //
    put("PayTypeControl", "N");
    getButton("VoucherFlowNoButton").setLabel("");
    // 預設值
    doBatchDefaultValue(booleanSource, exeUtil, exeFun);
    // 流程控制
    doFlowControl(exeUtil, exeFun);
    //
    if ("SYS".equals(getUser())) return value;
    // 欄位
    doControlField(booleanSource, exeFun);
    getButton("Button4").doClick();
    // 表格
    doControlTable1(exeFun); // 發票
    doControlTable2(exeUtil, exeFun); // 個人收據
    doControlTable6(exeUtil, exeFun); // 費用
    //
    doKindNoD();
    //
    boolean booleanFlag = "".equals(getButton("VoucherFlowNoButton").getLabel());
    if (getFunctionName().indexOf("人總") != -1) {
      booleanFlag = false;
    }
    if ("B3018".equals(getUser())) {
      booleanFlag = true;
      setEditable("UNDERGO_WRITE", true);
    }
    //
    doControlFieldEnabled(booleanFlag, exeUtil);
    getButton("ButtonTable1Status").doClick();
    getButton("ButtonTable2Status").doClick();
    //
    setVisible("ButtonLastFunction", false);
    if (getFunctionName().indexOf("POP") != -1) {
      setVisible("button2", false);
      setVisible("button1", false);
      setVisible("ButtonLastFunction", true);
      setValue("LastFunction", "" + get("Doc5Q011_Source"));
    }
    // 代銷合約 按鈕狀態判斷
    put("Doc7M02691_STATUS", "VIEW");
    getButton("ButtonTable16").doClick();
    getButton("ButtonPayType").doClick();
    getButton("ButtonUseStatus").doClick();
    put("Doc6M010_ReceiptTaxType", "null");
    put("PayTypeControl", "Y");
    //
    return value;
  }

  public void doKindNoD() throws Throwable {
    getButton("ButtonKindNoD").doClick();
    //
    String stringKindNoD = getValue("KindNoD").trim();
    Vector retVector = (Vector) get("Doc5M030_KindNoDs");
    Vector vectorKindNoDValue = (Vector) retVector.get(0);
    Vector vectorKindNoDView = (Vector) retVector.get(1);
    //
    setReference("KindNoD", vectorKindNoDView, vectorKindNoDValue);
    //
    if (vectorKindNoDView.size() == 1) {
      setValue("KindNoD", "" + vectorKindNoDValue.get(0));
    }
  }

  public void doNOTEnabled(FargloryUtil exeUtil) throws Throwable {
    if (getFunctionName().indexOf("人總") == -1) return;
    //
    setEditable("DepartNo", false);
    // setEditable("DocNo2", false) ;
    setEditable("DocNo3", false);
    setEditable("button2", false);
    // 發票
    JTable jtable = getTable("Table1");
    getTableButton("Table1", 0).setEnabled(false);
    getTableButton("Table1", 1).setEnabled(false);
    getTableButton("Table1", 2).setEnabled(false);
    // 個人收據
    jtable = getTable("Table2");
    getTableButton("Table2", 0).setEnabled(false);
    getTableButton("Table2", 1).setEnabled(false);
    getTableButton("Table2", 2).setEnabled(false);
    // 酬佣戶別
    jtable = getTable("Table3");
    getTableButton("Table3", 0).setEnabled(false);
    getTableButton("Table3", 1).setEnabled(false);
    getTableButton("Table3", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "◎", 0);
    exeUtil.doChangeTableField(jtable, "◎", 0);
    // 費用
    jtable = getTable("Table6");
    getTableButton("Table6", 0).setEnabled(false);
    getTableButton("Table6", 1).setEnabled(false);
    getTableButton("Table6", 2).setEnabled(false);
    //
    setVisible("Button7", true);
  }

  public void doControlFieldEnabled(boolean booleanEnable, FargloryUtil exeUtil) throws Throwable {
    String stringDepartNoSubject = "" + get("EMP_DEPT_CD");
    String stringFunctionName = getFunctionName();
    // 公文編碼
    if ("0231".equals(stringDepartNoSubject) && stringFunctionName.indexOf("審核") == -1) {
      // setEditable("DocNo2", false) ;
      setEditable("DocNo3", false);
      setEditable("DepartNo", false);
      setEditable("BarCode", false);
    }
    // 公司
    boolean booleanFlag = booleanEnable && (stringFunctionName.indexOf("經辦") != -1 || stringFunctionName.indexOf("承辦") != -1);
    setEditable("ComNo", booleanFlag); // 公司
    // 承辦人員
    setEditable("OriEmployeeNo", booleanEnable);
    // 需用日期
    setEditable("NeedDate", booleanEnable);
    // 報銷方式
    setEditable("PayType", booleanEnable);
    // 公文內容
    setEditable("Descript", booleanEnable);
    // 公文預定結案日期
    // setEditable("PreFinDate", booleanEnable) ;
    // 付款條件 1~2
    setEditable("PayCondition1", booleanEnable);
    setEditable("PayCondition2", booleanEnable);
    // 預定報銷日期
    setEditable("DestineExpenseDate", booleanEnable);
    // 酬庸戶別
    for (int intNo = 0; intNo < getTable("Table3").getRowCount(); intNo++) {
      // 案別
      setEditable("Table3", intNo, "ProjectID1", booleanEnable);
      // 格式
      setEditable("Table3", intNo, "Position", booleanEnable);
      // 所得金額
      setEditable("Table3", intNo, "InvoiceTotalMoney", booleanEnable);
      setEditable("Table3", intNo, "SignDate2", booleanEnable);
      setEditable("Table3", intNo, "SignDate", booleanEnable);
      setEditable("Table3", intNo, "ProjectID1Use", booleanEnable);
    }
    doNOTEnabled(exeUtil);
  }

  public void doBatchDefaultValue(boolean booleanSource, FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    // 本次請購金額
    String stringThisPurchaseMoney = ("Y".equals(getValue("PurchaseNoExist").trim())) ? getValue("RealMoneySum").trim() : "0";
    setValue("ThisPurchaseMoney", stringThisPurchaseMoney);
    //
    setValue("BarCodeOld", getValue("BarCode"));
    setValue("DocNoOld", getValue("DocNo"));
    //

    //
    if (POSITION != 1) {
      getButton("ButtonComName").doClick();
      return;
    }
    setValue("FactoryNo", "");
    // 公文類別
    String stringKindNo = getValue("KindNo").trim();
    if ("".equals(stringKindNo)) {
      // String stringTime = datetime.getTime("YYYY/mm/dd h:m:s").trim() ;
      stringKindNo = "26";
      setValue("KindNo", "26");
    }
    // 時間
    String stringCDate = getToday("yy/mm/dd").trim();
    String stringCTime = datetime.getTime("h:m:s");
    String stringEDateTime = exeUtil.getDateConvert(stringCDate) + " " + stringCTime;
    //
    setValue("CDate", stringCDate);
    setValue("CTime", stringCTime);
    setValue("EDateTime", stringEDateTime);
    // 需用日期
    String stringNeedDate = stringCDate;

    // 公文預定結案日期
    String stringYear = datetime.getYear(stringCDate.replaceAll("/", ""));
    String stringMonth = datetime.getMonth(stringCDate.replaceAll("/", ""));
    String stringPreFinDate = getValue("PreFinDate").trim();
    String stringKindDay = exeFun.getKindDay(stringKindNo);
    boolean boolean11Month = "11".equals(stringMonth);
    stringCDate = convert.replace(stringCDate, "/", "");
    stringPreFinDate = datetime.dateAdd(stringCDate, "d", exeUtil.doParseInteger(stringKindDay));
    if (boolean11Month && (stringYear + "/12/31").compareTo(stringPreFinDate) < 0) {
      stringPreFinDate = stringYear + "1231";
    }
    stringPreFinDate = exeUtil.getDateConvertFullRoc(stringPreFinDate);
    setValue("PreFinDate", stringPreFinDate);
    //

    // 預定報銷日期
    String stringDestineExpenseDate = getValue("DestineExpenseDate").trim();
    stringDestineExpenseDate = datetime.dateAdd(stringNeedDate.replaceAll("/", ""), "m", 1);
    if (boolean11Month && (stringYear + "/12/31").compareTo(stringDestineExpenseDate) < 0) {
      stringDestineExpenseDate = stringYear + "1231";
    }
    stringDestineExpenseDate = exeUtil.getDateConvertFullRoc(stringDestineExpenseDate);
    setValue("DestineExpenseDate", stringDestineExpenseDate);
    //
    
    // 公司
    String stringComNo = getValue("ComNo").trim();
    String stringUser = getUser().toUpperCase();
    if ("".equals(stringComNo)) {
      if (booleanSource) {
        stringComNo = "CS";
      } else {
        stringComNo = getComNoForEmpNo(stringUser);
      }
    }
    setValue("ComNo", stringComNo);
    getButton("ButtonComName").doClick();
    //
    String stringPurchaseNoExist = getValue("PurchaseNoExist").trim();
    if ("".equals(stringPurchaseNoExist)) setValue("PurchaseNoExist", "Y");
    // 公文編碼
    stringCDate = exeUtil.getDateConvertRoc(stringCDate).replaceAll("/", "");
    String stringDocNo2 = datetime.getYear(stringCDate) + datetime.getMonth(stringCDate);
    setValue("DocNo2", stringDocNo2);
    // 部門
    /*
     * String stringDepartNo = getValue("DepartNo").trim() ;
     * if("".equals(stringDepartNo)) { if(booleanSource) { String[][]
     * retDoc1Password = null ; // DEPT_CD, EMP_NO retDoc1Password =
     * exeFun.getFE3D103(stringUser, "", getToday("yymmdd")) ;
     * if(retDoc1Password.length != 0) { String stringDepartName = "" ; //
     * stringDepartNo = retDoc1Password[0][0].trim( ) ; stringDepartName =
     * exeFun.getDepartName(stringDepartNo) ; if("".equals(stringDepartName)) {
     * stringDepartNo += "A" ; stringDepartName =
     * exeFun.getDepartName(stringDepartNo) ; if("".equals(stringDepartName))
     * stringDepartNo = "" ; } } else { // 0 DepartType 1 EmployeeName 2 DepartNo 3
     * UseLevel 4 ComNo retDoc1Password = exeFun.getDoc1Password(stringUser) ;
     * if(retDoc1Password.length != 0) { stringDepartNo =
     * retDoc1Password[0][2].trim( ) ; } } if("0000".equals(stringDepartNo) &&
     * "B2829".equals(getUser().toUpperCase())) {stringDepartNo = "0231" ;} } else {
     * String[][] retFE3D05 = exeFun.getFE3D05(stringUser) ; // DEPT_CD, EMP_NO
     * if(retFE3D05.length > 0) { stringDepartNo = retFE3D05[0][0].trim() ; //
     * String stringDepartName = exeFun.getDepartName(stringDepartNo) ;
     * if("".equals(stringDepartName)) { String[][] retDoc7M040 =
     * exeFun.getDoc7M040("", "", "Y",
     * " AND  GroupType  LIKE  '"+stringDepartNo.substring(0,stringDepartNo.length()
     * -1)+"%' ") ; // if(retDoc7M040.length > 0) { stringDepartNo =
     * retDoc7M040[0][0].trim() ; } else { stringDepartNo = "" ; } } } }
     * setValue("DepartNo", stringDepartNo) ; setValue("DocNo1", stringDepartNo) ; }
     * setValue("DocNo3", "") ;
     */
  }

  public void doFlowControl(FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    String stringFlow = getFunctionName();
    // 033 FG 按鈕控管
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    boolean booleanFlag = stringSpecBudget.indexOf(getValue("DepartNo").trim()) != -1 && "Z6".equals(getValue("ComNo").trim());
    if ("Y".equals(getValue("PurchaseNoExist"))) {
      getButton("Button033FGInput").setLabel("特殊預算控管 細項查詢");
    } else {
      getButton("Button033FGInput").setLabel("特殊預算控管 細項輸入");
    }
    setVisible("Button033FGInput", booleanFlag);
    //
    setEditable("BarCode", false); // BarCode
    setEditable("KindNo", false); // KindNo
    //
    getButton("Button2").setVisible(false); // 簽核
    getButton("Button5").setVisible(false); // 下一筆
    getButton("Button7").setVisible(false); // 退件
    getButton("ButtonError").setVisible(false); // 作廢
    // 發票
    JTable jtable1 = getTable("Table1");//
    exeUtil.doChangeTableField(jtable1, "BarCode", 0);
    // exeUtil.doChangeTableField(jtable1, "格式", 0) ; /* [格式] 隱藏*/
    // exeUtil.doChangeTableField(jtable1, "發票未稅金額", 0) ; /* [發票未稅金額] 隱藏*/
    // exeUtil.doChangeTableField(jtable1, "發票稅額", 0) ; /* [發票稅額] 隱藏*/
    exeUtil.doChangeTableField(jtable1, "扣抵代號", 0);
    getTableButton("Table1", 0).setEnabled(true);// 新增
    getTableButton("Table1", 2).setEnabled(true);// 刪除
    // 扣繳
    JTable jtable2 = getTable("Table2");
    exeUtil.doChangeTableField(jtable2, "BarCode", 0);
    exeUtil.doChangeTableField(jtable2, "對應", 120);
    // exeUtil.doChangeTableField(jtable2, "格式", 0) ; /* [格式] */
    // exeUtil.doChangeTableField(jtable2, "所得淨額", 0) ; /* [所得淨額] */
    // exeUtil.doChangeTableField(jtable2, "扣繳金額", 0) ; /* [扣繳金額] */
    // exeUtil.doChangeTableField(jtable2, "稅率", 0) ; /* [格式] */
    exeUtil.doChangeTableField(jtable2, "<html>所得給付票據<br>到期日", 0); /* [格式] */
    getTableButton("Table2", 0).setEnabled(true);// 新增
    getTableButton("Table2", 2).setEnabled(true);// 刪除
    // 酬庸戶別
    JTable jtable3 = getTable("Table3");
    exeUtil.doChangeTableField(jtable3, "BarCode", 0);
    exeUtil.doChangeTableField(jtable3, "◎", 24);
    // 費用
    JTable jtable6 = getTable("Table6");
    exeUtil.doChangeTableField(jtable6, "BarCode", 0);
    exeUtil.doChangeTableField(jtable6, "◎1", 24);
    exeUtil.doChangeTableField(jtable6, "◎2", 24);
    exeUtil.doChangeTableField(jtable6, "未稅金額", 0);
    //
    if (stringFlow.indexOf("簽核") != -1) {
      getButton("Button2").setVisible(true); // 簽核
      getButton("Button2").setLabel("財務簽核"); // 簽核
      getButton("Button5").setVisible(true); // 下一筆
      getButton("Button7").setVisible(true); // 退件
      // 發票
      // exeUtil.doChangeTableField(jtable1, "格式", 69) ; /* [格式] */
      // exeUtil.doChangeTableField(jtable1, "發票未稅金額", 118) ; /* [發票未稅金額] */
      // exeUtil.doChangeTableField(jtable1, "發票稅額", 101) ; /* [發票稅額] */
      exeUtil.doChangeTableField(jtable1, "扣抵代號", 119); /* [扣抵代號] */
      // getTableButton("Table1",0).setEnabled(false) ;// 新增
      // getTableButton("Table1",2).setEnabled(false) ;// 刪除
      // 個人收據
      // exeUtil.doChangeTableField(jtable2, "對應", 120) ;
      // exeUtil.doChangeTableField(jtable2, "格式", 120) ; /* [格式] */
      // exeUtil.doChangeTableField(jtable2, "所得淨額", 103) ; /* [所得淨額] */
      // exeUtil.doChangeTableField(jtable2, "扣繳金額", 97) ; /* [扣繳金額] */
      // exeUtil.doChangeTableField(jtable2, "稅率", 75) ; /* [稅率] */
      // getTableButton("Table2",0).setEnabled(false) ;// 新增
      // getTableButton("Table2",2).setEnabled(false) ;// 刪除
    } else if (stringFlow.indexOf("業管") != -1) {
      setEditable("BarCode", getValue("BarCode").startsWith("Z"));
      getButton("Button2").setLabel("業管簽核"); // 簽核
      getButton("ButtonError").setVisible(true); // 作廢
    } else if (stringFlow.indexOf("審核") != -1 && stringFlow.indexOf("人總") == -1) {
      setEditable("BarCode", POSITION == 1);
      getButton("Button2").setLabel("業管簽核"); // 簽核
      getButton("ButtonError").setVisible(true); // 作廢
      // 發票
      // exeUtil.doChangeTableField(jtable1, "格式", 69) ; /* [格式] */
      // exeUtil.doChangeTableField(jtable1, "發票未稅金額", 118) ; /* [發票未稅金額] */
      // exeUtil.doChangeTableField(jtable1, "發票稅額", 101) ; /* [發票稅額] */
      exeUtil.doChangeTableField(jtable1, "扣抵代號", 119); /* [扣抵代號] */
    } else {
      // 承辦
      if (stringFlow.indexOf("--") != -1) {
        // 發票
        // exeUtil.doChangeTableField(jtable1, "格式", 69) ; /* [格式] */
        // exeUtil.doChangeTableField(jtable1, "發票未稅金額", 118) ; /* [發票未稅金額] */
        // exeUtil.doChangeTableField(jtable1, "發票稅額", 101) ; /* [發票稅額] */
        exeUtil.doChangeTableField(jtable1, "扣抵代號", 119); /* [扣抵代號] */
        setEditable("BarCode", POSITION == 1);
      }
    }
  }

  public void doControlField(boolean booleanSource, Doc.Doc2M010 exeFun) throws Throwable {
    // 退款查詢按鈕控制
    String stringID = getValue("ID").trim();
    if ("".equals(stringID)) {
      getButton("ButtonReason").setVisible(false);
    } else {
      getButton("ButtonReason").setVisible((exeFun.getDoc2M010Reason(stringID, "B", "")).length > 0);
    }
    boolean booleanFlag = "B3018".equals(getUser());
    setVisible("BarCodeOld", booleanFlag);
    setVisible("ButtonUseStatus", booleanFlag);
    setVisible("DocNoOld", booleanFlag);
    setVisible("ButtonHalfWidth", booleanFlag);
    setVisible("ShowTableRealMoney", booleanFlag);
    setVisible("Button3", booleanFlag);
    setVisible("ButtonConvert", booleanFlag);
    setVisible("ButtonTableCheck", booleanFlag);
    setVisible("ButtonSystem", booleanFlag);
    setVisible("Button4", booleanFlag);
    setVisible("Table9", booleanFlag);// setVisible("Table9", true) ;
    setVisible("Table17", booleanFlag);
    setVisible("ButtonDBData", booleanFlag);
    setVisible("ButtonProjectBudgetCheckView", booleanFlag);
    setVisible("ButtonYearBudgetCheck", booleanFlag);
    setVisible("ButtonYearBudgetCheckView", booleanFlag);
    // 請購狀態控管
    getButton("ButtonPurchaseNoExist").doClick();
    // 沖銷方式
    String stringPayType = getValue("PayType").trim();
    if ("".equals(stringPayType)) {
      stringPayType = "A";
      setValue("PayType", stringPayType);
    }
    put("PayTypeControl", "N");
    setValue("PayTypeView", stringPayType);
    // 公文編碼最大值判斷
    getButton("Button1").doClick();
    //
    String stringBarCode = getValue("BarCode").trim();
    String stringDepartNo = "";
    String[][] retDoc1M040 = null;
    if (!"".equals(stringBarCode)) {
      retDoc1M040 = exeFun.getDoc1M040(stringBarCode);
      if (retDoc1M040.length > 0) {
        stringDepartNo = retDoc1M040[retDoc1M040.length - 1][0].trim();
        stringDepartNo = exeFun.getDepartName(stringDepartNo);
      }
    }
    setValue("UNDERGO_WRITE_DEPT", stringDepartNo);
    /* 傳票號碼 */
    String stringVoucherFlowNo = "";
    String stringVoucherDate = "";
    String[][] retTable = exeFun.getDoc2M014(stringBarCode);
    if (retTable.length == 0) {
      setVisible("VoucherFlowNoButton", false);
      getButton("VoucherFlowNoButton").setLabel("");
      return;
    }
    if (!"Z".equals(retTable[0][17].trim())) {
      setVisible("VoucherFlowNoButton", false);
      getButton("VoucherFlowNoButton").setLabel("");
      return;
    }
    stringVoucherFlowNo = retTable[0][5].trim();
    stringVoucherDate = retTable[0][4].trim();
    String[][] retFED1012 = exeFun.getFED1012(convert.ac2roc(convert.replace(stringVoucherDate, "/", "")), stringVoucherFlowNo, retTable[0][7].trim(), retTable[0][8].trim());
    if (retFED1012.length == 0) {
      setVisible("VoucherFlowNoButton", false);
      return;
    }
    if (!"0".equals(stringVoucherFlowNo) && !"".equals(stringVoucherDate)) {
      stringVoucherDate = convert.ac2roc(stringVoucherDate.replaceAll("/", ""));
      stringVoucherFlowNo = stringVoucherDate + "-" + convert.add0(stringVoucherFlowNo, "5");
      setVisible("VoucherFlowNoButton", true);
      // getButton("VoucherFlowNoButton").setLabel("傳票號碼" + stringVoucherFlowNo) ;
      getButton("VoucherFlowNoButton").setLabel("傳票訊息");
    } else {
      setVisible("VoucherFlowNoButton", false);
      getButton("VoucherFlowNoButton").setLabel("");
    }
  }

  // 發票
  public void doControlTable1(Doc.Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    Vector vectorInvoiecNo = new Vector();
    String stringInvoiceNo = "";
    String stringFactoryNo = "";
    String stringInvoiceKind = "";
    //
    String[] arrayInvoiceKind = { "A", "M", "B", "C", "G", "H", "D", "L", "A", "", "", "", "Q", "R", "S", "T", "E" };
    String[] arrayInvoiceKindName = { "電子計算機", "統一發票三聯式(手開)", "收銀機三聯式", "收據", "收銀機收執聯", "收據內含稅", "不得抵扣", "電子發票", "電算", "紙本電子發票(收執聯)21", "紙本電子發票(收執聯)22", "紙本電子發票(收執聯)25",
        "載具號碼彙總25", "載具號碼25", "載具號碼分攤25", "載具號碼彙總分攤25", "免稅" };
    // 2011/01/24 起發票格式修改同收款
    if (!"Y".equals(getValue("UNDERGO_WRITE").trim())) arrayInvoiceKind[8] = "";
    arrayInvoiceKind[7] = "";
    // 格式設定
    Vector vectorView = new Vector();
    Vector vectorValue = new Vector();
    String stringView = "";
    String stringValue = "";
    for (int intNo = 0; intNo < arrayInvoiceKind.length; intNo++) {
      stringView = arrayInvoiceKindName[intNo];
      if ("".equals(stringView)) continue;
      stringValue = arrayInvoiceKind[intNo];
      if ("".equals(stringValue)) continue;
      vectorView.add(stringView);
      vectorValue.add(stringValue);
    }
    setTableReference("Table1", 3, vectorView, vectorValue);
    //
    for (int intRowNo = 0; intRowNo < jtable1.getRowCount(); intRowNo++) {
      stringInvoiceNo = (String) getValueAt("Table1", intRowNo, "InvoiceNo");
      stringFactoryNo = (String) getValueAt("Table1", intRowNo, "FactoryNo");
      stringInvoiceKind = (String) getValueAt("Table1", intRowNo, "InvoiceKind");
      //
      vectorInvoiecNo.add(stringInvoiceNo.trim());
    }
    // 已存在發票
    put("Doc6M010_InvoiceNo_Vector", vectorInvoiecNo);
    // 發票表格合計自動給值
    getButton("Button9").doClick();
  }

  // 個人收據
  public void doControlTable2(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2");
    //
    String stringUndergoWrite = getValue("UNDERGO_WRITE").trim();
    String stringToday = datetime.getToday("YYYY/mm/dd");
    if (stringToday.compareTo("2015/07/01") < 0 && ",E,Y,".indexOf(stringUndergoWrite) == -1 && ",B4197,".indexOf("," + getValue("EmployeeNo") + ",") == -1) {
      doChangeSupplementMoney(exeUtil, exeFun);
    }
    // 扣繳表格合計自動給值
    getButton("Button10").doClick();
  }

  public void doChangeSupplementMoney(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2");// 扣繳
    String stringToday = datetime.getToday("YYYY/mm/dd");
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringField = "";
    String stringValue = "";
    String stringTemp = "";
    String[] arrayField = { "ReceiptTotalMoney", "ReceiptMoney", "ReceiptTax", "ReceiptTaxType", "ReceiptKind", "FactoryNo", "ACCT_NO", "SupplementMoney", "PayCondition1" };
    Hashtable hashtableData = new Hashtable();
    double doubleSupplementMoney = 0;
    //
    hashtableData.put("PayCondition1", "0");
    hashtableData.put("EmployeeNo", stringEmployeeNo);
    hashtableData.put("TODAY", stringToday);
    hashtableData.put("SpecUserID", "Y");
    hashtableData.put("SpecCostID", "N");
    hashtableData.put("Table21Exist", "N");
    hashtableData.put("TYPE", "MONEY");
    for (int intNo = 0; intNo < jtable2.getRowCount(); intNo++) {
      stringTemp = ("" + getValueAt("Table2", intNo, "SupplementMoney")).trim();
      if (exeUtil.doParseDouble(stringTemp) <= 0) continue;
      //
      for (int intNoL = 0; intNoL < arrayField.length; intNoL++) {
        stringField = arrayField[intNoL];
        stringValue = ("" + getValueAt("Table2", intNo, stringField)).trim();
        //
        hashtableData.put(stringField, stringValue);
      }
      hashtableData.put("SupplementMoney", "0");
      exeFun.isSupplementMoneyOK(hashtableData, exeUtil);
      doubleSupplementMoney = exeUtil.doParseDouble("" + hashtableData.get("SupplementMoney"));
      //
      System.out.println(intNo + "原(" + stringTemp + ")後(" + convert.FourToFive("" + doubleSupplementMoney, 0) + ")--------------------------------------------------");
      setValueAt("Table2", convert.FourToFive("" + doubleSupplementMoney, 0), intNo, "SupplementMoney");
    }
  }

  // 費用
  public void doControlTable6(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    String stringComNo = getValue("ComNo").trim();
    String stringCDateAC = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringCostID = "";
    String stringCostID1 = "";
    Vector vectorDoc2M0201ForW = exeFun.getCostIDVDoc2M0201V(stringComNo, "", "", "W", stringCDateAC, " AND  FunctionName  LIKE  '%費用對照通路代碼(Doc2I0124)%' ");
    boolean booleanTableElse = false;
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringCostID = ("" + getValueAt("Table6", intNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table6", intNo, "CostID1")).trim();
      //
      if (vectorDoc2M0201ForW.indexOf(stringCostID + stringCostID1) != -1) booleanTableElse = true;
    }
    setVisible("ButtonTableElse", booleanTableElse);
  }

  public String getComNoForEmpNo(String stringUser) throws Throwable {
    talk dbFE3D = getTalk("" + get("put_FE3D"));
    String stringSql = "";
    String stringComNo = "";
    String[][] retFE3D72 = null;
    //
    stringSql = " SELECT  COMPANY_CD " + " FROM  FE3D72 F72,  FE3D70 F70 " + " WHERE  EMP_NO  =  '" + stringUser + "' " + " AND  F72.FIRM_NO  =  F70.FIRM_NO "
        + " AND  INSUR_KIND  =  '1' ";
    retFE3D72 = dbFE3D.queryFromPool(stringSql);
    if (retFE3D72.length > 0) stringComNo = retFE3D72[0][0].trim();
    return stringComNo;
  }
}
