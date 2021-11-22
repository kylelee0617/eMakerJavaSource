package Doc.Doc2M010PKG;

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
    System.out.println("Doc2M010 FromLoad----------------------------------------S1111111111");

    // messagebox("START") ;
    Doc.Doc2M010 exeFun = new Doc.Doc2M010();
    FargloryUtil exeUtil = new FargloryUtil();
    //
    put("Doc2M010_FormLoad", "FormLoad"); // �w�o��
    put("Doc2M010_RealTaxMoney", "FormLoad"); // �w�o��
    put("Doc2M010_STATUS", "FormLoad");
    //
    setEditable("CDate", false);
    Vector vectorCostID = new Vector();
    vectorCostID.add("31");
    vectorCostID.add("32");
    // �w�]��
    doBatchDefaultValue(exeFun, exeUtil);
    // �y�{����
    doFlowControl(vectorCostID, exeUtil, exeFun);
    // ���
    doControlField(vectorCostID, exeUtil, exeFun);
    // ����
    doControlTab1(vectorCostID, exeFun);
    // ���-�o��
    boolean booleanInvoiceKind = doControlTable1(exeUtil, exeFun);
    // ���-�O��
    doControlTable2(booleanInvoiceKind, exeFun, exeUtil);
    // ���-��ú(�P�_ �p�� Z0001 �ɡA�i����ú�|�B)
    System.out.println("doControlTable3----------------------------------------S");
    doControlTable3(exeUtil, exeFun);
    System.out.println("doControlTable3----------------------------------------E");
    // ���-����
    boolean booleanFlagL = "".equals(getButton("VoucherFlowNoButton").getLabel());
    doControlTable6(exeUtil, exeFun);
    // ���-�禬
    doControlTable7(exeFun);
    //
    if (getFunctionName().indexOf("�H�`") != -1) {
      booleanFlagL = false;
    }
    System.out.println("doControlFieldEnabled(" + booleanFlagL + ")----------------------------------------1111");
    if (",B3018,SYS,".indexOf(getUser()) != -1) booleanFlagL = true;
    doControlFieldEnabled(booleanFlagL, exeUtil);
    // �i�״ڼt�ӧP�_
    getButton("ButtonFactoryNo").doClick();
    // �~���w��
    String stringYearRoc = datetime.getYear(datetime.dateAdd(datetime.getToday("yymmdd"), "y", -1));
    String stringSqlAnd = "SELECT  BarCode " + " FROM  Doc2M080 " + " WHERE  VOUCHER_NO  LIKE  '" + (stringYearRoc) + "1231%' " + " AND  ISNULL(Status,  '')  <>  'Y' ";
    booleanFlagL = exeFun.getTableDataDoc(stringSqlAnd).length > 0;
    if (!booleanFlagL) {
      stringSqlAnd = "SELECT  BarCode " + " FROM  Doc2M0801 " + " WHERE  BarCode = '" + getValue("BarCode").trim() + "' ";
      booleanFlagL = exeFun.getTableDataDoc(stringSqlAnd).length > 0;
    }
    getButton("ButtonYearEnd").setVisible(booleanFlagL);
    setValue("YearEndStatus", booleanFlagL ? "Y" : "N");
    setVisible("ButtonConvert", "B3018".equals(getUser()));
    //
    put("Doc2M010_RealTaxMoney", "null");
    put("Doc2M010_STATUS", "null");
    put("Doc2M010_FormLoad", "null");
    System.out.println("PUT ���A NULL �]�w");

    setVisible("ButtonLastFunction", false);
    if (getFunctionName().indexOf("POP") != -1) {
      setVisible("ButtonYearEnd", false);
      setVisible("ConuntType", false);
      setVisible("button2", false);
      setVisible("Button13", false);
      setVisible("button3", false);
      setVisible("Button12", false);
      setVisible("ButtonLastFunction", true);
      setValue("LastFunction", "" + get("Doc5Q011_Source"));
    }
    // �N�P�X�� ���s���A�P�_
    put("Doc7M02691_STATUS", "VIEW");
    getButton("ButtonTable16").doClick();
    getButton("ButtonTable2Status").doClick();
    /*
     * if(!"".equals(getValue("RetainBarCode").trim())) {
     * getTabbedPane("Tab1").setSelectedIndex(7) ; // �ӤH���� }
     */
    String[][] retEmployNo = exeFun.getTableDataDoc(" SELECT  EmployeeNo  FROM  Doc3M011_EmployeeNo  WHERE  FunctionType  =  '06'  AND  EmployeeNo  =  '" + getUser() + "' ");
    setVisible("Button05", retEmployNo.length > 0);
    getButton("ButtonSalaryYYMMCheck").doClick();
    // messagebox("END") ;
    long longTime1 = exeUtil.doParseLong("" + get("START_TIME"));
    long longTime2 = exeUtil.getTimeInMillis();
    if (longTime1 > 0) {
      System.out.println("SHOW ��O�ɶ�-----------------" + ((longTime2 - longTime1) / 1000) + "��");
    }
    doPowerUser(exeUtil);
    if (getUser().toUpperCase().equals("B5385")) {
      setEditable("BarCode", true);
    }
    System.out.println("Doc2M010 FromLoad----------------------------------------E1111111111");
    return value;
  }

  public void doPowerUser(FargloryUtil exeUtil) throws Throwable {
    boolean booleanFlag = (",B3018,SYS,".indexOf(getUser()) != -1);
    setVisible("AccountCount", booleanFlag);
    setVisible("ButtonTableCheck", booleanFlag);
    setVisible("ButtonTableCheckN", booleanFlag);
    setVisible("ButtonInputDeptCdCheck", booleanFlag);
    setVisible("ButtonHalfWidth", booleanFlag);
    setVisible("ButtonSystem", booleanFlag);
    setVisible("ButtonTable7Status", booleanFlag);
    setVisible("ButtonTable6PurchaseData", booleanFlag);
    setVisible("ButtonTableCheckOLD", booleanFlag);
    setVisible("ButtonProjectBudgetCheck", booleanFlag);
    setVisible("ButtonProjectBudgetCheckView", booleanFlag);
    setVisible("ButtonYearBudgetCheck", booleanFlag);
    setVisible("ButtonYearBudgetCheckView", booleanFlag);
    setVisible("PurchaseEnd", booleanFlag);
    setVisible("DocNoOld", booleanFlag);
    setVisible("BarCodeOld", booleanFlag);
    setVisible("ButtonBackUpFlowData", booleanFlag);
    setVisible("LastEmployeeNo", booleanFlag);
    setEditable("LastEmployeeNo", booleanFlag);
  }

  public void doNOTEnabled(FargloryUtil exeUtil) throws Throwable {
    if (getFunctionName().indexOf("�H�`") == -1) return;
    //
    setEditable("PayCondition1", false);
    setEditable("PayCondition2", false);
    setEditable("Button6", false);
    setEditable("button3", false);
    setEditable("Multi-InvoiceKind", false);
    setEditable("DocNoType", false);
    setEditable("Button8", false);
    setEditable("DepartNoDest", false);
    // ����
    JTable jtable = getTable("Table6");
    getTableButton("Table6", 0).setEnabled(false);
    getTableButton("Table6", 1).setEnabled(false);
    getTableButton("Table6", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "��", 0);
    exeUtil.doChangeTableField(jtable, "��", 0);
    // �禬
    jtable = getTable("Table7");
    getTableButton("Table7", 0).setEnabled(false);
    getTableButton("Table7", 1).setEnabled(false);
    getTableButton("Table7", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "��", 0);
    exeUtil.doChangeTableField(jtable, "��", 0);
    // �O��
    jtable = getTable("Table2");
    getTableButton("Table2", 0).setEnabled(false);
    getTableButton("Table2", 1).setEnabled(false);
    getTableButton("Table2", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "��1", 0);
    exeUtil.doChangeTableField(jtable, "��1", 0);
    exeUtil.doChangeTableField(jtable, "��2", 0);
    exeUtil.doChangeTableField(jtable, "��2", 0);
    // �o��
    jtable = getTable("Table1");
    getTableButton("Table1", 0).setEnabled(false);
    getTableButton("Table1", 1).setEnabled(false);
    getTableButton("Table1", 2).setEnabled(false);
    // �ӤH����
    jtable = getTable("Table3");
    getTableButton("Table3", 0).setEnabled(false);
    getTableButton("Table3", 1).setEnabled(false);
    getTableButton("Table3", 2).setEnabled(false);
    // ����
    jtable = getTable("Table4");
    getTableButton("Table4", 0).setEnabled(false);
    getTableButton("Table4", 1).setEnabled(false);
    getTableButton("Table4", 2).setEnabled(false);
    exeUtil.doChangeTableField(jtable, "��", 0);
    exeUtil.doChangeTableField(jtable, "��", 0);
    //
    setVisible("Button7", true);
  }

  public void doControlFieldEnabled(boolean booleanEnable, FargloryUtil exeUtil) throws Throwable {
    boolean booleanPurchaseNoExist = "Y".equals(getValue("PurchaseNoExist").trim()) ? false : true;
    boolean booleanFlag = true;
    String stringFunctionName = getFunctionName();
    if (stringFunctionName.indexOf("ñ��") == -1) {
      // �����N�X
      setEditable("DepartNo", booleanEnable);
      // ����s�X 2,3
      // setEditable("DocNo2", booleanEnable) ;
      setEditable("DocNo3", booleanEnable);
    }
    String stringDepartNoSubject = "" + get("EMP_DEPT_CD");
    if ("0231,".indexOf(stringDepartNoSubject + ",") != -1 && getFunctionName().indexOf("�f��") == -1) {
      // �����N�X
      setEditable("DepartNo", false);
      // ����s�X 2,3
      // setEditable("DocNo2", false) ;
      setEditable("DocNo3", false);
    }
    setEditable("ComNo", booleanEnable && stringFunctionName.indexOf("�g��") != -1 || stringFunctionName.indexOf("�ӿ�") != -1);
    setEditable("LastPayDate", booleanEnable);
    setEditable("AccountCount", booleanEnable);
    // RetainMoney�BPurchaseNoExist �� �䥦�y�{���A���� ����
    // �ӿ�H��
    setEditable("OriEmployeeNo", booleanEnable);
    // ���夺�e
    setEditable("Descript", booleanEnable);
    // ����w�w���פ��
    // setEditable("PreFinDate", booleanEnable) ;
    // ���ʳ���
    getcLabel("Descript").requestFocus();
    // �I�ڱ��� 1,2
    if (!booleanPurchaseNoExist) {
      setEditable("PayCondition1", booleanEnable);
      setEditable("PayCondition2", booleanEnable);
    }
    // ����
    for (int intNo = 0; intNo < getTable("Table6").getRowCount(); intNo++) {
      // ����1,2,3
      setEditable("Table6", intNo, "PurchaseNo1", booleanEnable);
      setEditable("Table6", intNo, "PurchaseNo2", booleanEnable);
      setEditable("Table6", intNo, "PurchaseNo3", booleanEnable);
      // ���ʪ��B
      setEditable("Table6", intNo, "PurchaseMoney", booleanEnable);
      // �t��
      setEditable("Table6", intNo, "FactoryNo", booleanEnable);
    }
    // �禬
    for (int intNo = 0; intNo < getTable("Table7").getRowCount(); intNo++) {
      // �禬1,2,3
      setEditable("Table7", intNo, "OptometryNo1", booleanEnable);
      setEditable("Table7", intNo, "OptometryNo2", booleanEnable);
      setEditable("Table7", intNo, "OptometryNo3", booleanEnable);
      // �t��
      setEditable("Table7", intNo, "OptometryType", booleanEnable);
    }
    // �O��
    for (int intNo = 0; intNo < getTable("Table2").getRowCount(); intNo++) {
      booleanFlag = "I".equals(("" + getValueAt("Table2", intNo, "InOut")).trim()) ? false : true;
      // ���~�~
      setEditable("Table2", intNo, "InOut", booleanEnable);
      // �קO
      setEditable("Table2", intNo, "ProjectID", booleanEnable && booleanFlag);
      setEditable("Table2", intNo, "ProjectID1", booleanEnable && booleanFlag);
      // ����
      setEditable("Table2", intNo, "DepartNo", booleanEnable);
      // �O�ΥN�X
      setEditable("Table2", intNo, "CostID", booleanEnable && booleanPurchaseNoExist);
      setEditable("Table2", intNo, "CostID1", booleanEnable && booleanPurchaseNoExist);
      // �дڪ��B
      setEditable("Table2", intNo, "RealTotalMoney", booleanEnable);
    }
    // �o��
    for (int intNo = 0; intNo < getTable("Table1").getRowCount(); intNo++) {
      // �Τ@�s��
      setEditable("Table1", intNo, "FactoryNo", booleanEnable && booleanPurchaseNoExist);
      // �o���榡
      setEditable("Table1", intNo, "InvoiceKind", booleanEnable);
      // �o�����
      setEditable("Table1", intNo, "InvoiceDate", booleanEnable);
      // �o�����X
      setEditable("Table1", intNo, "InvoiceNo", booleanEnable);
      // �o���`���B
      setEditable("Table1", intNo, "InvoiceTotalMoney", booleanEnable);
      // �o�����|���B
      setEditable("Table1", intNo, "InvoiceMoney", booleanEnable);
      // �o���|�B
      setEditable("Table1", intNo, "InvoiceTax", booleanEnable);
    }
    // �ӤH����
    for (int intNo = 0; intNo < getTable("Table3").getRowCount(); intNo++) {
      // �Τ@�s��
      setEditable("Table3", intNo, "FactoryNo", booleanEnable && booleanPurchaseNoExist);
      // �榡
      setEditable("Table3", intNo, "ReceiptKind", booleanEnable);
      // �ұo���B
      setEditable("Table3", intNo, "ReceiptTotalMoney", booleanEnable);
      setEditable("Table3", intNo, "ReceiptMoney", booleanEnable);
      // ��ú���B
      setEditable("Table3", intNo, "ReceiptTax", booleanEnable);
      // �|�v
      setEditable("Table3", intNo, "ReceiptTaxType", booleanEnable);
      // ����
      setEditable("Table3", intNo, "ACCT_NO", booleanEnable);
    }
    // ����
    for (int intNo = 0; intNo < getTable("Table4").getRowCount(); intNo++) {
      // �������B
      setEditable("Table4", intNo, "DiscountMoney", booleanEnable);
      // �������B���|���B
      setEditable("Table4", intNo, "DiscountNoTaxMoney", booleanEnable);
    }
    doNOTEnabled(exeUtil);
  }

  public void doBatchDefaultValue(Doc.Doc2M010 exeFun, FargloryUtil exeUtil) throws Throwable {
    if (POSITION != 1) {
      getButton("ButtonComName").doClick();
      return;
    }
    //
    put("FactoryNo", "");
    //
    boolean booleanFlow = getFunctionName().indexOf("�ӿ�") != -1 || getFunctionName().indexOf("�g��") != -1;
    // �������O
    String stringKindNo = getValue("KindNo").trim();
    if ("".equals(stringKindNo)) {
      stringKindNo = "24";
      setValue("KindNo", stringKindNo);
    }
    // �ɶ�
    String stringCDate = getToday("yy/mm/dd").trim();
    String stringCTime = datetime.getTime("h:m:s"); // getValue("CTime").trim( ) ;
    String stringEDateTime = exeUtil.getDateConvert(stringCDate) + " " + stringCTime;
    setValue("CDate", stringCDate);
    setValue("CTime", stringCTime);
    setValue("EDateTime", stringEDateTime);
    // ����w�w���פ��
    String stringPreFinDate = getValue("PreFinDate").trim();
    if (booleanFlow && "".equals(stringPreFinDate)) {
      String stringKindDay = exeFun.getKindDay(stringKindNo);
      //
      stringCDate = convert.replace(stringCDate, "/", "");
      stringPreFinDate = datetime.dateAdd(stringCDate, "d", exeUtil.doParseInteger(stringKindDay));
      stringPreFinDate = exeUtil.getDateConvertFullRoc(stringPreFinDate);
      setValue("PreFinDate", stringPreFinDate);
    }
    // ���q
    String stringComNo = getValue("ComNo").trim();
    if ("".equals(stringComNo)) {
      stringComNo = "CS";
      setValue("ComNo", stringComNo);
    }
    getButton("ButtonComName").doClick();
    // ����s�X
    String stringDocNo2 = getValue("DocNo2").trim();
    if ("".equals(stringDocNo2)) {
      String stringToday = getToday("yy/mm/dd");
      String[] arrayString = convert.StringToken(stringToday, "/");
      stringDocNo2 = arrayString[0] + arrayString[1];
      setValue("DocNo2", stringDocNo2);
    }
    // �ƫ�ɵo��
    String stringDocNoType = getValue("DocNoType").trim();
    if ("".equals(stringDocNoType)) {
      setValue("DocNoType", "A");
    }
  }

  public void doFlowControl(Vector vectorCostID, FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    String stringFlow = getFunctionName();
    String stringComNo = getValue("ComNo").trim();
    String stringDepartNo = getValue("DepartNo").trim();
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    // 033 FG ���s����
    // System.out.println("033 FG
    // ���s����-----------------------------------------------S") ;
    boolean booleanFlag = stringSpecBudget.indexOf("," + stringDepartNo + ",") != -1;
    if ("Y".equals(getValue("PurchaseNoExist"))) {
      getButton("Button033FGInput").setLabel("�S��w�ⱱ�� �Ӷ��d��");
    } else {
      getButton("Button033FGInput").setLabel("�S��w�ⱱ�� �Ӷ���J");
    }
    setVisible("Button033FGInput", booleanFlag);
    setVisible("Button033FGInput", booleanFlag);
    // System.out.println("033 FG
    // ���s����("+booleanFlag+")-----------------------------------------------E") ;
    //
    setEditable("UNDERGO_WRITE", ("B3018".equals(getUser())));
    setEditable("BarCode", false); // BarCode
    setVisible("InvoiceMoneySUM", false); //
    setVisible("InvoiceTaxSum", false); //
    getButton("Button8").setVisible(true); // ����
    getButton("Button6").setVisible(true); // ����
    getButton("Button2").setVisible(false); // ñ��
    getButton("Button3").setVisible(false); // �U�@��
    getButton("Button7").setVisible(false); // �h��
    getButton("ButtonError").setVisible(false); // �@�o
    getButton("ButtonPurchaseEnd").setVisible(false); // ����
    getButton("ButtonYearEnd").setVisible(false); // �~���w��
    // �o��
    JTable jtable1 = getTable("Table1");
    exeUtil.doChangeTableField(jtable1, "BarCode", 0); /* [���X�s��] ���� */
    // exeUtil.doChangeTableField(jtable1, "�榡", 0) ; /* [�榡] ����*/
    // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 0) ; /* [�o�����|���B] ����*/
    // exeUtil.doChangeTableField(jtable1, "�o���|�B", 0) ; /* [�o���|�B] ����*/
    exeUtil.doChangeTableField(jtable1, "����N��", 0); /* [����N��] ���� */
    getTableButton("Table1", 0).setEnabled(true);// �s�W
    getTableButton("Table1", 2).setEnabled(true);// �R��
    // �O��
    JTable jtable2 = getTable("Table2");// �O��
    exeUtil.doChangeTableField(jtable2, "BarCode", 0); /* [���X�s��] ���� */
    // exeUtil.doChangeTableField(jtable2, "���|���B", 3) ; /* [���|���B[ ����*/
    getTableButton("Table2", 0).setEnabled(true);// �s�W
    getTableButton("Table2", 2).setEnabled(true);// �R��
    // �ӤH����
    JTable jtable3 = getTable("Table3");// �O��
    exeUtil.doChangeTableField(jtable3, "BarCode", 0); /* [���X�s��] ���� */
    // doChangTableField(jtable3, "�ұo�b�B", 0) ; /* [�ұo�b�B] ����*/
    // doChangTableField(jtable3, "��ú���B", 73) ; /* [��ú���B] ����*/
    // doChangTableField(jtable3, "�|�v", 0) ; /* [�榡] ����*/
    // exeUtil.doChangeTableField(jtable3, "�ɥR�O�O", 0) ; /* �ɥR�O�O ����*/
    // exeUtil.doChangeTableField(jtable3, "����", 0) ; /* ���� ����*/
    exeUtil.doChangeTableField(jtable3, "�I�ڱ���", 0); /* �I�ڱ��� ���� */
    exeUtil.doChangeTableField(jtable3, "<html>�ұo���I����<br>�����", 0); /* [�榡] ���� */
    getTableButton("Table3", 0).setEnabled(true);// �s�W
    getTableButton("Table3", 2).setEnabled(true);// �R��
    // ����
    JTable jtable4 = getTable("Table4");// �O��
    exeUtil.doChangeTableField(jtable4, "BarCode", 0); /* [���X�s��] ���� */
    exeUtil.doChangeTableField(jtable4, "���|���B", 0); /* ���|���B ���� */
    getTableButton("Table4", 0).setEnabled(true);// �s�W
    getTableButton("Table4", 2).setEnabled(true);// �R��
    // ����
    JTable jtable6 = getTable("Table6");// �O��
    exeUtil.doChangeTableField(jtable6, "BarCode", 0); /* [���X�s��] ���� */
    exeUtil.doChangeTableField(jtable6, "��", 24); /* [Pop-Up] �T�w */
    exeUtil.doChangeTableField(jtable6, "PurchaseNo", 0); /* [PurchaseNo] ���� */
    exeUtil.doChangeTableField(jtable6, "�O�d��", 0); /* [�O�d��] ���� */
    // if(!"B3018".equals(getUser()))doChangTableField(jtable6, "���ʼt��", 0) ; /*
    // [���ʼt��] ����*/
    //
    JTable jtable8 = getTable("Table8");// �O��
    exeUtil.doChangeTableField(jtable8, "�w�дڥ��I���B", 0);
    // �禬
    JTable jtable7 = getTable("Table7");// �O��
    exeUtil.doChangeTableField(jtable7, "BarCode", 0); /* [���X�s��] ���� */
    exeUtil.doChangeTableField(jtable7, "OptometryNo", 0); /* [OptometryNo] ���� */
    exeUtil.doChangeTableField(jtable7, "PurchaseNo1", 0); /* [PurchaseNo1] ���� */
    exeUtil.doChangeTableField(jtable7, "PurchaseNo2", 0); /* [PurchaseNo2] ���� */
    exeUtil.doChangeTableField(jtable7, "PurchaseNo3", 0); /* [PurchaseNo3] ���� */
    exeUtil.doChangeTableField(jtable7, "BarCodeRef", 0); /* [BarCodeRef] ���� */
    exeUtil.doChangeTableField(jtable7, "OptometryVersion", 0); /* [OptometryVersion] ���� */
    exeUtil.doChangeTableField(jtable7, "��", 24); /* [Pop-Up] �T�w */
    exeUtil.doChangeTableField(jtable7, "��", 24); /* [Pop-Up] �T�w */
    // doChangTableField(jtable7, "����", 0) ; /* */
    // �S�����
    JTable jtable5 = getTable("Table5");// �O��
    exeUtil.doChangeTableField(jtable5, "BarCode", 0); /* [���X�s��] ���� */
    //
    stringFlow = stringFlow.replaceAll("F", "");
    if (stringFlow.indexOf("ñ��") != -1) {
      // setEditable("DocNo2", false) ;
      setEditable("DocNo3", false);
      setEditable("DepartNo", false);
      setEditable("KindNoD", false);
      setVisible("InvoiceMoneySUM", true); //
      setVisible("InvoiceTaxSum", true); //
      getButton("Button3").setVisible(true); // �U�@��
      // setEditable("Multi-InvoiceKind", true) ;
      getButton("Button7").setVisible(true); // �h��
      getButton("Button2").setVisible(true);// ñ��
      getButton("Button2").setLabel("�]��ñ��"); // ñ��
      //
      getButton("ButtonYearEnd").setVisible(true && "Z6".equals(stringComNo)); // �~���w��
      // �o��
      // exeUtil.doChangeTableField(jtable1, "�榡", 90) ; /* [�榡] ����*/
      // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] ����*/
      // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] ����*/
      exeUtil.doChangeTableField(jtable1, "����N��", 119); /* [����N��] ���� */
      // �O��
      if (jtable2.getRowCount() > 0) {
        // �s�Ϊ��B���n��
        // exeUtil.doChangeTableField(jtable2, "���|���B", 180) ;
        getTableButton("Table1", 0).setEnabled(true);// �s�W
        getTableButton("Table1", 2).setEnabled(true);// �R��
      }
      getcLabel("Table2").setBounds(0, 0, 950, 200);
      // ��ú
      getcLabel("Table3").setBounds(0, 0, 875, 195);
      // exeUtil.doChangeTableField(jtable3, "����", 90) ; /* ���|���B ����*/
      exeUtil.doChangeTableField(jtable3, "�I�ڱ���", 75); /* �I�ڱ��� ���� */
      // ����
      getcLabel("Table4").setBounds(0, 0, 780, 200);
      exeUtil.doChangeTableField(jtable4, "���|���B", 120); /* ���|���B ���� */
    } else if (stringFlow.indexOf("�~��") != -1 || stringFlow.indexOf("�f��") != -1) {
      getButton("ButtonYearEnd").setVisible(true && "Z6".equals(stringComNo)); // �~���w��
      // setEditable("DocNo2", false) ;
      setEditable("DocNo3", false);
      setEditable("DepartNo", true);
      if (getValue("BarCode").startsWith("Z") || getValue("BarCode").startsWith("AC9555")) {
        setEditable("BarCode", true);
      }
      // setEditable("BarCode", getValue("BarCode").startsWith("Z")) ;
      // setEditable("KindNoD", true && "".equals(getValue("KindNoD").trim())) ;
      setEditable("KindNoD", false);
      //
      if (stringFlow.indexOf("Doc2M0122") != -1) {
        getButton("Button3").setVisible(true); // �U�@��
      }
      // �o��
      // exeUtil.doChangeTableField(jtable1, "�榡", 90) ; /* [�榡] ����*/
      // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] ����*/
      // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] ����*/
      exeUtil.doChangeTableField(jtable1, "����N��", 119); /* [����N��] ���� */
      // �O��
      // exeUtil.doChangeTableField(jtable2, "���|���B", 180) ;
      getcLabel("Table2").setBounds(0, 0, 950, 200);
      // ����
      getcLabel("Table4").setBounds(0, 0, 780, 200);
      exeUtil.doChangeTableField(jtable4, "���|���B", 120); /* ���|���B ���� */
      //
      getButton("ButtonError").setVisible(true); // �@�o
    } else {
      if (stringFlow.indexOf("�ӿ�F") != -1) getButton("Button3").setVisible(true); // �U�@��
      // �ӿ�
      // setEditable("DocNo2", true) ;
      setEditable("DocNo3", true);
      setEditable("DepartNo", "B3018".equals(getUser()));
      if (stringFlow.indexOf("--") != -1) {
        // �O��
        getcLabel("Table2").setBounds(0, 0, 950, 200);
        // exeUtil.doChangeTableField(jtable2, "���|���B", 180) ;
        // �o��
        // exeUtil.doChangeTableField(jtable1, "�榡", 90) ; /* [�榡] ����*/
        // exeUtil.doChangeTableField(jtable1, "�o�����|���B", 118) ; /* [�o�����|���B] ����*/
        // exeUtil.doChangeTableField(jtable1, "�o���|�B", 101) ; /* [�o���|�B] ����*/
        // exeUtil.doChangeTableField(jtable1, "����N��", 119) ; /* [����N��] ����*/
        setEditable("BarCode", POSITION == 1);
        setEditable("KindNoD", true && "".equals(getValue("KindNoD").trim()));
        getButton("ButtonError").setVisible(true); // �@�o
      } else {
        // setEditable("KindNoD", false) ;
      }
      setEditable("KindNoD", false);
      getcLabel("Table2").setBounds(0, 0, 950, 200);
      getcLabel("Table4").setBounds(0, 0, 660, 200);
    }
  }

  public void doControlField(Vector vectorCostID, FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    // �h�ڬd�߫��s����
    String stringID = getValue("ID").trim();
    if ("".equals(stringID)) {
      getButton("ButtonReason").setVisible(false);
    } else {
      getButton("ButtonReason").setVisible((exeFun.getDoc2M010Reason(stringID, "A", "")).length > 0);
    }
    //
    setEditable("CoinType", false);
    // �T�껡��
    getcLabel("textDB").setLocation(5, 490);
    // ����
    String stringDepartNoSubject = "" + get("EMP_DEPT_CD_N");
    Vector vectorValue = new Vector();
    vectorValue.add("");
    vectorValue.add("O3¾���˭�");
    vectorValue.add("��������]�ظm");
    setReference("LabelKind", vectorValue, vectorValue);
    setVisible("LabelKind", stringDepartNoSubject.startsWith("A17"));
    //
    String stringFunction = getFunctionName();
    boolean booleanFalg = stringFunction.indexOf("--") != -1;
    setVisible("ConuntType", booleanFalg);
    setVisible("ConuntType", booleanFalg);
    if ("B3018".equals(getUser())) {
      getButton("ShowTableRealMoney").setVisible(true);
      setVisible("BarCodeOld", true);
      setVisible("DocNoOld", true);
      getButton("Button11").setVisible(true); // ��P ����P�B
    } else {
      getButton("ShowTableRealMoney").setVisible(false);
      getButton("Button11").setVisible(false); // ��P ����P�B
    }
    // ����s�X�̤j�ȧP�_
    if (getFunctionName().indexOf("�ӿ�") != -1 || getFunctionName().indexOf("�g��") != -1) getButton("Button1").doClick();
    //
    JTable jtable2 = getTable("Table2");
    // System.out.println("jtable2.getRowCount()-----------------------"+jtable2.getRowCount())
    // ;
    if (jtable2.getRowCount() > 0) {
      // �s�Ϊ��B���n��
      String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
      if (vectorCostID.indexOf(stringCostID) != -1) {
        getButton("Button12").setVisible(true); // �έp��
      } else {
        getButton("Button12").setVisible(false); // �έp��
      }
    } else {
      getButton("Button12").setVisible(false); // �έp��
    }
    // ñ��
    String stringUndergoWrite = getValue("UNDERGO_WRITE").trim();
    /*
     * if("Y".equals(stringUndergoWrite)) { setEditable("Multi-InvoiceKind", false)
     * ; } else { setEditable("Multi-InvoiceKind", true) ; }
     */
    //
    String stringBarCode = getValue("BarCode").trim();
    String stringDepartNo = "";
    String[][] retDoc1M040 = null;
    setValue("BarCodeOld", stringBarCode);
    setValue("DocNoOld", getValue("DocNo"));
    if (!"".equals(stringBarCode)) {
      retDoc1M040 = exeFun.getDoc1M040(stringBarCode);
      if (retDoc1M040.length > 0) {
        stringDepartNo = retDoc1M040[retDoc1M040.length - 1][0].trim();
        stringDepartNo = exeFun.getDepartName(stringDepartNo);
      }
    }
    setValue("UNDERGO_WRITE_DEPT", stringDepartNo);
    // �ǲ����X
    String stringVoucherFlowNo = "";
    String stringVoucherDate = "";
    // 0 BarCode 1 DocNo 2 RowType 3 RecordNo 4 VOUCHER_YMD
    // 5 VOUCHER_FLOW_NO 6 VOUCHER_SEQ_NO 7 COMPANY_CD 8 KIND 9 DB_CR_CD
    // 10 ACCT_NO 11 DEPT_CD 12 OBJECT_CD 13 AMT 14 EXCHANG_AMT
    // 15 MONTEARY 16 ClaimerMoney 17 STATUS_CD 18 DESCRIPTION_1 19 DESCRIPTION_2
    // 20 DESCRIPTION_3 21 DESCRIPTION_4 22 DESCRIPTION_5 23 DESCRIPTION 24
    // LAST_USER
    // 25 LAST_YMD
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
    String stringVoucherYMD = retTable[0][4].trim();
    //
    stringVoucherYMD = exeUtil.getDateConvertRoc(stringVoucherYMD).replaceAll("/", "");
    retTable = exeFun.getFED1012(stringVoucherYMD, retTable[0][5].trim(), retTable[0][7].trim(), retTable[0][8].trim());
    if (retTable.length == 0) {
      setVisible("VoucherFlowNoButton", false);
      return;
    }
    //
    stringVoucherFlowNo = retTable[0][5].trim();
    stringVoucherDate = retTable[0][4].trim();
    if (!"0".equals(stringVoucherFlowNo) && !"".equals(stringVoucherDate)) {
      stringVoucherFlowNo = stringVoucherDate + "-" + convert.add0(stringVoucherFlowNo, "5");
      // �����β������
      String stringDateMessage = "";
      String stringCheckNoessage = "";
      String stringAcctNo = "";
      String[][] retFED1004 = null;
      for (int i = 0; i < retTable.length; i++) {
        if ("D".equals(retTable[i][9].trim())) continue;
        //
        stringAcctNo = retTable[i][10].trim();
        retFED1004 = exeFun.getFED1004(stringAcctNo);
        for (int int1004 = 0; int1004 < retFED1004.length; int1004++) {
          if ("A04".equals(retFED1004[int1004][0].trim())) {
            // �����
            if (!"".equals(stringDateMessage)) stringDateMessage += "�B";
            stringDateMessage += retTable[i][18 + int1004].trim();
          }
          if ("A03".equals(retFED1004[int1004][0].trim())) {
            // �䲼
            if (!"".equals(stringCheckNoessage)) stringCheckNoessage += "�B";
            stringCheckNoessage += retTable[i][18 + int1004].trim();
          }
        }
      }
      if (!"".equals(stringDateMessage) || !"".equals(stringCheckNoessage)) stringVoucherFlowNo += " (�T��...)";
      // if(!"".equals(stringDateMessage)) stringVoucherFlowNo += "
      // �����("+stringDateMessage+")" ;
      // if(!"".equals(stringCheckNoessage)) stringVoucherFlowNo += "
      // �䲼("+stringCheckNoessage+")" ;
      setVisible("VoucherFlowNoButton", true);
      // getButton("VoucherFlowNoButton").setLabel("�ǲ����X" + stringVoucherFlowNo+" ") ;
      getButton("VoucherFlowNoButton").setLabel("�ǲ��T��");
    } else {
      setVisible("VoucherFlowNoButton", false);
      getButton("VoucherFlowNoButton").setLabel("");
    }
  }

  // ����
  public void doControlTab1(Vector vectorCostID, Doc.Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");// �o��
    JTable jtable2 = getTable("Table2");// �O��
    JTable jtable3 = getTable("Table3");// �ӤH����
    //
    JTabbedPane jTabbedPane = getTabbedPane("Tab1");
    String stringDocNoType = getValue("DocNoType").trim();
    if (!"".equals(getValue("RetainBarCode").trim()) || "B".equals(stringDocNoType)) {
      // getButton("ButtonDocNoType").doClick() ;
      return;
    }
    setEditable("Retain", "C".equals(stringDocNoType));
    setEditable("DocNoType", true);
    //
    jTabbedPane.setEnabledAt(2, true); // �O��
    jTabbedPane.setEnabledAt(3, true); // �o��
    jTabbedPane.setEnabledAt(4, true); // �ӤH����
    jTabbedPane.setEnabledAt(5, true); // ����
    jTabbedPane.setEnabledAt(6, true);// �S�����
    //
    if (jtable1.getRowCount() > 0 && jtable3.getRowCount() > 0) {

    } else if (jtable1.getRowCount() > 0) {
      getTabbedPane("Tab1").setEnabledAt(4, false); // �ӤH����
    } else if (jtable3.getRowCount() > 0) {
      getTabbedPane("Tab1").setEnabledAt(3, false);
      getTabbedPane("Tab1").setEnabledAt(5, false);
    }
    // �S��
    Vector vectorDocNo = new Vector();
    String stringDocNo = getValue("DocNo").trim();
    String stringUndergoWrite = getValue("UNDERGO_WRITE").trim();
    vectorDocNo.add("03319610003");
    if (jtable2.getRowCount() > 0) {
      // �s�Ϊ��B���n��
      String stringCostID = ("" + getValueAt("Table2", 0, "CostID")).trim();
      if (vectorCostID.indexOf(stringCostID) != -1 && vectorDocNo.indexOf(stringDocNo) == -1) {
        String stringFlow = getFunctionName();
        //
        // getTabbedPane("Tab1").setEnabledAt(4, false) ; // �ӤH����
        getTabbedPane("Tab1").setEnabledAt(5, false); // ����
      }
    }
  }

  // �o��
  public boolean doControlTable1(FargloryUtil exeUtil, Doc.Doc2M010 exeFun) throws Throwable {
    JTable jtable1 = getTable("Table1");// �o��
    String stringFlow = getFunctionName();
    String stringInvoiceNo = "";
    String stringFactoryNo = "";
    String stringInvoiceKind = "";
    String stringInvoiceTaxType = exeFun.getInvoiceTaxType(jtable1);
    String stringEDateTime = exeUtil.doSubstring(getValue("EDateTime").trim(), 0, 10);
    Vector vectorInvoiecNo = new Vector();
    boolean booleanInvoiceKind = false;
    boolean booleanFlow = ((stringFlow.indexOf("�g��") != -1 || stringFlow.indexOf("�ӿ�") != -1) && stringFlow.indexOf("--") == -1);
    //
    String[] arrayInvoiceKind = { "N", "M", "B", "F", "C", "G", "X", "D", "E", "A", "O", "K", "P", "Q", "R", "S", "T", "L" };
    String[] arrayInvoiceKindName = { "�q�l�p���", "�Τ@�o���T�p��(��})", "���Ⱦ��T�p��", "�����N�x����|", "����", "���Ⱦ������p", "���ڤ��t�|", "���o�覩", "�K�|", "�q��", "�ȥ��q�l�o��(�����p)21", "�ȥ��q�l�o��(�����p)22", "�ȥ��q�l�o��(�����p)25",
        "���㸹�X�J�`25", "���㸹�X25", "���㸹�X���u25", "���㸹�X�J�`���u25", "�q�l�o��" };
    boolean booleanPurchaseNoExist = !"Y".equals(getValue("PurchaseNoExist").trim());
    if ("2016/12/28".compareTo(stringEDateTime) < 0) {
      arrayInvoiceKind[9] = "";
    }
    if ("2016/12/28".compareTo(stringEDateTime) < 0) {
      arrayInvoiceKind[10] = "";
      arrayInvoiceKind[11] = "";
      arrayInvoiceKind[12] = "";
    }
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
    // System.out.println("stringInvoiceTaxType---------------------["+stringInvoiceTaxType+"]")
    // ;
    if ("E".equals(stringInvoiceTaxType) || "F".equals(stringInvoiceTaxType)) {
      setValue("Multi-InvoiceKind", "Y");
      // exeUtil.doChangeTableField(jtable1, "�榡", 90) ; /* [�榡] ����*/
    } else {
      setValue("Multi-InvoiceKind", "N");
      if (booleanFlow) {
        // exeUtil.doChangeTableField(jtable1, "�榡", 0) ; /* [�榡] ����*/
      }
    }
    //
    setVisible("SpecicalRealMoneySum", false);
    if (jtable1.getRowCount() > 0) jtable1.setRowSelectionInterval(0, 0);
    //
    for (int intRowNo = 0; intRowNo < jtable1.getRowCount(); intRowNo++) {
      stringInvoiceNo = (String) getValueAt("Table1", intRowNo, "InvoiceNo");
      stringFactoryNo = (String) getValueAt("Table1", intRowNo, "FactoryNo");
      stringInvoiceKind = (String) getValueAt("Table1", intRowNo, "InvoiceKind");
      //
      if (!"D".equals(stringInvoiceKind)) booleanInvoiceKind = true; // �Ȧb���@�ܧ�
      setVisible("SpecicalRealMoneySum", true);
      if (stringFactoryNo.trim().length() == 10) {
        setEditable("Table1", intRowNo, "InvoiceKind", false); // �榡
      } else {
        setEditable("Table1", intRowNo, "InvoiceKind", true); // �榡
      }
      vectorInvoiecNo.add(stringInvoiceNo.trim());
    }
    // �w�s�b�o��
    put("Doc2M010_InvoiceNo_Vector", vectorInvoiecNo);
    // �o�����X�p�۰ʵ���
    getButton("Button9").doClick();
    return booleanInvoiceKind;
  }

  // �O��
  public void doControlTable2(boolean booleanInvoiceKind, Doc.Doc2M010 exeFun, FargloryUtil exeUtil) throws Throwable {
    JTable jtable2 = getTable("Table2");// �O��
    JTable jtableUse = null;
    //
    if (jtable2.getRowCount() > 0) jtable2.setRowSelectionInterval(0, 0);
    // �O�Ϊ�椺�e�]�w
    String stringCDate = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringFactoryNo = getValue("FactoryNoSpec").trim();
    String[] arrayTable = { "Table1", "Table3" };
    String stringComNo = getValue("ComNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringInOut = "";
    String stringCostID = "";
    String stringCostID1 = "";
    String stringKey = "";
    double doubleRealMoney = 0;
    double doubleTaxRate = exeUtil.doParseDouble(exeFun.getDoc2M040()[4].trim());
    Vector vectorFunction = new Vector();
    Vector vectorKey = new Vector();
    boolean booleanFlag = false;
    boolean booleanNoExist = getValue("PurchaseNoExist").trim().equals("Y");
    boolean booleanTableElse = false;
    boolean booleanCostID101 = false;
    //
    if ("".equals(stringFactoryNo)) {
      String stringTableName = "";
      for (int intNo = 0; intNo < arrayTable.length; intNo++) {
        stringTableName = arrayTable[intNo].trim();
        jtableUse = getTable(stringTableName);
        if (jtableUse.getRowCount() > 0) {
          stringFactoryNo = "" + getValueAt(stringTableName, 0, "FactoryNo");
          break;
        }
      }
    }
    //
    // vectorFunction.add("��P-�дڥӽЮ�-�~��(Doc2M011)") ;
    // vectorFunction.add("��P-�дڥӽЮ�--�ӿ�(Doc2M010)") ;
    // vectorFunction.add("�дڥӽЮ�-ñ��2(Doc2M010)") ;
    String stringDepartNo = "";
    String stringProjectID1 = "";
    String stringProjectID1BT = "";
    String stringBudgetID = "";
    String stringSpecBudgetVoucher = "" + get("SPEC_BUDGET_VOUCHER");
    String[][] retDoc7M0265 = null;
    String[][] retDoc2M020 = null;
    boolean boolean033FG = stringSpecBudgetVoucher.indexOf(getValue("DocNo1").trim()) != -1;
    boolean booleanBT = true;
    //
    setVisible("ButtonSetTable2RealTotalMoney", false);
    //
    Hashtable hashtbleFunctionType = exeFun.getCostIDVDoc2M0201H(stringComNo, "", "", stringCDate, "");
    Vector vectorDoc2M0201ForF = null;
    Vector vectorDoc2M0201ForK = null;
    Vector vectorDoc2M0201ForW = null;
    vectorDoc2M0201ForF = (Vector) hashtbleFunctionType.get("F");
    if (vectorDoc2M0201ForF == null) vectorDoc2M0201ForF = new Vector();
    vectorDoc2M0201ForK = (Vector) hashtbleFunctionType.get("K");
    if (vectorDoc2M0201ForK == null) vectorDoc2M0201ForK = new Vector();
    vectorDoc2M0201ForW = (Vector) hashtbleFunctionType.get("W");
    if (vectorDoc2M0201ForW == null) vectorDoc2M0201ForW = new Vector();
    for (int intRowNo = 0; intRowNo < jtable2.getRowCount(); intRowNo++) {
      stringInOut = "" + jtable2.getValueAt(intRowNo, 2);
      doubleRealMoney += exeUtil.doParseDouble("" + getValueAt("Table2", intRowNo, "RealMoney"));
      stringCostID = ("" + getValueAt("Table2", intRowNo, "CostID")).trim();
      stringCostID1 = ("" + getValueAt("Table2", intRowNo, "CostID1")).trim();
      stringDepartNo = ("" + getValueAt("Table2", intRowNo, "DepartNo")).trim();
      stringProjectID1 = ("" + getValueAt("Table2", intRowNo, "ProjectID1")).trim();
      stringKey = stringCostID + "---" + stringCostID1;
      booleanBT = false;
      //
      if (vectorDoc2M0201ForK.indexOf(stringCostID + stringCostID1) != -1) booleanCostID101 = true;
      if (!booleanNoExist && vectorDoc2M0201ForF.indexOf(stringCostID + stringCostID1) != -1) booleanTableElse = true;
      if (vectorDoc2M0201ForW.indexOf(stringCostID + stringCostID1) != -1) booleanTableElse = true;
      //
      if (vectorKey.indexOf(stringKey) != -1) {
        if (!booleanNoExist && !booleanFlag && exeFun.getDoc2m048(stringCostID, stringCostID1, stringCDate, stringFactoryNo).length > 0) booleanFlag = true;
      }
      vectorKey.add(stringKey);
      if ("I".equals(stringInOut)) {
        setEditable("Table2", intRowNo, "ProjectID", false); // �j�קO
        setEditable("Table2", intRowNo, "ProjectID1", false); // �p�קO
        setEditable("Table2", intRowNo, 6, false); // �קO
      } else {
        // BT �קO �����������\�ק���u��ҤΪ��B
        if (!booleanNoExist && "100/11/16".compareTo(stringCDate) > 0) {
          if (retDoc7M0265 == null) {
            retDoc7M0265 = exeFun.getTableDataDoc("SELECT  ProjectID1 " + " FROM  Doc7M0265 " + " WHERE  ProjectIDMajor  =  'BT' " + " AND  ComNo  =  '" + stringComNo + "' ");
            for (int intNoL = 0; intNoL < retDoc7M0265.length; intNoL++) {
              stringProjectID1BT += "," + retDoc7M0265[intNoL][0].trim() + ",";
            }
          }
          if (retDoc2M020 == null) {
            retDoc2M020 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
            stringBudgetID = retDoc2M020.length > 0 ? retDoc2M020[0][0].trim() : "";
          }
          if (retDoc7M0265 != null && retDoc2M020 != null) {
            if (stringProjectID1BT.indexOf(stringProjectID1) != -1 && stringBudgetID.startsWith("B")) {
              if ("BC31,BC32,".indexOf(stringBudgetID) == -1) {
                booleanBT = true;
              }
            }
          }
        }
        setEditable("Table2", intRowNo, "RealTotalMoney", !booleanBT); // �j�קO
        //
        setEditable("Table2", intRowNo, "ProjectID", true); // �j�קO
        setEditable("Table2", intRowNo, "ProjectID1", true); // �p�קO
        setEditable("Table2", intRowNo, 6, true); // �קO
      }
      setVisible("ButtonSetTable2RealTotalMoney", booleanBT);
      // ���|���B
      /*
       * if(vectorFunction.indexOf(getFunctionName()) == -1) { setValueAt("Table2",
       * "", intRowNo, "RealMoney") ; }
       */
    }
    // �Dñ�ֵe�������\�ϥΪ̧@ �w�ⱱ�� �קO���u���
    if (getFunctionName().indexOf("ñ��") == -1) {
      boolean033FG = false;
    }
    boolean booleanFlagL = boolean033FG && "Z6".equals(stringComNo) && !isAssetPur();
    setVisible("ButtonTable10", booleanFlagL);
    //
    getButton("ButtonTableElse").setVisible(booleanTableElse);
    if (booleanInvoiceKind) doubleRealMoney += exeFun.doParseDouble(convert.FourToFive("" + (doubleRealMoney * (doubleTaxRate / 100)), 0));
    setValue("RealMoneySum", "" + doubleRealMoney);
    setVisible("ButtonCostUse", booleanFlag);
    // KindNoD �U�Ԧ����
    System.out.println("KindNoD �U�Ԧ����111-----------------------------------------------S");
    String[][] retTable2 = getTableData("Table2");
    Vector retVector = exeFun.getKindNoDs(retTable2, true, exeUtil);
    Vector vectorKindNoDValue = (Vector) retVector.get(0);
    Vector vectorKindNoDView = (Vector) retVector.get(1);
    setReference("KindNoD", vectorKindNoDView, vectorKindNoDValue);
    System.out.println("KindNoD �U�Ԧ����-----------------------------------------------E");
    // �ӤH��ú
    JTable jtable3 = getTable("Table3");
    if (!booleanNoExist && booleanCostID101) {
      exeUtil.doChangeTableField(jtable3, "�����O�γ���", 150);
      exeUtil.doChangeTableField(jtable3, "��", 24);
    } else {
      exeUtil.doChangeTableField(jtable3, "�����O�γ���", 0);
      exeUtil.doChangeTableField(jtable3, "��", 0);
    }
    // �O�ΦX�p-����
    put("Doc2M010_RealTaxMoney", "average");
    getButton("Button5").doClick();
    getButton("ButtonTable2Status").doClick();
  }

  public boolean isAssetPur() throws Throwable {
    JTable jtable7 = getTable("Table7");
    String stringBarCode = getValue("BarCode").trim();
    if (",P38453,".indexOf("," + stringBarCode + ",") != -1) return false;
    String stringOptometryType = "";
    for (int intNo = 0; intNo < jtable7.getRowCount(); intNo++) {
      stringOptometryType = ("" + getValueAt("Table7", intNo, "OptometryType")).trim();
      if ("B".equals(stringOptometryType)) return true;
    }
    return false;
  }

  // ��ú(�ӤH����)
  public void doControlTable3(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable3 = getTable("Table3");// ��ú
    //
    if (jtable3.getRowCount() > 0) jtable3.setRowSelectionInterval(0, 0);
    //
    String stringFlow = getFunctionName();
    String stringFactoryNo = "";
    String stringReceiptKind = "";
    if (stringFlow.indexOf("ñ��") == -1 && jtable3.getRowCount() > 0) {
      stringFactoryNo = ("" + getValueAt("Table3", 0, "FactoryNo")).trim();
      stringReceiptKind = ("" + getValueAt("Table3", 0, "ReceiptKind")).trim();
      // if("Z0001".equals(stringFactoryNo) && "A".equals(stringReceiptKind)) {
      // doChangTableField(jtable3, "��ú���B", 97) ; /* [��ú���B] ����*/
      // } else {
      // doChangTableField(jtable3, "��ú���B", 0) ; /* [��ú���B] ����*/
      // }
    }
    // �I�ڱ��� �U�Ԧ����]�w
    Vector vectorView = new Vector();
    Vector vectorValue = new Vector();
    String stringPayCondition = "";
    for (int intNo = 0; intNo < 2; intNo++) {
      stringPayCondition = getValue("PayCondition" + (intNo + 1));
      //
      if ("999".equals(stringPayCondition)) continue;
      //
      vectorValue.add(stringPayCondition);
      //
      if ("000".equals(stringPayCondition)) {
        stringPayCondition = "�{��";
      } else if ("0".equals(stringPayCondition)) {
        stringPayCondition = "�Y��";
      }
      vectorView.add(stringPayCondition);
    }
    setTableReference("Table3", 11, vectorView, vectorValue);
    //
    System.out.println("doChangeSupplementMoney----------------------------------------S");
    String stringUndergoWrite = getValue("UNDERGO_WRITE").trim();
    String stringToday = datetime.getToday("YYYY/mm/dd");
    if (stringToday.compareTo("2015/07/01") < 0 && ",E,Y,".indexOf(stringUndergoWrite) == -1 && ",B4197,".indexOf("," + getValue("EmployeeNo") + ",") == -1) {
      doChangeSupplementMoney(exeUtil, exeFun);
    }
    System.out.println("doChangeSupplementMoney----------------------------------------E");
    // ��ú���X�p�۰ʵ���
    getButton("Button10").doClick();
  }

  public void doChangeSupplementMoney(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtable3 = getTable("Table3");// ��ú
    String stringPayCondition1 = getValue("PayCondition1").trim();
    String stringPayCondition2 = getValue("PayCondition2").trim();
    String stringToday = datetime.getToday("YYYY/mm/dd");
    String stringEmployeeNo = getValue("EmployeeNo").trim();
    String stringField = "";
    String stringValue = "";
    String stringTemp = "";
    String[] arrayField = { "ReceiptTotalMoney", "ReceiptMoney", "ReceiptTax", "ReceiptTaxType", "ReceiptKind", "FactoryNo", "ACCT_NO", "SupplementMoney", "PayCondition1" };
    Hashtable hashtableData = new Hashtable();
    double doubleSupplementMoney = 0;
    if ("999".equals(stringPayCondition1)) {
      stringPayCondition1 = ("999".equals(stringPayCondition2)) ? "" : stringPayCondition2;
    } else {
      if (!"999".equals(stringPayCondition2) && exeUtil.doParseDouble(stringPayCondition1) < exeUtil.doParseDouble(stringPayCondition2)) {
        stringPayCondition1 = stringPayCondition2;
      }
    }
    hashtableData.put("PayCondition1", stringPayCondition1);
    hashtableData.put("EmployeeNo", stringEmployeeNo);
    hashtableData.put("TODAY", stringToday);
    hashtableData.put("SpecUserID", "Y");
    hashtableData.put("SpecCostID", "N");
    hashtableData.put("TYPE", "MONEY");
    stringValue = getTableData("Table21").length > 0 ? "Y" : "N";
    hashtableData.put("Table21Exist", stringValue);
    for (int intNo = 0; intNo < jtable3.getRowCount(); intNo++) {
      stringTemp = ("" + getValueAt("Table3", intNo, "SupplementMoney")).trim();
      if (exeUtil.doParseDouble(stringTemp) <= 0) continue;
      for (int intNoL = 0; intNoL < arrayField.length; intNoL++) {
        stringField = arrayField[intNoL];
        stringValue = ("" + getValueAt("Table3", intNo, stringField)).trim();
        //
        hashtableData.put(stringField, stringValue);
      }
      hashtableData.put("SupplementMoney", "0");
      exeFun.isSupplementMoneyOK(hashtableData, exeUtil);
      doubleSupplementMoney = exeUtil.doParseDouble("" + hashtableData.get("SupplementMoney"));
      //
      System.out.println(intNo + "��(" + stringTemp + ")��(" + convert.FourToFive("" + doubleSupplementMoney, 0) + ")(" + hashtableData.get("SupplementMoney")
          + ")--------------------------------------------------");
      setValueAt("Table3", convert.FourToFive("" + doubleSupplementMoney, 0), intNo, "SupplementMoney");
    }
  }

  // ����
  public void doControlTable4(Doc.Doc2M010 exeFun) throws Throwable {
  }

  // ����
  public void doControlTable6(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringPurchaseNoExist = getValue("PurchaseNoExist").trim();
    String stringFunctionName = getFunctionName();
    if ("Y".equals(stringPurchaseNoExist)) {
      getButton("ButtonTable6PurchaseData").doClick(); // ���ʳ渹-����
    } else {
      put("CostID", "");
      put("CostID1", "");
    }
    //
    if (stringFunctionName.indexOf("ñ��") == -1) return;
    if (!"B3018".equals(getUser())) return;
    //
    talk dbConstAsk = getTalk("" + get("put_Const_Ask"));
    JTable jtable6 = getTable("Table6");
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringKindNoFront = "24".equals(stringKindNo) ? "17" : "15";
    String stringPurchaseNo = "";
    String stringPurchaseNo1 = "";
    String stringPurchaseNo2 = "";
    String stringPurchaseNo3 = "";
    String stringFactoryNo = "";
    String stringPurchaseMoney = "";
    String stringBarCodePur = "";
    String stringMessage = "";
    String stringPrdocode = "";
    Hashtable hashtableAnd = new Hashtable();
    double doubleContractMoney = 0;
    double doubleUseMoney = 0;
    Vector vectorRelContractSendDetailFin = null;
    for (int intNo = 0; intNo < jtable6.getRowCount(); intNo++) {
      stringPurchaseNo1 = ("" + getValueAt("Table6", intNo, "PurchaseNo1")).trim();
      stringPurchaseNo2 = ("" + getValueAt("Table6", intNo, "PurchaseNo2")).trim();
      stringPurchaseNo3 = ("" + getValueAt("Table6", intNo, "PurchaseNo3")).trim();
      stringFactoryNo = ("" + getValueAt("Table6", intNo, "FactoryNo")).trim();
      stringPurchaseMoney = ("" + getValueAt("Table6", intNo, "PurchaseMoney")).trim();
      //
      stringPurchaseNo = stringPurchaseNo1 + stringPurchaseNo2 + stringPurchaseNo3;
      //
      hashtableAnd.put("ComNo", stringComNo);
      hashtableAnd.put("KindNo", stringKindNoFront);
      hashtableAnd.put("DocNo", stringPurchaseNo);
      stringBarCodePur = exeFun.getNameUnionDoc("BarCode", "Doc3M011", "", hashtableAnd, exeUtil);
      if ("".equals(stringBarCodePur)) continue;
      // 50 �U�H�U����ĵ��
      doubleContractMoney = getContractMoney(stringBarCodePur, stringFactoryNo, exeUtil, exeFun);
      if (doubleContractMoney <= 500000) continue;
      // ���ϥΦ� 30 %����ĵ��
      doubleUseMoney = getUseMoney(stringPurchaseNo, stringFactoryNo, stringPurchaseMoney, exeUtil, exeFun);
      if (doubleUseMoney < doubleContractMoney * 0.3) continue;
      // �X���y�{�P�_
      stringPrdocode = exeUtil.getNameUnion("prdocode", "prdt", " AND  social  =  '" + stringFactoryNo + "' ", new Hashtable(), dbConstAsk);
      //
      hashtableAnd.put("cid_barcode", stringBarCodePur);
      hashtableAnd.put("cid_prdocode", stringPrdocode);
      // hashtableAnd.put("permit", "1") ;
      vectorRelContractSendDetailFin = exeUtil.getQueryDataHashtable("rel_contract_send_detail_fin", hashtableAnd, "", dbConstAsk);
      if (vectorRelContractSendDetailFin.size() == 0) {
        if (!"".equals(stringMessage)) stringMessage += "\n";
        stringMessage += "���ʳ�(" + stringPurchaseNo + ")�t��(" + stringFactoryNo + ") �|�������X���y�{�C";
      }
    }
    if (!"".equals(stringMessage)) messagebox(stringMessage);
  }

  public double getContractMoney(String stringBarCodePur, String stringFactoryNo, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringPurchaseMoney = "";
    Vector vectorDoc3M012 = null;
    double doubleContractMoney = 0;
    Hashtable hashtableAnd = new Hashtable();
    Hashtable hashtableDoc3M012 = new Hashtable();
    //
    hashtableAnd.put("BarCode", stringBarCodePur);
    hashtableAnd.put("FactoryNo", stringFactoryNo);
    vectorDoc3M012 = exeFun.getQueryDataHashtableDoc("Doc3M012", hashtableAnd, "", new Vector(), exeUtil);
    for (int intNo = 0; intNo < vectorDoc3M012.size(); intNo++) {
      hashtableDoc3M012 = (Hashtable) vectorDoc3M012.get(intNo);
      if (hashtableDoc3M012 == null) continue;
      stringPurchaseMoney = "" + hashtableDoc3M012.get("PurchaseMoney");
      //
      doubleContractMoney += exeUtil.doParseDouble(stringPurchaseMoney);
    }
    //
    return doubleContractMoney;
  }

  public double getUseMoney(String stringPurchaseNo, String stringFactoryNo, String stringPurchaseMoney, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue("ComNo").trim();
    String stringKindNo = getValue("KindNo").trim();
    String stringBarCode = getValue("BarCode").trim();
    String stringSql = "";
    String[][] retDoc2M017 = null;
    String[][] retDoc6M012 = null;
    double doubleUseMoney = exeUtil.doParseDouble(stringPurchaseMoney);
    //
    stringSql = " SELECT  SUM(M17.PurchaseMoney) " + " FROM  Doc2M010 M10,  Doc2M017 M17 " + " WHERE  M10.BarCode  =  M17.BarCode " + " AND  M10.UNDERGO_WRITE  <>  'E' "
        + " AND  M10.BarCode  <>  '" + stringBarCode + "' " + " AND  M10.ComNo  =  '" + stringComNo + "' " + " AND  M10.KindNo  =  '" + stringKindNo + "' "
        + " AND  M17.PurchaseNo  =  '" + stringPurchaseNo + "' " + " AND  M17.FactoryNo  =  '" + stringFactoryNo + "' ";
    retDoc2M017 = exeFun.getTableDataDoc(stringSql);
    doubleUseMoney += exeUtil.doParseDouble(retDoc2M017[0][0]);
    //
    stringSql = " SELECT  SUM(RealTotalMoney) " + " FROM  Doc6M012  " + " WHERE  BarCode IN (SELECT  BarCode " + " FROM  Doc6M010  " + " WHERE  UNDERGO_WRITE  <>  'E' "
        + " AND  BarCode  <>  '" + stringBarCode + "' " + " AND  ComNo  =  '" + stringComNo + "' " + " AND  KindNo  =  '" + stringKindNo + "' " + " AND  PurchaseNo  =  '"
        + stringPurchaseNo + "' " + " AND  FactoryNo  =  '" + stringFactoryNo + "' " + ") ";
    retDoc6M012 = exeFun.getTableDataDoc(stringSql);
    doubleUseMoney += exeUtil.doParseDouble(retDoc6M012[0][0]);
    //
    return doubleUseMoney;
  }

  public void doControlTable7(Doc.Doc2M010 exeFun) throws Throwable {
    getButton("ButtonTable7Status").doClick();
  }
}
