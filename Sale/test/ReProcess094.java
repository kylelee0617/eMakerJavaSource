package Sale.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;

public class ReProcess094 extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbEIP;
  talk dbJPSLIB;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbEIP = ksUtil.getTBean().getDbEIP();
    dbJPSLIB = getTalk("JGENLIB");
    serverType = get("serverType").toString().trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);

    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("���]�h��");

    String testText = "";
    if (isTest) testText = "top 100";

    String sql91 = "select " + testText + " * from sale05m094 where trxdate BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "' "
                 + "and OrderNo not in (select OrderNo from sale05m094 "
                 + "where trxdate BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "' "
                 + "and BItemCd='03' and MItemCd='03' and SItemCd='01' and DItemCd in ('01','03','04','07','08','09','10','11') ) " + "order by trxdate desc";
    String[][] ret = dbSale.queryFromPool(sql91);

    String stringToday = datetime.getToday("YYYY/mm/dd"); // 2015-05-13 B3018
    String RocNowDate = stringToday.replace("/", "");
    String tempROCYear = "" + (Integer.parseInt(RocNowDate.substring(0, RocNowDate.length() - 4)) - 1911);
    RocNowDate = tempROCYear + RocNowDate.substring(RocNowDate.length() - 4, RocNowDate.length());
    String strNowTime = new SimpleDateFormat("HHmmss").format(getDate());
    String userNo = getUser().toUpperCase().trim();
    String empNo = "system";
    String[][] retEip = null;
    String errMsg = "";

    // actionNo
    String ram = "";
    Random random = new Random();
    for (int i = 0; i < 4; i++) {
      ram += String.valueOf(random.nextInt(10));
    }
    String actionNo = stringToday.replace("/", "") + strNowTime + ram;
    System.out.println("actionNo=====>" + actionNo);

    // �����u�s��
    String stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
    retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }

    for (int i = 0; i < ret.length; i++) {
      String projectId1 = ret[i][0].trim();
      String orderNo = ret[i][1].trim();
      String trxDate = ret[i][2].trim();

      // �~���l�ܬy����
      int intRecordNo = 1;
      stringSQL = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + orderNo + "'";
      String[][] ret070Table = dbSale.queryFromPool(stringSQL);
      if (!"".equals(ret070Table[0][0].trim())) {
        intRecordNo = Integer.parseInt(ret070Table[0][0].trim()) + 1;
      }

      // ���Ȥ�
      stringSQL = "select distinct CustomNo , CustomName from Sale05M091 a " + "left join sale05m090 b on a.OrderNo = b.OrderNo "
          + "where ISNULL(a.StatusCd, '') != 'C' and a.OrderNo = '" + orderNo + "' ";
      String[][] ret2 = dbSale.queryFromPool(stringSQL);

      for (int j = 0; j < ret2.length; j++) {
        String strCustomNo = ret2[j][0].trim();
        String strCustomName = ret2[j][1].trim();

        // Sale05M070
        stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "
            + "VALUES " + "('" + orderNo + "','" + projectId1 + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C','"
            + strCustomNo + "','" + strCustomName + "','" + trxDate + "','RY','773','006','�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','"
            + strNowTime + "')";
        dbSale.execFromPool(stringSQL);
        intRecordNo++;

        // AS400
        stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + orderNo + "', '" + RocNowDate + "', '"
            + strCustomNo + "', '" + strCustomName + "', '773', '006', '�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶRĵ�ܡC','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
        dbJPSLIB.execFromPool(stringSQL);
      }

    }

    this.message(">>> ���]�h��ĵ�ܧ��� !!! <<<");
  }

  public boolean ����ˮ�() throws Throwable {
    �v�jDate = this.getValue("StartDate").trim();
    �w�oDate = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(�v�jDate) || StringUtils.isBlank(�w�oDate)) {
      message("���!");
      return false;
    }

    if (�v�jDate.length() == 8) {
      if (!check.isACDay(�v�jDate)) return false;
      �v�jDate = kUtil.formatACDate(�v�jDate, "/");
    } else if (�v�jDate.length() == 10 && StringUtils.contains(�v�jDate, "/")) {

    } else {
      message("�v�jDate����榡���~");
      return false;
    }

    if (�w�oDate.length() == 8) {
      if (!check.isACDay(�w�oDate)) return false;
      �w�oDate = kUtil.formatACDate(�w�oDate, "/");
    } else if (�w�oDate.length() == 10 && StringUtils.contains(�w�oDate, "/")) {

    } else {
      message("�w�oDate����榡���~");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
