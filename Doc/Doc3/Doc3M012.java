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
    // System.out.println("下一個----S") ;
    talk dbAO = getTalk("" + get("put_AO"));
    Doc2M010 exeFun = new Doc2M010();
    FargloryUtil exeUtil = new FargloryUtil();
    String stringFunctionName = getValue(".Function");
    Vector vectorTableDataS = (Vector) get("Doc3M011_Table_TABLEDATA_VECTOR");
    int intPos = exeUtil.doParseInteger("" + get("Doc3M011_Table_POSITION"));
    //
    if (stringFunctionName.indexOf("採購") != -1) getButton("button2").doClick();
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
    String stringApplyType = getValue(".ApplyType"); // 統購為 F
    String stringCheckAdd = getValue(".CheckAdd"); // 其它為 F
    String stringCheckAddDescript = getValue(".CheckAddDescript");
    String stringDateStart = getValue("DateStart").trim();
    String stringDateEnd = getValue("DateEnd").trim();
    String stringCostID = getValue("CostID").trim();
    String stringCostID1 = getValue("CostID1").trim();
    String stringCostID2 = getValue("CostID2").trim();
    String stringCostIDDetail = getValue("CostIDDetail").trim();
    String stringDocNo173 = getValue("DocNo173").trim();
    boolean booleanApplyType = "F".equals(stringApplyType); // true 為統購
    //
    if (!"F".equals(stringApplyType)) {
      // 非統購
      if ("805".equals(stringCostID + stringCostID1)) {
        if (!"F".equals(stringCheckAdd)) {
          setValue(".CheckAdd", "F");
          setValue(".CheckAddDescript", "簽呈編號：");
        }
      }
    }
    //
    stringDateStart = exeUtil.getDateConvert(stringDateStart);
    stringDateEnd = exeUtil.getDateConvert(stringDateEnd);
    //
    if (stringFunctionName.indexOf("採購") == -1) {
      setValue("PurchaseMoney", stringApplyMoney);
      setValue("PurchaseMoneyCompute", stringApplyMoney);
      setValue("ActualPrice", stringHistoryPrice);
      setValue("ActualNum", stringBudgetNum);
    }
    if (booleanApplyType && stringFunctionName.indexOf("採購") != -1) {
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
    // 案別分攤表格
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
      if (stringFunctionName.indexOf("採購") == -1) {
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

    // POP 表格
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

    // 議價資訊表格
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

  // 檢核
  // 前端資料檢核，正確回傳 True
  public boolean isBatchCheckOK(int intPos, String stringFunctionName, Vector vectorTableDataS, Doc2M010 exeFun, FargloryUtil exeUtil, talk dbAO) throws Throwable {
    String stringApplyType = getValue(".ApplyType").trim();// 統購為 F，固定資產 D
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
    // 固定資產 代碼
    String stringFILTER = getValue("FILTER").trim();
    String stringTemp = "";
    String stringInOutL = "";
    String stringFILTER_DO = "";
    String stringDepartNo = "";
    String stringSpecBudget = "," + get("SPEC_BUDGET") + ",";
    boolean booleanCostIDDetail = exeUtil.getDateConvert(getValue("CDate")).compareTo("" + get("NO_PAGE_DATE")) >= 0;
    if (booleanCostIDDetail) {
      if ("".equals(stringCostIDDetail) && !"D".equals(stringApplyType)) {
        messagebox("[分類工料代碼] 不可為空白。");
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
    // 20180309 固定資產判斷移除 By B03812
    /*
     * if("D".equals(stringApplyType)) { talk dbAsset = getTalk(""+get("put_Asset"))
     * ; Vector vectorColumnName = new Vector() ; Vector vectorAsAssetFilter = new
     * Vector() ; String stringComNoAcctNo = "" ; String[][] retDoc2M0401 =
     * exeFun.getDoc2M0401("", "U", "") ; if(retDoc2M0401.length > 0) {
     * stringFILTER_DO = retDoc2M0401[0][2].trim() ; } // 不可為空白
     * if("".equals(stringFILTER)) { messagebox("[固資代碼] 不可為空白。") ;
     * getcLabel("FILTER").requestFocus() ; return false ; } // 存在檢核
     * vectorAsAssetFilter = exeUtil.getQueryDataHashtable("AS_ASSET_FILTER", new
     * Hashtable(), " AND  FILTER  = '"+stringFILTER+"' ", vectorColumnName,
     * dbAsset) ; if(vectorAsAssetFilter.size() == 0) {
     * messagebox("[固資代碼] 不存在資料庫中。") ; getcLabel("FILTER").requestFocus() ; return
     * false ; } if(!"".equals(stringInOutL)) { String stringSqlAnd = "" ;
     * String[][] retDoc2M0201 = null ; //if("033FG".equals(stringDocNo1)) {
     * stringSqlAnd = " AND  Remark =  '內業' " ; //} else
     * if("I".equals(stringInOutL)) {
     * if(stringSpecBudget.indexOf(","+stringDepartNo+",") != -1) { stringSqlAnd =
     * " AND  Remark =  '外業' " ; } else { stringSqlAnd = " AND  Remark =  '內業' " ; }
     * } else { stringSqlAnd = " AND  Remark =  '外業' " ; } retDoc2M0201 =
     * exeFun.getDoc2M0201(stringComNo, "", "", "O", stringCDateAC, stringSqlAnd) ;
     * if(retDoc2M0201.length == 0) { messagebox("資料發生錯誤，請洽資訊室。") ; return false ; }
     * setValue("CostID", retDoc2M0201[0][0].trim()) ; setValue("CostID1",
     * retDoc2M0201[0][1].trim()) ; if(!stringFILTER.equals(stringFILTER_DO)) { //
     * 對應公司欄位欄位檢核 stringTemp = "SPEC_ACNTNO_SET_"+stringComNo ;
     * if("I".equals(stringInOutL)) stringTemp =
     * "SPEC_ACNTNO_SET_"+stringComNo+"_IN" ;
     * if(vectorColumnName.indexOf(stringTemp) == -1) {
     * messagebox("[固資代碼] 不存在對應 [公司-會計科目] 中。") ; getcLabel("FILTER").requestFocus()
     * ; return false ; } // 會計科目存在檢核 Hashtable hashtableTmp = (Hashtable)
     * vectorAsAssetFilter.get(0) ; if(hashtableTmp == null) {
     * messagebox("資料發生錯誤，請洽資訊室。") ; getcLabel("FILTER").requestFocus() ; return
     * false ; } stringComNoAcctNo = ""+hashtableTmp.get(stringTemp) ;
     * if("null".equals(stringComNoAcctNo) || "".equals(stringComNoAcctNo)) {
     * messagebox("[固資代碼] 對應 [公司-會計科目] 為空白。") ; getcLabel("FILTER").requestFocus() ;
     * return false ; } } } }
     */
    else {
      setValue("FILTER", "");
    }
    // 請款代碼
    String stringCostID = getValue("CostID").trim(); // if("B3018".equals(getUser())) messagebox(stringCostID) ;
    if ("".equals(stringCostID)) {
      messagebox("[請款代碼] 不可為空白。");
      getcLabel("CostID").requestFocus();
      return false;
    }
    String stringCostID1 = getValue("CostID1").trim();// if("B3018".equals(getUser())) messagebox(stringCostID1) ;
    if ("".equals(stringCostID1)) {
      messagebox("[小請款代碼] 不可為空白。");
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
     * if(!"".equals(stringTemp)) stringTemp += "、" ; stringTemp +=
     * ""+vecrorCostIDTypeO.get(intNo) ; }
     * messagebox("非 [固定資產] 之請款代碼 不能為 "+stringTemp+"。") ; return false ; } }
     */
    /*
     * 20180213 “分類工料代碼”78開頭的都是pop相關的 都不要鎖下面那格的”pop代碼”
     */
    if (!stringCostIDDetail.startsWith("78")) {
      if (stringFunctionName.indexOf("採購") != -1) {
        if (",782,".indexOf(stringCostID + stringCostID1) != -1 && ",P65288,P67082,P39936,Q02517,P90266,P65403,Q22241,P39874,".indexOf("," + getValue(".BarCode") + ",") == -1) {
          if ("".equals(getValue("PopCode").trim())) {
            messagebox("POP代碼 不能為 空白。");
            return false;
          }
        }
      }
    }
    if (stringSpecBudget.indexOf(getValue(".DocNo1")) == -1) {
      if (",033,053,133,".indexOf(exeUtil.doSubstring(getValue(".DocNo1"), 0, 3)) == -1) {
        if ("31,32,".indexOf(stringCostID) != -1) {
          messagebox("非行銷部室 不允許使用 [請款代碼](31、32)。\n(有問題請洽 [財務室])。");
          if (!"SYS".equals(getUser())) return false;
        }
        if (exeUtil.doParseDouble(stringCostID) >= 70 && !"D".equals(stringApplyType)) {
          if (getValue(".DocNo1").startsWith("015") && "721,".indexOf(stringCostID + stringCostID1) != -1) {
            // 特例允許
          } else {
            messagebox("非行銷部室 不允許使用 70 之後的請款代碼，不允許異動資料庫。\n(有問題請洽 [財務室])。");
            if ("SYS,B3018,".indexOf(getUser()) == -1) return false;
          }
        }
      } else {
        // 2017-03-16 B2358 修改邏輯，允許 60 使用於內業
        /*
         * if("60,".indexOf(stringCostID)!=-1) { if(!"0333".equals(getValue(".DocNo1")))
         * { messagebox("[行銷部室] 不允許使用 [請款代碼](60)。\n(有問題請洽 [財務室])。") ; return false ; } }
         */
      }
    }
    //
    String stringSqlAnd = " AND  FunctionType LIKE  '%2%' " + " AND  (DateStart='9999/99/99'  OR  DateStart<='" + stringCDateAC + "' )"
        + " AND  (DateEnd='9999/99/99'    OR  DateEnd>='" + stringCDateAC + "' )" + " AND  ComNo  IN ('ALL',  '" + stringComNo + "' )" + " AND  CostID  =  '" + stringCostID + "' "
        + " AND  CostID1  =  '" + stringCostID1 + "' ";
    Vector vectorDoc2M0201 = exeFun.getQueryDataHashtableDoc("Doc2M0201", new Hashtable(), stringSqlAnd, new Vector(), exeUtil);
    if (vectorDoc2M0201.size() > 0) {
      messagebox("[請款代碼][小請款代碼] 不允許使用。\n(有問題請洽 [行銷管理室])。1");
      return false;
    }
    // 提醒使用者
    if (!isUnionPurchase(exeUtil, exeFun)) return false;
    //
    System.out.println("[請款代碼][小請款代碼] 存在(" + stringCostID + ")(" + stringCostID1 + ")(" + stringCostID2 + ")----------------------------------------------S");
    String[][] retDoc7M011 = exeFun.getDoc7M011(stringComNo, "", stringCostID, stringCostID1);
    if (retDoc7M011.length == 0) {
      if (exeFun.getDoc2M0201("ALL", stringCostID, stringCostID1, "A").length == 0) {
        if (exeFun.getDoc2M0201(stringComNo, stringCostID, stringCostID1, "A").length == 0) {
          messagebox("[請款代碼][小請款代碼] 不存在資料庫中。\n(有問題請洽 [行銷管理室])。1");
          return false;
        }
      }
    }
    System.out.println("[請款代碼][小請款代碼] 存在----------------------------------------------E");
    // 通路代碼 檢核
    String stringBigBudget = getBigBudget(stringCostID, stringCostID1, exeUtil, exeFun);
    String stringSSMediaID = getValue("SSMediaID").trim();
    String stringSSMediaID1 = "";
    String stringSSMediaID1DB = "";
    if (stringFunctionName.indexOf("採購") == -1) {
      if (!"".equals(stringSSMediaID)) {
        // 0331I03201509-001 存在檢核
        if (!isERPKeyExist(stringSSMediaID, exeUtil, dbAO)) return false;
        //
        // 特殊請款代碼一定不可有活動代碼
        // 活動代碼 與 請款代碼 對應檢核
        stringSSMediaID1 = exeUtil.doSubstring(stringSSMediaID, stringSSMediaID.length() - 13, stringSSMediaID.length() - 12);
        stringSSMediaID1DB = getSSMediaIDDoc(stringCostID, stringCostID1, exeUtil, exeFun);
        if (!"".equals(stringSSMediaID1DB) && !stringSSMediaID1.equals(stringSSMediaID1DB)) {
          messagebox("[請款代碼]" + stringSSMediaID1DB + " 對應 [通路代碼] 關係錯誤。");
          return false;
        }
        // 須為企劃類請款代碼
        if ("A".equals(stringBigBudget)) {
          messagebox("[通路代碼] 只能使用企劃類的 請款代碼。");
          return false;
        }
      } else {
        // 0003 業務變動成本 一定要有活動代碼
        /*
         * if("0003".equals(stringBigBudget)) { getcLabel("SSMediaID").requestFocus() ;
         * messagebox("此請款代碼 隸屬於 [業務變動成本]，活動代碼 不可為空白。") ; return false ; }
         */
        if (getValue(".DocNo1").indexOf("333") == -1 && "782".equals(stringCostID + stringCostID1)) {
          getcLabel("SSMediaID").requestFocus();
          messagebox("POP 請款代碼 活動代碼 不可為空白。");
          return false;
        }
      }
    }
    //
    // 內容
    String stringClassName = getValue("ClassName").trim();
    if (!booleanApplyTypeF && "".equals(stringClassName)) {
      messagebox("[規格] 不可為空白。");
      getcLabel("ClassName").requestFocus();
      return false;
    }
    // 內容
    String stringClassNameDescript = getValue("ClassNameDescript").trim();
    if ("".equals(stringClassNameDescript)) {
      messagebox("[內容] 不可為空白。");
      getcLabel("ClassNameDescript").requestFocus();
      return false;
    }
    if (stringClassName.equals(stringClassNameDescript)) {
      messagebox("[分類工料名稱][內容] 內容不可相同。\n(有問題請洽 [財務室])");
      getcLabel("ClassName").requestFocus();
      return false;
    }
    // 權限檢核
    // 單位不可為空白
    String stringUnit = getValue("Unit").trim();
    if ("".equals(stringUnit)) {
      messagebox("[單位] 不可為空白。");
      getcLabel("Unit").requestFocus();
      return false;
    }
    // 請購數量
    String stringBudgetNum = getValue("BudgetNum").trim();
    String stringControlType = "" + get("ONLY_CONTROL_AMT");
    if (exeUtil.doParseDouble(stringBudgetNum) <= 0) {
      messagebox("[請購數量] 不能小於 0。");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    if (booleanUnit && ("," + stringControlType + ",").indexOf("," + stringUnit + ",") != -1 && exeUtil.doParseDouble(stringBudgetNum) != 1) {
      messagebox("[單位] 為 [" + stringControlType + "] 時，[請購數量] 只能為 1。");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    if (!check.isFloat(stringBudgetNum, "11,4")) {
      messagebox("[請購數量] 格式錯誤，只允許7 位數及小數點後 4 位。");
      getcLabel("BudgetNum").requestFocus();
      return false;
    }
    // 請購金額
    String stringApplyMoney = getValue("ApplyMoney").trim();
    if (!"".equals(stringApplyMoney) && exeUtil.doParseDouble(stringApplyMoney) == 0) {
      messagebox("[請購金額] 只能是數字。");
      getcLabel("ApplyMoney").requestFocus();
      return false;
    }
    if (exeUtil.doParseDouble(stringApplyMoney) < 0 && ",701,702,".indexOf("," + stringCostID + stringCostID1 + ",") == -1) {
      messagebox("僅 請款代碼 701接待中心裝璜設計費用(含精神堡壘)、702樣品屋裝璜設計費用 允許申請負值。");
      return false;
    }
    String stringHistoryPrice = getValue("HistoryPrice").trim();
    if (exeUtil.doParseDouble(stringApplyMoney) == 0 && exeUtil.doParseDouble(stringHistoryPrice) == 0) {
      messagebox("[請購數量][預算單價]，不可皆為 0。");
      getcLabel("ApplyMoney").requestFocus();
      return false;
    }
    // 使用日期
    String stringDateStart = getValue("DateStart").trim();
    String stringDateEnd = getValue("DateEnd").trim();
    Vector vectorCostID = getVectorCostID(exeFun);
    if (vectorCostID.indexOf(stringCostID + stringCostID1) != -1 || "49".equals(stringCostID)) {
      // 使用日期(起)
      stringDateStart = exeUtil.getDateFullRoc(stringDateStart, "使用日期(起)");
      if (stringDateStart.length() != 9) {
        messagebox(stringDateStart);
        getcLabel("DateStart").requestFocus();
        return false;
      } else {
        setValue("DateStart", stringDateStart);
      }
      if ("".equals(stringDateEnd)) {
        messagebox("[使用日期(訖)] 不可為空白。");
        return false;
      }
      stringDateEnd = exeUtil.getDateFullRoc(stringDateEnd, "使用日期(訖)");
      if (stringDateEnd.length() != 9) {
        messagebox(stringDateEnd);
        getcLabel("DateEnd").requestFocus();
        return false;
      } else {
        setValue("DateEnd", stringDateEnd);
      }
      if (datetime.subDays1(convert.replace(stringDateEnd, "/", ""), convert.replace(stringDateStart, "/", "")) < 0) {
        messagebox("[使用日期(訖)] 早於 [使用日期(起)]。");
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
      // 縣市
      if ("".equals(stringCoun)) {
        messagebox("[縣市] 不可為空白。");
        getcLabel("Coun").requestFocus();
        return false;
      }
      // 鄉鎮
      if ("".equals(stringTown)) {
        messagebox("[鄉鎮] 不可為空白。");
        getcLabel("Town").requestFocus();
        return false;
      }
      retTown = exeFun.getTown(stringCoun, stringTown, "");
      if (retTown.length == 0) {
        messagebox("[縣市][鄉鎮] 對應錯誤。");
        return false;
      }
    }
    //
    if (stringFunctionName.indexOf("採購") != -1) {
      // 實際數量
      String stringActualNum = getValue("ActualNum").trim();
      if (exeUtil.doParseDouble(stringActualNum) <= 0) {
        messagebox("[實際數量] 不能小於 0。");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      if (booleanUnit && ("," + stringControlType + ",").indexOf("," + stringUnit + ",") != -1 && exeUtil.doParseDouble(stringActualNum) != 1) {
        messagebox("[單位] 為 [" + stringControlType + "] 時，[實際數量] 只能為 1。");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      if (!check.isFloat(stringActualNum, "11,4")) {
        messagebox("[[請購數量] 格式錯誤，只允許 7 位數及小數點後 4 位。");
        getcLabel("ActualNum").requestFocus();
        return false;
      }
      // 發包單價
      String stringActualPrice = getValue("ActualPrice").trim();
      /*
       * if(!"".equals(stringActualPrice) && !exeUtil.isDigitNum(stringActualPrice)) {
       * message("[發包單價] 只能是數字。") ; getcLabel("ActualPrice").requestFocus() ; return
       * false ; }
       */
      // 採發金額
      String stringPurchaseMoney = getValue("PurchaseMoney").trim();
      /*
       * if(!"".equals(stringPurchaseMoney) &&
       * !exeUtil.isDigitNum(stringPurchaseMoney)) { message("[採發金額] 只能是數字。") ;
       * getcLabel("PurchaseMoney").requestFocus() ; return false ; }
       */
      if (exeUtil.doParseDouble(stringPurchaseMoney) < 0 && ",701,702,".indexOf("," + stringCostID + stringCostID1 + ",") == -1) {
        messagebox("僅 請款代碼 701接待中心裝璜設計費用(含精神堡壘)、702樣品屋裝璜設計費用 允許申請負值。");
        return false;
      }
      /*
       * if(exeUtil.doParseDouble(stringActualPrice) == 0 &&
       * exeUtil.doParseDouble(stringActualNum) == 0) {
       * messagebox("[發包單價][採發金額] 不能皆為 0。") ; return false ; }
       */
    }
    // 廠商
    String stringFactoryNo = getValue("FactoryNo").trim();
    if (stringFunctionName.indexOf("採購") != -1 || booleanApplyTypeF) {
      if ("".equals(stringFactoryNo)) {
        messagebox("[廠商代碼] 不可為空白。");
        getcLabel("FactoryNo").requestFocus();
        return false;
      }
    }
    // 停權
    String stringStopUseMessage = "";
    Hashtable hashtableCond = new Hashtable();
    hashtableCond.put("OBJECT_CD", stringFactoryNo);
    hashtableCond.put("CHECK_DATE", "");
    hashtableCond.put("SOURCE", "A");
    hashtableCond.put("FieldName", "[廠商代碼] ");
    stringStopUseMessage = exeFun.getStopUseObjectCDMessage(hashtableCond, exeUtil);
    if (!"TRUE".equals(stringStopUseMessage)) {
      messagebox(stringStopUseMessage);
      getcLabel("FactoryNo").requestFocus();
      return false;
    }
    // 統購
    if (booleanApplyTypeF) {
      // 統購
      if ("805".equals(stringCostID + stringCostID1)) {
        // 贈品 Doc3M016、Doc3M017
        if (!isApplyF805OK(stringFunctionName, exeUtil, exeFun)) return false;
      } else {
        // 非贈品 Doc3M0174
        if (!isApplyFNot805OK(stringFunctionName, exeUtil, exeFun)) return false;
      }
    }
    if (!isTableProjectCheck(stringFunctionName, exeUtil, exeFun)) return false; // 案別分攤
    if (!isTable17Check(stringFunctionName, exeUtil, exeFun)) return false; // 議價資訊
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
    if (stringFunctionName.indexOf("採購") != -1) return true;
    //
    hashtableDoc3M016 = getDoc3M016(stringDateStart, stringDateEnd, exeUtil, exeFun);
    if (hashtableDoc3M016 == null) {
      messagebox("此項目不存在 [2.統購廠商費用對照表-贈品(Doc3M0162)] 中。\n(有問題請洽 [採購室])");
      return false;
    }
    // 是否存在於統購廠商資料中
    if (exeUtil.doParseDouble(stringRecordNoDoc3M017) > 0) {
      hashtableDoc3M017 = getDoc3M017(stringRecordNoDoc3M017, hashtableDoc3M016, exeUtil, exeFun);
      if (hashtableDoc3M017 == null) {
        messagebox("此項目不存在 [2.統購廠商費用對照表-贈品(Doc3M0162)] 中。\n(有問題請洽 [採購室])");
        return false;
      }
      if (!isControlNumOK(stringDateStart, hashtableDoc3M017, exeUtil, exeFun)) return false;
    }
    // 廠商檢核
    stringFactoryNo17 = "" + hashtableDoc3M017.get("FactoryNo");
    if ("null".equals(stringFactoryNo17)) stringFactoryNo17 = "";
    if (!"".equals(stringFactoryNo17)) {
      if (!stringFactoryNo.equals(stringFactoryNo17)) {
        if (stringFunctionName.indexOf("採購") == -1) {
          setValue("FactoryNo", stringFactoryNo17);
        }
      }
    } else {
      stringDateStart = "" + hashtableDoc3M017.get("DateStart");
      stringDateEnd = "" + hashtableDoc3M017.get("DateEnd");
      if (exeFun.getDoc3M0171(stringCostID, stringCostID1, "", stringFactoryNo, stringDateStart, stringDateEnd, "", "").length == 0) {
        if (stringFunctionName.indexOf("採購") == -1) {
          messagebox("此 [廠商] 不存在 [2.統購廠商費用對照表-贈品(Doc3M0162)] 中。\n(有問題請洽 [採購室])");
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
    if (stringFunctionName.indexOf("採購") != -1) return true;
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
    // hashtableAnd.put("ItemName", stringClassName) ; // 規格
    // hashtableAnd.put("Unit", stringUnit) ; // 單位
    stringSqlAnd = " AND  ( DateStart = '9999/99/99'  OR  DateStart <= '" + stringDateStart + "' )" + " AND  ( DateEnd = '9999/99/99'  OR  DateEnd >= '" + stringDateEnd + "' )";
    vectorDoc3M0174 = exeFun.getQueryDataHashtableDoc("Doc3M0174", hashtableAnd, stringSqlAnd, new Vector(), exeUtil);
    if (vectorDoc3M0174.size() == 0) {
      messagebox("此項目不存在 不存在 [3.統購廠商費用對照表-行銷(Doc3M0163)]。\n(有問題請洽 [採購室])");
      return false;
    }
    // 廠商檢核
    stringFactoryNo17 = exeUtil.getVectorFieldValue(vectorDoc3M0174, 0, "FactoryNo");
    if (!"".equals(stringFactoryNo17)) {
      if (!stringFactoryNo.equals(stringFactoryNo17)) {
        if (stringFunctionName.indexOf("採購") == -1) {
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
      messagebox("統購管控數量 為 " + exeUtil.getFormatNum2("" + doubleControlNum) + "，現已申請(含本項) " + exeUtil.getFormatNum2("" + doubleUseNum) + "。 ");
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
    // 請購
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
    hashtableAnd.put("set_flag", "預算通過");
    vectorViewAOSeminar = exeUtil.getQueryDataHashtable("View_AO_Seminar", hashtableAnd, "", dbAO);
    //
    if (vectorViewAOSeminar.size() == 0) {
      messagebox("[通路代碼] 不存在資料庫中。");
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
    Vector vectorDeptCd = new Vector(); // 內業控管
    Vector vectorDeptCd2 = new Vector(); // 內業控管
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
    // 當為外業，且為 0531 時，小案別只能為 H35、H36A、H39、M51A
    // vectorProjectID1.add("H35") ;
    // vectorProjectID1.add("H36A") ;
    // vectorProjectID1.add("H39") ;
    // vectorProjectID1.add("M51A") ;
    // vectorProjectID1.add("H63A") ;
    // 內業控管
    vectorDeptCd.add("03335");
    vectorDeptCd.add("033622");
    vectorDeptCd.add("03363");
    vectorDeptCd.add("03365");
    vectorDeptCd.add("0333");
    for (int intNo = 0; intNo < arraySpecBudget.length; intNo++)
      vectorDeptCd.add(arraySpecBudget[intNo]);
    vectorDeptCd.add("03396");
    // 不作內外業檢核
    vectorDeptCd2.add("03395"); // 2014-01-28 依王承歡 新增
    if (jtableProject.getRowCount() <= 0) {
      jtabbedpane1.setSelectedIndex(0);
      messagebox("請輸入 [分攤案別] 表格。");
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
    if (stringFunctionName.indexOf("採購") != -1) {
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
      // BT 控管
      retDoc7M0265 = exeFun.getTableDataDoc("SELECT  AreaNum,  DateStart,  DateEnd,  ProjectIDMajor " + " FROM  Doc7M0265 " + " WHERE  ComNo  =  '" + stringComNo + "' "
          + " AND  ProjectID1  =  '" + stringProjectID1 + "' ");
      if (retDoc7M0265.length > 0) {
        String stringNeedDateL = exeUtil.getDateConvert(stringNeedDate);
        if (stringNeedDateL.compareTo(retDoc7M0265[0][1].trim()) < 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("第 " + (intNo + 1) + " 列 [案別](" + stringProjectID1 + ") 尚不允許使用。\n(有問題請洽 [財務室])");
          setFocus("TableProject", intNo, "ProjectID");
          if (!getUser().startsWith("b")) return false;
        }
        if (stringNeedDateL.compareTo(retDoc7M0265[0][2].trim()) > 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("第 " + (intNo + 1) + " 列 [案別](" + stringProjectID1 + ") 已不允許使用。\n(有問題請洽 [財務室])");
          setFocus("TableProject", intNo, "ProjectID");
          return false;
        }
        // 請款代碼控管
        /*
         * if("BT".equals(retDoc7M0265[0][3].trim()) &&
         * "701,710,".indexOf(stringCostID+stringCostID1) != -1) {
         * messagebox("[BT 案別] 不允許使用 [701 接待中心裝璜設計費用]及[710 園藝工程]。\n(有問題請洽 [財務室])") ;
         * return false ; }
         */
        /*
         * if("新莊".equals(retDoc7M0265[0][3].trim()) &&
         * "701,702,710,".indexOf(stringCostID+stringCostID1) != -1) {
         * messagebox("[新莊 案別] 不允許使用 [701][702]及[710]。\n(有問題請洽 [行銷管理室])") ; return false
         * ; }
         */
      }
      /*
       * if(retDoc7M0265.length > 0 && "BT".equals(retDoc7M0265[0][3].trim())) {
       * if("".equals(stringProjectID1) || !stringBudgetID.startsWith("B") ||
       * "".equals(stringBudgetID)) { if(!booleanActionNo &&
       * "Y".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT 案別] 不可與 非 [BT 案別] 一起使用。\n(有問題請洽 [財務室])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; }
       * stringProjectID1BTType = "N" ; // 非 BT案別 } else { if(retDoc7M0265.length ==
       * 0) { if(!booleanActionNo && "Y".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT 案別] 不可與 非 [BT 案別] 一起使用。\n(有問題請洽 [財務室])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; }
       * stringProjectID1BTType = "N" ; // 非 BT案別 } else { if(!booleanActionNo &&
       * "N".equals(stringProjectID1BTType)) {
       * jtableProject.setRowSelectionInterval(intNo, intNo) ;
       * messagebox("[BT 案別] 不可與 非 [BT 案別] 一起使用。\n(有問題請洽 [財務室])") ;
       * setFocus("TableProject", intNo, "ProjectID") ; return false ; } //
       * if(!booleanActionNo) { if(stringFunctionName.indexOf("採購") != -1) {
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
        messagebox("第 " + (intNo + 1) + " 列資料重複。");
        return false;
      }
      vectorKey.add(stringKey);
      if (stringDocNoF.indexOf("033FZ") != -1 && !"0333".equals(stringDepartNo)) {
        jtabbedpane1.setSelectedIndex(0);
        setFocus("TableProject", intNo, "DepartNo");
        messagebox("第 " + (intNo + 1) + " 列 之 公文編號 033FZ 只能用 內業 0333 作分攤 。\n(有問題請洽 [人總室])");
        return false;
      }
      // 內外業
      if ("".equals(stringInOut)) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("第 " + (intNo + 1) + " 列[內外業] 不得為空白。");
        setFocus("TableProject", intNo, "InOut");
        return false;
      }
      // 部門
      if ("".equals(stringDepartNo.trim())) {
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("第 " + (intNo + 1) + " 列[部門] 不得為空白。");
        setFocus("TableProject", intNo, "DepartNo");
        return false;
      }
      if ("".equals(exeFun.getDepartName(stringDepartNo))) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("[部門] 不存在資料庫中。\n(有問題請洽 [資訊室])");
        setFocus("TableProject", intNo, "DepartNo");
        return false;
      }
      // System.out.println("------------------費用代碼與內外業一致檢核") ;
      if (vectorDeptCd2.indexOf(stringDepartNo) == -1 && vectorDeptCd.indexOf(stringDepartNo) == -1 && !exeFun.isInOutToCostID(stringInOut, stringCostID)) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("[請款代碼-" + stringCostID + "] 與 [內/外業-" + stringInOut + "] 不一致。\n(有問題請洽 [行銷管理室])");
        setFocus("TableProject", intNo, "InOut");
        return false;
      }
      // 案別
      if (stringProjectID.startsWith("053") || stringProjectID1.startsWith("053")) {
        jtabbedpane1.setSelectedIndex(0);
        jtableProject.setRowSelectionInterval(intNo, intNo);
        messagebox("第 " + (intNo + 1) + " 列 之 [案別] 錯誤 。");
        setFocus("TableProject", intNo, "ProjectID1");
        return false;
      }
      if ("O".equals(stringInOut)) {
        // 案別
        if ("".equals(stringProjectID)) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("[大案別] 不得為空白。");
          setFocus("TableProject", intNo, "ProjectID");
          return false;
        }
        if ("".equals(stringProjectID1)) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("第 " + (intNo + 1) + " 列 外業時，[案別] 不得為空白。");
          setFocus("TableProject", intNo, "ProjectID1");
          return false;
        }
        if (",0331,1331,0531,".indexOf(stringDepartNo) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("外業時，[部門] 只能為 [0331] 、 [1331]、[0531]。");
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        if ("0531".equals(stringDepartNo) && vectorProjectID1.indexOf(stringProjectID1) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          //
          String stringTemp = "";
          for (int intNoL = 0; intNoL < vectorProjectID1.size(); intNoL++) {
            if (!"".equals(stringTemp)) stringTemp += "、";
            stringTemp += "[" + ("" + vectorProjectID1.get(intNoL)).trim() + "]";
          }
          messagebox("外業且部門為 0531 時，[案別] 只能為" + stringTemp + "。");
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        // 存在檢查
        if (!"0531".equals(stringDepartNo) && !exeFun.isExistProjectIDCheck(stringProjectID, stringProjectID1)) {
          jtableProject.setRowSelectionInterval(intNo, intNo);
          jtabbedpane1.setSelectedIndex(0);
          setFocus("TableProject", intNo, "ProjectID");
          messagebox("[大案別] [小案別] 不存在於資料庫中。\n(有問題請洽 [行钂管理室])");
          return false;
        }
        // 程式由 hashtableAProject 得到 之 Depart 為 8 時，為高雄，如有問題，提醒使用者
        stringDepart1 = "" + hashtableAProject.get(stringProjectID);
        stringDepart2 = "" + hashtableAProject.get(stringProjectID1);
        if (!"0531".equals(stringDepartNo)) {
          if ("8".equals(stringDepart2) || "8".equals(stringDepart1)) {
            // 高雄
            if (!"1331".equals(stringDepartNo.trim())) {
              stringDepartMessage = "第 " + (intNo + 1) + " 列之案別應為 1331，不允許異動資料庫。";
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              setFocus("TableProject", intNo, "ProjectID");
              messagebox(stringDepartMessage);
              return false;
            }
          } else if ("1331".equals(stringDepartNo.trim())) {
            stringDepartMessage = "第 " + (intNo + 1) + " 列之案別不應為 1331，不允許異動資料庫。";
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
          messagebox("第 " + (intNo + 1) + " 列 內業時，[案別] 不可有值。");
          jtableProject.setRowSelectionInterval(intNo, intNo);
          return false;
        }
        if ("033MP".equals(stringDepartNo.trim())) {
          stringDepartMessage = "第 " + (intNo + 1) + " 列 [033MP] 屬於外業，不允許異動資料庫。";
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox(stringDepartMessage);
          setFocus("TableProject", intNo, "DepartNo");
          return false;
        }
        if (stringSpecBudget.indexOf("," + stringDepartNo + ",") == -1 && !exeUtil.isDigitNum(stringDepartNo.trim())) {
          jtabbedpane1.setSelectedIndex(0);
          stringDepartMessage = "分攤案別第 " + (intNo + 1) + " 列 內業部門(" + stringDepartNo.trim() + ")格式錯誤，不允許異動資料庫。\n(有問題請洽 [資訊企劃室])";
          setFocus("TableProject", intNo, "DepartNo");
          messagebox(stringDepartMessage);
          return false;
        }
        if (stringSpecBudget.indexOf("," + stringDepartNo + ",") == -1 && !"".equals(stringExistDeptCd)
            && stringExistDeptCd.indexOf(exeUtil.doSubstring(stringDepartNo, 0, 3)) == -1) {
          jtabbedpane1.setSelectedIndex(0);
          stringDepartMessage = "第 " + (intNo + 1) + " 列 內業部門(" + stringDepartNo.trim() + ")不存在於該公司，不允許異動資料庫。\n(有問題請洽 [財務室])";
          messagebox(stringDepartMessage);
          setFocus("TableProject", intNo, "DepartNo");
          if (!"B3018".equals(getUser())) return false;
        }
      }

      // 金額
      if (stringFunctionName.indexOf("採購") != -1) {
        /*
         * if(exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
         * jtabbedpane1.setSelectedIndex(0) ;
         * jtableProject.setRowSelectionInterval(intNo, intNo) ;
         * messagebox("第 "+(intNo+1)+" 列 [採發金額] 不可等於 0。") ; setFocus("TableProject",
         * intNo, "PurchaseMoney") ; if(!"B3018".equals(getUser()))return false ; }
         */
        doubleApplyMoney += exeFun.doParseDouble(stringPurchaseMoney);
      } else {
        if (exeUtil.doParseDouble(stringApplyMoney) == 0) {
          jtabbedpane1.setSelectedIndex(0);
          jtableProject.setRowSelectionInterval(intNo, intNo);
          messagebox("第 " + (intNo + 1) + " 列 [金額] 不可等於 0。");
          setFocus("TableProject", intNo, "ApplyMoney");
          return false;
        }
        doubleApplyMoney += exeFun.doParseDouble(stringApplyMoney);
      }

      // 案預算檢查
      booleanFlag = !"001".equals(stringCostID + stringCostID1) && "O".equals(stringInOut) && stringFunctionName.indexOf("採購") == -1 && !"".equals(stringBudgetID)
          && vectorProjectID1NoUseBudget.indexOf(stringDepartNo + "---" + stringProjectID + "---" + stringProjectID1 + "---" + stringBudgetID.substring(0, 1)) == -1; // 特殊案別，不作預算檢核
      /*
       * if(!booleanFlag && vectorDeptCd.indexOf(stringDepartNo)==-1 &&
       * ((stringBudgetID.length()>0) && "B".equals(stringBudgetID.substring(0,1)))) {
       * stringProjectID1Use = stringDepartNo ; booleanFlag = true ; }
       */
      if (booleanFlag && !(stringProjectID1Use.startsWith("053") && "B".equals(stringBudgetID.substring(0, 1)))) {
        retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "U");
        if (retDoc7M020.length == 0) {
          String stringTemp2 = "A".equals(stringBudgetID.substring(0, 1)) ? "\n(有問題請洽 [行銷管理室])" : "\n(有問題請洽 [行銷企劃室])";
          // 案前預算
          retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "F");
          if (retDoc7M020.length == 0) {
            retDoc7M020 = exeFun.getDoc7M020ForComNo(stringComNo, stringProjectID1Use, stringBudgetID.substring(0, 1), stringCDateAC, "<=", "", "R");
          }
          if (retDoc7M020.length == 0) {
            String stringTemp = "A".equals(stringBudgetID.substring(0, 1)) ? "行政" : "企劃";
            jtabbedpane1.setSelectedIndex(0);
            jtableProject.setRowSelectionInterval(intNo, intNo);
            messagebox("第 " + (intNo + 1) + " 列 之 [案別](" + stringProjectID1Use + ") " + stringTemp + "沒有編列預算。" + stringTemp2);
            setFocus("TableProject", intNo, "ProjectID");
            return false;
          } else {
            // 雖未完成預算流程，但已簽核至 [體系主管] 且 [CDate] 在 [公開期] 之前，允許
            /*
             * if("A".equals(retDoc7M020[0][8].trim()) ||
             * "P".equals(retDoc7M020[0][8].trim())) { jtabbedpane1.setSelectedIndex(0) ;
             * jtableProject.setRowSelectionInterval(intNo, intNo) ; messagebox( "第 " +
             * (intNo+1) + " 列 之 [案預算控管(Doc7M020)] 未執行至 [體系主管]，不能執行 [案前預算]。") ; return false
             * ; }
             */
            if ("".equals(retDoc7M020[0][4].trim())) {
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              messagebox("第 " + (intNo + 1) + " 列 之 [案預算控管(Doc7M020)] 之 [公開期] 為空白，不能執行 [案前預算]。" + stringTemp2);
              setFocus("TableProject", intNo, "ProjectID");
              return false;
            }
            if (exeUtil.getDateConvert(getValue(".NeedDate")).compareTo(retDoc7M020[0][4].trim()) > 0) {
              jtabbedpane1.setSelectedIndex(0);
              jtableProject.setRowSelectionInterval(intNo, intNo);
              messagebox("第 " + (intNo + 1) + " 列 之 [需求日期] 位於 [案預算控管(Doc7M020)] 之 [公開期] 後，不能執行 [案前預算]。" + stringTemp2);
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
    if (stringFunctionName.indexOf("採購") != -1) {
      doubleApplyMoneyCF = exeFun.doParseDouble(getValue("PurchaseMoney").trim());
      stringTemp = "採發金額";
    } else {
      doubleApplyMoneyCF = exeFun.doParseDouble(getValue("ApplyMoney").trim());
      stringTemp = "預算金額";
    }
    if (!"Y".equals(stringProjectID1BTType)) {
      doubleApplyMoney = exeFun.doParseDouble(convert.FourToFive("" + doubleApplyMoney, 0));
      if (doubleApplyMoneyCF != doubleApplyMoney && "".equals(getValue(".ActionNo").trim())) {
        jtabbedpane1.setSelectedIndex(0);
        messagebox("[表格之" + stringTemp + "] 合計不等於 [" + stringTemp + "]。");
        return false;
      }
    }
    //
    return true;
  }

  public boolean isTable17Check(String stringFunctionName, FargloryUtil exeUtil, Doc2M010 exeFun) throws Throwable {
    if (stringFunctionName.indexOf("採購") == -1) return true;
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
      // 廠商不可空白
      if ("".equals(stringFactoryNo)) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("議價表格之第 " + (intNo + 1) + " 列 [議價廠商]不可為空白。");
        return false;
      }
      // 廠商資料庫檢核
      stringFactoryName = exeFun.getNameUnionDoc("OBJECT_FULL_NAME", "Doc3M015", " AND  OBJECT_CD  =  '" + stringFactoryNo + "' ", new Hashtable(), exeUtil);
      if ("".equals(stringFactoryName)) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("議價表格之第 " + (intNo + 1) + " 列 [議價廠商]不存在資料庫中。");
        return false;
      }
      // 單價及金額 不可皆為空白
      if (exeUtil.doParseDouble(stringActualPrice) == 0 && exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
        jtabbedpane1.setSelectedIndex(1);
        jtable17.setRowSelectionInterval(intNo, intNo);
        messagebox("議價表格之第 " + (intNo + 1) + " 列 [單價][金額] 不可皆為空白。");
        return false;
      } else if (exeUtil.doParseDouble(stringActualPrice) == 0) {
        // 單價空白處理
        stringActualPrice = "" + (exeUtil.doParseDouble(stringPurchaseMoney) / exeUtil.doParseDouble(stringActualNum));
        stringActualPrice = convert.FourToFive(stringActualPrice, 4);
        setValueAt("Table17", stringActualPrice, intNo, "ActualPrice");
      } else if (exeUtil.doParseDouble(stringPurchaseMoney) == 0) {
        // 金額空白處理
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
//                messagebox("[核決廠商]及[核決金額] 不存在 議價表格中。") ;
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
    if (stringFunction.indexOf("採購") != -1) return true;
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
      int ans = JOptionPane.showConfirmDialog(null, "請確認申請項目是否為統購，非統購項目請按 [是]，否則請按 [否] ?", "訊息", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
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
