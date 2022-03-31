package  Doc.Doc3 ;
import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import cLabel;
import Farglory.util.FargloryUtil ;
public class Doc3M011_MoneyCheckFront extends bproc{
  public String getDefaultValue(String value)throws Throwable{
        System.out.println("預算檢核 Batch--------------------------S11111111111") ;
        talk                dbFED1                                =  getTalk(""  +get("put_FED1"));
        talk                dbDoc                                   =  getTalk("Doc") ;
        FargloryUtil    exeUtil               =  new  FargloryUtil() ;
        long              longTime1                 =  exeUtil.getTimeInMillis( ) ;
        String        stringTemp                =  "" ;
        String        stringMessage             =  "" ;
        String        stringBarCode           =  getValue("BarCode").trim() ;
        String        stringComNo           =  getValue("ComNo").trim() ;
        String        stringErrMessage              = "" ;
        String        stringNowTime           =  datetime.getTime("YYYY/mm/dd h:m:s") ;
        String          stringProjectIDExistStatus      =  getProjectIDExistStatus(stringNowTime,  exeUtil,  dbDoc,  dbFED1) ;
        String        stringToday               =  datetime.getToday("YYYY/mm/dd") ;
        String        stringView                =  "View" ;  // 2016/11/08 上線
        String        stringUser              =  getUser().toUpperCase() ;
        String[][]      retTableData                =  null ;
        String[][]      retTableCheckData           =  new  String[1][1] ;
        boolean       booleanError              =  false ;
        String errBudgetMsg = "";
        //
        put("Doc3M011_CHECK",  "null") ;
        if("TEST".equals(getUser())) {
            put("Doc3M011_CHECK",  "TEST") ;
        }
        //
        setTableData("TableCheck",  new  String[0][0]) ;
        //
        System.out.println("案預算存在檢核 時間-----------------S") ;
        if(!"OK".equals(stringProjectIDExistStatus)) {
            message("案預算錯誤資訊，請點選燈泡，檢視祥細內容。\n"+stringProjectIDExistStatus) ;
            messagebox("案預算錯誤資訊") ;
            //
            retTableCheckData[0][0]  =  "ERROR" ;
            setTableData("TableCheck",  retTableCheckData) ;
            return  value ;
        }
        long        longTime2                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("案預算存在檢核 時間-----------------E("  +  ((longTime2-longTime1))+")") ;
        
        
        
        // 案
        System.out.println("案預算檢核 時間-----------------S") ;
        getButton("ButtonProjectBudgetCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            booleanError           =  true ;
            errBudgetMsg += "[案預算錯誤]";
            //if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            //stringMessage       +=  stringTemp ;
            if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
            stringErrMessage  +=  stringTemp ;
        }
        long        longTime3                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("案預算檢核 時間("+booleanError+")-----------------E("  +  ((longTime3-longTime2))+")") ;
        
        // 案年度 
        System.out.println("案年度預算檢核 時間-----------------S") ;
        getButton("ButtonYearBudgetCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            booleanError           =  true ;
            errBudgetMsg += "[案年度預算錯誤]";
            if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            //stringMessage       +=  stringTemp ;
            if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n\n" ;
            stringErrMessage  +=  stringTemp ;
        }
        long        longTime4                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("案年度預算檢核 時間("+booleanError+")-----------------E("  +  ((longTime4-longTime3))+")") ;
        
        if("".equals(stringView)) {
            // 已在 ButtonYearBudgetCheckView 中處理
            // 年度特別請款代碼控管 ButtonTableCheckN
            System.out.println("年度特別請款代碼控管 時間-----------------S") ;
            getButton("ButtonTableCheckN").doClick() ;
            retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
            stringTemp     =  retTableData[0][0].trim() ;
            if(!"".equals(stringTemp)) {
                booleanError           =  true ;
                //if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
                //stringMessage       +=  stringTemp ;
                if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
                stringErrMessage  +=  stringTemp ;
            }
        }
        long        longTime5                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("年度特別請款代碼控管 時間("+booleanError+")-----------------E("  +  ((longTime5-longTime4))+")") ;
        // 通路代碼 預算檢核
        getButton("ButtonSSMediaIDCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            //booleanError     =  false ;
            errBudgetMsg += "[通路預算錯誤]";
            if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            stringMessage       +=  stringTemp ;
            //if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
            //stringErrMessage  +=  stringTemp ;
            //if("B3018".equals(getUser()))messagebox("通路代碼 預算檢核錯誤"+stringMessage) ;
        }
        long        longTime6                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("通路代碼 預算檢核 時間-("+booleanError+")----------------E("  +  ((longTime6-longTime5))+")") ; 
        
        
        // 輸入部門預算控管
/*        System.out.println("輸入部門預算控管 時間("+booleanError+")-----------------S") ;
        getButton("ButtonInputDeptCdCheck").doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            booleanError     =  true ;
            //if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            //stringMessage  +=  stringTemp ;
            if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
            stringErrMessage  +=  stringTemp ;
        }*/
        long        longTime7                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("輸入部門預算控管 時間-("+booleanError+")----------------E("  +  ((longTime7-longTime6))+")") ;
        
        
        
        
        
        // 預算檢核例外
        String  stringBarCodeOld        =  getValue("BarCodeOld").trim() ;if("".equals(stringBarCodeOld))  stringBarCodeOld  =  getValue("BarCode").trim() ;
        String  stringBarCodeExcept  =  exeUtil.getNameUnion("BarCodeDoc3M011",  "Doc2M040",  " AND  ISNULL(BarCodeDoc3M011, '')  <> '' ",  new  Hashtable(),  dbDoc) ;
        if(!"".equals(stringBarCodeOld)  &&  stringBarCodeExcept.indexOf(stringBarCodeOld)!=-1) {
            booleanError  =  false ;
        }
        if(booleanError) {
            message("預算錯誤資訊"+errBudgetMsg+"，請點選燈泡，檢視祥細內容。\n"+stringErrMessage+"\n"+stringMessage) ;
            messagebox("預算錯誤"+errBudgetMsg+"。\n詳細資訊，請點狀態列選燈泡，檢視內容。\n"+stringMessage) ;
            retTableCheckData[0][0]  =  "ERROR" ;
        } else {
            if(!"".equals(stringMessage)  ||  !"".equals(stringMessage)) {
                messagebox(stringMessage) ;
                message("預算資訊"+errBudgetMsg+"，請點選燈泡，檢視祥細內容。\n"+stringErrMessage+stringMessage) ;
            } else {
                message("") ;
            }
            retTableCheckData[0][0]  =  "OK" ;
        }
        setTableData("TableCheck",  retTableCheckData) ;
        long        longTime8                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("預算檢核例外 時間-("+booleanError+")----------------E("  +  ((longTime8-longTime7))+")") ;
        //
        if("B3018".equals(stringUser)) {
            Hashtable  hashtableAnd  =  new  Hashtable() ;  hashtableAnd.put("KEY_USER",  stringUser) ;hashtableAnd.put("KEY_BarCode",  stringBarCode) ;
            String      stringSql     =  exeUtil.doDeleteDB("Doc3M014_BudgetCheckTemp",  hashtableAnd,  " AND  KEY_Time  <= '"+stringNowTime+"' ",  true,  dbDoc) ;
            System.out.println("刪除 Doc3M014_BudgetCheckTemp -------------------------"+stringSql) ;
            long        longTime9                 =  exeUtil.getTimeInMillis( ) ;
            System.out.println("刪除 時間-("+booleanError+")----------------E("  +  ((longTime9-longTime8))+")") ;
        }

        
        long        longTimeEND                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("預算檢核花費時間-----------------("  +  ((longTimeEND-longTime1))+")") ;
        System.out.println("預算檢核 Batch --------------------------E") ;
        return value;
    }
    public  String  getProjectIDExistStatus(String  stringNowTime,  FargloryUtil  exeUtil,  talk  dbDoc,  talk  dbFED1) throws  Throwable {
        String        stringTable                             =  "Table3" ;
        String        stringDepartNo                      =  "" ;
        String        stringInOut                           =  "" ;
        String        stringCostID                          =  "" ;
        String        stringCostID1                       =  "" ;
        String        stringProjectID1                    =  "" ;
        String        stringBudgetID                      =  "" ;
        String    stringComNo           =  getValue("ComNo").trim() ;
        String    stringBarCode           =  getValue("BarCode").trim() ;
        String        stringNeedDate                  =  getValue("NeedDate").trim() ;
        String        stringNeedDateAC              =  exeUtil.getDateConvert(stringNeedDate) ;
        String        stringTemp                            =  "" ;
        String        stringMessage                       =  "" ;
        String        stringDeptCdSql                     =  "" ;
        String        stringProjectID1Sql               =  "" ;
        String      stringUser            =  getUser().toUpperCase() ;
        JTable      jtable3                                   =  getTable(stringTable) ;
        Hashtable   hashtableAnd          =  new  Hashtable() ;
        Vector    vectorSepcDeptCd        =  getSpecDeptCd( ) ;
        for(int  intNo=0 ;  intNo<jtable3.getRowCount( )  ;  intNo++) {
            stringInOut                   =  (""+getValueAt(stringTable,  intNo,  "InOut")).trim() ;
            stringDepartNo              =  (""+getValueAt(stringTable,  intNo,  "DepartNo")).trim() ;
            stringCostID                  =  (""+getValueAt(stringTable,  intNo,  "CostID")).trim() ;
            stringCostID1                 =  (""+getValueAt(stringTable,  intNo,  "CostID1")).trim() ;
            stringProjectID1              =  (""+getValueAt(stringTable,  intNo,  "ProjectID1")).trim() ;
            //
            if("I".equals(stringInOut)) {
                if(vectorSepcDeptCd.indexOf(stringDepartNo)  !=  -1) {
                    if(!"".equals(stringDeptCdSql))  stringDeptCdSql  +=  ", " ;
                    stringDeptCdSql  +=  " '"+stringDepartNo+"' " ;
                }
            } else {
                if(!"".equals(stringProjectID1Sql))  stringProjectID1Sql  +=  ", " ;
                stringProjectID1Sql  +=  " '"+stringProjectID1+"' " ;
            }
            //
            if("I".equals(stringInOut)  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)  continue ;
            if("0531".equals(stringDepartNo))                                 continue ;
            //
            hashtableAnd.put("ComNo",   stringComNo) ;
            hashtableAnd.put("CostID",    stringCostID) ;
            hashtableAnd.put("CostID1",  stringCostID1) ;
            stringBudgetID  =  exeUtil.getNameUnion("BudgetID",  "Doc2M020",  "",  hashtableAnd,  dbDoc) ;
            if("".equals(stringBudgetID))                                        continue ;
            if("B".equals(stringBudgetID)  &&  ",03365,".indexOf(","+stringDepartNo+",")!=-1)  continue ;
            //
            stringTemp  =  getProjectIDExistMessage(stringProjectID1,  exeUtil.doSubstring(stringBudgetID,  0,  1),  stringNeedDateAC,  exeUtil,  dbDoc,  dbFED1) ;
            if(!"OK".equals(stringTemp)) {
                if(!"".equals(stringMessage))  stringMessage  +=  "\n" ;
                stringMessage  +=  stringTemp ;
            }
        }
        if("".equals(stringMessage)) {
            stringMessage  =  "OK" ;
            if("B3018".equals(stringUser)) {
                String  stringSql  =  " spDoc3M014_BudgetCheckTemp_Insert  '"+stringUser+"', '"+stringBarCode+"',  '"+stringNowTime+"',  '"+stringComNo+"',  \""+stringProjectID1Sql+"\",  '"+stringDeptCdSql+"'  " ;
                      //stringSql  =  convert.ToSql(stringSql) ;
                dbDoc.execFromPool(stringSql) ;
                System.out.println("新增 Doc3M014_BudgetCheckTemp -------------------------"+stringSql) ;
            }
        }
        return  stringMessage ;
    }
    public  Vector  getSpecDeptCd( ) throws  Throwable {
        String      stringSpecBudget       =  ""+get("SPEC_BUDGET") ;                     // 特殊預算控管
        String[]       arraySpecBudget          =  convert.StringToken(stringSpecBudget,",") ;
        Vector      vectorDeptCd          =  new  Vector() ;
        //
        vectorDeptCd.add("0331") ;  // 不動產行銷
        vectorDeptCd.add("0333") ;  // 行銷企劃室
        vectorDeptCd.add("03335") ; // 網路行銷組
        vectorDeptCd.add("033622") ; // 通路行銷組
        vectorDeptCd.add("03363") ;  // 數位研發科
        vectorDeptCd.add("03365") ;  //資料庫行銷科。
        vectorDeptCd.add("03396") ;  //整合行銷科
        for(int  intNo=0  ;  intNo<arraySpecBudget.length  ;  intNo++)  vectorDeptCd.add(arraySpecBudget[intNo]) ; 
        return  vectorDeptCd ;
    }
    public  String  getProjectIDExistMessage(String  stringProjectID1,  String  stringFunctionType,  String  stringNeedDateAC,  FargloryUtil  exeUtil,  talk  dbDoc,  talk  dbFED1) throws  Throwable {
        String         stringComNo                    =  getValue("ComNo").trim() ;
        String      stringTemp              =  "" ;
        String      stringOpenSaleDate      =  "" ;
        Vector      vectorDoc7M020        =  null ;
        Hashtable  hashtableAnd             =  new  Hashtable() ;
        Hashtable  hashtableDoc7M020    =  new  Hashtable() ;
        //
        hashtableAnd.put("ComNo",           stringComNo) ;
        hashtableAnd.put("ProjectID1",       stringProjectID1) ;
        hashtableAnd.put("FunctionType",    stringFunctionType) ;
        hashtableAnd.put("STATUS",               "U") ;
        vectorDoc7M020  =  exeUtil.getQueryDataHashtable("Doc7M020",  hashtableAnd,  "",  dbDoc) ;
        if(vectorDoc7M020.size()  >  0) return  "OK" ;
        //
        hashtableAnd.put("ComNo",           stringComNo) ;
        hashtableAnd.put("ProjectID1",       stringProjectID1) ;
        hashtableAnd.put("FunctionType",    stringFunctionType) ;
        hashtableAnd.put("STATUS",               "F") ;
        vectorDoc7M020  =  exeUtil.getQueryDataHashtable("Doc7M020",  hashtableAnd,  "",  dbDoc) ;
        if(vectorDoc7M020.size()  ==  0) {
            hashtableAnd.put("ComNo",           stringComNo) ;
            hashtableAnd.put("ProjectID1",       stringProjectID1) ;
            hashtableAnd.put("FunctionType",    stringFunctionType) ;
            hashtableAnd.put("STATUS",               "R") ;
            vectorDoc7M020  =  exeUtil.getQueryDataHashtable("Doc7M020",  hashtableAnd,  "",  dbDoc) ;
        }
        if(vectorDoc7M020.size()  ==  0) {
            stringTemp  =  exeUtil.getNameUnion("COMPANY_NAME",  "FED1023",  " AND  COMPANY_CD  =  '"+stringComNo+"'  ",  new  Hashtable(),  dbFED1) ;
            stringTemp  =   stringTemp+" 之 案別 "  +  stringProjectID1  +  " 不存在於 [案預算控管] 中，請 ["+("A".equals(stringFunctionType) ? "行銷管理室" : "行銷企劃室")+"] 新增案別資料。 "  ;
            return  stringTemp ;
        }
        // 雖未完成預算流程，但已簽核至 [體系主管] 且 [CDate] 在 [公開期] 之前，允許
        hashtableDoc7M020  =  (Hashtable)  vectorDoc7M020.get(0) ;          
        stringOpenSaleDate   =  ""+hashtableDoc7M020.get("OpenSaleDate") ;
        if("".equals(stringOpenSaleDate)) {
            stringTemp  =  "該案別("+stringProjectID1+") 之 [案預算控管(Doc7M020)] 之 [公開期] 為空白，不能執行 [案前預算]，請 詢問  ["+("A".equals(stringFunctionType) ? "行銷管理室" : "行銷企劃室")+"] 。" ;
            return  stringTemp ;
        }
        if(stringNeedDateAC.compareTo(stringOpenSaleDate)  >  0) {
            stringTemp  =  "[輸入日期] 位於 該案別("+stringProjectID1+") 之 [案預算控管(Doc7M020)]  [公開期] 後，不能執行 [案前預算]，請 詢問  ["+("A".equals(stringFunctionType) ? "行銷管理室" : "行銷企劃室")+"] 。"  ;
            return  stringTemp ;
        }
        String  stringDate  =  ""+hashtableDoc7M020.get("CreepSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("OpenSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("StrongSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("EndSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("DateStart") ;
        if(stringNeedDateAC.compareTo(stringDate)  <  0) {
            stringTemp  =  "[輸入日期]("+stringNeedDateAC+") 早於  案別("+stringProjectID1+")於[案預算控管(Doc7M020)] 可使用日期("+stringDate+")。"  ;
            return  stringTemp ;
        }
        return  "OK" ;
  }
  public String getInformation(){
    return "---------------ButtonTableCheck(\u5e74\u5ea6\u9810\u7b97\u6aa2\u6838).defaultValue()----------------";
  }
}
