package Sale.AML;

import jcx.db.talk;
import jcx.jform.bproc;

/**
 * �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
   �^�ǭȬ� false ��ܱ��U�Ӥ����������O
   �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
 * @author B04391
 *
 */

public class queryAmlStatus extends bproc{
  talk dbSale = null;
  talk db400CRM = null;
  
  public String getDefaultValue(String value)throws Throwable{
    dbSale = getTalk("Sale");
    db400CRM = getTalk("400CRM");
    
    if( "�d��".equals(value) ) {
      String customNo = getValue("customNo").trim();
      String customName = getValue("customName").trim();
      
      if("".equals(customNo) && "".equals(customName)) {
        messagebox("���~ : �d�߱��󬰪�");
        return value;
      }
      
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ");
      sb.append("A.CUSTOMERID , A.CUSTOMERNAME , A.BIRTHDAY ,  C.CREATOR , C.CREATEDDATE , L.CONTROLCLASSIFICATIONNAME , L.CONTROLCLASSIFICATIONCODE , C.NOTECONTECT ");
      sb.append("FROM CRCLNAPF A,CRCLNCPF C,CRCLCLPF L ");
      sb.append("WHERE A.CONTROLLISTNAMECODE=C.CONTROLLISTNAMECODE AND C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE ");
      if( !"".equals(customNo) ) {
        sb.append("AND A.CUSTOMERID='").append(customNo).append("' ");
      }
      if( !"".equals(customName) ) {
        sb.append("AND A.CUSTOMERNAME='").append(customName).append("' ");
      }
      sb.append("ORDER BY CUSTOMERID asc");
      
      String[][] customerInfo = db400CRM.queryFromPool(sb.toString());
      setTableData("table1",customerInfo);
      message("�@ " + customerInfo.length + " �����G!!");
      
      //check
      for(int i=0 ; i<customerInfo.length ; i++) {
        String[] info = customerInfo[i];
        for(int j=0 ; j<info.length ; j++) {
          System.out.println(info[j] + "--");
        }
      }
      return value;
    }
    
    return value;
  }
  
  public String getInformation(){
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
