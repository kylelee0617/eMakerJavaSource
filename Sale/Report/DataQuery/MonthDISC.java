package Sale.Report.DataQuery;

import javax.swing.JTable;

import jcx.db.talk;
import jcx.jform.bproc;

public class MonthDISC extends bproc{
  
  //param
  String yM = "";
  
  public String getDefaultValue(String value)throws Throwable{
    
    //�d�߱���
    yM = this.getValue("�]��-YearMonth").trim();
    
    String funcName = yM + "�����";
    
    //TODO: �d�߸��
    String[][] retTable = this.getMainData();

    this.setValue("TableCount", "�@" + retTable.length + "��");
    
    //TODO: �g���
    //���Y
    String tmpHeader = "DiscountNo�BRecordNo�BChoiceYES�BInvoiceNo�BPointNo�BInvoiceMoney�BInvoiceTax�BInvoiceTotalMoney�BYiDiscountMoney�BDiscountItemMoney�BCustomNo�BDELYes�BReason";
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
    
    String[] arrYM = yM.split("/");
    String newYear = Integer.toString( (Integer.parseInt(arrYM[0].trim())-1911) );
    
    talk dbINV = getTalk("Invoice");
    String sql = "Select A.*,B.CustomNo,B.DELYes,B.Reason from invom041 A,invom040 B where A.DiscountNo=B.DiscountNo and Substring(A.DiscountNo,8,5)='"+ newYear + arrYM[1].trim() +"' ";
    
    return dbINV.queryFromPool(sql);
  }
  
  
  public String getInformation(){
    return "---------------Query(\u67e5\u8a62).defaultValue()----------------";
  }
}
