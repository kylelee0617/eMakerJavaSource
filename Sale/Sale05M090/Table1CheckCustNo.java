package Sale.Sale05M090;

import javax.swing.JButton;
import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;
import jcx.util.check;

/**
 * table1 - �����Ҧr���ˮ�
 * 
 * @author B04391
 *
 */

public class Table1CheckCustNo extends bvalidate {
  public boolean check(String value) throws Throwable {
    System.out.println("getFunctionName>>>" + getFunctionName());
    System.out.println("POSITION>>>" + POSITION);

//    value = value.trim();
    KSqlUtils kSqlUtil = new KSqlUtils();
    KUtils kUtil = new KUtils();

    JTable tb1 = getTable("table1");
    int sRow = tb1.getSelectedRow();
    int sColumn = tb1.getSelectedColumn();
    boolean isCust = true;

    String custNo = "";
    String engNo = "";
    String projectId = getValue("field1").trim();
    String orderNo = getValue("field3").trim();
    String orderDate = getValue("field2").trim();
    String modCustFlag = getValue("CustomID_NAME_PER_Editable").toString().trim();
    String countryName = getValueAt("table1", sRow, "CountryName").toString();
    String countryName2 = getValueAt("table1", sRow, "CountryName2").toString();
    String columnName = tb1.getColumnName(sColumn); // �ˮ���쪺�W��
    System.out.println(">>columnName:" + columnName);

    if (POSITION == 4 && "0".equals(modCustFlag)) return false;

    if (sRow == -1) return false;

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
        setValueAt("table1", "1", sRow, "Nationality");

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

      // �����ID
//      custNo = value.trim();
//      engNo = getValueAt("table1", sRow, "EngNo").toString();
    } else if (StringUtils.equals(columnName, "�@���ҥ�")) {
      if (StringUtils.isBlank(countryName2)) {
        messagebox("�~����y������");
        return false;
      }
      
      isCust = false;

      // �����ID
//      custNo = getValueAt("table1", sRow, "CustomNo").toString();
//      engNo = value.trim();
    }

    // ���¦W����
    String custNo3 = kUtil.getCustNo3(custNo, engNo);
    QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo3);
    if (qBean == null) {
      messagebox("�L���H��T�A�Х�����¦W��d�ߡC");
      return false;
    }

    String tmpMsg = "";
    String errMsg = "";
    String bstatus = qBean.getbStatus();
    String cstatus = qBean.getcStatus();
    String rstatus = qBean.getrStatus();
    String custName = qBean.getName();
    String birthday = qBean.getBirthday();
    String indCode = qBean.getJobType();
    custNo = qBean.getQueryId();
    engNo = qBean.getEngNo();
    countryName = kSqlUtil.getCountryNameByNationCode(qBean.getNtCode());
    countryName2 = kSqlUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String[] cityTownZip = kSqlUtil.getCityTownZipName(qBean.getCity(), qBean.getTown());
    String engName = qBean.getEngName();
    String addr = qBean.getAddress();
    String funcName = getFunctionName().trim();
    String recordType = "�Ȥ���";
    setValueAt("table1", "A", sRow, "auditorship"); // �����A���O�@��(�w��ΡA���ä�)
    setValueAt("table1", StringUtils.equals(countryName, "���إ���") ? "1" : "2", sRow, "Nationality"); // �°��y�A���O����H�Υ~��H(��W: ���~��H)
    setValueAt("table1", custName, sRow, "CustomName");
    setValueAt("table1", engName, sRow, "EngName");
    
    //�g�J�t�@��id
    if(isCust) setValueAt("table1", engNo, sRow, "EngNo");
    if(!isCust) setValueAt("table1", custNo, sRow, "CustomNo");
    
    setValueAt("table1", birthday, sRow, "Birthday");
    setValueAt("table1", cityTownZip[0], sRow, "ZIP");
    setValueAt("table1", cityTownZip[1], sRow, "City");
    setValueAt("table1", cityTownZip[2], sRow, "Town");
    setValueAt("table1", addr, sRow, "Address");
    setValueAt("table1", countryName, sRow, "CountryName"); // ���y
    setValueAt("table1", countryName2, sRow, "CountryName2"); // ���y2
    setValueAt("table1", indCode, sRow, "IndustryCode"); // ��~�O�N�X
    setValueAt("table1", kSqlUtil.getNameByIndCode(indCode), sRow, "MajorName"); // ��~�O
    setValueAt("table1", bstatus, sRow, "IsBlackList");
    setValueAt("table1", cstatus, sRow, "IsControlList");
    setValueAt("table1", rstatus, sRow, "IsLinked");

    // �ܴ�Start
    String amlText = projectId + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + custName + "," + birthday + "," + indCode + ","
        + countryName + "," + countryName2 + "," + engName + "," + "query1821";
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    tmpMsg = getValue("AMLText").trim();
    errMsg += tmpMsg;
    // �ܴ�END

    // �¦W�� + ���ަW��
    if ("Y".equals(bstatus) || "Y".equals(cstatus)) {
      tmpMsg = "�Ȥ�" + custName + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
      errMsg += tmpMsg;
    }

    // �Q���H
    if ("Y".equals(rstatus)) {
      tmpMsg += "�Ȥ�" + custName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
      errMsg += tmpMsg;
    }

    // ���
    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }

    // �Y�u���@���A����100%
    if (tb1.getRowCount() == 1) setValueAt("table1", "100", sRow, "Percentage");

    // �w�f
    JButton buyedInfo = getButton("BuyedInfo");
    buyedInfo.setText("userCusNo=" + value);
    buyedInfo.doClick();

    return true;
  }

  public String getInformation() {
    return "---------------null(null).CustomNo.field_check()----------------";
  }
}
