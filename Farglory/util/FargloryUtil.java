package Farglory.util ;
import jcx.jform.bvalidate;
import  java.io.* ;
import  java.util.*;
import  java.math.BigDecimal;
import  javax.swing.* ;
import  javax.swing.table.* ;
import  jcx.util.*;
import  jcx.net.smtp ;
import  jcx.db.talk ;
import  javax.mail.MessagingException ;
import  javax.print.* ;
// �ŶKï
import  java.awt.Toolkit;
import  java.awt.datatransfer.Clipboard;
import  java.awt.datatransfer.StringSelection;
import  java.awt.datatransfer.*;

public  class  FargloryUtil  extends bvalidate{
      Vector      vectorTxtType        =  new  Vector() ;
      Hashtable   hashtableColumnsData =  new  Hashtable() ;
      int         intMail              =  0 ;
      
      /* ������b�� */
      public  String  convertToHalfWidth(String  stringSource) {
            if (null == stringSource)         return  "";
            if("".equals(stringSource))   return  stringSource ;
             //
            char[] charArray = stringSource.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                  int ic = (int) charArray[i];
                   
                  if (ic >= 65281 && ic <= 65374) {
                        charArray[i] = (char) (ic - 65248);
                  } else if (ic == 12288) {
                        charArray[i] = (char) 32;
                  } 
            }
            return new String(charArray);
      }
      /*** �b������� */
      public String  convertToFullWidth(String  stringSource) {
            if (null == stringSource)         return "";
            if ("".equals(stringSource))  return  stringSource ;
       
            char[] charArray = stringSource.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                  int ic = (int) charArray[i];
                   //
                  if (ic >= 33 && ic <= 126) {
                        charArray[i] = (char) (ic + 65248);
                  } else if (ic == 32) {
                        charArray[i] = (char) 12288;
                  }
            }
            return new String(charArray);
      }
      // �̶ǤJ ��Ұ}�C�A��Ѫ��B�A�^�Ǫ��B�}�C
      public String[] getMoneyFromRatio(String  stringSUM,  int  intPos,  String[][] retRatio)throws Throwable{
            if(intPos  ==  -1)                 return  new  String[0] ;
            if(retRatio.length  ==  0)         return  new  String[0] ;
            if(intPos  >  retRatio[0].length)  return  new  String[0] ;
            //
            String[]  arrayRatio      =  new  String[retRatio.length] ;
            for(int  intNo=0  ;  intNo<retRatio.length  ;  intNo++) {
                     arrayRatio[intNo]  =  retRatio[intNo][intPos].trim() ;
            }
            return  getMoneyFromRatio(stringSUM,  arrayRatio) ;
      }
      public String[] getMoneyFromRatio(String  stringSUM,  String[] arrayRatio)throws Throwable{
            double    doubleSumFinal    =  doParseDouble(stringSUM) ;
            double    doubleSum         =  doubleSumFinal ;
            double    doubleThisRaio    =  0 ;
            double    doubleThisMoney   =  0 ;
            double    dobuleRatioSum    =  0 ;
            double[]  arrayRatioD       =  new  double[arrayRatio.length] ;
            String[]  arrayMoney        =  new  String[arrayRatio.length] ;
            Vector    vectorMoney       =  new  Vector() ;
            // 
            if(doubleSumFinal  <=  0) {
                  System.out.println("[���u�X�p���B] �p�󵥩�0------------------------") ;
                  return  null ;
            }
            // ��ҦX�p
            for(int  intNo=0  ;  intNo<arrayRatio.length  ;  intNo++) {
                  doubleThisRaio  =  doParseDouble(arrayRatio[intNo]) ;
                  //
                  if(doubleThisRaio  ==  0) doubleThisRaio  =  0 ;
                  if(doubleThisRaio  <  0) {
                        System.out.println("����("+(intNo+1)+")����Ҥp�󵥩�0------------------------") ;
                        return  null ;
                  }
                  if(doubleThisRaio  !=  0) {
                        vectorMoney.add(""+doubleThisRaio) ;   
                  }
                  //
                  dobuleRatioSum     +=  doubleThisRaio ;
                  arrayRatioD[intNo]  =  doubleThisRaio ;
            }
            // ��ҭp��
            int  intFlow  =  0 ;
            for(int  intNo=0  ;  intNo<arrayRatioD.length  ;  intNo++) {
                  doubleThisRaio  =  arrayRatioD[intNo] ;
                  //
                  if(doubleThisRaio  ==  0) {
                        arrayMoney[intNo]  =  "0" ;
                        continue ; 
                  } 
                  //
                  if((intFlow+1)  ==  vectorMoney.size()) {
                        // �̫�@��
                        if(doubleSum  <  0) {
                              System.out.println("�̫�@���A�Ѿl���B�p��0------------------------") ;
                              return  null ;
                        }
                        doubleThisMoney    =  doubleSum ;
                        arrayMoney[intNo]  =  convert.FourToFive(""+doubleThisMoney, 0) ;
                  } else {
            				doubleThisMoney    =  doubleSumFinal  *  doubleThisRaio / dobuleRatioSum ;
            				if(doubleThisMoney  <  doubleSum) {
            						arrayMoney[intNo]  =  convert.FourToFive(""+doubleThisMoney,0) ;
            						doubleThisMoney    =  doParseDouble(arrayMoney[intNo]) ;
            						doubleSum         -=  doubleThisMoney ;				
            				} else {
            						arrayMoney[intNo]  =  convert.FourToFive(""+doubleSum,0) ;
            						doubleThisMoney    =  doParseDouble(arrayMoney[intNo]) ;
            						doubleSum               =  0 ;
            				}
                        if(doubleSum  <  0) {
                              System.out.println("����("+(intNo+1)+")�A�Ѿl���B�p��0------------------------") ;
                              return  null ;
                        }
                  }
                  if(doubleThisMoney  <  0) {
                        System.out.println("����("+(intNo+1)+")���p�����B�p��0------------------------") ;
                        return  null ;
                  }
                  intFlow++ ;
            }
            //
            return  arrayMoney ;
      }
      public String[] getOrderList(String[] arrayData)throws Throwable{
             return  getOrderList("MAX",  arrayData) ;
      }
      public String[] getOrderList(String  stringType,  String[] arrayData)throws Throwable{
             int       intPOS          = 0 ;
             String    stringValue     =  "" ;
             String    stringValueMax  =  "" ;
             String[]  arrayOrderPOS   =  new  String[arrayData.length] ;  for(int  intNo=0  ;  intNo<arrayOrderPOS.length  ;  intNo++) arrayOrderPOS[intNo] = "" ;
             String[]  arrayOrderList  =  doCopyArray(arrayData) ;
             //
      		for(int  intNo=0  ;  intNo<arrayData.length  ;  intNo++) {
      				stringValueMax  =  "" ;
      				intPOS          =  -1  ;
      				// ���o�̤j��
      				for(int  intNoL=0  ;  intNoL<arrayData.length  ;  intNoL++) {
      						stringValue  =  arrayData[intNoL].trim() ;  if(doParseDouble(stringValue)   ==  0)  continue ;
      						//
      						if(!"".equals(arrayOrderPOS[intNoL]))   	continue ;
      						//
                        if("MAX".equals(stringType)) {  
            						if(doParseDouble(stringValue)  >  doParseDouble(stringValueMax)) {
            								stringValueMax   =  stringValue ;
            								intPOS           =  intNoL ;
            						}
                        } else {
            						if("".equals(stringValueMax)  ||  doParseDouble(stringValue)  <  doParseDouble(stringValueMax)) {
            								stringValueMax   =  stringValue ;
            								intPOS           =  intNoL ;
            						}                           
                        }
      				}
      				if(intPOS  ==  -1)  continue ;
      				arrayOrderPOS[intPOS]  =  ""+(intNo+1) ;
      				// �ۦP�̤j�� �]�w
      				for(int  intNoL=0  ;  intNoL<arrayData.length  ;  intNoL++) {
      						stringValue  =  arrayData[intNoL].trim() ;  if(doParseDouble(stringValue)   ==  0)  continue ;
      						//
      						if(!"".equals(arrayOrderPOS[intNoL]))   	continue ;
      						//
      						if(doParseDouble(stringValue)  ==  doParseDouble(stringValueMax)) {
      								arrayOrderPOS[intNoL]  =  ""+(intNo+1) ;
      						}
      				}
      		}
            return  arrayOrderPOS ;
             
      }
      // �P�_�j��������
      public String isCheckChinaID(String stringID,  String  stringBirthday,  String  stringSex)throws Throwable{
             // ������18��r���զ�
             if(stringID.length()  !=  18) {
                     return  "[�j��������] �j�p���~�C" ; 
             }
             // �e17�쥲���Ʀr
             if(doParseDouble(stringID.substring(0,17))  <=  0) {
                     return  "[�j��������] �e17�쥲���Ʀr�C" ; 
             }
             // ��7-14�쥲�������X�ͤ��(YYYYMMDD)
             if(!"".equals(stringBirthday)  &&  stringID.substring(6, 14).equals(stringBirthday.replaceAll("/",  ""))) {
                     return  "[�j��������] ��7-14�쥲�������X�ͤ��(YYYYMMDD)�C" ; 
             }
             // ��17�X�Y���_�ƫh���k��(Male)���Ƭ��k��(Female)
             if(!"".equals(stringSex)) {
                    int  intNum  =  doParseInteger(stringID.substring(16, 17))  %  2 ;
                    if("M".equals(stringSex)  &&   intNum == 0) {
                              return  "[�j��������] [�ʧO] �P�_���~�C" ; 
                    }
                    if("F".equals(stringSex)  &&   intNum != 0) {
                              return  "[�j��������] [�ʧO] �P�_���~�C" ; 
                    }
             }
             // �ˮֽX
             int           intSum                =  0 ;
             int           intPos                  =  0 ;
             int[]        arrayIntWeight  =  {7, 9, 10, 5, 8, 4, 2, 1,  6, 3,  7,  9, 10,  5,  8,  4, 2} ;
             String[]  arrayData           =  {"1",  "0",  "X",  "9",  "8",  "7",  "6",  "5",  "4",  "3",  "2"} ;
             for(int  intNo=0  ;  intNo<stringID.length()-1  ;  intNo++) {
                     intSum  +=  doParseInteger(stringID.substring(intNo,  intNo+1)) * arrayIntWeight[intNo] ;
             }
             intPos  =  intSum % 11 ;
             if(!arrayData[intPos].equals(stringID.substring(17,  18))) {
                     return  "[�j��������] �޿���~�C" ; 
             }
             //
             return  "OK" ;
      }
      // �O�Ʀr�^�� true�A�_�h�^�� false�C
      public boolean check(String value)throws Throwable{
            return false;
      }
      public  boolean  isDigitNum (String   stringValue) throws  Throwable {
            boolean  booleanRet  =  true ;
            char     charWord    =  'A' ;
            char[]   arrayChar   =  stringValue.toCharArray() ;
            if(".".equals(stringValue))  return  false ;
            if("".equals(stringValue))   return  false ;
            for(int  intNo=0  ;  intNo<arrayChar.length  ;  intNo++) {
                  charWord  =  arrayChar[intNo] ;
                  if(".".equals(""+charWord)&& stringValue.substring(intNo+1).indexOf(".")==-1)  {continue ;}
                  if(!Character.isDigit(charWord)){
                        booleanRet  =  false ;
                        break ;
                  }
            }
            return booleanRet ;
      }
      public  String  getNormalFont (String   stringValue) throws  Throwable {
            String   stringBigFont        =  "�ϢТѢҢӢԢբ֢עآ٢ڢۢܢݢޢߢ��������袰������������������" ;
            String   stringSmallFont  =  "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890" ;
            String   stringChar             =  "" ;
             int        intPos                    =  0 ;
            int        intByte                   =  convert.StrToByte(stringValue).length() ;
            int        intString                 =  stringValue.length() ;
            //
            if(intByte  ==  intString)  return  stringValue ;
            //
            //System.out.println("��stringValue("+stringValue+")---------------------------") ;
            for(int  intNo=0  ;  intNo<stringValue.length()  ;  intNo++) {
                  stringChar  =  doSubstring(stringValue,  intNo,  intNo+1) ;
                  //
                  intPos       =  stringBigFont.indexOf(stringChar) ;                  if(intPos  ==  -1)  continue ;
                  //System.out.println("("+stringBigFont.substring(intPos, intPos+1)+")("+stringSmallFont�I.substring(intPos, intPos+1)+")---------------------------") ;
                  stringValue  =  stringValue.replaceAll(stringBigFont.substring(intPos, intPos+1),  stringSmallFont.substring(intPos, intPos+1)) ;
                  //
                  //System.out.println(intNo+"stringValue("+stringValue+")---------------------------") ;
            }
            //System.out.println("��stringValue("+stringValue+")---------------------------") ;
            return stringValue;
      }
      public  Vector  getClientPrint (String   stringPrintName) throws  Throwable {
               int     intPos      =  0 ;
               String  stringLine  =  "" ;
               String  stringTemp  =  "" ;
               Vector  vectorIP    =  new  Vector() ;
               //
               if(!"".equals(stringPrintName))  stringPrintName  =  convert.StrToByte(stringPrintName) ;
               try {
                     // ���O�r�� cmd �O DOS �Ҧ� /c �᭱���O���O <-- /c �� JAVA �S�����Y�A�O CMD �����O
                     // �q�`�O�� 1 �ӫ��O�A�p�G�ݭn�]�ܦh���O��ĳ�� BAT ��
                     String  cmd  =  "reg query \"HKEY_CURRENT_USER\\Software\\Microsoft\\Windows NT\\CurrentVersion\\PrinterPorts\"";
                     // ���o PROCESS
                     Process p = Runtime.getRuntime().exec(cmd);
                     // �N������O���^�ǰT���e�� STREAM
                     BufferedReader  input  =  new BufferedReader(new InputStreamReader(p.getInputStream( ))) ;
                     //PARSE STREAM���
                     while ((stringLine = input.readLine())  !=  null) {
                           if("".equals(stringPrintName)) {
                                 vectorIP.add(stringLine.trim()) ;
                           } else {
                                 stringLine  =  convert.StrToByte(stringLine) ;
                                 intPos  =  stringLine.indexOf(stringPrintName) ;
                                 if(intPos  !=  -1) {
                                       stringTemp  =  stringLine.substring(0,  intPos) ;
                                       stringTemp  =  convert.replace(stringTemp,  "\\",  "") ;
                                       vectorIP.add(stringTemp.trim()) ;
                                 }
                           }
                     }
                     input.close( ) ;
               }catch(IOException e) {}
               
               return  vectorIP ;
      }
      public  String[]  doCopyArray(String[]  arraySource) throws  Throwable {
            return  (String[])arraySource.clone() ; 
      }
      public  String[][]  doClone2DArray(String[][]  retData) throws Exception {
            String[]    arrayTemp  =  null ;
            Vector      vectorData =  new  Vector() ;
            //
            for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
                  arrayTemp  =  (String[])retData[intNo].clone() ;
                  vectorData.add(arrayTemp) ;
            }
            return  (String[][])  vectorData.toArray(new  String[0][0]) ;
      }
      public  String[]  doArraySize(String  stringData,  String[]  arrayData) throws Exception {
            String       stringTemp  =  "" ;
            Vector      vectorData   =  new  Vector() ;
            //
            for(int  intNo=0  ;  intNo<arrayData.length  ;  intNo++) {
                  stringTemp  =  arrayData[intNo].trim() ;
                  vectorData.add(stringTemp) ;
            }
            vectorData.add(stringData) ;
            return  (String[])  vectorData.toArray(new  String[0]) ;
      }
      public  String  getFormatNum (String   stringValue) throws  Throwable {
               return  getFormatNum2 (stringValue) ;
       }
      // ( new  Farglory.util.FargloryUtil()).getFormatNum2 (value.trim()) ;
      public  String  getFormatNum2 (String   stringValue) throws  Throwable {
                 String     stringTemp  =  "" ;
                 String     stringType    =  "" ;
                 String[]  arrayTemp   =  null ;
                 //
                 if("".equals(stringValue))  return  "0" ;
                 stringValue  =  convert.E2O(stringValue) ;
                 stringValue  =  doDeleteDogAfterZero (stringValue) ;
                 //
                 arrayTemp  =  convert.StringToken(stringValue,  ".") ;
                 if(arrayTemp.length  ==  2  &&  !"".equals(arrayTemp[1])) {
                        stringTemp  =  arrayTemp[1].trim() ;
                        for(int  intNo=0  ;  intNo<stringTemp.length()  ;  intNo++)  stringType+="9" ;
                 }
                 //
                 stringValue  =  getFormatNum (stringValue,  stringType) ;
                 return  stringValue ;
       }
      // ( new  Farglory.util.FargloryUtil()).getFormatNum (value.trim(),  "99") ;
      public  String  getFormatNum (String   stringValue,  String  stringType) throws  Throwable {
               String       stringRet       =  "0" ;
               String[]     arrayTemp       =  null ;
               boolean      booleanFlag     =  true ;
               double       doubleNumValue  =  doParseDouble(stringValue) ;
               //
               if("".equals(stringType)) {
                     stringType  =  "999,999,999,999" ;
                     stringValue  =  convert.StringToken(stringValue,  ".")[0].trim() ;
               } else {
                     stringType  =  "999,999,999,999."+stringType ;
               }
               if("".equals(stringValue))  return  format.format("0",  stringType).trim( )  ;
               //
               if(doubleNumValue  ==  0)  return  format.format("0",  stringType).trim( )  ;
               //
               stringValue  =  convert.E2O(stringValue) ;
               stringRet    =  format.format(stringValue,  stringType).trim( ) ;
               return  stringRet ;
       }
      public  String  doDeleteDogAfterZero (String   stringValue) throws  Throwable {
            char     charWord    =  'A' ;
            char[]   arrayChar   =  stringValue.toCharArray() ;
            if(stringValue.indexOf(".")  ==  -1)  return  stringValue ;
            if("".equals(stringValue))            return  stringValue ;
            for(int  intNo=arrayChar.length-1  ;  intNo>=0  ;  intNo--) {
                  charWord  =  arrayChar[intNo] ;
                  if(".".equals(""+charWord))           break ;
                  if(doParseDouble(""+charWord)  >  0)  break ;
                  stringValue  =  doSubstring(stringValue,  0,  intNo) ;
            }
            if(stringValue.endsWith(".")) stringValue  =  stringValue.substring(0,  stringValue.length()-1) ;
            return stringValue;
      }
      public  String  getDateMonthEnd (String  stringDate) throws  Throwable {
               String  stringDateRoc   =  "" ;
               String  stringDateAC    =  "" ;
               String  stringMonthEnd  =  "" ;
               String  stringYearType  =  "" ;
               //
               stringDateRoc  =  getDateFullRoc(stringDate,  "123456789") ;
               if(stringDateRoc.length()  ==  9) {
                     // ����~
                     stringYearType  =  "ROC" ;
               } else {
                     stringDateAC  =  getDateAC(stringDate,  "123456789") ;
                     if(stringDateAC.length()  ==  10) {
                        // �褸�~
                        stringYearType  =  "AC" ;
                     } else {
                           System.out.println("����榡���~--------------------1") ;
                           return  "" ;
                     }
               }
               if("ROC".equals(stringYearType)) {
                           stringDateRoc   =  stringDateRoc.replaceAll("/",  "") ;  if(stringDateRoc.length()  !=  7)  return  "" ;
                           stringMonthEnd  =  datetime.getYear(stringDateRoc)+datetime.getMonth(stringDateRoc)+"01" ;
                           stringMonthEnd  =  datetime.dateAdd(stringMonthEnd,  "m",  1) ;
                           stringMonthEnd  =  datetime.dateAdd(stringMonthEnd,  "d",  -1) ;
                           stringMonthEnd  =  getDateConvertFullRoc(stringMonthEnd) ;
                           return  stringMonthEnd ;
               } else if("AC".equals(stringYearType)) {
                           stringDateAC    =  stringDateAC.replaceAll("/",  "") ;  if(stringDateAC.length()  !=  8)  return  "" ;
                           stringMonthEnd  =  datetime.getYear(stringDateAC)+datetime.getMonth(stringDateAC)+"01" ;
                           stringMonthEnd  =  datetime.dateAdd(stringMonthEnd,  "m",  1) ;
                           stringMonthEnd  =  datetime.dateAdd(stringMonthEnd,  "d",  -1) ;
                           stringMonthEnd  =  getDateConvert(stringMonthEnd) ;
                           return  stringMonthEnd ;   
               } 
               System.out.println("����榡���~--------------------2") ;
               return "" ;
      }
      public  String  getDateConvert (String  stringDate) throws  Throwable {
               return getDateConvert (stringDate,  "") ;
      }
      public  String  getDateConvertRoc (String  stringDate) throws  Throwable {
               return getDateConvert (stringDate,  "ac2roc") ;
      }
      public  String  getDateConvertFullRoc (String  stringDate) throws  Throwable {
               stringDate  =  getDateConvert (stringDate,  "ac2roc") ;
               if(stringDate.length()  ==  8)  stringDate  =  "0"+stringDate ;
               return  stringDate ;
      }
      public  String  getDateConvert (String  stringDate, String  stringType) throws  Throwable {
            String  retDate  =  getDateAC (stringDate,  "1234567890")  ;
            if("0".equals(retDate.substring(0,1))  ||  retDate.length()  !=  10) {
                   retDate  =  getDateRoc (stringDate,  "1234567890",  "")  ;     
                   if(retDate.length()==8  ||  (retDate.length()==9  &&  datetime.getYear(retDate.replaceAll("/",  "")).length()==3)) {
                              retDate  =  convert.roc2ac(retDate.replaceAll("/","")) ;
                   } else {
                              return  stringDate;
                   }
            }
            //
            retDate  =  retDate.replaceAll("/","") ;
            if("ac2roc".equals(stringType)) {
                     retDate  =  convert.ac2roc(retDate) ;
            }
            retDate  =  convert.FormatedDate(retDate,  "/") ;
            return retDate;
      }
      public  String  getDateAC (String   value,  String  stringErrorFieldName) throws  Throwable {
            String    stringTmp    =  "";
            String[]  retArray     =  convert.StringToken(value,  "/") ;
            boolean  booleanFlow   =  (retArray.length  !=  3) ;
                     booleanFlow   =  booleanFlow  && ( (retArray.length  ==  1  && value.length()  !=  8)  ||  
                                                        (retArray.length  !=  1) ) ;
            if(booleanFlow) {
                  return "["+stringErrorFieldName+"] ����榡���~(YYYY/MM/DD)�C";
            }
            stringTmp  =  "" ;
            booleanFlow  =  true ;
            if(retArray[0].length()  <  4 )
                  stringTmp  =  "0"  +  retArray[0].trim() ;
            else
                  stringTmp  =  retArray[0].trim() ;
            for(int  intRetVector=1  ;  intRetVector<retArray.length  ;  intRetVector++) {
                  if( retArray[intRetVector].length( )  ==  1) {
                        stringTmp  +=  "/0"  +  retArray[intRetVector] ;
                  } else {
                        stringTmp  +=  "/"  +   retArray[intRetVector] ;
                  }
            }
            if(stringTmp.length()  !=  8  &&  stringTmp.length()  !=  10) { 
                return "["+stringErrorFieldName+"] ����榡���~(YYYY/MM/DD)�C";
            }
            if(stringTmp.length()  ==  8) {
                  stringTmp  =  stringTmp.substring(0,4) +  "/"  +  stringTmp.substring(4,6)  +  "/"  +  stringTmp.substring(6,8) ;
            }
            String retDate  =  stringTmp ;
            stringTmp  = stringTmp.substring(0,4)  +  stringTmp.substring(5,7)  +  stringTmp.substring(8,10) ;
            if(!check.isACDay(stringTmp)) {
                  return "["+stringErrorFieldName+"] ����榡���~(YYYY/MM/DD)�C";
            }
            return retDate;
      }
      // 2 ���������ơA�p 201401 �P 201403 �����`�@���j3�Ӥ��
      public  int  getMonthCount (String   stringYYYYMMStart,  String  YYYYMMEnd) throws  Throwable {
            int       intMonthCount    =  0 ;
            String    stringYearStart  =  ""  ;
            String    stringMonthStart =  ""  ;
            String    stringDateStart  =  getDateConvert(stringYYYYMMStart+"01") ;
            String    stringYearEnd    =  ""  ;
            String    stringMonthEnd    =  ""  ;
            String    stringDateEnd    =  getDateConvert(YYYYMMEnd+"01") ;
            //
            if(stringDateStart.length()  !=  10)                return  intMonthCount ;
            if(stringDateEnd.length()  !=  10)                  return  intMonthCount ;
            if(stringDateStart.compareTo(stringDateEnd)  >  0)  return  intMonthCount ;
            //
            stringYearStart  =  datetime.getYear(stringDateStart.replaceAll("/",  "")) ;
            stringMonthStart =  datetime.getMonth(stringDateStart.replaceAll("/",  "")) ;
            stringYearEnd    =  datetime.getYear(stringDateEnd.replaceAll("/",  "")) ;
            stringMonthEnd   =  datetime.getMonth(stringDateEnd.replaceAll("/",  "")) ;
            //
            if(stringYearStart.equals(stringYearStart)) {
                     // �P�~
                     intMonthCount  =  doParseInteger(stringMonthEnd)  -  doParseInteger(stringMonthStart) + 1 ;
            } else {
               
                     // ���P�~ 
                     intMonthCount   =  12  -  doParseInteger(stringMonthStart) + 1 ;
                     intMonthCount  +=  doParseInteger(stringMonthEnd) ;
            }
            return intMonthCount;
      }
      // �P�_ if(!(stringVoucherYMD.length()==8  ||  (stringVoucherYMD.length()==9  &&  datetime.getYear(stringVoucherYMD.replaceAll("/",  "")).length()==3))) {
      public  static String  getDateFullRoc (String   stringRocDate,  String  stringErrorFieldName) throws  Throwable {
               String  stringDate  =  getDateRoc (stringRocDate,  stringErrorFieldName) ;
               if(stringDate.length()  ==  8)  stringDate  =  "0"+stringDate ;
               return  stringDate ;
      }
      public  static String  getDateRoc (String   stringRocDate,  String  stringErrorFieldName) throws  Throwable {
               return  getDateRoc (stringRocDate,  stringErrorFieldName,  "CHECK")  ;
      }
      public  static String  getDateRoc (String   stringRocDate,  String  stringErrorFieldName, String  stringType) throws  Throwable {
            String      stringTmp       =  "";
            String[]   retArray            =  convert.StringToken(stringRocDate,  "/") ;
            boolean  booleanFlow  =  false ;
            //
            if(retArray.length  !=  3) {
                  booleanFlow  =  (retArray.length  ==  1  && (stringRocDate.length()  ==  6  ||  stringRocDate.length()  ==  7)) ;
                     if(!booleanFlow) {
                           return "["+stringErrorFieldName+"] ����榡���~(YY/MM/DD)�C";
                     }
            }
            stringTmp    =  "" ;
            booleanFlow  =  true ;
            if(retArray[0].length()  <  2 )
                  stringTmp  =  convert.add0(retArray[0].trim( ),  ""+2) ;
            else
                  stringTmp  = retArray[0] ;
            for(int  intReArray=1  ;  intReArray<retArray.length  ;  intReArray++) {
                  if(retArray[intReArray].length( )  ==  1) {
                        stringTmp  +=  convert.add0(retArray[intReArray].trim( ),  ""+2) ;
                  }else {
                          stringTmp  +=  retArray[intReArray].trim( ) ;
                  }
            }
            String retDate  =  convert.FormatedDate(stringTmp,  "/") ;
            //
            if(!check.isRocDay(retDate.replaceAll("/",""))) {
                  return "["+stringErrorFieldName+"] ����榡���~(YY/MM/DD)�C";
            }
            if("CHECK".equals(stringType)) {
                   if(retDate.length()  ==  8) retDate  =  "0"+retDate ;
                   if("050/01/01".compareTo(retDate) >= 0) {
                           return "�����\��J���� 50 �~�e������C";  
                   }
            }
            return retDate;
      }
      public  String  getDateBetweenQueryFullRoc (String  stringDate) throws  Throwable {
               return  getDateBetweenQuery ("FullRoc",  stringDate) ;
      }
      public  String  getDateBetweenQueryRoc (String  stringDate) throws  Throwable {
               return  getDateBetweenQuery ("Roc",  stringDate) ;
      }
      public  String  getDateBetweenQuery (String  stringDate) throws  Throwable {
               return  getDateBetweenQuery ("AC",  stringDate) ;
      }
      public  String  getDateBetweenQuery (String  stringType,  String  stringDate) throws  Throwable {
                String[]  arrayDate   =  convert.StringToken(stringDate,  "and") ;
                if("".equals(stringDate)  ||  arrayDate.length  !=  2)  return  "" ;
                //
                  String         stringDateStart  =  arrayDate[0].trim() ;
                  String         stringDateEnd    =  arrayDate[1].trim() ;
                  if("".equals(stringDateStart))  stringDateStart  =  stringDateEnd ;
                  if("".equals(stringDateEnd))    stringDateEnd    =  stringDateStart ;
                  if("FullRoc".equals(stringType)) {
                        stringDateStart  =  getDateConvertFullRoc(stringDateStart) ;
                        stringDateEnd    =  getDateConvertFullRoc(stringDateEnd) ;          
                  } else if("Roc".equals(stringType)) {
                        stringDateStart  =  getDateConvertRoc(stringDateStart) ;
                        stringDateEnd    =  getDateConvertRoc(stringDateEnd) ;
                   } else {
                        stringDateStart  =  getDateConvert(stringDateStart) ;
                        stringDateEnd    =  getDateConvert(stringDateEnd) ;          
                   }
                   return  stringDateStart+" and "+stringDateEnd ;
      }
      // �@��ϥ�
      public  void  doMessageEMail(String  stringTitle,  String  stringContent,  String  stringUser)  throws Throwable{
             if("".equals(stringUser))  return ;
            //
            String[]  arrayUser      =  {stringUser} ;
             doMessageEMail(stringTitle,  stringContent,  arrayUser) ;
      }
      public  void  doMessageEMail(String  stringTitle,  String  stringContent,  String[]  arrayUser)  throws Throwable{
               String    stringSend      =  "emaker@farglory.com.tw" ;
               //
               doAllEMailFinal(stringTitle,  stringContent,  stringSend,  null,  null,  "",  arrayUser) ;
      }
      public  void  doEMail(String  stringSubject,  String  stringContentL,  String  stringSend,  String[]  arrayUser)  throws Throwable{
            doEMail(stringSubject,  stringContentL,  stringSend,  "",  null,  "",  arrayUser) ;
      }
      public  void  doEMail(String  stringSubject,  String  stringContentL,  String  stringSend,  String  stringErrSend,  String[]  arrayFileName,  String  stringFilePath,  String[]  arrayUser)  throws Throwable{
            String[]  arrayUserL  =  new  String[1] ;
            for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                  arrayUserL[0]  =  arrayUser[intNo] ;
                  doAllEMail(stringSubject,  stringContentL,  stringSend,  stringErrSend,  arrayFileName,  stringFilePath,  arrayUserL) ;
            }
      }
      public  void  doAllEMail(String  stringSubject,  String  stringContentL,  String  stringSend,  String  stringErrSend,  String[]  arrayFileName,  String  stringFilePah,  String[]  arrayUser)  throws Throwable{
            String[]  arrayErrSend  =  null ;
            if(!"".equals(stringErrSend)) {
                  arrayErrSend  =  new  String[1] ;
                  arrayErrSend[0]  =  stringErrSend ;
            }
            doAllEMailFinal(stringSubject,  stringContentL,  stringSend,  arrayErrSend,  arrayFileName,  stringFilePah,  arrayUser) ;
      }
      public  void  doAllEMailFinal(String  stringSubject,  String  stringContentL,  String  stringSend,  String[]  arrayErrSend,  String[]  arrayFileName,  String  stringFilePah,  String[]  arrayUser)  throws Throwable{
            arrayUser        =  getMailTail(arrayUser) ;          if(arrayUser.length  ==  0)  return ;
            arrayErrSend  =  getMailTail(arrayErrSend) ;
            try{
                // 10.66.25.85
               System.out.println("stringSend("+stringSend+")-----------") ;
               for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                     System.out.println(intNo+"arrayUser("+arrayUser[intNo]+")-----------") ;
               }
                  // 2014-03-21 B3018 �令 10.66.25.85
                  //2014-07-30  B3018  �令 10.66.25.83
                  //2014-08-27  B3018  �令10.66.25.84
                  //2014-10-21 B3018  �令10.66.25.15 (�]�L�k�H�ܤH��)
                  String  sendRS = sendMailbcc("10.66.25.15", stringSend, arrayUser, stringSubject, stringContentL, arrayFileName, stringFilePah, "text/html");
                  if (sendRS.trim().equals("")){
                        System.out.println("Send mail complete !!");
                        intMail  =  0 ;
                  }else{
                        if(intMail  >  0)  {
                              intMail  =  0 ;
                              return ;
                        }
                        System.out.println("Send fail!!"+sendRS);
                        if(arrayErrSend.length>0) {
                            String  stringMessage  =  "" ;
                            for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                                   stringMessage  +=  arrayUser[intNo]+"<br>" ;
                            }
                             stringContentL  +=  "<br> ERROR Code"+sendRS+"<br>���H�e�W��G<br>"+stringMessage ;
                             intMail++ ;
                            doMessageEMail(stringSubject,  stringContentL,  arrayErrSend) ;
                        }
                  }
            }catch(Exception exc1){
                  System.out.println("send fail!!");
                  if(intMail  >  0)  {
                        intMail  =  0 ;
                        return ;
                  }
                  if(arrayErrSend.length>0) {
                      String  stringMessage  =  "" ;
                      for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                          stringMessage  +=  arrayUser[intNo]+"<br>" ;
                      }
                       stringContentL  +=  "<br> ERROR�G"+exc1.toString()+"<br>���H�e�W��G<br>"+stringMessage ;
                       intMail++ ;
                      doMessageEMail(stringSubject,  stringContentL,  arrayErrSend) ;
                  }
            }
      }
      public  String[]  getMailTail(String[]  arrayUser)  throws Throwable{
            if(arrayUser  ==  null)  return  new  String[0] ;
            //
            String   stringUser   =  "" ;
            Vector   vectorUser   =  new  Vector() ;
            for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                stringUser  =  arrayUser[intNo].trim() ;
               if("".equals(stringUser))  continue ;
               if(stringUser.indexOf("@")  ==  -1)   stringUser=stringUser+"@farglory.com.tw" ;
               vectorUser.add(stringUser) ;
            }
            return  (String[])  vectorUser.toArray(new  String[0]) ;
      }
      // �妸�ϥ�
      public  void  doMessageMail(String  stringTitle,  String  stringContent,  String  stringUser)  throws Throwable{
             if("".equals(stringUser))  return ;
            //
            String[]  arrayUser      =  {stringUser} ;
             doMessageMail(stringTitle,  stringContent,  arrayUser) ;
      }
      public  void  doMessageMail(String  stringTitle,  String  stringContent,  String[]  arrayUser)  throws Throwable{
            String  stringSend   =  "emaker@farglory.com.tw" ;
            //
            doAllMailFinal(stringTitle,  stringContent,  stringSend,  null,  null,  "",  arrayUser) ;
      }
      public  void  doMail(String  stringSubject,  String  stringContentL,  String  stringSend,  String[]  arrayUser)  throws Throwable{
               doMail(stringSubject,  stringContentL,  stringSend,  "",  null,  "",  arrayUser) ;
      }
      public  void  doMail(String  stringSubject,  String  stringContentL,  String  stringSend,  String  stringErrSend,  String[]  arrayFileName,  String  stringFilePath,  String[]  arrayUser)  throws Throwable{
             String[]  arrayUserL  =  new  String[1] ;
             for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                    arrayUserL[0]  =  arrayUser[intNo] ;
                    doAllMail(stringSubject,  stringContentL,  stringSend,  stringErrSend,  arrayFileName,  stringFilePath,  arrayUserL) ;
             }
      }
      public  void  doAllMail(String  stringSubject,  String  stringContent,  String  stringSend,  String  stringErrSend,  String[]  arrayFileName,  String  stringFilePath,  String[]  arrayUser)  throws Throwable{
            String[]  arrayErrSend  =  null ;
            if(!"".equals(stringErrSend)) {
                  arrayErrSend      =  new  String[1] ;
                  arrayErrSend[0]  =  stringErrSend ;
            }
             doAllMailFinal(stringSubject,  stringContent,  stringSend,  arrayErrSend,  arrayFileName,  stringFilePath,  arrayUser) ;  
      }
      // �զW��  emake@farglory.com.tw
      public  void  doAllMailFinal(String  stringSubject,  String  stringContent,  String  stringSend,  String[]  arrayErrSend,  String[]  arrayFileName,  String  stringFilePath,  String[]  arrayUser)  throws Throwable{
            String  stringSubjectL   =  convert.StrToByte(stringSubject) ;    
            String  stringContentL  =  convert.StrToByte(stringContent) ;
                               arrayUser            =  getMailTail(arrayUser) ;     if(arrayUser.length==0)  return ;   
                              arrayErrSend      =  getMailTail(arrayErrSend) ;
            try{
                  // 2014-03-21 B3018 �令 10.66.25.85
                  //2014-07-30  B3018  �令 10.66.25.83
                  //2014-08-27  B3018  �令10.66.25.84
                  // 2014-10-21 B3018  �令10.66.25.15 (�]�L�k�H�ܤH��)
                  String  sendRS = smtp.sendMailbcc("10.66.25.15", stringSend, arrayUser, stringSubjectL, stringContentL, arrayFileName, stringFilePath, "text/html");
                  if(",550,".indexOf(","+sendRS+",")  !=  -1) {
                        // 550
                        return ;
                  } else if (sendRS.trim().equals("")){
                        System.out.println("Send mail complete !!");
                        intMail  =  0 ;
                  }else{
                        System.out.println("Send fail!!"+sendRS);
                        if(intMail  >  0)  {
                              intMail  =  0 ;
                              return ;
                        }
                        if(arrayErrSend.length  > 0) {
                               String  stringMessage  =  "" ;
                               for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                                       stringMessage  +=  arrayUser[intNo]+"<br>" ;
                               }
                               stringContentL  =  stringContent+"<br> ERROR Code"+sendRS+"<br>���H�e�W��G<br>"+stringMessage ;
                                intMail++ ;
                               doMessageMail(stringSubject,  stringContentL,  arrayErrSend) ;
                        }
                  }
            }catch(Exception exc1){
                  System.out.println("send fail!!");
                  if(arrayErrSend.length  > 0) {
                     if(intMail  >  0)  {
                           intMail  =  0 ;
                           return ;
                     }
                      String  stringMessage  =  "" ;
                      for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                          stringMessage  +=  arrayUser[intNo]+"<br>" ;
                      }
                      stringContentL  =  stringContent+"<br> ERROR�G"+exc1.toString()+"<br>���H�e�W��G<br>"+stringMessage ;
                       intMail++ ;
                      doMessageMail(stringSubject,  stringContentL,  arrayErrSend) ;
                  }
            }
      }
      public  void  doAllMailFinalUTF8(String  stringSubject,  String  stringContent,  String  stringSend,  String[]  arrayErrSend,  String[]  arrayFileName,  String  stringFilePath,  String[]  arrayUser)  throws Throwable{
            String  stringSubjectL   =  stringSubject ;    
            String  stringContentL  =  stringContent ;
                               arrayUser            =  getMailTail(arrayUser) ;     if(arrayUser.length==0)  return ;   
                              arrayErrSend      =  getMailTail(arrayErrSend) ;
            try{
                  // 2014-03-21 B3018 �令 10.66.25.85
                  //2014-07-30  B3018  �令 10.66.25.83
                  //2014-08-27  B3018  �令10.66.25.84
                  // 2014-10-21 B3018  �令10.66.25.15 (�]�L�k�H�ܤH��)
                  String  sendRS = smtp.sendMailccUTF8("10.66.25.15", stringSend, arrayUser, stringSubjectL, stringContentL, arrayFileName, stringFilePath, "text/html");
                  if(",550,".indexOf(","+sendRS+",")  !=  -1) {
                        // 550
                        return ;
                  } else if (sendRS.trim().equals("")){
                        System.out.println("Send mail complete !!");
                        intMail  =  0 ;
                  }else{
                        System.out.println("Send fail!!"+sendRS);
                        if(intMail  >  0)  {
                              intMail  =  0 ;
                              return ;
                        }
                        if(arrayErrSend.length  > 0) {
                               String  stringMessage  =  "" ;
                               for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                                       stringMessage  +=  arrayUser[intNo]+"<br>" ;
                               }
                               stringContentL  =  stringContent+"<br> ERROR Code"+sendRS+"<br>���H�e�W��G<br>"+stringMessage ;
                                intMail++ ;
                               doMessageMail(stringSubject,  stringContentL,  arrayErrSend) ;
                        }
                  }
            }catch(Exception exc1){
                  System.out.println("send fail!!");
                  if(arrayErrSend.length  > 0) {
                     if(intMail  >  0)  {
                           intMail  =  0 ;
                           return ;
                     }
                      String  stringMessage  =  "" ;
                      for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                          stringMessage  +=  arrayUser[intNo]+"<br>" ;
                      }
                      stringContentL  =  stringContent+"<br> ERROR�G"+exc1.toString()+"<br>���H�e�W��G<br>"+stringMessage ;
                       intMail++ ;
                      doMessageMail(stringSubject,  stringContentL,  arrayErrSend) ;
                  }
            }
      }
      // e-office E-mail �q��
      // �s��� ��Ʈw IntraWare��
      // arrayUser(���H��s-���u�s��)    stringSend(�e�H��-���u�s��)  stringSendView(�e�H�̦W��)    stringBody(�D�n���e)
      // ��Ʈw IntraWare
      // exeUtil.doExtWebMail("TEST",   "TEST_CONTENT",  "B3018", "���H�q",  arrayEmp,  dbIntraWare)  ;
      public  void  doExtWebMail(String  stringSubject,  String  stringContent,  String  stringSend,  String  stringSendView,  String[]  arrayUser,  talk  dbIntraWare) throws  Throwable {
            String      stringSql      =  "" ;
            String      stringEmp      =  "" ;
            String      stringIndate   =  datetime.getTime("YYYY/mm/dd h:m:s") ;
            Vector      vectorSql      =  new  Vector() ;
            //
            stringContent  =  convert.ToSql(stringContent) ;
            //
            for(int  intNo=0  ;  intNo<arrayUser.length  ;  intNo++) {
                  stringEmp    =  arrayUser[intNo] ;
                  stringSql    =  "INSERT  INTO  extwebmail_mail(employ_no,  folder_kn,  fromname,  fromaddr,  rcptshort, "  +
                                                                                                            " inrcpt,     indate,     subject,   body,      charset, "  +
                                                                        " priority,   ishtml,     mailsize,  isread,    isconfirm, "  +
                                                                        " mailstatus) "  +
                                                           " VALUES( '"  +  stringEmp             +  "', "  +     // employ_no
                                                                             " '"  +  "@.mail"              +  "', "  +    // folder_kn
                                                                     " '"  +  stringSendView   +  "', "  +    // fromname �H��H
                                                                     " '"  +  stringSend            +  "', "  +    // fromaddr
                                                                     " '"  +  stringEmp             +  "', "  +    // rcptshort
                                                                       " '"  +  stringEmp             +  "', "  +    // inrcpt
                                                                       " '"  +  stringIndate         +  "', "  +    // indate
                                                                       " '"  +  stringSubject       +  "', "  +    // subject
                                                                       " '"  +  stringContent      +  "', "  +    // body
                                                                       " '"  +  "BIG5"                   +  "', "  +    // charset
                                                                     " "  +   "3"                          +  ", "   +    // priority
                                                                     " "  +   "1"                          +  ", "   +    // ishtml
                                                                     " "  +   "100"                     +  ", "   +    // mailsize
                                                                     " "  +   "0"                         +  ", "   +    // isread
                                                                     " "  +   "0"                        +  ", "  +     // isconfirm
                                                                     " "  +   "0"                        +  ") " ;      // mailstatus
                     vectorSql.add(stringSql) ;
            }
            //
            if(vectorSql.size() >  0) dbIntraWare.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
      }
      // ���
      // # + �|��Ʀr + : + ���W��
      public  String  getTableTitleName(String  stringName) throws  Throwable {
            String    stringTemp  =  "" ;
            String    retName     =  "" ;
            String[]  arrayTemp   =  convert.StringToken(stringName,  "#") ;
            String[]  arrayTempL  =  null ;
            for(int  intNo=0  ;  intNo<arrayTemp.length  ;  intNo++) {
                  stringTemp  =  arrayTemp[intNo].trim() ;  if("".equals(stringTemp))  continue ;
                  arrayTempL  =  convert.StringToken(stringTemp,  ":") ;
                  if(arrayTempL.length  !=  2)  continue ;
                  if(!"".equals(retName))  retName  +=  "\n" ;
                  retName     +=  arrayTempL[1] ;
            }
            if(!"".equals(retName))  stringName  =  retName ;
            return  stringName ;
      }
      public  String  doChangeTableField(JTable  tb1,  String  stringColumnName,  int  intWidthS) throws  Throwable {
            TableColumn  tc               =  tb1.getColumn(stringColumnName); 
            int          intcurrentWidth  =  tc.getPreferredWidth(); 
            //
            System.out.println(stringColumnName+"--------------"+intcurrentWidth) ;
            tc.setPreferredWidth(intWidthS); 
            tc.setMaxWidth(intWidthS) ; 
            tc.setMinWidth(intWidthS) ;
            return  ""+intcurrentWidth ;
      }
      public  String[]  getTableHead(JTable  jTable1,  int  intCol,  String  stringColumnName) throws  Throwable {
              String[]  arrayTableHead  =  getTableHead(jTable1) ;
              //
              if("".equals(stringColumnName))         return arrayTableHead;
              if(intCol  <  0)                        return arrayTableHead;
              if(arrayTableHead.length  <  intCol+1)  return arrayTableHead;
              //
              arrayTableHead[intCol]  =  stringColumnName ;
              //
              return  arrayTableHead ;
      }
      public  String[]  getTableHead(JTable  jTable1) throws  Throwable {
                 int     intCols           =  jTable1.getColumnCount() ;
                 String  stringColumnName  =  "";
                 Vector  vectorColumnName  =  new  Vector() ;
                 for(int  intNo=0  ;  intNo<intCols  ;  intNo++) {
                        stringColumnName  =  jTable1.getColumnName(intNo) ;
                        //
                        vectorColumnName.add(stringColumnName) ;
                 }
               return  (String[])vectorColumnName.toArray(new  String[0]) ;
      }
      // �ƭ�
      public  double  doFourToFive(String  stringNum,  int  intSize) throws  Throwable {
            return  doParseDouble(doFourToFiveS(stringNum,  intSize)) ;
      }
      public  double  doFourToFiveD(String  stringNum,  int  intSize) throws  Throwable {
            return  doParseDouble(doFourToFiveS(stringNum,  intSize)) ;
      }
      public  String  doFourToFiveS(String  stringNum,  int  intSize) throws  Throwable {
            double  doubleRet  =  0 ;
            //
            stringNum  =  convert.FourToFive(stringNum,  intSize+2) ;
            stringNum  =  convert.FourToFive(stringNum,  intSize) ;
            //
            return  stringNum ;
      }
      public  double  doParseDouble(String  stringNum) throws Exception {
            // 
            double  doubleNum  =  0 ;
            //
            if(stringNum==null  ||"".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
            //
            stringNum  =  stringNum.trim();
            //
            try{
                  doubleNum  =  Double.parseDouble(stringNum) ;
            } catch(Exception e) {
                    System.out.println("�L�k��R["  +  stringNum  +  "]�A�^�� 0�C") ;
                  return  0 ;
            }
            return  doubleNum ;
      }
       /** 
        * ���Ѻ�T���[�k�B��C 
        * @param v1  �Q�[�� 
        * @param v2  �[�� 
        * @return    ��ӰѼƪ��M 
        */  
       public static double add(double v1, double v2) {  
              BigDecimal b1 = new BigDecimal(Double.toString(v1));  
              BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        
              return b1.add(b2).doubleValue();  
       }  
       /** 
        * ���Ѻ�T����k�B��C 
        * @param v1  �Q��� 
        * @param v2  ��� 
        * @return    ��ӰѼƪ��t 
        */  
       public static double sub(double v1, double v2) {  
              BigDecimal b1 = new BigDecimal(Double.toString(v1));  
              BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        
              return b1.subtract(b2).doubleValue();  
       }  
       /** 
        * ���Ѻ�T�����k�B��C 
        * @param v1  �Q���� 
        * @param v2  ���� 
        * @return    ��ӰѼƪ��n 
        */  
       public static double mul(double v1, double v2) {  
              BigDecimal b1 = new BigDecimal(Double.toString(v1));  
              BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        
              return b1.multiply(b2).doubleValue();  
       }  
       /** 
        * ���ѡ]�۹�^��T�����k�B��A��o�Ͱ����ɪ����p�ɡA��T�� �p���I�H��10��A�H�᪺�Ʀr�|�ˤ��J�C 
        * @param v1  �Q���� 
        * @param v2  ���� 
        * @return    ��ӰѼƪ��� 
        */  
       public static double div(double v1, double v2) {  
               return div(v1, v2, 10);  
       }  
       /** 
        * ���ѡ]�۹�^��T�����k�B��C��o�Ͱ����ɪ����p�ɡA��scale�Ѽƫ� �w��סA�H�᪺�Ʀr�|�ˤ��J�C 
        * @param v1    �Q���� 
        * @param v2    ���� 
        * @param scale ��ܪ�ܻݭn��T��p���I�H��X��C 
        * @return      ��ӰѼƪ��� 
        */  
       public static double div(double v1, double v2, int scale) {  
              if (scale < 0) {  
                  throw new IllegalArgumentException(  
                          "The scale must be a positive integer or zero");  
              }  
        
              BigDecimal b1 = new BigDecimal(Double.toString(v1));  
              BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        
              return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
       }  
       /** 
        * ���Ѻ�T���p�Ʀ�|�ˤ��J�B�z�C 
        *  
        * @param v      �ݭn�|�ˤ��J���Ʀr 
        * @param scale  �p���I��O�d�X�� 
        * @return       �|�ˤ��J�᪺���G 
        */  
       public static double round(double v, int scale) {  
              if (scale < 0) {  
                  throw new IllegalArgumentException("The scale must be a positive integer or zero");  
              }  
        
              BigDecimal b = new BigDecimal(Double.toString(v));  
              BigDecimal one = new BigDecimal("1");  
        
              return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
       }  
       // �L����˥h�k
       public static double floor(double v) {  
               return Math.floor(v);  
       } 
       // �L����i��k
       public static double ceil(double v) {  
            return Math.ceil(v);  
       } 
       public static int getScale(double v) {  
              BigDecimal b1 = new BigDecimal(Double.toString(v));  
              return b1.scale();  
       }  
      public  int  doParseInteger(String  stringNum) throws Exception {
            // 
            int  intNum  =  0 ;
            if("".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
            try{
                  intNum  =  Integer.parseInt(stringNum) ;
            } catch(Exception e) {
                    System.out.println("�L�k��R["  +  stringNum  +  "]�A�^�� 0�C") ;
                  return  0 ;
            }
            return  intNum ;
      }
      public  float  doParseFloat(String  stringNum) throws Exception {
            // 
            float  floatNum  =  0 ;
            if("".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
            try{
                  floatNum  =  Float.parseFloat(stringNum) ;
            } catch(Exception e) {
                    System.out.println("�L�k��R["  +  stringNum  +  "]�A�^�� 0�C") ;
                  return  0 ;
            }
            return  floatNum ;
      }
      public  long  doParseLong(String  stringNum) throws Exception {
            // 
            long  longNum  =  0 ;
            if("".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
            try{
                  longNum  =  Long.parseLong(stringNum) ;
            } catch(Exception e) {
                    System.out.println("�L�k��R["  +  stringNum  +  "]�A�^�� 0�C") ;
                  return  0 ;
            }
            return  longNum ;
      }
      public  String  doSubstring(String  stringObject,  int  intStart,  int  intEnd) throws Exception {
            String  stringRet  =  "" ;
            // intStart
            if(stringObject.length( )  <  intStart)  return  stringRet ;
            intStart  =  (intStart  <  0) ?  0 :  intStart ;
            // intEnd
            if(intEnd  <  0)  return  stringRet ;
            intEnd  =  (stringObject.length( )  <  intEnd) ?  stringObject.length( ) :  intEnd ;
            stringRet  =  stringObject.substring(intStart,  intEnd) ;
            return  stringRet ;
      }
      public  String  doSubstringByte(String  stringObject,  int  intStart,  int  intEnd) throws Exception {
            String  stringRet   =  "" ;
            String  stringTemp  =  code.StrToByte(stringObject) ;
            // intStart
            if(stringTemp.length( )  <  intStart)  return  stringObject ;
            intStart  =  (intStart  <  0) ?  0 :  intStart ;
            stringRet  =  stringTemp.substring(intStart,  stringTemp.length()) ;
            intStart   =  (stringObject.indexOf(code.ByteToStr(stringRet))  !=  -1) ? intStart : intStart-1 ;
            if(intStart  <  0)  intStart  =  0 ;
            // intEnd
            if(intEnd  <  0)  return  stringObject ;
            intEnd  =  (stringTemp.length( )  <  intEnd) ?  stringTemp.length( ) :  intEnd ;
            stringRet  =  stringTemp.substring(0,  intEnd) ;
            intEnd   =  (stringObject.indexOf(code.ByteToStr(stringRet))  !=  -1) ? intEnd : intEnd-1 ;
            //
            stringRet  =  code.ByteToStr(stringTemp.substring(intStart,  intEnd)) ;
            return  stringRet ;
      }
      public  String[]  doCutStringBySize(int  intSize,  String  stringOrigin)throws  Throwable {
            String    stringFirst       =  stringOrigin ;
            String    stringRemain      =  "" ;
            String[]  retData           =  new  String[2] ;
            //
            if(code.StrToByte(stringOrigin).length()  >  intSize) {
                  stringFirst       =  code.StrToByte(stringOrigin).substring(0,intSize) ;
                  stringFirst       =  code.ByteToStr(stringFirst) ;
                  stringRemain      =  code.StrToByte(stringOrigin).substring(intSize) ;
                  stringRemain      =  code.ByteToStr(stringRemain) ;
                  if(stringOrigin.indexOf(stringFirst)  ==  -1) {
                        stringFirst   =  code.StrToByte(stringOrigin).substring(0,intSize-1) ;
                        stringFirst   =  code.ByteToStr(stringFirst) ;  
                        stringRemain  =  code.StrToByte(stringOrigin).substring(intSize-1) ;
                        stringRemain  =  code.ByteToStr(stringRemain) ;
                  }
            }
            retData[0]  =  stringFirst ;
            retData[1]  =  stringRemain ;
            return  retData ;
            
      }
      public  String[]  doCutStringBySizes(int  intSize,  String  stringOrigin)throws  Throwable {
            Vector    vectorData        =  new  Vector() ;
            String[]  arrayData         =  {"",  stringOrigin} ;
            //
            for(int  intNo=0  ;  intNo<100  ;  intNo++) {
                   arrayData           =  doCutStringBySize(intSize,  arrayData[1]) ; 
                   //
                   vectorData.add(arrayData[0]) ;
                   //
                   if("".equals(arrayData[1]))  break ;

            }
            return  (String[])vectorData.toArray(new  String[0]) ;
            
      }
      public  int  FromSqlGetAIntegerValue(String  stringSql,  talk dbSource) throws Exception {
            return  doParseInteger(FromSqlGetAStringValue(stringSql,  dbSource)) ;
      }
      public  String  FromSqlGetAStringValue(String  stringSql,  talk   dbSource) throws Exception {
            String      stringData  =  "" ;
            String[][]  retData     =  dbSource.queryFromPool(stringSql) ;
            if(retData.length  >  0) {
                  stringData  =  retData[0][0].trim() ;
            }
            return  stringData ;
      }
      //long        longTime1  =  getTimeInMillis( ) ;
      //System.out.println("�ɶ�-----------------"  +  ((longTime2-longTime1)/1000)) ;
      public  long  getTimeInMillis( )throws  Throwable {
            GregorianCalendar  time  =  new  GregorianCalendar( ) ;
            return  time.getTimeInMillis( ) ;
      }
      public  String[]  getArrayDataFromText (String   stringFilePath) throws  Exception {
            int         intCharcount    =  0 ;
            String      stringData      =  "" ;
            String[]    arrayFileData   =  null ;
            Vector      vectorLineData  =  new  Vector() ;
            try{
                  // �}���ɮ�
                  BufferedReader  fileReader  =  new  BufferedReader(new  FileReader(stringFilePath)) ;
                  // Ū�����              intCharcount    =  fileReader.read(arrayData) ;
                  while((stringData = fileReader.readLine())  !=  null) {
                      vectorLineData.add(stringData) ;
                 }
                  // �����ɮ�
                  fileReader.close( ) ;
                  //
            } catch (Exception e) {
                  System.out.println("���~��]�G"+e) ;
                  return  new  String[0] ;
            }
            return  (String[])vectorLineData.toArray(new  String[0]) ;
      }
      public  void  putDataToText (String  stringFilePath,  String   stringFileData) throws  Exception {
            //int         intCharcount    =  0 ;
            //String      stringData      =  "" ;
            //String[]    arrayFileData   =  new  String[0] ;
            try{
                  //char[]  arrayData  = new  char[100] ;
                  // �}���ɮ�
                  FileWriter  fileWriter  =  new  FileWriter(stringFilePath) ;
                  // �g�J���
                  fileWriter.write(stringFileData) ;
                  // �����ɮ�
                  fileWriter.close( ) ;
                  //
            } catch (Exception e) {
                  System.out.println("���~��]�G"+e) ;
            }
      }
      // �w�]���|
      public  String  setDefaultPath(String  stringPathKey,  JFileChooser  fileDialog) throws  Throwable {
               if("".equals(stringPathKey))  return  "" ;
               //
               String         stringFileName          =  "DefaultPath.txt" ;
               String         stringFilePath          =  "C:\\Emaker_Util\\"+stringFileName ;
               String         stringDefaultPath       =  "" ;
               String         stringkey               =  "" ;
               String         stringLimit             =  "%-%" ;
               String[]       arrayTemp               =   null ;
               String[]       retData                 =  getArrayDataFromText (stringFilePath) ;
               for (int  intNo=0  ;  intNo<retData.length  ;  intNo++) { 
                     stringkey  =  retData[intNo].trim() ;
                     if(stringkey.startsWith(stringPathKey)) {
                           arrayTemp          =  convert.StringToken(stringkey,  stringLimit) ;if(arrayTemp.length  !=  2)  return "" ;
                           stringDefaultPath  =  arrayTemp[1].trim() ;
                        if(!"".equals(stringDefaultPath)) {
                              File  dir  =  new  File(stringDefaultPath);fileDialog.setCurrentDirectory(dir)  ;
                        }
                           return stringDefaultPath ;
                     }
               }
               return  "" ;
       }
      public  void  doRecordPath(String  stringPathKey,  String  stringChoicePath,  String  stringDefaultPath) throws  Throwable {
               if("".equals(stringPathKey))  return   ;
               //
               String         stringFileName        =  "DefaultPath.txt" ;
               String         stringFilePath          =  "C:\\Emaker_Util\\" ;
               String         stringFileData          =  "" ;
               String         stringLimit                =  "%-%" ;
               //
               if("".equals(stringChoicePath))  return  ;
               //
               String[]   arrayTemp              =  convert.StringToken(stringChoicePath,  "\\") ;
                                stringChoicePath  =  convert.replace(stringChoicePath,  arrayTemp[arrayTemp.length-1],  "") ;
               if(stringChoicePath.equals(stringDefaultPath))  return ;
               //
               File  fileDir  =  new  File(stringFilePath) ;  if(!fileDir.exists())  fileDir.mkdir() ;
               //
               String     stringkey         =  "" ;
               String[]  retData            =  getArrayDataFromText (stringFilePath+stringFileName) ;
               // �ˬd�O�_�w�s�b�M��� 
               stringFileData  =  stringPathKey +stringLimit+stringChoicePath ;
               for (int  intNo=0  ;  intNo<retData.length  ;  intNo++) { 
                     stringkey  =  retData[intNo].trim() ;
                        //
                     if(stringkey.trim().length()==0)         continue ;
                     if(stringkey.startsWith(stringPathKey))  continue ;
                     //
                     if(!"".equals(stringFileData)) stringFileData  +=  "\n" ;
                     stringFileData  +=  stringkey ;
               } 
               //
               putDataToText (stringFilePath+stringFileName,  stringFileData) ;
      }
      //
      public  String  doNumToTranum (String   stringNum) throws  Exception {
             if("308638000".equals(stringNum))  return  "�ѻ��s�èճ��B�ѸU�åa" ;
             int            intMax                  =  stringNum.trim().length() ;
             String      stringTranum    =  "" ;  
             String      stringTemp         =  "" ;
             String      stringTxt1            =  "" ;
             String      stringTxt2            =  "" ;
             String      stringLast             =  "" ;
             String[]   arrayTraNum      =  {"�s", "��",  "�L" ,  "��",  "�v",  "��",  "��",  "�m",  "��",  "�h"} ;
             boolean   booleanFlow      =  true ;
             Vector      vectorNum         =  new  Vector()  ;
             Vector      vectorNumSign =  new  Vector()  ;
             char          charN                   =  'A' ;
             //
             vectorNum.add("0") ;
             vectorNum.add("1") ;
             vectorNum.add("2") ;
             vectorNum.add("3") ;
             vectorNum.add("4") ;
             vectorNum.add("5") ;
             vectorNum.add("6") ;
             vectorNum.add("7") ;
             vectorNum.add("8") ;
             vectorNum.add("9") ;
             //
             vectorNumSign.add("�B") ;
             vectorNumSign.add("��") ;
             vectorNumSign.add("�a") ;
             vectorNumSign.add("�U") ;
             vectorNumSign.add("��") ;
             for(int  intNo=0  ;  intNo<intMax  ;  intNo++) {
                    stringTemp  =  stringNum.substring(intNo,  intNo+1) ;
                    //System.out.println(intNo+"-------------------"+stringTemp) ;
                    //
                    stringTxt1  =  "" ;
                    if(vectorNum.indexOf(stringTemp)  !=  -1) {
                             if("0".equals(stringTemp)  &&  (intMax  ==  intNo+1)) {
                                    stringTxt1  =  "" ;
                             } else {
                                    stringTxt1  =  arrayTraNum[doParseInteger(stringTemp)] ;
                             }                
                    }
                    //System.out.println(intNo+"(stringTxt1)-------------------["+stringTxt1+"]") ;
                    //
                    booleanFlow  =  !"".equals(stringTxt1)  &&
                                                     (!"�s".equals(stringTxt1)  ||  ((intMax-intNo-1) % 4)==0) ;
                    if(booleanFlow) {
                                 switch(intMax-intNo-1) {
                                         case 9 :  stringTxt2  =  "�B" ;  break ;
                                         case 8 :  stringTxt2  =  "��" ;  break ;
                                         case 7 :  stringTxt2  =  "�a" ;  break ;
                                         case 6 :  stringTxt2  =  "��" ;  break ;
                                         case 5 :  stringTxt2  =  "�B" ;  break ;
                                         case 4 :  stringTxt2  =  "�U" ;  break ;
                                         case 3 :  stringTxt2  =  "�a" ;  break ;
                                         case 2 :  stringTxt2  =  "��" ;  break ;
                                         case 1 :  stringTxt2  =  "�B" ;  break ;
                                         default : stringTxt2  =  "" ;    break ;
                                }
                       }
                     //
               stringLast  =  (!"".equals(stringTranum)) ? stringTranum.substring(stringTranum.length()-1,  stringTranum.length()) : "" ;
                     if(vectorNumSign.indexOf(stringLast)!=-1  &&  stringLast.equals(stringTxt2)) {
                           stringTxt2  =  "" ;
                     }
                     //System.out.println(intNo+"(stringTxt2)-------------------"+stringTxt2) ;
                     booleanFlow  =  ("�s".equals(stringLast) && "�s".equals(stringTxt1)) ||
                                                    ("�s".equals(stringTxt1)  &&  !"".equals(stringTxt2)) ;
                     if(booleanFlow)  stringTxt1  =  "" ;
                     //System.out.println(intNo+"(stringTxt1)-------------------"+stringTxt1) ;
                     //
                     booleanFlow  =  "�s".equals(stringLast)  &&
                                                    (intMax - intNo -1) % 4 ==  0  &&
                                                   ("�s".equals(stringTxt1)  ||  "".equals(stringTxt1)) ;
                     if(booleanFlow)  stringTranum  =  stringTranum.substring(0,  stringTranum.length()-1) ;
                     //System.out.println(intNo+"(stringTranum)-------------------"+stringTranum) ;
                     //
                     stringTranum  +=  stringTxt1  +  stringTxt2 ;
                     //System.out.println(intNo+"(stringTranum)-------------------"+stringTranum) ;
             }
             return  stringTranum ;
      }
      public  String  doNumToTranum2 (String   stringNum) throws  Exception {
             int             intMax                   =  stringNum.trim().length() ;
             String       stringTranum      =  "" ;  
             String       stringTemp          =  "" ;
             String       stringTxt1             =  "" ;
             String       stringTxt2             =  "" ;
             String       stringLast             =  "" ;
             String[]    arrayTraNum      =  {"�s", "�@",  "�G" ,  "�T",  "�|",  "��",  "��",  "�C",  "�K",  "�E"} ;
             boolean   booleanFlow      =  true ;
             Vector      vectorNum          =  new  Vector()  ;
             Vector      vectorNumSign =  new  Vector()  ;
             char         charN                    =  'A' ;
             //
             vectorNum.add("0") ;
             vectorNum.add("1") ;
             vectorNum.add("2") ;
             vectorNum.add("3") ;
             vectorNum.add("4") ;
             vectorNum.add("5") ;
             vectorNum.add("6") ;
             vectorNum.add("7") ;
             vectorNum.add("8") ;
             vectorNum.add("9") ;
             //
             vectorNumSign.add("�Q") ;
             vectorNumSign.add("��") ;
             vectorNumSign.add("�a") ;
             vectorNumSign.add("�U") ;
             vectorNumSign.add("��") ;
             for(int  intNo=0  ;  intNo<intMax  ;  intNo++) {
                    stringTemp  =  stringNum.substring(intNo,  intNo+1) ;
                    //System.out.println(intNo+"-------------------"+stringTemp) ;
                    //
                    stringTxt1  =  "" ;
                    if(vectorNum.indexOf(stringTemp)  !=  -1) {
                             if("0".equals(stringTemp)  &&  (intMax  ==  intNo+1)) {
                                 stringTxt1  =  "" ;
                             } else {
                                 stringTxt1  =  arrayTraNum[doParseInteger(stringTemp)] ;
                             }                
                     }
                    //System.out.println(intNo+"(stringTxt1)-------------------["+stringTxt1+"]") ;
                    //
                    booleanFlow  =  !"".equals(stringTxt1)  &&
                                                   (!"�s".equals(stringTxt1)  ||  ((intMax-intNo-1) % 4)==0) ;
                    if(booleanFlow) {
                              switch(intMax-intNo-1) {
                                      case 9 :  stringTxt2  =  "�Q" ;  break ;
                                      case 8 :  stringTxt2  =  "��" ;  break ;
                                      case 7 :  stringTxt2  =  "�a" ;  break ;
                                      case 6 :  stringTxt2  =  "��" ;  break ;
                                      case 5 :  stringTxt2  =  "�B" ;  break ;
                                      case 4 :  stringTxt2  =  "�U" ;  break ;
                                      case 3 :  stringTxt2  =  "�a" ;  break ;
                                      case 2 :  stringTxt2  =  "��" ;  break ;
                                      case 1 :  stringTxt2  =  "�Q" ;  break ;
                                      default : stringTxt2  =  "" ;    break ;
                                    }
                         }
                     //
                 stringLast  =  (!"".equals(stringTranum)) ? stringTranum.substring(stringTranum.length()-1,  stringTranum.length()) : "" ;
                     if(vectorNumSign.indexOf(stringLast)!=-1  &&  stringLast.equals(stringTxt2)) {
                           stringTxt2  =  "" ;
                     }
                     //System.out.println(intNo+"(stringTxt2)-------------------"+stringTxt2) ;
                     booleanFlow  =  ("�s".equals(stringLast) && "�s".equals(stringTxt1)) ||
                                                     ("�s".equals(stringTxt1)  &&  !"".equals(stringTxt2)) ;
                     if(booleanFlow)  stringTxt1  =  "" ;
                     //System.out.println(intNo+"(stringTxt1)-------------------"+stringTxt1) ;
                     //
                     booleanFlow  =  "�s".equals(stringLast)  &&
                                                    (intMax - intNo -1) % 4 ==  0  &&
                                                    ("�s".equals(stringTxt1)  ||  "".equals(stringTxt1)) ;
                     if(booleanFlow)  stringTranum  =  stringTranum.substring(0,  stringTranum.length()-1) ;
                     //System.out.println(intNo+"(stringTranum)-------------------"+stringTranum) ;
                     //
                     stringTranum  +=  stringTxt1  +  stringTxt2 ;
                     //System.out.println(intNo+"(stringTranum)-------------------"+stringTranum) ;
             }
             return  stringTranum.replaceAll("�s",  "") ;
      }
      // �C���
       public  Vector  getPrinterPosition ( ) throws  Throwable{
               return  getPrinterPosition ("/") ;
       }
       public  Vector  getPrinterPosition (String  stringType) throws  Throwable{
               //�C�X�L����˸m�W��
               PrintService[]  printservice    =  java.awt.print.PrinterJob.lookupPrintServices() ;
               String                 stringPrinter   =  "" ;
               Vector                vectorPrinter  =  new  Vector() ;          
               for (int  i=0  ;  i<printservice.length  ;  i++) {
                      stringPrinter  =  (""+printservice[i]).trim().replace("Win32 Printer : ",  "") ;
                      if("/".equals(stringType))  stringPrinter  =  stringPrinter.replace("\\",  "/") ;
                      //
                      System.out.println("��"+(i+1)+"�x�C���-------["+stringPrinter+"]") ;
                      //
                     vectorPrinter.add(stringPrinter.trim().replace("",  "")) ;
               }
              //
               return  vectorPrinter ;
       }
       // \\misbank\GMCLJ4P
       public  String  getDefaultPrinter () throws  Throwable{
               //�C�X�w�]�L����˸m�W��
               return  (""+java.awt.print.PrinterJob.getPrinterJob().getPrintService()).replace("Win32 Printer : ",  "").trim() ;
       }
       // ���
       public  boolean  isPrinterExist (String  stringPrinter) throws  Throwable{
            return  isPrinterExist (stringPrinter,  "/") ;
       }
       public  boolean  isPrinterExist (String  stringPrinter,  String  stringType) throws  Throwable{
               Vector          vectorPrinter   =  getPrinterPosition (stringType) ;
               String          stringPrinterL  =  "" ;
               for(int  intNo=0  ;  intNo<vectorPrinter.size()  ;  intNo++) {
                     stringPrinterL  =  (""+vectorPrinter.get(intNo)).trim() ;
                     if(stringPrinterL.equals(stringPrinter))  return  true ;
               }
               return  false ;
       }
       // �����j�p�g
       public  boolean  isPrinterExist2 (String  stringPrinter) throws  Throwable{
               return  isPrinterExist2 (stringPrinter,  "/") ;
       }
       public  boolean  isPrinterExist2 (String  stringPrinter,  String  stringType) throws  Throwable{
               Vector          vectorPrinter   =  getPrinterPosition (stringType) ;
               String          stringPrinterL  =  "" ;
               for(int  intNo=0  ;  intNo<vectorPrinter.size()  ;  intNo++) {
                     stringPrinterL  =  (""+vectorPrinter.get(intNo)).trim() ;
                     if(stringPrinterL.equalsIgnoreCase(stringPrinter))  return  true ;
               }
               return  false ;
       }
       // �ŶKï
       public  void  ClipCopy (String  stringWord) throws  Throwable{
              StringSelection stringselection = new StringSelection(stringWord);
              Clipboard       clipbd          = Toolkit.getDefaultToolkit().getSystemClipboard();
              //
              clipbd.setContents(stringselection, stringselection);
       }
       public  String  ClipPaste ( ) throws  Throwable{
              String       stringWord   =  "" ;
              Clipboard    clipbd       = Toolkit.getDefaultToolkit().getSystemClipboard();
              Transferable transferable = clipbd.getContents(null);
              //
              if(transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                  //System.out.println("�ŶKï["+transferable.getTransferData(DataFlavor.stringFlavor)+"]");
                  stringWord = ""+transferable.getTransferData(DataFlavor.stringFlavor) ;
              }
              return  stringWord ;
       }
       // "YYYY/mm/dd h:m:s
       // exeUtil.getAddTime("2009/04/17 17:43:31",  "s",  1)
      public  String  getAddTime(String  stringDateTime,  String  stringType,  int  intAddNum) throws  Throwable {
            String    stringDate       =  "" ;
            String    stringTime       =  "" ;
            String    stringHour       =  "" ;
            String    stringMinute     =  "" ;
            String    stringSecond     =  "" ;
            String[]  arrayDateTime    =  null ;
            int       intTemp          = 0 ;
            int       intDay           = 0 ;
            int       intHour          = 0 ;
            int       intMinute        = 0 ;
            int       intSecond        = 0 ;
            //
            if(intAddNum  ==  0)                                      return  stringDateTime ;
            if(intAddNum  <  0)										          return  "";
            if("".equals(stringDateTime))                             return  "" ;
            if(",YYYY,mm,dd,s,".indexOf(","+stringType+",")  ==  -1)  return  "" ;
            //
            arrayDateTime     =  convert.StringToken(stringDateTime,  " ") ;     if(arrayDateTime.length  !=  2)  return  "" ;
            stringDate        =  arrayDateTime[0].trim() ;
            stringTime        =  arrayDateTime[1].trim() ;  
            arrayDateTime     =  convert.StringToken(stringTime,     ":") ;     if(arrayDateTime.length  !=  3)  return  "" ;
            stringHour        =  arrayDateTime[0] ;
            stringMinute      =  arrayDateTime[1] ;
            stringSecond      =  arrayDateTime[2] ;
            //
            if(",YYYY,mm,dd,".indexOf(","+stringType+",")  !=  -1) {
                 stringDate  =  datetime.dateAdd(stringDate.replaceAll("/",  ""),  stringType,  intAddNum) ;
                 stringDate  =  getDateConvert(stringDate) ;
            } else if(",h,".indexOf(","+stringType+",")  !=  -1) {
                 if(intAddNum  >  0) {
                        intTemp     =  doParseInteger(stringHour) + intAddNum   ; 
                        intHour     =  intTemp % 24 ;
                        intDay      =  intTemp / 24 ;
                        if(intDay  >  0) {
                                stringDate  =  datetime.dateAdd(stringDate.replaceAll("/",  ""),  "d",  intDay) ;   
                                stringDate  =  getDateConvert(stringDate) ;
                        }
                        stringTime  =  convert.add0(""+intHour,  "2")+":"+convert.add0(""+stringMinute,  "2")+":"+convert.add0(""+stringSecond,  "2") ;
                 } else {
                        intTemp     =  doParseInteger(stringHour) - intAddNum   ; 
                        if(intTemp  <  0) {
                              for(int  intNo=0  ;  intNo<=1000  ;  intNo++) {
                                       intTemp +=  24 ;
                                       intDay++ ;
                                       if(intTemp  >  0) break ;
                              }
                              intHour     =  intTemp ;
                              stringDate  =  datetime.dateAdd(stringDate.replaceAll("/",  ""),  "d",  intDay*-1) ;   
                              stringDate  =  getDateConvert(stringDate) ;
                        }
                        stringTime  =  convert.add0(""+intHour,  "2")+":"+convert.add0(""+stringMinute,  "2")+":"+convert.add0(""+stringSecond,  "2") ;
                 }
            } else if(",m,".indexOf(","+stringType+",")  !=  -1) {
                 if(intAddNum  >  0) {
                        // ��
                        intTemp     =  doParseInteger(stringMinute) + intAddNum   ; 
                        intMinute   =  intTemp % 60 ;
                        intHour     =  intTemp / 60 ;
                        // ��
                        intTemp     =  doParseInteger(stringHour) + intHour   ; 
                        intHour     =  intTemp % 24 ;
                        intDay      =  intTemp / 24 ;
                        if(intDay  >  0) {
                                stringDate  =  datetime.dateAdd(stringDate.replaceAll("/",  ""),  "d",  intDay) ;   
                                stringDate  =  getDateConvert(stringDate) ;
                        }
                        stringTime  =  convert.add0(""+intHour,  "2")+":"+convert.add0(""+intMinute,  "2")+":"+convert.add0(""+stringSecond,  "2") ;
                 } else {
                        // ��
                 }
            } else if(",s,".indexOf(","+stringType+",")  !=  -1) {
                 if(intAddNum  >  0) {
                        // ��
                        intTemp     =  doParseInteger(stringSecond) + intAddNum   ; 
                        intSecond   =  intTemp % 60 ;
                        intMinute   =  intTemp / 60 ;
                        // ��
                        intTemp     =  doParseInteger(stringMinute) + intMinute   ; 
                        intMinute   =  intTemp % 60 ;
                        intHour     =  intTemp / 60 ;
                        // ��
                        intTemp     =  doParseInteger(stringHour) + intHour   ; 
                        intHour     =  intTemp % 24 ;
                        intDay      =  intTemp / 24 ;
                        if(intDay  >  0) {
                                stringDate  =  datetime.dateAdd(stringDate.replaceAll("/",  ""),  "d",  intDay) ;   
                                stringDate  =  getDateConvert(stringDate) ;
                        }
                        stringTime  =  convert.add0(""+intHour,  "2")+":"+convert.add0(""+intMinute,  "2")+":"+convert.add0(""+intSecond,  "2") ;
                 } else {
                        // ��
                 }
            }
            return  stringDate+" "+stringTime ;
      }
       // exeUtil.getSubSecond("2009/04/17 17:43:31  ",  "2009/04/16 06:46:07 ")
      public  long  getSubSecond(String  stringDateTimeAfter,  String  stringDataeTimeBefore) throws  Throwable {
            String  stringDateAfter    =  "" ;
            String  stringTimeAfter    =  "" ;
            String  stringDateBefore   =  "" ;
            String  stringTimeBefore   =  "" ;
            String[]  arrayDateTime    =  null ;
            //
            arrayDateTime     =  convert.StringToken(stringDateTimeAfter,  " ") ;
            stringDateAfter   =  arrayDateTime[0].trim() ;              stringDateAfter  =  getDateConvert(stringDateAfter) ;
            stringTimeAfter   =  arrayDateTime[1].trim() ;  
            arrayDateTime     =  convert.StringToken(stringDataeTimeBefore,  " ") ;
            stringDateBefore  =  arrayDateTime[0].trim() ;              stringDateBefore  =  getDateConvert(stringDateBefore) ;
            stringTimeBefore  =  arrayDateTime[1].trim() ;  
            //System.out.println("stringDateAfter-----------------------["+stringDateAfter+"]") ;
            //System.out.println("stringTimeAfter-----------------------["+stringTimeAfter+"]") ;
            //System.out.println("stringDateBefore-----------------------["+stringDateBefore+"]") ;
            //System.out.println("stringTimeBefore-----------------------["+stringTimeBefore+"]") ;
            //
            int   intDay         =  datetime.subDays1(stringDateAfter.replaceAll("/",  ""),  stringDateBefore.replaceAll("/",  "")) ;
            long  longSecond     =  0 ;
            long  longTimeAfter  =  getTimeToSecond(stringTimeAfter) ;
            long  longTimeBefore =  getTimeToSecond(stringTimeBefore) ;
            long  longDaySecond  =  24  *  60  *  60 ;
            //System.out.println("intDay-----------------------["+intDay+"]") ;
            //System.out.println("longTimeAfter-----------------------["+longTimeAfter+"]") ;
            //System.out.println("longTimeBefore-----------------------["+longTimeBefore+"]") ;
            //
            if(intDay  ==  0) {
                  // �P�@��
                  longSecond  =  longTimeAfter  -  longTimeBefore ; // TimeAfter  -  TimeBefore
            } else if(intDay  >  0) {
                  longSecond  =  (intDay-1)*longDaySecond +  (longDaySecond  -  longTimeBefore)  +  longTimeAfter ;
            } else {
                  return  longSecond ;
            }
            return  longSecond ;
      }
      public  long  getTimeToSecond(String  stringTime) throws  Throwable {
            long       longSecond  =  0 ;  if(stringTime.length()  >  8)  stringTime  =  stringTime.substring(stringTime.length()-8,  stringTime.length()) ;
            String[]   arrayTime        =  convert.StringToken(stringTime,  ":") ; 
            //
            if(arrayTime.length  >=  1) {
                  longSecond  +=  doParseLong(arrayTime[0]) * 60 * 60 ;
            }
            if(arrayTime.length  >=  2) {
                  longSecond  +=  doParseLong(arrayTime[1]) * 60 ;
            }
            if(arrayTime.length  >=  3) {
                  longSecond  +=  doParseLong(arrayTime[2]) ;
            }
            return  longSecond;
      }
      public  String  getSecondToTime(long  longSecond) throws  Throwable {
            String    stringTime     =  "" ;
            long       longTemp     =  0 ;
            // �Ѽ�
            longTemp  =  longSecond  /  (24  *  60  * 60) ;
            //if(longTemp>0)
            stringTime  =  convert.add0(""+longTemp,  "2")  +  "�� " ;
            // �p��
            longSecond  =  longSecond  -  longTemp  *  (24  *  60  * 60) ;
            longTemp     =  longSecond  /  (60  * 60) ;
            if(longTemp>0) {
                  stringTime  +=  convert.add0(""+longTemp,  "2")  +  ":" ;
            } else {
                  stringTime  +=  "00:" ;
            }
            // �� 
            longSecond  =  longSecond  -  longTemp  *  (60  * 60) ;
            longTemp     =  longSecond  /  60 ;
            if(longTemp>0) {
                  stringTime  +=  convert.add0(""+longTemp,  "2")  +  ":" ;
            } else {
                  stringTime  +=  "00:" ;
            }
            // ��
            longTemp  =  longSecond  -  longTemp  *  60 ;
            if(longTemp>0) {
                  stringTime  +=  convert.add0(""+longTemp,  "2")  ;
            } else {
                  stringTime  +=  "00" ;
            }           
            return  stringTime;
      }
      // �ɮ׳B�z
      // stringClientFilePath  �Gg:/��T��/Excel/Doc/"+stringFileName
      // stringSavePath        �GC:\\Emaker_Util\\
      public  String  getExcelFilePath(String  stringClientFilePath,  String  stringSavePath) throws  Throwable {          
            String[]    arrayTemp             =  convert.StringToken(stringClientFilePath,  "/") ;
            String      stringFileName        =  arrayTemp[arrayTemp.length-1].trim() ;
            String      stringClientFilePathL = stringClientFilePath ;
            //
            if("".equals(stringSavePath))  stringSavePath  =  "C:\\Emaker_Util\\" ;
            //
      		if(!(new  File(stringClientFilePathL)).exists()) {
                      stringClientFilePathL  =  getClientFile(stringClientFilePath,  stringFileName,  stringSavePath) ;
      		}
      		if("".equals(stringClientFilePathL)) {
      				stringClientFilePathL  =  "http://emaker.farglory.com.tw:8080/servlet/baServer3?step=6?filename="+stringClientFilePath ;
      		}
            return  stringClientFilePathL ;
      }
      public  String  getClientFile(String  stringServerPath,  String  stringFileName,  String  stringSavePath) throws  Throwable {
      		String          stringClientPath  =  "" ;
      		//
      		if(doSaveFile(stringServerPath,  "Y")) {
      				stringClientPath   =  stringSavePath+stringFileName;
      				return  stringClientPath ;
      		}
      		return  "" ;
      }
      // stringServerPath �ɮצs�� stringClientPath �ݡA�����^�� true�C
      // �� StringReplace �� N �B Client �ɮצs�b�ɡA�N���@�B�z�U���\��C
      // �� StringReplace �� Y ���� Client �ɮצs�b�ɡA�ҳB�z�U���\��C
      // �^���ɮצs�b�P�_�C
      public  boolean  doSaveFile(String  stringServerPath) throws  Throwable {
               return  doSaveFile(stringServerPath,  "Y") ;
      }
      public  boolean  doSaveFile(String  stringServerPath,  String  StringReplace) throws  Throwable {
             String[]  arrayTemp         =  convert.StringToken(stringServerPath,  "\\") ;  if(arrayTemp.length==1)arrayTemp         =  convert.StringToken(stringServerPath,  "/") ; 
             String    stringClientPath  =  "C:\\Emaker_Util" ;    (new  File(stringClientPath)).mkdir() ;
             String    stringFilename    =  arrayTemp[arrayTemp.length-1] ;
             return  doSaveFile(stringServerPath,  stringClientPath+"\\"+stringFilename,  StringReplace) ;
      }
      public  boolean  doSaveFile(String  stringServerPath,  String  stringClientPath,  String  StringReplace) throws  Throwable {
             byte[]    arrayByteFile  =  getByte(stringServerPath) ;   if(arrayByteFile==null)                             return  false ;
             File      fileT          =  new  File(stringClientPath) ; if(!"Y".equals(StringReplace)  &&  fileT.exists())  { System.out.println("["+stringClientPath+"]�w�s�b") ;return  true ;}
              try{
                  FileOutputStream fd = new FileOutputStream(fileT);
                  fd.write(arrayByteFile);
                  fd.close();
              }catch(Exception e){
                  System.out.println("���~�G"+e.toString()) ;
                  return false;
              }
             return  new  File(stringClientPath).exists() ;
      }
       public  void  doBatchMkDir(String  stringPath) throws  Throwable {
               File        file            =  new  File(stringPath) ;
               File        fileTemp   =  null ;
               String    stringFile  =  "" ;
               String[]  arrayDir    =  convert.StringToken(stringPath,  "\\") ;
               //
               if(file.exists())  return ;
               //
               stringFile  =  convert.StringToken(stringPath,  arrayDir[1].trim())[0] ;
               for(int  intNo=1  ;  intNo<arrayDir.length  ;  intNo++) {
                     stringFile  +=  "\\"+arrayDir[intNo].trim() ;
                     fileTemp   =  new  File(stringFile) ;
                     if(!fileTemp.exists()) {
                           if(!fileTemp.mkdir()) {
                                 break ;
                           }
                     } else {
                     }
               }
        }
      /**
      * �R����
      * stringFilePathAndName �ɸ��|�ΦW�� �pc:/fqf.txt
      */
      public  String  doDelFile(String  stringFilePathAndName){
            try {
                  String         stringFilePath    =  stringFilePathAndName;
                                 stringFilePath     =  stringFilePath.toString();
                  java.io.File   fileMyDelFile       = new java.io.File(stringFilePath);
                  fileMyDelFile.delete();
            }
            catch(Exception e) {
                  e.printStackTrace();
                  return  "�R���ɾާ@�X��" ;
            }
            return  "OK" ;
      }
      /**
      * �ƻs�����
      * stringOldPath      ���ɸ��|    �p�Gc:/fqf.txt
      * stringNewPath      �ƻs����| �p�Gf:/fqf.txt
      (�ɦW���ťաA�|�y�����~)
      */
      public  String  doCopyFile(String stringOldPath, String  stringNewPath) throws IOException  {
            InputStream          inStream    =  null ;
            FileOutputStream  fs             =  null ;
            byte[]                    byteBuffer  = new byte[1024];
            int                     intLength ;
            try {
                  int   intByteSum   =  0 ;
                  int   intByteRead  =  0 ;
                  File   fileOld    = new File(stringOldPath);
                  //
                  if (!fileOld.exists()) return "�ɮפ��s�b�A���@�B�z" ;
                  //
                  inStream    =  new FileInputStream(stringOldPath) ;//Ū�J����
                  fs             =  new FileOutputStream(stringNewPath) ;
                  while ( (intByteRead = inStream.read(byteBuffer)) != -1) {
                        intByteSum  +=  intByteRead; //�줸�ռ� �ɮפj�p
                        System.out.println(intByteSum);
                        fs.write(byteBuffer,   0,   intByteRead);
                  }
                  fs.flush() ;
            }catch(IOException e){
                  return  "�ƻs����ɾާ@�X��" ;
            }finally{
                  inStream.close() ;
                  fs.close() ;
            }
            return  "OK" ;
      }
      // �ǲ�
      // ���o    stringKey �� CompanyCd+VoucherYMDRoc(����ǲ�����L�׽u)
      public  String  getVoucher(String  stringKey,  talk  dbFED1) throws  Throwable {
            return getVoucherFlowNo("R",  "A",  stringKey,  "0",  dbFED1) ;
      }
      // ���~�^�_    stringKey �� CompanyCd+VoucherYMDRoc(����ǲ�����L�׽u)
      public  String  doRollBackVoucher(String  stringKey,  String  stringFlowNoO,  talk  dbFED1) throws  Throwable {
            return getVoucherFlowNo("B",  "A",  stringKey,  stringFlowNoO,  dbFED1) ;
      }
      // ���`���� �R���ǲ����X�O��     stringKey �� CompanyCd+VoucherYMDRoc(����ǲ�����L�׽u)
      public  String  doDelVoucherRecord(String  stringKey,  String  stringFlowNoO,  talk  dbFED1) throws  Throwable {
            return getVoucherFlowNo("D",  "A",  stringKey,  stringFlowNoO,  dbFED1) ;
      }
      public  String  getVoucherFlowNo(String  stringStatus,  String  stringNoteType,  String  stringKey,  String  stringFlowNoO,  talk  dbFED1) throws  Throwable {
            String      stringFlowNo     =  "" ;
            if("R".equals(stringStatus)) {
                  // ���o�ǲ��y����  Z �O�̤j��  U �O���b�ϥ�
                  String      stringStatusCd  =  "" ;
                  String[][]  retFED1011      =  null ;
                  // 0  KEY_CD         1  FLOW_NO        2  STATUS_CD
                  retFED1011  =  getFED1011(stringNoteType,  stringKey,  dbFED1) ;
                  if(retFED1011.length  !=  0) {
                        stringFlowNo    =  retFED1011[0][1].trim( ) ;
                        stringStatusCd  =  retFED1011[0][2].trim( ) ;
                        if("Z".equals(stringStatusCd)) {
                              //System.out.println("doUpdateFED1011ForAddFlowNo---------------------") ;
                              doUpdateFED1011ForAddFlowNo(stringFlowNo,  stringNoteType,  stringKey,  dbFED1) ;
                              //System.out.println("doInsertFED1011---------------------") ;
                              doInsertFED1011(stringNoteType,  stringKey,  stringFlowNo,  "U",  dbFED1) ;
                        } else {
                              //System.out.println("doUpdateFED1011ForStatusU---------------------") ;
                              doUpdateFED1011ForStatusU(stringNoteType,  stringKey,  stringFlowNo,  dbFED1) ;
                        }
                  } else {
                        stringFlowNo  =  "0" ;
                        //System.out.println("doInsertFED1011---------------------") ;
                        doInsertFED1011(stringNoteType,  stringKey,  "2",  "Z",  dbFED1) ;
                        //System.out.println("doInsertFED1011---------------------") ;
                        doInsertFED1011(stringNoteType,  stringKey,  "1",  "U",  dbFED1) ;
                        stringFlowNo  =  "1" ;
                  }
            } else if("I".equals(stringStatus)) {
                  stringFlowNo  =  "0" ;
                  doInsertFED1011(stringNoteType,  stringKey,  stringFlowNoO,  " ",  dbFED1) ;
            } else if("D".equals(stringStatus)) {
                  // �����ǲ��y�{�ɡA�R�� U
                  stringFlowNo  =  "0" ;
                  //System.out.println("doDeleteFED1011---------------------") ;
                  doDeleteFED1011(stringNoteType,  stringKey,  stringFlowNoO,  dbFED1) ;
            } else if("B".equals(stringStatus)) {
                  // ���A�M�� �ť�
                  doUpdateFED1011NoneForStatusU(stringNoteType,  stringKey,  stringFlowNoO,  dbFED1) ;
            }
            
            return  stringFlowNo ;
      }
      public  String[][]  getFED1011(String  stringNoteType,  String  stringKey,  talk  dbFED1L) throws  Throwable {
            String      stringSql       =  "" ;
            String[][]  retFED1011  =  null ;
            // 0  KEY_CD         1  FLOW_NO        2  STATUS_CD
            stringSql  =   "SELECT  KEY_CD,  FLOW_NO,  STATUS_CD "  +
                            " FROM  FED1011 "  +
                           " WHERE  NOTE_TYPE  =  '"  +  stringNoteType  +  "' "  +
                             " AND  (KEY_CD  =  '"    +  stringKey       +  "' "  +
                                    " OR  KEY_CD  =  ' "    +  stringKey       +  "' "  +
                                    " OR  KEY_CD  =  '  "   +  stringKey       +  "') "  +
                             " AND  (STATUS_CD  =  ' '  OR  STATUS_CD  =  'Z') "  +
                         " ORDER BY  STATUS_CD,  FLOW_NO " ;
            retFED1011  =  dbFED1L.queryFromPool(stringSql) ;
            return  retFED1011 ;
      }
      public  void  doUpdateFED1011ForAddFlowNo(String  stringFlowNo,  String  stringNoteType,  String  stringKey,  talk  dbFED1L) throws  Throwable {
            String  stringSql  =  "" ;
            //
            stringSql  =  "UPDATE  FED1011  SET  FLOW_NO  =  "  +  (Integer.parseInt(stringFlowNo) + 1) +
                          " WHERE  STATUS_CD  =  'Z' "  +
                            " AND  NOTE_TYPE  =  '"  +  stringNoteType  +  "' "  +
                            " AND  (KEY_CD  =  '"    +  stringKey       +  "' "  +
                                  " OR  KEY_CD  =  ' "    +  stringKey       +  "' "  +
                                  " OR  KEY_CD  =  '  "   +  stringKey       +  "') " ;
            dbFED1L.execFromPool(stringSql) ;
      }
      public  void  doInsertFED1011(String  stringNoteType,  String  stringKey,  String  stringFlowNo,  String  stringStatus,  talk  dbFED1L) throws  Throwable {
            String  stringSql  =  "" ;
            //
            stringSql  =  "INSERT  FED1011  (NOTE_TYPE, KEY_CD, FLOW_NO, STATUS_CD)" +
                                          " VALUES( '"   + stringNoteType  +  "', "  +
                                                  " '"   + stringKey       +  "', "  +
                                                   " "   + stringFlowNo    +  ", "  +
                                                  " '"   +  stringStatus   +  "') " ;
            dbFED1L.execFromPool(stringSql) ;
      }
      public  void  doUpdateFED1011ForStatusU(String  stringNoteType,  String  stringKey,  String  stringFlowNo,  talk  dbFED1L) throws  Throwable {
            String  stringSql  =  "" ;
            //
            stringSql  =  "UPDATE  FED1011  SET  STATUS_CD  =  'U' "  +
                          " WHERE  STATUS_CD  =  ' ' "  +
                            " AND  NOTE_TYPE  =  '"  +  stringNoteType  +  "' "  +
                            " AND  (KEY_CD  =  '"  +  stringKey  +  "' "  +
                                  " OR  KEY_CD  =  ' "  +  stringKey  +  "' "  +
                                  " OR  KEY_CD  =  '  "  +  stringKey  +  "') "  +
                            " AND  FLOW_NO  =  "  +  stringFlowNo ;
            dbFED1L.execFromPool(stringSql) ;
      }
      public  void  doUpdateFED1011NoneForStatusU(String  stringNoteType,  String  stringKey,  String  stringFlowNo,  talk  dbFED1L) throws  Throwable {
            String  stringSql  =  "" ;
            //
            stringSql  =  "UPDATE  FED1011  SET  STATUS_CD  =  ' ' "  +
                          " WHERE  STATUS_CD  =  'U' "  +
                            " AND  NOTE_TYPE  =  '"  +  stringNoteType  +  "' "  +
                            " AND  (KEY_CD  =  '"  +  stringKey  +  "' "  +
                                  " OR  KEY_CD  =  ' "  +  stringKey  +  "' "  +
                                  " OR  KEY_CD  =  '  "  +  stringKey  +  "') "  +
                           " AND  FLOW_NO  =  "  +  stringFlowNo ;
            dbFED1L.execFromPool(stringSql) ;
      }
      public  void  doDeleteFED1011(String  stringNoteType,  String  stringKey,  String  stringFlowNo,  talk  dbFED1L) throws  Throwable {
            String  stringSql  =  "" ;
            //
            stringSql  =  "DELETE  FED1011 " +
                              " WHERE  STATUS_CD  =  'U' "  +
                                 " AND  NOTE_TYPE  =  '"  +  stringNoteType  +  "' "  +
                                 " AND  (KEY_CD  =  '"    +  stringKey  +  "' "  +
                                    " OR  KEY_CD  =  ' "   +  stringKey  +  "' "  +
                                      " OR  KEY_CD  =  '  "  +  stringKey  +  "') "  +
                                  " AND  FLOW_NO  =  "     +  stringFlowNo ;
            dbFED1L.execFromPool(stringSql) ;
      }
      public  String  getVoucherEmpNo(String  stringUser,  String stringComNo) throws  Throwable {
            stringComNo  =  stringComNo.trim() ;
            // �uŪ�ͳB�z
            if("A0824".equals(stringUser)  &&  !"Z6".equals(stringComNo)) return "154501" ; // �B�R��
            // ���s + 00
            // �L�f��  ���F 2013-12-13 �������ഫ
            /*if(",B3680,".indexOf(stringUser)  !=  -1) {
                  stringUser   =  stringUser+"00" ;
            }*/
            // ���v�e �w��¾ 2013-12-13 �������ഫ
            /*if(!"".equals(stringComNo)  &&  "01,12,".indexOf(stringComNo)!=-1  &&  "B4246".equals(stringUser)) {
                  stringUser   =  stringUser+"00" ;
            }*/
            // ���S  2017-09-04
            if(",B5346,".indexOf(stringUser)  !=  -1) {
                  stringUser   =  stringUser+"00" ;
            }
            // ������ 
            // 2013-12-13 �������ഫ (���w�����س]01_
            // 2014-09-22 �W�[������ �B�������q
            if ("B2557".equals(stringUser)) {
                  stringUser   =  stringUser+"00" ;
            }
            // ���d
            // 2017-02-22 ���q�� Z6 �ɡA�אּ 218500
            // 2017-03-06 ���q�� 10 �ɡA�אּ 218500
            // 2017-03-06 ���q�� 12 �ɡA�אּ 218500
            if ("B2185".equals(stringUser)  &&  ",Z6,20,12,10,".indexOf(stringComNo)!=-1) {
                  stringUser   =  stringUser+"00" ;
            }
            // 2016-03-11 �س]�ɡA�@���ഫ
            if ("B2185".equals(stringUser)  &&  "01".equals(stringComNo)) {
                  stringUser   =  stringUser+"00" ;
            }
            // ���s + 01
            // �B�z��
            if("B1721,".indexOf(stringUser)  !=  -1) {
                  stringUser   =  stringUser+"01" ;
            }
            // ���ߩ� ���J  2013-12-13 �������ഫ
            /*(if(!"".equals(stringComNo)  &&  "12,".indexOf(stringComNo)!=-1  &&  "B3437".equals(getUser())) {
                 stringUser   =  stringUser+"01" ;
            }*/
            return  stringUser ;
      }
		public  boolean  isDeptCdLength0CheckOK(String  stringAcctNo,  String  stringDeptCd)throws  Throwable {
				// �S��|�p��ءA���\�������ť�
            if(stringAcctNo.startsWith("1")) { 
                  if(stringAcctNo.startsWith("11")) { 
            				if("1103".equals(stringAcctNo) )   					return  true ;
            				if("110301".equals(stringAcctNo) )  				return  true ;
            				if("110302".equals(stringAcctNo) )  				return  true ;
            				if("110303".equals(stringAcctNo) )  				return  true ;
            				if("1106".equals(stringAcctNo) )  				  	return  true ;
            				if("1108".equals(stringAcctNo) )  				  	return  true ;
            				if("1109".equals(stringAcctNo) )  				  	return  true ;
            				if("1110".equals(stringAcctNo) )  				  	return  true ;
            				if("1112".equals(stringAcctNo) )  				  	return  true ;
            				if("1113".equals(stringAcctNo) )  				  	return  true ;
            				if("1117".equals(stringAcctNo) )  				  	return  true ;
            				if("1129".equals(stringAcctNo) )  				  	return  true ;
            				if("1142".equals(stringAcctNo) )  				  	return  true ;
            				if("1144".equals(stringAcctNo) )  				  	return  true ;
            				if("1146".equals(stringAcctNo) )  				  	return  true ;
            				if("1149".equals(stringAcctNo) )  				  	return  true ;
            				if("1167".equals(stringAcctNo) )  				  	return  true ;
            				if("1178".equals(stringAcctNo) )  				  	return  true ;
            				if("117802".equals(stringAcctNo) )  			  	return  true ;
            				if("1179".equals(stringAcctNo) )  				  	return  true ;
            				if("1180".equals(stringAcctNo) )  				  	return  true ;
            				if("118001".equals(stringAcctNo) ) 			  	   return  true ;
                  } else if(stringAcctNo.startsWith("12")) { 
            				if("1227".equals(stringAcctNo) )  				  	return  true ;
            				if("1228".equals(stringAcctNo) )  				  	return  true ;
            				if("1229".equals(stringAcctNo) )  				  	return  true ;
            				if("123".equals(stringAcctNo) )  				 	return  true ;
            				if("1231".equals(stringAcctNo) )  			   	return  true ;
            				if("1232".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1233".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1234".equals(stringAcctNo) )  			  	 	return  true ;
            				if("123438".equals(stringAcctNo) )  		  	 	return  true ;
            				if("1237".equals(stringAcctNo) )  			  	 	return  true ;
            				if("125".equals(stringAcctNo) )  				 	return  true ;
            				if("1251".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1258".equals(stringAcctNo) )  			  	 	return  true ;
            				if("128".equals(stringAcctNo) )  				 	return  true ;
            				if("1284".equals(stringAcctNo) )  			  	 	return  true ;
                  } else if(stringAcctNo.startsWith("14")) { 
            				if("14".equals(stringAcctNo) )  				  	 	return  true ;
            				if("141".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1411".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1418".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1423".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1425".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1428".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1429".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1450".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1480".equals(stringAcctNo) )  			  	 	return  true ;
                  } else if(stringAcctNo.startsWith("15")) { 
            				if("15".equals(stringAcctNo) )  			  	 	 	return  true ;
            				if("1511".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1519".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1529".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1539".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1540".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1549".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1551".equals(stringAcctNo) )  			  	 	return  true ;
            				if("1558".equals(stringAcctNo) )  			  	 	return  true ;
            				if("155810".equals(stringAcctNo) )  		  	 	return  true ;
            				if("155820".equals(stringAcctNo) )  		  	 	return  true ;
            				if("155830".equals(stringAcctNo) )  		  	 	return  true ;
            				if("155840".equals(stringAcctNo) )  		  	 	return  true ;
            				if("155850".equals(stringAcctNo) )  		  	 	return  true ;
            				if("155860".equals(stringAcctNo) )  		  	 	return  true ;
            				if("1559".equals(stringAcctNo) )  		  		 	return  true ;
            				if("1569".equals(stringAcctNo) )  		  		 	return  true ;
            				if("1579".equals(stringAcctNo) )  		  		 	return  true ;
            				if("1589".equals(stringAcctNo) )  		  		 	return  true ;
                  } else if(stringAcctNo.startsWith("16")) { 
            				if("1611".equals(stringAcctNo) )  		  		 	return  true ;
            				if("162".equals(stringAcctNo) )  	  		 		return  true ;
            				if("1662".equals(stringAcctNo) )		  		 		return  true ;
            				if("1663".equals(stringAcctNo) )		  		 		return  true ;
            				if("1664".equals(stringAcctNo) )		  		 		return  true ;
            				if("1665".equals(stringAcctNo) )		  		 		return  true ;
                  } else if(stringAcctNo.startsWith("17")) { 
                  		if("17".equals(stringAcctNo) )		  		 		    return  true ;
                  } else if(stringAcctNo.startsWith("18")) { 
            				if("18".equals(stringAcctNo) )		 	 		    return  true ;
            				if("1811".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1819".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1836".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1849".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1851".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1871".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1872".equals(stringAcctNo) )		  	 		    return  true ;
            				if("1889".equals(stringAcctNo) )		  	 		    return  true ;
                  }
            } else if(stringAcctNo.startsWith("2")) { 
      				if("2".equals(stringAcctNo) )		  	 		    	return  true ;
      				if("21".equals(stringAcctNo) )		  	 	  	   return  true ;
      				if("2108".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("211".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2141".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2145".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2216".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2218".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2219".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2221".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2225".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("225".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("226".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2269".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("227".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2271".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2272".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2273".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("228".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2282".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("228232".equals(stringAcctNo) )		       	return  true ;
      				if("2285".equals(stringAcctNo) )    				return  true ;			
      				if("2286".equals(stringAcctNo) )    				return  true ;
      				if("2288".equals(stringAcctNo) )    				return  true ;
      				if("2289".equals(stringAcctNo) )    				return  true ;
      				if("2399".equals(stringAcctNo) )    				return  true ;
      				if("239903".equals(stringAcctNo) )    				return  true ;
      				if("24".equals(stringAcctNo) )    			  		return  true ;
      				if("2400".equals(stringAcctNo) )  			  		return  true ;
      				if("241".equals(stringAcctNo) )  				 	return  true ;
      				if("2411".equals(stringAcctNo) )  			  	 	return  true ;
      				if("2419".equals(stringAcctNo) )  			  	 	return  true ;
      				if("242".equals(stringAcctNo) )  			 		return  true ;
      				if("2428".equals(stringAcctNo) )  		  	 		return  true ;
      				if("244".equals(stringAcctNo) )  			 		return  true ;
      				if("2441".equals(stringAcctNo) )  			  	 	return  true ;
      				if("2443".equals(stringAcctNo) )  			  	 	return  true ;
      				if("28".equals(stringAcctNo) )  			  	 	 	return  true ;
      				if("2830".equals(stringAcctNo) )  		  	 	 	return  true ;
      				if("2851".equals(stringAcctNo) )  			 	 	return  true ;
      				if("2881".equals(stringAcctNo) )  			 	 	return  true ;
            } else if(stringAcctNo.startsWith("3")) { 
      				if("3".equals(stringAcctNo) )  			    		return  true ;
      				if("3111".equals(stringAcctNo) )  			 		return  true ;
      				if("3141".equals(stringAcctNo) )  			 		return  true ;
      				if("3151".equals(stringAcctNo) )  			 		return  true ;
      				if("3211".equals(stringAcctNo) )  			 		return  true ;
      				if("321101".equals(stringAcctNo) )  		 		return  true ;
      				if("321102".equals(stringAcctNo) )  		 		return  true ;
      				if("321103".equals(stringAcctNo) )  		 		return  true ;
      				if("3271".equals(stringAcctNo) )  		 		 	return  true ;
      				if("3272".equals(stringAcctNo) )  		 		 	return  true ;
      				if("33".equals(stringAcctNo) )  		 		 	  	return  true ;
      				if("3311".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3321".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3351".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3352".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3353".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3354".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3355".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3356".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("335601".equals(stringAcctNo) )  		 	  	return  true ;
      				if("335602".equals(stringAcctNo) )  		 	  	return  true ;
      				if("335603".equals(stringAcctNo) )  		 	  	return  true ;
      				if("335604".equals(stringAcctNo) )  		 	  	return  true ;
      				if("34".equals(stringAcctNo) )  		 	  	 		return  true ;
      				if("3411".equals(stringAcctNo) )  		  	 		return  true ;	
      				if("3420".equals(stringAcctNo) )  		  	 		return  true ;
      				if("3430".equals(stringAcctNo) )  		  	 		return  true ;
      				if("343001".equals(stringAcctNo) )  		 		return  true ;
      				if("343002".equals(stringAcctNo) )  		 		return  true ;
      				if("3431".equals(stringAcctNo) )  		  	 		return  true ;
      				if("3460".equals(stringAcctNo) )  		  	 		return  true ;
      				if("346001".equals(stringAcctNo) )  	  	 		return  true ;
      				if("346002".equals(stringAcctNo) )  	  	 		return  true ;
            } else if(stringAcctNo.startsWith("4")) { 
      				if("4".equals(stringAcctNo) )  	  	 	  			return  true ;
      				if("4161".equals(stringAcctNo) )  	 	  			return  true ;
      				if("4223".equals(stringAcctNo) )  	 	  			return  true ;
      				if("4251".equals(stringAcctNo) )  	 	  			return  true ;
      				if("44".equals(stringAcctNo) )  	 	  			 	return  true ;
      				if("441".equals(stringAcctNo) )  	 			 	return  true ;
            } else if(stringAcctNo.startsWith("5")) { 
      				if("5".equals(stringAcctNo) )  	 	  		 	 	return  true ;
      				if("51".equals(stringAcctNo) )  	   		 	 	return  true ;
      				if("5161".equals(stringAcctNo) )  	  			 	return  true ;
      				if("52".equals(stringAcctNo) )  	   		 	 	return  true ;
      				if("5211".equals(stringAcctNo) )  	  		 	 	return  true ;
      				if("521101".equals(stringAcctNo) )  	 	 	 	return  true ;
      				if("521102".equals(stringAcctNo) )  	 	 	 	return  true ;
      				if("521103".equals(stringAcctNo) )  	 	 	 	return  true ;
      				if("521188".equals(stringAcctNo) )  	 	 	 	return  true ;
      				if("5212".equals(stringAcctNo) )  	 	 	  	   return  true ;
      				if("521201".equals(stringAcctNo) )  	 		   return  true ;
      				if("521202".equals(stringAcctNo) )  	 	 	   return  true ;
      				if("521203".equals(stringAcctNo) )  	 	 	   return  true ;
      				if("5213".equals(stringAcctNo) )  	 	 	   	return  true ;
      				if("5215".equals(stringAcctNo) )  	 	 	   	return  true ;
      				if("5221".equals(stringAcctNo) )  	 	 	   	return  true ;
      				if("5222".equals(stringAcctNo) )  	 	 	   	return  true ;
      				if("5223".equals(stringAcctNo) )  	 	 	   	return  true ;
      				if("53".equals(stringAcctNo) )  	   			 	return  true ;
      				if("531".equals(stringAcctNo) )  	   			return  true ;
      				if("5311".equals(stringAcctNo) )  	   			return  true ;
      				if("54".equals(stringAcctNo) )  	 		  	 		return  true ;
      				if("568".equals(stringAcctNo) )  	 		  	 	return  true ;
      				if("5681".equals(stringAcctNo) )  	 		  	 	return  true ;
      				if("5881".equals(stringAcctNo) )  	 		  	 	return  true ;
      				if("5910".equals(stringAcctNo) )  	 		  	 	return  true ;
            } else if(stringAcctNo.startsWith("6")) { 
      				if("6".equals(stringAcctNo) )  	 		  		 	return  true ;
      				if("62".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6201".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("64".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6401".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("65".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6501".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("6601".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("6701".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("6801".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("69".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6991".equals(stringAcctNo) )  	 	 		 	return  true ;
            } else if(stringAcctNo.startsWith("7")) { 
      				if("7".equals(stringAcctNo) )  	 	 	 	  		return  true ;
      				if("7171".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7251".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7271".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7375".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("79".equals(stringAcctNo) )     	  	 		 	return  true ;
      				if("7991".equals(stringAcctNo) )  	  	 		 	return  true ;
            } else if(stringAcctNo.startsWith("8")) { 
      				if("8".equals(stringAcctNo) )  	  	 	 	 		return  true ;
      				if("81".equals(stringAcctNo) )  	  	 		 		return  true ;
            }
				//
				if(stringDeptCd.length()  ==  0) 						return  false ;
				//
				return  true ;
		}
		public  boolean  isObjectCdLength0CheckOK(String  stringAcctNo,  String  stringDeptCd)throws  Throwable {
				// �S��|�p��ءA���\�t�Ӭ��ť�
	         if(stringAcctNo.startsWith("1")) { 
                  if("1101".equals(stringAcctNo) )    		 	   return  true ;
      				if("1103".equals(stringAcctNo) )    			   return  true ;
      				if("1106".equals(stringAcctNo) )  				  	return  true ;
      				if("1108".equals(stringAcctNo) )  				  	return  true ;
      				if("1110".equals(stringAcctNo) )  				  	return  true ;
      				if("1112".equals(stringAcctNo) )  				  	return  true ;
      				if("1113".equals(stringAcctNo) )  				  	return  true ;
      				if("1117".equals(stringAcctNo) )  				  	return  true ;
      				if("1129".equals(stringAcctNo) )  				  	return  true ;
      				if("1142".equals(stringAcctNo) )  				  	return  true ;
      				if("1144".equals(stringAcctNo) )  				  	return  true ;
      				if("1149".equals(stringAcctNo) )  				  	return  true ;
      				if("1178".equals(stringAcctNo) )  				  	return  true ;
      				if("1179".equals(stringAcctNo) )  				  	return  true ;
      				if("1180".equals(stringAcctNo) )  				  	return  true ;
      				if("118001".equals(stringAcctNo) ) 			  	   return  true ;
      				if("1227".equals(stringAcctNo) )  				  	return  true ;
      				if("1228".equals(stringAcctNo) )  				  	return  true ;
      				if("1229".equals(stringAcctNo) )  				  	return  true ;
      				if("123".equals(stringAcctNo) )  				  	return  true ;
      				if("1231".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1232".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1233".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1234".equals(stringAcctNo) )  			  	 	return  true ;
      				if("123438".equals(stringAcctNo) )  		  	 	return  true ;
      				if("1236".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1237".equals(stringAcctNo) )  			  	 	return  true ;
      				if("125".equals(stringAcctNo) )  				  	return  true ;
      				if("1251".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1258".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1265".equals(stringAcctNo) )  			  	 	return  true ;
      				if("128".equals(stringAcctNo) )  				  	return  true ;
      				if("1284".equals(stringAcctNo) )  			  	 	return  true ;
      				if("14".equals(stringAcctNo) )  				  	 	return  true ;
      				if("141".equals(stringAcctNo) )  				  	return  true ;
      				if("1411".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1418".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1423".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1429".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1450".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1480".equals(stringAcctNo) )  			  	 	return  true ;
      				if("15".equals(stringAcctNo) )  			  	 	 	return  true ;
      				if("1519".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1529".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1539".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1540".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1549".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1551".equals(stringAcctNo) )  			  	 	return  true ;
      				if("1558".equals(stringAcctNo) )  			  	 	return  true ;
      				if("155810".equals(stringAcctNo) )  		  	 	return  true ;
      				if("155820".equals(stringAcctNo) )  		  	 	return  true ;
      				if("155830".equals(stringAcctNo) )  		  	 	return  true ;
      				if("155840".equals(stringAcctNo) )  		  	 	return  true ;
      				if("155850".equals(stringAcctNo) )  		  	 	return  true ;
      				if("155860".equals(stringAcctNo) )  		  	 	return  true ;
      				if("1559".equals(stringAcctNo) )  		  		 	return  true ;
      				if("1569".equals(stringAcctNo) )  		  		 	return  true ;
      				if("1579".equals(stringAcctNo) )  		  		 	return  true ;
      				if("1589".equals(stringAcctNo) )  		  		 	return  true ;
      				if("1611".equals(stringAcctNo) )  		  		 	return  true ;
      				if("162".equals(stringAcctNo) )  		  	 		return  true ;
      				if("1621".equals(stringAcctNo) )		  		 		return  true ;
      				if("1622".equals(stringAcctNo) )		  		 		return  true ;
      				if("1623".equals(stringAcctNo) )		  		 		return  true ;
      				if("1629".equals(stringAcctNo) )		  		 		return  true ;
      				if("1661".equals(stringAcctNo) )		  		 		return  true ;
      				if("166110".equals(stringAcctNo) )		  	 		return  true ;
      				if("166125".equals(stringAcctNo) )		  	 		return  true ;
      				if("166127".equals(stringAcctNo) )		  	 		return  true ;
      				if("1662".equals(stringAcctNo) )		  		 		return  true ;
      				if("1663".equals(stringAcctNo) )		  		 		return  true ;
      				if("1664".equals(stringAcctNo) )		  		 		return  true ;
      				if("1665".equals(stringAcctNo) )		  		 		return  true ;
      				if("17".equals(stringAcctNo) )		  			    return  true ;
      				if("18".equals(stringAcctNo) )		  			    return  true ;
      				if("1811".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1819".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1836".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1849".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1851".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1872".equals(stringAcctNo) )		  	 		    return  true ;
      				if("1892".equals(stringAcctNo) )		  	 		    return  true ;
            } else if(stringAcctNo.startsWith("2")) { 
      				if("2".equals(stringAcctNo) )		  	 		    	return  true ;
      				if("21".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("211".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2141".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2145".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2218".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("225".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("226".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2269".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("227".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("228".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2282".equals(stringAcctNo) )		  	 	    	return  true ;
      				if("2285".equals(stringAcctNo) )    				return  true ;
      				if("2286".equals(stringAcctNo) )    				return  true ;
      				if("2288".equals(stringAcctNo) )    				return  true ;
      				if("2289".equals(stringAcctNo) )    				return  true ;
      				if("2399".equals(stringAcctNo) )    				return  true ;
      				if("24".equals(stringAcctNo) )    			  		return  true ;
      				if("2400".equals(stringAcctNo) )  			  		return  true ;
      				if("241".equals(stringAcctNo) )  				 	return  true ;
      				if("2411".equals(stringAcctNo) )  			  	 	return  true ;
      				if("242".equals(stringAcctNo) )  			 		return  true ;
      				if("244".equals(stringAcctNo) )  			 		return  true ;
      				if("28".equals(stringAcctNo) )  			  	 	 	return  true ;
      				if("2856".equals(stringAcctNo) )  			 	 	return  true ;
      				if("2892".equals(stringAcctNo) )  			 	 	return  true ;
            } else if(stringAcctNo.startsWith("3")) { 
      				if("3".equals(stringAcctNo) )  			 	  		return  true ;
      				if("3151".equals(stringAcctNo) )  			 		return  true ;
      				if("3211".equals(stringAcctNo) )  			 		return  true ;
      				if("321102".equals(stringAcctNo) )  		 		return  true ;
      				if("3271".equals(stringAcctNo) )  		 		 	return  true ;
      				if("3272".equals(stringAcctNo) )  		 		 	return  true ;
      				if("33".equals(stringAcctNo) )  		 		 	  	return  true ;
      				if("3311".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3321".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3351".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3352".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3353".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3354".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3355".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("3356".equals(stringAcctNo) )  		 	 	  	return  true ;
      				if("34".equals(stringAcctNo) )  		 	  	 		return  true ;
      				if("3411".equals(stringAcctNo) )  		    		return  true ;
      				if("3430".equals(stringAcctNo) )  		    		return  true ;
      				if("3431".equals(stringAcctNo) )  		    		return  true ;
      				if("3460".equals(stringAcctNo) )  		    		return  true ;
            } else if(stringAcctNo.startsWith("4")) { 
      				if("4".equals(stringAcctNo) )  	 		  			return  true ;
      				if("4111".equals(stringAcctNo) )  	 	  			return  true ;
      				if("44".equals(stringAcctNo) )  	 	  			 	return  true ;
      				if("441".equals(stringAcctNo) )   	  			 	return  true ;
            } else if(stringAcctNo.startsWith("5")) { 
      				if("5".equals(stringAcctNo) )  	  			 	 	return  true ;
      				if("51".equals(stringAcctNo) )  	  			 	 	return  true ;
      				if("5111".equals(stringAcctNo) )  	  			 	return  true ;
      				if("5113".equals(stringAcctNo) )  	  			 	return  true ;
      				if("5121".equals(stringAcctNo) )  	  			 	return  true ;
      				if("52".equals(stringAcctNo) )  	  			 	 	return  true ;
      				if("53".equals(stringAcctNo) )  	 			 	 	return  true ;
      				if("531".equals(stringAcctNo) )    			 	 	return  true ;
      				if("5311".equals(stringAcctNo) )    			  	return  true ;
      				if("531110".equals(stringAcctNo) )  	 		  	return  true ;
      				if("531111".equals(stringAcctNo) )  	 		  	return  true ;
      				if("531124".equals(stringAcctNo) )  	 		  	return  true ;
      				if("531125".equals(stringAcctNo) )  	 		  	return  true ;
      				if("531127".equals(stringAcctNo) )  	 		  	return  true ;
      				if("54".equals(stringAcctNo) )  	 		  	 		return  true ;
      				if("568".equals(stringAcctNo) ) 	 		  	 		return  true ;
      				if("5681".equals(stringAcctNo) )  	 		  	 	return  true ;
      				if("5881".equals(stringAcctNo) )  	 		  	 	return  true ;
      				if("588161".equals(stringAcctNo) )  	 	  	 	return  true ;
      				if("5910".equals(stringAcctNo) )  	 		  	 	return  true ;
            } else if(stringAcctNo.startsWith("6")) { 
      				if("6".equals(stringAcctNo) )  	 		  		 	return  true ;
      				if("62".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6201".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("64".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6401".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("65".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6501".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("6801".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("680154".equals(stringAcctNo) )  	   	 	return  true ;
      				if("69".equals(stringAcctNo) )  	 	  	 		 	return  true ;
      				if("6991".equals(stringAcctNo) )  	 	 		 	return  true ;
            } else if(stringAcctNo.startsWith("7")) { 
      				if("7".equals(stringAcctNo) )  	 		 	  		return  true ;
      				if("7181".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7251".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7271".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7561".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("7621".equals(stringAcctNo) )  	  	 		 	return  true ;
      				if("79".equals(stringAcctNo) )     	  	 		 	return  true ;
      				if("7991".equals(stringAcctNo) )  	  	 		 	return  true ;
            } else if(stringAcctNo.startsWith("8")) { 
      				if("8".equals(stringAcctNo) )	  	 		 	 		return  true ;
      				if("81".equals(stringAcctNo) )  	  	 		 		return  true ;
      				if("8111".equals(stringAcctNo) )  	  	 		 	return  true ;
            }
				//
				if(stringDeptCd.length()  ==  0) 						return  false ;
				//
				return  true ;
		}
      // ��Ʈw
      public  talk  getTalkCS(String  stringDBName) throws  Throwable {
      		talk  dbDocCS =  getUserTalk(stringDBName+"---B---172.16.8.19---"+stringDBName,  "invoice",  "invoicepw",  "USERS") ;
      		return  dbDocCS ;
      }
      public  talk  getUserTalk(String  dbName,  String  stringUser,  String  stringPwd,  String  stringTestTable) throws  Throwable {
         		talk			dbTalk  				   =  null ;
         		String[]    arrayData     			=  convert.StringToken(dbName,  "---") ;
         		String      stringDBName      	=  arrayData[0].trim() ;                  // ��Ʈw �s�u�W��
         		String      stringDBType      	=  arrayData[1].trim() ;                  // ��Ʈw �s�u�覡 A �����s�u B�ۭq�s�u
         		String      stringDBConnect      =  "" ;// arrayData[2].trim() ;           // ��Ʈw �ۭq�s�u���s�u��}
         		String      stringDBTable        =  "" ;// arrayData[3].trim() ;           // ��Ʈw �ۭq�s�u���s�u��Ʈw
         		//
         		if("A".equals(stringDBType)) return  getTalk(stringDBName) ;
         		//
         		if(arrayData.length  !=  4)  {
         				messagebox("��Ʈw�s�����~") ;
         				return  null ;
      		   }
         		//
         		stringDBConnect       =  arrayData[2].trim() ;
         		stringDBTable         =  arrayData[3].trim() ;
               try{ 
                     dbTalk=  new  talk("mssql",   stringDBConnect,           stringUser,              stringPwd,             stringDBTable) ;              
                     // ���ճs�u
                     if(!"".equals(stringTestTable)) {
                             dbTalk.getColumnsFromPool(stringTestTable) ;  
                     }
         		} catch (Throwable e) { 
      				   System.out.println("�s�u����") ; 
      					return null ;
      		   } 
         		return  dbTalk ;
      }
      // �d��
      public  void  setColumnsDataNULL( ) throws  Throwable {
               hashtableColumnsData  =  new  Hashtable() ;
      }
      public  Vector  getColumns(String  stringTableName,  talk  dbCon) throws  Throwable {
              String[][]    retColumnsData    =  dbCon.getColumnsFromPool(stringTableName) ;
              Vector        vectorColumnName  =  new  Vector() ;
              String        stringColumnName  =  "" ;
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                        stringColumnName   =  retColumnsData[intNo][0].trim() ;
                        //
                        vectorColumnName.add(stringColumnName) ;

              }
              return  vectorColumnName ;
       }
      public  String  getNameUnion(String  stringFieldName,  String  stringTableName,  String  stringSqlAnd,  Hashtable  hashtableAnd,  talk  dbCon)  throws Throwable{
            String      stringSql                        =  "" ;
            String      stringName                   =  "" ;
            Vector      vectorColumnName  =  new  Vector() ;
            //
            Vector  vectorTableData  =  getQueryDataHashtable(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
            if(vectorTableData.size()  !=  0) {
                  Hashtable  hashtableTemp  =  (Hashtable) vectorTableData.get(0) ;
                  if(hashtableTemp  !=  null) {
                        stringName  = (""+hashtableTemp.get(stringFieldName)).trim( ) ;
                        if("null".equals(stringName))  stringName  =  "" ;
                  }
            }
            return  stringName ;
      }
      public  double  getTableFieldSum(String  stringFieldName,  String  stringTableName,  String  stringSqlAnd,  Hashtable  hashtableAnd,  talk  dbCon)  throws Throwable{
            String      stringSql         =  "" ;
            String      stringName        =  "" ;
            Vector      vectorColumnName  =  new  Vector() ;
            Hashtable   hashtableTemp     =  null ;
            double      doubleFieldSum    =  0 ;
            //
            Vector  vectorTableData  =  getQueryDataHashtable(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
            for(int  intNo=0  ;  intNo<vectorTableData.size()  ;  intNo++) {
                  hashtableTemp  =  (Hashtable) vectorTableData.get(intNo) ;       if(hashtableTemp  ==  null) continue ;
                  stringName     = (""+hashtableTemp.get(stringFieldName)).trim( ) ;
                  //
                  doubleFieldSum  +=  doParseDouble(stringName) ;
            }
            return  doubleFieldSum ;
      }
      public  Vector  getQuerySumData(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSelectSql,  String  stringSqlAnd,  talk  dbCon) throws  Throwable {
              Vector       vectorTableData       =  new  Vector() ; 
              String        stringWhereSql          =  "" ;
              String        stringColumnName  =  "" ;
              String        stringColumnType   =  "" ;
              String        stringColumnValue  =  "" ;
              String        stringTableNameT   =  convert.StringToken(stringTableName,  "%-%")[0] ;  // �קK���P��Ʈw�A�ۦP���W�ٮɡA[���]%-%[��Ʈw]
              String[][]  retColumnsData       =  null ;
              retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
              if(retColumnsData  ==  null) {
                     retColumnsData        =  dbCon.getColumnsFromPool(stringTableNameT) ;
                     hashtableColumnsData.put(stringTableName,  retColumnsData) ;
              }
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                     stringColumnName   =  retColumnsData[intNo][0].trim() ;
                     stringColumnType    =  retColumnsData[intNo][2].trim() ;
                    // AND
                    stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
                    //
                    if(isNum(stringColumnType)  ||  "null".equals(stringColumnValue)) {
                           // �ƭ� 
                           if(doParseDouble(stringColumnValue)  ==  0)  stringColumnValue  =  "0" ;
                           if(!"".equals(stringWhereSql))  stringWhereSql  +=  " AND " ;
                           stringWhereSql  +=  " "+stringColumnName+" =  "+stringColumnValue+" \n" ;
                    } else {
                           // ��r
                           if(!"".equals(stringWhereSql))  stringWhereSql  +=  " AND " ;
                           stringWhereSql  +=  " "+stringColumnName+" =  '"+convert.ToSql(stringColumnValue)+"' \n" ;
                    }
                    // �M��
                    hashtableAnd.remove(stringColumnName) ;
              }
              if(!"".equals(stringWhereSql)) {
                  stringWhereSql  =  " WHERE  "  +  stringWhereSql  +  " " ;
              } else {
                  if(stringSqlAnd.trim().toUpperCase().startsWith("AND")) {
                        int  intPos             =  stringSqlAnd.toUpperCase().indexOf("AND") ;     
                              stringSqlAnd  =  " WHERE  "  +  doSubstring(stringSqlAnd,  intPos+3,  stringSqlAnd.length()) ;
                  }
              }
              //
              String  stringSql  =  " SELECT "   +  stringSelectSql        +  " \n" +
                                                      " FROM  "  +  stringTableNameT  +  " \n" +
                                                     stringWhereSql +
                                                     stringSqlAnd ;
              // ��ƨ��o                      
              String[][]   retData            =  new  String[0][0] ;
              boolean     booleanFlag  =  !"".equals(stringSelectSql) &&  
                                                                 !"".equals(stringTableNameT) &&  
                                                                 (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
              if(booleanFlag)  retData  =  dbCon.queryFromPool(stringSql) ;
              // ��ƾ�z
                              stringSelectSql       =  stringSelectSql.replaceAll(" as ",  " ") ;
                              stringSelectSql       =  stringSelectSql.replaceAll(" As ",  " ") ;
                              stringSelectSql       =  stringSelectSql.replaceAll(" aS ",  " ") ;
                              stringSelectSql       =  stringSelectSql.replaceAll(" AS ",  " ") ;
              int           intPosS                     =  0 ;
              int           intPosE                     =  0 ;
              String[]  arrayFieldName    =  convert.StringToken(stringSelectSql,  ",") ;
              String[]  arrayFieldNameL  =  null ;
              for(int  intNo=0  ;  intNo<arrayFieldName.length  ;  intNo++) {
                     stringColumnName  =  arrayFieldName[intNo].trim() ;
                     //
                     arrayFieldNameL  =  convert.StringToken(stringColumnName,  " ") ;
                     if(arrayFieldNameL.length  >  1) {
                              stringColumnName       =  arrayFieldNameL[arrayFieldNameL.length-1] ;
                     } else {
                                intPosS  =  stringColumnName.indexOf("(") ;
                                intPosE  =  stringColumnName.indexOf(")") ;
                                if(intPosS!=-1  &&  intPosE!=-1) {
                                       stringColumnName  =  doSubstring(stringColumnName,  intPosS+1,  intPosE) ;
                               }
                     }
                     arrayFieldName[intNo]  =  stringColumnName ;
              }
              //
              Hashtable  hashtableTemp  =  null ;
              for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
                       hashtableTemp      =  new  Hashtable() ;
                       for(int  intNoL=0  ;  intNoL<arrayFieldName.length  ;  intNoL++) {
                              stringColumnName   =  arrayFieldName[intNoL] ;
                              stringColumnValue   =  retData[intNo][intNoL].trim() ;
                              //
                              System.out.println("���W��("+stringColumnName+")����("+stringColumnValue+")-------------------------------") ;
                              hashtableTemp.put(stringColumnName,  stringColumnValue) ;
                       }
                       vectorTableData.add(hashtableTemp) ;
              }
              return  vectorTableData ;
       }
      public  Hashtable  getQueryDataHashtableH(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  talk  dbCon) throws  Throwable {
              Vector     vectorColumnName  =  new  Vector() ;              
              //
              return  getQueryDataHashtableH(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
      }
      public  Hashtable  getQueryDataHashtableH(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  Vector  vectorColumnName,  talk  dbCon) throws  Throwable {
              Vector     vectorRet         =  getQueryDataHashtableUnion(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
              Vector     vectorTableData   =  (Vector) vectorRet.get(1) ;
              String     stringSql         =  ""+vectorRet.get(0) ;
              Hashtable  hashtableData     =  null ;
              //
              if(vectorTableData.size()  >  0)  hashtableData  =  (Hashtable)  vectorTableData.get(0) ;
              if(hashtableData  ==  null)       hashtableData  =  new  Hashtable() ;
              hashtableData.put("TABLE_DATA_SIZE",  ""+vectorTableData.size()) ;
              hashtableData.put("TABLE_SQL",        stringSql) ;
              //
              return  hashtableData ;
      }
      public  Vector  getQueryDataHashtable(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  talk  dbCon) throws  Throwable {
              Vector  vectorColumnName  =  new  Vector() ;
              return  getQueryDataHashtable(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
      }
      public  Vector  getQueryDataHashtable(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  Vector  vectorColumnName,  talk  dbCon) throws  Throwable {
              Vector  vectorRet  =  getQueryDataHashtableUnion(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName,  dbCon) ;
              return  (Vector) vectorRet.get(1) ;
      }
      public  Vector  getQueryDataHashtableUnion(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  Vector  vectorColumnName,  talk  dbCon) throws  Throwable {
              Vector        vectorTableData        =  new  Vector() ; 
              String        stringSelectSql        =  "" ;
              String        stringWhereSql         =  "" ;
              String        stringColumnName       =  "" ;
              String        stringColumnType       =  "" ;
              String        stringColumnValue      =  "" ;
              String        stringTableNameT       =  convert.StringToken(stringTableName,  "%-%")[0] ;  // �קK���P��Ʈw�A�ۦP���W�ٮɡA[���]%-%[��Ʈw]
              String        stringIsNull           =  ""+hashtableAnd.get("ISNULL") ; hashtableAnd.remove("ISNULL") ;
              String[][]    retColumnsData         =  null ;
              Vector        vectorRet              =  new  Vector() ;
                            retColumnsData         =  (String[][]) hashtableColumnsData.get(stringTableName) ;
              if(retColumnsData  ==  null) {
                        retColumnsData        =  dbCon.getColumnsFromPool(stringTableNameT) ;
                        hashtableColumnsData.put(stringTableName,  retColumnsData) ;
              }
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                        stringColumnName   =  retColumnsData[intNo][0].trim() ;
                        stringColumnType    =  retColumnsData[intNo][2].trim() ;
                        //
                        vectorColumnName.add(stringColumnName) ;
                        // SELECT
                        if(!"".equals(stringSelectSql))  stringSelectSql  +=  ", " ;
                       stringSelectSql += stringColumnName  ;  
                       // AND
                       stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
                       //
                       if("null".equals(stringColumnValue)) {
                           //
                       } else if(isNum(stringColumnType)) {
                              // �ƭ� 
                              if(doParseDouble(stringColumnValue)  ==  0)  stringColumnValue  =  "0" ;
                              if(!"".equals(stringWhereSql))               stringWhereSql  +=  " AND " ;
                              if("Y".equals(stringIsNull)) {
                                     stringWhereSql  +=  " ISNULL("+stringColumnName+",0) =  "+stringColumnValue+" \n" ;
                              } else {
                                     stringWhereSql  +=  " "+stringColumnName+" =  "+stringColumnValue+" \n" ;
                              }
                       } else {
                              // ��r
                              if(!"".equals(stringWhereSql))                         stringWhereSql  +=  " AND " ;
                              if("Y".equals(stringIsNull)) {
                                      stringWhereSql  +=  "  ISNULL("+stringColumnName+",'') =  '"+convert.ToSql(stringColumnValue)+"' \n" ;
                              } else {
                                      stringWhereSql  +=  "  "+stringColumnName+" =  '"+convert.ToSql(stringColumnValue)+"' \n" ;                                 
                              }
                       }
                       // �M��
                       hashtableAnd.remove(stringColumnName) ;
              }
              if(!"".equals(stringWhereSql)) {
                     stringWhereSql  =  " WHERE  "  +  stringWhereSql  +  " " ;
              } else {
                     if(stringSqlAnd.trim().toUpperCase().startsWith("AND")) {
                           int  intPos              =  stringSqlAnd.toUpperCase().indexOf("AND") ;    
                                  stringSqlAnd  =  " WHERE  "  +  doSubstring(stringSqlAnd,  intPos+3,  stringSqlAnd.length()) ;
                     }
              }
              //
              String  stringSql  =  " SELECT  "  +  stringSelectSql  +  " \n" +
                                      " FROM  "  +  stringTableNameT  +  " \n" +
                                      stringWhereSql +
                                      stringSqlAnd ;
              vectorRet.add(stringSql) ;
              // ��ƨ��o                      
              String[][]   retData      =  new  String[0][0] ;
              boolean      booleanFlag  =  !"".equals(stringSelectSql) &&  
                                           !"".equals(stringTableNameT) &&  
                                          (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
              if(booleanFlag)  retData  =  dbCon.queryFromPool(stringSql) ;
              // ��ƾ�z
              Hashtable  hashtableTemp  =  null ;
              for(int  intNo=0  ;  intNo<retData.length  ;  intNo++) {
                       hashtableTemp      =  new  Hashtable() ;
                       for(int  intNoL=0  ;  intNoL<retColumnsData.length  ;  intNoL++) {
                              stringColumnName   =  retColumnsData[intNoL][0].trim() ;
                              stringColumnValue  =  retData[intNo][intNoL].trim() ;
                              //
                              hashtableTemp.put(stringColumnName,  stringColumnValue) ;
                       }
                       vectorTableData.add(hashtableTemp) ;
              }
              vectorRet.add(vectorTableData) ;
              return  vectorRet ;
       }
      //
      public  String  getVectorFieldValue(Vector  vectorTableData,  int  intRow,  String  stringField) throws  Throwable {
              return  getVectorFieldValue(vectorTableData,  intRow,  stringField,  new  Vector()) ;
      }
      public  String  getVectorFieldValue(Vector  vectorTableData,  int  intRow,  String  stringField,  Vector  vectorColumnName) throws  Throwable {
              if(vectorTableData  ==  null)                       return  "" ;
              if(intRow >  vectorTableData.size()-1)              return  "" ;
              //
              Hashtable  hashtableData  =  (Hashtable)vectorTableData.get(intRow) ;  
              if(hashtableData  ==  null)  return  "" ;
              //
              if(vectorColumnName  ==  null) {
                        vectorColumnName  =  new  Vector() ;
              }
              return getHashtableValue(stringField,  vectorColumnName,  hashtableData) ;
      }
      public  String  getHashtableValue(String  stringField,  Vector  vectorColumnName,  Hashtable  hashtableData) throws  Throwable {
              String  stringFieldValue  =  ""+hashtableData.get(stringField) ;
              //
              if(!"null".equals(stringFieldValue))  return  stringFieldValue ;
              //
              String  stringFieldL  =  "" ;
              for(int  intNo=0  ;  intNo<vectorColumnName.size()  ;  intNo++) {
                       stringFieldL  =  ""+vectorColumnName.get(intNo) ;
                       if(stringFieldL.equalsIgnoreCase (stringFieldL)) {
                                stringFieldValue  =  ""+hashtableData.get(stringFieldL) ;
                                return  stringFieldValue ;
                       }
              }
              return  "" ;
      }
      public  void  setHashtableValue(String  stringField,  String  stringFieldValue,  Vector  vectorColumnName,  Hashtable  hashtableData) throws  Throwable {
              String  stringFieldL  =  "" ;
              for(int  intNo=0  ;  intNo<vectorColumnName.size()  ;  intNo++) {
                       stringFieldL  =  ""+vectorColumnName.get(intNo) ;
                       if(stringFieldL.equalsIgnoreCase (stringFieldL)) {
                                hashtableData.put(stringFieldL,  stringFieldValue) ;
                                return ;
                       }
              }
      }
      // �s�W
      public  String  doInsertDB(String  stringTableName,  Hashtable  hashtableData,  boolean  booleanDB,  talk  dbCon) throws  Throwable {
              Vector        vectorTableData           =  new  Vector() ; 
              String        stringInsertNameSql    =  "" ;
              String        stringInsertValueSql     =  "" ;
              String        stringColumnName      =  "" ;
              String        stringColumnType        =  "" ;
              String        stringColumnValue      =  "" ;
              String        stringSign                        =  "" ;
              String        stringTableNameT       =  convert.StringToken(stringTableName,  "%-%")[0] ; // �קK���P��Ʈw�A�ۦP���W�ٮɡA[���]%-%[��Ʈw]
              String[][]  retColumnsData           =  null ;
              //
              retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
              if(retColumnsData  ==  null) {
                     retColumnsData        =  dbCon.getColumnsFromPool(stringTableNameT) ;
                     hashtableColumnsData.put(stringTableName,  retColumnsData) ;
              }
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                        stringColumnName   =  retColumnsData[intNo][0].trim() ;
                        stringColumnType    =  retColumnsData[intNo][2].trim() ;
                        stringColumnValue  =  (String) hashtableData.get(stringColumnName) ;  if(stringColumnValue  ==  null) continue ;
                        // INSERT FIELD
                        if(!"".equals(stringInsertNameSql))  stringInsertNameSql  +=  ", " ;
                       stringInsertNameSql += stringColumnName  ; 
                       // INSERT DATA
                       if(!"".equals(stringInsertValueSql))  stringInsertValueSql  +=  ", " ;
                       if(isNum(stringColumnType)  ||  "null".equals(stringColumnValue)) {
                              // �ƭ� 
                              if(doParseDouble(stringColumnValue)==0  &&  !"null".equals(stringColumnValue))  stringColumnValue =  "0" ;
                              stringInsertValueSql  +=  " "+stringColumnValue+" " ;
                       } else {
                              // ��r
                              if(stringColumnType.toUpperCase().startsWith("N")) {
                                  stringSign  =  "N" ;
                              } else {
                                  stringSign  =  "" ;
                              }
                              stringInsertValueSql  +=  " "+stringSign+"'"+convert.ToSql(stringColumnValue)+"' " ;
                       }
                        // �M��
                        hashtableData.remove(stringColumnName) ;
              }
              //
              String          stringSql         =  " INSERT INTO "+stringTableNameT+" ("  +  stringInsertNameSql  +  ") \n" +
                                                                         " VALUES ("  +  stringInsertValueSql +  ")" ;
              boolean     booleanFlag  =  booleanDB  &&  
                                                                   !"".equals(stringTableNameT)     &&  
                                                                  !"".equals(stringInsertNameSql)  &&  
                                                                  !"".equals(stringInsertValueSql) ;
              if(booleanFlag)  dbCon.execFromPool(stringSql) ;
              return  stringSql ;
       }
      // ��s
      public  String  doUpdateDB(String  stringTableName,  String  stringSqlAnd,  Hashtable  hashtableData,  Hashtable  hashtableAnd,  boolean  booleanDB,  talk  dbCon) throws  Throwable {
              Vector        vectorTableData        =  new  Vector() ; 
              String         stringUpdateDataSql   =  "" ;
              String         stringWhereSql        =  "" ;
              String         stringColumnName      =  "" ;
              String         stringColumnType      =  "" ;
              String         stringColumnValue1    =  "" ;
              String         stringColumnValue2    =  "" ;
              String         stringTableNameT      =  convert.StringToken(stringTableName,  "%-%")[0] ; // �קK���P��Ʈw�A�ۦP���W�ٮɡA[���]%-%[��Ʈw]
              String         stringIsNull          =  ""+hashtableAnd.get("ISNULL") ;hashtableAnd.remove("ISNULL") ;
              String[][]     retColumnsData        =  null ; 
              String         stringSign            =  "" ;
              boolean        booleanNum            =  false ;
              //
              retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
              if(retColumnsData  ==  null) {
                     retColumnsData        =  dbCon.getColumnsFromPool(stringTableNameT) ;
                     hashtableColumnsData.put(stringTableName,  retColumnsData) ;
              }
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                     stringColumnName    =  retColumnsData[intNo][0].trim() ;
                     stringColumnType    =  retColumnsData[intNo][2].trim() ;
                     stringColumnValue1  =  (String) hashtableData.get(stringColumnName) ; 
                     stringColumnValue2  =  (String) hashtableAnd.get(stringColumnName) ; 
                     booleanNum                =  isNum(stringColumnType);
                     // ��s���
                     if(stringColumnValue1  !=  null) {
                           if(!"".equals(stringUpdateDataSql))  stringUpdateDataSql  +=  ", \n" ;
                           if(booleanNum  ||  "null".equals(stringColumnValue1)) {
                                  // �ƭ�
                                  if(doParseDouble(stringColumnValue1)==0  &&  !"null".equals(stringColumnValue1))  stringColumnValue1 =  "0" ;
                                  stringUpdateDataSql  +=  " "+ stringColumnName+"  =  "+stringColumnValue1+" " ;
                           } else {
                                  if(stringColumnType.toUpperCase().startsWith("N")) {
                                       stringSign  =  "N" ;
                                  } else {
                                       stringSign  =  "" ;
                                  }
                                  // ��r
                                  stringUpdateDataSql  +=  " "+ stringColumnName+"  =  "+stringSign+"'"+convert.ToSql(stringColumnValue1)+"' " ;
                           }
                     }
                    // �������
                    if(stringColumnValue2  !=  null) {
                           if(booleanNum  ||  "null".equals(stringColumnValue2)) {
                                  // �ƭ� 
                                  if(doParseDouble(stringColumnValue2)  ==  0)  stringColumnValue2  =  "0" ;
                                  if(!"".equals(stringWhereSql))                stringWhereSql         +=  " AND " ;
                                  if("Y".equals(stringIsNull)) {
                                        stringWhereSql  +=  " ISNULL("  + stringColumnName + ",0)  =  "  +stringColumnValue2  +  " \n" ;
                                  } else {
                                        stringWhereSql  +=  " "  + stringColumnName + "  =  "  +stringColumnValue2  +  " \n" ;
                                  }
                           } else {
                                  // ��r
                                  if(!"".equals(stringWhereSql))  stringWhereSql  +=  " AND " ;
                                  if("Y".equals(stringIsNull)) {
                                        stringWhereSql  +=  " ISNULL("  + stringColumnName + ",'')  =  '"  +convert.ToSql(stringColumnValue2)  +  "' \n" ;
                                  } else {
                                        stringWhereSql  +=  " "  + stringColumnName + "  =  '"  +convert.ToSql(stringColumnValue2)  +  "' \n" ;                                    
                                  }
                           }
                    }
                     // �M��
                     hashtableData.remove(stringColumnName) ;
                     hashtableAnd.remove(stringColumnName) ;
              }
              if(!"".equals(stringWhereSql)) {
                  stringWhereSql  =  " WHERE  "  +  stringWhereSql  +  " " ;
              } else {
                  if(stringSqlAnd.trim().toUpperCase().startsWith("AND")) {
                        int  intPos              =  stringSqlAnd.toUpperCase().indexOf("AND") ;    
                               stringSqlAnd  =  " WHERE  "  +  doSubstring(stringSqlAnd,  intPos+3,  stringSqlAnd.length()) ;
                  }
              }
              //
              String         stringSql         =  " UPDATE  "  +  stringTableNameT+" \n"  +
                                                                 " SET  "  +  stringUpdateDataSql  +  " \n" +
                                                                  stringWhereSql +  " "+
                                                                  stringSqlAnd ;
              boolean     booleanFlag  =  booleanDB  &&  
                                                                 !"".equals(stringTableNameT)      &&  
                                                                 !"".equals(stringUpdateDataSql)  &&  
                                                                (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
              if(booleanFlag)  dbCon.execFromPool(stringSql) ;
              return  stringSql ;
       }
      // �R��
      public  String  doDeleteDB(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  boolean  booleanDB,  talk  dbCon) throws  Throwable {
              String        stringWhereSql     =  "" ;
              String        stringColumnName   =  "" ;
              String        stringColumnType   =  "" ;
              String        stringColumnValue  =  "" ;
              String        stringTableNameT   =  convert.StringToken(stringTableName,  "%-%")[0] ; // �קK���P��Ʈw�A�ۦP���W�ٮɡA[���]%-%[��Ʈw]
              String        stringIsNull       =  ""+hashtableAnd.get("ISNULL") ;hashtableAnd.remove("ISNULL") ;
              String[][]  retColumnsData       =  null ;
              //
              retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
              if(retColumnsData  ==  null) {
                     retColumnsData        =  dbCon.getColumnsFromPool(stringTableNameT) ;
                     hashtableColumnsData.put(stringTableName,  retColumnsData) ;
              }
              // SQL �y�k����
              for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                        stringColumnName   =  retColumnsData[intNo][0].trim() ;
                        stringColumnType   =  retColumnsData[intNo][2].trim() ;
                       // AND
                       stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
                       if(isNum(stringColumnType)  ||  "null".equals(stringColumnValue)) {
                              // �ƭ� 
                              if(doParseDouble(stringColumnValue)  ==  0)  stringColumnValue  =  "0" ;
                              if(!"".equals(stringWhereSql))  stringWhereSql  +=  " AND " ;
                              if("Y".equals(stringIsNull)) {
                                     stringWhereSql  +=  " ISNULL("+stringColumnName+",0) =  "+stringColumnValue+" \n" ;
                              } else {
                                     stringWhereSql  +=  " "+stringColumnName+" =  "+stringColumnValue+" \n" ;
                              }
                       } else {
                              // ��r
                              if(!"".equals(stringWhereSql))  stringWhereSql  +=  " AND " ;
                              if("Y".equals(stringIsNull)) {
                                     stringWhereSql  +=  "  ISNULL("+stringColumnName+",'') =  '"+stringColumnValue+"' \n" ;
                              } else {
                                     stringWhereSql  +=  "  "+stringColumnName+" =  '"+stringColumnValue+"' \n" ;
                              }
                       }
                       // �M��
                       hashtableAnd.remove(stringColumnName) ;
              }
              if(!"".equals(stringWhereSql)) {
                     stringWhereSql  =  " WHERE  "  +  stringWhereSql  +  " " ;
              } else {
                  if(stringSqlAnd.trim().toUpperCase().startsWith("AND")) {
                        int  intPos              =  stringSqlAnd.toUpperCase().indexOf("AND") ;    
                               stringSqlAnd  =  " WHERE  "  +  doSubstring(stringSqlAnd,  intPos+3,  stringSqlAnd.length()) ;
                  }
              }
              //
              String  stringSql  =  " DELETE  "  +  stringTableNameT  +  " \n" +
                                                   stringWhereSql +
                                                   stringSqlAnd ;
              //           
              boolean     booleanFlag  =  booleanDB  &&  
                                                                  !"".equals(stringTableNameT)      &&  
                                                                 (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;         
              if(booleanFlag)  dbCon.execFromPool(stringSql) ;
              return  stringSql ;
       }
      //
      public  boolean  isNum(String  stringType) throws  Throwable {
              if(vectorTxtType.size()  ==  0) {
                     //
                     vectorTxtType.add("varchar".toUpperCase()) ;
                     vectorTxtType.add("nvarchar".toUpperCase()) ;
                     vectorTxtType.add("char".toUpperCase()) ;
                     vectorTxtType.add("nchar".toUpperCase()) ;
                     vectorTxtType.add("ntext".toUpperCase()) ;
                     vectorTxtType.add("text".toUpperCase()) ;
                     //
                     vectorTxtType.add("d_casecode".toUpperCase()) ;
                     vectorTxtType.add("d_stemcode".toUpperCase()) ;
                     vectorTxtType.add("DACCT_NO".toUpperCase()) ;
                     vectorTxtType.add("DITEM_CD".toUpperCase()) ;
                     vectorTxtType.add("DCOMPAY_CD".toUpperCase()) ;
                     vectorTxtType.add("DDB_CR_CD".toUpperCase()) ;
                     vectorTxtType.add("DKIND".toUpperCase()) ;
                     vectorTxtType.add("DDEPT_CD".toUpperCase()) ;
                     vectorTxtType.add("OBJECT_CD".toUpperCase()) ;
                     vectorTxtType.add("DUSER".toUpperCase()) ;
                     vectorTxtType.add("DOBJECT_CD".toUpperCase()) ;
                     //
                     vectorTxtType.add("DOM_DEPT_CD".toUpperCase()) ;
                     vectorTxtType.add("DATETIME".toUpperCase()) ;
                     vectorTxtType.add("DYMD".toUpperCase()) ;
                     vectorTxtType.add("DYEAR".toUpperCase()) ;
                     vectorTxtType.add("DBANK_CD".toUpperCase()) ;
                     vectorTxtType.add("DOM_LAST_USER".toUpperCase()) ;
                     vectorTxtType.add("DOM_YMD".toUpperCase()) ;
                     vectorTxtType.add("DOM_YM".toUpperCase()) ;
                     vectorTxtType.add("DOM_EMP_NO".toUpperCase()) ;
                     vectorTxtType.add("DOM_ID_NO".toUpperCase()) ;
                     vectorTxtType.add("DOM_TEL".toUpperCase()) ;
                     vectorTxtType.add("DOM_EMP_NAME".toUpperCase()) ;
                     vectorTxtType.add("DOM_DEGREE_CD".toUpperCase()) ;
                     vectorTxtType.add("DOM_LEA_KIND_CD".toUpperCase()) ;
                     vectorTxtType.add("DOM_TITLE_CD".toUpperCase()) ;
                     vectorTxtType.add("DOM_WORK_CD".toUpperCase()) ;
                     vectorTxtType.add("DDEPT_CD".toUpperCase()) ;
                     vectorTxtType.add("DYEART".toUpperCase()) ;
                     vectorTxtType.add("DYMDT".toUpperCase()) ;
                     //
                     vectorTxtType.add("naoType".toUpperCase()) ;          // ����
                     vectorTxtType.add("DataStatus".toUpperCase()) ;       // ����
                     vectorTxtType.add("EmpNo".toUpperCase()) ;            // ����
                     vectorTxtType.add("UUID".toUpperCase()) ;             // ����
              }
              //
              boolean   booleanFlag  =  (vectorTxtType.indexOf(stringType.toUpperCase())==-1) ;
              return   booleanFlag;
       }

}
