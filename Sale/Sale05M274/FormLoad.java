package Sale.Sale05M274;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import jcx.db.talk;
import jcx.jform.bproc;

public class FormLoad extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    // Start 修改日期:20161024 員工編號:B3774
    put("put_stringSProjectID11", "O01A");
    //
    if (POSITION == 4 || POSITION == 5) {
      String stringSProjectID11 = (String) get("put_stringSProjectID11");
      //
      if (stringSProjectID11.indexOf(getValue("ProjectID1").trim()) != -1) {
        put("put_stringIsSFlow1", "Y");
      } else {
        put("put_stringIsSFlow1", "N");
      }
      System.out.println("stringIsSFlow1=" + get("put_stringIsSFlow1"));
    }
    // End 修改日期:20161024 員工編號:B3774
    // 修改日期:20120718 員工編號:B3774 舊版本:20120718資料夾
    if (POSITION == 1 && getValue("ContractNo").trim().length() > 0 && getValue("CompanyCd").trim().length() > 0) {
      action(9);
    }
    //
    setTableData("table5", new String[0][0]);
    getButton(5).setVisible(false);
    getButton("btnPrintRealData").setVisible(false); // 修改日期:20121213 員工編號:B3774
    //
    JTabbedPane jtp = getTabbedPane("tab1");
    int intTabPreSelectedIndex = jtp.getSelectedIndex();
    //
    // Start 修改日期:20130410 員工編號:B3774
    setVisible("btnReloadSquareAndMoneyData", false);
    setVisible("btnReSaveSquareAndMoneyData", false);
    // End 修改日期:20130410 員工編號:B3774
    setVisible("btnOpenVoid", false);
    setVisible("btnVoid", false);
    setVisible("btnOpenGetBack", false);
    setVisible("btnGetBack", false);
    //
    talk dbSale = getTalk("" + get("put_dbSale"));
    talk dbFE3D = getTalk("" + get("put_dbFE3D"));
    String stringSql = "";
    String retData[][] = null;
    String stringContractNo = getValue("ContractNo").trim();
    String stringStatus = getValue("Status");
    String stringProjectID1 = getValue("ProjectID1").trim();
    String stringContractDate = getValue("ContractDate").trim();
    String stringTransferDate = getValue("TransferDate").trim(); // 修改日期:20150811 員工編號:B3774
    String stringCompanyCd = getValue("CompanyCd").trim();
    String stringContractType = getValue("ContractType").trim(); // 修改日期:20140721 員工編號:B3774
    String stringIsVoid = getValue("IsVoid").trim();
    String stringGetBackReason = getValue("GetBackReason").trim();
    String stringGetBackReasonDisplay = getDisplayValue("GetBackReason").trim();
    String stringGetBackRemark = getValue("GetBackRemark").trim();
    String stringNotifyUser = getValue("NotifyUser").trim();
    String stringNotifyUserDeptCd = "";
    String stringUser = getUser().toUpperCase();
    String stringDeptCd = "";
    String stringState = getState();
    String stringDeptCdAll = "";
    String stringUnit = ""; // 修改日期:20130222 員工編號:B3774
    boolean blnHas01 = false;
    String stringMSG = ""; // 修改日期:20160519 員工編號:B3774
    //
    if ("A1".equals(stringProjectID1)) {
      stringDeptCdAll = "A01A";
    } else if ("A2".equals(stringProjectID1)) {
      stringDeptCdAll = "A02A";
      // Start 修改日期:20140801 員工編號:B3774
    } else if ("H28".equals(stringProjectID1)) {
      stringDeptCdAll = "H28A";
      // End 修改日期:20140801 員工編號:B3774
      // Start 修改日期:20130329 員工編號:B3774
    } else if ("M48".equals(stringProjectID1)) {
      stringDeptCdAll = "M48A";
      // End 修改日期:20130329 員工編號:B3774
    } else {
      stringDeptCdAll = stringProjectID1;
    }
    //
    stringSql = "select CmpCompanyCd " + "from Sale05M294 " + "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
    retData = dbSale.queryFromPool(stringSql);
    for (int intRow = 0; intRow < retData.length; intRow++) {
      if ("01".equals(retData[intRow][0])) {
        blnHas01 = true;
        break;
      }
    }
    //
    stringSql = "select substring(dept_cd,1,3) " + "from FE3D05 " + "where emp_no='" + stringNotifyUser + "'";
    retData = dbFE3D.queryFromPool(stringSql);
    if (retData.length > 0) {
      stringNotifyUserDeptCd = retData[0][0];
    }
    //
    stringSql = "select substring(dept_cd,1,3) " + "from FE3D05 " + "where emp_no='" + stringUser + "'";
    retData = dbFE3D.queryFromPool(stringSql);
    if (retData.length > 0) {
      stringDeptCd = retData[0][0];
    }
    //
    // Start 修改日期:20151120 員工編號:B3774
    if ("NA".equals(stringStatus)) {
      setEditable("ContractType", false);
    } else {
      setEditable("ContractType", true);
    }
    // End 修改日期:20151120 員工編號:B3774
    //
    setEditable("table1", "IsTrust", false);
    //
    // Start 修改日期:20130222 員工編號:B3774
    JTable jtable20 = getTable("table20");
    for (int intRow = 0; intRow < jtable20.getRowCount(); intRow++) {
      stringUnit = ("" + getValueAt("table20", intRow, "Unit")).trim();
      //
      if ("價款分算(元)".equals(stringUnit)) {
        setEditable("table20", intRow, "MainBuildingArea", true);
        setEditable("table20", intRow, "VerandaArea", true);
        setEditable("table20", intRow, "AwningArea", true);
      } else {
        setEditable("table20", intRow, "MainBuildingArea", false);
        setEditable("table20", intRow, "VerandaArea", false);
        setEditable("table20", intRow, "AwningArea", false);
      }
    }
    // End 修改日期:20130222 員工編號:B3774
    //
    JTable jtable26 = getTable("table26"); // 修改日期:20161017 員工編號:B3774
    //
    JTable jtable8 = getTable("table8");
    for (int intRow = 0; intRow < jtable8.getRowCount(); intRow++) {
      setEditable("table8", intRow, "DocNo1", false);
      setEditable("table8", intRow, "DocNo2", false);
      setEditable("table8", intRow, "DocNo3", false);
    }
    //
    // Start 修改日期:20151124 員工編號:B3774
    if ("GB".equals(stringStatus)) {
      ((JLabel) getcLabel("TransferDate").getComponent(1)).setText("合約修改日 ");
      // Start 修改日期:20160816 員工編號:B3774
    } else if ("NA".equals(stringStatus)) {
      ((JLabel) getcLabel("TransferDate").getComponent(1)).setText("合約修改日 ");
      // End 修改日期:20160816 員工編號:B3774
    } else {
      ((JLabel) getcLabel("TransferDate").getComponent(1)).setText("讓與日期      ");
    }
    // End 修改日期:20151124 員工編號:B3774
    if (POSITION == 1) {
      setEditable("ProjectID1", true);
      setEditable("ContractDate", true);
      setEditable("TransferDate", true); // 修改日期:20150811 員工編號:B3774
      // Start 修改日期:20140721 員工編號:B3774
      setEditable("table25", false);
      getTableButton("table25", 0).setVisible(false);
      getTableButton("table25", 2).setVisible(false);
      // End 修改日期:20140721 員工編號:B3774
      getButton("btnLoadChooseDataO").doClick();
      getButton("btnLoadChooseDataF").doClick();
      setVisible("text1", false);
      setVisible("tableFlowHistory", false);
      setVisible("text9", false);
      setVisible("tableFlowHistory2", false);
      setVisible("table11", false);
      //
      if (jtp.indexOfTab("作廢") != -1) {
        put("TabVoidComponent", jtp.getComponentAt(jtp.indexOfTab("作廢")));
        jtp.remove(jtp.indexOfTab("作廢"));
      }
      if (jtp.indexOfTab("取回通知") != -1) {
        put("TabGetBackComponent", jtp.getComponentAt(jtp.indexOfTab("取回通知")));
        jtp.remove(jtp.indexOfTab("取回通知"));
      }
      //
      jtp.setSelectedIndex(0);
    } else if (POSITION == 4) {
      boolean blnIsVoid = false;
      boolean blnIsGetBack = false;
      //
      setEditable("ProjectID1", false);
      setEditable("ContractDate", false);
      setEditable("TransferDate", false); // 修改日期:20150811 員工編號:B3774
      // Start 修改日期:20140721 員工編號:B3774
      if ("成屋".equals(stringContractType)) {
        setEditable("table25", true);
        getTableButton("table25", 0).setVisible(true);
        getTableButton("table25", 2).setVisible(true);
      } else {
        setEditable("table25", false);
        getTableButton("table25", 0).setVisible(false);
        getTableButton("table25", 2).setVisible(false);
      }
      // End 修改日期:20140721 員工編號:B3774
      setVisible("text1", true);
      setVisible("tableFlowHistory", true);
      setVisible("text9", blnHas01);
      setVisible("tableFlowHistory2", blnHas01);
      getButton(5).setVisible(true);
      getButton("btnPrintRealData").setVisible(true); // 修改日期:20121213 員工編號:B3774
      getButton("btnLoadSecPositionData").doClick();
      setVisible("table11", true);
      getButton("btnLoadReName").doClick(); // 修改日期:20160519 員工編號:B3774
      //
      // Start 修改日期:20150813 員工編號:B3774
      // if("G".equals(stringStatus) || "GA".equals(stringStatus)){
      // Start 修改日期:20151120 員工編號:B3774
      // if("G".equals(stringStatus) || "GA".equals(stringStatus) ||
      // "I".equals(stringStatus)){
      if ("G".equals(stringStatus) || "GA".equals(stringStatus) || "GB".equals(stringStatus) || "I".equals(stringStatus)) {
        // End 修改日期:20151120 員工編號:B3774
        setEditable("ContractDate", false);
        // End 修改日期:20150813 員工編號:B3774
        setVisible("TransferDate", true);
      } else {
        setEditable("ContractDate", true); // 修改日期:20150813 員工編號:B3774
        // Start 修改日期:20160816 員工編號:B3774
        if ("NA".equals(stringStatus)) {
          setVisible("TransferDate", true);
        } else {
          // End 修改日期:20160816 員工編號:B3774
          setVisible("TransferDate", false);
        } // 修改日期:20160816 員工編號:B3774
      }
      //
      getButton("btnFlowEmpData").doClick();
      getButton("btnFlowSignerData").doClick();
      getButton("btnLoadDocData").doClick();
      // 承辦在非審核時可以修改刪除
      stringSql = "select F_INP_STAT, F_INP_ID " + "from Sale05M274_FLOWC " + "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length > 0) {
        if ("承辦".equals(retData[0][0]) && stringUser.equals(retData[0][1]) && POSITION != 5) {
          if (stringGetBackReason.length() > 0) {
            getButton(3).setEnabled(false);
            getButton(4).setEnabled(false);
          } else {
            getButton(3).setEnabled(true);
            getButton(4).setEnabled(true);
            // Start 修改日期:20150813 員工編號:B3774
            // Start 修改日期:20151120 員工編號:B3774
            // if("G".equals(stringStatus) || "GA".equals(stringStatus) ||
            // "I".equals(stringStatus)){
            if ("G".equals(stringStatus) || "GA".equals(stringStatus) || "GB".equals(stringStatus) || "I".equals(stringStatus)) {
              // End 修改日期:20151120 員工編號:B3774
              setEditable("ContractDate", false);
            } else {
              // End 修改日期:20150813 員工編號:B3774
              setEditable("ContractDate", true);
            } // 修改日期:20150813 員工編號:B3774
            setEditable("TransferDate", true); // 修改日期:20150811 員工編號:B3774
            //
            // Start 修改日期:20161017 員工編號:B3774
            for (int intRow = 0; intRow < jtable26.getRowCount(); intRow++) {
              setEditable("table26", intRow, "FriendID", true);
              setEditable("table26", intRow, "FriendName", true);
              setEditable("table26", intRow, "CommMoney", true);
              setEditable("table26", intRow, "CommMoney1", true);
            }
            // End 修改日期:20161017 員工編號:B3774
            //
            for (int intRow = 0; intRow < jtable8.getRowCount(); intRow++) {
              setEditable("table8", intRow, "DocNo1", true);
              setEditable("table8", intRow, "DocNo2", true);
              setEditable("table8", intRow, "DocNo3", true);
            }
          }
          //
          setVisible("btnReloadChangeNameFeeDefaultAndF01", true);
          setVisible("btnReloadO01", true);
        } else {
          getButton(3).setEnabled(false);
          getButton(4).setEnabled(false);
          setVisible("btnReloadChangeNameFeeDefaultAndF01", false);
          setVisible("btnReloadO01", false);
        }
        if ("承辦".equals(retData[0][0])) {
          getButton(5).setEnabled(true);
          getButton("btnPrintRealData").setEnabled(true); // 修改日期:20121213 員工編號:B3774
          //
          if (stringGetBackReason.length() > 0) {
            blnIsGetBack = true;
            jtp.addTab("取回通知", (java.awt.Component) get("TabGetBackComponent"));
          }
        } else if ("END".equals(retData[0][0])) {
          getButton(5).setEnabled(true);
          getButton("btnPrintRealData").setEnabled(true); // 修改日期:20121213 員工編號:B3774
          // 未作廢且登入者為營業，則開放作廢按鈕
          if ("N".equals(stringIsVoid)) {
            // Start 修改日期:20161021 員工編號:B3774
            // if("035".equals(stringDeptCd)){
            if ("035".equals(stringDeptCd) || "037".equals(stringDeptCd)) {
              // End 修改日期:20161021 員工編號:B3774
              setVisible("btnOpenVoid", true);
              // Start 修改日期:20130410 員工編號:B3774
              setVisible("btnReloadSquareAndMoneyData", true);
              setVisible("btnReSaveSquareAndMoneyData", true);
              // End 修改日期:20130410 員工編號:B3774
            }
            // Start 修改日期:20161021 員工編號:B3774
            // if("033".equals(stringDeptCd)){
            if ("033".equals(stringDeptCd) || "133".equals(stringDeptCd)) {
              // End 修改日期:20161021 員工編號:B3774
              setVisible("btnOpenGetBack", true);
            }
          } else {
            blnIsVoid = true;
            jtp.addTab("作廢", (java.awt.Component) get("TabVoidComponent"));
            jtp.setForegroundAt(jtp.indexOfTab("作廢"), new Color(255, 0, 0));
            //
            if (stringGetBackReason.length() > 0) {
              blnIsGetBack = true;
              jtp.addTab("取回通知", (java.awt.Component) get("TabGetBackComponent"));
            }
          }
        } else {
          getButton(5).setEnabled(false);
          getButton("btnPrintRealData").setEnabled(false); // 修改日期:20121213 員工編號:B3774
          //
          if (stringGetBackReason.length() == 0) {
            // Start 修改日期:20161021 員工編號:B3774
            /*
             * if(("035".equals(stringDeptCd) && !("內業業管".equals(retData[0][0]) ||
             * "行銷".equals(retData[0][0]) || "行銷_出文".equals(retData[0][0]) ||
             * "營業經辦".equals(retData[0][0]))) || ("033".equals(stringDeptCd) &&
             * !("內業業管".equals(retData[0][0]) || "行銷".equals(retData[0][0])))){
             * setVisible("btnOpenGetBack", true); }
             */
            if ((("035".equals(stringDeptCd) || "037".equals(stringDeptCd))
                && !("內業業管".equals(retData[0][0]) || "行銷".equals(retData[0][0]) || "行銷_出文".equals(retData[0][0]) || "營業經辦".equals(retData[0][0])))
                || (("033".equals(stringDeptCd) || "133".equals(stringDeptCd)) && !("內業業管".equals(retData[0][0]) || "行銷".equals(retData[0][0])))) {
              setVisible("btnOpenGetBack", true);
            }
            // End 修改日期:20161021 員工編號:B3774
          } else {
            blnIsGetBack = true;
            jtp.addTab("取回通知", (java.awt.Component) get("TabGetBackComponent"));
          }
        }
      }
      //
      if (!blnIsVoid) {
        jtp.remove((java.awt.Component) get("TabVoidComponent"));
      }
      if (!blnIsGetBack) {
        jtp.remove((java.awt.Component) get("TabGetBackComponent"));
      }
      // 已送出要隱藏刪除按鈕
      stringSql = "select count(*) " + "from Sale05M274_FLOWC_HIS " + "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (Integer.parseInt(retData[0][0]) > 1) {
        getButton(4).setEnabled(false);
      }
      //
      if (intTabPreSelectedIndex <= jtp.getTabCount() - 1) {
        jtp.setSelectedIndex(intTabPreSelectedIndex);
      }
      System.out.println("test123123123");
    } else if (POSITION == 5) {
      // Start 修改日期:20140721 員工編號:B3774
      if ("成屋".equals(stringContractType)) {
        setEditable("table25", true);
        getTableButton("table25", 0).setVisible(true);
        getTableButton("table25", 2).setVisible(true);
      } else {
        setEditable("table25", false);
        getTableButton("table25", 0).setVisible(false);
        getTableButton("table25", 2).setVisible(false);
      }
      // End 修改日期:20140721 員工編號:B3774
      getTableButton("table1", 0).setVisible(false);
      getTableButton("table1", 2).setVisible(false);
      getTableButton("table2", 0).setVisible(false);
      getTableButton("table2", 2).setVisible(false);
      setVisible("text1", true);
      setVisible("tableFlowHistory", true);
      setVisible("text9", blnHas01);
      setVisible("tableFlowHistory2", blnHas01);
      getButton("btnLoadSecPositionData").doClick();
      setVisible("table11", true);
      getButton("btnLoadReName").doClick(); // 修改日期:20160519 員工編號:B3774
      //
      // Start 修改日期:20150813 員工編號:B3774
      // if("G".equals(stringStatus) || "GA".equals(stringStatus)){
      // Start 修改日期:20151120 員工編號:B3774
      // if("G".equals(stringStatus) || "GA".equals(stringStatus) ||
      // "I".equals(stringStatus)){
      if ("G".equals(stringStatus) || "GA".equals(stringStatus) || "GB".equals(stringStatus) || "I".equals(stringStatus)) {
        // End 修改日期:20151120 員工編號:B3774
        setEditable("ContractDate", false);
        // End 修改日期:20150813 員工編號:B3774
        setVisible("TransferDate", true);
      } else {
        setEditable("ContractDate", true); // 修改日期:20150813 員工編號:B3774
        // Start 修改日期:20160816 員工編號:B3774
        if ("NA".equals(stringStatus)) {
          setVisible("TransferDate", true);
        } else {
          // End 修改日期:20160816 員工編號:B3774
          setVisible("TransferDate", false);
        } // 修改日期:20160816 員工編號:B3774
      }
      //
      getButton("btnFlowEmpData").doClick();
      getButton("btnFlowSignerData").doClick();
      getButton("btnLoadDocData").doClick();
      //
      // Start 修改日期:20130410 員工編號:B3774
      if ("承辦".equals(stringState)) {
        setVisible("btnReloadSquareAndMoneyData", true);
      }
      // End 修改日期:20130410 員工編號:B3774
      // Start 修改日期:20140619 員工編號:B3774
      if ("營業經辦".equals(stringState)) {
        setVisible("btnReloadSquareAndMoneyData", true);
        setVisible("btnReSaveSquareAndMoneyData", true);
      }
      // End 修改日期:20140619 員工編號:B3774
      // 客戶代號預設值(營業經辦才帶 & 狀態是新售戶或換戶換車)
      // Start 修改日期:20151120 員工編號:B3774
      // if("N".equals(stringStatus) || "A".equals(stringStatus)){
      if ("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)) {
        // End 修改日期:20151120 員工編號:B3774
        stringSql = "select (case when isnull(T303.Agent,'')!='' then T303.Agent " + "else T225.EmployeeNo end) " + "from Sale05M225 T225 left join (select EmpNo, Agent "
            + "from Sale05M303 " + "where REPLACE(convert(char(16),getdate(),120),'-','/') " + "between StartDate+' '+StartTime and EndDate+' '+EndTime) T303 "
            + "on T225.EmployeeNo=T303.EmpNo, Sale05M228 T228 " + "where T225.FlowFormID=T228.FlowFormID " + "and T225.CensorSeq=T228.CensorSeq "
            + "and T225.FlowFormID='SALE05M27401' " + "and T225.ProjectID1='" + stringProjectID1 + "' "
            + "and convert(char(10),getdate(),111) between T225.StartDate and T225.EndDate " + "and (case when isnull(T303.Agent,'')!='' then T303.Agent "
            + "else T225.EmployeeNo end)='" + stringUser + "' " + "and T228.CensorName='營業經辦' " + "order by T225.SeqNo";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) { // 是營業經辦
          stringSql = "select TOP 1 Position " + "from Sale05M275_New " + "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "' "
              + "order by substring(Position,4,1) desc, No";
          retData = dbSale.queryFromPool(stringSql);
          talk dbFE5D = getTalk("" + get("put_dbFE5D"));
          stringSql = "SELECT OBJECT_CD " + "FROM FE5D05 " + "WHERE RTRIM(DEPT_CD)+DEPT_CD_1='" + stringDeptCdAll + "' " + "AND OBJECT_CD LIKE '" + retData[0][0] + "%' "
              + "AND STATUS_CD=''";
          retData = dbFE5D.queryFromPool(stringSql);
          setEditable("OBJECT_CD", true);
          if (retData.length > 0) {
            setValue("OBJECT_CD", retData[0][0]);
          }
        }
      }
      // 內業業管、行銷可以修改簽約日期、簽約金到期日、合約書編號及特約事項、附件內所有欄位表格
      if ("內業業管".equals(stringState) || "行銷".equals(stringState) ||
      // Start 修改日期:20151120 員工編號:B3774
      // (("N".equals(stringStatus) || "A".equals(stringStatus)) &&
      // "承辦".equals(stringState))){
          (("N".equals(stringStatus) || "NA".equals(stringStatus) || "A".equals(stringStatus)) && "承辦".equals(stringState))) {
        // End 修改日期:20151120 員工編號:B3774
        // Start 修改日期:20151123 員工編號:B3774
        if ("NA".equals(stringStatus)) {
          setEditable("ContractType", false);
        } else {
          // End 修改日期:20151123 員工編號:B3774
          setEditable("ContractType", true); // 修改日期:20140718 員工編號:B3774
        } // 修改日期:20151123 員工編號:B3774
        setEditable("ContractDate", true);
        setEditable("CashInDate", true);
        setEditable("ContractSerialNo", true);
        //
        // Start 修改日期:20161103 員工編號:B3774
        setEditable("table26", "FriendID", true);
        setEditable("table26", "FriendName", true);
        setEditable("table26", "CommMoney", true);
        setEditable("table26", "CommMoney1", true);
        // End 修改日期:20161103 員工編號:B3774
        //
        // Start 修改日期:20161017 員工編號:B3774
        for (int intRow = 0; intRow < jtable26.getRowCount(); intRow++) {
          setEditable("table26", intRow, "FriendID", true);
          setEditable("table26", intRow, "FriendName", true);
          setEditable("table26", intRow, "CommMoney", true);
          setEditable("table26", intRow, "CommMoney1", true);
        }
        // End 修改日期:20161017 員工編號:B3774
        //
        for (int intRow = 0; intRow < jtable8.getRowCount(); intRow++) {
          setEditable("table8", intRow, "DocNo1", true);
          setEditable("table8", intRow, "DocNo2", true);
          setEditable("table8", intRow, "DocNo3", true);
        }
        //
        setEditable("table5", true);
        setEditable("SOther", true);
        setEditable("ChangeNameFee", true);
        setEditable("table13", true);
        setEditable("OrderType", true);
        setEditable("CmpChangeType", true);
        setEditable("IDType", true);
        setEditable("PassportType", true);
        setEditable("ResidencePermitType", true);
        setEditable("MoneyScaleType", true);
        setEditable("DealType", true);
        setEditable("AttachmentsRemark", true);
        setEditable("table6", true);
        setVisible("btnReloadSalesData", true);
        setVisible("btnReloadPositionData", true);
        setVisible("btnReloadCustomData", true);
        setVisible("btnReloadRealData", true); // 修改日期:20121211 員工編號:B3774
        setVisible("btnReLoadCallMoney", true);
        setVisible("btnReloadGiftData", true);
      }
      //
      if ("內業業管".equals(stringState) || "行銷".equals(stringState)) {
        setVisible("btnReloadChangeNameFeeDefaultAndF01", true);
        setVisible("btnReloadO01", true);
      }
      //
      if (stringGetBackReason.length() > 0) {
        // Start 修改日期:20161021 員工編號:B3774
        // if(!(("營業經辦".equals(stringState) && "035".equals(stringNotifyUserDeptCd)) ||
        // ("承辦".equals(stringState) && "033".equals(stringNotifyUserDeptCd)))){
        if (!(("營業經辦".equals(stringState) && ("035".equals(stringNotifyUserDeptCd) || "037".equals(stringNotifyUserDeptCd)))
            || ("承辦".equals(stringState) && ("033".equals(stringNotifyUserDeptCd) || "133".equals(stringNotifyUserDeptCd))))) {
          // End 修改日期:20161021 員工編號:B3774
          Sale.Sale05M22701 exeFun = new Sale.Sale05M22701();
          String stringNotifyUserName = exeFun.getEmpName(stringNotifyUser);
          String stringGetBackMSG = "";
          //
          if (stringNotifyUserName.length() > 0) {
            stringNotifyUserName = stringNotifyUser + " " + stringNotifyUserName;
          }
          //
          // Start 修改日期:20161021 員工編號:B3774
          // if("035".equals(stringNotifyUserDeptCd)){
          if ("035".equals(stringNotifyUserDeptCd) || "037".equals(stringNotifyUserDeptCd)) {
            // End 修改日期:20161021 員工編號:B3774
            stringGetBackMSG = "該筆合約營業取回：\n";
          } else {
            stringGetBackMSG = "該筆合約行銷取回：\n";
          }
          stringGetBackMSG = stringGetBackMSG + "取回原因：" + stringGetBackReasonDisplay + "\n";
          if ("6".equals(stringGetBackReason)) {
            stringGetBackMSG = stringGetBackMSG + "備註：" + stringGetBackRemark + "\n";
          }
          stringGetBackMSG = stringGetBackMSG + "取回人：" + stringNotifyUserName;
          messagebox(stringGetBackMSG);
        }
      }
      //
      jtp.setSelectedIndex(0);
      if (jtp.indexOfTab("作廢") != -1) {
        put("TabVoidComponent_Flow", jtp.getComponentAt(jtp.indexOfTab("作廢")));
      }
      if (jtp.indexOfTab("作廢 ") != -1) {
        put("TabVoidComponent_Flow", jtp.getComponentAt(jtp.indexOfTab("作廢 ")));
      }
      if (jtp.indexOfTab("取回通知") != -1) {
        put("TabGetBackComponent_Flow", jtp.getComponentAt(jtp.indexOfTab("取回通知")));
      }
      if (jtp.indexOfTab("作廢") != -1) {
        jtp.remove(jtp.indexOfTab("作廢"));
      }
      if (jtp.indexOfTab("取回通知") != -1) {
        jtp.remove(jtp.indexOfTab("取回通知"));
      }
      if (jtp.indexOfTab("作廢 ") != -1) {
        jtp.remove(jtp.indexOfTab("作廢 "));
      }
      //
      // Start 修改日期:20161021 員工編號:B3774
      // if(("營業經辦".equals(stringState) && !"033".equals(stringNotifyUserDeptCd)) ||
      if (("營業經辦".equals(stringState) && !("033".equals(stringNotifyUserDeptCd) || "133".equals(stringNotifyUserDeptCd))) ||
      // End 修改日期:20161021 員工編號:B3774
          "承辦".equals(stringState) || "行銷".equals(stringState)) {
        jtp.addTab("作廢 ", (java.awt.Component) get("TabVoidComponent_Flow"));
        jtp.setForegroundAt(jtp.indexOfTab("作廢 "), new Color(255, 0, 0));
        //
        if (stringGetBackReason.length() > 0) {
          jtp.setSelectedIndex(jtp.indexOfTab("作廢 "));
        }
        //
        setEditable("VoidDate", true);
        setEditable("VoidReason", true);
        setEditable("VoidRemark", true);
        setEditable("VoidFileName", true); // 修改日期:20151124 員工編號:B3774
        //
        if (stringGetBackReason.length() > 0) {
          setValue("VoidDate", getToday("YYYY/mm/dd"));
          setValue("VoidReason", stringGetBackReason);
        }
      }
    }
    // 隱藏表格修改者、修改時間欄位
    Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil();
    exeUtil.doChangeTableField(getTable("table1"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table1"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table18"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table18"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table2"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table2"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table3"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table3"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table4"), "合約坪數", 0);
    exeUtil.doChangeTableField(getTable("table4"), "計價坪數", 0);
    exeUtil.doChangeTableField(getTable("table4"), "贈送坪數", 0);
    exeUtil.doChangeTableField(getTable("table4"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table4"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table20"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table20"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table14"), "公司1", 0);
    exeUtil.doChangeTableField(getTable("table14"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table14"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table17"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table17"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table15"), "公司1", 0);
    // Start 修改日期:20161017 員工編號:B3774
    exeUtil.doChangeTableField(getTable("table26"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table26"), "LastDateTime", 0);
    // End 修改日期:20161017 員工編號:B3774
    // Start 修改日期:20121213 員工編號:B3774
    exeUtil.doChangeTableField(getTable("table21"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table21"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table22"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table22"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table23"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table23"), "LastDateTime", 0);
    // End 修改日期:20121213 員工編號:B3774
    exeUtil.doChangeTableField(getTable("table10"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table10"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table7"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table7"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table8"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table8"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table5"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table5"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table13"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table13"), "LastDateTime", 0);
    exeUtil.doChangeTableField(getTable("table6"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table6"), "LastDateTime", 0);
    // Start 修改日期:20140718 員工編號:B3774
    exeUtil.doChangeTableField(getTable("table25"), "QueryUse", 0);
    exeUtil.doChangeTableField(getTable("table25"), "LastUser", 0);
    exeUtil.doChangeTableField(getTable("table25"), "LastDateTime", 0);
    // End 修改日期:20140718 員工編號:B3774
    // 帶出棟樓房土資料-依公司別
    getButton("btnLoadPositionByComData").doClick();
    // 帶出法令遵循
    getButton("btnLoadCompliance").doClick();
    //
    // Start 修改日期:20161021 員工編號:B3774
    // if("035".equals(stringDeptCd)){
    if ("035".equals(stringDeptCd) || "037".equals(stringDeptCd)) {
      // End 修改日期:20161021 員工編號:B3774
      setEditable("OBJECT_CD", true);
    } else {
      setEditable("OBJECT_CD", false);
    }
    // 依人員帶出合約編號及公司別
    if (POSITION == 4 || POSITION == 5) {
      stringSql = "select * " + "from USERS_DEPT, USERS_DEPT_BAS " + "where USERS_DEPT.DEP_NO=USERS_DEPT_BAS.DEP_NO " + "and USERS_DEPT_BAS.DEP_NO='1007' "
          + "and USERS_DEPT_BAS.DEP_NAME='人壽人員' " + "and USERS_DEPT.ID='" + stringUser + "'";
      retData = dbSale.queryFromPool(stringSql);
      if (retData.length > 0) { // 人壽人員
        stringSql = "select CmpCompanyCd, " + "CmpContractNo " + "from Sale05M294 " + "where ContractNo='" + stringContractNo + "' " + "and CompanyCd='" + stringCompanyCd + "' "
            + "and CmpCompanyCd='CS'";
        retData = dbSale.queryFromPool(stringSql);
        if (retData.length > 0) {
          setValue("CompanyCdDisplay", retData[0][0]);
          setValue("ContractNoDisplay", retData[0][1]);
        } else {
          setValue("CompanyCdDisplay", "");
          setValue("ContractNoDisplay", "");
        }
      } else {
        setValue("CompanyCdDisplay", stringCompanyCd);
        setValue("ContractNoDisplay", stringContractNo);
      }
      //
      setValue("ContractNoDB", stringContractNo);
      setValue("CompanyCdDB", stringCompanyCd);
      setValue("ContractDateDB", stringContractDate);
      setValue("TransferDateDB", stringTransferDate); // 修改日期:20150811 員工編號:B3774
      //

      /*
       * if("CS".equals(stringUser.substring(0,2))){ // Start 修改日期:20160526 員工編號:B3774
       * //if("H56A".equals(stringProjectID1) || "H73A".equals(stringProjectID1) ||
       * "H85A".equals(stringProjectID1) || "H106A".equals(stringProjectID1)){
       * if("H56A".equals(stringProjectID1) || "H73A".equals(stringProjectID1) ||
       * "H85A".equals(stringProjectID1) || "H93A".equals(stringProjectID1) ||
       * "H106A".equals(stringProjectID1)){ // End 修改日期:20160526 員工編號:B3774
       * getButton(8).setVisible(false); setVisible("text1", false);
       * setVisible("tableFlowHistory", false); setVisible("text9", false);
       * setVisible("tableFlowHistory2", false); // Start 修改日期:20120926 員工編號:B3774
       * //}else if("H91A".equals(stringProjectID1)){ // Start 修改日期:20130319
       * 員工編號:B3774 //}else if("H91A".equals(stringProjectID1) ||
       * "H116A".equals(stringProjectID1)){ // Start 修改日期:20170802 員工編號:B3774 //}else
       * if("H91A".equals(stringProjectID1) || "H110A".equals(stringProjectID1) ||
       * "H116A".equals(stringProjectID1)){ }else if("H91A".equals(stringProjectID1)
       * || "H110A".equals(stringProjectID1) || "H111A".equals(stringProjectID1) ||
       * "H116A".equals(stringProjectID1)){ // End 修改日期:20170802 員工編號:B3774 // End
       * 修改日期:20130319 員工編號:B3774 // End 修改日期:20120926 員工編號:B3774 stringSql =
       * "SELECT OrderDate "+ "FROM Sale05M090 "+ "WHERE OrderNo IN (SELECT OrderNo "+
       * "FROM Sale05M275_New "+ "WHERE ContractNo='"+stringContractNo+"' "+
       * "AND CompanyCd='"+stringCompanyCd+"') "+ "AND OrderDate>='2012/02/16'";
       * retData = dbSale.queryFromPool(stringSql); if(retData.length > 0){
       * getButton(8).setVisible(false); setVisible("text1", false);
       * setVisible("tableFlowHistory", false); setVisible("text9", false);
       * setVisible("tableFlowHistory2", false); }else{ getButton(8).setVisible(true);
       * } }else{ getButton(8).setVisible(true); } }else{
       * getButton(8).setVisible(true); }
       */
      //
      // Start 修改日期:20160329 員工編號:B3774
      // Start 修改日期:20160519 員工編號:B3774
      // stringSql = "SELECT '統編/身分證號:'+T1.CustomNo+'，已改名為:'+T1.ReName "+
      stringSql = "SELECT '統編/身分證號:'+T1.CustomNo+'，已改名為:'+T1.ReName+'，原名：'+T2.CustomName+'，改名日期：'+T1.ReNameDate " +
      // End 修改日期:20160519 員工編號:B3774
          "FROM Sale05M351 T1, Sale05M352 T2, Sale05M277 T7 " + "WHERE T1.ProjectID1=T2.ProjectID1 " + "AND T1.CustomNo=T2.CustomNo " + "AND T1.ReNameDate=T2.ReNameDate "
          + "AND T1.CustomNo=T7.CustomNo " +
          // "AND T1.ReName=T7.CustomName "+ // 修改日期:20160519 員工編號:B3774
          "AND T1.ProjectID1='" + stringProjectID1 + "' " + "AND T7.ContractNo='" + stringContractNo + "' " +
          // Start 修改日期:20160519 員工編號:B3774
          // "AND T7.CompanyCd='"+stringCompanyCd+"'";
          "AND T7.CompanyCd='" + stringCompanyCd + "' " + "ORDER BY T1.ReNameDate DESC, T1.CustomNo";
      // End 修改日期:20160519 員工編號:B3774
      retData = dbSale.queryFromPool(stringSql);
      // Start 修改日期:20160519 員工編號:B3774
      /*
       * if(retData.length > 0){ messagebox(retData[0][0]); }
       */
      for (int intRow = 0; intRow < retData.length; intRow++) {
        stringMSG = stringMSG.length() == 0 ? retData[intRow][0] : stringMSG + "\n" + retData[intRow][0];
      }
      if (stringMSG.length() > 0) {
        messagebox(stringMSG);
      }
      // End 修改日期:20160519 員工編號:B3774
      // End 修改日期:20160329 員工編號:B3774
    }
    // Start 修改日期:20140114 員工編號:B3774
    /// * // 修改日期:20140912 員工編號:B3774
    // Start 修改日期:20130315 員工編號:B3774
    JTabbedPane jtp2 = getTabbedPane("tab2");
    //
    // jtp2.setEnabledAt(5, false);//法令
    jtp2.setEnabledAt(6, false);
    //
    setVisible("btnPrintRealData", false);
    // End 修改日期:20130315 員工編號:B3774
    // */ // 修改日期:20140912 員工編號:B3774
    // End 修改日期:20140114 員工編號:B3774
    /* ******************************************* */
    if ("B3774".equals(stringUser)) {
      // getButton(3).setEnabled(true);
      // getButton(4).setEnabled(true);
      getButton(5).setEnabled(true);
      getButton("btnPrintRealData").setEnabled(true); // 修改日期:20121213 員工編號:B3774
      setVisible("IsReloadSalesData", true);
      setVisible("DocStatusForList", true);
      setVisible("ContractDate_Old", true);
      setVisible("ContractNo", true);
      setVisible("ContractNoDB", true);
      setVisible("CompanyCd", true);
      setVisible("CompanyCdDB", true);
      setVisible("ContractDateDB", true);
      setEditable("Status", true);
      setEditable("ContractDate", true);
      setVisible("CustomName", true);
      jtp2.setEnabledAt(5, true); // 修改日期:20130315 員工編號:B3774 // 修改日期:20140114 員工編號:B3774 // 修改日期:20140912
                                  // 員工編號:B3774
      setVisible("table9", true);
      setVisible("table16", true);
      setVisible("table17", true);
      setVisible("table19", true);
      setVisible("btnLoadNewContractNo", true);
      setVisible("LoanMoney", true);
      setVisible("btnReloadSalesData", true);
      setVisible("btnReloadPositionData", true);
      setVisible("btnReloadCustomData", true);
      setVisible("btnReloadRealData", true); // 修改日期:20121211 員工編號:B3774
      setVisible("btnReloadSquareAndMoneyData", true);
      setVisible("btnReSaveSquareAndMoneyData", true);
      setVisible("btnReLoadCallMoney", true);
      setVisible("btnReloadGiftData", true);
      setVisible("btnReloadChangeNameFeeDefaultAndF01", true);
      setVisible("btnReloadO01", true);
      setVisible("btnLoadSecPositionData", true);
      setVisible("btnLoadReName", true);
      setVisible("btnLoadPositionByComData", true);
      setVisible("btnOrderCount", true);
      setVisible("btnClearData", true);
      setVisible("btnLoadCustomerPositionData", true);
      setVisible("btnPrintRealData", true); // 修改日期:20130315 員工編號:B3774 // 修改日期:20140114 員工編號:B3774 // 修改日期:20140912
                                            // 員工編號:B3774
      // Start 修改日期:20150811 員工編號:B3774
      setVisible("TransferDate_Old", true);
      setVisible("TransferDateDB", true);
      setEditable("TransferDate", true);
      // End 修改日期:20150811 員工編號:B3774
      //
      Vector vectorValue = new Vector();
      Vector vectorDisplay = new Vector();
      vectorValue.add("N");
      vectorValue.add("NA"); // 修改日期:20151120 員工編號:B3774
      vectorValue.add("A");
      vectorValue.add("G");
      vectorValue.add("GA");
      vectorValue.add("GB"); // 修改日期:20151120 員工編號:B3774
      vectorValue.add("C");
      vectorValue.add("CA");
      vectorValue.add("I");
      vectorDisplay.add("新售戶");
      // Start 修改日期:20160816 員工編號:B3774
      // vectorDisplay.add("新售戶-指定過戶");
      vectorDisplay.add("簽約後營業指定過戶");
      // End 修改日期:20160816 員工編號:B3774
      vectorDisplay.add("換戶換車");
      vectorDisplay.add("換名(讓與協議)");
      vectorDisplay.add("換名(房地產仲介讓與)");
      vectorDisplay.add("營業指定過戶異動");
      vectorDisplay.add("換名(換新契約)");
      vectorDisplay.add("換名(房地產仲介換約)");
      vectorDisplay.add("繼承");
      setReference("Status", vectorDisplay, vectorValue);
    } else if ("B1869".equals(stringUser)) {
      setVisible("btnReloadSalesData", true);
      setVisible("btnReloadPositionData", true);
      setVisible("btnReloadCustomData", true);
      setVisible("btnReloadRealData", true); // 修改日期:20121211 員工編號:B3774
      setVisible("btnReloadSquareAndMoneyData", true);
      setVisible("btnReSaveSquareAndMoneyData", true);
      setVisible("btnReLoadCallMoney", true);
      setVisible("btnReloadGiftData", true);
      setVisible("btnReloadChangeNameFeeDefaultAndF01", true);
      setVisible("btnReloadO01", true);
    }
    /* ******************************************* */
    //
    getButton("btnReSizeFileNameField").doClick();
    //
    JToolBar jtb = getToolBar();
    if (jtb != null) {
      for (int i = 0; i < jtb.getComponentCount(); i++) {
        Component cp1 = jtb.getComponent(i);
        if (cp1 instanceof Container) {
          Container ct1 = (Container) cp1;
          for (int j = 0; j < ct1.getComponentCount(); j++) {
            Component cp2 = ct1.getComponent(j);
            if (cp2 instanceof Container) {
              Container ct2 = (Container) cp2;
              Component cp3 = ct2.getComponent(0);
              if (cp3 instanceof JButton) {
                JButton jb = (JButton) cp3;
                // Start 修改日期:20121213 員工編號:B3774
                // if("作廢".equals(jb.getText())){
                if ("列印實價登錄資料檢核表".equals(jb.getText())) {
                  jtb.setComponentZOrder(cp1, 6);
                  jtb.updateUI();
                } else if ("作廢".equals(jb.getText())) {
                  // End 修改日期:20121213 員工編號:B3774
                  jtb.setComponentZOrder(cp1, 7);
                  jtb.updateUI();
                } else if ("取回通知".equals(jb.getText())) {
                  jtb.setComponentZOrder(cp1, 8);
                  jtb.updateUI();
                }
              }
            }
          }
        }
      }
      for (int i = 0; i < jtb.getComponentCount(); i++) {
        Component cp1 = jtb.getComponent(i);
        if (cp1 instanceof JButton) {
          JButton jbcheck = (JButton) cp1;
          System.out.println(i + "==>ch" + jbcheck.getText().trim());
          if (("流程紀錄").equals(jbcheck.getText().trim())) {
            getButton(8).setVisible(false);
            // cp1.setVisible(false);
            jtb.updateUI();
          }
        }
      }
    }
    // 201808check 流程紀錄不顯示
    setVisible("text1", false);
    setVisible("tableFlowHistory", false);
    return value;
  }

  public String getInformation() {
    return "---------------FormLoad().defaultValue()----------------";
  }
}
