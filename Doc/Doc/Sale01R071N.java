package  Doc.Doc ;
import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import cLabel;
import  com.jacob.activeX.*;
import  com.jacob.com.*;
import  Farglory.util.FargloryUtil ;
import  Farglory.Excel.FargloryExcel ;
public class Sale01R071N extends bproc{
  public String getDefaultValue(String value)throws Throwable{
        Farglory.util.FargloryUtil  exeUtil                       =  new  Farglory.util.FargloryUtil() ;
        talk                                  dbDoc                       =  getTalk(""  +get("put_Doc"));
        talk                                  dbDocCS                  =  exeUtil.getTalkCS("Doc");
        //talk                                  dbSale                      =  getTalk(""  +get("put_Sale"));  
        talk                                  dbSale                      =  getTalk("Sale");  
        //
           if(!isBatchCheckOK(dbSale,  exeUtil)) return  value ;
         doExcel(dbSale,  dbDoc,  dbDocCS,  exeUtil) ;
         return value;
    }
    
    
    
    // �ˮ�
    // �e�ݸ���ˮ֡A���T�^�� True
    public  boolean  isBatchCheckOK(talk  dbSale,  Farglory.util.FargloryUtil  exeUtil)throws  Throwable {
        // �~��
        String  stringStartDate  =  getValue("StartDate").trim( ) ;
        if("".equals(stringStartDate)) {
            messagebox("�� [���İ϶�-�_] ���i���ťաC") ;
            getcLabel("StartDate").requestFocus( ) ;
            return  false ;
        }
        stringStartDate  =  exeUtil.getDateAC(stringStartDate,  "���İ϶�-�_") ;
        if(stringStartDate.length()  !=  10) {
            messagebox(stringStartDate) ;
            getcLabel("StartDate").requestFocus( ) ;
            return  false ;
        }
        String  stringEndDate  =  getValue("EndDate").trim( ) ;
        if("".equals(stringEndDate)) {
            messagebox("�� [���İ϶�-��] ���i���ťաC") ;
            getcLabel("EndDate").requestFocus( ) ;
            return  false ;
        }
        stringEndDate  =  exeUtil.getDateAC(stringEndDate,  "���İ϶�-�W") ;
        if(stringEndDate.length()  !=  10) {
            messagebox(stringEndDate) ;
            getcLabel("EndDate").requestFocus( ) ;
            return  false ;
        }
        String  stringYearACS     =  datetime.getYear(stringStartDate.replaceAll("/",  "")) ;
        String  stringYearACE     =  datetime.getYear(stringEndDate.replaceAll("/",  "")) ;
        if(!stringYearACS.equals(stringYearACE)) {
            messagebox("[���İ϶�] ���~�׶����P�~�C") ;
            return  false ;
        }
        if(stringStartDate.compareTo(stringEndDate)  >  0) {
            messagebox("[���İ϶�] �_�� ���Y���~�C") ;
            return  false ;
        }
        //
        String  stringProjectID1  =  getValue("ProjectID1").trim() ;
        if("".equals(stringProjectID1)) {
            messagebox("�� [�קO�N�X] ���i���ťաC") ;
            getcLabel("ProjectID1").requestFocus( ) ;
            return  false ;
        }
        if(",017PR,033FG,033CRM,033VIP,".indexOf(","+stringProjectID1+",")  ==  -1) {
            stringProjectID1  =  getProjectName(stringProjectID1,  dbSale) ; 
            if("053H59A".indexOf(stringProjectID1)==-1  &&  stringProjectID1.length()  ==  0) {
                messagebox("[�קO�N�X] ���s�b��Ʈw���C") ;
                getcLabel("ProjectID1").requestFocus( ) ;
                return  false ;
            }
        }
        message("") ;
        return  true ;
    }
    
    
    
