package Invoice.Report;

import javax.swing.JTable;

import jcx.db.talk;
import jcx.jform.bproc;

public class MonthInvoice extends bproc{
  
  //param
  String yM = "";
  
  public String getDefaultValue(String value)throws Throwable{
    
    //�d�߱���
    yM = this.getValue("Year").trim();
    
    
    String funcName = yM + "��}�ߵo��";
    
    //TODO: �d�߸��
    String[][] retTable = this.getMainData();

    this.setValue("TableCount", "�@" + retTable.length + "��");
    
    //TODO: �g���
    //���Y
    String tmpHeader = "InvoiceNo�BInvoiceDate�BInvoiceTime�BInvoiceKind�BCompanyNo�BDepartNo�BProjectNo�BInvoiceWay�BHuBei�BCustomNo�BCustomName�BPointNo�BInvoiceMoney�BInvoiceTax�B"
        + "InvoiceTotalMoney�BTaxKind�BDisCountMoney�BDisCountTimes�BPrintYes�BPrintTimes�BDELYes�BLuChangYes�BProcessInvoiceNo�BTransfer�BCreateUserNo�BCreateDateTime�BUpdateUserNo�B"
        + "UpdateDateTime�BDeleteUserNo�BDeleteDateTime�BLastUserNo�BLastDateTime�BCarbonPrintYes�BOBJECT_CD�BOBJECT_FULL_NAME�BRandomCode�BInvoTransYN�BdelTransYN";
    String[] tableHeader = tmpHeader.split("�B");
    JTable tb1 = getTable("ResultTable");
    tb1.setName(funcName);
    this.setTableHeader("ResultTable", tableHeader);

    this.setValue("TableName", funcName);   //���~�ئW��
    
    this.setTableData("ResultTable", retTable);   //���A�D���
    
    //�glog
    //ksUtil.setSaleLog(this.getFunctionName(), "IRA Data", this.getUser(), "�d��IRA��Ʀ��\");
    
    return value;
  }
  
  public String[][] getMainData() throws Throwable{
    talk dbINV = getTalk("Invoice");
    String sql = "Select * from invom030 where invoiceDate>='"+yM+"/01' and invoiceDate<='"+yM+"/99' order by invoiceNo";
    
    return dbINV.queryFromPool(sql);
  }
  
  
  public String getInformation(){
    return "---------------Query(\u67e5\u8a62).defaultValue()----------------";
  }
}
