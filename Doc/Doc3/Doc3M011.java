package   Doc.Doc3 ;
import      jcx.jform.bTransaction;
import      java.io.*;
import      java.util.*;
import      jcx.util.*;
import      jcx.html.*;
import      jcx.db.*;
import      javax.swing.* ;
import      Farglory.util.FargloryUtil  ; 
import      Doc.Doc2M010 ;
public  class  Doc3M011  extends  bTransaction {
    public  boolean  action (String  value) throws  Throwable { 
        //201808check BEGIN
        System.out.println("chk==>"+getUser()+" , action value==>"+value.trim());
        if(value.trim().equals("�s�W") || value.trim().equals("�ק�") || value.trim().equals("�R��")) {
          if(getUser() != null && getUser().toUpperCase().equals("B9999")) {
            messagebox(value.trim()+"�v�������\!!!");
            return false;
          }
        }
        //201808check FINISH          
        // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
        // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
        // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
        // isTable12NoPageOK                    ����O�_���\�ҥ~�B�z
        // doSyncBarCode                        �s�W�e BarCode �]��
        // doSetPurchaseSupervisor                  ���ʬ�D�ޤ��]�w
        // doSetBackFlowData                    �h�󤧩ӿ�ñ�֮ɡA����ñ�֦ܸӰh��̫e�@���d
        System.out.println("Doc3M011 Check BarCodeOld ==================> "+getValue("BarCodeOld"));
        getButton("ButtonHalfWidth").doClick() ;          // �����B�z
        getButton("ButtonSetTable12Data").doClick() ;       // ������B�z 
        getButton("ButtonPurchaseState").doClick() ;      // ���ʪ��A�ץ�
        getButton("ButtonPurchaseSubject").doClick() ;    // ���ʦW�ٳB�z
        getButton("ButtonTable3Default").doClick() ;
        getButton("ButtonDoc3M0123").doClick() ;        // ���ʶ��خקO���u�B�z
        getButton("ButtonGroupID").doClick()  ;           // �I�ڸ��
        getButton("ButtonFlow").doClick() ;
        //
        Doc.Doc2M010                exeFun                              =  new  Doc.Doc2M010( ) ;
        FargloryUtil                      exeUtil                               =  new  FargloryUtil() ;
        String                              stringSubject                   =  getFunctionName() ;
        String                              stringSend                      =  "emaker@farglory.com.tw" ;
        String[]                            arrayUser                       =  {"B3018@farglory.com.tw"} ;
        String                  stringUndergoWirteCheck   =  ""+get("Doc3M011_UNDERGO_CHECK") ; put("Doc3M011_UNDERGO_CHECK",  "null") ;    put("Doc3M011_UNDERGO_CHECK_L",  stringUndergoWirteCheck) ;
        String                              stringBarCodeE              =  getValue("BarCode").trim( ) ;
        String                              stringBarCodeOldE           =  getValue("BarCodeOld").trim( ) ;
        String                              stringMessage                   =  stringSubject+"("+value.trim()+")���ʸ�Ʈw-----"+stringBarCodeE+"-----------"+stringBarCodeOldE+"(�ϥΪ̡G"+getUser()+")<br>" ;
        //
        try {
            String      stringBarCode  =  getValue("BarCode").trim( ); 
            getButton("ButtonTable3Default").doClick() ;// �קO���u
            //getButton("button1").doClick() ;// �I�ڱ���
            //if("SYS".equals(getUser()))  { 
            if("SYS".equals(getUser())  &&  !"�R��".equals(value))  { 
                doSyncBarCode( ) ; 
                /*
                if(!isTable12CheckOK(exeUtil,  exeFun))  return  false ;
                */
                /*if("�R��".equals(value.trim( ))) {
                    exeFun.doDeleteDoc1M040(stringBarCode) ;
                    exeFun.doDeleteDoc1M030(stringBarCode) ;
                }*/
                //getButton("ButtonSynDocFlow").doClick() ;
                //setFlowDataNew(exeUtil,   exeFun) ;
                getButton("ButtonTable10View").doClick() ;
                //
                return  true ;
            }
            put("Doc3M010_Status",  "STOP") ;

            if(!isFlowCheckOK(value.trim( ),  exeFun,  exeUtil))                 {put("Doc3M010_Status",  "NULL") ;  return  false ;}
            if(!isBatchCheckOK(value,  exeFun,  exeUtil)){
                put("Doc3M010_Status",                "NULL") ;  
                exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode",  new  Hashtable(),  " AND  BarCode  =  '"+getValue("BarCode").trim( )+"' ",  true,  exeUtil) ;
                return  false ;
            }

            if(!isCheckDoc3M080(exeFun,  exeUtil)) {
                put("Doc3M010_Status",                "NULL") ;  
                exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode",  new  Hashtable(),  " AND  BarCode  =  '"+getValue("BarCode").trim( )+"' ",  true,  exeUtil) ;
                return  false ;
            }

            setFlowData(exeUtil,  exeFun) ;
            if(!"�R��".equals(value.trim( ))  &&  stringSubject.indexOf("�H�`")  ==  -1)  doCheckDulFactoryNo (exeFun) ;
            if(getUser().endsWith("--")){
                put("Doc3M010_Status",                "NULL") ;
                return  false ;
            }
            // BarCode �۰ʨ���
            String  stringID                      =  getValue("ID").trim() ;
            String  stringFunctionName  =  getFunctionName() ;
            if("�s�W".equals(value.trim())  &&  stringFunctionName.indexOf("�ӿ�")!=-1)   {
                stringID  =  exeFun.getMaxIDForDoc3M011( ) ;
                setValue("ID",              stringID) ;
                //
                //if(!booleanNoPageDate) {
                //    String      stringDepartNoSubject         =  ""+get("EMP_DEPT_CD") ;
                //    if(stringFunctionName.indexOf("--")==-1)  setValue("BarCode",  exeFun.getMaxBarCode("Z")) ;
                //}
            }
            // ñ�֬y�{�O��
            System.out.println("doInsertDoc5M0182-----------------------------------------------------S") ;
             doInsertDoc5M0182 (exeUtil,  exeFun) ;
             System.out.println("doInsertDoc5M0182-----------------------------------------------------E") ;
             //
            doReSetBarCode(exeUtil,  exeFun) ;
            doSyncBarCode( ) ;
            getButton("ButtonSyn").doClick() ;      // �P�B����l��
            //
            doKindNoD (exeUtil,  exeFun) ;
            // �y�{�O��
            String     stringDeptCd               =  "" ;
            String      stringUser                   =  getUser().toUpperCase() ;
            String      stringComNo              =  getValue("ComNo").trim() ;
            String      stringDocNo               =  getValue("DocNo1").trim()+getValue("DocNo2").trim()+getValue("DocNo3").trim() ;
            String      stringClassNameList  =  getValue("ClassNameList").trim() ;
            String       stringToday              =  datetime.getToday("yymmdd") ;
                             stringToday              =  exeUtil.getDateConvertFullRoc(stringToday).replaceAll("/","") ;
            String[][]  retFE3D103               =  exeFun.getFE3D103(stringUser,  "",  stringToday) ;
            //
            if(retFE3D103.length  >  0)  stringDeptCd  =  retFE3D103[0][0].trim() ;
            //
            stringClassNameList  =  convert.replace(stringClassNameList,  "'",  "''") ;
            exeFun.doInsertForDoc3M011History(stringID,                                                   datetime.getTime("YYYY/mm/dd h:m:s"),  stringUser,  stringDeptCd,  getFunctionName()+" "+value+"---"+stringBarCodeE+"---"+stringBarCodeOldE,
                                          stringClassNameList+"---"+stringDocNo,  stringComNo,  true)  ;
            if("�R��".equals(value.trim( ))) {
                exeFun.doDeleteDoc1M040(stringBarCode) ;
                exeFun.doDeleteDoc1M030(stringBarCode) ;
                exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode",  new  Hashtable(),  " AND  BarCode  =  '"+stringBarCode+"' ",  true,  exeUtil) ;
            }
            if("B3849,B3446,".indexOf(getUser().toUpperCase())==-1)exeUtil.ClipCopy (getValue("BarCode").trim()) ;
        }catch(Exception e){
            Vector  vectorUse  =  exeFun.getEmployeeNoDoc3M011("P",  "") ;
                          arrayUser  =  (String[])  vectorUse.toArray(new  String[0]) ;
            exeUtil.doEMail(stringSubject,  stringMessage+"<br>"+e.toString(),  stringSend,  arrayUser) ;
            messagebox("�{���o�Ϳ��~") ;
            message("��T\n"+e.toString()) ;
            //
            exeFun.doDeleteDBDoc("Doc2M044_AutoBarCode",  new  Hashtable(),  " AND  BarCode  =  '"+getValue("BarCode").trim( )+"' ",  true,  exeUtil) ;
            return  false ;
        }
        getButton("ButtonTable10View").doClick() ;
        if(",B3018,".indexOf(","+getUser().toUpperCase()+",")!=-1){ messagebox("TEST") ;return  false ;}
        return true;
    }
    public  void  doInsertDoc5M0182 (FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable{
        String          stringFlow                      =  getFunctionName() ; 
        String          stringSql                       =  "" ; 
        String          stringUndergoWrite          = "" ;
        String          stringUndergoName         = "" ;
        String        stringUndergoWirteCheck   =  ""+get("Doc3M011_UNDERGO_CHECK_L") ; 
        JTable        jtable10                  =  getTable("Table10") ; 
        System.out.println(jtable10.getRowCount()+"-----------------------------------------------------S") ;
        if(jtable10.getRowCount()  ==  0)  return ;
        Hashtable     hashtableData             =  new  Hashtable() ;
        if(stringFlow.indexOf("����")  !=  -1) {
            //stringUndergoName  =  ("Y".equals(stringUndergoWirteCheck))?"���ʫǩӿ�-�e�e" : "���ʫǩӿ�" ;
            stringUndergoName  =  ("Y".equals(stringUndergoWirteCheck))?"�����y�{" : "���ʫǩӿ�" ;
            stringUndergoWrite  =  "50" ;
        } else if(stringFlow.indexOf("�ӿ�")  !=  -1) {
            stringUndergoName  =  "�ӿ�" ;
            stringUndergoWrite  =  "10" ;
        } else {
            stringUndergoName  =  "�~��" ;
            stringUndergoWrite  =  "10" ;
        }
        // ñ�ְO��
        hashtableData.put("ID",                    getValue("ID").trim()) ;
        hashtableData.put("SourceType",              "A") ;
        hashtableData.put("BarCode",              getValue("BarCode").trim()) ;
        hashtableData.put("CheckDate",            datetime.getTime("YYYY/mm/dd h:m:s")) ;
        hashtableData.put("UNDERGO_WRITE",  stringUndergoWrite) ;
        hashtableData.put("UNDERGO_NAME",       stringUndergoName) ;
        hashtableData.put("CheckStatus",          "Y") ;
        hashtableData.put("CheckEmployeeNo",    getUser()) ;
        hashtableData.put("Descript",                     "") ;
        stringSql  =  exeFun.doInsertDBDoc("Doc5M0182",  hashtableData,  false,  exeUtil) ;addToTransaction(stringSql) ;
        System.out.println(""+stringSql) ;
    }
    // �ҥ~-���X�s�����s�]�w
    public void  doReSetBarCode(FargloryUtil  exeUtil,  Doc2M010  exeFun)throws Throwable{
        String        stringBarCode        =  getValue("BarCode").trim() ;
        String        stringBarCodeOld  =  getValue("BarCodeOld").trim() ;
        Vector        vectorDoc2M040   =  new  Vector() ;
        Hashtable  hashtableAnd        =  new  Hashtable() ;
        Hashtable  hashtableTemp     =  new  Hashtable() ;
        Hashtable  hashtableData       =  new  Hashtable() ;
        //
        vectorDoc2M040  =  exeFun.getQueryDataHashtableDoc("Doc2M040",  hashtableAnd,  " ORDER BY  ACCT_D ",  new  Vector(),  exeUtil) ;
        if(vectorDoc2M040.size()  ==  0)                    return ;
        //
        hashtableTemp  =  (Hashtable)  vectorDoc2M040.get(0) ;
        if(hashtableTemp  ==  null)                              return ;
        //
        if("".equals(stringBarCodeOld) )                    return ;
        if(stringBarCodeOld.equals(stringBarCode))  return ;
        // �w��ҥ~
        String  stringBarCodeDoc3M011  =  ""+hashtableTemp.get("BarCodeDoc3M011") ;
        //
        if("".equals(stringBarCodeDoc3M011)  ||  "null".equals(stringBarCodeDoc3M011)) return ;
        
        if(stringBarCodeDoc3M011.indexOf(stringBarCodeOld)  ==  -1) return ;
        hashtableAnd.put("BarCodeDoc3M011",   stringBarCodeDoc3M011) ;
        hashtableData.put("BarCodeDoc3M011",  stringBarCodeDoc3M011.replaceAll(stringBarCodeOld,  stringBarCode)) ;
        exeFun.doUpdateDBDoc("Doc2M040",  "",  hashtableData,  hashtableAnd,  true,  exeUtil) ;
    }
    public  void  doKindNoD (FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable{
        JTable   jtable3                      =  getTable("Table3") ;
        String    stringUndergoWrite  =  getValue("UNDERGO_WRITE").trim() ;
        String    stringField                =  (getFunctionName().indexOf("����")==-1)?"BudgetMoney":"RealMoney" ;
        String    stringKindNoD          =  "" ;
        double  doubleMoney           =  0 ;
        for(int  intNo=0  ;  intNo<jtable3.getRowCount()  ;  intNo++) {
            doubleMoney  +=  exeUtil.doParseDouble((""+getValueAt("Table3",  intNo,  stringField)).trim()) ;
        }
        /* 2016-07-18 �ץ�
        17-6 �@�������ʳ�(1000�U�H�W)
        17-5 �@�������ʳ�(200�U~1000�U)
        17-4 �@�������ʳ�(200�U�H�U)
        */
        if(doubleMoney  <=  2000000) {
            stringKindNoD  =  "17-4" ;
        }else if(doubleMoney  <=  10000000) {
            stringKindNoD  =  "17-5" ;
        }else  {
            stringKindNoD  =  "17-6" ;
        }
        setValue("KindNoD",  stringKindNoD) ;
        // �̤������O KindNoD�A�ץ��w�w���פ��
        String  stringKindDay      =  exeFun.getKindDay(stringKindNoD) ;
        String  stringCDate          =  getValue("CDate").trim().replaceAll("/",  "") ;
        String  stringCDateAC     =  exeUtil.getDateConvert(stringCDate.replaceAll("/",  "")) ;
        String   stringPreFinDate =  datetime.dateAdd(stringCDate,  "d",  exeUtil.doParseInteger(stringKindDay)) ;
        setValue("PreFinDate",  exeUtil.getDateConvertFullRoc(stringPreFinDate)) ;
    }
    // �䥦�X���n���@�~�ˮ�
    public  boolean  isCheckDoc3M080 (Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable{
        Vector  vectorUserU  =  new  Vector() ;
        //vectorUserU.add("B3018") ;
        //vectorUserU.add("B1381") ;
        if(vectorUserU.indexOf(getUser().toUpperCase())  ==  -1)    return  true ;
        //
        String  stringFunctionName  =  getFunctionName() ;
        String  stringComNo             =  getValue("ComNo").trim();
        String  stringDocNo               =  getValue("DocNo").trim();
        String  stringBarCode           =  getValue("BarCode").trim();
        String  stringApply                =  getValue("ApplyType").trim() ;
        String  stringCDate              =  getValue("CDate").trim() ;
                    stringCDate             =  exeUtil.getDateConvertFullRoc(stringCDate) ;
        //
        if(stringFunctionName.indexOf("����")  ==  -1)  return  true ;
        if("F".equals(stringApply))                                  return  true ;
        //�P�_�䥦�X���O�_���n��
        // 0  �ƥ�      1  AcceptRealDate       2  PickRealDate
        String[][]  retDoc8M010  =  getDoc8M010(exeFun) ;
        boolean  booleanFlag    =  true ;
        //
        if (retDoc8M010==null  ||   retDoc8M010.length==0      ||   retDoc8M010[0][0].trim().length()==0  ||   "0".equals(retDoc8M010[0][0].trim())) {
            // ���s�b
            JInternalFrame  ff  =  getInternalFrame(stringFunctionName);
            int                       n  =  JOptionPane.showConfirmDialog(ff , "�Ц� [�䥦�X���n���@�~] �i��X���n��" , "�T������" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                put("Brian_SA001_2_status" , "1");
                put("Brian_SA001_2_CDate" , stringCDate);
                showDialog("��L�X���n���@�~(Doc8M010)" , "" , false , true , 100 , 100 , 850 , 650);
            } 
            return  false ;
        } else if (retDoc8M010!=null  &&  ((retDoc8M010[0][1].trim().length()!=0  &&  retDoc8M010[0][2].trim().length()==0) || 
                                                               (retDoc8M010[0][1].trim().length()==0  &&  retDoc8M010[0][2].trim().length()!=0)) ) {
            JInternalFrame  ff  =  getInternalFrame(stringFunctionName) ; 
            int                        n  =  JOptionPane.showConfirmDialog(ff , "�Цܨ�L�X���n���@�~��J�����ڤ�αĵo��ڤ�" , "�T������" , JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION){
                put("Brian_SA001_2_status" , "2");
                put("Brian_SA001_2_CDate" , stringCDate);
                //
                Hashtable htQuery = new Hashtable ();
                htQuery.put("ComNo" ,             stringComNo);
                htQuery.put("BarCode" ,          stringBarCode);
                htQuery.put("DocNo" ,              stringDocNo);
                htQuery.put("PurchaseType",  "");
                htQuery.put("CDate" ,               "");  
                showDialog("��L�X���n���@�~(Doc8M010)" , "" , false , true , 100 , 100 , 850 , 650 , htQuery);
            }
            return  false ;
        } else if (retDoc8M010!=null  &&  retDoc8M010[0][1].trim().length()!=0  &&  retDoc8M010[0][2].trim().length()!=0) {
            String  stringID    =  getValue("ID").trim() ;
            String  stringSql  =  "" ;
            // �s�b
            stringSql  =  " UPDATE  Doc8M010  SET  ClassName  =  (SELECT  ClassName "  +
                                                                                                         " FROM  Doc3M012 "  +
                                                          " WHERE  RecordNo  =  '1' "  +
                                                                " AND  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                          " FactoryNo   =  (SELECT  FactoryNo "  +
                                                                        " FROM  Doc3M012 "  +
                                                          " WHERE  RecordNo  =  '1'  "  +
                                                               " AND  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                          " PurchaseEmployeeNo  =  (SELECT  PurchaseEmployeeNo "  +
                                                                                            " FROM  Doc3M011 "  +
                                                                    " WHERE  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                          " BudgetMoney  =  (SELECT  SUM(ApplyMoney) "  +
                                                                              " FROM   Doc3M012 "  +
                                                             " WHERE  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                          " RealMoney       =  (SELECT  SUM(PurchaseMoney) "  +
                                                                              " FROM  Doc3M012 "  +
                                                            " WHERE  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                         " DifferenceMoney  =  (SELECT  ISNULL(SUM(ApplyMoney),0) - ISNULL(SUM(PurchaseMoney),0) "  +
                                                                                     " FROM  Doc3M012 "  +
                                                               " WHERE  ID  =  '"  +  convert.ToSql(stringID)  +  "'), "  +
                                                                         " DifferenceRate  =  (CASE (SELECT  SUM(ApplyMoney) FROM Doc3M012  WHERE  ID  =  '"+convert.ToSql(stringID)+"') "  +
                                                                                " WHEN  0   THEN  0  "  +
                                                                                                            " ELSE (SELECT  ((ISNULL(SUM(ApplyMoney),0) - ISNULL(SUM(PurchaseMoney),0)) /  ISNULL(SUM(ApplyMoney),1) * 100) "  +
                                                                           " FROM  Doc3M012 "  +
                                                                   " WHERE  ID  =  '"  +  convert.ToSql(stringID)  +  "')  end) "  +
                             " WHERE  ComNo  =  '"     +  convert.ToSql(stringComNo)    +  "' "  +
                                   " AND  BarCode  =  '"  +  convert.ToSql(stringBarCode)  +  "' "  +
                                   " AND  DocNo  =  '"      +  convert.ToSql(stringDocNo)     +  "' " ;
            addToTransaction(stringSql);
            System.out.println("---------------------------------------"+stringSql) ;
        }
        return  true ;
    }
    public  void  doCheckDulFactoryNo (Doc.Doc2M010  exeFun) throws  Throwable{
        String      stringFactoryNo         =  "" ;
        String      stringFactoryName    =  "" ;
        String      stringFactoryNameQ  =  "" ;
        String      stringMessage            =  "" ;
        Vector      vectorFactoryNo       =  getFactoryNoTable2( ) ;
        String[][]  retData                      =  null ;
        //
        for(int  intNo=0  ;  intNo<vectorFactoryNo.size()  ;  intNo++) {
            stringFactoryNo       =  (""+vectorFactoryNo.get(intNo)).trim() ;
            stringFactoryName  =  exeFun.getFactoryNameForDoc3M015(stringFactoryNo) ;
            //
            if(stringFactoryName.length()>2) {
                stringFactoryNameQ  =  stringFactoryName.substring(0,2) ;
            } else {
                stringFactoryNameQ  =  stringFactoryName ;
            }
            //
            retData  =  exeFun.getDoc3M015And("",  " AND  OBJECT_SHORT_NAME  LIKE  '"  +  stringFactoryNameQ  +  "%' ") ;
            if(retData.length  >  1) {
                if(!"".equals(stringMessage))  stringMessage  +=  "\n" ;
                stringMessage +=  "�t�� "+stringFactoryNo+"("+stringFactoryName+") ����W�١A��Ʈw�s�b "+(retData.length)+" ���A���ˬd�t�ӬO�_���T�C" ;
            }
        }
        if(!"".equals(stringMessage))JOptionPane.showMessageDialog(null,  stringMessage,  "�T��",  JOptionPane.ERROR_MESSAGE) ;
    }
    public  boolean  isFlowCheckOK(String  value,  Doc.Doc2M010  exeFun,  FargloryUtil exeUtil) throws  Throwable {
        String      stringUnderGoWrite        =  getValue("UNDERGO_WRITE").trim( ) ;
        String      stringFlow                        =  getFunctionName() ;
        String      stringComNo                     =  getValue("ComNo").trim();
        String    stringNoPageDate        =  ""+get("NO_PAGE_DATE") ;     // ���ʵL�ȤƤW�u���
        String    stringTodayL              =  exeUtil.getDateConvert(getValue("CDate")) ;
        boolean booleanNoPageDate       =  stringTodayL.compareTo(stringNoPageDate)>0 ;
        if(booleanNoPageDate  &&  getTableData("Table10").length<=0) {
            messagebox("ñ�֬y�{��ƿ��~") ;
            return  false ;
        }
        //
        if(!"�s�W".equals(value.trim())) {
            String      stringID            =  getValue("ID").trim() ;
            String[][]  retDoc3M011  =  exeFun.getTableDataDoc("SELECT  UNDERGO_WRITE  FROM  Doc3M011  WHERE  ID  =  "+stringID+" ") ;
            if(retDoc3M011.length  ==  0) {
                JOptionPane.showMessageDialog(null,  "��Ƶo�Ϳ��~�A�Ь���T�ǡC1",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;   
            }
            stringUnderGoWrite  =  retDoc3M011[0][0].trim() ;
        }
        
        /*
        // 20201110 Kyle : ���אּ��s����y�{��� START
        // ����y�{�w�ϥΡA���i���ʸ�Ʈw
        //�д�
        String     stringDocNo     =  getValue("DocNo1").trim()+getValue("DocNo2").trim()+getValue("DocNo3").trim() ;
        String[][]  retDoc5M027  =  exeFun.getTableDataDoc("SELECT  M10.BarCode,  M10.UNDERGO_WRITE "  +
                                                     " FROM  Doc2M017 M17,  Doc2M010  M10 "  +
                                                  "  WHERE  M17.BarCode  =  M10.BarCode "  +
                                                      " AND  M17.PurchaseNo  =  '"+stringDocNo+"' "  +
                                                      " AND  M10.ComNo  =  '"         +stringComNo+"' ") ;
        if(retDoc5M027.length  >  0) {
            for(int  intNo=0  ;  intNo<retDoc5M027.length  ;  intNo++) {
                if(!"E".equals(retDoc5M027[0][1].trim())){
                    messagebox("����y�{(�д�)�w�ϥΡA���i���ʸ�Ʈw�C") ;
                    return  false ;   
                }
            }
        } 
        // Doc5M030 �ɴڨR�P
        String[][]  retDoc5M030  =  exeFun.getTableDataDoc("SELECT  BarCode,  UNDERGO_WRITE "  +
                                                                                              " FROM  Doc6M010 "  +
                                                  " WHERE  PurchaseNo  =  '"+stringDocNo+"' "  +
                                                       " AND  ComNo  =  '"         +stringComNo+"' ") ;
        if(retDoc5M030.length  >  1) {
            for(int  intNo=0  ;  intNo<retDoc5M030.length  ;  intNo++) {
                if(!"E".equals(retDoc5M030[0][1].trim())){
                    messagebox("����y�{(�ɴڨR�P)�w�ϥΡA���i���ʸ�Ʈw�C") ;
                    return  false ;   
                }
            }
        }
        // 20201110 Kyle : ���אּ��s����y�{��� END
        */
        

        // ��t�D�� �B ���� �B �I�ڸ�T�����
        String      stringBarCodeOld    =  getValue("BarCodeOld").trim( ) ;
        String[][]  retDoc3M013           =  exeFun.getDoc3M013(stringBarCodeOld,  "") ;
        if("X".equals(stringUnderGoWrite)) {
            JOptionPane.showMessageDialog(null,  "�@�o��ơA���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            return  false ;       
        }

        // ñ�֬y�{�ܧ�2016-07-04       
        JTable    jtable10          =  getTable("Table10") ;
        boolean  booleanTable10  =  (jtable10.getRowCount()  >  0) ;  
        Vector     vectorUser          =  new  Vector() ;  vectorUser.add("B3018") ;
        if(booleanTable10)  return  isFlowCheckOKNew(stringUnderGoWrite,  vectorUser,  exeFun,  exeUtil) ;
        
        
        //
        if(stringFlow.indexOf("�ӿ�")!=-1  &&  stringFlow.indexOf("--")==-1)  {
            if("G".equals(stringUnderGoWrite)  ||  "H".equals(stringUnderGoWrite)) {
                JOptionPane.showMessageDialog(null,  "�H�`��ơA���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            /*if("R".equals(stringUnderGoWrite)) {
                JOptionPane.showMessageDialog(null,  "���ʰh���ơA���i���� [�ק�] [�R��] �\��C1",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }*/
            if("B".equals(stringUnderGoWrite)) {
                JOptionPane.showMessageDialog(null,  "�~�ޤwñ�ֹL���i���� [�ק�] [�R��] �\��C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            if(!"A".equals(stringUnderGoWrite)  &&  !"".equals(stringUnderGoWrite.trim())) {
                JOptionPane.showMessageDialog(null,  "["+stringUnderGoWrite+"]�D [�ӿ�] �y�{���i���� [�ק�] [�R��] �\��C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            //
            String  StringEmployeeNo  =  getValue("EmployeeNo").trim( ).toUpperCase() ;
            //
            if(vectorUser.indexOf(getUser())  ==  -1) {
                if(!StringEmployeeNo.equals(getUser().toUpperCase())) {
                    JOptionPane.showMessageDialog(null,  "�� "  + StringEmployeeNo  +  " �إߤ���ơA�䥦�H���ಧ�ʳB�z�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    return  false ;
                }
            }
        } 
        if(stringFlow.indexOf("--�ӿ�")!=-1){
            // I�G�ݤH�`ñ��    O�G�h��(�H�`)    
            if(!"".equals(stringUnderGoWrite)  &&  "I,O,G,H,C,".indexOf(stringUnderGoWrite)==-1) {
                JOptionPane.showMessageDialog(null,  "���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            //
            String  StringEmployeeNo  =  getValue("EmployeeNo").trim( ).toUpperCase() ;
            if(vectorUser.indexOf(getUser().toUpperCase())  ==  -1) {
                if(!StringEmployeeNo.equals(getUser().toUpperCase())) {
                    JOptionPane.showMessageDialog(null,  "�� "  + StringEmployeeNo  +  " �إߤ���ơA�䥦�H���ಧ�ʳB�z�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    return  false ;
                }
            }
        }
        if(stringFlow.indexOf("�~��") !=  -1)   {
            if("G".equals(stringUnderGoWrite)  ||  "H".equals(stringUnderGoWrite)) {
                JOptionPane.showMessageDialog(null,  "�H�`��ơA���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            //  ���\�y�{
            //  A�F��P-�ӿ�   B�G�~��        R�G�h��(����)    Q�G�h��(�H�`)
            // P�G��P�M��     S�G��t�D��
            if("A,B,R,Q,P,S,J,".indexOf(stringUnderGoWrite)  ==  -1) {
                JOptionPane.showMessageDialog(null,  "�������y�{�A���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
        }
        // �H�`
        if(stringFlow.indexOf("�H�`") !=  -1)   {
            // I�G�ݤH�`ñ��    J�G�H�`-�f��
            if("I,J,K,".indexOf(stringUnderGoWrite)   ==  -1) {
                JOptionPane.showMessageDialog(null,  "���i���� [�ק�] �\��C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
        }
        // ��P
        if(stringFlow.indexOf("����") !=  -1)   {
            // �T�� S
            String      stringToday                     =  exeUtil.getDateConvert(getValue("CDate").trim()) ;
            String      stringAssetDate              =  ""+get("ASSET_DATE"); 
            String      stringApply                      =  getValue("ApplyType").trim() ;
            boolean   booleanAssetDate         =  !"".equals(stringAssetDate)  &&  !"null".equals(stringAssetDate)  &&  stringToday.compareTo(stringAssetDate)>=0 ;
            // �T�� E
            if("R".equals(stringUnderGoWrite)) {
                JOptionPane.showMessageDialog(null,  "���ʰh���ơA���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            //  ���\�y�{
            //  B�G�~��        K�G�H�`-�f��   Y�G����    H�G�H�`-�ӿ�
            // S�G��t�D��     C�G����
            if("B,K,C,S,H,Y,".indexOf(stringUnderGoWrite)  ==  -1) {
                JOptionPane.showMessageDialog(null,  "�������y�{�A���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                return  false ;
            }
            if("B3018".equals(getUser()))  booleanAssetDate  =  true ;
            if(booleanAssetDate  &&  ",P83219,E35990,J29564,P83218,".indexOf(","+stringBarCodeOld+",")==-1){  
              //20180309 ���ˬd�T�w�겣 By B03812
              /*
                // �T��
                if("D".equals(stringApply)) {
                  String[][]  retDoc1M040  =  exeFun.getTableDataDoc("SELECT  Barcode "  +
                                                               " FROM  Doc1M040 "  +
                                                             " WHERE  Barcode  =  '"+stringBarCodeOld+"' "+
                                                                " AND  DepartNo  LIKE  '023%' "+
                                                                " AND  DocStatus  =  '4' ") ;
                    if(retDoc1M040.length  ==  0) {
                        messagebox("[�T�w�겣]���ʳ�A[�H�`] ����l�ܩ|���X��A���i���ʸ�Ʈw�C") ;
                        return  false ;
                    }
                }
                */
            }
        }
        return  true ;
    }
    public  boolean  isFlowCheckOKNew(String  stringUnderGoWrite,  Vector  vectorUser,  Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        JTable      jtable10                            =  getTable("Table10") ;
        String      stringCheckDate10           =  "" ;
        String      stringUndergoType10         =  "" ;
        String      stringUndergoType10Next   =  "" ;
        String      stringCheckEmployeeNo10   =  "" ;
        String      stringUndergoWrite10            =  "" ;
        String      stringUndergoName10           =  "" ;
        String      stringFlow                            =  getFunctionName() ;
        String      stringUser                  = getUser().toUpperCase() ;
        String      stringApply                             =  getValue("ApplyType").trim() ;
        String      stringBarCodeOld                  =  getValue("BarCodeOld").trim( ) ;
        String      StringEmployeeNo            =  getValue("EmployeeNo").trim( ).toUpperCase() ;
        String      stringUndergoWirteCheck       =  ""+get("Doc3M011_UNDERGO_CHECK_L") ; 
        boolean   booleanUndergoWriteCheck  =  "Y".equals(stringUndergoWirteCheck) ;
        //
        if(",Q07511,Q81614,Q81619,Q14353,Q57670,Q81747,Q57711,Q14387,Q81146,Q81748,".indexOf(","+stringBarCodeOld+",")  !=  -1)  return  true ;
        if("Y".equals(stringUnderGoWrite)) {
            String  stringTemp  =  "" ;
            if("Y".equals(stringUnderGoWrite))  stringTemp  =  "�w����ñ�֬y�{" ;
            JOptionPane.showMessageDialog(null,  stringTemp+"�A���i���ʸ�Ʈw�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            return  false ;       
        }
        //
        for(int  intNo=0  ;  intNo<jtable10.getRowCount()  ;  intNo++) {
            stringCheckDate10             =  (""+getValueAt("Table10",  intNo,  "CheckDate")).trim() ;
            stringUndergoType10       =  (""+getValueAt("Table10",  intNo,  "UNDERGO_TYPE")).trim() ;
            stringCheckEmployeeNo10  =  (""+getValueAt("Table10",  intNo,  "CheckEmployeeNo")).trim() ;
            stringUndergoWrite10          =  (""+getValueAt("Table10",  intNo,  "UNDERGO_WRITE")).trim() ;
            stringUndergoName10          =  (""+getValueAt("Table10",  intNo,  "UNDERGO_NAME")).trim() ;
            if("".equals(stringCheckDate10)) {
                stringUndergoType10Next  =  stringUndergoWrite10 ;
                break ;
            }
        }
        String  stringCheckEmployeeNoSign10  =  "" ;
        for(int  intNo=jtable10.getRowCount()-1  ;  intNo>=0  ;  intNo--) {
            stringCheckDate10               =  (""+getValueAt("Table10",  intNo,  "CheckDate")).trim() ;
            stringUndergoType10         =  (""+getValueAt("Table10",  intNo,  "UNDERGO_TYPE")).trim() ;
            stringCheckEmployeeNo10     =  (""+getValueAt("Table10",  intNo,  "CheckEmployeeNo")).trim() ;
            stringCheckEmployeeNoSign10   =  (""+getValueAt("Table10",  intNo,  "CheckEmployeeNoSign")).trim() ;
            stringUndergoWrite10              =  (""+getValueAt("Table10",  intNo,  "UNDERGO_WRITE")).trim() ;
            stringUndergoName10             =  (""+getValueAt("Table10",  intNo,  "UNDERGO_NAME")).trim() ;
            if(!"".equals(stringCheckDate10)) {
                if("55".equals(stringUndergoWrite10)  &&  stringCheckEmployeeNoSign10.equals(getUser().toUpperCase())) {
                    continue ;
                } else {
                    break ;
                }
            }
        }
        // �ӿ�10����20����30�������t�D��40�����ʫ�(2)50,60���`�޲z�B70���س]���ƪ�80�����θ��ƪ�90
        String   stringToday            =  datetime.getToday("YYYY/mm/dd") ;
        if(stringFlow.indexOf("�ӿ�")!=-1  &&  stringFlow.indexOf("--") ==  -1)   {
            if("G".equals(stringUnderGoWrite)  ||  "H".equals(stringUnderGoWrite)) {
                messagebox("�H�`��ơA���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            /*if("R".equals(stringUnderGoWrite)) {
                messagebox("���ʰh���ơA���i���� [�ק�] [�R��] �\��C") ;
                return  false ;
            }*/
            // A �w�e�e ���i���� [�ק�] [�R��] �\��C
            if("M".equals(stringUnderGoWrite)) {
                messagebox("�w�e�e ���i���� [�ק�] [�R��] �\��C") ;
                return  false ;             
            }
            if("B".equals(stringUnderGoWrite)) {
                messagebox("�~�ޤwñ�ֹL���i���� [�ק�] [�R��] �\��C") ;
                return  false ;
            }
            if(!"10".equals(stringUndergoWrite10)  &&  !"10".equals(stringUndergoType10Next)) { 
                messagebox("�wñ�֦� "+stringUndergoName10+" �y�{�A���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            //
            if(vectorUser.indexOf(getUser())  ==  -1) {
                if(!StringEmployeeNo.equals(getUser().toUpperCase())) {
                    messagebox("�� "  + StringEmployeeNo  +  " �إߤ���ơA�䥦�H���ಧ�ʳB�z�C") ;
                    return  false ;
                }
            }
        }
        if(stringFlow.indexOf("--�ӿ�")  !=  -1)  {
            if(!"10".equals(stringUndergoWrite10)  &&  !"10".equals(stringUndergoType10Next)) { 
                messagebox("�wñ�֦� "+stringUndergoName10+" �y�{�A���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            //
            if(vectorUser.indexOf(getUser().toUpperCase())  ==  -1) {
                if(!StringEmployeeNo.equals(getUser().toUpperCase())) {
                    messagebox("�� "  + StringEmployeeNo  +  " �إߤ���ơA�䥦�H���ಧ�ʳB�z�C") ;
                    return  false ;
                }
            }
        }

        if(stringFlow.indexOf("�~��") !=  -1)   {
            if("G".equals(stringUnderGoWrite)  ||  "H".equals(stringUnderGoWrite)) {
                messagebox("�H�`��ơA���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            if("A".equals(stringUnderGoWrite)) {
                messagebox("�ӿ�|���e�e��ơA���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            if(!"10".equals(stringUndergoWrite10)  &&  !"10".equals(stringUndergoType10Next)) { 
                messagebox("�wñ�֦� "+stringUndergoName10+" �y�{�A���i���ʸ�Ʈw�C") ;
                return  false ;
            }
        }
        
        
        // ����
        if(stringFlow.indexOf("����") !=  -1)   {
            if("R".equals(stringUnderGoWrite)) {
                messagebox("�h���ơA���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            if(!booleanUndergoWriteCheck  &&  "C".equals(stringUnderGoWrite)) {
                messagebox("�w�e�e��ơA���i���ʸ�Ʈw�C") ;
                return  false ;
            }
            // �T��
            if(!"50".equals(stringUndergoWrite10)  &&  !"50".equals(stringUndergoType10Next)) { 
                if(exeUtil.doParseDouble(stringUndergoWrite10)  <  40) {
                    messagebox("[�@����]�����ʳ�|��ñ�֦� [�����t�D��]�A���i���ʸ�Ʈw�C") ;
                    return  false ;
                } else {
                    messagebox("�wñ�֦� "+stringUndergoName10+" �y�{�A���i���ʸ�Ʈw�C") ;
                    return  false ;
                }
            }
            //2018/03/09 ���ˬd�T��P�_  by B03812
            /*
            if("D".equals(stringApply)) {
              String[][]  retDoc1M040  =  exeFun.getTableDataDoc("SELECT  Barcode "  +
                                                           " FROM  Doc1M040 "  +
                                                         " WHERE  Barcode  =  '"+stringBarCodeOld+"' "+
                                                            " AND  DepartNo  LIKE  '023%' "+
                                                            " AND  DocStatus  =  '4' ") ;
                if(retDoc1M040.length  ==  0) {
                    messagebox("[�T�w�겣]���ʳ�A[�H�`] ����l�ܩ|���X��A���i���ʸ�Ʈw�C") ;
                    return  false ;
                }
            } 
            */
        }
        return  true ;
    }
    
    
    
    
    public  void  setFlowData(FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTable jtable10  =  getTable("Table10") ;
        if(jtable10.getRowCount()  >  0)  {
            setFlowDataNew(exeUtil,  exeFun) ;
            return ;
        }
        //
        String      stringFlow                  =  getFunctionName() ;
        String      stringApply                =  getValue("ApplyType").trim() ;
        
        //20180204 �����Τ@�y�{����
        if("".equals(getValue("PurchaseEmployeeNo").trim()))   setValue("PurchaseEmployeeNo",     getUser().toUpperCase()) ;
        setValue("UNDERGO_WRITE",          "Y") ;
        setValue("FlowEndDate",                  datetime.getToday("YYYY/mm/dd")) ;

        //
        if(stringFlow.indexOf("�ӿ�")!=-1)  {
            setValue("UNDERGO_WRITE",  "A") ;
        } 
        if(stringFlow.indexOf("--�ӿ�")!=-1)  {
            // I�G�ݤH�`ñ��    O�G�h��(�H�`)    
            if(getValue("DocNo1").indexOf("033FZ")  !=  -1) {
                setValue("UNDERGO_WRITE",  "I") ;  // �H�`��ñ��
            } else {
                setValue("UNDERGO_WRITE",  "H") ;  // �ܦ���@�y�{
            }
        }
        if(stringFlow.indexOf("�~��") !=  -1)   {
            if(getValue("DocNo1").indexOf("033FZ")  !=  -1) {
                setValue("UNDERGO_WRITE",  "J") ;  // �H�`��ñ��
            } else {
                setValue("UNDERGO_WRITE",  "B") ;
            }
        }
        // �H�`
        if(stringFlow.indexOf("�H�`") !=  -1)   {
            setValue("UNDERGO_WRITE",  "K") ;
        }
        // ��P
        if(stringFlow.indexOf("����") !=  -1)   {
            if("".equals(getValue("PurchaseEmployeeNo").trim()))   setValue("PurchaseEmployeeNo",     getUser().toUpperCase()) ;
            setValue("UNDERGO_WRITE",          "Y") ;
            setValue("FlowEndDate",                  datetime.getToday("YYYY/mm/dd")) ;
        }
    }
    public  void  setFlowDataNew(FargloryUtil  exeUtil,   Doc2M010  exeFun) throws  Throwable {
        int         intPos                        =  -1 ;
        JTable        jtable10                      =  getTable("Table10") ;
        String        stringCheckDate               =  "" ;
        String        stringUndergoWrite            =  "" ;
        String        stringSql                     =  "" ;
        String        stringCheckEmployeeNo       =  "" ;
        String        stringUndergoType             =  "" ;
        String        stringDeptCd                =  "" ;
        String        stringEmployeeNo              =  "" ;
        String        stringUser                  =  getUser().toUpperCase() ;
        String      stringPurchaseEmployeeNo      =  getValue("PurchaseEmployeeNo").trim() ;
        String          stringFlow                          =  getFunctionName() ; 
        String        stringTodayTime             =  datetime.getTime("YYYY/mm/dd h:m:s") ;
        String        stringUndergoWirteCheck          =  ""+get("Doc3M011_UNDERGO_CHECK_L") ; 
        Hashtable     hashtableData                 =  new  Hashtable() ;
        boolean     booleanEndFlow              =  true ;
        boolean     booleanUndergoWriteCheck    =  "Y".equals(stringUndergoWirteCheck) ;
        //
        if(stringFlow.indexOf("����") !=  -1)   {
            if("".equals(stringPurchaseEmployeeNo))    setValue("PurchaseEmployeeNo",     stringUser) ;
        }
        // ���oñ�֤H�������
        stringDeptCd              =  exeFun.getNameUnionFE3D("DEPT_CD",  "FE3D05",  " AND  EMP_NO  =  '"+stringUser+"' ",  new  Hashtable(),  exeUtil) ;  
        for(int  intNo=0  ;  intNo<jtable10.getRowCount()  ;  intNo++) {
            stringCheckDate                 =  (""+getValueAt("Table10",  intNo,  "CheckDate")).trim() ;
            stringCheckEmployeeNo         =  (""+getValueAt("Table10",  intNo,  "CheckEmployeeNo")).trim() ;
            stringUndergoType                  =  (""+getValueAt("Table10",  intNo,  "UNDERGO_TYPE")).trim() ;
            stringUndergoWrite          =  (""+getValueAt("Table10",  intNo,  "UNDERGO_WRITE")).trim() ;
            //
            // �P�_�O�_�����y�{
            if("".equals(stringCheckDate)) {
                if("A".equals(stringUndergoType)) {
                    if(stringFlow.indexOf("����") ==  -1 && !stringCheckEmployeeNo.equals(stringUser)) {
                      booleanEndFlow  =  false ;
                    }
                } else {
                    if(!stringDeptCd.startsWith(stringCheckEmployeeNo))booleanEndFlow  =  false ;
                    if(stringUser.equalsIgnoreCase("B9034")) {
                      booleanEndFlow = true;
                    }
                }
            }
            //
            if(stringFlow.indexOf("�ӿ�")!=-1)  {
                if("10".equals(stringUndergoWrite)) {
                    intPos  =  intNo ;
                }
                stringEmployeeNo  =  getValue("OriEmployeeNo").trim() ;
            } else if(stringFlow.indexOf("�~��")  !=  -1) {
                if("10".equals(stringUndergoWrite)) {
                    intPos  =  intNo ;
                }
                stringEmployeeNo  =  getValue("OriEmployeeNo").trim() ;
            } else {
                if("50".equals(stringUndergoWrite)) {
                    intPos  =  intNo ;
                }
                stringEmployeeNo  =  stringPurchaseEmployeeNo ;
            }
        }
        // �]�wñ�֬y�{���
        // ���� �B ���ʫD�e�e �ɤ��@���B�z
        if(stringFlow.indexOf("����")==-1  ||  booleanUndergoWriteCheck) {
            setValueAt("Table10",  stringTodayTime,       intPos,  "CheckDate") ;
            setValueAt("Table10",  stringEmployeeNo,    intPos,  "CheckEmployeeNoSign") ;
        }
        // �]�w�D��椧ñ�֪��A
        if(stringFlow.indexOf("--�ӿ�")!=-1)  {
            setValue("UNDERGO_WRITE",  "H") ;  // �ܦ���@�y�{
        } else if(stringFlow.indexOf("�ӿ�")!=-1)   {
            setValue("UNDERGO_WRITE",  "Y".equals(stringUndergoWirteCheck) ? "M" : "A") ;
            
        } else if(stringFlow.indexOf("�~��") !=  -1)  {
            setValue("UNDERGO_WRITE",  "B") ;
        } else {
            // ���ʰe�e����
            String    stringUndergoWirteL         =   "" ;
            if("Y".equals(stringUndergoWirteCheck))  {
                //stringUndergoWirteL  =  "C" ;
                stringUndergoWirteL  =  "Y" ;
                if(booleanEndFlow) {
                    stringUndergoWirteL  =  "Y" ;
                }
            } else {
                stringUndergoWirteL  =  booleanEndFlow  ?  "Y"  :  "H" ;
            }
            setValue("UNDERGO_WRITE",        stringUndergoWirteL) ;  
        }
        // �P�_�O�_�����y�{
        if(booleanEndFlow) {
            setValue("FlowEndDate",                 datetime.getToday("YYYY/mm/dd")) ;
        }
        //20180426 �L���ʬ�D�ެy�{
        /*
        // ���ʬ�D�޳B�z
        doSetPurchaseSupervisor(booleanUndergoWriteCheck,  stringDeptCd,  stringTodayTime,  exeUtil,   exeFun) ;
        */
        // �h�󤧩ӿ�ñ�֮ɡA����ñ�֦ܸӰh��̫e�@���d
        doSetBackFlowData(stringTodayTime,  exeUtil,   exeFun) ;
    }
    public  void  doSetPurchaseSupervisor(boolean  booleanUndergoWriteCheck,  String  stringDeptCd,  String  stringTodayTime,  FargloryUtil  exeUtil,   Doc2M010  exeFun) throws  Throwable {
        if(!stringDeptCd.startsWith("021"))  return   ;
        if(",OO,CS,".indexOf(","+getValue("ComNo").trim()+",")  !=  -1)  return ;
        // �ӿ�H������D��
        String    stringPurchaseSupervisor              =  exeFun.getNameUnionFE3D("CENSOR_EMP_NO",  "FE3D110",  " AND  DEPT_CD  =  '"+stringDeptCd+"' AND  ITEM  =  '2' ",  new  Hashtable(),  exeUtil) ;          if("".equals(stringPurchaseSupervisor))  return ;
        boolean booleanPurchaseSupervisor      =  stringPurchaseSupervisor.equals(getUser().toUpperCase())   ;
        //
        String    stringTable            =  "Table10" ;
        JTable      jtable                  =  getTable(stringTable) ;
        String    stringRecordNo        =  "" ;
        String      stringUndergoWrite      =  "" ;
        for(int  intNo=0  ;  intNo<jtable.getRowCount()  ;  intNo++) {
            stringUndergoWrite        =  (""+getValueAt(stringTable,  intNo,  "UNDERGO_WRITE")).trim() ;
            stringRecordNo          =  (""+getValueAt(stringTable,  intNo,  "RecordNo")).trim() ;
            //
            if("55".equals(stringUndergoWrite)) {
                // �s�bñ�����
                setValueAt(stringTable,  stringPurchaseSupervisor,  intNo,  "CheckEmployeeNo") ;
                if(booleanPurchaseSupervisor  &&  booleanUndergoWriteCheck) {
                    setValueAt(stringTable,  stringTodayTime,          intNo,  "CheckDate") ;
                    setValueAt(stringTable,  stringPurchaseSupervisor, intNo,  "CheckEmployeeNoSign") ;
                }
                return ;
            }
            if(exeUtil.doParseDouble(stringUndergoWrite)  >  55) {
                break ;
            }
        }
        Hashtable  hashtableData  =  new  Hashtable() ;
        // ���s�bñ�����
        hashtableData.put("ID",                   getValue("ID").trim()) ;
        hashtableData.put("RecordNo",             stringRecordNo) ;
        hashtableData.put("SourceType",                 getFunctionName().indexOf("Doc3")!=-1?"A" : "B") ;
        hashtableData.put("BarCode",                getValue("BarCode").trim()) ;
        hashtableData.put("UNDERGO_WRITE",    "55") ;
        hashtableData.put("UNDERGO_NAME",       "���ʬ�D��") ;
        hashtableData.put("UNDERGO_KindNo",     "�f") ;
        hashtableData.put("UNDERGO_TYPE",       "A") ;
        hashtableData.put("CheckEmployeeNo",      stringPurchaseSupervisor) ;
        hashtableData.put("CheckEmployeeNoSign",  booleanPurchaseSupervisor?stringPurchaseSupervisor:"") ;
        hashtableData.put("CheckDate",              booleanPurchaseSupervisor?stringTodayTime:"") ;       
        hashtableData.put("Descript",                       "") ;
        String  stringSql  =  exeFun.doInsertDBDoc("Doc5M0181",  hashtableData,  false,  exeUtil) ;System.out.println(""+stringSql) ;addToTransaction(stringSql) ;
        System.out.println("�s�W����ñ�֬�D��------------------------------------------"+stringSql) ;
    }
    // �h�󤧷~��ñ�֮ɡA����ñ�֦ܸӰh��̫e�@���d
    public  void  doSetBackFlowData(String  stringTodayTime,  FargloryUtil  exeUtil,   Doc2M010  exeFun) throws  Throwable {
        //System.out.println("�h�󤧩ӿ�ñ�֮ɡA����ñ�֦ܸӰh��̫e�@���d doSetBackFlowData------------------------------------------S") ;
        if(getFunctionName().indexOf("�~��")  ==  -1)                   return ;      // �D�~�ޡA�h�󤣳B�z
        // �����ʳ椧�e���h��O��
        String        stringBarCode             =  getValue("BarCode").trim() ;
        Vector        vectorDoc5M0182             =  exeFun.getQueryDataHashtableDoc("Doc5M0182",  new  Hashtable(),  " AND BarCode  =  '"+stringBarCode+"' ORDER BY  CheckDate DESC ",  new  Vector(),  exeUtil) ;if(vectorDoc5M0182.size()  ==  0)  return ;
        String        stringCheckEmployeeNo       =  "" ;
        String      stringUndergoName       =  "" ;
        String        stringUndergoWrite          =  "" ;
        String        stringUndergoWriteCheck   =  "" ;
        String        stringCheckStatus         =  "" ;
        Hashtable     hashtableCheckEmployeeNo  =  new  Hashtable() ;
        for(int  intNo=0  ;  intNo<vectorDoc5M0182.size()  ;  intNo++) {
            stringCheckEmployeeNo     =  exeUtil.getVectorFieldValue(vectorDoc5M0182,  intNo,  "CheckEmployeeNo") ;
            stringUndergoName         =  exeUtil.getVectorFieldValue(vectorDoc5M0182,  intNo,  "UNDERGO_NAME") ;
            stringUndergoWrite          =  exeUtil.getVectorFieldValue(vectorDoc5M0182,  intNo,  "UNDERGO_WRITE") ;   if("null".equals(stringUndergoWrite)) stringUndergoWrite  =  "" ;
            stringCheckStatus         =  exeUtil.getVectorFieldValue(vectorDoc5M0182,  intNo,  "CheckStatus") ;
            // ñ�����d�N�X
            if("".equals(stringUndergoWrite)) {
                stringUndergoWrite    =  getUndergoWrite(stringUndergoName,  exeUtil) ; 
            }
            // ñ�����d�N�X vs ñ�֤H��
            hashtableCheckEmployeeNo.put(stringUndergoWrite,  stringCheckEmployeeNo) ;
            //System.out.println(intNo+"-----ñ�����d�N�X vs ñ�֤H��-------------------------------------1") ;
            //
            if("�ӿ�".equals(stringUndergoName))                continue ;
            if(exeUtil.doParseDouble(stringUndergoWrite)  <=  20)     break ;
            // �h��
            if(!"N".equals(stringCheckStatus))  continue ;
            // �O���h�����d
            stringUndergoWriteCheck  =  stringUndergoWrite ;
            //System.out.println(intNo+"-----�h�����d("+stringUndergoWriteCheck+")-------------------------------------2") ;
        }
        if("".equals(stringUndergoWriteCheck))  return ;
        //
        JTable        jtable10                    =  getTable("Table10") ;
        String        stringCheckDateL          =  "" ;
        String        stringUndergoWriteL       =  "" ;
        String        stringUndergoWrite2L        =  "" ;
        String        stringCheckEmployeeNoSign  =  "" ;
        for(int  intNo=0  ;  intNo<jtable10.getRowCount()  ;  intNo++) {
            stringUndergoWriteL             =  (""+getValueAt("Table10",  intNo,  "UNDERGO_WRITE")).trim() ;
            stringCheckDateL              =  (""+getValueAt("Table10",  intNo,  "CheckDate")).trim() ;
            stringCheckEmployeeNo           =  (""+getValueAt("Table10",  intNo,  "CheckEmployeeNo")).trim() ;
            stringUndergoWrite2L            =  exeUtil.doSubstring(stringUndergoWriteL,  0  ,  2) ;
            // ���e���d������ñ�֤H��
            stringCheckEmployeeNoSign     =  ""+hashtableCheckEmployeeNo.get(stringUndergoWriteL) ;
            if("null".equals(stringCheckEmployeeNoSign)) {
                stringCheckEmployeeNoSign     =  ""+hashtableCheckEmployeeNo.get(stringUndergoWrite2L) ;
            }
            if("null".equals(stringCheckEmployeeNoSign))   stringCheckEmployeeNoSign  =  "" ;
            if("".equals(stringCheckEmployeeNoSign)) {
                stringCheckEmployeeNoSign  =  convert.StringToken(stringCheckEmployeeNo,  ",")[0].trim() ;
            } else if(stringCheckEmployeeNo.indexOf(stringCheckEmployeeNoSign)  ==  -1) {
                stringCheckEmployeeNoSign  =  convert.StringToken(stringCheckEmployeeNo,  ",")[0].trim() ;
            }
            //System.out.println(intNo+"-----���e���d("+stringUndergoWriteL+")������ñ�֤H��--("+stringCheckEmployeeNoSign+")-------------------------------------0") ;
            // �w��ñ�ָ�Ƥ��B�z
            if(!"".equals(stringCheckDateL))  continue ;
            //System.out.println(intNo+"-----�w��ñ�ָ�Ƥ��B�z-------------------------------------1") ;
            // �P�_�O�_�O�h����
            if(stringUndergoWriteCheck.length()  >  2) {
                //System.out.println(intNo+"-----�P�_�O�_�O�h����-------------------------------------2-1") ;
                if(stringUndergoWriteCheck.equals(stringUndergoWriteL))  break ;
                //System.out.println(intNo+"-----�P�_�O�_�O�h����-------------------------------------2-2") ;
            } else {
                //System.out.println(intNo+"-----�P�_�O�_�O�h����-------------------------------------3-1") ;
                if(stringUndergoWriteCheck.equals(stringUndergoWrite2L))  break ;
                //System.out.println(intNo+"-----("+stringUndergoWriteCheck+")("+stringUndergoWrite2L+")�P�_�O�_�O�h����-------------------------------------3-2") ;
            }
            // ���ʩӿ줧�ᤣ�@�B�z
            if(exeUtil.doParseDouble(stringUndergoWrite2L)  >=  50)  break ;
            //System.out.println(intNo+"-----���ʩӿ줧�ᤣ�@�B�z-------------------------------------4") ;
            // �ٲ��y�{
            setValueAt("Table10",  stringTodayTime,             intNo,  "CheckDate") ;
            setValueAt("Table10",  stringCheckEmployeeNoSign,  intNo,  "CheckEmployeeNoSign") ;
        }
        getButton("ButtonSynDocFlow").doClick() ;
        //System.out.println("�h�󤧩ӿ�ñ�֮ɡA����ñ�֦ܸӰh��̫e�@���d doSetBackFlowData------------------------------------------E") ;
    }
    public  String  getUndergoWrite(String  stringUndergoName,  FargloryUtil  exeUtil) throws  Throwable {
        if("�ӿ��D��".equals(stringUndergoName))         return  "20" ;
        if("��P�����ǩӿ�".equals(stringUndergoName))     return  "32-01" ;
        if("��P�����ǥD��".equals(stringUndergoName))     return  "32-02" ;
        if("��P������".equals(stringUndergoName))         return  "33" ;
        if("��P�޲z�B".equals(stringUndergoName))         return  "34" ;
        if("�����t�D��".equals(stringUndergoName))      return  "40" ;
        if("�]�p�ʲz".equals(stringUndergoName))            return  "42" ;
        if("��T�ǥD��".equals(stringUndergoName))         return  "43" ;
        if("�H�`���`�ȥD��".equals(stringUndergoName))     return  "46" ;
        if("���ʫǩӿ�".equals(stringUndergoName))         return  "50" ;
        return  "" ;
    }
    public  void  doSyncBarCode( ) throws  Throwable {
        String   stringBarCode        =  getValue("BarCode").trim( ) ;
        String   stringBarCodeT     =  "" ;
        JTable  jtable                     =  null ;
        //   1-3,6
        //  Table1    ���ʶ���                  Table2    �I�ڸ�T                    Table3    �קO���u
        //  Table4    �ϥΪ��A                        Table5    �L                                     Table6    ���ʸ�T���קO���u
        //  Table7    033FG �קO�Ӷ�����      Table8    033FG ñ�e����              Table9    ���ʸ�T��POP��T         
        //  Table10  ���ʳ�-ñ�֬y�{             Table11  ���ʳ�-�y�{�O��             Table12   ����
        //  Table13  �L                                    Table14  �L                                    Table15  �L
        //  Table16 ���ʳ�-�N�P�X���Ƭd    Table17 ĳ����T���        Table18 �t��ĳ����T���
        for(int  intTableNo=1  ;  intTableNo<=18  ;  intTableNo++) {
            if(intTableNo==4)    continue ;
            if(intTableNo==5)    continue ;
            if(intTableNo==13)  continue ;
            if(intTableNo==14)  continue ;
            if(intTableNo==15)  continue ;
            jtable  =  getTable("Table"+intTableNo) ;
            for(int  intNo=0  ;  intNo<jtable.getRowCount()  ;  intNo++) {
                   stringBarCodeT  =  (""+getValueAt("Table"+intTableNo,  intNo,  "BarCode")).trim() ;
                setValueAt("Table"+intTableNo,  stringBarCode,  intNo,  "BarCode") ;
            }
        }
    }
      // �e�ݸ���ˮ֡A���T�^�� True
      public  boolean  isBatchCheckOK(String  stringFunction,  Doc.Doc2M010  exeFun,  Farglory.util.FargloryUtil  exeUtil)throws  Throwable {
         setValue("DocNo1",  getValue("DepartNo").trim()) ;
         // 
         String       stringFlow                          =  getFunctionName() ;
         String       stringBarCode                   =  getValue("BarCode").trim( ) ;
         String       stringBarCodeOld            =  getValue("BarCodeOld").trim( ) ;
         String[][]   retDoc1M040                   =  exeFun.getDoc1M040(stringBarCodeOld) ;
         String     stringUndergoWirteCheck   =  ""+get("Doc3M011_UNDERGO_CHECK_L") ; 
         boolean  booleanUndergoWriteCheck  =  stringFlow.indexOf("����")==-1  ||
                                            "Y".equals(stringUndergoWirteCheck)  ;
         // �N�P�X��
        put("Doc7M02691_STATUS",  "DB") ;
        getButton("ButtonTable16").doClick() ;
        if(booleanUndergoWriteCheck  &&  !"OK".equals(""+get("Doc7M02691_STATUS"))) {
            messagebox("�п�J �N�P�X�� ���") ;
            return  false ;
        }
         // �S��w�ⱱ�� ����H�� 
         String     stringSpecBudget       =  ","+get("SPEC_BUDGET")+"," ;
         String     stringEmployeeNo         =  getValue("EmployeeNo").trim() ;
         String     stringDocNo1                =  getValue("DocNo1").trim() ;
         String     stringFunctionType     =  exeFun.get033FGFunctionType (stringDocNo1,  exeUtil) ;
         if(booleanUndergoWriteCheck  &&
            stringSpecBudget.indexOf(","+stringDocNo1+",")!=-1  &&  exeFun.getTableDataDoc("SELECT  EmployeeNo "  +
                                                                                                                                                        " FROM  Doc3M011_EmployeeNo "  +
                                                                               " WHERE  FunctionType  =  '"+stringFunctionType+"' "  +
                                                                                      " AND  EmployeeNo  =  '"+stringEmployeeNo+"' ").length  <=  0) {
              messagebox("�D�S��H�������\�ӽ� "+stringDocNo1+" �O�ΡC\n(�����D�Ь� [��P������])") ;
            return  false ;
         }
         if(stringSpecBudget.indexOf(","+stringDocNo1+",")!=-1  &&  getTable("Table7").getRowCount()  ==  0) {
              getButton("Button033FGInput").doClick() ;
         }
         //
         if(stringFlow.indexOf("�H�`")  !=  -1)  return  true ;
        // ���q�N�X
        String  stringComNo  =  getValue("ComNo").trim( ) ;
        if("".equals(stringComNo)) {
            JOptionPane.showMessageDialog(null,  "[���q�N�X] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            getcLabel("ComNo").requestFocus( ) ;
            return  false ;       
        }
        /*
        if(!"A".equals(exeFun.getUseType(stringComNo,  ""))) {
            messagebox("���q "  +  stringComNo  +  "("+exeFun.getCompanyName(stringComNo)+") �����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
            getcLabel("ComNo").requestFocus( ) ;
            return  false ;       
        }
        */

         // ����s�X
         String       stringPurchaseNo1         =  getValue("DocNo1").trim( ) ;
         String       stringPurchaseNo2         =  getValue("DocNo2").trim( ) ;
         String       stringPurchaseNo3         =  getValue("DocNo3").trim( ) ;
         String       stringKindNo                    =  getValue("KindNo").trim( ) ;
         String       stringDepartNoSubject   =  ""+get("EMP_DEPT_CD") ;
         String       stringPurchaseNo            =  stringPurchaseNo1+stringPurchaseNo2+stringPurchaseNo3 ;
         //
         if("".equals(stringPurchaseNo1)) {
            JOptionPane.showMessageDialog(null,  "[���ʳ渹1] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            getcLabel("DocNo1").requestFocus( ) ;
            return  false ;
         }
         if("".equals(stringPurchaseNo2)) {
            JOptionPane.showMessageDialog(null,  "[���ʳ渹2] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            getcLabel("DocNo2").requestFocus( ) ;
            return  false ;
         }
        // ���ʳ椧����ˮ�
        String  retDateRoc  =  exeUtil.getDateFullRoc (stringPurchaseNo2+"01",  "12345678") ;
        if(retDateRoc.length( )  !=  9) {
            JOptionPane.showMessageDialog(null,  "[���ʳ渹2] �榡���~(yymm)�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            getcLabel("DocNo2").requestFocus( ) ;
            return  false ;
        }
        retDateRoc              =  exeUtil.getDateConvertRoc(retDateRoc).replaceAll("/","") ;
        stringPurchaseNo2  =  datetime.getYear(retDateRoc)+datetime.getMonth(retDateRoc) ;
        if( "�s�W".equals(stringFunction)  &&  "0231,".indexOf(stringDepartNoSubject+",")!=-1) {
            // �۰ʵ���
            stringPurchaseNo3        =  exeFun.getDocNo3Max(stringComNo,  stringKindNo,  stringPurchaseNo1,  stringPurchaseNo2,  stringPurchaseNo1.startsWith("023")?"B":"A") ;
            stringPurchaseNo          =  stringPurchaseNo1+stringPurchaseNo2+stringPurchaseNo3 ;
            setValue("DocNo3",  stringPurchaseNo3) ;
        } else {
             if("".equals(stringPurchaseNo3)) {
                JOptionPane.showMessageDialog(null,  "[���ʳ渹3] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                getcLabel("DocNo3").requestFocus( ) ;
                return  false ;
             }
             // ����
             System.out.println("BarCode Check ======> Old:"+stringBarCodeOld+" :::::::::::::::::::::::::::::: New:"+stringBarCode+" <================ END");
            boolean  booleanFlag  =  exeFun.isExistDocNoCheck(stringPurchaseNo1,  stringPurchaseNo2,  stringPurchaseNo3,  stringKindNo,  stringComNo,  stringBarCodeOld) ;
             if(!booleanFlag) {
                JOptionPane.showMessageDialog(null,  "[���ʳ渹] �o�ͭ��СC1",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                getcLabel("DocNo3").requestFocus( ) ;
                return  false ;
             }
         }
        // �H���X�s���ˮ֤���s���O�_�s�b�A�B�@�P
        String      stringDocNoOld  =  getValue("DocNoOld").trim( ) ;
        String[][]  retDoc1M030     =  exeFun.getDoc1M030(stringBarCodeOld) ;
        boolean  booleanNoUser  =  exeFun.isNoUseDoc1M040(stringBarCodeOld) ;
         if(retDoc1M030.length>0  &&  "5".equals(retDoc1M030[0][6].trim())) {
            messagebox("����l�ܨt�Τ��A�����ʳ�w [�@�o]�A�����\����C") ;
            return  false ;
         }
        if(retDoc1M030.length  >  0  &&  !booleanNoUser) {
            if(!stringKindNo.equals(retDoc1M030[0][5].trim())) {
                messagebox("[�������O] ���@�P�A�Ь� [��T������] �B�z�C ") ;
                return  false ;
            }
            if(!stringDocNoOld.equals(retDoc1M030[0][2].trim()+retDoc1M030[0][3].trim()+retDoc1M030[0][4].trim())) {
                messagebox("[����N�X] ���@�P�A�Ь� [��T������] �B�z�C "+stringDocNoOld) ;
                return  false ;
            }
        }
         setValue("DocNo", stringPurchaseNo) ;
        if("�R��".equals(stringFunction)) {
            if(retDoc1M040.length  >  1) {
                messagebox(" ���ʳ�w�����o��A�����\�R���A�Ь� [��T������]�C") ;
                return  false ;
            }
            if(retDoc1M040.length==1  &&  !"1".equals(retDoc1M040[0][6].trim())) {
                messagebox(" ���ʳ�D�Ф�A�����\�R���A�Ь� [��T������]�C") ;
                return  false ;
            }
            return  true ;
        }
        if(!"".equals(stringBarCode)  &&  !"".equals(stringBarCodeOld)  &&  !stringBarCode.equals(stringBarCodeOld)) {
            if(stringFlow.indexOf("�ӿ�")  !=  -1) {
                messagebox(" ���ʳ椣���\�ܧ���X�s���A�Ь� [��T������]�C") ;
                return  false ;
            }
            if(booleanNoUser  &&  retDoc1M040.length  >  1) {
                messagebox(" ���ʳ�w�����o��A�����\�ܧ���X�s���A�Ь� [��T������]�C") ;
                return  false ;
            }
            if(booleanNoUser  &&  retDoc1M040.length==1  &&  !"1".equals(retDoc1M040[0][6].trim())) {
                messagebox(" ���ʳ�D�Ф�A�����\�ܧ���X�s���A�Ь� [��T������]�C") ;
                return  false ;
            }
        }
        // ���X���i�ť�
        boolean     booleanFlow             =  !(stringFlow.indexOf("�ӿ�")  !=  -1  ||  stringFlow.indexOf("�~��")  !=  -1    ||  stringFlow.indexOf("�f��")  !=  -1) ;
        String        stringNoPageDate    =  ""+get("NO_PAGE_DATE") ;     // ���ʵL�ȤƤW�u���
        String      stringToday          =  exeUtil.getDateConvert(getValue("CDate")) ;
        boolean   booleanNoPageDate   =  stringToday.compareTo(stringNoPageDate)>=0 ;
        if("�s�W".equals(stringFunction)) {
            String      stringTemp      =  exeFun.getNoPageAutoBarCode("S",  exeUtil) ;
            Hashtable   hashtableData   =  new  Hashtable() ;
            //
            if(!"".equals(stringTemp)) {
                stringBarCode  =  stringTemp ;
                hashtableData.put("BarCode",          stringBarCode) ;
                hashtableData.put("EDateTime",        datetime.getTime("YYYY/mm/dd h:m:s")) ;
                hashtableData.put("LastEmployeeNo",   getUser()) ;
                hashtableData.put("Descript",         "����") ;
                exeFun.doInsertDBDoc("Doc2M044_AutoBarCode",  hashtableData,  true,  exeUtil) ;
            }
            put("BarCode_Tmp",  stringBarCode) ;
        }
        if("".equals(stringBarCode)) {
            String  stringTemp   =  "" ;
                    stringTemp  =  "�۰ʨ��� �� [���X�s��] �w�ϥΧ��C" ;
            messagebox(stringTemp) ;
            getcLabel("BarCode").requestFocus( ) ;
            return  false ;
        }

        //
        if(!"".equals(stringBarCode)) {
            String  stringBarCode1  = stringBarCode.substring(0,1) ;
            if(!(stringFlow.indexOf("�ӿ�")!=-1  &&  stringFlow.indexOf("--")==-1)  &&  exeFun.getBarCodeFirstChar( ).indexOf(stringBarCode1)==-1) {
                if(stringBarCode1.equals("S")) {
                } else {
                    messagebox("[���X�s��] �����T�A�Э��s��J�C") ;
                    return  false ;
                }
            }
        }
        //
        // ��Ʈw�s�b�ˮ�
        if(stringFlow.indexOf("�~��")  !=  -1  ||  stringFlow.indexOf("�f��")  !=  -1) {
            if(!stringBarCodeOld.equals(stringBarCode)  &&  !exeFun.isExistBarCodeCheck(stringBarCode)) {
                messagebox("[���X�s��] �w�s�b��Ʈw���A�а��� [�d��] ��A�@�ק�C") ;
                getcLabel("BarCode").requestFocus( ) ;
                return  false ;
            }
        }
        // �����N�X���s�b DepartNo
        String  stringDepartNo  =  getValue("DepartNo").trim( ) ;
        if("".equals(stringDepartNo)) {
            messagebox("[�����N�X] ���i���ťաC") ;
            getcLabel("DepartNo").requestFocus( ) ;
            return  false ;
        }
        String  stringDepartName  =  exeFun.getDepartName(stringDepartNo) ;
        if("".equals(stringDepartName)) {
            messagebox("[�����N�X] ���s�b��Ʈw���C\n(�����D�Ь� [��T������])") ;
            getcLabel("DepartNo").requestFocus( ) ;
            return  false ;
        }
        if("033H39,0333H39,0333H42,0333H42A,033H42,033H42A,033H42B,".indexOf(stringDepartNo+",")  !=  -1) {
            messagebox("[�����N�X]("+stringDepartNo+") �����\�ϥ�\n�����D�Ь��~�ޡC") ;//�}�ɬ�
            return  false ;
        }
         // �ݨD���
         String  stringNeedDate  =  getValue("NeedDate").trim() ;
         if("".equals(stringNeedDate)) {
            messagebox("[�ݨD���] ���i���ťաC") ;
            getcLabel("NeedDate").requestFocus( ) ;
            return  false ; 
         }
        stringNeedDate  =  exeUtil.getDateFullRoc (stringNeedDate,  "�ݨD���") ;
        if(stringNeedDate.length( )  !=  9) {
            messagebox(stringNeedDate) ;
            getcLabel("NeedDate").requestFocus( ) ;
            return  false ;
        }
        setValue("NeedDate",      stringNeedDate) ;
        setValue("NeedDateEnd",   stringNeedDate) ;
         //�w�w���פ��
         String  stringPreFinDate  =  getValue("PreFinDate").trim( ) ;
         if("".equals(stringPreFinDate)) {
            messagebox("[�w�w���פ��] ���i���ťաC") ;
            getcLabel("PreFinDate").requestFocus( ) ;
            return  false ; 
         }
        stringPreFinDate  =  exeUtil.getDateFullRoc (stringPreFinDate,  "�w�w���פ��") ;
        if(stringPreFinDate.length( )  !=  9) {
            messagebox(stringPreFinDate) ;
            getcLabel("PreFinDate").requestFocus( ) ;
            return  false ;
        }
        setValue("PreFinDate",  stringPreFinDate) ;
        // �ӿ�H��
        String  stringOriEmployeeNo  =  getValue("OriEmployeeNo").trim( ) ;
        if("".equals(stringOriEmployeeNo)) {
            messagebox("[�ӿ�H��] ���i���ťաC") ;
            getcLabel("OriEmployeeNo").requestFocus( ) ;
            return  false ; 
        }
        // 
        String  stringEmpName  =  exeFun.getEmpName(stringOriEmployeeNo) ;
        if("".equals(stringEmpName)) {
            messagebox("[�ӿ�H��] ���s�b��Ʈw���C\n(�����D�Ь� [�H�`��])") ;
            getcLabel("OriEmployeeNo").requestFocus( ) ;
            //if(!getUser().startsWith("b"))
            return  false ; 
        }

        if(",01,12,".indexOf(","+stringComNo+",")  !=  -1) {
            String  stringDoc3M011EmployeeNo  =  exeFun.getTableDataDoc("SELECT  EmployeeNo  FROM  Doc3M011_EmployeeNo  WHERE  FunctionType = 'W' ")[0][0].trim() ;
            if(stringDepartNo.startsWith("033") ||  stringDepartNo.startsWith("053") ||  stringDepartNo.startsWith("133")) {
                if(!stringDoc3M011EmployeeNo.equals(stringOriEmployeeNo)) {
                    stringOriEmployeeNo  =  stringDoc3M011EmployeeNo ;
                    setValue("OriEmployeeNo",  stringDoc3M011EmployeeNo) ;
                }
            }
            // �S�O���ޤ��q
            String  stringComNoCF  =  exeFun.getComNoForEmpNo(stringOriEmployeeNo) ;
            if(stringFlow.indexOf("����")== -1  &&  !stringComNo.equals(stringComNoCF)) {
                messagebox("[�ӿ�H��] ��O���q�� ["+exeFun.getCompanyName(stringComNoCF)+"] �D ["+exeFun.getCompanyName(stringComNo)+"]�A�����\���ʡC") ;
                getcLabel("OriEmployeeNo").requestFocus( ) ;
                return  false ; 
            }
        }
        //   0  DEPT_CD     1  EMP_NO       2  EMP_NAME 
        String[][]  retFE3D05  =  exeFun.getFE3D05(stringOriEmployeeNo) ;
        if(retFE3D05.length  == 0)  return  false ;
        if("B3841,".indexOf(getUser())==-1   &&    // B3841 �}�ɬ�
          stringFlow.indexOf("����")==-1) {
            // 
            Hashtable  hashtableData  =  new  Hashtable() ;
            hashtableData.put("DEPT_CD_USER",   retFE3D05[0][0].trim()) ;
            hashtableData.put("DEPT_CD",          stringDepartNo) ;
            hashtableData.put("EmployeeNo",     getValue("EmployeeNo").trim()) ;
            //String  stringErr  =  getDocNoUserCheckErrTEST (hashtableData,  exeUtil) ;
            String  stringErr  =  exeFun.getDocNoUserCheckErr (hashtableData,  exeUtil) ;
            if(!"".equals(stringErr)) {
                messagebox(stringErr) ;
                getcLabel("DepartNo").requestFocus( ) ;
                return  false ;
            }
        }

        if(booleanFlow) {
            // ���ʤH��
            String  stringPurchaseEmployeeNo  =  getValue("PurchaseEmployeeNo").trim( ) ;
            if("".equals(stringPurchaseEmployeeNo)) {
                //messagebox("[���ʤH��] ���i���ťաC") ;
                //getcLabel("PurchaseEmployeeNo").requestFocus( ) ;
                //return  false ; 
            } else {
                stringEmpName  =  exeFun.getEmpName(stringPurchaseEmployeeNo) ;
                if("".equals(stringEmpName)) {
                    messagebox("[���ʤH��] ���s�b��Ʈw���C\n(�����D�Ь� [�H�`��])") ;
                    getcLabel("PurchaseEmployeeNo").requestFocus( ) ;
                    return  false ; 
                }
            }
        }
        // �ӽФ���  ApplyType
        String  stringApplyType        =  getValue("ApplyType").trim() ;
        String  stringUnderGoWrite  =  getValue("UNDERGO_WRITE").trim( ) ;
        if("".equals(stringApplyType)) {
            messagebox("�п�� [�ӽФ���]�C") ;
            getcLabel("ApplyType").requestFocus( ) ;
            return  false ; 
        }
        if(!"F".equals(stringApplyType)  &&  stringFlow.indexOf("����")==-1) {
            setTableData("Table2",  new  String[0][0]) ;
        }
        // �˪�
        String  stringCheckAdd  =  getValue("CheckAdd").trim() ;
        if("".equals(stringCheckAdd)) {
            messagebox("�п�� [�˪�]�C") ;
            getcLabel("CheckAdd").requestFocus( ) ;
            return  false ; 
        }
        String  stringCheckAddDescript  =  getValue("CheckAddDescript").trim() ;
        if("F".equals(stringCheckAdd)) {
            if("".equals(stringCheckAddDescript)) {
                messagebox("�п�J [�˪��䥦����] ���e�C") ;
                getcLabel("CheckAddDescript").requestFocus( ) ;
                return  false ; 
            }
        } else {
            setValue("CheckAddDescript",  "") ;
        }

        // ���R�έn�D  Analysis
        String    stringAnalysis          =  getValue("Analysis").trim() ;
        String    stringPayConditionTXT   =  getValue("PayConditionTXT").trim() ;
        boolean   booleanFlag           =  isTable1PurchaseMoney0(exeUtil) ;
        if(booleanUndergoWriteCheck  &&  stringFlow.indexOf("����")!=-1  &&  "".equals(stringAnalysis)) {
            if(!booleanFlag) {
                messagebox("[���R�έn�D] �����\�ťաC") ;
                return  false ;
            }
        }
        if(booleanUndergoWriteCheck  &&  stringFlow.indexOf("����")!=-1  &&  "".equals(stringPayConditionTXT)) {
            if(!booleanFlag) {
                messagebox("[�I�ڻ���] �����\�ťաC") ;
                return  false ;
            }
        }
        // ���ʸ�T
        if(!isTable1CheckOK(booleanUndergoWriteCheck,  exeFun,  exeUtil))  return  false ;
        // �I�ڸ�T
        boolean  booleanApply  =  "096/11/26".compareTo(exeUtil.getDateConvertFullRoc(getValue("CDate").trim()))<=0  &&  "F".equals(getValue("ApplyType").trim()) ;
        if((booleanFlow  ||  booleanApply)  &&  !"T".equals(""+get("Doc3M011_Negative"))  &&  !isTable2CheckOK(booleanUndergoWriteCheck,  exeFun,  exeUtil))  return  false ;
        // �קO���u
        if(!isTable3CheckOK(booleanUndergoWriteCheck,  stringFunction,  exeUtil,  exeFun))  return  false ;
        System.out.println("-----------------------------------------------------------isTable3CheckOK........................................END") ;
        // ���O
        if(!isCoinTypeCheckOK(exeUtil,  exeFun))  return  false ;
        // ����
        /*
        if(!isTable12CheckOK(exeUtil,  exeFun))  return  false ;
        */
        // �t��ĳ������ˮ�
        if(!isTable18CheckOK(booleanUndergoWriteCheck,  exeUtil,  exeFun))  return  false ;
        //
        if(!"".equals(stringBarCode))  setValue("BarCode",  stringBarCode) ;
              return  true ;
        }
         // ����s��  ��P�Υ��� �ˮ�
         public  String  getDocNoUserCheckErrTEST (Hashtable  hashtableData,  FargloryUtil  exeUtil) throws  Throwable{
                  String      stringUseDeptCd   =  (""+hashtableData.get("DEPT_CD_USER")).trim() ;
                  String      stringUseDeptCd3  =   exeUtil.doSubstring(stringUseDeptCd,  0,  3) ;
                  String      stringDepartNo    =  (""+hashtableData.get("DEPT_CD")).trim() ;
                  String      stringEmployeeNo  =  (""+hashtableData.get("EmployeeNo")).trim() ;
                  String      stringDepartNo4   =   exeUtil.doSubstring(stringDepartNo,  0,  4) ;
                  String      stringSpecBudget  =  ",017PR,033FG,033VIP,033CRM," ; //+get("SPEC_BUDGET")+"," ;
                  
                  Vector      vectorDeptCd      =  new  Vector() ;
                  boolean     booleanFlag       =  true ;
                  // �S���� ���� ����
                  vectorDeptCd.add("0338") ;
                  //
         System.out.println("stringUseDeptCd("+stringUseDeptCd+")---------------------------------") ;
                  //
                  if(stringUseDeptCd.indexOf("0333")!=-1  ||  vectorDeptCd.indexOf(stringUseDeptCd)!=-1) {
                        // ����
                        booleanFlag  =  (",0333,0533,1333,".indexOf(","+stringDepartNo4+",")       ==    -1)  &&                        // �P�_����l�ܫD����
                                        ((","+stringSpecBudget+",033FZ,033MP,").indexOf(","+stringDepartNo+",") == -1)  &&              // �ҥ~�����A�����ˮ֭���
                                        (",B2834,".indexOf(","+stringEmployeeNo+",")  !=-1  &&  "033TC".equals(stringDepartNo)) ;       // �ҥ~�H���A�����ˮ֭���  B2834 �i�Q�u
                        if(booleanFlag) {
                              return  "���� �� [�����N�X] �����������קO�C\n(�����D�Ь� [��P�޲z��])" ;
                        } 
                  } else if(",033,133,".indexOf(stringUseDeptCd3)  !=  -1) {
                        // ��P
                        booleanFlag  =  (",0333,0533,1333,".indexOf(","+stringDepartNo4+",")       !=    -1) ;                                     // �P�_����l�ܬO����
                        if(booleanFlag) {
                              return  "��P �� [�����N�X] ���i���������קO �C\n(�����D�Ь� [��P�޲z��])" ;
                        }     
                  }
                  return  "" ;
         }
    // ���ʸ�T
    public  boolean  isTable1CheckOK(boolean  booleanUndergoWriteCheck,  Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        //put("Doc3M010_PurchaseMoney",  "null") ;
        talk                dbAsset                                         =  getTalk(""+get("put_Asset")) ;
        talk          dbAO                              =  getTalk(""+get("put_AO")) ;
        JTable              jtable1                                                       =  getTable("Table1") ;
        JTabbedPane   jtabbedPane1                                              =  getTabbedPane("Tab1") ;
        int                     intTablePanel                                           =  0 ;
        String                stringBarCode                                           =  getValue("BarCode").trim() ;
        String                stringApplyType                                       =  getValue("ApplyType").trim() ;
        String                stringRecordNo                                        =  "" ;
        String                stringClassName                                     =  "" ;
        String                stringClassNameDescript                         =  "" ;
        String                stringComNo                                            =  getValue("ComNo").trim() ;
        String                stringCostID                                              =  "" ;
        String                stringCostID1                                           =  "" ;
        String                stringCostID2                                           =  "" ;
        String                stringDescript                                            =  "" ;
        String                stringKey                                                 =  "" ;
        String                stringType                                                =  "" ;
        String                stringUnit                                                    =  "" ;
        String                stringBudgetNum                                       =  "" ;
        String                stringHistoryPrice                                      =  "" ;
        String                stringActualNum                                       =  "" ;
        String                stringApplyMoney                                    =  "" ;
        String                stringFlow                                                =  getFunctionName() ;
        String                stringFactoryNo                                       =  "" ;
        String                stringActualPrice                                     =  "" ;
        String                stringPurchaseMoney                               =  "" ;
        String                stringPurchaseMoneyCompute                =  "" ;
        String                stringUnderGoWrite                                =  getValue("UNDERGO_WRITE").trim( ) ;
        String                stringCDate                                               =  exeUtil.getDateConvertFullRoc(getValue("CDate").trim()) ;
        String            stringStopUseMessage                  =  "" ;
        String                stringDocNo1                                       =  getValue("DocNo1").trim( ) ;
        String              stringSpecBudget                      =  ","+get("SPEC_BUDGET")+"," ;
        double               doubleApplyMoneySum                            =  0 ;
        double               doublePurchaseMoney                            =  0 ;
        double               doublePurchaseMoneySum                     =  0 ;
        double               doubleTotalPurchaseMoneySum              =  0 ;
        double               doubleTemp                                             =  0 ;
        Vector               vectorTable2FactoryNo                              =  getFactoryNoTable2( ) ;    // �I�ڱ���t��
        Vector               vectorCostID                                             =  getCostIDTable3( ) ;
        Vector               vectorFactoryNoCostID                            =  new  Vector() ;
        Hashtable         hashtableFactoryNo                                  =  new  Hashtable( ) ;
        Hashtable          hashtableCond                          =  new  Hashtable() ;
        boolean             booleanApplyF                                           =  "F".equals(stringApplyType) ;    // ����F
        boolean             booleanUser                                             =  stringFlow.indexOf("����")  ==  -1   ;
        if(jtable1.getRowCount()  ==  0) {
            return  retTable1Message(" [���ʸ�T] �L��ơC",  -1,  false,  exeUtil) ;
        }
        Vector     vectorMoneySignSame  = new  Vector() ;
        // �T��
        int               intAssetCount                       =  0 ;
        String          stringFILTER                        =  "" ;
        String          stringTemp                          =  "" ;
        String        stringRecordNoDoc3M017    =  "" ;
        boolean       booleanAssetDate                =  true ;//!"".equals(stringAssetDate)  &&  !"null".equals(stringAssetDate)  &&  stringToday.compareTo(stringAssetDate)>=0 ;
        //
        String          stringCDateAC                     =  exeUtil.getDateConvert(getValue("CDate")) ;
        String        stringFiletrDo                        =  "" ;
        String            stringFactoryNo17           =  "" ;
        String            stringPopCode               =  "" ;
        String      stringCostIDDetail          =  "" ;
        String      stringUseStatus           =  "" ;
        String      stringBigBudget           =  "" ;
        String      stringSSMediaID            =  "" ;
        String      stringSSMediaID1          =  "" ;
        String      stringSSMediaID1DB          =  "" ;
        String        stringControlType           =  ""+get("ONLY_CONTROL_AMT") ;
        String[][]    retDoc2M0401                    =  exeFun.getDoc2M0401("",  "U",  "") ;if(retDoc2M0401.length>0)  stringFiletrDo  =  retDoc2M0401[0][2].trim() ;    // Remark  �T��[�u
        Hashtable   hashtbleFunctionType        =  exeFun.getCostIDVDoc2M0201H(stringComNo,  "",  "",  stringCDateAC,  "")  ;
        Hashtable   hashtableFactoryNoAsset       =  new  Hashtable() ;
        Hashtable   hashtableDoc3M016           =  new  Hashtable() ;
        Hashtable   hashtableDoc3M017           =  new  Hashtable() ;
        Vector        vecrorCostIDTypeO             =  (Vector)  hashtbleFunctionType.get("O") ;    if(vecrorCostIDTypeO  ==  null)  vecrorCostIDTypeO  =  new  Vector() ; // �T��дڥN�X
        Vector        vectorFactoryNo                 =  new  Vector( ) ;
        Vector        vecrorRecordNoToTable9    =  new  Vector( ) ;
        Vector        vectorAsAssetFilter         =  new  Vector( ) ;
        Vector        vectorDoc2M022              =  new  Vector( ) ;
        //
        String        stringTodayL                  =  exeUtil.getDateConvert(getValue("CDate")) ;
        String      stringNoPageDate          =  ""+get("NO_PAGE_DATE") ;     // ���ʵL�ȤƤW�u���
        boolean     booleanNoPageDate         =  stringTodayL.compareTo(stringNoPageDate)>=0;

        for(int  intNo=0  ;  intNo<jtable1.getRowCount() ;  intNo++) {
            stringRecordNo                  =  (""+getValueAt("Table1",  intNo,  "RecordNo")).trim() ;
            stringCostID                        =  (""+getValueAt("Table1",  intNo,  "CostID")).trim() ;
            stringCostID1                     =  (""+getValueAt("Table1",  intNo,  "CostID1")).trim() ; 
            stringType                          =  (""+getValueAt("Table1",  intNo,  "TYPE")).trim() ;
            stringUnit                            =  (""+getValueAt("Table1",  intNo,  "Unit")).trim() ;
            stringBudgetNum                 =  (""+getValueAt("Table1",  intNo,  "BudgetNum")).trim() ;           // �w��ƶq
            stringHistoryPrice                =  (""+getValueAt("Table1",  intNo,  "HistoryPrice")).trim() ;            // ���v���
            stringApplyMoney              =  (""+getValueAt("Table1",  intNo,  "ApplyMoney")).trim() ;            // �w����B
            stringActualNum                 =  (""+getValueAt("Table1",  intNo,  "ActualNum")).trim() ;           // ��ڼƶq
            stringFactoryNo                   =  (""+getValueAt("Table1",  intNo,  "FactoryNo")).trim() ;
            stringClassName                 =  (""+getValueAt("Table1",  intNo,  "ClassName")).trim() ;
            stringClassNameDescript   =  (""+getValueAt("Table1",  intNo,  "ClassNameDescript")).trim() ;
            stringDescript                      =  (""+getValueAt("Table1",  intNo,  "Descript")).trim() ;              // �W��
            stringActualPrice                 =  (""+getValueAt("Table1",  intNo,  "ActualPrice")).trim() ;           // �o�]���
            stringPurchaseMoney         =  (""+getValueAt("Table1",  intNo,  "PurchaseMoney")).trim() ;         // ���ʪ��B
            stringApplyMoney              =  (""+getValueAt("Table1",  intNo,  "ApplyMoney")).trim() ;                // �ӽЪ��B
            stringFILTER                      =  (""+getValueAt("Table1",  intNo,  "FILTER")).trim() ;
            stringRecordNoDoc3M017  =  (""+getValueAt("Table1",  intNo,  "RecordNoDoc3M017")).trim() ;
            stringPopCode                   =  (""+getValueAt("Table1",  intNo,  "PopCode")).trim() ;
            stringCostIDDetail                =  (""+getValueAt("Table1",  intNo,  "CostIDDetail")).trim() ;
            stringSSMediaID                 =  (""+getValueAt("Table1",  intNo,  "SSMediaID")).trim() ;
            // �����u�ƥN�X
            if(!"D".equals(stringApplyType)) {
                // �D�T��
                setValueAt("Table1",  "",  intNo,  "FILTER") ;
                //                
                if(stringBarCode.startsWith("S")) {
                    if("".equals(stringCostIDDetail)) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�����u�ƥN�X] ���i���ťաC" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                    vectorDoc2M022  =  exeFun.getQueryDataHashtableDoc("Doc2M022",  new  Hashtable(),  " AND  CostIDDetail  =  '"+stringCostIDDetail+"' ",  new  Vector(),  exeUtil) ;
                    if(vectorDoc2M022.size()  <=  0 ) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�����u�ƥN�X] ���s�b��Ʈw���C" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                    stringUseStatus  =  exeUtil.getVectorFieldValue(vectorDoc2M022,  0,  "UseStatus") ;
                    if(!"Y".equals(stringUseStatus)) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�����u�ƥN�X] �����\�ϥΡC" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                    stringCostID    =  exeUtil.getVectorFieldValue(vectorDoc2M022,  0,  "CostID") ;   setValueAt("Table1",  stringCostID,   intNo,  "CostID") ;
                    stringCostID1   =  exeUtil.getVectorFieldValue(vectorDoc2M022,  0,  "CostID1") ;    setValueAt("Table1",  stringCostID1,  intNo,  "CostID1") ;
                    stringCostID2   =  exeUtil.getVectorFieldValue(vectorDoc2M022,  0,  "CostID2") ;    setValueAt("Table1",  stringCostID2,  intNo,  "CostID2") ;
                }
                //20180206 ���ʤ~�ˮ� POP �N�X
                if(!stringCostIDDetail.startsWith("78")) {          
                  if(getFunctionName().indexOf("����")  != -1) {
                    if(booleanUndergoWriteCheck  &&  ",782,".indexOf(stringCostID+stringCostID1)!=-1  &&  "".equals(stringPopCode)) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [POP �N�X] ���i���ťաC" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                  }
                }
                // ���� Table9 POP ���� ?????
                // �D�T��дڥN�X�ˮ� 2018-02-14 �������ˮ�
                vecrorCostIDTypeO.add("703") ;
                vecrorCostIDTypeO.add("704") ;
                vecrorCostIDTypeO.add("395") ;
                vecrorCostIDTypeO.add("396") ;
                vecrorCostIDTypeO.add("392") ;
                vecrorCostIDTypeO.add("3110") ;  // 2014-06-17 
                vecrorCostIDTypeO.add("3211") ;  // 2014-06-17 
                /*if(booleanUndergoWriteCheck  &&  vecrorCostIDTypeO.indexOf(stringCostID+stringCostID1)  !=  -1) {
                    stringTemp  =  "" ;
                    for(int  intNoL=0  ;  intNoL<vecrorCostIDTypeO.size()  ;  intNoL++) {
                        if(!"".equals(stringTemp))  stringTemp  +=  "�B" ;
                        stringTemp  +=  ""+vecrorCostIDTypeO.get(intNoL) ;
                    }
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� �D [�T�w�겣] �� �дڥN�X �����J"+stringTemp+"�C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                }*/
                // 
                if(stringSpecBudget.indexOf(stringDocNo1)  ==  -1) {
                    if(",033,053,133,".indexOf(exeUtil.doSubstring(stringDocNo1,  0,  3))  ==  -1) {
                        if("31,32,".indexOf(stringCostID)!=-1) {
                            stringTemp  =  "�� "  +  (intNo+1)  +  " �C �D��P���� �����\�ϥ� [�j-�дڥN�X](31�B32)�C\n(�����D�Ь� [�]�ȫ�])�C" ;
                            return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                        }
                        if(exeUtil.doParseDouble(stringCostID)  >=  70 ) {
                            if(stringDocNo1.startsWith("015")  &&  "721,".indexOf(stringCostID+stringCostID1)!=-1) {
                              // �S�Ҥ��\
                            } else {
                                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �D��P���� �����\�ϥ� 70 ���᪺�дڥN�X�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])�C" ;
                                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                            }
                        }
                    } 
                }
                // �дڥN�X    CostID�BCostID1
                //20180212 ���ʤ~�ˮ֤����u�ƥN�X
                if(getFunctionName().indexOf("����")  != -1) {
                  if("".equals(stringCostID)) {
                      stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�j-�дڥN�X] ���i���ťաC" ;
                      return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                  }
                  if("".equals(stringCostID1)) {
                      stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [��-�дڥN�X] ���i���ťաC" ;
                      return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
      
                  }
                  if(exeFun.getDoc7M011(stringComNo,  "",  stringCostID,  stringCostID1).length==0) {
                      if(exeFun.getDoc2M0201(stringComNo,  stringCostID,  stringCostID1,  "A").length  ==  0) {
                          stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�j-�дڥN�X][��-�дڥN�X] ���s�b [�w��O�ι�Ӫ�] ���C\n(�����D�Ь� [��P�޲z��])" ;
                          return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                      }
                  }
                  // Table 9 ����
                  if("781".equals(stringCostID+stringCostID1)) {
                      //vecrorRecordNoToTable9.add(stringRecordNo) ;    // 
                  }
                }
            } else if("D".equals(stringApplyType)) {
                // �T��
                setValueAt("Table1",  "",  intNo,  "CostIDDetail") ;
                //
                //20180309 �����T�w�겣����
                /*
                if("".equals(stringFILTER)) {             
                    if(booleanUndergoWriteCheck) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�T��N�X] ���i���ťաC" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                } else {
                    vectorAsAssetFilter  =  exeUtil.getQueryDataHashtable("AS_ASSET_FILTER",  new  Hashtable(),  " AND  FILTER  = '"+stringFILTER+"' ",  dbAsset) ;
                    if(booleanUndergoWriteCheck  &&  vectorAsAssetFilter.size()  ==  0) {
                        stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�T��N�X] ���s�b��Ʈw���C" ;
                        return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                    }
                    // �T�ꤧ�дڥN�X �̲Ĥ@�ӮקO���u �@ �����A�� Doc2M0201 ����(��)    �G�h�ܤ@�h�ɡA�����\�ק�дڥN�X
                    // �T��p��
                    if(!stringFILTER.equals(stringFiletrDo)) {
                        // �T��-�дڥN�X�p��
                        intAssetCount  =  exeUtil.doParseInteger(""+hashtableFactoryNoAsset.get(stringFactoryNo+"A"))  +  1 ;  
                        hashtableFactoryNoAsset.put(stringFactoryNo+"A",  ""+intAssetCount) ;
                        //
                    } else {
                        // �T��-�[�u-�дڥN�X�p��
                        intAssetCount  =  exeUtil.doParseInteger(""+hashtableFactoryNoAsset.get(stringFactoryNo+"B"))  +  1 ;  
                      hashtableFactoryNoAsset.put(stringFactoryNo+"B",  ""+intAssetCount) ;
                      }
                }
                */
            }
            // �q���N�X �ˮ�
            if(!"".equals(stringSSMediaID)) {
                if(!isERPKeyExist(stringSSMediaID,  exeUtil,  dbAO)) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�q���N�X] ���s�b��Ʈw���C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
                }
                // �S��дڥN�X�@�w���i�����ʥN�X
                // ���ʥN�X �P  �дڥN�X �����ˮ�
                stringSSMediaID1      =  exeUtil.doSubstring(stringSSMediaID,  stringSSMediaID.length()-13,  stringSSMediaID.length()-12) ;
                stringSSMediaID1DB    =  getSSMediaIDDoc(stringCostID,  stringCostID1,  exeUtil,  exeFun) ;
                if(!"".equals(stringSSMediaID1DB)  &&  !stringSSMediaID1.equals(stringSSMediaID1DB)) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�дڥN�X]"+stringSSMediaID1DB+" ���� [�q���N�X] ���Y���~�C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
                }
                // �����������дڥN�X
                stringBigBudget         =  getBigBudget(stringCostID,  stringCostID1,  exeUtil,  exeFun) ;
                if("A".equals(stringBigBudget)) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�q���N�X] �u��ϥΥ������� �дڥN�X�C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
                }
            } else {
                //20180206 ���ʤ~�ˮ� POP �N�X
                if(!stringCostIDDetail.startsWith("78"))  {     
                  if(getFunctionName().indexOf("����")  != -1) {
                      //20180323 �NPOP���дڥN�X�M���ʥN�X�����P�_�Ȯɥ�����
                      /*
                      if(booleanUndergoWriteCheck  &&  stringDocNo1.indexOf("333")==-1  &&  "782".equals(stringCostID+stringCostID1)) {
                          stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� POP �дڥN�X ���ʥN�X ���i���ťաC" ;
                          return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
                      }
                      */
                  }
                }
            }
            //
            if(vectorFactoryNo.indexOf(stringFactoryNo)  ==  -1)  vectorFactoryNo.add(stringFactoryNo) ;
            //
            stringKey                         =  stringCostID  +  "-"  +  stringCostID1 ;
            doubleApplyMoneySum  +=  exeUtil.doParseDouble(stringApplyMoney) ;
            //
            if(intNo==0) {
                if("781".equals(stringCostID+stringCostID1)) {
                    setValue("ClassNameList",  stringClassNameDescript) ;
                } else {
                    if("".equals(stringClassName)) {
                        setValue("ClassNameList",  stringClassNameDescript) ; 
                    } else {
                        setValue("ClassNameList",  stringClassName) ;
                    }
                }
            }
            //  �@����                   ����
            /*if((booleanApplyF ||  !booleanUser)  &&  vectorFactoryNoCostID.indexOf(stringCostIDDetail+"-"+stringFactoryNo)  !=  -1) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�Τ@�s��][�����u�ƥN�X] ���ơC" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
            }*/
            // ���ʼƶq   BudgetNum
            if(exeUtil.doParseDouble(stringBudgetNum)  <=  0) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���ʼƶq] ���i�p�󵥩� 0�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
            }
            if((","+stringControlType+",").indexOf(","+stringUnit+",")!=-1  &&  exeUtil.doParseDouble(stringBudgetNum)!=1) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���] �� ["+stringControlType+"] �ɡA[���ʼƶq] �u�ର 1�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
            }
            if(!check.isFloat(stringBudgetNum,  "11,4")) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���ʼƶq] �榡���~�A�u���\7 ��ƤΤp���I�� 4 ��C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;
            }
            // ���ʪ��B
            if(exeUtil.doParseDouble(stringApplyMoney)<0  &&  ",701,702,".indexOf(","+stringCostID+stringCostID1+",")==-1) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���ʪ��B] ���i�p�� 0�C\n[�� �дڥN�X 701���ݤ��߸˿X�]�p�O��(�t�믫���S)�B702�˫~�θ˿X�]�p�O�� ���\�ӽЭt�ȡC]" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
            }
            // 
            if(!"".equals(stringApplyMoney)  &&  exeUtil.doParseDouble(stringApplyMoney)==0) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���ʪ��B] �����\���� 0�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
            }
            // �w����
            if(exeUtil.doParseDouble(stringHistoryPrice)  ==  0) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�w����] �����\���� 0�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
            }
            if(booleanUser) {
                // �D����
                // ��ڼƶq   ActualNum
                setValueAt("Table1",  stringBudgetNum,  intNo,  "ActualNum") ;
                stringActualNum  =  stringBudgetNum ;
            } else {
                // ����
                // ��ڼƶq
                if(exeUtil.doParseDouble(stringActualNum)  <  0) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [��ڼƶq] ���i�p�� 0�C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
                }
                if((","+stringControlType+",").indexOf(","+stringUnit+",")!=-1  &&  exeUtil.doParseDouble(stringActualNum)!=1) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���] �� ["+stringControlType+"] �ɡA[��ڼƶq] �u�ର 1�C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
                }
                if(!check.isFloat(stringActualNum,  "11,4")) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���ʼƶq] �榡���~�A�u���\ 7 ��ƤΤp���I�� 4 ��C" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
                }
                // �ĵo���B
                if(exeUtil.doParseDouble(stringPurchaseMoney)<0  &&  ",701,702,".indexOf(","+stringCostID+stringCostID1+",")==-1) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�ĵo���B] ���i�p�� 0�C\n[�� �дڥN�X 701���ݤ��߸˿X�]�p�O��(�t�믫���S)�B702�˫~�θ˿X�]�p�O�� ���\�ӽЭt�ȡC]" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
                }
                // �ĵo���B�B�ĵo�p����B�B�z
                if(exeUtil.doParseDouble(stringPurchaseMoney) !=  0  &&  exeUtil.doParseDouble(stringActualPrice)  !=  0) {
                    doubleTemp                               =  exeUtil.doParseDouble(stringActualPrice)  *  exeUtil.doParseDouble(stringActualNum) ;
                    stringPurchaseMoneyCompute  =  convert.FourToFive(""+doubleTemp, 0) ;
                    setValueAt("Table1",  stringPurchaseMoneyCompute,  intNo,  "PurchaseMoneyCompute") ;
                } else if(exeUtil.doParseDouble(stringPurchaseMoney) !=  0) {
                    // ���ʪ��B
                    doubleTemp         =  exeUtil.doParseDouble(stringPurchaseMoney)  /  exeUtil.doParseDouble(stringActualNum) ;
                    stringActualPrice  =  convert.FourToFive(""+doubleTemp, 2) ;
                    setValueAt("Table1",  stringActualPrice,  intNo,  "ActualPrice") ;
                    //
                    doubleTemp                               =  exeUtil.doParseDouble(stringActualPrice)  *  exeUtil.doParseDouble(stringActualNum) ;
                    stringPurchaseMoneyCompute  =  convert.FourToFive(""+doubleTemp, 0) ;
                     setValueAt("Table1",  stringPurchaseMoneyCompute,  intNo,  "PurchaseMoneyCompute") ;
                } else {
                    //  �o�]���
                    doubleTemp                               =  exeUtil.doParseDouble(stringActualPrice)  *  exeUtil.doParseDouble(stringActualNum) ;
                    stringPurchaseMoney                =  convert.FourToFive(""+doubleTemp, 0) ;
                    stringPurchaseMoneyCompute  =  stringPurchaseMoney ;
                    setValueAt("Table1",  stringPurchaseMoney,  intNo,  "PurchaseMoney") ;
                    setValueAt("Table1",  stringPurchaseMoney,  intNo,  "PurchaseMoneyCompute") ;
                }
                if(exeUtil.doParseDouble(stringPurchaseMoney)  !=  exeUtil.doParseDouble(stringPurchaseMoneyCompute)) {
                    int  ans  =  JOptionPane.showConfirmDialog(null,  
                                                    "�� "  +  (intNo+1)  +  " �C �� [�p����B] ������ [���ʪ��B]�A�p�n�~��Ы� [�O]�C",
                                                    "�п��?",
                                                    JOptionPane.YES_NO_OPTION,
                                                    JOptionPane.WARNING_MESSAGE) ;
                    if(ans  ==  JOptionPane.NO_OPTION) {
                        return  false ;
                    }
                }
                //
                doublePurchaseMoney                  =  exeUtil.doParseDouble(stringPurchaseMoney) ;
                doubleTotalPurchaseMoneySum  +=  doublePurchaseMoney ;
                // �w����
                if(exeUtil.doParseDouble(stringHistoryPrice)  ==  0) setValueAt("Table1",  stringActualPrice,  intNo,  "HistoryPrice") ;
                if(exeUtil.doParseDouble(stringApplyMoney)  ==  0) {
                    setValueAt("Table1",  stringActualNum,                 intNo,  "BudgetNum") ;
                    setValueAt("Table1",  stringActualPrice,                 intNo,  "HistoryPrice") ;
                    setValueAt("Table1",  ""+doublePurchaseMoney,   intNo,  "ApplyMoney") ;
                }
            }
            // �ĵo���B
            if(exeUtil.doParseDouble(stringPurchaseMoney) >0) {
                if(vectorMoneySignSame.indexOf("����")==-1)  vectorMoneySignSame.add("����") ;
            } else if(exeUtil.doParseDouble(stringPurchaseMoney) <0) {
                if(vectorMoneySignSame.indexOf("�t��")==-1)  vectorMoneySignSame.add("�t��") ;
            } 
            // �w����B
            if(exeUtil.doParseDouble(stringApplyMoney) >0) {
                if(vectorMoneySignSame.indexOf("����")==-1)  vectorMoneySignSame.add("����") ;
            } else if(exeUtil.doParseDouble(stringApplyMoney) <0) {
                if(vectorMoneySignSame.indexOf("�t��")==-1)  vectorMoneySignSame.add("�t��") ;
                put("Doc3M011_Negative",  "T") ;
            } 
            if(vectorMoneySignSame.size() > 1) {
                stringTemp  =  "���B�o�Ϳ��~�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            // FactoryNo �b [�I�ڸ�T] �����۹���������
            if(booleanUndergoWriteCheck  &&  vectorTable2FactoryNo.indexOf(stringFactoryNo)  ==  -1  &&  (vectorTable2FactoryNo.size()  >  0  ||  "C".equals(stringUnderGoWrite)  ||  "Y".equals(stringUnderGoWrite))) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� �b [���ʸ�T] �L�۹������t�ӥI�ڸ�T�C" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            // �дڥN�X�b [�קO���u] �����۹���������
            if(booleanUndergoWriteCheck  &&  vectorCostID.size() > 0  &&  vectorCostID.indexOf(stringKey)  ==  -1) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�קO����] �L�۹��������u��ơC" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            //                                            �D����
            /*if(!booleanNoPageDate  &&  !booleanApplyF  &&  "".equals(stringClassName)) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�W��] ���i���ťաC" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }*/
            if("".equals(stringClassNameDescript)) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���e] ���i���ťաC" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            // ���       Unit
            if("".equals(stringUnit)) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [���] ���i���ťաC" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            if((!booleanUser  ||  booleanApplyF)  &&  booleanUndergoWriteCheck) { 
                if("".equals(stringFactoryNo)) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�t�ӲΤ@�s��] ���i���ťաC" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;    
                }
                if(exeFun.getDoc3M015(stringFactoryNo).length  ==  0) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�t�ӲΤ@�s��] ���s�b [���ʼt�Ӻ��@�@�~(Doc3M015)]�C\n(�����D�Ь� [���ʫ�])" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
                }
                // ���v
                hashtableCond.put("OBJECT_CD",        stringFactoryNo) ;
                hashtableCond.put("CHECK_DATE",     stringCDate) ;
                hashtableCond.put("SOURCE",           "A") ;
                hashtableCond.put("FieldName",        "�� "  +  (intNo+1)  +  " �C �� [�t�ӲΤ@�s��]") ;
                stringStopUseMessage  =  exeFun.getStopUseObjectCDMessage (hashtableCond,  exeUtil) ;
                if(!"TRUE".equals(stringStopUseMessage)) {
                    return  retTable1Message(stringStopUseMessage,  intNo,  false,  exeUtil) ;  
                }
            }
            if(booleanUndergoWriteCheck  &&  booleanApplyF) {
                // ����
                if("805".equals(stringCostID+stringCostID1)) {
                    // �ث~ Doc3M016�BDoc3M017
                    if(!isApplyF805OK(intNo,  exeUtil,  exeFun))  return  false ;
                } else {
                    // �D�ث~ Doc3M0174
                    if(!isApplyFNot805OK(intNo,  exeUtil,  exeFun))  return  false ;
                }
            }
        }
        if(doubleApplyMoneySum  ==  0) {
            JOptionPane.showMessageDialog(null,  "[���ʪ��B] �X�p ���i���� 0�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            return  false ; 
        }
        //20180309 ���ˬd�T�w�겣 By B03812
        /*
        if(booleanUndergoWriteCheck  &&  booleanAssetDate  &&  stringFlow.indexOf("����")!=-1  &&  "D".equals(stringApplyType) ){
            for(int  intNo=0  ;  intNo<vectorFactoryNo.size()  ;  intNo++)             {
                stringFactoryNo  =  ""+vectorFactoryNo.get(intNo) ;
                intAssetCount     =  exeUtil.doParseInteger(""+hashtableFactoryNoAsset.get(stringFactoryNo+"A")) ;  
                if(intAssetCount  ==  0) {
                    stringTemp  =  "�t��("+stringFactoryNo+") �T�w�겣���ʳ椧�T�궵�� �ܤ� �@�C�C" ;
                    return  retTable1Message(stringTemp,  -1,  false,  exeUtil) ; 
                } 
                intAssetCount     =  exeUtil.doParseInteger(""+hashtableFactoryNoAsset.get(stringFactoryNo+"B")) ;  
                if(intAssetCount  >  1) {
                    stringTemp  =  "�t��("+stringFactoryNo+") �T�w�겣���ʳ椧�D�T�궵�� �� ��@�C�C" ;
                    return  retTable1Message(stringTemp,  -1,  false,  exeUtil) ; 
                } 
            } 
        }
        */
        if(booleanUndergoWriteCheck  &&  !isTable1SameTable9CheckOK(vecrorRecordNoToTable9,  exeFun,  exeUtil)) {
              jtabbedPane1.setSelectedIndex(intTablePanel) ;
              return  false ; 
        }
        return  true ;
    }
    public  boolean  isApplyF805OK(int  intNo,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String      stringCDate           =  exeUtil.getDateConvert(getValue("CDate").trim()) ;
        String      stringDateStart         =  stringCDate ; //(""+getValueAt("Table1",  intNo,  "DateStart")).trim() ;
        String      stringDateEnd           =  stringCDate ; //(""+getValueAt("Table1",  intNo,  "DateEnd")).trim() ;
        String      stringCostID              =  (""+getValueAt("Table1",  intNo,  "CostID")).trim() ;
        String      stringCostID1           =  (""+getValueAt("Table1",  intNo,  "CostID1")).trim() ;
        String      stringRecordNoDoc3M017  =  (""+getValueAt("Table1",  intNo,  "RecordNoDoc3M017")).trim() ;
        String      stringFactoryNo           =  (""+getValueAt("Table1",  intNo,  "FactoryNo")).trim() ;
        String      stringTemp            =  "" ;
        String      stringFactoryNo17       =  "" ;
        String      stringFunctionName      =  getFunctionName() ;
        Hashtable   hashtableDoc3M016       =  null ;
        Hashtable   hashtableDoc3M017       =  null ;
        //
        hashtableDoc3M016  =  getDoc3M016(stringCostID,  stringCostID1,  stringDateStart,  stringDateEnd,  exeUtil,  exeFun) ;
        if(hashtableDoc3M016  ==  null) {
            stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� ���s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)]�C\n(�����D�Ь� [���ʫ�])" ;
            return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
        }
        if(exeUtil.doParseDouble(stringRecordNoDoc3M017)  >  0) {
            hashtableDoc3M017  =  getDoc3M017(stringRecordNoDoc3M017,  hashtableDoc3M016,  exeUtil,  exeFun) ;
            if(hashtableDoc3M017  ==  null) {
                stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� ���s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)]�C\n(�����D�Ь� [���ʫ�])" ;
                return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
            }
            if(!isControlNumOK(stringRecordNoDoc3M017,  hashtableDoc3M017,  exeUtil,  exeFun))  return  false ;
        }
        // �t���ˮ�
        stringFactoryNo17  =  ""+hashtableDoc3M017.get("FactoryNo") ;  if("null".equals(stringFactoryNo17))  stringFactoryNo17  =  "" ;
        if(!"".equals(stringFactoryNo17)) {
            if(!stringFactoryNo.equals(stringFactoryNo17)) {
                if(stringFunctionName.indexOf("����")  ==  -1) {
                    setValueAt("Table1",  stringFactoryNo17,  intNo,  "FactoryNo") ;
                }
            }
        } else {
            stringDateStart   =  ""+hashtableDoc3M017.get("DateStart") ;
            stringDateEnd     =  ""+hashtableDoc3M017.get("DateEnd") ;
            if(exeFun.getDoc3M0171(stringCostID,      stringCostID1,  "",  stringFactoryNo,  stringDateStart,  stringDateEnd,  "").length  ==  0) {
                if(stringFunctionName.indexOf("����")  ==  -1) {
                    stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� [�t�ӲΤ@�s��] ���s�b [2.���ʼt�ӶO�ι�Ӫ�-�ث~(Doc3M0162)] ���C\n(�����D�Ь� [���ʫ�])" ;
                    return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
                }
            }
        }
        return  true ;
    }
    public  boolean  isControlNumOK(String  stringRecordNoDoc3M017,  Hashtable  hashtableDoc3M017,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String  stringControlNum          =  ""+hashtableDoc3M017.get("ControlNum") ;
        double  doubleControlNum      =  exeUtil.doParseDouble(stringControlNum) ;      
        double  doubleUseNum          =  getNumDoc3M012(stringRecordNoDoc3M017,  hashtableDoc3M017,  exeUtil,  exeFun) ;
        //
        if(doubleControlNum  <=  0)  return  true ;
        //
        if(doubleUseNum  >  doubleControlNum) {
            messagebox("���ʺޱ��ƶq �� "+exeUtil.getFormatNum2(""+doubleControlNum)+"�A�{�w�ӽ�(�t����) "+exeUtil.getFormatNum2(""+doubleUseNum)+"�C ") ;
            return  false ;
        }
        return  true ;
    }
    public  double  getNumDoc3M012(String  stringRecordNoDoc3M017,  Hashtable  hashtableDoc3M017,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String              stringCostID              =  ""+hashtableDoc3M017.get("CostID") ;
        String              stringCostID1                 =  ""+hashtableDoc3M017.get("CostID1") ;
        String              stringCostID2                 =  ""+hashtableDoc3M017.get("CostID2") ;  
        String              stringDateStart             =  ""+hashtableDoc3M017.get("DateStart") ;
        String              stringDateEnd                   =  ""+hashtableDoc3M017.get("DateEnd") ;
        String        stringBarCode             =  getValue("BarCode").trim() ;
        String          stringSql                     =  "" ;
        String          stringUseNum                      =  "" ;
        String[][]      retDoc3M012               =  null ;
        double        doubleUseNum            = 0 ;
        //
         stringSql          =  " SELECT  M12.ActualNum " +
                                        " FROM  Doc3M011 M11,  Doc3M012 M12 "  +
                   " WHERE  M11.BarCode  =  M12.BarCode " +
                         " AND  M11.UNDERGO_WRITE  <>  'X' "  +
                        " AND  M11.BarCode  <>  '"      +stringBarCode          +"' "  +
                      " AND  M11.CDate  BETWEEN  '"   +stringDateStart        +"'  AND  '"+stringDateEnd+"' " +
                      " AND  M12.CostID  =  '"                  +stringCostID                       +"' " +
                      " AND  M12.CostID1  =  '"                +stringCostID1                     +"' " +
                      " AND  M12.RecordNoDoc3M017  =  "+stringRecordNoDoc3M017  +" " ;
         retDoc3M012  =  exeFun.getTableDataDoc(stringSql) ;
         for(int  intNo=0  ;  intNo<retDoc3M012.length  ;  intNo++) {
            stringUseNum =  retDoc3M012[intNo][0].trim() ;
            //
            doubleUseNum  +=  exeUtil.doParseDouble(stringUseNum)  ;
         }
         talk       dbDocCS       =  exeUtil.getTalkCS("Doc") ;
         if(dbDocCS  !=  null) {
             retDoc3M012  =  dbDocCS.queryFromPool(stringSql) ;
             for(int  intNo=0  ;  intNo<retDoc3M012.length  ;  intNo++) {
                stringUseNum =  retDoc3M012[intNo][0].trim() ;
                //
                doubleUseNum  +=  exeUtil.doParseDouble(stringUseNum)  ;
             }
         }
         doubleUseNum  +=  exeUtil.doParseDouble(getValue("ActualNum")) ;
         return  doubleUseNum ;
    }
    public  boolean  isApplyFNot805OK(int  intNo,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String      stringDateStart         =  (""+getValueAt("Table1",  intNo,  "DateStart")).trim() ;
        String      stringDateEnd           =  (""+getValueAt("Table1",  intNo,  "DateEnd")).trim() ;
        String      stringCostIDDetail        =  (""+getValueAt("Table1",  intNo,  "CostIDDetail")).trim() ;
        String      strinDocNo173           =  (""+getValueAt("Table1",  intNo,  "DocNo173")).trim() ;
        String      stringRecordNoDoc3M017  =  (""+getValueAt("Table1",  intNo,  "RecordNoDoc3M017")).trim() ;
        String      stringFactoryNo           =  (""+getValueAt("Table1",  intNo,  "FactoryNo")).trim() ;
        String      stringUnit                =  (""+getValueAt("Table1",  intNo,  "Unit")).trim() ;
        String      stringClassName       =  (""+getValueAt("Table1",  intNo,  "ClassName")).trim() ;
        String      stringCDate           =  exeUtil.getDateConvert(getValue("CDate").trim()) ;
        String      stringCDateUse          =  exeUtil.getDateConvert(getValue("CDateUse").trim()) ;
        String      stringTemp            =  "" ;
        String      stringFactoryNo17       =  "" ;
        String      stringSqlAnd          =  "" ;
        String      stringFunctionName      =  getFunctionName() ;
        Hashtable   hashtableDoc3M016       =  null ;
        Hashtable   hashtableDoc3M017       =  null ;
        Hashtable   hashtableAnd              =  new  Hashtable() ;
        Vector    vectorDoc3M0174       =  new  Vector() ;
        //
        if(stringFunctionName.indexOf("����")  !=  -1)   return  true ;
        // 
        if(!stringCostIDDetail.startsWith("74")) {
            if("".equals(stringCDateUse))  stringCDateUse  =  stringCDate ;
            //
            stringDateStart   =  stringCDateUse ;
            stringDateEnd     = stringCDateUse ;
        }
        // 
        hashtableAnd.put("DocNo",     strinDocNo173) ;
        hashtableAnd.put("CostIDDetail",  stringCostIDDetail) ;
        hashtableAnd.put("RecordNo",    stringRecordNoDoc3M017) ;
        hashtableAnd.put("FactoryNo",   stringFactoryNo) ;
        //hashtableAnd.put("ItemName",  stringClassName) ;    // �W��
        //hashtableAnd.put("Unit",        stringUnit) ;       // ���
        stringSqlAnd    =  " AND  ( DateStart = '9999/99/99'  OR  DateStart <= '"+stringDateStart+"' )" +
                         " AND  ( DateEnd = '9999/99/99'  OR  DateEnd >= '"+stringDateEnd+"' )" ;
        vectorDoc3M0174  =  exeFun.getQueryDataHashtableDoc("Doc3M0174",  hashtableAnd,   stringSqlAnd,  new  Vector(),  exeUtil) ;
        if(vectorDoc3M0174.size()  ==  0) {
            stringTemp  =  "�� "  +  (intNo+1)  +  " �C �� ���s�b [3.���ʼt�ӶO�ι�Ӫ�-��P(Doc3M0163)]�C\n(�����D�Ь� [���ʫ�])" ;
            return  retTable1Message(stringTemp,  intNo,  false,  exeUtil) ;  
        }
        // �t���ˮ�
        stringFactoryNo17  =  exeUtil.getVectorFieldValue(vectorDoc3M0174,  0,  "FactoryNo") ;
        if(!"".equals(stringFactoryNo17)) {
            if(!stringFactoryNo.equals(stringFactoryNo17)) {
                setValueAt("Table1",  stringFactoryNo17,  intNo,  "FactoryNo") ;
            }
        }
        return  true ;
    }
    public  boolean  retTable1Message(String  stringMessage,  int  intRowPosition,  boolean  booleanRet,  FargloryUtil  exeUtil) throws  Throwable {
        JTabbedPane     jtabbedPane1                                          =  getTabbedPane("Tab1") ;
        JTable               jtable1                                                      =  getTable("Table1") ;
        int                     intTablePanel                                           =  0 ;
        //
        if(intRowPosition  !=  -1)jtable1.setRowSelectionInterval(intRowPosition,  intRowPosition) ;
        jtabbedPane1.setSelectedIndex(intTablePanel) ;
        //
        if(!"".equals(stringMessage))messagebox(stringMessage) ;
        return  booleanRet ;
    }
    public  String  getBigBudget(String  stringCostID,  String  stringCostID1,  FargloryUtil  exeUtil,  Doc2M010  exeFun)throws  Throwable {
        String      stringBigBudget     =  "" ;
        String      stringBudgetID        =  "" ;
        String      stringComNo       =  getValue("ComNo") ;
        Hashtable  hashtableAnd         =  new  Hashtable() ;
        //
        hashtableAnd.put("ComNo",       stringComNo) ;
        hashtableAnd.put("CostID",      stringCostID) ;
        hashtableAnd.put("CostID1",     stringCostID1) ;
        stringBudgetID  =  exeFun.getNameUnionDoc("BudgetID",  "Doc2M020",  "",  hashtableAnd,  exeUtil) ;
        if(!stringBudgetID.startsWith("B"))  return "A" ;
        //
        hashtableAnd.put("BudgetID",     stringBudgetID) ;
        stringBigBudget  =  exeFun.getNameUnionDoc("BigBudget",  "Doc7M072",  "",  hashtableAnd,  exeUtil) ;
        //
        return  stringBigBudget ;
    }
    public  boolean  isERPKeyExist(String  stringERPKey,  FargloryUtil  exeUtil,  talk  dbAO)throws  Throwable {
        Vector      vectorViewAOSeminar     =  null ;
        Hashtable   hashtableAnd          =  new  Hashtable() ;
        //
        hashtableAnd.put("ERP_Key",  stringERPKey) ;
        hashtableAnd.put("set_flag",    "�w��q�L") ;
        vectorViewAOSeminar  =  exeUtil.getQueryDataHashtable("View_AO_Seminar",  hashtableAnd,  "",  dbAO)  ;
        //
        if(vectorViewAOSeminar.size()  ==  0)  {
            return  false ;
        }
        return  true ;
    }
    public  String  getSSMediaIDDoc(String  stringCostID,  String  stringCostID1,  FargloryUtil  exeUtil,  Doc2M010  exeFun)throws  Throwable {
        String      stringSSMediaID1   =  "" ;
        Hashtable  hashtableAnd          =  new  Hashtable() ;
        //
        hashtableAnd.put("CostID",      stringCostID) ;
        hashtableAnd.put("CostID1",       stringCostID1) ;
        hashtableAnd.put("UseType",     "A") ;
        stringSSMediaID1  =  exeFun.getNameUnionDoc("SSMediaID",  "Doc7M070",  "",  hashtableAnd,  exeUtil) ;
        if("".equals(stringSSMediaID1))  return "" ;
        //
        stringSSMediaID1  =  exeUtil.doSubstring(stringSSMediaID1,  0,  1) ;
        return  stringSSMediaID1 ;
    }
    public  Hashtable  getDoc3M016(String  stringCostID,  String  stringCostID1,  String  stringDateStart,  String  stringDateEnd,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
           String         stringSqlAnd          =  "" ;
           Hashtable        hashtableAnd          =  new  Hashtable() ;
           Vector         vectorDoc3M016      =  null ;
           //
         stringSqlAnd  = " AND  ( (DateStart  >=  '"   +  stringDateStart   +  "'  AND  DateStart  <=  '" +  stringDateEnd  +  "')  OR "  +
                        " (DateEnd   >=  '"   +  stringDateStart   +  "'  AND  DateEnd   <=  '"  +  stringDateEnd  +  "')  OR "  +
                        " (DateStart  <=  '"   +  stringDateStart   +  "'  AND  DateEnd   >=  '"  +  stringDateEnd  +  "')  OR "  +
                        " (DateStart  >=  '"   +  stringDateStart   +  "'  AND  DateEnd   <=  '"  +  stringDateEnd  +  "')) " ;
          if(!"".equals(stringCostID))      hashtableAnd.put("CostID",      stringCostID) ;
          if(!"".equals(stringCostID1))       hashtableAnd.put("CostID1",       stringCostID1) ;
           vectorDoc3M016  =  exeFun.getQueryDataHashtableDoc("Doc3M016",  hashtableAnd,  stringSqlAnd,  new  Vector(),  exeUtil) ;
           //
           if(vectorDoc3M016.size()  <=  0)  return  null ;
           if(vectorDoc3M016.size()  >    1)  return  null ;
           return (Hashtable) vectorDoc3M016.get(0) ;
    }
    public  Hashtable  getDoc3M017(String  stringRecordNo,  Hashtable  hashtableDoc3M016,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        String                stringCostID                  =  ""+hashtableDoc3M016.get("CostID") ;
        String                stringCostID1                 =  ""+hashtableDoc3M016.get("CostID1") ;
        String                stringDateStart         =  ""+hashtableDoc3M016.get("DateStart") ;
        String                stringDateEnd           =  ""+hashtableDoc3M016.get("DateEnd") ;
        String            stringSql                 =  "" ;
        Vector          vectorDoc3M017        =  null ;
        Hashtable       hashtableAnd             =  new  Hashtable() ;
        // ����
        hashtableAnd.put("CostID",      stringCostID) ;
        hashtableAnd.put("CostID1",       stringCostID1) ;
        hashtableAnd.put("DateStart",     stringDateStart) ;
        hashtableAnd.put("DateEnd",       stringDateEnd) ;
        hashtableAnd.put("RecordNo",    stringRecordNo) ;
        vectorDoc3M017  =  exeFun.getQueryDataHashtableDoc("Doc3M017",  hashtableAnd,  "",  new  Vector(),  exeUtil) ;
        if(vectorDoc3M017.size()  !=  1)  return  null ;
        return (Hashtable) vectorDoc3M017.get(0) ;
    }
    public  boolean  isTable1SameTable9CheckOK(Vector  vecrorRecordNoToTable9,  Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        if(vecrorRecordNoToTable9.size()  ==  0)  {
            setTableData("Table9",  new  String[0][0]) ;
            return  true ;
        }
        //
        JTable               jtable9                            =  getTable("Table9") ;
        String        stringRecordNo        =  "" ;
        String        stringRecordNoL         =  "" ;
        String[][]    retTable9Data           =  getTableData("Table9") ;
        Vector        vectorTable9Data        =  new  Vector() ;
        boolean       booleanFlag           =  true ;
        for(int  intNo=0  ;  intNo<vecrorRecordNoToTable9.size()  ;  intNo++) {
            stringRecordNo  =  (""+vecrorRecordNoToTable9.get(intNo)).trim() ;
            booleanFlag       =  false ;
            for(int  intNoL=0  ;  intNoL<jtable9.getRowCount()  ;  intNoL++) {
                stringRecordNoL  =  (""+getValueAt("Table9",  intNoL,  "RecordNo")).trim() ;
                if(stringRecordNo.equals(stringRecordNoL)) {
                    booleanFlag  =  true ;
                    break ;
                }
            }
            if(!booleanFlag) {
                messagebox("[���ʸ�T���] ��"+stringRecordNo+"�C�L������ POP �����T�C") ;
                return  false ;
            }
        }
        for(int  intNo=0  ;  intNo<jtable9.getRowCount()  ;  intNo++) {
            stringRecordNo  =  (""+getValueAt("Table9",  intNo,  "RecordNo")).trim() ;
            if(vecrorRecordNoToTable9.indexOf(stringRecordNo)  !=  -1) {
                // �s�b
                vectorTable9Data.add(retTable9Data[intNo]) ;
            }
        }
        setTableData("Table9",  (String[][])  vectorTable9Data.toArray(new  String[0][0])) ;
        return true ;
    }
    
    // �I�ڸ�T
    public  boolean  isTable2CheckOK(boolean  booleanUndergoWriteCheck,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        JTabbedPane     jtabbedPane1                      =  getTabbedPane("Tab1") ;
        JTable               jtable2                                  =  getTable("Table2") ;
        int                     intTablePanel                       =  1 ;
        String                stringFactoryNo                     =  "" ;
        String                stringRecordNo                    =  "" ;
        String                stringRecordNoOld               =  "" ;
        String                stringPayCondition1             =  "" ;
        String                stringPayCondition2             =  "" ;
        String                stringPercentRate                =  "" ;
        String                stringMonthNum                  =  "" ;
        String                stringPurchaseMoney           =  "" ;
        String                stringGroupID                      =  "" ;
        double                doublePercentRate                =  0 ;
        double                doublePercentRateSum         =  0 ;
        double                doublePurchaseMoney           =  0 ;
        double                doublePurchaseMoneySum    =  0 ;
        double                doublePurchaseMoneySum2  =  0 ;
        Vector               vectorFactoryNo                    =  new Vector( ) ;
        Hashtable         hashtablePercentRate           =  new  Hashtable( ) ;
        Hashtable           hashtablePurchaseMoney      =  new  Hashtable( ) ;
        if(jtable2.getRowCount()  ==  0) {
            if(!booleanUndergoWriteCheck  ||  isTable1PurchaseMoney0(exeUtil)) {
                return  true ;
            }
            JOptionPane.showMessageDialog(null,  " [�I�ڸ�T] �L��ơC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            jtabbedPane1.setSelectedIndex(intTablePanel) ;
            return  false ;
        }
        for(int  intNo=0 ;  intNo<jtable2.getRowCount( )  ;  intNo++) {
            stringFactoryNo            =  (""+getValueAt("Table2",  intNo,  "FactoryNo")).trim() ;
            stringPayCondition1     =  (""+getValueAt("Table2",  intNo,  "PayCondition1")).trim() ;
            stringPayCondition2     =  (""+getValueAt("Table2",  intNo,  "PayCondition2")).trim() ;
            stringPercentRate        =  (""+getValueAt("Table2",  intNo,  "PercentRate")).trim() ;
            stringPurchaseMoney  =  (""+getValueAt("Table2",  intNo,  "PurchaseMoney")).trim() ;
            stringMonthNum           =  (""+getValueAt("Table2",  intNo,  "MonthNum")).trim() ;
            stringGroupID               =  (""+getValueAt("Table2",  intNo,  "GroupID")).trim() ;
            // FactoryNo
            if(vectorFactoryNo.indexOf(stringFactoryNo+"-"+stringGroupID)  ==  -1)  vectorFactoryNo.add(stringFactoryNo+"-"+stringGroupID) ;
            // ���           PercentRate
            if("".equals(stringPercentRate)) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [���] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table2",  intNo,  "PercentRate") ;
                return  false ; 
            }
            doublePercentRate          =  exeUtil.doParseDouble(stringPercentRate) ;
            doublePercentRateSum   =  exeUtil.doParseDouble(""+hashtablePercentRate.get(stringFactoryNo+"-"+stringGroupID)) ;
            doublePercentRateSum +=  doublePercentRate ;
            hashtablePercentRate.put(stringFactoryNo+"-"+stringGroupID,  convert.FourToFive(""+doublePercentRateSum,  0)) ;
            // PurchaseMoney
            doublePurchaseMoney           =  exeUtil.doParseDouble(stringPurchaseMoney) ;
            doublePurchaseMoneySum    =  exeUtil.doParseDouble(""+hashtablePurchaseMoney.get(stringFactoryNo+"-"+stringGroupID)) ;
            doublePurchaseMoneySum  +=  doublePurchaseMoney ;
            hashtablePurchaseMoney.put(stringFactoryNo+"-"+stringGroupID,  ""+doublePurchaseMoneySum) ;
            // �I�ڱ���       PayCondition1
            if("999".equals(stringPayCondition1)) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [�I�ڱ���1] ���i���L�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table2",  intNo,  "PayCondition") ;
                return  false ; 
            }
            // ����           MonthNum
/*            if("".equals(stringMonthNum)) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [����] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table2",  intNo,  "MonthNum") ;
                return  false ; 
            }*/
        }
        // �ץ� ���ʸ�T �P �I�ڸ�T �@�P �޿� 
        String            stringKey                         =  "" ;
        String[]          arrayTemp                     =  null ;
        Hashtable           hashtablePurchaseMoneyTable1    =  getGroupIDTable1(exeUtil) ;
        for(int  intNo=0  ;  intNo<vectorFactoryNo.size()  ;  intNo++) {
            stringKey                      =  (""+vectorFactoryNo.get(intNo)).trim() ;
            arrayTemp                    =  convert.StringToken(stringKey,  "-") ;
            stringFactoryNo            =  arrayTemp[0].trim() ;
            // 100 % �ˮ�
            doublePercentRateSum   =  exeFun.doParseDouble(""+hashtablePercentRate.get(stringKey)) ;
            /*if(doublePercentRateSum  !=  100) {
                JOptionPane.showMessageDialog(null,  "[�t��] �� "  +  stringFactoryNo  +  " �� [���] �M���� 100 %�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                return  false ; 
            }*/
            
            
            // ���B�۵��ˮ�
            doublePurchaseMoneySum      =  exeUtil.doParseDouble(""+hashtablePurchaseMoney.get(stringKey)) ;
            doublePurchaseMoneySum2    =  exeUtil.doParseDouble(""+hashtablePurchaseMoneyTable1.get(stringKey)) ;
            if(doublePurchaseMoneySum  !=  doublePurchaseMoneySum2) {
                JOptionPane.showMessageDialog(null,  "[�t��] �� "  +  stringFactoryNo  +  " �� [���B] �`�M ("+convert.FourToFive(""+doublePurchaseMoneySum,  0)  +  ")"  +
                                                                                                                       " �P�۹��� [���ʸ�T] �`�M ("+convert.FourToFive(""+doublePurchaseMoneySum2,  0)  +  ") ���P �C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                return  false ; 
            }
        }
        return  true ;
    }
    public  Hashtable  getGroupIDTable1(FargloryUtil  exeUtil) throws  Throwable {
        JTable               jtable1                                                     =  getTable("Table1") ;
        String                stringKEY                                                =  "" ;
        String                stringGroupID                                         =  "" ;
        String                stringFactoryNo                                      =  "" ;
        String                stringPurchaseMoney                             =  "" ;
        Hashtable         hashtablePurchaseMoney               =  new  Hashtable( ) ;
        double               doublePurchaseMoney               =  0 ;     
        for(int  intNo=0  ;  intNo<jtable1.getRowCount() ;  intNo++) {
            stringGroupID                   =  (""+getValueAt("Table1",  intNo,  "GroupID")).trim() ;
            stringFactoryNo                 =  (""+getValueAt("Table1",  intNo,  "FactoryNo")).trim() ;
            stringPurchaseMoney       =  (""+getValueAt("Table1",  intNo,  "PurchaseMoney")).trim() ;     // ���ʪ��B
            //
            stringKEY  =  stringFactoryNo+"-"+stringGroupID ;
            //
            doublePurchaseMoney           =  exeUtil.doParseDouble(stringPurchaseMoney)  +  exeUtil.doParseDouble(""+hashtablePurchaseMoney.get(stringKEY)) ;
            hashtablePurchaseMoney.put(stringKEY,  convert.FourToFive(""+doublePurchaseMoney,  0)) ;
            //System.out.println("getGroupIDTable1------------stringKEY("+stringKEY+")doublePurchaseMoney("+doublePurchaseMoney+")------------------------------------------------") ;
        }
        return  hashtablePurchaseMoney;
    }
    public  Vector  getFactoryNoTable2( ) throws  Throwable {
        JTable   jtable2                 =  getTable("Table2") ;
        String    stringFactoryNo   =  "" ;
        Vector   vectorFactoryNo  =  new  Vector( ) ;
        for(int  intNo=0 ;  intNo<jtable2.getRowCount( )  ;  intNo++) {
            stringFactoryNo  =  (""+getValueAt("Table2",  intNo,  "FactoryNo")).trim() ;
            //
            if(vectorFactoryNo.indexOf(stringFactoryNo)  !=  -1)  continue ;
            //
            vectorFactoryNo.add(stringFactoryNo) ;
        }
        return  vectorFactoryNo ;
    }
    // �קO���u 
    public  boolean  isTable3CheckOK(boolean  booleanUndergoWriteCheck,  String  stringFunction,  Farglory.util.FargloryUtil  exeUtil,  Doc.Doc2M010  exeFun) throws  Throwable {
        JTabbedPane   jtabbedPane1                              =  getTabbedPane("Tab1") ;
        JTable                jtable3                                         =  getTable("Table3") ;
        int                      intTablePanel                               =  4 ;
        int                      intYear                                         =  0 ;
        String                stringActionNo                              =  getValue("ActionNo").trim() ;
        String                stringBarCode                             =  getValue("BarCodeOld").trim( ) ;
        String                stringBudgetID                             =  "" ;
        String                stringBudgetMoney                      =  "" ;
        String                stringCDate                                  =  exeUtil.getDateConvertFullRoc(getValue("CDate").trim()) ;
        String                stringNeedDate                            =  getValue("NeedDate").trim() ;
        String                stringNeedDateAC                       =  exeUtil.getDateConvert(stringNeedDate) ;
        String                stringCDateAC                             =  "" ;
        String                stringCostID                                 = "" ;
        String                stringCostID1                               = "" ;
        String                stringComNo                                  =  getValue("ComNo").trim() ;
        String                stringDateStart                             =  "" ;
        String                stringDepartNo                             =  "" ;
        String                stringDulKey                                 =  "" ;
        String                stringEDateTime                           =  getValue("EDateTime").trim() ;
        String                stringFlow                                     =  getFunctionName() ;
        String                stringKey                                       =  "" ;
        String                stringProjectID1                            = "" ;
        String                stringProjectID1Use                      = "" ;
        String                stringRealMoney                           = "" ;
        String                stringType                                     =  "" ;
        String                stringTemp                                     =  "" ;
        String                stringLimitOut                                =  "%--%" ;
        String                stringDateStage                             =  "" ;
        String                stringStageDateStart                     =  "" ;
        String                stringStageDateEnd                       =  "" ;
        String                stringProjectID                                =  "" ; 
        String                sringProjectIDComput                     =  getProjectIDFromDepartNo(exeUtil,  exeFun) ;
        String                stringInOut                                      =  "" ;
        String                stringSqlAnd                                   =  "" ;
        String[][]            retDoc7M011                                 =  null ;
        String[][]            retDoc7M015                                 =  null ;
        String[][]            retDoc7M020                                 =  null ;
        double              doubleActionMoney                         = 0 ;
        double              doubleBudgetMoney                       = 0 ;
        double              doubleRealMoney                           = 0 ;
        double              doubleRealMoneySum                    = 0 ;
        double              doubleRealMoneySumS                   = 0 ;
        //Hashtable         hashtableDateStart                          =  new  Hashtable() ;                                   // �קK ���ƧP�_���q�վ�@�~���ͮĤ���A�x�s
        Hashtable         hashtableBudgetMoney                  =  new  Hashtable() ;                                     // �w���ˮ֮ɨϥ�
        Hashtable         hashtableBudgetMoneyBudgetID    =  new  Hashtable() ;                                   // �w���ˮ֮ɨϥ�
        Hashtable         hashtableBudgetMoney2                 =  new  Hashtable() ;                                     // �w���ˮ֮ɨϥ�
        Hashtable         hashtableBudgetMoneyBudgetID2 =  new  Hashtable() ;                                   // �w���ˮ֮ɨϥ�
        Hashtable         hashtableRealMoney                      =  new  Hashtable() ;                                   // [���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
        Vector               vectorCostID                                  =  new  Vector() ;                                     // [���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
        Vector               vectorDulKey                                 =  new  Vector() ;                                      // �P�_��� [�קO][�дڥN�X][�p�дڥN�X] �O�_����
        Vector               vectorProjectID1BudgetID               =  new  Vector() ;                                      // �w���ˮ֮ɨϥ�
        Vector               vectorProjectID1Type                     =  new  Vector() ;                                      // �קK���� �w�� �� ���q�s�b �ˮ�
        Vector               vectorProjectID1NoUseBudget        =  new  Vector() ;                                                                           // ���ˬd�w��
        Vector               vectorProject                                    =  new  Vector() ;
        Vector              vectorSpecCostID              =  getFunctionTypeUDoc2M0201 (exeUtil,  exeFun) ;
        boolean            booleanApply                                   =  ("096/11/26".compareTo(stringCDate)<=0) &&  "F".equals(getValue("ApplyType").trim()) ;
        boolean            booleanUser                                  =  stringFlow.indexOf("����")  ==  -1 ;
        boolean            booleanTemp                                =  true ;
        boolean            boolean053Start                           =  getValue("DepartNo").startsWith("053") ;
        Hashtable        hashtableDoc3M043                       =  getDoc3M043(stringActionNo,  vectorProject,  exeFun,  exeUtil) ;
        Hashtable        hashtableThisUseMoney                =  new  Hashtable() ;

        if(booleanUndergoWriteCheck  &&  jtable3.getRowCount()  ==  0) {
          System.out.println(".......................................................isTable3CheckOK--------------------[�קO���u] �L��ơC") ;
            JOptionPane.showMessageDialog(null,  " [�קO���u] �L��ơC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
            jtabbedPane1.setSelectedIndex(intTablePanel) ;
            return  false ;
        }

        // �M��
        // ���ʥN�X E02573�BG89456 ���]�ҥ~
        if(!"".equals(stringActionNo)  &&  vectorProject.size()==0)  {
            if(!"Z6".equals(stringComNo)) {
                messagebox("�D ["+get("Z6")+"] �����\�ϥ� �M�� �y�{�C") ;
                return  false ;
            }
            //
            String[][]  retDoc3M040  =  getDoc3M040(stringComNo,  stringActionNo,  exeFun) ;
            if(retDoc3M040.length  ==  0) {
                messagebox(" [���ʥN�X] ���s�b�C") ;
            } else {
                stringTemp  =  retDoc3M040[0][0].trim() ;
                if(!"".equals(stringTemp)) {
                    messagebox(" [���ʥN�X] �|�������y�{�C\n(�����D�Ь� [��P������])") ;
                } else {
                    /*switch(stringTemp.charAt(0)) {
                        case 'A' : stringTemp  =  "�Ф�" ;         break ;
                        case 'B' : stringTemp  =  "�D��H" ;     break ;
                        case 'C' : stringTemp  =  "�f��" ;        break ;
                        case 'Y' : stringTemp  =  "��t�D��" ; break ;
                    }*/
                    messagebox(" [���ʥN�X] ��� [�ӿ�] �y�{�A�����\����C\n(�����D�Ь� [��P������])") ;
                }
            }
            return  false ;
        }
        //
        vectorProjectID1NoUseBudget.add("0331---F1---F1") ;   // 
        vectorProjectID1NoUseBudget.add("0531---H42---H42A") ;   // 
        vectorProjectID1NoUseBudget.add("0531---M---M51A") ;   // 
        //
        stringCDateAC              =  exeUtil.getDateConvert(stringCDate) ;
        intYear                          =  exeUtil.doParseInteger(datetime.getYear(stringCDateAC.replaceAll("/",  ""))) ;    
        //
        boolean  booleanDepartProjectIDSame  =  "".equals(sringProjectIDComput) ;

        // ���~����
        Vector     vectorDeptCd                  =  new  Vector() ;
        String    stringSpecBudget      =  ""+get("SPEC_BUDGET") ;
        String    stringDocNo1                  =  getValue("DocNo1").trim() ;
        String[]    arraySpecBudget         =  convert.StringToken(stringSpecBudget,",") ;
        vectorDeptCd.add("0331") ;    // ���ʲ���P��
        vectorDeptCd.add("0333") ;      // ��P������
        for(int  intNo=0  ;  intNo<arraySpecBudget.length  ;  intNo++)  vectorDeptCd.add(arraySpecBudget[intNo]) ;    
        Vector  vectorDeptCd2  =  new  Vector() ;
        vectorDeptCd2.add("03365") ;    // 2014-01-28 �̤����w �s�W
        //
        String     stringUndergoWrite  =  getValue("UNDERGO_WRITE").trim() ;
        String     stringApplyType        =  getValue("ApplyType").trim() ;
        String     string033FGType      =  "0" ;  // 0 ���]�w  1 033FG  2 �D033FG  3 �V��
        boolean  booleanRealMoney  =  stringFlow.indexOf("����")!=-1; //("C,Y,".indexOf(stringUndergoWrite)!=-1)  ||  ("S".equals(stringUndergoWrite)  &&  "F".equals(stringApplyType)) ;
        boolean  booleanCostID805   =  false ;
        //
        if(!"Z6".equals(stringComNo)  &&  (","+stringSpecBudget+",").indexOf(","+stringDocNo1+",")!=-1) {
            messagebox("�D ["+get("Z6")+"] �����\�ϥ� "+stringDocNo1+" �O�ΡC") ;
            return  false ;
        }
        Vector  vectorDoc2M0201 =  null ;
        String    stringSqlAnd807 =   " AND  FunctionType LIKE  '%2%' "+
                             " AND  (DateStart='9999/99/99'  OR  DateStart<='"+stringCDate+"' )" +
                             " AND  (DateEnd='9999/99/99'    OR  DateEnd>='"  +stringCDate+"' )" +
                             " AND  ComNo  IN ('ALL',  '"  +stringComNo+"' )" ;
        for(int  intNo=0 ;  intNo<jtable3.getRowCount( )  ;  intNo++) {
            stringInOut                 =  (""+getValueAt("Table3",  intNo,  "InOut")).trim() ;
            stringProjectID           =  (""+getValueAt("Table3",  intNo,  "ProjectID")).trim() ;
            stringProjectID1         =  (""+getValueAt("Table3",  intNo,  "ProjectID1")).trim() ;
            stringCostID               =  (""+getValueAt("Table3",  intNo,  "CostID")).trim() ;
            stringCostID1             =  (""+getValueAt("Table3",  intNo,  "CostID1")).trim() ;
            stringRealMoney        =  (""+getValueAt("Table3",  intNo,  "RealMoney")).trim() ;
            stringBudgetMoney    =  (""+getValueAt("Table3",  intNo,  "BudgetMoney")).trim() ;
            stringDepartNo           =  (""+getValueAt("Table3",  intNo,  "DepartNo")).trim() ;
            //
            if("805".equals(stringCostID+stringCostID1))  booleanCostID805  =  true ;
            // 310 �Ȥ��\�ϥΦb�ɴڤ�
            if("310".indexOf(stringCostID+stringCostID1)  !=  -1) {
                  messagebox("�дڥN�X 310 �����\�ϥΡC\n(�����D�Ь� [�]�ȫ�])") ;
                  jtabbedPane1.setSelectedIndex(intTablePanel) ;
                  jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                  return  false ; 
            }
            //
            vectorDoc2M0201   =  exeFun.getQueryDataHashtableDoc("Doc2M0201",  new  Hashtable(),  stringSqlAnd807+" AND  CostID  =  '"+stringCostID+"' " +" AND  CostID1  =  '"+stringCostID1+"' ",  new  Vector(),  exeUtil) ;
            if(vectorDoc2M0201.size()  >  0) {
                  messagebox("[�j-�дڥN�X][��-�дڥN�X]("+stringCostID+stringCostID1+") �����\�ϥΡC\n(�����D�Ь� [��P�޲z��])�C") ;
                  jtabbedPane1.setSelectedIndex(intTablePanel) ;
                  jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                  return  false ;   
            }
            //
            if(!"".equals(stringBarCode)  &&  "E99385,E99362,".indexOf(stringBarCode)  !=  -1) {

            } else if(",033CRM,".indexOf(","+getValue("DocNo1")+",")  !=  -1) {
                // ���@����
            } else {
                System.out.println(intNo+"stringSpecBudget("+stringSpecBudget+")stringDepartNo("+stringDepartNo+")string033FGType("+string033FGType+")----------------------------------------") ;
                if(stringSpecBudget.indexOf(stringDepartNo)  ==  -1) {
                    if("1".equals(string033FGType)){
                        messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                        jtabbedPane1.setSelectedIndex(intTablePanel) ;
                        jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                        return  false ; 
                    }
                    if(stringSpecBudget.indexOf(getValue("DocNo1"))  !=  -1) {
                        messagebox(getValue("DocNo1")+"���קO���u�u�ର"+getValue("DocNo1")+"�C\n(�����D�Ь� [�]�ȫ�])") ;
                        jtabbedPane1.setSelectedIndex(intTablePanel) ;
                        jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                        return  false ;                     
                    }
                    System.out.println(intNo+"string033FGType("+string033FGType+")----------------------------------------1") ;
                    string033FGType  =  "2" ;
                } else {
                    if("I".equals(stringInOut)) {
                        if(stringDocNo1.equals(stringDepartNo)) {
                            if("2".equals(string033FGType)){
                                messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                                return  false ; 
                            }
                            if(stringSpecBudget.indexOf(stringDepartNo)  ==  -1) {
                                messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                                return  false ;                             
                            }
                            string033FGType  =  "1" ;
                        } else {
                            if("1".equals(string033FGType)){
                                messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                                return  false ; 
                            }
                            if(stringSpecBudget.indexOf(stringDepartNo)  !=  -1) {
                                messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                                return  false ;                             
                            }
                            string033FGType  =  "2" ; 
                        }
                    } else {
                        if("1".equals(string033FGType)){
                            messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                            jtabbedPane1.setSelectedIndex(intTablePanel) ;
                            jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                            return  false ; 
                        }
                        if(stringSpecBudget.indexOf(stringDepartNo)  !=  -1) {
                            messagebox(stringDepartNo+" �����\�M�䥦�����ήקO�@�P���u�C\n(�����D�Ь� [�]�ȫ�])") ;
                            jtabbedPane1.setSelectedIndex(intTablePanel) ;
                            jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                            return  false ;                             
                        }
                        string033FGType  =  "2" ;
                    }
                    System.out.println(intNo+"string033FGType("+string033FGType+")----------------------------------------2") ;
                    if("1".equals(string033FGType)){
                        if(!stringDepartNo.equals(stringDocNo1)) {
                            messagebox("�קO���u���u�� ["+stringDepartNo+"] �ɡA����s������ "+stringDocNo1+"�C\n(�����D�Ь� [�]�ȫ�])") ;
                            jtabbedPane1.setSelectedIndex(intTablePanel) ;
                            jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                            return  false ; 
                        }
                    }
                    if("2".equals(string033FGType)){
                        if(stringSpecBudget.indexOf(stringDocNo1)  !=  -1) {
                            messagebox("����s���� "+stringDocNo1+"�ɡA�קO���u�����Ȥ��\ ["+stringDepartNo+"]�C\n(�����D�Ь� [�]�ȫ�])") ;
                            jtabbedPane1.setSelectedIndex(intTablePanel) ;
                            jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                            return  false ; 
                        }
                    }
                }
            }
            // �H�خקO�ˬd
            /*if("Z6".equals(stringComNo)  &&  !"".equals(stringProjectID1)&&  "H56A,H85A".indexOf(stringProjectID1)!=-1) {
                messagebox("�� " +(intNo+1) +" �C [�����Ы�] �����\�ϥ� H56A�BH85A �� �קO�I�A�����\���ʸ�Ʈw�C\n(�����D�Ь� [�]�ȫ�])") ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            }*/
            //if("B3018".equals(getUser())) {
            //    messagebox(sringProjectIDComput) ;
            //}
            if(!booleanDepartProjectIDSame) {
                if(boolean053Start) {
                    if("0531".equals(stringDepartNo)  &&  sringProjectIDComput.indexOf(","+stringProjectID1+",")!=-1) booleanDepartProjectIDSame  =  true ;
                } else {
                    if(!"0531".equals(stringDepartNo)  &&  sringProjectIDComput.indexOf(","+stringProjectID1+",")!=-1) booleanDepartProjectIDSame  =  true ;
                }
            }
            //
            if("0531".equals(stringDepartNo)) {
                stringProjectID1Use  =  stringDepartNo.substring(0,  3)  +  stringProjectID1;
            } else {
                stringProjectID1Use  =  stringProjectID1 ;
            }
            //
            if("".equals(stringProjectID)) {
                stringKey  =  stringDepartNo ;
            } else {
                stringKey  =  stringProjectID+"---"+stringProjectID1 ;
            }
            if(!"".equals(stringActionNo)  &&  vectorProject.indexOf(stringKey)==-1  &&  "E02573,".indexOf(stringBarCode+",")==-1) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [�קO] ("+stringProjectID+"---"+stringProjectID1+") ���s�b [���ʥN�X] ���C\n(�����D�Ь� [��P������])",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            } else {
                if(booleanRealMoney) {
                    stringTemp  =  stringRealMoney ;
                } else {
                    stringTemp   =  stringBudgetMoney ;
                }
                doubleActionMoney  +=  exeFun.doParseDouble(stringTemp) ;
                stringTemp                 =  ""+(exeUtil.doParseDouble(""+hashtableThisUseMoney.get(stringKey))+exeUtil.doParseDouble(stringTemp)) ;
                hashtableThisUseMoney.put(stringKey,  stringTemp) ;
            }
            //
            stringKey                    =  stringCostID            +  "-"  +  stringCostID1 ;
            if("I".equals(stringInOut)) {
                stringDulKey              =  stringDepartNo  +  "-"  +  stringKey ;
            } else {
                stringDulKey              =  stringProjectID1Use  +  "-"  +  stringKey ;
            }
            //
            /*if(exeUtil.doParseDouble(stringRealMoney)  ==  0) {
                if(booleanUser){
                  JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [���B]  ���i�� 0�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                  jtabbedPane1.setSelectedIndex(intTablePanel) ;
                  jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                  return  false ; 
                }
            }*/
            // �����ˮ�
            if(vectorDulKey.indexOf(stringDulKey)  !=  -1) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [�קO] [�j-�дڥN�X] [��-�дڥN�X] �o�ͭ��ơC\n(�����D�Ь� [��P�޲z��])",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            }
            vectorDulKey.add(stringDulKey) ;
            // �дڥN�X                   CostID�BCostID1
            if("".equals(stringCostID)) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [�j-�дڥN�X] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            }
            if("".equals(stringCostID1)) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [��-�дڥN�X] ���i���ťաC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            }
            retDoc7M011  =  exeFun.getDoc7M011(stringComNo,  "",  stringCostID,  stringCostID1) ;
            if(retDoc7M011.length==0) {
                if("1".equals(string033FGType)  ||  exeFun.getDoc2M0201(stringComNo,  stringCostID,  stringCostID1,  "A").length  ==  0) {
                    JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� �дڥN�X ���s�b�� [�w��O�ι�ӧ@�~(Doc7M015)] ���C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    jtabbedPane1.setSelectedIndex(intTablePanel) ;
                    jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                    return  false ;               
                }
            }
            if(vectorDeptCd2.indexOf(stringDepartNo)==-1   &&  vectorDeptCd.indexOf(stringDepartNo)==-1   &&  !exeFun.isInOutToCostID(stringInOut.trim( ),  stringCostID.trim( ))) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C [�дڥN�X] �P [��/�~�~] ���@�P�C\n(�����D�Ь� [��P�޲z��])",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ; 
            }
            if(vectorCostID.indexOf(stringKey)  ==  -1)       vectorCostID.add(stringKey) ;
            // ��@�дڥN�X�ˮ� 
            if(vectorSpecCostID.size()>0  &&  vectorSpecCostID.indexOf(stringCostID+stringCostID1)!=-1) {
                if(vectorCostID.size()  >  1) {
                    JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �S�� [�дڥN�X] �����\�M�䥦 [�дڥN�X] �@�_���ʡC\n(�����D�Ь� [��T������])",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    jtabbedPane1.setSelectedIndex(intTablePanel) ;
                    jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                    return  false ;                 
                }
            }
            /* �׶��q�վ� �s�b�ˮ� */
            stringBudgetID  =  (retDoc7M011.length>0) ? retDoc7M011[0][0].trim() : "" ;
            stringType         =  (stringBudgetID.length()>0) ? stringBudgetID.substring(0,  1) : "" ;
            // "H45A".equals(stringProjectID1)  &&  
            if("1".equals(string033FGType)  &&  !"B".equals(stringType)  &&  ",J20996,".indexOf(","+stringBarCode+",")==-1) {
                messagebox("�� "  +  (intNo+1)  +  " �C "+stringDocNo1+" �u���\�ϥΥ����� [�дڥN�X]�C\n(�����D�Ь� [�]�ȫ�])") ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ;                   
            }
            if("03365,".indexOf(stringDepartNo+",")!=-1  &&  "A".equals(stringType)) {
                stringProjectID1Use  =  stringDepartNo ;
            }
            if("B".equals(stringType)  &&  "F1".equals(stringProjectID1)  &&  "23,26,49,72".indexOf(stringCostID+",")==-1) {
                messagebox("�� "  +  (intNo+1)  +  " �C �קO F1 �u���\�ӽЯS�w [�дڥN�X] (23,26,49,72)�C\n(�����D�Ь� [�]�ȫ�])") ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable3.setRowSelectionInterval(intNo,  intNo)  ;
                return  false ;   
            }
            booleanTemp    =  !("001".equals(stringCostID+stringCostID1))  &&  
                                           !"".equals(stringProjectID1Use)  &&  
                             !"".equals(stringType)  &&  
                             vectorProjectID1Type.indexOf(stringProjectID1Use+"-"+stringType)  ==  -1  &&
                             vectorProjectID1NoUseBudget.indexOf(stringDepartNo+"---"+stringProjectID+"---"+stringProjectID1)  ==  -1 ;  // �S��קO�A���@�w���ˮ�
            if(vectorDeptCd.indexOf(stringDepartNo)!=-1  &&  "B".equals(stringType)) {
                stringProjectID1Use  =  stringDepartNo ;
                booleanTemp           =  false ;
            }
            if(booleanTemp) {
                stringStageDateStart       =  "" ; 
                stringStageDateEnd        =  "" ; 
                retDoc7M020                  =  exeFun.getDoc7M020ForComNo(stringComNo,  stringProjectID1Use,  stringType,  stringNeedDateAC,  "<=",  "",  "U") ;
                if(retDoc7M020.length  ==  0) {
                    booleanTemp              =  true ;
                    stringDateStage          =  "00" ;
                } else {
                    booleanTemp              =  false ;
                }
                // 0  BuildYMD        1  DateStage                                      2  BudgetMoney                   3  StageDateStart      4  StageDateEnd
                // 5  DateStart            
                vectorProjectID1Type.add(stringProjectID1Use+"-"+stringType) ;
            }
            if(!"".equals(stringProjectID1Use)  &&  vectorProjectID1BudgetID.indexOf(stringProjectID1Use+"-"+stringBudgetID)  ==  -1
                                                                   &&  vectorProjectID1NoUseBudget.indexOf(stringDepartNo+"---"+stringProjectID+"---"+stringProjectID1)  ==  -1) vectorProjectID1BudgetID.add(stringProjectID1Use+"-"+stringBudgetID) ;
            // �`���B ������ ���ʸ�T�`���B  RealMoney
            doubleRealMoney  =  exeUtil.doParseDouble(stringRealMoney)  ;
            /*if(booleanUser  &&  doubleRealMoney  ==  0) {
                JOptionPane.showMessageDialog(null,  "�� "  +  (intNo+1)  +  " �C �� [�`���B] ���i�� 0�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table3",  intNo,  "RealMoney") ;
                return  false ; 
            }*/
            // ��ƳB�z
            doubleRealMoneySum        =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKey)) ;
            doubleRealMoneySum      +=  doubleRealMoney ;
            hashtableRealMoney.put(stringKey,                ""+doubleRealMoneySum) ;   // [���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
            //
            doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringProjectID1Use+"-"+stringType)) ;
            doubleRealMoneySum  +=  doubleRealMoney ;
            hashtableBudgetMoney.put(stringProjectID1Use+"-"+stringType,  ""+doubleRealMoneySum) ;    // �w���ˮ֮ɨϥ�
            //System.out.println(stringProjectID1Use+"-"+stringType+"]-------------------------------------------------"+doubleRealMoneySum) ;
            //
            doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableBudgetMoneyBudgetID.get(stringProjectID1Use+"-"+stringBudgetID)) ;
            doubleRealMoneySum  +=  doubleRealMoney ;
            hashtableBudgetMoneyBudgetID.put(stringProjectID1Use+"-"+stringBudgetID,  ""+doubleRealMoneySum) ;    // �w���ˮ֮ɨϥ�
            //System.out.println(stringProjectID1Use+"-"+stringBudgetID+"]-------------------------------------------------"+doubleRealMoneySum) ;
            // �w����B
            doubleBudgetMoney        =  exeUtil.doParseDouble(stringBudgetMoney)  ;
            doubleRealMoneySum     =  exeUtil.doParseDouble(""+hashtableBudgetMoney2.get(stringProjectID1Use+"-"+stringType)) ;
            doubleRealMoneySum   +=  doubleBudgetMoney ;
            hashtableBudgetMoney2.put(stringProjectID1Use+"-"+stringType,  ""+doubleRealMoneySum) ; 
            //System.out.println(stringProjectID1Use+"-"+stringType+"]-------------------------------------------------"+doubleRealMoneySum) ;
            //
            doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableBudgetMoneyBudgetID2.get(stringProjectID1Use+"-"+stringBudgetID)) ;
            doubleRealMoneySum  +=  doubleBudgetMoney ;
            hashtableBudgetMoneyBudgetID2.put(stringProjectID1Use+"-"+stringBudgetID,  ""+doubleRealMoneySum) ;   // �w���ˮ֮ɨϥ�
            //System.out.println(stringProjectID1Use+"-"+stringBudgetID+"]-------------------------------------------------"+doubleRealMoneySum) ;
        }
        // 805 
        String  stringCheckAdd            =  getValue("CheckAdd").trim();
        String  stringCheckAddDescript    =  getValue("CheckAddDescript").trim();
        if(booleanCostID805) {
            if(!"F".equals(stringApplyType)) {
                  if(!"F".equals(stringCheckAdd)) {
                        // �D���� �D�䥦
                        setValue("CheckAdd",          "F") ;
                        setValue("CheckAddDescript",   "ñ�e�s���G") ;
                        messagebox("�дڥN�X 805 �ث~ �D���ʮɡA[�˪�] ������� �䥦�A[�˪�����] ������J ñ�e�s���C") ;
                        return  false ;
                  } else {
                        if(stringCheckAddDescript.indexOf("ñ�e�s��")  ==  -1) {
                            messagebox("�дڥN�X 805 �ث~ �D���ʮɡA[�˪�] ������� �䥦�A[�˪�����] ������J ñ�e�s���C") ;
                            return  false ;
                        }
                  }
            }
        }
        // �M���ˬd 
        if(!"".equals(stringActionNo)  &&  !(!"".equals(stringBarCode)  &&  "E02573,".indexOf(stringBarCode+",")!=-1)) {
            String         stringKeyL                                  =  "" ;
            Hashtable  hashtableUseMoneyDoc3M011  =  getUseMoneyDoc3M011(stringBarCode,  stringActionNo, exeFun,  exeUtil) ;
            double       doubleUseMoney                         =  0 ;
            double       doubleCheckMoney                     =  0 ;
            for(int  intNo=0  ;  intNo<vectorProject.size()  ;  intNo++) {
                stringKeyL                =  ""+vectorProject.get(intNo) ;
                doubleUseMoney      =  exeUtil.doParseDouble(""+hashtableUseMoneyDoc3M011.get(stringKeyL))  +  exeUtil.doParseDouble(""+hashtableThisUseMoney.get(stringKeyL)) ;
                doubleCheckMoney  =  exeUtil.doParseDouble(""+hashtableDoc3M043.get(stringKeyL)) ;
                if(doubleCheckMoney+100  <  doubleUseMoney) {
                    JOptionPane.showMessageDialog(null,  "�קO("+stringKeyL+") �w�ϥΪ��B("+exeUtil.getFormatNum2 (""+doubleUseMoney)+")�W�L�M�ץi�Ϊ��B("+exeUtil.getFormatNum2 (""+doubleCheckMoney)+")�C\n(�����D�Ь� [��P������])",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    jtabbedPane1.setSelectedIndex(intTablePanel) ;
                    return  false ;   
                }
            }
        }
        // �����B�קO���u�@�P�ˮ�
        if(!booleanDepartProjectIDSame  &&  !"".equals(stringBarCode)  &&  "E43509,E45116,E43517,".indexOf(stringBarCode)  ==  -1) {
            boolean  booleanError  =  true ;
            if(",J38125,".indexOf(","+stringBarCode+",")  !=  -1) {
                booleanError  =  false ;
            }
            if(booleanError) {
                JOptionPane.showMessageDialog(null,  "�������s�b�קO���u���C("+sringProjectIDComput+")",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                return  false ;   
            }
        }
        if((stringFlow.indexOf("����")!=-1  ||  booleanApply)  &&  booleanUndergoWriteCheck) {
            //Hashtable         hashtableRealMoneyS                  =  (Hashtable) get("Doc3M010_PurchaseMoney_CostID") ;    // [���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
            Hashtable         hashtableRealMoneyS                  =  getCostIDMoneyTable1(exeUtil) ;     // [���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
            for(int  intNo=0  ;  intNo<vectorCostID.size()  ;  intNo++) {
                stringKey                         =  (""+vectorCostID.get(intNo)).trim() ;
                doubleRealMoneySum    =  exeUtil.doParseDouble(""+hashtableRealMoney.get(stringKey)) ;
                doubleRealMoneySumS  =  exeUtil.doParseDouble(""+hashtableRealMoneyS.get(stringKey)) ;
                //System.out.println(intNo+"[���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�-----stringKey("+stringKey+")doubleRealMoneySum("+doubleRealMoneySum+")doubleRealMoneySumS("+doubleRealMoneySumS+")----------------------------------------") ;
                // �����G[���ʸ�T] �� [�קO���u] ���B �@�P�ˮ�
                if(doubleRealMoneySum  !=  doubleRealMoneySumS) {
                    String[]  arrayCostID  =  convert.StringToken(stringKey,  "-") ;
                    //
                    stringCostID   =  "" ;
                    stringCostID1  =  "" ;
                    if(arrayCostID.length  ==  2) {
                        stringCostID    =  arrayCostID[0] ;
                        stringCostID1  =  arrayCostID[1] ;
                    }
                    JOptionPane.showMessageDialog(null,  "�дڥN�X("+stringCostID+")�B�p�дڥN�X("+stringCostID1+") �� ������ [ �קO���u]("+doubleRealMoneySum+") �� [���ʸ�T]("+doubleRealMoneySumS+") �`���B���۵��C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
                    return  false ;
                }
            }
        }

        if(!booleanUndergoWriteCheck)  return  true ;
        //
        vectorProjectID1Type  =  new  Vector() ;
        //
        String         stringBuildYMD                    =  "" ;
        String         stringBudgetName                =  "" ;
        String         stringDateStartRoc              =  "" ;
        String         stringErrorMessage             =  "" ;
        String         stringMailMessage               =  "" ;
        String         stringEmployeeNo               =  "" ;
        String         stringProjectID1F                 =  "" ;
        String         stringStageName                 =  "" ;
        String[]       retMoneyCheck                    =  null ;
        String[]       retMoneyCheck2                  =  null ;
        String[]       arrayTemp                           =  null ;
        String[]       arrayProjectID1BudgetID   =  (String[]) vectorProjectID1BudgetID.toArray(new  String[0]) ;
        String[][]     retDoc7M012                      =  null ;
        String[][]     retDoc7M019                      =  null ;
        String[][]     retDoc7M021                      =  null ;
        double       doubleBudgetMoneyL         =  0 ;
        double       doubleBudgetMoneyL2         =  0 ;
        double       doubleBudgetMoneySum    = 0 ;
        double       doubleBudgetMoneySumL  =  0 ;
        double       doubleBudgetMoneySumL2  =  0 ;
        double       doublePurchaseMoney       =  0 ;
        double       doublePurchaseMoney2     = 0 ;
        boolean     booleanError                       =  true  ;
        boolean     booleanCheckNew               =  (","+stringSpecBudget+",").indexOf(","+stringDocNo1+",")!=-1 ;
        Hashtable  hashtableUserIDMessage  =  new  Hashtable() ;
        Vector        vectorUserID                      =  new  Vector() ;
        //
        Arrays.sort(arrayProjectID1BudgetID) ;
        if(booleanCheckNew) {
            // 033FG �w��S�O�ˬd
            getButton("Button033FG").doClick() ;
            //
            String[][]  retTableData  =  getTableData("TableCheck") ;
            if(retTableData.length==0) {
                messagebox("��Ƶo�Ϳ��~�A�Ь���T�ǡC2") ;
                return  false ;
            }
            if(retTableData.length==1  &&  "OK".equals(retTableData[0][0])) {
                return  true ;
            }
            booleanError  =  false ;
        } else {
            setTableData("Table7",  new  String[0][0]) ;
            setTableData("Table8",  new  String[0][0]) ;
            System.out.println("�~���ˮ�------------------------------------------------S���ʸ�Ʈw") ;
            Vector  vectorTableCheck  =  new  Vector() ;
            for(int  intNo=0  ;  intNo<arrayProjectID1BudgetID.length  ;  intNo++) {
                stringTemp                            =  arrayProjectID1BudgetID[intNo].trim() ;
                arrayTemp                            =  convert.StringToken(stringTemp,  "-") ;               
                stringProjectID1                    =  arrayTemp[0].trim() ;      stringProjectID1F  =  stringProjectID1 ;  if("".equals(stringProjectID1))  continue ;
                stringBudgetID                      =  arrayTemp[1].trim() ;                                                                        if("".equals(stringBudgetID))     continue ;
                stringType                             =  stringBudgetID.substring(0,  1) ;
                doubleBudgetMoneySumL    =  exeUtil.doParseDouble(""+hashtableBudgetMoney.get(stringProjectID1+"-"+stringType)) ;
                doubleBudgetMoneySumL2  =  exeUtil.doParseDouble(""+hashtableBudgetMoney2.get(stringProjectID1+"-"+stringType)) ;
                doubleBudgetMoneyL           =  exeUtil.doParseDouble(""+hashtableBudgetMoneyBudgetID.get(stringProjectID1+"-"+stringBudgetID)) ;
                doubleBudgetMoneyL2        =  exeUtil.doParseDouble(""+hashtableBudgetMoneyBudgetID2.get(stringProjectID1+"-"+stringBudgetID)) ;
                //
                arrayTemp                           =  new  String[1] ;
                arrayTemp[0]                       =  stringProjectID1+"%-%"+stringBudgetID+"%-%"+doubleBudgetMoneyL+"%-%"+doubleBudgetMoneySumL+"%-%"+doubleBudgetMoneyL2+"%-%"+doubleBudgetMoneySumL2 ;
                vectorTableCheck.add(arrayTemp) ;
            }

            if(vectorTableCheck.size()  >  0) {
                setTableData("TableCheck",  (String[][])vectorTableCheck.toArray(new  String[0][0])) ;
                getButton("ButtonTableCheck").doClick() ;
                String[][]  retTableData  =  getTableData("TableCheck") ;
                if(retTableData.length==0) {
                    messagebox("��Ƶo�Ϳ��~�A�Ь���T�ǡC3") ;
                    return  false ;
                }
                if(retTableData.length==1  &&  "OK".equals(retTableData[0][0]))     return  true ;
                return  false ;
            } else {
                return  true ;
            }
        }
        return  booleanError ;
    }
    public  Hashtable  getCostIDMoneyTable1(FargloryUtil  exeUtil) throws  Throwable {
        Hashtable         hashtablePurchaseMoney                       =  new  Hashtable( ) ;
        JTable               jtable1                                                     =  getTable("Table1") ;
        String                stringApplyMoney                                   =  "" ;
        String                stringCostID                                            =  "" ;
        String                stringCostID1                                          =  "" ;
        String                stringKey                                                 =  "" ;
        String                stringPurchaseMoney                             =  "" ;
        String                stringFlow                                               =  getFunctionName() ;
        double              doublePurchaseMoney                           =  0 ;
        boolean             booleanFlag                                            =  stringFlow.indexOf("����")  ==  -1  ;
        for(int  intNo=0  ;  intNo<jtable1.getRowCount() ;  intNo++) {
            stringApplyMoney             =  (""+getValueAt("Table1",  intNo,  "ApplyMoney")).trim() ;  // �w����B
            stringPurchaseMoney       =  (""+getValueAt("Table1",  intNo,  "PurchaseMoney")).trim() ;     // ���ʪ��B
            stringCostID                      =  (""+getValueAt("Table1",  intNo,  "CostID")).trim() ;
            stringCostID1                    =  (""+getValueAt("Table1",  intNo,  "CostID1")).trim() ; 
            //
            stringKey                           =  stringCostID  +  "-"  +  stringCostID1 ;
            //
            doublePurchaseMoney                  =  exeUtil.doParseDouble(stringPurchaseMoney) ;
            if(booleanFlag)     doublePurchaseMoney  =  exeUtil.doParseDouble(stringApplyMoney) ;
            //
            doublePurchaseMoney          +=  exeUtil.doParseDouble(""+hashtablePurchaseMoney.get(stringKey)) ;
            hashtablePurchaseMoney.put(stringKey,  ""+doublePurchaseMoney) ;
            //System.out.println(intNo+"getCostIDMoneyTable1-----stringKey("+stringKey+")doublePurchaseMoney("+doublePurchaseMoney+")----------------------------------------") ;
        }
        return  hashtablePurchaseMoney;
    }
    public  Vector  getFunctionTypeUDoc2M0201 (FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable{
        String            stringComNo                 =  getValue("ComNo").trim() ;
        String              stringCDateAC                 =  exeUtil.getDateConvert(getValue("CDate").trim()) ;
        String            stringSqlAnd                  =  " AND  (ComNo  =  '"+stringComNo+"'  OR  ComNo  =  'ALL') "  +
                                          " AND  FunctionType  LIKE  '%U%' " +
                                          " AND  (DateStart  <=  '"+stringCDateAC+"'  OR  DateStart  = '9999/99/99') " +
                                          " AND  (DateEnd  >=  '"+stringCDateAC+"') " ;
        String             stringCostID                     =  "" ;
        String             stringCostID1                   =  "" ;
        Vector            vectorSpecCostID        =  new  Vector() ;
        Vector            vectorDoc2M0201           =  exeFun.getQueryDataHashtableDoc("Doc2M0201",  new  Hashtable(),  stringSqlAnd,  new  Vector(),  exeUtil) ;
        Hashtable     hashtableDoc2M0201    =  null ;
        for(int  intNo=0  ;  intNo<vectorDoc2M0201.size()  ;  intNo++) {
            hashtableDoc2M0201  =  (Hashtable) vectorDoc2M0201.get(intNo) ;  if(hashtableDoc2M0201  ==  null)  continue ;
            stringCostID                 =  ""+hashtableDoc2M0201.get("CostID") ;        
            stringCostID1               =  ""+hashtableDoc2M0201.get("CostID1") ;        
            //
            System.out.println(intNo+"getFunctionTypeUDoc2M0201("+(stringCostID+stringCostID1)+")---------------------------------") ;
            if(vectorSpecCostID.indexOf(stringCostID+stringCostID1)  ==  -1)  vectorSpecCostID.add(stringCostID+stringCostID1) ;
        }
        return  vectorSpecCostID ;
    }
    public  boolean  isCoinTypeCheckOK(FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTable                jtable3                                         =  getTable("Table3") ;
        String                stringComNo                                 =  getValue("ComNo").trim() ;
        String                stringInOut                                    = "" ;
        String                stringProjectID1                            = "" ;
        String                stringCoinTypeL                           = "" ;
        String                stringCoinType                             = "" ;
        Vector                vectorProjectID1                          =  new  Vector() ;
        Hashtable         hashtableAnd                               =  new  Hashtable() ;
        //
        for(int  intNo=0 ;  intNo<jtable3.getRowCount( )  ;  intNo++) {
            stringProjectID1         =  (""+getValueAt("Table3",  intNo,  "ProjectID1")).trim() ;
            stringInOut                 =  (""+getValueAt("Table3",  intNo,  "InOut")).trim() ;
            //
            if("I".equals(stringInOut)) {
                stringCoinTypeL  =  "NTD" ;
            } else {
                if(vectorProjectID1.indexOf(stringProjectID1)  !=  -1)  continue ;
                vectorProjectID1.add(stringProjectID1) ;
                //
                hashtableAnd.put("ComNo",        stringComNo) ;
                hashtableAnd.put("ProjectID1",  stringProjectID1) ;
                stringCoinTypeL   =  exeFun.getNameUnionDoc("CoinType",  "Doc7M0204",  "",  hashtableAnd,  exeUtil) ;
                if("".equals(stringCoinTypeL)) {
                    hashtableAnd.put("ComNo",        stringComNo) ;
                    hashtableAnd.put("ProjectID1",  stringProjectID1) ;
                    stringCoinTypeL   =  exeFun.getNameUnionDoc("CoinType",  "Doc7M020",  "",  hashtableAnd,  exeUtil) ;
                }
                if("".equals(stringCoinTypeL))  stringCoinTypeL  =  "NTD" ;
            }
            //
            if(!"".equals(stringCoinTypeL)  &&  !"".equals(stringCoinType)  &&  !stringCoinType.equals(stringCoinTypeL)) {
                messagebox("���ʳ� �����\�h���ȡC") ;
                return  false ;
            }
            stringCoinType  =  stringCoinTypeL ;
        }
        //
        setValue("CoinType",  stringCoinType) ;
        return   true ;
    }
    public  void  doMailData(String  stringProjectID1,  String  stringType,  String  stringMailMessage,  Hashtable  hashtableUserIDMessage,  Vector  vectorUserID,  Doc.Doc2M010  exeFun) throws  Throwable {
        String      stringEmployeeNo  =   "" ;
        String      stringTemp             =  "" ;
        String[][]  retDoc7M019         =  exeFun.getDoc7M019(stringProjectID1,  "",  stringType,  "A") ;
        for(int  intDoc7M019=0  ;  intDoc7M019<retDoc7M019.length  ;  intDoc7M019++) {
            stringEmployeeNo  =  retDoc7M019[intDoc7M019][1].trim() ;
            stringTemp             =  ""+hashtableUserIDMessage.get(stringEmployeeNo) ;
            if("null".equals(stringTemp))  stringTemp    =  "" ;
            if(!"".equals(stringTemp))        stringTemp  +=  "<br>" ;
            stringTemp  +=  stringMailMessage ;
            hashtableUserIDMessage.put(stringEmployeeNo,  stringTemp) ;
            if(vectorUserID.indexOf(stringEmployeeNo)  ==  -1)  vectorUserID.add(stringEmployeeNo) ;
        }
        retDoc7M019  =  exeFun.getDoc7M019("ALL",  "",  stringType,  "A") ;
        for(int  intDoc7M019=0  ;  intDoc7M019<retDoc7M019.length  ;  intDoc7M019++) {
            stringEmployeeNo  =  retDoc7M019[intDoc7M019][1].trim() ;
            stringTemp             =  ""+hashtableUserIDMessage.get(stringEmployeeNo) ;
            if("null".equals(stringTemp))  stringTemp    =  "" ;
            if(!"".equals(stringTemp))        stringTemp  +=  "<br>" ;
            stringTemp  +=  stringMailMessage ;
            hashtableUserIDMessage.put(stringEmployeeNo,  stringTemp) ;
            if(vectorUserID.indexOf(stringEmployeeNo)  ==  -1)  vectorUserID.add(stringEmployeeNo) ;
        }
    }
    public  String  getProjectIDFromDepartNo(Farglory.util.FargloryUtil  exeUtil,  Doc.Doc2M010  exeFun) throws  Throwable {
        String    stringDepartNo  =  getValue("DepartNo").trim() ;
        String    stringBarCode   =  getValue("BarCode").trim() ;
        String    stringComNo     =  getValue("ComNo").trim() ;
        return  exeFun.getProjectIDFromDepartNo(stringDepartNo,  "",  stringBarCode,  stringComNo,  exeUtil) ; 
    }
    public  Vector  getCostIDTable3( ) throws  Throwable {
        Vector  vectorCostID        =  new  Vector( ) ;
        JTable   jtable3                =  getTable("Table3") ; 
        String    stringCostID       =  "" ;
        String    stringCostID1     =  "" ;
        String    stringKey            =  "" ;
        for(int  intNo=0 ;  intNo<jtable3.getRowCount( )  ;  intNo++) {
            stringCostID    =  (""+getValueAt("Table3",  intNo,  "CostID")).trim() ;
            stringCostID1  =  (""+getValueAt("Table3",  intNo,  "CostID1")).trim() ;
            stringKey         =  stringCostID  +  "-"  +  stringCostID1 ;
            if(vectorCostID.indexOf(stringKey)  !=  -1)  continue ;
            vectorCostID.add(stringKey) ;
        }
        return  vectorCostID ;
    }
    //
    public  String[][]  getDoc3M040(String  stringComNo,  String  stringActionNo,  Doc.Doc2M010  exeFun) throws  Throwable {
        String      stringSql                =  "" ;
        String      stringKey               =  "" ;
        String[][]  retDoc3M040        =  null ;
        //
        stringSql  =  "SELECT  UNDERGO_WRITE "  +
                             " FROM  Doc3M040 "  +
                   " WHERE  DocNo  =  '"   +  stringActionNo  +  "' " +
                         " AND  ComNo  =  '"  +  stringComNo    +  "' "  ;
        retDoc3M040  =  exeFun.getTableDataDoc(stringSql) ;
        return  retDoc3M040 ;
    }
    public  Hashtable  getDoc3M043(String  stringActionNo,  Vector  vectorProject,  Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        String        stringSql                     =  "" ;
        String         stringKey                   =  "" ;
        String         stringDateAC             =  exeUtil.getDateConvert(getValue("CDate").trim()) ;
        String         stringComNo              =  getValue("ComNo").trim() ;
        String[][]     retDoc3M043             =  null ;
        double       doubleTemp               =  0 ;
        Hashtable  hashtableDoc3M043  =  new  Hashtable() ;
        //
        if("".equals(stringActionNo)) {
            return  hashtableDoc3M043 ;
        }
        //
        stringSql  =  "SELECT  ProjectID,  ProjectID1,  UseMoney,  DepartNo "  +
                             " FROM  Doc3M043 "  +
                   " WHERE  RTRIM(DocNo1)+RTRIM(DocNo2)+RTRIM(DocNo3)  IN  (SELECT  DocNo "  +
                                                                                                                                   " FROM  Doc3M040 "  +
                                                                         " WHERE  UNDERGO_WRITE  =  'Y' "  +
                                                                               " AND  ComNo  =  '"        +  stringComNo     +  "' "  +
                                                                             " AND  DocNo  =  '"         +  stringActionNo  +  "' "  +
                                                                             " AND  DateStart  <=  '"  +  stringDateAC     +  "' "  +
                                                                             " AND  DateEnd   >=  '"   +  stringDateAC      +  "') "  ;
        retDoc3M043  =  exeFun.getTableDataDoc(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc3M043.length  ;  intNo++) {
            if("".equals(retDoc3M043[intNo][0].trim())) {
                stringKey  =  retDoc3M043[intNo][3].trim() ;
            } else {
                stringKey  =  retDoc3M043[intNo][0].trim()  +  "---"+retDoc3M043[intNo][1].trim() ;
                /*if("Z6".equals(stringComNo)) {
                    if("H56A,H85A,".indexOf(retDoc3M043[intNo][1].trim())  !=  -1)  continue ;
                }
                if("CS".equals(stringComNo)) {
                    if("H56A,H85A,".indexOf(retDoc3M043[intNo][1].trim())  ==  -1)  continue ;
                }*/
            }
            if(vectorProject.indexOf(stringKey)==-1)  vectorProject.add(stringKey) ;
            doubleTemp  =  exeUtil.doParseDouble(""+hashtableDoc3M043.get(stringKey))+exeUtil.doParseDouble(retDoc3M043[intNo][2].trim()) ;
            hashtableDoc3M043.put(stringKey,  convert.FourToFive(""+doubleTemp,  0)) ;
        }
        return  hashtableDoc3M043 ;
    }
    public  Hashtable  getUseMoneyDoc3M011(String  stringBarCode,  String  stringActionNo, Doc.Doc2M010  exeFun,  FargloryUtil  exeUtil) throws  Throwable {
        String        stringSql                                      =  "" ;
        String         stringKey                                     =  "" ;
        String         stringProjectID                            =  "" ;
        String         stringProjectID1                          =  "" ;
        String         stringDepartNo                           =  "" ;
        String[][]     retDoc3M014                              =  null ;
        double       doubleTemp                                =  0 ;
        Hashtable  hashtableUseMoneyDoc3M011  =  new  Hashtable() ;
        //
        stringSql  =  "SELECT  ProjectID,  ProjectID1,  (RealMoney-NoUseRealMoney),  M14.DepartNo "  +
                             " FROM  Doc3M014 M14,  Doc3M011 M11 "  +
                   " WHERE  M14.BarCode  =  M11.BarCode "  +
                       " AND  (M11.UNDERGO_WRITE  IN  ('Y',  'C')  OR "  +
                               " (M11.UNDERGO_WRITE='S'  AND  ApplyType  =  'F'))"  +
                     " AND  M11.UNDERGO_WRITE  <>  'X' "  +
                     " AND  M11.BarCode  <>  '" +  stringBarCode  +  "' "  +
                     " AND  M11.ActionNo  =  '" +  stringActionNo  +  "' "  ;
        retDoc3M014  =  exeFun.getTableDataDoc(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc3M014.length  ;  intNo++) {
            stringProjectID    =  retDoc3M014[intNo][0].trim() ;
            stringProjectID1  =  retDoc3M014[intNo][1].trim() ;
            stringDepartNo  =  retDoc3M014[intNo][3].trim() ;
            if("".equals(stringProjectID)  &&  "".equals(stringProjectID1)) {
                stringKey            =  stringDepartNo ;
            } else {
                stringKey            =  stringProjectID  +  "---"+stringProjectID1 ;
            }
            doubleTemp  =  exeUtil.doParseDouble(""+hashtableUseMoneyDoc3M011.get(stringKey))+exeUtil.doParseDouble(retDoc3M014[intNo][2].trim()) ;
            hashtableUseMoneyDoc3M011.put(stringKey,  convert.FourToFive(""+doubleTemp,  0)) ;
        }
        stringSql  =  "SELECT  ProjectID,  ProjectID1,  (RealMoney-NoUseRealMoney),  M14.DepartNo "  +
                             " FROM  Doc3M014 M14,  Doc3M011 M11 "  +
                   " WHERE  M14.BarCode  =  M11.BarCode "  +
                       " AND  NOT  (M11.UNDERGO_WRITE  IN  ('Y',  'C')  OR "  +
                                        " (M11.UNDERGO_WRITE='S'  AND  ApplyType  =  'F'))"  +
                     " AND  M11.UNDERGO_WRITE  <>  'X' "  +
                     " AND  M11.BarCode  <>  '" +  stringBarCode  +  "' "  +
                     " AND  M11.ActionNo  =  '" +  stringActionNo  +  "' "  ;
        retDoc3M014  =  exeFun.getTableDataDoc(stringSql) ;
        for(int  intNo=0  ;  intNo<retDoc3M014.length  ;  intNo++) {
            stringProjectID    =  retDoc3M014[intNo][0].trim() ;
            stringProjectID1  =  retDoc3M014[intNo][1].trim() ;
            stringDepartNo   =  retDoc3M014[intNo][3].trim() ;
            if("".equals(stringProjectID)  &&  "".equals(stringProjectID1)) {
                stringKey            =  stringDepartNo ;
            } else {
                stringKey            =  stringProjectID  +  "---"+stringProjectID1 ;
            }
            doubleTemp  =  exeUtil.doParseDouble(""+hashtableUseMoneyDoc3M011.get(stringKey))+exeUtil.doParseDouble(retDoc3M014[intNo][2].trim()) ;
            hashtableUseMoneyDoc3M011.put(stringKey,  convert.FourToFive(""+doubleTemp,  0)) ;
        }
        return  hashtableUseMoneyDoc3M011 ;
    }
    public  String[][]  getDoc8M010(Doc.Doc2M010  exeFun) throws  Throwable {
        String      stringSql         =  "" ;
        String     stringComNo    = getValue("ComNo").trim();
        String     stringDocNo     = getValue("DocNo").trim();
        String     stringBarCode = getValue("BarCode").trim();
        String[][]  retDoc8M010  =  null ;
        // 0  �ƥ�      1  AcceptRealDate       2  PickRealDate
        stringSql  =    " SELECT  COUNT(*),   AcceptRealDate,  PickRealDate "  +
                              " FROM  Doc8M010 "  +
                        " WHERE  ComNo  =  '"     +  convert.ToSql(stringComNo)     +  "' "  +
                              " AND  BarCode  =  '"  +  convert.ToSql(stringBarCode)  +  "' "  +
                              " AND  DocNo  =  '"     +  convert.ToSql(stringDocNo)      +  "' "  +
                  " GROUP BY  AcceptRealDate,  PickRealDate " ;
        retDoc8M010  =  exeFun.getTableDataDoc(stringSql) ;
        return  retDoc8M010 ;
    }
    
    
    public  boolean  isTable12CheckOK(FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTabbedPane jtabbedPane1                =  getTabbedPane("Tab1") ;
        int                 intTablePanel                 =  3 ;
        JTable      jtable10              =  getTable("Table10") ;  // ñ�֬y�{ ���
        JTable      jtable12              =  getTable("Table12") ;  // ����        ���
        String      stringRecordNo        =  "" ;
        String      stringPutDateTime     =  "" ;
        String      stringEmployeeNo        =  "" ;
        String      stringBuildYM           =  "" ;
        String      stringYYYYMM          =  exeUtil.doSubstring(getValue("EDateTime"),  0,  7).replaceAll("/",  "") ;
        String      stringStubPath        =  "" ;
        String      stringDescript            =  "" ;
        String      stringDocumentName    =  "";
        String      stringFunctionName    =  getFunctionName() ;
        Vector      vectorRecordNo          =  new  Vector() ;
        Hashtable  hashtableStubPath      =  new  Hashtable() ;
        //
        System.out.println("isTable12CheckOK------------------------1") ;
        System.out.println("jtable10("+jtable10.getRowCount()+")------------------------2") ;
        System.out.println("jtable12("+jtable12.getRowCount()+")------------------------3") ;
        /*if(jtable10.getRowCount()==0) {
            System.out.println("isTable12CheckOK------------------------4") ;
            return  true ;
        }*/
        // �u�Wñ�֮ɡA���󤣥i���ť�
        if(jtable10.getRowCount()>0  &&  jtable12.getRowCount()==0  &&  stringFunctionName.indexOf("����")==-1) {
            if(!isTable12NoPageOK(exeUtil,  exeFun)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                messagebox("�u�Wñ�֮ɡA���󤣥i���ť�") ;
                return  false ;
            }
        }
        boolean  booleanFlag  =  false ;
        for(int  intNo=0  ;  intNo<jtable12.getRowCount()  ;  intNo++) {
            stringRecordNo       =  (""+getValueAt("Table12",  intNo,  "RecordNo")).trim() ;
            stringPutDateTime  =  (""+getValueAt("Table12",  intNo,  "PutDateTime")).trim() ;
            stringEmployeeNo   =  (""+getValueAt("Table12",  intNo,  "EmployeeNo")).trim() ;
            stringBuildYM           =  (""+getValueAt("Table12",  intNo,  "BuildYM")).trim() ;
            stringStubPath         =  (""+getValueAt("Table12",  intNo,  "StubPath")).trim() ;
            stringDocumentName=  (""+getValueAt("Table12",  intNo,  "DocumentName")).trim() ;
            stringDescript           =  (""+getValueAt("Table12",  intNo,  "DocumentRemark")).trim() ;
            System.out.println(intNo+"Table12------------------------") ;
            //
            if("".equals(stringPutDateTime)) {
                setValueAt("Table12",  datetime.getTime("YYYY/mm/dd h:m:s"),  intNo,  "PutDateTime") ;
            }
            if("".equals(stringEmployeeNo)) {
                setValueAt("Table12",  getUser(),  intNo,  "EmployeeNo") ;
            }
            if("".equals(stringBuildYM)) {
                setValueAt("Table12",  stringYYYYMM,  intNo,  "BuildYM") ;
            }
            // �Ǹ�
            if("".equals(stringRecordNo)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 �Ǹ� ���i�ťաC") ;
                return  false ;
            }
            if(vectorRecordNo.indexOf(stringRecordNo)  !=  -1) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 �Ǹ� ���ơC") ;
                return  false ;
            }
            vectorRecordNo.add(stringRecordNo) ;
            // �W��
            /*if("".equals(stringDocumentName)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 �W�� ���i�ťաC") ;
                return  false ;
            }*/
            // ����
            if("".equals(stringDescript)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 ��ƦW�� ���i�ťաC") ;
                return  false ;
            }
            // ���|
            if("".equals(stringStubPath)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 ���| ���i�ťաC") ;
                return  false ;
            }
            if(stringStubPath.endsWith(".pdf")  ||  stringStubPath.endsWith(".PDF")) {
            } else if(stringStubPath.endsWith(".jpg")  ||  stringStubPath.endsWith(".JPG")) {
            } else if(stringStubPath.endsWith(".jpge")  ||  stringStubPath.endsWith(".JPGE")) {
            } else {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("�����椧�� "+(intNo+1)+" �椧 �u���\�W�� PDF �� JPG �ɮסC") ;
                return  false ;           
            }
            System.out.println(intNo+"Table12--isUpdateFileOK----------------------") ;
            booleanFlag  =  isUpdateFileOK(intNo,  stringStubPath,  stringYYYYMM,  hashtableStubPath) ;
            if(!booleanFlag) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                jtable12.setRowSelectionInterval(intNo,  intNo) ;
                return  false ;                       
            }
            System.out.println(intNo+"Table12--isUpdateFileOK("+booleanFlag+")----------------------") ;
        }
        return  true ;
    }
    public  boolean  isTable12NoPageOK(FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTable    jtable1             =  getTable("Table1") ;
        String    stringApplyType     =  getValue("ApplyType").trim() ;
        String    stringDocNo1      =  getValue("DocNo1").trim() ;
        String    stringCDate           =  exeUtil.getDateConvert(getValue("CDate")) ;
        String    stringCostID          =  "" ;
        String    stringCostID1       =  "" ;
        String    stringSqlAnd      =  " AND  CostIDType  =  'A' "  +
                                " AND  UseType  =  'A' "  +
                                " AND  DeptCd_Doc  LIKE  '"+exeUtil.doSubstring(stringDocNo1,  0,  3) +"%' " +
                                " AND  (UseDateStart  = '9999/99/99' OR  UseDateStart<='"+stringCDate+"') " +
                                " AND  (UseDateEnd  = '9999/99/99'   OR  UseDateEnd>='"+stringCDate+"') " ;
        Vector    vectorDoc5M0183   =  new  Vector() ;
        Vector    vectorCostID          =  new  Vector() ;
        Vector    vectorColumnName    =  new  Vector() ;
        //        
        vectorDoc5M0183  =  exeFun.getQueryDataHashtableDoc("Doc5M0183",  new  Hashtable(),  stringSqlAnd,  vectorColumnName,  exeUtil)  ;
        if(vectorDoc5M0183.size()  ==  0)  return  false ;
        for(int  intNo=0  ;  intNo<vectorDoc5M0183.size()  ;  intNo++) {
            stringCostID    =  exeUtil.getVectorFieldValue(vectorDoc5M0183,  intNo,  "CostID",  vectorColumnName) ;
            stringCostID1   =  exeUtil.getVectorFieldValue(vectorDoc5M0183,  intNo,  "CostID1",  vectorColumnName) ;
            //
            vectorCostID.add(stringCostID+stringCostID1) ;
        }
        //if(!"F".equals(stringApplyType))                return  false ;
        //
        for(int  intNo=0  ;  intNo<jtable1.getRowCount()  ;  intNo++) {
            stringCostID  =  (""+getValueAt("Table1",  intNo,  "CostID")).trim() ;
            stringCostID1  =  (""+getValueAt("Table1",  intNo,  "CostID1")).trim() ;
            //
            if(vectorCostID.indexOf(stringCostID+stringCostID1)==-1)  return  false ;
        }
        return  true ;
    }
    public boolean isUpdateFileOK(int  intRowPos,  String  stringStubPath,  String  stringYYYYMM,  Hashtable  hashtableStubPath)throws  Throwable {
        hashtableStubPath.put(""+intRowPos,  stringStubPath) ;
        // 2017/11/13 �ץ� �ɦW�L���X�s�����D
        String  stringBarCode       = getValue("BarCode").trim();
        String  stringBarCodeT    =  ""+get("BarCode_Tmp") ;          put("BarCode_Tmp",  "null") ;if(!"null".equals(stringBarCodeT)  &&  "".equals(stringBarCode))  stringBarCode  =  stringBarCodeT ;
        String  stringPutDateTime  =  datetime.getTime("YYYY/mm/dd h:m:s")  ;           stringPutDateTime  =  stringPutDateTime.replaceAll(" ",  "_").replaceAll("/",  "").replaceAll(":",  "") ;
        String  stringFullFileName  =  stringBarCode+"_"+stringPutDateTime+"_" ;
        String  stringFilePath          =  "g:\\02���ǰ�\\0151��T������\\01 �M�׷J�`��\\�t�ΤW�Ǥ��\\Doc\\���ʪ���\\"+stringYYYYMM+"" ;
        if(stringStubPath.startsWith(stringFilePath)) {
            return  true ;
        }
        getButton("ButtonMkDir").doClick() ;
        //
        String[][]  retTableData  =  getTableData("TableCheck") ;
        if(retTableData.length!=1)              return  false ;
        if(!"OK".equals(retTableData[0][0].trim())) return  false ;
        // �ɦW �榡�ˮ�(�ɦW���i���ť�)
        String[]  arrayFile         =  convert.StringToken(stringStubPath,  "\\") ;
        String    stringFileName  =  arrayFile[arrayFile.length-1].trim() ;
        String[]  arrayFileName    =  convert.StringToken(stringFileName,   " ") ;
        if(arrayFileName.length  >  1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i���ťաC") ;
            return  false ;
        }
        if(stringStubPath.indexOf("%")  !=  -1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i�� %�C") ;
            return  false ;         
        }
        if(stringStubPath.indexOf("=")  !=  -1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i�� ����(��)�C") ;
            return  false ;         
        }
        if(stringStubPath.indexOf(",")  !=  -1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i�� �r��(�A)�C") ;
            return  false ;         
        }
        if(stringStubPath.indexOf("&")  !=  -1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i�� &�C") ;
            return  false ;         
        }
        if(stringStubPath.indexOf("_")  !=  -1) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�]���ɮ׸��|���ɮצW�� ���i�� ���u(_)�C") ;
            return  false ;         
        }
        // �����ɮצs�b�ˮ�
        if(!(new  File(stringStubPath)).exists()) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C��[�W���ɮ�] ���s�b�C") ;
            return  false ;       
        }
        // �W���ɮ�
        String  stringFileTarget      =  stringFilePath+"\\"+stringFullFileName+stringFileName ;
        //
        stringStubPath     =  convert.replace(stringStubPath,  "\\",  "/") ;
        stringFileTarget   =  convert.replace(stringFileTarget,  "\\",  "/") ;
        if(!upload(stringStubPath,  stringFileTarget)) {
            doBackStubPath(stringFilePath,  hashtableStubPath) ;
            messagebox("�����椧�� "+(intRowPos+1)+" �C���W���ɮץ���("+stringStubPath+")�C") ;
            return  false ;
        }
        // 
        stringFileTarget  =  convert.replace(stringFileTarget,  "/",  "\\") ;
        setValueAt("Table12",  stringFileTarget,  intRowPos,  "StubPath") ;
        return  true ;
    }
    public void doBackStubPath(String  stringFilePath,  Hashtable  hashtableStubPath)throws  Throwable {
        JTable  jtable12                =  getTable("Table12") ;
        String   stringStubPathOld  =  "" ;
        for(int  intNo=0  ;  intNo<jtable12.getRowCount( )  ;  intNo++) {
            stringStubPathOld   =  (""+hashtableStubPath.get(""+intNo)).trim() ;    if("null".equals(stringStubPathOld))  continue ;
            //
            setValueAt("Table12",  stringStubPathOld,  intNo,  "StubPath") ;
        }
    }
    public  boolean  isTable18CheckOK(boolean  booleanUndergoWriteCheck,  FargloryUtil  exeUtil,  Doc2M010  exeFun) throws  Throwable {
        JTabbedPane   jtabbedPane1                       =  getTabbedPane("Tab1") ;
        int                   intTablePanel                       =  2 ;
        JTable        jtable1                       =  getTable("Table1") ;  
        JTable        jtable18                      =  getTable("Table18") ;  
        String        stringFactoryNo               =  "" ;
        String        stringPurchaseMoney           =  "" ;
        String        stringPurchaseMoneyFirst        =  "" ;
        String        stringPurchaseMoneyEnd      =  "" ;
        String        stringNoPageDate            =  ""+get("NO_PAGE_DATE") ;     if(stringNoPageDate.length()  !=  10)   stringNoPageDate  =  "2017/12/31" ;// ���ʵL�ȤƤW�u���
        String        stringToday                   =  exeUtil.getDateConvert(getValue("CDate")) ;
        String        stringApplyType               =  getValue("ApplyType").trim() ;
        String        stringBarCode               =  getValue("BarCode").trim() ;
        Vector          vectorFactoryNo1            =  new  Vector() ;
        Vector          vectorFactoryNo18             =  new  Vector() ;
        double          doubleTemp                  =  0 ;
        Hashtable   hashtablePurchaseMoney1   =  new  Hashtable() ;
        Hashtable   hashtablePurchaseMoney18    =  new  Hashtable() ;
        //
        if(getFunctionName().indexOf("����")  == -1)                                              return  true ;
        if(jtable18.getRowCount()==0  &&  (!booleanUndergoWriteCheck  ||  "F".equals(stringApplyType)))   return  true ;
        if(isTable1PurchaseMoney0(exeUtil))  {
            setTableData("Table18",  new  String[0][0]) ;
            return  true ;
        }
        //
        for(int  intNo=0  ;  intNo<jtable18.getRowCount()  ;  intNo++) {
            stringFactoryNo         =  (""+getValueAt("Table18",  intNo,  "FactoryNo")).trim() ;
            stringPurchaseMoneyFirst    =  (""+getValueAt("Table18",  intNo,  "PurchaseMoneyFirst")).trim() ;
            stringPurchaseMoneyEnd    =  (""+getValueAt("Table18",  intNo,  "PurchaseMoneyEnd")).trim() ;
            //
            if(vectorFactoryNo18.indexOf(stringFactoryNo)  !=  -1) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table18",  intNo,  "FactoryNo") ;
                messagebox("�t��ĳ���O����椧�� "+(intNo+1)+" �C��[�t��] ���ƥX�{�C") ;
                return  false ;
            }
            vectorFactoryNo18.add(stringFactoryNo) ;
            //
            if("".equals(stringFactoryNo)) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table18",  intNo,  "FactoryNo") ;
                messagebox("�t��ĳ���O����椧�� "+(intNo+1)+" �C��[�t��] ���i���ťաC") ;
                return  false ;
            }
            // �t�Ӧs�b�ˮ�
            if(exeFun.getDoc3M015(stringFactoryNo).length  ==  0) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table18",  intNo,  "FactoryNo") ;
                messagebox("�t��ĳ���O����椧�� "  +  (intNo+1)  +  " �C �� [�t�ӲΤ@�s��] ���s�b [���ʼt�Ӻ��@�@�~(Doc3M015)]�C") ;
                return  false ; 
            }
            // �o�л��ΨM�л����������� 0
            if(exeUtil.doParseDouble(stringPurchaseMoneyFirst)  ==  0) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table18",  intNo,  "PurchaseMoneyFirst") ;
                messagebox("�t��ĳ���O����椧�� "  +  (intNo+1)  +  " �C �� [�o�л�] �����\���� 0�C") ;
                return  false ; 
            }
            if(exeUtil.doParseDouble(stringPurchaseMoneyEnd)  ==  0) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                setFocus("Table18",  intNo,  "PurchaseMoneyEnd") ;
                messagebox("�t��ĳ���O����椧�� "  +  (intNo+1)  +  " �C �� [�o�л�] �����\���� 0�C") ;
                return  false ; 
            }
            hashtablePurchaseMoney18.put(stringFactoryNo,  stringPurchaseMoneyEnd) ;
        }
        for(int  intNo=0  ;  intNo<jtable1.getRowCount()  ;  intNo++) {
            stringFactoryNo         =  (""+getValueAt("Table1",  intNo,  "FactoryNo")).trim() ;
            stringPurchaseMoney     =  (""+getValueAt("Table1",  intNo,  "PurchaseMoney")).trim() ;
            // �o�мt�ӥ����s�b��ĳ���t�Ӥ�
            if(vectorFactoryNo18.indexOf(stringFactoryNo)  ==  -1)  {
                jtabbedPane1.setSelectedIndex(0) ;
                jtable1.setRowSelectionInterval(intNo,  intNo) ;
                messagebox("���ʶ��ت�椧�� "  +  (intNo+1)  +  " �C �� [�t��] ���s�b [�t��ĳ���O��] ���C") ;
                return  false ; 
            }
            if(vectorFactoryNo1.indexOf(stringFactoryNo)  ==  -1)  vectorFactoryNo1.add(stringFactoryNo) ;
            //
            doubleTemp  =  exeUtil.doParseDouble(stringPurchaseMoney)  +  exeUtil.doParseDouble(""+hashtablePurchaseMoney1.get(stringFactoryNo)) ;
            hashtablePurchaseMoney1.put(stringFactoryNo,  convert.FourToFive(""+doubleTemp,  0)) ;
        }
        // �o�мt�Ӥ��M�л����@�P���ʦX�����B 
        /*double  doublePurchaseMoney1    =  0 ;
        double  doublePurchaseMoney18  =  0 ;
        for(int  intNo=0  ;  intNo<vectorFactoryNo1.size()  ;  intNo++) {
            stringFactoryNo       =  ""+vectorFactoryNo1.get(intNo) ;
            doublePurchaseMoney1    =  exeUtil.doParseDouble(convert.FourToFive(""+hashtablePurchaseMoney1.get(stringFactoryNo),  0)) ;
            doublePurchaseMoney18   =  exeUtil.doParseDouble(convert.FourToFive(""+hashtablePurchaseMoney18.get(stringFactoryNo),  0)) ;
            if(doublePurchaseMoney1  !=  doublePurchaseMoney18) {
                jtabbedPane1.setSelectedIndex(intTablePanel) ;
                messagebox("�t��ĳ���O����� �P  ���ʶ��ت�� �� �t��("+stringFactoryNo+")[�o�л�] ���@�P�C") ;
                return  false ;   
            }
        }*/
        return  true ;
    }
    public  boolean  isTable1PurchaseMoney0(FargloryUtil  exeUtil) throws  Throwable {
        JTable  jtable              =  getTable("Table1") ;
        String  stringPurchaseMoney   =  "" ;
        for(int  intNo=0  ;  intNo<jtable.getRowCount()  ;  intNo++) {
            stringPurchaseMoney  =  (""+getValueAt("Table1",  intNo,  "PurchaseMoney")).trim() ;
            //
            if(exeUtil.doParseDouble(stringPurchaseMoney)  !=  0)  return  false ;
        }
        return  true ;
    }
    //
    public String getInformation(){
        return "---------------�s�W���s�{��.preProcess()----------------";
    }
}



