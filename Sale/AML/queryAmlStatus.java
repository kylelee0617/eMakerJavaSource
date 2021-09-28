package Sale.AML;

import jcx.db.talk;
import jcx.jform.bTransaction;

/**
 * �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
   �^�ǭȬ� false ��ܱ��U�Ӥ����������O
   �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
 * @author B04391
 *
 */

public class queryAmlStatus extends bTransaction{
  talk dbSale = null;
  talk db400CRM = null;
  
  public boolean action(String value)throws Throwable{
    dbSale = getTalk("Sale");
    db400CRM = getTalk("400CRM");
    
    if( "�d��".equals(value) ) {
      String customNo = getQueryValue("customNo").trim();
      String customName = getQueryValue("customName").trim();
      
      if("".equals(customNo) && "".equals(customName)) {
        messagebox("���~ : �d�߱��󬰪�");
        return false;
      }
      
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT ");
      sb.append("A.CUSTOMERID , A.CUSTOMERNAME , A.BIRTHDAY ,  MODIFIERNO , MODIFIEDDATE , L.CONTROLCLASSIFICATIONNAME , L.CONTROLCLASSIFICATIONCODE , C.NOTECONTECT ");
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
      setTableData("AmlStatus",customerInfo);
      message("�@ " + customerInfo.length + " �����G!!");
      
//      for(int i=0 ; i<customerInfo.length ; i++) {
//        String[] info = customerInfo[i];
//        for(int j=0 ; j<info.length ; j++) {
//          System.out.println(info[j] + "--");
//        }
//      }
      
      setValue("customNo", customNo);
      setValue("customName", customName);
      
      return false;
    }
    
    
    return false;
  }
  public String getInformation(){
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
