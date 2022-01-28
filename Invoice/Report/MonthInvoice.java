package Invoice.Report;

import javax.swing.JTable;

import jcx.db.talk;
import jcx.jform.bproc;

public class MonthInvoice extends bproc{
  
  //param
  String yM = "";
  
  public String getDefaultValue(String value)throws Throwable{
    
    //查詢條件
    yM = this.getValue("Year").trim();
    
    
    String funcName = yM + "月開立發票";
    
    //TODO: 查詢資料
    String[][] retTable = this.getMainData();

    this.setValue("TableCount", "共" + retTable.length + "筆");
    
    //TODO: 寫表格
    //表頭
    String tmpHeader = "InvoiceNo、InvoiceDate、InvoiceTime、InvoiceKind、CompanyNo、DepartNo、ProjectNo、InvoiceWay、HuBei、CustomNo、CustomName、PointNo、InvoiceMoney、InvoiceTax、"
        + "InvoiceTotalMoney、TaxKind、DisCountMoney、DisCountTimes、PrintYes、PrintTimes、DELYes、LuChangYes、ProcessInvoiceNo、Transfer、CreateUserNo、CreateDateTime、UpdateUserNo、"
        + "UpdateDateTime、DeleteUserNo、DeleteDateTime、LastUserNo、LastDateTime、CarbonPrintYes、OBJECT_CD、OBJECT_FULL_NAME、RandomCode、InvoTransYN、delTransYN";
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
    talk dbINV = getTalk("Invoice");
    String sql = "Select * from invom030 where invoiceDate>='"+yM+"/01' and invoiceDate<='"+yM+"/99' order by invoiceNo";
    
    return dbINV.queryFromPool(sql);
  }
  
  
  public String getInformation(){
    return "---------------Query(\u67e5\u8a62).defaultValue()----------------";
  }
}
