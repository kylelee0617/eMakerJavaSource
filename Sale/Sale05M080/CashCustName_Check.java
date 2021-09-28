package Sale.Sale05M080;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bvalidate;

public class CashCustName_Check extends bvalidate{
  public boolean check(String value)throws Throwable{

    // �ŭ��ˮ�
    if (StringUtils.isBlank(value)) return false;

    KSqlUtils ksUtil = new KSqlUtils();
    String projectID = getValue("field2").trim();

    // ���o�¦W��
    QueryLogBean qBean = ksUtil.getQueryLogByName(projectID, value);
    if (qBean == null || StringUtils.isBlank(qBean.getRealId(value))) {
      message("�Nú�H: " + value + " �d�L��ơA�Х�����¦W��d�ߡC");
      return false;
    }

    String errMsg = "";
    String funcName = getFunctionName().trim();
    String recordType = "�{���Nú�ڤH���";
    
    String name = value;
    String custNo = qBean.getRealId(name);
    String birthday = qBean.getBirthday().replace("/", "-");
    String indCode = qBean.getJobType();
    String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String engName = qBean.getEngName();
    String bStatus = qBean.getbStatus();
    String cStatus = qBean.getcStatus();
    String rStatus = qBean.getrStatus();
    String processType = "query1821";

    setValue("DeputyID", custNo);
    setValue("B_STATUS", bStatus);
    setValue("C_STATUS", cStatus);
    setValue("R_STATUS", rStatus);

    // ���ӥu�|���@�ӭq��A�u���J���ӦA�令�h�q��A�ѦҫH�Υd
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + custNo + "," + name + "," + birthday + "," + indCode + "," + countryName
        + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    // 19. �Q�`���Y�H
    if ("Y".equals(rStatus)) {
      errMsg += "�Nú�ڤH" + name + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
    }

    // 20. �¦W��
    // �P���ަW��X��

    // 17. ���ަW��
    if ("Y".equals(bStatus) || "Y".equals(cStatus)) {
      errMsg += "�Nú�ڤH" + name + "���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
    }

    if (!"".equals(errMsg)) {
      messagebox(errMsg);
    }
    
     return true;
  }
  public String getInformation(){
    return "---------------DeputyName( , \u4ee3\u7e73\u4eba).field_check()----------------";
  }
}