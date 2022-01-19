package Invoice.Job;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;//
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import jcx.net.*;
import jcx.net.smtp;
import java.text.*;
import java.nio.channels.*;//
import Farglory.util.FargloryUtil;
import java.lang.String;

public class FatfDataSync {
  public static void main(String args[]) throws Throwable {
    // FargloryUtil fgUtil = new FargloryUtil();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // System.out.println("================= FATF data Sync Run !!! @ " +
    // sdf.format(new Date()) + " =================");
    // String DocNo = "CS0331H92A1080104001";
    // �B�z��P���ڤj�B�q��
    doBLCASH();
  }

  /*
   * 2019/02/13 == �B�z�j�B�q����� �W�[�ȪA��~�j�B�q�� �������ҳs�u400��dbTestAs400 �������ҳs�u400��dbAs400
   * 
   */
  public static void doBLCASH() throws Throwable {
    talk dbSale = new talk("mssql", "MISSQL", "sale", "salefglife", "Sale");
    talk dbTestAs400 = new talk("AS400", "172.22.8.3", "WEBERP", "ERP9908", "JCRMLIB");
    talk dbAs400 = new talk("AS400", "172.16.8.1", "WEBCR", "WEBCR", "PCRMLIB");
    talk dbFE5D65 = new talk("mssql", "172.22.14.6", "sa", "sapwdfglife", "FE5D");

    System.out.println("test doBLCASH BEGIN");

    System.out.println("do Sale notice");
    String nowDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
    nowDateTime = nowDateTime.replaceAll("/", "-");
    String stringUserID = "flife";
    String stringDeptID = "5800800";// ���ʲ���P, 5600600:���ʲ���~
    String dept0351ID = "5600600";
    String stringSql = "";
    String updateSql = "";
    String[][] retSale05M080Data = null;
    String[][] retSale05M084Data = null;
    int limitMaxAmt = 0;
    
    //���ʲ���P ���ڳ�
    stringSql = " SELECT DocNo, ProjectID1, EDate, CashMoney , DeputyID  FROM Sale05M080 WHERE EDate > '2019/02/15' AND NoticeBLCASH = 'N' AND CashMoney > " + limitMaxAmt + " ORDER BY EDate";
    retSale05M080Data = dbSale.queryFromPool(stringSql);

    if (retSale05M080Data.length > 0) {
      for(int ii=0 ; ii<retSale05M080Data.length ; ii++) {
        String docNo = retSale05M080Data[ii][0].trim();
        String projectID = retSale05M080Data[ii][1].trim();
        String cntDate = retSale05M080Data[ii][2].trim().replaceAll("/", "-") + " 00:00:00";
        int tradeAmt = (int) Double.parseDouble(retSale05M080Data[ii][3]);
        String deputyID = retSale05M080Data[ii][4].trim();
        
        stringSql = "SELECT CustomNo FROM Sale05M084 WHERE DocNo = '" + docNo.trim() + "' ORDER BY RecordNo";
        retSale05M084Data = dbSale.queryFromPool(stringSql);

        // �ǳƳq��
        String trnType = "���ʲ����ڭq��";
        String ntCode = "TWN";
        String stringCreator = "RealtySale";
        // deptNo = 5800800
        // ���� max CASH_ID
        String[][] cashID = null;
        stringSql = "SELECT max(CASH_ID) as max_cash_id FROM BLCASH";
        cashID = dbAs400.queryFromPool(stringSql);
        int lastCashID = 0;

        int insertCashID = (Integer.parseInt(cashID[0][0])) + 1;
        for (int i = 0; i < retSale05M084Data.length; i++) {
          insertCashID = insertCashID + i;
          String insertSql = "INSERT INTO BLCASH "
              + "(CASH_ID,INP_EMPNO,INP_DEPT,KEY_NO,CASE_NO,CNT_DATE,TRN_TYPE,CLIENT_ID,CLIENT_NAT,AGENT_ID,AGENT_NAT,TRN_DATE,TRN_AMOUNT,IS_VALID,CREATOR,CREATETIME) " + "VALUES "
              + "(" + insertCashID  + ",'" + stringUserID + "','" + stringDeptID + "','" + docNo.trim() + "','" + projectID + "','" + cntDate + "','" + trnType + "','"
              + retSale05M084Data[i][0] + "','" + ntCode + "','','','" + cntDate + "'," + tradeAmt + ",'Y','" + stringCreator + "','" + nowDateTime + "')";
          dbAs400.execFromPool(insertSql);
        }
        lastCashID = insertCashID + 1;

        // Kyle 20201130 mod : �]�����ˡA�{���Nú�ڤH�]�ݭn�e�h400����
        if (!"".equals(deputyID)) {
          String insertSql = "INSERT INTO BLCASH "
              + "(CASH_ID,INP_EMPNO,INP_DEPT,KEY_NO,CASE_NO,CNT_DATE,TRN_TYPE,CLIENT_ID,CLIENT_NAT,AGENT_ID,AGENT_NAT,TRN_DATE,TRN_AMOUNT,IS_VALID,CREATOR,CREATETIME) " + "VALUES "
              + "(" + lastCashID + ",'" + stringUserID + "','" + stringDeptID + "','" + docNo.trim() + "','" + projectID + "','" + cntDate + "','" + trnType + "-ú�ڤH" + "','"
              + deputyID + "','" + ntCode + "','','','" + cntDate + "'," + tradeAmt + ",'Y','" + stringCreator + "','" + nowDateTime + "')";
          dbAs400.execFromPool(insertSql);
        }

        // ��s�q���ɶ�
        updateSql = "UPDATE Sale05M080 SET NoticeBLCASH = 'Y', NoticeBLCASHdtime = '" + nowDateTime + "' WHERE DocNo = '" + docNo.trim() + "'";
        dbSale.execFromPool(updateSql);
      }
      
    }
    

    // �ȪA�t�Φ���
    System.out.println("do FE5D notice");
    String[][] retFE5D10Data = null;
    stringSql = "select DEPT_CD, DEPT_CD_1, CHECK_YMD, CHECK_FLOW_NO, LIST_NO, PAPER_AMT,REMARK from FE5D10 where AMT_KIND = '2' and PAPER_AMT > " + limitMaxAmt
        + " and REMARK not like '%BLCASH%' and CHECK_YMD > '1080215' order by CHECK_YMD";
    retFE5D10Data = dbFE5D65.queryFromPool(stringSql);
    if (retFE5D10Data.length > 0) {
      String deptCD = retFE5D10Data[0][0].trim();
      String deptCD1 = retFE5D10Data[0][1].trim();
      String projectID = deptCD + deptCD1;
      String chkYMD = retFE5D10Data[0][2].trim();
      String chkFlowNo = retFE5D10Data[0][3].trim();
      String listNo = retFE5D10Data[0][4].trim();
      String docNo = chkYMD + "-" + chkFlowNo + "-" + listNo;
      String cntDate = String.valueOf(Integer.parseInt(chkYMD.substring(0, 3)) + 1911) + "-" + chkYMD.substring(3, 5) + "-" + chkYMD.substring(5, 7) + " 00:00:00";
      int tradeAmt = (int) Double.parseDouble(retFE5D10Data[0][5]);
      String remark = retFE5D10Data[0][6].trim();

      stringSql = "select a.OBJECT_ID , b.OBJECT_ID, b.COUNTRY from FE5D05 a LEFT  JOIN FE5D55 b ON RTRIM(a.DEPT_CD)+RTRIM(a.DEPT_CD_1) = RTRIM(b.DEPT_CD)+RTRIM(b.DEPT_CD_1) and a.OBJECT_CD = b.OBJECT_CD where a.DEPT_CD = '"
          + deptCD + "' and a.DEPT_CD_1 = '" + deptCD1 + "' and a.OBJECT_CD in (select OBJECT_CD from FE5D08 where DEPT_CD = '" + deptCD + "' and DEPT_CD_1 = '" + deptCD1
          + "' and CHECK_YMD = '" + chkYMD + "' and CHECK_FLOW_NO = " + Integer.parseInt(chkFlowNo) + ")";
      String[][] retFE5D05Data = dbFE5D65.queryFromPool(stringSql);

      // �ǳƳq��
      String trnType = "���ʲ���~����";
      String ntCode = "TWN";
      String stringCreator = "RealtyCustomer";
      // ���� max CASH_ID
      String[][] cashID = null;
      stringSql = "SELECT max(CASH_ID) as max_cash_id FROM BLCASH";
      cashID = dbAs400.queryFromPool(stringSql);
      String deputyID = retFE5D05Data[0][1].trim();
      int lastCashID = 0;

      for (int i = 0; i < retFE5D05Data.length; i++) {
        int insertCashID = Integer.parseInt(cashID[0][0]) + 1 + i;
        lastCashID = insertCashID;
        String insertSql = "INSERT INTO BLCASH "
            + "(CASH_ID,INP_EMPNO,INP_DEPT,KEY_NO,CASE_NO,CNT_DATE,TRN_TYPE,CLIENT_ID,CLIENT_NAT,AGENT_ID,AGENT_NAT,TRN_DATE,TRN_AMOUNT,IS_VALID,CREATOR,CREATETIME) " + "VALUES "
            + "(" + insertCashID + ",'" + stringUserID + "','" + dept0351ID + "','" + docNo.trim() + "','" + projectID + "','" + cntDate + "','" + trnType + "','"
            + retFE5D05Data[i][0] + "','" + ntCode + "','','','" + cntDate + "'," + tradeAmt + ",'Y','" + stringCreator + "','" + nowDateTime + "')";
        dbAs400.execFromPool(insertSql);
      }

      if (!"".equals(deputyID)) {
        String insertSql = "INSERT INTO BLCASH "
            + "(CASH_ID,INP_EMPNO,INP_DEPT,KEY_NO,CASE_NO,CNT_DATE,TRN_TYPE,CLIENT_ID,CLIENT_NAT,AGENT_ID,AGENT_NAT,TRN_DATE,TRN_AMOUNT,IS_VALID,CREATOR,CREATETIME) " + "VALUES "
            + "(" + lastCashID + 1 + ",'" + stringUserID + "','" + dept0351ID + "','" + docNo.trim() + "','" + projectID + "','" + cntDate + "','" + trnType + "-ú�ڤH" + "','"
            + deputyID + "','" + ntCode + "','','','" + cntDate + "'," + tradeAmt + ",'Y','" + stringCreator + "','" + nowDateTime + "')";
        dbAs400.execFromPool(insertSql);
      }

      // ��s�q���ɶ�
      updateSql = "UPDATE FE5D10 SET REMARK = 'BLCASH " + remark + "' WHERE DEPT_CD = '" + deptCD + "' and DEPT_CD_1 = '" + deptCD1 + "' and CHECK_YMD = '" + chkYMD
          + "' and CHECK_FLOW_NO = " + Integer.parseInt(chkFlowNo) + "";
      dbFE5D65.execFromPool(updateSql);

    }
    System.out.println("test doBLCASH FINISH");
  }
}
