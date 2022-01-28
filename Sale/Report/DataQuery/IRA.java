package Sale.Report.DataQuery;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import org.apache.commons.lang.StringUtils;
import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import Farglory.util.QueryLogBean;
import jcx.jform.bproc;

public class IRA extends bproc{
  KSqlUtils ksUtil = new KSqlUtils();
  KUtils kUtil = new KUtils();
  
  //param
  String year = "";
  
  public String getDefaultValue(String value)throws Throwable{  
    
    //TODO: �d�߱���
    year = this.getValue("��P-Year").trim();
    
    //TODO: �d�߸��
    String[][] retTable = this.getMainData();

    //�qqueryLOG�ɥR�ʤ֪����
    int tableCount = 0;
    for(int i=0; i<retTable.length; i++) {
      String projectId = retTable[i][1].trim();
      String custNoss = retTable[i][17].trim(); //17  19
      String benNoss = retTable[i][27].trim();  //27  29  31
      
      //�Ȥ�
      if(StringUtils.isNotBlank(custNoss)) {  //���Ȥ���
        String[] custNos = custNoss.split(","); //��
        
        StringBuilder sbCustNo = new StringBuilder(); 
        for(int c=0; c<custNos.length; c++) { //�C�@�ӤH�B�z
          if(sbCustNo.length() > 0) sbCustNo.append(" , ");
          
          QueryLogBean qBean = ksUtil.getQueryLogLike3(projectId, custNos[c].trim());
          if(qBean == null) {
            KUtils.info(projectId + "/" + custNos[c].trim() + " �¦W��L���");
            continue;
          }
          
          String sexCode = qBean.getSex();
          if(StringUtils.equals(sexCode, "M")) {
            sbCustNo.append("�k");
          }else if(StringUtils.equals(sexCode, "F")) {
            sbCustNo.append("�k");
          }
        }
        retTable[i][19] = sbCustNo.toString();  //�^��}�C
      }
      
      //����H
      if(StringUtils.isNotBlank(benNoss)) { //������H���
        String[] benNos = benNoss.split(","); //��
        
        StringBuilder sbBenNo = new StringBuilder();
        for(int c=0; c<benNos.length; c++) {  //�C�@�ӤH�B�z
          if(sbBenNo.length() > 0) sbBenNo.append(" , ");
          
          QueryLogBean qBean = ksUtil.getQueryLogLike3(projectId, benNos[c].trim());
          if(qBean == null) {
            KUtils.info(projectId + "/" + benNos[c].trim() + " null");
            continue;
          }
          //�ʧO
          String sexCode = qBean.getSex();
          if(StringUtils.equals(sexCode, "M")) {
            sbBenNo.append("�k");
          }else if(StringUtils.equals(sexCode, "F")) {
            sbBenNo.append("�k");
          }
          retTable[i][29] = sbBenNo.toString();
          //¾�~(code��name)
          retTable[i][31] = ksUtil.getNameByIndCode(qBean.getJobType());
        } 
      }
      
      tableCount++;
    }
    this.setValue("TableCount", "�@" + tableCount + "��");
    
    //TODO: �g���
    //���Y
    String tmpHeader = "�q��s���B�קO�B��O�B����O�B������B(�U)�B�I�q��Bñ����B"
        + "���ڤ���B���ڪ��B(��)�B�H�Υd(��)�B�{��(��)�B�Ȧ�(��)�B����(��)�B�������B�B������]�B"
        + "�h���B�h���]�B�h�ڪ��B�B"
        + "�Ȥ���y�B�Ȥ�ID�B�Ȥ�m�W�B�Ȥ�ʧO�B�Ȥ�X�ͤ���B�Ȥ�~�O�B�Ȥ᭷�I�ȡB�Ȥ�PEPS�B�Ȥ�¦W��B�Ȥᱱ�ަW��B"
        + "����H���y�B����HID�B����H�m�W�B����H�ʧO�B����H�X�ͤ���B����H�~�O�B����HPEPS�B����H�¦W��B����H���ަW��";
    String[] tableHeader = tmpHeader.split("�B");
    JTable tb1 = getTable("ResultTable");
    tb1.setName("IRA �ݨ��ƾ�");
    this.setTableHeader("ResultTable", tableHeader);

    //��NAME
    this.setValue("TableName", "IRA �ݨ��ƾ�");
    
    this.setTableData("ResultTable", retTable);   //���A�D���
    
    //sale log
    ksUtil.setSaleLog(this.getFunctionName(), "IRA Data", this.getUser(), "�d��IRA��Ʀ��\");
    
    return value;
  }
  
  public String[][] getMainData() throws Throwable{
    String sql = "select DISTINCT " + 
        "a.OrderNo as �q��s��" + 
        ", a.ProjectID1 AS �קO" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 3, '') , '') as �ɼӧO" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 3, '') , '') as ����O" +
        ", ISNULL( (SELECT SUM(aa.DealMoney) FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and HouseCar='house' GROUP BY aa.OrderNo) , 0) + ISNULL( (SELECT SUM(aa.DealMoney) FROM Sale05M092 aa WHERE aa.OrderNo = a.OrderNo and HouseCar='car' GROUP BY aa.OrderNo) , 0) as ������B" +
        ", a.orderdate as �I�q��" + 
        ",ISNULL( (select top 1 aa.contractDate  from Sale05M274 aa , Sale05M278 bb where aa.ContractNo=bb.ContractNo and bb.OrderNo=a.OrderNo order by aa.ContractDate desc) , '') AS ñ����" +
        ", b.EDate as ���ڤ�� " +
        ", b.ReceiveMoney as ���ڪ��B " +
        ",ISNULL((select sum(aa.CreditCardMoney) from sale05m083 aa where aa.docNo = b.DocNo), '0') as �H�Υd " +
        ", b.CashMoney as �{�� " +
        ",ISNULL((select sum(aa.BankMoney) from sale05m328 aa where aa.docNo = b.DocNo), '0') as �Ȧ� " + 
        ",ISNULL((select sum(aa.CheckMoney) from sale05m082 aa where aa.docNo = b.DocNo), '0') as ���� " +
        ", b.DiscountTotalMoney as �������B " +
        ", b.DiscountReason as ������] " +
        ",ISNULL( (select TrxDate from Sale05M094 aa where aa.OrderNo=a.OrderNo) , '') AS �h����" + 
        ",ISNULL( (select top 1 (select top 1 aaa.BItemName from Sale05M230 aaa where aaa.BItemCd=aa.BItemCd) + '-' + (select top 1 aaa.MItemName from Sale05M231 aaa where aaa.MItemCd=aa.MItemCd) + '-' + (select top 1 aaa.SItemName + '-' + aaa.DItemName from Sale05M233 aaa where aaa.MItemCd=aa.MItemCd and aaa.SItemCd=aa.SItemCd and aaa.DItemCd=aa.DItemCd) from Sale05M094 aa where aa.OrderNo=a.OrderNo order by TrxDate desc) , '') AS �h���]" + 
        ",ISNULL( (select top 1 aa.Amt from Sale05M094 aa where aa.OrderNo=a.OrderNo order by TrxDate desc) , '') AS �h�ڪ��B" +
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.CountryName) FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as �Ȥ���y" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.customNo FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '�Ȥ�ID'" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '�Ȥ�Name'" +
        ", '' as �Ȥ�ʧO" + 
        ",ISNULL( STUFF( (SELECT ',' + aa.Birthday FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�ͤ�" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.MajorName) FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as �Ȥ�¾�~" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.RiskValue FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as �Ȥ᭷�I��" + 
        ", case when (select count(*) from Sale05M070 aa where aa.SHB06B = '021' and RecordDesc like '�Ȥ�%' and aa.OrderNo = a.orderNo) > 0 then 'Y' else '' end as �Ȥ�PEPS" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsBlackList FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as �Ȥ�¦W��" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsControlList FROM Sale05M091 aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as �Ȥᱱ�ަW��" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.CountryName) FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as ����H���y" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.BCustomNo FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '����HID'" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.BenName FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '') as '����HName'" +
        ", '' as ����H�ʧO" + 
        ", ISNULL( STUFF( (SELECT ' , ' + RTRIM(aa.Birthday) FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as ����H�ͤ�" + 
        ", '' as ����H¾�~" + 
        ", case when (select count(*) from Sale05M070 aa where aa.SHB06B = '021' and RecordDesc like '�����q�H%' and aa.OrderNo = a.orderNo) > 0 then 'Y' else '' end as ����HPEPS" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsBlackList FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as ����H�¦W��" + 
        ", ISNULL( STUFF( (SELECT ' , ' + aa.IsControlList FROM Sale05M091Ben aa WHERE aa.OrderNo = a.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 3, '') , '-') as ����H���ަW�� " + 
        "from Sale05M090 a , Sale05M080 b , Sale05M086 c " +
        "where b.DocNo = c.DocNo and a.OrderNo = c.OrderNo " + 
        "and a.OrderDate BETWEEN '"+year+"/01/01' AND '"+year+"/12/31' " + 
        "order by a.OrderNo ASC ";
    String[][] retTable = ksUtil.getTBean().getDbSale().queryFromPool(sql);
    
    return retTable;
  }
  
  
  public String getInformation(){
    return "---------------Query(\u67e5\u8a62).defaultValue()----------------";
  }
}
