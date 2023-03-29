package Doc.Doc2 ;
import     jcx.jform.bTransaction;
import     java.io.*;
import     java.util.*;
import     jcx.util.*;
import     jcx.html.*;
import     jcx.db.*;
import     Farglory.util.FargloryUtil ;
import     javax.swing.* ;
import     Doc.Doc2M010 ;
public  class  Doc2M014  extends  bTransaction {
    talk   dbDoc          =  getTalk(""  +  get("put_Doc")) ;
    talk  dbFED1        =  getTalk(""  +  get("put_FED1"));
    //talk  dbRent          =  getTalk(""  +  get("put_Rent"));
    talk  dbRent          =  getTalk(""  +  get("put_Doc"));
    public  boolean  action(String  value) throws  Throwable {
        // isAssetOK                      固定資產檢核
        // doWriteVoucher                   1對1 傳票沖銷(固資)
        // doSaveDoc2M0142          固資 驗收日期 維護(Doc2I0142)
        // isA312105BackRentSysOK       租賃退款-暫收款 回押租賃系統
        //
        // 回傳值為 true 表示執行接下來的資料庫異動或查詢
        // 回傳值為 false 表示接下來不執行任何指令
        // 傳入值 value 為 "新增","查詢","修改","刪除","列印","PRINT" (列印預覽的列印按鈕),"PRINTALL" (列印預覽的全部列印按鈕) 其中之一
        Doc2M010      exeFun                     =  new  Doc2M010( ) ;
        FargloryUtil        exeUtil                       =  new  FargloryUtil() ;
        String               stringBarCode           =  getValue("BarCode").trim( ) ;
        String               stringComNo              =  (""+getValueAt("Table1",  0,  "COMPANY_CD")).trim() ;
        boolean            booleanSource          =  (exeFun.getDoc2M010(stringBarCode).length  >  0) ;  // true 行銷  false 一般物料
        boolean            booleanSource272    = booleanSource? false : exeFun.getDoc5M0272(stringBarCode,  "").length>0 ;  // true 不存在  false 存在 
        boolean            booleanComNoType  =  "E".equals(exeFun.getComNoType(stringComNo)) ;    // true 基金會  false 非基金會
        String[][]           retDoc5M0224            =  exeFun.getDoc5M0224(stringBarCode,  "") ;
        Hashtable        hashtableAsset           =  new  Hashtable() ;
        Vector          vectorRentSql        =  new  Vector() ;
        //
        if(booleanComNoType) {
            exeFun.doErrorEmail("基金會----請款申請書轉傳票") ;
        }
        // 退保留款
        String[][]  retDoc2M010              =  null ;
        if(booleanSource) {
            retDoc2M010                            =  exeFun.getDoc2M010(stringBarCode) ;
        } else {
            retDoc2M010                            =  exeFun.getDoc5M020(stringBarCode) ;
        }
        //
        put("Doc2M014_VoucherYMD_Status",  "NoAction") ;
        if(!isBatchCheckOK(booleanSource272,  retDoc5M0224,  retDoc2M010,  booleanSource,  booleanComNoType,  exeFun,  exeUtil,  hashtableAsset,  vectorRentSql))  {  put("Doc2M014_VoucherYMD_Status",  "null") ;  return false ; }
        if("B3018".equals(getUser())) return  false ;
        put("Doc2M014_VoucherYMD_Status",  "null") ;
        if("修改".equals(value.trim( ))) {  
            String      stringRowType         =  (""+getValueAt("Table1",  0,  "RowType")).trim() ;
            String      stringVoucherType  =  getValue("VoucherType").trim( ) ;
            String[][]  retDoc2M014           =  getTableData("Table1") ;
            if(retDoc2M014.length  ==  0)  return  false ;
            // 轉傳票
            //doDoc2M010etc(booleanSource,  exeFun,  exeUtil) ;
            doInsertDB(booleanSource,  booleanComNoType,  retDoc2M014,  retDoc5M0224,  retDoc2M010,  hashtableAsset,  exeFun,  exeUtil,  vectorRentSql) ;
            if(",B,".indexOf(stringVoucherType)  !=  -1) {
                String[][]  retDoc2M0143  =  exeFun.getTableDataDoc("SELECT  COMPANY_CD,  VOUCHER_YMD,  VOUCHER_FLOW_NO  FROM  Doc2M0143  WHERE  BarCode  =  '"+stringBarCode+"'  AND  STATUS_CD = 'Z'") ;
                String      stringTemp        =  ""; 
                stringTemp  +=  retDoc2M0143[0][1].trim().replaceAll("/",  "")+"-" +
                              convert.add0(retDoc2M0143[0][2].trim(),  "5") ;
                setValue("VOUCHER_FLOW_NO",  stringTemp) ;
                put("Doc2M014_VOUCHER_YMD",  "") ;
                messagebox("預估傳票-資料庫異動完成") ;
            }
            if("TRUE".equals(""+get("Doc2M014_ERROR"))) {
                getButton("button2").doClick( ) ;//顯示傳票訊息
                // 通路活動 更新 已使用金額 
                getButton("ButtonSSMediaIDUpdate").doClick() ;
                //
                if(",A,C,".indexOf(stringVoucherType)  !=  -1) {
                    doUpdateDoc1M030(exeFun,  exeUtil) ;
                    messagebox("資料庫異動完成。") ;
                    getButton(3).setEnabled(false) ;  // 修改
                    getInternalFrame("請款申請書-轉傳票(Doc2M014)").setVisible(false) ;
                    getInternalFrame("請款申請書-傳票彙總(Doc2M014)").setVisible(false) ;
                    getInternalFrame("請款申請書-傳票彙總(Doc2M014)").setVisible(true) ;
                    action(2) ;// 查詢
                }
            }
        }
        return false;
    }
    public  void  doUpdateDoc1M030(Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        int            intChar                        =  24 ; // (傳票號碼：960605-00004)
        String      stringSql                      =  "" ;
        String      stringBarCode             =  getValue("BarCode").trim() ;
        String      stringVoucherFlowNo  =  getValue("VOUCHER_FLOW_NO").trim() ;
        String      stringDescript              =  "" ;
        String      stringDescriptB            =  "" ;
        String[][]  retDoc1M030              =  exeFun.getSelectDoc1M030(stringBarCode,  "") ;
        boolean   booleanDB                 =  true ;
        //
        if(retDoc1M030.length  !=  1)  return ;
        //
        stringDescript    =  retDoc1M030[0][13].trim() ;
        stringDescript    =  convert.replace(stringDescript,  "'",  "''") ;
        int     intPos      =  stringDescript.indexOf("傳票號碼") ;
        if(intPos  !=  -1){
              stringDescript  =  stringDescript.substring(0,  intPos-1) ;
        }
        //
        stringDescriptB  =  convert.StrToByte(stringDescript) ;
        if(stringDescriptB.length()  >  255-intChar) {
            String  stringTemp  =  "" ;
                for(int  intNo=0  ;  intNo<2  ;  intNo++) {
                stringTemp  =  convert.ByteToStr(stringDescriptB.substring(0,  stringDescriptB.length()-intChar-4-intNo)) ;
                if(stringDescript.indexOf(stringTemp)  !=  -1) {
                    break ;
                }
            }
            stringDescriptB  =  stringDescript ;
            stringDescript    =  stringTemp+"....(傳票號碼："+stringVoucherFlowNo+")" ;
        } else {
            stringDescriptB  =  stringDescript ;
            stringDescript    =  stringDescript+"(傳票號碼："+stringVoucherFlowNo+")" ;
        }
        //
        stringSql          =  exeFun.doUpdateDoc1M030(stringDescript,  stringBarCode,  booleanDB) ;
        // E-mail
        String                             stringSubject          =  "請款申請書-轉傳票同步" ;
        String                             stringSend              =  "B3018@farglory.com.tw" ;
        String[]                            arrayUser               =  {"B3018@farglory.com.tw"} ;
        //exeUtil.doEMail(stringSubject,  "原本公文內容"+stringDescriptB+"<br>"+stringSql,  stringSend,  arrayUser) ;
    }
    public  void  doInsertDB(boolean  booleanSource,  boolean  booleanComNoType,  String[][]  retDoc2M014,  String[][]  retDoc5M0224,  String[][]  retDoc2M010,  Hashtable  hashtableAsset,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorRentSql) throws  Throwable {
          put("Doc2M014_ERROR",  "null") ;
        //
        getButton("button9").doClick() ;
        //
        String         stringSubject                           =  getFunctionName() ;
          String         stringSend                               =  "B3018@farglory.com.tw" ;
        String         stringAssetStatus                    = ""+hashtableAsset.get("STATUS") ;  // OK
          String[]       arrayUser                                =  {"B3018@farglory.com.tw"} ;
          String         stringMessage                         =  "" ;                                                      
        int               intVoucherSeqNo                     =  1 ;//
        int               intRowTypeD                            = 0 ;
        String         stringBarCode                          =  getValue("BarCode").trim( ) ;
        String         stringVoucherYMD                   =  "" ;//
        String         stringVoucherYMDRoc             =  "" ;//
        String         stringVoucherFlowNo                =  "" ;//
        String         stringVoucherSeqNo                 =  "" ;//
        String         stringAccountNo                       =  "" ;//
        String         stringDBCrCd                           =  "" ;//
        String         stringDBCrCd1003                   =  "" ;//
        String         stringCompanyCd                     =  "" ;//
        String         stringCompanyCdO                   =  "" ;//
        String         stringObjectCd                          =  "" ;//
        String         stringKind                                  =  "0" ;
        String         stringSql                                    =  "" ;
        String         stringMaxInvoiceNo                   =  "" ;
        String         stringDiscountNo                      =  "" ;
        String         stringDepartNo                         =  "" ;
        String          stringStatusCd                        =  "" ;//
        String          stringRowType                         =  "" ;
        String[][]      retDoc2M015                            =  null ;
        String[][]      retDoc2M012                            =  null ;
        String[][]      retFED1003                              =  null ;//
        double        doubleAmtD                              = 0 ;
        double        doubleAMT                               =  0 ;//
        double        doubleMaxAMT                         =  0 ;
        double        doubleInvoiceTotalMoneySum  =  0 ;
        double        doubleRealMoneySum              =  0 ;
        double        doubleTaxRate                          =  exeUtil.doParseDouble(exeFun.getDoc2M040()[4].trim( ))  ;
        Vector         vectorSql                                  =  new  Vector( ) ;
        Vector         vectorDocSql                            =  new  Vector( ) ;
        Vector        vectorError                               =  new  Vector( ) ;
        Vector        vectorYOrN                              =  new  Vector( ) ;
        Vector         vectorVoucherSeqNo               =  new  Vector( ) ;
        Vector         vectorAMTYearEnd                  =  new  Vector( ) ;
        String          stringUser                                  =  getUser( ).toUpperCase() ;
        String          stringUserN                                =  getUser( ).toUpperCase() ;
        Hashtable   hashtableVoucherAmt               =  new  Hashtable() ;
        // 退保留款
        if(booleanSource) {
            doubleRealMoneySum              = exeUtil.doParseDouble(exeFun.getRealMoneySumForDoc2M012(stringBarCode)) ; 
            doubleInvoiceTotalMoneySum  =  exeFun.getInvoiceMoneySum(stringBarCode)  +  exeFun.getInvoiceTaxSum(stringBarCode) ;
        } else {
            doubleRealMoneySum              =  exeUtil.doParseDouble(exeFun.getRealMoneySumForDoc2M012Union("Doc5M022",  stringBarCode)) ; 
            doubleInvoiceTotalMoneySum  =  exeFun.getInvoiceMoneySum("Doc5M021",  stringBarCode)  +  exeFun.getInvoiceTaxSum("Doc5M021",  stringBarCode) ;
        }
        String      stringRetainBarCode  =  retDoc2M010[0][26].trim() ;
        String[][]  retDoc5M02201         =  new  String[0][0] ;
        if(!booleanSource  &&  !"".equals(stringRetainBarCode)) {
            retDoc5M02201        =  exeFun.getDoc5M02201(stringBarCode,  "",  "") ;
            if(retDoc5M02201.length  ==  0) {
                // E-mail 通知
                stringMessage  =  "此次轉傳票條碼編號："  +  stringBarCode  +  "，無對應 Doc5M02201 資料，無法沖帳。"+getUser() ;
                exeUtil.doEMail(stringSubject,  stringMessage,  stringSend,  arrayUser) ;
                put("Doc2M014_ERROR",  "FALSE") ;
                return ;
            }
        }
        // 刪除
        exeFun.doDeleteDoc2M014(stringBarCode,  exeUtil) ;
        // 折讓
        if(!"".equals(stringBarCode)) {
            stringSql  =  "DELETE  FROM Doc2M016 WHERE  BarCode  =  '"+stringBarCode+"' " ;
            vectorSql.add(stringSql) ;
            //
            exeFun.doExecVectorSqlForDoc(vectorSql) ;
        }
        vectorSql  =  new  Vector() ;
        //
        stringVoucherYMDRoc  =  exeUtil.getDateConvertRoc(retDoc2M014[0][4].trim( )).replaceAll("/",  "") ;
        stringVoucherYMD         =  exeUtil.getDateConvert(stringVoucherYMDRoc) ;
        stringCompanyCdO       =  retDoc2M014[0][7].trim( ) ;
        stringCompanyCd          =  convert.addSpace("",  2-stringCompanyCdO.length())  +  stringCompanyCdO ;
        stringStatusCd               =  retDoc2M014[0][17].trim( ) ;
        put("Doc2M014_ERROR",  "TRUE") ;
        //
        stringUserN  =  exeFun.getVoucherEmpNo(stringUserN,  stringCompanyCd) ;
        try{
            // 取得傳票號碼
            stringVoucherFlowNo  =  exeFun.getVoucherFlowNo("R",  "A",  stringCompanyCd+stringVoucherYMDRoc,  "0",  booleanComNoType) ;
            System.out.println("取得傳票號碼---------------------------------------"+stringVoucherFlowNo) ;
            intVoucherSeqNo        =  1 ;
            //
            if(!"".equals(stringCompanyCd)  &&  "10,12,".indexOf(stringCompanyCd)!=-1) {
                String  stringTempL  =  "" ;
                for(int  intNo=0  ;  intNo<retDoc2M014.length  ;  intNo++) {
                    stringTempL  =  retDoc2M014[intNo][23].trim( ) ;
                    //
                    if(!stringTempL.startsWith("#")  &&  !stringTempL.startsWith("*")) {
                        stringTempL  =  exeUtil.doSubstringByte("#"+stringTempL,  0,  30) ;
                    }
                    //
                    retDoc2M014[intNo][23]  =  stringTempL ;
                }
            }
            // 0010 訂簽款轉存信託專戶 特別處理 S
            int               intDBCrCdC                       =  0 ;
            int               intRecordNo                      =  0 ;
            boolean     booleanCostID0010          =  true ;
            String         stringRecordNo                 =  "" ;
            String         stringLastYMD                   =  "" ;
            String[]       arrayVouceherData          =  new  String[12] ;
            String[]       arrayWriteVouceherData  =  new  String[12] ;
            String[][]     ret0010Doc2M012            =  null ;
            Hashtable  hashtableCanWriteAmt     =  null ;
            //
            if(booleanSource  &&  retDoc5M0224.length>0) {
                if(ret0010Doc2M012==null) {
                    ret0010Doc2M012  =  exeFun.getDoc2M012Union("Doc2M012",  stringBarCode) ;
                    for(int  intNoL=0  ;  intNoL<ret0010Doc2M012.length  ;  intNoL++) {
                        if(",0010,".indexOf(","+ret0010Doc2M012[intNoL][4].trim()+ret0010Doc2M012[intNoL][5].trim()+",")  ==  -1) {
                            booleanCostID0010  =  false ;
                            break ;
                        } else {
                            booleanCostID0010  =  true ;
                        }
                    }
                }
            } else {
                booleanCostID0010  =  false ;
            }
            // 0010 訂簽款轉存信託專戶 特別處理 E
            String    stringDeptCd                 =  "" ;
            String[][]  retWriteVouceherData  =  null ;
            boolean  boolean134O1           =  false ;
            for(int  intNo=0  ;  intNo<retDoc2M014.length  ;  intNo++) {
                stringRowType                 =  retDoc2M014[intNo][2].trim( ) ;
                stringDBCrCd                   =  retDoc2M014[intNo][9].trim( ) ;
                stringAccountNo               =  retDoc2M014[intNo][10].trim( ) ;
                stringDeptCd                    =  retDoc2M014[intNo][11].trim( ) ;
                stringObjectCd                 =  retDoc2M014[intNo][12].trim( ) ;
                stringLastYMD                  =  retDoc2M014[intNo][25].trim( ) ;
                retDoc2M014[intNo][13]    =  convert.FourToFive(retDoc2M014[intNo][13].trim( ),  0) ;
                doubleAMT                       =  exeUtil.doParseDouble(retDoc2M014[intNo][13].trim( )) ;
                stringRecordNo                =  (""  +  getValueAt("Table1",  intNo,  "RecordNo")).trim( ) ;
                intRecordNo                     =  exeUtil.doParseInteger(stringRecordNo) ;
                //arrayWriteVouceherData  =  convert.StringToken(stringLastYMD,  "-") ;
                retWriteVouceherData  =  getWriteVouceherData(stringLastYMD,  exeUtil) ;
                // 格式：傳票日期1-傳票流水號1-傳票序號1-傳票金額1%-%傳票日期2-傳票流水號2-傳票序號2-傳票金額2
                System.out.println("getWriteVouceherData--------------------------S") ;
                if("ERROR".equals(stringLastYMD)) {
                    exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
                    exeFun.doDeleteDoc2M014(stringBarCode) ;
                    exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                    put("Doc2M014_ERROR",  "FALSE") ;
                    return ;
                }
                if(retWriteVouceherData  !=  null) {
                    stringLastYMD                =  getToday("YYYY/mm/dd") ;
                    retDoc2M014[intNo][25]  =  stringLastYMD ;
                }
                System.out.println("getWriteVouceherData("+(retWriteVouceherData  !=  null)+")--------------------------E") ;
                //
                if(doubleAMT  <=  0  &&  !"1264".equals(stringAccountNo))  continue ;
                if("D".equals(stringRowType)) {
                    intRowTypeD++ ;
                    doubleAmtD  += doubleAMT ;
                }
                // Doc2M014
                stringSql  =  exeFun.getInsertDoc2M014Sql(stringVoucherFlowNo,  ""+intVoucherSeqNo,  stringKind,  retDoc2M014[intNo]) ;
                System.out.println(intNo+"(getInsertDoc2M014Sql)---------"+stringSql) ;
                vectorDocSql.add(stringSql) ;
                // 立沖
                //System.out.println(intNo+"(retDoc5M0224)---------"+retDoc5M0224.length) ;
                //System.out.println(intNo+"(stringAccountNo)---------"+stringAccountNo) ;
                //System.out.println(intNo+"(stringDBCrCd)---------"+stringDBCrCd) ;
                if(!booleanCostID0010  &&  retDoc5M0224.length>0  &&  "N".equals(stringRowType)) {
                    //  0  BarCode    1  VOUCHER_YMD   2  VOUCHER_FLOW_NO   3  VOUCHER_SEQ_NO    4  FactoryNo   5  Amt
                    String   stringVoucherYMDL        =  retDoc5M0224[intRecordNo-1][1].trim() ;
                    String   stringVoucherFlowNoL    =  retDoc5M0224[intRecordNo-1][2].trim() ;
                    String   stringVoucherSeqNoL     =  retDoc5M0224[intRecordNo-1][3].trim() ;
                    String   stringAmtL                       =  retDoc5M0224[intRecordNo-1][5].trim() ;
                    //
                    if(exeUtil.doParseDouble(stringAmtL)  <=  0) {
                        stringAmtL  =  ""+(exeUtil.doParseDouble(stringAmtL) * -1) ;
                    }
                    if(",J35648,".indexOf(stringBarCode)  ==  -1) {
                        // 新增 FED1014
                        stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDL,    stringVoucherFlowNoL,  stringVoucherSeqNoL,  
                                                          stringCompanyCd,       stringKind,                      stringVoucherYMDRoc,
                                                          stringVoucherFlowNo,  ""+intVoucherSeqNo,     stringAmtL,
                                                           " ",                              stringUserN) ;
                        vectorSql.add(stringSql) ;
                        System.out.println(intNo+"立沖  (FED1014)---------"+stringSql) ;
                        // 更新 FED1013
                        stringSql  =  exeFun.getUpdateFED1013Sql(stringAmtL,                    stringVoucherYMDL,        stringVoucherFlowNoL,  
                                                           stringVoucherSeqNoL,   stringCompanyCd,            stringKind) ;
                        System.out.println(intNo+"立沖  (FED1013)---------"+stringSql) ;
                        vectorSql.add(stringSql) ;
                    }
                    //System.out.println(intNo+"立沖  (getUpdateFED1013Sql)---------"+stringSql) ;
                }
                //  0  DB_CR_CD       1  ENTER_BOOK_CD      2  WRITE_OFF_CD            3  MATERIAL_CD     4  OBJECT_CD
                //  5  DEPT_CD          6  MONTEARY_CD        7  ACCT_CHINESE_NAME
                System.out.println("沖帳處理 getFED1003--------------------------------") ;
                retFED1003            =  exeFun.getFED1003(stringAccountNo,  booleanComNoType) ;
                stringDBCrCd1003  =  retFED1003[0][0].trim( ) ;
                if("Y".equals(retFED1003[0][2].trim( ))) {
                    if(stringDBCrCd.equals(stringDBCrCd1003)) {
                        if(",J35648,".indexOf(stringBarCode)  ==  -1) {
                            // FED1013 處理
                            stringSql  =  exeFun.getInsertFED1013Sql(stringVoucherYMDRoc,  stringVoucherFlowNo,  ""+intVoucherSeqNo,  
                                                              stringCompanyCd,          stringKind,                    stringAccountNo,
                                                              stringObjectCd,               " ",                                "0",
                                                               "0",                                "0",                              "0") ;
                            vectorSql.add(stringSql) ;
                            System.out.println(intNo+"(FED1013)---------"+stringSql) ;
                            if("W".equals(stringRowType)) {
                                System.out.println(intNo+"年(月)底預估 沖帳處理--------------------------------N1") ;
                                //vectorYOrN.add("N") ;
                                //vectorAMTYearEnd.add(convert.FourToFive(""+doubleAMT,  0)) ;
                                //vectorVoucherSeqNo.add(""+intVoucherSeqNo) ;
                            }
                        }
                    } else {
                        // FED1014 處理 (沖帳才須做此處理)
                        if("W".equals(stringRowType)) {
                            System.out.println(intNo+"stringDBCrCd("+stringDBCrCd+")--------------------------------") ;
                            // 沖借方 D
                            if("D".equals(stringDBCrCd)) {
                                stringVoucherSeqNo  =  ""+intVoucherSeqNo ;
                                vectorYOrN.add("Y") ;
                                vectorAMTYearEnd.add(convert.FourToFive(""+doubleAMT,  0)) ;
                                vectorVoucherSeqNo.add(""+intVoucherSeqNo) ;
                                System.out.println(intNo+"年(月)底預估 沖帳處理--------------------------------Y2") ;
                            } else {
                                System.out.println(intNo+"年(月)底預估 沖帳處理--------------------------------N2") ;
                            }
                        //} else if(arrayWriteVouceherData.length  >  1) {
                        } else if(retWriteVouceherData  !=  null) {
                            // 1對1 沖銷轉傳票(固資)
                            System.out.println("doWriteVoucher----------------------------------------------S") ;
                            if(!doWriteVoucher(intVoucherSeqNo,  stringVoucherYMDRoc,  stringVoucherFlowNo,  stringUserN,  retDoc2M014[intNo],  retWriteVouceherData,  exeFun,  exeUtil,  vectorSql)) {
                                    exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
                                    exeFun.doDeleteDoc2M014(stringBarCode) ;
                                    exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                    put("Doc2M014_ERROR",  "FALSE") ;
                                    return ;
                            } 
                            System.out.println("doWriteVoucher----------------------------------------------S") ;
                        } else {
                            if(retDoc5M02201.length  ==  0) {
                                if(booleanCostID0010) {
                                    boolean  booleanError  =  false ;
                                    System.out.println(intNo+"(DBCrCd："+stringDBCrCd+")對沖 0010 訂簽款轉存信託專戶 沖帳處理--------------------------------") ;
                                    // 0010 訂簽款轉存信託專戶 特別處理 S    
                                    arrayVouceherData[0]    =  ""+intNo ;
                                    arrayVouceherData[1]    =  ""+intDBCrCdC ;
                                    arrayVouceherData[2]    =  stringCompanyCd ;
                                    arrayVouceherData[3]    =  stringKind ;
                                    arrayVouceherData[4]    =  stringVoucherYMDRoc ;
                                    arrayVouceherData[5]    =  stringVoucherFlowNo ;
                                    arrayVouceherData[6]    =  ""+intVoucherSeqNo ;
                                    arrayVouceherData[7]    =  stringUserN ;
                                    arrayVouceherData[8]    =  stringAccountNo ;
                                    arrayVouceherData[9]    =  retDoc2M014[intNo][11].trim( ) ;
                                    arrayVouceherData[10]  =  retDoc2M014[intNo][12].trim( ) ;
                                    arrayVouceherData[11]  =  convert.FourToFive(""+doubleAMT,  0) ;
                                    if("D".equals(stringDBCrCd)) {
                                        // 依 公司+會計科目+部門+廠商 沖銷
                                        booleanError  =  doCostID0010TypeD(arrayVouceherData,  hashtableVoucherAmt,  exeFun,  exeUtil,  vectorSql) ;
                                    } else {
                                        // 依 retDoc5M0224 對應沖銷
                                        booleanError  =  doCostID0010TypeC(arrayVouceherData,  retDoc5M0224,  hashtableCanWriteAmt,  exeFun,  exeUtil,  vectorSql) ;
                                        intDBCrCdC++ ;
                                    }
                                    if(booleanError) {
                                        exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
                                        exeFun.doDeleteDoc2M014(stringBarCode) ;
                                        exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                        put("Doc2M014_ERROR",  "FALSE") ;
                                        return ;
                                    }
                                    // 0010 訂簽款轉存信託專戶 特別處理 E
                                } else if(",J35648,".indexOf(stringBarCode)  ==  -1) {
                                    System.out.println(intNo+"對沖 條碼編號 沖帳處理--------------------------------") ;
                                    // 更新 FED1013
                                    //  0  InOut          1  DepartNo       2  ProjectID      3  ProjectID1       4  CostID           5  CostID1      6  DiscountMoney 
                                    retDoc2M015  =  exeFun.getDoc2M015Union(booleanSource?"Doc2M015":"Doc5M025",  stringBarCode,  ""+intRowTypeD) ;
                                    if(retDoc2M015.length  >  0)  {
                                        retDoc2M012  =  exeFun.getDoc2M012Union(booleanSource?"Doc2M012":"Doc5M022",  stringBarCode,  retDoc2M015[0][0].trim(),  retDoc2M015[0][1].trim(),  retDoc2M015[0][3].trim(),  retDoc2M015[0][4].trim(),  retDoc2M015[0][5].trim()) ;
                                        stringSql          =  exeFun.getUpdateFED1013Sql(""+doubleAMT,                    stringVoucherYMDRoc,  stringVoucherFlowNo,  
                                                                                retDoc2M012[0][8].trim(),       stringCompanyCd,            stringKind) ;
                                        /*stringSql  =  exeFun.getInsertFED1013Sql(stringVoucherYMDRoc,  stringVoucherFlowNo,  "1",  
                                                                          stringCompanyCd,          stringKind,                    stringAccountNo,
                                                                          stringObjectCd,               " ",                                "0",
                                                                           ""+doubleAMT,                "0",                               "0") ;*/
                                        vectorSql.add(stringSql) ;
                                        System.out.println(intNo+"(FED1013)---------"+stringSql) ;
                                        // 新增 FED1014
                                        stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDRoc,        stringVoucherFlowNo,           retDoc2M012[0][8].trim(),  
                                                                          stringCompanyCd,               stringKind,                             stringVoucherYMDRoc,
                                                                          stringVoucherFlowNo,          ""+intVoucherSeqNo,            ""+doubleAMT,
                                                                           " ",                                      stringUserN) ;
                                        vectorSql.add(stringSql) ;
                                        System.out.println(intNo+"(FED1014)---------"+stringSql) ;
                                    }
                                }
                            } else {
                                if(!booleanSource) {
                                      System.out.println(intNo+"管理費用 退保留款 沖帳處理--------------------------------") ;
                                      // 管理費用 oce Doc5M02201    BarCode     BarCodeRef      BackRetainMoney
                                      String      stringBarCodeL                =  "" ;
                                      String      stringAcctNoL                   =  "" ;
                                      String      stringVoucherYMDL         =  "" ;
                                      String      stringVoucherFlowNoL     =  "" ;
                                      String      stringVoucherSeqNoL      =  "" ;
                                      String      stringCompanyCdL          =  "" ;
                                      String      stringKindL                       =  "" ;
                                      String      stringBackRetainMoneyL   =  "" ;
                                      String[][]  retDoc2M014L    =  null ;
                                      String[][]  retFED1012L      =  null ;
                                      if(intNo  <  retDoc5M02201.length) {
                                      //for(int  intNoL=0  ;  intNoL<retDoc5M02201.length  ;  intNoL++) {
                                          int  intNoL  =  intNo ;
                                          stringBarCodeL                =  retDoc5M02201[intNoL][1].trim() ;
                                          retDoc2M014L                  =  exeFun.getDoc2M014(stringBarCodeL,  "I") ;
                                          stringBackRetainMoneyL  =  retDoc5M02201[intNoL][2].trim() ;
                                          stringAcctNoL                   =  retDoc2M014L[0][0].trim() ;
                                          stringVoucherYMDL         =  retDoc2M014L[0][1].trim() ;
                                          stringVoucherFlowNoL     =  retDoc2M014L[0][2].trim() ;
                                          stringVoucherSeqNoL      =  retDoc2M014L[0][3].trim() ;
                                          stringCompanyCdL          =  retDoc2M014L[0][4].trim() ;
                                          stringKindL                       =  retDoc2M014L[0][5].trim() ;
                                          //
                                          stringVoucherYMDL      =  exeUtil.getDateConvertRoc(stringVoucherYMDL).replaceAll("/",  "") ;
                                          if(",J35648,".indexOf(stringBarCode)  ==  -1) {
                                              // 新增 FED1014
                                              stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDL,    stringVoucherFlowNoL,  stringVoucherSeqNoL,  
                                                                                stringCompanyCdL,     stringKindL,                    stringVoucherYMDRoc,
                                                                                stringVoucherFlowNo,  ""+intVoucherSeqNo,     stringBackRetainMoneyL,
                                                                                 " ",                               stringUserN) ;
                                              vectorSql.add(stringSql) ;
                                              System.out.println(intNo+"(FED1014)---------"+stringSql) ;
                                              // 更新 FED1013
                                              stringSql  =  exeFun.getUpdateFED1013Sql(stringBackRetainMoneyL,   stringVoucherYMDL,  stringVoucherFlowNoL,  
                                                                                 stringVoucherSeqNoL,         stringCompanyCdL,   stringKindL) ;
                                              vectorSql.add(stringSql) ;
                                              System.out.println(intNo+"(FED1013)---------"+stringSql) ;
                                          }
                                      }
                                } else {
                                    // 行銷
                                    // 退保留款沖銷
                                    System.out.println(intNo+"行銷 退保留款 沖帳處理--------------------------------") ;
                                    String[][]  retDoc2M014L  =  exeFun.getDoc2M014(stringRetainBarCode,  "I") ;
                                    String[][]  retFED1012L    =  null ;
                                    if(retDoc2M014L.length  ==  0) {
                                        // E-mail 通知
                                        stringMessage  =  "此次轉傳票條碼編號："  +  stringBarCode  +  "，退保留款條碼編號："  +  stringRetainBarCode +
                                                       " <br> 無法沖帳<br>查無對應之原保留款傳票資訊(Doc2M014)。" ;
                                        exeUtil.doEMail(stringSubject,  stringMessage,  stringSend,  arrayUser) ;
                                    }
                                    String      stringAcctNoL                =  retDoc2M014L[0][0].trim() ;
                                    String      stringVoucherYMDL      =  retDoc2M014L[0][1].trim() ;
                                    String      stringVoucherFlowNoL  =  retDoc2M014L[0][2].trim() ;
                                    String      stringVoucherSeqNoL   =  retDoc2M014L[0][3].trim() ;
                                    String      stringCompanyCdL       =  retDoc2M014L[0][4].trim() ;
                                    String      stringKindL                    =  retDoc2M014L[0][5].trim() ;
                                    //
                                    stringVoucherYMDL      =  convert.replace(stringVoucherYMDL,  "/",  "") ;
                                    stringVoucherYMDL      =  convert.ac2roc(stringVoucherYMDL) ;
                                    //
                                    retFED1012L                 =  exeFun.getFED1012(stringVoucherYMDL,  stringVoucherFlowNoL,  stringVoucherSeqNoL,  stringCompanyCdL,  stringKindL,  booleanComNoType) ;
                                    if(retFED1012L.length == 0  ||  !stringAcctNoL.equals(retFED1012L[0][1].trim())) {
                                        // E-mail 通知 傳票資料已變更，不作處理，請洽資訊室。
                                        stringMessage  =  "此次轉傳票條碼編號："  +  stringBarCode  +  "，退保留款條碼編號："  +  stringRetainBarCode +
                                                       " <br> 無法沖帳<br>查無對應之原保留款傳票資訊(FED1012)。" ;
                                        exeUtil.doEMail(stringSubject,  stringMessage,  stringSend,  arrayUser) ;
                                    }
                                    // 新增 FED1014
                                    stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDL,    stringVoucherFlowNoL,  stringVoucherSeqNoL,  
                                                                      stringCompanyCdL,     stringKindL,                    stringVoucherYMDRoc,
                                                                      stringVoucherFlowNo,  ""+intVoucherSeqNo,     ""+doubleAMT,
                                                                       " ",                              stringUserN) ;
                                    vectorSql.add(stringSql) ;
                                    System.out.println(intNo+"(FED1014)---------"+stringSql) ;
                                    // 更新 FED1013
                                    stringSql  =  exeFun.getUpdateFED1013Sql(""+doubleAMT,                 stringVoucherYMDL,  stringVoucherFlowNoL,  
                                                                       stringVoucherSeqNoL,       stringCompanyCdL,   stringKindL) ;
                                    vectorSql.add(stringSql) ;
                                    System.out.println(intNo+"(FED1013)---------"+stringSql) ;
                                    // 更新狀態
                                    String  stringWriteRetainMoney  =  "" ;
                                    Vector  vectorSqlL                      =  new  Vector() ;
                                    stringSql                          =  "SELECT  SUM(WriteRetainMoney)  FROM  Doc2M010  Where   RetainBarCode  = '"+stringRetainBarCode +"' " ;
                                    stringWriteRetainMoney  =  convert.FourToFive(""+exeUtil.doParseDouble(exeFun.getTableDataDoc(stringSql)[0][0]),  0) ;
                                    stringSql                           =  " UPDATE  Doc2M010  SET  WriteRetainMoney  =  "+stringWriteRetainMoney+" " +
                                                            " WHERE BarCode  =  '"+stringRetainBarCode+"' "  ;
                                    vectorSqlL.add(stringSql) ;
                                    exeFun.doExecVectorSqlForDoc(vectorSqlL) ;
                                }
                            }
                        }
                    }
                } else {
                    if("W".equals(stringRowType)) {
                        //vectorYOrN.add("N") ;
                        //vectorAMTYearEnd.add(convert.FourToFive(""+doubleAMT,  0)) ;
                        //vectorVoucherSeqNo.add(""+intVoucherSeqNo) ;
                        System.out.println(intNo+"年(月)底預估 沖帳處理--------------------------------N3") ;
                    }
                }
                // FED1012 處理
                stringSql  =  exeFun.getInsertFED1012Sql(stringVoucherYMDRoc,               stringVoucherFlowNo,                ""+intVoucherSeqNo,
                                                  retDoc2M014[intNo][7].trim( ),     stringKind,                                  retDoc2M014[intNo][9].trim( ),
                                                  retDoc2M014[intNo][10].trim( ),   stringDeptCd,                   retDoc2M014[intNo][12].trim( ),
                                                  retDoc2M014[intNo][13].trim( ),   "0",                                             retDoc2M014[intNo][15].trim( ),
                                                  retDoc2M014[intNo][17].trim( ),   retDoc2M014[intNo][18].trim( ),  retDoc2M014[intNo][19].trim( ),
                                                  retDoc2M014[intNo][20].trim( ),   retDoc2M014[intNo][21].trim( ),  retDoc2M014[intNo][22].trim( ), 
                                                  retDoc2M014[intNo][23].trim( ),    stringUserN) ;
                System.out.println(intNo+"(FED1012)-----------------"+stringSql) ;
                vectorSql.add(stringSql) ;
                if(!boolean134O1  &&  "134O1".equals(stringDeptCd)) {
                    boolean134O1  =  true ;
                    // 新增傳票 FED1158
                    Hashtable   hashtableData   =  new  Hashtable() ;
                    String          stringComNoL    =  retDoc2M014[0][7].trim( ) ;
                    hashtableData.put("VOUCHER_YMD",          stringVoucherYMDRoc) ;
                    hashtableData.put("VOUCHER_FLOW_NO",    stringVoucherFlowNo) ;
                    hashtableData.put("COMPANY_CD",           stringComNoL) ;
                    hashtableData.put("REMARK",                 "請購請款") ;
                    stringSql  =  exeFun.doInsertDBFED1("FED1158",  hashtableData,  false,  exeUtil) ;
                    vectorSql.add(stringSql) ;
                    // mail 寄送 Doc7M004
                    /*hashtableData.put("EDateTime",        datetime.getTime("YYYY/mm/dd h:m:s")) ;
                    hashtableData.put("MailKEY",          "FED1158通知"+stringComNoL+"-"+stringVoucherYMDRoc+"-"+stringVoucherFlowNo) ;
                    hashtableData.put("EmployeeNo",     getUser()) ;
                    hashtableData.put("MailTitle",            "FED1158通知") ;
                    hashtableData.put("MailContent",      "請款<br>"+stringBarCode+"<br>"+stringComNoL+"-"+stringVoucherYMDRoc+"-"+stringVoucherFlowNo) ;
                    hashtableData.put("MailUserID",       "B3018") ;
                    stringSql  =  exeFun.doInsertDBDoc("Doc7M004",  hashtableData,  false,  exeUtil) ;
                    vectorDocSql.add(stringSql) ;*/
                }
                // 折讓
                if("H".equals(stringRowType.substring(0,1))) {
                    doInsertDiscount(booleanSource,  retDoc2M014[intNo],  exeFun,  exeUtil) ;
                } if("D".equals(stringRowType.substring(0,1))  &&  (intNo+1==retDoc2M014.length  ||  (!"H".equals(retDoc2M014[intNo+1][2].trim( ))  &&  
                                                                                                                                                         !"D".equals(retDoc2M014[intNo+1][2].trim( )))) ) {
                    // 無折讓稅額之折讓單
                    doInsertDiscountNoTax(booleanSource,  convert.FourToFive(""+doubleAmtD,  0),  retDoc2M014[intNo],  exeFun,  exeUtil) ;
                }
                intVoucherSeqNo++ ;
            }
            System.out.println("---------- (傳票)年底預估S") ;
            //
            int           intVoucherSeqNoEndYear         =  0 ;
            String      stringID                                     =  exeFun.getIDForDoc2M010(booleanSource?"Doc2M010":"Doc5M020",  stringBarCode) ;
            String      stringVoucherDateEndYear      =  "" ;
            String      stringVoucherFlowNoEndYear  =  "" ;
            String      stringVoucherSeqNoEndYear    =  "" ;
            String      stringYOrN                                 =  "" ;
            String      stringAMTYearEnd                    =  "" ;
            String      stringTemp                                =  "" ;
            String      sringRemark                             =  "預估" ;
            String      stringCanWriteAmt                    =  "" ;
            String      stringBarCodeExcept                 =  exeFun.getDoc2M040( )[13] ;
            String[]    arrayTempL                              =  null ;
            String[][]  retDoc2M080                            =  null ;
            String[][]  retFED1012                              =  null ;
            String[][]  retFED1012L                           =  null ;
            String[][]  retFED1013                              =  null ;
            String[][]  retData                                      =  exeFun.getTableDataDoc("SELECT  ID_Def,  UseMoney "  +
                                                                                                                             " FROM  Doc2M0801 "  +
                                                                     " WHERE  ID_BarCode  =  "  +  stringID                  +  " "  +
                                                                           " AND  ComNo  =  '"         +  stringCompanyCd  +  "' "  +
                                                                " ORDER BY ID_Def  ") ;
            Vector     vectorVoucherData                   =  new  Vector() ;
            boolean  booleanYearFlag                       =  false ;
            boolean  booleanFlag                              =  false ;
            boolean  booleanExcept                =  false ;
            String     stringYearMessage                    =  "" ;
            //
            for(int  intData=0  ;  intData<retData.length  ;  intData++) {
                stringID                       =  retData[intData][0].trim() ;
                if(",2846,2847,2848,2849,2850,2851,2852,2853,2862,2863,2864,2865,2866,2867,2868,2869,2870,2871,2872,2895,".indexOf(stringID)!=-1) {
                    booleanExcept  =  true ;
                    break ;
                }
            }
            //
            if(stringBarCodeExcept.indexOf(stringBarCode)==-1) {
                //if(retData.length  >  0)  stringID  =  retData[0][0].trim() ;
                if(retData.length  ==  0  &&  !"".equals(stringID)) {
                    retData          =  new  String[1][1] ;
                    retData[0][0]  =  stringID ;
                    sringRemark  =  "請款" ;
                    booleanFlag  =  true ;
                }
                if(booleanSource  &&  retData.length  >  0) {
                    if(!booleanExcept) {
                        for(int  intData=0  ;  intData<retData.length  ;  intData++) {
                            stringID                       =  retData[intData][0].trim() ;
                            System.out.println(intData+"---------- getDoc2M080-------------------------------S") ;
                            retDoc2M080              =  exeFun.getDoc2M080_2(stringID,  sringRemark,  stringCompanyCd) ;  
                            System.out.println(intData+"---------- getDoc2M080---"+retDoc2M080.length+"---S") ;
                            // 2014-12-24 新增 Doc2M0803
                            vectorVoucherData  =  getWriteVourcherData(stringCompanyCd,  stringID,  retDoc2M080,  exeFun,  exeUtil,  vectorVoucherData) ;
                            //
                            if(vectorYOrN.size()  ==  0  &&  vectorVoucherData.size()  >  0) {
                                    put("Doc2M014_ERROR",  "FALSE") ;
                                    exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo) ;  //清空傳票號碼
                                    exeFun.doDeleteDoc2M014(stringBarCode) ;
                                    exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                    JOptionPane.showMessageDialog(null,  "此筆有年底預估，但未出現年底預估傳票，請洽請洽資訊室。",  "訊息",  JOptionPane.ERROR_MESSAGE) ;
                                    return ;
                            }
                            System.out.println("---------- getDoc2M080---"+retDoc2M080.length+"---E") ;
                        }
                        System.out.println("---------- getDoc2M080--E("+vectorYOrN.size()+")("+vectorVoucherData.size()+")") ;
                        for(int  intVector=0  ;  intVector<vectorYOrN.size()  ;  intVector++) {
                              if(intVector  >=  vectorVoucherData.size()) {
                                  put("Doc2M014_ERROR",  "FALSE") ;
                                  exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo) ;  //清空傳票號碼
                                  exeFun.doDeleteDoc2M014(stringBarCode) ;
                                  exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                  JOptionPane.showMessageDialog(null,  vectorVoucherData.size()+"程式發生錯誤，請洽資訊室。"+intVector,  "訊息",  JOptionPane.ERROR_MESSAGE) ;
                                  return ;
                              }
                              arrayTempL  =  convert.StringToken(""+vectorVoucherData.get(intVector),  "-") ;
                              if(arrayTempL.length  !=  4) {
                                  put("Doc2M014_ERROR",  "FALSE") ;
                                  exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo) ;  //清空傳票號碼
                                  exeFun.doDeleteDoc2M014(stringBarCode) ;
                                  exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                  JOptionPane.showMessageDialog(null,  "程式發生錯誤，請洽資訊室。",  "訊息",  JOptionPane.ERROR_MESSAGE) ;
                                  return ;
                              }
                              stringVoucherDateEndYear      =  arrayTempL[0].trim(); 
                              stringVoucherFlowNoEndYear  =  arrayTempL[1].trim(); 
                              //
                              intVoucherSeqNoEndYear        =  exeUtil.doParseInteger(arrayTempL[2].trim()); 
                              //
                              System.out.println("(["+intVector+"]--["+vectorYOrN.size()+"])-----------------intVoucherSeqNoEndYear ["+intVoucherSeqNoEndYear+"]") ;
                              //
                              stringYOrN                 =  (""+vectorYOrN.get(intVector)).trim() ;
                              stringVoucherSeqNo  =  (""+vectorVoucherSeqNo.get(intVector)).trim() ;
                              stringAMTYearEnd     =  (""+vectorAMTYearEnd.get(intVector)).trim() ;
                              //System.out.println(intVector+"---------- ("+exeUtil.doParseInteger(""+intVoucherSeqNoEndYear)+")") ;
                              if("N".equals(stringYOrN)) {
                                  intVoucherSeqNoEndYear-- ;
                                  continue; 
                              }
                              //
                              //stringVoucherDateEndYear      =  arrayTempL[0].trim() ;
                              //stringVoucherFlowNoEndYear  =  ""+exeUtil.doParseInteger(arrayTempL[1].trim()) ;
                              // 新增 FED1014
                              stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherDateEndYear,  stringVoucherFlowNoEndYear,  ""+intVoucherSeqNoEndYear,  
                                                                stringCompanyCd,                 stringKind,                                 stringVoucherYMDRoc,
                                                                stringVoucherFlowNo,            stringVoucherSeqNo,                stringAMTYearEnd,
                                                                 " ",                                        stringUserN) ;
                              vectorSql.add(stringSql) ;
                              System.out.println("新增 FED1014---------------------"+stringSql) ;
                              // 更新 FED1013
                              stringSql  =  exeFun.getUpdateFED1013Sql(stringAMTYearEnd,                          stringVoucherDateEndYear,  stringVoucherFlowNoEndYear,  
                                                                  ""+intVoucherSeqNoEndYear,          stringCompanyCd,                 stringKind) ;
                              vectorSql.add(stringSql) ;
                              System.out.println("更新 FED1013---------------------"+stringSql) ;
                              // 沖銷金額檢核
                              retFED1012L                 =  exeFun.getFED1012(stringVoucherDateEndYear,  stringVoucherFlowNoEndYear,  ""+intVoucherSeqNoEndYear,  stringCompanyCd,  stringKind) ;
                              if(retFED1012L.length  ==  0) {
                                  //stringYearMessage  +=  "此次轉傳票條碼編號("  +  stringBarCode  +  ")，找無欲 年底預估沖銷之傳票資料(FED1012)。\n" ;
                                  //booleanYearFlag        =  true ;
                                  continue ;
                              }
                              retFED1003            =  exeFun.getFED1003(retFED1012L[0][1].trim()) ;
                              if(!"Y".equals(retFED1003[0][2].trim( ))) {
                                  put("Doc2M014_ERROR",  "FALSE") ;
                                  exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo) ;  //清空傳票號碼
                                  exeFun.doDeleteDoc2M014(stringBarCode) ;
                                  exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                                  messagebox("[年底預估] 沖銷錯誤。1") ;
                                  return ;
                              }
                              System.out.println("----------------------------------S") ;
                              stringSql                        =  "(I.VOUCHER_YMD  =  "          +  stringVoucherDateEndYear      +  "  AND  "  +
                                                    " I.VOUCHER_FLOW_NO  =  "+  stringVoucherFlowNoEndYear  +  "  AND "  +
                                                    " I.VOUCHER_SEQ_NO  =  "   +  intVoucherSeqNoEndYear         +  " )" ;
                              stringSql                        =  " AND ("+  stringSql + ")" ;
                              retFED1013                  =  exeFun.getCanWriteAmtForFED1013(stringCompanyCd,   stringSql) ;
                              if(retFED1013.length  ==  0) {
                                  stringYearMessage  +=  "此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無對應欲年底預估沖銷之 傳票資訊(FED1013)。\n" ;
                                  booleanYearFlag        =  true ;
                              } else {
                                  stringCanWriteAmt       =   retFED1013[0][3].trim() ;
                                  if(exeUtil.doParseDouble(stringCanWriteAmt)  <  exeUtil.doParseDouble(stringAMTYearEnd)) {
                                      stringYearMessage  +=  "此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，欲年底預估沖銷之傳票可沖銷金額("+exeUtil.getFormatNum2 (stringCanWriteAmt)+")小於本次沖銷金額("+exeUtil.getFormatNum2 (stringAMTYearEnd)+")。\n" ;
                                      booleanYearFlag       =  true ;
                                  }
                              }
                              System.out.println("----------------------------------E") ;
                              //
                              intVoucherSeqNoEndYear-- ;
                        }
                        if(stringBarCodeExcept.indexOf(stringBarCode)==-1  &&  booleanYearFlag) {
                            exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
                            exeFun.doDeleteDoc2M014(stringBarCode) ;
                            exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                            put("Doc2M014_ERROR",  "FALSE") ;
                            messagebox("[年底預估] 沖銷錯誤。") ;
                            message("資訊\n"+stringYearMessage) ;
                            return  ;
                        }
                    }
                    // 更新 Doc2M080 狀態
                    if(exeFun.getTableDataDoc("SELECT  BarCode  FROM  Doc2M0801  WHERE  BarCode  =  '"  +  stringBarCode  +  "' ").length  ==  0) {
                          stringSql  =  "UPDATE  Doc2M080  SET  Status  =  'Y' "  +
                                     " WHERE  ID_Def  IN  (SELECT  ID  FROM  "+(booleanSource?"Doc2M010":"Doc5M020")+"  WHERE  BarCode  =  '"    +  stringBarCode    +  "') " +
                                      " AND  Remark  LIKE  '%請款%'" ;
                        // exeFun.doUpdateDoc2M080("",  "Y",  stringBarCode,  "",  "",  "",  false) ;
                        vectorDocSql.add(stringSql) ;
                    }
                    //System.out.println("Doc2M080---------------------"+stringSql) ;
                }
                System.out.println("---------- (傳票)年底預估E") ;
            }
            doSaveDoc2M0142 (vectorDocSql,  exeUtil,  exeFun) ;
            // 批次處理
            String  stringUserL  =  getUser() ;
            if("B3018".equals(stringUserL)  ||  stringUserL.startsWith("b")) {
                exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
                exeFun.doDeleteDoc2M014(stringBarCode) ;
                exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
                put("Doc2M014_ERROR",  "FALSE") ;
                return ;
            } 
            // 薪資-轉傳票處理 START
            doSalaryUpdate(stringCompanyCdO,  stringVoucherYMDRoc,  stringVoucherFlowNo,  stringUserN,  exeFun,  exeUtil,  vectorSql,  vectorDocSql) ;
            System.out.println(".................................................... END");
            // 薪資-轉傳票處理 END
            for (int i=0; i<vectorSql.size(); i++)
            {
              System.out.println(".................................................... vectorSql_" + i + "=" + vectorSql.get(i));
            }
            if(vectorDocSql.size( )  >  0) dbDoc.execFromPool((String[])    vectorDocSql.toArray(new  String[0])) ;
            System.out.println(".................................................... vectorSql.size()=" + vectorSql.size());
            if(vectorSql.size()  >  0){
                /*if(booleanComNoType) {
                    talk  dbFED1A                      =  getTalk(""  +get("put_FED1A"));
                    // 基金會
                    dbFED1.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
                    exeFun.doErrorEmail("基金會----批次轉傳票") ;
                } else {*/
                System.out.println("....................................................dbFED1");
                    dbFED1.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
                System.out.println("....................................................11111");
                //}
            }
            // 回押 租賃系統
            if(vectorRentSql.size()  >  0)  {
                //dbRent.execFromPool((String[])  vectorRentSql.toArray(new  String[0])) ;
            }
        } catch(Exception  e) {
            System.out.println("錯誤處理-------------------------------------------------S") ;
            put("Doc2M014_ERROR",  "FALSE") ;
            exeFun.getVoucherFlowNo("B",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;  //清空傳票號碼
            exeFun.doDeleteDoc2M014(stringBarCode) ;
            exeFun.doInsertDoc2M014Batch(exeFun.getTableDataFrom(booleanSource,  stringBarCode)) ;
            exeFun.doUpdateDoc2M080("",  "N",  stringBarCode,  "",  "",  "",  true) ;
            exeUtil.ClipCopy(e.toString()) ;
            System.out.println("錯誤處理-------------------------------------------------E") ;
            JOptionPane.showMessageDialog(null,  e.toString(),  "訊息",  JOptionPane.ERROR_MESSAGE) ;
            return  ;
        } 
        // 刪除傳票號碼
        System.out.println("....................................................22222");
        exeFun.getVoucherFlowNo("D",  "A",  stringCompanyCd+stringVoucherYMDRoc,  stringVoucherFlowNo,  booleanComNoType) ;
        System.out.println("....................................................33333");
        // 大包印花稅 回寫 成控 處理
        doBackDoc5M0229(stringCompanyCd,  stringVoucherYMDRoc,  stringVoucherFlowNo,  stringBarCode,  exeUtil,  exeFun) ;
        System.out.println("....................................................66666");
    }
    public  void  doBackDoc5M0229(String  stringCompanyCd,  String  stringVoucherYMDRoc,  String  stringVoucherFlowNo,  String  stringBarCode,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        talk          dbCostAsk             =  getTalk(""+get("put_Const_Ask")); 
        Hashtable  hashtableAnd             =  new  Hashtable() ;
        Hashtable  hashtableData              =  new  Hashtable() ;
        Hashtable  hashtableDoc5M0229     =  null ;
        Vector      vectorDoc5M0229         =  null ;
        int         intPos                  = 0 ;
        String      stringCasecode          =  "" ;
        String      stringPrdocodeA       =  "" ;
        String      stringTimeKind          =  "" ;
        String      stringCostType          =  "" ;
        String[][]      retProprContractField   =  {{"company_cd",             "voucher_ymd",            "voucher_flow_no",             "permit"},
                                                                         {"add1_company_cd",  "add1_voucher_ymd",  "add1_voucher_flow_no",   "permit1"},
                                         {"add2_company_cd",  "add2_voucher_ymd",  "add2_voucher_flow_no",   "permit2"},
                                         {"add3_company_cd",  "add3_voucher_ymd",  "add3_voucher_flow_no",   "permit3"},
                                         {"add4_company_cd",  "add4_voucher_ymd",  "add4_voucher_flow_no",    "permit4"}} ;
        //
        hashtableAnd.put("BarCode",  stringBarCode) ;
        vectorDoc5M0229  =  exeFun.getQueryDataHashtableDoc("Doc5M0229",  hashtableAnd,  "",  new  Vector(),  exeUtil) ;
        for(int  intNo=0  ;  intNo<vectorDoc5M0229.size()  ;  intNo++) {
            hashtableDoc5M0229  =  (Hashtable)  vectorDoc5M0229.get(intNo) ;              if(hashtableDoc5M0229  ==  null)  continue ;
            stringCasecode           =  ""+hashtableDoc5M0229.get("Casecode") ;
            stringPrdocodeA         =  ""+hashtableDoc5M0229.get("PrdocodeA") ;
            stringTimeKind            =  ""+hashtableDoc5M0229.get("TimeKind") ;
            stringCostType            =  ""+hashtableDoc5M0229.get("CostType") ;
            //
            if("原合約".equals(stringTimeKind)) {
                intPos  =  0 ;
            } else if("追一".equals(stringTimeKind)) {
                intPos  =  1 ;
            } else if("追二".equals(stringTimeKind)) {
                intPos  =  2 ;
            } else if("追三".equals(stringTimeKind)) {
                intPos  =  3 ;
            } else if("追四".equals(stringTimeKind)) {
                intPos  =  4 ;
            }
            //
             hashtableAnd.put("casecode",                     stringCasecode) ;
             hashtableAnd.put("prdocode_a",                 stringPrdocodeA) ;
             hashtableAnd.put("type",                               stringCostType) ;
             hashtableAnd.put(retProprContractField[intPos][3],   "1") ;
            hashtableData.put(retProprContractField[intPos][0],   stringCompanyCd) ;
            hashtableData.put(retProprContractField[intPos][1],   stringVoucherYMDRoc.replaceAll("/", "")) ;
            hashtableData.put(retProprContractField[intPos][2],   stringVoucherFlowNo) ;
            exeUtil.doUpdateDB("propr_contract",  "",  hashtableData,  hashtableAnd,  true,  dbCostAsk) ;
        }
    }
    public  String[][]  getWriteVouceherData(String  stringWriteVoucher,  FargloryUtil  exeUtil) throws  Throwable {
        System.out.println("stringWriteVoucher("+stringWriteVoucher+")--------------------------") ;
        String[]  arrayWriteVouceherData  =  convert.StringToken(stringWriteVoucher,  "%-%") ;
        String[]  arrayTemp                        =  null ;
        Vector   vectorVoucherData           =  new  Vector() ;
        for(int  intNo=0  ;  intNo<arrayWriteVouceherData.length  ;  intNo++) {
            stringWriteVoucher  =  arrayWriteVouceherData[intNo].trim() ;
            arrayTemp               =  convert.StringToken(stringWriteVoucher,  "-") ;
            //
            System.out.println(intNo+"stringWriteVoucher("+stringWriteVoucher+")("+arrayTemp.length+")--------------------------") ;
            if(arrayTemp.length  <=  1) {
                  return  null ;
            }
            vectorVoucherData.add(arrayTemp) ;
        }
        return  (String[][])  vectorVoucherData.toArray(new  String[0][0]) ;
    }
    public  Vector  getWriteVourcherData(String  stringCompanyCd,  String  stringID,  String[][]  retDoc2M080,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorVoucherData)throws  Throwable {
        if(retDoc2M080.length  ==  0)  return  new  Vector(); 
        //
        String[]       arrayVoucherData                =  getVoucherData(retDoc2M080) ; 
        Vector        vectorDoc2M0803       =  new  Vector() ;
        Hashtable  hashtableAnd             =  new  Hashtable() ;
        // 查詢 Doc2M0803
         hashtableAnd.put("COMPANY_CD",          stringCompanyCd) ;
         hashtableAnd.put("VOUCHER_YMD",         exeUtil.getDateConvert(arrayVoucherData[0])) ;
         hashtableAnd.put("VOUCHER_FLOW_NO",  ""+exeUtil.doParseInteger(arrayVoucherData[1])) ;
         hashtableAnd.put("KIND",                    "0") ;
         hashtableAnd.put("ID_Def",                    stringID) ;
         vectorDoc2M0803  =  exeFun.getQueryDataHashtableDoc("Doc2M0803",  hashtableAnd,  " ORDER BY   VOUCHER_SEQ_NO ",  new  Vector(),  exeUtil)  ;
         //
         if(vectorDoc2M0803.size()  >  0) {
            vectorVoucherData  =  getWriteVourcherDataNew(vectorDoc2M0803,  exeFun,  exeUtil,  vectorVoucherData) ;
         } else {
            vectorVoucherData  =  getWriteVourcherDataOld(retDoc2M080,  exeFun,  exeUtil,  vectorVoucherData) ;
         }
         return  vectorVoucherData ;
    }
    public  String[]  getVoucherData(String[][]  retDoc2M080)throws  Throwable {
        String    stringVoucherFlowNoEndYear    =  "" ;
        String[]    arrayTempL                  =  null ;
        //
        for(int  intNo=0  ;  intNo<retDoc2M080.length  ;  intNo++) {
            stringVoucherFlowNoEndYear  =  retDoc2M080[intNo][23].trim() ;
            arrayTempL                               =  convert.StringToken(stringVoucherFlowNoEndYear,  "-") ;
            if(arrayTempL.length  >=  3) {
                return  arrayTempL ;
            }
        }
        return  null ;
    }
    public  Vector  getWriteVourcherDataNew(Vector  vectorDoc2M0803,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorVoucherData)throws  Throwable {
        int         intVoucherSeqNo           =  0 ;
        String      stringVoucherDate               =  "" ;
        String      stringVoucherFlowNo           =  "" ;
        String      stringDbCrCd                  =  "" ;
        String      stringTemp                  =  "" ;
        String      stringComNo             =  "" ;
        String      stringUseStatus           =  "" ;
        String       stringAccountNo                     =  "" ;
        String[][]    retFED1003                        =  null ;
        Hashtable  hashtableDoc2M0803         =  new  Hashtable() ;
        Hashtable  hashtableAnd                 =  new  Hashtable() ;
        for(int  intNo=vectorDoc2M0803.size()-1  ;  intNo>=0  ;  intNo--) {
        //for(int  intNo=0  ;  intNo<vectorDoc2M0803.size()  ;  intNo++) {
            hashtableDoc2M0803  =  (Hashtable)  vectorDoc2M0803.get(intNo) ;  if(hashtableDoc2M0803  ==  null)  continue ;
            stringVoucherDate       =  exeUtil.getDateConvertRoc(""+hashtableDoc2M0803.get("VOUCHER_YMD")).replaceAll("/",  "") ;
            stringVoucherFlowNo   =  ""+hashtableDoc2M0803.get("VOUCHER_FLOW_NO") ;
            stringComNo           =  ""+hashtableDoc2M0803.get("COMPANY_CD") ;
            stringUseStatus       =  ""+hashtableDoc2M0803.get("UseStatus") ;
            intVoucherSeqNo         =  exeUtil.doParseInteger(""+hashtableDoc2M0803.get("VOUCHER_SEQ_NO")) ;
            //
            System.out.println(intNo+"getWriteVourcherDataNew--------------------------------------------------------") ;
            if("N".equals(stringUseStatus))  continue ;
            //
            hashtableAnd.put("VOUCHER_YMD",         stringVoucherDate) ;
            hashtableAnd.put("VOUCHER_FLOW_NO",  stringVoucherFlowNo) ;
            hashtableAnd.put("VOUCHER_SEQ_NO",    ""+intVoucherSeqNo) ;
            hashtableAnd.put("COMPANY_CD",        stringComNo) ;
            hashtableAnd.put("KIND",                  "0") ;
            stringDbCrCd               =  exeFun.getNameUnionFED1("DB_CR_CD",  "FED1012",  "",  hashtableAnd,  exeUtil) ;
            //找貸方
            if(!"C".equals(stringDbCrCd))  continue ;
            System.out.println(intNo+"找貸方--------------------------------------------------------OK") ;
            //
            hashtableAnd.put("VOUCHER_YMD",         stringVoucherDate) ;
            hashtableAnd.put("VOUCHER_FLOW_NO",  stringVoucherFlowNo) ;
            hashtableAnd.put("VOUCHER_SEQ_NO",    ""+intVoucherSeqNo) ;
            hashtableAnd.put("COMPANY_CD",        stringComNo) ;
            hashtableAnd.put("KIND",                  "0") ;
            stringAccountNo     =  exeFun.getNameUnionFED1("ACCT_NO",  "FED1012",  "",  hashtableAnd,  exeUtil) ;
            retFED1003            =  exeFun.getFED1003(stringAccountNo,  false) ;
            System.out.println("DB_CR_CD("+retFED1003[0][0].trim( )+")("+(stringDbCrCd)+")--------------------------------------------------------") ;
            if(!"Y".equals(retFED1003[0][2].trim( ))) continue ;
            System.out.println(intNo+"FED1003--------------------------------------------------------OK") ;
            //
            stringTemp                                =  stringVoucherDate+"-"+stringVoucherFlowNo+"-"+intVoucherSeqNo+"-0" ;
            vectorVoucherData.add(stringTemp) ;
        }
        return  vectorVoucherData ;
    }
    public  Vector  getWriteVourcherDataOld(String[][]  retDoc2M080,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorVoucherData)throws  Throwable {
        int       intVoucherSeqNoEndYear        =  0 ;
        String    stringVoucherDateEndYear      =  "" ;
        String    stringVoucherFlowNoEndYear    =  "" ;
        String    stringTemp                    =  "" ;
        String[]    arrayTempL                  =  null ;
        for(int  intNo=0  ;  intNo<retDoc2M080.length  ;  intNo++) {
            stringVoucherFlowNoEndYear  =  retDoc2M080[intNo][23].trim() ;
            arrayTempL                               =  convert.StringToken(stringVoucherFlowNoEndYear,  "-") ;
            if(arrayTempL.length  >=  3) {
                stringVoucherDateEndYear      =  arrayTempL[0].trim() ;
                stringVoucherFlowNoEndYear  =  ""+exeUtil.doParseInteger(arrayTempL[1].trim()) ;
                if(arrayTempL.length  ==  4) {
                    intVoucherSeqNoEndYear        =  exeUtil.doParseInteger(arrayTempL[3].trim())+1 ;
                    stringTemp                                =  stringVoucherDateEndYear+"-"+stringVoucherFlowNoEndYear+"-"+intVoucherSeqNoEndYear+"-0" ;
                    vectorVoucherData.add(stringTemp) ;
                }
                //break ;
            }
        }
        return  vectorVoucherData ;
    }
    public  void  doSalaryUpdate(String  stringCompanyCdO,  String  stringVoucherYMDRoc,  String  stringVoucherFlowNo,  String  stringUserN,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorSql,  Vector  vectorDocSql)throws  Throwable {
        String        stringVoucherType    =  getValue("VoucherType").trim( ) ;if(",B,C,".indexOf(stringVoucherType)  ==  -1) return ;
        String        stringVoucherYMD     =  exeUtil.getDateConvert(stringVoucherYMDRoc) ;
        String        stringBarCode           =  getValue("BarCode").trim( ) ;
        String        stringSql                    =  "" ;
        String        stringTempKey          =  "" ;
        Hashtable  hashtableAnd            =  new  Hashtable() ;
        Hashtable  hashtableData           =  new  Hashtable() ;
        String        stringSqlAnd               =  "" ;
        String        stringToday                =  getToday("yymmdd") ;
        boolean     booleanDB                =  false ;
        //
        if(stringToday.length()  >  6)    stringToday =  stringToday.substring(stringToday.length()-6,  stringToday.length()) ;
        if(stringUserN.length( )  >  1)    stringUserN  =  stringUserN.substring(1,  stringUserN.length( )) ;
        // 查詢 Doc2M0143
        if("B".equals(stringVoucherType)) {
            stringSqlAnd  =  " AND  TEMP_KEY  LIKE  '%1' " ;
        } else {
            stringSqlAnd  =  " AND  TEMP_KEY  LIKE  '%2' " ;
        }
        hashtableAnd.put("BarCode",                         stringBarCode) ;
        stringTempKey  =  exeFun.getNameUnionDoc("TEMP_KEY",  "Doc2M0143",  stringSqlAnd,  hashtableAnd,  exeUtil) ;
        // 更新 
        hashtableAnd.put("BarCode",                         stringBarCode) ;
        hashtableAnd.put("TEMP_KEY",                     stringTempKey) ;
        hashtableData.put("VOUCHER_YMD",            stringVoucherYMD) ;
        hashtableData.put("VOUCHER_FLOW_NO",  stringVoucherFlowNo) ;
        hashtableData.put("STATUS_CD",                  "Z") ;
        stringSql  =  exeFun.doUpdateDBDoc("Doc2M0143",  stringSqlAnd,  hashtableData,  hashtableAnd,  booleanDB,  exeUtil) ;
        vectorDocSql.add(stringSql) ;
        // 更新 FED1040 
        hashtableAnd.put("SYSTEM_CD",                         "3") ;
        hashtableAnd.put("TEMP_KEY",                            stringTempKey) ;
        hashtableAnd.put("COMPANY_CD",                      stringCompanyCdO) ;
        
        hashtableData.put("VOUCHER_YMD",            stringVoucherYMDRoc) ;
        hashtableData.put("VOUCHER_FLOW_NO",  stringVoucherFlowNo) ;
        hashtableData.put("STATUS_CD",                  "Z") ;
        hashtableData.put("LAST_USER",                   stringUserN) ;
        hashtableData.put("LAST_YMD",                     stringToday) ;
        stringSql  =  exeFun.doUpdateDBFED1("FED1040",  stringSqlAnd,  hashtableData,  hashtableAnd,  booleanDB,  exeUtil) ;
        vectorSql.add(stringSql) ;
    }
    public  boolean  doCostID0010TypeD(String[]  arrayVouceherData,  Hashtable  hashtableVoucherAmt,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorSql)throws  Throwable {
        int            intRowNo                      =  exeUtil.doParseInteger(arrayVouceherData[0].trim()) ;
        int            intDBCrCdC                  =  exeUtil.doParseInteger(arrayVouceherData[1].trim()) ;
        String     stringComNo                  =  arrayVouceherData[2].trim() ;
        String     stringKind                      =  arrayVouceherData[3].trim() ;
        String     stringVoucherYMDRoc  =  arrayVouceherData[4].trim() ;
        String     stringVoucherFlowNo    =  arrayVouceherData[5].trim() ;
        String     stringVoucherSeqNo     =  arrayVouceherData[6].trim() ;
        String     stringUserN                   =  arrayVouceherData[7].trim() ;
        String     stringAcctNo                  =  arrayVouceherData[8].trim() ;
        String     stringDeptCd                =  arrayVouceherData[9].trim() ;
        String     stringObjectCd              =  arrayVouceherData[10].trim() ;
        String     stringAmt                      =  arrayVouceherData[11].trim() ;
        String     stringAmtL                    =  "" ;
        String     stringVoucher                =  "" ;
        String     stringVoucherYMDL      =  "" ;
        String     stringVoucherFlowNoL  =  "" ;
        String     stringVoucherSeqNoL   =  "" ;
        String     stringSql                        =  "" ;
        String     stringTemp                    =  "" ;
        double   doubleAmt                     =  exeUtil.doParseDouble(stringAmt) ;
        double   doubleAmtL                   =   0 ;
        double   doubleTotalMoney        =  0 ;
        // 依 公司+會計科目+部門+廠商 沖銷
        String     stringSqlAnd                  =  " AND  J.ACCT_NO  =  '"       +  stringAcctNo       +  "' "  +
                                                   " AND  J.DEPT_CD  =  '"        +  stringDeptCd      +  "' "  +
                                                   " AND  J.OBJECT_CD  =  '"   +  stringObjectCd   +  "' "+
                                " ORDER BY  I.VOUCHER_YMD,  I.VOUCHER_FLOW_NO,  I.VOUCHER_SEQ_NO ";
        String[][]     retFED1013     =  exeFun.getCanWriteAmtForFED1013(stringComNo,   stringSqlAnd,  false) ;
        for(int  intNo=0  ;  intNo<retFED1013.length  ;  intNo++) {
            stringVoucherYMDL        =  retFED1013[intNo][0].trim() ;
            stringVoucherFlowNoL    =  retFED1013[intNo][1].trim() ;
            stringVoucherSeqNoL     =  retFED1013[intNo][2].trim() ;
            stringAmtL                       =  retFED1013[intNo][3].trim() ;
            //
            doubleAmtL                  =  exeUtil.doParseDouble(stringAmtL) ;  
            stringVoucher               =  stringVoucherYMDL  +  "-"  +  convert.add0(stringVoucherFlowNoL,  "5")  +  "-"  +  convert.add0(stringVoucherSeqNoL,  "4") ;
            stringTemp                   =  ""+hashtableVoucherAmt.get(stringVoucher) ;
            System.out.println(intNo+"["+stringVoucher+"]-----------------------------------("+doubleAmtL+")原") ;
            if(!"null".equals(stringTemp)) {
                doubleAmtL                  =  exeUtil.doParseDouble(stringTemp) ;  
            }
            System.out.println(intNo+"["+stringVoucher+"]-----------------------------------("+doubleAmtL+")後") ;
            if(doubleAmtL  <=  0)  continue ;
            //
            doubleTotalMoney  +=  doubleAmtL ;
            if(doubleAmtL  >  doubleAmt) {
                stringAmtL  =  convert.FourToFive(""+doubleAmt,  0) ;
                hashtableVoucherAmt.put(stringVoucher,  ""+(doubleAmtL-doubleAmt)) ;
            } else {
                stringAmtL  =  convert.FourToFive(""+doubleAmtL,  0) ;
                hashtableVoucherAmt.put(stringVoucher,  "0") ;
            }
            // 新增 FED1014
            stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDL,    stringVoucherFlowNoL,  stringVoucherSeqNoL,  
                                              stringComNo,               stringKind,                      stringVoucherYMDRoc,
                                              stringVoucherFlowNo,  stringVoucherSeqNo,     stringAmtL,
                                               " ",                              stringUserN) ;
            vectorSql.add(stringSql) ;
            System.out.println(intRowNo+"立沖  (getInsertDoc2M014Sql)("+stringAmtL+")---------"+stringSql) ;
            // 更新 FED1013
            stringSql  =  exeFun.getUpdateFED1013Sql(stringAmtL,                    stringVoucherYMDL,        stringVoucherFlowNoL,  
                                               stringVoucherSeqNoL,   stringComNo,                    stringKind) ;
            System.out.println(intRowNo+"立沖  (getInsertDoc2M014Sql)("+stringAmtL+")---------"+stringSql) ;
            vectorSql.add(stringSql) ;
            //
            doubleAmt  -=  doubleAmtL ;
            if(doubleAmt  <=  0) {
                break ;
            }
        }
        if(doubleAmt  >  0) {
            messagebox("第 "  +  (intRowNo+1)  +  " 列之 [請款金額]合計("+exeUtil.getFormatNum2 (stringAmt)+") 大於 [可沖銷金額]("+exeUtil.getFormatNum2 (""+doubleTotalMoney)+")。") ;
            return  true ;
        }
        return  false ;
    }
    public  boolean  doCostID0010TypeC(String[]  arrayVouceherData,  String[][]  retDoc5M0224,  Hashtable  hashtableCanWriteAmt,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorSql)throws  Throwable {
        int            intRowNo                       =  exeUtil.doParseInteger(arrayVouceherData[0].trim()) ;
        int            intDBCrCdC                  =  exeUtil.doParseInteger(arrayVouceherData[1].trim()) ;
        String     stringComNo                  =  arrayVouceherData[2].trim() ;
        String     stringKind                      =  arrayVouceherData[3].trim() ;
        String     stringVoucherYMDRoc  =  arrayVouceherData[4].trim() ;
        String     stringVoucherFlowNo    =  arrayVouceherData[5].trim() ;
        String     stringVoucherSeqNo     =  arrayVouceherData[6].trim() ;
        String     stringUserN                   =  arrayVouceherData[7].trim() ;
        String     stringSql                        =  "" ;
        String     stringVoucher                =  "" ;
        double   doubleMoney                 =  0 ;
        double   doubleTotalMoney         =  0 ;
        //
        if(hashtableCanWriteAmt  ==  null) {
            hashtableCanWriteAmt  =  getCanWriteAmtForFED1013(stringComNo,  retDoc5M0224,  false,  exeFun) ;
        }
        // 依 retDoc5M0224 對應沖銷
        String   stringVoucherYMDL        =  retDoc5M0224[intDBCrCdC][1].trim() ;
        String   stringVoucherFlowNoL    =  retDoc5M0224[intDBCrCdC][2].trim() ;
        String   stringVoucherSeqNoL     =  retDoc5M0224[intDBCrCdC][3].trim() ;
        String   stringAmtL                       =  retDoc5M0224[intDBCrCdC][5].trim() ;
        //
        stringVoucher               =  stringVoucherYMDL  +  "-"  +  convert.add0(stringVoucherFlowNoL,  "5")  +  "-"  +  convert.add0(stringVoucherSeqNoL,  "4") ;
        System.out.println("傳票("+stringVoucher+")-----------------------") ;
        doubleMoney                =  exeUtil.doParseDouble(stringAmtL) ;
        doubleTotalMoney        =  exeUtil.doParseDouble(""+hashtableCanWriteAmt.get(stringVoucher)) ;
        if(doubleTotalMoney  <  doubleMoney) {
            messagebox("第 "  +  (intRowNo+1)  +  " 列之 [請款金額]合計("+exeUtil.getFormatNum2 (""+doubleMoney)+") 大於 [可沖銷金額]("+exeUtil.getFormatNum2 (""+doubleTotalMoney)+")。") ;
            return  true ;
        }
        // 新增 FED1014
        stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYMDL,    stringVoucherFlowNoL,  stringVoucherSeqNoL,  
                                          stringComNo,               stringKind,                      stringVoucherYMDRoc,
                                          stringVoucherFlowNo,  stringVoucherSeqNo,     stringAmtL,
                                           " ",                              stringUserN) ;
        vectorSql.add(stringSql) ;
        System.out.println(intRowNo+"立沖  (getInsertDoc2M014Sql)---------"+stringSql) ;
        // 更新 FED1013
        stringSql  =  exeFun.getUpdateFED1013Sql(stringAmtL,                    stringVoucherYMDL,        stringVoucherFlowNoL,  
                                           stringVoucherSeqNoL,   stringComNo,                    stringKind) ;
        System.out.println(intRowNo+"立沖  (getInsertDoc2M014Sql)---------"+stringSql) ;
        vectorSql.add(stringSql) ;
        return  false ;
    }
      // 檢核
      // 前端資料檢核，正確回傳 True
      public  boolean  isBatchCheckOK(boolean  booleanSource272,  String[][]  retDoc5M0224,  String[][]  retDoc2M010,  boolean  booleanSource,  boolean  booleanComNoType,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil,  Hashtable  hashtableAsset,  Vector  vectorRentSql)throws  Throwable {
        //
        int                intPositionC                            =  0 ;
        String          stringAcctNo                           =  "" ;
        String          stringAcctNoCf                        =  "" ;
        String          stringVoucherYMDFinal          =  "" ;
        String          stringVoucherYMD                 =  "" ;
        String          stringExpiredDate                   =  "" ;
        String          stringItemCd                           =  "" ;
        String          stringType                              =  "" ;
        String          stringUseCd                           =  "" ;
        String          stringExchang                        =  "" ;
        String          stringMonteary                       =  "" ;
        String          stringDbCrCd                         =  "" ;
        String          stringAmt                                =  "" ;
        String          stringCompanyCD                  =  "" ;
        String          stringInvoiceDate                    =  "" ;
        String          stringBarCode                         =  getValue("BarCode").trim( ) ;
        String          stringStatusCd                        =  getValue("STATUS_CD").trim( ) ;
        String          stringToday                             =  "" ;
        String          stringPayCondition1                =  getValue("PayCondition1").trim( )  ;
        String          stringPayCondition2                =  getValue("PayCondition2").trim( )  ;
        String          stringInvoiceKind                     =  "" ;
        String          stringCostIDPosition                =  "" ;
        String          stringTableType                      =  getValue("TableType").trim( ) ;  //  R 扣繳    I 發票
        String          stringInvoiceNo                       =  "" ;
        String          stringRecordNo                       =  "" ;
        String          stringDate                               =  "" ;
        String          stringDate2146                       =  "" ;
        String          stringObjectCd                        =  "" ;
        String          stringDeptCd                           =  "" ;
        String          stringTemp                              =  "" ;
        String         stringRetainBarCode               =  retDoc2M010[0][26].trim() ;
        String[][]      retFED1002                            =  null ;
        String[][]      retFED1004                            =  null ;
        String[]        retDoc2M040                          =  exeFun.getDoc2M040( ) ;
        Vector         vectorItemCd                           =  new  Vector( ) ;
        Vector         vectorAcctNo                           =  new  Vector( ) ;
        Vector         vectorDescription                    =  new  Vector( ) ;
        Vector         vectorInvoiceNo                       =  new  Vector( ) ;
        Vector         vectorDeptCd                          =  new  Vector( ) ;
        JTable         jtable1                                     =  getTable("Table1") ;
        double        doubleAmtD                            =  0 ;
        double        doubleAmtC                            =  0 ;
        double        doubleInvoiceTax                    =  0 ;
        double       doubleInvoiceMoney                 =  0 ;
        double       doubleInvoiecMoneyTax           =  0 ;
        double        doubleTaxRate                         =  exeUtil.doParseDouble(retDoc2M040[4].trim( )) ;
        double        doubleInvoiceTotalMoney        =  0 ;
        double       doubleSpecialDiscountMoney  =  0 ;
        Hashtable  hashtableDoc2M042                 =  exeFun.getDoc2M042( ) ;
        boolean      booleanCostID                         =  true ;
        boolean      booleanRowTypeA                   =  false ;
        boolean      booleanRowTypeW                  =  false ;
        boolean      boolean2146DateCheck          =  false ;
        boolean      booleanA110202                     =  false ;
        boolean      booleanA110201                     =  false ; // 是否檢核金額一致
        boolean      booleanTable14                     =  false ; // 代收代付 發票檢核(允許不同公司相同發票號碼)
        boolean      booleanF297001                      =  false ; // 撥補零用金
        Vector         vectorAcctNoBandNo               =  new  Vector() ;
        //
        boolean     booleanCostID0010       =  false ;
        if(!booleanSource) {
            String      stringCostIDL           =  "" ;
            String[][]  retDoc2M012L         =  exeFun.getDoc2M012Union("Doc5M022",  stringBarCode) ;
            Vector     vectorCostIDExcept  =  exeFun.getDoc2M0401V("",  "B",  "") ;
                             booleanTable14      =  exeFun.getDoc5M0225(stringBarCode).length>0 ;
            for(int  intNo=0  ;  intNo<retDoc2M012L.length  ;  intNo++) {
                 stringCostIDL   =  retDoc2M012L[intNo][4].trim();
                 if("A110202,A110302,A110303,A110401,A110901,A110902,A110903,A110911,A110940,A110941,A110942,".indexOf(stringCostIDL) !=  -1)      {  booleanA110202  =  true ;   }
                 if("A110940,A110941,A110942,A140802,A140803,A140804".indexOf(stringCostIDL) !=  -1)                                                                                   {  booleanTable14    =  true ;   }
                 if(vectorCostIDExcept.indexOf(stringCostIDL) !=  -1)                                                                                                                                                   {  booleanA110201  =  true ;   }
                 if("F297001,".indexOf(stringCostIDL+",") !=  -1)                                                                                                                                                        {  booleanF297001  =  true ;   }
                 if(booleanA110202  &&  booleanA110201  &&  booleanTable14)  break ;
            }
        } else {
            String      stringCostIDL           =  "" ;
            String[][]  retDoc2M012L         =  exeFun.getDoc2M012Union("Doc2M012",  stringBarCode) ;
            for(int  intNo=0  ;  intNo<retDoc2M012L.length  ;  intNo++) {
                if(",0010,".indexOf(","+retDoc2M012L[intNo][4].trim()+retDoc2M012L[intNo][5].trim()+",")  ==  -1) {
                    booleanCostID0010  =  false ;
                    break ;
                } else {
                    booleanCostID0010  =  true ;
                }
            }
            if(retDoc5M0224.length  <  0)  booleanCostID0010  =  false ;
        }
        //
        vectorAcctNoBandNo.add("1103") ;
        vectorAcctNoBandNo.add("110301") ;
        vectorAcctNoBandNo.add("110302") ;
        vectorAcctNoBandNo.add("1107") ;
        //
        stringAcctNoCf  =  retDoc2M040[1].trim( ) ;
        // 已轉傳票
        if("Z".equals(stringStatusCd)  ||  "E".equals(stringStatusCd)) {
            message("已轉傳票。") ;
            return  false ;
        }
        String               stringRowType           =  (""+getValueAt("Table1",  0,  "RowType")).trim() ;
        String               stringVoucherType    =  getValue("VoucherType").trim( ) ;
        Hashtable         hashtableAnd           =  new  Hashtable() ;
        Hashtable         hashtableTemp        =  new  Hashtable() ;
        Vector               vectorDoc2M0143   =  new  Vector() ;
        boolean           booleanRowTypeR    =  "R".equals(stringRowType) ;
        hashtableAnd.put("BarCode",  stringBarCode) ;
        vectorDoc2M0143  =  exeFun.getQueryDataHashtableDoc("Doc2M0143",  hashtableAnd,  " ORDER BY  TEMP_KEY ",  new  Vector(),  exeUtil) ;
        if(booleanRowTypeR) {
            // 薪資轉傳票
            if(vectorDoc2M0143.size()  !=  2) {  messagebox("資料錯誤，請洽資訊室。1") ;  return  false ; }
            // 一般 A
            if("A".equals(stringVoucherType)) {  messagebox("狀態錯誤，請洽資訊室。") ;  return  false ; }
            //
            String     stringTempKeyL            =  "" ;
            String     stringSystemCdL           =  "" ;
            String     stringVoucherYMDL      =  "" ;
            String     stringVoucherFlowNoL  =  "" ;
            String     stringCompanyCdL       =  "" ;
            String     stringStatusCdL             =  "" ;
            String     stringNowStatusL            =  "" ;
            Vector    vectorFED1040              =  new  Vector() ;
            Vector    vectorFED1012              =  new  Vector() ;
            for(int  intNo=0  ;  intNo<vectorDoc2M0143.size()  ;  intNo++) {
                  hashtableTemp              =  (Hashtable)  vectorDoc2M0143.get(intNo) ;  if(hashtableTemp  ==  null)  continue ;
                  stringSystemCdL           =  ""+hashtableTemp.get("SYSTEM_CD") ;  
                  stringTempKeyL            =  ""+hashtableTemp.get("TEMP_KEY") ;  
                  stringVoucherYMDL      =  ""+hashtableTemp.get("VOUCHER_YMD") ;  
                  stringVoucherFlowNoL  =  ""+hashtableTemp.get("VOUCHER_FLOW_NO") ;  
                  stringCompanyCdL       =  ""+hashtableTemp.get("COMPANY_CD") ;  
                  stringStatusCdL             =  ""+hashtableTemp.get("STATUS_CD") ;  
                  stringNowStatusL           =  stringTempKeyL.endsWith("1") ? "預估"  :  "實際" ;
                  //
                  // FED1040 預估 存在檢核
                  hashtableAnd.put("SYSTEM_CD",     stringSystemCdL) ;
                  hashtableAnd.put("TEMP_KEY",        stringTempKeyL) ;
                  hashtableAnd.put("COMPANY_CD",  stringCompanyCdL) ;
                  vectorFED1040  =  exeFun.getQueryDataHashtableFED1("FED1040",    hashtableAnd,  "",  new  Vector(),  exeUtil) ;
                  if(vectorFED1040.size()  ==  0) {
                      messagebox(stringNowStatusL+"-傳票 不存在 FED1040 資料庫中。") ;  
                      return  false ;
                  }
                  // 預估
                  if("B".equals(stringVoucherType)) {
                      if("預估".equals(stringNowStatusL)) {
                          // 應尚未轉傳票
                          if("Z".equals(stringStatusCdL)) {
                              messagebox(stringNowStatusL+"-傳票 已轉傳票。") ;  
                              return  false ;
                          }
                      } else if("實際".equals(stringNowStatusL)) {
                          // 應尚未轉傳票
                          if("Z".equals(stringStatusCdL)) {
                              messagebox(stringNowStatusL+"-傳票 已轉傳票。") ;  
                              return  false ;
                          }
                      } else {
                          messagebox("資料錯誤，請洽資訊室。2") ;  
                          return  false ;
                      }
                  } else if("C".equals(stringVoucherType)) {
                      if("預估".equals(stringNowStatusL)) {
                          // 應已轉傳票
                          if(!"Z".equals(stringStatusCdL)) {
                              messagebox(stringNowStatusL+"-傳票 尚未轉傳票。") ;  
                              return  false ;
                          }
                          // FED1012 預估 轉傳票 存在檢核
                          stringVoucherYMDL  =  exeUtil.getDateConvertRoc(stringVoucherYMDL).replaceAll("/",  "") ;
                          hashtableAnd.put("VOUCHER_YMD",            stringVoucherYMDL) ;
                          hashtableAnd.put("VOUCHER_FLOW_NO",  stringVoucherFlowNoL) ;
                          hashtableAnd.put("COMPANY_CD",              stringCompanyCdL) ;
                          vectorFED1012  =  exeFun.getQueryDataHashtableFED1("FED1012",    hashtableAnd,  "",  new  Vector(),  exeUtil) ;
                          if(vectorFED1012.size()  ==  0) {
                              messagebox(stringNowStatusL+"-傳票 不存在 FED1012 資料庫中。") ;  
                              return  false ;
                          }
                      } else if("實際".equals(stringNowStatusL)) {
                          // 應尚未轉傳票
                          if("Z".equals(stringStatusCdL)) {
                              messagebox(stringNowStatusL+"-傳票 已轉傳票。") ;  
                              return  false ;
                          }
                      } else {
                          messagebox("資料錯誤，請洽資訊室。3") ;  
                          return  false ;
                      }                 
                  }

            }
        } else {
            if(vectorDoc2M0143.size()  >  0) {  messagebox("資料錯誤，請洽資訊室。") ;  return  false ; }
        }
        // 折讓檢核
        Vector        vectorDoc2M0161  =  new  Vector() ;
        hashtableAnd.put("BarCode",            stringBarCode) ;
        hashtableAnd.put("PRINT_STAUTS",  "Y") ;
        vectorDoc2M0161  =  exeFun.getQueryDataHashtableDoc("Doc2M0161",    hashtableAnd,  "",  new  Vector(),  exeUtil) ;
        if(vectorDoc2M0161.size()  >  0) {
            messagebox("已列印折讓單，請註銷折讓單後，再作轉傳票。") ;
            return  false ;
        }
        // 付款條件 1
        if(!"".equals(stringRetainBarCode))  booleanSource272  =  true ;
        
        if(!booleanSource272  &&  "999".equals(stringPayCondition1)) {
            message("[付款條件1] 不可為無。") ;
            return false ;
        }
        // 明細資料須特別處理的 ItemCd  1(有選項)
        vectorItemCd.add("B21") ;       // 發票種類
        // 明細資料須特別處理的 ItemCd  2
        vectorDescription.add("B08") ;    // 發票日期
        //vectorDescription.add("B09") ;   // 發票號碼
        vectorDescription.add("B10") ;   // 銷售類
        //vectorDescription.add("G08") ;   // 所得給付票據到期日 G08
        //vectorDescription.add("G20") ;   // 所得總額 G20
        vectorDescription.add("I07") ;   // 財產編號
        //
        if(jtable1.getRowCount( )  ==  0) {
            message("無資料項可存檔。") ;
            return  false ;
        } else {
            stringVoucherYMD          =  ""  +  getValueAt("Table1",  0,  "VOUCHER_YMD") ;
            stringCompanyCD           =  ""  +  getValueAt("Table1",  0,  "COMPANY_CD") ;
            // 傳票日期
            if("".equals(stringVoucherYMD)) {
                message("[傳票日期] 不可空白。") ;
                return  false ;
            }
            stringVoucherYMD  =  exeUtil.getDateFullRoc(stringVoucherYMD.trim( ),  "傳票日期") ;
            if(stringVoucherYMD.length()!=9) {
                message(stringVoucherYMD) ;
                return  false ;
            } else {
                // 避免修改傳票日期而觸發到期日的修改，因而清空到期日欄位
                for(int  intRow=0  ;  intRow<getTable("Table1").getRowCount( )  ;  intRow++) {
                    setValueAt("Table1",  stringVoucherYMD,  intRow,  "VOUCHER_YMD") ;
                }
                stringVoucherYMDFinal  =  stringVoucherYMD ; 
            }
            //
            stringVoucherYMD  =  exeUtil.getDateConvertRoc(stringVoucherYMD).replaceAll("/","") ;
            // System.out.println("----------------- 月結") ;
            if(!exeFun.isFED1019Exist(stringVoucherYMD,  stringCompanyCD,  booleanComNoType)) {
                message("當月份已月結，故不可作帳。") ;
                return  false ;
            }
            // System.out.println("----------------- 日結") ;
            if(!exeFun.isFED1043Exist(stringVoucherYMD,  stringCompanyCD,  booleanComNoType)) {
                message("當日已日結，故不可作帳。") ;
                return  false ;
            }
            stringDate2146  = datetime.getDate(stringVoucherYMD) ;
        }
        int               intRecordNo                   =  0 ;
        int               intCount                         =  0 ;
        Hashtable  hashtableDoc5M0224    =  null ;
        Hashtable  hashtableCanWriteAmt  =   null ;
        String         stringWriteAmt               =  "" ;
        String         stringBarCodeExcept     =  exeFun.getDoc2M040( )[13] ;
                for(int  intRowNo=0  ;  intRowNo<jtable1.getRowCount( )  ;  intRowNo++) {
            stringAcctNo               =  (""  +  getValueAt("Table1",  intRowNo,  "ACCT_NO")).trim( ) ;
            stringVoucherYMD     =  (""  +  getValueAt("Table1",  intRowNo,  "VOUCHER_YMD")).trim( ) ;
            stringType                  =  (""  +  getValueAt("Table1",  intRowNo,  "RowType")).trim( ) ;
            stringExchang            =  (""  +  getValueAt("Table1",  intRowNo,  "EXCHANG_AMT")).trim( ) ;
            stringMonteary           =  (""  +  getValueAt("Table1",  intRowNo,  "MONTEARY")).trim( ) ;
            stringDbCrCd             =  (""  +  getValueAt("Table1",  intRowNo,  "DB_CR_CD")).trim( ) ;
            stringAmt                    =  (""  +  getValueAt("Table1",  intRowNo,  "AMT")).trim( ) ;
            stringCompanyCD      =  (""  +  getValueAt("Table1",  intRowNo,  "COMPANY_CD")).trim( ) ;
            stringObjectCd            =  (""  +  getValueAt("Table1",  intRowNo,  "OBJECT_CD")).trim( ) ;
            stringDeptCd              =  (""  +  getValueAt("Table1",  intRowNo,  "DEPT_CD")).trim( ) ;
            stringRecordNo          =  (""  +  getValueAt("Table1",  intRowNo,  "RecordNo")).trim( ) ;
            stringCostIDPosition   =  (""+hashtableDoc2M042.get(stringAcctNo)).trim( ) ;
            retFED1004               =  exeFun.getFED1004(stringAcctNo,  booleanComNoType) ;
            //
            if(!"".equals(stringObjectCd)) {
                if(!isObjectCdStopUseOK(stringBarCode,  stringObjectCd,  exeFun,  exeUtil))  return  false ;
            }
            //
            if("J".equals(stringType))  stringWriteAmt  =  stringAmt ;
            // [沖銷金額] 須小於等於 [帳列金額  可沖銷金額(財會)]
            if(!booleanCostID0010  &&  "8111,1282,".indexOf(stringAcctNo)==-1  &&  retDoc5M0224.length>0  &&  "D".equals(stringDbCrCd)) {
                intRecordNo  =  exeUtil.doParseInteger(stringRecordNo) ;
                if(intRecordNo<=0  ||  intRecordNo>retDoc5M0224.length)  {
                    message("第 "  +  (intRowNo+1)  +  " 列之資料發生錯誤。") ;
                    jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                    return  false ;
                }
                if(",J35648,".indexOf(","+stringBarCode+",")  ==  -1) {
                    if(exeUtil.doParseDouble(stringAmt)  !=  exeUtil.doParseDouble(retDoc5M0224[intRecordNo-1][5].trim())) {
                        message("第 "  +  (intRowNo+1)  +  " 列之[沖銷金額]("+exeUtil.getFormatNum2 (stringAmt)+")  與 [請款金額]("+exeUtil.getFormatNum2 (retDoc5M0224[intRecordNo-1][5].trim())+")  不同。") ;
                        jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                        return  false ;
                    }
                }
                if(hashtableDoc5M0224==null)  {
                    hashtableDoc5M0224    =  exeFun.getVouherAmtDoc5M0224(stringBarCode,   stringCompanyCD,  exeUtil)  ;
                    hashtableCanWriteAmt  =  getCanWriteAmtForFED1013(stringCompanyCD,  retDoc5M0224,  booleanComNoType,  exeFun) ;
                }
                String    stringVoucherYMDL      =  retDoc5M0224[intRecordNo-1][1].trim() ;
                String    stringVoucherFlowNoL  =  retDoc5M0224[intRecordNo-1][2].trim() ;
                String    stringVoucherSeqNoL   =  retDoc5M0224[intRecordNo-1][3].trim() ;
                String    stringVoucher                =  stringVoucherYMDL  +  "-"  +  convert.add0(stringVoucherFlowNoL,  "5")  +  "-"  +  convert.add0(stringVoucherSeqNoL,  "4") ;
                double  doubleMoney                 =  exeUtil.doParseDouble(stringAmt)  +  exeUtil.doParseDouble(""+hashtableDoc5M0224.get(stringVoucher)) ;
                double  doubleTotalMoney         =  exeUtil.doParseDouble(""+hashtableCanWriteAmt.get(stringVoucher)) ;
                //
                doubleMoney                =  exeUtil.doParseDouble(convert.FourToFive(""+doubleMoney,  0)) ;
                if(",J35648,".indexOf(","+stringBarCode+",")  ==  -1  &&  doubleTotalMoney  <  doubleMoney) {
                    messagebox("第 "  +  (intRowNo+1)  +  " 列之 [請款金額]合計("+exeUtil.getFormatNum2 (""+doubleMoney)+") 大於 [可沖銷金額]("+exeUtil.getFormatNum2 (""+doubleTotalMoney)+")。") ;
                    return  false ;
                }
            }
            //
            if(stringBarCodeExcept.indexOf(stringBarCode)==-1  &&  !"".equals(stringDeptCd)) {
                if(vectorDeptCd.indexOf(stringDeptCd)  ==  -1) {
                    if("".equals(exeFun.getDepartNameFED1006(stringDeptCd,  booleanComNoType))) {
                        message("[部門] 不存在資料庫。") ;
                        jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                        if(!booleanRowTypeR)return  false ;
                    }
                }
                vectorDeptCd.add(stringDeptCd) ;
            }
            /* 年底預估不做處理 */
            if("W".equals(stringType))  {  booleanRowTypeW  =  true ;  continue ; }
            //
            if(!boolean2146DateCheck  &&  ("2146".equals(stringAcctNo)  ||  "2147".equals(stringAcctNo))  &&  !"11".equals(stringDate2146))   boolean2146DateCheck  =  true ;
            //
            if(!stringVoucherYMDFinal.equals(stringVoucherYMD)) {
                message("[傳票日期] 不一致。") ;
                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                return  false ;
            }
            // 發票進項稅額
            if("A".equals(stringType)) {
                booleanRowTypeA             =  true ;
                stringInvoiceNo                   =  (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_2")).trim( ) ;
                //doubleInvoiceTotalMoney  +=  exeFun.doParseDouble( (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_4")).trim( )) ;
            }
            // 折讓(減去特別折讓後須大於零)
            if("H".equals(stringType)) {
                double  doubleInvoiceTotalMoneyL       =  exeUtil.doParseDouble( (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_4")).trim( )) ;
                //stringRecordNo                         =  (""  +  getValueAt("Table1",  intRowNo,  "ClaimerMoney")).trim( ) ;
                doubleSpecialDiscountMoney   =  exeFun.getDiscountMoneyForDoc5M010(stringBarCode,  stringRecordNo) ;
                if(doubleInvoiceTotalMoneyL-doubleSpecialDiscountMoney  <  0) {
                    message("已變動到特別折讓部分。") ;
                    jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                    return  false ;
                }
            }
            //
            stringVoucherYMD  =  exeUtil.getDateConvert(stringVoucherYMD) ;
            //
            if("C".equals(stringDbCrCd)) {
                doubleAmtC  +=  exeUtil.doParseDouble(stringAmt) ;
            } else {
                doubleAmtD  +=  exeUtil.doParseDouble(stringAmt) ;
            }
            if("C".equals(stringType.trim( )))  intPositionC++ ;
            // 金額
            if(",Q81487,".indexOf(stringBarCode)==-1  &&  !"1264".equals(stringAcctNo)  &&  !stringAcctNoCf.equals(stringAcctNo)  &&  exeUtil.doParseDouble(stringAmt.trim( ))  <=  0) {
                message("[金額] 不可小於等於 0。") ;
                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                return  false ;
            }
            // 匯率
            if(exeUtil.doParseDouble(stringExchang.trim( ))  <  0) {
                message("[匯率] 不可小於 0。") ;
                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                return  false ;
            }
            setValueAt("Table1",  (""+exeUtil.doParseDouble(stringExchang.trim( ))),  intRowNo,  "EXCHANG_AMT") ;
            // 原類金額
            if(exeUtil.doParseDouble(stringMonteary.trim( ))  <  0) {
                message("[原類金額] 不可小於 0。") ;
                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                return  false ;
            }
            setValueAt("Table1",  (""+exeUtil.doParseDouble(stringMonteary.trim( ))),  intRowNo,  "MONTEARY") ;
            Hashtable  hashtableAcct  =  exeFun.getDoc2M041( ) ;
            String         stringPosition   =  (""+hashtableAcct.get(stringAcctNo)).trim( ) ;
            //  撥補零用金F297001 且為1264 不做處理
            if("1264".equals(stringAcctNo)  &&  booleanF297001) continue ;
            // 明細一 至 明細五 
            for(int  intNo=0  ;  intNo<retFED1004.length  ;  intNo++) {
                if(stringPosition.equals(""+(intNo+1)))  continue ;
                stringItemCd  =  retFED1004[intNo][0].trim( ) ;
                stringUseCd   =  (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_"+(intNo+1))).trim( ) ;
                System.out.println("stringUseCd@"+stringUseCd+"@"+intNo+" intRowNo"+intRowNo);
                if("A04".equals(stringItemCd))  {
                    stringDate  =  exeUtil.getDateRoc (stringUseCd,  "到期日") ;
                    if("C".equals(stringDbCrCd)) {
                        if(stringDate.length( )!=8  &&  stringDate.length( )!=9) {
                            if("".equals(stringUseCd)) {
                            } else {
                                message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 到期日格式錯誤。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                        } else {
                            stringDate  =  exeUtil.getDateConvertRoc(stringDate).replaceAll("/",  "") ;
                            setValueAt("Table1",  convert.replace(stringDate,  "/",  ""),  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                        }
                    }
                    continue ;// 到期日處理 A04
                }
                // 廣告企劃費代碼
                booleanCostID  =  !"".equals(stringCostIDPosition)        &&
                                             !"null".equals(stringCostIDPosition)  &&
                               check.isNum(stringCostIDPosition)     &&
                               Integer.parseInt(stringCostIDPosition)  ==  intNo ;
                if(booleanCostID) continue ;
                // 檢核值是正確
                retFED1002  =  exeFun.getFED1002(stringItemCd,  booleanComNoType) ;
                System.out.println("stringItemCd=>"+stringItemCd+" retFED1002=>"+retFED1002.length+" stringUseCd=>"+stringUseCd);
                 if(retFED1002.length  !=  0) {
                    if(!"".equals(stringUseCd.trim( ))) {
                        if(vectorItemCd.indexOf(stringItemCd)  !=  -1)  continue ;// 發票格式(與請款申請書不同)
                        if("!".equals(stringUseCd.trim( ).substring(0,1))) {
                            // 開頭為 !，使用者輸入為代號
                            stringUseCd  =  exeFun.getUseName(stringItemCd,  stringUseCd.trim( ).substring(1),  booleanComNoType) ;
                            if("".equals(stringUseCd)) {
                                message("[明細"  +  (intNo+1)  +  "]查無此代號。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            } else {
                                setValueAt("Table1",  stringUseCd,  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                            }
                        } else {
                            // 檢核資料
                            stringUseCd  =  exeFun.getUseCd(stringItemCd,  stringUseCd.trim( ),  booleanComNoType) ;
                            if("".equals(stringUseCd)) {
                                message("[明細"  +  (intNo+1)  +  "] 輸入資料不正確。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                        }
                    } 
                } else {
                    //  vectorDescription(B21、B08、B09、B10)
                    if(vectorAcctNoBandNo.indexOf(stringAcctNo)!=-1  &&  "A02".equals(stringItemCd)) {
                          if("".equals(stringUseCd)) {
                              message("第 "  +  (intRowNo+1)  +  " 列之[明細"  +  (intNo+1)  +  "]資料不得為空白。") ;
                              jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                              if(!booleanRowTypeR)return  false ;
                          } else {
                              if(!exeFun.isBanKNoExist(stringObjectCd,  stringUseCd,  stringCompanyCD)) {
                                  message("查無此帳號") ;
                                  if(!booleanRowTypeR)return  false ;
                              }
                          }
                    }
                    if(",2143,".indexOf(stringAcctNo)==-1  &&  vectorDescription.indexOf(stringItemCd)  !=  -1  &&  "".equals(stringUseCd)) {                         
                          if("I07".equals(stringItemCd)  &&  ",E96853,".indexOf(","+stringBarCode+",")==-1) {
                          //沒有導入固支系統，故先不檢核財產編號
                          /*
                              message("第 "  +  (intRowNo+1)  +  " 列之[明細"  +  (intNo+1)  +  "]資料不得為空白。") ;
                              jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                              if(!booleanRowTypeR)return  false ;
                          */    
                          }
                    }
                    // 財產編號 I07
                    if("I07".equals(stringItemCd)  &&  !"".equals(stringUseCd)) {
                        String[]  arrayTempL  =  convert.StringToken(stringUseCd,  "-") ;
                        if(arrayTempL[0].length()  != 2) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [財產編號] 格式錯誤。1") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;                       
                        }
                        if(arrayTempL[1].length()  != 9) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [財產編號] 格式錯誤。2") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;                       
                        }
                        if(arrayTempL.length>2  &&  arrayTempL[2].length()  != 5) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [財產編號] 格式錯誤。3") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;                       
                        }
                    }
                    // 發票日期 B08
                    if("B08".equals(stringItemCd)) {
                        if(",1264,".indexOf(stringAcctNo)!= -1  ||  !"".equals(stringUseCd)) {
                            stringInvoiceDate  =  exeUtil.getDateRoc(stringUseCd,  "發票日期") ;
                            if(stringInvoiceDate.length()!=8  &&  stringInvoiceDate.length()!=9) {
                                message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之"  +  stringInvoiceDate) ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            } else {
                                stringInvoiceDate  =  exeUtil.getDateConvertRoc(stringInvoiceDate).replaceAll("/",  "") ;
                                setValueAt("Table1",  convert.replace(stringInvoiceDate,"/",""),  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                            }
                        }
                    }
                    // 發票號碼 B09
                    if("1264".equals(stringAcctNo)  &&  "B09".equals(stringItemCd)  &&  "A".equals(stringType)) {
                        stringInvoiceKind   =  (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_5")).trim( ) ;
                        if(("電算".equals(stringInvoiceKind)  ||  "收銀".equals(stringInvoiceKind)) &&  stringUseCd.length()  !=  10) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 發票大小錯誤。") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;
                        }
                        // 重覆
                        System.out.println("vectorInvoiceNo>>>" + vectorInvoiceNo);
                        System.out.println("stringUseCd>>>" + stringUseCd);
                        if(vectorInvoiceNo.indexOf(stringUseCd)  !=  -1) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 重覆。") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;
                        }
                        vectorInvoiceNo.add(stringUseCd) ;
                        // 當不屬於此 BarCode 的發票號碼 且 資料庫存在此發票時
                        //if(exeFun.getInvoiceNoDoc2M011(stringBarCode).indexOf(stringUseCd)  ==  -1  &&  !exeFun.isExistInvoiceNoCheck(stringUseCd)) {
                        String  stringFactoryNameL  =  exeFun.getFactoryName(stringObjectCd,  booleanComNoType) ;if("".equals(stringFactoryNameL))  stringFactoryNameL  =  "11111" ;
                        if(stringInvoiceKind.indexOf("收據")  !=  -1  ||  stringInvoiceKind.indexOf("載具")  !=  -1) {
                        
                        } else if(booleanA110202  &&  !"".equals(stringInvoiceKind)  &&  stringInvoiceKind.indexOf("收據")!=-1  && (stringFactoryNameL.indexOf("管委會")!=-1  ||
                                                                                                                                                                                                     stringFactoryNameL.indexOf("中華電信")!=-1 ||
                                                                                                                     stringFactoryNameL.indexOf("亞太電信")!=-1 ||
                                                                                                                      stringFactoryNameL.indexOf("台灣電信")!=-1
                                                                                                                                                                                                    )) {
                        } else if(booleanTable14) {
                            // 代收代付
                            Vector  vectorComNo  =  new  Vector() ;  vectorComNo.add(stringCompanyCD) ;
                            if(!exeFun.isExistInvoiceNoCheck14(stringInvoiceNo,  stringBarCode,  "",  vectorComNo)) {
                                message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 已存在資料庫中。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                            Hashtable  hashtableLimit   =  new  Hashtable() ;
                            String      stringDateStart  = stringVoucherYMD ;
                            //
                            stringDateStart  =  exeUtil.getDateConvertRoc(stringDateStart).replaceAll("/",  "") ;
                            stringDateStart  =  datetime.dateAdd(stringDateStart,  "y",  -2) ;
                            //
                            hashtableLimit.put("ComNo",      stringCompanyCD) ;
                            hashtableLimit.put("DateStart",  stringDateStart) ;
                            hashtableLimit.put("InvoiceNo",  stringInvoiceNo) ;
                            System.out.println("isExistInvoiceNoCheckFED1012-----------------------------------------S") ;
                            if(!"B1721".equals(getUser())  &&  !exeFun.isExistInvoiceNoCheckFED1012(hashtableLimit,  exeUtil)) {
                            //if(!exeFun.isExistInvoiceNoCheckFED1012(hashtableLimit,  exeUtil)) {
                                String  stringBarCodeDoc2M0102  =  exeFun.getNameUnionDoc("BarCodeDoc2M0102",  "Doc2M040",  " AND  ISNULL(BarCodeDoc2M0102,'')  <>  '' ",  new  Hashtable(),  exeUtil) ;
                                if(stringBarCodeDoc2M0102.indexOf(stringBarCode)  == -1) {
                                    message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 已存在資料庫中。") ;
                                    jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                    if(!booleanRowTypeR)return  false ;
                                }
                            }
                            System.out.println("isExistInvoiceNoCheckFED1012-----------------------------------------E") ;
                        } else {
                            if(!exeFun.isExistInvoiceNoCheck(stringInvoiceNo,  stringBarCode)) {
                                message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 已存在資料庫中。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                            Hashtable  hashtableLimit   =  new  Hashtable() ;
                            String      stringDateStart  = stringVoucherYMD ;
                            //
                            stringDateStart  =  exeUtil.getDateConvertRoc(stringDateStart).replaceAll("/",  "") ;
                            stringDateStart  =  datetime.dateAdd(stringDateStart,  "y",  -2) ;
                            //
                            hashtableLimit.put("ComNo",      stringCompanyCD) ;
                            hashtableLimit.put("DateStart",  stringDateStart) ;
                            hashtableLimit.put("InvoiceNo",  stringInvoiceNo) ;
                            System.out.println("isExistInvoiceNoCheckFED1012-----------------------------------------S") ;
                            if(!"B1721".equals(getUser())  &&  !exeFun.isExistInvoiceNoCheckFED1012(hashtableLimit,  exeUtil)) {
                            //if(!exeFun.isExistInvoiceNoCheckFED1012(hashtableLimit,  exeUtil)) {
                                String  stringBarCodeDoc2M0102  =  exeFun.getNameUnionDoc("BarCodeDoc2M0102",  "Doc2M040",  " AND  ISNULL(BarCodeDoc2M0102,'')  <>  '' ",  new  Hashtable(),  exeUtil) ;
                                if(stringBarCodeDoc2M0102.indexOf(stringBarCode)  == -1) {
                                    message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之 [發票號碼] 已存在資料庫中。") ;
                                    jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                    if(!booleanRowTypeR)return  false ;
                                }
                            }
                        }
                    }
                    // 發票未稅金額 B10
                    if("1264".equals(stringAcctNo)  &&  "B10".equals(stringItemCd)  &&  ",J48865,E91373,".indexOf(stringBarCode)==-1 ) {
                        doubleInvoiecMoneyTax  =  exeUtil.doParseDouble(stringUseCd)  *  (doubleTaxRate/100) ;
                        doubleInvoiceTax             =  exeUtil.doParseDouble(stringAmt) ;
                        //
                        double  doubleInvoiceMoneyL                 =  exeUtil.doParseDouble((""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_3")).trim( )) ;
                        double  doubleInvoiceTotalMoneyL         =  exeUtil.doParseDouble((""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_4")).trim( )) ;
                        if(exeUtil.doParseDouble(stringAmt)==0  &&  doubleInvoiceMoneyL==doubleInvoiceTotalMoneyL) {
                            // 稅額為 0 不作處理
                        }else if((doubleInvoiecMoneyTax-3  >  doubleInvoiceTax)  ||  (doubleInvoiecMoneyTax+3  <  doubleInvoiceTax)) {
                            stringInvoiceKind   =  (""  +  getValueAt("Table1",  intRowNo,  "DESCRIPTION_5")).trim( ) ;
                            // 2014-08-19 依管理費用(收據彙總)
                            if(stringInvoiceKind.indexOf("收據彙總")!=-1) {
                            
                            } else if(stringInvoiceKind.indexOf("載具號碼彙總")!=-1) {
                            
                            } else {
                                message("第 "  +  (intRowNo+1)  +  " 列之[發票稅額] 須為 [發票未稅金額] 乘以 [稅率] 正負 3 的範圍內。") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                        }
                    }
                    // 發票總金額 B11
                    if("1264".equals(stringAcctNo)  &&  "B11".equals(stringItemCd)) {
                        doubleInvoiceTax                        =  exeUtil.doParseDouble(stringAmt.trim( )) ;
                        doubleInvoiceMoney                   =  exeUtil.doParseDouble((""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_3")).trim( )) ;
                        if(exeUtil.doParseDouble(stringUseCd)  !=  (doubleInvoiceTax  +  doubleInvoiceMoney)) {
                            message("第 "  +  (intRowNo+1)  +  " 列之[發票總金額] "  +  stringUseCd  +  " 不等於 [發票未稅金額] "  +  doubleInvoiceMoney  +  " 加上 [發票稅額] "  +  doubleInvoiceTax  +  "。") ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;                       
                        }
                        //setValueAt("Table1",  convert.FourToFive(""  +  (doubleInvoiceTax  +  doubleInvoiceMoney), 0),  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                    }
                    // 所得給付票據到期日 G08
                    if("G08".equals(stringItemCd)    &&  "C".equals(stringDbCrCd)) {
                        stringInvoiceDate  =  exeUtil.getDateRoc(stringUseCd,  "所得給付票據到期日") ;
                        if(stringInvoiceDate.length()!=8  &&  stringInvoiceDate.length()!=9) {
                            message("第 "  +  (intRowNo+1)  +  " 列之 [明細"  +  (intNo+1)  +  "] 之"  +  stringInvoiceDate) ;
                            jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                            if(!booleanRowTypeR)return  false ;
                        } else {
                            stringInvoiceDate  =  exeUtil.getDateConvertRoc(stringInvoiceDate).replaceAll("/",  "") ;
                            setValueAt("Table1",  convert.replace(stringInvoiceDate,  "/",  ""),  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                        }
                    }
                    // 所得總額 G20 
                    if("A".equals(stringVoucherType)  &&  !"Y".equals(getValue("CountSame"))  &&  "G20".equals(stringItemCd)    &&  "C".equals(stringDbCrCd)  &&  "228231,".indexOf(stringAcctNo)==-1) {
                        double     doubleReceiptTaxRate       =  exeUtil.doParseDouble(getValue("ReceiptRate").trim( )) /  100 ;
                        String[][]  retDoc2M013                      =  null ;
                        if(booleanSource) {
                            // 行銷
                            retDoc2M013  =  exeFun.getTableDataDoc(" SELECT  ReceiptTaxType "  +
                                                                                                  " FROM  Doc2M013 "  +
                                                              " WHERE  BarCode  = '"+stringBarCode+"' "  +
                                                                   " AND  ReceiptTax > 0  "  +
                                                                 " ORDER BY  RecordNo" ) ;
                        } else {
                            // 管理
                            retDoc2M013  =  exeFun.getTableDataDoc(" SELECT  ReceiptTaxType "  +
                                                                                                  " FROM  Doc5M023 "  +
                                                              " WHERE  BarCode  = '"+stringBarCode+"' "  +
                                                                   " AND  ReceiptTax > 0  "  +
                                                                 " ORDER BY  RecordNo" ) ;
                        }
                        System.out.println("intCount("+intCount+")stringType("+stringType+")---------------------------------"+retDoc2M013.length) ;
                        doubleReceiptTaxRate       =  exeUtil.doParseDouble(retDoc2M013[intCount][0].trim()) /  100 ;
                        intCount++ ;
                        //
                        double  doubleReceiptMoney          =  exeUtil.doParseDouble((""+getValueAt("Table1",  intRowNo,  "ClaimerMoney")).trim( )) ;
                        double  doubleReceiptTotalMoney  =  exeUtil.doParseDouble(stringUseCd) ;
                        
                        double  doubleReceiptMoneyTax     =  doubleReceiptTotalMoney  *  doubleReceiptTaxRate ;
                        double  doubleReceiptTax               =  exeUtil.doParseDouble(stringAmt.trim( )) ;
                        
                        // 扣繳金額須為所得淨額乘以稅率正負3 的範圍內
                        if(",Z0001,Z8000,".indexOf(","+stringObjectCd+",")  ==  -1) {
                            doubleReceiptMoneyTax  =  exeUtil.doParseDouble(convert.FourToFive(""+doubleReceiptMoneyTax,  0)) ;
                            if((doubleReceiptMoneyTax-3  >  doubleReceiptTax)  ||  (doubleReceiptMoneyTax+3  <  doubleReceiptTax)) {
                                message("第 "  +  (intRowNo+1)  +  " 列之[扣繳稅額] ("+exeUtil.getFormatNum2(""+doubleReceiptTax)+") 須為 [所得淨額] 乘以 [稅率] 正負 3 的範圍內。("+exeUtil.getFormatNum2(""+doubleReceiptMoneyTax)+")") ;
                                jtable1.setRowSelectionInterval(intRowNo,  intRowNo) ;
                                if(!booleanRowTypeR)return  false ;
                            }
                        }
                        // 所得淨額 + 扣繳金額
                        //setValueAt("Table1",  convert.FourToFive(""+(doubleReceiptTax+doubleReceiptMoney),  0),  intRowNo,  "DESCRIPTION_"+(intNo+1)) ;
                    }
                }
            }
        }
        // 零用金時不做處理 START
        String      stringCostID                =  "" ;
        String      stringCostID1              =  "" ;
        String[][]  retDoc2M012              =  booleanSource?
                                                                exeFun.getDoc2M012(stringBarCode) :
                                    new  String[0][0] ;
        Vector     vectorPocketMoney    =  new  Vector( ) ;
        Vector     vectorCostIDExcept    =  new  Vector( ) ;
        boolean  booleanPocketMoney  =  false ;  // true 為零用金特殊狀態
        boolean  booleanCostIDExcept  =  false ;  // true 為特殊費用狀態
        //
        vectorPocketMoney.add("31") ;
        vectorPocketMoney.add("32") ;
        if(retDoc2M012.length  >  0) {
            stringCostID  =  retDoc2M012[0][4].trim( ) ;
            if(vectorPocketMoney.indexOf(stringCostID)  !=  -1)  booleanPocketMoney  =  true ;
            vectorCostIDExcept.add("261") ;
            vectorCostIDExcept.add("231") ;
            for(int  intNo=0  ;  intNo<retDoc2M012.length  ;  intNo++) {
                stringCostID  =  retDoc2M012[intNo][4].trim( ) ;
                stringCostID1  =  retDoc2M012[intNo][5].trim( ) ;
                if(vectorCostIDExcept.indexOf(stringCostID+stringCostID1)  !=  -1) {
                    booleanCostIDExcept  =  true ;
                }
            }
        } else {
            booleanCostIDExcept  =  true ;
        }
        // 零用金時不做處理 END
        // 2013-09-11 固資
        if(!isAssetOK (booleanSource,  stringBarCode,  exeUtil,  exeFun,  hashtableAsset)) return  false  ;
        String         stringAssetStatus                 = ""+hashtableAsset.get("STATUS") ;  // OK
        String      stringDocNoType         =  exeFun.getNameUnionDoc("DocNoType",  booleanSource? "Doc2M010" : "Doc5M020",  " AND  BarCode  =  '"+stringBarCode+"' ",  new  Hashtable(),  exeUtil) ;
        //System.out.println(doubleRealMoneySum+"----------------------"+doubleInvoiceTotalMoneySum) ;
        if(",B,C,".indexOf(","+stringDocNoType+",")==-1  &&  !booleanRowTypeR  &&  !booleanCostIDExcept  &&  !booleanA110201) {
            doubleInvoiceTotalMoney  =  exeFun.getInvoiceMoneySum(booleanSource?"Doc2M011":"Doc5M021",  stringBarCode) +  
                                                          exeFun.getInvoiceTaxSum(booleanSource?"Doc2M011":"Doc5M021",  stringBarCode) ;
            if(doubleInvoiceTotalMoney  <=  0  ||  !booleanSource) {
                doubleInvoiceTotalMoney  +=  exeFun.getReceiptTotalMoneySumForDoc2M013Union(booleanSource?"Doc2M013":"Doc5M023",  stringBarCode)  ;
            }
            if(!booleanSource) {
                doubleInvoiceTotalMoney  +=  exeFun.getInvoiceMoneySum("Doc5M0211",  stringBarCode) +  
                                          exeFun.getInvoiceTaxSum("Doc5M0211",  stringBarCode) ;
            }
            if(!"OK".equals(stringAssetStatus)  &&  doubleAmtC  !=  doubleInvoiceTotalMoney  &&  !booleanCostIDExcept  &&  !booleanPocketMoney  &&  !booleanRowTypeW) {
                //20180522 modified by B03812
                /*
                int  ans  =  JOptionPane.showConfirmDialog(null,  
                                                "借方總金額("+doubleAmtC+") 不等於 請款申請書之發票總金額或個人收據總金額合計("+doubleInvoiceTotalMoney+")，是否要存檔?"+stringAssetStatus,
                                                "訊息",  
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.WARNING_MESSAGE) ;
                if(ans  ==  JOptionPane.NO_OPTION)  {
                    return false ;
                }
                */
                messagebox("借方總金額("+doubleAmtC+") 不等於 請款申請書之發票總金額或個人收據總金額合計("+doubleInvoiceTotalMoney+")。") ;
                return  false ;                 
            }
        }
        // 
        if(doubleAmtC  !=  doubleAmtD) {
            //20180522 modified by B03812       
            /*
            //System.out.println(doubleAmtC+"---------------"+doubleAmtD) ;
            int  ans  =  JOptionPane.showConfirmDialog(null,  
                                            "[借方金額] 不等於 [貸方金額]，是否要存檔?",
                                            "訊息",  
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.WARNING_MESSAGE) ;
            if(ans  ==  JOptionPane.NO_OPTION)  {
                return false ;
            }
            */
            messagebox("[借方金額] 不等於 [貸方金額]。") ;
            return  false ;           
        }
        /*if(boolean2146DateCheck) {
            int  ans  =  JOptionPane.showConfirmDialog(null,  
                                            "會計科目為 2146 或 2147 票期非 11 日是否要存檔。",
                                            "訊息",  
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.WARNING_MESSAGE) ;
            if(ans  ==  JOptionPane.NO_OPTION)  {
                return false ;
            }
        }
        */
        System.out.println("退保留("+retDoc2M010[0][26].trim()+")-----------------------S") ;
        // 2010/02/24 建立 [管理費用退保留款沖銷] 機制oce
        String      stringBarCodeL                 =  "" ;
        String[]    arrayTemp                        =  null ;
        double    doubleBackRetainMoney  =  0 ;
        if(!booleanSource  &&  !"".equals(stringRetainBarCode)) {
            System.out.println("退保留流程-----------------------") ;
            String       stringSql                  =  "" ;
            String       stringCanWriteAmt  =  "" ;
            String[][]  retDoc5M02201        =  exeFun.getDoc5M02201(stringBarCode,  "",  "") ;
            Vector     vectorDoc5M02201  =  new  Vector() ;
            if(retDoc5M02201.length  >  0) {
                // 依 stringBarCode 於 Doc5M02201 存在
                doubleBackRetainMoney  =  0 ;
                for(int  intNo=0  ;  intNo<retDoc5M02201.length  ;  intNo++) {
                    doubleBackRetainMoney  +=  exeUtil.doParseDouble(retDoc5M02201[intNo][2].trim()) ;
                    vectorDoc5M02201.add(retDoc5M02201[intNo]) ;
                }
                doubleBackRetainMoney  =  exeUtil.doParseDouble(convert.FourToFive(""+doubleBackRetainMoney,  0)) ;
                if(doubleBackRetainMoney  !=  exeUtil.doParseDouble(stringWriteAmt)) {
                    vectorDoc5M02201  =  new  Vector() ;
                    //messagebox("金額合計與本次退保留金額不相同，重新建立 Doc5M02201 資料") ;
                    //return  false ;
                }
            } 
            //重新計算公式
            if(vectorDoc5M02201.size()  ==  0) {
                String[][]  retDoc5M020  =  exeFun.getDoc2M010Union("Doc5M020",  stringRetainBarCode) ;
                if(retDoc5M020.length   ==  0) {
                    System.out.println("退保留-請購單----------------------") ;
                    // 由 stringRetainBarCode 找到  [所有的請購單號]
                    String[][]  retDoc5M011          =  exeFun.getDoc3M011Union("Doc5M011",  stringRetainBarCode);
                    if(retDoc5M011.length  ==  0) {
                        messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無對應之請購條碼編號("  +  stringRetainBarCode +")請購資訊。") ;
                        return  false ;
                    }
                    String      stringPurchaseNo      =  retDoc5M011[0][1].trim() ;
                    Vector     vectorPurchaseNo     =  new  Vector() ;  vectorPurchaseNo.add(stringPurchaseNo) ;
                    Vector     vectorAllPurchaseNo  =  exeFun.getAllPurchaseNo(stringCompanyCD,  stringObjectCd,  vectorPurchaseNo,  "",  "",  false) ;
                    if(vectorAllPurchaseNo.size()  ==  0) {
                        messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無對應之條碼編號("  +  stringRetainBarCode +")請購資訊。") ;
                        return  false ;
                    }
                    // 由 [所有的請購單號] 找到對應的 [Doc5M020 資料(已轉傳票)(依時間排列)]
                    String[][]  retDoc5M020L           =  getDoc5M020(stringCompanyCD,  stringObjectCd,  vectorAllPurchaseNo,  exeFun) ;
                    if(retDoc5M020L.length  ==  0) {
                        messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無保留金額之請款條碼編號。") ;
                        return  false ;
                    }
                    // 已使用的退保留金額  
                    // 0  BarCode       1  RetainMoney
                    stringSql  =  "" ;
                    for(int  intNo=0  ;  intNo<retDoc5M020L.length  ;  intNo++) {
                        stringBarCodeL  =  retDoc5M020L[intNo][0].trim() ;
                        if(!"".equals(stringSql))  stringSql  +=  ", " ;
                        stringSql  +=  " '"+stringBarCodeL+"' " ;
                    }
                                        stringSql                            =  " AND  BarCode  <>  '"+stringBarCode+"' " +
                                                                            " AND  BarCodeRef  IN  ("+stringSql+") " ;
                    Hashtable  hashtableBarCodeRef        =  getDoc5M02201(stringSql,  exeFun,  exeUtil) ;
                    String         stringRetainMoneyUseL      =  "" ;
                    String         stringRetainMoneySumL     =  "" ;
                    String         stringRetainMoneyL            =  "" ;
                    double        doubleCanRetainMoneyL   =  0 ;
                    double        doubleBackRetainMoneyL  =  0 ;
                    double        doubleThisRetainMoneyL   =  exeUtil.doParseDouble(stringWriteAmt) ;
                    boolean      booleanFlag                        =  true ;
                    // 依序判斷，建立 [退保留款沖銷表格 Doc5M02201]
                    for(int  intNo=0  ;  intNo<retDoc5M020L.length  ;  intNo++) {
                        stringBarCodeL                  =  retDoc5M020L[intNo][0].trim() ;
                        stringRetainMoneySumL    =  retDoc5M020L[intNo][1].trim() ;
                        stringRetainMoneyUseL     =  ""+hashtableBarCodeRef.get(stringBarCodeL) ;
                        doubleCanRetainMoneyL  =  exeUtil.doParseDouble(stringRetainMoneySumL)  -  exeUtil.doParseDouble(stringRetainMoneyUseL) ;
                        booleanFlag                       =  doubleCanRetainMoneyL  >  doubleThisRetainMoneyL ;
                        if(doubleCanRetainMoneyL  <=  0)  continue ;
                        //
                        if(booleanFlag) {
                            // 結束
                            doubleBackRetainMoneyL  =  doubleThisRetainMoneyL ;
                        } else {
                            doubleBackRetainMoneyL    =  doubleCanRetainMoneyL ;
                            doubleThisRetainMoneyL    -=  doubleCanRetainMoneyL ;
                        }
                        if(doubleBackRetainMoneyL  ==  0)  continue ;
                        arrayTemp      =  new  String[4] ;
                        arrayTemp[0]  =  stringBarCode ;
                        arrayTemp[1]  =  stringBarCodeL ;
                        arrayTemp[2]  =  convert.FourToFive(""+doubleBackRetainMoneyL,  0) ;
                        vectorDoc5M02201.add(arrayTemp) ;
                        if(booleanFlag) break ;// 結束
                    }
                } else {
                    System.out.println("退保留-請款單----------------------") ;
                    String      stringWriteRetainMoney    =  retDoc5M020[0][13].trim() ;
                    String[][]  retDoc5M02201L               =  exeFun.getDoc5M02201("",  stringRetainBarCode,  " AND  BarCode  <>  '"+stringBarCode+"' ") ;
                                     doubleBackRetainMoney  =  0 ;
                    for(int  intNo=0  ;  intNo<retDoc5M02201L.length  ;  intNo++) {
                        doubleBackRetainMoney  +=  exeUtil.doParseDouble(retDoc5M02201L[intNo][2].trim()) ;
                    }
                    doubleBackRetainMoney  +=  exeUtil.doParseDouble(stringWriteAmt) ;
                    doubleBackRetainMoney    =  exeUtil.doParseDouble(convert.FourToFive(""+doubleBackRetainMoney,  0)) ;
                    if(doubleBackRetainMoney  >  exeUtil.doParseDouble(stringWriteRetainMoney)) {
                        messagebox("請款申請書之沖銷合計("+exeUtil.getFormatNum2 (""+doubleBackRetainMoney)+") 大於可沖銷金額("+exeUtil.getFormatNum2 (stringWriteRetainMoney)+")") ;
                        return  false ;
                    }
                    arrayTemp      =  new  String[4] ;
                    arrayTemp[0]  =  stringBarCode ;
                    arrayTemp[1]  =  stringRetainBarCode ;
                    arrayTemp[2]  =  stringWriteAmt ;
                    vectorDoc5M02201.add(arrayTemp) ;
                }
                if(vectorDoc5M02201.size()  ==  0) {
                    messagebox("Doc5M02201 資料發生錯誤，請洽資訊室。");
                    return  false ;
                }
                Vector  vectorSql            =  new  Vector() ;
                // 刪除 Doc5M02201
                stringSql  =  "DELETE  FROM  Doc5M02201  WHERE  BarCode  =  '"+stringBarCode+"'  " ;  vectorSql.add(stringSql) ;
                // 新增 Doc5M02201    BarCode     BarCodeRef      BackRetainMoney
                for(int  intNo=0  ;  intNo<vectorDoc5M02201.size()  ;  intNo++) {
                    arrayTemp  =  (String[])  vectorDoc5M02201.get(intNo) ;
                    if(arrayTemp  ==  null) {
                        messagebox("資料發生錯誤，請洽資訊室。");
                        return  false ;
                    }
                    stringBarCode    =  arrayTemp[0] ;
                    stringBarCodeL  =  arrayTemp[1] ;
                    stringAmt            =  arrayTemp[2] ;
                    stringSql  =  " INSERT  INTO  Doc5M02201 (BarCode,                    BarCodeRef,               BackRetainMoney)  "  +
                                                                         " VALUES ( '"+stringBarCode+"',  '"+stringBarCodeL+"',   "+stringAmt+" ) " ; 
                    vectorSql.add(stringSql) ;
                }
                exeFun.doExecVectorSqlForDoc(vectorSql) ;
            }
            String      stringAcctNoL                =  "" ;
            String      stringVoucherYMDL      =  "" ;
            String      stringVoucherYMDRoc   =  "" ;
            String      stringVoucherFlowNoL  =  "" ;
            String      stringVoucherSeqNoL   =  "" ;
            String      stringCompanyCdL       =  "" ;
            String      stringKindL                    =  "" ;
            String[][]  retDoc2M014L              =  null ;
            String[][]  retFED1012L                =  null ;
            String[][]  retFED1013                  =  null ;
            
            for(int  intNo=0  ;  intNo<vectorDoc5M02201.size()  ;  intNo++) {
                arrayTemp  =  (String[])  vectorDoc5M02201.get(intNo) ;
                if(arrayTemp  ==  null) {
                    messagebox("資料發生錯誤，請洽資訊室。2");
                    return  false ;
                }
                stringBarCode    =  arrayTemp[0] ;
                stringBarCodeL  =  arrayTemp[1] ;
                stringAmt            =  arrayTemp[2] ;
                // 傳票可沖銷金額 須大於等於 此次沖銷，否則發生錯誤不允許轉傳票
                retDoc2M014L  =  exeFun.getDoc2M014(stringBarCodeL,  "I") ;
                if(retDoc2M014L.length  ==  0) {
                    messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無對應欲沖銷之請款條碼編號("  +  stringBarCodeL +")傳票資訊(Doc2M014)。") ;
                    return  false ;
                }
                stringAcctNoL                =  retDoc2M014L[0][0].trim() ;
                stringVoucherYMDL      =  retDoc2M014L[0][1].trim() ;
                stringVoucherYMDL       =  exeUtil.getDateConvertRoc(stringVoucherYMDL).replaceAll("/","") ;
                stringVoucherFlowNoL  =  retDoc2M014L[0][2].trim() ;
                stringVoucherSeqNoL   =  retDoc2M014L[0][3].trim() ;
                stringCompanyCdL       =  retDoc2M014L[0][4].trim() ;
                stringKindL                    =  retDoc2M014L[0][5].trim() ;
                retFED1012L                 =  exeFun.getFED1012(stringVoucherYMDL,  stringVoucherFlowNoL,  stringVoucherSeqNoL,  stringCompanyCdL,  stringKindL,  booleanComNoType) ;
                if(retFED1012L.length == 0  ||  !stringAcctNoL.equals(retFED1012L[0][1].trim())) {
                    messagebox("此次轉傳票條碼編號("  +  stringBarCode  +  ")，欲沖銷之請款條碼編號退保留款條碼編號("  +  stringBarCodeL +")傳票會計科目資訊不一致(FED1012)。") ;
                    return  false ;
                }
                //
                stringSql                        =  "(I.VOUCHER_YMD  =  "          +  stringVoucherYMDL     +  "  AND  "  +
                                            " I.VOUCHER_FLOW_NO  =  "+  stringVoucherFlowNoL  +  "  AND "  +
                                            " I.VOUCHER_SEQ_NO  =  "   +  stringVoucherSeqNoL   +  " )" ;
                stringSql                        =  " AND ("+  stringSql + ")" ;
                retFED1013                  =  exeFun.getCanWriteAmtForFED1013(stringCompanyCdL,   stringSql,  booleanComNoType) ;
                if(retFED1013.length  ==  0) {
                    messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，查無對應欲沖銷之請款條碼編號("  +  stringBarCodeL +") 傳票資訊(FED1013)。") ;
                    return  false ;
                }
                stringCanWriteAmt       =   retFED1013[0][3].trim() ;
                if(stringBarCodeExcept.indexOf(stringBarCode)==-1  &&  exeUtil.doParseDouble(stringCanWriteAmt)  <  exeUtil.doParseDouble(stringAmt)) {
                    messagebox("此次轉傳票請款條碼編號("  +  stringBarCode  +  ")，欲沖銷之請款條碼編號("  +  stringBarCodeL +") 傳票可沖銷金額("+exeUtil.getFormatNum2 (stringCanWriteAmt)+")小於本次沖銷金額("+exeUtil.getFormatNum2 (stringAmt)+")。") ;
                    return  false ;
                }
            }
        }
        System.out.println("退保留-----------------------E") ;
        //20180328 移除回押租賃系統判斷
        /*
        if(!isA312105BackRentSysOK(exeFun,  exeUtil,  vectorRentSql)) return  false ;
        */
        message("") ;
              return  true ;
    }
    public  boolean  isObjectCdStopUseOK(String  stringBarCode,  String  stringObjectCd,  Doc2M010  exeFun,  FargloryUtil  exeUtil)throws  Throwable {
        Vector      vectorDoc2M010          =  null ;
        Hashtable  hashtableDoc2M010        =  null ;
        Hashtable  hashtableCond              =  new  Hashtable() ;
        String      stringCDate               =  "" ;
        String      stringPurchaseNoExist       =  "" ;
        String      stringDocNoType         =  "" ;
        String      stringStopUseMessage      =  "" ;
        //
        vectorDoc2M010      =  exeFun.getQueryDataHashtableDoc("Doc2M010",  new  Hashtable(),  " AND  BarCode  =  '"+stringBarCode+"'  ",  new  Vector(),  exeUtil) ;       if(vectorDoc2M010.size()  ==  0)  return  false ;
        hashtableDoc2M010   =  (Hashtable)  vectorDoc2M010.get(0) ;                                                                                             if(hashtableDoc2M010  ==  null)    return  false ;
        stringCDate           =  ""+hashtableDoc2M010.get("CDate") ;
        stringPurchaseNoExist =  ""+hashtableDoc2M010.get("PurchaseNoExist") ;
        stringDocNoType       =  ""+hashtableDoc2M010.get("DocNoType") ;
        // 停權
        hashtableCond.put("OBJECT_CD",            stringObjectCd) ;
        hashtableCond.put("CHECK_DATE",         stringCDate) ;
        hashtableCond.put("PurchaseNo_Exist",     stringPurchaseNoExist) ;
        hashtableCond.put("DocNoType",            stringDocNoType) ;
        hashtableCond.put("SOURCE",               "C") ;
        hashtableCond.put("FieldName",          "[統一編號] ") ;
        stringStopUseMessage  =  exeFun.getStopUseObjectCDMessage (hashtableCond,  exeUtil) ;
        if(!"TRUE".equals(stringStopUseMessage)) {
            messagebox(stringStopUseMessage) ;
            return  false ;
        }
        return  true ;
    }
    // A312105 回押租賃系統暫收款欄位
    public  boolean  isA312105BackRentSysOK(Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorRentSql)throws  Throwable {
        if(!"B3018".equals(getUser()))  return  true ;
        String    stringBarCode     =  getValue("BarCode").trim() ;
        Vector    vectorREAGTLG  =  exeUtil.getQueryDataHashtable("REAGTLG",  new  Hashtable(),  " AND  BarCode  =  '"+stringBarCode+"' ",  dbRent) ;
        if(vectorREAGTLG.size()  >  0) {
            // 已存在 REAGTLG 時，不作後續處理，但金額不一致時，mail 通知
            doA312105ErrorMail(exeUtil) ;
            return  true ;
        }
        // 判斷是否是 A312105 
        Vector    vectorDoc5M0224  =  exeUtil.getQueryDataHashtable("Doc5M0224",  new  Hashtable(),  " AND  BarCode  =  '"+stringBarCode+"' ",  dbDoc) ;
        if(vectorDoc5M0224.size()  ==  0)  return  true ;
        Vector    vectorDoc5M022  =  exeUtil.getQueryDataHashtable("Doc5M022",  new  Hashtable(),  " AND  BarCode  =  '"+stringBarCode+"'   AND  CostID  =  'A312101' ",  dbDoc) ;
        if(vectorDoc5M022.size()  ==  0)  return  true ;
        // 查詢 符合條件的合約，順序沖銷
        Hashtable  hashtableAnd                     =  new  Hashtable() ;
        Hashtable  hashtableData                      =  new  Hashtable() ;
        Hashtable  hashtableDoc5M0224               =  (Hashtable)vectorDoc5M0224.get(0) ;
        String      stringFactoryNo                   =  ""+hashtableDoc5M0224.get("FactoryNo") ;
        String      stringFirmNo                      =  exeUtil.getNameUnion("FIRM_NO",  "Doc5M020",  " AND  BarCode  =  '"+stringBarCode+"' ",  new  Hashtable(),  dbDoc) ; 
        String          stringDeptCd                    =  getDeptCdFED1012(hashtableDoc5M0224,  exeUtil) ;
        String          stringAggTemporary                =  "" ;
        String          stringAggTemporaryOrg           =  "" ;
        String          stringAggUuid                     =  "" ;
        String          stringSql                               =  "" ;
        String          stringAggId                             =  "" ;
        String          stringOwnId                           =  "" ;
        String          stringCasName                       =  "" ;
        String          stringActAgoId                        =  "" ;
        String      stringMailContent                   =  "" ;
        String[][]    retRentRemainSTemporary         = null ;
        double      doubleAmtSum                    =  getAmtDoc5M0224(vectorDoc5M0224,  exeUtil) ;
        double      doubleThisAmt                     =  0 ;
        double      doubleAggTemporary                     =  0 ;
        double      doubleAggTemporaryNet                  =  0 ;
        double      doubleAggTemporaryNetCF           =  0 ;
        
        retRentRemainSTemporary  =  getRentRemainSTemporary(stringFirmNo,  stringDeptCd,  stringFactoryNo,  exeUtil,  dbRent) ;
        // 請款準備沖銷金額 大於 合約暫收款可沖銷金額，不允許轉傳票
        // 0  agg_temporary     1  agg_temporary_org        2  agg_uuid       3  agg_id         4  own_id
        // 5  cas_name                6  act_ago_id
        for(int  intNo=0  ;  intNo<retRentRemainSTemporary.length  ;  intNo++) {
            stringAggTemporary              =  retRentRemainSTemporary[intNo][0].trim() ;         // 暫收款金額
            stringAggTemporaryOrg           =  retRentRemainSTemporary[intNo][1].trim() ;       // 原始-暫收款金額
            stringAggUuid                           =  retRentRemainSTemporary[intNo][2].trim() ;               // 合約 uuid
            stringAggId                                 =  retRentRemainSTemporary[intNo][3].trim() ;
            stringOwnId                               =  retRentRemainSTemporary[intNo][4].trim() ;
            stringCasName                           =  retRentRemainSTemporary[intNo][5].trim() ;
            stringActAgoId                          =  retRentRemainSTemporary[intNo][6].trim() ;
            doubleAggTemporary              =  exeUtil.doParseDouble(stringAggTemporary) ;
            // 金額判斷
            if(doubleAmtSum  <=  0)  break ;
            if(doubleAmtSum  >=  doubleAggTemporary) {
                  doubleThisAmt    =  doubleAggTemporary ;
                  doubleAmtSum  -=  doubleAggTemporary ;
            } else {
                  doubleThisAmt  =  doubleAmtSum ;
                  doubleAmtSum  =  0 ;
            }
            doubleAggTemporaryNet       =  doubleAggTemporary - doubleThisAmt ;
            doubleAggTemporaryNet       =  exeUtil.doParseDouble(convert.FourToFive(""+doubleAggTemporaryNet,  0)) ;
            doubleAggTemporaryNetCF    =  exeUtil.doParseDouble(stringAggTemporaryOrg)  -  getUseAmtReagTlg(stringAggUuid,  exeUtil)  - doubleThisAmt ;
            doubleAggTemporaryNetCF =  exeUtil.doParseDouble(convert.FourToFive(""+doubleAggTemporaryNetCF,  0)) ;
            if(doubleAggTemporaryNetCF  !=  doubleAggTemporaryNet) {
                messagebox("合約暫收款可沖銷金額 不一致，不允許轉傳票。") ;
                return  false ;
            }
            // 更新 rent_remain_temporary 暫收款金額
            stringSql  =  " UPDATE  rent_remain_temporary SET  agg_temporary  =  "+convert.FourToFive(""+doubleAggTemporaryNet,0)+" " +
                                " WHERE  agg_uuid  =  '"+stringAggUuid+"' " ;
            vectorRentSql.add(stringSql) ;
            stringMailContent  +=  stringSql+"<br>";
            // 新增 REAGTLG 記錄檔
            hashtableData.put("agg_uuid",                     stringAggUuid) ;
            hashtableData.put("agg_id",                           stringAggId) ;
            hashtableData.put("own_id",                         stringOwnId) ;
            hashtableData.put("cas_name",                     stringCasName) ;
            hashtableData.put("act_ago_id",                     stringActAgoId) ;
            hashtableData.put("agg_temporary",                stringAggTemporaryOrg) ;
            hashtableData.put("agg_temporary_reversal",         convert.FourToFive(""+doubleThisAmt,                  0)) ;
            hashtableData.put("agg_temporary_remainder",      convert.FourToFive(""+doubleAggTemporaryNet,  0)) ;
            hashtableData.put("barcode",                      stringBarCode) ;
            stringSql  =  exeUtil.doInsertDB("REAGTLG",  hashtableData,   false,  dbRent) ;vectorRentSql.add(stringSql) ;
            stringMailContent  +=  stringSql+"<br>";
        }
        if(doubleAmtSum  !=  0) {
            messagebox("請款準備沖銷金額 大於 合約暫收款可沖銷金額，不允許轉傳票。") ;
            return  false ;
        }
        doA312105Mail(stringMailContent,  exeUtil) ;
        return  true ;
    }
    public  String[][]  getRentRemainSTemporary(String  stringFirmNo,  String  stringDeptCd,  String  stringFactoryNo,  FargloryUtil  exeUtil,  talk  dbRent)throws  Throwable {
        String    stringSql       =  "" ;
        String[][]  retTableData  =  null ;
        // 0  agg_temporary     1  agg_temporary_org        2  agg_uuid       3  agg_id         4  own_id
        // 5  cas_name                6act_ago_id
        stringSql         =  " SELECT  agg_temporary,  agg_temporary_org,  agg_uuid,  agg_id,  own_id,  "  +
                                                  " cas_name,           act_ago_id "  +
                                     " FROM  rent_remain_temporary "  +
                       " WHERE  own_id  =  '"    +stringFirmNo     +"' "  +
                            " AND  cas_name  =  '"+stringDeptCd    +"' "  +
                        " AND  own_id  =  '"     +stringFactoryNo+"' "  +
                  " ORDER BY  own_id,  cas_name,  act_ago_id,  agg_id " ;
        retTableData  =  dbRent.queryFromPool(stringSql) ;
        return  retTableData ;
    }
    public  double  getUseAmtReagTlg(String  stringAggUuid,  FargloryUtil  exeUtil)throws  Throwable {
        String          stringAmt             =  "" ;
        Hashtable  hashtableReagTlg       =  new  Hashtable() ;
        Vector      vectorReagTlg           =  null ;
        double      doubleAmt               =  0 ;
        //
        vectorReagTlg  =  exeUtil.getQueryDataHashtable("REAGTLG",  new  Hashtable(),  " AND  agg_uuid  =  '"+stringAggUuid+"' ",  dbRent) ;
        for(int  intNo=0  ;  intNo<vectorReagTlg.size()  ;  intNo++) {
            hashtableReagTlg      =  (Hashtable)  vectorReagTlg.get(intNo) ;  if(hashtableReagTlg  ==  null)  continue ;
            stringAmt              =  ""+hashtableReagTlg.get("agg_temporary_reversal") ;
            //
            doubleAmt  +=  exeUtil.doParseDouble(stringAmt) ;
        }
        return  doubleAmt;
    }
    public  double  getAmtDoc5M0224(Vector  vectorDoc5M0224,  FargloryUtil  exeUtil)throws  Throwable {
        String          stringAmt             =  "" ;
        Hashtable  hashtableDoc5M0224   =  new  Hashtable() ;
        double      doubleAmt               =  0 ;
        //
        for(int  intNo=0  ;  intNo<vectorDoc5M0224.size()  ;  intNo++) {
            hashtableDoc5M0224  =  (Hashtable)  vectorDoc5M0224.get(intNo) ;  if(hashtableDoc5M0224  ==  null)  continue ;
            stringAmt              =  ""+hashtableDoc5M0224.get("Amt") ;
            //
            doubleAmt  +=  exeUtil.doParseDouble(stringAmt) ;
        }
        return  doubleAmt;
    }
    public  String  getDeptCdFED1012(Hashtable  hashtableDoc5M0224,  FargloryUtil  exeUtil)throws  Throwable {
        String      stringDeptCd            =  "" ;
        String      stringComNo             =  (""+getValueAt("Table1",  0,  "COMPANY_CD")).trim() ;
        String          stringVoucherYMD        =  ""+hashtableDoc5M0224.get("VOUCHER_YMD") ;
        String          stringVoucherFlowNo       =  ""+hashtableDoc5M0224.get("VOUCHER_FLOW_NO") ;
        String          stringVoucherSeqNo      =  ""+hashtableDoc5M0224.get("VOUCHER_SEQ_NO") ;
        Hashtable  hashtableAnd           =  new  Hashtable() ;
        //
        stringVoucherYMD  =  exeUtil.getDateConvertRoc(stringVoucherYMD).replaceAll("/",  "") ;
        //
        hashtableAnd.put("VOUCHER_YMD",         stringVoucherYMD) ;
        hashtableAnd.put("VOUCHER_FLOW_NO",  stringVoucherFlowNo) ;
        hashtableAnd.put("VOUCHER_SEQ_NO",    stringVoucherSeqNo) ;
        hashtableAnd.put("COMPANY_CD",        stringComNo) ;
        stringDeptCd  =  exeUtil.getNameUnion("DEPT_CD",  "FED1012",  "",  hashtableAnd,  dbFED1) ;
        return  stringDeptCd;
    }
    public  void  doA312105ErrorMail(FargloryUtil  exeUtil)throws  Throwable {
        // 金額不一致時，mail 通知
        String      stringBarCode       =  getValue("BarCode").trim() ;
        String      stringSql           =  " SELECT  SUM(Amt)  FROM  Doc5M0224  WHERE  BarCode  =  '"+stringBarCode+"' " ;
        String[][]    retDoc5M0224        =  dbDoc.queryFromPool(stringSql) ;
        String[][]    retReagTlg          =  null ;
        double        doubleDoc5M0224  =  exeUtil.doParseDouble(retDoc5M0224[0][0]) ;
        double        doubleReagTlg        =  0 ;
        //
        stringSql           =  " SELECT  SUM(agg_temporary_reversal)  FROM  REAGTLG  WHERE  BarCode  =  '"+stringBarCode+"' " ;
        retReagTlg          =  dbRent.queryFromPool(stringSql) ;
        doubleReagTlg        =  exeUtil.doParseDouble(retReagTlg[0][0]) ;
        //
        doubleReagTlg        =  exeUtil.doParseDouble(convert.FourToFive(""+doubleReagTlg,  0)) ;
        doubleDoc5M0224  =  exeUtil.doParseDouble(convert.FourToFive(""+doubleDoc5M0224,  0)) ;
        if(doubleReagTlg  ==  doubleDoc5M0224)  return ;
        //
        String      stringEDateTime       =  datetime.getTime("YYYY/mm/dd h:m:s") ;
        String      stringMailKEY         =  "A312105 回押租賃系統 錯誤通知" ;
        String      stringMailContent     =  "條碼編號("+stringBarCode+") 重複轉傳票，已存在租賃系統中，金額不一致。" ;
        Hashtable  hashtableData        =  new  Hashtable() ;
        hashtableData.put("EDateTime",      stringEDateTime) ;
        hashtableData.put("MailKEY",        stringMailKEY) ;
        hashtableData.put("EmployeeNo",    getUser()) ;
        hashtableData.put("MailTitle",        stringMailKEY) ;
        hashtableData.put("MailContent",    stringMailContent) ;
        hashtableData.put("MailUserID",       "B3018") ;
        exeUtil.doInsertDB("Doc7M004",  hashtableData,  true,  dbDoc) ;
        //
        hashtableData.put("EDateTime",      stringEDateTime) ;
        hashtableData.put("MailKEY",        stringMailKEY) ;
        hashtableData.put("EmployeeNo",    getUser()) ;
        hashtableData.put("MailTitle",        stringMailKEY) ;
        hashtableData.put("MailContent",    stringMailContent) ;
        hashtableData.put("MailUserID",       "B4039") ;
        exeUtil.doInsertDB("Doc7M004",  hashtableData,  true,  dbDoc) ;
    }
    public  void  doA312105Mail(String  stringMailContent,  FargloryUtil  exeUtil)throws  Throwable {
        String      stringEDateTime       =  datetime.getTime("YYYY/mm/dd h:m:s") ;
        String      stringMailKEY         =  "A312105 回押租賃系統 通知" ;
        Hashtable  hashtableData        =  new  Hashtable() ;
        hashtableData.put("EDateTime",      stringEDateTime) ;
        hashtableData.put("MailKEY",        stringMailKEY) ;
        hashtableData.put("EmployeeNo",    getUser()) ;
        hashtableData.put("MailTitle",        stringMailKEY) ;
        hashtableData.put("MailContent",    stringMailContent) ;
        hashtableData.put("MailUserID",       "B3018") ;
        exeUtil.doInsertDB("Doc7M004",  hashtableData,  true,  dbDoc) ;
    }
    
    
    
    
    public  boolean  doWriteVoucher(int  intVoucherSeqNo,  String  stringVoucherYMDRoc,  String  stringVoucherFlowNo,  String  stringUserN,  String[]  arrayDoc2M014,  String[][]  retWriteVouceherData,  Doc2M010  exeFun,  FargloryUtil  exeUtil,  Vector  vectorSql)throws  Throwable {
        String     stringDeptCd                    =  arrayDoc2M014[11].trim() ;
        String     stringKEY                         =  "" ;
        String     stringVoucherYmdL          =  "" ;
        String     stringVoucherFlowNoL     =  "" ;
        String     stringVoucherSeqNoL      =  "" ;
        String     stringMoney                     =  "" ;
        String     stringMoneyCF                 =  "" ;
        String     stringCompanyCd            =  arrayDoc2M014[7].trim( ) ;
        String     stringKind                        =  "0" ;
        String     stringSql                          =  "" ;
        String     stringSqlAnd                     =  "" ;
        String[]   arrayTemp                        =  null ;
        String[]  arrayWriteVouceherData   =  null ;
        String[][] retFED1013                      =  null ;
        //
        for(int  intNo=0  ;  intNo<retWriteVouceherData.length  ;  intNo++) {
              arrayWriteVouceherData    =  retWriteVouceherData[intNo] ;
              stringVoucherYmdL           =  arrayWriteVouceherData[0].trim() ;
              stringVoucherFlowNoL        =  arrayWriteVouceherData[1].trim() ;
              stringVoucherSeqNoL         =  arrayWriteVouceherData[2].trim() ;
              if(arrayWriteVouceherData.length  >  3) {
                  stringMoney                   =  arrayWriteVouceherData[3] ;
              } else {
                  stringMoney                   =  convert.FourToFive(arrayDoc2M014[13].trim(),  0) ;
              }
              //
            stringSqlAnd              =  " (I.VOUCHER_YMD  =  "          +  stringVoucherYmdL      +  "  AND  "  +
                                 " I.VOUCHER_FLOW_NO  =  "+  stringVoucherFlowNoL  +  "  AND "  +
                                 " I.VOUCHER_SEQ_NO  =  "   +  stringVoucherSeqNoL   +  " )" ;
            stringSqlAnd              =  " AND ("+  stringSqlAnd + ")" ;
            retFED1013              =  exeFun.getCanWriteAmtForFED1013(stringCompanyCd,   stringSqlAnd,  false) ;
            if(retFED1013.length  >  0) {
                stringMoneyCF       =  retFED1013[0][3].trim() ;
                stringMoneyCF       =  convert.FourToFive(stringMoneyCF,  0) ;
            }
            if(!stringMoneyCF.equals(stringMoney)) {
                messagebox("固資沖銷傳票 金額錯誤，請洽 資訊室。") ;
                return  false ;
            }
            //
            stringSql          =  exeFun.getUpdateFED1013Sql(stringMoney,                      stringVoucherYmdL,           stringVoucherFlowNoL,  
                                                    stringVoucherSeqNoL,       stringCompanyCd,            stringKind) ;
            vectorSql.add(stringSql) ;
            System.out.println("沖銷傳票(FED1013)---------"+stringSql) ;
            // 新增 FED1014
            stringSql  =  exeFun.getInsertFED1014Sql(stringVoucherYmdL,               stringVoucherFlowNoL,           stringVoucherSeqNoL,  
                                              stringCompanyCd,                 stringKind,                               stringVoucherYMDRoc,
                                              stringVoucherFlowNo,          ""+intVoucherSeqNo,               stringMoney,
                                               " ",                                      stringUserN) ;
            vectorSql.add(stringSql) ;
            System.out.println("沖銷傳票(FED1014)---------"+stringSql) ;
        }
        return  true ;
    }
    public  void  doSaveDoc2M0142 (Vector   vectorDocSql,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTable     jtable2                          =  getTable("Table2") ;  if(jtable2.getRowCount()  ==  0)  return ;
        String      stringDocNo                 =  "" ;
        String      stringAcceptanceYMD  =  "" ;
        String      stringLastUser              =  "" ;
        String      stringLastYMD              =  "" ;
        String      stringSql                       =  "" ;
        String      stringMessage              =  "" ;
        String      stringBarCode              =  getValue("BarCode").trim() ;
        // 儲存 Doc2M0142 表格及固資相關表格資料。
        for(int   intNo=0  ;  intNo<jtable2.getRowCount()  ;  intNo++) {
            stringDocNo                  =  (""+getValueAt("Table2",  intNo,  "DocNo")).trim() ;
            stringAcceptanceYMD  =  (""+getValueAt("Table2",  intNo,  "AcceptanceYMD")).trim() ;
            stringLastUser              =  (""+getValueAt("Table2",  intNo,  "LAST_USER")).trim() ;
            stringLastYMD             =  (""+getValueAt("Table2",  intNo,  "LAST_YMD")).trim() ;
            //
            stringSql  =  " INSERT  INTO  Doc2M0142 ( BarCode ,                  DocNo ,                  AcceptanceYMD ,                 LAST_USER ,              LAST_YMD ) VALUES "  +
                                                                    " ( '"+stringBarCode+"' , '"+stringDocNo+"' , '"+stringAcceptanceYMD+"' ,   '"+stringLastUser+"' , '"+stringLastYMD+"' ) "  ;
            stringMessage  +=  "固資 驗收日期 維護(Doc2I0142) \n"+  stringSql  + "\n\n" ;
            vectorDocSql.add(stringSql) ;
            //
        }
        // 更新 固資 驗收日期     
        talk           dbAsset                          =  getTalk(""+get("put_Asset")) ;
        String[]    arrayPurchaseData        =  getPurchaseData(stringBarCode,  exeFun) ;
        Vector     vectorAssetSql               =  new  Vector() ;
        // 0 公司     1  類別       2  請購單號     3  廠商       4 來源    5 請款類別
        stringAcceptanceYMD  =  stringAcceptanceYMD.replaceAll("/",  "") ;
         // AS_RECEIPT
        stringSql        =  " UPDATE    AS_RECEIPT "  +
                                         " SET  RCT_UTM_DATE  =  '"+stringAcceptanceYMD+"', "  +
                                   " RCT_LAST_USER  =  '"+stringLastUser           +"', "  +
                               " RCT_LAST_DATE  =   getDate() "                   +  // 2012-11-12 17:44:45.103
                      " WHERE  RCT_COMP_ID  =  '"+arrayPurchaseData[0].trim()  +"' "  +
                         " AND  PUR_ID  =  '"            +arrayPurchaseData[2].trim()   +"' "  +
                       //" AND             =  '"                 +arrayPurchaseData[1].trim()   +"' "  + // 暫時 KindNo 不作查詢限制
                         " AND  SUP_ID  =  '"            +arrayPurchaseData[3].trim()   +"' "  ;
        vectorAssetSql.add(stringSql) ;
        stringMessage  +=  "固資 驗收日期 維護( AS_RECEIPT)\n"+stringSql+"\n\n" ;
        
        // AS_ASSET
        stringSql        =  " UPDATE    AS_ASSET "  +
                                         " SET  ASS_PUR_DATE  =  '"  +stringAcceptanceYMD+"', "  +
                                   "  ASS_LAST_USER  =  '"+stringLastUser           +"', "  +
                               "  ASS_LAST_DATE  =  getDate() "                              +  // 2012-11-12 17:44:45.103
                      " WHERE  RCT_ID IN (SELECT  RCT_GUID  " +
                                                          " FROM  AS_RECEIPT "  +
                                      " WHERE  RCT_COMP_ID  =  '"+arrayPurchaseData[0].trim()  +"' "  +
                                          " AND  PUR_ID  =  '"            +arrayPurchaseData[2].trim()   +"' "  +
                                         //" AND             =  '"                +arrayPurchaseData[1].trim()   +"' "  + // 暫時 KindNo 不作查詢限制
                                          " AND  SUP_ID  =  '"            +arrayPurchaseData[3].trim()   +"') "  ;
        vectorAssetSql.add(stringSql) ;
        stringMessage  +=  "固資 驗收日期 維護(AS_ASSET)\n"+stringSql ;
        //
        if(vectorAssetSql.size()  >  0)  {
            doMail (stringMessage,  exeUtil) ;
            dbAsset.execFromPool((String[])  vectorAssetSql.toArray(new  String[0])) ;
        }
    }
    public  void  doMail (String   stringContent,  FargloryUtil  exeUtil) throws  Throwable {
        String    stringSubject   =  "請款系統之固資驗收日期更新" ;
        String    stringSend      =  "emaker@farglory.com.tw" ;
        String[]  arrayUser        =  {"B3018@farglory.com.tw",  "B4039@farglory.com.tw"} ;
        //
        stringContent  =  stringContent.replaceAll("\n",  "<br>") ;
        //
        exeUtil.doEMail(stringSubject,  stringContent,  stringSend,  arrayUser) ;
    }
    public  boolean  isAssetOK (boolean  booleanSource,  String   stringBarCode,  FargloryUtil  exeUtil,  Doc2M010  exeFun,  Hashtable  hashtableAsset) throws  Throwable {
        // 固資資料判斷
        talk           dbAsset                          =  getTalk(""+get("put_Asset")) ;
        String      stringAssetDate              =  ""+get("ASSET_DATE"); 
        String      stringToday                    =  datetime.getToday("YYYY/mm/dd") ;
        String[]    arrayPurchaseData        =  getPurchaseData(stringBarCode,  exeFun) ;// 0 公司    1  類別       2  請購單號     3  廠商       4 來源    5 請款類別
        //
        if(",K01241,K01261,".indexOf(stringBarCode)  !=  -1)          return  true ;
        if(arrayPurchaseData   ==  null)                                             return  true ;
        if(!isApplyTypeD(arrayPurchaseData,  exeFun))                   return  true ;
        // 檢核
        JTable     jtable2                          =  getTable("Table2") ;
        String      stringDocNo                 =  "" ;
        String      stringAcceptanceYMD  =  "" ;
        String      stringLastUser              =  "" ;
        String      stringLastYMD              =  "" ;
        String      stringSql                       =  "" ;
        String[][]  retAsAsset                    =  getAsAsset(arrayPurchaseData,  exeFun,  dbAsset) ;
        boolean  booleasnLastDoc          =  exeFun.isLastDocVoucher(booleanSource,  stringBarCode,  arrayPurchaseData[0].trim(),  arrayPurchaseData[5].trim(),  arrayPurchaseData[2].trim(),  arrayPurchaseData[3].trim()) ;
        //
        String    stringFiletrDo           =  exeUtil.getNameUnion("Remark",  "Doc2M0401",  " AND  UseType  =  'U' ",  new  Hashtable(),  dbDoc) ;
        //
        if(retAsAsset.length  ==  0)      return  true ;
        if(!booleasnLastDoc)               return  true ;
        // 未輸入驗收資料時，自動彈出視窗。
        if(jtable2.getRowCount()  ==  0)  getButton("ButtonAsset").doClick() ;
        if(jtable2.getRowCount()  ==  0)  {
            messagebox("請輸入 驗收日期 資料") ;
            return  false ;
        }
        // 固資 vs 請購金額一致檢核。
        if(!isSameAsAsset(stringFiletrDo,  arrayPurchaseData,  retAsAsset,  exeUtil,  exeFun))  return  false ;
        // 財會-沖銷金額 檢核
        if(!isVoucherCheckOK (stringBarCode,  arrayPurchaseData,  exeUtil,  exeFun,  hashtableAsset) &&  "B67283".indexOf(stringBarCode)==-1)  return  false ;
        hashtableAsset.put("STATUS",  "OK") ;
        return  true ;
    }
    public  boolean  isVoucherCheckOK ( String  stringBarCode,  String[]  arrayPurchaseData,  FargloryUtil  exeUtil,  Doc2M010  exeFun,  Hashtable  hashtableAsset) throws  Throwable {
        String          stringComNo                                        =  arrayPurchaseData[0].trim() ;
        String          stringKindNoPurchase                          =  arrayPurchaseData[1].trim() ;
        String          stringKindNo                                        =  stringKindNoPurchase ;
        String          stringPurchaseNo                                =  arrayPurchaseData[2].trim() ;
        String          stringFactoryNo                                    =  arrayPurchaseData[3].trim() ;
        String          stringTYPE                                           =  arrayPurchaseData[4].trim() ;
        String          stringTYPE2                                         =  "" ;
        String          stringTable10                                       =  "" ;
        String          stringTable171                                     =  "" ;
        String          stringTable60                                        =  "" ;
        String          stringTable671                                     =  "" ;
        String          stringSql                                               =  "" ;
        String          stringBarCodeL                                    =  "" ;
        String[][]      retDoc2M0171                                     =  null ;
        String[][]      retDoc6M0171                                     =  null ;
        Vector         vectorBarCode                                    =  new  Vector() ;
        Hashtable   hashtableTYPE                                    =  new  Hashtable() ;
        if("15".equals(stringKindNo))                 stringKindNo                 =  "23" ;
        if("17".equals(stringKindNo))                stringKindNo                  =  "24" ;
        if("23".equals(stringKindNoPurchase))  stringKindNoPurchase  =  "15" ;
        if("24".equals(stringKindNoPurchase))  stringKindNoPurchase  =  "17" ;
        if("A".equals(stringTYPE)) {
            stringTable171  =  "Doc2M0171" ;
            stringTable10    =  "Doc2M010" ;
            stringTable671  =  "Doc6M0171" ;
            stringTable60    =  "Doc6M010" ;
        } else {
            stringTable171  =  "Doc5M0272" ;
            stringTable10   =  "Doc5M020" ;
            stringTable671  =  "" ;
            stringTable60     =  "" ;
        }
        // 取得 該請購單 前期之請款單
        // 請款金額- 請款
        stringSql  =  "SELECT  DISTINCT  M17.BarCode "  +
                       " FROM  "+stringTable171+" M17,  "+stringTable10+"  M10 "  +
                      " WHERE  M17.BarCode  =  M10.BarCode "  +
                        " AND  M10.BarCode  <>  '"    +  stringBarCode       +  "' "  +
                      " AND  M10.ComNo  =  '"         +  stringComNo          +  "' "  +
                        " AND  M17.PurchaseNo  =  '"+  stringPurchaseNo  +  "' "  +
                        " AND  M10.KindNo  =  '"         +  stringKindNo          +  "' "  +
                      " AND  M17.FactoryNo  =  '"    +  stringFactoryNo     +  "' " +
                 " ORDER BY  M17.BarCode ";
        retDoc2M0171                 =  exeFun.getTableDataDoc(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc2M0171.length  ;  intNo++) {
            stringBarCodeL                      =  retDoc2M0171[intNo][0].trim() ;
            //
            if(vectorBarCode.indexOf(stringBarCodeL)  ==  -1)  vectorBarCode.add(stringBarCodeL) ;
            //
            hashtableTYPE.put(stringBarCodeL,  "A") ;
        }
        // 請款金額- 借款沖銷
        if("A".equals(stringTYPE)) {
            stringSql  =  "SELECT  DISTINCT  M17.BarCode "  +
                         " FROM  "+stringTable671+" M17,  "+stringTable60+"  M10 "  +
                        " WHERE  M17.BarCode  =  M10.BarCode "  +
                          " AND  M10.BarCode  <>  '"   +  stringBarCode         +  "' "  +
                          " AND  M10.ComNo  =  '"        +  stringComNo           +  "' "  +
                          " AND  M10.KindNo  =  '"        +  stringKindNo           +  "' "  +
                          " AND  M10.PurchaseNo  =  '"+  stringPurchaseNo  +  "' "  +
                          " AND  M17.FactoryNo  =  '"    +  stringFactoryNo     +  "' "  +
                     " ORDER BY  M17.BarCode ";
            retDoc6M0171           =  exeFun.getTableDataDoc(stringSql) ;
            for(int  intNo=0  ;  intNo<retDoc6M0171.length  ;  intNo++) {
                stringBarCodeL                  =  retDoc6M0171[intNo][0].trim() ;
                //
                if(vectorBarCode.indexOf(stringBarCodeL)  ==  -1)  vectorBarCode.add(stringBarCodeL) ;
                //
                hashtableTYPE.put(stringBarCodeL,  "B") ;
            }
        }
        if(vectorBarCode.size()  ==  0)  return  true ;
        //
        String         stringRecordNo12                =  "" ;
        String         stringMoney                         =  "" ;
        String         stringDeptCd                       =  "" ;
        String         stringVoucherYmd               =  "" ;
        String         stringVoucherFlowNo           =  "" ;
        String         stringVoucherSeqNo            =  "" ;
        String         stringKind                            =  "0" ;
        String         stringSqlAnd                        =  "" ;
        String         stringMessage                     =  "" ;
        String         stringTemp                          =  "" ;
        String         stringKEY                            =  "" ;
        String[][]     retDoc2M014                      =  null ;
        String[][]     retFED1012                        =  null ;
        String[][]     retFED1013                        =  null ;
        double       doubleMoneySumDoc         =  0 ;
        double       doubleMoneySumVoucher   =  0 ;
        double       doubleTemp                         =  0 ;
        double       doubleMoneyDoc                 =  0 ;
        double       doubleMoneyVoucher         =  0 ;
        Vector        vectorDeptCdDoc                =  new  Vector() ;
        Vector        vectorDeptCdVoucher         =  new  Vector() ;
        Vector        vectorVoucherKEY              =  new  Vector() ;
        Hashtable  hahstableMoneyDoc            =  new  Hashtable() ;
        Hashtable  hahstableMoneyVoucher     =  new  Hashtable() ;
        Hashtable  hashtableDeptCd                =  new  Hashtable() ;
        for(int  intNo=0  ;  intNo<vectorBarCode.size()  ;  intNo++) {
            stringBarCodeL                  =  (""+vectorBarCode.get(intNo)) ;
            stringTYPE2                       =  ""+hashtableTYPE.get(stringBarCodeL) ;
            //
            vectorDeptCdDoc                =  new  Vector() ;
            vectorDeptCdVoucher         =  new  Vector() ;
            hahstableMoneyDoc            =  new  Hashtable() ;
            hahstableMoneyVoucher     =  new  Hashtable() ;
            //
            if("A".equals(stringTYPE)) {
                // 行銷
                stringTable171  =  ("A".equals(stringTYPE2)) ? "Doc2M0171" : "Doc6M0171" ;
            } else {
                // 管理費用
                stringTable171  =  ("A".equals(stringTYPE2)) ?  "Doc5M0272" : "" ;
            }
            // 整理 請款單之請購項目  ，結果為 部門->金額
            stringSql  =  "SELECT  RecordNo12,  SUM(PurchaseNoTaxMoney) "  +
                         " FROM  "+stringTable171+"   "  +
                       " WHERE  1=1 "  +
                          " AND  BarCode  =  '"  +  stringBarCodeL       +  "' "+
                   " GROUP  BY  RecordNo12 "  ;
            retDoc6M0171           =  exeFun.getTableDataDoc(stringSql) ;
            for(int  intNoL=0  ;  intNoL<retDoc6M0171.length  ;  intNoL++) {
                stringRecordNo12          =  retDoc6M0171[intNoL][0].trim() ;
                stringMoney                   =  retDoc6M0171[intNoL][1].trim() ;
                doubleMoneySumDoc  +=  exeUtil.doParseDouble(stringMoney) ;
                //
                stringDeptCd  =  exeFun.getDeptCdVoucher(stringTYPE,  stringRecordNo12,  stringComNo,  stringKindNoPurchase,  stringPurchaseNo,  exeUtil) ;
                //
                if(vectorDeptCdDoc.indexOf(stringDeptCd)  ==  -1)  vectorDeptCdDoc.add(stringDeptCd) ;
                //
                doubleTemp  =  exeUtil.doParseDouble(""+hahstableMoneyDoc.get(stringDeptCd))  +  exeUtil.doParseDouble(stringMoney) ;
                hahstableMoneyDoc.put(stringDeptCd,  convert.FourToFive(""+doubleTemp,  0)) ;
            }
            // 整理 請款單之傳票 1252，結果為 部門->可沖銷金額
            // Doc2M014
            retDoc2M014               =  exeFun.getDoc2M014(stringBarCodeL) ;
            if(retDoc2M014.length  ==  0) {
                messagebox("前期請款單("+stringBarCodeL+") 尚未轉傳票。") ;
                return  false ;
            }
            if(!"Z".equals(retDoc2M014[0][17])) {
                messagebox("前期請款單("+stringBarCodeL+") 尚未轉傳票。") ;
                return  false ;
            }
            stringVoucherYmd       =  exeUtil.getDateConvertRoc(retDoc2M014[0][4].trim()).replaceAll("/",  "") ;
            stringVoucherFlowNo  =  retDoc2M014[0][5].trim() ;
            stringComNo               =  retDoc2M014[0][7].trim() ;
            // 由 FED1012 限制 公司、傳票日期、傳票流水號 及 會計科目 1252
            retFED1012  =  exeFun.getFED1012ForFED1013(stringVoucherYmd,  stringVoucherFlowNo,  stringComNo,  stringKind,  "1252") ;
            if(retFED1012.length  ==  0) {
                messagebox("前期請款單("+stringBarCodeL+") 找不到對應傳票之會計科目 1252 分錄。") ;
                return  false ;               
            }
            for(int  intNoL=0  ;  intNoL<retFED1012.length  ;  intNoL++) {
                stringVoucherSeqNo  =  retFED1012[intNoL][6].trim() ;
                //
                if(!stringFactoryNo.equals(retFED1012[intNoL][12].trim())) {
                    messagebox("前期請款單("+stringBarCode+") 對應傳票之廠商("+retFED1012[intNoL][12].trim()+") 不一致。") ;
                    return  false ;
                }
                //
                stringSqlAnd              =  " (I.VOUCHER_YMD  =  "          +  stringVoucherYmd      +  "  AND  "  +
                                         " I.VOUCHER_FLOW_NO  =  "+  stringVoucherFlowNo  +  "  AND "  +
                                       " I.VOUCHER_SEQ_NO  =  "   +  stringVoucherSeqNo   +  " )" ;
                stringSqlAnd              =  " AND ("+  stringSqlAnd + ")" ;
                retFED1013              =  exeFun.getCanWriteAmtForFED1013(stringComNo,   stringSqlAnd,  false) ;
                //
                stringMoney     =  retFED1013[0][3].trim() ;
                stringDeptCd   =  retFED1013[0][4].trim() ;
                //
                doubleMoneySumVoucher  +=  exeUtil.doParseDouble(stringMoney) ;
                //
                if(vectorDeptCdVoucher.indexOf(stringDeptCd)  ==  -1)  vectorDeptCdVoucher.add(stringDeptCd) ;
                //
                doubleTemp  =  exeUtil.doParseDouble(""+hahstableMoneyVoucher.get(stringDeptCd))  +  exeUtil.doParseDouble(stringMoney) ;
                hahstableMoneyVoucher.put(stringDeptCd,  convert.FourToFive(""+doubleTemp,  0)) ;
                //
                // 回傳 部門-[傳票日期%-%流水號%-%序號%-%金額]
                vectorVoucherKEY  =  (Vector) hashtableAsset.get(stringDeptCd) ;
                stringKEY                =  stringVoucherYmd+"%-%"+stringVoucherFlowNo+"%-%"+stringVoucherSeqNo+"%-%"+stringMoney ;
                if(vectorVoucherKEY  ==  null) {
                    vectorVoucherKEY  =  new  Vector() ;
                    hashtableAsset.put(stringDeptCd,  vectorVoucherKEY) ;
                }
                if(vectorVoucherKEY.indexOf(stringKEY)  ==  -1) {
                    vectorVoucherKEY.add(stringKEY) ;
                }
            }
        }
        // 部門一致檢核
        if(vectorDeptCdDoc.size()  !=  vectorDeptCdVoucher.size()) {
            stringDeptCd  =  "" ;
            for(int  intNo=0  ;  intNo<vectorDeptCdDoc.size()  ;  intNo++) {
                stringTemp  =  ""+vectorDeptCdDoc.get(intNo) ;
                if(!"".equals(stringDeptCd))  stringDeptCd  +=  "、" ;
                stringDeptCd  +=  stringTemp ;
            }
            stringMessage  +=  "請款部門("+stringDeptCd+") " ;
            stringDeptCd      =  "" ;
            for(int  intNo=0  ;  intNo<vectorDeptCdVoucher.size()  ;  intNo++) {
                stringTemp  =  ""+vectorDeptCdVoucher.get(intNo) ;
                if(!"".equals(stringDeptCd))  stringDeptCd  +=  "、" ;
                stringDeptCd  +=  stringTemp ;
            }
            stringMessage  +=  "與 財會系統部門("+stringDeptCd+") 不一致。" ;
        }
        for(int  intNo=0  ;  intNo<vectorDeptCdVoucher.size()  ;  intNo++) {
            stringTemp  =  ""+vectorDeptCdDoc.get(intNo) ;
            if(vectorDeptCdDoc.indexOf(stringTemp)  ==  -1) {
                if(!"".equals(stringMessage))  stringMessage  +=  "\n" ;
                stringMessage  +=  "請款系統 缺少部門 "  + stringTemp+"。" ;
            }
        }
        for(int  intNo=0  ;  intNo<vectorDeptCdDoc.size()  ;  intNo++) {
            stringTemp  =  ""+vectorDeptCdDoc.get(intNo) ;
            if(vectorDeptCdVoucher.indexOf(stringTemp)  ==  -1) {
                if(!"".equals(stringMessage))  stringMessage  +=  "\n" ;
                stringMessage  +=  "財會系統 缺少部門 "  + stringTemp+"。" ;
                continue ;
            }
            //
            doubleMoneyDoc         =  exeUtil.doParseDouble(""+hahstableMoneyDoc.get(stringDeptCd)) ;
            doubleMoneyVoucher  =  exeUtil.doParseDouble(""+hahstableMoneyVoucher.get(stringDeptCd)) ;
            if(doubleMoneyDoc  !=  doubleMoneyVoucher) {
                stringMessage  =  "部門 "+stringTemp+" 之請款系統前期合計金額("+exeUtil.getFormatNum2 (""+doubleMoneyDoc)+") 與 財會系統前期合計金額("+exeUtil.getFormatNum2 (""+doubleMoneyVoucher)+") 不一致。" ;
            }
        }
        // 總金額一致
        doubleMoneySumDoc         =  exeUtil.doParseDouble(convert.FourToFive(""+doubleMoneySumDoc,         0)) ;
        doubleMoneySumVoucher  =  exeUtil.doParseDouble(convert.FourToFive(""+doubleMoneySumVoucher,  0)) ;
        if(doubleMoneySumDoc  !=  doubleMoneySumVoucher) {
            messagebox("請款系統前期合計金額("+exeUtil.getFormatNum2 (""+doubleMoneySumDoc)+") 與 財會系統前期合計金額("+exeUtil.getFormatNum2 (""+doubleMoneySumVoucher)+") 不一致。") ;
            return  false ;
        }
        if(!"".equals(stringMessage)) {
            messagebox(stringMessage) ;
            return  false ;
        }
        return  true ;
    }
    public  String  getDeptCd(String  stringRecordNo12,  String[]  arrayPurchaseData,  FargloryUtil  exeUtil,  Doc2M010  exeFun,  Hashtable  hashtableDeptCd) throws  Throwable {
        String          stringComNo                                        =  arrayPurchaseData[0].trim() ;
        String          stringKindNo                                        =  arrayPurchaseData[1].trim() ;
        String          stringPurchaseNo                                =  arrayPurchaseData[2].trim() ;
        String          stringFactoryNo                                    =  arrayPurchaseData[3].trim() ;
        String          stringTYPE                                           =  arrayPurchaseData[4].trim() ;
        String          stringDeptCd                                        =  "" ;
        String          stringSql                                               =  "" ;
        String          stringInOut                                           =  "" ;
        String          stringDepartNo                                    =  "" ;
        String          stringProjectID                                     =  "" ;
        String          stringProjectID1                                    =  "" ;
        String[]        arrayTemp                                           =  null ;
        String[][]      retDoc3M012                                        =  null ;
        String[][]      retDoc3M0123                                      =  null ;
        String[][]      retDoc5M0121                                      =  null ;
        //
        stringDeptCd  =  ""+hashtableDeptCd.get(stringRecordNo12) ;
        if(!"null".equals(stringDeptCd))  return  stringDeptCd ;
        //
        if("23".equals(stringKindNo))  stringKindNo  =  "15" ;
        if("24".equals(stringKindNo))  stringKindNo  =  "17" ;
        //
        if("A".equals(stringTYPE)) {
            // 行銷
            stringSql  =  " SELECT M12.InOut,  M12.DepartNo,  M12.ProjectID,  M12.ProjectID1 "  +
                                  " FROM  Doc3M011  M11,  Doc3M0123  M12 "  +
                      " WHERE M11.BarCode  =  M12.BarCode "  +
                            " AND  M11.ComNo  =  '"    +stringComNo        +"' "  +
                          " AND  M11.KindNo  =  '"    +stringKindNo         +"' "  +
                          " AND  M11.DocNo  =  '"     +stringPurchaseNo +"' "  +
                          " AND  M12.RecordNo  =  '"+stringRecordNo12+"' "  ;
            retDoc3M0123  =  exeFun.getTableDataDoc(stringSql) ;
            if(retDoc3M0123.length  >  0) {
                stringInOut          =  retDoc3M0123[0][0].trim() ;
                stringDepartNo    =  retDoc3M0123[0][1].trim() ;
                stringProjectID    =  retDoc3M0123[0][2].trim() ;
                stringProjectID1  =  retDoc3M0123[0][3].trim() ;
            } else {
                stringSql  =  " SELECT  M12.ProjectID1 "  +
                            " FROM  Doc3M011  M11,  Doc3M012  M12 "  +
                          " WHERE M11.BarCode  =  M12.BarCode "  +
                              " AND  M11.ComNo  =  '"    +stringComNo        +"' "  +
                              " AND  M11.KindNo  =  '"    +stringKindNo         +"' "  +
                              " AND  M11.DocNo  =  '"     +stringPurchaseNo +"' "  +
                              " AND  M12.RecordNo  =  '"+stringRecordNo12+"' "  ;
                retDoc3M012     =  exeFun.getTableDataDoc(stringSql) ;
                stringProjectID1  =  retDoc3M012[0][0].trim() ;
                stringProjectID1  =  convert.StringToken(stringProjectID1,  "%-%")[0] ;
                arrayTemp          =  convert.StringToken(stringProjectID1,  ",") ;
                if(arrayTemp.length  >  4) {
                  stringInOut          =  arrayTemp[0].trim() ;
                  stringDepartNo    =  arrayTemp[1].trim() ;
                  stringProjectID    =  arrayTemp[2].trim() ;
                  stringProjectID1  =  arrayTemp[3].trim() ;  
                }
            }
            stringDeptCd  =  exeFun.getVoucherDepartNo(stringInOut,  stringDepartNo,  stringProjectID,  stringProjectID1,  exeUtil) ;
        } else {
            String                stringComNoType                     =  exeFun.getComNoType(stringComNo) ;  
            String                stringCostID                              =  "" ;
            String                stringAccountNo                        =  "" ;
            String[][]            retDoc5M012                            =  null ;
            String[][]            retDoc7M0552                          =  null ;
            Hashtable         hashtableInOut                         =  new  Hashtable() ;
            // 管理費用
            stringSql  =  " SELECT M12.InOut,  M12.DepartNo,  M12.ProjectID1,  M12.BarCode "  +
                                  " FROM  Doc5M011  M11,  Doc5M0121  M12 "  +
                      " WHERE M11.BarCode  =  M12.BarCode "  +
                            " AND  M11.ComNo  =  '"    +stringComNo        +"' "  +
                          " AND  M11.ComNo  =  '"    +stringKindNo         +"' "  +
                          " AND  M11.DocNo  =  '"     +stringPurchaseNo +"' "  +
                          " AND  M12.RecordNo  =  '"+stringRecordNo12+"' "  ;
            retDoc5M0121  =  exeFun.getTableDataDoc(stringSql) ;
            if(retDoc5M0121.length ==  0) return  "" ;
            //
            stringInOut          =  retDoc3M0123[0][0].trim() ;
            stringDepartNo    =  retDoc3M0123[0][1].trim() ;
            stringProjectID1  =  retDoc3M0123[0][2].trim() ;
            //
            stringSql  =  " SELECT CostID "  +
                        " FROM  Doc5M012 "  +
                      " WHERE  BarCode  =  '"+retDoc3M0123[0][3].trim() +"' "  +
                          " AND  RecordNo  =  '"+stringRecordNo12          +"' "  ;
            retDoc5M012    =  exeFun.getTableDataDoc(stringSql) ;
            stringCostID      =  retDoc5M012[0][0].trim() ;
            retDoc7M0552  =  exeFun.getDoc7M0552(stringCostID,  stringComNoType,  stringInOut,  "")  ;
            if(retDoc7M0552.length  ==  0)  return  "" ;
            stringAccountNo  =  retDoc7M0552[0][3].trim() ;
            //
            // 取得 Doc5M0121 RecordNo 之 InOut、DepartNo、ProjectID1
            stringDeptCd  =  exeFun.getVoucherDepartNoDoc5(stringComNo,       stringComNoType,  stringInOut,       stringDepartNo,  stringProjectID1,  
                                                              stringProjectID1,  stringAccountNo,     hashtableInOut,    exeUtil) ;
        }
        hashtableDeptCd.put(stringRecordNo12,  stringDeptCd) ;
        return  stringDeptCd ;
    }
    // 0 公司     1  類別       2  請購單號     3  廠商       4 來源
    public  boolean  isSameAsAsset(String  stringFiletrDo,  String[]  arrayPurchaseData,  String[][]  retAsAsset,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String           stringBarCode                                     =  getValue("BarCode").trim() ;  if("B67283".indexOf(stringBarCode)!=-1)  return  true ;
        String          stringComNo                                        =  arrayPurchaseData[0].trim() ;
        String          stringKindNoPurchase                          =  arrayPurchaseData[1].trim() ;
        String          stringPurchaseNo                                =  arrayPurchaseData[2].trim() ;
        String          stringFactoryNo                                    =  arrayPurchaseData[3].trim() ;
        String          stringTYPE                                           =  arrayPurchaseData[4].trim() ;
        String          stringSql                                               =  "" ;
        String          stringTable10                                       =  "" ;
        String          stringTable11                                       =  "" ;
        String          stringTable12                                       =  "" ;
        String          stringTable171                                     =  "" ;
        String          stringTable671                                     =  "" ;
        String          stringTable60                                        =  "" ;
        String          stringFilter                                            =  "" ;
        String          stringKindNo                                          =  stringKindNoPurchase ;
        String          stringRecordNo12                                 =  "" ;
        String          stringRecordNo12Sum                           =  "" ;
        String          stringPurchaseMoney                            =  "" ;
        String          stringPurchaseNoTaxMoney                  =  "" ;
        String[][]      retDoc3M012                                        =  new  String[0][0] ;
        String[][]      retDoc2M0171                                      =  new  String[0][0] ;
        String[][]      retDoc6M0171                                      =  new  String[0][0] ;
        Hashtable   hashtableRecordNo12                          =  new  Hashtable() ;
        double         doublePurchaseMoney                        =  0 ;
        double         doublePurchaseMoneyDoc                  =  0 ;
        double        doublePurchaseMoneySum                  =  0 ;
        double        doublePurchaseMoneySumDoc            =  0 ;
        double         doublePurchaseNoTaxMoney              =  0 ;
        double         doublePurchaseNoTaxMoneyDoc        =  0 ;
        double        doublePurchaseNoTaxMoneySum        =  0 ;
        double        doublePurchaseNoTaxMoneySumDoc  =  0 ;
        //
        if("15".equals(stringKindNo))                   stringKindNo                 =  "23" ;
        if("17".equals(stringKindNo))                   stringKindNo                 =  "24" ;
        if("23".equals(stringKindNoPurchase))    stringKindNoPurchase  =  "15" ;
        if("24".equals(stringKindNoPurchase))    stringKindNoPurchase  =  "17" ;
        //
        if("A".equals(stringTYPE)) {
            stringTable11    =  "Doc3M011" ;
            stringTable12    =  "Doc3M012" ;
            stringTable171  =  "Doc2M0171" ;
            stringTable10    =  "Doc2M010" ;
            stringTable671  =  "Doc6M0171" ;
            stringTable60    =  "Doc6M010" ;
        } else {
            stringTable11    =  "Doc5M011" ;
            stringTable12    =  "Doc5M012" ;
            stringTable171  =  "Doc5M0272" ;
            stringTable10   =  "Doc5M020" ;
            stringTable671  =  "" ;
            stringTable60     =  "" ;
        }
        // 請購單之固資項次
        stringSql  =  " SELECT  M12.FILTER,  M12.RecordNo "  +
                        " FROM  "+stringTable11+" M11,  "+stringTable12+" M12 "+
                       " WHERE  M11.BarCode  =  M12.BarCode "  +
                         " AND  M11.ComNo  =  '"       +stringComNo               +"' "  +
                         " AND  M11.DocNo  =  '"       +stringPurchaseNo         +"' "  +
                         " AND  M11.KindNo  =  '"      +stringKindNoPurchase  +"' "  ;
        retDoc3M012  =  exeFun.getTableDataDoc(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc3M012.length  ;  intNo++) {
            stringFilter                =  retDoc3M012[intNo][0].trim() ;
            stringRecordNo12     =  retDoc3M012[intNo][1].trim() ;
            //
            if(stringFiletrDo.equals(stringFilter))  stringFilter  =  "OTHER" ;
            //
            stringRecordNo12Sum    =  ""+hashtableRecordNo12.get(stringFilter) ;  if("null".equals(stringRecordNo12Sum))  stringRecordNo12Sum  =  "" ;
            stringRecordNo12Sum   +=  ","+stringRecordNo12+"," ;
            hashtableRecordNo12.put(stringFilter,  stringRecordNo12Sum) ;
            System.out.println(intNo+"stringFilter("+stringFilter+")stringRecordNo12Sum("+stringRecordNo12Sum+")-----------------------") ;
        }
        // 請款金額- 請款
        stringSql  =  "SELECT  M17.RecordNo12,  SUM(M17.PurchaseMoney),  SUM(M17.PurchaseNoTaxMoney) "  +
                       " FROM  "+stringTable171+" M17,  "+stringTable10+"  M10 "  +
                      " WHERE  M17.BarCode  =  M10.BarCode "  +
                        " AND  M10.ComNo  =  '"        +  stringComNo           +  "' "  +
                        " AND  M17.PurchaseNo  =  '"+  stringPurchaseNo  +  "' "  +
                        " AND  M10.KindNo  =  '"         +  stringKindNo          +  "' "  +
                      " AND  M17.FactoryNo  =  '"    +stringFactoryNo       +"' " +
                " GROUP BY  M17.RecordNo12 "  ;
        retDoc2M0171                 =  exeFun.getTableDataDoc(stringSql) ;
        stringRecordNo12Sum    =  ""+hashtableRecordNo12.get("OTHER") ;  if("null".equals(stringRecordNo12Sum))  stringRecordNo12Sum  =  "" ;
        for(int  intNo=0  ;  intNo<retDoc2M0171.length  ;  intNo++) {
            stringRecordNo12                   =  retDoc2M0171[intNo][0].trim() ;
            stringPurchaseMoney             =  retDoc2M0171[intNo][1].trim() ;
            stringPurchaseNoTaxMoney  =  retDoc2M0171[intNo][2].trim() ;
            //
            if(stringRecordNo12Sum.indexOf(","+stringRecordNo12+",")  ==  -1)  continue ;
            //
            System.out.println(intNo+"請款金額- 請款 代工("+stringPurchaseMoney+")------------------------------------------") ;
            doublePurchaseMoneySumDoc             +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
            doublePurchaseNoTaxMoneySumDoc   +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
        }
        // 請款金額- 借款沖銷
        if("A".equals(stringTYPE)) {
            stringSql  =  "SELECT  M17.RecordNo12,  M17.PurchaseMoney,  M17.PurchaseNoTaxMoney "  +
                         " FROM  "+stringTable671+" M17,  "+stringTable60+"  M10 "  +
                        " WHERE  M17.BarCode  =  M10.BarCode "  +
                          " AND  M10.ComNo  =  '"        +  stringComNo          +  "' "  +
                          " AND  M10.KindNo  =  '"        +  stringKindNo          +  "' "  +
                          " AND  M10.PurchaseNo  =  '"+  stringPurchaseNo  +  "' "  +
                          " AND  M10.FactoryNo  =  '"    +stringFactoryNo       +"' "  ;
            retDoc6M0171           =  dbDoc.queryFromPool(stringSql) ;
            for(int  intNo=0  ;  intNo<retDoc6M0171.length  ;  intNo++) {
                stringRecordNo12                  =  retDoc6M0171[intNo][0].trim() ;
                stringPurchaseMoney            =  retDoc6M0171[intNo][1].trim() ;
                stringPurchaseNoTaxMoney  =  retDoc6M0171[intNo][2].trim() ;
                //
                if(stringRecordNo12Sum.indexOf(","+stringRecordNo12+",")  ==  -1)  continue ;
                //
                System.out.println(intNo+"請款金額- 借款沖銷  請款 代工("+stringPurchaseNoTaxMoney+")------------------------------------------") ;
                doublePurchaseMoneySumDoc             +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
                doublePurchaseNoTaxMoneySumDoc   +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
            }
        }
        // 0 列帳 Y     1 固資代碼      2. 合計金額     3. 未稅金額
        for(int  intNo=0  ;  intNo<retAsAsset.length  ;  intNo++) {
            stringFilter                                   =  retAsAsset[intNo][1].trim() ;
            stringPurchaseMoney                 =  retAsAsset[intNo][2].trim() ;
            stringPurchaseNoTaxMoney       =  retAsAsset[intNo][3].trim() ;
            System.out.println(intNo+"固資代碼 ("+stringFilter+")("+retAsAsset.length+")--------------------") ;
            //
            doublePurchaseMoney                   =  exeUtil.doParseDouble(stringPurchaseMoney) ;
            doublePurchaseMoneyDoc             = 0 ;
            doublePurchaseNoTaxMoney         =  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
            doublePurchaseNoTaxMoneyDoc  = 0 ;
            // 金額整理-請款單-請款單
            stringRecordNo12Sum    =  ""+hashtableRecordNo12.get(stringFilter) ;  if("null".equals(stringRecordNo12Sum))  stringRecordNo12Sum  =  "" ;
            for(int  intNoL=0  ;  intNoL<retDoc2M0171.length  ;  intNoL++) {
                stringRecordNo12                  =  retDoc2M0171[intNoL][0].trim() ;
                stringPurchaseMoney            =  retDoc2M0171[intNoL][1].trim() ;
                stringPurchaseNoTaxMoney  =  retDoc2M0171[intNoL][2].trim() ;
                //
                if(stringRecordNo12Sum.indexOf(","+stringRecordNo12+",")  ==  -1) {
                    //doublePurchaseMoneySumDoc            +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
                    //doublePurchaseNoTaxMoneySumDoc  +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
                    //System.out.println(intNo+" 1檢核金額合計，請款系統("+stringPurchaseMoney+")。--------------------") ;
                    continue ;
                }
                //
                doublePurchaseMoneyDoc             +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
                doublePurchaseNoTaxMoneyDoc   +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
            }
            // 金額整理-請款單-借款沖銷
            for(int  intNoL=0  ;  intNoL<retDoc6M0171.length  ;  intNoL++) {
                stringRecordNo12                  =  retDoc6M0171[intNoL][0].trim() ;
                stringPurchaseMoney            =  retDoc6M0171[intNoL][1].trim() ;
                stringPurchaseNoTaxMoney  =  retDoc6M0171[intNoL][2].trim() ;
                //
                if(stringRecordNo12Sum.indexOf(","+stringRecordNo12+",")  ==  -1) {
                    //doublePurchaseMoneySumDoc            +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
                    //doublePurchaseNoTaxMoneySumDoc  +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
                    //System.out.println(intNo+" 2檢核金額合計，請款系統("+stringPurchaseMoney+")。--------------------") ;
                    continue ;
                }
                //
                doublePurchaseMoneyDoc             +=  exeUtil.doParseDouble(stringPurchaseMoney) ;
                doublePurchaseNoTaxMoneyDoc   +=  exeUtil.doParseDouble(stringPurchaseNoTaxMoney) ;
            }
            System.out.println(intNo+" 檢核各固資代碼("+stringFilter+")含稅金額合計，請款系統("+doublePurchaseMoneyDoc+")不得大於固資系統("+doublePurchaseMoney+")。--------------------") ;
            doublePurchaseMoney         = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseMoney,     0)) ;
            doublePurchaseMoneyDoc   = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseMoneyDoc,  0)) ;
            if(doublePurchaseMoneyDoc  >  doublePurchaseMoney) {
                messagebox("[固資系統]("+exeUtil.getFormatNum2(""+doublePurchaseMoney)+") [請款系統]("+exeUtil.getFormatNum2(""+doublePurchaseMoneyDoc)+")之 固資代碼("+stringFilter+")對應含稅金額 不一致，請通知人總室修正固資系統金額。1") ;
                return  false ;
            }
            System.out.println(intNo+" 檢核各固資代碼("+stringFilter+")未稅金額合計，請款系統("+doublePurchaseNoTaxMoneyDoc+")不得大於固資系統("+doublePurchaseNoTaxMoney+")。--------------------") ;
            doublePurchaseNoTaxMoney        = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseNoTaxMoney,         0)) ;
            doublePurchaseNoTaxMoneyDoc  = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseNoTaxMoneyDoc,  0)) ;
            if(doublePurchaseNoTaxMoneyDoc  >  doublePurchaseNoTaxMoney) {
                messagebox("[固資系統]("+exeUtil.getFormatNum2(""+doublePurchaseNoTaxMoney)+") [請款系統]("+exeUtil.getFormatNum2(""+doublePurchaseNoTaxMoneyDoc)+")之 固資代碼("+stringFilter+")對應未稅金額 不一致，請通知人總室修正固資系統金額。") ;
                return  false ;
            }
            // 合計金額整理
            doublePurchaseMoneySum                   +=  doublePurchaseMoney ;
            doublePurchaseMoneySumDoc             +=  doublePurchaseMoneyDoc ;
            doublePurchaseNoTaxMoneySum         +=  doublePurchaseNoTaxMoney ;
            doublePurchaseNoTaxMoneySumDoc   +=  doublePurchaseNoTaxMoneyDoc ;
            System.out.println(intNo+" 3檢核未稅金額合計，請款系統("+doublePurchaseMoneySumDoc+")("+doublePurchaseNoTaxMoneyDoc+")。--------------------") ;
        }
        // 檢核含稅金額合計，請款系統須等於固資系統。
        doublePurchaseMoneySum          = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseMoneySum,           0)) ;
        doublePurchaseMoneySumDoc   = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseMoneySumDoc,      0)) ;
        System.out.println(" 檢核含稅金額合計，請款系統("+doublePurchaseMoneySumDoc+")不得大於固資系統("+doublePurchaseMoneySum+")。--------------------") ;
        if(doublePurchaseMoneySum  !=  doublePurchaseMoneySumDoc) {
            messagebox("[固資系統]("+exeUtil.getFormatNum2(""+doublePurchaseMoneySum)+") [請款系統]("+exeUtil.getFormatNum2(""+doublePurchaseMoneySumDoc)+")之 "  +
                                 "含稅金額合計 不一致，請通知人總室修正固資系統金額。1") ;
            return  false ;
        }
        // 檢核未稅金額合計，請款系統須等於固資系統。
        doublePurchaseNoTaxMoneySum         = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseNoTaxMoneySum,            0)) ;
        doublePurchaseNoTaxMoneySumDoc   = exeUtil.doParseDouble(convert.FourToFive(""+doublePurchaseNoTaxMoneySumDoc,      0)) ;
        System.out.println(" 檢核未稅金額合計，請款系統("+doublePurchaseNoTaxMoneySumDoc+")不得大於固資系統("+doublePurchaseNoTaxMoneySum+")。--------------------") ;
        if(doublePurchaseNoTaxMoneySum  !=  doublePurchaseNoTaxMoneySumDoc) {
            messagebox("[固資系統]("+exeUtil.getFormatNum2(""+doublePurchaseNoTaxMoneySum)+") [請款系統]("+exeUtil.getFormatNum2(""+doublePurchaseNoTaxMoneySumDoc)+")之"  +
                                 "未稅金額合計 不一致，請通知人總室修正固資系統金額。3") ;
            return  false ;
        }
        return  true ;
    }
    public  String[]  getPurchaseData(String  stringBarCode,  Doc2M010  exeFun) throws  Throwable {
        String      stringComNo                 =  "" ;
        String      stringKindNo                 =  "" ;
        String      stringKindNoPurchase  =   "" ;
        String      stringPurchaseNo         =  "" ;
        String      stringTable                   =  "" ;
        String      stringSql                       =  "" ;
        String      stringFactoryNo            =  "" ;
        String      stringTYPE                    =  "" ;
        String[][]  retTableData                =  null ;
        String[]    arrayPurchaseData      =  null ;
        // 請款
        retTableData  =  exeFun.getTableDataDoc("SELECT  KindNo,  ComNo  FROM  Doc2M010  WHERE  BarCode  =  '"+stringBarCode+"' ") ;
        if(retTableData.length  >  0)  {
            stringTable      =  "Doc2M017" ;
            stringTYPE      =  "A" ;
        } else {
            retTableData  =  exeFun.getTableDataDoc("SELECT  KindNo,  ComNo  FROM  Doc5M020  WHERE  BarCode  =  '"+stringBarCode+"' ") ;
            stringTable      =  "Doc5M027" ;
            stringTYPE      =  "B" ;
        }
        if(retTableData.length  ==  0)  return  arrayPurchaseData ;
        //
        stringKindNo  =  retTableData[0][0].trim() ;
        stringComNo  =  retTableData[0][1].trim() ;
        //
        if("23".equals(stringKindNo))  stringKindNoPurchase  =  "15" ;
        if("24".equals(stringKindNo))  stringKindNoPurchase  =  "17" ;
        // 
        retTableData  =  exeFun.getTableDataDoc("SELECT  PurchaseNo,  FactoryNo  FROM  "+stringTable+"  WHERE  BarCode  =  '"+stringBarCode+"' ") ;
        if(retTableData.length  ==  0)  return  arrayPurchaseData ;
        // 請購單 固資代碼檢核
        stringPurchaseNo   =  retTableData[0][0].trim() ;
        stringFactoryNo      =  retTableData[0][1].trim() ;
        // 0 公司     1  類別       2  請購單號     3  廠商       4 來源    5 請款類別
        arrayPurchaseData      =  new  String[6] ;
        arrayPurchaseData[0]  =  stringComNo ;
        arrayPurchaseData[1]  =  stringKindNoPurchase ;
        arrayPurchaseData[2]  =  stringPurchaseNo ;
        arrayPurchaseData[3]  =  stringFactoryNo ;
        arrayPurchaseData[4]  =  stringTYPE ;
        arrayPurchaseData[5]  =  stringKindNo ;
        return  arrayPurchaseData ;
    }
    // 0 公司     1  類別       2  請購單號     3  廠商       4 來源
    public  boolean  isApplyTypeD(String[]  arrayPurchaseData,  Doc2M010  exeFun) throws  Throwable {
        String      stringComNo          =  arrayPurchaseData[0].trim() ;
        String      stringKindNo          =  arrayPurchaseData[1].trim() ;
        String      stringPurchaseNo  =  arrayPurchaseData[2].trim() ;
        String      stringSql                 =  "" ;
        String      stringFactoryNo      =  arrayPurchaseData[3].trim() ;
        String      stringTYPE             =  arrayPurchaseData[4].trim() ;
        String      stringTable11         =  "" ;
        String      stringTable12         =  "" ;
        String[][]  retTableData          =  null ;
        //
        if("23".equals(stringKindNo))  stringKindNo  =  "15" ;
        if("24".equals(stringKindNo))  stringKindNo  =  "17" ;
        //
        if("A".equals(stringTYPE))  {
            stringTable11  =  "Doc3M011" ;
            stringTable12  =  "Doc3M012" ;
        } else {
            stringTable11  =  "Doc5M011" ;
            stringTable12  =  "Doc5M012" ;
        }
        //
        stringSql        =  " SELECT  M12.FILTER "  +
                      " FROM  "+stringTable12+" M12,  "+stringTable11+" M11 "   +
                      " WHERE  M11.BarCode  =  M12.BarCode "   +
                        " AND  ISNULL(FILTER,  '')  <>  ''  "  +
                        " AND  M11.ComNo   =  '" +stringComNo       +"' "  +
                        " AND  M11.KindNo  =  '" +stringKindNo        +"' "  +
                        " AND  M11.DocNo   =  '" +stringPurchaseNo +"' "  ;
        retTableData  =  exeFun.getTableDataDoc(stringSql) ;
        if(retTableData.length  ==  0)  return  false ;
        return  true ;
    }
    public  String[][]  getAsAsset(String[]  arrayPurchaseData,  Doc2M010  exeFun,  talk  dbAsset) throws  Throwable {
        String      stringComNo          =  arrayPurchaseData[0].trim() ;
        String      stringKindNo          =  arrayPurchaseData[1].trim() ;
        String      stringPurchaseNo  =  arrayPurchaseData[2].trim() ;
        String      stringSql                 =  "" ;
        String      stringFactoryNo      =  arrayPurchaseData[3].trim() ;
        String[][]  retTableData          =  null ;
        //
        if("23".equals(stringKindNo))  stringKindNo  =  "15" ;
        if("24".equals(stringKindNo))  stringKindNo  =  "17" ;
        //
        stringSql        =  " SELECT  a.ASS_ACCOUNT_ASSET,  (SELECT  FILTER FROM  AS_ASSET_FILTER  WHERE  F3_GUID = a.ASS_FILTER),  (a.ASS_AMOUNT+a.ASS_TAX_ADJ),  a.ASS_AMOUNT_UNTAX  "  +
                      " FROM  AS_RECEIPT b, AS_ASSET a  " +
                      " WHERE  a.RCT_ID = b.RCT_GUID " +
                        " AND  b.RCT_COMP_ID  =  '"+stringComNo           +"' "  +
                        " AND  b.PUR_ID  =  '"           +stringPurchaseNo     +"' "  +
                        //" AND             =  '"                  +stringKindNo             +"' "  + // 暫時 KindNo 不作查詢限制
                          " AND  b.SUP_ID  =  '"          +stringFactoryNo        +"' "  ;
        retTableData  =  dbAsset.queryFromPool(stringSql) ;
        //
        int         intPos          =  0 ;
        String   stringFilter   =  "" ;
        String[]  arrayTemp  =  null ;
        Vector  vectorFilter  =  new  Vector() ;
        Vector  vectorData  =  new  Vector() ;
        for(int  intNo=0  ;  intNo<retTableData.length  ;  intNo++) {
            stringFilter  =  retTableData[intNo][1].trim() ;
            //
            intPos  =  vectorFilter.indexOf(stringFilter) ;
            if(intPos  ==  -1) {
                vectorFilter.add(stringFilter) ;
                vectorData.add(retTableData[intNo]) ;
                continue ;
            }
            arrayTemp  =  (String[])  vectorData.get(intPos) ;  if(arrayTemp  ==  null)  continue ;
            arrayTemp[2]  = ""+(exeFun.doParseDouble(arrayTemp[2])+exeFun.doParseDouble(retTableData[intNo][2])) ;
            arrayTemp[3]  = ""+(exeFun.doParseDouble(arrayTemp[3])+exeFun.doParseDouble(retTableData[intNo][3])) ;
        }
        return  (String[][])  vectorData.toArray(new  String[0][0]) ;
    }
    // 由 [所有的請購單號] 找到對應的 [Doc5M020 資料(已轉傳票)(依時間排列)]
    public  String[][]  getDoc5M020(String  stringComNo,  String  stringFactoryNo,  Vector  vectorPurchaseNo,  Doc.Doc2M010  exeFun) throws  Throwable {
        boolean     booleanSource           =  false ;
        String        stringTable17              =  booleanSource ? "Doc2M017" :  "Doc5M027" ;
        String         stringTable10             =  booleanSource ? "Doc2M010" :  "Doc5M020" ;
        String         stringSql                     =  "" ;
        String        stringPurchaseNoSql  =  getPurchaseNoSql(vectorPurchaseNo) ;
        String[][]     retDoc5M020             =  null ;
        Hashtable  hashtableDocNo        =  new  Hashtable() ;
        Hashtable  hashtableBarCode     =  new  Hashtable() ;
        Vector        vectorEDateTime        =  new  Vector() ;
        //
        // 請款  0  BarCode       1  RetainMoney
        stringSql  =  "SELECT  M10.BarCode,  M10.RetainMoney " +
                       " FROM  "  +  stringTable17+ " M17,  "+stringTable10+" M10 " +
                      " WHERE  M17.BarCode  =  M10.BarCode " +
                         " AND  M10.ComNo  =  '"     +stringComNo     +"' " + 
                         " AND  M17.FactoryNo  =  '"+stringFactoryNo+"' " + 
                         " AND  M17.PurchaseNo  IN ("+stringPurchaseNoSql+") " +
                         " AND  M10.BarCode  IN  (SELECT  BarCode  FROM  Doc2M014  WHERE  STATUS_CD  = 'Z') "  +
               " ORDER BY  EDateTime " ;
        retDoc5M020  =  exeFun.getTableDataDoc(stringSql) ;
        //
        return retDoc5M020 ;
    }
    public  String  getPurchaseNoSql(Vector  vectorPurchaseNo) throws  Throwable {
        String  stringSqlAnd      =  "" ;
        String  stringPurchaseNo  =  "" ;
        for(int  intNo=0  ;  intNo<vectorPurchaseNo.size()  ;  intNo++) {
          stringPurchaseNo  =  (""+vectorPurchaseNo.get(intNo)).trim() ;  if("null".equals(stringPurchaseNo))  continue ;
                                                                               if("".equals(stringPurchaseNo))      continue ;
          if(!"".equals(stringSqlAnd))  stringSqlAnd  +=  ", " ;
          stringSqlAnd     +=  "'"+stringPurchaseNo+"'" ;
        }
        return stringSqlAnd ;
    }
    public  Hashtable  getDoc5M02201(String  stringSqlAnd,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        Hashtable  hashtableBarCodeRef     =  new  Hashtable() ;
        String         stringBarCodeRef           =  "" ;
        String         stringBackRetainMoney  =  "" ;
        String[][]     retDoc5M02201               =  exeFun.getDoc5M02201("",  "",  stringSqlAnd) ;
        double       dobuleBackRetainMoney =  0 ;
        // 0  BarCode     1  BarCodeRef     2  BackRetainMoney
        for(int  intNo=0  ;  intNo<retDoc5M02201.length  ;  intNo++) {
            stringBarCodeRef           =  retDoc5M02201[intNo][1].trim() ;
            stringBackRetainMoney  =  retDoc5M02201[intNo][2].trim() ;
            //
            dobuleBackRetainMoney  =  exeUtil.doParseDouble(stringBackRetainMoney)  +  exeUtil.doParseDouble(""+hashtableBarCodeRef.get(stringBarCodeRef)) ;
            stringBackRetainMoney    =  convert.FourToFive(""+dobuleBackRetainMoney,  0) ;
            hashtableBarCodeRef.put(stringBarCodeRef,  stringBackRetainMoney) ;
        }
        return  hashtableBarCodeRef;
    }
    //  0  BarCode    1  VOUCHER_YMD   2  VOUCHER_FLOW_NO   3  VOUCHER_SEQ_NO    4  FactoryNo   5  Amt
    public  Hashtable  getCanWriteAmtForFED1013(String  stringComNo,  String[][]  retDoc5M0224,  boolean  booleanComNoType,  Doc.Doc2M010  exeFun) throws  Throwable {
        String         stringVoucherYMD         =  "" ;
        String         stringVoucherFlowNo     =  "" ;
        String         stringVoucherSeqNo      =  "" ;
        String         stringSqlAnd                  =  "" ;
        Hashtable  hashtableCanWriteAmt  =  new  Hashtable() ;
        for(int  intNo=0  ;  intNo<retDoc5M0224.length  ;  intNo++) {
            stringVoucherYMD       =  retDoc5M0224[intNo][1].trim() ;
            stringVoucherFlowNo   =  retDoc5M0224[intNo][2].trim() ;
            stringVoucherSeqNo    =  retDoc5M0224[intNo][3].trim() ; 
            if("".equals(stringVoucherYMD))     continue ;
            if("".equals(stringVoucherFlowNo)) continue ;
            if("".equals(stringVoucherSeqNo))  continue ;
            if(!"".equals(stringSqlAnd))  stringSqlAnd  +=  " OR " ;
            stringSqlAnd  +=  "(I.VOUCHER_YMD  =  "          +  stringVoucherYMD      +  "  AND  "  +
                              " I.VOUCHER_FLOW_NO  =  "+  stringVoucherFlowNo  +  "  AND "  +
                              " I.VOUCHER_SEQ_NO  =  "   +  stringVoucherSeqNo   +  " )" ;
        }
        if("".equals(stringSqlAnd))   return  hashtableCanWriteAmt ;
        stringSqlAnd  =  " AND ("+  stringSqlAnd + ")" ;
        String         stringAMt          =  "" ;
        String         stringVoucher  =  "" ;
        String[][]     retFED1013     =  exeFun.getCanWriteAmtForFED1013(stringComNo,   stringSqlAnd,  booleanComNoType) ;
        for(int  intNo=0  ;  intNo<retFED1013.length  ;  intNo++) {
            stringVoucherYMD       =   retFED1013[intNo][0].trim() ;
            stringVoucherFlowNo   =   retFED1013[intNo][1].trim() ;
            stringVoucherSeqNo    =   retFED1013[intNo][2].trim() ;
            stringAMt                      =   retFED1013[intNo][3].trim() ;
            stringVoucher               =  stringVoucherYMD  +  "-"  +  convert.add0(stringVoucherFlowNo,  "5")  +  "-"  +  convert.add0(stringVoucherSeqNo,  "4") ;
            //System.out.println("stringVoucher----------------------["+stringVoucher+"]["+stringAMt+"]") ;
            hashtableCanWriteAmt.put(stringVoucher,  stringAMt) ;
        }
        return  hashtableCanWriteAmt ;
    }
    //    0  BarCode                          1  DocNo                          2  RowType                3  RecordNo               4  VOUCHER_YMD
    //    5  VOUCHER_FLOW_NO    6  VOUCHER_SEQ_NO    7  COMPANY_CD      8  KIND                        9  DB_CR_CD
    //  10  ACCT_NO                      11  DEPT_CD                  12  OBJECT_CD        13  AMT                      14  EXCHANG_AMT
    //  15  MONTEARY                   16  ClaimerMoney            17  STATUS_CD        18  DESCRIPTION_1  19  DESCRIPTION_2  
    //  20  DESCRIPTION_3         21  DESCRIPTION_4        22  DESCRIPTION_5  23  DESCRIPTION      24  LAST_USER         
    //  25  LAST_YMD
    public  Vector  doInsertDiscount(String[][]  retDoc2M014,  double  doubleDiscountTotalMoney,  Vector  vectorError,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        String       stringInvoiceNo                      =  "" ;
         String     stringBarCode                        =  retDoc2M014[0][0].trim( ) ;
         String      stringDiscountDate                =  convert.FormatedDate(convert.roc2ac(convert.replace(retDoc2M014[0][4].trim( ),  "/",  "")),  "/") ;
        String      stringCompanyCdO               =  retDoc2M014[0][7].trim( ) ;
        String      stringObjectCd                       =  retDoc2M014[0][12].trim( ) ;
        String      stringDescription                    =  retDoc2M014[0][23].trim( ) ;
        String      stringDepartNo                       =  exeFun.getDoc2M010(stringBarCode)[0][12].trim( ) ;
        String      stringDiscountNo                   =  "" ;
        String      stringRowType                      =  "" ;
        String[][]  retDoc2M011                        =  exeFun.getDoc2M011ForDiscountBarCode(stringBarCode) ;
        double    doubleInvoiceTotalMoney     =  0  ;
        double    doubleDiscountMoney           =  0  ;
        //
        for(int  intNo=0  ;  intNo<100  ;  intNo++) {
            if(intNo  <  retDoc2M011.length) {
                stringInvoiceNo                  =  retDoc2M011[intNo][0].trim( ) ;
                doubleInvoiceTotalMoney  =  exeFun.doParseDouble(retDoc2M011[intNo][3].trim( )) ;
                stringDiscountNo               =  exeFun.getDiscountNo(stringCompanyCdO,  stringDepartNo,  stringDiscountDate) ;
                //
                if(doubleInvoiceTotalMoney  >  doubleDiscountTotalMoney) {
                    doubleDiscountMoney  =  doubleDiscountTotalMoney ;
                } else {
                    doubleDiscountMoney   =  doubleInvoiceTotalMoney ;
                }
                stringDescription  =  getDescription(stringBarCode,  stringDescription,  exeFun,  exeUtil) ;
                exeFun.doInsertDoc2M016(stringDiscountNo,      stringDiscountDate,  stringBarCode,  stringInvoiceNo,  
                                              stringCompanyCdO,  stringDescription,     stringDepartNo,  doubleDiscountMoney) ;
                doubleDiscountTotalMoney  -=  doubleInvoiceTotalMoney ;
                if(doubleDiscountTotalMoney  <=  0) {
                    return vectorError ;
                }
            } else if(doubleDiscountTotalMoney  >  0) {
                // 特例：當請款金額小於零時，須再折讓前期的發票   
                //    0  InvoiceNo    1  InvoiceMoney   2  InvoiceDate      3  InvoiceTotalMoney
                String[][]  retDoc2M011ForDiscount            =  exeFun.getDoc2M011ForDiscount(stringDepartNo,  stringCompanyCdO,  stringObjectCd) ;
                double    doubleDiscountTotalMoneySum  =  0 ;
                double    doubleInvoiceMoneyExist             =  0 ;
                for(int  intDataNo=0  ; intDataNo<retDoc2M011ForDiscount.length  ;  intDataNo++) {
                    stringInvoiceNo                            =  retDoc2M011ForDiscount[intDataNo][0].trim( ) ;
                    doubleInvoiceTotalMoney            =  exeFun.doParseDouble(retDoc2M011ForDiscount[intDataNo][3].trim( )) ;
                    doubleDiscountTotalMoneySum  =  exeFun.getDiscountTotalMoneySum(stringInvoiceNo) ;
                    doubleInvoiceMoneyExist             =  doubleInvoiceTotalMoney  -  doubleDiscountTotalMoneySum ;
                    if(doubleInvoiceMoneyExist  >  0) {
                        // 開立折讓單
                        stringDiscountNo       =  exeFun.getDiscountNo(stringCompanyCdO,  stringDepartNo,  stringDiscountDate) ;
                        if(doubleInvoiceMoneyExist  >  doubleDiscountTotalMoney) {
                            doubleDiscountMoney  =  doubleDiscountTotalMoney ;
                        } else {
                            doubleDiscountMoney  =  doubleInvoiceMoneyExist ;
                        }
                        stringDescription  =  getDescription(stringBarCode,  stringDescription,  exeFun,  exeUtil) ;
                        exeFun.doInsertDoc2M016(stringDiscountNo,      stringDiscountDate,  stringBarCode,  stringInvoiceNo,  
                                                      stringCompanyCdO,  stringDescription,     stringDepartNo,  doubleDiscountMoney) ;
                         doubleDiscountTotalMoney  -=  doubleDiscountMoney ;
                        if(doubleDiscountTotalMoney  <=  0) {
                            return vectorError ;
                        }
                    }
                }
                if(doubleDiscountTotalMoney  >  0) {
                    vectorError.add("[條碼編號] 為 " +  stringBarCode +  "；[傳票日期] 為 " + stringDiscountDate +  "之傳票仍有 "  +  doubleDiscountTotalMoney  +  " 元未折讓。") ;
                    return vectorError ;
                }
            } 
        }
        return vectorError ;
    }
    public  String  getDescription(String  stringBarCode,  String  stringDescription,  Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {  
        String         stringSql          =  "" ;
        String         stringTemp      =  "" ;
        String[][]     retDoc2M010   =  null ;
        //
        stringSql          =  " SELECT  Descript FROM Doc2M010  WHERE  BarCode  =  '"+stringBarCode+"' " ;
        retDoc2M010  =  exeFun.getTableDataDoc(stringSql) ;
        if(retDoc2M010.length  ==  0) {
            stringSql          =  " SELECT  Descript FROM Doc5M020  WHERE  BarCode  =  '"+stringBarCode+"' " ;
            retDoc2M010  =  exeFun.getTableDataDoc(stringSql) ;
        }
        if(retDoc2M010.length  >  0)  stringDescription  =  retDoc2M010[0][0].trim() ;
        //
        stringTemp    =  exeUtil.doSubstring(code.StrToByte(stringDescription), 0, 30) ;
        stringTemp    =  code.ByteToStr(stringTemp) ;
        if(stringDescription.indexOf(stringTemp)  ==  -1) {
            stringTemp    =  exeUtil.doSubstring(code.StrToByte(stringDescription), 0, 29) ;
            stringTemp    =  code.ByteToStr(stringTemp) ;
        }
        stringDescription  =  stringTemp ;
        stringDescription  =  convert.replace(stringDescription,  "'",  "''") ;
        return  stringDescription ;
    }
    //    0  BarCode                          1  DocNo                          2  RowType                3  RecordNo               4  VOUCHER_YMD
    //    5  VOUCHER_FLOW_NO    6  VOUCHER_SEQ_NO    7  COMPANY_CD      8  KIND                        9  DB_CR_CD
    //  10  ACCT_NO                      11  DEPT_CD                  12  OBJECT_CD        13  AMT                      14  EXCHANG_AMT
    //  15  MONTEARY                   16  ClaimerMoney            17  STATUS_CD        18  DESCRIPTION_1  19  DESCRIPTION_2  
    //  20  DESCRIPTION_3         21  DESCRIPTION_4        22  DESCRIPTION_5  23  DESCRIPTION      24  LAST_USER         
    //  25  LAST_YMD
    public  void  doInsertDiscount(boolean  booleanSource,  String[]  retDoc2M014,  Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        String       stringInvoiceNo                      =  retDoc2M014[19].trim( ) ;
         String     stringBarCode                        =  retDoc2M014[0].trim( ) ;
         String      stringDiscountDate                =  exeUtil.getDateConvert(retDoc2M014[4].trim( )) ;
        String      stringCompanyCdO               =  retDoc2M014[7].trim( ) ;
        String      stringDiscountTax                  =  retDoc2M014[13].trim( ) ;
        String      stringDiscountMoney             =  retDoc2M014[20].trim( ) ;
        String      stringDiscountTotalMoney     =  retDoc2M014[21].trim( ) ;
        String      stringDescription                    =  retDoc2M014[23].trim( ) ;
        String      stringDepartNo                       =  (booleanSource? exeFun.getDoc2M010(stringBarCode) :exeFun.getDoc5M020(stringBarCode))[0][12].trim( ) ;
        String      stringDiscountNo                   =  "" ;
        //
        System.out.println("doInsertDiscount---------------------------") ;
        stringDiscountNo               =  exeFun.getDiscountNo(stringCompanyCdO,  stringDepartNo,  stringDiscountDate) ;
        stringDescription               =  getDescription(stringBarCode,  stringDescription,  exeFun,  exeUtil) ;
        exeFun.doInsertDoc2M016(stringDiscountNo,      stringDiscountDate,  stringBarCode,  stringInvoiceNo,  
                              stringCompanyCdO,  stringDescription,     stringDepartNo,  stringDiscountTotalMoney,
                              stringDiscountMoney,  stringDiscountTax) ;
    }
    public  void  doInsertDiscountNoTax(boolean  booleanSource,  String  stringAmt,  String[]  retDoc2M014,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
         String     stringBarCode                        =  retDoc2M014[0].trim( ) ;
         String      stringDiscountDate                =  exeUtil.getDateConvert(retDoc2M014[4].trim( )) ;
        String      stringCompanyCdO               =  retDoc2M014[7].trim( ) ;
        String      stringDiscountTax                  =  "0" ;
        String      stringDiscountMoney             =  stringAmt ;
        String      stringDiscountTotalMoney     =  stringAmt ;
        String      stringDescription                    =  retDoc2M014[23].trim( ) ;
        String       stringInvoiceNo                      =  booleanSource?
                                                                           exeFun.getDoc2M011(stringBarCode)[0][3].trim() :
                                           exeFun.getDoc5M021(stringBarCode)[0][3].trim() ;//
        String      stringDepartNo                       =  (booleanSource? exeFun.getDoc2M010(stringBarCode) : exeFun.getDoc5M020(stringBarCode))[0][12].trim( ) ;
        String      stringDiscountNo                   =  "" ;
        //
        stringDiscountNo               =  exeFun.getDiscountNo(stringCompanyCdO,  stringDepartNo,  stringDiscountDate) ;
        stringDescription               =  getDescription(stringBarCode,  stringDescription,  exeFun,  exeUtil) ;
        exeFun.doInsertDoc2M016(stringDiscountNo,      stringDiscountDate,  stringBarCode,  stringInvoiceNo,  
                              stringCompanyCdO,  stringDescription,     stringDepartNo,  stringDiscountTotalMoney,
                              stringDiscountMoney,  stringDiscountTax) ;
    }
    // 資料庫
    public  void  doDoc2M010etc(boolean  booleanSource,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        //
        int            intAccountCount         =  exeUtil.doParseInteger(getValue("AccountCount").trim()) ;
        String      stringBarCode            =  getValue("BarCode").trim( ) ;
        String      stringPayCondition1   =  getValue("PayCondition1").trim( ) ;
        String      stringPayCondition2   =  getValue("PayCondition2").trim( ) ;
        String      stringPurchaseNo1     =  "" ;
        String      stringPurchaseNo2     =  "" ;
        String      stringPurchaseNo3     =  "" ;
        String      stringPurchaseNo4     =  "" ;
        String      stringComNo               =  (""+getValueAt("Table1",  0,  "COMPANY_CD")).trim( ) ;
        String       stringDbCrCd            =  "" ;
        String       stringRowType          =  "" ;
        String       stringRowTypeNext   =  "" ;
        String       stringRecordNo         =  "" ;
        String       stringRecordNoNext  =  "" ;
        String       stringAMT                 =  "" ;
        String       stringDescription1    =  "" ;
        String       stringDescription2    =  "" ;
        String       stringDescription3    =  "" ;
        String       stringDescription4    =  "" ;
        String       stringDescription5    =  "" ;
        String       stringDocNoType      =  "" ;
        String       stringDescript           =  (""+getValueAt("Table1",  0,  "DESCRIPTION")).trim( ) ;
        String       stringTaxType           =  (""+getValueAt("Table1",  0,  "TaxType")).trim( ) ;
        String       stringSql                   =  "" ;
        String       stringAcctNo             =  "" ;
        double     doubleAMT               =  0 ;
        double     doubleTaxRate         =  exeUtil.doParseDouble(exeFun.getDoc2M040( )[4].trim( ))  /  100 ;
        Vector      vectorSql                   =  new  Vector( ) ;
        JTable      jtable1                       =  getTable("Table1") ;
        //  退保留款不作處理
        String[][]  retDoc2M010  =  booleanSource?exeFun.getDoc2M010(stringBarCode) :exeFun.getDoc5M020(stringBarCode);
        if(!"".equals(retDoc2M010[0][26].trim()))             return ;
        stringDocNoType  =  retDoc2M010[0][27].trim() ;
        if("B".equals(stringDocNoType))  return  ;
        // 零用金時不做處理 START
        // 水電費 261、零用金231 不處理
        String      stringCostID              =  "" ;
        String      stringCostID1            =  "" ;
        String[][]  retDoc2M012            =  booleanSource?exeFun.getDoc2M012(stringBarCode):exeFun.getDoc5M022(stringBarCode) ;
        Vector     vectorPocketMoney  =  new  Vector( ) ;
        Vector     vectorSpecCostID     =  new  Vector( ) ;
        //
        vectorPocketMoney.add("31") ;
        vectorPocketMoney.add("32") ;
        vectorSpecCostID.add("231") ;
        vectorSpecCostID.add("261") ;
        stringCostID   =  retDoc2M012[0][4].trim( ) ;
        stringCostID1  =  retDoc2M012[0][5].trim( ) ;
        if(vectorPocketMoney.indexOf(stringCostID)  !=  -1)  return ;
        if(vectorSpecCostID.indexOf(stringCostID+stringCostID1)  !=  -1)  return ;
        // 零用金時不做處理 END
        // 稅率 START
        String     stringFactoryNo  =  "" ;
        String[][]  retDoc                =  null ;
        //  0  FactoryNo      1  InvoiceKind           2  InvoiceDate     3  InvoiceNo      4  InvoiceMoney
        //  5  InvoiceTax         6  InvoiceTotalMoney   7  DeductKind
        retDoc  =  booleanSource?
                        exeFun.getDoc2M011(stringBarCode) :
                exeFun.getDoc5M021(stringBarCode);    // 取得發票資料，Doc2M011
        if(retDoc.length  ==  0)  {
            //  0  FactoryNo                    1  ReceiptKind      2  ReceiptDate      3  ReceiptMoney     4  ReceiptTax
            //  5  ReceiptTotalMoney      6 ReceiptTaxType
            retDoc  =  booleanSource?
                            exeFun.getDoc2M013(stringBarCode) :
                    exeFun.getDoc5M023(stringBarCode);    // 取得扣繳資料，Doc2M013      
            if("B".equals(retDoc[0][1].trim( ))) {
                doubleTaxRate  =  0 ;
            } else {
                doubleTaxRate  =  exeUtil.doParseDouble(retDoc[0][6].trim( ))  /  100 ;
            }
        } else {
            if("D".equals(retDoc[0][1].trim( )))  doubleTaxRate  =  0 ;
        }
        // 稅率 END
        // Doc3M010
        // 付款條件更新
        String[][]  retDoc2M017  =  booleanSource?
                                                   exeFun.getDoc2M017(stringBarCode) :
                               exeFun.getDoc5M027(stringBarCode)  ;
        for(int  intNo=0  ;  intNo<retDoc2M017.length  ;  intNo++) {
            stringPurchaseNo1  =  retDoc2M017[intNo][0].trim( ) ;
            stringPurchaseNo2  =  retDoc2M017[intNo][1].trim( ) ;
            stringPurchaseNo3  =  retDoc2M017[intNo][2].trim( ) ;
            stringPurchaseNo4  =  retDoc2M017[intNo][5].trim( ) ;
            if(booleanSource  &&  !"".equals(stringPurchaseNo1)  &&  !"".equals(stringPurchaseNo2)  &&  !"".equals(stringPurchaseNo3)) {
                exeFun.doUpdateForDoc3M010(stringPayCondition1,  stringPayCondition2,  "N",                         stringComNo,
                                          stringPurchaseNo1,    stringPurchaseNo2,    stringPurchaseNo3,  stringPurchaseNo4) ;
            }
        }
        if(booleanSource)exeFun.doUpdatePayConditionForDoc2M010(stringPayCondition1,  stringPayCondition2,  stringBarCode) ;
        // Doc2M011(發票A)、Doc2M012(費用B、F)、Doc2M013(扣繳G)、Doc2M015(折讓D、折讓H)
        String[]  arrayDescription  =  new  String[5] ;
        for(int  intRowNo=0  ;  intRowNo<jtable1.getRowCount( )  ;  intRowNo++) {
            stringDbCrCd         =  (""+getValueAt("Table1",  intRowNo,  "DB_CR_CD")).trim( ) ;
            stringRowType       =  (""+getValueAt("Table1",  intRowNo,  "RowType")).trim( ) ;
            stringRecordNo      =  (""+getValueAt("Table1",  intRowNo,  "RecordNo")).trim( ) ;
            stringAMT               =  (""+getValueAt("Table1",  intRowNo,  "AMT")).trim( ) ;
            stringAcctNo           =  (""+getValueAt("Table1",  intRowNo,  "ACCT_NO")).trim( ) ;
             doubleAMT            =  exeUtil.doParseDouble(stringAMT) ;
            stringDescription1  =  (""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_1")).trim( ) ;  // 發票日期，所得給付票據到期日
            stringDescription2  =  (""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_2")).trim( ) ;  // 發票號碼，所後總額
            stringDescription3  =  (""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_3")).trim( ) ;  // 發票銷售額
            stringDescription4  =  (""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_4")).trim( ) ;  // 發票總額
            stringDescription5  =  (""+getValueAt("Table1",  intRowNo,  "DESCRIPTION_5")).trim( ) ;  // 發票種類
            arrayDescription[0]  =  stringDescription1 ;
            arrayDescription[1]  =  stringDescription2 ;
            arrayDescription[2]  =  stringDescription3 ;
            arrayDescription[3]  =  stringDescription4 ;
            arrayDescription[4]  =  stringDescription5 ;
            //
            if("P".equals(stringRowType))  continue ;
            //
            stringSql            =  "" ;
            if("D".equals(stringDbCrCd)) {
                if("A".equals(stringRowType.substring(0,1))) {
                    if(booleanSource) {
                        if("電算".equals(stringDescription5)) {
                            stringDescription5  =  "A" ;
                        } else if("收銀".equals(stringDescription5)) {
                            stringDescription5  =  "B" ;
                        } else if("其他".equals(stringDescription5)) {
                            stringDescription5  =  "C" ;
                        } else if("不得抵扣".equals(stringDescription5)) {
                            stringDescription5  =  "D" ;
                        } else if("免稅".equals(stringDescription5)) {
                            stringDescription5  =  "E" ;
                        } else if("海關代徵營業稅繳納證".equals(stringDescription5)) {
                            stringDescription5  =  "F" ;
                        } else if("其他二聯".equals(stringDescription5)) {
                            stringDescription5  =  "G" ;
                        } else if("其他含稅".equals(stringDescription5)) {
                            stringDescription5  =  "H" ;
                        } else {
                            stringDescription5  =  "" ;
                        }
                    } else {
                        if("三聯式".equals(stringDescription5)) {
                            stringDescription5  =  "A" ;
                        } else if("收銀機三聯式".equals(stringDescription5)) {
                            stringDescription5  =  "B" ;
                        } else if("收據".equals(stringDescription5)) {
                            stringDescription5  =  "C" ;
                        } else if("不得抵扣".equals(stringDescription5)) {
                            stringDescription5  =  "D" ;
                        } else if("免稅三聯式".equals(stringDescription5)) {
                            stringDescription5  =  "E" ;
                        } else if("海關代徵扣抵聯".equals(stringDescription5)) {
                            stringDescription5  =  "F" ;
                        } else if("收銀機二聯式".equals(stringDescription5)) {
                            stringDescription5  =  "G" ;
                        } else if("收據(含稅)".equals(stringDescription5)) {
                            stringDescription5  =  "H" ;
                        } else if("免稅二聯式".equals(stringDescription5)) {
                            stringDescription5  =  "I" ;
                        } else if(" 收據彙總".equals(stringDescription5)) {
                            stringDescription5  =  "J" ;
                        } else if("收據彙總(含稅)".equals(stringDescription5)) {
                            stringDescription5  =  "K" ;
                        } else {
                            stringDescription5  =  "" ;
                        }
                    }
                    // 發票
                    stringDescription1  =  convert.replace(stringDescription1,  "/",  "") ;
                    stringDescription1  =  convert.FormatedDate(stringDescription1,  "/") ;
                    if(!"".equals(stringDescription5))
                    stringSql  =  exeFun.getUpdateDoc2M011SqlUnion(stringBarCode,        stringRecordNo,       stringDescription5,
                                                                 stringDescription1,  stringDescription2,   stringDescription3,
                                                                  stringAMT,               stringDescription4,    stringTaxType,
                                                              booleanSource?"Doc2M011":"Doc5M021") ;
                    //System.out.println(intRowNo+"發票-------------------------------------"+stringSql) ;
                } else if("B".equals(stringRowType.substring(0,1))  ||  "F".equals(stringRowType.substring(0,1))) {
                    // 費用
                    //System.out.println(intRowNo+"-----費用未稅金額-------"+doubleAMT) ;
                    //
                    String  stringRecordNoDoc2M015  =  exeFun.getRecordNoForDoc2M015FromDoc2M012Union (booleanSource,  stringBarCode,  stringRecordNo) ;
                    String  stringRecordNoDoc5M010  =  exeFun.getRecordNoForDoc5M010FromDoc2M012Union (booleanSource,  stringBarCode,  stringRecordNo) ;
                    String  stringDiscountMoneyL         =  "0" ;
                    if(!"".equals(stringRecordNoDoc2M015)) {
                        String  stringRowTypeL                 =  "" ;
                        String  stringRecordNoL                =  "" ;
                        String  stringDiscountTotalMoney  =  "" ;
                        for(int  intNo=0  ;  intNo<jtable1.getRowCount( )  ;  intNo++) {
                            stringRowTypeL           =  (""+getValueAt("Table1",  intNo,  "RowType")).trim( ) ;
                            stringDiscountMoneyL  =  (""+getValueAt("Table1",  intNo,  "DESCRIPTION_3")).trim( ) ;  // 折讓未稅
                            stringRecordNoL           =  (""+getValueAt("Table1",  intNo,  "RecordNo")).trim( ) ;
                            //System.out.println(intNo+"---"+stringRowTypeL+"------------"+stringRecordNoDoc2M015) ;
                            if("H".equals(stringRowTypeL)) {
                                if(stringRecordNoL.equals(stringRecordNoDoc2M015)) {
                                    //System.out.println(intRowNo+"----"+intNo+"------費用對應的 折讓未稅金額 -------"+stringDiscountMoneyL) ;
                                    stringDiscountTotalMoney  =  exeFun.getSpecialDiscountTotalMoneyUnion (booleanSource?"Doc2M015":"Doc5M025",  stringBarCode,  stringRecordNo) ;
                                    //System.out.println(intRowNo+"----"+intNo+"------費用對應的 特別折讓金額 -------"+stringDiscountTotalMoney) ;
                                    //
                                    stringDiscountTotalMoney  =  ""+(exeUtil.doParseDouble(stringDiscountTotalMoney)  /  (doubleTaxRate+1)) ;  // 變成未稅
                                    //System.out.println(intRowNo+"----"+intNo+"------費用對應的 特別未稅折讓金額 -------"+stringDiscountTotalMoney) ;
                                    stringDiscountMoneyL         =  convert.FourToFive(""+(exeUtil.doParseDouble(stringDiscountMoneyL)  -  
                                                                                exeUtil.doParseDouble(stringDiscountTotalMoney)),  0) ;
                                    break ;
                                }
                                // 費用 + 折讓金額，故須減去折讓才是原始費用，如只有特別折讓的話，不須扣除
                                stringRowTypeL            =  (""+getValueAt("Table1",  intNo,  "ClaimerMoney")).trim( ) ;
                                if(stringRecordNoL.equals(stringRecordNoDoc5M010)) {
                                    //System.out.println(intRowNo+"----"+intNo+"------費用對應的 折讓未稅金額 -------"+stringDiscountMoneyL) ;
                                    stringDiscountMoneyL  =  "0" ;
                                    break ;
                                }
                            }
                        }
                    }
                    //
                    //System.out.println(intRowNo+"折讓-------------------------------------"+stringDiscountMoneyL) ;
                    doubleAMT  -=  exeUtil.doParseDouble(stringDiscountMoneyL) ;
                    stringSql       =  exeFun.getUpdateDoc2M012SqlUnion(booleanSource?"Doc2M012":"Doc5M022",  ""+doubleAMT,  stringBarCode,  stringRecordNo)  ;
                    //System.out.println(intRowNo+"費用-------------------------------------"+stringSql) ;
                }
            } else {
                if("G".equals(stringRowType.substring(0,1))  &&  !(intAccountCount>1)) {
                    String      stringItemCd                       =  "" ;
                    String      stringTotalReceiptMoney   =  "" ;
                    String      stringReceiptDate              =  "" ;
                    String      stringReceiptTaxType       =  getValue("ReceiptRate").trim( ) ;   // 格式
                    String[][]  retFED1004                       =  exeFun.getFED1004(stringAcctNo) ;
                    for(int  intNo=0  ;  intNo<retFED1004.length  ;  intNo++) {
                        stringItemCd  =  retFED1004[intNo][0].trim( ) ;
                        if("G20".equals(stringItemCd))  stringTotalReceiptMoney  =  arrayDescription[intNo] ;
                        if("G08".equals(stringItemCd))  {
                            stringReceiptDate  =  arrayDescription[intNo] ;
                            stringReceiptDate  =  convert.replace(stringReceiptDate,  "/",  "") ;
                            stringReceiptDate  =  convert.FormatedDate(stringReceiptDate,  "/") ;
                        }
                    }
                    if("".equals(stringTotalReceiptMoney)) {
                        String[][]  retDoc2M013  =  exeFun.getDoc2M013Union(booleanSource?"Doc2M013":"Doc5M023",  stringBarCode,  stringRecordNo) ;
                        if(retDoc2M013.length  >  0)  stringTotalReceiptMoney  =  retDoc2M013[0][5].trim() ;
                    }
                    String  stringReceiptMoney     =  ""+(exeUtil.doParseDouble(stringTotalReceiptMoney)  - exeUtil.doParseDouble(stringAMT))  ;                              // 所得淨額
                    //
                    stringReceiptMoney  =  convert.FourToFive(stringReceiptMoney,  0) ;
                    //stringAMT、stringDescription1、stringDescription2、stringRecordNo
                    stringSql  =  exeFun.getUpdateDoc2M013SqlUnion(stringAMT,                    stringReceiptDate,  stringTotalReceiptMoney,  stringReceiptMoney,  
                                                                                                                    stringReceiptTaxType,  stringBarCode,        stringRecordNo,                 booleanSource?"Doc2M013":"Doc5M023") ;
                    //System.out.println(intRowNo+"扣繳 -------------------------------------"+stringSql) ;
                }
                if("H".equals(stringRowType.substring(0,1))) {
                    // 折讓
                    // 須扣除特殊折讓
                    /*String  stringDiscountTotalMoney  =  exeFun.getSpecialDiscountTotalMoneyUnion (booleanSource?"Doc2M015":"Doc5M025",  stringBarCode,  stringRecordNo) ;
                    //
                    stringDescription4  =  convert.FourToFive(""+(exeUtil.doParseDouble(stringDescription4)  -  exeUtil.doParseDouble(stringDiscountTotalMoney)),  0)  ;
                    if(exeUtil.doParseDouble(stringDescription4)  >=  0) {
                        exeFun.doUpdateDiscountMoneyForDoc2M015Union(booleanSource?"Doc2M015":"Doc5M025",  stringDescription4,  stringBarCode,  stringRecordNo) ;
                    }*/
                }
                if("I".equals(stringRowType.substring(0,1))) {
                    // 保留款
                    // 更新保留款
                    exeFun.doUpdateRetainMoneyForDoc2M010Union(booleanSource?"Doc2M010":"Doc5M020",  stringBarCode,  ""+doubleAMT) ;
                }
            }
            if(!"".equals(stringSql))  vectorSql.add(stringSql) ;
        }
        //
        if(vectorSql.size( )  >  0)
            dbDoc.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;   
    }
    public  String  getInformation( ) {
        return "---------------修改按鈕程式.preProcess()----------------";
    }
}




