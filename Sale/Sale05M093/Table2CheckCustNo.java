package Sale.Sale05M093;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.db.talk;
import jcx.jform.bvalidate;
import jcx.util.check;

public class Table2CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();
    String custTable = "table2";

    JTable tb2 = getTable(custTable);
    int sRow = tb2.getSelectedRow();
    int sColumn = tb2.getSelectedColumn();

    String custNo = "";
    String engNo = "";
    String projectId = getValue("ProjectID1").trim();
    String orderNo = getValue("OrderNo");
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String countryName = getValueAt(custTable, sRow, "CountryName").toString();
    String countryName2 = getValueAt(custTable, sRow, "CountryName2").toString();
    String columnName = tb2.getColumnName(sColumn); // �ˮ���쪺�W��
    System.out.println(">>columnName:" + columnName);

    if (sRow == -1) return true;

    // ���ˮ�
    if ("".equals(projectId)) {
      message("[�קO�N�X] ���i�ť�!");
      return false;
    }
    if (value.length() == 0) {
      message(columnName + " �ˮֿ��~ : �ť�!");
      return false;
    }
    if (value.length() != value.trim().length()) {
      message(columnName + " �ˮֿ��~ : �Ů�!");
      return false;
    }

    // ���ˮֲνs/�����Ҹ���� �� ID���T��
    if (StringUtils.equals(columnName, "�νs/�����Ҹ�")) {
      if (StringUtils.isBlank(countryName)) {
        messagebox("(����)���y������");
        return false;
      }

      // ����H�ˮ֨����Ҹ�
      if (StringUtils.equals(countryName, "���إ���")) { // ����H
        setValueAt(custTable, "1", sRow, "Nationality");

        if (value.length() != 8 && value.length() != 10) {
          messagebox("[�νs/�����Ҹ�] ���׿��~!");
          return false;
        } else if (value.length() == 8 && check.isCoId(value) == false) {
          messagebox("[�νs/�����Ҹ�] �Τ@�s�����~!");
          return false;
        } else if (value.length() == 10 && check.isID(value) == false) {
          messagebox("[�νs/�����Ҹ�] �����Ҹ����~!");
          return false;
        }
      }

      // ��ID��
      custNo = value.trim();
      engNo = getValueAt(custTable, sRow, "EngNo").toString();
    } else if (StringUtils.equals(columnName, "�@���ҥ�")) {
      if (StringUtils.isBlank(countryName2)) {
        messagebox("�~����y������");
        return false;
      }

      // ��ID
      custNo = getValueAt(custTable, sRow, "CustomNo").toString();
      engNo = value.trim();
    }
    System.out.println(">>>custNO:" + custNo);
    System.out.println(">>>engNo:" + engNo);

    // ���¦W����
    String custNo3 = kUtil.getCustNo3(custNo, engNo);
    QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectId, custNo3);
    if (qBean == null) {
      messagebox("�L���H��T�A�Х�����¦W��d�ߡC");
      return false;
    }

    String tmpMsg = "";
    String errMsg = "";
    String bstatus = qBean.getbStatus();
    String cstatus = qBean.getcStatus();
    String rstatus = qBean.getrStatus();
    String qName = qBean.getName();
    String birthday = qBean.getBirthday();
    countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String[] cityTownZip = ksUtil.getCityTownZipName(qBean.getCity(), qBean.getTown());
    String engName = qBean.getEngName();
    String addr = qBean.getAddress();
    String funcName = getFunctionName().trim();
    String recordType = "�Ȥ���";
    String indCode = qBean.getJobType();
    String processType = "query1821";

    setValueAt(custTable, StringUtils.equals(countryName, "���إ���") ? "1" : "2", sRow, "Nationality"); // �°��y�A���O����H�Υ~��H(��W: ���~��H)
    setValueAt(custTable, qName, sRow, "CustomName");
    setValueAt(custTable, engName, sRow, "EngName");
    setValueAt(custTable, qBean.getEngNo(), sRow, "EngNo");
    setValueAt(custTable, birthday, sRow, "Birthday");
    setValueAt(custTable, cityTownZip[0], sRow, "ZIP");
    setValueAt(custTable, cityTownZip[1], sRow, "City");
    setValueAt(custTable, cityTownZip[2], sRow, "Town");
    setValueAt(custTable, addr, sRow, "Address");
    setValueAt(custTable, countryName, sRow, "CountryName"); // ���y
    setValueAt(custTable, countryName2, sRow, "CountryName2"); // ���y2
//    setValueAt(custTable, indCode, sRow, "IndustryCode"); // ��~�O�N�X
    setValueAt(custTable, ksUtil.getNameByIndCode(indCode), sRow, "MajorName"); // ��~�O
    setValueAt(custTable, bstatus, sRow, "IsBlackList");
    setValueAt(custTable, cstatus, sRow, "IsControlList");
    setValueAt(custTable, rstatus, sRow, "IsLinked");

    // �ܴ�Start
    String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + qName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    tmpMsg = getValue("AMLText").trim();
    errMsg += tmpMsg;
    // �ܴ�END

    // �¦W�� + ���ަW��
    if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
      tmpMsg = "�Ȥ�" + qName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
      errMsg += tmpMsg;
    }

    // �Q���H
    if ("Y".equals(rstatus)) {
      tmpMsg += "�Ȥ�" + qName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
      errMsg += tmpMsg;
    }

    // ���
    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    return true;
  }

  public String getInformation() {
    return "---------------null(null).CustomNoNew.field_check()----------------";
  }
}
