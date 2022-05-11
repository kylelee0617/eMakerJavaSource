package Sale.Sale05M274;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import Farglory.aml.AMLTools_Lyods;
import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCustomBean;
import Farglory.util.KSqlUtils;
import Farglory.util.QueryLogBean;
import Farglory.util.Result;
import Farglory.util.TalkBean;
import jcx.db.talk;
import jcx.jform.sproc;

public class AML extends sproc {
  public String getDefaultValue(String value) throws Throwable {
    // 20191030 �~�����ަW��LOG
    System.out.println("==============Sale05M274 AML START====================================");
    talk dbSale = getTalk("Sale");

    // config
    Map config = (HashMap) get("config");
    boolean isTest = "PROD".equals(config.get("serverType").toString()) ? false : true;
    String lyodsSoapURL = config.get("lyodsSoapURL").toString();

    TalkBean tBean = new TalkBean();
    tBean.setDb400CRM(getTalk("400CRM"));
    tBean.setDbPw0D(getTalk("pw0d"));
    tBean.setDbSale(getTalk("Sale"));
    tBean.setDbEIP(getTalk("EIP"));
    
    KSqlUtils kSqlUtil = new KSqlUtils(tBean);
    Result rs = new Result();
    String stringSQL = "";
    String errMsg = "";
    String funcName = value.trim();
    String recordType = "";
    String actionName = getValue("text11").trim();
    String projectId = getValue("ProjectID1").trim();
    String orderNo = "";
    String orderDate = "";
    StringBuilder custNames = new StringBuilder();

    // �e����
    String stringContractNo = getValue("ContractNoDisplay").trim();

    // �q���T
    orderNo = kSqlUtil.getOrderNoByContractNo(stringContractNo);
    if (StringUtils.isBlank(orderNo)) {
      System.out.println(this.getClass().toString() + ": �d�L�q��s�� by contractNo:" + stringContractNo);
      return value;
    }
    orderDate = kSqlUtil.getOrderDateByOrderNo(orderNo);
    
    AMLyodsBean aBean = new AMLyodsBean();
    aBean.setDb400CRM(getTalk("400CRM"));
    aBean.setDbPw0D(getTalk("pw0d"));
    aBean.setDbSale(tBean.getDbSale());
    aBean.setDbEIP(getTalk("EIP"));
    aBean.settBean(tBean);
    aBean.setProjectID1(projectId);
    aBean.setOrderNo(orderNo);
    aBean.setOrderDate(orderDate);
    aBean.setFunc(funcName);
    aBean.setRecordType(recordType);
    aBean.setActionName(actionName);
    aBean.setEmakerUserNo(getUser());
    aBean.setTestServer(isTest);
    aBean.setLyodsSoapURL(lyodsSoapURL);

    stringSQL = "SELECT ProjectID1,ContractDate FROM Sale05M274 WHERE ContractNo='" + stringContractNo + "'";
    String[][] retSale = dbSale.queryFromPool(stringSQL);
    if (retSale.length > 0) {// ���X���O��
      String strProjectID1 = retSale[0][0].trim();
      String strContractDate = retSale[0][1].trim();

      // �Ȥ���
      stringSQL = "SELECT CustomNo, CustomName, CountryName, IsBlackList, IsControlList, IsLinked FROM Sale05M091 WHERE ORDERNO = '" + orderNo + "' "
          + "and ISNULL(StatusCd , '') != 'C' ";
      String[][] custData = dbSale.queryFromPool(stringSQL);
      if (custData.length > 0) {
        aBean.setRecordType("�Ȥ���");
        AMLTools_Lyods aml = new AMLTools_Lyods(aBean);

        for (int m = 0; m < custData.length; m++) {
          String custNo = custData[m][0].trim();
          String custName = custData[m][1].trim();
          QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo);

          RiskCustomBean cBean = new RiskCustomBean();
          cBean.setCustTitle("�Ȥ�");
          cBean.setCustomNo(custNo); // �����Ҧr��
          cBean.setCustomName(custName); // �m�W
          cBean.setBirthday(qBean.getBirthday()); // �ͤ�
          cBean.setIndustryCode(qBean.getJobType()); // �~�O
          cBean.setCountryName(custData[m][2].trim()); // ��W
          cBean.setbStatus(custData[m][3].trim()); // �¦W��
          cBean.setcStatus(custData[m][4].trim()); // ���ަW��
          cBean.setrStatus(custData[m][5].trim()); // �Q�`���Y�H
          cBean.setqBean(qBean);

          // 18. ����W��
          errMsg += (aml.chkAML018_San(cBean).getData()).toString().trim();

          // 21. PEPS
          errMsg += (aml.chkAML021_PEPS(cBean).getData()).toString().trim();

          // �]���X���|�f���ĤT�H��e�U�H���S��"�D�H"�A�G�H�Ȥᶰ�X�@���D�H�W
          custNames.append(m > 0 ? "," : "").append(custName);
        }
      }

      // �Q�e�U�H
      stringSQL = "SELECT IsChoose FROM Sale05M279 where ContractNo ='" + stringContractNo + "' and   ItemCd='O01' and ItemlsCd = 'Other2'";
      String[][] retOther2 = dbSale.queryFromPool(stringSQL);
      if (retOther2.length > 0) {// �Q�e�U�H START
        String strIsChoose = retOther2[0][0].trim();
        if ("Y".equals(strIsChoose)) {
          stringSQL = "SELECT TrusteeName,TrusteeId,Blacklist,Controllist,Stakeholder,Relation FROM Sale05M355 where ContractNo ='" + stringContractNo + "'";
          String[][] retM355 = dbSale.queryFromPool(stringSQL);
          if (retM355.length > 0) {
            aBean.setRecordType("�Q�e�U�H���");
            AMLTools_Lyods aml = new AMLTools_Lyods(aBean);

            for (int m = 0; m < retM355.length; m++) {
              String custNo = retM355[m][1].trim();
              String custName = retM355[m][0].trim();
              QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo);

              RiskCustomBean cBean = new RiskCustomBean();
              cBean.setCustTitle("�Q�e�U�H");
              cBean.setCustomNo(custNo); // �����Ҧr��
              cBean.setCustomName(custName); // �m�W
              cBean.setBirthday(qBean.getBirthday()); // �ͤ�
              cBean.setIndustryCode(qBean.getJobType()); // �~�O
              cBean.setbStatus(retM355[m][2].trim()); // �¦W��
              cBean.setcStatus(retM355[m][3].trim()); // ���ަW��
              cBean.setrStatus(retM355[m][4].trim()); // �Q�`���Y�H
              cBean.setAgentRel(retM355[m][5].trim());// ��]
              cBean.setCustTitle2("�Ȥ�");
              cBean.setCustomName2(custNames.toString()); // �D�Hname
              cBean.setqBean(qBean);

              // ���A��LOG1,2,3,4,6,7,9~16
              int[] noUseAML = { 1, 2, 3, 4, 6, 7, 9, 10, 11, 12, 13, 14, 15, 16 };
              aml.insNotUse(noUseAML, cBean);

              // 5. ���Y�D�G����ÿ�
              errMsg += (aml.chkAML005(cBean).getData()).toString().trim();

              // 8. �~���ĤK��
              errMsg += (aml.chkAML008(cBean).getData()).toString().trim();

              // 17.�¦W��&���ަW��
              errMsg += (aml.chkAML017(cBean).getData()).toString().trim();

              // 18. ����W��
              errMsg += (aml.chkAML018_San(cBean).getData()).toString().trim();

              // 19.�Q�`���Y�H
              errMsg += (aml.chkAML019(cBean).getData()).toString().trim();

              // 21. PEPS
              errMsg += (aml.chkAML021_PEPS(cBean).getData()).toString().trim();
            }
          }
        }
      } // �Q�e�U�H END

