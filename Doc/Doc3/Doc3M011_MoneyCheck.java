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
        String         stringNeedDate                 =  (stringFlow.indexOf("����")!=-1  ||  stringFlow.indexOf("�ɴڥ�")!=-1) ?getValue("NeedDate").trim():getValue("CDate").trim() ;    //oce
        String         stringBarCodeOld                       =  getValue("BarCodeOld").trim() ;
        String         stringNeedDateAC                     =  exeUtil.getDateConvert(stringNeedDate) ;
        String          stringSqlAndAll                         =  "" ;
        String          stringErrMessage                      =  "" ;
        String      stringBudgetType                  =  "" ;
        String      stringBigBudgetName             =  "" ;
        String[]       retCheckMoney                          =  null ;
        String[][]     retCheckTable                          =  null ;
        String[][]     retTableData                             =  new  String[1][1]  ;setTableData("TableCheck",  new  String[0][0]) ;
        String[][]      retFunctionTypeV                      =  getFunctionTypeDoc2M0201(stringNeedDateAC,  "V",  exeUtil,  dbDoc)  ;    // ���@�w�ⱱ�ޤ]����J�w�⤺���дڥN�X
        double        doubleUseMoneyL                   =  0 ;
        double        doubleUseMoneyL_NoControl          =  0 ;
        double        doubleUseMoneyL2                  =  0 ;
        double        doubleUseMoneyL2_NoControl        =  0 ;
        double        doubleUseMoney                        =  0 ;
        double        doubleUseMoney2                   =  0 ;
        double        doubleBudgetMoney                 =  0 ;
        double        doubleUseMoneyThis                =  0 ;
        boolean       booleanFlow                       =  stringFlow.indexOf("����")==-1  ||  (stringFlow.indexOf("����")!=-1  ||  stringFlow.indexOf("��T")!=-1) ;//oce
        boolean       booleanTest                             =  "TEST".equals(""+get("Doc3M011_CHECK")) ;
        boolean     booleanAllNoControl             =  false ;
        Vector          vectorSpecProjectID1                =  getSpecProjectID1( ) ;  // ���ˮ֦~�׹w��
        Vector          vectorFullProjectID1                  =  new  Vector() ;  //
        Hashtable   hashtableBudgetIDs              =  new  Hashtable() ;
        Hashtable   hashtableBigBudget              =  new  Hashtable() ;
        // hashtableBudgetIDs �j�w�������z
        // hashtableBigBudget �w��N�X ���� �j�w�����
        doDoc7M072Data(dbDoc,  hashtableBudgetIDs,  hashtableBigBudget) ;
        // 0 �קO     1 ����        2 ��-�ĵo-�ϥΪ��B       3 ��-�w��-�ϥΪ��B     4 ��-�ĵo-�����ު��B       5 ��-�w��-�����ު��B
        retCheckTable                 =  getDataReorganization(stringFlow,  retFunctionTypeV,  vectorSpecProjectID1,  hashtableBigBudget,  exeUtil,  dbDoc) ;
        for(int  intNo=0  ;  intNo<retCheckTable.length  ;  intNo++) {
            stringProjectID1                        =  retCheckTable[intNo][0].trim() ; 
            stringBudgetType                    =  retCheckTable[intNo][1].trim() ;
            doubleUseMoneyL                     =  exeUtil.doParseDouble(retCheckTable[intNo][2].trim()) ;
            doubleUseMoneyL2                    =  exeUtil.doParseDouble(retCheckTable[intNo][3].trim()) ;
            doubleUseMoneyL_NoControl         =  exeUtil.doParseDouble(retCheckTable[intNo][4].trim()) ;    // �S��дڥN�X���ĵo���B
            doubleUseMoneyL2_NoControl       =  exeUtil.doParseDouble(retCheckTable[intNo][5].trim()) ;     // �S��дڥN�X���w����B
            stringBigBudgetName                 =  stringBudgetType.length()==1  ?  ""  :  exeUtil.getNameUnion("BigBudgetName",  "Doc7M071",  " AND  BigBudget  =  '"+stringBudgetType+"' ",  new  Hashtable(),  dbDoc) ;
            //System.out.println(intNo+"("+retCheckTable.length+")stringBudgetType("+stringBudgetType+")stringBigBudgetName("+stringBigBudgetName+")-----------------------------------------") ;
            // 0  �ĵo���B        1  �ĵo���B            2  �w�����B                   3  �׹w��ϥΤ�      4  �׹w��y�{��
            retCheckMoney           =  getCheckMoneyFront(stringProjectID1,  stringBudgetType,  stringBarCodeOld,  retFunctionTypeV,  booleanFlow,  exeUtil,  dbDoc) ;
            stringSqlAndAll         =  getDoc2M020Sql(stringBudgetType,  dbDoc) ;
            // �צX�p
            doubleUseMoney        =  exeUtil.doParseDouble(retCheckMoney[0].trim())+
                                                   exeUtil.doParseDouble(retCheckMoney[1].trim());            //  0  �ĵo���B
            doubleUseMoney2     =  exeUtil.doParseDouble(retCheckMoney[2].trim()) ;               //  1  �w�����B
            doubleBudgetMoney  =  exeUtil.doParseDouble(retCheckMoney[3].trim()) ;           //   2  �׹w��ϥΤ�
            if(doubleBudgetMoney  <=0  )  doubleBudgetMoney  =  exeUtil.doParseDouble(retCheckMoney[4].trim()) ;  // 3 �׹w��y�{��
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
                    stringBigBudgetName  =  "��"+stringBigBudgetName ;
                }
                stringTemp  =   "�קO�� "  +  stringProjectID1  +  "�����w��"+stringBigBudgetName+" "  +
                             ((doubleUseMoney2==0)?"":" ���ܱ��ʤ��w����B("  +  exeUtil.getFormatNum2(""+doubleUseMoney2).trim( )  +  ") �P")  +
                             " �w�ܱ��ʤ��ĵo���B("  +  exeUtil.getFormatNum2(""+doubleUseMoney).trim( )  +  ") �X�p �W�L "  +
                             " �ܮץi�ϥιw����B�X�p("  +  exeUtil.getFormatNum2(""+doubleBudgetMoney).trim( )  +  ")�A�и߰� [��P������]�C "  ;                   
                if(!booleanAllNoControl) {
                    if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n" ;
                    stringErrMessage  +=  stringTemp ;
                }
            }
            // �צU�~�D�O�w��X�p�ˮ�
            // �w���~���@�B�z         �קK�����ˮ�      
            if((!booleanAllNoControl  &&  "".equals(stringErrMessage)   &&    vectorFullProjectID1.indexOf(stringProjectID1)==-1)  ||  booleanTest) {
                // �ǤJ�Ѽ�
                retTableData[0][0]  =  stringProjectID1+"%-%"+doubleUseMoneyThis ;
                setTableData("TableCheck",  retTableData) ;
                //
                long  longTime1  =  exeUtil.getTimeInMillis( ) ;
                getButton("ButtonProjectCheck").doClick() ;
                long longTime2  =  exeUtil.getTimeInMillis( ) ;
                System.out.println("�`�קO�w���ˮ� �ɶ�-----------------"  +  ((longTime2-longTime1))) ;
                // �^�Ǫ��A
                retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
                stringTemp     =  retTableData[0][0].trim() ;
                System.out.println(intNo+"�צU�~�D�O�w��X�p�ˮ�["+stringTemp+"]-------------------------------") ;
                // �P�_
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
        //vectorBudgetCd.add("BC31") ;  //�l�q
        //vectorBudgetCd.add("BC32") ; //���q
        //vectorBudgetCd.add("BC33") ; //��L�ҰȶO
        //vectorBudgetCd.add("BC35") ; // ����
        //vectorBudgetCd.add("BC36") ; //���ݤ��߯���
        //vectorBudgetCd.add("BD46") ; // ��d����O
        return  vectorBudgetCd ;
    }
    public  Vector  getSpecCostID( ) throws  Throwable {
        Vector      vectorCostID          =  new  Vector() ;
        //
        vectorCostID.add("26-1") ;     // ���O
        vectorCostID.add("26-2") ;     // �q�O
        vectorCostID.add("23-1") ;     // �q�ܶO
        vectorCostID.add("99-0") ;     // ����
        // �� 2017/01/31 ��������
        if("2017/01/31".compareTo(datetime.getToday("YYYY/mm/dd"))  >=  0)  vectorCostID.add("83-1") ;     // ��d����O
        return  vectorCostID ;
    }
    public  Vector  getSpecProjectID1( ) throws  Throwable {
        Vector      vectorDeptCd          =  new  Vector() ;
        //
        vectorDeptCd.add("TC") ;  // �קK getDataReorganization �P�_�����~
        vectorDeptCd.add("GT") ;    
        vectorDeptCd.add("O03A") ;    // 2012-09-17  �����q���W�[
        vectorDeptCd.add("M61A") ;    // 2012-09-17  �����q���W�[
        vectorDeptCd.add("XG2") ;       // 2014-11-28  �P���o �q�� �W�[(���߰ݯ���)
        vectorDeptCd.add("F1") ;       
        vectorDeptCd.add("XG2") ;       
        return  vectorDeptCd ;
    }
    public  String[][]  getDataReorganization(String  stringFlow,  String[][]  retFunctionTypeV,  Vector  vectorSpecProjectID1,  Hashtable  hashtableBigBudget,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        // oce
        String         stringTable                               =  "Table3" ;  
        String      stringFieldName             =  "RealMoney" ;
        boolean     boolean���ʸ�T             =  false ;
        if(stringFlow.indexOf("���ʸ�T")  !=  -1) {
            stringTable         =  "Table3" ;
            stringFieldName   =  "PurchaseMoneyNow" ;
            boolean���ʸ�T  =  true ;
        } else if(stringFlow.indexOf("�ɴڨR�P")  !=  -1) {
            stringTable         =  "Table2" ;
            stringFieldName   =  "RealTotalMoney" ;
        } else if(stringFlow.indexOf("�ɴ�")  !=  -1) {
            stringTable         =  "Table6" ;
            stringFieldName   =  "RealTotalMoney" ;
        } else if(stringFlow.indexOf("�д�")  !=  -1) {
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
        Vector       vectorProjectID1NoUseBudget   =  new  Vector() ;                                                                               // ���ˬd�w��
        Vector       vectorCostIDNoUseBudgetV       =  getVectorCostIDNoUseBudget( retFunctionTypeV,  exeUtil) ;             // ��-���ˬd�w��
        Vector        vectorBudgetCd                     =  getSpecBudgetCd( ) ;  // �S��w��N�X�A�Y�Ϲw��W�L���i����
        Vector        vectorCostID                           =  getSpecCostID() ;        // �S��дڥN�X�A�Y�Ϲw��W�L���i����
        Vector      vectorType                  =  new  Vector() ;
        //
        if(boolean���ʸ�T) {
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
            if(!boolean���ʸ�T)
            stringCostID                    =  (""+getValueAt(stringTable,  intNo,  "CostID")).trim() ;
            if(!boolean���ʸ�T)
            stringCostID1                 =  (""+getValueAt(stringTable,  intNo,  "CostID1")).trim() ;
            if(boolean���ʸ�T)
            stringNoUseRealMoney  =  (""+getValueAt(stringTable,  intNo,  "PurchaseMoneyNotUse")).trim() ;
            //
            stringRealMoney             =  (""+getValueAt(stringTable,  intNo,  stringFieldName)).trim() ;
            stringBudgetMoney         =  (stringFlow.indexOf("��T")!=-1 || stringFlow.indexOf("����")==-1)  ?  "0":(""+getValueAt(stringTable,  intNo,  "BudgetMoney")).trim() ;     // oce
            //
            stringRealMoney           =  ""+(exeUtil.doParseDouble(stringRealMoney)  -  exeUtil.doParseDouble(stringNoUseRealMoney)) ;
            doubleRealMoney           =  exeUtil.doParseDouble(stringRealMoney)  ;
            doubleBudgetMoney        =  exeUtil.doParseDouble(stringBudgetMoney)  ;
            //
            if("I".equals(stringInOut)  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)                            continue ;      // ���~���@�׹w���ˮ�
            if("0531".equals(stringDepartNo))                                                               continue ;      // �򤶤��@�׹w���ˮ�
            if(vectorSpecProjectID1.indexOf(stringProjectID1)  !=  -1)                                              continue ;
            
            
            
            
            
            // �קO or ���� ��z
            // 
            hashtableAnd.put("ComNo",     stringComNo) ;
            hashtableAnd.put("CostID",      stringCostID) ;
            hashtableAnd.put("CostID1",     stringCostID1) ;
            //hashtableAnd.put("UseStatus", "Y") ;
            stringBudgetID  =  exeUtil.getNameUnion("BudgetID",  "Doc2M020",  "",  hashtableAnd,  dbDoc) ;
            //
            if("".equals(stringBudgetID))                                                 continue ;      // �L�����w��N�X ���@�w���ˮ�
            if(stringBudgetID.startsWith("A")  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)   continue ;      // ��F���w��N�X ���@�w���ˮ�
            if(vectorCostIDNoUseBudgetV.indexOf(stringCostID+"%-%"+stringCostID1)  !=  -1)        continue ;      // ���@�w���ˮ� �]���t��w�⤺
            // ��ƳB�z 
            if(vectorBudgetCd.indexOf(stringBudgetID)==-1  &&    vectorCostID.indexOf(stringCostID+"-"+stringCostID1)==-1) {
                stringType         =  exeUtil.doSubstring(stringBudgetID,  0,  1) ;           if(vectorType.indexOf(stringType)  ==  -1)vectorType.add(stringType) ;
                stringBigBudget  =  ""+hashtableBigBudget.get(stringBudgetID) ;       if(!"null".equals(stringBigBudget)  &&  vectorBigBudget.indexOf(stringBigBudget)==-1)  vectorBigBudget.add(stringBigBudget) ;
                //System.out.println("stringBudgetID("+stringBudgetID+")stringBigBudget("+stringBigBudget+")----------------------------------------") ;
                // �קO�έp
                if(vectorProjectID1.indexOf(stringProjectID1)  ==  -1) vectorProjectID1.add(stringProjectID1) ;
            }
            
            // �ĵo���B
            // ��
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
            // ��-�|�j��
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
            // �w����B
            // ��
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
            // ��-�|�j��
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
        if(boolean���ʸ�T) {
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
                // �קO or ���� ��z
                if("I".equals(stringInOut)  &&  ",03365,".indexOf(","+stringDepartNo+",")==-1)                            continue ;      // ���~���@�׹w���ˮ�
                if("0531".equals(stringDepartNo))                                                               continue ;      // �򤶤��@�׹w���ˮ�
                if(vectorSpecProjectID1.indexOf(stringProjectID1)  !=  -1)                                              continue ;
                
                //
                hashtableAnd.put("ComNo",     stringComNo) ;
                hashtableAnd.put("CostID",      stringCostID) ;
                hashtableAnd.put("CostID1",     stringCostID1) ;
                stringBudgetID  =  exeUtil.getNameUnion("BudgetID",  "Doc2M020",  "",  hashtableAnd,  dbDoc) ;
                stringType          =  (stringBudgetID.length()>0) ? stringBudgetID.substring(0,  1) : "" ;
                //
                if(!"B".equals(stringType)) continue ;
                // �ĵo���B
                // ��
                stringKEY               =  stringProjectID1+"-"+stringType ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;  
                doubleRealMoneySum  +=  doubleRealMoney ;
                hashtableRealMoney.put(stringKEY,  ""+doubleRealMoneySum) ; 
                // ��-�|�j��
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
            // ����
            for(int  intNoL=0  ;  intNoL<vectorType.size()  ;  intNoL++) {
                stringType  =  ""+vectorType.get(intNoL) ;if("".equals(stringType)) continue ;
                //
                stringKEY                       =  stringProjectID1+"-"+stringType ;
                doubleBudgetMoneySumL         =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;              // ��-�ĵo
                doubleBudgetMoneySumL_1       =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY+"-SPEC")) ;      // ��-�ĵo������
                doubleBudgetMoneySumL2      =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;            // ��-�w��
                doubleBudgetMoneySumL2_1    =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY+"-SPEC")) ;    // ��-�w�⤣����
                if(doubleBudgetMoneySumL>0  ||  doubleBudgetMoneySumL2>0) {
                    arrayTemp                      =  new  String[6] ;
                    arrayTemp[0]             =  stringProjectID1 ;                                      // �קO
                    arrayTemp[1]             =  "B" ;                                               // 
                    arrayTemp[2]             =  convert.FourToFive(""+doubleBudgetMoneySumL,    0) ;      // ��-�ĵo-�ϥΪ��B
                    arrayTemp[3]             =  convert.FourToFive(""+doubleBudgetMoneySumL2,  0) ;       // ��-�w��-�ϥΪ��B
                    arrayTemp[4]             =  convert.FourToFive(""+doubleBudgetMoneySumL_1,      0) ;      // ��-�ĵo-�����ު��B
                    arrayTemp[5]             =  convert.FourToFive(""+doubleBudgetMoneySumL2_1,     0) ;      // ��-�w��-�����ު��B
                    vectorTableCheck.add(arrayTemp) ;
                }
            }
        }
        if("B30181".equals(getUser()))
        for(int  intNo=0  ;  intNo<vectorProjectID1.size()  ;  intNo++) {
            stringProjectID1                    =  (""+vectorProjectID1.get(intNo)).trim() ;
            // �|�j��
            for(int  intNoL=0  ;  intNoL<vectorBigBudget.size()  ;  intNoL++) {
                stringBigBudget  =  ""+vectorBigBudget.get(intNoL) ;
                stringKEY         =  stringProjectID1+"-"+stringBigBudget ;
                //
                System.out.println(intNoL+"�|�j��------------------------------------1") ;
                if(exeUtil.doParseDouble(stringBigBudget)  <=  0)  continue ;
                //
                doubleBudgetMoneySumL               =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY)) ;
                doubleBudgetMoneySumL_1               =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKEY+"-SPEC")) ;
                doubleBudgetMoneySumL2              =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY)) ;            
                doubleBudgetMoneySumL2_1            =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringKEY+"-SPEC")) ;  
                System.out.println(intNoL+"�|�j��("+doubleBudgetMoneySumL+")("+doubleBudgetMoneySumL2+")------------------------------------2") ;
                if(doubleBudgetMoneySumL>0  ||  doubleBudgetMoneySumL2>0) {
                    arrayTemp                      =  new  String[6] ;
                    arrayTemp[0]             =  stringProjectID1 ;                                            // �קO
                    arrayTemp[1]             =  stringBigBudget ;                                         // stringBigBudget
                    arrayTemp[2]             =  convert.FourToFive(""+doubleBudgetMoneySumL,    0) ;          // ��-�ĵo-�ϥΪ��B
                    arrayTemp[3]             =  convert.FourToFive(""+doubleBudgetMoneySumL2,  0) ;         // ��-�w��-�ϥΪ��B
                    arrayTemp[4]             =  convert.FourToFive(""+doubleBudgetMoneySumL_1,    0) ;      // ��-�ĵo-�����ު��B
                    arrayTemp[5]             =  convert.FourToFive(""+doubleBudgetMoneySumL2_1,  0) ;       // ��-�w��-�����ު��B
                    vectorTableCheck.add(arrayTemp) ;
                    System.out.println(intNoL+"�|�j��("+vectorTableCheck.size()+")------------------------------------3") ;
                }
            }
        }
        if(vectorTableCheck.size()  >  0) {
            return  (String[][])vectorTableCheck.toArray(new  String[0][0]) ;
        } 
        return  new  String[0][0] ;
    }
    // �q��
    public void doMail(String  stringTxt,  String[][]  retTable,  String[][]  retTable2,  FargloryUtil  exeUtil)throws Throwable{
        String                             stringDocNo     =  getValue("DocNo1").trim()+"-"+getValue("DocNo2").trim()+"-"+getValue("DocNo3").trim() ;
        String                             stringBarCode  =  getValue("BarCodeOld").trim( ) ;;
        //
        String    stringFunction    =  getFunctionName() ;
        String    stringSubject      =  stringFunction+"���s�¦~�׺��ˮֿ��~ �q��" ;
        String    stringContent     =  stringSubject  +  "<br>���X�s���G["+stringBarCode+"]<br>����s���G["+stringDocNo+"]<br>"+stringTxt ;
        String    stringSend          =  "B3018@farglory.com.tw" ;
        String[]  arrayUser           =  {stringSend} ;
        for(int  intNo=0  ;  intNo<retTable.length  ;  intNo++) {
            stringContent  +=  "<br>"+intNo+"---��["+retTable[intNo][0]+"]"  ;
        }
        for(int  intNo=0  ;  intNo<retTable2.length  ;  intNo++) {
            stringContent  +=  "<br>"+intNo+"---�s["+retTable2[intNo][0]+"]"  ;
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
    // ��Ʈw Doc
    // ��Ʈw Doc2M010...
    public  String[]  getCheckMoneyFront(String  stringProjectID1,  String  stringBudgetType,  String  stringBarCodeOld,  String[][]  retFunctionTypeV,  boolean  booleanFlow,  FargloryUtil  exeUtil,  talk  dbDoc) throws  Throwable {
        String    stringTemp                                 =  "" ;  
        String    stringCostIDNoUseBudgetVSql    =  getCostIDNoUseBudgetSql(retFunctionTypeV,  exeUtil) ;
        String[]  retCheckMoney                           =  getCheckMoneyDoc3M014(stringProjectID1,  stringBudgetType,  stringBarCodeOld,  stringCostIDNoUseBudgetVSql,  booleanFlow,  exeUtil,  dbDoc) ;
        double  doubleTemp                              = 0 ;
        // 0  �ĵo���B                    1  �w�����B                   2  �׹w��ϥΤ�       3  �׹w��y�{��
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
        // 0  �ĵo���B        1  �ĵo���B            2  �w�����B                   3  �׹w��ϥΤ�      4  �׹w��y�{��
        stringSql    =  " SELECT \n " ;
        //   0  �ĵo���B
        stringSql  +=  "(SELECT  SUM(RealMoney) \n"  +
                   " FROM  "+stringTable+" \n"  +
                  stringSqlAnd  +
                                  " AND  BarCode  <>  '"+stringBarCodeOld+"' \n"  +
                   " AND  ComNo  =  '"+stringComNo+"' \n" +
                   " AND  CostID1  <> '' " +
                   " AND  TYPE  =  'A' \n" +
                             ") AS '0  �ĵo���B1' , \n" ;
        stringSql  +=  "(SELECT  SUM(RealMoney) \n"  +
                    " FROM  "+stringTable+" \n"  +

                  convert.replace(stringSqlAnd,  "+RTRIM(CostID1)",  "")  +
                                  " AND  BarCode  <>  '"+stringBarCodeOld+"' \n"  +
                   " AND  ComNo  =  '"+stringComNo+"' \n" +
                   " AND  CostID1  = '' " +
                   " AND  TYPE  =  'A' \n" +
                             ") AS '1  �ĵo���B2' , \n" ;
        //  1  �w�����B
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
                                   ") AS '2  �w�����B', \n" ;
        }
        //  2  CHECK(�צX�p) 1
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
        stringSql  +=  ") AS '3  CHECK(��) 1', \n" ;
        //  7  CHECK(�צX�p) 1
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
        stringSql  +=  ") AS '4  CHECK(��) 2' \n" ;
        System.out.println(stringProjectID1+"--------------getCheckMoney-----------------------------------");
        String[][]  retData  =  dbDoc.queryFromPool(stringSql) ;
        //
        return  retData[0] ;
    }
    // ��ƪ� Doc2M020
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
    // ��ƪ� Doc2M0201
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
    // ��ƪ� Doc7M072
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
            // �j�w�������z
            vectorBudgetID  =  (Vector) hashtableBudgetIDs.get(stringBigBudget) ;
            if(vectorBudgetID  ==  null) {
                vectorBudgetID  =  new  Vector() ;
                hashtableBudgetIDs.put(stringBigBudget,  vectorBudgetID) ;
            }
            if(vectorBudgetID.indexOf(stringBudgetID)  ==  -1)vectorBudgetID.add(stringBudgetID) ;
            // �w��N�X ���� �j�w�����
            hashtableBigBudget.put(stringBudgetID,  stringBigBudget) ;
        }
        
        
  }
  public String getInformation(){
    return "---------------ButtonTableCheck(\u5e74\u5ea6\u9810\u7b97\u6aa2\u6838).defaultValue()----------------";
  }
}
