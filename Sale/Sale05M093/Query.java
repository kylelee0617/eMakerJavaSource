package Sale.Sale05M093;

import javax.swing.JTabbedPane;

import Farglory.util.KSqlUtils;
import jcx.db.talk;
import jcx.jform.bproc;

public class Query extends bproc {
  public String getDefaultValue(String value) throws Throwable {
    KSqlUtils ksUtil = new KSqlUtils();
    String logText = "���W�e�d��";
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), logText + ":Start");
    
    setEditable("nonAgent", false);
    getButton("button2").setVisible(false);
    getButton("button3").setVisible(false);
    getButton("button4").setVisible(false);

    String projectId = getValue("ProjectID1").trim();
    String orderNo = getValue("OrderNo").trim();
    if (projectId.length() == 0) {
      message("�п�J�קO");
      return value;
    }
    if (getValue("TrxDate").length() == 0) {
      message("�п�J�h����");
      return value;
    }
    if (orderNo.length() == 0) {
      message("�п�J�ϥγ�s��");
      return value;
    }

    talk dbSale = getTalk("Sale");
    String[][] ret91s = ksUtil.getCustom4Sale093Query(orderNo);
    String[][] ret91s2 = ksUtil.getCustom4Sale093Query(orderNo);
    
    //��FEMAKER���|�A���M�ΦP�@���ܼƸ˪���Ʒ|�۳q�A�ҥHtalbe1 table2 ����ΦP�@��ret91s
    
    setTableData("table1", ret91s);
    setTableData("table2", ret91s2);
    setValue("nonAgent2", "1"); // �w�]�ȵL�N�z�H
    setEditable("ProjectID1", false);
    setEditable("OrderNo", false);
    setEditable("TrxDate", false);
    getButton("button1").setVisible(false);
    getButton("button2").setVisible(true);

    // 20200226 kyle : �אּ�����
    // 20210127 Kyle : �S�n��ܰ�~
    // �����q�H
    JTabbedPane jtp2 = getTabbedPane("tab2");
    String str091BenSql = " SELECT  RecordNo,(SELECT TOP 1 a.CustomName FROM Sale05M091 a WHERE a.CustomNo=b.CustomNo) AS CustomName, BenName, BCustomNo,Birthday, CountryName, HoldType, IsBlackList,IsControlList,IsLinked "
        + " FROM Sale05M091Ben b WHERE orderNo = '" + getValue("OrderNo") + "' AND  (StatusCd ='' or StatusCd is null) ORDER BY RecordNo";
    String retSale05M091Ben[][] = dbSale.queryFromPool(str091BenSql);
    String[][] ret5 = new String[(retSale05M091Ben.length)][12];
    if (retSale05M091Ben.length > 0) {
      for (int a = 0; a < retSale05M091Ben.length; a++) {
        ret5[a][0] = getValue("OrderNo").trim(); // OrderNo
        ret5[a][1] = "" + (a + 1); // No
        ret5[a][2] = retSale05M091Ben[a][1].trim(); // �q��m�W
        ret5[a][3] = retSale05M091Ben[a][2].trim(); // ����H�m�W
        ret5[a][4] = retSale05M091Ben[a][3].trim(); // �����Ҹ�
        ret5[a][5] = retSale05M091Ben[a][4].trim(); // �ͤ�
        ret5[a][6] = retSale05M091Ben[a][5].trim(); // ��O
        ret5[a][7] = retSale05M091Ben[a][6].trim(); // ��H�O
        ret5[a][8] = retSale05M091Ben[a][7].trim(); // �¦W��
        ret5[a][9] = retSale05M091Ben[a][8].trim(); // ���ަW��
        ret5[a][10] = retSale05M091Ben[a][9].trim(); // �Q�`���Y�H
      }
      setTableData("table5", ret5);
    }

    // 20200226 kyle : �אּ�����
    // 20210127 Kyle : �S�n��ܰ�~
    // 20210127 Kyle : ���W�S����ܰ�
    // �N�z�H
//    String  str091AgentSql = "SELECT RecordNo, (SELECT TOP 1 a.CustomName FROM Sale05M091 a WHERE a.CustomNo=b.CustomNo) AS CustomName,AgentName, ACustomNo, CountryName, AgentRel,AgentReason,IsBlackList,IsControlList,IsLinked " +
//                        "FROM Sale05M091Agent b WHERE orderNo = '"+getValue("OrderNo")+"' AND  (StatusCd ='' or StatusCd is null) ORDER BY RecordNo"; 
//    String retSale05M091Agent[][] = dbSale.queryFromPool(str091AgentSql); 
//    String [][]ret6=new String[(retSale05M091Agent.length)][12];    
//    if (retSale05M091Agent.length>0){
//      for (int b=0;b<retSale05M091Agent.length;b++){  
//        ret6[b][0]  =getValue("OrderNo").trim( );         // OrderNo   
//        ret6[b][1]  =""+(b+1);                            // No
//        ret6[b][2]  =retSale05M091Agent[b][1].trim( );        // �q��m�W
//        ret6[b][3]  =retSale05M091Agent[b][2].trim( );        // �N�z�H�m�W
//        ret6[b][4]  =retSale05M091Agent[b][3].trim( );        // �����Ҹ�
//        ret6[b][5]  =retSale05M091Agent[b][4].trim( );        // ��O
//        ret6[b][6]  =retSale05M091Agent[b][5].trim( );        // ���Y
//        ret6[b][7]  =retSale05M091Agent[b][6].trim( );        // �N�z��]
//        ret6[b][8]  =retSale05M091Agent[b][7].trim( );        //�¦W��
//        ret6[b][9]  =retSale05M091Agent[b][8].trim( );    // ���ަW��
//        ret6[b][10]  =retSale05M091Agent[b][9].trim( );   // �Q�`���Y�H
//      }
//      setTableData("table6",ret6);
//      setValue("nonAgent2","0");
//    }
    
    ksUtil.setSaleLog(this.getFunctionName(), value, this.getUser(), logText + ":End");

    return value;
  }

  public String getInformation() {
    return "---------------button1(button1).defaultValue()----------------";
  }
}
