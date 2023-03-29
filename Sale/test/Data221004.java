package Sale.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;
import jcx.util.convert;

public class Data221004 extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    serverType = get("serverType").toString().trim();
    �v�jDate = this.getValue("StartDate").trim();
    �w�oDate = this.getValue("EndDate").trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>����:" + serverType);

    if (!this.����ˮ�()) return value;

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    JTable tb1 = getTable("ResultTable");
    tb1.setName("���ʲ���Pĵ�ܰT���M��");

    String[] title = { "�q��s��", "���ڽs��", "������", "�קO", "�ɼӧO", "����O", "�Ȥ�m�W", "���ˤHID", "���ˤH�m�W"
        , "�\��O", " �æ��~���������", " �æ��~���A�ˤ��e", "ú�ڪ��B", "ú�ڤ覡", "�״ڦa", "�N�z�H", "���Y", "�N�z��]" };
    this.setTableHeader("ResultTable", title);
    
    String sql = "select DISTINCT   " //�ʫ��ҩ��� - �Ȥ�
        + "t70.OrderNo as �q��s��,  " 
        + "t70.DocNo as ���ڽs��,   " 
        + "t70.OrderDate as ������,   " 
        + "t70.ProjectID1 as �קO,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "t70.CustomName as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O,   " 
        + "t70.RecordType as �æ��~���������,   " 
        + "t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "'' as ú�ڪ��B,   " 
        + "'' as ú�ڤ覡,   " 
        + "'' as �״ڦa,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z�H,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as ���Y,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�ʫ�%' and t70.RecordType like '�Ȥ�%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
//        + "AND RecordDesc not like '���I��:%'  " 
        + "  " 
        + "UNION   "  //�ʫ��ҩ��� - ����H
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.OrderDate as ������, t70.ProjectID1 as �קO,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "'' as ú�ڪ��B,   " 
        + "'' as ú�ڤ覡,   " 
        + "'' as �״ڦa,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z�H,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as ���Y,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�ʫ�%' and t70.RecordType like '�����q�H%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.benName FROM Sale05M091ben aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
