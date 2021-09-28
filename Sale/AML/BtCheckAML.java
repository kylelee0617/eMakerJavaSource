package Sale.AML;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fglife.soap.cr.RenewRelatedReply;

import Farglory.aml.AMLTools_Lyods;
import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCustomBean;
import Farglory.util.Result;
import Farglory.util.ResultStatus;
import Farglory.util.TalkBean;
import jcx.db.talk;

/**
 * �����n���@�� server side �����s�A�L�k�����q�e�ݨϥΫ�ݪ���
 * 
 * �ǤJ��: (�ťեΥb�ΪŮ��ܡA���X�᪽��trim��) 0.orderNo 1.orderDate 2.custNo 3.custName
 * 4.birthday 5.industryCode
 * 
 * @author B04391
 *
 */
public class BtCheckAML extends jcx.jform.sproc {
  public String getDefaultValue(String value) throws Throwable {
    String rsMsg = "";
    Result result = null;

    // config
    Map config = (HashMap) get("config");
    boolean isTest = "PROD".equals(config.get("serverType").toString()) ? false : true;
    String lyodsSoapURL = config.get("lyodsSoapURL").toString();

//    String[][] checkAMLs = (String[][]) get("BTCHECKAML_RQ");
//    System.out.println(">>>BTCHECKAML_RQ:" + get("BTCHECKAML_RQ"));
    
    String[][] checkAMLs = getTableData("TableCheckAML");
    System.out.println(">>>test111:" + checkAMLs.length);
    
    for (int i = 0; i < checkAMLs.length; i++) {
      /**
       * Param
       * 
       * 0. projectId �קO�N�X 1. orderNo �q��s�� 2. orderDate �q���� 3. func �\��j 4. recordType �\��p 
       * 5. custNo �Ȥ�ID 6. custName �Ȥ�name 7. birth �ͤ�/���U�� 8. indCode ��~�O�N�X 
       * 9. processType ����N�X
       */
      String[] arrParam = getTableData("TableCheckAML")[i];
      String projectId = arrParam[0].toString().trim();
      String orderNo = arrParam[1].toString().trim();
      String orderDate = arrParam[2].toString().trim();
      String func = arrParam[3].toString().trim();
      String recordType = arrParam[4].toString().trim();
      String custNo = arrParam[5].toString().trim();
      String custName = arrParam[6].toString().trim();
      String birth = arrParam[7].toString().trim();
      String indCode = arrParam[8].toString().trim();
      String countryName = arrParam[9].toString().trim();
      String countryName2 = arrParam[10].toString().trim();
      String engName = arrParam[11].toString().trim();
      String processType = arrParam[12].toString().trim();

      // ����ˮ�
      if (orderNo.length() == 0) {
        rsMsg = "<BtCustAML�L�q��s���A�{�ǲפ�>";
        return value;
      }
      if (orderDate.length() == 0) {
        rsMsg = "<BtCustAML�L�q�����A�{�ǲפ�>";
        return value;
      }

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

      AMLyodsBean aBean = new AMLyodsBean();
      aBean.setProjectID1(projectId);
      aBean.setOrderNo(orderNo);
      aBean.setOrderDate(orderDate.replaceAll("/", "").replaceAll("-", ""));
      aBean.setFunc(func);
      aBean.setRecordType(recordType);
      aBean.setEmakerUserNo(getUser());
      aBean.setTestServer(isTest);
      aBean.setLyodsSoapURL(lyodsSoapURL);
      aBean.setDb400CRM(db400CRM);
      aBean.setDbSale(dbSale);
      aBean.setDbEIP(dbEIP);
      aBean.settBean(tBean);

      RiskCustomBean cBean = new RiskCustomBean();
      cBean.setCustomNo(custNo);
      cBean.setCustomName(custName);
      cBean.setBirthday(birth.replaceAll("/", "").replaceAll("-", ""));
      cBean.setIndustryCode(indCode);
      cBean.setCountryName(countryName);
      cBean.setCountryName2(countryName2);
      cBean.setEngName(engName);

      if ("query1821".equals(processType)) { // �d��PEPS or ���
        // ����W��
        AMLTools_Lyods aml = new AMLTools_Lyods(aBean);
        result = aml.chkAML018_San(cBean);
        if (ResultStatus.SUCCESS[0].equals(result.getRsStatus()[0])) {
          rsMsg += result.getData().toString().trim() + "\n";
        }
        // PEPS
        result = aml.chkAML021_PEPS(cBean);
        if (ResultStatus.SUCCESS[0].equals(result.getRsStatus()[0])) {
          rsMsg += result.getData().toString().trim() + "\n";
        }
//      rsMsg += aml.getLyodsHits(cBean);  //�ݩR���ƻ�
      } 
      else if ("query18".equals(processType)) { // �u�ݨ��
        // ����W��
        AMLTools_Lyods aml = new AMLTools_Lyods(aBean);
        result = aml.chkAML018_San(cBean);
        if (ResultStatus.SUCCESS[0].equals(result.getRsStatus()[0])) {
          rsMsg += result.getData().toString().trim() + "\n";
        }
        System.out.println(">>>query18 rsMsg:" + rsMsg);
      } 
      else if ("updRelated".equals(processType)) { // ��s���p�H
        AMLTools_Lyods aml = new AMLTools_Lyods(aBean);
        result = aml.renewRelated(cBean);
        if (ResultStatus.SUCCESS[0].equals(result.getRsStatus()[0])) {
          RenewRelatedReply related = (RenewRelatedReply) result.getData();
          rsMsg += related.getResult().toString().trim() + "\n";
        }
      } 
      else if ("deleteRelated".equals(processType)) { // ���P���p�H
        aBean.setAddAccount("N");
        aBean.setAddCustomer("N");
        AMLTools_Lyods aml = new AMLTools_Lyods(aBean);
        result = aml.renewRelated(cBean);
        if (ResultStatus.SUCCESS[0].equals(result.getRsStatus()[0])) {
          RenewRelatedReply related = (RenewRelatedReply) result.getData();
          rsMsg += related.getResult().toString().trim() + "\n";
        }
      }
    }
    
//    put("BTCHECKAML_RS", rsMsg);
    setValue("AMLText", rsMsg);
    
    return value;
  }

  public String getInformation() {
    return "---------------button7(button7).defaultValue()----------------";
  }
}