    public void doExcel(talk  dbSale,  talk  dbDoc,  talk  dbDocCS,  FargloryUtil  exeUtil)throws Throwable{
        // ����^��
        String      stringTemp                        =  "" ;
        Hashtable    hashtableDealMoney                         =  new  Hashtable() ;
        Hashtable    hashtableTargetsMoney                    =  new  Hashtable() ;
        Hashtable    hashtableUseMoney                          =  new  Hashtable() ;
        String        stringMessage                 =  "" ;
        // �~�Z  �ؼФι��
        stringMessage  +=  "�~�Z  �ؼФι�� �d��---------------------------------\n" ;
        stringTemp         =  doDealMoneyForASale(stringTemp,  exeUtil,  dbSale,  hashtableDealMoney,  hashtableTargetsMoney) ;
        //stringMessage  +=  stringTemp ;
        // �ʤ���B
        stringMessage  +=  "\n\n�ʤ���B �d��---------------------------------S\n" ;
        stringTemp         =  doUseMoneyForASale("",  exeUtil,  dbDoc,  dbDocCS,  dbSale,  hashtableUseMoney) ;
        stringMessage  +=  stringTemp ;
        stringMessage  +=  "\n\n�ʤ���B �d��---------------------------------E\n" ;
        //if("B3018".equals(getUser())){message("Data\n"+stringMessage) ;return ;}




        // Excel
        FargloryExcel  exeExcel  =  new  FargloryExcel( ) ;
        //
        String         stringFilePah              =  "g:/��T��/Excel/Doc/Sale01R071N.xlt" ;
        if(!(new File(stringFilePah)).exists()) {
            stringFilePah  =  "https://emaker.farglory.com.tw:8080/servlet/baServer3?step=6?filename="+stringFilePah ;
        }
         Vector    retVector              =  exeExcel.getExcelObject(stringFilePah) ;
        Dispatch  objectSheets        =  (Dispatch)retVector.get(3) ;
        Dispatch  objectSheet1        =  (Dispatch)retVector.get(1) ;
        Dispatch  objectSheet          =   null ;
        Dispatch  objectClick            =  null ;
        // �קO
        String  stringProjectID1  =  getValue("ProjectID1").trim() ;
        exeExcel.putDataIntoExcel(0,  0,  "�קO�G"+stringProjectID1, objectSheet1) ;
        // �϶�
        String  stringStartDate  =  getValue("StartDate").trim( ) ;
        String  stringEndDate   =  getValue("EndDate").trim( ) ;
        String  stringYear        =  datetime.getYear(stringStartDate.replaceAll("/",  "")) ;
        exeExcel.putDataIntoExcel(4,    0,  "�϶��G"+stringStartDate+"��"+stringEndDate, objectSheet1) ;
        // ����
        String  stringType                  =  getValue("Type").trim() ;
        String  stringSourceType       =  getValue("SourceType").trim() ;
        if("ALL".equals(stringType)) {
              stringTemp  =  "�Ҧ����q\n" ;
        } else if("OTHER".equals(stringType)) {
              stringTemp  =  "�䥦���q\n" ;
        } else {
              stringTemp  =  "���q�G"+stringType+"\n" ;
        }
        if("ALL".equals(stringType)) {
              stringTemp  =  "�Ҧ����q\n" ;
        } else if("OTHER".equals(stringType)) {
              stringTemp  =  "�䥦���q\n" ;
        } else {
              stringTemp  =  "���q�G"+stringType+"\n" ;
        }
        stringTemp  +=  "A".equals(stringSourceType)?"���ʽдڰʤ�":"�д�" ;
        exeExcel.putDataIntoExcel(16,  0,  stringTemp, objectSheet1) ;
        //
        String    stringTitle1      =  "" ;
        String    stringCostIDs   =  "" ;
        String    stringMonth     =  "" ;
        String    stringValue      =  "" ;
        String[]  arrayTemp     =  null ;
        for(int  intRow=2  ;  intRow<70  ;  intRow++) {
            stringTitle1     =  exeExcel.getDataFromExcel2(1,   intRow,  objectSheet1) ;
            stringCostIDs  =  exeExcel.getDataFromExcel2(3,   intRow,  objectSheet1) ;
            for(int  intCol=4  ;  intCol<19  ;  intCol++) {
                  stringMonth  =  exeExcel.getDataFromExcel2(intCol,   1,  objectSheet1) ;
                  // 
                  if("�X�p".equals(stringMonth))            continue ;
                  if(exeUtil.doParseDouble(stringMonth)  >  0)  stringMonth  =  stringYear+convert.add0(stringMonth,  "2") ;
                  //
                  if("�~�Z�ؼ�".equals(stringTitle1)) {
                      stringValue        =  ""+hashtableTargetsMoney.get(stringMonth) ;     if(exeUtil.doParseDouble(stringValue)  ==  0)  stringValue  =  "0" ;
                      //stringMessage  +=  "\n\n�~�Z�ؼ� Excel("+stringMonth+")("+stringValue+")\n" ;
                  } else if("��ڷ~�Z".equals(stringTitle1)) {
                      stringValue  =  ""+hashtableDealMoney.get(stringMonth) ;        if(exeUtil.doParseDouble(stringValue)  ==  0)  stringValue  =  "0" ;
                      //stringMessage  +=  "\n\n��ڷ~�Z Excel("+stringMonth+")("+stringValue+")\n" ;
                  } else {
                      if("".equals(stringCostIDs)) {
                          continue ;
                      } else {
                          stringMessage  +=  "\n\n�ʤ���B Excel   ROW("+intRow+")Col("+intCol+") ---------------------------------------S\n" ;
                          stringValue         =  getRequestMoneyForCostID("" ,  stringMonth,  stringCostIDs,  hashtableUseMoney,  exeUtil) ;
                          arrayTemp       =  convert.StringToken(stringValue,  "%%%") ;
                          stringValue         =  arrayTemp[0] ;
                          stringTemp           =  arrayTemp[1] ;
                          stringMessage  +=  stringTemp ;
                          stringMessage  +=  "\n\n�ʤ���B Excel   ROW("+intRow+")Col("+intCol+") ---------------------------------------E\n" ;
                      }
                  }
                  exeExcel.putDataIntoExcel(intCol,  intRow,  stringValue, objectSheet1) ;
            
            }
        }       
        // ���� Excel ����
        exeExcel.getReleaseExcelObject(retVector) ;
        //
        if("B3018".equals(getUser()))message("Data\n"+stringMessage) ;
    }
    public String  getRequestMoneyForCostID(String  stringMessage,  String  stringYYMM,  String  stringCostIDs,  Hashtable  hashtableUseMoney,  FargloryUtil  exeUtil) throws Throwable{
        String      stringCostID                        =  "" ;
        String      stringCostIDL                      =  "" ;
        String      stringCostID1                      =  "" ;
        String      stringKey                            =  "" ;
        String    stringTemp                =  "" ;
        double    doubleRequestMoneySum  =  0 ;
        String[]   arrayCostID              =  convert.StringToken(stringCostIDs,  "\n") ;
        String[]    arrayTemp                          =  null ;
        Vector    vectorCostID            =  new  Vector() ;
        Object    objectTemp                          =  null ;
        //
        for(int  intNo=0  ;  intNo<arrayCostID.length  ;  intNo++) {
            stringCostID  =  arrayCostID[intNo] ;                       if("".equals(stringCostID))  continue ;
            stringKey       =  stringCostID+"%-%"+stringYYMM ;
            //
            stringCostIDL  =  exeUtil.doSubstring(stringCostID,  0,  2) ;
            if(vectorCostID.indexOf(stringCostIDL)==-1)  vectorCostID.add(stringCostIDL) ;
            //
            doubleRequestMoneySum    +=  exeUtil.doParseDouble(""+hashtableUseMoney.get(stringKey)) ;  
            stringTemp                             =  "1��"+(intNo+1)+"�C----------("+stringKey+")("+convert.FourToFive(""+doubleRequestMoneySum,  0)+")\n" ;
            stringMessage            +=  stringTemp ;
            //System.out.println("("+stringTemp+")--------------------------------------") ;
        }
        for(int  intNo=0  ;  intNo<vectorCostID.size()  ;  intNo++) {
            stringCostID  =  ""+vectorCostID.get(intNo) ;                       if("".equals(stringCostID)  ||  "null".equals(stringCostID))  continue ;
            stringKey       =  stringCostID+"%-%"+stringYYMM ;
            //
            doubleRequestMoneySum    +=  exeUtil.doParseDouble(""+hashtableUseMoney.get(stringKey)) ;  
            stringTemp                             =  "2��"+(intNo+1)+"�C----------("+stringKey+")("+convert.FourToFive(""+doubleRequestMoneySum,  0)+")\n" ;
            stringMessage            +=  stringTemp ;
            //System.out.println("("+stringTemp+")--------------------------------------") ;
        }
        return  convert.FourToFive(""+(doubleRequestMoneySum/10000),  4)+"%%%"+stringMessage ;
    }
    
    
    