//        + "AND RecordDesc not like '���I��:%'  " 
        + "  " 
        + "UNION   "  //�ʫ��ҩ��� - �N�z�H
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.OrderDate as ������, t70.ProjectID1 as �קO,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "'' as ú�ڪ��B,   " 
        + "'' as ú�ڤ覡,   " 
        + "'' as �״ڦa,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z�H,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as ���Y,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "where 1=1 "
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�ʫ�%' and t70.RecordType like '�N�z�H%'  " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''   " 
        + "and charindex(t70.CustomName, ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE t70.OrderNo = aa.OrderNo FOR XML PATH('')) , 1, 1, '') , '') ) > 0  " 
        + "and t70.CustomName != ''  " 
        + "AND ISNULL(t70.OrderDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
        + "AND RecordDesc not like '���I��:%'  " 
        + "  " 
        + "UNION   "  //����
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.EDate as ������, t70.ProjectID1 as �קO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '�{��' then t80.CashMoney   " 
        + "when '�H��' then t80.CreditCardMoney   " 
        + "when '�Ȧ�' then t80.BankMoney   " 
        + "when '����' then t80.CheckMoney   " 
        + "else ''   " 
        + "end ) as ú�ڪ��B,   " 
        + "SUBSTRING(t70.RecordType, 1, 2) as ú�ڤ覡,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '�Ȧ�' then (select top 1 ExportingPlace from Sale05M328 t328 where t328.DocNo=t70.DocNo)   " 
        + "else ''  " 
        + "end ) as �״ڦa,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '�{��' then t80.DeputyName   " 
        + "when '�H��' then (select top 1 DeputyName from Sale05M083 aa  where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '�Ȧ�' then (select top 1 DeputyName from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '����' then (select top 1 DeputyName from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "else ''  " 
        + "end ) as �N�z�H,   " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '�{��' then t80.DeputyRelationship   " 
        + "when '�H��' then (select top 1 DeputyRelationship from Sale05M083 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '�Ȧ�' then (select top 1 DeputyRelationship from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)   " 
        + "when '����' then (select top 1 DeputyRelationship from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)   " 
        + "else ''  " 
        + "end ) as ���Y,  " 
        + "(case SUBSTRING(t70.RecordType, 1, 2)   " 
        + "when '�{��' then t80.Reason   " 
        + "when '�H��' then (select top 1 Reason from Sale05M083 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '�Ȧ�' then (select top 1 Reason from Sale05M328 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "when '����' then (select top 1 Reason from Sale05M082 aa where aa.DocNo=t70.DocNo and aa.DeputyID = t70.customID order by aa.RecordNo)  " 
        + "else ''  " 
        + "end ) as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "left join sale05m080 t80 on t70.DocNo = t80.DocNo   " 
        + "where 1=1 "
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '����%'   " 
        + "and ISNULL( STUFF( (SELECT ',' + t92.[Position] FROM Sale05M092 t92 WHERE t70.OrderNo = t92.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "and ISNULL( STUFF( (SELECT ',' + aa.DocNo FROM Sale05M080 aa WHERE t70.DocNo = aa.DocNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.EDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
        + "and RecordDesc not like '%�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��%'   " 
        + "and RecordDesc not like '%�ӫȤᬰ�æ��¦W���H%'  " 
        + "and RecordDesc not like '%�P�@�Ȥ�P�@��~�餺2��(�t)�H�W�]�t�{��%'   " 
        + "and RecordDesc not like '%�Ȥ��I���ʲ�������ڶ�%'  " 
        + "  " 
        + "UNION   " //�X��-�Q�e�U�H
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.CDate as ������, t70.ProjectID1 as �קO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "'' as ú�ڪ��B,   " 
        + "'' as ú�ڤ覡,   " 
        + "'' as �״ڦa,   " 
        + "(SELECT top 1 aa.TrusteeName FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID)  as �N�z�H,  " 
        + "(SELECT top 1 aa.Relation FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID) as ���Y,  " 
        + "(SELECT top 1 aa.Reason FROM Sale05M355 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.TrusteeId = t70.CustomID) as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "where RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�X��%' and RecordType like '%����-�Q�e�U�H%'  " 
        + "AND ISNULL( STUFF( (SELECT ',' +  t355.TrusteeName  from Sale05M355 t355 join Sale05M275_New t275 on t355.ContractNo = t275.ContractNo  and t275.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.CDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
        + "  " 
        + "UNION   " //�X��-���w�ĤT�H
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.CDate as ������, t70.ProjectID1 as �קO, " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO," 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O," 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W," 
        + "t70.CustomID as ���ˤHID," 
        + "t70.CustomName as ���ˤH�m�W," 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e, " 
        + "'' as ú�ڪ��B, " 
        + "'' as ú�ڤ覡, " 
        + "'' as �״ڦa, " 
        + "(SELECT top 1 aa.DesignatedName FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID)  as �N�z�H, " 
        + "(SELECT top 1 aa.Relation FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID) as ���Y, " 
        + "(SELECT top 1 aa.Reason FROM Sale05M356 aa join Sale05M275_New bb on aa.ContractNo = bb.ContractNo and bb.OrderNo = t70.OrderNo and aa.DesignatedId = t70.CustomID) as �N�z��] " 
        + "from Sale05M070 t70 " 
        + "where 1=1 "
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�X��%' and RecordType like '%����-���w�ĤT�H%' " 
        + "AND ISNULL( STUFF( (SELECT ',' +  t356.DesignatedName  from Sale05M356 t356 join Sale05M275_New t275 on t356.ContractNo = t275.ContractNo  and t275.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') != ''  " 
        + "AND ISNULL(t70.CDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "'  " 
        + "  " 
        + "UNION   "  //�h��
        + "select DISTINCT t70.OrderNo , t70.DocNo, t70.EDate as ������, t70.ProjectID1 as �קO,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'house' FOR XML PATH('')) , 1, 1, '') , '') as �ɼӧO,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.[Position] FROM Sale05M092 aa WHERE aa.OrderNo = t70.OrderNo and aa.HouseCar = 'car' FOR XML PATH('')) , 1, 1, '') , '') as ����O,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.customName FROM Sale05M091 aa WHERE aa.OrderNo = t70.OrderNo FOR XML PATH('')) , 1, 1, '') , '') as �Ȥ�m�W,  " 
        + "t70.CustomID as ���ˤHID,  " 
        + "t70.CustomName as ���ˤH�m�W,  " 
        + "t70.Func as �\��O, t70.RecordType as �æ��~���������, t70.RecordDesc as �æ��~���A�ˤ��e,   " 
        + "'' as ú�ڪ��B,   " 
        + "'' as ú�ڤ覡,   " 
        + "'' as �״ڦa,   " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentName FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z�H,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentRel FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as ���Y,  " 
        + "ISNULL( STUFF( (SELECT ',' + aa.AgentReason FROM Sale05M091Agent aa WHERE aa.OrderNo = t70.OrderNo and ISNULL(aa.StatusCd , '') != 'C' FOR XML PATH('')) , 1, 1, '') , '') as �N�z��]  " 
        + "from Sale05M070 t70  " 
        + "where 1=1"
//        + "and RecordDesc != '���A��' and RecordDesc != '���ŦX' and t70.Func like '�h��%'  "  
        + "and (SELECT COUNT(t94.OrderNo) FROM Sale05M094 t94 WHERE t70.OrderNo = t94.OrderNo group by t94.OrderNo) > 0  " 
        + "AND ISNULL(t70.EDate , '') BETWEEN '" + �v�jDate + "' AND '" + �w�oDate + "' "
        + "order by t70.Func desc, t70.orderno, t70.ProjectID1 desc";
    String[][] ret = dbSale.queryFromPool(sql);
    
    this.setValue("TableCount", Integer.toString(ret.length));
    this.setTableData("ResultTable", ret);

    if (tb1.getRowCount() > 0) tb1.addRowSelectionInterval(0, 0);
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
