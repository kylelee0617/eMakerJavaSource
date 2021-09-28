package Doc.Doc3;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import Doc.Doc2M010;
import Farglory.util.FargloryUtil;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;

public class Doc3M012 extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    // System.out.println("�U�@��----S") ;
    talk dbAO = getTalk("" + get("put_AO"));
    Doc2M010 exeFun = new Doc2M010();
    FargloryUtil exeUtil = new FargloryUtil();
    String stringFunctionName = getValue(".Function");
    Vector vectorTableDataS = (Vector) get("Doc3M011_Table_TABLEDATA_VECTOR");
    int intPos = exeUtil.doParseInteger("" + get("Doc3M011_Table_POSITION"));
    //
    if (stringFunctionName.indexOf("����") != -1) getButton("button2").doClick();
    //
    setValue("STATUS", "USE");
    put("Doc3M011_CHECK", "null");
    if (!"SYS".equals(getUser()) && !isBatchCheckOK(intPos, stringFunctionName, vectorTableDataS, exeFun, exeUtil, dbAO)) {
      put("Doc3M011_CHECK", "ERROR");
      setValue("STATUS", "null");
      return value;
    }
    setData(intPos, stringFunctionName, vectorTableDataS, exeUtil, exeFun);
    setValue("STATUS", "null");
    return value;
  }

  public void setData(int intPos, String stringFunctionName, Vector vectorTableDataS, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    // System.out.println("setData("+intPos+")vectorTableDataS("+vectorTableDataS.size()+")-----------------------------S")
    // ;
    String stringHistoryPrice = getValue("HistoryPrice").trim();
    String stringBudgetNum = getValue("BudgetNum").trim();
    String stringApplyMoney = getValue("ApplyMoney").trim();
    String stringActualNum = getValue("ActualNum").trim();
    String stringActualPrice = getValue("ActualPrice").trim();
    String stringPurchaseMoney = getValue("PurchaseMoney").trim();
    String stringApplyType = getValue(".ApplyType"); // ���ʬ� F
    String stringCheckAdd = getValue(".CheckAdd"); // �䥦�� F
    String stringCheckAddDescript = getValue(".CheckAddDescript");
    String stringDateStart = getValue("DateStart").trim();
    String stringDateEnd = getValue("DateEnd").trim();
    String stringCostID = getValue("CostID").trim();
    String stringCostID1 = getValue("CostID1").trim();
    String stringCostID2 = getValue("CostID2").trim();
    String stringCostIDDetail = getValue("CostIDDetail").trim();
    String stringDocNo173 = getValue("DocNo173").trim();
    boolean booleanApplyType = "F".equals(stringApplyType); // true ������
    //
    if (!"F".equals(stringApplyType)) {
      // �D����
      if ("805".equals(stringCostID + stringCostID1)) {
        if (!"F".equals(stringCheckAdd)) {
          setValue(".CheckAdd", "F");
          setValue(".CheckAddDescript", "ñ�e�s���G");
        }
      }
    }
    //
    stringDateStart = exeUtil.getDateConvert(stringDateStart);
    stringDateEnd = exeUtil.getDateConvert(stringDateEnd);
    //
    if (stringFunctionName.indexOf("����") == -1) {
      setValue("PurchaseMoney", stringApplyMoney);
      setValue("PurchaseMoneyCompute", stringApplyMoney);
      setValue("ActualPrice", stringHistoryPrice);
      setValue("ActualNum", stringBudgetNum);
    }
    if (booleanApplyType && stringFunctionName.indexOf("����") != -1) {
      setValue("BudgetNum", stringActualNum);
      setValue("HistoryPrice", stringActualPrice);
      setValue("ApplyMoney", stringPurchaseMoney);
    }
    String stringRecordNo = "";
    if (vectorTableDataS != null) {
      Hashtable hashtableTableDataRow = (Hashtable) vectorTableDataS.get(intPos);
      //
      stringRecordNo = "" + hashtableTableDataRow.get("RecordNo");
      if ("null".equals(stringRecordNo)) stringRecordNo = "";
      //
      hashtableTableDataRow.put("CostID", stringCostID);
      hashtableTableDataRow.put("CostID1", stringCostID1);
      hashtableTableDataRow.put("FactoryNo", getValue("FactoryNo").trim());
      hashtableTableDataRow.put("ClassName", getValue("ClassName").trim());
      hashtableTableDataRow.put("ClassNameDescript", getValue("ClassNameDescript").trim());
      hashtableTableDataRow.put("cno", getValue("Coun").trim());
      hashtableTableDataRow.put("tno", getValue("Town").trim());
      hashtableTableDataRow.put("DateStart", stringDateStart);
      hashtableTableDataRow.put("DateEnd", stringDateEnd);
      hashtableTableDataRow.put("Descript", getValue("Descript").trim());
      hashtableTableDataRow.put("Unit", getValue("Unit").trim());
      hashtableTableDataRow.put("BudgetNum", getValue("BudgetNum").trim());
      hashtableTableDataRow.put("ApplyMoney", getValue("ApplyMoney").trim());
      hashtableTableDataRow.put("HistoryPrice", getValue("HistoryPrice").trim());
      hashtableTableDataRow.put("ActualNum", getValue("ActualNum").trim());
      hashtableTableDataRow.put("ActualPrice", getValue("ActualPrice").trim());
      hashtableTableDataRow.put("PurchaseMoneyCompute", getValue("PurchaseMoneyCompute").trim());
      hashtableTableDataRow.put("PurchaseMoney", getValue("PurchaseMoney").trim());
      hashtableTableDataRow.put("ProjectID1", "");
      hashtableTableDataRow.put("FILTER", getValue("FILTER").trim());
      hashtableTableDataRow.put("SSMediaID", getValue("SSMediaID").trim());
      hashtableTableDataRow.put("PopCode", getValue("PopCode").trim());
      hashtableTableDataRow.put("CostIDDetail", stringCostIDDetail);
      hashtableTableDataRow.put("CostID2", stringCostID2);
      hashtableTableDataRow.put("DocNo173", stringDocNo173);
    }
    // �קO���u���
    JTable jtableProject = getTable("TableProject");
    String stringApplyPercent = "";
    String stringPurchasePercent = "";
    boolean booleanFlag = false;
    for (int intNo = 0; intNo < jtableProject.getRowCount(); intNo++) {
      stringApplyPercent = ("" + getValueAt("TableProject", intNo, "ApplyPercent")).trim();
      stringApplyMoney = ("" + getValueAt("TableProject", intNo, "ApplyMoney")).trim();
      stringPurchasePercent = ("" + getValueAt("TableProject", intNo, "PurchasePercent")).trim();
      stringPurchaseMoney = ("" + getValueAt("TableProject", intNo, "PurchaseMoney")).trim();
      //
      if (stringFunctionName.indexOf("����") == -1) {
        setValueAt("TableProject", stringApplyPercent, intNo, "PurchasePercent");
        setValueAt("TableProject", stringApplyMoney, intNo, "PurchaseMoney");
        System.out.println(intNo + "stringApplyMoney(" + stringApplyMoney + ")-------------------------------1");
      } else {
        if (booleanApplyType) booleanFlag = true;
        if (exeUtil.doParseDouble(stringApplyPercent) <= 0 || exeUtil.doParseDouble(stringApplyMoney) <= 0) booleanFlag = true;
        //
        if (booleanFlag) {
          setValueAt("TableProject", stringPurchasePercent, intNo, "ApplyPercent");
          setValueAt("TableProject", stringPurchaseMoney, intNo, "ApplyMoney");
          System.out.println(intNo + "stringApplyMoney(" + stringApplyMoney + ")-------------------------------2");
        }
      }
    }
    setTableData(intPos, ".Table6", "TableProject", exeUtil);

    // POP ���
    JTable jtable9 = getTable("Table9");
    String[][] retTableData = getTableData("Table9");
    Vector vectorTableDataL = new Vector();
    String stringRecordNoL = "";
    for (int intNo = 0; intNo < jtable9.getRowCount(); intNo++) {
      stringRecordNoL = ("" + getValueAt("Table9", intNo, "RecordNo")).trim();
      //
      if (stringRecordNoL.equals(stringRecordNo)) {
        if (!"781".equals(getValue("CostID").trim() + getValue("CostID1").trim())) continue;
      }
      //
      vectorTableDataL.add(retTableData[intNo]);
    }
    setTableData(".Table9", (String[][]) vectorTableDataL.toArray(new String[0][0]));

    // ĳ����T���
    setTableData(intPos, ".Table17", "Table17", exeUtil);
    System.out.println("setData-----------------------------E");
  }

  public void setTableData(int intPos, String stringTableFront, String stringTableThis, FargloryUtil exeUtil) throws Throwable {
    // JTable jtable17 = getTable("Table17") ;
    Vector vectorTableData = new Vector();
    String stringBarCode = getValue(".BarCode").trim();
    String[][] retTableData = getTableData(stringTableFront);
    String[][] retTableDataL = getTableData(stringTableThis);
    for (int intNo = 0; intNo < retTableData.length; intNo++) {
      if (exeUtil.doParseInteger(retTableData[intNo][2].trim()) == (intPos + 1)) {
        continue;
      }
      vectorTableData.add(retTableData[intNo]);
    }
    for (int intNo = 0; intNo < retTableDataL.length; intNo++) {
      retTableDataL[intNo][1] = stringBarCode;
      retTableDataL[intNo][2] = "" + (intPos + 1);
      vectorTableData.add(retTableDataL[intNo]);
    }
    setTableData(stringTableFront, (String[][]) vectorTableData.toArray(new String[0][0]));
  }

  // �ˮ�
  // �e�ݸ���ˮ֡A���T�^�� True
  public boolean isBatchCheckOK(int intPos, String stringFunctionName, Vector vectorTableDataS, Doc2M010 exeFun, FargloryUtil exeUtil, talk dbAO) throws Throwable {
    String stringApplyType = getValue(".ApplyType").trim();// ���ʬ� F�A�T�w�겣 D
    String stringBarCode = getValue(".BarCode").trim();
    String stringComNo = getValue(".ComNo").trim();
    String stringDocNo1 = getValue(".DocNo1").trim();
    String stringCDateAC = exeUtil.getDateConvert(getValue(".CDate"));
    boolean booleanApplyTypeF = "F".equals(stringApplyType);
    boolean booleanActionNo = !"".equals(getValue(".ActionNo"));
    Hashtable hashtbleFunctionType = exeFun.getCostIDVDoc2M0201H(stringComNo, "", "", stringCDateAC, "");
    boolean booleanUnit = stringCDateAC.compareTo("2014/02/21") > 0;
    Vector vecrorCostIDTypeO = (Vector) hashtbleFunctionType.get("O");
    if (vecrorCostIDTypeO == null) vecrorCostIDTypeO = new Vector();
    String stringCostIDDetail = getValue("CostIDDetail").trim();
    // �T�w�겣 �N�X
    String stringFILTER = getValue("FILTER").trim();
    String stringTemp = "";
    String stringInOutL = "";
    String stringFILTER_DO = "";
    String stringDepartNo = "";
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    boolean booleanCostIDDetail = exeUtil.getDateConvert(getValue("CDate")).compareTo("" + get("NO_PAGE_DATE")) >= 0;
    if (booleanCostIDDetail) {
      if ("".equals(stringCostIDDetail) && !"D".equals(stringApplyType)) {
        messagebox("[�����u�ƥN�X] ���i���ťաC");
        getcLabel("CostIDDetail").requestFocus();
        return false;
      }
      if ("D".equals(stringApplyType)) {
        setValue("CostIDDetail", "");
      }
    }
    if (getTableData("TableProject").length > 0) {
      stringInOutL = ("" + getValueAt("TableProject", 0, "InOut")).trim();
      stringDepartNo = ("" + getValueAt("TableProject", 0, "DepartNo")).trim();
    }
    // 20180309 �T�w�겣�P�_���� By B03812
    /*
     * if("D".equals(stringApplyType)) { talk dbAsset = getTalk(""+get("put_Asset"))
     * ; Vector vectorColumnName = new Vector() ; Vector vectorAsAssetFilter = new
     * Vector() ; String stringComNoAcctNo = "" ; String[][] retDoc2M0401 =
     * exeFun.getDoc2M0401("", "U", "") ; if(retDoc2M0401.length > 0) {
     * stringFILTER_DO = retDoc2M0401[0][2].trim() ; } // ���i���ť�
     * if("".equals(stringFILTER)) { messagebox("[�T��N�X] ���i���ťաC") ;
     * getcLabel("FILTER").requestFocus() ; return false ; } // �s�b�ˮ�
     * vectorAsAssetFilter = exeUtil.getQueryDataHashtable("AS_ASSET_FILTER", new
     * Hashtable(), " AND  FILTER  = '"+stringFILTER+"' ", vectorColumnName,
     * dbAsset) ; if(vectorAsAssetFilter.size() == 0) {
     * messagebox("[�T��N�X] ���s�b��Ʈw���C") ; getcLabel("FILTER").requestFocus() ; return
     * false ; } if(!"".equals(stringInOutL)) { String stringSqlAnd = "" ;
     * String[][] retDoc2M0201 = null ; //if("033FG".equals(stringDocNo1)) {
     * stringSqlAnd = " AND  Remark =  '���~' " ; //} else
     * if("I".equals(stringInOutL)) {
     * if(stringSpecBudget.indexOf(","+stringDepartNo+",") != -1) { stringSqlAnd =
     * " AND  Remark =  '�~�~' " ; } else { stringSqlAnd = " AND  Remark =  '���~' " ; }
     * } else { stringSqlAnd = " AND  Remark =  '�~�~' " ; } retDoc2M0201 =
     * exeFun.getDoc2M0201(stringComNo, "", "", "O", stringCDateAC, stringSqlAnd) ;
     * if(retDoc2M0201.length == 0) { messagebox("��Ƶo�Ϳ��~�A�Ь���T�ǡC") ; return false ; }
     * setValue("CostID", retDoc2M0201[0][0].trim()) ; setValue("CostID1",
     * retDoc2M0201[0][1].trim()) ; if(!stringFILTER.equals(stringFILTER_DO)) { //
     * �������q�������ˮ� stringTemp = "SPEC_ACNTNO_SET_"+stringComNo ;
     * if("I".equals(stringInOutL)) stringTemp =
     * "SPEC_ACNTNO_SET_"+stringComNo+"_IN" ;
     * if(vectorColumnName.indexOf(stringTemp) == -1) {
     * messagebox("[�T��N�X] ���s�b���� [���q-�|�p���] ���C") ; getcLabel("FILTER").requestFocus()
     * ; return false ; } // �|�p��ئs�b�ˮ� Hashtable hashtableTmp = (Hashtable)
     * vectorAsAssetFilter.get(0) ; if(hashtableTmp == null) {
     * messagebox("��Ƶo�Ϳ��~�A�Ь���T�ǡC") ; getcLabel("FILTER").requestFocus() ; return
     * false ; } stringComNoAcctNo = ""+hashtableTmp.get(stringTemp) ;
     * if("null".equals(stringComNoAcctNo) || "".equals(stringComNoAcctNo)) {
     * messagebox("[�T��N�X] ���� [���q-�|�p���] ���ťաC") ; getcLabel("FILTER").requestFocus() ;
     * return false ; } } } }
     */
    else {
      setValue("FILTER", "");
    }
    // �дڥN�X
    String stringCostID = getValue("CostID").trim(); // if("B3018".equals(getUser())) messagebox(stringCostID) ;
    if ("".equals(stringCostID)) {
      messagebox("[�дڥN�X] ���i���ťաC");
      getcLabel("CostID").requestFocus();
      return false;
    }
    String stringCostID1 = getValue("CostID1").trim();// if("B3018".equals(getUser())) messagebox(stringCostID1) ;
    if ("".equals(stringCostID1)) {
      messagebox("[�p�дڥN�X] ���i���ťաC");
      getcLabel("CostID1").requestFocus();
      return false;
    }
    String stringCostID2 = getValue("CostID2").trim();
    if ("".equals(stringCostID2)) {
      setValue("CostID2", "XX");
    }
    // if("B3018".equals(getUser())) messagebox(stringCostID2) ;
    // 2018-02-14
    /*
     * if(!"D".equals(stringApplyType)) { vecrorCostIDTypeO.add("392") ;
     * vecrorCostIDTypeO.add("395") ; vecrorCostIDTypeO.add("396") ;
     * vecrorCostIDTypeO.add("704") ; vecrorCostIDTypeO.add("3110") ; // 2014-06-17
     * vecrorCostIDTypeO.add("3211") ; // 2014-06-17
     * if(",B67045,".indexOf(","+stringBarCode+",")==-1 &&
     * vecrorCostIDTypeO.indexOf(stringCostID+stringCostID1) != -1) { stringTemp =
     * "" ; for(int intNo=0 ; intNo<vecrorCostIDTypeO.size() ; intNo++) {
     * if(!"".equals(stringTemp)) stringTemp += "�B" ; stringTemp +=
     * ""+vecrorCostIDTypeO.get(intNo) ; }
     * messagebox("�D [�T�w�겣] ���дڥN�X ���ର "+stringTemp+"�C") ; return false ; } }
     */
    /*
     * 20180213 �������u�ƥN�X��78�}�Y�����Opop������ �����n��U�����檺��pop�N�X��
     */
    if (!stringCostIDDetail.startsWith("78")) {
      if (stringFunctionName.indexOf("����") != -1) {
        if (",782,".indexOf(stringCostID + stringCostID1) != -1 && ",P65288,P67082,P39936,Q02517,P90266,P65403,Q22241,P39874,".indexOf("," + getValue(".BarCode") + ",") == -1) {
          if ("".equals(getValue("PopCode").trim())) {
            messagebox("POP�N�X ���ର �ťաC");
            return false;
          }
        }
      }
    }
    if (stringSpecBudget.indexOf(getValue(".DocNo1")) == -1) {
      if (",033,053,133,".indexOf(exeUtil.doSubstring(getValue(".DocNo1"), 0, 3)) == -1) {
        if ("31,32,".indexOf(stringCostID) != -1) {
          messagebox("�D��P���� �����\�ϥ� [�дڥN�X](31�B32)�C\n(�����D�Ь� [�]�ȫ�])�C");
          if (!"SYS".equals(getUser())) return false;
        }
        if (exeUtil.doParseDouble(stringCostID) >= 70 && !"D".equals(stringApplyType)) {
          if (getValue(".DocNo1").startsWith("015") && "721,".indexOf(stringCostID + stringCostID1) != -1) {
            // �S�Ҥ��\
          } else {
            messagebox("�D��P���� �����\�ϥ� 70 ���᪺�дڥN�X�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])�C");
            if ("SYS,B3018,".indexOf(getUser()) == -1) return false;
          }
        }
      } else {
        // 2017-03-16 B2358 �ק��޿�A���\ 60 �ϥΩ󤺷~
        /*
         * if("60,".indexOf(stringCostID)!=-1) { if(!"0333".equals(getValue(".DocNo1")))
         * { messagebox("[��P����] �����\�ϥ� [�дڥN�X](60)�C\n(�����D�Ь� [�]�ȫ�])�C") ; return false ; } }
         */
      }
    }
    //
    String stringSqlAnd = " AND  FunctionType LIKE  '%2%' " + " AND  (DateStart='9999/99/99'  OR  DateStart<='" + stringCDateAC + "' )"
        + " AND  (DateEnd='9999/99/99'    OR  DateEnd>='" + stringCDateAC + "' )" + " AND  ComNo  IN ('ALL',  '" + stringComNo + "' )" + " AND  CostID  =  '" + stringCostID + "' "
        + " AND  CostID1  =  '" + stringCostID1 + "' ";
    Vector vectorDoc2M0201 = exeFun.getQueryDataHashtableDoc("Doc2M0201", new Hashtable(), stringSqlAnd, new Vector(), exeUtil);
    if (vectorDoc2M0201.size() > 0) {
      messagebox("[�дڥN�X][�p�дڥN�X] �����\�ϥΡC\n(�����D�Ь� [��P�޲z��])�C1");
      return false;
    }
    // �����ϥΪ�
    if (!isUnionPurchase(exeUtil, exeFun)) return false;
    //
    System.out.println("[�дڥN�X][�p�дڥN�X] �s�b(" + stringCostID + ")(" + stringCostID1 + ")(" + stringCostID2 + ")----------------------------------------------S");
    String[][] retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
    if (retDoc7M011.length == 0) {
      if (exeFun.getDoc2M0201("ALL", stringCostID, stringCostID1, "A").length == 0) {
        if (exeFun.getDoc2M0201(stringComNo, stringCostID, stringCostID1, "A").length == 0) {
          messagebox("[�дڥN�X][�p�дڥN�X] ���s�b��Ʈw���C\n(�����D�Ь� [��P�޲z��])�C1");
          return false;
        }
      }
    }
    System.out.println("[�дڥN�X][�p�дڥN�X] �s�b----------------------------------------------E");
    // �q���N�X �ˮ�
    String stringBigBudget = getBigBudget(stringCostID, stringCostID1, exeUtil, exeFun);
    String stringSSMediaID = getValue("SSMediaID").trim();
    String stringSSMediaID1 = "";
    String stringSSMediaID1DB = "";
    if (stringFunctionName.indexOf("����") == -1) {
      if (!"".equals(stringSSMediaID)) {
        // 0331I03201509-001 �s�b�ˮ�
        if (!isERPKeyExist(stringSSMediaID, exeUtil, dbAO)) return false;
        //
        // �S��дڥN�X�@�w���i�����ʥN�X
        // ���ʥN�X �P �дڥN�X �����ˮ�
        stringSSMediaID1 = exeUtil.doSubstring(stringSSMediaID, stringSSMediaID.length() - 13, stringSSMediaID.length() - 12);
        stringSSMediaID1DB = getSSMediaIDDoc(stringCostID, stringCostID1, exeUtil, exeFun);
        if (!"".equals(stringSSMediaID1DB) && !stringSSMediaID1.equals(stringSSMediaID1DB)) {
          messagebox("[�дڥN�X]" + stringSSMediaID1DB + " ���� [�q���N�X] ���Y���~�C");
          return false;
        }
        // �����������дڥN�X
        if ("A".equals(stringBigBudget)) {
          messagebox("[�q���N�X] �u��ϥΥ������� �дڥN�X�C");
          return false;
        }
      } else {
        // 0003 �~���ܰʦ��� �@�w�n�����ʥN�X
        /*
         * if("0003".equals(stringBigBudget)) { getcLabel("SSMediaID").requestFocus() ;
         * messagebox("���дڥN�X ���ݩ� [�~���ܰʦ���]�A���ʥN�X ���i���ťաC") ; return false ; }
         */
        if (getValue(".DocNo1").indexOf("333") == -1 && "782".equals(stringCostID + stringCostID1)) {
          getcLabel("SSMediaID").requestFocus();
          messagebox("POP �дڥN�X ���ʥN�X ���i���ťաC");
          return false;
        }
      }
    }
    //
    // ���e
    String stringClassName = getValue("ClassName").trim();
    if (!booleanApplyTypeF && "".equals(stringClassName)) {
      messagebox("[�W��] ���i���ťաC");
      getcLabel("ClassName").requestFocus();
      return false;
    }
    // ���e
    String stringClassNameDescript = getValue("ClassNameDescript").trim();
    if ("".equals(stringClassNameDescript)) {
      messagebox("[���e] ���i���ťաC");
      getcLabel("ClassNameDescript").requestFocus();
      return false;
    }
    if (stringClassName.equals(stringClassNameDescript)) {
      messagebox("[�����u�ƦW��][���e] ���e���i�ۦP�C\n(�����D�Ь� [�]�ȫ�])");
      getcLabel("ClassName").requestFocus();
      return false;
    }
    // �v���ˮ�
    // ��줣�i���ť�
    String stringUnit = getValue("Unit").trim();
    if ("".equals(stringUnit)) {
      messagebox("[���] ���i���ťաC");
      getcLabel("Unit").requestFocus();
      return false;
    }
    // ���ʼƶq
    String stringBudgetNum = getValue("BudgetNum").trim();
    String stringControlType = "" + get("ONLY_CONTROL_AMT");
    if (exeUtil.doParseDouble(stringBudgetNum) <= 0) {
      messagebox("[���ʼƶq] ����p�� 0�C");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    if (booleanUnit && ("," + stringControlType + ",").indexOf("," + stringUnit + ",") != -1 && exeUtil.doParseDouble(stringBudgetNum) != 1) {
      messagebox("[���] �� [" + stringControlType + "] �ɡA[���ʼƶq] �u�ର 1�C");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    if (!check.isFloat(stringBudgetNum, "11,4")) {
      messagebox("[���ʼƶq] �榡���~�A�u���\7 ��ƤΤp���I�� 4 ��C");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    // ���ʪ��B
    String stringApplyMoney = getValue("ApplyMoney").trim();
    if (!"".equals(stringApplyMoney) && exeUtil.doParseDouble(stringApplyMoney) == 0) {
      messagebox("[���ʪ��B] �u��O�Ʀr�C");
      getcLabel("ApplyMoney").requestFocus();
      return false;
    }
    if (exeUtil.doParseDouble(stringApplyMoney) < 0 && ",701,702,".indexOf("," + stringCostID + stringCostID1 + ",") == -1) {
      messagebox("�� �дڥN�X 701���ݤ��߸˿X�]�p�O��(�t�믫���S)�B702�˫~�θ˿X�]�p�O�� ���\�ӽЭt�ȡC");
      return false;
    }
    String stringHistoryPrice = getValue("HistoryPrice").trim();
    if (exeUtil.doParseDouble(stringApplyMoney) == 0 && exeUtil.doParseDouble(stringHistoryPrice) == 0) {
      messagebox("[���ʼƶq][�w����]�A���i�Ҭ� 0�C");
      getcLabel("ApplyMoney").requestFocus();
      return false;
    }
    // �ϥΤ��
    String stringDateStart = getValue("DateStart").trim();
    String stringDateEnd = getValue("DateEnd").trim();
    Vector vectorCostID = getVectorCostID(exeFun);
    if (vectorCostID.indexOf(stringCostID + stringCostID1) != -1 || "49".equals(stringCostID)) {
      // �ϥΤ��(�_)
      stringDateStart = exeUtil.getDateFullRoc(stringDateStart, "�ϥΤ��(�_)");
      if (stringDateStart.length() != 9) {
        messagebox(stringDateStart);
        getcLabel("DateStart").requestFocus();
        return false;
      } else {
        setValue("DateStart", stringDateStart);
      }
      if ("".equals(stringDateEnd)) {
        messagebox("[�ϥΤ��(�W)] ���i���ťաC");
        return false;
      }
      stringDateEnd = exeUtil.getDateFullRoc(stringDateEnd, "�ϥΤ��(�W)");
      if (stringDateEnd.length() != 9) {
        messagebox(stringDateEnd);
        getcLabel("DateEnd").requestFocus();
        return false;
      } else {
        setValue("DateEnd", stringDateEnd);
      }
      if (datetime.subDays1(convert.replace(stringDateEnd, "/", ""), convert.replace(stringDateStart, "/", "")) < 0) {
        messagebox("[�ϥΤ��(�W)] ���� [�ϥΤ��(�_)]�C");
        return false;
      }
    }
    if (vectorCostID.indexOf(stringCostID + stringCostID1) != -1) {
      int intCoun = 0;
      String stringCoun = getValue("Coun").trim();
      String stringCounName = "";
      String stringTown = getValue("Town").trim();
      String[][] retTown = null;
      Vector vectorCoun = new Vector();
      // ����
      if ("".equals(stringCoun)) {
        messagebox("[����] ���i���ťաC");
        getcLabel("Coun").requestFocus();
        return false;
      }
      // �m��
      if ("".equals(stringTown)) {
        messagebox("[�m��] ���i���ťաC");
        getcLabel("Town").requestFocus();
        return false;
      }
      retTown = exeFun.getTown(stringCoun, stringTown, "");
      if (retTown.length == 0) {
        messagebox("[����][�m��] �������~�C");
        return false;
      }
    }
    //
    if (stringFunctionName.indexOf("����") != -1) {
      // ��ڼƶq
      String stringActualNum = getValue("ActualNum").trim();
      if (exeUtil.doParseDouble(stringActualNum) <= 0) {
        messagebox("[��ڼƶq] ����p�� 0�C");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      if (booleanUnit && ("," + stringControlType + ",").indexOf("," + stringUnit + ",") != -1 && exeUtil.doParseDouble(stringActualNum) != 1) {
        messagebox("[���] �� [" + stringControlType + "] �ɡA[��ڼƶq] �u�ର 1�C");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      if (!check.isFloat(stringActualNum, "11,4")) {
        messagebox("[[���ʼƶq] �榡���~�A�u���\ 7 ��ƤΤp���I�� 4 ��C");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      // �o�]���
      String stringActualPrice = getValue("ActualPrice").trim();
      /*
       * if(!"".equals(stringActualPrice) && !exeUtil.isDigitNum(stringActualPrice)) {
       * message("[�o�]���] �u��O�Ʀr�C") ; getcLabel("ActualPrice").requestFocus() ; return
       * false ; }
       */
      // �ĵo���B
      String stringPurchaseMoney = getValue("PurchaseMoney").trim();
      /*
       * if(!"".equals(stringPurchaseMoney) &&
       * !exeUtil.isDigitNum(stringPurchaseMoney)) { message("[�ĵo���B] �u��O�Ʀr�C") ;
       * getcLabel("PurchaseMoney").requestFocus() ; return false ; }
       */
      if (exeUtil.doParseDouble(stringPurchaseMoney) < 0 && ",701,702,".indexOf("," + stringCostID + stringCostID1 + ",") == -1) {
        messagebox("�� �дڥN�X 701���ݤ��߸˿X�]�p�O��(�t�믫���S)�B702�˫~�θ˿X�]�p�O�� ���\�ӽЭt�ȡC");
        return false;
      }
      /*
       * if(exeUtil.doParseDouble(stringActualPrice) == 0 &&
       * exeUtil.doParseDouble(stringActualNum) == 0) {
       * messagebox("[�o�]���][�ĵo���B] ����Ҭ� 0�C") ; return false ; }
       */
    }
    // �t��
    String stringFactoryNo = getValue("FactoryNo").trim();
    if (stringFunctionName.indexOf("����") != -1 || booleanApplyTypeF) {
      if ("".equals(stringFactoryNo)) {
        messagebox("[�t�ӥN�X] ���i���ťաC");
        getcLabel("FactoryNo").requestFocus();
        return false;
      }
    }
    // ���v
    String stringStopUseMessage = "";
    Hashtable hashtableCond = new Hashtable();
    hashtableCond.put("OBJECT_CD", stringFactoryNo);
    hashtableCond.put("CHECK_DATE", "");
    hashtableCond.put("SOURCE", "A");
    hashtableCond.put("FieldName", "[�t�ӥN�X] ");
    stringStopUseMessage = exeFun.getStopUseObjectCDMessage(hashtableCond, exeUtil);
    if (!"TRUE".equals(stringStopUseMessage)) {
      messagebox(stringStopUseMessage);
      getcLabel("FactoryNo").requestFocus();
      return false;
    }
    // ����
    if (booleanApplyTypeF) {
      // ����
      if ("805".equals(stringCostID + stringCostID1)) {
        // �ث~ Doc3M016�BDoc3M017
        if (!isApplyF805OK(stringFunctionName, exeUtil, exeFun)) return false;
      } else {
        // �D�ث~ Doc3M0174
        if (!isApplyFNot805OK(stringFunctionName, exeUtil, exeFun)) return false;
      }
    }
    if (!isTableProjectCheck(stringFunctionName, exeUtil, exeFun)) return false; // �קO���u
    if (!isTable17Check(stringFunctionName, exeUtil, exeFun)) return false; // ĳ����T
    return true;
  }

  public boolean isApplyF805OK(String stringFunctionName, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCDate = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringDateStart = stringCDate;
    String stringDateEnd = stringCDate;
    String stringCostID = getValue("CostID").trim();
    String stringCostID1 = getValue("CostID1").trim();
    String stringRecordNoDoc3M017 = getValue("RecordNoDoc3M017").trim();
    String stringFactoryNo = getValue("FactoryNo").trim();
    String stringFactoryNo17 = "";
    Hashtable hashtableDoc3M016 = null;
    Hashtable hashtableDoc3M017 = null;
    //
    if (stringFunctionName.indexOf("����") != -1) return true;
    //
    hashtableDoc3M016 = getDoc3M016(stringDateStart, stringDateEnd, exeUtil, exeFun);
    if (hashtableDoc3M016 == null) {
      messagebox("�����ؤ��s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)] ���C\n(�����D�Ь� [���ʫ�])");
      return false;
    }
    // �O�_�s�b����ʼt�Ӹ�Ƥ�
    if (exeUtil.doParseDouble(stringRecordNoDoc3M017) > 0) {
      hashtableDoc3M017 = getDoc3M017(stringRecordNoDoc3M017, hashtableDoc3M016, exeUtil, exeFun);
      if (hashtableDoc3M017 == null) {
        messagebox("�����ؤ��s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)] ���C\n(�����D�Ь� [���ʫ�])");
        return false;
      }
      if (!isControlNumOK(stringDateStart, hashtableDoc3M017, exeUtil, exeFun)) return false;
    }
    // �t���ˮ�
    stringFactoryNo17 = "" + hashtableDoc3M017.get("FactoryNo");
    if ("null".equals(stringFactoryNo17)) stringFactoryNo17 = "";
    if (!"".equals(stringFactoryNo17)) {
      if (!stringFactoryNo.equals(stringFactoryNo17)) {
        if (stringFunctionName.indexOf("����") == -1) {
          setValue("FactoryNo", stringFactoryNo17);
        }
      }
    } else {
      stringDateStart = "" + hashtableDoc3M017.get("DateStart");
      stringDateEnd = "" + hashtableDoc3M017.get("DateEnd");
      if (exeFun.getDoc3M0171(stringCostID, stringCostID1, "", stringFactoryNo, stringDateStart, stringDateEnd, "", "").length == 0) {
        if (stringFunctionName.indexOf("����") == -1) {
          messagebox("�� [�t��] ���s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)] ���C\n(�����D�Ь� [���ʫ�])");
          return false;
        }
      }
    }
    return true;
  }

  public boolean isApplyFNot805OK(String stringFunctionName, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCDate = exeUtil.getDateConvert(getValue("CDate").trim());
    String stringDateStart = exeUtil.getDateConvert(getValue("DateStart").trim());
    String stringDateEnd = exeUtil.getDateConvert(getValue("DateEnd").trim());
    String stringCostIDDetail = getValue("CostIDDetail").trim();
    String stringBarCode = getValue(".BarCode").trim();
    if (!stringBarCode.startsWith("S")) return true;
    String stringRecordNoDoc3M017 = getValue("RecordNoDoc3M017").trim();
    String strinDocNo173 = getValue("DocNo173").trim();
    String stringFactoryNo = getValue("FactoryNo").trim();
    String stringUnit = getValue("Unit").trim();
    String stringClassName = getValue("ClassName").trim();
    String stringFactoryNo17 = "";
    String stringSqlAnd = "";
    Hashtable hashtableDoc3M016 = null;
    Hashtable hashtableDoc3M017 = null;
    Hashtable hashtableAnd = new Hashtable();
    Vector vectorDoc3M0174 = new Vector();
    //
    if (stringFunctionName.indexOf("����") != -1) return true;
    //
    if (!stringCostIDDetail.startsWith("74")) {
      stringDateStart = stringCDate;
      stringDateEnd = stringCDate;
    }
    //
    hashtableAnd.put("DocNo", strinDocNo173);
    hashtableAnd.put("CostIDDetail", stringCostIDDetail);
    hashtableAnd.put("RecordNo", stringRecordNoDoc3M017);
    hashtableAnd.put("FactoryNo", stringFactoryNo);
    // hashtableAnd.put("ItemName", stringClassName) ; // �W��
    // hashtableAnd.put("Unit", stringUnit) ; // ���
    stringSqlAnd = " AND  ( DateStart = '9999/99/99'  OR  DateStart <= '" + stringDateStart + "' )" + " AND  ( DateEnd = '9999/99/99'  OR  DateEnd >= '" + stringDateEnd + "' )";
    vectorDoc3M0174 = exeFun.getQueryDataHashtableDoc("Doc3M0174", hashtableAnd, stringSqlAnd, new Vector(), exeUtil);
    if (vectorDoc3M0174.size() == 0) {
      messagebox("�����ؤ��s�b ���s�b [3.���ʼt�ӶO�ι�Ӫ�-��P(Doc3M0163)]�C\n(�����D�Ь� [���ʫ�])");
      return false;
    }
    // �t���ˮ�
    stringFactoryNo17 = exeUtil.getVectorFieldValue(vectorDoc3M0174, 0, "FactoryNo");
    if (!"".equals(stringFactoryNo17)) {
      if (!stringFactoryNo.equals(stringFactoryNo17)) {
        if (stringFunctionName.indexOf("����") == -1) {
          setValue("FactoryNo", stringFactoryNo17);
        }
      }
    }
    return true;
  }

  public boolean isControlNumOK(String stringDateStart, Hashtable hashtableDoc3M017, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringControlNum = "" + hashtableDoc3M017.get("ControlNum");
    double doubleControlNum = exeUtil.doParseDouble(stringControlNum);
    double doubleUseNum = getNumDoc3M012(hashtableDoc3M017, exeUtil, exeFun);
    //
    if (doubleControlNum <= 0) return true;
    //
    if (doubleUseNum > doubleControlNum) {
      messagebox("���ʺޱ��ƶq �� " + exeUtil.getFormatNum2("" + doubleControlNum) + "�A�{�w�ӽ�(�t����) " + exeUtil.getFormatNum2("" + doubleUseNum) + "�C ");
      return false;
    }
    return true;
  }

  public double getNumDoc3M012(Hashtable hashtableDoc3M017, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCostID = "" + hashtableDoc3M017.get("CostID");
    String stringCostID1 = "" + hashtableDoc3M017.get("CostID1");
    String stringCostID2 = "" + hashtableDoc3M017.get("CostID2");
    if (!"XX".equals(stringCostID2)) return -1;
    String stringDateStart = "" + hashtableDoc3M017.get("DateStart");
    String stringDateEnd = "" + hashtableDoc3M017.get("DateEnd");
    String stringBarCode = getValue(".BarCode").trim();
    String stringRecordNoDoc3M017 = getValue("RecordNoDoc3M017").trim();
    String stringSql = "";
    String stringUseNum = "";
    String[][] retDoc3M012 = null;
    double doubleUseNum = 0;
    //
    stringSql = " SELECT  M12.ActualNum " + " FROM  Doc3M011 M11,  Doc3M012 M12 " + " WHERE  M11.BarCode  =  M12.BarCode " + " AND  M11.UNDERGO_WRITE  <>  'X' "
        + " AND  M11.BarCode  <>  '" + stringBarCode + "' " + " AND  M11.CDate  BETWEEN  '" + stringDateStart + "'  AND  '" + stringDateEnd + "' " + " AND  M12.CostID  =  '"
        + stringCostID + "' " + " AND  M12.CostID1  =  '" + stringCostID1 + "' " + " AND  M12.RecordNoDoc3M017  =  " + stringRecordNoDoc3M017 + " ";
    retDoc3M012 = exeFun.getTableDataDoc(stringSql);
    for (int intNo = 0; intNo < retDoc3M012.length; intNo++) {
      stringUseNum = retDoc3M012[intNo][0].trim();
      //
      doubleUseNum += exeUtil.doParseDouble(stringUseNum);
    }
    talk dbDocCS = exeUtil.getTalkCS("Doc");
    if (dbDocCS != null) {
      retDoc3M012 = dbDocCS.queryFromPool(stringSql);
      for (int intNo = 0; intNo < retDoc3M012.length; intNo++) {
        stringUseNum = retDoc3M012[intNo][0].trim();
        //
        doubleUseNum += exeUtil.doParseDouble(stringUseNum);
      }
    }
    doubleUseNum += exeUtil.doParseDouble(getValue("ActualNum"));
    return doubleUseNum;
  }

  public Hashtable getDoc3M016(String stringDateStart, String stringDateEnd, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCostID = getValue("CostID").trim();
    String stringCostID1 = getValue("CostID1").trim();
    String stringCostID2 = getValue("CostID2").trim();
    String stringSqlAnd = "";
    Hashtable hashtableAnd = new Hashtable();
    Vector vectorDoc3M016 = null;
    //
    stringSqlAnd = " AND  ( (DateStart  >=  '" + stringDateStart + "'  AND  DateStart  <=  '" + stringDateEnd + "')  OR " + " (DateEnd   >=  '" + stringDateStart
        + "'  AND  DateEnd   <=  '" + stringDateEnd + "')  OR " + " (DateStart  <=  '" + stringDateStart + "'  AND  DateEnd   >=  '" + stringDateEnd + "')  OR "
        + " (DateStart  >=  '" + stringDateStart + "'  AND  DateEnd   <=  '" + stringDateEnd + "')) ";
    if (!"".equals(stringCostID)) hashtableAnd.put("CostID", stringCostID);
    if (!"".equals(stringCostID1)) hashtableAnd.put("CostID1", stringCostID1);
    // if(!"".equals(stringCostID2)) hashtableAnd.put("CostID2", stringCostID2) ;
    vectorDoc3M016 = exeFun.getQueryDataHashtableDoc("Doc3M016", hashtableAnd, stringSqlAnd, new Vector(), exeUtil);
    //
    if (vectorDoc3M016.size() <= 0) return null;
    if (vectorDoc3M016.size() > 1) return null;
    return (Hashtable) vectorDoc3M016.get(0);
  }

  public Hashtable getDoc3M017(String stringRecordNo, Hashtable hashtableDoc3M016, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringCostID = "" + hashtableDoc3M016.get("CostID");
    String stringCostID1 = "" + hashtableDoc3M016.get("CostID1");
    String stringCostID2 = "" + hashtableDoc3M016.get("CostID2");
    String stringDateStart = "" + hashtableDoc3M016.get("DateStart");
    String stringDateEnd = "" + hashtableDoc3M016.get("DateEnd");
    String stringSql = "";
    Vector vectorDoc3M017 = null;
    Hashtable hashtableAnd = new Hashtable();
    // ����
    hashtableAnd.put("CostID", stringCostID);
    hashtableAnd.put("CostID1", stringCostID1);
    hashtableAnd.put("CostID2", stringCostID2);
    hashtableAnd.put("DateStart", stringDateStart);
    hashtableAnd.put("DateEnd", stringDateEnd);
    hashtableAnd.put("RecordNo", stringRecordNo);
    vectorDoc3M017 = exeFun.getQueryDataHashtableDoc("Doc3M017", hashtableAnd, "", new Vector(), exeUtil);
    if (vectorDoc3M017.size() != 1) return null;
    return (Hashtable) vectorDoc3M017.get(0);
  }

  public boolean isERPKeyExist(String stringERPKey, FargloryUtil exeUtil, talk dbAO) throws Throwable {
    Vector vectorViewAOSeminar = null;
    Hashtable hashtableAnd = new Hashtable();
    //
    hashtableAnd.put("ERP_Key", stringERPKey);
    hashtableAnd.put("set_flag", "�w��q�L");
    vectorViewAOSeminar = exeUtil.getQueryDataHashtable("View_AO_Seminar", hashtableAnd, "", dbAO);
    //
    if (vectorViewAOSeminar.size() == 0) {
      messagebox("[�q���N�X] ���s�b��Ʈw���C");
      return false;
    }
    return true;
  }

  public String getBigBudget(String stringCostID, String stringCostID1, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringBigBudget = "";
    String stringBudgetID = "";
    String stringComNo = getValue(".ComNo");
    Hashtable hashtableAnd = new Hashtable();
    //
    hashtableAnd.put("ComNo", stringComNo);
    hashtableAnd.put("CostID", stringCostID);
    hashtableAnd.put("CostID1", stringCostID1);
    stringBudgetID = exeFun.getNameUnionDoc("BudgetID", "Doc2M020", "", hashtableAnd, exeUtil);
    if (!stringBudgetID.startsWith("B")) return "A";
    //
    hashtableAnd.put("BudgetID", stringBudgetID);
    stringBigBudget = exeFun.getNameUnionDoc("BigBudget", "Doc7M072", "", hashtableAnd, exeUtil);
    //
    return stringBigBudget;
  }

  public String getSSMediaIDDoc(String stringCostID, String stringCostID1, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringSSMediaID1 = "";
    Hashtable hashtableAnd = new Hashtable();
    //
    hashtableAnd.put("CostID", stringCostID);
    hashtableAnd.put("CostID1", stringCostID1);
    hashtableAnd.put("UseType", "A");
    stringSSMediaID1 = exeFun.getNameUnionDoc("SSMediaID", "Doc7M070", "", hashtableAnd, exeUtil);
    if ("".equals(stringSSMediaID1)) return "";
    //
    stringSSMediaID1 = exeUtil.doSubstring(stringSSMediaID1, 0, 1);
    return stringSSMediaID1;
  }

  public boolean isTableProjectCheck(String stringFunctionName, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    JTable jtableProject = getTable("TableProject");
    JTabbedPane jtabbedpane1 = getTabbedPane("Tab1");
    String stringCDate = getValue(".NeedDate").trim();
    String stringCDateAC = exeUtil.getDateConvert(stringCDate);
    String stringCostID = getValue("CostID").trim();
    String stringCostID1 = getValue("CostID1").trim();
    String stringComNo = getValue(".ComNo").trim();
    String stringBudgetID = "";
    String stringExistDeptCd = exeFun.getVFEE(stringComNo, stringCDate, exeUtil);
    String[] arraySpecBudget = convert.StringToken("" + get("SPEC_BUDGET"), ",");
    String[][] retDoc7M011 = null;
    Vector vectorProjectID1NoUseBudget = new Vector();
    Vector vectorProjectID1 = exeFun.getDoc2M051();
    Vector vectorDeptCd = new Vector(); // ���~����
    Vector vectorDeptCd2 = new Vector(); // ���~����
    boolean booleanActionNo = !"".equals(getValue(".ActionNo"));
    //
    String stringNeedDate = getValue(".NeedDate").trim();
    String stringSqlAndL = "";
    if ("".equals(stringNeedDate)) {
      stringNeedDate = datetime.getToday("YYYY/mm/dd");
    } else {
      stringNeedDate = exeUtil.getDateConvert(stringNeedDate);
    }
    stringSqlAndL = " AND  DateStart  <=  '" + stringNeedDate + "'  AND  DateEnd  >=  '" + stringNeedDate + "' ";
    vectorProjectID1 = exeFun.getDoc2M051(stringSqlAndL);
    //
    vectorProjectID1NoUseBudget.add("0331---F1---F1---A"); //
    vectorProjectID1NoUseBudget.add("0331---F1---F1---B"); //
    // ���~�~�A�B�� 0531 �ɡA�p�קO�u�ର H35�BH36A�BH39�BM51A
    // vectorProjectID1.add("H35") ;
    // vectorProjectID1.add("H36A") ;
    // vectorProjectID1.add("H39") ;
    // vectorProjectID1.add("M51A") ;
    // vectorProjectID1.add("H63A") ;
    // ���~����
    vectorDeptCd.add("03335");
    vectorDeptCd.add("033622");
    vectorDeptCd.add("03363");
    vectorDeptCd.add("03365");
    vectorDeptCd.add("0333");
    for (int intNo = 0; intNo < arraySpecBudget.length; intNo++)
      vectorDeptCd.add(arraySpecBudget[intNo]);
    vectorDeptCd.add("03396");
    // ���@���~�~�ˮ�
    vectorDeptCd2.add("03395"); // 2014-01-28 �̤����w �s�W
    if (jtableProject.getRowCount() <= 0) {
      jtabbedpane1.setSelectedIndex(0);
      messagebox("�п�J [���u�קO] ���C");
      return false;
    }
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
    if (retDoc7M011.length > 0) stringBudgetID = retDoc7M011[0][0].trim();
    //
    String stringApplyMoney = "";
    String stringBarCode = getValue(".BarCode");
    String stringDocNoF = getValue(".DocNo1");
    String stringDepart1 = "";
    String stringDepart2 = "";
    String stringDepartNo = "";
    String stringDepartMessage = "";
    String stringInOut = "";
    String stringKey = "";
    String stringProjectID = "";
    String stringProjectID1 = "";
    String stringProjectID1Use = "";
    String stringPurchaseMoney = "";
    String stringProjectID1BTType = "";
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    String[][] retDoc7M0265 = null;
    String[][] retDoc7M020 = null;
    double doubleApplyMoney = 0;
    double doubleApplyMoneyCF = 0;
    Vector vectorKey = new Vector();
    boolean booleanFlag = true;
    Hashtable hashtableAProject = exeFun.getAProject();
    //
    double doubleMoneyCF = 0;
    if (stringFunctionName.indexOf("����") != -1) {
      doubleMoneyCF = exeUtil.doParseDouble(getValue("PurchaseMoney"));
    } else {
      doubleMoneyCF = exeUtil.doParseDouble(getValue("ApplyMoney"));
    }
    for (int intNo = 0; intNo < jtableProject.getRowCount(); intNo++) {
      stringInOut = ("" + getValueAt("TableProject", intNo, "InOut")).trim();
      stringDepartNo = ("" + getValueAt("TableProject", intNo, "DepartNo")).trim();
      stringProjectID = ("" + getValueAt("TableProject", intNo, "ProjectID")).trim();
      stringProjectID1 = ("" + getValueAt("TableProject", intNo, "ProjectID1")).trim();
      stringApplyMoney = ("" + getValueAt("TableProject", intNo, "ApplyMoney")).trim();
      stringPurchaseMoney = ("" + getValueAt("TableProject", intNo, "PurchaseMoney")).trim();
      //
      if ("0531".equals(stringDepartNo)) {
        stringProjectID1Use = exeFun.get053ProjectID1Doc2M051(stringProjectID1);
      } else {
        stringProjectID1Use = stringProjectID1;
      }
      // BT ����
      retDoc7M0265 = exeFun.getTableDataDoc("SELECT  AreaNum,  DateStart,  DateEnd,  ProjectIDMajor " + " FROM  Doc7M0265 " + " WHERE  ComNo  =  '" + stringComNo + "' "
          + " AND  ProjectID1  =  '" + stringProjectID1 + "' ");
      if (retDoc7M0265.length > 0) {
        String stringNeedDateL = exeUtil.getDateConvert(stringNeedDate);
        if (stringNeedDateL.compareTo(retDoc7M0265[0][1].trim()) < 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�� " + (intNo + 1) + " �C [�קO](" + stringProjectID1 + ") �|�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
          setFocus("TableProject", intNo, "ProjectID");
          if (!getUser().startsWith("b")) return false;
        }
        if (stringNeedDateL.compareTo(retDoc7M0265[0][2].trim()) > 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�� " + (intNo + 1) + " �C [�קO](" + stringProjectID1 + ") �w�����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])");
          setFocus("TableProject", intNo, "ProjectID");
          return false;
        }
        // �дڥN�X����
        /*
         * if("BT".equals(retDoc7M0265[0][3].trim()) &&
         * "701,710,".indexOf(stringCostID+stringCostID1) != -1) {
         * messagebox("[BT �קO] �����\�ϥ� [701 ���ݤ��߸˿X�]�p�O��]��[710 �����u�{]�C\n(�����D�Ь� [�]�ȫ�])") ;
         * return false ; }
         */
        /*
         * if("�s��".equals(retDoc7M0265[0][3].trim()) &&
         * "701,702,710,".indexOf(stringCostID+stringCostID1) != -1) {
         * messagebox("[�s�� �קO] �����\�ϥ� [701][702]��[710]�C\n(�����D�Ь� [��P�޲z��])") ; return false
         * ; }
         */
      }
      /*
       * if(retDoc7M0265.length > 0 && "BT".equals(retDoc7M0265[0][3].trim())) {
       * if("".equals(stringProjectID1) || !stringBudgetID.startsWith("B") ||
       * "".equals(stringBudgetID)) { if(!booleanActionNo &&
       * "Y".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; }
       * stringProjectID1BTType = "N" ; // �D BT�קO } else { if(retDoc7M0265.length ==
       * 0) { if(!booleanActionNo && "Y".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; }
       * stringProjectID1BTType = "N" ; // �D BT�קO } else { if(!booleanActionNo &&
       * "N".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT �קO] ���i�P �D [BT �קO] �@�_�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; } //
       * if(!booleanActionNo) { if(stringFunctionName.indexOf("����") != -1) {
       * setValueAt("TableProject", "", intNo, "ApplyPercent") ;
       * setValueAt("TableProject", retDoc7M0265[0][0], intNo, "ApplyMoney") ;
       * stringPurchaseMoney = retDoc7M0265[0][0] ; } else {
       * setValueAt("TableProject", "", intNo, "PurchasePercent") ;
       * setValueAt("TableProject", retDoc7M0265[0][0], intNo, "PurchaseMoney") ;
       * stringApplyMoney = retDoc7M0265[0][0] ; } } stringProjectID1BTType = "Y" ; }
       * } }
       */
      //
      stringKey = stringInOut + "-" + stringDepartNo + "-" + stringProjectID + "-" + stringProjectID1;
      if (vectorKey.indexOf(stringKey) != -1) {
        jtableProject.setRowSelectionInterval(intNo, intNo);
        jtabbedpane1.setSelectedIndex(0);
        messagebox("�� " + (intNo + 1) + " �C��ƭ��ơC");
        return false;
      }
      vectorKey.add(stringKey);
      if (stringDocNoF.indexOf("033FZ") != -1 && !"0333".equals(stringDepartNo)) {
        jtabbedpane1.setSelectedIndex(0);
        setFocus("TableProject", intNo, "DepartNo");
        messagebox("�� " + (intNo + 1) + " �C �� ����s�� 033FZ �u��� ���~ 0333 �@���u �C\n(�����D�Ь� [�H�`��])");
        return false;
      }
      // ���~�~
      if ("".equals(stringInOut)) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("�� " + (intNo + 1) + " �C[���~�~] ���o���ťաC");
        setFocus("TableProject", intNo, "InOut");
        return false;
      }
      // ����
      if ("".equals(stringDepartNo.trim())) {
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("�� " + (intNo + 1) + " �C[����] ���o���ťաC");
        setFocus("TableProject", intNo, "DepartNo");
        return false;
      }
      if ("".equals(exeFun.getDepartName(stringDepartNo))) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("[����] ���s�b��Ʈw���C\n(�����D�Ь� [��T��])");
        setFocus("TableProject", intNo, "DepartNo");
        return false;
      }
      // System.out.println("------------------�O�ΥN�X�P���~�~�@�P�ˮ�") ;
      if (vectorDeptCd2.indexOf(stringDepartNo) == -1 && vectorDeptCd.indexOf(stringDepartNo) == -1 && !exeFun.isInOutToCostID(stringInOut, stringCostID)) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("[�дڥN�X-" + stringCostID + "] �P [��/�~�~-" + stringInOut + "] ���@�P�C\n(�����D�Ь� [��P�޲z��])");
        setFocus("TableProject", intNo, "InOut");
        return false;
      }
      // �קO
      if (stringProjectID.startsWith("053") || stringProjectID1.startsWith("053")) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("�� " + (intNo + 1) + " �C �� [�קO] ���~ �C");
        setFocus("TableProject", intNo, "ProjectID1");
        return false;
      }
      if ("O".equals(stringInOut)) {
        // �קO
        if ("".equals(stringProjectID)) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("[�j�קO] ���o���ťաC");
          setFocus("TableProject", intNo, "ProjectID");
          return false;
        }
        if ("".equals(stringProjectID1)) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�� " + (intNo + 1) + " �C �~�~�ɡA[�קO] ���o���ťաC");
          setFocus("TableProject", intNo, "ProjectID1");
          return false;
        }
        if (",0331,1331,0531,".indexOf(stringDepartNo) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�~�~�ɡA[����] �u�ର [0331] �B [1331]�B[0531]�C");
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        if ("0531".equals(stringDepartNo) && vectorProjectID1.indexOf(stringProjectID1) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          //
          String stringTemp = "";
          for (int intNoL = 0; intNoL < vectorProjectID1.size(); intNoL++) {
            if (!"".equals(stringTemp)) stringTemp += "�B";
            stringTemp += "[" + ("" + vectorProjectID1.get(intNoL)).trim() + "]";
          }
          messagebox("�~�~�B������ 0531 �ɡA[�קO] �u�ର" + stringTemp + "�C");
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        // �s�b�ˬd
        if (!"0531".equals(stringDepartNo) && !exeFun.isExistProjectIDCheck(stringProjectID, stringProjectID1)) {
          jtableProject.setRowSelectionInterval(intNo, intNo);
          jtabbedpane1.setSelectedIndex(0);
          setFocus("TableProject", intNo, "ProjectID");
          messagebox("[�j�קO] [�p�קO] ���s�b���Ʈw���C\n(�����D�Ь� [�����޲z��])");
          return false;
        }
        // �{���� hashtableAProject �o�� �� Depart �� 8 �ɡA�������A�p�����D�A�����ϥΪ�
        stringDepart1 = "" + hashtableAProject.get(stringProjectID);
        stringDepart2 = "" + hashtableAProject.get(stringProjectID1);
        if (!"0531".equals(stringDepartNo)) {
          if ("8".equals(stringDepart2) || "8".equals(stringDepart1)) {
            // ����
            if (!"1331".equals(stringDepartNo.trim())) {
              stringDepartMessage = "�� " + (intNo + 1) + " �C���קO���� 1331�A�����\���ʸ�Ʈw�C";
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              setFocus("TableProject", intNo, "ProjectID");
              messagebox(stringDepartMessage);
              return false;
            }
          } else if ("1331".equals(stringDepartNo.trim())) {
            stringDepartMessage = "�� " + (intNo + 1) + " �C���קO������ 1331�A�����\���ʸ�Ʈw�C";
            jtabbedpane1.setSelectedIndex(0);
            jtableProject.setRowSelectionInterval(intNo, intNo);
            setFocus("TableProject", intNo, "ProjectID");
            messagebox(stringDepartMessage);
            return false;
          }
        }
      } else {
        if (!"".equals(stringProjectID1)) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�� " + (intNo + 1) + " �C ���~�ɡA[�קO] ���i���ȡC");
          jtableProject.setRowSelectionInterval(intNo, intNo);
          return false;
        }
        if ("033MP".equals(stringDepartNo.trim())) {
          stringDepartMessage = "�� " + (intNo + 1) + " �C [033MP] �ݩ�~�~�A�����\���ʸ�Ʈw�C";
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox(stringDepartMessage);
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        if (stringSpecBudget.indexOf("," + stringDepartNo + ",") == -1 && !exeUtil.isDigitNum(stringDepartNo.trim())) {
          jtabbedpane1.setSelectedIndex(0);
          stringDepartMessage = "���u�קO�� " + (intNo + 1) + " �C ���~����(" + stringDepartNo.trim() + ")�榡���~�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [��T������])";
          setFocus("TableProject", intNo, "DepartNo");
          messagebox(stringDepartMessage);
          return false;
        }
        if (stringSpecBudget.indexOf("," + stringDepartNo + ",") == -1 && !"".equals(stringExistDeptCd)
            && stringExistDeptCd.indexOf(exeUtil.doSubstring(stringDepartNo, 0, 3)) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          stringDepartMessage = "�� " + (intNo + 1) + " �C ���~����(" + stringDepartNo.trim() + ")���s�b��Ӥ��q�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])";
          messagebox(stringDepartMessage);
          setFocus("TableProject", intNo, "DepartNo");
          if (!"B3018".equals(getUser())) return false;
        }
      }

      // ���B
      if (stringFunctionName.indexOf("����") != -1) {
        /*
         * if(exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
         * jtabbedpane1.setSelectedIndex(0) ;
         * jtableProject.setRowSelectionInterval(intNo, intNo) ;
         * messagebox("�� "+(intNo+1)+" �C [�ĵo���B] ���i���� 0�C") ; setFocus("TableProject",
         * intNo, "PurchaseMoney") ; if(!"B3018".equals(getUser()))return false ; }
         */
        doubleApplyMoney += exeFun.doParseDouble(stringPurchaseMoney);
      } else {
        if (exeUtil.doParseDouble(stringApplyMoney) == 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("�� " + (intNo + 1) + " �C [���B] ���i���� 0�C");
          setFocus("TableProject", intNo, "ApplyMoney");
          return false;
        }
        doubleApplyMoney += exeFun.doParseDouble(stringApplyMoney);
      }

      // �׹w���ˬd
      booleanFlag = !"001".equals(stringCostID + stringCostID1) && "O".equals(stringInOut) && stringFunctionName.indexOf("����") == -1 && !"".equals(stringBudgetID)
          && vectorProjectID1NoUseBudget.indexOf(stringDepartNo + "---" + stringProjectID + "---" + stringProjectID1 + "---" + stringBudgetID.substring(0, 1)) == -1; // �S��קO�A���@�w���ˮ�
      /*
       * if(!booleanFlag && vectorDeptCd.indexOf(stringDepartNo)==-1 &&
       * ((stringBudgetID.length()>0) && "B".equals(stringBudgetID.substring(0,1)))) {
       * stringProjectID1Use = stringDepartNo ; booleanFlag = true ; }
       */
      if (booleanFlag && !(stringProjectID1Use.startsWith("053") && "B".equals(stringBudgetID.substring(0, 1)))) {
        retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "U");
        if (retDoc7M020.length == 0) {
          String stringTemp2 = "A".equals(stringBudgetID.substring(0, 1)) ? "\n(�����D�Ь� [��P�޲z��])" : "\n(�����D�Ь� [��P������])";
          // �׫e�w��
          retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "F");
          if (retDoc7M020.length == 0) {
            retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "R");
          }
          if (retDoc7M020.length == 0) {
            String stringTemp = "A".equals(stringBudgetID.substring(0, 1)) ? "��F" : "����";
            jtabbedpane1.setSelectedIndex(0);
            jtableProject.setRowSelectionInterval(intNo, intNo);
            messagebox("�� " + (intNo + 1) + " �C �� [�קO](" + stringProjectID1Use + ") " + stringTemp + "�S���s�C�w��C" + stringTemp2);
            setFocus("TableProject", intNo, "ProjectID");
            return false;
          } else {
            // ���������w��y�{�A���wñ�֦� [��t�D��] �B [CDate] �b [���}��] ���e�A���\
            /*
             * if("A".equals(retDoc7M020[0][8].trim()) ||
             * "P".equals(retDoc7M020[0][8].trim())) { jtabbedpane1.setSelectedIndex(0) ;
             * jtableProject.setRowSelectionInterval(intNo, intNo) ; messagebox( "�� " +
             * (intNo+1) + " �C �� [�׹w�ⱱ��(Doc7M020)] ������� [��t�D��]�A������� [�׫e�w��]�C") ; return false
             * ; }
             */
            if ("".equals(retDoc7M020[0][4].trim())) {
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              messagebox("�� " + (intNo + 1) + " �C �� [�׹w�ⱱ��(Doc7M020)] �� [���}��] ���ťաA������� [�׫e�w��]�C" + stringTemp2);
              setFocus("TableProject", intNo, "ProjectID");
              return false;
            }
            if (exeUtil.getDateConvert(getValue(".NeedDate")).compareTo(retDoc7M020[0][4].trim()) > 0) {
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              messagebox("�� " + (intNo + 1) + " �C �� [�ݨD���] ��� [�׹w�ⱱ��(Doc7M020)] �� [���}��] ��A������� [�׫e�w��]�C" + stringTemp2);
              setFocus("TableProject", intNo, "ProjectID");
              return false;
            }
          }
        }
      }
    }
    if ("Y".equals(stringProjectID1BTType)) getButton("button2").doClick();
    //
    String stringTemp = "";
    if (stringFunctionName.indexOf("����") != -1) {
      doubleApplyMoneyCF = exeFun.doParseDouble(getValue("PurchaseMoney").trim());
      stringTemp = "�ĵo���B";
    } else {
      doubleApplyMoneyCF = exeFun.doParseDouble(getValue("ApplyMoney").trim());
      stringTemp = "�w����B";
    }
    if (!"Y".equals(stringProjectID1BTType)) {
      doubleApplyMoney = exeFun.doParseDouble(convert.FourToFive("" + doubleApplyMoney, 0));
      if (doubleApplyMoneyCF != doubleApplyMoney && "".equals(getValue(".ActionNo").trim())) {
        jtabbedpane1.setSelectedIndex(0);
        messagebox("[��椧" + stringTemp + "] �X�p������ [" + stringTemp + "]�C");
        return false;
      }
    }
    //
    return true;
  }

  public boolean isTable17Check(String stringFunctionName, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    if (stringFunctionName.indexOf("����") == -1) return true;
    //
    JTable jtable17 = getTable("Table17");
    JTabbedPane jtabbedpane1 = getTabbedPane("Tab1");
    String stringFactoryNo = "";
    String stringFactoryNoCheck = getValue("FactoryNo").trim();
    String stringFactoryName = "";
    String stringActualNum = getValue("ActualNum").trim();
    if (exeUtil.doParseDouble(stringActualNum) <= 0) stringActualNum = "1";
    String stringActualPrice = "";
    String stringPurchaseMoney = "";
    String stringPurchaseMoneyCheck = getValue("PurchaseMoney").trim();
    stringPurchaseMoneyCheck = convert.FourToFive(stringPurchaseMoneyCheck, 0);
    Vector vectorFactoryNo = new Vector();
    boolean booleanExistOK = false;
    for (int intNo = 0; intNo < jtable17.getRowCount(); intNo++) {
      stringFactoryNo = ("" + getValueAt("Table17", intNo, "FactoryNo")).trim();
      stringActualPrice = ("" + getValueAt("Table17", intNo, "ActualPrice")).trim();
      stringPurchaseMoney = ("" + getValueAt("Table17", intNo, "PurchaseMoney")).trim();
      // �t�Ӥ��i�ť�
      if ("".equals(stringFactoryNo)) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("ĳ����椧�� " + (intNo + 1) + " �C [ĳ���t��]���i���ťաC");
        return false;
      }
      // �t�Ӹ�Ʈw�ˮ�
      stringFactoryName = exeFun.getNameUnionDoc("OBJECT_FULL_NAME", "Doc3M015", " AND  OBJECT_CD  =  '" + stringFactoryNo + "' ", new Hashtable(), exeUtil);
      if ("".equals(stringFactoryName)) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("ĳ����椧�� " + (intNo + 1) + " �C [ĳ���t��]���s�b��Ʈw���C");
        return false;
      }
      // ����Ϊ��B ���i�Ҭ��ť�
      if (exeUtil.doParseDouble(stringActualPrice) == 0 && exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("ĳ����椧�� " + (intNo + 1) + " �C [���][���B] ���i�Ҭ��ťաC");
        return false;
      } else if (exeUtil.doParseDouble(stringActualPrice) == 0) {
        // ����ťճB�z
        stringActualPrice = "" + (exeUtil.doParseDouble(stringPurchaseMoney) / exeUtil.doParseDouble(stringActualNum));
        stringActualPrice = convert.FourToFive(stringActualPrice, 4);
        setValueAt("Table17", stringActualPrice, intNo, "ActualPrice");
      } else if (exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
        // ���B�ťճB�z
        stringPurchaseMoney = "" + (exeUtil.doParseDouble(stringActualPrice) * exeUtil.doParseDouble(stringActualNum));
        stringPurchaseMoney = convert.FourToFive(stringPurchaseMoney, 0);
        setValueAt("Table17", stringPurchaseMoney, intNo, "PurchaseMoney");
      }
      if (stringFactoryNo.equals(stringFactoryNoCheck)) {
        stringPurchaseMoney = convert.FourToFive(stringPurchaseMoney, 0);
        if (exeUtil.doParseDouble(stringPurchaseMoney) == exeUtil.doParseDouble(stringPurchaseMoneyCheck)) {
          booleanExistOK = true;
        }
      }
    }
    //
    if (!booleanExistOK) {
//                jtabbedpane1.setSelectedIndex(1) ;
//                messagebox("[�֨M�t��]��[�֨M���B] ���s�b ĳ����椤�C") ;
//                return  false ;
    }
    return true;
  }

  public Vector getVectorCostID(Doc2M010 exeFun) throws Throwable {
    String stringComNo = getValue(".ComNo").trim();
    String[][] retDoc7M011 = null;
    Vector vectorCostID = new Vector();
    //
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "74", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "75", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "77", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    vectorCostID.add("781");
    vectorCostID.add("782");
    vectorCostID.add("785");
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "80", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "86", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", "89", "");
    for (int intNo = 0; intNo < retDoc7M011.length; intNo++)
      vectorCostID.add(retDoc7M011[intNo][1].trim() + retDoc7M011[intNo][2].trim());
    return vectorCostID;
  }

  public boolean isUnionPurchase(FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    String stringFunction = getValue(".Function").trim();
    String stringCDate = getValue(".CDate").trim();
    String stringApplyType = getValue(".ApplyType").trim();
    String stringCostIDDetail = getValue("CostIDDetail").trim();
    String stringSqlAnd = "";
    String[][] retDoc3M017 = null;
    boolean booleanFlag = false;
    //
    if (stringFunction.indexOf("����") != -1) return true;
    if ("F".equals(stringApplyType)) return true;
    if (stringCostIDDetail.startsWith("74")) return true;
    //
    stringCDate = exeUtil.getDateConvert(stringCDate);
    //
    if (stringCostIDDetail.startsWith("8005")) {
      stringSqlAnd = " SELECT  CostIDDetail " + " FROM  Doc3M017 " + " WHERE  CostIDDetail  =  '" + stringCostIDDetail + "' " + " AND  DateStart  <=  '" + stringCDate + "' "
          + " AND  DateEnd  >=  '" + stringCDate + "' ";
      retDoc3M017 = exeFun.getTableDataDoc(stringSqlAnd);
      if (retDoc3M017.length > 0) booleanFlag = true;
    } else {
      stringSqlAnd = " SELECT  CostIDDetail " + " FROM  Doc3M0174 " + " WHERE  CostIDDetail  =  '" + stringCostIDDetail + "' " + " AND  DateStart  <=  '" + stringCDate + "' "
          + " AND  DateEnd  >=  '" + stringCDate + "' ";
      retDoc3M017 = exeFun.getTableDataDoc(stringSqlAnd);
      if (retDoc3M017.length > 0) booleanFlag = true;
    }
    if (booleanFlag) {
      int ans = JOptionPane.showConfirmDialog(null, "�нT�{�ӽж��جO�_�����ʡA�D���ʶ��ؽЫ� [�O]�A�_�h�Ы� [�_] ?", "�T��", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
      if (ans == JOptionPane.NO_OPTION) {
        return false;
      }
    }
    return true;
  }

  public String getInformation() {
    return "---------------ButtonUpdate(\u7c3d\u6838).defaultValue()----------------";
  }
}
