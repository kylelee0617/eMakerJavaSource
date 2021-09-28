package Sale.Sale05M080;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.bvalidate;

public class CreditCustNo_Check extends bvalidate {
  public boolean check(String value) throws Throwable {
    String tableName = "table5";

    if (StringUtils.isBlank(value)) return false;

//    talk dbSale = getTalk("Sale");
//    talk dbpw0d = getTalk("pw0d");
//    TalkBean tBean = new TalkBean();
//    tBean.setDbSale(dbSale);
//    tBean.setDbPw0D(dbpw0d);
    KSqlUtils ksUtil = new KSqlUtils();
    String projectID = getValue("field2").trim();

    // ���o�¦W��
    QueryLogBean qBean = ksUtil.getQueryLogByCustNoProjectId(projectID, value);
    if (qBean == null) {
      message("ID: " + value + " �d�L��ơA�Х�����¦W��d�ߡC");
      return false;
    }

    String errMsg = "";
    String funcName = getFunctionName().trim();
    String recordType = "�H�Υd�Nú�ڤH���";
    String name = qBean.getName();
    String birthday = qBean.getBirthday().replace("/", "-");
    String indCode = qBean.getJobType();
    String countryName = ksUtil.getCountryNameByNationCode(qBean.getNtCode());
    String countryName2 = ksUtil.getCountryNameByNationCode(qBean.getNtCode2());
    String engName = qBean.getEngName();
    String bStatus = qBean.getbStatus();
    String cStatus = qBean.getcStatus();
    String rStatus = qBean.getrStatus();
    String processType = "query1821";
    
    JTable tb5 = getTable(tableName);
    int s_row = tb5.getSelectedRow();
    setValueAt(tableName, name, s_row, "DeputyName");
    setValueAt(tableName, "Y", s_row, "PaymentDeputy");
    setValueAt(tableName, bStatus, s_row, "B_STATUS");
    setValueAt(tableName, cStatus, s_row, "C_STATUS");
    setValueAt(tableName, rStatus, s_row, "R_STATUS");
    
    //���ӥu�|���@�ӭq��A�u���J���ӦA�令�U�����ˤl�A�d�۰��Ѧ�
    String orderNo = getValueAt("table4", 0, "OrderNo").toString().trim();
    String orderDate = ksUtil.getOrderDateByOrderNo(orderNo);
    String amlText = projectID + "," + orderNo + "," + orderDate + "," + funcName + "," + recordType + "," + value + "," + name + "," 
        + birthday + "," + indCode + "," + countryName + "," + countryName2 + "," + engName + "," + processType;
    setValue("AMLText", amlText);
    getButton("BtCustAML").doClick();
    errMsg += getValue("AMLText").trim();

    //210526 Kyle : �ܴ��ק� Start : �����h���q��ΫȤ�
    //���q��s�� & ���
//    JTable jt3 = this.getTable("table3"); //�Ȥ���
//    JTable jt4 = this.getTable("table4"); //�q����
//    for(int i=0 ; i<jt4.getRowCount() ; i++) {
//      String orderNo = getValueAt("table4", i, "OrderNo").toString().trim();
//      String orderDate = "";
//      for(int ii=0 ; ii<jt3.getRowCount() ; ii++) {
//        String customNo = getValueAt("table3", i, "CustomNo").toString().trim();
//        String sql = "select a.orderNo , a.orderDate from Sale05M090 a , Sale05M091 b where a.orderNo=b.orderNo and b.orderNo='"+orderNo+"' and b.customNo='"+customNo+"' ";
//        String[][] retOrder = dbSale.queryFromPool(sql);
//        if(retOrder.length > 0) {
//          orderDate = retOrder[0][1].trim();  //���ӥu�|���@��
//          break;
//        }
//      }
//      if(StringUtils.isNotBlank(orderDate)) {
//        //�q��&�Ȥ�w�t��A����ܴ� start
//        String amlText = projectID + "," + orderNo + "," + orderDate + "," + getFunctionName() + "," + "�H�Υd�Nú�ڤH���" 
//                       + "," + value + "," + retQuery[0][0].trim() + "," + BirthDay + "," + indCode + "," + "query1821";
//        setValue("AMLText" , amlText);
//        getButton("BtCustAML").doClick();
//        errMsg += getValue("AMLText").trim();
//        // �ܴ�END
//        
//        //���ӥu�|�t���@���A�����N�i�H�{�F
//        break;
//      }
//    }
    //210526 Kyle : �ܴ��ק� End

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

  public String getInformation() {
    return "---------------null(null).DeputyID.field_check()----------------";
  }
}
