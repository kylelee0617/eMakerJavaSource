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
import Doc.Doc2M010 ;
//public class Doc3M011_MoneyCheck extends jcx.jform.sproc{   // Server
public class Doc3M011_MoneyCheck extends bproc{   // Client
  public String getDefaultValue(String value)throws Throwable{
        talk               dbDoc                                        =  getTalk("Doc") ;
        talk               dbSale                                       =  getTalk("Sale") ;
        FargloryUtil  exeUtil                                       =  new  FargloryUtil() ;
        String         stringTemp                                 =  "" ;
        String         stringProjectID1                           =  "" ;
        String         stringFlow                                   =  getValue("Function").trim() ;
        String         stringNeedDate                 =  (stringFlow.indexOf("請購")!=-1  ||  stringFlow.indexOf("借款申")!=-1) ?getValue("NeedDate").trim():getValue("CDate").trim() ;    //oce
        String         stringBarCodeOld                       =  getValue("BarCodeOld").trim() ;
        String         stringNeedDateAC                     =  exeUtil.getDateConvert(stringNeedDate) ;
        String          stringSqlAndAll                         =  "" ;
        String          stringErrMessage                      =  "" ;
        String      stringBudgetType                  =  "" ;
        String      stringBigBudgetName             =  "" ;
        String[]       retCheckMoney                          =  null ;
        String[][]     retCheckTable                          =  null ;
        String[][]     retTableData                             =  new  String[1][1]  ;setTableData("TableCheck",  new  String[0][0]) ;
        String[][]      retFunctionTypeV                      =  getFunctionTypeDoc2M0201(stringNeedDateAC,  "V",  exeUtil,  dbDoc)  ;    // 不作預算控管也不算入預算內的請款代碼
        double        doubleUseMoneyL                   =  0 ;
        double        doubleUseMoneyL_NoControl          =  0 ;
        double        doubleUseMoneyL2                  =  0 ;
        double        doubleUseMoneyL2_NoControl        =  0 ;
        double        doubleUseMoney                        =  0 ;
        double        doubleUseMoney2                   =  0 ;
        double        doubleBudgetMoney                 =  0 ;
        double        doubleUseMoneyThis                =  0 ;
        boolean       booleanFlow                       =  stringFlow.indexOf("請購")==-1  ||  (stringFlow.indexOf("採購")!=-1  ||  stringFlow.indexOf("資訊")!=-1) ;//oce
        boolean       booleanTest                             =  "TEST".equals(""+get("Doc3M011_CHECK")) ;
        boolean     booleanAllNoControl             =  false ;
        Vector          vectorSpecProjectID1                =  getSpecProjectID1( ) ;  // 僅檢核年度預算
        Vector          vectorFullProjectID1                  =  new  Vector() ;  //
        Hashtable   hashtableBudgetIDs              =  new  Hashtable() ;
        Hashtable   hashtableBigBudget              =  new  Hashtable() ;
        // hashtableBudgetIDs 大預算分類整理
        // hashtableBigBudget 預算代碼 對應 大預算分類
        doDoc7M072Data(dbDoc,  hashtableBudgetIDs,  hashtableBigBudget) ;
        // 0 案別     1 種類        2 案-採發-使用金額       3 案-預算-使用金額     4 案-採發-不控管金額       5 案-預算-不控管金額
        retCheckTable                 =  getDataReorganization(stringFlow,  retFunctionTypeV,  vectorSpecProjectID1,  hashtableBigBudget,  exeUtil,  dbDoc) ;
        for(int  intNo=0  ;  intNo<retCheckTable.length  ;  intNo++) {
            stringProjectID1                        =  retCheckTable[intNo][0].trim() ; 
            stringBudgetType                    =  retCheckTable[intNo][1].trim() ;
            doubleUseMoneyL                     =  exeUtil.doParseDouble(retCheckTable[intNo][2].trim()) ;
            doubleUseMoneyL2                    =  exeUtil.doParseDouble(retCheckTable[intNo][3].trim()) ;
            doubleUseMoneyL_NoControl         =  exeUtil.doParseDouble(retCheckTable[intNo][4].trim()) ;    // 特殊請款代碼之採發金額
            doubleUseMoneyL2_NoControl       =  exeUtil.doParseDouble(retCheckTable[intNo][5].trim()) ;     // 特殊請款代碼之預算金額
            stringBigBudgetName                 =  stringBudgetType.length()==1  ?  ""  :  exeUtil.getNameUnion("BigBudgetName",  "Doc7M071",  " AND  BigBudget  =  '"+stringBudgetType+"' ",  new  Hashtable(),  dbDoc) ;
            //System.out.println(intNo+"("+retCheckTable.length+")stringBudgetType("+stringBudgetType+")stringBigBudgetName("+stringBigBudgetName+")-----------------------------------------") ;
            // 0  採發金額        1  採發金額            2  預估金額                   3  案預算使用中      4  案預算流程中
            retCheckMoney           =  getCheckMoneyFront(stringProjectID1,  stringBudgetType,  stringBarCodeOld,  retFunctionTypeV,  booleanFlow,  exeUtil,  dbDoc) ;
            stringSqlAndAll         =  getDoc2M020Sql(stringBudgetType,  dbDoc) ;
            // 案合計
            doubleUseMoney        =  exeUtil.doParseDouble(retCheckMoney[0].trim())+
                                                   exeUtil.doParseDouble(retCheckMoney[1].trim());            //  0  採發金額
            doubleUseMoney2     =  exeUtil.doParseDouble(retCheckMoney[2].trim()) ;               //  1  預估金額
            doubleBudgetMoney  =  exeUtil.doParseDouble(retCheckMoney[3].trim()) ;           //   2  案預算使用中
            if(doubleBudgetMoney  <=0  )  doubleBudgetMoney  =  exeUtil.doParseDouble(retCheckMoney[4].trim()) ;  // 3 案預算流程中
            if(booleanFlow) {
                doubleUseMoney        +=  doubleUseMoneyL ;
                doubleUseMoneyThis  =  doubleUseMoneyL ;
                booleanAllNoControl    =  doubleUseMoneyL<=doubleUseMoneyL_NoControl ;
            } else {
                doubleUseMoney2      +=  doubleUseMoneyL2 ;
                doubleUseMoneyThis   =  doubleUseMoneyL2 ;
                booleanAllNoControl     =  doubleUseMoneyL2<=doubleUseMoneyL2_NoControl ;
            }
            doubleUseMoney          =  exeUtil.doParseDouble(convert.FourToFive(""+doubleUseMoney,  0)) ;
            doubleUseMoney2        =  exeUtil.doParseDouble(convert.FourToFive(""+doubleUseMoney2,  0)) ;
            
            if(doubleUseMoney2+doubleUseMoney > doubleBudgetMoney ||  booleanTest) {
                if(!"".equals(stringBigBudgetName)) {
                    stringBigBudgetName  =  "之"+stringBigBudgetName ;
                }
                stringTemp  =   "案別為 "  +  stringProjectID1  +  "企劃預算"+stringBigBudgetName+" "  +
                             ((doubleUseMoney2==0)?"":" 未至採購之預算金額("  +  exeUtil.getFormatNum2(""+doubleUseMoney2).trim( )  +  ") 與")  +
                             " 已至採購之採發金額("  +  exeUtil.getFormatNum2(""+doubleUseMoney).trim( )  +  ") 合計 超過 "  +
                             " 至案可使用預算金額合計("  +  exeUtil.getFormatNum2(""+doubleBudgetMoney).trim( )  +  ")，請詢問 [行銷企劃室]。 "  ;                   
                if(!booleanAllNoControl) {
                    if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n" ;
                    stringErrMessage  +=  stringTemp ;
                }
            }
            // 案各業主別預算合計檢核
            // 已錯誤不作處理         避免重複檢核      
            if((!booleanAllNoControl  &&  "".equals(stringErrMessage)   &&    vectorFullProjectID1.indexOf(stringProjectID1)==-1)  ||  booleanTest) {
                // 傳入參數
                retTableData[0][0]  =  stringProjectID1+"%-%"+doubleUseMoneyThis ;
                setTableData("TableCheck",  retTableData) ;
                //
                long  longTime1  =  exeUtil.getTimeInMillis( ) ;
                getButton("ButtonProjectCheck").doClick() ;
                long longTime2  =  exeUtil.getTimeInMillis( ) ;
                System.out.println("總案別預算檢核 時間-----------------"  +  ((longTime2-longTime1))) ;
                // 回傳狀態
                retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
                stringTemp     =  retTableData[0][0].trim() ;
                System.out.println(intNo+"案各業主別預算合計檢核["+stringTemp+"]-------------------------------") ;
                // 判斷
                if(!"OK".equals(stringTemp)) {
                    if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n" ;
                    stringErrMessage  +=  stringTemp+"\n" ;
                }
                vectorFullProjectID1.add(stringProjectID1) ;
            }
            System.out.println(intNo+"("+retCheckTable.length+")FOR LOOP-----------------------------------------E") ;
        }
        retTableData          =  new  String[1][1] ;
        retTableData[0][0]  =  stringErrMessage ;//System.out.println("--------------------------------------------\n"+stringErrMessage+"\n--------------------------------\n\n\n") ;
        setTableData("TableCheck",  retTableData) ;
        return value;
    }
    public  Vector  getSpecBudgetCd( ) throws  Throwable {
        Vector      vectorBudgetCd          =  new  Vector() ;
        //
        //vectorBudgetCd.add("BC31") ;  //郵電
        //vectorBudgetCd.add("BC32") ; //水電
        //vectorBudgetCd.add("BC33") ; //其他勞務費
        //vectorBudgetCd.add("BC35") ; // 佣金
        //vectorBudgetCd.add("BC36") ; //接待中心租賃
        //vectorBudgetCd.add("BD46") ; // 刷卡手續費
        return  vectorBudgetCd ;
    }
    public  Vector  getSpecCostID( ) throws  Throwable {
        Vector      vectorCostID          =  new  Vector() ;
        //
        vectorCostID.add("26-1") ;     // 水費
        vectorCostID.add("26-2") ;     // 電費
        vectorCostID.add("23-1") ;     // 電話費
        vectorCostID.add("99-0") ;     // 佣金
        // 至 2017/01/31 仍不控管
        if("2017/01/31".compareTo(datetime.getToday("YYYY/mm/dd"))  >=  0)  vectorCostID.add("83-1") ;     // 刷卡手續費
        return  vectorCostID ;
    }
    public  Vector  getSpecProjectID1( ) throws  Throwable {
        Vector      vectorDeptCd          =  new  Vector() ;
        //
        vectorDeptCd.add("TC") ;  // 避免 getDataReorganization 判斷成內業
        vectorDeptCd.add("GT") ;    
        vectorDeptCd.add("O03A") ;    // 2012-09-17  傳環通知增加
        vectorDeptCd.add("M61A") ;    // 2012-09-17  傳環通知增加
        vectorDeptCd.add("XG2") ;       // 2014-11-28  周綺穎 通知 增加(有詢問笑怡)
        vectorDeptCd.add("F1") ;       
        vectorDeptCd.add("XG2") ;       
        return  vectorDeptCd ;
    }
    public  String[][]  getDataReorganization(String  stringFlow,  String[][]  retFunctionTypeV,  Vector  vectorSpecProjectID1,  Hashtable  hashtableBigBudget,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        // oce
        String         stringTable                               =  "Table3" ;  
        String      stringFieldName             =  "RealMoney" ;
        boolean     boolean請購資訊             =  false ;
        if(stringFlow.indexOf("請購資訊")  !=  -1) {
            stringTable         =  "Table3" ;
            stringFieldName   =  "PurchaseMoneyNow" ;
            boolean請購資訊  =  true ;
        } else if(stringFlow.indexOf("借款沖銷")  !=  -1) {
            stringTable         =  "Table2" ;
            stringFieldName   =  "RealTotalMoney" ;
        } else if(stringFlow.indexOf("借款")  !=  -1) {
            stringTable         =  "Table6" ;
            stringFieldName   =  "RealTotalMoney" ;
        } else if(stringFlow.indexOf("請款")  !=  -1) {
            stringTable         =  "Table2" ;
            stringFieldName   =  "RealTotalMoney" ;
        }
        //
        String         stringBudgetID                         =  "" ;
        String         stringInOut                               =  "" ;
        String         stringProjectID1                       =  "" ;
        String         stringCostID                             =  "" ;
        String         stringCostID1                           =  "" ;
        String         stringRealMoney                      =  "" ;
        String         stringBudgetMoney                  =  "" ;
        String      stringBigBudget               =  "" ;
        String         stringDepartNo                         =  "" ;
        String         stringType                                =  "" ;
        String         stringKEY                                =  "" ;
        String      stringComNo               =  getValue("ComNo").trim() ;
        String      stringRecordNo              =  "" ;
        String         stringBarCode                      =  getValue("BarCodeOld").trim() ;
        String      stringNoUseRealMoney      =  "" ;
        double        doubleRealMoneySum            =  0 ;
        double        doubleRealMoney                    = 0 ;
        double        doubleBudgetMoney                = 0 ;
        JTable        jtable3                                      =  getTable(stringTable) ;
        Hashtable  hashtableBudgetMoney             =  new  Hashtable() ;
        Hashtable  hashtableRealMoney                 =  new  Hashtable() ;
        Hashtable  hashtableAnd                 =  new  Hashtable();
        Vector       vectorBigBudget                   =  new  Vector() ;                                                                               // 
        Vector       vectorProjectID1                         =  new  Vector() ;                                                                                // 
        Vector       vectorProjectID1NoUseBudget   =  new  Vector() ;                                                                               // 不檢查預算
        Vector       vectorCostIDNoUseBudgetV       =  getVectorCostIDNoUseBudget( retFunctionTypeV,  exeUtil) ;             // 案-不檢查預算
        Vector        vectorBudgetCd                     =  getSpecBudgetCd( ) ;  // 特殊預算代碼，即使預算超過仍可執行
        Vector        vectorCostID                           =  getSpecCostID() ;        // 特殊請款代碼，即使預算超過仍可執行
        Vector      vectorType                  =  new  Vector() ;
        //
        if(boolean請購資訊) {
            JTable        jtable1                                      =  getTable("Table1") ;
            //
            stringCostID       =  (""+getValueAt("Table1",  jtable1.getSelectedRow(),  "CostID")).trim() ;
            stringCostID1     =  (""+getValueAt("Table1",  jtable1.getSelectedRow(),  "CostID1")).trim() ;
            stringRecordNo  =  (""+getValueAt("Table1",  jtable1.getSelectedRow(),  "RecordNo")).trim() ;
        }
        for(int  intNo=0 ;  intNo<jtable3.getRowCount( )  ;  intNo++) {
            stringInOut                     =  (""+getValueAt(stringTable,  intNo,  "InOut")).trim() ;
            stringDepartNo              =  (""+getValueAt(stringTable,  intNo,  "DepartNo")).trim() ;
            //stringProjectID               =  (""+getValueAt(stringTable,  intNo,  "ProjectID")).trim() ;
            stringProjectID1              =  (""+getValueAt(stringTable,  intNo,  "ProjectID1")).trim() ;
            //
            if(!boolean請購資訊)
            stringCostID                    =  (""+getValueAt(stringTable,  intNo,  "CostID")).trim() ;
            if(!boolean請購資訊)
            stringCostID1                 =  (""+getValueAt(stringTable,  intNo,  "CostID1")).trim() ;
            if(boolean請購資訊)
            stringNoUseRealMoney  =  (""+getValueAt(stringTable,  intNo,  "PurchaseMoneyNotUse")).trim() ;
            //
            stringRealMoney             =  (""+getValueAt(stringTable,  intNo,  stringFieldName)).trim() ;
            stringBudgetMoney         =  (stringFlow.indexOf("資訊")!=-1 || stringFlow.indexOf("請購")==-1)  ?  "0":(""+getValueAt(stringTable,  intNo,  "BudgetMoney")).trim() ;     // oce
            //
            stringRealMoney           =  ""+(exeUtil.doParseDouble(stringRealMoney)  -  exeUtil.doParseDouble(stringNoUseRealMoney)) ;
            doubleRealMoney           =  exeUtil.doParseDouble(stringRealMoney)  ;
            doubleBudgetMoney        =  exeUtil.doParseDouble(stringBudgetMoney)  ;
            //
            if("I".equals(stringInOut)  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)                            continue ;      // 內業不作案預算檢核
            if("0531".equals(stringDepartNo))                                                               continue ;      // 仲介不作案預算檢核
            if(vectorSpecProjectID1.indexOf(stringProjectID1)  !=  -1)                                              continue ;
            
            
            
            
            
            // 案別 or 部門 整理
            // 
            hashtableAnd.put("ComNo",     stringComNo) ;
            hashtableAnd.put("CostID",      stringCostID) ;
            hashtableAnd.put("CostID1",     stringCostID1) ;
            //hashtableAnd.put("UseStatus", "Y") ;
            stringBudgetID  =  exeUtil.getNameUnion("BudgetID",  "Doc2M020",  "",  hashtableAnd,  dbDoc) ;
            //
            if("".equals(stringBudgetID))                                                 continue ;      // 無對應預算代碼 不作預算檢核
            if(stringBudgetID.startsWith("A")  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)   continue ;      // 行政類預算代碼 不作預算檢核
            if(vectorCostIDNoUseBudgetV.indexOf(stringCostID+"%-%"+stringCostID1)  !=  -1)        continue ;      // 不作預算檢核 也不含於預算內
            // 資料處理 
            if(vectorBudgetCd.indexOf(stringBudgetID)==-1  &&    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)==-1) {
                stringType         =  exeUtil.doSubstring(stringBudgetID,  0,  1) ;           if(vectorType.indexOf(stringType)  ==  -1)vectorType.add(stringType) ;
                stringBigBudget  =  ""+hashtableBigBudget.get(stringBudgetID) ;       if(!"null".equals(stringBigBudget)  &&  vectorBigBudget.indexOf(stringBigBudget)==-1)  vectorBigBudget.add(stringBigBudget) ;
                //System.out.println("stringBudgetID("+stringBudgetID+")stringBigBudget("+stringBigBudget+")----------------------------------------") ;
                // 案別統計
                if(vectorProjectID1.indexOf(stringProjectID1)  ==  -1) vectorProjectID1.add(stringProjectID1) ;
            }
            
            // 採發金額
            // 案
            stringKEY               =  stringProjectID1+"-"+stringType ;
            doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;  
            doubleRealMoneySum  +=  doubleRealMoney ;
            hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
            if(vectorBudgetCd.indexOf(stringBudgetID)!=-1  ||    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)!=-1) {
                stringKEY               =  stringProjectID1+"-"+stringType+"-SPEC" ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;  
                doubleRealMoneySum  +=  doubleRealMoney ;
                hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
            }
            // 案-四大類
            if(!"null".equals(stringBigBudget)  &&  !"".equals(stringBigBudget)) {
                stringKEY               =  stringProjectID1+"-"+stringBigBudget ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;
                doubleRealMoneySum  +=  doubleRealMoney ;
                hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                if(vectorBudgetCd.indexOf(stringBudgetID)!=-1  ||    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)!=-1) {
                    stringKEY               =  stringProjectID1+"-"+stringBigBudget+"-SPEC" ;
                    doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;  
                    doubleRealMoneySum  +=  doubleRealMoney ;
                    hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                }
            }
            // 預算金額
            // 案
            stringKEY               =  stringProjectID1+"-"+stringType ;
            doubleRealMoneySum     =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;
            doubleRealMoneySum   +=  doubleBudgetMoney ;
            hashtableBudgetMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
            if(vectorBudgetCd.indexOf(stringBudgetID)!=-1  ||    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)!=-1) {
                stringKEY               =  stringProjectID1+"-"+stringType+"-SPEC" ;
                doubleRealMoneySum     =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;
                doubleRealMoneySum   +=  doubleBudgetMoney ;
                hashtableBudgetMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
            }
            // 案-四大類
            if(!"null".equals(stringBigBudget)  &&  !"".equals(stringBigBudget)) {
                stringKEY               =  stringProjectID1+"-"+stringBigBudget ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;
                doubleRealMoneySum  +=  doubleBudgetMoney ;
                hashtableBudgetMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                if(vectorBudgetCd.indexOf(stringBudgetID)!=-1  ||    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)!=-1) {
                    stringKEY               =  stringProjectID1+"-"+stringBigBudget+"-SPEC" ;
                    doubleRealMoneySum     =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;
                    doubleRealMoneySum   +=  doubleBudgetMoney ;
                    hashtableBudgetMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                }
            }
        }
        if(boolean請購資訊) {
            // Doc3M0123
            Vector        vectorDoc3M0123          =  null ;
            Hashtable  hashtableDoc3M0123     =  new  Hashtable() ;
            hashtableAnd.put("BarCode",  stringBarCode) ;
            vectorDoc3M0123  =  exeUtil.getQueryDataHashtable("Doc3M0123",  hashtableAnd,  "  AND  RecordNo  <>  "  +  stringRecordNo +  " ",  dbDoc)  ;
            for(int  intNo=0 ;  intNo<vectorDoc3M0123.size()  ;  intNo++) {
                hashtableDoc3M0123      =  (Hashtable)  vectorDoc3M0123.get(intNo) ;  if(hashtableDoc3M0123  ==  null)  continue ;
                stringCostID                        =  (""+hashtableDoc3M0123.get("CostID")).trim() ;
                stringCostID1                     =  (""+hashtableDoc3M0123.get("CostID1")).trim() ;
                stringDepartNo                    =  (""+hashtableDoc3M0123.get("DepartNo")).trim() ;
                stringRealMoney                 =  (""+hashtableDoc3M0123.get("PurchaseMoney")).trim() ;
                stringNoUseRealMoney      =  (""+hashtableDoc3M0123.get("NoUseRealMoney")).trim() ;
                stringProjectID1                  =  (""+hashtableDoc3M0123.get("ProjectID1")).trim() ;
                //
                stringRealMoney   =  ""+(exeUtil.doParseDouble(stringRealMoney)  -  exeUtil.doParseDouble(stringNoUseRealMoney)) ;
                stringRealMoney   =  convert.FourToFive(stringRealMoney,  0) ;
                doubleRealMoney =  exeUtil.doParseDouble(stringRealMoney)  ;
                // 案別 or 部門 整理
                if("I".equals(stringInOut)  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)                            continue ;      // 內業不作案預算檢核
                if("0531".equals(stringDepartNo))                                                               continue ;      // 仲介不作案預算檢核
                if(vectorSpecProjectID1.indexOf(stringProjectID1)  !=  -1)                                              continue ;
                
                //
                hashtableAnd.put("ComNo",     stringComNo) ;
                hashtableAnd.put("CostID",      stringCostID) ;
                hashtableAnd.put("CostID1",     stringCostID1) ;
                stringBudgetID  =  exeUtil.getNameUnion("BudgetID",  "Doc2M020",  "",  hashtableAnd,  dbDoc) ;
                stringType          =  (stringBudgetID.length()>0) ? stringBudgetID.substring(0,  1) : "" ;
                //
                if(!"B".equals(stringType)) continue ;
                // 採發金額
                // 案
                stringKEY               =  stringProjectID1+"-"+stringType ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;  
                doubleRealMoneySum  +=  doubleRealMoney ;
                hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                // 案-四大類
                stringBigBudget  =  ""+hashtableBigBudget.get(stringBudgetID) ;     
                if(vectorBigBudget.indexOf(stringBigBudget)  !=  -1) {
                    stringKEY               =  stringProjectID1+"-"+stringBigBudget ;
                    doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;
                    doubleRealMoneySum  +=  doubleRealMoney ;
                    hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                }
            }
        }
        Vector    vectorTableCheck                    =  new  Vector() ;
        String[]   arrayTemp                                =  null ;
        double    doubleBudgetMoneySumL         =  0 ;
        double    doubleBudgetMoneySumL_1     =  0 ;
        double    doubleBudgetMoneySumL2        =  0 ;
        double    doubleBudgetMoneySumL2_1    =  0 ;
        //
        for(int  intNo=0  ;  intNo<vectorProjectID1.size()  ;  intNo++) {
            stringProjectID1                    =  (""+vectorProjectID1.get(intNo)).trim() ;
            // 企劃
            for(int  intNoL=0  ;  intNoL<vectorType.size()  ;  intNoL++) {
                stringType  =  ""+vectorType.get(intNoL) ;if("".equals(stringType)) continue ;
                //
                stringKEY                       =  stringProjectID1+"-"+stringType ;
                doubleBudgetMoneySumL         =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;              // 案-採發
                doubleBudgetMoneySumL_1       =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY+"-SPEC")) ;      // 案-採發不控管
                doubleBudgetMoneySumL2      =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;            // 案-預算
                doubleBudgetMoneySumL2_1    =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY+"-SPEC")) ;    // 案-預算不控管
                if(doubleBudgetMoneySumL>0  ||  doubleBudgetMoneySumL2>0) {
                    arrayTemp                      =  new  String[6] ;
                    arrayTemp[0]             =  stringProjectID1 ;                                      // 案別
                    arrayTemp[1]             =  "B" ;                                               // 
                    arrayTemp[2]             =  convert.FourToFive(""+doubleBudgetMoneySumL,    0) ;      // 案-採發-使用金額
                    arrayTemp[3]             =  convert.FourToFive(""+doubleBudgetMoneySumL2,  0) ;       // 案-預算-使用金額
                    arrayTemp[4]             =  convert.FourToFive(""+doubleBudgetMoneySumL_1,      0) ;      // 案-採發-不控管金額
                    arrayTemp[5]             =  convert.FourToFive(""+doubleBudgetMoneySumL2_1,     0) ;      // 案-預算-不控管金額
                    vectorTableCheck.add(arrayTemp) ;
                }
            }
        }
        if("B30181".equals(getUser()))
        for(int  intNo=0  ;  intNo<vectorProjectID1.size()  ;  intNo++) {
            stringProjectID1                    =  (""+vectorProjectID1.get(intNo)).trim() ;
            // 四大類
            for(int  intNoL=0  ;  intNoL<vectorBigBudget.size()  ;  intNoL++) {
                stringBigBudget  =  ""+vectorBigBudget.get(intNoL) ;
                stringKEY         =  stringProjectID1+"-"+stringBigBudget ;
                //
                System.out.println(intNoL+"四大類------------------------------------1") ;
                if(exeUtil.doParseDouble(stringBigBudget)  <=  0)  continue ;
                //
                doubleBudgetMoneySumL               =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;
                doubleBudgetMoneySumL_1               =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY+"-SPEC")) ;
                doubleBudgetMoneySumL2              =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;            
                doubleBudgetMoneySumL2_1            =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY+"-SPEC")) ;  
                System.out.println(intNoL+"四大類("+doubleBudgetMoneySumL+")("+doubleBudgetMoneySumL2+")------------------------------------2") ;
                if(doubleBudgetMoneySumL>0  ||  doubleBudgetMoneySumL2>0) {
                    arrayTemp                      =  new  String[6] ;
                    arrayTemp[0]             =  stringProjectID1 ;                                            // 案別
                    arrayTemp[1]             =  stringBigBudget ;                                         // stringBigBudget
                    arrayTemp[2]             =  convert.FourToFive(""+doubleBudgetMoneySumL,    0) ;          // 案-採發-使用金額
                    arrayTemp[3]             =  convert.FourToFive(""+doubleBudgetMoneySumL2,  0) ;         // 案-預算-使用金額
                    arrayTemp[4]             =  convert.FourToFive(""+doubleBudgetMoneySumL_1,    0) ;      // 案-採發-不控管金額
                    arrayTemp[5]             =  convert.FourToFive(""+doubleBudgetMoneySumL2_1,  0) ;       // 案-預算-不控管金額
                    vectorTableCheck.add(arrayTemp) ;
                    System.out.println(intNoL+"四大類("+vectorTableCheck.size()+")------------------------------------3") ;
                }
            }
        }
        if(vectorTableCheck.size()  >  0) {
            return  (String[][])vectorTableCheck.toArray(new  String[0][0]) ;
        } 
        return  new  String[0][0] ;
    }
    // 通知
    public void doMail(String  stringTxt,  String[][]  retTable,  String[][]  retTable2,  FargloryUtil  exeUtil)throws Throwable{
        String                             stringDocNo     =  getValue("DocNo1").trim()+"-"+getValue("DocNo2").trim()+"-"+getValue("DocNo3").trim() ;
        String                             stringBarCode  =  getValue("BarCodeOld").trim( ) ;;
        //
        String    stringFunction    =  getFunctionName() ;
        String    stringSubject      =  stringFunction+"之新舊年度算檢核錯誤 通知" ;
        String    stringContent     =  stringSubject  +  "<br>條碼編號：["+stringBarCode+"]<br>公文編號：["+stringDocNo+"]<br>"+stringTxt ;
        String    stringSend          =  "B3018@farglory.com.tw" ;
        String[]  arrayUser           =  {stringSend} ;
        for(int  intNo=0  ;  intNo<retTable.length  ;  intNo++) {
            stringContent  +=  "<br>"+intNo+"---原["+retTable[intNo][0]+"]"  ;
        }
        for(int  intNo=0  ;  intNo<retTable2.length  ;  intNo++) {
            stringContent  +=  "<br>"+intNo+"---新["+retTable2[intNo][0]+"]"  ;
        }
        exeUtil.doEMail(stringSubject,  stringContent,  stringSend,  arrayUser) ;
    }
    //
    public  Vector  getVectorCostIDNoUseBudget(String[][]  retFunctionTypeV,  FargloryUtil  exeUtil) throws  Throwable {
        String  stringCostID                  =  "" ;
        String  stringCostID1               =  "" ;
        Vector  vectorCostIDNoUseBudget    =  new  Vector() ;
        for(int  intNo=0  ;  intNo<retFunctionTypeV.length  ;   intNo++) {
            stringCostID    =  retFunctionTypeV[intNo][0].trim() ;
            stringCostID1  =  retFunctionTypeV[intNo][1].trim() ;
            //
            vectorCostIDNoUseBudget.add(stringCostID+"%-%"+stringCostID1) ;
        }
        return  vectorCostIDNoUseBudget ;
    }
    public  String  getCostIDNoUseBudgetSql(String[][]  retFunctionTypeV,  FargloryUtil  exeUtil) throws  Throwable {
        String  stringCostID                  =  "" ;
        String  stringCostID1               =  "" ;
        String  stringSqlAnd                =  "" ;
        for(int  intNo=0  ;  intNo<retFunctionTypeV.length  ;   intNo++) {
            stringCostID    =  retFunctionTypeV[intNo][0].trim() ;
            stringCostID1  =  retFunctionTypeV[intNo][1].trim() ;
            //
            if(!"".equals(stringSqlAnd))  stringSqlAnd  +=  ", " ;
            stringSqlAnd  +=  " '"+stringCostID+stringCostID1+"' " ;
        }
        if(!"".equals(stringSqlAnd)) {
            stringSqlAnd  =  " AND  RTRIM(CostID)+RTRIM(CostID1)  NOT  IN ("+stringSqlAnd+")" ;
        }
        return  stringSqlAnd ;
    }
    // 資料庫 Doc
    // 資料庫 Doc2M010...
    public  String[]  getCheckMoneyFront(String  stringProjectID1,  String  stringBudgetType,  String  stringBarCodeOld,  String[][]  retFunctionTypeV,  boolean  booleanFlow,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        String    stringTemp                                 =  "" ;  
        String    stringCostIDNoUseBudgetVSql    =  getCostIDNoUseBudgetSql(retFunctionTypeV,  exeUtil) ;
        String[]  retCheckMoney                           =  getCheckMoneyDoc3M014(stringProjectID1,  stringBudgetType,  stringBarCodeOld,  stringCostIDNoUseBudgetVSql,  booleanFlow,  exeUtil,  dbDoc) ;
        double  doubleTemp                              = 0 ;
        // 0  採發金額                    1  預估金額                   2  案預算使用中       3  案預算流程中
        return  retCheckMoney ;
    }
    public  String[]  getCheckMoneyDoc3M014(String  stringProjectID1,  String  stringBudgetType,  String  stringBarCodeOld,  String  stringCostIDNoUseBudgetVSql,  boolean  booleanFlow,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        String  stringComNo           =  getValue("ComNo").trim() ;
        String  stringSql                     =  "" ;
        String  stringSqlAnd              =  "" ;
        String  stringInOut                 =  "O" ;
        //String   stringTable         =  "B3018".equals(getUser()) ? "Doc3M014_BudgetCheckTemp" : "Doc3M014_UseMoney_view" ;
        //String   stringTable         =  "B3018".equals(getUser()) ? "Doc3M014_UseMoney_BudgetCheck_view" : "Doc3M014_UseMoney_view" ;
        String   stringTable         =  "Doc3M014_UseMoney_BudgetCheck_view" ;
        //
        stringSqlAnd  =  " WHERE  ProjectID1  =  '"   +  stringProjectID1  +  "' \n" +
                         stringCostIDNoUseBudgetVSql +" \n" ;
        if(!"".equals(stringInOut))         stringSqlAnd  +=  " AND  InOut  =  '"           +  stringInOut       +  "' \n" ;
        if(stringBudgetType.length()  ==  1) {
                  stringSqlAnd  +=  " AND  RTRIM(CostID)+RTRIM(CostID1)  IN  (SELECT  RTRIM(CostID)+RTRIM(CostID1) \n"  +
                                                                                 " FROM  Doc2M020 \n"  +
                                                                               " WHERE  BudgetID  LIKE  '"+stringBudgetType+"%' \n" +
                                                                                   " AND  ComNo  =  'CS') \n" ;
        } else {
                  stringSqlAnd  +=  " AND  RTRIM(CostID)+RTRIM(CostID1)  IN  (SELECT  RTRIM(M20.CostID)+RTRIM(M20.CostID1) \n"  +
                                                                                 " FROM  Doc2M020 M20,  Doc7M072 M72 \n"  +
                                                                               " WHERE  M20.BudgetID  =  M72.BudgetID \n"  +
                                                                           " AND  M72.BigBudget  =  '"+stringBudgetType+"' \n" +
                                                                                   " AND  M20.ComNo  =  'CS') \n" ;
        }
        // 0  採發金額        1  採發金額            2  預估金額                   3  案預算使用中      4  案預算流程中
        stringSql    =  " SELECT \n " ;
        //   0  採發金額
        stringSql  +=  "(SELECT  SUM(RealMoney) \n"  +
                   " FROM  "+stringTable+" \n"  +
                  stringSqlAnd  +
                                  " AND  BarCode  <>  '"+stringBarCodeOld+"' \n"  +
                   " AND  ComNo  =  '"+stringComNo+"' \n" +
                   " AND  CostID1  <> '' " +
                   " AND  TYPE  =  'A' \n" +
                             ") AS '0  採發金額1' , \n" ;
        stringSql  +=  "(SELECT  SUM(RealMoney) \n"  +
                    " FROM  "+stringTable+" \n"  +

                  convert.replace(stringSqlAnd,  "+RTRIM(CostID1)",  "")  +
                                  " AND  BarCode  <>  '"+stringBarCodeOld+"' \n"  +
                   " AND  ComNo  =  '"+stringComNo+"' \n" +
                   " AND  CostID1  = '' " +
                   " AND  TYPE  =  'A' \n" +
                             ") AS '1  採發金額2' , \n" ;
        //  1  預估金額
        if(booleanFlow) {
            stringSql  +=  " 0, \n" ;
        } else {
            stringSql  +=  "(SELECT  SUM(RealMoney) \n"  +
                        " FROM  "+stringTable+"\n"  +
                     stringSqlAnd+
                                       " AND  BarCode  <>  '"+stringBarCodeOld+"' \n"  +
                        " AND  ComNo  =  '"+stringComNo+"' \n" +
                        " AND  TYPE  =  'B' \n" +
                        " AND  RealMoney > 0 \n" +
                                   ") AS '2  預估金額', \n" ;
        }
        //  2  CHECK(案合計) 1
        stringSql         +=  "(SELECT  SUM(M21.BudgetMoney) \n"  +
                     " FROM  Doc7M020 M20,  Doc7M021 M21 \n"  +
                      " WHERE  M20.ComNo             =  M21.ComNo \n"  +
                           " AND  M20.ProjectID1        =  M21.ProjectID1 \n"  +
                        " AND  M20.FunctionType  =  M21.FunctionType \n"  +
                        " AND  M20.BuildYMD        =  M21.BuildYMD \n"  +
                        " AND  M20.ProjectID1  =  '"     +  stringProjectID1                     +"' \n" +
                        " AND  M20.ComNo  =  '"           +  stringComNo                          +  "' \n" +
                        " AND  M20.STATUS =  'U' \n" ;
        if(stringBudgetType.length()  ==  1) {
            stringSql  +=      "  AND  M21.FunctionType  =  '"+  stringBudgetType+"' \n"  ;
        } else {
            stringSql  +=      "  AND  M21.BudgetID  IN  (SELECT  BudgetID \n"  +
                                        " FROM  Doc7M072 \n"  +
                                         " WHERE  BigBudget  =  '"+stringBudgetType+"') \n" ;
        }
        stringSql  +=  ") AS '3  CHECK(案) 1', \n" ;
        //  7  CHECK(案合計) 1
        stringSql         +=  "(SELECT  SUM(M21.BudgetMoney) \n"  +
                      " FROM  Doc7M020 M20,  Doc7M021 M21 \n"  +
                    " WHERE  M20.ComNo             =  M21.ComNo \n"  +
                         " AND  M20.ProjectID1        =  M21.ProjectID1 \n"  +
                      " AND  M20.FunctionType  =  M21.FunctionType \n"  +
                      " AND  M20.BuildYMD        =  M21.BuildYMD \n"  +
                      " AND  M20.ProjectID1  =  '"     +  stringProjectID1                     +"' \n" +
                      " AND  M20.ComNo  =  '"           +  stringComNo                          +  "' \n" +
                      " AND  M20.STATUS IN ('F','R') \n" ;
        if(stringBudgetType.length()  ==  1) {
            stringSql  +=      "  AND  M21.FunctionType  =  '"+  stringBudgetType+"' \n"  ;
        } else {
            stringSql  +=      "  AND  M21.BudgetID  IN  (SELECT  BudgetID \n"  +
                                                " FROM  Doc7M072 \n"  +
                                                " WHERE  BigBudget  =  '"+stringBudgetType+"') \n" ;
        }
        stringSql  +=  ") AS '4  CHECK(案) 2' \n" ;
        System.out.println(stringProjectID1+"--------------getCheckMoney-----------------------------------");
        String[][]  retData  =  dbDoc.queryFromPool(stringSql) ;
        //
        return  retData[0] ;
    }
    // 資料表 Doc2M020
    public  String  getDoc2M020Sql(String  stringBudgetType,  talk  dbDoc) throws  Throwable {
        String      stringSql                         =  "" ;
        String      stringDescription            =  "" ;
        String      stringCostID                   =  "" ;
        String[][]  retDoc2M020                 =  null ;
        Vector    vectorCostID                   =  new  Vector() ;
        // 0  CostID      1  CostID1
        if(stringBudgetType.length()  ==  1) {
            stringSql  =  "SELECT  M20.CostID,  M20.CostID1 "  +
                       " FROM  Doc2M020 M20 "  +
                       " WHERE  M20.BudgetID  LIKE  '"  +  stringBudgetType  +  "%' " ;
        } else {
            stringSql  =  "SELECT  M20.CostID,  M20.CostID1 "  +
                       " FROM  Doc2M020 M20,  Doc7M072 M72 "  +
                       " WHERE  M20.BudgetID  =  M72.BudgetID "  +
                                     " AND  M72.BigBudget  =  '"+stringBudgetType+"' " ;
        }
        retDoc2M020  =  dbDoc.queryFromPool(stringSql) ;
        stringSql          =  "" ;
        for(int  intNo=0  ;  intNo<retDoc2M020.length  ;  intNo++) {
            stringCostID  =  retDoc2M020[intNo][1].trim() ;
            //
            if(vectorCostID.indexOf(stringCostID)  !=  -1)  continue ;
            vectorCostID.add(stringCostID) ;
            //
            if(!"".equals(stringSql))  stringSql  +=  ", " ;
            stringSql  +=  " '"+stringCostID+"' " ;
        }
        return  stringSql ;
    }
    // 資料表 Doc2M0201
    public  String[][]  getFunctionTypeDoc2M0201(String  stringData,  String  stringFunctionType,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        String      stringSql                         =  "" ;
        String      stringDataAC              =  exeUtil.getDateConvert(stringData) ;
        String      stringComNo                    =  getValue("ComNo").trim() ;
        String[][]  retDoc2M0201                 =  null ;
        // 0  CostID      1  CostID1
        stringSql  =  "SELECT  CostID,  CostID1 "  +
                     " FROM  Doc2M0201 "  +
                   " WHERE  (ComNo='ALL'  OR ComNo  =  '"+stringComNo+"') " +
                         " AND  FunctionType  =  '"+stringFunctionType+"' "  +
                      " AND  (DateStart  =  '9999/99/99'  OR  DateStart  <=  '"+stringDataAC+"') "+
                      " AND  (DateEnd   =  '9999/99/99'   OR  DateEnd   >=  '"+stringDataAC+"') "+
                      "";
        stringSql          +=  " ORDER BY CostID,  CostID1 " ;
        retDoc2M0201  =  dbDoc.queryFromPool(stringSql) ;
        return  retDoc2M0201 ;
    }
    // 資料表 Doc7M072
    public  void  doDoc7M072Data(talk  dbDoc,  Hashtable  hashtableBudgetIDs,  Hashtable  hashtableBigBudget) throws  Throwable {
        String      stringSql                         =  "" ;
        String      stringBigBudget              =  "" ;
        String      stringBudgetID                =  "" ;
        String[][]  retDoc7M072                 =  null ;
        Vector     vectorBudgetID         =  null ;
        //
        stringSql  =  "SELECT  BigBudget,  BudgetID "  +
                   " FROM  Doc7M072 "  +
                " ORDER BY  BigBudget " ;
        retDoc7M072  =  dbDoc.queryFromPool(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc7M072.length  ;  intNo++) {
            stringBigBudget  =  retDoc7M072[intNo][0].trim() ;
            stringBudgetID    =  retDoc7M072[intNo][1].trim() ;
            // 大預算分類整理
            vectorBudgetID  =  (Vector) hashtableBudgetIDs.get(stringBigBudget) ;
            if(vectorBudgetID  ==  null) {
                vectorBudgetID  =  new  Vector() ;
                hashtableBudgetIDs.put(stringBigBudget,  vectorBudgetID) ;
            }
            if(vectorBudgetID.indexOf(stringBudgetID)  ==  -1)vectorBudgetID.add(stringBudgetID) ;
            // 預算代碼 對應 大預算分類
            hashtableBigBudget.put(stringBudgetID,  stringBigBudget) ;
        }
        
        
  }
  public String getInformation(){
    return "---------------ButtonTableCheck(\u5e74\u5ea6\u9810\u7b97\u6aa2\u6838).defaultValue()----------------";
  }
}