      // ���w�ĤT�H
      stringSQL = "SELECT IsChoose FROM Sale05M279 where ContractNo ='" + stringContractNo + "' and   ItemCd='O01' and ItemlsCd = 'Other44'";
      String[][] retOther44 = dbSale.queryFromPool(stringSQL);
      if (retOther44.length > 0) {// ���w�ĤT�H START
        String strIsChoose = retOther44[0][0].trim();
        if ("Y".equals(strIsChoose)) {
          stringSQL = "SELECT DesignatedName,DesignatedId,Blacklist,Controllist,Stakeholder,ExportingPlace,Relation FROM Sale05M356 where ContractNo ='" + stringContractNo + "'";
          String[][] retM356 = dbSale.queryFromPool(stringSQL);
          if (retM356.length > 0) {
            aBean.setRecordType("�Q�e�U�H���");
            AMLTools_Lyods aml = new AMLTools_Lyods(aBean);

            for (int m = 0; m < retM356.length; m++) {
              String custNo = retM356[m][1].trim();
              String custName = retM356[m][0].trim();
              QueryLogBean qBean = kSqlUtil.getQueryLogByCustNoProjectId(projectId, custNo);

              RiskCustomBean cBean = new RiskCustomBean();
              cBean.setCustTitle("�Q�e�U�H");
              cBean.setCustomNo(custNo); // �����Ҧr��
              cBean.setCustomName(custName); // �m�W
              cBean.setBirthday(qBean.getBirthday()); // �ͤ�
              cBean.setIndustryCode(qBean.getJobType()); // �~�O
              cBean.setbStatus(retM356[m][2].trim()); // �¦W��
              cBean.setcStatus(retM356[m][3].trim()); // ���ަW��
              cBean.setrStatus(retM356[m][4].trim()); // �Q�`���Y�H
              cBean.setCountryName(retM356[m][5].trim()); // ��O
              cBean.setAgentRel(retM356[m][6].trim());// ��]
              cBean.setCustTitle2("�Ȥ�");
              cBean.setCustomName2(custNames.toString()); // �D�Hname
              cBean.setqBean(qBean);

              // ���A��LOG1~4,6, 7, 8, 10,13~16
              int[] noUseAML = { 1, 2, 3, 4, 6, 7, 8, 9, 10, 13, 14, 15, 16 };
              aml.insNotUse(noUseAML, cBean);

              // 5. ���Y�D�G����ÿ�
              errMsg += (aml.chkAML005(cBean).getData()).toString().trim();

              // 9. �ꮣ�a��
              errMsg += (aml.chkAML009(cBean).getData()).toString().trim();

              // 12. �X���ĤT�H
              errMsg += (aml.chkAML012(cBean).getData()).toString().trim();

              // 17.�¦W��&���ަW��
              errMsg += (aml.chkAML017(cBean).getData()).toString().trim();

              // 18. ����W��
              errMsg += (aml.chkAML018_San(cBean).getData()).toString().trim();

              // 19.�Q�`���Y�H
              errMsg += (aml.chkAML019(cBean).getData()).toString().trim();

              // 21. PEPS
              errMsg += (aml.chkAML021_PEPS(cBean).getData()).toString().trim();

            }
          }
        }
      } // ���w�ĤT�H END
    }
    if (!"".equals(errMsg)) {
      messagebox(errMsg.trim().replaceAll("<br>", "\n"));
    }
    System.out.println("==============�~�����v�ˮ�LOG END====================================");
    return value;
  }

  public String getInformation() {
    return "---------------\u6d17\u9322\u9632\u5236(AML).defaultValue()----------------";
  }
}
