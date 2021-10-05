package Sale.AML;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import Farglory.util.AMLBean;
import Farglory.util.AMLTools;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class CheckAML2 extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    // 20191107 �~���θꮣ���I�޲z�F���B�z�{�ǧ@�~+����~���Υ����ꮣ���I�����B�z�{�ǧ@�~+�~���θꮣ�W����B�z�{�ǧ@�~
    System.out.println("===========AML============S");
    talk dbSale = getTalk("Sale");
    talk db400CRM = getTalk("400CRM");
    talk dbPW0D = getTalk("pw0d");
    talk dbJGENLIB = getTalk("JGENLIB");
    talk dbEIP = getTalk("EIP");
    KSqlUtils ksUtil = new KSqlUtils(); 
    
    String strSaleSql = "";
    String str400CRMSql = "";
    String strPW0DSql = "";
    String strJGENLIBSql = "";
    String strEIPSql = "";
    String strBDaysql = "";
    String str400sql = "";
    String stringSQL = "";
    String strPW0Dsql = "";
    String[][] ret080Table;// �{��
    String[][] ret083Table;// �H�Υd
    String[][] ret328Table;// �Ȧ�
    String[][] ret082Table;// ����
    String[][] ret070Table;
    String[][] retPDCZPFTable;
    String[][] retQueryLog;
    String[][] retCList;
    // ���e����
    String strActionName = getValue("actionName").trim();// �@�ʦW��
    String strCreditCardMoney = getValue("CreditCardMoney").trim();// �H�Υd
    String strCashMoney = getValue("CashMoney").trim();// �{��
    String strBankMoney = getValue("BankMoney").trim();// �Ȧ�
    String strCheckMoney = getValue("CheckMoney").trim();// ����
    String strReceiveMoney = getValue("ReceiveMoney").trim();// �����`�B
    String strProjectID1 = getValue("field2").trim();// �קO�N�X
    String strEDate = getValue("field3").trim();// ���ڤ��
    String strDocNo = getValue("field4").trim();// �s��
    if ("".equals(strCreditCardMoney)) {
      strCreditCardMoney = "0";
    }
    if ("".equals(strCashMoney) || "0.0".equals(strCashMoney)) {
      strCashMoney = "0";
    }
    if ("".equals(strBankMoney) || "0.0".equals(strBankMoney)) {
      strBankMoney = "0";
    }
    if ("".equals(strCheckMoney)) {
      strCheckMoney = "0";
    }
    // �Nú�H����
    String strDeputy = getValue("PaymentDeputy").trim();
    String strDeputyName = getValue("DeputyName").trim();
    String strDeputyID = getValue("DeputyID").trim();
    String strDeputyRelationship = getValue("DeputyRelationship").trim();
    String bStatus = getValue("B_STATUS").trim();
    String cStatus = getValue("C_STATUS").trim();
    String rStatus = getValue("R_STATUS").trim();
    // �ʶR�H�m�W
    String allOrderID = "";
    String allOrderName = "";
    String percentage = "";
    String[][] orderCustomTable = getTableData("table3");
    for (int g = 0; g < orderCustomTable.length; g++) {
      if ("".equals(allOrderName)) {
        allOrderID = orderCustomTable[g][3].trim();
        allOrderName = orderCustomTable[g][4].trim();
        percentage = orderCustomTable[g][5].trim();
      } else {
        allOrderID = allOrderID + "�B" + orderCustomTable[g][3].trim();
        allOrderName = allOrderName + "�B" + orderCustomTable[g][4].trim();
        percentage = percentage + "�B" + orderCustomTable[g][5].trim();
      }
    }

    // Rule13,14,22
    String rule13 = getValue("Rule13").trim();
    String rule14 = getValue("Rule14").trim();
    
    // �@��
    String errMsg = "";
    String allCustomName = allOrderName;
    String allCustomID = allOrderID;
    // ���ڤ������榡
    String[] tempEDate = strEDate.split("/");
    String rocDate = "";
    String year = tempEDate[0];
    int intYear = Integer.parseInt(year) - 1911;
    rocDate = Integer.toString(intYear) + tempEDate[1] + tempEDate[2];
    // LOG NOW DATE
    Date now = new Date();
    SimpleDateFormat nowsdf = new SimpleDateFormat("yyyyMMdd");
    String strNowDate = nowsdf.format(now);
    String tempROCYear = "" + (Integer.parseInt(strNowDate.substring(0, strNowDate.length() - 4)) - 1911);
    String RocNowDate = tempROCYear + strNowDate.substring(strNowDate.length() - 4, strNowDate.length());
    SimpleDateFormat nowTimeSdf = new SimpleDateFormat("HHmmss");
    String strNowTime = nowTimeSdf.format(now);
    SimpleDateFormat nowTimestampSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String strNowTimestamp = nowTimestampSdf.format(now);
    // ���s
    String userNo = getUser().toUpperCase().trim();
    String empNo = "";
    String[][] retEip = null;
    strEIPSql = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'";
    retEip = dbEIP.queryFromPool(strEIPSql);
    if (retEip.length > 0) {
      empNo = retEip[0][0];
    }
    // �ʪ��ҩ��渹
    String strOrderNo = "";
    String orderNos = "";
    String[][] orderNoTable = getTableData("table4");
    strOrderNo = orderNoTable[0][2].trim();
    for (int g = 0; g < orderNoTable.length; g++) {
      if ("".equals(orderNos)) {
        orderNos = orderNoTable[g][2].trim();
      } else {
        orderNos += "�B" + orderNoTable[g][2].trim();
      }
    }

    // actionNo
    String ram = "";
    Random random = new Random();
    for (int i = 0; i < 4; i++) {
      ram += String.valueOf(random.nextInt(10));
    }
    String actionNo = strNowDate + strNowTime + ram;

    // start of �˺A1~4 Kyle
    // 1�P�@�Ȥ�P�@��~�餺2��(�t)�H�W�]�t�{���B�״ڡB�H�Υd�B�䲼����A�B�C���Ҥ���s�x��450,000~499,999���A�t���ˮֹwĵ�C(����n�R��)
    // 2�P�@�Ȥ�3����~�餺�A��2��H�{���ζ״ڹF450,000~499,999��, �t���ˮִ��ܳq���C
    // 3�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C
    // 4�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    if (strCashMoney == null || "".equals(strCashMoney)) { // �{���`�B
      strCashMoney = "0";
    }
    if (strCreditCardMoney == null || "".equals(strCreditCardMoney)) { // �H�Υd�`�B
      strCreditCardMoney = "0";
    }
    if (strBankMoney == null || "".equals(strBankMoney)) { // �Ȧ��`�B
      strBankMoney = "0";
    }
    if (strCheckMoney == null || "".equals(strCheckMoney)) { // �����`�B
      strCheckMoney = "0";
    }
    if (strReceiveMoney == null || "".equals(strReceiveMoney)) { // ���ڳ��`�B
      strReceiveMoney = "0";
    }
    double dCashMoney = Double.parseDouble(strCashMoney);
    double dCheckMoney = Double.parseDouble(strCheckMoney);
    double dCreditMoney = Double.parseDouble(strCreditCardMoney);
    double dBankMoney = Double.parseDouble(strBankMoney);
    double dReceiveMoney = Double.parseDouble(strReceiveMoney);
    String[] orderNoss = orderNos.split("�B");
    String[] customNos = allCustomID.split("�B");
    String[] percentages = percentage.split("�B");

    KUtils kutil = new KUtils();
    String tempMsg = "";
    AMLBean aml = new AMLBean();
    aml.setDocNo(strDocNo);
    aml.seteDate(strEDate);
    aml.setOrderNo(strOrderNo);
    aml.setProjectID1(strProjectID1);
    aml.setFuncName("����");
    aml.setFuncName2("���");
    aml.setActionName("�s��");
    aml.setCustomTitle("�Ȥ�");
    aml.setTrxDate(strEDate);
    aml.setOrderNos(kutil.genQueryInString(orderNoss));
    aml.setCustomNos(kutil.genQueryInString(customNos));
    aml.setCustomNames(allCustomName);
    AMLTools amlTool = new AMLTools(aml);

    // �A��1 - �q�� - �Ӥ�ҭp��U���`�B
    for (int g = 0; g < orderNoss.length; g++) {
      double pers = 1 / orderNoss.length;
      if ((dCashMoney * pers >= 450000 && dCashMoney * pers <= 499999) || (dCreditMoney * pers >= 450000 && dCreditMoney * pers <= 499999)
          || (dCheckMoney * pers >= 450000 && dCheckMoney * pers <= 499999) || (dBankMoney * pers >= 450000 && dBankMoney * pers <= 499999)) {
        tempMsg = amlTool.chkAML001(aml, "order").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
    }

    // �A��1 - �ӤH - �Ӥ�ҭp��U���`�B
    for (int g = 0; g < customNos.length; g++) {
      double pers = Double.parseDouble(percentages[g].trim()) / 100;
      if ((dCashMoney * pers >= 450000 && dCashMoney * pers <= 499999) || (dCreditMoney * pers >= 450000 && dCreditMoney * pers <= 499999)
          || (dCheckMoney * pers >= 450000 && dCheckMoney * pers <= 499999) || (dBankMoney * pers >= 450000 && dBankMoney * pers <= 499999)) {
        tempMsg = amlTool.chkAML001(aml, "custom").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
    }

    // �A��2
    // ����Y���@���{���ζ״ڤ���45~49�h�ˬd�e���
    // Tips: �q���Ȥ�n���}�B�z
    if (dCashMoney > 0 || dBankMoney > 0) {
      if ((dCashMoney >= 450000 && dCashMoney <= 499999) || (dBankMoney >= 450000 && dBankMoney <= 499999)) { // �q��
        tempMsg = amlTool.chkAML002(aml, "order").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
      for (int g = 0; g < customNos.length; g++) {
        if ((dCashMoney * Double.parseDouble(percentages[g].trim()) / 100 >= 450000 && dCashMoney * Double.parseDouble(percentages[g].trim()) / 100 <= 499999)
            || (dBankMoney * Double.parseDouble(percentages[g].trim()) / 100 >= 450000 && dBankMoney * Double.parseDouble(percentages[g].trim()) / 100 <= 499999)) { // �Ȥ�
          aml.setCustomId(customNos[g].trim());
          tempMsg = amlTool.chkAML002(aml, "custom").getData().toString();
          if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
        }
      }
    }

    // �A��3
    if (dCashMoney > 0) {
      for (int g = 0; g < orderNoss.length; g++) {
        tempMsg = amlTool.chkAML0031(aml, orderNoss[g].trim(), "order").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }

      for (int g = 0; g < customNos.length; g++) {
        tempMsg = amlTool.chkAML0031(aml, customNos[g].trim(), "custom").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
    }

    // �A��4
    // ���q��Ĥ@����ú��??
    String sql = "";
    String sqlEDate = "";
    for (int g = 0; g < orderNoss.length; g++) {
      sql = "select Top 1 EDate from sale05m080 a , Sale05M086 b where a.DocNo=b.DocNo and b.OrderNo = '" + orderNoss[g].trim() + "' ORDER BY EDate";
      sqlEDate = dbSale.queryFromPool(sql)[0][0].toString().trim();
      if (!(strEDate.equals(sqlEDate)) && dCashMoney > 0) {
        // �N���O���q��Ĥ@��ú�� & �����{��
        tempMsg = amlTool.chkAML0041(aml, orderNoss[g].trim(), "order").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
    }
    // �ӤH�Ĥ@��ú��?
    for (int g = 0; g < customNos.length; g++) {
      sql = "select top 1 EDate from sale05m080 a , Sale05M084 b where a.DocNo=b.DocNo and  b.CustomNo = '" + customNos[g].trim() + "' order by EDate";
      sqlEDate = dbSale.queryFromPool(sql)[0][0].toString().trim();
      if ((!strEDate.equals(sqlEDate)) && dCashMoney > 0) {
        // �N���O���H�Ĥ@��ú�� & �����{��
        tempMsg = amlTool.chkAML0041(aml, customNos[g].trim(), "custom").getData().toString();
        if (!errMsg.contains(tempMsg)) errMsg += tempMsg;
      }
    }

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // End of �A��1~4 Kyle

    // �~���l�ܬy����
    // 20201207 Kyle : �]���A��1~4�t�~�W�߳B�z�A�קK�v�T��y�����B��A�G�y�L�U��
    int intRecordNo = 1;
    // strSaleSql = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo
    // ='"+strOrderNo+"'";
    strSaleSql = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE DocNo ='" + strDocNo + "'";
    ret070Table = dbSale.queryFromPool(strSaleSql);
    if (!"".equals(ret070Table[0][0].trim())) {
      intRecordNo = Integer.parseInt(ret070Table[0][0].trim()) + 1;
    }

    // �H�Υd
    ret083Table = getTableData("table5");
    if (ret083Table.length > 0) {
      for (int e = 0; e < ret083Table.length; e++) {
        String str083Deputy = ret083Table[e][7].trim();// ���Hú��
        String str083DeputyName = ret083Table[e][8].trim();// �m�W
        String str083DeputyId = ret083Table[e][9].trim();// �����Ҹ�
        String str083Rlatsh = ret083Table[e][10].trim();// ���Y
        String str083Rstatus = ret083Table[e][14].trim();// �Q���H

        // ���A��LOG_2,3,4,6,7,9,10,11,12,15,16 (�Чi�D�ڤ��ΰj��g���z��)
        int[] noUseAML = { 2, 3, 4, 6, 7, 9, 10, 11, 12, 15, 16 };
        Map mapAMLMsg = amlTool.getAMLReTurn();
        for (int ii = 0; ii < noUseAML.length; ii++) {
          String amlNo = "";
          if (noUseAML[ii] < 10) {
            amlNo = "00" + noUseAML[ii];
          } else {
            amlNo = "0" + noUseAML[ii];
          }
          String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
              .replaceAll("<customTitle2>", "");
          strSaleSql = "INSERT INTO Sale05M070 "
              + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
              + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
              + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
              + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }

        if ("Y".equals(str083Deputy)) { // ���Nú�H
          // �Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
          if ("�B��".equals(str083Rlatsh) || "��L".equals(str083Rlatsh)) {
            // Sale05M070
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName + "','�Nú�ڤH" + str083DeputyName
                + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + str083DeputyId + "','" + str083DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH"
                + str083DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str083DeputyId + "', '" + str083DeputyName + "', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime
                + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);
            errMsg += "�H�Υd�Nú�ڤH" + str083DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName + "','���ŦX','" + str083DeputyId
                + "','" + str083DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH" + str083DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }

          // ���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
          // Sale05M070
          strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName + "','�Nú�ڤH" + str083DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + str083DeputyId + "','" + str083DeputyName + "','" + strEDate + "','RY','773','008','�Nú�ڤH" + str083DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
          // AS400
          strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
              + "', '" + str083DeputyId + "', '" + str083DeputyName + "', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJGENLIB.execFromPool(strJGENLIBSql);
          errMsg += "�H�Υd�Nú�ڤH" + str083DeputyName + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";

          // �Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
          if ("Y".equals(str083Rstatus)) {
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName + "','�Nú�ڤH" + str083DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + str083DeputyId + "','" + str083DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str083DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str083DeputyId + "', '" + str083DeputyName + "', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','"
                + strNowTime + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);

            errMsg += "�H�Υd�Nú�ڤH" + str083DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "', '����','�H�Υd���','" + strActionName + "', '���ŦX','"
                + str083DeputyId + "','" + str083DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str083DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo
                + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        } else {
          // ���A��
          int[] noUseAML1 = { 5, 8, 17, 19, 20 };
          mapAMLMsg = amlTool.getAMLReTurn();
          for (int ii = 0; ii < noUseAML1.length; ii++) {
            String amlNo = "";
            if (noUseAML1[ii] < 10) {
              amlNo = "00" + noUseAML1[ii];
            } else {
              amlNo = "0" + noUseAML1[ii];
            }
            String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
                .replaceAll("<customTitle2>", "");
            strSaleSql = "INSERT INTO Sale05M070 "
                + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
                + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
                + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
                + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        }
      }
    }

    // �{��(�u���@��)
    if (StringUtils.isNumeric(strCashMoney) && Double.parseDouble(strCashMoney) > 0) {
      // ���A��LOG_6,9,10,11,12,15,16
      int[] noUseAML = { 6, 9, 10, 11, 12, 15, 16 };
      Map mapAMLMsg = amlTool.getAMLReTurn();
      for (int ii = 0; ii < noUseAML.length; ii++) {
        String amlNo = "";
        if (noUseAML[ii] < 10) {
          amlNo = "00" + noUseAML[ii];
        } else {
          amlNo = "0" + noUseAML[ii];
        }
        String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "").replaceAll("<customTitle2>",
            "");
        strSaleSql = "INSERT INTO Sale05M070 "
            + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
            + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName + "', '���A��','"
            + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate + "','"
            + strNowTime + "')";
        dbSale.execFromPool(strSaleSql);
        intRecordNo++;
      }

      if ("Y".equals(strDeputy)) {// ���Nú�H
        // �Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
        if ("�B��".equals(strDeputyRelationship) || "��L".equals(strDeputyRelationship)) {
          // Sale05M070
          strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "', '����','�{�����','" + strActionName + "', '�Nú�ڤH" + strDeputyName
              + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + strDeputyID + "','" + strDeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH" + strDeputyName + "�P�Ȥ�"
              + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
          // AS400
          strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
              + "', '" + strDeputyID + "', '" + strDeputyName + "', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJGENLIB.execFromPool(strJGENLIBSql);

          errMsg += "�{���Nú�ڤH" + strDeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
        } else {
          // ���ŦX
          strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "', '����','�{�����','" + strActionName + "', '���ŦX','" + strDeputyID
              + "','" + strDeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH" + strDeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
              + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }

        // ���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
        // Sale05M070
        strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
            + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�{�����','" + strActionName + "','�Nú�ڤH" + strDeputyName
            + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + strDeputyID + "','" + strDeputyName + "','" + strEDate + "','RY','773','008','�Nú�ڤH" + strDeputyName + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"
            + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
        dbSale.execFromPool(strSaleSql);
        intRecordNo++;
        // AS400
        strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
            + "', '" + strDeputyID + "', '" + strDeputyName + "', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
        dbJGENLIB.execFromPool(strJGENLIBSql);

        errMsg += "�{���Nú�ڤH" + strDeputyName + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";

        // �Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
        if ("Y".equals(rStatus)) {
          strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�{�����','" + strActionName + "', '�Nú�ڤH" + strDeputyName
              + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + strDeputyID + "','" + strDeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + strDeputyName
              + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
          // AS400
          strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
              + "', '" + strDeputyID + "', '" + strDeputyName + "', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','" + strNowTime
              + "')";
          dbJGENLIB.execFromPool(strJGENLIBSql);

          errMsg += "�{���Nú�ڤH" + strDeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
        } else {
          // ���ŦX
          strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�{�����','" + strActionName + "', '���ŦX','" + strDeputyID
              + "','" + strDeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + strDeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate
              + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }
      } else {
        // �{�����Hú�ڤ��A��5,8,17,18,19,20
        int[] noUseAML1 = { 5, 8, 17, 19, 20 };
        mapAMLMsg = amlTool.getAMLReTurn();
        for (int ii = 0; ii < noUseAML1.length; ii++) {
          String amlNo = "";
          if (noUseAML1[ii] < 10) {
            amlNo = "00" + noUseAML1[ii];
          } else {
            amlNo = "0" + noUseAML1[ii];
          }
          String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
              .replaceAll("<customTitle2>", "");
          strSaleSql = "INSERT INTO Sale05M070 "
              + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
              + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
              + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
              + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }
      }
    }

    // �Ȧ�״�
    ret328Table = getTableData("table9");
    if (ret328Table.length > 0) {
      for (int f = 0; f < ret328Table.length; f++) {
        String str328Deputy = ret328Table[f][5].trim();// ���Hú��
        String str328DeputyName = ret328Table[f][6].trim();// �m�W
        String str328DeputyId = ret328Table[f][7].trim();
        String str328ExPlace = ret328Table[f][8].trim();
        String str328Rlatsh = ret328Table[f][9].trim();
        String str328bStatus = ret328Table[f][11].trim();
        String str328cStatus = ret328Table[f][12].trim();
        String str328rStatus = ret328Table[f][13].trim();

        // ���A��3,4,6,7,9,11,12,15,16
        int[] noUseAML = { 3, 4, 6, 7, 9, 11, 12, 15, 16 };
        Map mapAMLMsg = amlTool.getAMLReTurn();
        for (int ii = 0; ii < noUseAML.length; ii++) {
          String amlNo = "";
          if (noUseAML[ii] < 10) {
            amlNo = "00" + noUseAML[ii];
          } else {
            amlNo = "0" + noUseAML[ii];
          }
          String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
              .replaceAll("<customTitle2>", "");
          strSaleSql = "INSERT INTO Sale05M070 "
              + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
              + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
              + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
              + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }

        // �۪��ĺʷ��޲z�e���|�����ڨ���~����´�Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`��ڨ���~����´��ĳ����a�Φa�϶פJ������ڶ��C
        strJGENLIBSql = "SELECT CZ07 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09 = '" + str328ExPlace + "'";
        retPDCZPFTable = dbJGENLIB.queryFromPool(strJGENLIBSql);
        if (retPDCZPFTable.length > 0) {
          String strCZ07 = retPDCZPFTable[0][0].trim();
          if ("�u���k��".equals(strCZ07)) {
            // Sale05M070
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "', '����','�Ȧ���','" + strActionName + "', '�Nú�ڤH"
                + str328DeputyName + "�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','" + str328DeputyId + "','" + str328DeputyName + "','" + strEDate
                + "','RY','773','010','�Nú�ڤH" + str328DeputyName + "�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str328DeputyId + "', '" + str328DeputyName + "', '773', '010', '�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);

            String strTempMsg = "";
            if ("Y".equals(str328Deputy)) {
              strTempMsg = "�Ȧ�Nú�ڤH" + str328DeputyName;
            } else {
              strTempMsg = "�Ȥ�" + allOrderName;
            }
            errMsg += strTempMsg + "�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "', '����','�Ȧ���','" + strActionName + "', '���ŦX','" + allCustomID
                + "','" + allCustomName + "','" + strEDate + "','RY','773','010','�Nú�ڤH" + str328DeputyName + "�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','" + empNo
                + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        }

        if ("Y".equals(str328Deputy)) {// ���Nú�H
          // �Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
          if ("�B��".equals(str328Rlatsh) || "��L".equals(str328Rlatsh)) {
            // Sale05M070
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName, RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȧ���','" + strActionName + "','�Nú�ڤH" + str328DeputyName
                + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + str328DeputyId + "','" + str328DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH"
                + str328DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str328DeputyId + "', '" + str328DeputyName + "', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime
                + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);

            errMsg += "�Ȧ�Nú�ڤH" + str328DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName, RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȧ���','" + strActionName + "','���ŦX','" + str328DeputyId
                + "','" + str328DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH" + str328DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }

          // ���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
          // Sale05M070
          strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȧ���','" + strActionName + "','�Nú�ڤH" + str328DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + str328DeputyId + "','" + str328DeputyName + "','" + strEDate + "','RY','773','008','�Nú�ڤH" + str328DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
          // AS400
          strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
              + "', '" + str328DeputyId + "', '" + str328DeputyName + "', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJGENLIB.execFromPool(strJGENLIBSql);
          errMsg += "�Ȧ�Nú�ڤH" + str328DeputyName + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";

          // �Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
          if ("Y".equals(str328rStatus)) {
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȧ���','" + strActionName + "', '�Nú�ڤH" + str328DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + str328DeputyId + "','" + str328DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str328DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str328DeputyId + "', '" + str328DeputyName + "', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','"
                + strNowTime + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);

            errMsg += "�Ȧ�Nú�ڤH" + str328DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȧ���','" + strActionName + "', '���ŦX','" + str328DeputyId
                + "','" + str328DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str328DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        } else {
          // ���Hú��(���A��)5,8,10,17,19,20,21
          int[] noUseAML1 = { 5, 8, 10, 17, 19, 20, 21 };
          mapAMLMsg = amlTool.getAMLReTurn();
          for (int ii = 0; ii < noUseAML1.length; ii++) {
            String amlNo = "";
            if (noUseAML1[ii] < 10) {
              amlNo = "00" + noUseAML1[ii];
            } else {
              amlNo = "0" + noUseAML1[ii];
            }
            String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
                .replaceAll("<customTitle2>", "");
            strSaleSql = "INSERT INTO Sale05M070 "
                + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
                + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
                + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
                + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        }
      }
    }

    // ����
    ret082Table = getTableData("table2");
    if (ret082Table.length > 0) {
      for (int g = 0; g < ret082Table.length; g++) {
        String str082Deputy = ret082Table[g][8].trim();// ���Hú��
        String str082DeputyName = ret082Table[g][9].trim();// �m�W
        String str082DeputyId = ret082Table[g][10].trim();// �����Ҹ�
        String str082Rlatsh = ret082Table[g][11].trim();// ���Y
        String str082Bstatus = ret082Table[g][13].trim();// �¦W��
        String str082Cstatus = ret082Table[g][14].trim();// ���ަW��
        String str082Rstatus = ret082Table[g][15].trim();// �Q���H

        // ���A��2,3,4,6,7,9,10,11,12,15,16
        int[] noUseAML = { 2, 3, 4, 6, 7, 9, 10, 11, 12, 15, 16 };
        Map mapAMLMsg = amlTool.getAMLReTurn();
        for (int ii = 0; ii < noUseAML.length; ii++) {
          String amlNo = "";
          if (noUseAML[ii] < 10) {
            amlNo = "00" + noUseAML[ii];
          } else {
            amlNo = "0" + noUseAML[ii];
          }
          String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
              .replaceAll("<customTitle2>", "");
          strSaleSql = "INSERT INTO Sale05M070 "
              + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
              + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
              + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
              + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
        }

        if ("Y".equals(str082Deputy)) {// ���Nú�H

          // �Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
          if ("�B��".equals(str082Rlatsh) || "��L".equals(str082Rlatsh)) {
            // Sale05M070
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','���ڸ��','" + strActionName + "','�Nú�ڤH" + str082DeputyName
                + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + str082DeputyId + "','" + str082DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH"
                + str082DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str082DeputyId + "', '" + str082DeputyName + "', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime
                + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);

            errMsg += "���ڥNú�ڤH" + str082DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','���ڸ��','" + strActionName + "','���ŦX','" + str082DeputyId
                + "','" + str082DeputyName + "','" + strEDate + "','RY','773','005','�Nú�ڤH" + str082DeputyName + "�P�Ȥ�" + allOrderName + "�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
          // ���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
          // Sale05M070
          strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
              + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','���ڸ��','" + strActionName + "','�Nú�ڤH" + str082DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + str082DeputyId + "','" + str082DeputyName + "','" + strEDate + "','RY','773','008','�Nú�ڤH" + str082DeputyName
              + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbSale.execFromPool(strSaleSql);
          intRecordNo++;
          // AS400
          strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
              + "', '" + str082DeputyId + "', '" + str082DeputyName + "', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
          dbJGENLIB.execFromPool(strJGENLIBSql);
          errMsg += "���ڥNú�ڤH" + str082DeputyName + "�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";

          // �Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
          if ("Y".equals(str082Rstatus)) {
            // Sale05M070
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','���ڸ��','" + strActionName + "','�Nú�ڤH" + str082DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + str082DeputyId + "','" + str082DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str082DeputyName
                + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
            // AS400
            strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
                + "', '" + str082DeputyId + "', '" + str082DeputyName + "', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','" + RocNowDate + "','"
                + strNowTime + "')";
            dbJGENLIB.execFromPool(strJGENLIBSql);
            errMsg += "���ڥNú�ڤH" + str082DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
          } else {
            // ���ŦX
            strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"
                + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','���ڸ��','" + strActionName + "','���ŦX','" + str082DeputyId
                + "','" + str082DeputyName + "','" + strEDate + "','RY','773','019','�Nú�ڤH" + str082DeputyName + "�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','" + empNo + "','"
                + RocNowDate + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        } else {// ���Hú��
          // ���A��5,8,17,19,20,21
          int[] noUseAML1 = { 5, 8, 17, 19, 20, 21 };
          mapAMLMsg = amlTool.getAMLReTurn();
          for (int ii = 0; ii < noUseAML1.length; ii++) {
            String amlNo = "";
            if (noUseAML1[ii] < 10) {
              amlNo = "00" + noUseAML1[ii];
            } else {
              amlNo = "0" + noUseAML1[ii];
            }
            String amlDesc = mapAMLMsg.get(amlNo).toString().replaceAll("<customName>", "").replaceAll("<customTitle>", "").replaceAll("<customName2>", "")
                .replaceAll("<customTitle2>", "");
            strSaleSql = "INSERT INTO Sale05M070 "
                + "(DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
                + "VALUES " + "('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�H�Υd���','" + strActionName
                + "', '���A��','" + allCustomID + "'" + ",'" + allCustomName + "','" + strEDate + "','RY','773','" + amlNo + "','" + amlDesc + "'" + ",'" + empNo + "','" + RocNowDate
                + "','" + strNowTime + "')";
            dbSale.execFromPool(strSaleSql);
            intRecordNo++;
          }
        }
      }
    }

    // 13.�Ȥ��I���ʲ�������ڶ��A�H�{�r��I�q���H�~�U�����ڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C
    if ("Y".equals(rule13)) {
      // Sale05M070
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
          + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','�Ȥ�" + allCustomName
          + "�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','" + allCustomID + "','" + allCustomName + "','" + strEDate + "','RY','773','013','�Ȥ�" + allCustomName
          + "�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
      // AS400
      strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate + "', '"
          + strDeputyID + "', '" + strDeputyName + "', '773', '013', '�Ȥ��I���ʲ�������ڶ��A�H�{�r��I�q���H�~�U�����ڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime
          + "')";
      dbJGENLIB.execFromPool(strJGENLIBSql);

      errMsg += "�Ȥ�" + allCustomName + "�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C\n";
    } else {
      // ���ŦX
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
          + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','���ŦX','" + allCustomID + "','"
          + allCustomName + "','" + strEDate + "','RY','773','013','�Ȥ�" + allCustomName + "�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime
          + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
    }

    // 14.�Ȥ��ñ���e���e�I�M�۳ƴڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C
    if ("Y".equals(rule14)) {
      // Sale05M070
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
          + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','�Ȥ�" + allCustomName
          + "ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','" + allCustomID + "','" + allCustomName + "','" + strEDate + "','RY','773','014','�Ȥ�" + allCustomName
          + "ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
      // AS400
      strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '" + strDocNo + "', '" + RocNowDate + "', '"
          + strDeputyID + "', '" + strDeputyName + "', '773', '014', '�Ȥ��ñ���e���e�I�M�۳ƴڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbJGENLIB.execFromPool(strJGENLIBSql);

      errMsg += "�Ȥ�" + allCustomName + "ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C\n";
    } else {
      // ���ŦX
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"
          + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','���ŦX','" + allCustomID + "','"
          + allCustomName + "','" + strEDate + "','RY','773','014','�Ȥ�" + allCustomName + "ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','" + empNo + "','" + RocNowDate + "','" + strNowTime
          + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
    }

    // 22. Rule22 ���x�n�D�t�סA�ҥH�ӤW���H�K�g�g (��@��S�S�H�K�g�g�F)
    String rule22 = getValue("Rule22").trim();
    String amlDesc22 = ksUtil.getAMLDescOne("022").replaceAll("<customName>", allCustomName).replaceAll("<customTitle>", "�Ȥ�");
    if (StringUtils.equals(rule22, "Y")) {
      // Sale05M070
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
          + "VALUES ('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','" + amlDesc22
          + "','" + allCustomID + "','" + allCustomName + "','" + strEDate + "','RY','773','022','" + amlDesc22 + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
      // AS400
      strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) " + "VALUES ('RY', '" + strDocNo + "', '" + RocNowDate
          + "', '" + strDeputyID + "', '" + strDeputyName + "', '773', '022', '" + amlDesc22 + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbJGENLIB.execFromPool(strJGENLIBSql);

      errMsg += amlDesc22 + "\n";
    } else {
      // ���ŦX
      strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) "
          + "VALUES ('" + strDocNo + "','" + strOrderNo + "','" + strProjectID1 + "','" + intRecordNo + "','" + actionNo + "','����','�Ȥ���','" + strActionName + "','���ŦX','"
          + allCustomID + "','" + allCustomName + "','" + strEDate + "','RY','773','022','" + amlDesc22 + "','" + empNo + "','" + RocNowDate + "','" + strNowTime + "')";
      dbSale.execFromPool(strSaleSql);
      intRecordNo++;
    }

    if (!"".equals(errMsg)) {
      setValue("errMsgBoxText", errMsg);
      getButton("errMsgBoxBtn").doClick();
      getButton("sendMail").doClick();
    }

    System.out.println("value=====>" + value);
    System.out.println("===========AML============E");
    return value;
  }

  public String getInformation() {
    return "---------------AML(\u6d17\u9322\u9632\u5236).defaultValue()----------------";
  }
}