    // ��Ʈw Doc
    public String doUseMoneyForASale(String  stringMessage,  FargloryUtil  exeUtil,  talk  dbDoc,  talk  dbDocCS,  talk  dbSale,  Hashtable  hashtableUseMoney)throws Throwable{
        String          stringStartDate       =  getValue("StartDate").trim( ) ;
        String          stringEndDate        =  getValue("EndDate").trim( ) ;
        String          stringACYear          =  datetime.getYear(stringStartDate.replaceAll("/",  "")) ;
        String      stringYearLast        =   ""+(exeUtil.doParseInteger(stringACYear)-1) ;
        String      stringProjectID1      =  getValue("ProjectID1").trim() ;
        String      stringType            =  getValue("Type").trim() ;
        String      stringSourceType    =  getValue("SourceType").trim() ;
        //                                                       �e�@�~��                            �ײ֭p                  ���~
        String[]    arrayKEY          =  {"�e�@�~��",                      "�ײ֭p",              ""} ;
        String[]    arrayDateStart      =  {stringYearLast+"/01/01",  "",                     stringStartDate} ;
        String[]    arrayDateEnd      =  {stringYearLast+"/12/31",  stringEndDate,      stringEndDate} ;
        String[]    arraySize           =  {"",                                     "",                          "7"} ;
        String[][]    retData           =  null ;
        //
        if("A".equals(stringSourceType)) {
            // �ʤ�
            for(int  intNo=0  ;  intNo<arrayKEY.length  ;  intNo++) {
                //System.out.println(arrayKEY[intNo]+"-------------------------------------------------------") ;
                retData           =  getRealMoneyForDoc3M014UseMoneyACView(stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  dbDoc) ;
                //stringMessage  +=  arrayKEY[intNo]+"�ʤ�("+retData.length+")-----------------------------S\n" ;
                stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                if("ALL".equals(stringType)  ||  "CS".equals(stringType)) {
                    retData         =  getRealMoneyForDoc3M014UseMoneyACView(stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  dbDocCS) ;
                    //stringMessage  +=  "�H�ذʤ�("+retData.length+")-----------------------------\n" ;
                    stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                }
            }
        } else {
            // �д�
            arraySize[2]  =  "6" ;
            for(int  intNo=0  ;  intNo<arrayKEY.length  ;  intNo++) {
                // �д�
                retData         =  getRequestMoneyForDoc2M012(stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  exeUtil,  dbDoc) ;
                stringMessage  +=  "�д�("+retData.length+")-----------------------------\n" ;
                stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                // �ɴ�+�ɴڨR�P
                retData         =  getRequestMoneyForDoc6M012("Z6",  stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  exeUtil,  dbDoc) ;
                stringMessage  +=  "�ɴ�+�ɴڨR�P("+retData.length+")-----------------------------\n" ;
                stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                // �ɴڨR�P(�s)
                retData         =  getRequestMoneyForDoc6M0121(stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  exeUtil,  dbDoc) ;
                stringMessage  +=  "�ɴڨR�P(�s)("+retData.length+")-----------------------------\n" ;
                stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                // ��ʿ�J
                retData         =  getRealMoneyHandForZCoReaMM(stringProjectID1,  arraySize[intNo],  stringType,  arrayDateStart[intNo],  arrayDateEnd[intNo],  exeUtil,  dbSale) ;
                stringMessage  +=  "��ʿ�J("+retData.length+")-----------------------------\n" ;
                stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                if("ALL".equals(stringType)  ||  "CS".equals(stringType)) {
                    // �д�                 
                    retData             =  getRequestMoneyForDoc2M012(stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  exeUtil,  dbDocCS) ;
                    stringMessage  +=  "�H��-�д�("+retData.length+")-----------------------------\n" ;
                    stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                    // �ɴڨR�P
                    retData             =  getRequestMoneyForDoc6M012("CS",  stringProjectID1,  arraySize[intNo],  arrayDateStart[intNo],  arrayDateEnd[intNo],  stringType,  exeUtil,  dbDocCS) ;
                    stringMessage  +=  "�H��-�ɴڨR�P("+retData.length+")-----------------------------\n" ;
                    stringMessage  =  doUseMoney(stringMessage,  arrayKEY[intNo],  retData,  exeUtil,  hashtableUseMoney) ;
                }
            }
        }
        return  stringMessage ;
    }
    public String doUseMoney(String  stringMessage,  String  stringKEY,  String[][]  retData,  FargloryUtil  exeUtil,  Hashtable  hashtableRealMoney)throws Throwable{
        String    stringMonth         =  "" ;
        String    stringCostID        =  "" ;
        String    stringCostID1       =  "" ;
        String    stringRealMoney  =  "" ;
        String    stringKEYL          =  "" ;
        double   doubleTemp     = 0 ;
        // 0  CDate       1  CostID       2  CostID1      3  RealMoney
        for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
            stringMonth           =  !"".equals(stringKEY)?stringKEY                :retData[intNo][0].trim() ;
            stringCostID          =  !"".equals(stringKEY)?retData[intNo][0].trim()     :retData[intNo][1].trim() ;
            stringCostID1       =  !"".equals(stringKEY)?retData[intNo][1].trim()     :retData[intNo][2].trim() ;
            stringRealMoney     =  !"".equals(stringKEY)?retData[intNo][2].trim()     :retData[intNo][3].trim() ;
            //
            if(!"".equals(stringKEY)) {
                stringMonth  =  stringKEY ;
            } else {
                stringMonth  =  retData[intNo][0].trim().replaceAll("/",  "") ;
                if(stringMonth.length()  ==  5) {
                    stringMonth  =  exeUtil.getDateConvert(stringMonth+"01") ;
                    stringMonth  =  exeUtil.doSubstring(stringMonth,  0,  7) ;
                }
            }
            stringKEYL          =  stringCostID+stringCostID1+"%-%"+stringMonth.replaceAll("/",  "") ;
            //
            doubleTemp       =  exeUtil.doParseDouble(stringRealMoney)+exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEYL)) ;
            stringMessage  +=  (intNo+1)+"------------"+stringKEYL+"�G"+convert.FourToFive(""+doubleTemp,  0)+"\n" ;
            hashtableRealMoney.put(stringKEYL,  convert.FourToFive(""+doubleTemp,  0)) ;
            
        }
        return  stringMessage ;
    }
    public String[][] getRealMoneyForDoc3M014UseMoneyACView(String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  String  stringType,  talk  dbDoc)throws Throwable{
        String      stringSql              =  "" ;
        String      stringSqlAnd        =  "" ;
        String      stringSqlItem        =  "" ;
        String      stringSqlTemp     =  "" ;
        String      stringDepartNo   =  "" ;
        //String      stringProjectID1  =  "" ;
        String[][]  retData                =  new  String[0][0] ;
        //
        if(dbDoc  ==  null)  return  retData ;
        // 0  CDate       1  CostID       2  CostID1      3  RealMoney
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(10),UseDate,111),1,"+stringSize+"), "   ;
        } else {
            stringSqlItem  =  ""   ;
        }

        stringSql         =  "SELECT  "+stringSqlItem+"  CostID,  CostID1,  SUM(RealMoney) \n"  +
                        " FROM  Doc3M014_UseMoney_AC_view \n"  +
                                      " WHERE  ISNULL(CostID,  '')  <> '' \n" ;
        if("ALL".equals(stringType)) {
            // �����q
        } else if("OTHER".equals(stringType)) {
            stringSql  +=  " AND  ComNo  NOT IN ('Z6',  '01',  'CS') \n" ;
        } else {
            stringSql  +=  " AND  ComNo  = '"+stringType+"' \n" ;
        }
        if(!"".equals(stringDateS))          stringSql  +=  " AND  UseDate  >=  '"   +  stringDateS         +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  UseDate <=  '"    +  stringDateE         +"'  \n" ;
        if(!"".equals(stringProjectID1)) {
            if(",017PR,033CRM,033FG,033VIP,".indexOf(","+stringProjectID1+",")  ==  -1) {
                stringSql  +=  " AND  ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;       
            } else {
                stringSql  +=  " AND  DepartNo  =  '"  +  stringProjectID1  +  "' \n" ;       
            }
        }
        stringSql  +=  "  GROUP BY  "+stringSqlItem+"  CostID,  CostID1 \n" ;
        retData      =  dbDoc.queryFromPool(stringSql) ;
        return  retData ;
    }
    public String[][] getRequestMoneyForDoc2M012(String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  String  stringType,  FargloryUtil  exeUtil,  talk  dbDoc)throws Throwable{
        String      stringSql              =  "" ;
        String      stringSqlAnd        =  "" ;
        String      stringSqlItem        =  "" ;
        String      stringSqlTemp     =  "" ;
        String[][]  retDoc2M012      =  null ;
        //
        stringDateS  =  exeUtil.getDateConvertFullRoc(stringDateS) ;
        stringDateE  =  exeUtil.getDateConvertFullRoc(stringDateE) ;
        // 0  CDate       1  CostID       2  CostID1      3  SUM(RealTotalMoney)
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(8),M10.CDate,111),1,"+stringSize+"), "   ;
        } else {
            stringSqlItem  =  " "   ;
        }
        stringSql         +=  "SELECT  "+stringSqlItem+"  M12.CostID,  M12.CostID1,  SUM(M12.RealTotalMoney) \n"  +
                        " FROM  Doc2M012 M12,  Doc2M010 M10 \n"  +
                                      " WHERE  M12.BarCode =  M10.BarCode \n"  +
                             " AND  M10.UNDERGO_WRITE  <>  'E'  \n"  +
                           " AND  M12.CostID  <>  '13' \n" +
                           " AND  M12.CostID+M12.CostID1  <> '001' \n" ;
        if("ALL".equals(stringType)) {
            // �����q
        } else if("OTHER".equals(stringType)) {
            stringSql  +=  " AND  M10.ComNo  NOT IN ('Z6',  '01',  'CS') \n" ;
        } else {
            stringSql  +=  " AND  M10.ComNo  = '"+stringType+"' \n" ;
        }
        if(!"".equals(stringDateS))          stringSql  +=  " AND  M10.CDate  >=  '"       +  stringDateS          +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  M10.CDate <=  '"         +  stringDateE         +"'  \n" ;
        if(!"".equals(stringProjectID1)) {
            if(",017PR,033CRM,033FG,033VIP,".indexOf(stringProjectID1)  !=  -1) {
                stringSql  +=  " AND  M12.DepartNo  =  '"  +  stringProjectID1  +  "' \n" ;   
            } else {
                stringSql  +=  " AND  M12.ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;   
            }
        }
        //
        stringSql  +=  "  GROUP BY  "+stringSqlItem+"  CostID,  CostID1 \n" ;
        retDoc2M012  =  dbDoc.queryFromPool(stringSql) ;
        return  retDoc2M012 ;
    }
    public String[][] getRequestMoneyForDoc6M012(String  stringDBSource,  String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  String  stringType,  FargloryUtil  exeUtil,  talk  dbDoc)throws Throwable{
        String      stringSql              =  "" ;
        String      stringSqlAnd        =  "" ;
        String      stringSqlItem        =  "" ;
        String      stringSqlTemp     =  "" ;
        String      stringDepartNo   =  "" ;
        String      stringDateField   =  "CS".equals(stringDBSource)?"CDate":"NeedDate" ;
        String[][]  retDoc6M012      =  null ;
        //
        stringDateS  =  exeUtil.getDateConvertFullRoc(stringDateS) ;
        stringDateE  =  exeUtil.getDateConvertFullRoc(stringDateE) ;
        // 0    CDate       1  CostID       2  CostID1      3  SUM(RealMoney)
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(8),M10."+stringDateField+",111),1,"+stringSize+"), "   ;
        } else {
            stringSqlItem  =  " "   ;
        }
        stringSql         +=  "SELECT  "+stringSqlItem+"  M12.CostID,  M12.CostID1,  SUM(M12.RealTotalMoney) \n"  +
                        " FROM  Doc6M012 M12,  Doc6M010 M10 \n"  +
                                      " WHERE  M12.BarCode =  M10.BarCode \n"  +
                             " AND  M10.UNDERGO_WRITE  <>  'E'  \n"  +
                           " AND  M10.PurchaseNoExist  IN ('Y',  'N') \n" +
                           " AND  M12.CostID  <>  '13' \n" +
                           " AND  M12.CostID+M12.CostID1  <> '001' \n" ;
        if("ALL".equals(stringType)) {
            // �����q
        } else if("OTHER".equals(stringType)) {
            stringSql  +=  " AND  M10.ComNo  NOT IN ('Z6',  '01',  'CS') \n" ;
        } else {
            stringSql  +=  " AND  M10.ComNo  = '"+stringType+"' \n" ;
        }
        if(!"".equals(stringDateS))          stringSql  +=  " AND  M10."+stringDateField+"  >=  '"       +  stringDateS          +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  M10."+stringDateField+" <=  '"         +  stringDateE         +"'  \n" ;
        if(!"".equals(stringProjectID1)) {
            if(",017PR,033CRM,033FG,033VIP,".indexOf(stringProjectID1)  !=  -1) {
                stringSql  +=  " AND  M12.DepartNo  =  '"  +  stringProjectID1  +  "' \n" ;   
            } else {
                stringSql  +=  " AND  M12.ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;   
            }
        }
        //        
        stringSql  +=  "  GROUP BY  "+stringSqlItem+"  CostID,  CostID1 " ;
        retDoc6M012  =  dbDoc.queryFromPool(stringSql) ;
        return  retDoc6M012 ;
    }
    public String[][] getRequestMoneyForDoc6M0121(String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  String  stringType,  FargloryUtil  exeUtil,  talk  dbDoc)throws Throwable{
        String      stringSql              =  "" ;
        String      stringSqlAnd        =  "" ;
        String      stringSqlItem        =  "" ;
        String      stringSqlTemp     =  "" ;
        String      stringDepartNo   =  "" ;
        String[][]  retDoc6M0121      =  null ;
        //
        stringDateS  =  exeUtil.getDateConvertFullRoc(stringDateS) ;
        stringDateE  =  exeUtil.getDateConvertFullRoc(stringDateE) ;
        // 0    CDate       1  CostID       2  CostID1      3  SUM(RealMoney)
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(8),M10.NeedDate,111),1,"+stringSize+"), "   ;
        } else {
            stringSqlItem  =  " "   ;
        }
        stringSql         +=  "SELECT  "+stringSqlItem+"  M12.CostID,  M12.CostID1,  SUM(M12.RealTotalMoney) \n"  +
                        " FROM  Doc6M0121 M12,  Doc6M010 M10 \n"  +
                                      " WHERE  M12.BarCode =  M10.BarCode \n"  +
                             " AND  M10.UNDERGO_WRITE  <>  'E'  \n"  +
                           " AND  M12.DocType  IN  ('C') \n"  +
                           " AND  M12.CostID  <>  '13' \n" +
                           " AND  M12.CostID+M12.CostID1  <> '001' \n" ;
        if("ALL".equals(stringType)) {
            // �����q
        } else if("OTHER".equals(stringType)) {
            stringSql  +=  " AND  M10.ComNo  NOT IN ('Z6',  '01',  'CS') \n" ;
        } else {
            stringSql  +=  " AND  M10.ComNo  = '"+stringType+"' \n" ;
        }
        if(!"".equals(stringDateS))          stringSql  +=  " AND  M10.NeedDate  >=  '"       +  stringDateS          +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  M10.NeedDate <=  '"         +  stringDateE         +"'  \n" ;
        if(!"".equals(stringProjectID1))  {
            if(",017PR,033CRM,033FG,033VIP,".indexOf(stringProjectID1)  !=  -1) {
                stringSql  +=  " AND  M12.DepartNo  =  '"  +  stringProjectID1  +  "' \n" ;   
            } else {
                stringSql  +=  " AND  M12.ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;   
            }
        }
        //        
        stringSql  +=  "  GROUP BY  "+stringSqlItem+"  CostID,  CostID1 \n" ;
        retDoc6M0121  =  dbDoc.queryFromPool(stringSql) ;
        return  retDoc6M0121 ;
    }
    
    // ��� A_Sale
    // ��� A_Project 
    public  String  getProjectName(String  stringProjectID,  talk  dbSale) throws  Throwable {
        String      stringSql                 =  "" ;
        String      stringProjectName  =  "" ;
        String[][]  retAProject             =  null ;
        //
        stringSql  =  " SELECT  ProjectName "  +
                      " FROM  A_Project " +
                     " WHERE  ProjectID  =  '" +  stringProjectID  +  "' "  ;
        retAProject  =  dbSale.queryFromPool(stringSql) ;
        if(retAProject.length  !=  0) {
            stringProjectName  = retAProject[0][0].trim( ) ;
        }
        return  stringProjectName ;
    }
    // ��ڷ~�Z
    public String doDealMoneyForASale(String  stringMessage,  FargloryUtil  exeUtil,  talk  dbSale,  Hashtable  hashtableDealMoney,  Hashtable  hashtableTargetsMoney)throws Throwable{
          String          stringYearLast                                    =  "" ;
          String          stringStartDate                                   =  getValue("StartDate").trim( ) ;
          String          stringEndDate                                    =  getValue("EndDate").trim( ) ;
          String          stringYear                                           =  datetime.getYear(stringStartDate.replaceAll("/",  "")) ;
          String      stringProjectID1                    =  getValue("ProjectID1").trim() ;
          String      stringDealMoney                   =  "" ;
          String      stringTargetsMoney                =  "" ;
          String      stringCDate                     =  "" ;
          String[][]    retData                         =  null ;
          //
          stringYearLast        =  ""+(exeUtil.doParseInteger(stringYear)-1) ;
          // ��ڷ~�Z
              // �e�@�~��
              retData                     =  getDealMoneyForASale(stringProjectID1,  "",  stringYearLast+"/01/01",  stringYearLast+"/12/31",  dbSale) ;
              stringDealMoney      =  convert.FourToFive(""+exeUtil.doParseDouble(retData[0][0].trim()),  0) ;
              stringMessage        +=  "��ڷ~�Z    �e�@�~�סG"+stringDealMoney +  "\n" ;
              hashtableDealMoney.put("�e�@�~��",  stringDealMoney) ;
              // �ײ֭p
              retData                     =  getDealMoneyForASale(stringProjectID1,  "",  "",  stringEndDate,  dbSale) ;
              stringDealMoney      =  convert.FourToFive(""+exeUtil.doParseDouble(retData[0][0].trim()),  0) ;
              stringMessage        +=  "��ڷ~�Z    �ײ֭p�G"+stringDealMoney +  "\n" ;
              hashtableDealMoney.put("�ײ֭p",  stringDealMoney) ;
              // ���~
              retData                =  getDealMoneyForASale(stringProjectID1,  "7",  stringStartDate,  stringEndDate,  dbSale) ;
              //  0 Date  1  DealMoney
              for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
                  stringCDate                          =  retData[intNo][0].trim().replaceAll("/","") ;
                  stringDealMoney                  =  retData[intNo][1].trim() ;
                  //
                  stringMessage        +=  "��ڷ~�Z    "  +  stringCDate+"�G"+stringDealMoney +  "\n" ;
                  hashtableDealMoney.put(stringCDate,  stringDealMoney) ;
              }
          
          // �~�Z�ؼ�
              // �e�@�~��
              retData                     =  getTargetsForAStarMM(stringProjectID1,  "",  stringYearLast+"/01/01",  stringYearLast+"/12/31",  dbSale) ;
              stringTargetsMoney  =  convert.FourToFive(""+exeUtil.doParseDouble(retData[0][0].trim()),  0) ;
              stringMessage        +=  "�~�Z�ؼ�    �e�@�~�סG"+stringTargetsMoney +  "\n" ;
              hashtableTargetsMoney.put("�e�@�~��",  stringTargetsMoney) ;
              // �ײ֭p
              retData                     =  getTargetsForAStarMM(stringProjectID1,  "",  "",  "",  dbSale) ;
              stringTargetsMoney  =  convert.FourToFive(""+exeUtil.doParseDouble(retData[0][0].trim()),  0) ;
              stringMessage        +=  "�~�Z�ؼ�    �ײ֭p�G"+stringTargetsMoney +  "\n" ;
              hashtableTargetsMoney.put("�ײ֭p",  stringTargetsMoney) ;
              // ���~
              retData                 =  getTargetsForAStarMM(stringProjectID1,  "7",  stringYear+"/01/01",  stringYear+"/12/31",  dbSale) ;
              //  0 Date  1  Targets
              for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
                  stringCDate                          =  retData[intNo][0].trim().replaceAll("/","") ;
                  stringTargetsMoney             =  retData[intNo][1].trim() ;
                  //
                  stringMessage        +=  "�~�Z�ؼ�    "  +  stringCDate+"�G"+stringTargetsMoney +  "\n" ;
                  hashtableTargetsMoney.put(stringCDate,  stringTargetsMoney) ;
              }
        return   stringMessage ;
    }
    public String[][] getDealMoneyForASale(String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  talk  dbSale)throws Throwable{
        String      stringSql               =  "" ;
        String      stringSqlItem         =  "" ;
        String      stringDealMoney  =  "" ;
        String[][]  retASale               =  new  String[0][0] ;
        //  0 Date  1  DealMoney
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(10),OrderDate,111),1,"+stringSize+")," ;
        }
        stringSql  =  " SELECT  "+stringSqlItem+"  SUM(DealMoney) \n" +
                                  " FROM  A_Sale  \n"  +
                  " WHERE  ISNULL(ProjectID,'') <>  '' \n" ;
        if(!"".equals(stringDateS))          stringSql  +=  " AND  OrderDate  >=  '" +  stringDateS        +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  OrderDate <=  '"  +  stringDateE        +"'  \n" ;
        if(!"".equals(stringProjectID1)) {
            if("GT".equals(stringProjectID1)) {
                stringSql  +=  " AND  ProjectID1  IN  ( 'GT',  'CT') \n" ;
            } else {
                stringSql  +=  " AND  ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;
            }
        }
        if(!"".equals(stringSize))        stringSql  +=  " GROUP BY  "  +  stringSqlItem.substring(0,stringSqlItem.length()-1) +  " \n" ;
        retASale                =  dbSale.queryFromPool(stringSql) ;
        return  retASale ;
    }
    // ��� A_STarMM
    // �~�Z�ؼ�
    public String[][] getTargetsForAStarMM(String  stringProjectID1,  String  stringSize,  String  stringDateS,  String  stringDateE,  talk  dbSale)throws Throwable{
        String      stringSql                =  "" ;
        String      stringSqlAnd         =  "" ;
        String      stringSqlItem         =  "" ;
        String[][]  retAStarMM           =  new  String[0][0] ;
        //  0 Date  1  Targets
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(10),YearMM,111),1,"+stringSize+")," ;
        }
        stringSql  =  " SELECT  "+stringSqlItem+"  SUM(Targets) \n" +
                                  " FROM  A_STarMM  \n"  +
                               " WHERE  ISNULL(ProjectID,'') <>  '' \n" ;
        if(!"".equals(stringDateS))          stringSql  +=  " AND  YearMM  >=  '" +  stringDateS        +"'  \n" ;
        if(!"".equals(stringDateE))         stringSql  +=  " AND  YearMM <=  '"  +  stringDateE        +"'  \n" ;
        if(!"".equals(stringProjectID1))    stringSql  +=  " AND  ProjectID  =  '"  +  stringProjectID1  +  "' \n" ;
        if(!"".equals(stringSize))        stringSql  +=  " GROUP BY  "  +  stringSqlItem.substring(0,stringSqlItem.length()-1) +  " \n" ;
        retAStarMM    =  dbSale.queryFromPool(stringSql) ;
        return  retAStarMM ;
    }
    
    public  String[][] getRealMoneyHandForZCoReaMM(String  stringProjectID1,  String  stringSize,  String  stringType,  String  stringDateACS,  String  stringDateACE,  FargloryUtil  exeUtil,  talk  dbSale) throws  Throwable {
        String         stringSql                     =  "" ;
        String         stringSqlAnd               =  "" ;
        String         stringSqlItem               =  "" ;
        String[][]     retData                       =   null ;
        //
        if(exeUtil.doParseDouble(stringSize)  >  0)  stringSize  =  "7" ;
        //
        if(!"".equals(stringSize)) {
            stringSqlItem  =  " SUBSTRING(CONVERT(char(10),YYMM,111),1,"+stringSize+"), "   ;
        } else {
            stringSqlItem  =  " "   ;
        }
        // ���(�`��) 
        // 0  YYMM    1  CostID       2  SUM(RealTotalMoney)
        stringSql    =  "SELECT  " + stringSqlItem + "  CostID,  ISNULL(CostID1,  ''),  SUM(RealMoney*10000) \n"  +
                    " FROM  Z_CoReaMM \n"  +
                    " WHERE  ISNULL(CostID,  '')  <>  '' \n" +
                         " AND  (ISNULL(BarCode,  '')  =  ''  OR  ISNULL(Transfer,'')  = '����l��' ) \n"  ;
        if(!"".equals(stringDateACS))             stringSql  +=  " AND  YYMM  >=  '"    +  stringDateACS  +  "' \n" ;
        if(!"".equals(stringDateACE))             stringSql   +=  " AND  YYMM  <=  '"    +  stringDateACE  +  "' \n" ;
        if(!"".equals(stringProjectID1)) {
                stringSql  +=  " AND  ProjectID1  =  '"  +  stringProjectID1  +  "' \n" ;   
        }
        if("Z6".equals(stringType)) {
            stringSql  +=  " AND  ISNULL(ComNo,  '')  IN  ('',  '06') \n" ;
        } else if("CS".equals(stringType)) {
            stringSql  +=  " AND  ISNULL(ComNo,  '')  =  '20' \n" ;
        } else if("01".equals(stringType)) {
            stringSql  +=  " AND  ISNULL(ComNo,  '')  =  '01' \n" ;
        } else if("OTHER".equals(stringType)) {
            stringSql  +=  " AND  ISNULL(ComNo,  '')  NOT  IN  ('',  '06',  '20',  '01') \n" ;
        }
        stringSql  +=  " GROUP BY "+stringSqlItem+"CostID,  ISNULL(CostID1,  '') \n" ;
        retData  =  dbSale.queryFromPool(stringSql) ;
        return  retData;  
  }   
    
    
  public String getInformation(){
      return "---------------button2(\u5217\u5370).defaultValue()----------------";
  }
}
