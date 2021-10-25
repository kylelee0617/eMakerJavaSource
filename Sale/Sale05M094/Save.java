package Sale.Sale05M094;

import java.text.SimpleDateFormat;
import java.util.Random;

import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;
import jcx.util.datetime;

public class Save extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    // �h��s��
    KSqlUtils ksUtil = new KSqlUtils();
    talk dbSale = getTalk("Sale");
    talk dbJPSLIB = getTalk("JGENLIB");
    talk dbEIP = getTalk("EIP");
    String[][] tb1_string = getTableData("table1");
    String[][] tb2_string = getTableData("table2");
    String stringSQL = "";
    boolean flag1 = false;
    boolean flag2 = false;
    String stringCustomName = "";
    String[][] retSale05M091 = null;
    StringBuilder sblog = new StringBuilder();
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), ">>>Start");

    if (getValue("field1").trim().length() == 0) {
      message("�קO���i�ť�");
      return value;
    }
    String stringTrxDate = getValue("field2").trim();
    if (stringTrxDate.length() == 0) {
      message("�h�W������i�ť�");
      return value;
    }
    // 2011/07/18 ���u�s�� B3018 Start
    if (stringTrxDate.length() != 10) {
      message("[�h�W���] �榡���~(YYYY/MM/DD)�C");
      return value;
    }
    stringTrxDate = convert.replace(stringTrxDate, "/", "");
    if (!check.isACDay(stringTrxDate)) {
      message("[�h�W���] �榡���~(YYYY/MM/DD)�C");
      return value;
    } else {
      setValue("field2", convert.FormatedDate(stringTrxDate, "/"));
    }
    // 2011/07/18 ���u�s�� B3018 End
    // Start �ק��G2008/01/09 ���u�s���GB3774
    if (getValue("BItemCd").trim().length() == 0) {
      message("�j�����N�X���i�ť�");
      return value;
    }
    if (getValue("MItemCd").trim().length() == 0) {
      message("�������N�X���i�ť�");
      return value;
    }
    if (getValue("SItemCd").trim().length() == 0) {
      message("�p�����N�X���i�ť�");
      return value;
    }
    if (getValue("DItemCd").trim().length() == 0) {
      message("�Ӥ����N�X���i�ť�");
      return value;
    }
    if (getValue("unsubscribe").trim().length() == 0) {
      message("�h�q�覡���i�ť�");
      return value;
    }
    // End
    
    for (int i = 0; i < tb2_string.length; i++) {
      System.out.println("tb2_string" + tb2_string[i][0] + "");
      if (tb2_string[i][0].equals("1")) {
        flag1 = true;
      }
    }
    System.out.println("flag1" + flag1);
    if (flag1 == false) {
      message("�Цܤ֤Ŀ�@���h��");
    } else {
      ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), ">>>orderNo:" + getValue("field3").trim());
      stringSQL = "select max(RecordNo) from Sale05M091 " + " WHERE OrderNo = '" + getValue("field3") + "'";
      retSale05M091 = dbSale.queryFromPool(stringSQL);
      int RecordNo = Integer.parseInt(retSale05M091[0][0]) + 1;
      stringSQL = "delete SALE05M094  " + " WHERE ProjectID1 = '" + getValue("field1").trim() + "'" + " AND OrderNo = '" + getValue("field3").trim() + "'" + " AND TrxDate = '"
          + getValue("field2").trim() + "'";
      dbSale.execFromPool(stringSQL);
      stringSQL = "delete SALE05M095  " + " WHERE ProjectID1 = '" + getValue("field1").trim() + "'" + " AND OrderNo = '" + getValue("field3").trim() + "'" + " AND TrxDate = '"
          + getValue("field2").trim() + "'";
      dbSale.execFromPool(stringSQL);

      for (int i = 0; i < tb2_string.length; i++) {
        System.out.println("i2" + tb2_string[i][2]);
        // if (tb2_string[i][0]!="0"){
        if (tb2_string[i][0].equals("1")) {
          stringSQL = " INSERT  " + " INTO Sale05M095" + " ( " + "ProjectID1," + " OrderNo," + " TrxDate," + " RecordNo," + " HouseCar," + " Position " + " ) " + " VALUES " + " ( "
              + "'" + getValue("field1").trim() + "'," + "'" + getValue("field3").trim() + "'," + "'" + getValue("field2").trim() + "'," + "N'" + tb2_string[i][2] + "'," + "N'"
              + tb2_string[i][3] + "'," + "N'" + tb2_string[i][4] + "'" + " ) ";
          dbSale.execFromPool(stringSQL);

          stringSQL = " UPDATE Sale05M092 SET" + "  StatusCd = 'D', " + "  TrxDate = '" + getValue("field2").trim() + "' " + " WHERE OrderNo ='" + getValue("field3").trim() + "' "
              + " and  Position ='" + tb2_string[i][4] + "' ";
          dbSale.execFromPool(stringSQL);

        } else {
          stringSQL = " UPDATE Sale05M092 SET" + "  StatusCd = null, " + "  TrxDate = null " + " WHERE OrderNo ='" + getValue("field3").trim() + "' " + " and  Position ='"
              + tb2_string[i][4] + "' ";
          dbSale.execFromPool(stringSQL);

        }
      }
      String stringToday = datetime.getToday("YYYY/mm/dd"); // 2015-05-13 B3018
      stringSQL = " INSERT INTO Sale05M094 ( " + "ProjectID1," + " OrderNo," + " TrxDate," + " BItemCd," + " MItemCd," + " SItemCd," + " DItemCd," + " LastYMD," + " unsubscribe "
          + " ) " + " VALUES " + " ( " + "'" + getValue("field1").trim() + "'," + "'" + getValue("field3").trim() + "'," + "'" + getValue("field2").trim() + "'," + "'"
          + getValue("BItemCd").trim() + "'," + "'" + getValue("MItemCd").trim() + "'," + "'" + getValue("SItemCd").trim() + "'," + "'" + getValue("DItemCd").trim() + "', " + "'"
          + stringToday + "', " + "'" + getValue("unsubscribe").trim() + "' " + " ) ";
      dbSale.execFromPool(stringSQL);
      // �t�κæ��~���A��
      // �~���l�ܬy����
      int intRecordNo = 1;
      stringSQL = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + getValue("field3").trim() + "'";
      String[][] ret070Table = dbSale.queryFromPool(stringSQL);
      if (!"".equals(ret070Table[0][0].trim())) {
        intRecordNo = Integer.parseInt(ret070Table[0][0].trim()) + 1;
      }

      boolean isNonList = true;
      String RocNowDate = stringToday.replace("/", "");
      String tempROCYear = "" + (Integer.parseInt(RocNowDate.substring(0, RocNowDate.length() - 4)) - 1911);
      RocNowDate = tempROCYear + RocNowDate.substring(RocNowDate.length() - 4, RocNowDate.length());
      String strNowTime = new SimpleDateFormat("HHmmss").format(getDate());
      String userNo = getUser().toUpperCase().trim();
      String empNo = "";
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

      // ���m�W
      String custName = tb1_string[0][3].trim();
      String custID = tb1_string[0][2].trim();

      System.out.println("custName=====>" + custName);
      System.out.println("custID=====>" + custID);
      stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
      retEip = dbEIP.queryFromPool(stringSQL);
      if (retEip.length > 0) {
        empNo = retEip[0][0];
      }

      // ���A��LOG1~5,7~14
      // 1
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','001','�P�@�Ȥ�P�@��~�餺2��(�t)�H�W�]�t�{���B�״ڡB�H�Υd�B�䲼����A�B�C���Ҥ���s�x��450,000~499,999���A�t���ˮֹwĵ�C','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 2
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','002','�P�@�Ȥ�3����~�餺�A��2��H�{���ζ״ڹF450,000~499,999��, �t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 3
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','003','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 4
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','004','�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 5
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','005','�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 7
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','007','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 8
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','008','���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 9
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','009','�Ȥ�Y�ӦۥD�޾����Ҥ��i����~���P�����ꮣ���Y���ʥ�����a�Φa�ϡA�Ψ�L����`�Υ��R����`����a�Φa�ϡA���ˮ֨�X�z�ʡC','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 10
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','010','�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','" + empNo + "','" + RocNowDate + "','"
          + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 11
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','011','����̲ר��q�H�Υ���H���D�޾������i�����Ƥ��l�ι���F�ΰ�ڻ{�w�ΰl�d�����Ʋ�´�F�Υ������æ��P���Ʋ�´�����p�̡A���̸ꮣ����k�i������@�~�C','" + empNo + "','" + RocNowDate
          + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 12
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','012','�Ȥ�n�D�N���ʲ��v�Q�n�O���ĤT�H�A���ണ�X�������p�Ωڵ����������`���p�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 13
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','013','�Ȥ��I���ʲ�������ڶ��A�H�{�r��I�q���H�~�U�����ڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime
          + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // 14
      stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
          + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','���A��','" + custID + "','" + custName
          + "','" + getValue("field2").trim() + "','RY','773','014','�Ȥ��ñ���e���e�I�M�۳ƴڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(stringSQL);
      intRecordNo++;
      // �h��wĵ�бư��G03�Ȥ᭱-03-��L-01�ӤH�]��-03.04.07.08.09.10.11
      // strAlertmag = "�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶRĵ�ܡC�Ш̬~������@�~��z�C";
      if ("03".equals(getValue("DItemCd").trim()) || "04".equals(getValue("DItemCd").trim()) || "07".equals(getValue("DItemCd").trim()) || "08".equals(getValue("DItemCd").trim())
          || "09".equals(getValue("DItemCd").trim()) || "10".equals(getValue("DItemCd").trim()) || "11".equals(getValue("DItemCd").trim())) {
        if ("01".equals(getValue("SItemCd").trim())) {
          if ("03".equals(getValue("MItemCd").trim())) {
            if ("03".equals(getValue("BItemCd").trim())) {
              isNonList = false;
            }
          }
        }
      }

      // 20191004
      // �䲼 �����T�I �������u
      if ("�䲼-�����T�I".equals(getValue("unsubscribe").trim()) || "�䲼-�������u".equals(getValue("unsubscribe").trim())) {
        stringSQL = "SELECT CustomNo, CustomName FROM Sale05M091 WHERE OrderNo = '" + getValue("field3").trim() + "'";
        retSale05M091 = dbSale.queryFromPool(stringSQL);
        if (retSale05M091.length > 0) {
          for (int i = 0; i < retSale05M091.length; i++) {
            String strCustomno = retSale05M091[i][0].trim();
            String strCustomName = retSale05M091[i][1].trim();
            if ("�䲼-�����T�I".equals(getValue("unsubscribe").trim())) {
              // Sale05M070
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�����T�I','�x�s','�Ȥ�" + strCustomName
                  + "��������h�ٴڶ��A�n�D���q���I�����T��I�����������ڡA�Ш̬~���θꮣ����@�~��z�C','" + strCustomno + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','015','�Ȥ�"
                  + strCustomName + "��������h�ٴڶ��A�n�D���q���I�����T��I�����������ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
              // AS400
              stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
                  + RocNowDate + "', '" + retSale05M091[i][0] + "', '" + retSale05M091[i][1] + "', '773', '015', '�n�D���q�}�ߨ����T��I�������䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbJPSLIB.execFromPool(stringSQL);
              if ("".equals(errMsg)) {
                errMsg = "�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�����T��I�����������ڡA�Ш̬~���θꮣ����@�~��z�C";
              } else {
                errMsg = errMsg + "\n�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�����T��I�����������ڡA�Ш̬~���θꮣ����@�~��z�C";
              }
            } else {
              // ���ŦX
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�����T�I','�x�s','���ŦX','" + strCustomno + "','"
                  + strCustomName + "','" + getValue("field2").trim() + "','RY','773','015','�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�����T��I�����������ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
            }
            if ("�䲼-�������u".equals(getValue("unsubscribe").trim())) {
              // Sale05M070
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "', '�h��','�������u','�x�s','�Ȥ�" + strCustomName
                  + "��������h�ٴڶ��A�n�D���q���I�M�P����u�����ڡA�Ш̬~���θꮣ����@�~��z�C','" + strCustomno + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','016','�Ȥ�" + strCustomName
                  + "��������h�ٴڶ��A�n�D���q���I�M�P����u�����ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
              // AS400
              stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
                  + RocNowDate + "', '" + retSale05M091[i][0] + "', '" + retSale05M091[i][1] + "', '773', '016', '�n�D���q�}�ߺM�P����u(�������u)�䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbJPSLIB.execFromPool(stringSQL);
              if ("".equals(errMsg)) {
                errMsg = "�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�M�P����u�����ڡA�Ш̬~���θꮣ����@�~��z�C";
              } else {
                errMsg = errMsg + "\n�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�M�P����u�����ڡA�Ш̬~���θꮣ����@�~��z�C";
              }
            } else {
              // ���ŦX
              stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                  + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "', '�h��','�������u','�x�s','���ŦX','" + strCustomno + "','"
                  + strCustomName + "','" + getValue("field2").trim() + "','RY','773','016','�Ȥ�" + strCustomName + "��������h�ٴڶ��A�n�D���q���I�M�P����u�����ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
                  + RocNowDate + "','" + strNowTime + "')";
              dbSale.execFromPool(stringSQL);
              intRecordNo++;
            }
          }
        }
      }

      RiskCustomBean[] cBeans = ksUtil.getCustom("", getValue("field3").trim(), true);
      if (isNonList) {
        // 4.�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶR�A���ˮ֨�X�z�ʡC
        for (int i = 0; i < cBeans.length; i++) {
          RiskCustomBean cBean = cBeans[i];
          String strCustomNo = cBean.getCustomNo();
          String strCustomName = cBean.getCustomName();
          // Sale05M070
          stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s','�Ȥ�" + strCustomName
              + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C','" + strCustomNo + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','006','�Ȥ�" + strCustomName
              + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(stringSQL);
          intRecordNo++;
          // AS400
          stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
              + RocNowDate + "', '" + strCustomNo + "', '" + strCustomName + "', '773', '006', '�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶRĵ�ܡC','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJPSLIB.execFromPool(stringSQL);
          if ("".equals(errMsg)) {
            errMsg = "�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C";
          } else {
            errMsg = errMsg + "\n�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C";
          }
        }
      }

      // TODO: rule23
      String rule23 = getValue("AMLRule231").trim();
      if ("B".equals(rule23)) {
        String amlDesc = ksUtil.getAMLDescOne("023");
        String allCustNames = ksUtil.getCustomNames(getValue("field1").trim(), getValue("field3").trim());
        amlDesc = amlDesc.replaceAll("<customTitle>", "�Ȥ�").replace("<customName>", allCustNames);
        errMsg += "\n" + amlDesc;

        for (int i = 0; i < cBeans.length; i++) {
          RiskCustomBean cBean = cBeans[i];
          String strCustomNo = cBean.getCustomNo();
          String strCustomName = cBean.getCustomName();
          // Sale05M070
          stringSQL = "INSERT INTO Sale05M070 ( OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + getValue("field3").trim() + "','" + getValue("field1").trim() + "','" + intRecordNo + "','" + actionNo + "','�h��','�h��ĵ��','�x�s'," + "'" + amlDesc + "','" + strCustomNo
              + "','" + strCustomName + "','" + getValue("field2").trim() + "','RY','773','023'," + "'" + amlDesc + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(stringSQL);
          intRecordNo++;
          // AS400
          stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97,SHB98, SHB99) VALUES ('RY', '" + getValue("field3").trim() + "', '"
              + RocNowDate + "', '" + strCustomNo + "', '" + strCustomName + "', '773', '023', '" + amlDesc + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJPSLIB.execFromPool(stringSQL);
          if ("".equals(errMsg)) {
            errMsg = "�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C";
          } else {
            errMsg = errMsg + "\n�Ȥ�" + strCustomName + "ñ���e�h�q��������A�Ш̬~���θꮣ����@�~��z�C";
          }
        }

        getButton("SendMailAction2").doClick();
      }

      // �e�XerrMsg
      if (!"".equals(errMsg)) {
        setValue("errMsgBoxText", errMsg);
        getButton("errMsgBoxBtn").doClick();
      }

      // �}�ҫ��s
      getButton("button3").setVisible(true);
      getButton("button4").setVisible(true);

      // ���P���p�H
      getButton("RenewRelated").doClick();

      message("�h��s�ɦ��\�C");

      ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), sblog.toString() + " : End");
    }
    return value;
  }

  public String getInformation() {
    return "---------------button4(\u9000\u6236\u524d\u67e5\u8a62).defaultValue()----------------";
  }
}
