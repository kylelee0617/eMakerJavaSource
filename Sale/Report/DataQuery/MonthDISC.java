package Sale.Report.DataQuery;

import javax.swing.JTable;

import jcx.db.talk;
import jcx.jform.bproc;

public class MonthDISC extends bproc{
  
  //param
  String yM = "";
  
  public String getDefaultValue(String value)throws Throwable{
    
    //查詢條件
    yM = this.getValue("財務-YearMonth").trim();
    
    String funcName = yM + "月折讓";
    
    //TODO: 查詢資料
    String[][] retTable = this.getMainData();

    this.setValue("TableCount", "共" + retTable.length + "筆");
    
    //TODO: 寫表格
    //表頭
    String tmpHeader = "DiscountNo、RecordNo、ChoiceYES、InvoiceNo、PointNo、InvoiceMoney、InvoiceTax、InvoiceTotalMoney、YiDiscountMoney、DiscountItemMoney、CustomNo、DELYes、Reason";
    String[] tableHeader = tmpHeader.split("、");
    JTable tb1 = getTable("ResultTable");
    tb1.setName(funcName);
    this.setTableHeader("ResultTable", tableHeader);

    this.setValue("TableName", funcName);   //表格外框名稱
    
    this.setTableData("ResultTable", retTable);   //表身，主資料
    
    //寫log
    //ksUtil.setSaleLog(this.getFunctionName(), "IRA Data", this.getUser(), "查詢IRA資料成功");
    
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
