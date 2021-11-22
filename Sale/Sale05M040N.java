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
          messagebox("轉檔權限不允許!!!");
          return value;
        }
        //201808check FINISH      
        FargloryUtil  exeUtil  =  new  FargloryUtil() ;
        // 檔案對話方塊
        setValue("ExcelFile",  "")  ;
        JFileChooser  fileDialog  =  new  JFileChooser( ) ;
        // 顯示檔案對話方塊
        int  ret  =  fileDialog.showOpenDialog(null) ;
        // 取得使用者選擇的檔案
        File  obj  =  fileDialog.getSelectedFile( ) ;
        // 按下[開啟]鍵後，顯示檔案的路徑
        if(ret  ==  JFileChooser.APPROVE_OPTION) {
            setValue("ExcelFile",  obj.getPath( ))  ;
        } else {
             return value;  
        }
        //
        if(!isBatchCheckOK(exeUtil))  return value ;
        boolean  booleanOK  =  doUpdate(exeUtil) ;
        if(booleanOK) {
            JOptionPane.showMessageDialog(null,  "價目表轉檔成功。",  "訊息",  JOptionPane.ERROR_MESSAGE) ;
        } else {
            JOptionPane.showMessageDialog(null,  "價目表轉檔失敗。",  "訊息",  JOptionPane.ERROR_MESSAGE) ;
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
        // 車位價目表
        String[]    arrayCarField          =  {"車位別",                    "價目表NO",                 "公司別代碼-車位建物",      "公司別",                 "公司別代碼-車位土地", 
                                                             "公司別",                    "樓層",                         "性質",                                "類別",                     "size",
                                   "產品別代碼",             "產品別",                     "權狀Y/N",                           "表價",                     "表價-車位建物款",
                                   "表價-車位土地款",     "底價",                         "底價-車位建物款",             "底價-車位土地款" ,  "租售並進",  
                                   "遠壽代銷關係",          "車位類別-代碼",          "車位類別",                         "車位持分面積",        "主要用途",  // 2012-12-11 B3018 增加 主要用途
                                   "持分比例-車位建物",  "持分比例-車位土地",  "委託銷售人-車房",              "受託銷售人-車房",   "委託銷售人-車土",   
                                   "受託銷售人-車土",      "代收公司別-車房",      "代收公司別-車土"} ;  // 2014-02-10 B3018 修改
        String[]    arrayHouseField      =   {"棟別",                    "樓別",                       "戶別",                      "價目表NO",           "公司別代碼-房屋",
                                                              "公司別",                "公司別代碼-土地",    "公司別",                   "產品別代碼",         "產品別",
                                                  "坪數",                    "牌單",                       "牌價",                      "牌價-房屋",           "牌價-土地",  
                                                  "底單",                     "底價",                       "底價-房屋",             "底價-土地",           "租售並進",  
                                    "遠壽代銷關係",       "主要用途",                "建物型態-代碼",      "建物型態",             "房",  
                                    "廳",                        "衛",                           "隔間",                      "持分比例-房屋",    "持分比例-土地",
                                    "委託銷售人-房屋",  "受託銷售人-房屋",     "委託銷售人-土地",  "受託銷售人-土地",  "代收公司別-房屋",
                                    "代收公司別-土地"} ;  // 2014-02-10 B3018 修改
        Object     objectUsedRange    =  null ;
        Vector     vectorSql                 =  new  Vector( ) ;
          // 取得 Exce 物件
          Vector    retVector         =  getExcelObject(stringExcelFile) ;
          Object    objectSheet1   =  retVector.get(1) ; // 車位價目表
        Object    objectSheet2   =  retVector.get(2) ; // 房屋價目表
          Object    object1A1        =  retVector.get(3) ;
        Object    object2A1        =  retVector.get(4) ;
        // 2012-11-05 B3018 修改 S
        Object    objectSheet3   =  retVector.get(5) ; // 車位價目表(共有)
        Object    objectSheet4   =  retVector.get(6) ; // 房屋價目表(共有)
          Object    object3A1        =  retVector.get(7) ;
        Object    object4A1        =  retVector.get(8) ;
        // 2012-11-05 B3018 修改 E
        // 車位
        //Dispatch.call(object1A1,  "Select");
        System.out.println("stringType:"+stringType);
        if("全部".equals(stringType)  ||  "車位".equals(stringType)) {
            // 回傳已使用的總行數及總欄數($A$1:$BN$132)
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
        //System.out.println("車位-----"   +  stringTemp+"----"+arrayTemp.length  +  "--------------"+intCarTotalRow) ;
        // 房屋
        //Dispatch.call(object2A1,  "Select");
        if("全部".equals(stringType)  ||  "房屋".equals(stringType)) {
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
        System.out.println("ff房屋-----"   +  stringTemp+"----"+arrayTemp.length  +  "--------------"+intHouseTotalRow) ;
        // 欄位名稱檢核 房屋價目表
        //Dispatch.call(object2A1,  "Select");
        for(int  intFieldNo=0  ;  intFieldNo<arrayHouseField.length  ;  intFieldNo++) {
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet2) ;
            System.out.println(intFieldNo  +  "------"  +  arrayHouseField[intFieldNo]+"---------------"+stringTemp) ;
            if(!arrayHouseField[intFieldNo].equals(stringTemp)) {
                message("房屋價目表 第1列-第"  +  (intFieldNo+1)  +  "欄必須為("  +  arrayHouseField[intFieldNo]  +  ")") ;
                // 釋放 Excel 物件
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 S
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet4) ;
            if(!arrayHouseField[intFieldNo].equals(stringTemp)) {
                message("房屋價目表(共有)第1列-第"  +  (intFieldNo+1)  +  "欄必須為("  +  arrayHouseField[intFieldNo]  +  ")現為("+stringTemp+")") ;
                // 釋放 Excel 物件
                getReleaseExcelObject(retVector) ;
                return false ;
            }           
            // 2012-11-05 B3018 E
        }
        // 欄位名稱檢核 車位價目表
        //Dispatch.call(object1A1,  "Select");
        for(int  intFieldNo=0  ;  intFieldNo<arrayCarField.length  ;  intFieldNo++) {
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet1) ;
            //System.out.println(intFieldNo  +  "------"  +  arrayCarField[intFieldNo]+"---------------"+stringTemp) ;
            if(!arrayCarField[intFieldNo].equals(stringTemp)) {
                message("車位價目表第1列-第"  +  (intFieldNo+1)  +  "欄必須為("  +  arrayCarField[intFieldNo]  +  ")") ;
                // 釋放 Excel 物件
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 S
            stringTemp  =  getDataFromExcel(intFieldNo, 0,  objectSheet3) ;
            if(!arrayCarField[intFieldNo].equals(stringTemp)) {
                message("車位價目表(合計)第1列-第"  +  (intFieldNo+1)  +  "欄必須為("  +  arrayCarField[intFieldNo]  +  ")") ;
                // 釋放 Excel 物件
                getReleaseExcelObject(retVector) ;
                return false ;
            }
            // 2012-11-05 B3018 E
        }       
        // Start 修改日期:20100426 員工編號:B3774
        // 比對EXCEL坪數和Sale05M402坪數是否一致
        String    retData[][]             =   new  String[0][0] ;
        String    retSale05M402[][] =   new  String[0][0] ;
        String    stringProjectID1    = getValue("ProjectId").trim();
        String    stringVisionKind    = "";
        String    stringVision           = "0";
        String    stringPosition        = "";
        String    stringPingSu         = "";
        boolean blnHasPosition     = false;
        //
/* 2013-08-20 B3018 先註解
        if("全部".equals(stringType)  ||  "房屋".equals(stringType)){
            stringSql = "select TOP 1 VisionKind, Vision "+
                      "from Sale05M401 "+
                      "where ProjectID1='"+stringProjectID1+"' "+
                      "and HouseCar='House' "+
                      "and convert(char(10),getdate(),111) between StartDate and EndDate "+
                      "order by VisionKind desc";
            retData = dbSale.queryFromPool(stringSql);
            if(retData.length == 0){
                message("房屋 沒有坪數資料可以比對!");
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
                    message("房屋 沒有坪數資料可以比對!");
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
                if("null".equals(stringPosition))    continue ;           // 2012-11-26 B3018 新增
                if("null".equals(stringPingSu))      stringPingSu    =  "0" ;  // 2012-11-26 B3018 新增
                if("".equals(stringPingSu))             stringPingSu    =  "0" ;  
                //
                if (getSale05M092Count(stringPosition)<= 0){
                    for(int intNo=0; intNo<retSale05M402.length; intNo++){
                        System.out.println("["+intRow+"]["+intNo+"--------------------------------------stringPosition("+stringPosition+")("+retSale05M402[intNo][0]+")") ;
                        System.out.println(intNo+"--------------------------------------stringPingSu("+stringPingSu+")("+retSale05M402[intNo][1]+")") ;
                        if(stringPosition.equals(retSale05M402[intNo][0])){
                            if(operation.compareTo(stringPingSu,retSale05M402[intNo][1]) != 0){
                                message("房屋 "  +stringPosition+"的坪數錯誤!");
                                getReleaseExcelObject(retVector);
                                return false;
                            }
                            blnHasPosition = true;
                        }
                    }
                    if(!blnHasPosition){
                        message("房屋 沒有"+stringPosition+"的面積資料!");
                        getReleaseExcelObject(retVector);
                        return false;
                    }
                }
            }
        }
2013-08-20 B3018 先註解 */ 
        // End 修改日期:20100426 員工編號:B3774
        String      stringCarArea             =  "" ;  // 
        String      stringCarVisionKind    =  "" ;
        String      stringCarVision            =  "0" ;
        String[][]  retSale05M407           =  new  String[0][0] ;
/* 2013-08-20 B3018 先註解
        if("全部".equals(stringType)  ||  "車位".equals(stringType)) {
            // 坪數檢查
            stringSql = "select TOP 1 VisionKind, Vision "+
                      "from Sale05M401 "+
                      "where ProjectID1='"+stringProjectID1+"' "+
                      "and HouseCar='Car' "+
                      "and convert(char(10),getdate(),111) between StartDate and EndDate "+
                      "order by VisionKind desc";
            retData = dbSale.queryFromPool(stringSql);
            if(retData.length == 0){
                message("車位 沒有坪數資料可以比對!");
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
                    message("車位 沒有坪數資料可以比對!");
                    getReleaseExcelObject(retVector);
                    return false;
                }
            }
            //
            for(int intRow=1; intRow<intCarTotalRow; intRow++){
                blnHasPosition = false;
                stringPosition   =  getDataFromExcel2(0,   intRow,  objectSheet1) ;    // 戶別
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
                                message("車位 "+stringPosition+"的坪數錯誤!");
                                getReleaseExcelObject(retVector);
                                return false;
                            }
                            blnHasPosition = true;
                        }
                    }
                    if(!blnHasPosition){
                        message("車位 沒有"+stringPosition+"的面積資料!");
                        getReleaseExcelObject(retVector);
                        return false;
                    }
                }
            }
        }
2013-08-20 B3018 先註解 */
        // 資料庫動作
        int        intRecord                 =  0 ;
        //String  stringPosition           =  "" ; // 修改日期:20100426 員工編號:B3774
        String  stringListPriceNo      =  "" ;
        String  stringHCom              =  "" ;
        String  stringLCom              =  "" ;
        // Start 修改日期:20100507 員工編號:B3774
        String  stringCarKind1        =  "" ;
        String  stringCarKind2        =  "" ;
        String  stringCarSize           =  "" ;
        // End 修改日期:20100507 員工編號:B3774
        String  stringTypeNo           =  "" ;
        String  stringCarPaper        =  "" ; // 修改日期:20100507 員工編號:B3774
        //String  stringPingSu            =  "" ; // 修改日期:20100426 員工編號:B3774
        String  stringListUintPrice    =  "" ;
        String  stringListPrice          =  "" ;
        String  stringHListPrice        =  "" ;
        String  stringLListPrice         =  "" ;
        String  stringFloorUnitPrice  =  "" ;
        String  stringFloorPrice        =  "" ;
        String  stringHFloorPrice      =  "" ;
        String  stringLFloorPrice      =  "" ;
        String stringSaleKind="";
        String stringTotalSquare     = ""; // 修改日期:20100426 員工編號:B3774
        String stringSaleFlag           = ""; // 修改日期:2012/06/29 員工編號:B3018
        // 房屋
        String  stringMainUse          =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuildCd           =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuildType       =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuild1              =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuild2              =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuild3              =  "" ;  // 2012-08-03 B3018 新增
        String  stringBuild4              =  "" ;  // 2012-08-03 B3018 新增
        String  stringHratio               =  "" ;  // 2012-11-05 B3018 新增
        String  stringLratio               =  "" ;// 2012-11-05 B3018 新增
        String  stringAHCom            =  "" ;// 2012-11-05 B3018 新增
        String  stringBHCom            =  "" ;// 2012-11-05 B3018 新增
        String  stringALCom             =  "" ;// 2012-11-05 B3018 新增
        String  stringBLCom              =  "" ;// 2012-11-05 B3018 新增
        String  stringIHCom                =  "" ;// 2014-02-10 B3018 新增
        String  stringILCom               =  "" ;// 2014-02-10 B3018 新增
        String  stringVisionKindL       =  "" ;
        String  stringVisionL              =  "" ;
        if("全部".equals(stringType)  ||  "房屋".equals(stringType)) {
            // 房屋價目表
            for(int  intRowNo=1  ;  intRowNo<intHouseTotalRow  ;  intRowNo++) {
                System.out.println("1-房屋--------------------------------");
                stringPosition           =  getDataFromExcel(2, intRowNo,  objectSheet2) ;    // 戶別
                stringListPriceNo      =  getDataFromExcel(3, intRowNo,  objectSheet2) ;    // 價目表 NO
                stringHCom              =  getDataFromExcel2(4, intRowNo,  objectSheet2) ;    // 公司別代碼-房    2013-11-28  修改  原引用 getDataFromExcel
                stringLCom              =  getDataFromExcel2(6, intRowNo,  objectSheet2) ;    // 公司別代碼-土    2013-11-28  修改  原引用 getDataFromExcel
                //System.out.println("1房屋---stringPosition:"+stringPosition + " stringLCom:"+stringLCom);
                stringTypeNo           =  getDataFromExcel(8, intRowNo,  objectSheet2) ;    // 產品別代碼
                stringPingSu            =  getDataFromExcel(10, intRowNo,  objectSheet2) ;  // 坪數
                stringListUintPrice    =  getDataFromExcel(11, intRowNo,  objectSheet2) ;  // 牌單
                stringListPrice          =  getDataFromExcel(12, intRowNo,  objectSheet2) ;  // 牌價
                stringHListPrice        =  getDataFromExcel(13, intRowNo,  objectSheet2) ;  // 牌價-房屋
                stringLListPrice         =  getDataFromExcel(14, intRowNo,  objectSheet2) ;  // 牌價-土地
                stringFloorUnitPrice  =  getDataFromExcel(15, intRowNo,  objectSheet2) ;  // 底單價
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet2) ;  // 底價
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet2) ;  // 底價-房屋
                stringLFloorPrice       =  getDataFromExcel(18, intRowNo,  objectSheet2) ;  // 底價-土地
                stringSaleKind           =  getDataFromExcel(19, intRowNo,  objectSheet2) ;  //租售並進
                stringSaleFlag           =  getDataFromExcel(20, intRowNo,  objectSheet2) ;  //遠壽代銷關係    2012/06/29 B3018
                stringMainUse            =  getDataFromExcel2(21, intRowNo,  objectSheet2) ;  // 主要用途          2012-08-03 B3018 新增 
                stringBuildCd             =  getDataFromExcel2(22, intRowNo,  objectSheet2) ;  // 建物型態-代碼  2012-08-03 B3018 新增 
                stringBuildType          =  getDataFromExcel2(23, intRowNo,  objectSheet2) ;  // 建物型態          2012-08-03 B3018 新增 
                stringBuild1               =  getDataFromExcel2(24, intRowNo,  objectSheet2) ;  // 房                     2012-08-03 B3018 新增 
                stringBuild2               =  getDataFromExcel2(25, intRowNo,  objectSheet2) ;  // 廳                     2012-08-03 B3018 新增 
                stringBuild3               =  getDataFromExcel2(26, intRowNo,  objectSheet2) ;  // 衛                     2012-08-03 B3018 新增 
                stringBuild4               =  getDataFromExcel2(27, intRowNo,  objectSheet2) ;  // 隔間                  2012-08-03 B3018 新增 
                stringHratio                =  getDataFromExcel2(28, intRowNo,  objectSheet2) ;  //持分比例-房屋      2012-11-05 B3018 新增 
                stringLratio                =  getDataFromExcel2(29, intRowNo,  objectSheet2) ;  //持分比例-土地      2012-11-05 B3018 新增 
                stringAHCom              =  getDataFromExcel2(30, intRowNo,  objectSheet2) ;  //委託銷售人-房屋   2012-11-05 B3018 新增 
                stringBHCom              =  getDataFromExcel2(31, intRowNo,  objectSheet2) ;  //受託銷售人-房屋   2012-11-05 B3018 新增 
                stringALCom              =  getDataFromExcel2(32, intRowNo,  objectSheet2) ;  //委託銷售人-土地   2012-11-05 B3018 新增 
                stringBLCom              =  getDataFromExcel2(33, intRowNo,  objectSheet2) ;  //受託銷售人-土地   2012-11-05 B3018 新增 
                stringIHCom               =  getDataFromExcel2(34, intRowNo,  objectSheet2) ;  //代收公司別-房屋   2014-02-10 B3018 新增 
                stringILCom               =  getDataFromExcel2(35, intRowNo,  objectSheet2) ;  //代收公司別-土地   2014-02-10 B3018 新增 
                
                //
                if("null".equals(stringMainUse))    stringMainUse    =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuildCd))     stringBuildCd      =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuildType))  stringBuildType  =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild1))        stringBuild1        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild2))        stringBuild2        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild3))        stringBuild3        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild4))        stringBuild4        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringHratio))         stringHratio        =  "" ;  // 2014-01-02 B3018 修改
                if("null".equals(stringLratio))         stringLratio        =  "" ;  // 2014-01-02 B3018 修改
                //System.out.println("1-"+intRowNo+"房屋   stringHratio("+stringHratio+")stringLratio("+stringLratio+")---------------------------------------------") ;
                if("null".equals(stringAHCom))     stringAHCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringBHCom))     stringBHCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringALCom))     stringALCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringBLCom))     stringBLCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringIHCom))      stringIHCom          =  "" ;  // 2014-02-10 B3018 新增
                if("null".equals(stringILCom))      stringILCom          =  "" ;  // 2014-02-10 B3018 新增
                //
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";
                }                               
                // 檢核
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
                  // 刪除
                  stringSql  =  getDelete(stringPosition,  "House") ;
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))  dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "House---"+stringSql) ;
                  // 新增
                  // Start 修改日期:20100426 員工編號:B3774
                  /*
                  if(retSale05M402 == null){ // 之後要拿掉
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
                  // End 修改日期:20100426 員工編號:B3774
                                     stringPingSu,             stringListUintPrice,  stringListPrice,        stringHListPrice,     stringLListPrice,
                                     // Start 修改日期:20100426 員工編號:B3774
                                     //stringFloorUnitPrice,  stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "House",stringSaleKind) ;
                                     stringFloorUnitPrice,   stringFloorPrice,      stringHFloorPrice,   stringLFloorPrice,  "House", 
                                     stringSaleKind,           stringTotalSquare,   stringVisionKindL,   stringVisionL,          stringSaleFlag,
                                     stringMainUse,            stringBuildCd,         stringBuildType,      stringBuild1,          stringBuild2,
                                      stringBuild3,                stringBuild4,            stringHratio,            stringLratio,           stringAHCom,
                                      stringBHCom,               stringALCom,         stringBLCom,          stringIHCom,          stringILCom);
                                     // End 修改日期:20100426 員工編號:B3774
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "House---"+stringSql) ;
                  intRecord++ ;
                } else {
                   System.out.println("以賣:"+stringPosition);
                }
            }
            // 房屋價目表 合計
            // 刪除
            stringSql  =  getDeleteSale05M342("House") ;
            System.out.println("2 房屋合計  SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
            for(int  intRowNo=1  ;  intRowNo<intHouseTotalRow2  ;  intRowNo++) {
                System.out.println("2房屋合計--------------------------------");
                stringPosition           =  getDataFromExcel(2, intRowNo,  objectSheet4) ;    // 戶別
                stringListPriceNo      =  getDataFromExcel(3, intRowNo,  objectSheet4) ;    // 價目表 NO
                stringHCom              =  getDataFromExcel2(4, intRowNo,  objectSheet4) ;    // 公司別代碼-房    2013-11-28  修改  原引用 getDataFromExcel
                stringLCom              =  getDataFromExcel2(6, intRowNo,  objectSheet4) ;    // 公司別代碼-土    2013-11-28  修改  原引用 getDataFromExcel
                stringTypeNo           =  getDataFromExcel(8, intRowNo,  objectSheet4) ;    // 產品別代碼
                stringPingSu            =  getDataFromExcel(10, intRowNo,  objectSheet4) ;  // 坪數
                stringListUintPrice    =  getDataFromExcel(11, intRowNo,  objectSheet4) ;  // 牌單
                stringListPrice          =  getDataFromExcel(12, intRowNo,  objectSheet4) ;  // 牌價
                stringHListPrice        =  getDataFromExcel(13, intRowNo,  objectSheet4) ;  // 牌價-房屋
                stringLListPrice         =  getDataFromExcel(14, intRowNo,  objectSheet4) ;  // 牌價-土地
                stringFloorUnitPrice  =  getDataFromExcel(15, intRowNo,  objectSheet4) ;  // 底單價
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet4) ;  // 底價
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet4) ;  // 底價-房屋
                stringLFloorPrice       =  getDataFromExcel(18, intRowNo,  objectSheet4) ;  // 底價-土地
                stringSaleKind           =  getDataFromExcel(19, intRowNo,  objectSheet4) ;  //租售並進
                stringSaleFlag           =  getDataFromExcel(20, intRowNo,  objectSheet4) ;  //遠壽代銷關係
                stringMainUse            =  getDataFromExcel2(21, intRowNo,  objectSheet4) ;  // 主要用途      
                stringBuildCd             =  getDataFromExcel2(22, intRowNo,  objectSheet4) ;  // 建物型態-代碼  
                stringBuildType          =  getDataFromExcel2(23, intRowNo,  objectSheet4) ;  // 建物型態          
                stringBuild1               =  getDataFromExcel2(24, intRowNo,  objectSheet4) ;  // 房                   
                stringBuild2               =  getDataFromExcel2(25, intRowNo,  objectSheet4) ;  // 廳                 
                stringBuild3               =  getDataFromExcel2(26, intRowNo,  objectSheet4) ;  // 衛            
                stringBuild4               =  getDataFromExcel2(27, intRowNo,  objectSheet4) ;  // 隔間                
                stringHratio                =  getDataFromExcel2(28, intRowNo,  objectSheet4) ;  //持分比例-房屋
                stringLratio                =  getDataFromExcel2(29, intRowNo,  objectSheet4) ;  //持分比例-土地 
                stringAHCom              =  getDataFromExcel2(30, intRowNo,  objectSheet4) ;  //委託銷售人-房屋
                stringBHCom              =  getDataFromExcel2(31, intRowNo,  objectSheet4) ;  //受託銷售人-房屋 
                stringALCom              =  getDataFromExcel2(32, intRowNo,  objectSheet4) ;  //委託銷售人-土地 
                stringBLCom              =  getDataFromExcel2(33, intRowNo,  objectSheet4) ;  //受託銷售人-土地
                stringIHCom               =  getDataFromExcel2(34, intRowNo,  objectSheet4) ;  //代收公司別-房屋   2014-02-10 B3018 新增 
                stringILCom               =  getDataFromExcel2(35, intRowNo,  objectSheet4) ;  //代收公司別-土地   2014-02-10 B3018 新增 
                //
                if("null".equals(stringMainUse))    stringMainUse    =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuildCd))     stringBuildCd      =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuildType))  stringBuildType  =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild1))        stringBuild1        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild2))        stringBuild2        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild3))        stringBuild3        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringBuild4))        stringBuild4        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringHratio))        stringHratio         =  "" ;  // 2014-01-02 B3018 修改
                if("null".equals(stringLratio))        stringLratio         =  "" ;  // 2014-01-02 B3018 修改
                //System.out.println("2-"+intRowNo+"房屋   stringHratio("+stringHratio+")stringLratio("+stringLratio+")---------------------------------------------NEW") ;
                if("null".equals(stringAHCom))     stringAHCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringBHCom))     stringBHCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringALCom))     stringALCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringBLCom))     stringBLCom        =  "" ;  // 2012-11-05 B3018 新增
                if("null".equals(stringIHCom))      stringIHCom          =  "" ;  // 2014-02-10 B3018 新增
                if("null".equals(stringILCom))      stringILCom          =  "" ;  // 2014-02-10 B3018 新增
                //
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";
                }                               
                // 檢核
                if("null".equals(stringPosition))    {
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    intRecord++ ;
                    break ;
                }
                // 新增
                if(retSale05M402 == null){ // 之後要拿掉
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
        //System.out.println("House-----共"  +  intRecord  +  "筆") ;
        stringMessage  =  "房屋價目表 已轉入"  +  intRecord  +  "筆" ;
        intRecord  =  0 ;
        // 車位
        String      stringCarCd              =  "" ;  // 2012-08-03 B3018 新增
        String      stringCarType           =  "" ;  // 2012-08-03 B3018 新增
        String      stringCarHratio          =  "" ;  // 2012-11-05 B3018 新增
        String      stringCarLratio          =  "" ;  // 2012-11-05 B3018 新增
        String      stringCarAHCom        =  "" ;  // 2012-11-05 B3018 新增
        String      stringCarBHCom        =  "" ;  // 2012-11-05 B3018 新增
        String      stringCarALCom        =  "" ;  // 2012-11-05 B3018 新增 
        String      stringCarBLCom        =  "" ;  // 2012-11-05 B3018 新增
        String      stringCarMainUse      =  "" ;  // 2012-12-11 B3018 新增
        if("全部".equals(stringType)  ||  "車位".equals(stringType)) {
            // 資料新增       
            for(int  intRowNo=1  ;  intRowNo<intCarTotalRow  ;  intRowNo++) {
                stringPosition           =  getDataFromExcel(0, intRowNo,  objectSheet1) ;    // 戶別
                stringListPriceNo      =  getDataFromExcel(1, intRowNo,  objectSheet1) ;    // 價目表 NO
                stringHCom              =  getDataFromExcel2(2, intRowNo,  objectSheet1) ;    // 公司別代碼-房    2013-11-28  修改  原引用 getDataFromExcel
                stringLCom              =  getDataFromExcel2(4, intRowNo,  objectSheet1) ;    // 公司別代碼-土        2013-11-28  修改  原引用 getDataFromExcel
                if (stringLCom.equals("null"))
                  stringLCom="";
                // Start 修改日期:20100507 員工編號:B3774
                stringCarKind1         =  getDataFromExcel(7, intRowNo,  objectSheet1) ;    // 性質
                stringCarKind2         =  getDataFromExcel(8, intRowNo,  objectSheet1) ;    // 類別
                stringCarSize           =  getDataFromExcel(9, intRowNo,  objectSheet1) ;    // size
                // End 修改日期:20100507 員工編號:B3774
                stringTypeNo           =  getDataFromExcel(10, intRowNo,  objectSheet1) ;    // 產品別代碼
                stringCarPaper        =  getDataFromExcel(12, intRowNo,  objectSheet1) ;    // 權狀Y/N // 修改日期:20100507 員工編號:B3774
                stringPingSu            =  "0" ;  // 坪數
                stringListUintPrice    =  "0" ;  // 牌單
                stringListPrice          =  getDataFromExcel(13, intRowNo,  objectSheet1) ;  // 牌價
                stringHListPrice        =  getDataFromExcel(14, intRowNo,  objectSheet1) ;  // 牌價-房屋
                /* stringLListPrice         =  getDataFromExcel(15, intRowNo,  objectSheet1) ;  // 牌價-土地  */               
                stringLListPrice         =  (getDataFromExcel(15, intRowNo,  objectSheet1).equals("-"))? "0":getDataFromExcel(15, intRowNo,  objectSheet1);  // 牌價-土地                 
                stringFloorUnitPrice  =  "0" ;  // 底單價
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet1) ;  // 底價
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet1) ;  // 底價-房屋
                /* stringLFloorPrice      =  getDataFromExcel(18, intRowNo,  objectSheet1) ;  // 底價-土地 */
                stringLFloorPrice      =  (getDataFromExcel(18, intRowNo,  objectSheet1).equals("-"))? "0":getDataFromExcel(18, intRowNo,  objectSheet1);   // 底價-土地
                stringSaleKind       =  getDataFromExcel(19, intRowNo,  objectSheet1) ;  //租售並進
                stringSaleFlag       =  getDataFromExcel(20, intRowNo,  objectSheet1) ;  //遠壽代銷關係  2012/06/29 B3018
                stringCarCd           =  getDataFromExcel2(21, intRowNo,  objectSheet1) ;  //車位類別-代碼  2012/08/03 B3018
                stringCarType        =  getDataFromExcel2(22, intRowNo,  objectSheet1) ;  //車位類別          2012/08/03 B3018
                stringCarArea        =  getDataFromExcel2(23, intRowNo,  objectSheet1) ;  //車位持分面積   2012/08/03 B3018
                stringCarMainUse =  getDataFromExcel2(24, intRowNo,  objectSheet1) ;  // 2012-12-11 B3018 增加 主要用途
                stringCarHratio     =  getDataFromExcel2(25, intRowNo,  objectSheet1) ;  //持分比例-房屋   2012/11/05 B3018
                stringCarLratio      =  getDataFromExcel2(26, intRowNo,  objectSheet1) ;  //持分比例-土地   2012/11/05 B3018
                stringCarAHCom   =  getDataFromExcel2(27, intRowNo,  objectSheet1) ;  //委託銷售人-房屋   2012/11/05 B3018
                stringCarBHCom   =  getDataFromExcel2(28, intRowNo,  objectSheet1) ;  //受託銷售人-房屋   2012/11/05 B3018
                stringCarALCom    =  getDataFromExcel2(29, intRowNo,  objectSheet1) ;  //委託銷售人-土地   2012/11/05 B3018
                stringCarBLCom    =  getDataFromExcel2(30, intRowNo,  objectSheet1) ;  //受託銷售人-土地   2012/11/05 B3018
                stringIHCom           =  getDataFromExcel2(31, intRowNo,  objectSheet1) ;  //代收公司別-車房   2014-02-10 B3018 新增 
                stringILCom           =  getDataFromExcel2(32, intRowNo,  objectSheet1) ;  //代收公司別-車土   2014-02-10 B3018 新增 
                System.out.println("3車位 stringIHCom("+stringIHCom+") stringILCom("+stringILCom+")--------------------------------");
                //
                if("null".equals(stringCarHratio))                        stringCarHratio     =  ""  ;  // 2014-01-02 B3018 修改
                if("null".equals(stringCarLratio))                        stringCarLratio     =  ""  ;  // 2014-01-02 B3018 修改
                if("null".equals(stringCarAHCom))                      stringCarAHCom   =  ""  ;  // 2012-11-05 B3018 新增
                if("null".equals(stringCarBHCom))                      stringCarBHCom   =  ""  ;  // 2012-11-05 B3018 新增
                if("null".equals(stringCarALCom))                      stringCarALCom    =  ""  ;  // 2012-11-05 B3018 新增
                if("null".equals(stringCarBLCom))                      stringCarBLCom    =  ""  ;  // 2012-11-05 B3018 新增
                if("null".equals(stringCarMainUse))                    stringCarMainUse  =  ""  ;  // 2012-12-11 B3018 新增
                //
                if("null".equals(stringCarCd))                             stringCarCd        =  "" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringCarType))                          stringCarType    =  "" ;  // 2012-08-03 B3018 新增
                if(exeUtil.doParseDouble(stringCarArea)<=0)    stringCarArea    =  "0" ;  // 2012-08-03 B3018 新增
                if("null".equals(stringIHCom))                             stringIHCom       =  "" ;  // 2014-02-10 B3018 新增
                if("null".equals(stringILCom))                             stringILCom       =  "" ;  // 2014-02-10 B3018 新增
                //
                //System.out.println("stringPosition :"+ stringPosition + " stringSaleKind :"+stringSaleKind+ "stringHCom"+stringHCom + "stringLCom"+stringLCom);
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";                 
                }                                               
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                // 檢核
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
                  // 刪除
                  stringSql  =  getDelete(stringPosition,  "Car") ;
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println((intRowNo+1)  +  "Car---"+stringSql) ;
                  // 新增
                  // Start 修改日期:20100507 員工編號:B3774
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
                  // End 修改日期:20100507 員工編號:B3774
                  //vectorSql.add(stringSql) ;
                  System.out.println(intRowNo+"SQL------------------------\n"+stringSql+"\n---------------------------") ;
                  if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
                  //if(intRowNo==1)  System.out.println("Car---"+stringSql) ;
                  intRecord++ ;
                } else{
                  System.out.println("以賣--"+stringPosition);
                }
            }
            // 2012-11-05 B3018 S
            // 刪除
            stringSql  =  getDeleteSale05M342("Car") ;
            System.out.println("4 實位合計 SQL------------------------\n"+stringSql+"\n---------------------------") ;
            if(!"B3018".equals(getUser()))dbSale.execFromPool(stringSql) ;
            for(int  intRowNo=1  ;  intRowNo<intCarTotalRow2  ;  intRowNo++) {
                System.out.println("4車位合計--------------------------------");
                stringPosition           =  getDataFromExcel(0, intRowNo,  objectSheet3) ;    // 戶別
                stringListPriceNo      =  getDataFromExcel(1, intRowNo,  objectSheet3) ;    // 價目表 NO
                stringHCom              =  getDataFromExcel2(2, intRowNo,  objectSheet3) ;    // 公司別代碼-房    2013-11-28  修改  原引用 getDataFromExcel
                stringLCom              =  getDataFromExcel2(4, intRowNo,  objectSheet3) ;    // 公司別代碼-土    2013-11-28  修改  原引用 getDataFromExcel
                if (stringLCom.equals("null"))
                  stringLCom="";
                // Start 修改日期:20100507 員工編號:B3774
                stringCarKind1         =  getDataFromExcel(7, intRowNo,  objectSheet3) ;    // 性質
                stringCarKind2         =  getDataFromExcel(8, intRowNo,  objectSheet3) ;    // 類別
                stringCarSize           =  getDataFromExcel(9, intRowNo,  objectSheet3) ;    // size
                // End 修改日期:20100507 員工編號:B3774
                stringTypeNo           =  getDataFromExcel(10, intRowNo,  objectSheet3) ;    // 產品別代碼
                stringCarPaper        =  getDataFromExcel(12, intRowNo,  objectSheet3) ;    // 權狀Y/N // 修改日期:20100507 員工編號:B3774
                stringPingSu            =  "0" ;  // 坪數
                stringListUintPrice    =  "0" ;  // 牌單
                stringListPrice          =  getDataFromExcel(13, intRowNo,  objectSheet3) ;  // 牌價
                stringHListPrice        =  getDataFromExcel(14, intRowNo,  objectSheet3) ;  // 牌價-房屋
                stringLListPrice         =  (getDataFromExcel(15, intRowNo,  objectSheet3).equals("-"))? "0":getDataFromExcel(15, intRowNo,  objectSheet3);  // 牌價-土地                 
                stringFloorUnitPrice  =  "0" ;  // 底單價
                stringFloorPrice        =  getDataFromExcel(16, intRowNo,  objectSheet3) ;  // 底價
                stringHFloorPrice      =  getDataFromExcel(17, intRowNo,  objectSheet3) ;  // 底價-房屋
                stringLFloorPrice      =  (getDataFromExcel(18, intRowNo,  objectSheet3).equals("-"))? "0":getDataFromExcel(18, intRowNo,  objectSheet3);   // 底價-土地
                stringSaleKind          =  getDataFromExcel(19, intRowNo,  objectSheet3) ;  //租售並進
                stringSaleFlag          =  getDataFromExcel(20, intRowNo,  objectSheet3) ;  //遠壽代銷關係  
                stringCarCd              =  getDataFromExcel2(21, intRowNo,  objectSheet3) ;  //車位類別-代碼 
                stringCarType           =  getDataFromExcel2(22, intRowNo,  objectSheet3) ;  //車位類別          
                stringCarArea            =  getDataFromExcel2(23, intRowNo,  objectSheet3) ;  //車位持分面積  
                stringCarMainUse     =  getDataFromExcel2(24, intRowNo,  objectSheet3) ;  //主要用途 2012/12/11 B3018  修改
                stringCarHratio          =  getDataFromExcel2(25, intRowNo,  objectSheet3) ;  //持分比例-房屋 
                stringCarLratio          =  getDataFromExcel2(26, intRowNo,  objectSheet3) ;  //持分比例-土地 
                stringCarAHCom        =  getDataFromExcel2(27, intRowNo,  objectSheet3) ;  //委託銷售人-房屋  
                stringCarBHCom       =  getDataFromExcel2(28, intRowNo,  objectSheet3) ;  //受託銷售人-房屋  
                stringCarALCom        =  getDataFromExcel2(29, intRowNo,  objectSheet3) ;  //委託銷售人-土地 
                stringCarBLCom        =  getDataFromExcel2(30, intRowNo,  objectSheet3) ;  //受託銷售人-土地 
                stringIHCom               =  getDataFromExcel2(31, intRowNo,  objectSheet3) ;  //代收公司別-車房   2014-02-10 B3018 新增 
                stringILCom                =  getDataFromExcel2(32, intRowNo,  objectSheet3) ;  //代收公司別-車土   2014-02-10 B3018 新增 
                //
                if("null".equals(stringCarHratio))                        stringCarHratio     =  "" ;  // 2014-01-02 B3018 修改
                if("null".equals(stringCarLratio))                        stringCarLratio     =  "" ;  // 2014-01-02 B3018 修改
                if("null".equals(stringCarAHCom))                      stringCarAHCom  =  ""  ; 
                if("null".equals(stringCarBHCom))                      stringCarBHCom  =  ""  ;  
                if("null".equals(stringCarALCom))                      stringCarALCom   =  ""  ; 
                if("null".equals(stringCarBLCom))                      stringCarBLCom   =  ""  ;
                if("null".equals(stringCarCd))                             stringCarCd           =  "" ; 
                if("null".equals(stringCarType))                          stringCarType       =  "" ;  
                if("null".equals(stringCarMainUse))                    stringCarMainUse  =  "" ;  //2012-12-11 B3018 新增
                if("null".equals(stringIHCom))                             stringIHCom       =  "" ;  // 2014-02-10 B3018 新增
                if("null".equals(stringILCom))                             stringILCom       =  "" ;  // 2014-02-10 B3018 新增
                if(exeUtil.doParseDouble(stringCarArea)<=0)    stringCarArea         =  "0" ; 
                //
                if("null".equals(stringSaleKind) || "".equals(stringSaleKind))    {
                  stringSaleKind="N";                 
                }                                               
                if("null".equals(stringSaleFlag))    {
                    stringSaleFlag  =  "" ;
                }
                // 檢核
                if("null".equals(stringPosition))    {
                    intRecord++ ;
                    break ;
                }
                if("null".equals(stringListPriceNo))    {
                    intRecord++ ;
                    break ;
                }
                // 新增
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
        System.out.println("Car-----共"  +  intRecord  +  "筆") ;
        stringMessage  +=  " 　　　房屋價目表 已轉入"  +  intRecord  +  "筆" ;
        // 實際寫作資料庫中
        //if(vectorSql.size( )  >  0)  dbSale.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
          // 釋放 Excel 物件
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
                                                               " AND  (Sale05M090.FlowStatus <> '業管-作廢'  OR  Sale05M090.FlowStatus IS NULL) "  +
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
                                          " '"  +  stringHouseCar       +  "', "  +  // 例外，14
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
    // Start 修改日期:20100507 員工編號:B3774
    public String getInsertC(String stringPosition,           String stringListPriceNo,       String stringHCom,          String stringLCom,          String stringTypeNo,
                          String stringPingSu,            String stringListUintPrice,      String stringListPrice,      String stringHListPrice,   String stringLListPrice,
                          String stringFloorUnitPrice, String stringFloorPrice,         String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar,
                        String stringSaleKind,         String stringCarKind1,           String stringCarKind2,     String stringCarSize,       String stringCarPaper,
                          String  stringSaleFlag,         String stringCarCd,               String  stringCarType,     String  stringCarArea,       String  stringCarHratio,
                          String  stringCarLratio,        String  stringCarAHCom,       String  stringCarBHCom,  String  stringCarALCom,  String  stringCarBLCom,
                        String  stringCarMainUse,    String  stringCarVisionKind,  String  stringCarVision,    String  stringIHCom,         String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 增加
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
                                        " '"  +  stringHouseCar          +  "', "  +  // 例外，14
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
                                         " '"  +  stringSaleFlag          +  "',  "  +  // 2012/06/29 B3018 修改    
                                         " '"  +  stringCarCd              +  "',  "  +   // 2012-08-03 B3018 修改    24
                                         " '"  +  stringCarType          +  "',  "  +   // 2012-08-03 B3018 修改
                                         "  "  +  stringCarArea          +  ",  "  +  // 2012-08-03 B3018 修改
                                         " '"  +  stringCarHratio         +  "',  "  +    // 2014-01-02 B3018 修改
                                         " '"  +  stringCarLratio        +  "',  "  +    // 2014-01-02 B3018 修改
                                         " '"  +  stringCarAHCom      +  "',  "  +  // 2012-11-05 B3018 修改    29
                                         " '"  +  stringCarBHCom      +  "',  "  +  // 2012-11-05 B3018 修改
                                         " '"  +  stringCarALCom       +  "',  "  +   // 2012-11-05 B3018 修改
                                         " '"  +  stringCarBLCom      +  "',  "  +      // 2012-08-03 B3018 修改
                                         "  "  +  "0"                            +  " ,  "  +   // 2012-12-04 B3018 修改      
                                         "  "  +  "0"                            +  ",  "  +  // 2012-08-03 B3018 修改      34
                                         " '"  +  stringCarMainUse     +  "',  "  +   // 2012-12-11 B3018 修改
                                         " '"  +  stringCarVisionKind  +  "',  "  +   // 2012-12-17 B3018 修改
                                         " '"  +  stringCarVision          +  "',  "  +   // 2012-12-17 B3018 修改
                                         " '"  +  stringIHCom               +  "',  "  +  // 2014-02-10 B3018 修改
                                         " '"  +  stringILCom                +  "',  "  +   // 2017-06-02 B3018 修改      39
                                         " '"  +  stringStartUseDate    +  "',  "  +  // 2017-06-02 B3018 修改
                                         " '"  +  ""                    +  "',  "  +  // 2017-06-02 B3018 修改
                                         " '"  +  ""                    +  "')  "  ;  // 2017-06-02 B3018 修改
        return stringSql;
    }
    // End 修改日期:20100507 員工編號:B3774
    public String getInsertC_Sale05M342(String stringPosition,           String stringListPriceNo,      String stringHCom,          String stringLCom,          String stringTypeNo,
                                               String stringPingSu,            String stringListUintPrice,      String stringListPrice,      String stringHListPrice,   String stringLListPrice,
                                               String stringFloorUnitPrice, String stringFloorPrice,         String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar,
                                             String stringSaleKind,         String stringCarKind1,           String stringCarKind2,     String stringCarSize,       String stringCarPaper,
                                               String  stringSaleFlag,         String stringCarCd,               String  stringCarType,     String  stringCarArea,       String  stringCarHratio,
                                               String  stringCarLratio,        String  stringCarAHCom,       String  stringCarBHCom,  String  stringCarALCom,  String  stringCarBLCom,
                                             String  stringCarMainUse,    String  stringCarVisionKind,  String  stringCarVision,    String  stringIHCom,         String  stringILCom) throws Throwable{
        String stringSql                    = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 增加
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
                                        " '"  +  stringHouseCar          +  "', "  +  // 例外，14
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
                                         " '"  +  stringSaleFlag          +  "',  "  +  // 2012/06/29 B3018 修改
                                         " '"  +  stringCarCd              +  "',  "  +   // 2012-08-03 B3018 修改
                                         " '"  +  stringCarType          +  "',  "  +   // 2012-08-03 B3018 修改
                                         "  "  +  stringCarArea          +  ",  "  +  // 2012-08-03 B3018 修改
                                         " '"  +  stringCarHratio        +  "',  "  +    // 2014-01-02 B3018 修改
                                         " '"  +  stringCarLratio        +  "',  "  +     // 2014-01-02 B3018 修改
                                         " '"  +  stringCarAHCom      +  "',  "  +  // 2012-11-05 B3018 修改
                                         " '"  +  stringCarBHCom      +  "',  "  +  // 2012-11-05 B3018 修改
                                         " '"  +  stringCarALCom       +  "',  "  +   // 2012-11-05 B3018 修改
                                         " '"  +  stringCarBLCom      +  "',  "  +  // 2012-08-03 B3018 修改
                                         "  "  +  "0"                            +  " ,  "   +  // 2012-12-04 B3018 修改
                                         "  "  +  "0"                            +  ", "    +   // 2012-12-04 B3018 修改
                                         "  '"  +  stringCarMainUse    +  "', "   +   // 2012-12-11 B3018 修改
                                         "  '"  +  stringCarVisionKind +  "',  "  +   // 2012-12-11 B3018 修改
                                         "  '"  +  stringCarVision        +  "' ,  "  +   // 2012-12-11 B3018 修改
                                         "  '"  +  stringIHCom             +  "' ,  "  +  // 2014-02-10 B3018 修改
                                         "  '"  +  stringILCom             +  "' ,  "  +  // 2014-02-10 B3018 修改
                                         "  '"  +  "Y"                           +  "' ,  "  +  // 2017-06-02 B3018 修改
                                         "  '"  +  stringStartUseDate  +  "' ,  "  +  // 2017-06-02 B3018 修改
                                         "  '"  +  ""                   +  "' ,  "  +   // 2017-06-02 B3018 修改
                                         "  '"  +  ""                   +  "' )  "  ;   // 2017-06-02 B3018 修改
        return stringSql;
    }
    // Start 修改日期:20100426 員工編號:B3774
    // 修改日期:2012-08-03 員工編號:B3018
    public String getInsertH(String stringPosition,           String stringListPriceNo,   String stringHCom,          String  stringLCom,          String stringTypeNo, 
                        String stringPingSu,            String stringListUintPrice, String stringListPrice,      String  stringHListPrice,    String stringLListPrice, 
                        String stringFloorUnitPrice, String stringFloorPrice,     String stringHFloorPrice, String  stringLFloorPrice, String stringHouseCar, 
                        String stringSaleKind,         String stringTotalSquare,  String stringVisionKind,   String  stringVision,          String  stringSaleFlag,
                        String  stringMainUse,        String  stringBuildCd,         String  stringBuildType,   String  stringBuild1,         String  stringBuild2,
                          String  stringBuild3,            String  stringBuild4,             String  stringHratio,        String  stringLratio,         String  stringAHCom,
                          String  stringBHCom,          String  stringALCom,           String  stringBLCom,      String  stringIHCom,        String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 增加
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
                                         " '" + stringSaleFlag                                         + "', " +  // 2012/06/29 B3018 修改
                                         " '" + stringMainUse                                         + "',  " + // 2012/08/03 B3018 修改
                                         " '" + stringBuildCd                                          + "',  " + // 2012/08/03 B3018 修改      24
                                         " '" + stringBuildType                                      + "',  " + // 2012/08/03 B3018 修改
                                         " '" + stringBuild1                                            + "',  " + // 2012/08/03 B3018 修改
                                         " '" + stringBuild2                                             + "',  " + // 2012/08/03 B3018 修改
                                         " '" + stringBuild3                                             + "',  " + // 2012/08/03 B3018 修改
                                         " '" + stringBuild4                                              + "', " +  // 2012/08/03 B3018 修改     29
                                         " '" + stringHratio                                              + "', " + // 2014/01/02 B3018 修改
                                         " '" + stringLratio                                              + "', " + // 2014/01/02 B3018 修改
                                         " '" + stringAHCom                                           + "' , " + // 2012/11/05 B3018 修改
                                         " '" + stringBHCom                                           + "' , " + // 2012/11/05 B3018 修改
                                         " '" + stringALCom                                           + "' , " + // 2012/11/05 B3018 修改       34
                                         " '" + stringBLCom                                           + "' , " +  // 2012/11/05 B3018 修改
                                         "  " + "0"                                                          + ",  "  +  // 2012/12/04 B3018 修改
                                         "  " + "0"                                                          + ", "   +  // 2012/12/04 B3018 修改
                                         "  '" + ""                                                            + "' , " +  // 2012/12/11 B3018 修改
                                         "  '" + ""                                                            + "' , " +  // 2012/12/17 B3018 修改     39
                                         "   " + "0"                                                          + " , "  + // 2012/12/17 B3018 修改
                                         "  '" + stringIHCom                                            + "',  " + // 2014/02/10 B3018 修改
                                         "  '" + stringILCom                                            + "',  "  +  // 2017/06/02 B3018 修改
                                         "  '" + stringStartUseDate                                  + "' , " + // 2017/06/02 B3018 修改
                                         "  '" + ""                                                 + "',  " +  // 2017/06/02 B3018 修改    44
                                         "  '" + ""                                                 + "' ) "; // 2017/06/02 B3018 修改
        return stringSql;
    }
    // End 修改日期:20100426 員工編號:B3774
    public String getInsertH_Sale05M342(String stringPosition,           String stringListPriceNo,   String stringHCom,          String stringLCom,          String stringTypeNo, 
                                              String stringPingSu,            String stringListUintPrice, String stringListPrice,      String stringHListPrice,    String stringLListPrice, 
                                              String stringFloorUnitPrice, String stringFloorPrice,     String stringHFloorPrice, String stringLFloorPrice, String stringHouseCar, 
                                             String stringSaleKind,         String stringTotalSquare,  String stringVisionKind,   String stringVision,          String  stringSaleFlag,
                                             String  stringMainUse,        String  stringBuildCd,         String  stringBuildType,   String  stringBuild1,         String  stringBuild2,
                                               String  stringBuild3,            String  stringBuild4,             String  stringHratio,         String  stringLratio,         String  stringAHCom,
                                               String  stringBHCom,          String  stringALCom,           String  stringBLCom,       String  stringIHCom,        String  stringILCom) throws Throwable{
        String stringSql                   = "";
        String stringProjectId          = getValue("ProjectId").trim();
        String stringStartUseDate  =  getValue("StartUseDate").trim( ) ;      // 2017-06-02 B3018 增加
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
                                         " '" + stringHratio                                              + "', " +   // 2014-01-02 B3018 修改
                                         " '" + stringLratio                                              + "', " +   // 2014-01-02 B3018 修改
                                         " '" + stringAHCom                                           + "' , " + 
                                         " '" + stringBHCom                                           + "' , " + 
                                         " '" + stringALCom                                           + "' , " +
                                         " '" + stringBLCom                                           + "' , "+
                                         "  " + "0"                                                          + ",  "+
                                         "  " + "0"                                                          + ", " +
                                         "  '" + ""                                                            + "', " + 
                                         "  '" + ""                                                            + "', " +
                                         "   " + "0"                                                         + " , " +
                                         "  '" + stringIHCom                                           + "',  "  +   // 2014-02-10 B3018 修改
                                         "  '" + stringILCom                                           + "' , " +   // 2014-02-10 B3018 修改
                                         "   '" + "Y"                                                        + "' , " + // 2017-06-02 B3018 修改
                                         "   '" + stringStartUseDate                               + "' , " + // 2017-06-02 B3018 修改
                                         "   '" + ""                                                          + "' , " + // 2017-06-02 B3018 修改
                                         "   '" + ""                                                           + "' ) " ; // 2017-06-02 B3018 修改
        return stringSql;
    }
    public  boolean  isBatchCheckOK(FargloryUtil  exeUtil) throws  Throwable {
        // Excel 檔案不可空白
        String  stringExcelFile  =  getValue("ExcelFile").trim( ) ;
        if("".equals(stringExcelFile)) {
            message("請選擇檔案。") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;
        }
        // 格式判斷
        String[]  arrayTemp  =  convert.StringToken(stringExcelFile,  ".") ;        
        // Start 修改日期:20100805 員工編號:B3774
        //if(!"XLS".equals(arrayTemp[1].trim( ).toUpperCase( ))) {
        if(!"XLS".equals(arrayTemp[arrayTemp.length-1].trim( ).toUpperCase( ))) {
        // End 修改日期:20100805 員工編號:B3774
            message("請選擇 Excel 格式的檔案。") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;       
        }
        // Excel 檔案存在判斷
        File  excelFile  =  new  File(stringExcelFile) ;
        if(!excelFile.exists()) {
            message("報表不存在！") ;
            getcLabel("ExcelFile").requestFocus( ) ;
            return  false ;
        }
        // 案別
        String      stringProjectId  =  getValue("ProjectId").trim( ) ;
        if("".equals(stringProjectId)) {
            message("[案別] 不可空白。") ;
            getcLabel("ProjectId").requestFocus( ) ;
            return  false ;
        }
        if(!isProjectIdExistOK(stringProjectId)) {
            message("[案別] 不存在於資料庫中。") ;
            getcLabel("ProjectId").requestFocus( ) ;
            return  false ;
        }
        // 啟用日期
        String      stringStartUseDate  =  getValue("StartUseDate").trim( ) ;
        if("".equals(stringStartUseDate)) {
            message("[啟用日期] 不可空白。") ;
            getcLabel("StartUseDate").requestFocus( ) ;
            return  false ;
        }
        // 2017-06-02 B3018 增加 Start
        String                stringDateRoc  =  exeUtil.getDateAC (stringStartUseDate,  "需用日期") ;
        if(stringDateRoc.length()  !=  10) {
            message(stringStartUseDate) ;
            getcLabel("StartUseDate").requestFocus( ) ;
            return  false ;
        } 
        setValue("StartUseDate",  stringDateRoc) ;
        // 2017-06-02 B3018 增加 END
        message("") ;
        return  true ;
    }
    // 案別
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
        " AND (Sale05M090.FlowStatus <> '業管-作廢' OR Sale05M090.FlowStatus IS NULL)  AND ISNULL(StatusCd,'')<>'D' " ;
        String retSale05M092[][] = dbSale.queryFromPool(stringSql);
        return  retSale05M092.length ;
    }             
    // Excel
    // 建立 Excel 元件
    public Vector getExcelObject(String stringExcelName){
        Vector  retVector  =  new  Vector( ) ;
        // 建立com元件
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
        Object    objectSheet1  =  Dispatch.call(objectSheets,  "Item",  "車位價目表").toDispatch() ;
        //Object    objectSheet1  =  Dispatch.call(objectSheets,  "Item",  new Variant(3)).toDispatch() ;
        Object    objectSheet2  =  Dispatch.call(objectSheets,  "Item",  "房屋價目表").toDispatch() ;
        //Object    objectSheet2  =  Dispatch.call(objectSheets,  "Item",  new Variant(4)).toDispatch() ;
        Object    objectSheet3  =  Dispatch.call(objectSheets,  "Item",  "車位價目表(共有)").toDispatch() ;
        Object    objectSheet4  =  Dispatch.call(objectSheets,  "Item",  "房屋價目表(共有)").toDispatch() ;
        //A1 for Copy &Paste
        //Dispatch.call(objectSheet1,  "Activate");
        Object    object1A1  =  null ;
        object1A1    =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        //Dispatch.call(objectSheet2,  "Activate");
        Object    object2A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet2,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        // 2012-11-05 B3018 修改 S
        Object    object3A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet3,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        Object    object4A1  =  null ;
        object2A1    =  Dispatch.invoke(objectSheet4,  "Range",  Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch(); 
        // 2012-11-05 B3018 修改 E

        // 回傳資料
        retVector.add(Excel) ;
        retVector.add(objectSheet1) ;
        retVector.add(objectSheet2) ;
        retVector.add(object1A1) ;
        retVector.add(object2A1) ;
        // 2012-11-05 B3018 修改 S
        retVector.add(objectSheet3) ;
        retVector.add(objectSheet4) ;
        retVector.add(object3A1) ;
        retVector.add(object4A1) ;
        // 2012-11-05 B3018 修改 E
        return  retVector ;
    }
    // 顯示並釋放 Excel 物件
    public void getReleaseExcelObject(Vector  vectorExcelObject){
        // 資料處理
        ActiveXComponent  Excel  =  (ActiveXComponent)  vectorExcelObject.get(0) ;
        Object    objectSheet1      =  vectorExcelObject.get(1) ;
        Object    objectSheet2      =  vectorExcelObject.get(2) ;
        //顯示 Excel
        Excel.setProperty("DisplayAlerts", new Variant(true));        
        Dispatch.call(Excel, "Quit");
        // 釋放com元件
        ComThread.Release() ;
    }
    //依Client Excel Version 開啟
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
                  System.out.println("請使用 Excel 97 以上版本!");                             
                  message("請使用 Excel 97 以上版本!");  
              }
            }
          }
        }
        Excel = new ActiveXComponent("Excel.Application");
        System.out.println("All is OK!");                  
        return Excel;       
    }
      //Excel 欄位處理
        public int getExcelColumnNo(String stringColumn){
                    int  intReturn  =  0 ;
            for(int  intIndex=0  ;  intIndex<stringColumn.length()  ;  intIndex++) {
                intReturn  +=  (stringColumn.charAt(intIndex)  -  'A'  +  1)  *  Math.pow(26,  stringColumn.length()-1-intIndex);
            }
            return intReturn;          
        }
      //Excel 欄位處理
    public String getExcelColumnName(String stringColumn,int intCalculate){
                    //char charColumn = stringColumn.charAt(0);
                    int intColumn = getExcelColumnNo(stringColumn) + 'A'  -1;
                    String stringReturn = ""; 
                    // < A
                    if ((intColumn + intCalculate) < 65 ) {
                stringReturn = "0";
                message("[欄位小於A] 錯誤!") ;
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
      // intPosColumn       Excel 的位置(行)
      // intPosRow            Excel 的位置(列)
      //  objectSheet2        Excel 的工作表
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
      return "---------------button1(轉檔).defaultValue()----------------";
    }
}
