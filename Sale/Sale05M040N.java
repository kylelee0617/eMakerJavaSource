package  Sale ;
import      javax.swing.*;
import      jcx.jform.bproc;
import      java.io.*;
import      java.util.*;
import      jcx.util.*;
import      jcx.html.*;
import      jcx.db.*;
import      cLabel;
import      com.jacob.com.*;
import      com.jacob.activeX.*;
import      Farglory.util.FargloryUtil ;
public  class  Sale05M040N  extends  bproc{
    public String getDefaultValue(String value)throws Throwable{
        //201808check BEGIN
        System.out.println("chk==>"+getUser()+" , value==>"+value.trim());
        if(getUser() != null && getUser().toUpperCase().equals("B9999")) {
          messagebox("�����v�������\!!!");
          return value;
        }
        //201808check FINISH      
        FargloryUtil  exeUtil  =  new  FargloryUtil() ;
        // �ɮ׹�ܤ��
        setValue("ExcelFile",  "")  ;
        JFileChooser  fileDialog  =  new  JFileChooser( ) ;
        // ����ɮ׹�ܤ��
        int  ret  =  fileDialog.showOpenDialog(null) ;
        // ���o�ϥΪ̿�ܪ��ɮ�
        File  obj  =  fileDialog.getSelectedFile( ) ;
        // ���U[�}��]���A����ɮת����|
        if(ret  ==  JFileChooser.APPROVE_OPTION) {
            setValue("ExcelFile",  obj.getPath( ))  ;
        } else {
             return value;  
        }
        //
        if(!isBatchCheckOK(exeUtil))  return value ;
        boolean  booleanOK  =  doUpdate(exeUtil) ;
        if(booleanOK) {
            JOptionPane.showMessageDialog(null,  "���ت����ɦ��\�C",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
        } else {
            JOptionPane.showMessageDialog(null,  "���ت����ɥ��ѡC",  "�T��",  JOptionPane.ERROR_MESSAGE) ;
        }
        //message("11111") ;
        return value;
    }
    public  boolean  doUpdate(FargloryUtil  exeUtil) throws  Throwable {
        talk           dbSale                    =  getTalk(""  +  get("put_dbSale")) ;
        int            intCarTotalRow       =  0 ;
        int            intCarTotalRow2     =  0 ;     // 2012-11-05  B3018
        int            intHouseTotalRow   =  0 ;
        int            intHouseTotalRow2 =  0 ;   // 2012-11-05  B3018
        String      stringTemp              =  "" ;
        String      stringSql                  =  "" ;
        String      stringExcelFile         =  getValue("ExcelFile").trim( ) ;
        String      stringType               =  getValue("Type").trim( ) ;
        String      stringProjectId         =  getValue("ProjectId").trim( ) ;
        String      stringMessage        =  "" ;
        String[]    arrayTemp              =  null ;
        // ������ت�
        String[]    arrayCarField          =  {"����O",                    "���ت�NO",                 "���q�O�N�X-����ت�",      "���q�O",                 "���q�O�N�X-����g�a", 
                                                             "���q�O",                    "�Ӽh",                         "�ʽ�",                                "���O",                     "size",
                                   "���~�O�N�X",             "���~�O",                     "�v��Y/N",                           "���",                     "���-����ت���",
                                   "���-����g�a��",     "����",                         "����-����ت���",             "����-����g�a��" ,  "����öi",  
                                   "���إN�P���Y",          "�������O-�N�X",          "�������O",                         "����������n",        "�D�n�γ~",  // 2012-12-11 B3018 �W�[ �D�n�γ~
                                   "�������-����ت�",  "�������-����g�a",  "�e�U�P��H-����",              "���U�P��H-����",   "�e�U�P��H-���g",   
                                   "���U�P��H-���g",      "�N�����q�O-����",      "�N�����q�O-���g"} ;  // 2014-02-10 B3018 �ק�
        String[]    arrayHouseField      =   {"�ɧO",                    "�ӧO",                       "��O",                      "���ت�NO",           "���q�O�N�X-�Ы�",
                                                              "���q�O",                "���q�O�N�X-�g�a",    "���q�O",                   "���~�O�N�X",         "���~�O",
                                                  "�W��",                    "�P��",                       "�P��",                      "�P��-�Ы�",           "�P��-�g�a",  
                                                  "����",                     "����",                       "����-�Ы�",             "����-�g�a",           "����öi",  
                                    "���إN�P���Y",       "�D�n�γ~",                "�ت����A-�N�X",      "�ت����A",             "��",  
                                    "�U",                        "��",                           "�j��",                      "�������-�Ы�",    "�������-�g�a",
                                    "�e�U�P��H-�Ы�",  "���U�P��H-�Ы�",     "�e�U�P��H-�g�a",  "���U�P��H-�g�a",  "�N�����q�O-�Ы�",
                                    "�N�����q�O-�g�a"} ;  // 2014-02-10 B3018 �ק�
        Object     objectUsedRange    =  null ;
        Vector     vectorSql                 =  new  Vector( ) ;
          // ���o Exce ����
          Vector    retVector         =  getExcelObject(stringExcelFile) ;
          Object    objectSheet1   =  retVector.get(1) ; // ������ت�
        Object    objectSheet2   =  retVector.get(2) ; // �Ыλ��ت�
          Object    object1A1        =  retVector.get(3) ;
        Object    object2A1        =  retVector.get(4) ;
        // 2012-11-05 B3018 �ק� S
        Object    objectSheet3   =  retVector.get(5) ; // ������ت�(�@��)
        Object    objectSheet4   =  retVector.get(6) ; // �Ыλ��ت�(�@��)
          Object    object3A1        =  retVector.get(7) ;
        Object    object4A1        =  retVector.get(8) ;
        // 2012-11-05 B3018 �ק� E
        // ����
        //Dispatch.call(object1A1,  "Select");
        System.out.println("stringType:"+stringType);
        if("����".equals(stringType)  ||  "����".equals(stringType)) {
            // �^�Ǥw�ϥΪ��`��Ƥ��`���($A$1:$BN$132)
            objectUsedRange  =  Dispatch.get(objectSheet1, "UsedRange").toDispatch();
            stringTemp            =  Dispatch.call(objectUsedRange, "Address").toString( ) ;
            arrayTemp             =  convert.StringToken(stringTemp,  "$") ;
            intCarTotalRow      =  Integer.parseInt(arrayTemp[4].trim( )) ;
            // 2012-11-05 B3018 S
            objectUsedRange  =  Dispatch.get(objectSheet3, "UsedRange").toDispatch();
            stringTemp            =  Dispatch.call(objectUsedRange, "Address").toString( ) ;
            arrayTemp             =  convert.StringToken(stringTemp,  "$") ;
            intCarTotalRow2      =  Integer.parseInt(arrayTemp[4].trim( )) ;
            // 2012-11-05 B3018 E
        }
        System.out.println("ee-------------------------------------------------------");
        //System.out.println("����-----"   +  stringTemp+"----"+arrayTemp.length  +  "--------------"+intCarTotalRow) ;
        // �Ы�
        //Dispatch.call(object2A1,  "Select");
        if("����".equals(stringType)  ||  "�Ы�".equals(stringType)) {
            objectUsedRange   =  Dispatch.get(objectSheet2, "UsedRange").toDispatch();
            stringTemp             =  Dispatch.call(objectUsedRange, "Address").toString( ) ;
            arrayTemp              =  convert.StringToken(stringTemp,  "$") ;
            intHouseTotalRow   =  Integer.parseInt(arrayTemp[4].trim( )) ;
            // 2012-11-05 B3018 S
            objectUsedRange     =  Dispatch.get(objectSheet4, "UsedRange").toDispatch();
            stringTemp               =  Dispatch.call(objectUsedRange, "Address").toString( ) ;
            arrayTemp                =  convert.StringToken(stringTemp,  "$") ;
            intHouseTotalRow2   =  Integer.parseInt(arrayTemp[4].trim( )) ;
            // 2012-11-05 B3018 E
        }
        System.out.println("ff�Ы�-----"   +  stringTemp+"----"+arrayTemp.length  +  "--------------"+intHouseTotalRow) ;
        // ���W���ˮ� �Ыλ��ت�
        //Dispatch.call(object2A1,  "Select");
        for(int  intFieldNo=0  ;  intFieldNo<arrayHouseField.length  ;  intFieldNo++) {
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet2) ;
            System.out.println(intFieldNo  +  "------"  +  arrayHouseField[intFieldNo]+"---------------"+stringTemp) ;
            if(!arrayHouseField[intFieldNo].equals(stringTemp)) {
                message("�Ыλ��ت� ��1�C-��"  +  (intFieldNo+1)  +  "�楲����("  +  arrayHouseField[intFieldNo]  +  ")") ;
                // ���� Excel ����
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 S
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet4) ;
            if(!arrayHouseField[intFieldNo].equals(stringTemp)) {
                message("�Ыλ��ت�(�@��)��1�C-��"  +  (intFieldNo+1)  +  "�楲����("  +  arrayHouseField[intFieldNo]  +  ")�{��("+stringTemp+")") ;
                // ���� Excel ����
                getReleaseExcelObject(retVector) ;
                return false ;
            }           
            // 2012-11-05 B3018 E
        }
        // ���W���ˮ� ������ت�
        //Dispatch.call(object1A1,  "Select");
        for(int  intFieldNo=0  ;  intFieldNo<arrayCarField.length  ;  intFieldNo++) {
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet1) ;
            //System.out.println(intFieldNo  +  "------"  +  arrayCarField[intFieldNo]+"---------------"+stringTemp) ;
            if(!arrayCarField[intFieldNo].equals(stringTemp)) {
                message("������ت��1�C-��"  +  (intFieldNo+1)  +  "�楲����("  +  arrayCarField[intFieldNo]  +  ")") ;
                // ���� Excel ����
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 S
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet3) ;
            if(!arrayCarField[intFieldNo].equals(stringTemp)) {
                message("������ت�(�X�p)��1�C-��"  +  (intFieldNo+1)  +  "�楲����("  +  arrayCarField[intFieldNo]  +  ")") ;
                // ���� Excel ����
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 E
        }       
        // Start �ק���:20100426 ���u�s��:B3774
        // ���EXCEL�W�ƩMSale05M402�W�ƬO�_�@�P
        String    retData[][]             =   new  String[0][0] ;
        String    retSale05M402[][] =   new  String[0][0] ;
        String    stringProjectID1    = getValue("ProjectId").trim();
        String    stringVisionKind    = "";
        String    stringVision           = "0";
        String    stringPosition        = "";
        String    stringPingSu         = "";
        boolean blnHasPosition     = false;
        //
/* 2013-08-20 B3018 ������
        if("����".equals(stringType)  ||  "�Ы�".equals(stringType)){
            stringSql = "select TOP 1 VisionKind, Vision "+
                      "from Sale05M401 "+
                      "where ProjectID1='"+stringProjectID1+"' "+
                      "and HouseCar='House' "+
                      "and convert(char(10),getdate(),111) between StartDate and EndDate "+
                      "order by VisionKind desc";
            retData = dbSale.queryFromPool(stringSql);
            if(retData.length == 0){
                message("�Ы� �S���W�Ƹ�ƥi�H���!");
                getReleaseExcelObject(retVector);
                return false;
            }else{
                stringVisionKind = retData[0][0];
                stringVision        = retData[0][1];
                //
                stringSql = "select Position, TotalPingSu, TotalSquare "+
                          "from Sale05M402 "+
                          "where ProjectID1='"+stringProjectID1+"' "+
                          "and VisionKind='"+stringVisionKind+"' "+
                          "and Vision="+stringVision;
                retSale05M402 = dbSale.queryFromPool(stringSql);
                if(retSale05M402.length == 0){
                    message("�Ы� �S���W�Ƹ�ƥi�H���!");
                    getReleaseExcelObject(retVector);
                    return false;
                }
            }
            //
            for(int intRow=1; intRow<intHouseTotalRow; intRow++){
                blnHasPosition = false;
                stringPosition   = getDataFromExcel(  2,  intRow,  objectSheet2);
                stringPingSu    = getDataFromExcel(10,  intRow,  objectSheet2);
                //
                if("null".equals(stringPosition))    continue ;           // 2012-11-26 B3018 �s�W
                if("null".equals(stringPingSu))      stringPingSu    =  "0" ;  // 2012-11-26 B3018 �s�W
                if("".equals(stringPingSu))             stringPingSu    =  "0" ;  
                //
                if (getSale05M092Count(stringPosition)<= 0){
                    for(int intNo=0; intNo<retSale05M402.length; intNo++){
                        System.out.println("["+intRow+"]["+intNo+"--------------------------------------stringPosition("+stringPosition+")("+retSale05M402[intNo][0]+")") ;
                        System.out.println(intNo+"--------------------------------------stringPingSu("+stringPingSu+")("+retSale05M402[intNo][1]+")") ;
                        if(stringPosition.equals(retSale05M402[intNo][0])){
                            if(operation.compareTo(stringPingSu,retSale05M402[intNo][1]) != 0){
                                message("�Ы� "  +stringPosition+"���W�ƿ��~!");
                                getReleaseExcelObject(retVector);
                                return false;
                            }
                            blnHasPosition = true;
                        }
                    }
                    if(!blnHasPosition){
                        message("�Ы� �S��"+stringPosition+"�����n���!");
                        getReleaseExcelObject(retVector);
                        return false;
                    }
                }
            }
        }
2013-08-20 B3018 ������ */ 
        // End �ק���:20100426 ���u�s��:B3774
        String      stringCarArea             =  "" ;  // 
        String      stringCarVisionKind    =  "" ;
        String      stringCarVision            =  "0" ;
        String[][]  retSale05M407           =  new  String[0][0] ;
/* 2013-08-20 B3018 ������
        if("����".equals(stringType)  ||  "����".equals(stringType)) {
            // �W���ˬd
            stringSql = "select TOP 1 VisionKind, Vision "+
                      "from Sale05M401 "+
                      "where ProjectID1='"+stringProjectID1+"' "+
                      "and HouseCar='Car' "+
                      "and convert(char(10),getdate(),111) between StartDate and EndDate "+
                      "order by VisionKind desc";
            retData = dbSale.queryFromPool(stringSql);
            if(retData.length == 0){
                message("���� �S���W�Ƹ�ƥi�H���!");
                getReleaseExcelObject(retVector);
                return false;
            }else{
                stringCarVisionKind = retData[0][0];
                stringCarVision        = retData[0][1];
                //
                stringSql = "select Position, BuildingSquare "+
                          "from Sale05M407 "+
                          "where ProjectID1='"+stringProjectID1+"' "+
                          "and VisionKind='"+stringCarVisionKind+"' "+
                          "and Vision="+stringCarVision;
                retSale05M407 = dbSale.queryFromPool(stringSql);
                if(retSale05M407.length == 0){
                    message("���� �S���W�Ƹ�ƥi�H���!");
                    getReleaseExcelObject(retVector);
                    return false;
                }
            }
            //
            for(int intRow=1; intRow<intCarTotalRow; intRow++){
                blnHasPosition = false;
                stringPosition   =  getDataFromExcel2(0,   intRow,  objectSheet1) ;    // ��O
                stringCarArea  =  getDataFromExcel2(23, intRow,  objectSheet1) ; 
                //
                if("null".equals(stringPosition))    continue ;
                if("".equals(stringPosition))          continue ;           
                if("null".equals(stringCarArea))    stringCarArea    =  "0" ;  
                if("".equals(stringCarArea))          stringCarArea    =  "0" ;  
                //
                if (getSale05M092Count(stringPosition)<= 0){
                    for(int intNo=0; intNo<retSale05M407.length; intNo++){
                        System.out.println(intNo+"retSale05M407["+intRow+"]["+intNo+"]--------------------------------------stringPosition("+stringPosition+")("+retSale05M407[intNo][0]+")") ;
                        System.out.println(intNo+"retSale05M407--------------------------------------stringCarArea("+stringCarArea+")("+retSale05M407[intNo][1]+")") ;
                        if(stringPosition.equals(retSale05M407[intNo][0])){
                            if(operation.compareTo(stringCarArea,retSale05M407[intNo][1]) != 0){
                                message("���� "+stringPosition+"���W�ƿ��~!");
                                getReleaseExcelObject(retVector);
                                return false;
                            }
                            blnHasPosition = true;
                        }
                    }
                    if(!blnHasPosition){
                        message("���� �S��"+stringPosition+"�����n���!");
                        getReleaseExcelObject(retVector);
                        return false;
                    }
                }
            }
        }
2013-08-20 B3018 ������ */
        // ��Ʈw�ʧ@
        int        intRecord                 =  0 ;
        //String  stringPosition           =  "" ; // �ק���:20100426 ���u�s��:B3774
        String  stringListPriceNo      =  "" ;
        String  stringHCom              =  "" ;
        String  stringLCom              =  "" ;
        // Start �ק���:20100507 ���u�s��:B3774
        String  stringCarKind1        =  "" ;
        String  stringCarKind2        =  "" ;
        String  stringCarSize           =  "" ;
        // End �ק���:20100507 ���u�s��:B3774
        String  stringTypeNo           =  "" ;
        String  stringCarPaper        =  "" ; // �ק���:20100507 ���u�s��:B3774
        //String  stringPingSu            =  "" ; // �ק���:20100426 ���u�s��:B3774
        String  stringListUintPrice    =  "" ;
        String  stringListPrice          =  "" ;
        String  stringHListPrice        =  "" ;
        String  stringLListPrice         =  "" ;
        String  stringFloorUnitPrice  =  "" ;
        String  stringFloorPrice        =  "" ;
        String  stringHFloorPrice      =  "" ;
        String  stringLFloorPrice      =  "" ;
        String stringSaleKind="";
        String stringTotalSquare     = ""; // �ק���:20100426 ���u�s��:B3774
        String stringSaleFlag           = ""; // �ק���:2012/06/29 ���u�s��:B3018
        // �Ы�
        String  stringMainUse          =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuildCd           =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuildType       =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuild1              =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuild2              =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuild3              =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringBuild4              =  "" ;  // 2012-08-03 B3018 �s�W
        String  stringHratio               =  "" ;  // 2012-11-05 B3018 �s�W
        String  stringLratio               =  "" ;// 2012-11-05 B3018 �s�W
        String  stringAHCom            =  "" ;// 2012-11-05 B3018 �s�W
        String  stringBHCom            =  "" ;// 2012-11-05 B3018 �s�W
        String  stringALCom             =  "" ;// 2012-11-05 B3018 �s�W
        String  stringBLCom              =  "" ;// 2012-11-05 B3018 �s�W
        String  stringIHCom                =  "" ;// 2014-02-10 B3018 �s�W
        String  stringILCom               =  "" ;// 2014-02-10 B3018 �s�W
        String  stringVisionKindL       =  "" ;
        String  stringVisionL              =  "" ;
        if("����".equals(stringType)  ||  "�Ы�".equals(stringType)) {
            // �Ыλ��ت�
            for(int  intRowNo=1  ;  intRowNo<intHouseTotalRow  ;  intRowNo++) {
                System.out.println("1-�Ы�--------------------------------");
                stringPosition           =  getDataFromExcel(2, intRowNo,  objectSheet2) ;    // ��O
                stringListPriceNo      =  getDataFromExcel(3, intRowNo,  objectSheet2) ;    // ���ت� NO
                stringHCom              =  getDataFromExcel2(4, intRowNo,  objectSheet2) ;    // ���q�O�N�X-��    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                stringLCom              =  getDataFromExcel2(6, intRowNo,  objectSheet2) ;    // ���q�O�N�X-�g    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                //System.out.println("1�Ы�---stringPosition:"+stringPosition + " stringLCom:"+stringLCom);
                stringTypeNo           =  getDataFromExcel(8, intRowNo,  objectSheet2) ;    // ���~�O�N�X
                stringPingSu            =  getDataFromExcel(10, intRowNo,  objectSheet2) ;  // �W��
                stringListUintPrice    =  getDataFromExcel(11, intRowNo,  objectSheet2) ;  // �P��
                stringListPrice          =  getDataFromExcel(12, intRowNo,  objectSheet2) ;  // �P��
                stringHListPrice        =  getDataFromExcel(13, intRowNo,  objectSheet2) ;  // �P��-�Ы�
                stringLListPrice         =  getDataFromExcel(14, intRowNo,  objectSheet2) ;  // �P��-�g�a
                stringFloorUnitPrice  =  getDataFromExcel(15, intRowNo,  objectSheet2) ;  // �����
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet2) ;  // ����
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet2) ;  // ����-�Ы�
                stringLFloorPrice       =  getDataFromExcel(18, intRowNo,  objectSheet2) ;  // ����-�g�a
                stringSaleKind           =  getDataFromExcel(19, intRowNo,  objectSheet2) ;  //����öi
                stringSaleFlag           =  getDataFromExcel(20, intRowNo,  objectSheet2) ;  //���إN�P���Y    2012/06/29 B3018
                stringMainUse            =  getDataFromExcel2(21, intRowNo,  objectSheet2) ;  // �D�n�γ~          2012-08-03 B3018 �s�W 
                stringBuildCd             =  getDataFromExcel2(22, intRowNo,  objectSheet2) ;  // �ت����A-�N�X  2012-08-03 B3018 �s�W 
                stringBuildType          =  getDataFromExcel2(23, intRowNo,  objectSheet2) ;  // �ت����A          2012-08-03 B3018 �s�W 
                stringBuild1               =  getDataFromExcel2(24, intRowNo,  objectSheet2) ;  // ��                     2012-08-03 B3018 �s�W 
                stringBuild2               =  getDataFromExcel2(25, intRowNo,  objectSheet2) ;  // �U                     2012-08-03 B3018 �s�W 
                stringBuild3               =  getDataFromExcel2(26, intRowNo,  objectSheet2) ;  // ��                     2012-08-03 B3018 �s�W 
                stringBuild4               =  getDataFromExcel2(27, intRowNo,  objectSheet2) ;  // �j��                  2012-08-03 B3018 �s�W 
                stringHratio                =  getDataFromExcel2(28, intRowNo,  objectSheet2) ;  //�������-�Ы�      2012-11-05 B3018 �s�W 
                stringLratio                =  getDataFromExcel2(29, intRowNo,  objectSheet2) ;  //�������-�g�a      2012-11-05 B3018 �s�W 
                stringAHCom              =  getDataFromExcel2(30, intRowNo,  objectSheet2) ;  //�e�U�P��H-�Ы�   2012-11-05 B3018 �s�W 
                stringBHCom              =  getDataFromExcel2(31, intRowNo,  objectSheet2) ;  //���U�P��H-�Ы�   2012-11-05 B3018 �s�W 
                stringALCom              =  getDataFromExcel2(32, intRowNo,  objectSheet2) ;  //�e�U�P��H-�g�a   2012-11-05 B3018 �s�W 
                stringBLCom              =  getDataFromExcel2(33, intRowNo,  objectSheet2) ;  //���U�P��H-�g�a   2012-11-05 B3018 �s�W 
                stringIHCom               =  getDataFromExcel2(34, intRowNo,  objectSheet2) ;  //�N�����q�O-�Ы�   2014-02-10 B3018 �s�W 
                stringILCom               =  getDataFromExcel2(35, intRowNo,  objectSheet2) ;  //�N�����q�O-�g�a   2014-02-10 B3018 �s�W 
                
                //
                if("null".equals(stringMainUse))    stringMainUse    =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuildCd))     stringBuildCd      =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuildType))  stringBuildType  =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild1))        stringBuild1        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild2))        stringBuild2        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild3))        stringBuild3        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild4))        stringBuild4        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringHratio))         stringHratio        =  "" ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringLratio))         stringLratio        =  "" ;  // 2014-01-02 B3018 �ק�
                //System.out.println("1-"+intRowNo+"�Ы�   stringHratio("+stringHratio+")stringLratio("+stringLratio+")---------------------------------------------") ;
                if("null".equals(stringAHCom))     stringAHCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringBHCom))     stringBHCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringALCom))     stringALCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringBLCom))     stringBLCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringIHCom))      stringIHCom          =  "" ;  // 2014-02-10 B3018 �s�W
                if("null".equals(stringILCom))      stringILCom          =  "" ;  // 2014-02-10 B3018 �s�W
                //
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";
                }                               
                // �ˮ�
                if("null".equals(stringPosition))    {
                    System.out.println((intRowNo+1)  +  "House---") ;
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    System.out.println((intRowNo+1)  +  "House---") ;
                    intRecord++ ;
                    break ;
                }
                //System.out.println("getSale05M092Count----------------------------------") ;
                if (getSale05M092Count(stringPosition)<= 0){
                  // �R��
                  stringSql  =  getDelete(stringPosition,  "House") ;
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))  dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "House---"+stringSql) ;
                  // �s�W
                  // Start �ק���:20100426 ���u�s��:B3774
                  /*
                  if(retSale05M402 == null){ // ����n����
                    stringTotalSquare = null;
                    stringVision = null;
                  }
                  */
                  stringTotalSquare  =  "0" ;
                  stringVisionKindL   =  stringVisionKind ;
                  stringVisionL          =  stringVision ;
                  for(int intNo=0; intNo<retSale05M402.length; intNo++){
                      if(stringPosition.equals(retSale05M402[intNo][0])){
                          stringTotalSquare = retSale05M402[intNo][2];
                      }
                  }
                  if(exeUtil.doParseDouble(stringTotalSquare)  <=  0) {
                      stringVisionKindL  =  "" ;
                      stringVisionL         =  "0" ;
                  }
                  //
                  stringSql  =  getInsertH(stringPosition,            stringListPriceNo,    stringHCom,            stringLCom,           stringTypeNo,
                  // End �ק���:20100426 ���u�s��:B3774
                                     stringPingSu,             stringListUintPrice,  stringListPrice,        stringHListPrice,     stringLListPrice,
                                     // Start �ק���:20100426 ���u�s��:B3774
                                     //stringFloorUnitPrice,  stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "House",stringSaleKind) ;
                                     stringFloorUnitPrice,   stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "House", 
                                     stringSaleKind,           stringTotalSquare,   stringVisionKindL,   stringVisionL,          stringSaleFlag,
                                     stringMainUse,            stringBuildCd,         stringBuildType,      stringBuild1,          stringBuild2,
                                      stringBuild3,                stringBuild4,            stringHratio,            stringLratio,           stringAHCom,
                                      stringBHCom,               stringALCom,         stringBLCom,          stringIHCom,          stringILCom);
                                     // End �ק���:20100426 ���u�s��:B3774
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "House---"+stringSql) ;
                  intRecord++ ;
                } else {
                   System.out.println("�H��:"+stringPosition);
                }
            }
            // �Ыλ��ت� �X�p
            // �R��
            stringSql  =  getDeleteSale05M342("House") ;
            System.out.println("2 �ЫΦX�p  SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
            for(int  intRowNo=1  ;  intRowNo<intHouseTotalRow2  ;  intRowNo++) {
                System.out.println("2�ЫΦX�p--------------------------------");
                stringPosition           =  getDataFromExcel(2, intRowNo,  objectSheet4) ;    // ��O
                stringListPriceNo      =  getDataFromExcel(3, intRowNo,  objectSheet4) ;    // ���ت� NO
                stringHCom              =  getDataFromExcel2(4, intRowNo,  objectSheet4) ;    // ���q�O�N�X-��    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                stringLCom              =  getDataFromExcel2(6, intRowNo,  objectSheet4) ;    // ���q�O�N�X-�g    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                stringTypeNo           =  getDataFromExcel(8, intRowNo,  objectSheet4) ;    // ���~�O�N�X
                stringPingSu            =  getDataFromExcel(10, intRowNo,  objectSheet4) ;  // �W��
                stringListUintPrice    =  getDataFromExcel(11, intRowNo,  objectSheet4) ;  // �P��
                stringListPrice          =  getDataFromExcel(12, intRowNo,  objectSheet4) ;  // �P��
                stringHListPrice        =  getDataFromExcel(13, intRowNo,  objectSheet4) ;  // �P��-�Ы�
                stringLListPrice         =  getDataFromExcel(14, intRowNo,  objectSheet4) ;  // �P��-�g�a
                stringFloorUnitPrice  =  getDataFromExcel(15, intRowNo,  objectSheet4) ;  // �����
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet4) ;  // ����
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet4) ;  // ����-�Ы�
                stringLFloorPrice       =  getDataFromExcel(18, intRowNo,  objectSheet4) ;  // ����-�g�a
                stringSaleKind           =  getDataFromExcel(19, intRowNo,  objectSheet4) ;  //����öi
                stringSaleFlag           =  getDataFromExcel(20, intRowNo,  objectSheet4) ;  //���إN�P���Y
                stringMainUse            =  getDataFromExcel2(21, intRowNo,  objectSheet4) ;  // �D�n�γ~      
                stringBuildCd             =  getDataFromExcel2(22, intRowNo,  objectSheet4) ;  // �ت����A-�N�X  
                stringBuildType          =  getDataFromExcel2(23, intRowNo,  objectSheet4) ;  // �ت����A          
                stringBuild1               =  getDataFromExcel2(24, intRowNo,  objectSheet4) ;  // ��                   
                stringBuild2               =  getDataFromExcel2(25, intRowNo,  objectSheet4) ;  // �U                 
                stringBuild3               =  getDataFromExcel2(26, intRowNo,  objectSheet4) ;  // ��            
                stringBuild4               =  getDataFromExcel2(27, intRowNo,  objectSheet4) ;  // �j��                
                stringHratio                =  getDataFromExcel2(28, intRowNo,  objectSheet4) ;  //�������-�Ы�
                stringLratio                =  getDataFromExcel2(29, intRowNo,  objectSheet4) ;  //�������-�g�a 
                stringAHCom              =  getDataFromExcel2(30, intRowNo,  objectSheet4) ;  //�e�U�P��H-�Ы�
                stringBHCom              =  getDataFromExcel2(31, intRowNo,  objectSheet4) ;  //���U�P��H-�Ы� 
                stringALCom              =  getDataFromExcel2(32, intRowNo,  objectSheet4) ;  //�e�U�P��H-�g�a 
                stringBLCom              =  getDataFromExcel2(33, intRowNo,  objectSheet4) ;  //���U�P��H-�g�a
                stringIHCom               =  getDataFromExcel2(34, intRowNo,  objectSheet4) ;  //�N�����q�O-�Ы�   2014-02-10 B3018 �s�W 
                stringILCom               =  getDataFromExcel2(35, intRowNo,  objectSheet4) ;  //�N�����q�O-�g�a   2014-02-10 B3018 �s�W 
                //
                if("null".equals(stringMainUse))    stringMainUse    =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuildCd))     stringBuildCd      =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuildType))  stringBuildType  =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild1))        stringBuild1        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild2))        stringBuild2        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild3))        stringBuild3        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringBuild4))        stringBuild4        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringHratio))        stringHratio         =  "" ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringLratio))        stringLratio         =  "" ;  // 2014-01-02 B3018 �ק�
                //System.out.println("2-"+intRowNo+"�Ы�   stringHratio("+stringHratio+")stringLratio("+stringLratio+")---------------------------------------------NEW") ;
                if("null".equals(stringAHCom))     stringAHCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringBHCom))     stringBHCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringALCom))     stringALCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringBLCom))     stringBLCom        =  "" ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringIHCom))      stringIHCom          =  "" ;  // 2014-02-10 B3018 �s�W
                if("null".equals(stringILCom))      stringILCom          =  "" ;  // 2014-02-10 B3018 �s�W
                //
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";
                }                               
                // �ˮ�
                if("null".equals(stringPosition))    {
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    intRecord++ ;
                    break ;
                }
                // �s�W
                if(retSale05M402 == null){ // ����n����
                  stringTotalSquare = null;
                  stringVision = null;
                }
                if (getSale05M092Count(stringPosition)<= 0){
                    stringSql  = getInsertH_Sale05M342(stringPosition,            stringListPriceNo,    stringHCom,            stringLCom,           stringTypeNo,
                                                 stringPingSu,             stringListUintPrice,  stringListPrice,        stringHListPrice,     stringLListPrice,
                                                  stringFloorUnitPrice,   stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "House", 
                                                  stringSaleKind,           stringTotalSquare,   stringVisionKind,     stringVision,           stringSaleFlag,
                                                  stringMainUse,            stringBuildCd,         stringBuildType,      stringBuild1,          stringBuild2,
                                                  stringBuild3,                stringBuild4,            stringHratio,            stringLratio,           stringAHCom,
                                                  stringBHCom,               stringALCom,         stringBLCom,          stringIHCom,           stringILCom);
                    System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                    if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                    intRecord++ ;
                }
            }
            stringSql  =  "insert  into  Sale05M342  select  * "  +
                                                                          " from  sale05m040 "  +
                                            " where  ProjectID1  =  '"+stringProjectId+"' "  +
                                               " and  HouseCar = 'House' "  +
                                                "and  Position not in (select Position "  +
                                                                              " from Sale05M342 "  +
                                                              " where  ProjectID1  =  '"+stringProjectId+"' "  +
                                                                " and  HouseCar = 'House') "  ;
            System.out.println("SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
        }
        //System.out.println("House-----�@"  +  intRecord  +  "��") ;
        stringMessage  =  "�Ыλ��ت� �w��J"  +  intRecord  +  "��" ;
        intRecord  =  0 ;
        // ����
        String      stringCarCd              =  "" ;  // 2012-08-03 B3018 �s�W
        String      stringCarType           =  "" ;  // 2012-08-03 B3018 �s�W
        String      stringCarHratio          =  "" ;  // 2012-11-05 B3018 �s�W
        String      stringCarLratio          =  "" ;  // 2012-11-05 B3018 �s�W
        String      stringCarAHCom        =  "" ;  // 2012-11-05 B3018 �s�W
        String      stringCarBHCom        =  "" ;  // 2012-11-05 B3018 �s�W
        String      stringCarALCom        =  "" ;  // 2012-11-05 B3018 �s�W 
        String      stringCarBLCom        =  "" ;  // 2012-11-05 B3018 �s�W
        String      stringCarMainUse      =  "" ;  // 2012-12-11 B3018 �s�W
        if("����".equals(stringType)  ||  "����".equals(stringType)) {
            // ��Ʒs�W       
            for(int  intRowNo=1  ;  intRowNo<intCarTotalRow  ;  intRowNo++) {
                stringPosition           =  getDataFromExcel(0, intRowNo,  objectSheet1) ;    // ��O
                stringListPriceNo      =  getDataFromExcel(1, intRowNo,  objectSheet1) ;    // ���ت� NO
                stringHCom              =  getDataFromExcel2(2, intRowNo,  objectSheet1) ;    // ���q�O�N�X-��    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                stringLCom              =  getDataFromExcel2(4, intRowNo,  objectSheet1) ;    // ���q�O�N�X-�g        2013-11-28  �ק�  ��ޥ� getDataFromExcel
                if (stringLCom.equals("null"))
                  stringLCom="";
                // Start �ק���:20100507 ���u�s��:B3774
                stringCarKind1         =  getDataFromExcel(7, intRowNo,  objectSheet1) ;    // �ʽ�
                stringCarKind2         =  getDataFromExcel(8, intRowNo,  objectSheet1) ;    // ���O
                stringCarSize           =  getDataFromExcel(9, intRowNo,  objectSheet1) ;    // size
                // End �ק���:20100507 ���u�s��:B3774
                stringTypeNo           =  getDataFromExcel(10, intRowNo,  objectSheet1) ;    // ���~�O�N�X
                stringCarPaper        =  getDataFromExcel(12, intRowNo,  objectSheet1) ;    // �v��Y/N // �ק���:20100507 ���u�s��:B3774
                stringPingSu            =  "0" ;  // �W��
                stringListUintPrice    =  "0" ;  // �P��
                stringListPrice          =  getDataFromExcel(13, intRowNo,  objectSheet1) ;  // �P��
                stringHListPrice        =  getDataFromExcel(14, intRowNo,  objectSheet1) ;  // �P��-�Ы�
                /* stringLListPrice         =  getDataFromExcel(15, intRowNo,  objectSheet1) ;  // �P��-�g�a  */               
                stringLListPrice         =  (getDataFromExcel(15, intRowNo,  objectSheet1).equals("-"))? "0":getDataFromExcel(15, intRowNo,  objectSheet1);  // �P��-�g�a                 
                stringFloorUnitPrice  =  "0" ;  // �����
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet1) ;  // ����
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet1) ;  // ����-�Ы�
                /* stringLFloorPrice      =  getDataFromExcel(18, intRowNo,  objectSheet1) ;  // ����-�g�a */
                stringLFloorPrice      =  (getDataFromExcel(18, intRowNo,  objectSheet1).equals("-"))? "0":getDataFromExcel(18, intRowNo,  objectSheet1);   // ����-�g�a
                stringSaleKind       =  getDataFromExcel(19, intRowNo,  objectSheet1) ;  //����öi
                stringSaleFlag       =  getDataFromExcel(20, intRowNo,  objectSheet1) ;  //���إN�P���Y  2012/06/29 B3018
                stringCarCd           =  getDataFromExcel2(21, intRowNo,  objectSheet1) ;  //�������O-�N�X  2012/08/03 B3018
                stringCarType        =  getDataFromExcel2(22, intRowNo,  objectSheet1) ;  //�������O          2012/08/03 B3018
                stringCarArea        =  getDataFromExcel2(23, intRowNo,  objectSheet1) ;  //����������n   2012/08/03 B3018
                stringCarMainUse =  getDataFromExcel2(24, intRowNo,  objectSheet1) ;  // 2012-12-11 B3018 �W�[ �D�n�γ~
                stringCarHratio     =  getDataFromExcel2(25, intRowNo,  objectSheet1) ;  //�������-�Ы�   2012/11/05 B3018
                stringCarLratio      =  getDataFromExcel2(26, intRowNo,  objectSheet1) ;  //�������-�g�a   2012/11/05 B3018
                stringCarAHCom   =  getDataFromExcel2(27, intRowNo,  objectSheet1) ;  //�e�U�P��H-�Ы�   2012/11/05 B3018
                stringCarBHCom   =  getDataFromExcel2(28, intRowNo,  objectSheet1) ;  //���U�P��H-�Ы�   2012/11/05 B3018
                stringCarALCom    =  getDataFromExcel2(29, intRowNo,  objectSheet1) ;  //�e�U�P��H-�g�a   2012/11/05 B3018
                stringCarBLCom    =  getDataFromExcel2(30, intRowNo,  objectSheet1) ;  //���U�P��H-�g�a   2012/11/05 B3018
                stringIHCom           =  getDataFromExcel2(31, intRowNo,  objectSheet1) ;  //�N�����q�O-����   2014-02-10 B3018 �s�W 
                stringILCom           =  getDataFromExcel2(32, intRowNo,  objectSheet1) ;  //�N�����q�O-���g   2014-02-10 B3018 �s�W 
                System.out.println("3���� stringIHCom("+stringIHCom+") stringILCom("+stringILCom+")--------------------------------");
                //
                if("null".equals(stringCarHratio))                        stringCarHratio     =  ""  ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringCarLratio))                        stringCarLratio     =  ""  ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringCarAHCom))                      stringCarAHCom   =  ""  ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringCarBHCom))                      stringCarBHCom   =  ""  ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringCarALCom))                      stringCarALCom    =  ""  ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringCarBLCom))                      stringCarBLCom    =  ""  ;  // 2012-11-05 B3018 �s�W
                if("null".equals(stringCarMainUse))                    stringCarMainUse  =  ""  ;  // 2012-12-11 B3018 �s�W
                //
                if("null".equals(stringCarCd))                             stringCarCd        =  "" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringCarType))                          stringCarType    =  "" ;  // 2012-08-03 B3018 �s�W
                if(exeUtil.doParseDouble(stringCarArea)<=0)    stringCarArea    =  "0" ;  // 2012-08-03 B3018 �s�W
                if("null".equals(stringIHCom))                             stringIHCom       =  "" ;  // 2014-02-10 B3018 �s�W
                if("null".equals(stringILCom))                             stringILCom       =  "" ;  // 2014-02-10 B3018 �s�W
                //
                //System.out.println("stringPosition :"+ stringPosition + " stringSaleKind :"+stringSaleKind+ "stringHCom"+stringHCom + "stringLCom"+stringLCom);
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";                 
                }                                               
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                // �ˮ�
                if("null".equals(stringPosition))    {
                    System.out.println((intRowNo+1)  +  "Car---"+stringSql) ;
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    System.out.println((intRowNo+1)  +  "Car---"+stringSql) ;
                    intRecord++ ;
                    break ;
                }
                //System.out.println("getSale05M092Count----------------------------------") ;
                  if (getSale05M092Count(stringPosition)<= 0){                                          
                  // �R��
                  stringSql  =  getDelete(stringPosition,  "Car") ;
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "Car---"+stringSql) ;
                  // �s�W
                  // Start �ק���:20100507 ���u�s��:B3774
                  /*
                  stringSql  =  getInsert(stringPosition,            stringListPriceNo,    stringHCom,            stringLCom,           stringTypeNo,
                                     stringPingSu,             stringListUintPrice,  stringListPrice,        stringHListPrice,     stringLListPrice,
                                     stringFloorUnitPrice,  stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "Car",stringSaleKind) ;
                  */
                  stringSql  =  getInsertC(stringPosition,            stringListPriceNo,    stringHCom,            stringLCom,           stringTypeNo,
                                     stringPingSu,             stringListUintPrice,  stringListPrice,        stringHListPrice,     stringLListPrice,
                                     stringFloorUnitPrice,  stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "Car",
                                     stringSaleKind,          stringCarKind1,        stringCarKind2,       stringCarSize,        stringCarPaper,
                                      stringSaleFlag,          stringCarCd,             stringCarType,        stringCarArea,       stringCarHratio,
                                    stringCarLratio,          stringCarAHCom,     stringCarBHCom,     stringCarALCom,   stringCarBLCom,
                                    stringCarMainUse,     stringCarVisionKind,  stringCarVision,      stringIHCom,           stringILCom) ;
                  // End �ק���:20100507 ���u�s��:B3774
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println("Car---"+stringSql) ;
                  intRecord++ ;
                } else{
                  System.out.println("�H��--"+stringPosition);
                }
            }
            // 2012-11-05 B3018 S
            // �R��
            stringSql  =  getDeleteSale05M342("Car") ;
            System.out.println("4 ���X�p SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
            for(int  intRowNo=1  ;  intRowNo<intCarTotalRow2  ;  intRowNo++) {
                System.out.println("4����X�p--------------------------------");
                stringPosition           =  getDataFromExcel(0, intRowNo,  objectSheet3) ;    // ��O
                stringListPriceNo      =  getDataFromExcel(1, intRowNo,  objectSheet3) ;    // ���ت� NO
                stringHCom              =  getDataFromExcel2(2, intRowNo,  objectSheet3) ;    // ���q�O�N�X-��    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                stringLCom              =  getDataFromExcel2(4, intRowNo,  objectSheet3) ;    // ���q�O�N�X-�g    2013-11-28  �ק�  ��ޥ� getDataFromExcel
                if (stringLCom.equals("null"))
                  stringLCom="";
                // Start �ק���:20100507 ���u�s��:B3774
                stringCarKind1         =  getDataFromExcel(7, intRowNo,  objectSheet3) ;    // �ʽ�
                stringCarKind2         =  getDataFromExcel(8, intRowNo,  objectSheet3) ;    // ���O
                stringCarSize           =  getDataFromExcel(9, intRowNo,  objectSheet3) ;    // size
                // End �ק���:20100507 ���u�s��:B3774
                stringTypeNo           =  getDataFromExcel(10, intRowNo,  objectSheet3) ;    // ���~�O�N�X
                stringCarPaper        =  getDataFromExcel(12, intRowNo,  objectSheet3) ;    // �v��Y/N // �ק���:20100507 ���u�s��:B3774
                stringPingSu            =  "0" ;  // �W��
                stringListUintPrice    =  "0" ;  // �P��
                stringListPrice          =  getDataFromExcel(13, intRowNo,  objectSheet3) ;  // �P��
                stringHListPrice        =  getDataFromExcel(14, intRowNo,  objectSheet3) ;  // �P��-�Ы�
                stringLListPrice         =  (getDataFromExcel(15, intRowNo,  objectSheet3).equals("-"))? "0":getDataFromExcel(15, intRowNo,  objectSheet3);  // �P��-�g�a                 
                stringFloorUnitPrice  =  "0" ;  // �����
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet3) ;  // ����
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet3) ;  // ����-�Ы�
                stringLFloorPrice      =  (getDataFromExcel(18, intRowNo,  objectSheet3).equals("-"))? "0":getDataFromExcel(18, intRowNo,  objectSheet3);   // ����-�g�a
                stringSaleKind          =  getDataFromExcel(19, intRowNo,  objectSheet3) ;  //����öi
                stringSaleFlag          =  getDataFromExcel(20, intRowNo,  objectSheet3) ;  //���إN�P���Y  
                stringCarCd              =  getDataFromExcel2(21, intRowNo,  objectSheet3) ;  //�������O-�N�X 
                stringCarType           =  getDataFromExcel2(22, intRowNo,  objectSheet3) ;  //�������O          
                stringCarArea            =  getDataFromExcel2(23, intRowNo,  objectSheet3) ;  //����������n  
                stringCarMainUse     =  getDataFromExcel2(24, intRowNo,  objectSheet3) ;  //�D�n�γ~ 2012/12/11 B3018  �ק�
                stringCarHratio          =  getDataFromExcel2(25, intRowNo,  objectSheet3) ;  //�������-�Ы� 
                stringCarLratio          =  getDataFromExcel2(26, intRowNo,  objectSheet3) ;  //�������-�g�a 
                stringCarAHCom        =  getDataFromExcel2(27, intRowNo,  objectSheet3) ;  //�e�U�P��H-�Ы�  
                stringCarBHCom       =  getDataFromExcel2(28, intRowNo,  objectSheet3) ;  //���U�P��H-�Ы�  
                stringCarALCom        =  getDataFromExcel2(29, intRowNo,  objectSheet3) ;  //�e�U�P��H-�g�a 
                stringCarBLCom        =  getDataFromExcel2(30, intRowNo,  objectSheet3) ;  //���U�P��H-�g�a 
                stringIHCom               =  getDataFromExcel2(31, intRowNo,  objectSheet3) ;  //�N�����q�O-����   2014-02-10 B3018 �s�W 
                stringILCom                =  getDataFromExcel2(32, intRowNo,  objectSheet3) ;  //�N�����q�O-���g   2014-02-10 B3018 �s�W 
                //
                if("null".equals(stringCarHratio))                        stringCarHratio     =  "" ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringCarLratio))                        stringCarLratio     =  "" ;  // 2014-01-02 B3018 �ק�
                if("null".equals(stringCarAHCom))                      stringCarAHCom  =  ""  ; 
                if("null".equals(stringCarBHCom))                      stringCarBHCom  =  ""  ;  
                if("null".equals(stringCarALCom))                      stringCarALCom   =  ""  ; 
                if("null".equals(stringCarBLCom))                      stringCarBLCom   =  ""  ;
                if("null".equals(stringCarCd))                             stringCarCd           =  "" ; 
                if("null".equals(stringCarType))                          stringCarType       =  "" ;  
                if("null".equals(stringCarMainUse))                    stringCarMainUse  =  "" ;  //2012-12-11 B3018 �s�W
                if("null".equals(stringIHCom))                             stringIHCom       =  "" ;  // 2014-02-10 B3018 �s�W
                if("null".equals(stringILCom))                             stringILCom       =  "" ;  // 2014-02-10 B3018 �s�W
                if(exeUtil.doParseDouble(stringCarArea)<=0)    stringCarArea         =  "0" ; 
                //
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";                 
                }                                               
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                // �ˮ�
                if("null".equals(stringPosition))    {
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    intRecord++ ;
                    break ;
                }
                // �s�W
                if (getSale05M092Count(stringPosition)<= 0){
                    stringSql  =  getInsertC_Sale05M342(stringPosition,            stringListPriceNo,        stringHCom,            stringLCom,           stringTypeNo,
                                                   stringPingSu,             stringListUintPrice,       stringListPrice,        stringHListPrice,     stringLListPrice,
                                                   stringFloorUnitPrice,  stringFloorPrice,          stringHFloorPrice,   stringLFloorPrice,  "Car",
                                                   stringSaleKind,          stringCarKind1,            stringCarKind2,       stringCarSize,        stringCarPaper,
                                                  stringSaleFlag,          stringCarCd,                stringCarType,        stringCarArea,       stringCarHratio,
                                                  stringCarLratio,          stringCarAHCom,        stringCarBHCom,     stringCarALCom,   stringCarBLCom,
                                                  stringCarMainUse,     stringCarVisionKind,    stringCarVision,       stringIHCom,          stringILCom) ;
                    System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                    if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                    intRecord++ ;
                }
            }
            stringSql  =  "insert  into  Sale05M342  select  * "  +
                                                                          " from  sale05m040 "  +
                                            " where  ProjectID1  =  '"+stringProjectId+"' "  +
                                               " and  HouseCar = 'Car' "  +
                                                "and  Position not in (select Position "  +
                                                                              " from Sale05M342 "  +
                                                              " where  ProjectID1  =  '"+stringProjectId+"' "  +
                                                                " and  HouseCar = 'Car') "  ;
            System.out.println("SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
            // 2012-11-05 B3018 E
        }
        System.out.println("Car-----�@"  +  intRecord  +  "��") ;
        stringMessage  +=  " �@�@�@�Ыλ��ت� �w��J"  +  intRecord  +  "��" ;
        // ��ڼg�@��Ʈw��
        //if(vectorSql.size( )  >  0)  dbSale.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
          // ���� Excel ����
          getReleaseExcelObject(retVector) ;
        return  true ;
    }
    public  String  getDelete(String  stringPosition,  String  stringHouseCar) throws  Throwable {
        String  stringSql           =  "" ;
        String  stringProjectId  =  getValue("ProjectId").trim( ) ;
        //
        stringSql  =  " DELETE  FROM  Sale05M040 "  +
                    " WHERE  ProjectID1  =  '"  +  stringProjectId  +  "' "  +
                        " AND  HouseCar  =  '"+ stringHouseCar +"' "  +
                      " AND  Position  =  '"  +  stringPosition  +  "' " ;
        return  stringSql ;
    }
    public  String  getDeleteSale05M342(String  stringHouseCar) throws  Throwable {
        String  stringSql           =  "" ;
        String  stringProjectId  =  getValue("ProjectId").trim( ) ;
        //
        stringSql  =  " DELETE  FROM  Sale05M342 "  +
                    " WHERE  ProjectID1  =  '"  +  stringProjectId  +  "' "  +
                        " AND  HouseCar  =  '"+ stringHouseCar +"' "  +
                      " AND  Position NOT IN (SELECT  Sale05M092.Position " +
                                                           " FROM  Sale05M090, Sale05M092 " +
                                                         " WHERE  Sale05M090.OrderNo  =  Sale05M092.OrderNo " +
                                               " AND  Sale05M090.ProjectID1 = '" + stringProjectId + "' " +
                                                               " AND  (Sale05M090.FlowStatus <> '�~��-�@�o'  OR  Sale05M090.FlowStatus IS NULL) "  +
                                           " AND ISNULL(StatusCd,'')<>'D' " +
                                                           ")" ;
        return  stringSql ;
    }
    public  String  getInsert(String  stringPosition,           String  stringListPriceNo,    String  stringHCom,           String  stringLCom,           String  stringTypeNo,
                        String  stringPingSu,             String  stringListUintPrice,  String  stringListPrice,       String  stringHListPrice,     String  stringLListPrice,
                        String  stringFloorUnitPrice,  String  stringFloorPrice,      String  stringHFloorPrice,  String  stringLFloorPrice,  String  stringHouseCar,String stringSaleKind) throws  Throwable {
        String  stringSql           =  "" ;
        String  stringProjectId  =  getValue("ProjectId").trim( ) ;
        //
        stringSql  =  "INSERT  INTO  Sale05M040 (ProjectID1,     HouseCar,   Position,            ListPriceNo,     H_Com, "  +
                                                           " L_Com,          TypeNo,        PingSu,              ListUnitPrice,  ListPrice, "  +
                                           " H_ListPrice,  L_ListPrice,  FloorUnitPrice,  FloorPrice,     H_FloorPrice, "  +
                                           " L_FloorPrice ,SaleKind,EmployeeNo, EDateTime) "  +
                                  " VALUES ( '"  +  stringProjectId        +  "', "  +
                                          " '"  +  stringHouseCar       +  "', "  +  // �ҥ~�A14
                                           " '"  +  stringPosition         +  "', "  +
                                           " '"  +  stringListPriceNo    +  "', "  +
                                           " '"  +  stringHCom             +  "', "  +  // 04
                                           " '"  +  stringLCom             +  "', "  +
                                           " '"  +  stringTypeNo          +  "', "  +
                                                      stringPingSu            +  ", "  +
                                                stringListUintPrice    +  ", "  +
                                                stringListPrice          +  ", "  + // 09
                                                stringHListPrice        +  ", "  +
                                                stringLListPrice        +  ", "  +
                                                stringFloorUnitPrice  +  ", "  +
                                                stringFloorPrice        +  ", "  +
                                                stringHFloorPrice      +  ", "  +  // 14
                                                stringLFloorPrice       +  ", '"  +  stringSaleKind    +  "'  ,'" + getUser() + "','" + datetime.getTime("YYYY/mm/dd h:m:s") + "' ) " ;
        return  stringSql ;
    }   
    // Start �ק���:20100507 ���u�s��:B3774
    public String getInsertC(String stringPosition,           String stringListPriceNo,       String stringHCom,          String stringLCom,          String stringTypeNo,
                          String stringPingSu,            String stringListUintPrice,      String stringListPrice,      String stringHListPrice,   String stringLListPrice,
                          String stringFloorUnitPrice, String stringFloorPrice,         String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar,
                        String stringSaleKind,         String stringCarKind1,           String stringCarKind2,     String stringCarSize,       String stringCarPaper,
                          String  stringSaleFlag,         String stringCarCd,               String  stringCarType,     String  stringCarArea,       String  stringCarHratio,
                          String  stringCarLratio,        String  stringCarAHCom,       String  stringCarBHCom,  String  stringCarALCom,  String  stringCarBLCom,
                        String  stringCarMainUse,    String  stringCarVisionKind,  String  stringCarVision,    String  stringIHCom,         String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 �W�[
        //
        stringSql = "INSERT INTO Sale05M040 (ProjectID1,     HouseCar,        Position,            ListPriceNo,    H_Com, "+
                                        "L_Com,          TypeNo,            PingSu,             ListUnitPrice,  ListPrice, "+
                                        "H_ListPrice,    L_ListPrice,      FloorUnitPrice,  FloorPrice,      H_FloorPrice, "+
                                        "L_FloorPrice, SaleKind,           CarKind1,         CarKind2,      CarSize,    "+
                                        "CarPaper,       EmployeeNo,     EDateTime,      SaleFlag,       CarCd,  "  +
                                        "CarType,         CarArea,           CarHratio,         CarLratio,      CarAH_Com, "  +
                                        "CarBH_Com,    CarAL_Com,    CarBL_Com,     Hratio,            Lratio,  "  +
                                        "CarMainUse,    CarVisionKind ,CarVision,         IH_Com,         IL_Com, "  +
                                        " StartUseDate,  CheckDateTime,  CheckMan) "+
                                 "VALUES ( '"  +  stringProjectId           +  "', "  +
                                        " '"  +  stringHouseCar          +  "', "  +  // �ҥ~�A14
                                         " '"  +  stringPosition            +  "', "  +
                                         " '"  +  stringListPriceNo       +  "', "  +
                                         " '"  +  stringHCom                +  "', "  +  // 04
                                         " '"  +  stringLCom                +  "', "  +
                                         " '"  +  stringTypeNo             +  "', "  +
                                              stringPingSu             +  ", "  +
                                              stringListUintPrice     +  ", "  +
                                              stringListPrice           +  ", "  + // 09
                                              stringHListPrice         +  ", "  +
                                              stringLListPrice         +  ", "  +
                                              stringFloorUnitPrice  +  ", "  +
                                              stringFloorPrice        +  ", "  +
                                              stringHFloorPrice      +  ", "  +  // 14
                                              stringLFloorPrice      +  ", "+
                                         " '"  +  stringSaleKind           +  "', "+
                                         " '"  +  stringCarKind1          +  "', "  +
                                         " '"  +  stringCarKind2          +  "', "  +
                                         " '"  +  stringCarSize             +  "', "  +//19
                                         " '"  +  stringCarPaper          +  "', "  +
                                         " '"  +  getUser()                    + "', "+
                                         " '"  +  datetime.getTime("YYYY/mm/dd h:m:s") + "', "+
                                         " '"  +  stringSaleFlag          +  "',  "  +  // 2012/06/29 B3018 �ק�    
                                         " '"  +  stringCarCd              +  "',  "  +   // 2012-08-03 B3018 �ק�    24
                                         " '"  +  stringCarType          +  "',  "  +   // 2012-08-03 B3018 �ק�
                                         "  "  +  stringCarArea          +  ",  "  +  // 2012-08-03 B3018 �ק�
                                         " '"  +  stringCarHratio         +  "',  "  +    // 2014-01-02 B3018 �ק�
                                         " '"  +  stringCarLratio        +  "',  "  +    // 2014-01-02 B3018 �ק�
                                         " '"  +  stringCarAHCom      +  "',  "  +  // 2012-11-05 B3018 �ק�    29
                                         " '"  +  stringCarBHCom      +  "',  "  +  // 2012-11-05 B3018 �ק�
                                         " '"  +  stringCarALCom       +  "',  "  +   // 2012-11-05 B3018 �ק�
                                         " '"  +  stringCarBLCom      +  "',  "  +      // 2012-08-03 B3018 �ק�
                                         "  "  +  "0"                            +  " ,  "  +   // 2012-12-04 B3018 �ק�      
                                         "  "  +  "0"                            +  ",  "  +  // 2012-08-03 B3018 �ק�      34
                                         " '"  +  stringCarMainUse     +  "',  "  +   // 2012-12-11 B3018 �ק�
                                         " '"  +  stringCarVisionKind  +  "',  "  +   // 2012-12-17 B3018 �ק�
                                         " '"  +  stringCarVision          +  "',  "  +   // 2012-12-17 B3018 �ק�
                                         " '"  +  stringIHCom               +  "',  "  +  // 2014-02-10 B3018 �ק�
                                         " '"  +  stringILCom                +  "',  "  +   // 2017-06-02 B3018 �ק�      39
                                         " '"  +  stringStartUseDate    +  "',  "  +  // 2017-06-02 B3018 �ק�
                                         " '"  +  ""                    +  "',  "  +  // 2017-06-02 B3018 �ק�
                                         " '"  +  ""                    +  "')  "  ;  // 2017-06-02 B3018 �ק�
        return stringSql;
    }
    // End �ק���:20100507 ���u�s��:B3774
    public String getInsertC_Sale05M342(String stringPosition,           String stringListPriceNo,      String stringHCom,          String stringLCom,          String stringTypeNo,
                                               String stringPingSu,            String stringListUintPrice,      String stringListPrice,      String stringHListPrice,   String stringLListPrice,
                                               String stringFloorUnitPrice, String stringFloorPrice,         String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar,
                                             String stringSaleKind,         String stringCarKind1,           String stringCarKind2,     String stringCarSize,       String stringCarPaper,
                                               String  stringSaleFlag,         String stringCarCd,               String  stringCarType,     String  stringCarArea,       String  stringCarHratio,
                                               String  stringCarLratio,        String  stringCarAHCom,       String  stringCarBHCom,  String  stringCarALCom,  String  stringCarBLCom,
                                             String  stringCarMainUse,    String  stringCarVisionKind,  String  stringCarVision,    String  stringIHCom,         String  stringILCom) throws Throwable{
        String stringSql                    = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 �W�[
        //
        stringSql = "INSERT INTO Sale05M342 (ProjectID1,     HouseCar,    Position,           ListPriceNo,   H_Com, "+
                                        "L_Com,          TypeNo,       PingSu,             ListUnitPrice, ListPrice, "+
                                        "H_ListPrice,    L_ListPrice,  FloorUnitPrice, FloorPrice,    H_FloorPrice, "+
                                        "L_FloorPrice, SaleKind,      CarKind1,         CarKind2,      CarSize,    "+
                                        "CarPaper,       EmployeeNo, EDateTime,     SaleFlag,       CarCd,  "  +
                                        "CarType,         CarArea,       CarHratio,         CarLratio,      CarAH_Com, "  +
                                        "CarBH_Com,    CarAL_Com,  CarBL_Com,   Hratio,            Lratio,  "  +
                                        " CarMainUse,    CarVisionKind ,CarVision,    IH_Com,         IL_Com, "  +
                                        " MFlag,               StartUseDate,  CheckDateTime,  CheckMan) "+
                                 "VALUES ( '"  +  stringProjectId           +  "', "  +
                                        " '"  +  stringHouseCar          +  "', "  +  // �ҥ~�A14
                                         " '"  +  stringPosition            +  "', "  +
                                         " '"  +  stringListPriceNo       +  "', "  +
                                         " '"  +  stringHCom                +  "', "  +  // 04
                                         " '"  +  stringLCom                +  "', "  +
                                         " '"  +  stringTypeNo             +  "', "  +
                                              stringPingSu             +  ", "  +
                                              stringListUintPrice     +  ", "  +
                                              stringListPrice           +  ", "  + // 09
                                              stringHListPrice         +  ", "  +
                                              stringLListPrice         +  ", "  +
                                              stringFloorUnitPrice  +  ", "  +
                                              stringFloorPrice        +  ", "  +
                                              stringHFloorPrice      +  ", "  +  // 14
                                              stringLFloorPrice      +  ", "+
                                         " '"  +  stringSaleKind           +  "', "+
                                         " '"  +  stringCarKind1          +  "', "  +
                                         " '"  +  stringCarKind2          +  "', "  +
                                         " '"  +  stringCarSize             +  "', "  +
                                         " '"  +  stringCarPaper          +  "', "  +
                                         " '"  +  getUser()                    + "', "+
                                         " '"  +  datetime.getTime("YYYY/mm/dd h:m:s") + "', "+
                                         " '"  +  stringSaleFlag          +  "',  "  +  // 2012/06/29 B3018 �ק�
                                         " '"  +  stringCarCd              +  "',  "  +   // 2012-08-03 B3018 �ק�
                                         " '"  +  stringCarType          +  "',  "  +   // 2012-08-03 B3018 �ק�
                                         "  "  +  stringCarArea          +  ",  "  +  // 2012-08-03 B3018 �ק�
                                         " '"  +  stringCarHratio        +  "',  "  +    // 2014-01-02 B3018 �ק�
                                         " '"  +  stringCarLratio        +  "',  "  +     // 2014-01-02 B3018 �ק�
                                         " '"  +  stringCarAHCom      +  "',  "  +  // 2012-11-05 B3018 �ק�
                                         " '"  +  stringCarBHCom      +  "',  "  +  // 2012-11-05 B3018 �ק�
                                         " '"  +  stringCarALCom       +  "',  "  +   // 2012-11-05 B3018 �ק�
                                         " '"  +  stringCarBLCom      +  "',  "  +  // 2012-08-03 B3018 �ק�
                                         "  "  +  "0"                            +  " ,  "   +  // 2012-12-04 B3018 �ק�
                                         "  "  +  "0"                            +  ", "    +   // 2012-12-04 B3018 �ק�
                                         "  '"  +  stringCarMainUse    +  "', "   +   // 2012-12-11 B3018 �ק�
                                         "  '"  +  stringCarVisionKind +  "',  "  +   // 2012-12-11 B3018 �ק�
                                         "  '"  +  stringCarVision        +  "' ,  "  +   // 2012-12-11 B3018 �ק�
                                         "  '"  +  stringIHCom             +  "' ,  "  +  // 2014-02-10 B3018 �ק�
                                         "  '"  +  stringILCom             +  "' ,  "  +  // 2014-02-10 B3018 �ק�
                                         "  '"  +  "Y"                           +  "' ,  "  +  // 2017-06-02 B3018 �ק�
                                         "  '"  +  stringStartUseDate  +  "' ,  "  +  // 2017-06-02 B3018 �ק�
                                         "  '"  +  ""                   +  "' ,  "  +   // 2017-06-02 B3018 �ק�
                                         "  '"  +  ""                   +  "' )  "  ;   // 2017-06-02 B3018 �ק�
        return stringSql;
    }
    // Start �ק���:20100426 ���u�s��:B3774
    // �ק���:2012-08-03 ���u�s��:B3018
    public String getInsertH(String stringPosition,           String stringListPriceNo,   String stringHCom,          String  stringLCom,          String stringTypeNo, 
                        String stringPingSu,            String stringListUintPrice, String stringListPrice,      String  stringHListPrice,    String stringLListPrice, 
                        String stringFloorUnitPrice, String stringFloorPrice,     String stringHFloorPrice, String  stringLFloorPrice, String stringHouseCar, 
                        String stringSaleKind,         String stringTotalSquare,  String stringVisionKind,   String  stringVision,          String  stringSaleFlag,
                        String  stringMainUse,        String  stringBuildCd,         String  stringBuildType,   String  stringBuild1,         String  stringBuild2,
                          String  stringBuild3,            String  stringBuild4,             String  stringHratio,        String  stringLratio,         String  stringAHCom,
                          String  stringBHCom,          String  stringALCom,           String  stringBLCom,      String  stringIHCom,        String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 �W�[
        //
        stringSql = "INSERT INTO Sale05M040 (ProjectID1,     HouseCar,   Position,           ListPriceNo,   H_Com, "+
                                        "L_Com,          TypeNo,       PingSu,            ListUnitPrice, ListPrice, "+
                                        "H_ListPrice,    L_ListPrice, FloorUnitPrice, FloorPrice,     H_FloorPrice, "+
                                        "L_FloorPrice, SaleKind,     EmployeeNo,    EDateTime,   Square, "+
                                        "VisionKind,     Vision,         SaleFlag,          MainUse,        BuildCd, "  +
                                        "BuildType,      Build1,         Build2,              Build3,           Build4,  "+
                                       " Hratio,             Lratio,          AH_Com,         BH_Com,       AL_Com, "  +
                                       " BL_Com,         CarHratio,    CarLratio,        CarMainUse, CarVisionKind, "  +
                                       " CarVision,       IH_Com,       IL_Com,            StartUseDate,  CheckDateTime, "  +
                                       " CheckMan ) "+
                              " VALUES ( '" + stringProjectId                                        + "', " +        // 0
                                         " '" + stringHouseCar                                      + "', " +   
                                         " '" + stringPosition                                         + "', " +
                                         " '" + stringListPriceNo                                    + "', " +
                                         " '" + stringHCom                                            + "', " +       // 4
                                         " '" + stringLCom                                            + "', " +
                                         " '" + stringTypeNo                                         + "', " +
                                             stringPingSu                                           + ", " +
                                             stringListUintPrice                                   + ", " +
                                             stringListPrice                                         + ", " +     //9
                                             stringHListPrice                                       + ", " +      //
                                             stringLListPrice                                       + ", " +
                                             stringFloorUnitPrice                                + ", " +
                                             stringFloorPrice                                      + ", " +
                                             stringHFloorPrice                                    + ", " +    //14
                                             stringLFloorPrice                                    + ", " +
                                         " '" + stringSaleKind                                        + "'  ," +
                                         " '" + getUser()                                                + "'," +
                                         " '" + datetime.getTime("YYYY/mm/dd h:m:s") + "'," +
                                         "  " + stringTotalSquare                                   + "," +       //19
                                         " '" + stringVisionKind                                      + "'," +
                                         "  " + stringVision                                             + "," +
                                         " '" + stringSaleFlag                                         + "', " +  // 2012/06/29 B3018 �ק�
                                         " '" + stringMainUse                                         + "',  " + // 2012/08/03 B3018 �ק�
                                         " '" + stringBuildCd                                          + "',  " + // 2012/08/03 B3018 �ק�      24
                                         " '" + stringBuildType                                      + "',  " + // 2012/08/03 B3018 �ק�
                                         " '" + stringBuild1                                            + "',  " + // 2012/08/03 B3018 �ק�
                                         " '" + stringBuild2                                             + "',  " + // 2012/08/03 B3018 �ק�
                                         " '" + stringBuild3                                             + "',  " + // 2012/08/03 B3018 �ק�
                                         " '" + stringBuild4                                              + "', " +  // 2012/08/03 B3018 �ק�     29
                                         " '" + stringHratio                                              + "', " + // 2014/01/02 B3018 �ק�
                                         " '" + stringLratio                                              + "', " + // 2014/01/02 B3018 �ק�
                                         " '" + stringAHCom                                           + "' , " + // 2012/11/05 B3018 �ק�
                                         " '" + stringBHCom                                           + "' , " + // 2012/11/05 B3018 �ק�
                                         " '" + stringALCom                                           + "' , " + // 2012/11/05 B3018 �ק�       34
                                         " '" + stringBLCom                                           + "' , " +  // 2012/11/05 B3018 �ק�
                                         "  " + "0"                                                          + ",  "  +  // 2012/12/04 B3018 �ק�
                                         "  " + "0"                                                          + ", "   +  // 2012/12/04 B3018 �ק�
                                         "  '" + ""                                                            + "' , " +  // 2012/12/11 B3018 �ק�
                                         "  '" + ""                                                            + "' , " +  // 2012/12/17 B3018 �ק�     39
                                         "   " + "0"                                                          + " , "  + // 2012/12/17 B3018 �ק�
                                         "  '" + stringIHCom                                            + "',  " + // 2014/02/10 B3018 �ק�
                                         "  '" + stringILCom                                            + "',  "  +  // 2017/06/02 B3018 �ק�
                                         "  '" + stringStartUseDate                                  + "' , " + // 2017/06/02 B3018 �ק�
                                         "  '" + ""                                                 + "',  " +  // 2017/06/02 B3018 �ק�    44
                                         "  '" + ""                                                 + "' ) "; // 2017/06/02 B3018 �ק�
        return stringSql;
    }
    // End �ק���:20100426 ���u�s��:B3774
    public String getInsertH_Sale05M342(String stringPosition,           String stringListPriceNo,   String stringHCom,          String stringLCom,          String stringTypeNo, 
                                              String stringPingSu,            String stringListUintPrice, String stringListPrice,      String stringHListPrice,    String stringLListPrice, 
                                              String stringFloorUnitPrice, String stringFloorPrice,     String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar, 
                                             String stringSaleKind,         String stringTotalSquare,  String stringVisionKind,   String stringVision,          String  stringSaleFlag,
                                             String  stringMainUse,        String  stringBuildCd,         String  stringBuildType,   String  stringBuild1,         String  stringBuild2,
                                               String  stringBuild3,            String  stringBuild4,             String  stringHratio,         String  stringLratio,         String  stringAHCom,
                                               String  stringBHCom,          String  stringALCom,           String  stringBLCom,       String  stringIHCom,        String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 �W�[
        //
        if("".equals(stringVision))  stringVision  =  "0" ;
        //
        stringSql = "INSERT INTO Sale05M342 (ProjectID1,     HouseCar,   Position,           ListPriceNo,   H_Com, "+
                                        "L_Com,          TypeNo,       PingSu,            ListUnitPrice, ListPrice, "+
                                        "H_ListPrice,    L_ListPrice, FloorUnitPrice, FloorPrice,     H_FloorPrice, "+
                                        "L_FloorPrice, SaleKind,     EmployeeNo,    EDateTime,   Square, "+
                                        "VisionKind,     Vision,         SaleFlag,          MainUse,        BuildCd, "  +
                                        "BuildType,      Build1,         Build2,              Build3,           Build4,  "+
                                       " Hratio,             Lratio,          AH_Com,         BH_Com,       AL_Com, "  +
                                       " BL_Com,        CarHratio,   CarLratio,          CarMainUse,  CarVisionKind, "  +
                                       " CarVision,      IH_Com,       IL_Com,             MFlag,           StartUseDate, " +
                                       " CheckDateTime,     CheckMan) "+
                                " VALUES ( '" + stringProjectId                                        + "', " +
                                         " '" + stringHouseCar                                      + "', " +
                                         " '" + stringPosition                                         + "', " +
                                         " '" + stringListPriceNo                                    + "', " +
                                         " '" + stringHCom                                            + "', " +
                                         " '" + stringLCom                                            + "', " +
                                         " '" + stringTypeNo                                         + "', " +
                                             stringPingSu                                           + ", " +
                                             stringListUintPrice                                   + ", " +
                                             stringListPrice                                         + ", " +
                                             stringHListPrice                                       + ", " +
                                             stringLListPrice                                       + ", " +
                                             stringFloorUnitPrice                                + ", " +
                                             stringFloorPrice                                      + ", " +
                                             stringHFloorPrice                                    + ", " +
                                             stringLFloorPrice                                    + ", " +
                                         " '" + stringSaleKind                                        + "'  ," +
                                         " '" + getUser()                                                + "'," +
                                         " '" + datetime.getTime("YYYY/mm/dd h:m:s") + "'," +
                                         "  " + stringTotalSquare                                   + "," +
                                         " '" + stringVisionKind                                      + "'," +
                                         "  " + stringVision                                             + "," +
                                         " '" + stringSaleFlag                                         + "', " +  
                                         " '" + stringMainUse                                         + "',  " +
                                         " '" + stringBuildCd                                          + "',  " + 
                                         " '" + stringBuildType                                      + "',  " + 
                                         " '" + stringBuild1                                            + "',  " + 
                                         " '" + stringBuild2                                             + "',  " +
                                         " '" + stringBuild3                                             + "',  " + 
                                         " '" + stringBuild4                                              + "', " + 
                                         " '" + stringHratio                                              + "', " +   // 2014-01-02 B3018 �ק�
                                         " '" + stringLratio                                              + "', " +   // 2014-01-02 B3018 �ק�
                                         " '" + stringAHCom                                           + "' , " + 
                                         " '" + stringBHCom                                           + "' , " + 
                                         " '" + stringALCom                                           + "' , " +
                                         " '" + stringBLCom                                           + "' , "+
                                         "  " + "0"                                                          + ",  "+
                                         "  " + "0"                                                          + ", " +
                                         "  '" + ""                                                            + "', " + 
                                         "  '" + ""                                                            + "', " +
                                         "   " + "0"                                                         + " , " +
                                         "  '" + stringIHCom                                           + "',  "  +   // 2014-02-10 B3018 �ק�
                                         "  '" + stringILCom                                           + "' , " +   // 2014-02-10 B3018 �ק�
                                         "   '" + "Y"                                                        + "' , " + // 2017-06-02 B3018 �ק�
                                         "   '" + stringStartUseDate                               + "' , " + // 2017-06-02 B3018 �ק�
                                         "   '" + ""                                                          + "' , " + // 2017-06-02 B3018 �ק�
                                         "   '" + ""                                                           + "' ) " ; // 2017-06-02 B3018 �ק�
        return stringSql;
    }
    public  boolean  isBatchCheckOK(FargloryUtil  exeUtil) throws  Throwable {
        // Excel �ɮפ��i�ť�
        String  stringExcelFile  =  getValue("ExcelFile").trim( ) ;
        if("".equals(stringExcelFile)) {
            message("�п���ɮסC") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;
        }
        // �榡�P�_
        String[]  arrayTemp  =  convert.StringToken(stringExcelFile,  ".") ;        
        // Start �ק���:20100805 ���u�s��:B3774
        //if(!"XLS".equals(arrayTemp[1].trim( ).toUpperCase( ))) {
        if(!"XLS".equals(arrayTemp[arrayTemp.length-1].trim( ).toUpperCase( ))) {
        // End �ק���:20100805 ���u�s��:B3774
            message("�п�� Excel �榡���ɮסC") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;       
        }
        // Excel �ɮצs�b�P�_
        File  excelFile  =  new  File(stringExcelFile) ;
        if(!excelFile.exists()) {
            message("�����s�b�I") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;
        }
        // �קO
        String      stringProjectId  =  getValue("ProjectId").trim( ) ;
        if("".equals(stringProjectId)) {
            message("[�קO] ���i�ťաC") ;
            getcLabel("ProjectId").requestFocus( ) ;
            return  false ;
        }
        if(!isProjectIdExistOK(stringProjectId)) {
            message("[�קO] ���s�b���Ʈw���C") ;
            getcLabel("ProjectId").requestFocus( ) ;
            return  false ;
        }
        // �ҥΤ��
        String      stringStartUseDate  =  getValue("StartUseDate").trim( ) ;
        if("".equals(stringStartUseDate)) {
            message("[�ҥΤ��] ���i�ťաC") ;
            getcLabel("StartUseDate").requestFocus( ) ;
            return  false ;
        }
        // 2017-06-02 B3018 �W�[ Start
        String                stringDateRoc  =  exeUtil.getDateAC (stringStartUseDate,  "�ݥΤ��") ;
        if(stringDateRoc.length()  !=  10) {
            message(stringStartUseDate) ;
            getcLabel("StartUseDate").requestFocus( ) ;
            return  false ;
        } 
        setValue("StartUseDate",  stringDateRoc) ;
        // 2017-06-02 B3018 �W�[ END
        message("") ;
        return  true ;
    }
    // �קO
    public  boolean  isProjectIdExistOK(String  stringProjectId) throws  Throwable {
        talk           dbSale             =  getTalk(""  +  get("put_dbSale")) ;
        String      stringSql           =  "" ;
        String[][]  retAGroup        =  null ;
        //
        stringSql  =  "SELECT  ProjectID1  FROM  A_Group  WHERE  ProjectID1  =  '"  +  stringProjectId  +  "' " ;
        retAGroup  =  dbSale.queryFromPool(stringSql) ;
        if(retAGroup.length  ==  0)  return  false ;
        return  true ;
    }
    //
    public  int  getSale05M092Count(String  stringPosition) throws  Throwable {       
        talk           dbSale             =  getTalk(""  +  get("put_dbSale")) ;
        String stringSql=" SELECT Sale05M092.Position FROM Sale05M090,Sale05M092  " +                                 
            " WHERE Sale05M090.OrderNo = Sale05M092.OrderNo " +
              " AND Sale05M090.ProjectID1 = '" + getValue("ProjectId").trim() + "'" +
              " AND Sale05M092.Position = '" + stringPosition + "'" +
        " AND (Sale05M090.FlowStatus <> '�~��-�@�o' OR Sale05M090.FlowStatus IS NULL)  AND ISNULL(StatusCd,'')<>'D' " ;
        String retSale05M092[][] = dbSale.queryFromPool(stringSql);
        return  retSale05M092.length ;
    }             
    // Excel
    // �إ� Excel ����
    public Vector getExcelObject(String stringExcelName){
        Vector  retVector  =  new  Vector( ) ;
        // �إ�com����
        ActiveXComponent Excel;
        ComThread.InitSTA();
        Excel  =  ExcelVerson();
        Excel.setProperty("Visible",  new Variant(true));   
        Excel.setProperty("DisplayAlerts", new Variant(false));       
        Object    objectExcel      =  Excel.getObject();
        Object    objectWorkbooks  =  Dispatch.get(objectExcel,  "Workbooks").toDispatch();
        Object    objectWorkbook   = null ;
        objectWorkbook    =  Dispatch.call(objectWorkbooks, "Open", stringExcelName).toDispatch();
        // objectWorkbook = Dispatch.call(objectWorkbooks, "Open", "http://mis_emaker1:8080/farglory/Sale/Sale05R120.xls").toDispatch();
        Object    objectSheets  =  Dispatch.get(objectWorkbook,  "Sheets").toDispatch();
        Object    objectSheet1  =  Dispatch.call(objectSheets,  "Item",  "������ت�").toDispatch() ;
        //Object    objectSheet1  =  Dispatch.call(objectSheets,  "Item",  new Variant(3)).toDispatch() ;
        Object    objectSheet2  =  Dispatch.call(objectSheets,  "Item",  "�Ыλ��ت�").toDispatch() ;
        //Object    objectSheet2  =  Dispatch.call(objectSheets,  "Item",  new Variant(4)).toDispatch() ;
        Object    objectSheet3  =  Dispatch.call(objectSheets,  "Item",  "������ت�(�@��)").toDispatch() ;
        Object    objectSheet4  =  Dispatch.call(objectSheets,  "Item",  "�Ыλ��ت�(�@��)").toDispatch() ;
        //A1 for Copy &Paste
        //Dispatch.call(objectSheet1,  "Activate");
        Object    object1A1  =  null ;
        object1A1    =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        //Dispatch.call(objectSheet2,  "Activate");
        Object    object2A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet2,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        // 2012-11-05 B3018 �ק� S
        Object    object3A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet3,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        Object    object4A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet4,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        // 2012-11-05 B3018 �ק� E

        // �^�Ǹ��
        retVector.add(Excel) ;
        retVector.add(objectSheet1) ;
        retVector.add(objectSheet2) ;
        retVector.add(object1A1) ;
        retVector.add(object2A1) ;
        // 2012-11-05 B3018 �ק� S
        retVector.add(objectSheet3) ;
        retVector.add(objectSheet4) ;
        retVector.add(object3A1) ;
        retVector.add(object4A1) ;
        // 2012-11-05 B3018 �ק� E
        return  retVector ;
    }
    // ��ܨ����� Excel ����
    public void getReleaseExcelObject(Vector  vectorExcelObject){
        // ��ƳB�z
        ActiveXComponent  Excel  =  (ActiveXComponent)  vectorExcelObject.get(0) ;
        Object    objectSheet1      =  vectorExcelObject.get(1) ;
        Object    objectSheet2      =  vectorExcelObject.get(2) ;
        //��� Excel
        Excel.setProperty("DisplayAlerts", new Variant(true));        
        Dispatch.call(Excel, "Quit");
        // ����com����
        ComThread.Release() ;
    }
    //��Client Excel Version �}��
    public ActiveXComponent ExcelVerson(){
      ActiveXComponent Excel;
      ComThread.InitSTA();
      int intExcelVerson = 0;
      try{
         Excel = new ActiveXComponent("Excel.Application.8");//Excel 97
         System.out.println("Excel 97 is OK!");
         return Excel;       
      }catch(Exception Excel97){
        try{
           Excel = new ActiveXComponent("Excel.Application.9");//Excel 2000
           System.out.println("Excel 2000 is OK!");          
           return Excel;
         }catch(Exception Excel2000){
            try{
               Excel = new ActiveXComponent("Excel.Application.10");//Excel 2002
               System.out.println("Excel 2002 is OK!");              
               return Excel;
            }catch(Exception Excel2002){
              try{
                 Excel = new ActiveXComponent("Excel.Application.11");//Excel 2003
                 System.out.println("Excel 2003 is OK!");              
                return Excel;
              }catch(Exception ExcelError){
                  System.out.println("�Шϥ� Excel 97 �H�W����!");                             
                  message("�Шϥ� Excel 97 �H�W����!");  
              }
            }
          }
        }
        Excel = new ActiveXComponent("Excel.Application");
        System.out.println("All is OK!");                  
        return Excel;       
    }
      //Excel ���B�z
        public int getExcelColumnNo(String stringColumn){
                    int  intReturn  =  0 ;
            for(int  intIndex=0  ;  intIndex<stringColumn.length()  ;  intIndex++) {
                intReturn  +=  (stringColumn.charAt(intIndex)  -  'A'  +  1)  *  Math.pow(26,  stringColumn.length()-1-intIndex);
            }
            return intReturn;          
        }
      //Excel ���B�z
    public String getExcelColumnName(String stringColumn,int intCalculate){
                    //char charColumn = stringColumn.charAt(0);
                    int intColumn = getExcelColumnNo(stringColumn) + 'A'  -1;
                    String stringReturn = ""; 
                    // < A
                    if ((intColumn + intCalculate) < 65 ) {
                stringReturn = "0";
                message("[���p��A] ���~!") ;
            }
            //int  intTemp  =  (intColumn + intCalculate - 'A' -1)  /  26 ;
          int  intTemp  =  (intColumn + intCalculate - 'A')  /  26 ;
            stringReturn  =  Character.toString((char)(((intColumn + intCalculate - 'A')  %  26) +  'A')) ;
            while(intTemp  > 0) {
                stringReturn  =  Character.toString((char)((intTemp  %  26)  +  'A'  -  1))  +  stringReturn ;
                intTemp  =  intTemp  /  26 ;
            }
            if(intTemp  >  0) {
                stringReturn  =  Character.toString((char)((intTemp  %  26)  +  'A'  -  1))  +  stringReturn ;
            }
                    return stringReturn;            
        }
      // intPosColumn       Excel ����m(��)
      // intPosRow            Excel ����m(�C)
      //  objectSheet2        Excel ���u�@��
      public String  getDataFromExcel(int  intPosColumn,  int  intPosRow,  Object  objectSheet)throws  Throwable {    
          String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
          Object cell                =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
          String  stringReturn  =  Dispatch.get(cell,"Value").toString();
        //
        return  stringReturn.trim() ;
      }
      public String  getDataFromExcel2(int  intPosColumn,  int  intPosRow,  Object  objectSheet)throws  Throwable {   
          String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
          Object cell                =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
          String  stringReturn  =  Dispatch.get(cell,"Value").toString();
        //
        if("null".equals(stringReturn))  stringReturn  =  "" ;
        //
        if(!"".equals(stringReturn)) {
            Farglory.util.FargloryUtil  exeUtil  =  new  Farglory.util.FargloryUtil() ;
            if(exeUtil.doParseDouble(stringReturn)  >  0) {
              stringReturn  =  exeUtil.doDeleteDogAfterZero (stringReturn);
            }
        }
        return  stringReturn.trim() ;
      }
    //
    public String getInformation(){
      return "---------------button1(����).defaultValue()----------------";
    }
}
