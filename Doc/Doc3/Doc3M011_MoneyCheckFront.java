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
        System.out.println("�w���ˮ� Batch--------------------------S11111111111") ;
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
        String        stringView                =  "View" ;  // 2016/11/08 �W�u
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
        System.out.println("�׹w��s�b�ˮ� �ɶ�-----------------S") ;
        if(!"OK".equals(stringProjectIDExistStatus)) {
            message("�׹w����~��T�A���I��O�w�A�˵����Ӥ��e�C\n"+stringProjectIDExistStatus) ;
            messagebox("�׹w����~��T") ;
            //
            retTableCheckData[0][0]  =  "ERROR" ;
            setTableData("TableCheck",  retTableCheckData) ;
            return  value ;
        }
        long        longTime2                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�׹w��s�b�ˮ� �ɶ�-----------------E("  +  ((longTime2-longTime1))+")") ;
        
        
        
        // ��
        System.out.println("�׹w���ˮ� �ɶ�-----------------S") ;
        getButton("ButtonProjectBudgetCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            booleanError           =  true ;
            errBudgetMsg += "[�׹w����~]";
            //if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            //stringMessage       +=  stringTemp ;
            if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
            stringErrMessage  +=  stringTemp ;
        }
        long        longTime3                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�׹w���ˮ� �ɶ�("+booleanError+")-----------------E("  +  ((longTime3-longTime2))+")") ;
        
        // �צ~�� 
        System.out.println("�צ~�׹w���ˮ� �ɶ�-----------------S") ;
        getButton("ButtonYearBudgetCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            booleanError           =  true ;
            errBudgetMsg += "[�צ~�׹w����~]";
            if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            //stringMessage       +=  stringTemp ;
            if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n\n" ;
            stringErrMessage  +=  stringTemp ;
        }
        long        longTime4                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�צ~�׹w���ˮ� �ɶ�("+booleanError+")-----------------E("  +  ((longTime4-longTime3))+")") ;
        
        if("".equals(stringView)) {
            // �w�b ButtonYearBudgetCheckView ���B�z
            // �~�ׯS�O�дڥN�X���� ButtonTableCheckN
            System.out.println("�~�ׯS�O�дڥN�X���� �ɶ�-----------------S") ;
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
        System.out.println("�~�ׯS�O�дڥN�X���� �ɶ�("+booleanError+")-----------------E("  +  ((longTime5-longTime4))+")") ;
        // �q���N�X �w���ˮ�
        getButton("ButtonSSMediaIDCheck"+stringView).doClick() ;
        retTableData  =  getTableData("TableCheck")  ;  setTableData("TableCheck",  new  String[0][0]) ;
        stringTemp     =  retTableData[0][0].trim() ;
        if(!"".equals(stringTemp)) {
            //booleanError     =  false ;
            errBudgetMsg += "[�q���w����~]";
            if(!"".equals(stringMessage))  stringMessage  +=  "\n\n" ;
            stringMessage       +=  stringTemp ;
            //if(!"".equals(stringErrMessage))  stringErrMessage  +=  "\n\n" ;
            //stringErrMessage  +=  stringTemp ;
            //if("B3018".equals(getUser()))messagebox("�q���N�X �w���ˮֿ��~"+stringMessage) ;
        }
        long        longTime6                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�q���N�X �w���ˮ� �ɶ�-("+booleanError+")----------------E("  +  ((longTime6-longTime5))+")") ; 
        
        
        // ��J�����w�ⱱ��
/*        System.out.println("��J�����w�ⱱ�� �ɶ�("+booleanError+")-----------------S") ;
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
        System.out.println("��J�����w�ⱱ�� �ɶ�-("+booleanError+")----------------E("  +  ((longTime7-longTime6))+")") ;
        
        
        
        
        
        // �w���ˮ֨ҥ~
        String  stringBarCodeOld        =  getValue("BarCodeOld").trim() ;if("".equals(stringBarCodeOld))  stringBarCodeOld  =  getValue("BarCode").trim() ;
        String  stringBarCodeExcept  =  exeUtil.getNameUnion("BarCodeDoc3M011",  "Doc2M040",  " AND  ISNULL(BarCodeDoc3M011, '')  <> '' ",  new  Hashtable(),  dbDoc) ;
        if(!"".equals(stringBarCodeOld)  &&  stringBarCodeExcept.indexOf(stringBarCodeOld)!=-1) {
            booleanError  =  false ;
        }
        if(booleanError) {
            message("�w����~��T"+errBudgetMsg+"�A���I��O�w�A�˵����Ӥ��e�C\n"+stringErrMessage+"\n"+stringMessage) ;
            messagebox("�w����~"+errBudgetMsg+"�C\n�ԲӸ�T�A���I���A�C��O�w�A�˵����e�C\n"+stringMessage) ;
            retTableCheckData[0][0]  =  "ERROR" ;
        } else {
            if(!"".equals(stringMessage)  ||  !"".equals(stringMessage)) {
                messagebox(stringMessage) ;
                message("�w���T"+errBudgetMsg+"�A���I��O�w�A�˵����Ӥ��e�C\n"+stringErrMessage+stringMessage) ;
            } else {
                message("") ;
            }
            retTableCheckData[0][0]  =  "OK" ;
        }
        setTableData("TableCheck",  retTableCheckData) ;
        long        longTime8                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�w���ˮ֨ҥ~ �ɶ�-("+booleanError+")----------------E("  +  ((longTime8-longTime7))+")") ;
        //
        if("B3018".equals(stringUser)) {
            Hashtable  hashtableAnd  =  new  Hashtable() ;  hashtableAnd.put("KEY_USER",  stringUser) ;hashtableAnd.put("KEY_BarCode",  stringBarCode) ;
            String      stringSql     =  exeUtil.doDeleteDB("Doc3M014_BudgetCheckTemp",  hashtableAnd,  " AND  KEY_Time  <= '"+stringNowTime+"' ",  true,  dbDoc) ;
            System.out.println("�R�� Doc3M014_BudgetCheckTemp -------------------------"+stringSql) ;
            long        longTime9                 =  exeUtil.getTimeInMillis( ) ;
            System.out.println("�R�� �ɶ�-("+booleanError+")----------------E("  +  ((longTime9-longTime8))+")") ;
        }

        
        long        longTimeEND                 =  exeUtil.getTimeInMillis( ) ;
        System.out.println("�w���ˮ֪�O�ɶ�-----------------("  +  ((longTimeEND-longTime1))+")") ;
        System.out.println("�w���ˮ� Batch --------------------------E") ;
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
                System.out.println("�s�W Doc3M014_BudgetCheckTemp -------------------------"+stringSql) ;
            }
        }
        return  stringMessage ;
    }
    public  Vector  getSpecDeptCd( ) throws  Throwable {
        String      stringSpecBudget       =  ""+get("SPEC_BUDGET") ;                     // �S��w�ⱱ��
        String[]       arraySpecBudget          =  convert.StringToken(stringSpecBudget,",") ;
        Vector      vectorDeptCd          =  new  Vector() ;
        //
        vectorDeptCd.add("0331") ;  // ���ʲ���P
        vectorDeptCd.add("0333") ;  // ��P������
        vectorDeptCd.add("03335") ; // ������P��
        vectorDeptCd.add("033622") ; // �q����P��
        vectorDeptCd.add("03363") ;  // �Ʀ��o��
        vectorDeptCd.add("03365") ;  //��Ʈw��P��C
        vectorDeptCd.add("03396") ;  //��X��P��
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
            stringTemp  =   stringTemp+" �� �קO "  +  stringProjectID1  +  " ���s�b�� [�׹w�ⱱ��] ���A�� ["+("A".equals(stringFunctionType) ? "��P�޲z��" : "��P������")+"] �s�W�קO��ơC "  ;
            return  stringTemp ;
        }
        // ���������w��y�{�A���wñ�֦� [��t�D��] �B [CDate] �b [���}��] ���e�A���\
        hashtableDoc7M020  =  (Hashtable)  vectorDoc7M020.get(0) ;          
        stringOpenSaleDate   =  ""+hashtableDoc7M020.get("OpenSaleDate") ;
        if("".equals(stringOpenSaleDate)) {
            stringTemp  =  "�ӮקO("+stringProjectID1+") �� [�׹w�ⱱ��(Doc7M020)] �� [���}��] ���ťաA������� [�׫e�w��]�A�� �߰�  ["+("A".equals(stringFunctionType) ? "��P�޲z��" : "��P������")+"] �C" ;
            return  stringTemp ;
        }
        if(stringNeedDateAC.compareTo(stringOpenSaleDate)  >  0) {
            stringTemp  =  "[��J���] ��� �ӮקO("+stringProjectID1+") �� [�׹w�ⱱ��(Doc7M020)]  [���}��] ��A������� [�׫e�w��]�A�� �߰�  ["+("A".equals(stringFunctionType) ? "��P�޲z��" : "��P������")+"] �C"  ;
            return  stringTemp ;
        }
        String  stringDate  =  ""+hashtableDoc7M020.get("CreepSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("OpenSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("StrongSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("EndSaleDate") ;
        if("".equals(stringDate))  stringDate  =  ""+hashtableDoc7M020.get("DateStart") ;
        if(stringNeedDateAC.compareTo(stringDate)  <  0) {
            stringTemp  =  "[��J���]("+stringNeedDateAC+") ����  �קO("+stringProjectID1+")��[�׹w�ⱱ��(Doc7M020)] �i�ϥΤ��("+stringDate+")�C"  ;
            return  stringTemp ;
        }
        return  "OK" ;
  }
  public String getInformation(){
    return "---------------ButtonTableCheck(\u5e74\u5ea6\u9810\u7b97\u6aa2\u6838).defaultValue()----------------";
  }
}
