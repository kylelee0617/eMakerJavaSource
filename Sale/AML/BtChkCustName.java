package Sale.AML;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bproc;

public class BtChkCustName extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    String payment_custName = get("payment_custName").toString().trim();
    String payment_caller = get("payment_caller").toString().trim();
    KSqlUtils ksUtil = new KSqlUtils();

    String tableName = "";
    String funcName = getFunctionName().trim();
    String recordType = "###";
    String processType = "query1821";
    String projectID = getValue("field2").trim();

    if (StringUtils.equals(payment_caller, "credit")) {
      tableName = "table5";
      recordType = "�H�Υd�Nú�ڤH���";
    } else if (StringUtils.equals(payment_caller, "cash")) {
      tableName = "";
      recordType = "�{���Nú�ڤH���";
    } else if (StringUtils.equals(payment_caller, "bank")) {
      tableName = "table9";
      recordType = "�Ȧ�Nú�ڤH���";
    } else if (StringUtils.equals(payment_caller, "check")) {
      tableName = "table2";
      recordType = "���ڥNú�ڤH���";
    } else {
      return value;
    }

    if (StringUtils.isBlank(payment_custName)) return value;

    // ���o�¦W��
    QueryLogBean qBean = ksUtil.getQueryLogByName(projectID, payment_custName);
    if ( qBean == null || StringUtils.isBlank(qBean.getRealId(payment_custName)) ) {
      messagebox("�Nú�ڤH: " + payment_custName + " �d�L��ơA�Х�����¦W��d�ߡC");
      return value;
    }

    String errMsg = "";

    String custNo = qBean.getRealId(payment_custName);
    String birthday = qBean.getBirthday().replace("/", "-");
    String indCode = qBean.getJobType();
    String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String engName = qBean.getEngName();
    String bStatus = qBean.getbStatus();
    String cStatus = qBean.getcStatus();
    String rStatus = qBean.getrStatus();

    JTable jtb = getTable(tableName);
    int s_row = jtb.getSelectedRow();
    setValueAt(tableName, custNo, s_row, "DeputyID");
    setValueAt(tableName, "Y", s_row, "PaymentDeputy");
    setValueAt(tableName, bStatus, s_row, "B_STATUS");
    setValueAt(tableName, cStatus, s_row, "C_STATUS");
    setValueAt(tableName, rStatus, s_row, "R_STATUS");

    // ���ӥu�|���@�ӭq��A�u���J���ӦA��
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + custNo + "," + payment_custName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    // 19. �Q�`���Y�H
    if ("Y".equals(rStatus)) {
      errMsg += "�Nú�ڤH" + payment_custName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
    }

    // 20. �¦W��
    // �P���ަW��X��

    // 17. ���ަW��
    if ("Y".equals(bStatus) || "Y".equals(cStatus)) {
      errMsg += "�Nú�ڤH" + payment_custName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
    }

    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    return value;
  }

  public String getInformation() {
    return "---------------BtChkCustNo(\u6536\u6b3eChkCustNo).defaultValue()----------------";
  }
}
