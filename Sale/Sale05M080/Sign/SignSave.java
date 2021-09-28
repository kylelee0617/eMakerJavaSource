package  Sale.Sale05M080.Sign ;
import      javax.swing.*;
import      jcx.jform.bproc;
import      java.io.*;
import      java.util.*;
import      jcx.util.*;
import      jcx.html.*;
import      jcx.db.*;
import Farglory.util.KUtils;

/**
 * ��P�o���b�o�}
 * 
 * @author B04391
 *
 */

public  class  SignSave  extends  bproc {
    talk  dbSale    =  getTalk(""+get("put_dbSale"));
    talk dbInvoice = getTalk(""+get("put_dbInvoice"));
    talk dbTestAs400 = getTalk("test400CRM");
    talk dbAs400 = getTalk("400CRM");
    String GENLIB = ((Map)get("config")).get("GENLIB").toString().trim();
    KUtils kUtil = new KUtils();
    
    //����mapCustomers
    Map mapCustomers = new HashMap();
    
    public  String  getDefaultValue(String  value) throws  Throwable {
        
        //�ˬd����
        String serverIP = get("serverIP").toString().trim();
        String serverKind = serverIP.contains("172.16.14.4")? "prod":"test";
      
        //setValue("FlowStatus",  "�~��-�f��") ;
        //���ڽs��:0151M51A950620001
        //setValue("DocNo",  "0151M51A950620001") ;
        // ��ƳB�z
        int               intInvoiceNo                             =  0 ;
        int               intAvailableInvoice                   =  0 ;
        int               intCustomPos                          =  0 ;
        String         stringFlowStatus                      =  getValue("FlowStatus") ;
        String         stringInvoiceNo1                      =  "" ;
        String         stringDocNo                             =  getValue("DocNo").trim( ) ;
        String         stringCompanyNo                    =  "" ;
        String         stringCompanyCd                    =  "" ;
        String         stringDepartNo                        =  "" ;
        String         stringProjectID1                       =  "" ;
        String         stringEDate                             =  "" ;
        String         stringInvoiceNo                       =  "" ;
        String         stringEndYes                          =  "" ;
        String         stringCustomNo                      =  "" ;
        String         stringInvoiceKind                    =  "" ; 
        String         stringPercentage                    =  "" ;
        String         stringOrderNo                         =  "" ;
        String         stringCustomPos                    =  "" ;
        String         stringSUMReceiveCheck        =  "" ;
        String         stringSUMReceiveCheckUse  =  "" ;
        String         stringSUMDiscountCheck       =  "" ;// ���� Add
        String         stringKey                                =  "" ;
        String         stringITEMLS_CD                   =  "" ;
        String         stringORDER_NO                   =  "" ;
        String         stringInvoiceTotalMoney         =  "" ;
        String         stringPosition                          =  "" ;
        String         stringL_DiscountMoney          =  "" ;
        String         stringPointNo                          =  "" ;
        String[]       arrayPointNo                           =  {"2101",  "2102",  "2103",  "2104"} ;
        String[][]     retSale05M040                        =  null ;
        String[][]     retSale05M080                        =  null ;
        String[][]     retSale05M081                        =  null ;
        String[][]     retSale05M084P                      =  null ;
        String[][]     retSale05M084                        =  null ;
        String[][]     retSale05M085                        =  null ;
        String[][]     retSale05M085UPDATE          =  null ;
        String[][]     retSale05M086                        =  null ;
          String[][]     retInvoM022                            =  null ;
        String[][]     retInvoM010                            =  null ;
        float            floatSUMReceive                     =  0 ;
        double       doubleSUMReceive                  =  0 ;
        Hashtable  hashtableOrderNo                   =  new  Hashtable( ) ;
        Hashtable  hashtableSUMReceive             =  new  Hashtable( ) ;
        Hashtable  hashtableData                          =  new  Hashtable( ) ;
        Hashtable  hashtableTaxKind                     =  new  Hashtable( ) ;
        Vector        vectorSale05M061                   =  new  Vector( ) ;
        Vector        vectorSale05M061Sql              =  new  Vector( ) ;
        Vector        vectorRet                                 =  new  Vector( ) ;
        Vector        vectorCompanyCd                   =  null ;
        Vector        vectorPosition                          =  null ;
        Vector        vectorUniquePosition               =  new  Vector( ) ;
        Hashtable  hashtablePosition                     =  new  Hashtable() ;
        //
        for(int  intNo=0  ;  intNo<arrayPointNo.length  ;  intNo++) {
            stringPointNo    =  arrayPointNo[intNo] ;
            //  0  TaxRate   1  TaxKind
             retInvoM010  =  getInvoM010(stringPointNo) ;
             if(retInvoM010.length  ==  0) {
                  message("�d�L�������|�O�����C") ;
                return  value ;
             } else {
                  hashtableTaxKind.put(stringPointNo,  retInvoM010[0][1].trim( )) ;
             }
        }
        //  2802
        //  0  TaxRate   1  TaxKind
         retInvoM010  =  getInvoM010("2802") ;
         if(retInvoM010.length  ==  0) {
            message("�d�L�������|�O�����C") ;
            return  value ;
         } else {
            hashtableTaxKind.put("2802",  retInvoM010[0][1].trim( )) ;
         }
        //
        // doInsert_Sale05M080_Flow_HIS( ) ;  // ���v�ɰO��(�i�h��)  Primary Key �� DocNo�BFlowStatus�BEDateTime�C
        // doInsert_Sale05M080_Flow( ) ;         // ���v�ɰO��               Primary Key �� DocNo�BFlowStatus�BEDateTime�C
        // ��s���A FlowStatus (�~��-�f�֡B�g��)  Primary Key �� DocNo�C
        // doUpdate_Sale05M080( ) ;
        //
        getInternalFrame("���ڳ�-ñ��").setVisible(false);
        //
        if(stringFlowStatus.equals("�~��-�f��")){
            // �ˮ�(�o���O�_�}��) Sale05M087�GDocNo�BRecordNo
            stringInvoiceNo1  =  isCheckSale05M087Exist( ) ;
            if(!"".equals(stringInvoiceNo1)) {
                message(stringInvoiceNo1  +  "�o���w�}�ߡI") ;
                return  value ;
            }
            //Temp 
            //  0  CompanyNo      1  DepartNo     2  ProjectID1       3  EDate
            retSale05M080  =  getSale05M080Tmp( ) ; // �^�����Ȥ@�� �� �L
            for(int  intSale05M080=0  ;  intSale05M080<retSale05M080.length  ;  intSale05M080++) {
                stringCompanyNo  =  retSale05M080[intSale05M080][0].trim( ) ;
                stringDepartNo      =  retSale05M080[intSale05M080][1].trim( ) ;
                stringProjectID1     =  retSale05M080[intSale05M080][2].trim( ) ;
                stringEDate            =  retSale05M080[intSale05M080][3].trim( ) ; //��εo�������   
            }
            System.out.println(stringCompanyNo  +  "-----------------------"+
                             stringDepartNo      +  "-----------------------"+
                             stringProjectID1    +  "-----------------------"+
                             stringEDate) ;
            
            //�ˮֵo��������i�p���ѫe : by Kyle
            String today = datetime.getToday("YYYY/mm/dd");
            long subDays = kUtil.subACDaysRDay(today, stringEDate);
            if( "prod".equals(serverKind) ) {
              if(subDays > 2) {
                messagebox("�o��������i�p���ѫe�C�C�C �Y��(�E�עE)���� ");
                return value;
              }
            }
            
            // ���o���q
            vectorRet                    =  getCompanys(stringDocNo,  stringProjectID1) ;
            vectorCompanyCd      =  (Vector)    vectorRet.get(0) ;
            vectorPosition             =  (Vector)    vectorRet.get(1) ;
            retSale05M040           =  (String[][])  vectorRet.get(2) ;
            vectorUniquePosition  =  (Vector)     vectorRet.get(3) ;
            String    stringStatus  =  (String)      vectorRet.get(4) ;
            if(vectorCompanyCd ==  null  ||  vectorCompanyCd.size( )  ==  0  ||  "NULL".equals(stringStatus)) {
                message("�L�۹��������q��ơC") ;
                return  value ;
            }
            //  0  CustomNo    �^�Ǧh��
            Vector  vectorInvoiceKind  =  new  Vector( ) ;
            String   stringNationality     =  "" ;
            retSale05M084P  =  getSale05M084( ) ;  /* Primary Key �� DocNo�BCustomNo */
            for(int  intSale05M084=0  ;  intSale05M084<retSale05M084P.length  ;  intSale05M084++) {
                stringCustomNo = retSale05M084P[intSale05M084][0].trim( ) ;
                stringNationality = retSale05M084P[intSale05M084][1].trim( ) ;
                //
                stringInvoiceKind     =  (stringCustomNo.length( ) == 8)  ?  "3"  :  "2" ;
                if("2".equals(stringNationality))  stringInvoiceKind  =  "2" ;
                if("4".equals(stringNationality))  stringInvoiceKind  =  "2" ;      // 20100401 �s�W
                if(vectorInvoiceKind.indexOf(stringInvoiceKind)  ==  -1) {
                    vectorInvoiceKind.add(stringInvoiceKind) ;
                }
            }
            
            //�����ڳ�U�Ҧ��Ȥ�
            mapCustomers = this.getSale05M091Users();
            
            //
            Hashtable  hashtableAvailableInvoice     =  new  Hashtable( ) ;/* �i�Ϊ��o����(���q�O + �o������(3 or 2)) �w �̫���o���ƮɡA�n�ϥΡC*/
            //�O�@�o��������
            doDeleteSale05M085( ) ; 
            for(int  intCompany=0  ;  intCompany<vectorCompanyCd.size()  ;  intCompany++) {
                stringCompanyCd  =  ""  +  (String)  vectorCompanyCd.get(intCompany) ;
                //System.out.println(intCompany  +  "(�O�@�o��������)--------------------------------"  +  stringCompanyCd) ;
                for(int  intNo=0  ;  intNo<vectorInvoiceKind.size( )  ;  intNo++) {
                    stringInvoiceKind  =  (String)  vectorInvoiceKind.get(intNo) ;
                    //�o�����X�۰ʲ���
                    //  0  InvoiceYYYYMM    1  FSChar                2  StartNo             3  InvoiceBook      4  InvoiceStartNo
                    //  5  InvoiceEndNo           6  MaxInvoiceNo     7  MaxInvoiceDate
                    retInvoM022  =  getInvoM022(retSale05M080,  stringInvoiceKind,  stringCompanyCd) ;
                    if (retInvoM022.length == 0){
                        message("���q�G"  +  getCompanyName(stringCompanyCd)  +  "�B�קO�G"  +  stringProjectID1  +  " �� "  +  stringInvoiceKind  +  " �p���q���o���w�Χ�! �Ь� �]�ȫ� ���!");
                        doInvoM022Undo(vectorCompanyCd,  intCompany) ;// �N���A�Ѷ}�A�ϥL�H�i�H�ϥ�
                        return value; 
                        
                    }
                    //�o���i�θ��X
                    for(int  intInvoM022=0  ;  intInvoM022<retInvoM022.length  ;  intInvoM022++) {
                        String  stringEndNo  =  retInvoM022[intInvoM022][5].trim( ) ;
                        String  stringMaxNo  =  retInvoM022[intInvoM022][6].trim( ) ;
                        if(stringMaxNo.length( )  ==  0) {
                            intAvailableInvoice  =  intAvailableInvoice  +  50 ;
                        } else {
                            intAvailableInvoice  =  intAvailableInvoice + doParseInteger(stringEndNo.substring(2,10)) - doParseInteger(stringMaxNo.substring(2,10));
                        } 
            System.out.println("test0>>>" + intAvailableInvoice);
                    }//End of for int intInvoM022
                    hashtableAvailableInvoice.put((stringCompanyCd +  stringInvoiceKind),  ""+intAvailableInvoice) ;
                    //�O�@�o��������
                    for(int  intInvoM022=0  ;  intInvoM022<retInvoM022.length  ;  intInvoM022++) {
                        doUpdateInvoM022(stringCompanyCd,  retInvoM022[intInvoM022]) ;     // ��s���A�A�קK�L�H�ϥ�
                        doInsertSale05M085(stringCompanyCd,  retInvoM022[intInvoM022],stringInvoiceKind)  ;  // �N��ƽƻs�� Sale05M080
                    } 
                }             
            }
// �� Sale05M084 ���o���Ҧ��Ȥ� START
            int               intType                                                              =  0 ;
            int               intPos                                                                = 0 ;
            int               intCount                                                             =  0 ;
            Vector        vectorData                                                         =  new  Vector( ) ;
            Hashtable  hashtableCustomCompanyPositionTypeCount  =  new  Hashtable( ) ;  // [�o��������]�p��
            Hashtable  hashtableInvoiceTotalCount                               =  new  Hashtable( ) ;  // [�o��]�p��(���q�O + �o������(3 or 2))
            //System.out.println("(LOOP1)�� Sale05M084 ���o���Ҧ��Ȥ�--------"+retSale05M084P.length) ;
            for(int  intSale05M084P=0  ;  intSale05M084P<retSale05M084P.length  ;  intSale05M084P++) {
                // ���o���P�o�����A�`��
                stringCustomNo         =  retSale05M084P[intSale05M084P][0].trim( ) ;
                stringNationality         =  retSale05M084P[intSale05M084P][1].trim( ) ;
                stringInvoiceKind       =  (stringCustomNo.length( ) == 8)  ?  "3"  :  "2" ;
                if("2".equals(stringNationality))  stringInvoiceKind  =  "2" ;
                if("3".equals(stringNationality))  stringInvoiceKind  =  "2" ;    // 2010-3-5  �W�[
// ���o�S�w�Ȥ᪺�ʫ��ҩ��� START                           
                retSale05M086  =  getSale05M086(stringDocNo,  stringCustomNo) ;
                //System.out.println(intSale05M084P+"(LOOP2)���o�S�w�Ȥ᪺�ʫ��ҩ���--------"+retSale05M086.length) ;
                for(int  intSale05M086=0  ;  intSale05M086<retSale05M086.length  ;  intSale05M086++) {
                    stringOrderNo  =  retSale05M086[intSale05M086][0].trim( ) ;
/* ���o�S�w�Ȥ�B�S�w�ʫ��ҩ��椧���ڳ����  START*/
                    //   0  HouseCar                     1  Position                  2  ORDER_NO               3  ITEMLS_CD
                    //   4  H_MomentaryMoney     5  H_UsableMoney     6  H_ReceiveMoney       7  (H_ReceiveMoney + H_UsableMoney) AS H_InvoiceMoney
                    //   8  L_MomentaryMoney     9  L_UsableMoney    10  L_ReceiveMoney     11  (L_ReceiveMoney + L_UsableMoney) AS L_InvoiceMoney
                    // 12 H_DiscountMoney        13  H_FeeMoney        14  L_FeeMoney 
                    retSale05M081  =  getSale05M081(stringDocNo,  stringOrderNo,  stringCustomNo) ;
                    //
                    for(int  intSale05M081=0  ;  intSale05M081<retSale05M081.length  ;  intSale05M081++) {
                        String  stringHouseCar                        =  retSale05M081[intSale05M081][0].trim( ) ;    // �Ш�
                        String  stringH_MomentaryMoney       =  retSale05M081[intSale05M081][4].trim( ) ;     // ��-�Ȧ�
                        String  stringH_InvoiceMoney              =  retSale05M081[intSale05M081][7].trim( ) ;      // ��-�ꦬ+��-�i��
                        String  stringL_MomentaryMoney        =  retSale05M081[intSale05M081][8].trim( ) ;      // �g-�Ȧ�
                        String  stringL_InvoiceMoney               =  retSale05M081[intSale05M081][11].trim( ) ;  //�g-�ꦬ+�g-�i��
                        String  stringFeeMoney                        =  "" ;                                                                 // ���O��
                        String  stringFeeMoneyTemp               =  "" ;                                                                  // ���O��
                        String  stringFeeMoneyUse                  =  "" ;                                                                  // ���O��
                        String  stringSUMReceiveCheckFee    =  ""  ;
                        String  stringInvoiceTotalMoneyTemp  =  "" ;    
                        String  stringH_DiscountMoneyUse      =  "" ;
                        //
                        stringL_DiscountMoney        =  retSale05M081[intSale05M081][12].trim( ) ;  // �Q������
                        stringITEMLS_CD                 =  retSale05M081[intSale05M081][3].trim( ) ;//�ڶ�
                        stringORDER_NO                 =  retSale05M081[intSale05M081][2].trim( ) ; //���O
                        stringPosition                        =  retSale05M081[intSale05M081][1].trim( ) ;//��O
                        stringSUMReceiveCheck      =  stringHouseCar  +  stringPosition  +  stringORDER_NO ;
                        intPos                                   =  vectorPosition.indexOf(stringPosition) ;
                        stringPointNo                        =  "" ;
                        // oce
                        if("01002".equals(stringITEMLS_CD)) {
                            if(doParseDouble(stringH_InvoiceMoney)>0)   hashtablePosition.put(stringPosition+"_H",  "Y") ;
                            if(doParseDouble(stringL_InvoiceMoney)>0)   hashtablePosition.put(stringPosition+"_L",  "Y") ;
                        }
                        // 1 �ت���   2 �g�a��
                        for(int  intInvoice=1  ;  intInvoice<=2  ;  intInvoice++) {
                            // intType  ����
                            // 0 �Ы�-�ت���         1 �Ы�-�g�a��          2 ����-�ت���         3 ����-�g�a�� 
                            // 4 �Ы�-�ت���(��)    5 �Ы�-�g�a��(��)   6 ����-�ت���(��)  7 ����-�g�a�� (��)
                            stringH_DiscountMoneyUse    =  (intInvoice == 1) ? stringL_DiscountMoney : "0" ;  // ���� Add
                            //�Ыδ�
                            if (intInvoice == 1)  if (stringH_InvoiceMoney.length() ==0 || doParseFloat(stringH_InvoiceMoney) == 0) continue;
                            //�g�a��
                            if (intInvoice == 2)  if (stringL_InvoiceMoney.length() ==0 || doParseFloat(stringL_InvoiceMoney) == 0) continue;
                            //�Ы�-�ت���
                            if (stringHouseCar.equals("House")  &&  intInvoice == 1){
                                intType                                      =  0 ;
                                stringInvoiceTotalMoneyTemp  =  ""  +  doParseFloat(stringH_InvoiceMoney) * 10000 ;  //�o�����B
                                //
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                            }
                            //�Ы�-�g�a��
                            if (stringHouseCar.equals("House")  &&  intInvoice == 2){
                                intType                                     =  1 ;
                                stringInvoiceTotalMoneyTemp  =  ""  +  doParseFloat(stringL_InvoiceMoney) * 10000 ;   //�o�����B
                                //
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                            }
                            //����-�ت���    
                            if (stringHouseCar.equals("Car")  &&  intInvoice == 1){
                                intType                                     =  2 ;
                                stringInvoiceTotalMoneyTemp  =  ""  +  doParseFloat(stringH_InvoiceMoney) * 10000 ;   //�o�����B
                                //
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                            }
                            //����-�g�a��
                            if (stringHouseCar.equals("Car")  &&  intInvoice == 2){
                                intType                                     =  3 ;
                                stringInvoiceTotalMoneyTemp  =  ""  +  doParseFloat(stringL_InvoiceMoney) * 10000 ;    //�o�����B
                                //
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                            }
                            // ���o�P�@�Ӳ��~�b�P�@���ʫ��ҩ��椺�A���X���U�ȨæP����
                            int  intCustomCount               =  getSale05M086Count(stringDocNo,  stringCustomNo,  stringPosition) ;
                            stringSUMDiscountCheck       =  stringSUMReceiveCheck  +  "-"  +  stringH_DiscountMoneyUse ;      // ���� Add
                            stringSUMReceiveCheckUse  =  stringSUMReceiveCheck  +  "-"+stringInvoiceTotalMoneyTemp ;
                            if(intCustomCount  >  1) {
                                // ���� Start
                                if(doParseDouble(stringH_DiscountMoneyUse)  >  0) {
                                    intCustomPos  =  doParseInteger(""  +  (String)hashtableOrderNo.get(stringSUMDiscountCheck)) ;
                                    if(intCustomCount  !=  (intCustomPos+1)) {
                                        stringPercentage                  =  getSale05M091ForPercentage(stringOrderNo,  stringCustomNo) ;
                                        stringH_DiscountMoneyUse  =  "" + (doParseDouble(stringH_DiscountMoneyUse) * (doParseDouble(stringPercentage) /100)); 
                                        stringH_DiscountMoneyUse  =  convert.FourToFive(stringH_DiscountMoneyUse,  4);
                                        doubleSUMReceive              =  doParseDouble(""  +  (String)hashtableSUMReceive.get(stringSUMDiscountCheck)) ;
                                        doubleSUMReceive            +=  doParseDouble(stringH_DiscountMoneyUse);
                                        //
                                        hashtableOrderNo.put(stringSUMDiscountCheck,        ""  +  (intCustomPos+1)) ;
                                        hashtableSUMReceive.put(stringSUMDiscountCheck,  ""  +  doubleSUMReceive) ;
                                    } else {
                                        // �̫�@��
                                        doubleSUMReceive              =  doParseDouble(""  +  (String)hashtableSUMReceive.get(stringSUMDiscountCheck)) ;
                                        stringH_DiscountMoneyUse  =  "" + (doParseDouble(stringH_DiscountMoneyUse) - doubleSUMReceive);
                                        stringH_DiscountMoneyUse  =  convert.FourToFive(stringH_DiscountMoneyUse,4);
                                        hashtableSUMReceive.remove(stringSUMDiscountCheck) ;
                                        hashtableOrderNo.remove(stringSUMDiscountCheck) ;
                                    }
                                }
                                // ���� End
                                intCustomPos  =  doParseInteger(""  +  (String)hashtableOrderNo.get(stringSUMReceiveCheckUse)) ;
                                if(intCustomCount  !=  (intCustomPos+1)) {
                                    stringPercentage             =  getSale05M091ForPercentage(stringOrderNo,  stringCustomNo) ;
                                    stringInvoiceTotalMoney  =  "" + (doParseFloat(stringInvoiceTotalMoneyTemp) * (doParseFloat(stringPercentage) /100)); 
                                    stringInvoiceTotalMoney  =  convert.FourToFive(stringInvoiceTotalMoney,0);
                                    floatSUMReceive              =  doParseFloat(""  +  (String)hashtableSUMReceive.get(stringSUMReceiveCheckUse)) ;
                                    floatSUMReceive            +=  doParseFloat(stringInvoiceTotalMoney);
                                    // 
                                    //if(intInvoice == 2) {
                                        hashtableOrderNo.put(stringSUMReceiveCheckUse,        ""  +  (intCustomPos+1)) ;
                                        hashtableSUMReceive.put(stringSUMReceiveCheckUse,  ""  +  floatSUMReceive) ;
                                    //}
                                } else {
                                    //�̫�1�ӫȤ�δ
                                    floatSUMReceive              =  doParseFloat(""  +  (String)hashtableSUMReceive.get(stringSUMReceiveCheckUse)) ;
                                    stringInvoiceTotalMoney  =  "" + (doParseFloat(stringInvoiceTotalMoneyTemp) - floatSUMReceive);
                                    stringInvoiceTotalMoney  =  convert.FourToFive(stringInvoiceTotalMoney,0);
                                    //if(intInvoice == 2) {
                                        hashtableSUMReceive.remove(stringSUMReceiveCheckUse) ;
                                        hashtableOrderNo.remove(stringSUMReceiveCheckUse) ;
                                    //}
                                }
                            } else {                                
                                // �Ȥ@��Ȥ��
                                stringInvoiceTotalMoney  =  convert.FourToFive(stringInvoiceTotalMoneyTemp,0);
                            }
                            //System.out.println("stringInvoiceTotalMoneyTemp-----------------"+stringInvoiceTotalMoneyTemp+"---------"+stringSUMReceiveCheck+"-----"+stringInvoiceTotalMoney) ;
                            // �x�s���(�Ȥ�s��-���q-Position-���A-���A�ƥ�)
                            stringCompanyCd                      =  (intType==0  ||  intType==2) ?  retSale05M040[intPos][0] :  retSale05M040[intPos][1] ;
                            stringKey                                   =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intType ;
                            intCount                                     =  doParseInteger(""  +  (String)  hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
                            stringKey                                   =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intType]  +  intCount ;
                            /*System.out.println(stringCustomNo           +  "-----------------"  +
                                             stringCompanyCd        +  "-----------------"  +
                                             stringPosition               +  "-----------------"  +
                                             arrayPointNo[intType]  +  "-----------------"  +
                                             intCount                        +  "-----------------"  +  stringH_DiscountMoneyUse) ;*/
                            // 0  stringInvoiceKind               1  stringInvoiceTotalMoney      2  stringITEMLS_CD      3  stringORDER_NO       4  stringPosition
                            // 5  stringL_DiscountMoney     6  stringDocNo                7  L_COM            8  OrderNo
                            vectorData.add(stringInvoiceKind) ;
                            vectorData.add(stringInvoiceTotalMoney) ;
                            vectorData.add(stringITEMLS_CD) ;
                            vectorData.add(stringORDER_NO) ;
                            vectorData.add(stringPosition) ;
                            vectorData.add(stringH_DiscountMoneyUse) ;
                            vectorData.add(stringDocNo) ;
                            vectorData.add(retSale05M040[intPos][3]) ;
                            vectorData.add(stringOrderNo) ;
                            hashtableData.put(stringKey,  vectorData) ;
                            vectorData  =  new  Vector( ) ;
                        }
                        // ���O��
                        // 1 �ت���   2 �g�a��
                        for(int  intInvoice=1  ;  intInvoice<=2  ;  intInvoice++) {
                            // intType  ����
                            // 0 �Ы�-�ت���         1 �Ы�-�g�a��          2 ����-�ت���         3 ����-�g�a�� 
                            // 4 �Ы�-�ت���(��)    5 �Ы�-�g�a��(��)   6 ����-�ت���(��)  7 ����-�g�a�� (��)
                            // ���O�� S
                            if(intInvoice == 1) {
                                stringFeeMoney                      =  retSale05M081[intSale05M081][13].trim( ) ;
                            } else {
                                stringFeeMoney                      =  retSale05M081[intSale05M081][14].trim( ) ;
                            }// ���O��  E
                            if(doParseDouble(stringFeeMoney)  <=  0)  continue ;
                            //�Ы�-�ت���
                            if (stringHouseCar.equals("House")  &&  intInvoice == 1){
                                intType                         =  4 ;
                                stringFeeMoneyTemp  =  ""  +  doParseFloat(stringFeeMoney) * 10000 ;  //�o�����B
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                                //System.out.println("intType("+intType+")-----------------"+stringFeeMoneyTemp) ;
                            }
                            //�Ы�-�g�a��
                            if (stringHouseCar.equals("House")  &&  intInvoice == 2){
                                intType                         =  5 ;
                                stringFeeMoneyTemp  =  ""  +  doParseFloat(stringFeeMoney) * 10000 ;  //�o�����B
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                                //System.out.println("intType("+intType+")-----------------"+stringFeeMoneyTemp) ;
                            }
                            //����-�ت���    
                            if (stringHouseCar.equals("Car")  &&  intInvoice == 1){
                                intType                         =  6 ;
                                stringFeeMoneyTemp  =  ""  +  doParseFloat(stringFeeMoney) * 10000 ;  //�o�����B
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                                //System.out.println("intType("+intType+")-----------------"+stringFeeMoneyTemp) ;
                            }
                            //����-�g�a��
                            if (stringHouseCar.equals("Car")  &&  intInvoice == 2){
                                intType                         =  7 ;
                                stringFeeMoneyTemp  =  ""  +  doParseFloat(stringFeeMoney) * 10000 ;  //�o�����B
                                doCount(intPos,  intType,  stringCustomNo,  stringInvoiceKind,  retSale05M040,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                                //System.out.println("intType("+intType+")-----------------"+stringFeeMoneyTemp) ;
                            }
                            // ���o�P�@�Ӳ��~�b�P�@���ʫ��ҩ��椺�A���X���U�ȨæP����
                            int  intCustomCount               =  getSale05M086Count(stringDocNo,  stringCustomNo,  stringPosition) ;
                            stringSUMReceiveCheckFee  =  stringSUMReceiveCheck  +  "-"+stringFeeMoneyTemp+"Fee" ;
                            if(intCustomCount  >  1) {
                                intCustomPos  =  doParseInteger(""  +  (String)hashtableOrderNo.get(stringSUMReceiveCheckFee)) ;
                                if(intCustomCount  !=  (intCustomPos+1)) {
                                    stringPercentage             =  getSale05M091ForPercentage(stringOrderNo,  stringCustomNo) ;
                                    stringFeeMoneyUse  =  "" + (doParseFloat(stringFeeMoneyTemp) * (doParseFloat(stringPercentage) /100)); 
                                    stringFeeMoneyUse  =  convert.FourToFive(stringFeeMoneyUse,0);
                                    floatSUMReceive       =  doParseFloat(""  +  (String)hashtableSUMReceive.get(stringSUMReceiveCheckFee)) ;
                                    floatSUMReceive      +=  doParseFloat(stringInvoiceTotalMoney);
                                    hashtableOrderNo.put(stringSUMReceiveCheckFee,        ""  +  (intCustomPos+1)) ;
                                    hashtableSUMReceive.put(stringSUMReceiveCheckFee,  ""  +  floatSUMReceive) ;
                                } else {
                                    //�̫�1�ӫȤ�δ
                                    // ���O��
                                    floatSUMReceive       =  doParseFloat(""  +  (String)hashtableSUMReceive.get(stringSUMReceiveCheckFee)) ;
                                    stringFeeMoneyUse  =  "" + (doParseFloat(stringFeeMoneyTemp) - floatSUMReceive);
                                    stringFeeMoneyUse  =  convert.FourToFive(stringFeeMoneyUse,0);
                                    hashtableSUMReceive.remove(stringSUMReceiveCheckFee) ;
                                    hashtableOrderNo.remove(stringSUMReceiveCheckFee) ;
                                }
                            } else {                                
                                // �Ȥ@��Ȥ��
                                stringFeeMoneyUse         =  stringFeeMoneyTemp ;
                            }
                            //System.out.println("stringInvoiceTotalMoneyTemp-----------------"+stringInvoiceTotalMoneyTemp+"---------"+stringSUMReceiveCheck+"-----"+stringInvoiceTotalMoney) ;
                            // �x�s���(�Ȥ�s��-���q-Position-���A-���A�ƥ�)
                            stringCompanyCd                      =  (intType==0  ||  intType==2) ?  retSale05M040[intPos][0] :  retSale05M040[intPos][1] ;
                            stringKey                                   =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intType ;
                            intCount                                     =  doParseInteger(""  +  (String)  hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
                            stringKey                                   =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intType-4]  +  intCount +"Fee" ;
                            /*System.out.println("���O��--------------------["+stringKey+"]") ;
                            System.out.println(stringCustomNo               +  "-----------------"  +
                                             stringCompanyCd            +  "-----------------"  +
                                             stringPosition                   +  "-----------------"  +
                                             arrayPointNo[intType-4]   +  "-----------------"  +
                                             intCount                           +  "-----------------" +
                                             stringFeeMoneyUse         +  "-----------------"  +  stringH_DiscountMoneyUse) ;*/
                            // 0  stringInvoiceKind               1  stringInvoiceTotalMoney      2  stringITEMLS_CD      3  stringORDER_NO       4  stringPosition
                            // 5  stringL_DiscountMoney     6  stringDocNo                7  L_COM            8  OrderNo
                            vectorData.add(stringInvoiceKind) ;
                            vectorData.add(stringFeeMoneyUse) ;
                            vectorData.add(stringITEMLS_CD) ;
                            vectorData.add(stringORDER_NO) ;
                            vectorData.add(stringPosition) ;
                            vectorData.add("0") ;
                            vectorData.add(stringDocNo) ;
                            vectorData.add(retSale05M040[intPos][3]) ;
                            vectorData.add(stringOrderNo) ;
                            hashtableData.put(stringKey,  vectorData) ;
                            vectorData  =  new  Vector( ) ;
                        }
                        /* �Ȧ���(�@�Ӧ��ک��Ӫ�A�@�ӼȦ���) */
                        if(vectorSale05M061.indexOf(stringSUMReceiveCheck)  ==  -1) {
                            vectorSale05M061Sql  =  doUpdateSale05M061(stringH_MomentaryMoney,  stringL_MomentaryMoney,  stringProjectID1,
                                                                   stringHouseCar,                  stringPosition,                     stringORDER_NO,
                                                                   vectorSale05M061Sql) ;
                            vectorSale05M061.add(stringSUMReceiveCheck) ;
                        }
                    } 
// ���o�S�w�Ȥ�B�S�w�ʫ��ҩ��椧���ڳ���� END
                }
// ���o�S�w�Ȥ᪺�ʫ��ҩ��� END
            }
// �� Sale05M084 ���o���Ҧ��Ȥ�  END
            // stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intType ;
            // ���� 5 ���ت��o���A�[1 �B�z
            for(int  intSale05M084P=0  ;  intSale05M084P<retSale05M084P.length  ;  intSale05M084P++) {
                // ���o���P�o�����A�`��
                stringCustomNo         =  retSale05M084P[intSale05M084P][0].trim( ) ;
                stringNationality         =  retSale05M084P[intSale05M084P][1].trim( ) ;
                stringInvoiceKind       =  (stringCustomNo.length( ) == 8)  ?  "3"  :  "2" ;
                if("2".equals(stringNationality))  stringInvoiceKind  =  "2" ;
                for(int  intCompanyNo=0  ;  intCompanyNo<vectorCompanyCd.size( )  ;  intCompanyNo++) {
                    stringCompanyCd  =  ""  +  (String) vectorCompanyCd.get(intCompanyNo) ;
                    for(int  intPositionNo=0  ;  intPositionNo<vectorUniquePosition.size( )  ;  intPositionNo++) {
                        stringPosition  =  ""  +  (String) vectorUniquePosition.get(intPositionNo) ;
                        for(int  intNo=0  ;  intNo<8  ;  intNo++) {
                            doCount2(intNo,  stringCustomNo,  stringInvoiceKind,  stringCompanyCd,  stringPosition,  hashtableCustomCompanyPositionTypeCount,  hashtableInvoiceTotalCount) ;
                        }
                    }
                }
            }
            // �ˮ�
            int  intInvoiceTotalCount  =  0 ;  // �`�@�n�}�ߪ��o����
            for(int  intCompany=0  ;  intCompany<vectorCompanyCd.size()  ;  intCompany++) {
                stringCompanyCd  =  ""  +  (String)  vectorCompanyCd.get(intCompany) ;
                for(int  intNo=0  ;  intNo<vectorInvoiceKind.size()  ;  intNo++) {
                    stringInvoiceKind      =  (String)  vectorInvoiceKind.get(intNo) ;
                    intAvailableInvoice    =  doParseInteger(""  +  (String)   hashtableAvailableInvoice.get(stringCompanyCd  +  stringInvoiceKind)) ;
                    intInvoiceTotalCount =  doParseInteger(""  +  (String)hashtableInvoiceTotalCount.get(stringCompanyCd  +  stringInvoiceKind)) ;
                    //System.out.println(intCompany  +  "-----------"  +  intNo+"-----------"+intAvailableInvoice+"------------------"+intInvoiceTotalCount) ;
                    if (intInvoiceTotalCount  >  intAvailableInvoice) {
                        //message("���q�G"  +  getCompanyName(stringCompanyCd)  +  "���q���o�������I �Ь� �]�ȫ� ����I�|��"  +  (intInvoiceTotalCount  -  intAvailableInvoice)  +  "�i") ;
                        message("���q�G"  +  getCompanyName(stringCompanyCd)  +  "�B�קO�G"  +  stringProjectID1  +  " �� "  +  stringInvoiceKind  +  " �p���q���o�������I �Ь� �]�ȫ� ����I�|��"  +  (intInvoiceTotalCount  -  intAvailableInvoice)  +  "�i") ;
                        doInvoM022Undo(vectorCompanyCd,  vectorCompanyCd.size( )) ;// �N���A�Ѷ}�A�ϥL�H�i�H�ϥ�
                        return  value ;
                    } 
                }
            }
            // �� Sale05M084 ���o���Ҧ��Ȥ� START
            int            intRecordNo                      =  1 ;  //for Sale05M087  
            int            intDiscountCount              =  1 ;
            int            intDiscountCountInvoice   =  0 ;
            int            intRecordNoSale05M089  =  1 ;
            int            intRecordMax                    =  0 ;
            //int            intFeeCount                      =  0 ;
            Object     objtectReturn                    =  null ;
            Vector     vectorReturn                     =  null ;
            String      stringITEMLS_CHINESE    =  "" ;
            String      stringInvoiceMoney           =  "" ;
            String      stringInvoiceTax                =  "" ;
            String      stringInvoiceMessage        = "";
            String      stringInvoiceYYYYMM       =  "" ;
            String      stringFSChar                     =  "" ;
            String      stringStartNo                     =  "" ;
            String      stringInvoiceBook              =  "" ;
            String      stringInvoiceStartNo           =  "" ;
            String      stringInvoiceEndNo            =  "" ;
            String      stringMaxInvoiceNo            =  "" ;
            String      stringTaxRate                    =  "" ;
            String      stringTaxKind                     =  "" ; 
            String      stringTemp                        =  "" ;
            String      stringDiscountNo               =  "" ;
            String      stringLCom                        =  "" ;
            String      stringFeeMoney                 =  "" ;
            //String      stringInvoiceNoFee            =  "" ;
            double    doubleInvoiceMoney          =  0 ;
            double    doubleInvoiceTotalMoney  =  0 ;
            //double    doubleInvoiceTotalMoneyFee  =  0 ;
            double    doubleInvoiceTax               =  0 ;
            double    doubleDiscountMoney       =  0 ;
            boolean  booleanNextNotNull            =  true ;
            boolean  booleanDiscountFlag         =  false ; 
            boolean  booleanFlag                       =  true ;
            Vector     vectorLCom                       =  new  Vector( ) ;
            //
            vectorLCom.add("1") ;
            vectorLCom.add("K") ;
                vectorLCom.add("L") ;
            vectorLCom.add("P") ; //990609
            vectorLCom.add("U") ; //2010/10/14
            vectorLCom.add("4") ; //1001019
            vectorLCom.add("A5") ; //2014-05-15 B3018
            // �Ȥ�s��-���q-Position-���A-���A�ƥ�
            for(int  intSale05M084P=0  ;  intSale05M084P<retSale05M084P.length  ;  intSale05M084P++) {
                System.out.println("1111111111�i�J Sale05M084P START =============================");
                stringCustomNo  =  retSale05M084P[intSale05M084P][0].trim( ) ;
                stringNationality  =  retSale05M084P[intSale05M084P][1].trim( ) ;
                stringInvoiceKind     =  (stringCustomNo.length( ) == 8)  ?  "3"  :  "2" ;  //�p��
                if("2".equals(stringNationality))  stringInvoiceKind  =  "2" ;
                if("3".equals(stringNationality))  stringInvoiceKind  =  "2" ;      //  2101-3-5
                if("4".equals(stringNationality))  stringInvoiceKind  =  "2" ;      // 20100401
                for(int  intPositionNo=0  ;  intPositionNo<vectorUniquePosition.size( )  ;  intPositionNo++) {
                    System.out.println("22222222�i�J vectorUniquePosition START =============================" + intPositionNo + "/" + vectorUniquePosition);    
                    stringPosition  =  ""  +  (String)  vectorUniquePosition.get(intPositionNo) ;
                    for(int  intCompanNo=0  ;  intCompanNo<vectorCompanyCd.size( )  ;  intCompanNo++) {
                        System.out.println("3333333333�i�J vectorCompanyCd START =============================" + intCompanNo + "/" + vectorCompanyCd);   
                        stringCompanyCd  =  ""  +  (String)  vectorCompanyCd.get(intCompanNo) ;
                        for(int  intNo=0  ;  intNo<4  ;  intNo++) {
                            System.out.println("44444444�i�J intNo START =============================" + intNo + "/" + 4); 
                            intDiscountCount               =  1 ;  // ������y����
                            intDiscountCountInvoice    =  0 ;
                            doubleInvoiceMoney          =  0 ;
                            doubleInvoiceTotalMoney  =  0 ;
                            doubleInvoiceTax               =  0 ;
                            doubleDiscountMoney        =  0 ;
                            booleanDiscountFlag          =  false ;
                            stringKey                             =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intNo ;
                            System.out.println("stringKey===========>"+stringKey);
                            System.out.println("hashtableCustomCompanyPositionTypeCount===========>"+hashtableCustomCompanyPositionTypeCount);
                            intRecordMax                      =  doParseInteger(""  +  (String)hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
                            System.out.println("intRecordMax===========>"+intRecordMax);
                            for(int  intRecord=1  ;  intRecord<=intRecordMax  ;  intRecord++) {
                                System.out.println("55555555�i�J intRecordMax START =============================");    
                                booleanNextNotNull  =  true ;
                                stringKey                 =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intNo]  +  intRecord ;
                                objtectReturn           =  hashtableData.get(stringKey) ;
                                if(objtectReturn  ==  null) break ;
                                // ���o���
                                // 0  stringInvoiceKind               1  stringInvoiceTotalMoney      2  stringITEMLS_CD      3  stringORDER_NO       4  stringPosition
                                // 5  stringL_DiscountMoney     6  stringDocNo                7  L_COM            8  OrderNo
                                vectorReturn                    =  (Vector) objtectReturn ;
                                stringInvoiceKind              =  (String)  vectorReturn.get(0) ;
                                stringInvoiceTotalMoney  =  (String)  vectorReturn.get(1) ;
                                stringITEMLS_CD            =  (String)  vectorReturn.get(2) ;               //  2101-3-5 �ק�
                                stringITEMLS_CHINESE   =  getItemlsChinese(stringITEMLS_CD) ;         //  2101-3-5 �ק�
                                stringORDER_NO            =  (String)  vectorReturn.get(3) ;
                                stringPosition                   =  (String)  vectorReturn.get(4) ;
                                stringL_DiscountMoney    =  (String)  vectorReturn.get(5) ;
                                stringDocNo                      =  (String)  vectorReturn.get(6) ;
                                stringLCom                       =  (String)  vectorReturn.get(7) ;
                                stringOrderNo                   =  (String)  vectorReturn.get(8) ;
                                System.out.println("��--------------------"+
                                                               stringCustomNo            +  "-----------------"  +
                                                     stringCompanyCd        +  "-----------------"  +
                                                     stringPosition               +  "-----------------"  +
                                                     arrayPointNo[intNo]      +  "-----------------"  +
                                                     intRecord                     +  "------------------"  +  stringL_DiscountMoney) ;
                                // �P�_�U�@���O�_����
                                stringKey                          =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intNo]  +  (intRecord+1) ;
                                objtectReturn                    =  hashtableData.get(stringKey) ;
                                if(objtectReturn  ==  null) booleanNextNotNull  =  false ;
                                // ���o�o�����X
                                if(intRecord  %  5  ==  1) {
                                    System.out.println("6666666666�i�J Sale05M084P START =============================");   
                                    /*�o�����X�۰ʲ���(�Ȥ@��)*/
                                    //   0  InvoiceYYYYMM     1  FSChar     2  StartNo    3  InvoiceBook    4  InvoiceStartNo
                                    //   5  InvoiceEndNo      6  MaxInvoiceNo
                                    retSale05M085  =  getSale05M085(retSale05M080,stringCompanyCd,stringInvoiceKind) ;
                                    for(int  intSale05M085=0  ;  intSale05M085<retSale05M085.length  ;  intSale05M085++) {
                                        stringInvoiceYYYYMM  =  retSale05M085[intSale05M085][0].trim( ) ;
                                        stringFSChar               =  retSale05M085[intSale05M085][1].trim( ) ;
                                        stringStartNo                =  retSale05M085[intSale05M085][2].trim( ) ;
                                        stringInvoiceBook         =  retSale05M085[intSale05M085][3].trim( ) ;
                                        stringInvoiceStartNo     =  retSale05M085[intSale05M085][4].trim( ) ;
                                        stringInvoiceEndNo      =  retSale05M085[intSale05M085][5].trim( ) ;
                                        stringMaxInvoiceNo      =  retSale05M085[intSale05M085][6].trim( ) ;
                                        //�o�����X
                                        if(stringMaxInvoiceNo.length( )  ==  0) {
                                            stringInvoiceNo  =  stringInvoiceStartNo ;
                                        } else  {
                                            intInvoiceNo       =  doParseInteger(stringMaxInvoiceNo.substring(2,10))  +  1 ;
                                            stringInvoiceNo  =  stringMaxInvoiceNo.substring(0,2)  +  convert.add0(("" + intInvoiceNo),"8") ;
                                        }
                                        System.out.println("���o�o�����X==================>"+stringInvoiceNo);
                                      }
                                      //System.out.println("------------------------�o�����X�G"+stringInvoiceNo) ;
                                    //�o���w�}��Flag 
                                    if(stringInvoiceNo.equals(stringInvoiceEndNo))
                                        stringEndYes = "Y";
                                    else
                                        stringEndYes = "N";
                                    //�o���T�� 
                                    stringInvoiceMessage  +=  stringInvoiceNo  +  "\n" ;
                                }
                                 //�p��|�v
                                /* //  0  TaxRate   1  TaxKind
                                retInvoM010  =  getInvoM010(arrayPointNo[intNo]) ;
                                if (retInvoM010.length  ==  0){
                                    message("�o���t��.�K�n�N�X ���~!");
                                    return  value ; 
                                }*/
                                // �s�J DB
                                /*�o��.InvoM030(Head)(�@��)*/
                                if(intNo  ==  0) {
                                    if(!"".equals(stringL_DiscountMoney)  &&  doParseDouble(stringL_DiscountMoney)  !=  0) {
                                        doubleDiscountMoney  +=  doParseDouble(stringL_DiscountMoney) *  10000 ;
                                        intDiscountCountInvoice++ ;
                                    }
                                }
                                doubleInvoiceTotalMoney  +=  doParseDouble(stringInvoiceTotalMoney)  ;
                                if(intRecord % 5 == 0  ||  !booleanNextNotNull) {
                                    doubleInvoiceTotalMoney  +=  doubleDiscountMoney  ;
                                    if(intNo  ==  0  ||  intNo  ==  2) {
                                        doubleInvoiceMoney  =  (doubleInvoiceTotalMoney / 1.05) ;
                                    } else {
                                        doubleInvoiceMoney  =  doubleInvoiceTotalMoney ;
                                    }
                                    doubleInvoiceTax       =  doubleInvoiceTotalMoney  -  doubleInvoiceMoney;
                                    stringPointNo  =  arrayPointNo[intNo].trim( ) ;
                                    //
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "M51A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H51A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H50A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H71A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ; //990603
                                    }
                                    // 2010/10/14  �ץ�
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H80A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    //1001018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H68A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    //2012/05/21 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H102A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H102A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2013/09/04 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H75A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H75A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2012/09/05 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H117A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H117A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014/04/08 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H118A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H118A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014-05-15 B3018 START
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H76A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H76A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014-05-15 B3018 END
                                    //  2101-3-5 �ק� Start
                                    if("2101".equals(stringPosition)  ||  "2102".equals(stringPosition)) {
                                        // ��
                                        booleanFlag  =  "Y".equals(""+hashtablePosition.get(stringPosition+"_H")) ;
                                    } else if("2103".equals(stringPosition)  ||  "2104".equals(stringPosition)) {
                                        // �g
                                        booleanFlag  =  "Y".equals(""+hashtablePosition.get(stringPosition+"_L")) ;
                                    } else {
                                        booleanFlag  =  true ;
                                    }
                                    /*if("H72A".equals(stringProjectID1) &&  booleanFlag  &&  "2103".equals(stringPointNo) &&  !"A30148".equals(stringPosition)) {
                                    //if("H72A".equals(stringProjectID1) &&  "01002".equals(stringITEMLS_CD)  &&  "2103".equals(stringPointNo) &&  !"A30148".equals(stringPosition)) {
                                        stringPointNo  =  "2113" ;
                                    }
                                    if("H72A".equals(stringProjectID1) &&  booleanFlag   &&  "2104".equals(stringPointNo) &&  !"A30148".equals(stringPosition)) {
                                    //if("H72A".equals(stringProjectID1) && "01002".equals(stringITEMLS_CD)  &&  "2104".equals(stringPointNo) &&  !"A30148".equals(stringPosition)) {
                                        stringPointNo  =  "2112" ;
                                    }*/
                                    if("2101".equals(stringPointNo) &&  "3".equals(stringNationality)) {
                                        stringPointNo  =  "2114" ;
                                    }
                                    if("2103".equals(stringPointNo) &&  "3".equals(stringNationality)) {
                                        stringPointNo  =  "2115" ;
                                    }
                                    // 20100401 START B3018
                                    if("2101".equals(stringPointNo) &&  "4".equals(stringNationality)) {
                                        stringPointNo  =  "2114" ;
                                    }
                                    if("2103".equals(stringPointNo) &&  "4".equals(stringNationality)) {
                                        stringPointNo  =  "2115" ;
                                    }
                                    // 20100401 END
                                    stringTaxKind  =  (""+hashtableTaxKind.get(stringPointNo)).trim( ) ;
                                    if("".equals(stringTaxKind)  ||  "null".equals(stringTaxKind)) {
                                         retInvoM010  =  getInvoM010(stringPointNo) ;
                                         if(retInvoM010.length  ==  0) {
                                            
                                         } else {
                                            hashtableTaxKind.put(stringPointNo,  retInvoM010[0][1].trim( )) ;
                                            stringTaxKind  =  retInvoM010[0][1].trim( ) ;
                                         }
                                    }
                                    if("2114".equals(stringPointNo)  ||  "2115".equals(stringPointNo)) {
                                        doubleInvoiceMoney  =  doubleInvoiceTotalMoney ;
                                        doubleInvoiceTax       =  0 ;
                                    }
                                    //  2101-3-5 �ק� End
                                    doInvertInvoM030(stringInvoiceNo,                                                 stringInvoiceKind,                                                          stringPosition,  
                                                   stringCustomNo,                                                 stringPointNo,                                                               convert.FourToFive(""+doubleInvoiceMoney,  0),
                                                   convert.FourToFive(""+doubleInvoiceTax,  0),  convert.FourToFive(""+doubleInvoiceTotalMoney,0),  stringTaxKind,
                                                   stringCompanyCd,                                             retSale05M080,                                                              ""  +  doubleDiscountMoney,
                                                   intDiscountCountInvoice) ;
                                    doubleInvoiceMoney          =  0 ;
                                    doubleInvoiceTotalMoney  =  0 ;
                                    doubleInvoiceTax               =  0  ;
                                    intDiscountCountInvoice    =  0 ;
                                    doInvertInvoM0C0(stringCustomNo,  stringOrderNo);
                                }
                                /*�o��.InvoM031(Body)(�h��)*/
                                if(doParseInteger(stringORDER_NO)<=  0)stringORDER_NO   =  "" ;
                                
                                doInvertInvoM031(stringInvoiceNo,  intRecord,  stringITEMLS_CHINESE,  stringORDER_NO) ;
                                //System.out.println("---------------"+stringORDER_NO+"-----------") ;
                                //
                                if(intRecord  %  5  ==  1) {
                                    doInsertSale05M087(stringDocNo,  intRecordNo,  stringInvoiceNo) ;
                                    intRecordNo++ ;
                                }
                                //�o��. UPDATE Sale05M085(�o���ޱ���)
                                if(intRecord  %  5  ==  1) {
                                    doUpdateSale05M085(stringInvoiceNo,            stringEDate,                  stringEndYes,
                                                      stringCompanyCd,        stringInvoiceYYYYMM,  stringFSChar,  
                                                      stringStartNo,         stringInvoiceBook) ;
                                }
                                //
                                if(intNo  !=  0) continue ;
                                else {
                                    //System.out.println(intSale05M084P+"-------"  +  intSale05M081+"-------"  +intNo+"--------------------------------------------"+intRecord+"--------A") ;
                                    if(!("".equals(stringL_DiscountMoney)  ||  doParseDouble(stringL_DiscountMoney)  ==  0))  {
                                        //System.out.println(intSale05M084P+"-------"  +  intSale05M081+"-------"  +intNo+"--------------------------------------------"+intRecord+"--------B") ;
                                        /* ���o�����渹�X */
                                        if(intDiscountCount  %  5  ==  1) {
                                            stringDiscountNo        =  getDiscountNo(stringCompanyCd,  retSale05M080) ;
                                            booleanDiscountFlag  =  true ;
                                        }
                                        // doubleDiscountMoney  +=  doParseDouble(stringL_DiscountMoney) ;
                                        //System.out.println(intSale05M084P+"-------"  +  intSale05M081+"-------"  +intNo+"--------------------------------------------"+intRecord+"--------C") ;
                                        /* ������ InvoM040 (Head)(�@��) */
                                        if(intDiscountCount  %  5  ==  0) {
                                            // �s�W
                                            //System.out.println("-----------------------�s�W�Y��") ;
                                            doInsertInvoM040(stringDiscountNo,  stringCustomNo,  stringCompanyCd,  retSale05M080,  doubleDiscountMoney) ;
                                            //System.out.println(intNo+"--------------------------------------------"+intRecord+"--------D") ;
                                            booleanDiscountFlag  =  false ;
                                            doubleDiscountMoney  =  0 ;
                                            
                                        }
                                        // ������ InvoM041
                                        //System.out.println("-----------------------�s�W����") ;
                                        doInsertInvoM041(stringDiscountNo,           intDiscountCount,  stringInvoiceNo,  arrayPointNo[intNo], 
                                                       ""  +  (doParseDouble(stringL_DiscountMoney)*10000)) ;
                                        //
                                        doInvertSale05M089(stringDocNo,  intRecordNoSale05M089,  stringDiscountNo,  stringInvoiceNo) ;
                                        intRecordNoSale05M089++ ;
                                        // �s�W
                                        intDiscountCount++ ;
                                    }
                                }
                                if((!booleanNextNotNull  ||  intRecord  %  5  ==  0)  &&  doubleDiscountMoney  >  0  &&  booleanDiscountFlag) {
                                    //System.out.println("-----------------------�s�W�Y��") ;
                                    doInsertInvoM040(stringDiscountNo,  stringCustomNo,  stringCompanyCd,  retSale05M080,  doubleDiscountMoney) ;
                                    //System.out.println(intNo+"--------------------------------------------"+intRecord+"--------E") ;
                                    doubleDiscountMoney  =  0 ;
                                    booleanDiscountFlag  =  false ;
                                }
                            }//
                        }
                        
                        // ���O��
                        //System.out.println("���O��--------------------S") ;
                        for(int  intNo=4  ;  intNo<8  ;  intNo++) {
                            //
                            intDiscountCount               =  1 ;  // ������y����
                            intDiscountCountInvoice    =  0 ;
                            doubleInvoiceMoney          =  0 ;
                            doubleInvoiceTotalMoney  =  0 ;
                            doubleInvoiceTax               =  0 ;
                            doubleDiscountMoney        =  0 ;
                            booleanDiscountFlag          =  false ;
                            stringKey                             =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intNo ;
                            intRecordMax                      =  doParseInteger(""  +  (String)hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
                            for(int  intRecord=1  ;  intRecord<=intRecordMax  ;  intRecord++) {
                                booleanNextNotNull  =  true ;
                                stringKey                 =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intNo-4]  +  intRecord+"Fee" ;
                                objtectReturn           =  hashtableData.get(stringKey) ;
                                if(objtectReturn  ==  null) break ;
                                // ���o���
                                // 0  stringInvoiceKind               1  stringInvoiceTotalMoney      2  stringITEMLS_CD      3  stringORDER_NO       4  stringPosition
                                // 5  stringL_DiscountMoney     6  stringDocNo                7  L_COM            8  stringFeeMoneyUse
                                vectorReturn                    =  (Vector) objtectReturn ;
                                stringInvoiceKind              =  (String)  vectorReturn.get(0) ;
                                stringInvoiceTotalMoney  =  (String)  vectorReturn.get(1) ;
                                stringITEMLS_CHINESE   =  getItemlsChinese((String)  vectorReturn.get(2)) ;
                                stringORDER_NO            =  (String)  vectorReturn.get(3) ;
                                stringPosition                   =  (String)  vectorReturn.get(4) ;
                                stringL_DiscountMoney    =  (String)  vectorReturn.get(5) ;
                                stringDocNo                      =  (String)  vectorReturn.get(6) ;
                                stringLCom                       =  (String)  vectorReturn.get(7) ;
                                /*System.out.println("��-���O��--------------------"+
                                                               stringCustomNo            +  "-----------------"  +
                                                     stringCompanyCd        +  "-----------------"  +
                                                     stringPosition               +  "-----------------"  +
                                                     arrayPointNo[intNo-4]   +  "-----------------"  +
                                                     intRecord                     +  "------------------"  +
                                                 stringInvoiceTotalMoney    +  "------------------"  +  stringL_DiscountMoney) ;*/
                                // �P�_�U�@���O�_����
                                stringKey                          =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  arrayPointNo[intNo-4]  +  (intRecord+1)+"Fee" ;
                                objtectReturn                    =  hashtableData.get(stringKey) ;
                                if(objtectReturn  ==  null) booleanNextNotNull  =  false ;
                                // ���o�o�����X
                                if(intRecord  %  5  ==  1) {
                                    /*�o�����X�۰ʲ���(�Ȥ@��)*/
                                    //   0  InvoiceYYYYMM     1  FSChar     2  StartNo    3  InvoiceBook    4  InvoiceStartNo
                                    //   5  InvoiceEndNo      6  MaxInvoiceNo
                                    retSale05M085  =  getSale05M085(retSale05M080,stringCompanyCd,stringInvoiceKind) ;
                                    for(int  intSale05M085=0  ;  intSale05M085<retSale05M085.length  ;  intSale05M085++) {
                                        stringInvoiceYYYYMM  =  retSale05M085[intSale05M085][0].trim( ) ;
                                        stringFSChar               =  retSale05M085[intSale05M085][1].trim( ) ;
                                        stringStartNo                =  retSale05M085[intSale05M085][2].trim( ) ;
                                        stringInvoiceBook         =  retSale05M085[intSale05M085][3].trim( ) ;
                                        stringInvoiceStartNo     =  retSale05M085[intSale05M085][4].trim( ) ;
                                        stringInvoiceEndNo      =  retSale05M085[intSale05M085][5].trim( ) ;
                                        stringMaxInvoiceNo      =  retSale05M085[intSale05M085][6].trim( ) ;
                                        //�o�����X
                                        if(stringMaxInvoiceNo.length( )  ==  0) {
                                            stringInvoiceNo  =  stringInvoiceStartNo ;
                                        } else  {
                                            intInvoiceNo       =  doParseInteger(stringMaxInvoiceNo.substring(2,10))  +  1 ;
                                            stringInvoiceNo  =  stringMaxInvoiceNo.substring(0,2)  +  convert.add0(("" + intInvoiceNo),"8") ;
                                        }
                                      }
                                      //System.out.println("------------------------�o�����X�G"+stringInvoiceNo) ;
                                    //�o���w�}��Flag 
                                    if(stringInvoiceNo.equals(stringInvoiceEndNo))
                                        stringEndYes = "Y";
                                    else
                                        stringEndYes = "N";
                                    //�o���T�� 
                                    stringInvoiceMessage  +=  stringInvoiceNo  +  "\n" ;
                                }
                                // �s�J DB
                                doubleInvoiceTotalMoney  +=  doParseDouble(stringInvoiceTotalMoney)  ;
                                if(intRecord % 5 == 0  ||  !booleanNextNotNull) {
                                    doubleInvoiceTotalMoney  +=  doubleDiscountMoney  ;
                                    if(intNo  ==  4  ||  intNo  ==  6) {
                                        doubleInvoiceMoney  =  (doubleInvoiceTotalMoney / 1.05) ;
                                    } else {
                                        doubleInvoiceMoney  =  doubleInvoiceTotalMoney ;
                                    }
                                    doubleInvoiceTax       =  doubleInvoiceTotalMoney  -  doubleInvoiceMoney;
                                    stringPointNo  =  arrayPointNo[intNo-4].trim( ) ;
                                    //
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "M51A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H51A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H50A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ;
                                    } 
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H71A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ; //990603
                                    }                                         
                                    // 2010/10/14
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H80A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ; 
                                    } 
                                    //1001018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H68A".equals(stringProjectID1)  &&  ("2102".equals(stringPointNo)  ||  "2104".equals(stringPointNo))) {
                                        stringPointNo  =  "2802" ; 
                                    }                                     
                                    //2012/05/21 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H102A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H102A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2013/09/04 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H75A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H75A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2012/09/05 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H117A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H117A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014/04/08 B3018
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H118A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H118A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014-05-15 B3018 START
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H76A".equals(stringProjectID1)  &&  "2102".equals(stringPointNo)) {
                                        stringPointNo  =  "2802" ; //2802    ���U�N�P-�N���g�a��  2102    �g�a��
                                    }
                                    if(vectorLCom.indexOf(stringLCom)  != -1  &&  "H76A".equals(stringProjectID1)  &&  "2104".equals(stringPointNo)) {
                                        stringPointNo  =  "2803" ; //2803    ���U�N�P-�N�����g��  2104    ����ڢw�g�a 
                                    }
                                    //2014-05-15 END
                                    // 2010/03/15
                                    stringTaxKind  =  (""+hashtableTaxKind.get(stringPointNo)).trim( ) ;
                                    if("".equals(stringTaxKind)  ||  "null".equals(stringTaxKind)) {
                                         retInvoM010  =  getInvoM010(stringPointNo) ;
                                         if(retInvoM010.length  ==  0) {
                                            
                                         } else {
                                            hashtableTaxKind.put(stringPointNo,  retInvoM010[0][1].trim( )) ;
                                            stringTaxKind  =  retInvoM010[0][1].trim( ) ;
                                         }
                                    }
                                    doInvertInvoM030(stringInvoiceNo,                                                 stringInvoiceKind,                                                          stringPosition,  
                                                   stringCustomNo,                                                 stringPointNo,                                                               convert.FourToFive(""+doubleInvoiceMoney,  0),
                                                   convert.FourToFive(""+doubleInvoiceTax,  0),  convert.FourToFive(""+doubleInvoiceTotalMoney,0),  stringTaxKind,
                                                   stringCompanyCd,                                             retSale05M080,                                                              "0",
                                                   0) ;
                                    doubleInvoiceMoney          =  0 ;
                                    doubleInvoiceTotalMoney  =  0 ;
                                    doubleInvoiceTax               =  0  ;
                                    intDiscountCountInvoice    =  0 ;
                                    doInvertInvoM0C0(stringCustomNo,  stringOrderNo);
                                }
                                /*�o��.InvoM031(Body)(�h��)*/
                                if(doParseInteger(stringORDER_NO)<=  0)stringORDER_NO   =  "" ;
                                
                                doInvertInvoM031(stringInvoiceNo,  intRecord,  stringITEMLS_CHINESE,  stringORDER_NO) ;
                                //System.out.println("---------------"+stringORDER_NO+"-----------") ;
                                //
                                if(intRecord==intRecordMax)  {
                                    doInvertInvoM031(stringInvoiceNo,  intRecord+1,  "���O��",  "") ;
                                }
                                if(intRecord  %  5  ==  1) {
                                    doInsertSale05M087(stringDocNo,  intRecordNo,  stringInvoiceNo,  "Y") ;
                                    intRecordNo++ ;
                                }
                                //�o��. UPDATE Sale05M085(�o���ޱ���)
                                if(intRecord  %  5  ==  1) {
                                    doUpdateSale05M085(stringInvoiceNo,            stringEDate,                  stringEndYes,
                                                      stringCompanyCd,        stringInvoiceYYYYMM,  stringFSChar,  
                                                      stringStartNo,         stringInvoiceBook) ;
                                }
                            }//
                        }
                        //System.out.println("���O��--------------------E") ;
                    }
                }
            }
            // �Ȧs�� vectorSale05M061Sql
            if(vectorSale05M061Sql.size( )  >  0)
                dbSale.execFromPool((String[])  vectorSale05M061Sql.toArray(new  String[0])) ;
            // 
            for(int  intCompanNo=0  ;  intCompanNo<vectorCompanyCd.size( )  ;  intCompanNo++) {
                stringCompanyCd           =  ""  +  (String)  vectorCompanyCd.get(intCompanNo) ;
                retSale05M085UPDATE  =  getSale05M085(stringCompanyCd) ;
                for(int intSale05M085UPDATE = 0 ;intSale05M085UPDATE < retSale05M085UPDATE.length;intSale05M085UPDATE++){
                    doUpdateInvoM022(retSale05M085UPDATE[intSale05M085UPDATE],  stringCompanyCd) ;
                }
            }
            // ������(Sale05M089)
            String      stringRecordNo    =  "" ;
            String       stringSql              =  "" ;
            String[][]  retInvoM041         =  null ;
            String[][]  retSale05M089    =  getSale05M089(stringDocNo) ;
            Vector     vectorSql              =  new  Vector( ) ;
            //  0  DiscountNo       1  InvoiceNo
            for(int  intNo=0  ;  intNo<retSale05M089.length  ;  intNo++) {
                stringDiscountNo             =  retSale05M089[intNo][0].trim() ;
                stringInvoiceNo                =  retSale05M089[intNo][1].trim() ;
                stringInvoiceTotalMoney  =  getInvoiceTotalMoney(stringInvoiceNo) ;
                //
                if("".equals(stringInvoiceTotalMoney))  continue ;
                
                stringSql             =  "UPDATE  InvoM041  SET  InvoiceTotalMoney  =  "  +  stringInvoiceTotalMoney  +  
                                 " WHERE  DiscountNo  =  '"  +  stringDiscountNo  +  "' "  +
                                       " AND  InvoiceNo  =  '"     +  stringInvoiceNo     +  "' " ;
                vectorSql.add(stringSql) ;
                
            }
            if(vectorSql.size()  >  0)dbInvoice.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
            //
            doInsert_Sale05M080_Flow_HIS( ) ;  // ���v�ɰO��(�i�h��)  Primary Key �� DocNo�BFlowStatus�BEDateTime�C
            doInsert_Sale05M080_Flow( ) ;         // ���v�ɰO��               Primary Key �� DocNo�BFlowStatus�BEDateTime�C
            doUpdate_Sale05M080( ) ;
            
            //�o���}�ߦ��\�A�ǰe²�T�A�A�^���o���T�� (�������Ҥ~�o²�T)
            if( "prod".equals(serverKind) ) {
              getButton("ButtonCellphone").doClick() ;
            }
            
            //20190117 �j�B�q���B�z�P�_ BEGIN
            //doBLCASH(stringDocNo);
            //20190117 �j�B�q���B�z�P�_ FINISH
            JOptionPane.showMessageDialog(null,  stringInvoiceMessage,"�o�� �}�ߦ��\! ���͵o���T��", JOptionPane.INFORMATION_MESSAGE); 
            //
        }
        //
        //showForm("���ڳ�(Sale05M080)");
        setValue("FlowStatus",stringFlowStatus);
        getButton("button3").setLabel("ñ�֬y�{:[" + stringFlowStatus + "]");
        //getInternalFrame("�ʫ�ñ��").setClosed(true);
        return value;
    }
    
    //2019/02/13 == �B�z�j�B�q�����
    public void doBLCASH(String docNo) throws  Throwable {
      String stringSql = "";
      String updateSql = "";
      String[][] retSale05M080Data = null;
      String[][] retSale05M084Data = null;
      //
      stringSql = " SELECT ProjectID1, EDate, CashMoney "  + 
                  " FROM Sale05M080 "  +
                " WHERE DocNo = '"  + docNo.trim( )  +  "' AND NoticeBLCASH = 'N'";
      retSale05M080Data = dbSale.queryFromPool(stringSql);
      
      double limitMaxAmt = 0.0d;
      if(retSale05M080Data.length > 0 && Double.parseDouble(retSale05M080Data[0][2]) > limitMaxAmt) {
        String nowDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
        nowDateTime = nowDateTime.replaceAll("/", "-");       
        String stringUserID = getUser() ;
        String stringDeptID = "5800800";
        int tradeAmt = (int) Double.parseDouble(retSale05M080Data[0][2]);       
        stringSql = "SELECT CustomNo FROM Sale05M084 WHERE DocNo = '" + docNo.trim() + "' ORDER BY RecordNo";
        retSale05M084Data = dbSale.queryFromPool(stringSql);
        
        //�ǳƳq��
        String cntDate = retSale05M080Data[0][1].trim().replaceAll("/", "-")+" 00:00:00";
        String trnType = "���ʲ����ڭq��";
        String ntCode = "TWN";
        String stringCreator = "RealtySale";
        //deptNo = 5800800
        //���� max CASH_ID
        String[][] cashID = null;
        stringSql = "SELECT max(CASH_ID) as max_cash_id FROM BLCASH";
        cashID = dbAs400.queryFromPool(stringSql);
        
        for(int i = 0; i < retSale05M084Data.length; i++) {
          int insertCashID = Integer.parseInt(cashID[0][0]) + 1 + i;
          String insertSql = "INSERT INTO BLCASH "+
                        "(CASH_ID,INP_EMPNO,INP_DEPT,KEY_NO,CASE_NO,CNT_DATE,TRN_TYPE,CLIENT_ID,CLIENT_NAT,AGENT_ID,AGENT_NAT,TRN_DATE,TRN_AMOUNT,IS_VALID,CREATOR,CREATETIME) "+
                        "VALUES "+
                        "("+insertCashID+",'"+stringUserID+"','"+stringDeptID+"','"+docNo.trim()+"','"+retSale05M080Data[0][0]+"','"+cntDate+"','"+trnType+"','"+retSale05M084Data[i][0]+"','"+ntCode+"','','','"+cntDate+"',"+tradeAmt+",'Y','"+stringCreator+"','"+nowDateTime+"')";
          dbAs400.execFromPool(insertSql);
        }
        
        //��s�q���ɶ�        
        updateSql = "UPDATE Sale05M080 SET NoticeBLCASH = 'Y', NoticeBLCASHdtime = '"+nowDateTime+"' WHERE DocNo = '" + docNo.trim() + "'";
        dbSale.execFromPool(updateSql);
      }
    }
    
    // ���q START
    // stringStatus ��ܶ�����^�_���ƥ�
    public  void  doInvoM022Undo(Vector  vectorCompanyCd,  int  intCompanySize) throws  Throwable {
        String      stringCompanyCd            =  "" ;
        String[][]  retSale05M085UPDATE  =  null ;
        //
        if(intCompanySize  >=  vectorCompanyCd.size( ))  return ;
        //System.out.println("�^�_ InvoM022 �����A  START------------------------------------------------------------"+intCompanySize) ;
        for(int  intCompanNo=0  ;  intCompanNo<intCompanySize  ;  intCompanNo++) {
            stringCompanyCd           =  ""  +  (String)  vectorCompanyCd.get(intCompanNo) ;
            retSale05M085UPDATE  =  getSale05M085(stringCompanyCd) ;
            for(int intSale05M085UPDATE = 0 ;intSale05M085UPDATE < retSale05M085UPDATE.length;intSale05M085UPDATE++){
                doUpdateInvoM022Undo(retSale05M085UPDATE[intSale05M085UPDATE],  stringCompanyCd) ;
            }
        }
        doDeleteSale05M085( ) ;
        //System.out.println("�^�_ InvoM022 �����A  END------------------------------------------------------------") ;
    }
    public  String  doStringSubstring(String  stringObject,  int  intStart,  int  intEnd) {
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
    public  int  doParseInteger(String  stringNum) {
        System.out.println("stringNum>>>" + stringNum) ;
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
    public  float  doParseFloat(String  stringNum) {
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
    public  double  doParseDouble(String  stringNum) {
        // 
        double  doubleNum  =  0 ;
        if("".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
        try{
            doubleNum  =  Double.parseDouble(stringNum) ;
        } catch(Exception e) {
                System.out.println("�L�k��R["  +  stringNum  +  "]�A�^�� 0�C") ;
            return  0 ;
        }
        return  doubleNum ;
    }
    // hashtableCustomCompanyPositionTypeCount  �� KEY �ȭp���`�o������    �U��+���q+�ɼӧO+���A
    // hashtableInvoiceTotalCount                              �� KEY �ȭp���`�o���i��  ���q+�o���榡
    public  void  doCount(int  intPos,  int  intType,  String  stringCustomNo,  String  stringInvoiceKind,  String[][]  retSale05M040,
                      Hashtable  hashtableCustomCompanyPositionTypeCount,  Hashtable  hashtableInvoiceTotalCount) throws  Throwable {
        int        intCount                     =  0 ;
        int        intInvoiceTotalCount  =  0 ;
        String  stringKey                   =  "" ;
        String  stringPosition             =  "" ;
        String  stringCompanyCd      =  "" ;
        //
        stringPosition         =  retSale05M040[intPos][2] ;
        stringCompanyCd  =  (intType==0  ||  intType==2) ?  retSale05M040[intPos][0].trim( ) : retSale05M040[intPos][1].trim( ) ;
        stringKey               =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intType ;
        intCount                 =  doParseInteger(""  +  (String)  hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
        intCount++ ;
        //System.out.println("hashtableCustomCompanyPositionTypeCount-------------"+stringKey+"------------"+intCount) ;
        hashtableCustomCompanyPositionTypeCount.put(stringKey,  ""  +  intCount) ;
        if(intCount  %  5  ==  0)  {
            stringKey                   =  stringCompanyCd  +  stringInvoiceKind ;
            intInvoiceTotalCount  =  doParseInteger(""  +  (String)  hashtableInvoiceTotalCount.get(stringKey))  +  1;
            //System.out.println("hashtableInvoiceTotalCount-------------"+stringKey+"------------"+intInvoiceTotalCount) ;
            hashtableInvoiceTotalCount.put(stringKey,  ""  +  intInvoiceTotalCount) ;
        }
    }
    public  void  doCount2(int  intType,  String  stringCustomNo,  String  stringInvoiceKind,  String  stringCompanyCd,  String  stringPosition,  
                         Hashtable  hashtableCustomCompanyPositionTypeCount,  Hashtable  hashtableInvoiceTotalCount) throws  Throwable {
        int        intCount                     =  0 ;
        int        intInvoiceTotalCount  =  0 ;
        String  stringKey                   =  "" ;
        //
        stringKey               =  stringCustomNo  +  stringCompanyCd  +  stringPosition  +  intType ;
        intCount                 =  doParseInteger(""  +  (String)  hashtableCustomCompanyPositionTypeCount.get(stringKey)) ;
        //System.out.println("hashtableCustomCompanyPositionTypeCount-------------"+stringKey+"------------"+intCount) ;
        if(intCount  %  5  !=  0)  {
            stringKey                   =  stringCompanyCd  +  stringInvoiceKind ;
            intInvoiceTotalCount  =  doParseInteger(""  +  (String)  hashtableInvoiceTotalCount.get(stringKey))  +  1;
            //System.out.println("hashtableInvoiceTotalCount-------------"+stringKey+"------------"+intInvoiceTotalCount) ;
            hashtableInvoiceTotalCount.put(stringKey,  ""  +  intInvoiceTotalCount) ;
        }
    }
    public  Vector  getCompanys(String  stringDocNo,  String  stringProjectID1) throws  Throwable {
        //
        String          stringSql                                 =  "" ;
        String          stringHCompany                     =  ""  ;
        String          stringLCompany                     =  ""  ;
        String          stringPosition                         =  "" ;
        String          stringStatus                           =  "OK" ;
        String          stringLCom                            = "" ;
        String          stringHCom                            = "" ;
        String[]         arraySale05M040                 =  null ;
        String[]        arrayHCom                            =  null ;
        String[]        arrayLCom                            =  null ;
        String[][]      retSale05M040                      =  null ;
        Vector         vectorResult                          =  new  Vector( ) ;
        Vector         vectorCompanyCd                =  new  Vector( ) ;
        Vector         vectorPosition                       =  new  Vector( ) ;
        Vector         vectorUniquePosition            =  new  Vector( ) ;
        Vector         vectorCompanyCdPerson     =  new  Vector( ) ;
        Vector         vectorSale05M040                =  new  Vector( ) ;
        Hashtable   hashtaleHCompanyPositioin  =  new  Hashtable( ) ;
        Hashtable   hashtaleLCompanyPositioin  =  new  Hashtable( ) ;
        //
        vectorCompanyCdPerson.add("1") ;
        vectorCompanyCdPerson.add("K") ;
        vectorCompanyCdPerson.add("L") ;
        vectorCompanyCdPerson.add("P") ; //990609
        vectorCompanyCdPerson.add("U") ; //2010/10/14
        vectorCompanyCdPerson.add("4") ; //1001019
        vectorCompanyCdPerson.add("A5") ; //2014-05-15 B3018
        // 0  H_Com       1 L_Com         2  Position
        //System.out.println("getSale05M040----------------------------") ;
        retSale05M040  =  getSale05M040(stringDocNo,  stringProjectID1) ;
        for(int  intSale05M040=0  ;  intSale05M040<retSale05M040.length  ;  intSale05M040++) {
            arraySale05M040  =  new  String[4] ;
            stringHCom          =  retSale05M040[intSale05M040][0].trim( ) ;
            stringLCom          =  retSale05M040[intSale05M040][1].trim( ) ;
            stringHCompany  =  getACom(stringHCom) ;
            if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "M51A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H51A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H50A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;               
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H71A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //990603                          
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H80A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2010/10/14
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H68A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //1001018           
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H102A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2012/04/30 B3018
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H75A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2013/09/04 B3018
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H76A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2014-5-15 B3018
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H117A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2013/09/05 B3018
            }else if(vectorCompanyCdPerson.indexOf(stringLCom)  !=  -1  &&  "H118A".equals(stringProjectID1)) {
                stringLCompany  =  stringHCompany ;     //2014/04/08 B3018
            } else {
                stringLCompany  =   getACom(stringLCom) ;
            }
            
            //if("".equals(stringHCompany)  ||  "".equals(stringLCompany))  stringStatus  =  "NULL" ;
            stringPosition      =  retSale05M040[intSale05M040][2].trim( ) ;
            //
            arraySale05M040[0]  =  stringHCompany ;
            arraySale05M040[1]  =   stringLCompany ;
            arraySale05M040[2]  =   stringPosition ;
            arraySale05M040[3]  =   stringLCom ;
            vectorPosition.add(stringPosition) ;
            if(vectorUniquePosition.indexOf(stringPosition)  ==  -1) {
              vectorUniquePosition.add(stringPosition) ;
            }
            if(!"".equals(stringHCompany)  &&  vectorCompanyCd.indexOf(stringHCompany)  ==  -1) {
              //20180109 add by FGlife-B03812  01 ���O(�س])���B�z
              if("CS".equals(stringHCompany)) {
                vectorCompanyCd.add(stringHCompany) ;
              }
            }
            if(!"".equals(stringLCompany)  &&  vectorCompanyCd.indexOf(stringLCompany)  ==  -1) {
              //20180109 add by FGlife-B03812  01 ���O(�س])���B�z
              if("CS".equals(stringLCompany)) {
                vectorCompanyCd.add(stringLCompany) ;
              }
            }
            vectorSale05M040.add(arraySale05M040) ;
        }
        vectorResult.add(vectorCompanyCd) ;  // ������
        vectorResult.add(vectorPosition) ;
        vectorResult.add((String[][])  vectorSale05M040.toArray(new  String[0][0])) ;
        vectorResult.add(vectorUniquePosition) ;// ������
        vectorResult.add(stringStatus) ;
        return  vectorResult ;
    }
    public  String[][]  getSale05M040(String  stringDocNo,  String  stringProjectID1) throws  Throwable {
        String      stringSql            =  "" ;
        String[][]  retSale05M040  =  null ;
        // 0  H_Com       1 L_Com         2  Position
        stringSql  =  "SELECT  DISTINCT  T040.H_Com, T040.L_Com,  T040.Position "  +
                       " FROM  Sale05M086 T86,  Sale05M092 T92,  Sale05M081 T81,  Sale05M091 T91,  Sale05M040 T040 "  +
                     " WHERE  T86.DocNo  =  '"  +  stringDocNo  +  "' "  +
                     " AND  T92.OrderNo  =  T86.OrderNo " +
                     " AND  T81.DocNo  =  T86.DocNo "  + 
                     " AND  T81.Position  =  T92.Position "  +
                     " AND  ISNULL(T92.StatusCd,  '')  <>  'D' "  +
                     " AND  T92.OrderNo  =  T91.OrderNo "  +
                     " AND  ISNULL(T91.StatusCd,  '')  <>  'C' "  +
                     " AND  T81.Position  =  T040.Position "  +  //
                     " AND  T81.HouseCar  =  T040.HouseCar "  +  //
                     " AND  T040.ProjectID1  =  '"  +  stringProjectID1  +"' "  ;
        retSale05M040  =  dbSale.queryFromPool(stringSql);
        return  retSale05M040 ;
    }
    public  String  getACom(String  stringComNo) throws  Throwable {
        String      stringSql                 =  "" ;
        String      stringCompanyCd  =  "" ;
        String[][]  retACom                =  null ;
        // 
        stringSql  =  "SELECT  COMPANY_CD " +
                     " FROM  A_COM "  +
                   " WHERE  Com_No  =  '"  +  stringComNo  +  "' " ;
        retACom  =  dbSale.queryFromPool(stringSql);
        if(retACom.length  !=  0) {
            stringCompanyCd  =  retACom[0][0].trim( ) ;
        }
        return  stringCompanyCd ;
    }
    public  String  getCompanyName(String  stringCompanyCd) throws  Throwable {
        String      stringSql                 =  "" ;
        String      stringCompanyName  =  "" ;
        String[][]  retACom                =  null ;
        // 
        stringSql  =  "SELECT  Com_Name " +
                     " FROM  A_COM "  +
                   " WHERE  COMPANY_CD  =  '"  +  stringCompanyCd  +  "' " ;
        retACom  =  dbSale.queryFromPool(stringSql);
        if(retACom.length  !=  0) {
            stringCompanyName  =  doStringSubstring(retACom[0][0].trim( ),  0,  6) ;
        }
        return  stringCompanyName ;
    }
    // ���q END
    // ������ START
    // ���o�����渹�X
    // 0  CompanyNo     1  DepartNo       2  ProjectID1       3  EDate
    public  String  getDiscountNo(String  stringCompanyCd,  String[][]  retSale05M080) throws  Throwable {
        //
        String      stringSql                =  "" ;
        String      stringDiscountNo   =  "" ;
        String      stringmaxNo           =  "" ;
        //String      stringCompanyNo  =  "" ;
        String      stringDepartNo       =  "" ;
        String      stringEDate            =  "" ;
        String      stringYM                 =  "" ;
        String       stringProjectID1    =  "" ;
        String[][]  retInvoM040           =  null ;
        for(int intSale05M080 = 0 ;intSale05M080 < retSale05M080.length;intSale05M080++){
            //stringCompanyNo  =  retSale05M080[intSale05M080][0].trim();
            stringDepartNo      =  retSale05M080[intSale05M080][1].trim();
            stringProjectID1     =  retSale05M080[intSale05M080][2].trim();
            stringEDate           =   retSale05M080[intSale05M080][3].trim(); 
            stringYM                =  (Integer.parseInt(stringEDate.substring(0,4))-1911)  +  stringEDate.substring(5,7) ;
        }
        //
        stringSql  =  "SELECT  MAX(DiscountNo) "  +
                   " FROM  InvoM040 "  +
                   " WHERE  CompanyNo  =  '"  +  stringCompanyCd  +  "' "  +
                         " AND  DepartNo  =  '"  +  stringDepartNo  +  "' "  +
                     " AND  SUBSTRING(DiscountDate, 1, 7)  =  SUBSTRING('"  +  stringEDate  +  "' , 1, 7) " ;
        retInvoM040  =  dbInvoice.queryFromPool(stringSql) ;
        //if (retInvoM040.length  >  0){    
        if (retInvoM040[0][0].length()>0){    
               stringmaxNo  =  retInvoM040[0][0];
               stringmaxNo  =  "000"  +  (Integer.parseInt(stringmaxNo.substring(stringmaxNo.length()-3)) + 1);
               stringmaxNo  =  stringmaxNo.substring(stringmaxNo.length()  -  3); 
               stringDiscountNo        =  stringCompanyCd  +  convert.add0("",  ""+(5-stringDepartNo.length()))  +  stringDepartNo  +  stringYM  +  stringmaxNo ;
        }else{
               stringDiscountNo        =  stringCompanyCd  +  convert.add0("",  ""+(5-stringDepartNo.length()))  +  stringDepartNo  +  stringYM  +  "001" ;
        }  
        //System.out.println("------------------------�����渹�G"+stringDiscountNo) ;
        return  stringDiscountNo ;
    }
    // 0  CompanyNo     1  DepartNo       2  ProjectID1       3  EDate
    public  void  doInsertInvoM040(String  stringDiscountNo,  String  stringCustomNo,  String  stringCompanyCd,  String[][]  retSale05M080,  double  doubleDiscountMoney) throws  Throwable {
         String    stringEDate                =  "" ;
         String    stringProjectID1         =  "" ;
         String    stringCompanyNo       =  "" ;
         String    stringDepartNo           =  "" ;
         String    stringSql                     =  "" ;
         String  stringDiscountMoney    =  convert.FourToFive(""+doubleDiscountMoney,0) ;
         String  stringInvoiceMoney       =  "" ;
         String  stringInvoiceTax            =  "" ;
         double  doubleInvoiceMoney   =  doubleDiscountMoney  /  1.05 ;
         double  doubleInvoiceTax        =  doubleDiscountMoney  -  doubleInvoiceMoney ;
        for(int intSale05M080 = 0 ;intSale05M080 < retSale05M080.length;intSale05M080++){
            stringCompanyNo  =  retSale05M080[intSale05M080][0].trim();
            stringDepartNo      =  retSale05M080[intSale05M080][1].trim();
            stringProjectID1     =  retSale05M080[intSale05M080][2].trim();
            stringEDate           =   retSale05M080[intSale05M080][3].trim(); 
        }
         //
         stringInvoiceMoney  =  convert.FourToFive(""+doubleInvoiceMoney,0) ;
         stringInvoiceTax       =  convert.FourToFive(""+doubleInvoiceTax,0) ;
         //
        stringSql  =  "INSERT  INTO  InvoM040 ( DiscountNo,     DiscountDate,              CompanyNo,         DepartNo,                           ProjectNo, "  +
                                        " HuBei,              CustomNo,                    DiscountWay,       NewHuBeiORCustomNo,  DiscountMoney, "  +
                                        " DiscountTax,   DiscountTotalMoney,  PrintYes,               PrintTimes,                         DELYes, "  +
                                        " LuChangYes,  EmployeeNo,                ModifyDateTime,  ProcessDiscountNo ) "  +
                                 " VALUES ( '"  +  stringDiscountNo                                        +  "', "  +  // �����渹�X
                                          " '"  +  stringEDate                                                +  "', "   +  // ��������
                                                " '"  +  stringCompanyCd                                      +  "', "   +  // ���q�N�X
                                          " '"  +  stringDepartNo                                          +  "', "   +  // �����N�X
                                          " '"  +   stringProjectID1                                        +  "', "    +  // �קO�N�X            4
                                          " '"  +  ""                                                               +  "', "   +  // ��O�N�X
                                          " '"  +   stringCustomNo                                         +  "', "    +  // �Ȥ�N�X(�νs)
                                          " '"  +  "B"                                                             +  "', "   +  // �����覡
                                          " '"  +  ""                                                                +  "', "   +  // ��O�N�X or �Ȥ�N�X
                                               stringInvoiceMoney                                     +  ", "    +  // �������|���B       9
                                               stringInvoiceTax                                          +  ", "    +  // �����|�B
                                                 stringDiscountMoney                                   +  ", "    +  // �����`���B
                                          " '"  +  "N"                                                              +  "', "   +  // �w�C�L(Y/N)
                                                 "0"                                                              +  ", "   +  // �ɦL����
                                         " '"  +  "N"                                                              +  "', "   +  // �@�u                     14
                                         " '"  +  "N"                                                              +  "', "   +  // �J�b
                                         " '"  +  getUser( )                                                    +  "', "  +  // �ק�H
                                         " '"  +  datetime.getTime("YYYY/mm/dd h:m:s")  +  "', "  +  // �ק�ɶ�
                                         " '"   +  "1"                                                              +  "') "  ;  //                                18
        //System.out.println("doInsertInvoM040-----------------"+stringSql) ;
        dbInvoice.execFromPool(stringSql);  
    }
    public  String[][]  getInvoM041(String  stringDiscountNo) throws  Throwable {
          String       stringSql        =  "" ;
          String[][]   retInvoM041  =  null ;
          //  0  DiscountNo       1  RecordNo
          stringSql  =  "SELECT  DiscountNo,  RecordNo "  +
                               " FROM  InvoM041 "  +
                     " WHERE  DiscountNo  =  '"  +  stringDiscountNo  +  "' " ;
          //System.out.println("getInsertInvoM041-----------------"+stringSql) ;
          retInvoM041  =  dbInvoice.queryFromPool(stringSql);
          return  retInvoM041 ;
    }
    public  void  doInsertInvoM041(String  stringDiscountNo,           int  intDiscountCount,  String  stringInvoiceNo,  String  stringITEMLS_CHINESE, 
                              String  stringInvoiceTotalMoney) throws  Throwable {
          stringInvoiceTotalMoney           =  convert.FourToFive(""+stringInvoiceTotalMoney,0) ;
          double  doubleInvoiceMoney   =  Double.parseDouble(stringInvoiceTotalMoney)  /  1.05 ;
          double  doubleInvoiceTax        =  Double.parseDouble(stringInvoiceTotalMoney)  -  doubleInvoiceMoney ;
          String    stringSql                     =  "" ;
          String    stringInvoiceMoney     =  convert.FourToFive(""+doubleInvoiceMoney,0) ;
          String    stringInvoiceTax         =  convert.FourToFive(""+doubleInvoiceTax,0) ;
          //
          stringSql  =  "INSERT  INTO  Invom041  ( DiscountNo,       RecordNo,   ChoiceYES,                 InvoiceNo,               PointNo, "  +
                                           " InvoiceMoney,  InvoiceTax,  InvoiceTotalMoney,  YiDiscountMoney,  DiscountItemMoney) "  +
                                     " VALUES ( '"  +  stringDiscountNo                      +  "', "   + // �����渹�X
                                                   intDiscountCount                     +  ", "   +  // ����
                                           " '"  +  "Y"                                          +  "', "  +  // �Ŀ�
                                           " '"  +  stringInvoiceNo                       +  "', "   +  // �o�����X
                                           " '"  +  stringITEMLS_CHINESE         +  "', "   +  // �K�n�N�X
                                                stringInvoiceMoney                +  ", "    +  // �o�����|�B
                                                stringInvoiceTax                     +  ", "    +  // �o���|�B
                                                stringInvoiceTotalMoney        +  ", "    +  // �o���`���B
                                                "0"                                         +  ", "    +  // �w�������B 
                                                stringInvoiceTotalMoney        +  ") "   ;    // �������B
          //System.out.println("doInsertInvoM041-----------------"+stringSql) ;
          dbInvoice.execFromPool(stringSql);
    }
    // ������ END
    public  String  getItemlsChinese(String  stringITEMLS_CD) throws  Throwable {
        //
        String      stringSql                   =  "" ;
        String      stringItemlsChinese  =  "" ;
        String[][]  retSale05M052         =  null ;
        //
        stringSql   = " SELECT  ITEMLS_CHINESE " + 
                    " FROM  Sale05M052 "  +
                  " WHERE  ITEMLS_CD  =  '"  +  stringITEMLS_CD  +  "' " ;
        retSale05M052  =  dbSale.queryFromPool(stringSql) ;
        if(retSale05M052.length  !=  0) {
            stringItemlsChinese  =  retSale05M052[0][0].trim( ) ;
        }
        return  stringItemlsChinese ;
    }
    public  void  doUpdateSale05M061(int        intInvoice,                   String  stringReceiveMoney,  String  stringProjectID1,
                                   String   stringHouseCar,         String  stringPosition,             String  stringORDER_NO) throws  Throwable {
        String  stringSql  =  " UPDATE Sale05M061 ";
        //�Ыδ�
        if (intInvoice  ==  1) {
            stringSql  +=  " SET  H_ReceiveMoney = H_ReceiveMoney + "    + stringReceiveMoney + "," +
                                  " HL_ReceiveMoney = HL_ReceiveMoney + " + stringReceiveMoney ;
        }
        //�g�a��   
        if (intInvoice == 2){     
            stringSql  +=  " SET  L_ReceiveMoney = L_ReceiveMoney + "     + stringReceiveMoney + "," +
                            " HL_ReceiveMoney = HL_ReceiveMoney + " + stringReceiveMoney ;
        }
        stringSql  +=  " WHERE  ProjectID1  =  '" + stringProjectID1 +  "' "  +
                         " AND  HouseCar  =  '"  + stringHouseCar  +  "' "  +
                       " AND  Position  =  '"     + stringPosition     +  "' "  +
                         " AND  ORDER_NO  =  " +  stringORDER_NO ;
        //System.out.println("doUpdateSale05M061-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    public  Vector  doUpdateSale05M061(String  stringH_MomentaryMoney,  String  stringL_MomentaryMoney,  String  stringProjectID1,
                                      String  stringHouseCar,                   String  stringPosition,                     String  stringORDER_NO,
                                    Vector  vectorSale05M061Sql) throws  Throwable {
        String  stringSql  =  " UPDATE  Sale05M061  SET  H_MomentaryMoney  =  H_MomentaryMoney  + "  + stringH_MomentaryMoney  +  ", "  +
                                                " L_MomentaryMoney  =  L_MomentaryMoney  +  " + stringL_MomentaryMoney + "," +  
                                                              " HL_MomentaryMoney = H_MomentaryMoney + L_MomentaryMoney " +
                          " WHERE  ProjectID1  =  '"   +  stringProjectID1  +  "' "  +
                            " AND  HouseCar  =  '"   +  stringHouseCar   +  "' "  +
                            " AND  Position  =  '"      +  stringPosition      +  "' "  +
                            " AND  ORDER_NO  =  "  +  stringORDER_NO ;
        //System.out.println("doUpdateSale05M061(2)-----------------------------------"+stringSql) ;
        vectorSale05M061Sql.add(stringSql) ;
        return  vectorSale05M061Sql ;
    }
    // ���v�ɰO��
    // Primary Key �� DocNo�BFlowStatus�BEDateTime�C
    public  void  doInsert_Sale05M080_Flow_HIS( ) throws  Throwable {
        String  stringSql  =  "" ;
        //
        stringSql  = " INSERT  INTO  Sale05M080_Flow_HIS (DocNo,  FlowStatus,  EmployeeNo,  EDateTime,  Opinion) "  +
                                                                 " VALUES  ( N'"  +  getValue("DocNo").trim( )                         +  "', "  +
                                                       " N'"  +  getValue("FlowStatus").trim( )                 +  "', "  +                  
                                                     " N'"  +  getUser()                                                    +  "', "  +
                                                     " N'"  +  datetime.getTime("YYYY/mm/dd h:m:s") + "', "   +
                                                     " N'"  +  getValue("Opinion").trim( )                       + "' ) " ;
        //System.out.println("doInsert_Sale05M080_Flow_HIS-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    // ���v�ɰO��
    // Primary Key �� DocNo�BFlowStatus�BEDateTime�C
    public  void  doInsert_Sale05M080_Flow( ) throws  Throwable {
        String      stringSql  =  "" ;
        String[][]  retSale05M080_Flow  =  null ;
        //
        stringSql   = " SELECT  Opinion "  + 
                    " FROM  Sale05M080_Flow "  +
                  " WHERE  DocNo  =  '"  +  getValue("DocNo").trim()  + "' "  +
                      " AND  FlowStatus  =  '"  +  getValue("FlowStatus").trim( )  +  "' " ;
        retSale05M080_Flow  =  dbSale.queryFromPool(stringSql);
        if(retSale05M080_Flow.length  ==  0) {
            stringSql   = " INSERT  INTO  Sale05M080_Flow  (DocNo,  FlowStatus,  EmployeeNo,  EDateTime,  Opinion)  " +
                                                              " VALUES  ( N'"  +  getValue("DocNo").trim( )                          +  "', "  +
                                                              " N'"  +  getValue("FlowStatus").trim( )                  +  "', "  +                  
                                                    " N'"  +  getUser( )                                                    +  "', "  +
                                                    " N'"  +  datetime.getTime("YYYY/mm/dd h:m:s")  +  "', "  +
                                                    " N'"  +  getValue("Opinion").trim( )                        +  "') " ;
            dbSale.execFromPool(stringSql);                
        }
        else{
            stringSql   = " UPDATE  Sale05M080_Flow  SET  EmployeeNo  =  N'"  +  getUser( )  +  "', "  +
                                                                         " EDateTime  =  N'"     +  datetime.getTime("YYYY/mm/dd h:m:s")  +  "', "  +
                                                   " Opinion  =  N'"          +  getValue("Opinion").trim() + "' " +
                       " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' "  +
                           " AND  FlowStatus  =  '"  +  getValue("FlowStatus").trim( )  +  "' " ;
            dbSale.execFromPool(stringSql);                
        }
        //System.out.println("doInsert_Sale05M080_Flow-----------------------------------"+stringSql) ;
    }
    // ��s���A  
    // Primary Key�GDocNo
    public  void  doUpdate_Sale05M080( ) throws  Throwable {
        String  stringSql = " UPDATE  Sale05M080  SET  FlowStatus  =  N'"      +  getValue("FlowStatus")       +  "', "  +
                                                                                    " SendMessage  =  '"  +  getValue("SendMessage")  +  "' "  +
                             " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' " ;
        //System.out.println("doUpdate_Sale05M080( )-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql) ;
    }
    //  0  CompanyNo      1  DepartNo     2  ProjectID1       3  EDate
    public  String[][]  getSale05M080Tmp( ) throws  Throwable {
        String      stringSql            =  "" ;
        String[][]  retSale05M080  =  null ;
        //
//        stringSql   = " SELECT  CompanyNo,  DepartNo,  ProjectID1,  EDate "  +
          stringSql   = " SELECT  CompanyNo,  DepartNo,  ProjectID1,  PaperEDate "  +   //20201211 kyle : �D�n�ϥεo����� 
                              " FROM  Sale05M080 "  +
                  " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' " ;
        retSale05M080  =  dbSale.queryFromPool(stringSql) ;
        return  retSale05M080 ;
    }
    // ���o�S�w�Ȥ�B�S�w�ʫ��ҩ��椧���ڳ���� 
    // DocNo�BHouseCar�B[Position]�BORDER_NO
    public  String[][]  getSale05M081(String  stringDocNo,  String  stringOrderNo,  String  stringCustomNo) throws  Throwable {
        //
        String      stringSql            =  "" ;
        String[][]  retSale05M081  =  null ;
        //   0  HouseCar                     1  Position                  2  ORDER_NO               3  ITEMLS_CD
        //   4  H_MomentaryMoney     5  H_UsableMoney     6  H_ReceiveMoney       7  (H_ReceiveMoney + H_UsableMoney) AS H_InvoiceMoney
        //   8  L_MomentaryMoney     9  L_UsableMoney    10  L_ReceiveMoney     11  (L_ReceiveMoney + L_UsableMoney) AS L_InvoiceMoney
          // 12 H_DiscountMoney        13  H_FeeMoney        14  L_FeeMoney 
        stringSql  =  "SELECT  T81.HouseCar,              T81.Position,         ORDER_NO,              ITEMLS_CD, " + 
                          " H_MomentaryMoney,  H_UsableMoney,  H_ReceiveMoney,  (H_ReceiveMoney + H_UsableMoney) AS H_InvoiceMoney, "  + 
                          " L_MomentaryMoney,   L_UsableMoney,  L_ReceiveMoney,   (L_ReceiveMoney + L_UsableMoney) AS L_InvoiceMoney, "  +
                          " H_DiscountMoney,      H_FeeMoney,       L_FeeMoney "  +
                       " FROM  Sale05M086 T86,  Sale05M092 T92,  Sale05M081 T81,  Sale05M091 T91 "  +
                     " WHERE  T86.DocNo  =  '"  +  stringDocNo  +  "' "  +
                       " AND  T92.OrderNo  =  '"  +  stringOrderNo  +  "' "  +
                     " AND  T92.OrderNo  =  T86.OrderNo " +
                     " AND  T81.DocNo  =  T86.DocNo "  + 
                     " AND  T81.Position  =  T92.Position "  +
                     " AND  ISNULL(T92.StatusCd,  '')  <>  'D' "  +
                     " AND  T92.OrderNo  =  T91.OrderNo "  +
                     " AND  T91.customNo  =  '"  +  stringCustomNo  +  "' " +
                      " AND  ISNULL(T91.StatusCd,  '')  <>  'C' "  +
                " ORDER BY  T92.OrderNo,  T92.Position,  T92.HouseCar DESC, T81.ORDER_NO " ;
        retSale05M081  =  dbSale.queryFromPool(stringSql);
        return  retSale05M081 ;
    }
    // ���o�S�w�Ȥᤧ���ڳ���� 
    // DocNo�BHouseCar�B[Position]�BORDER_NO
    public  String[][]  getSale05M081ForCustomNo(String  stringDocNo,  String  stringCustomNo) throws  Throwable {
        //
        String      stringSql            =  "" ;
        String[][]  retSale05M081  =  null ;
        // 1  HouseCar                     2  Position                  3  ORDER_NO               4  ITEMLS_CD
        // 5  H_MomentaryMoney     6  H_UsableMoney     7  H_ReceiveMoney       8  (H_ReceiveMoney + H_UsableMoney) AS H_InvoiceMoney
        // 9  L_MomentaryMoney   10  L_UsableMoney    11  L_ReceiveMoney     12  (L_ReceiveMoney + L_UsableMoney) AS L_InvoiceMoney "  +
        stringSql  =  "SELECT  DISTINCT  T81.Position "  +
                       " FROM  Sale05M086 T86,  Sale05M092 T92,  Sale05M081 T81,  Sale05M091 T91 "  +
                     " WHERE  T86.DocNo  =  '"  +  stringDocNo  +  "' "  +
                     " AND  T92.OrderNo  =  T86.OrderNo " +
                     " AND  T81.DocNo  =  T86.DocNo "  + 
                     " AND  T81.Position  =  T92.Position "  +
                     " AND  ISNULL(T92.StatusCd,  '')  <>  'D' "  +
                     " AND  T92.OrderNo  =  T91.OrderNo "  +
                     " AND  T91.customNo  =  '"  +  stringCustomNo  +  "' " +
                      " AND  ISNULL(T91.StatusCd,  '')  <>  'C' "  +
                " ORDER BY  T81.Position " ;
        retSale05M081  =  dbSale.queryFromPool(stringSql);
        return  retSale05M081 ;
    }
    public  String[][]  getSale05M084( ) throws  Throwable {
        // 
        String      stringSql            =  "" ;
        String[][]  retSale05M084  =  null ;
        //  0  CustomNo
        stringSql  =  " SELECT  DISTINCT  CustomNo,  Nationality " + 
                    " FROM  Sale05M084 " + 
                  " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' ";
        retSale05M084  =  dbSale.queryFromPool(stringSql);
        return  retSale05M084 ;
    }
    // 0  CustomNo    1  Percentage
    public  String  getSale05M091ForPercentage(String  stringOrderNo,  String  stringCustomNo) throws  Throwable {
        String      stringSql                =  "" ;
        String      stringPercentage  =  "0" ;
        String[][]  retSale05M091     =  null ;
        //
          stringSql  =  "SELECT  CustomNo,  Percentage " +
                         " FROM  Sale05M091 T91 " +
                    " WHERE  OrderNo  =  '"  +  stringOrderNo  +  "' "  +
                     " AND  CustomNo  =  '"  +  stringCustomNo  +  "' "  +
                     " AND  ISNULL(T91.StatusCd,  '')  <>  'C' " ;    
        retSale05M091  =  dbSale.queryFromPool(stringSql) ;
        if(retSale05M091.length  !=  0) {
            stringPercentage  =  retSale05M091[0][1].trim( ) ;
        }
        return  stringPercentage ;
    }
    public  String  getSale05M091ForNationality(String  stringOrderNo,  String  stringCustomNo) throws  Throwable {
        String      stringSql                =  "" ;
        String      stringNationality    =  "0" ;
        String[][]  retSale05M091     =  null ;
        //
          stringSql  =  "SELECT  Nationality " +
                         " FROM  Sale05M091 T91 " +
                    " WHERE  OrderNo  =  '"  +  stringOrderNo  +  "' "  +
                     " AND  CustomNo  =  '"  +  stringCustomNo  +  "' "  +
                     " AND  ISNULL(T91.StatusCd,  '')  <>  'C' " ;    
        retSale05M091  =  dbSale.queryFromPool(stringSql) ;
        if(retSale05M091.length  !=  0) {
            stringNationality  =  retSale05M091[0][0].trim( ) ;
        }
        return  stringNationality ;
    }
    // Primary Key�GDocNo�BCompanyNo�BInvoiceYYYYMM�BFSChar�BStartNo�BInvoiceBook
    public  void  doDeleteSale05M085( ) throws  Throwable {
        String  stringSql  =  " DELETE  FROM  Sale05M085 " + 
                             " WHERE  DocNo  =  '"  + getValue("DocNo").trim( )  +  "' " ;
        //System.out.println("doDeleteSale05M085-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    // Primary Key�GDocNo�BCompanyNo�BInvoiceYYYYMM�BFSChar�BStartNo�BInvoiceBook
    // retSale05M080
    // 0  CompanyNo     1  DepartNo     2  ProjectID1       3  EDate
    // retInvoM022
    // 0  InvoiceYYYYMM   1  FSChar                2  StartNo             3  InvoiceBook      4  InvoiceStartNo
    // 5  InvoiceEndNo           6  MaxInvoiceNo    7  MaxInvoiceDate
    public  void  doInsertSale05M085(String  stringCompanyCd,  String[]  retInvoM022,String stringInvoiceKind) throws  Throwable {
        String  stringSql  =  " INSERT  INTO Sale05M085 (DocNo,            CompanyNo,        InvoiceYYYYMM,  FSChar,              StartNo, "  +
                                                 " InvoiceBook,  InvoiceStartNo,  InvoiceEndNo,  InvoiceKind,     MaxInvoiceNo,  MaxInvoiceDate, "  +
                                                 " EndYES ) " +
                                                  " VALUES (N'"  +  getValue("DocNo").trim( )  +  "', "  +
                                                          " N'"  +  stringCompanyCd                +  "', "  +
                                              " N'"  +  retInvoM022[0].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[1].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[2].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[3].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[4].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[5].trim( )          +  "', "  +     
                                              " N'"  +  stringInvoiceKind                +  "', "  +
                                              " N'"  +  retInvoM022[6].trim( )          +  "', "  +
                                              " N'"  +  retInvoM022[7].trim( )          +  "', "  +
                                              " N'N')" ;
        //System.out.println("doInsertSale05M085-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    // �o�����X�۰ʲ���
    // retSale05M080
    // 0  CompanyNo     1  DepartNo     2  ProjectID1       3  EDate
    public  String[][]  getSale05M085(String[][]  retSale05M080,String  stringCompanyCd,String stringInvoiceKind) throws  Throwable {
        String      stringSql            =  "" ;
        String      stringEDate       =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][3].trim( ) :  "" ;
        String[][]  retSale05M085  =  null ;
        //   0  InvoiceYYYYMM     1  FSChar     2  StartNo    3  InvoiceBook    4  InvoiceStartNo
        //   5  InvoiceEndNo      6  MaxInvoiceNo
        stringSql = " SELECT  TOP 1  InvoiceYYYYMM,  FSChar,  StartNo,  InvoiceBook,  InvoiceStartNo, "  + 
                               " InvoiceEndNo,  MaxInvoiceNo "  +
                  " FROM  Sale05M085 " + 
                  " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' "  +
                      " AND  (MaxInvoiceDate <= '" + stringEDate + "'  OR  MaxInvoiceDate IS NULL) " +
                    " AND  EndYES  =  'N'  AND  CompanyNo  =  '"  +  stringCompanyCd  +  "'  and  InvoiceKind = '" + stringInvoiceKind + "' " +
              " ORDER BY  InvoiceStartNo " ;
        retSale05M085  =  dbSale.queryFromPool(stringSql); 
        return  retSale05M085 ;
    }
    public  String[][]  getSale05M085(String  stringCompanyCd) throws  Throwable {
        String      stringSql                          =  "" ;
        String[][]  retSale05M085UPDATE  =  null ;
        //
        stringSql  =  " SELECT  InvoiceYYYYMM,  FSChar,  StartNo,  InvoiceBook,  MaxInvoiceNo, "  +
                           " MaxInvoiceDate,   EndYES " + 
                      " FROM  Sale05M085 " + 
                    " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' "  +  
                        " AND  CompanyNo  =  '"  +  stringCompanyCd  +  "' " ;
          retSale05M085UPDATE  =  dbSale.queryFromPool(stringSql) ;
          return  retSale05M085UPDATE ;
    }
    public void  doUpdateSale05M085(String  stringInvoiceNo,            String  stringEDate,                  String  stringEndYes,
                                  String  stringCompanyCd,        String  stringInvoiceYYYYMM,  String  stringFSChar,  
                                  String  stringStartNo,            String  stringInvoiceBook) throws  Throwable {
        String  stringSql  =  " UPDATE  Sale05M085  SET  MaxInvoiceNo  =  '"     +  stringInvoiceNo  +  "', " +
                                                       " MaxInvoiceDate  =  '"  +  stringEDate        +  "', " +
                                                       " EndYES  =  '"                 +  stringEndYes     +  "' "  +                     
                         " WHERE  DocNo  =  '"                    +  getValue("DocNo").trim( )  +  "' "  +
                               " AND  CompanyNo  =  '"          +  stringCompanyCd                +  "' "  +
                             " AND  InvoiceYYYYMM  =  '"  +  stringInvoiceYYYYMM         +  "' "  +
                             " AND  FSChar  =  '"                   +  stringFSChar                      +  "' "  +
                             " AND  StartNo  =  '"                   +  stringStartNo                      +  "' "  +                  
                             " AND  InvoiceBook  =  '"          +  stringInvoiceBook               +  "' " ;
        //System.out.println("doUpdateSale05M085-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql) ;
    }
    // ���o�S�w�Ȥ᪺�ʫ��ҩ���
    public  String[][]  getSale05M086(String  stringDocNo,  String  stringCustomNo) throws  Throwable {
        String      stringSql            =  "" ;
        String[][]  retSale05M086  =  null ;
        //
        stringSql  =  " SELECT  DISTINCT  T86.OrderNo " + 
                    " FROM  Sale05M086 T86,  Sale05M092 T92,  Sale05M081 T81,  Sale05M091 T91 "  +
                  " WHERE  T86.DocNo  =  '"  +  stringDocNo  +  "' "  +
                        " AND  T81.DocNo  =  T86.DocNo " + 
                      " AND  T92.OrderNo  =  T86.OrderNo " +
                      " AND  T81.Position  =  T92.Position " + 
                      " AND  ISNULL(T92.StatusCd,  '')  <>  'D' "  +
                      " AND  T92.OrderNo  =  T91.OrderNo "  +
                      " AND  T91.customNo  =  '"  +  stringCustomNo  +  "' " +
                      " AND  ISNULL(T91.StatusCd,  '')  <>  'C' " ;
        //System.out.println("getSale05M086-----------------------------------"+stringSql) ;
        retSale05M086  =  dbSale.queryFromPool(stringSql) ;
        return  retSale05M086 ;
    }
    // ���o�P�@�Ӳ��~�b�P�@���ʫ��ҩ��椺�A���X���U�ȨæP����
    public  int  getSale05M086Count(String  stringDocNo,  String  stringCustomNo,  String  stringPosition) throws  Throwable {
        String      stringSql            =  "" ;
        String[][]  retSale05M086  =  null ;
        //
        stringSql  =  " SELECT  DISTINCT  T91.CustomNo " + 
                    " FROM  Sale05M086 T86,  Sale05M092 T92,  Sale05M081 T81,  Sale05M091 T91 "  +
                  " WHERE  T86.DocNo  =  '"  +  stringDocNo  +  "' "  +
                        " AND  T81.DocNo  =  T86.DocNo " + 
                      " AND  T92.OrderNo  =  T86.OrderNo " +
                      " AND  T92.OrderNo  =  T91.OrderNo "  +
                      " AND  T81.Position  =  T92.Position " + 
                      " AND  T81.Position  =  '"  +  stringPosition  +  "' "  +
                      " AND  ISNULL(T92.StatusCd,  '')  <>  'D' "  +
                      " AND  ISNULL(T91.StatusCd,  '')  <>  'C' " ;
        //System.out.println("getSale05M086Count-----------------------------------"+stringSql) ;
        retSale05M086  =  dbSale.queryFromPool(stringSql) ;
        return  retSale05M086.length ;
    }
    // �ˮ֬O�_�s�b
    // DocNo�BRecordNo
    public  String  isCheckSale05M087Exist( ) throws  Throwable {
        String      stringSql              =  "" ;
        String      stringInvoiceNo1  =  "" ;
        String[][]  retSale05M087    =  null ;
        //
        stringSql  =  " SELECT  TOP 1  InvoiceNo "  + 
                    " FROM  Sale05M087 "  +
                  " WHERE  DocNo  =  '"  +  getValue("DocNo").trim( )  +  "' " ;
        retSale05M087  =  dbSale.queryFromPool(stringSql);
        if(retSale05M087.length  !=  0) {
            stringInvoiceNo1  =  retSale05M087[0][0].trim( ) ;
        }
        return  stringInvoiceNo1 ;
    }
    public  void  doInsertSale05M087(String  stringDocNo,  int  intRecordNo,  String  stringInvoiceNo) throws  Throwable {
        doInsertSale05M087(stringDocNo,  intRecordNo,  stringInvoiceNo,  "N") ;
    }
    public  void  doInsertSale05M087(String  stringDocNo,  int  intRecordNo,  String  stringInvoiceNo,  String  stringFeeYes) throws  Throwable {
        String  stringSql  =  "INSERT  INTO  Sale05M087 (DocNo,  RecordNo,  InvoiceNo,  FeeYes,  DELYes) " +
                                             " VALUES ( '"  +  stringDocNo       +  "', "  +
                                                               intRecordNo       +  ", "   +
                                                        " '"  +  stringInvoiceNo  +  "', "  +
                                                " '"  +  stringFeeYes      +  "', "  +
                                                " N'N') " ;

        //System.out.println("doInsertSale05M087-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    //
    public  String[][]  getSale05M089(String  stringDocNo) throws  Throwable {
        String[][]  retSale05M089  =  null ;
        String      stringSql            =  "" ;
        //  0  DiscountNo       1  InvoiceNo
        stringSql  =  "SELECT   DiscountNo,  InvoiceNo " +
                             " FROM  Sale05M089 "  +
                   " WHERE  DocNo  =  '"  +  stringDocNo  +  "' " ;
        //System.out.println("getSale05M089-----------------------------------"+stringSql) ;
        retSale05M089  =  dbSale.queryFromPool(stringSql);
        return  retSale05M089 ;
    }
    public  void  doInvertSale05M089(String  stringDocNo,  int  intRecordNo,  String  stringDiscountNo,  String  stringInvoiceNo) throws  Throwable {
        String  stringSql  =  "INSERT  INTO  Sale05M089 (DocNo,  RecordNo,  DiscountNo,  InvoiceNo) " +
                                             " VALUES ( '"  +  stringDocNo       +  "', "  +
                                                               intRecordNo       +  ", "   +
                                                        " '"  +  stringDiscountNo  +  "', "  +
                                                " '"  +  stringInvoiceNo  +  "') " ;
        //System.out.println("doInvertSale05M089-----------------------------------"+stringSql) ;
        dbSale.execFromPool(stringSql);
    }
    //
    public  String[][]  getInvoM010(String  stringPointNo) throws  Throwable {
        String      stringSql  =  "" ;
        String[][]  retInvoM010  =  null ;
        //  0  TaxRate   1  TaxKind
        stringSql   = "SELECT  TaxRate,  TaxKind "  +
                   " FROM  InvoM010 " +
                   " WHERE  PointNo  =  '"  +  stringPointNo  +  "' " ;
        retInvoM010  =  dbInvoice.queryFromPool(stringSql);
        return  retInvoM010 ;
    }
    // Primary Key�GCompanyNo�BInvoiceYYYYMM�BFSChar�BStartNo�BInvoiceBook
    public  String[][]  getInvoM022(String[][]  retSale05M080,  String  stringInvoiceKind,  String  stringCompanyCd) throws  Throwable {
        // 
        String      stringSql                =  "" ;
        String      stringCompanyNo  =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][0].trim( ) :  "" ;
        String      stringDepartNo      =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][1].trim( ) :  "" ;
        String      stringProjectID1     =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][2].trim( ) :  "" ;
        String      stringEDate           =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][3].trim( ) :  "" ;
        String[][]  retInvoM022  =  null ;
        //  0  InvoiceYYYYMM    1  FSChar                2  StartNo             3  InvoiceBook      4  InvoiceStartNo
        //  5  InvoiceEndNo           6  MaxInvoiceNo     7  MaxInvoiceDate
        stringSql = " SELECT  TOP 2  InvoiceYYYYMM,  FSChar,              StartNo,                InvoiceBook,  InvoiceStartNo, " + 
                                   " InvoiceEndNo,       MaxInvoiceNo,  MaxInvoiceDate " + 
                      " FROM  InvoM022 " + 
                    " WHERE  CompanyNo  =  '"     +   stringCompanyCd  +  "' "  +
                          " AND  DepartNo  =  '"         +  stringDepartNo  +  "' "  +
                        " AND  ProjectNo  =  '"        +  stringProjectID1  +  "' "  +
                        " AND  InvoiceKind  =  '"     +  stringInvoiceKind  +  "' "  +
                        " AND  UseYYYYMM  =  '"  +  stringEDate.substring(0,7)  +  "' "  +
                        " AND  (MaxInvoiceDate <= '"  +  stringEDate  +  "'  OR  MaxInvoiceDate IS NULL)" +
                        " AND  ENDYES  =  'N' " +
                        " AND  CloseYes  =  'N' " +
                        " AND  ProcessInvoiceNo  =  '1' " + 
                   " ORDER BY  InvoiceStartNo " ;
         System.out.println("getRetInvoM022: "+stringSql);
        retInvoM022  =  dbInvoice.queryFromPool(stringSql);
        return  retInvoM022 ;
    }
    // Primary Key�GCompanyNo�BInvoiceYYYYMM�BFSChar�BStartNo�BInvoiceBook
    // retSale05M080
    // 0  CompanyNo     1  DepartNo     2  ProjectID1       3  EDate
    // retInvoM022
    // 0  InvoiceYYYYMM   1  FSChar                2  StartNo             3  InvoiceBook      4  InvoiceStartNo
    // 5  InvoiceEndNo           6  MaxInvoiceNo    7  MaxInvoiceDate
    public  void  doUpdateInvoM022(String  stringCompanyCd,  String[]  retInvoM022) throws  Throwable {
        //
        String  stringSql                = "" ;
        //
        stringSql   = " UPDATE  InvoM022  SET  CloseYes  =  N'Y' " + 
                   " WHERE  CompanyNo  =  '"           +  stringCompanyCd  +  "' "  +
                         " AND  InvoiceYYYYMM  =  '"  +  retInvoM022[0].trim( )  +  "' "  +
                       " AND  FSChar  =  '"                   +  retInvoM022[1].trim( )  +  "' "  +
                       " AND  StartNo  =  '"                   +  retInvoM022[2].trim( )  +  "' "  +                   
                       " AND  InvoiceBook  =  '"          +  retInvoM022[3].trim( )  +  "' " ;
        //System.out.println("doUpdateInvoM022-----------------------------------"+stringSql) ;
        dbInvoice.execFromPool(stringSql) ;
    }
    public  void  doUpdateInvoM022(String[]  retSale05M085UPDATE,  String  stringCompanyCd) throws  Throwable {
        String  stringSql = " UPDATE  InvoM022  SET  MaxInvoiceNo  =  '"     +  retSale05M085UPDATE[4].trim( )  +  "', "  +
                                                         " MaxInvoiceDate  =  '"  +  retSale05M085UPDATE[5].trim( )  +  "', "  +
                                                         " EndYES  =  '"                 +  retSale05M085UPDATE[6].trim( )  +  "', "  +
                                                         " CloseYes  =  'N' "  +                    
                        " WHERE  CompanyNo  =  '"           +  stringCompanyCd                          +  "' "  +
                            " AND  InvoiceYYYYMM  =  '"  +  retSale05M085UPDATE[0].trim( )  +  "' "  +
                            "  AND  FSChar  =  '"                   +  retSale05M085UPDATE[1].trim( )  +  "' "  +
                          " AND  StartNo  =  '"                   +  retSale05M085UPDATE[2].trim( )  +  "' "  +                  
                            " AND  InvoiceBook  =  '"          +  retSale05M085UPDATE[3].trim( )  +  "' " ;
        //System.out.println("doUpdateInvoM022(2)-----------------------------------"+stringSql) ;
        dbInvoice.execFromPool(stringSql) ;
    }
    public  void  doUpdateInvoM022Undo(String[]  retSale05M085UPDATE,  String  stringCompanyCd) throws  Throwable {
        String  stringSql = " UPDATE  InvoM022  SET  CloseYes  =  'N' "  +
                        " WHERE  CompanyNo  =  '"           +  stringCompanyCd                          +  "' "  +
                            " AND  InvoiceYYYYMM  =  '"  +  retSale05M085UPDATE[0].trim( )  +  "' "  +
                            "  AND  FSChar  =  '"                   +  retSale05M085UPDATE[1].trim( )  +  "' "  +
                          " AND  StartNo  =  '"                   +  retSale05M085UPDATE[2].trim( )  +  "' "  +                  
                            " AND  InvoiceBook  =  '"          +  retSale05M085UPDATE[3].trim( )  +  "' " ;
        //System.out.println("doUpdateInvoM022(2)-----------------------------------"+stringSql) ;
        dbInvoice.execFromPool(stringSql) ;
    }
    ////�o��.InvoM030(Head)
    // retSale05M080
    public  String  getInvoiceTotalMoney(String  stringInvoiceNo) throws  Throwable {
        String       stringSql                           =  "" ;
        String       stringInvoiceTotalMoney  =  "" ;
        String[][]   retInvoM030                     =  null ;
        //
        stringSql        =  "SELECT  InvoiceTotalMoney  FROM  InvoM030  WHERE  InvoiceNo  =  '"  +  stringInvoiceNo  +  "' " ;
        retInvoM030  =  dbInvoice.queryFromPool(stringSql);
        if(retInvoM030.length  >  0)  stringInvoiceTotalMoney  =  retInvoM030[0][0].trim() ;
        return  stringInvoiceTotalMoney ;
    }
    
    //�g�J�o���D��
    // 0  CompanyNo     1  DepartNo     2  ProjectID1       3  EDate
    public  void  doInvertInvoM030(String     stringInvoiceNo,      String  stringInvoiceKind,             String   stringPosition,  
                                                       String      stringCustomNo,     String  stringPointNo,                   String  stringInvoiceMoney,
                               String      stringInvoiceTax,    String  stringInvoiceTotalMoney,  String   stringTaxKind,
                               String      stringCompanyCd,  String[][]  retSale05M080,            String  stringL_DiscountMoney,
                               int            intDiscountCountInvoice) throws  Throwable {
        //String  stringCompanyNo  =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][0].trim( ) :  "" ;
        String  stringDepartNo      =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][1].trim( ) :  "" ;
        String  stringProjectID1     =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][2].trim( ) :  "" ;
        String  stringEDate    =  retSale05M080.length!=0 ?  retSale05M080[retSale05M080.length-1][3].trim( ) :  "" ;      //�o����
        String  strInvoiceTime = datetime.getTime("h:m:s") ;                                                               //�o���ɶ�
        String  stringDateTime = datetime.getTime("YYYY/mm/dd h:m:s") ;                                                    //�o���}�߮� �t�ήɶ�
        String  stringUserID           =  getUser() ;
        String  customName = mapCustomers.get(stringCustomNo) !=null? mapCustomers.get(stringCustomNo).toString():"";
        Random r1 = new Random();
        
        //�̷�2020/12/11�q�l�o���|ĳ����:
        //1.�Y�o����D�}�߷��A�h�}�߮ɶ��[�@�p��
        //2.�Y�[�@�p�ɫ�j�󵥩�24�I�A�h�H23�I�p
        if( !stringDateTime.split(" ")[0].trim().equals(stringEDate) ) {
          String[] arrTmpTime = strInvoiceTime.split(":");
          int tmpTimeH = (Integer.parseInt(arrTmpTime[0].trim()) + 1) >=24 ? 23:(Integer.parseInt(arrTmpTime[0].trim()) + 1);
          //tip : �Ʀr�p���A�p��10��0�|�����A�n�A�ɦ^��
          strInvoiceTime = kUtil.add0(tmpTimeH, 2, "F") + ":" + arrTmpTime[1].trim() + ":" + arrTmpTime[2].trim(); 
        }
        
        String  stringSql       =  " INSERT  INTO  InvoM030 (InvoiceNo,                InvoiceDate, InvoiceTime,  InvoiceKind,              CompanyNo,  DepartNo, "  +
                                                            " ProjectNo,                InvoiceWay,   Hubei,                         CustomNo,     PointNo, "  +
                                                            " InvoiceMoney,         InvoiceTax,    InvoiceTotalMoney,  TaxKind,         DisCountMoney,  "  +
                                                            " DisCountTimes,        PrintYes,       PrintTimes,                 DELYes,          LuChangYes ," +
                                                            " ProcessInvoiceNo,  Transfer,       CreateUserNo,           CreateDateTime,  LastUserNo, "  +
                                                            " LastDateTime, RandomCode, CustomName) " +
                                " VALUES ( '"  +  stringInvoiceNo                +  "', "  +
                                       " '"  +  stringEDate                     +  "', "  +
                                       " '"  +  strInvoiceTime                    +  "', "  +
                                       " '"  +  stringInvoiceKind             +  "', "  +
                                       " '"  +  stringCompanyCd            +  "', "  +
                                       " '"  +  stringDepartNo                +  "', "  +  // 04
                                       " '"  +  stringProjectID1               +  "', "  +
                                       " 'A', " +
                                       " '"  +  stringPosition                    +  "', "  +
                                       " '"  +  stringCustomNo                +  "', "  +
                                       " '"  +  stringPointNo                    +  "', "  +  // 09
                                            stringInvoiceMoney           +  ", "  +
                                            stringInvoiceTax                +  ", "  +
                                            stringInvoiceTotalMoney   +  ", "  +
                                       " '"  +  stringTaxKind                    +  "', "  +
                                                   stringL_DiscountMoney    +  ", " + // 14
                                                   intDiscountCountInvoice   +  ", " +
                                      " 'N', " +
                                      " 0, " +
                                      " 'N', " +
                                      " 'N', " +
                                      " '1', " +
                                      " '����', " +
                                      " '"  +  stringUserID                    +  "', "  +
                                      " '"  +  stringDateTime               +  "', "  +
                                      " '"  +  stringUserID                 +  "', "  +
                                      " '"  +  stringDateTime               +  "', "  + 
                                      " '"  +  kUtil.add0(r1.nextInt(9999), 4, "F")  +  "', "  +
                                      " '"  +  customName                   +  "' "  +
                                      "  ) "  ;
        //System.out.println("InvoM030-------------"  +  stringSql) ;
        dbInvoice.execFromPool(stringSql);

        //------------------2020/11/06 Kyle Add support �q�l�o��
        //�g�JAS400
        //A : �D��
        StringBuilder sbSQL = new StringBuilder();
        sbSQL.append("insert into "+GENLIB+".GLEAPFUF ");
        sbSQL.append("(EA01U, EA02U, EA03U, EA04U, EA05U, EA06U, EA07U, EA08U, EA09U, EA10U, EA11U, EA12U, EA13U, EA14U, EA15U, EA16U, EA17U, EA18U, EA19U, EA20U, EA21U, EA22U) ");
        sbSQL.append("values ");
        sbSQL.append("(");
        sbSQL.append("'").append(stringInvoiceNo).append("', ");         //�o�����X
        sbSQL.append("'").append( stringEDate ).append("', ");           //�o�����
        sbSQL.append("'").append( stringInvoiceKind ).append("', ");     //�o���p��
        sbSQL.append("'").append( stringCompanyCd ).append("', ");       //���q�N�X
        sbSQL.append("'").append( stringDepartNo ).append("', ");        //�����N�X
        sbSQL.append("'").append( stringProjectID1 ).append("', ");      //�קO�N�X
        sbSQL.append("'").append( "A" ).append("', ");                   //Invoice Way
        sbSQL.append("'").append( stringPosition ).append("', ");        //��O�N��
        sbSQL.append("'").append( stringCustomNo ).append("', ");        //�Ȥ�N��
        sbSQL.append("'").append( stringPointNo ).append("', ");         //�K�n
        sbSQL.append("").append( stringInvoiceMoney ).append(", ");      //���|
        sbSQL.append("").append( stringInvoiceTax ).append(", ");        //�|�B
        sbSQL.append("").append( stringInvoiceTotalMoney ).append(", "); //�t�|
        sbSQL.append("'").append( stringTaxKind ).append("', ");         //�|�O
        sbSQL.append("").append( stringL_DiscountMoney ).append(", ");             //�w�������B
        sbSQL.append("").append( intDiscountCountInvoice ).append(", ");             //�w��������
        sbSQL.append("'").append("N").append("', ");         //�w�C�LYN
        sbSQL.append("").append(0).append(", ");             //�ɦL����
        sbSQL.append("'").append("N").append("', ");         //�@�oYN
        sbSQL.append("'").append("N").append("', ");         //�J�bYN
        sbSQL.append("'").append("").append("', ");          //�o���B�z�覡
        sbSQL.append("'").append("����").append("' ");       //����/�ȪA
        sbSQL.append(") ");
        dbAs400.execFromPool(sbSQL.toString());
        //------------------2020/11/06 Kyle Add support �q�l�o��
    }
    
    //�g�J�o���Ӷ�(Body)
    public  void  doInvertInvoM031(String  stringInvoiceNo,  int  intRecord,  String  stringITEMLS_CHINESE,  String  stringORDER_NO) throws  Throwable {
        intRecord  =  (intRecord  %  5  ==  0)  ?  5  :  (intRecord  %  5) ;
        //
        String  stringTemp  =  code.StrToByte(stringITEMLS_CHINESE) ;
        if(stringTemp.length()  >  24) {
            stringITEMLS_CHINESE  =  code.ByteToStr(stringTemp.substring(0,24)) ;
        }
        //
        String  stringSql  = " INSERT  INTO  InvoM031 (InvoiceNo,  RecordNo,  DetailItem,  Remark) " +
                                              " VALUES ( '"  +  stringInvoiceNo  +  "',  "  +
                                                                  intRecord           +  ", "  +    
                                                     " '"  +  stringITEMLS_CHINESE  +  "', "  + 
                                               //" '"  +  stringITEMLS_CHINESE.substring(0,5)  +  "', "  +  
                                                     " '"  +  stringORDER_NO  +  "') " ;
        //System.out.println("InvoM031-------------"  +  stringSql) ;
        dbInvoice.execFromPool(stringSql);
    }
    
    /**
     * �g�J�Ȥ�A�@���@��
     * 
     * @param stringCustomNo 
     * @param stringORDER_NO
     * @throws Throwable
     */
    public  void  doInvertInvoM0C0(String  stringCustomNo,  String  stringORDER_NO) throws  Throwable {
      System.out.println("�g�J�Ȥ�>>>" + stringCustomNo);
      String  stringSql =  "" ;
      String  stringCustomName = mapCustomers.get(stringCustomNo) !=null? mapCustomers.get(stringCustomNo).toString().trim():"";
      String  stringNationality = getSale05M091ForNationality(stringORDER_NO,  stringCustomNo) ;     
      String[][] retInvoM0C0 = null;
      stringSql = " SELECT  CustomName from InvoM0C0 where CustomNo='" + stringCustomNo+ "' " ;
      retInvoM0C0  =  dbInvoice.queryFromPool(stringSql);
      if( retInvoM0C0.length == 0 && !"".equals(stringCustomName) ) {
        stringSql  = " INSERT  INTO  InvoM0C0 (CustomNo,  CustomName,  Transfer,  Nationality) "
                   + " values ('" + stringCustomNo+ "',N'" + stringCustomName+ "','Y',  '"+stringNationality+"')";
        String ret = dbInvoice.execFromPool(stringSql);
      }
      
      //------------------2020/11/06 Kyle Add support �q�l�o��
      //400�@�w�g
      //400 D : �Ȥ���
      StringBuilder sbSQL = new StringBuilder();
      sbSQL.append("select ED01U from "+GENLIB+".GLEDPFUF where ED01U = '" + stringCustomNo + "' ");
      String[][] arrGLEDPFUF = dbAs400.queryFromPool(sbSQL.toString());
      if(arrGLEDPFUF.length == 0 && !"".equals(stringCustomName)) {
	    //400�S��ݨD�A�p�󤻭Ӧr�n�ɺ��줻�ӥ���
		if(stringCustomName.length() < 6) stringCustomName = kUtil.addWhat(stringCustomName, 6, "�@", 1);
		
        sbSQL = new StringBuilder();
        sbSQL.append("insert into "+GENLIB+".GLEDPFUF ");
        sbSQL.append("(ED01U, ED02U) ");
        sbSQL.append("values ");
        sbSQL.append("(");
        sbSQL.append("'").append( stringCustomNo ).append("', ");
        sbSQL.append("'").append( stringCustomName ).append("' ");
        sbSQL.append(") ");
        dbAs400.execFromPool(sbSQL.toString());
      }
      //------------------2020/11/06 Kyle Add support �q�l�o��
    }
    
    //���o�q��Ȥ�(����)
    public Map getSale05M091Users() throws  Throwable {
      Map rsMap = new HashMap();
      StringBuilder sql = new StringBuilder("select customNo , customName from Sale05M091 a ");
      sql.append("where a.orderno in (select b.orderNo from Sale05M086 b where b.DocNo='" + getValue("DocNo").trim() + "')");
      String[][] retSQL = dbSale.queryFromPool(sql.toString());
      if(retSQL.length > 0) {
        for(int i=0 ; i<retSQL.length ; i++) {
          rsMap.put(retSQL[i][0].trim(), retSQL[i][1].trim());
        }
      }
      return rsMap;
    }
    
    public  String  getInformation( ) {
        return "---------------OK(�s��).defaultValue()----------------";
    }
}
