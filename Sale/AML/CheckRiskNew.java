package Sale.AML;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCheckTools_Lyods;
import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.RiskCheckRS;
import Farglory.util.SendMailBean;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.util.datetime;

/**
 * ���\��b��Ʈw���ʫ����A �G�q��Ʈw���o�D�n�Ȥ��ơA�i�����e���v�T�P�ʫ��ҩ���@��
 * 
 * @author B04391
 *
 */
public class CheckRiskNew extends jcx.jform.sproc {

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("Class >>> Sale.AML.ChekRiskNew");
    System.out.println(datetime.getTime("h:m:s"));

    // config
    Map config = (HashMap) get("config");
    boolean isTest = "PROD".equals(config.get("serverType").toString()) ? false : true;
    String lyodsSoapURL = config.get("lyodsSoapURL").toString();

    talk dbSale = getTalk("Sale");
    talk dbEMail = getTalk("eMail");
    talk db400CRM = getTalk("400CRM");
    talk dbEIP = getTalk("EIP");
    talk dbPw0d = getTalk("pw0d");
    TalkBean tBean = new TalkBean();
    tBean.setDbSale(dbSale);
    tBean.setDbEMail(dbEMail);
    tBean.setDb400CRM(db400CRM);
    tBean.setDbEIP(dbEIP);
    tBean.setDbPw0D(dbPw0d);
    KUtils kUtil = new KUtils(tBean);
    KSqlUtils kSqlUtil = new KSqlUtils(tBean);
    
    String funcName = value;
    String recordType = "�Ȥ᭷�I�ȭp��";
    String projectId = "";
    String orderNo = "";
    String orderDate = "";
    String actionText = "";

    // �ھڤ��P�\����o�קO�q��s������T
    System.out.println(">>>funcName:" + funcName);
    if (StringUtils.contains(funcName, "�ʫ��ҩ���")) {
      projectId = getValue("field1").trim();
      orderNo = getValue("field3").trim();
      orderDate = getValue("field2").trim();
      actionText = getValue("actionText").trim();
    } else if (StringUtils.contains(funcName, "���W")) {
      projectId = getValue("ProjectID1").trim();
      orderNo = getValue("OrderNo").trim();
      orderDate = kUtil.getOrderDateByOrderNo(orderNo);
      actionText = getValue("actionText").trim();
    } else if (StringUtils.contains(funcName, "�h��")) {
      projectId = getValue("field1").trim();
      orderNo = getValue("field3").trim();
      orderDate = kUtil.getOrderDateByOrderNo(orderNo);
      actionText = getValue("actionText").trim();
    }
    
    //�إ�AMLyodsBean����
    AMLyodsBean aBean = new AMLyodsBean();
    aBean.setProjectID1(projectId);
    aBean.setOrderNo(orderNo);
    aBean.setOrderDate(orderDate);
    aBean.setActionName(actionText);
    aBean.setFunc(funcName);
    aBean.setRecordType(recordType);
    aBean.setEmakerUserNo(getUser());
    aBean.setTestServer(isTest);
    aBean.setLyodsSoapURL(lyodsSoapURL);
    aBean.setDbSale(dbSale);
    aBean.setDbEMail(dbEMail);
    aBean.setDb400CRM(db400CRM);
    aBean.setDbEIP(dbEIP);
    aBean.setDbPw0D(dbPw0d);
    aBean.settBean(tBean);
    aBean.setUpdSale05M091(true);
    aBean.setUpd070Log(true);
    aBean.setSendMail(true);
    if (StringUtils.contains(funcName, "���P")) {
      aBean.setAddCustomer("N");
      aBean.setAddAccount("N");
      aBean.setUpdSale05M091(false);
      aBean.setUpd070Log(false);
      aBean.setSendMail(false);
    }

    // DB���X�D�n�Ȥ�W��
    RiskCustomBean[] cBeans = kSqlUtil.getCustomBean(projectId, orderNo);

    //�إ߭��I���ˮ֪���
    RiskCheckTools_Lyods risk = new RiskCheckTools_Lyods(aBean);
    
    // ���浲�G
    Result rs = risk.processRisk(cBeans);
    String[] rsStatus = rs.getRsStatus();
    System.out.println("���浲�G>>>" + rsStatus[3]);
    if( !StringUtils.equals(rs.getRsStatus()[3], ResultStatus.SUCCESS[3]) ) {
      System.out.println("�o�Ϳ��~�F�A���F�ֱ���!!");
      return value;
    }
    
    RiskCheckRS rcRs = (RiskCheckRS) rs.getData();
    System.out.println("�ܴ����浲�G>>>" + rcRs.getRsMsg());

    // ��X
    String rsMsg = "";
    if (StringUtils.contains(funcName, "���P")) {
      message("risk cancel done!");
    } else {
      rsMsg = !"".equals(rcRs.getRsMsg()) ? rcRs.getRsMsg() : "�L���I�ȵ��G";
      messagebox(rsMsg);

      // �H�oEmail
      if ( !isTest ) {
        List rsSendMailList = (List) rcRs.getSendMailList();
        for (int ii = 0; ii < rsSendMailList.size(); ii++) {
          SendMailBean smbean = (SendMailBean) rsSendMailList.get(ii);
          String sendRS = sendMailbcc(smbean.getColm1(), smbean.getColm2(), smbean.getArrayUser(), smbean.getSubject(), smbean.getContext(), null, "", "text/html");
          System.out.println("�H�oMAIL>>>" + sendRS);
        }
      }
    }

    return value;
  }

  public String getInformation() {
    return "---------------test111(test111).defaultValue()----------------";
  }
}
