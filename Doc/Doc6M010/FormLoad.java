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
    // �w�]��
    doBatchDefaultValue(booleanSource, exeUtil, exeFun);
    // �y�{����
    doFlowControl(exeUtil, exeFun);
    //
    if ("SYS".equals(getUser())) return value;
    // ���
    doControlField(booleanSource, exeFun);
    getButton("Button4").doClick();
    // ���
    doControlTable1(exeFun); // �o��
    doControlTable2(exeUtil, exeFun); // �ӤH����
    doControlTable6(exeUtil, exeFun); // �O��
    //
    doKindNoD();
    //
    boolean booleanFlag = "".equals(getButton("VoucherFlowNoButton").getLabel());
    if (getFunctionName().indexOf("�H�`") != -1) {
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
    // �N�P�X�� ���s���A�P�_
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
    if (getFunctionName().indexOf("�H�`") == -1) return;
    //
    setEditable("DepartNo", false);
    // setEditable("DocNo2", false) ;
    setEditable("DocNo3", false);
    setEditable("button2", false);
    // �o��
    JTable jtable = getTable("Table1");
    getTableButton("Table1", 0).setEnabled(false);
    getTableButton("Table1", 1).setEnabled(false);
    getTableButton("Table1", 2).setEnabled(false);
    // �ӤH����
    jtable = getTable("Table2");
    getTableButton("Table2", 0).setEnabled(false);
    getTableButton("Table2", 1).setEnabled(false);
    getTableButton("Table2", 2).setEnabled(false);
    // �S����O
    jtable = getTable("Table3");
    getTableButton("Table3", 0).setEnabled(false);
    getTableButton("Table3", 1).setEnabled(false);
    getTableButton("Table3", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "��", 0);
    exeUtil.doChangeTableField(jtable, "��", 0);
    // �O��
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
    // ����s�X
    if ("0231".equals(stringDepartNoSubject) && stringFunctionName.indexOf("�f��") == -1) {
      // setEditable("DocNo2", false) ;
      setEditable("DocNo3", false);
      setEditable("DepartNo", false);
      setEditable("BarCode", false);
    }
    // ���q
    boolean booleanFlag = booleanEnable && (stringFunctionName.indexOf("�g��") != -1 || stringFunctionName.indexOf("�ӿ�") != -1);
    setEditable("ComNo", booleanFlag); // ���q
    // �ӿ�H��
    setEditable("OriEmployeeNo", booleanEnable);
    // �ݥΤ��
    setEditable("NeedDate", booleanEnable);
    // ���P�覡
    setEditable("PayType", booleanEnable);
    // ���夺�e
    setEditable("Descript", booleanEnable);
    // ����w�w���פ��
    // setEditable("PreFinDate", booleanEnable) ;
    // �I�ڱ��� 1~2
    setEditable("PayCondition1", booleanEnable);
    setEditable("PayCondition2", booleanEnable);
    // �w�w���P���
    setEditable("DestineExpenseDate", booleanEnable);
    // �S�e��O
    for (int intNo = 0; intNo < getTable("Table3").getRowCount(); intNo++) {
      // �קO
      setEditable("Table3", intNo, "ProjectID1", booleanEnable);
      // �榡
      setEditable("Table3", intNo, "Position", booleanEnable);
      // �ұo���B
      setEditable("Table3", intNo, "InvoiceTotalMoney", booleanEnable);
      setEditable("Table3", intNo, "SignDate2", booleanEnable);
      setEditable("Table3", intNo, "SignDate", booleanEnable);
      setEditable("Table3", intNo, "ProjectID1Use", booleanEnable);
    }
    doNOTEnabled(exeUtil);
  }

  public void doBatchDefaultValue(boolean booleanSource, FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    // �������ʪ��B
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
    // �������O
    String stringKindNo = getValue("KindNo").trim();
    if ("".equals(stringKindNo)) {
      // String stringTime = datetime.getTime("YYYY/mm/dd h:m:s").trim() ;
      stringKindNo = "26";
      setValue("KindNo", "26");
    }
    // �ɶ�
    String stringCDate = getToday("yy/mm/dd").trim();
    String stringCTime = datetime.getTime("h:m:s");
    String stringEDateTime = exeUtil.getDateConvert(stringCDate) + " " + stringCTime;
    //
    setValue("CDate", stringCDate);
    setValue("CTime", stringCTime);
    setValue("EDateTime", stringEDateTime);
    // �ݥΤ��
    String stringNeedDate = stringCDate;

    // ����w�w���פ��
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

    // �w�w���P���
    String stringDestineExpenseDate = getValue("DestineExpenseDate").trim();
    stringDestineExpenseDate = datetime.dateAdd(stringNeedDate.replaceAll("/", ""), "m", 1);
    if (boolean11Month && (stringYear + "/12/31").compareTo(stringDestineExpenseDate) < 0) {
      stringDestineExpenseDate = stringYear + "1231";
    }
    stringDestineExpenseDate = exeUtil.getDateConvertFullRoc(stringDestineExpenseDate);
    setValue("DestineExpenseDate", stringDestineExpenseDate);
    //
    
    // ���q
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
    // ����s�X
    stringCDate = exeUtil.getDateConvertRoc(stringCDate).replaceAll("/", "");
    String stringDocNo2 = datetime.getYear(stringCDate) + datetime.getMonth(stringCDate);
    setValue("DocNo2", stringDocNo2);
    // ����
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
    // 033 FG ���s����
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    boolean booleanFlag = stringSpecBudget.indexOf(getValue("DepartNo").trim()) != -1 && "Z6".equals(getValue("ComNo").trim());
    if ("Y".equals(getValue("PurchaseNoExist"))) {
      getButton("Button033FGInput").setLabel("�S��w�ⱱ�� �Ӷ��d��");
    } else {
      getButton("Button033FGInput").setLabel("�S��w�ⱱ�� �Ӷ���J");
    }
    setVisible("Button033FGInput", booleanFlag);
    //
    setEditable("BarCode", false); // BarCode
    setEditable("KindNo", false); // KindNo
    //
    getButton("Button2").setVisible(false); // ñ��
    getButton("Button5").setVisible(false); // �U�@��
    getButton("Button7").setVisible(false); // �h��
    getButton("ButtonError").setVisible(false); // �@�o
    // �o��
    JTable jtable1 = getTable("Table1");//
    exeUtil.doChangeTableField(jtable1, "BarCode", 0);
    // exeUtil.doChangeTableField(jtable1, "�榡", 0) ; /* [�榡] ����*/
    // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 0) ; /* [�o�����|���B] ����*/
    // exeUtil.doChangeTableField(jtable1, "�o���|�B", 0) ; /* [�o���|�B] ����*/
    exeUtil.doChangeTableField(jtable1, "����N��", 0);
    getTableButton("Table1", 0).setEnabled(true);// �s�W
    getTableButton("Table1", 2).setEnabled(true);// �R��
    // ��ú
    JTable jtable2 = getTable("Table2");
    exeUtil.doChangeTableField(jtable2, "BarCode", 0);
    exeUtil.doChangeTableField(jtable2, "����", 120);
    // exeUtil.doChangeTableField(jtable2, "�榡", 0) ; /* [�榡] */
    // exeUtil.doChangeTableField(jtable2, "�ұo�b�B", 0) ; /* [�ұo�b�B] */
    // exeUtil.doChangeTableField(jtable2, "��ú���B", 0) ; /* [��ú���B] */
    // exeUtil.doChangeTableField(jtable2, "�|�v", 0) ; /* [�榡] */
    exeUtil.doChangeTableField(jtable2, "<html>�ұo���I����<br>�����", 0); /* [�榡] */
    getTableButton("Table2", 0).setEnabled(true);// �s�W
    getTableButton("Table2", 2).setEnabled(true);// �R��
    // �S�e��O
    JTable jtable3 = getTable("Table3");
    exeUtil.doChangeTableField(jtable3, "BarCode", 0);
    exeUtil.doChangeTableField(jtable3, "��", 24);
    // �O��
    JTable jtable6 = getTable("Table6");
    exeUtil.doChangeTableField(jtable6, "BarCode", 0);
    exeUtil.doChangeTableField(jtable6, "��1", 24);
    exeUtil.doChangeTableField(jtable6, "��2", 24);
    exeUtil.doChangeTableField(jtable6, "���|���B", 0);
    //
    if (stringFlow.indexOf("ñ��") != -1) {
      getButton("Button2").setVisible(true); // ñ��
      getButton("Button2").setLabel("�]��ñ��"); // ñ��
      getButton("Button5").setVisible(true); // �U�@��
      getButton("Button7").setVisible(true); // �h��
      // �o��
      // exeUtil.doChangeTableField(jtable1, "�榡", 69) ; /* [�榡] */
      // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] */
      // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] */
      exeUtil.doChangeTableField(jtable1, "����N��", 119); /* [����N��] */
      // getTableButton("Table1",0).setEnabled(false) ;// �s�W
      // getTableButton("Table1",2).setEnabled(false) ;// �R��
      // �ӤH����
      // exeUtil.doChangeTableField(jtable2, "����", 120) ;
      // exeUtil.doChangeTableField(jtable2, "�榡", 120) ; /* [�榡] */
      // exeUtil.doChangeTableField(jtable2, "�ұo�b�B", 103) ; /* [�ұo�b�B] */
      // exeUtil.doChangeTableField(jtable2, "��ú���B", 97) ; /* [��ú���B] */
      // exeUtil.doChangeTableField(jtable2, "�|�v", 75) ; /* [�|�v] */
      // getTableButton("Table2",0).setEnabled(false) ;// �s�W
      // getTableButton("Table2",2).setEnabled(false) ;// �R��
    } else if (stringFlow.indexOf("�~��") != -1) {
      setEditable("BarCode", getValue("BarCode").startsWith("Z"));
      getButton("Button2").setLabel("�~��ñ��"); // ñ��
      getButton("ButtonError").setVisible(true); // �@�o
    } else if (stringFlow.indexOf("�f��") != -1 && stringFlow.indexOf("�H�`") == -1) {
      setEditable("BarCode", POSITION == 1);
      getButton("Button2").setLabel("�~��ñ��"); // ñ��
      getButton("ButtonError").setVisible(true); // �@�o
      // �o��
      // exeUtil.doChangeTableField(jtable1, "�榡", 69) ; /* [�榡] */
      // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] */
      // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] */
      exeUtil.doChangeTableField(jtable1, "����N��", 119); /* [����N��] */
    } else {
      // �ӿ�
      if (stringFlow.indexOf("--") != -1) {
        // �o��
        // exeUtil.doChangeTableField(jtable1, "�榡", 69) ; /* [�榡] */
        // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] */
        // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] */
        exeUtil.doChangeTableField(jtable1, "����N��", 119); /* [����N��] */
        setEditable("BarCode", POSITION == 1);
      }
    }
  }

  public void doControlField(boolean booleanSource, Doc.Doc2M010 exeFun) throws Throwable {
    // �h�ڬd�߫��s����
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
    // ���ʪ��A����
    getButton("ButtonPurchaseNoExist").doClick();
    // �R�P�覡
    String stringPayType = getValue("PayType").trim();
    if ("".equals(stringPayType)) {
      stringPayType = "A";
      setValue("PayType", stringPayType);
    }
    put("PayTypeControl", "N");
    setValue("PayTypeView", stringPayType);
    // ����s�X�̤j�ȧP�_
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
    /* �ǲ����X */
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
      // getButton("VoucherFlowNoButton").setLabel("�ǲ����X" + stringVoucherFlowNo) ;
      getButton("VoucherFlowNoButton").setLabel("�ǲ��T��");
    } else {
      setVisible("VoucherFlowNoButton", false);
      getButton("VoucherFlowNoButton").setLabel("");
    }
  }

  // �o��
  public void doControlTable1(Doc.Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");
    Vector vectorInvoiecNo = new Vector();
    String stringInvoiceNo = "";
    String stringFactoryNo = "";
    String stringInvoiceKind = "";
    //
    String[] arrayInvoiceKind = { "A", "M", "B", "C", "G", "H", "D", "L", "A", "", "", "", "Q", "R", "S", "T", "E" };
    String[] arrayInvoiceKindName = { "�q�l�p���", "�Τ@�o���T�p��(��})", "���Ⱦ��T�p��", "����", "���Ⱦ������p", "���ڤ��t�|", "���o�覩", "�q�l�o��", "�q��", "�ȥ��q�l�o��(�����p)21", "�ȥ��q�l�o��(�����p)22", "�ȥ��q�l�o��(�����p)25",
        "���㸹�X�J�`25", "���㸹�X25", "���㸹�X���u25", "���㸹�X�J�`���u25", "�K�|" };
    // 2011/01/24 �_�o���榡�ק�P����
    if (!"Y".equals(getValue("UNDERGO_WRITE").trim())) arrayInvoiceKind[8] = "";
    arrayInvoiceKind[7] = "";
    // �榡�]�w
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
    // �w�s�b�o��
    put("Doc6M010_InvoiceNo_Vector", vectorInvoiecNo);
    // �o�����X�p�۰ʵ���
    getButton("Button9").doClick();
  }

  // �ӤH����
  public void doControlTable2(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2");
    //
    String stringUndergoWrite = getValue("UNDERGO_WRITE").trim();
    String stringToday = datetime.getToday("YYYY/mm/dd");
    if (stringToday.compareTo("2015/07/01") < 0 && ",E,Y,".indexOf(stringUndergoWrite) == -1 && ",B4197,".indexOf("," + getValue("EmployeeNo") + ",") == -1) {
      doChangeSupplementMoney(exeUtil, exeFun);
    }
    // ��ú���X�p�۰ʵ���
    getButton("Button10").doClick();
  }

  public void doChangeSupplementMoney(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable2 = getTable("Table2");// ��ú
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
      System.out.println(intNo + "��(" + stringTemp + ")��(" + convert.FourToFive("" + doubleSupplementMoney, 0) + ")--------------------------------------------------");
      setValueAt("Table2", convert.FourToFive("" + doubleSupplementMoney, 0), intNo, "SupplementMoney");
    }
  }

  // �O��
  public void doControlTable6(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable6 = getTable("Table6");
    String stringComNo = getValue("ComNo").trim();
    String stringCDateAC = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringCostID = "";
    String stringCostID1 = "";
    Vector vectorDoc2M0201ForW = exeFun.getCostIDVDoc2M0201V(stringComNo, "", "", "W", stringCDateAC, " AND  FunctionName  LIKE  '%�O�ι�ӳq���N�X(Doc2I0124)%' ");
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
