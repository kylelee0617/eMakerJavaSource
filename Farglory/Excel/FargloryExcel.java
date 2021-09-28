package Farglory.Excel ;
import  java.util.*;
import  com.jacob.activeX.*;
import  com.jacob.com.*;
import  jcx.util.*;
import  jcx.jform.bproc;
public  class  FargloryExcel {
	// �����]�w
	int          intStartDataRowGlobal       =  6 ;                // �}�lExcel �C��(Sheet ��ܡA�D��0 �}�l)
	int          intPageDataRowGlobal        =  20 ;               // ����Excel �C��(��ک�Ȧ��)
	int          intPageAllRowGlobal         =  28 ;               // �@�����C��
	int          intPageNoGlobal             =  1 ;                // ��l����
	int          intStartClearColG           =  0 ;                // �}�l�M�����(�w�] 'A')
	int          intEndClearColG             =  17 ;               // �����M�����(�w�] 'R')
	boolean      booleanFlag                 =  true ;             // �w���Gtrue ��ܭn�w���C
	boolean      booleanType                 =  true ;             // 
	boolean      booleanFirstSheetFlagG      =  true ;             // �]�w�� Sheet ��Action�A�w�]�ĤG��
	boolean      booleanVisibleG             =  true ;             // �]�w�n���n��� Excel ���ϥΪ̬�
	boolean      booleanSheetEndViewG        =  true ;             // �w�] true �̫���ܲĤ@�� Sheet
	boolean      booleanStopG                =  false ;            // �]�w true ��ܰ���̫�@�Ӱʧ@(Quit �� View)
	String       stringFilePath              =  "" ;               // ���w���ɡA�D�Ŧr��ɡA�t�s�s��
	String       stringExcelFileNameGlobal   =  "" ;
	
	// �غc�l
	public FargloryExcel(){
		booleanType  =  true ;    
	}
	
	public FargloryExcel(String  stringExcelFileName){
		stringExcelFileNameGlobal  =  stringExcelFileName ;
		booleanType  =  true ;
	}
	
	public FargloryExcel(int  intStartDataRow,  int  intPageDataRow,  int  intPageAllRow,  int  intPageNo){
		intStartDataRowGlobal  =  intStartDataRow ;
		intPageDataRowGlobal   =  intPageDataRow ;
		intPageAllRowGlobal    =  intPageAllRow ;
		intPageNoGlobal        =  intPageNo ;
		booleanType            =  false ;
	}
	
	// �����㤣��� Excel
	public void setVisibleProperty(boolean  booleanVisible) throws Exception {
		booleanVisibleG  =  booleanVisible ;
	}
	
	public void setSheetEndView(boolean  booleanSheetEndView) throws Exception {
		booleanSheetEndViewG  =  booleanSheetEndView ;
	}
	
	//
	public void setClearCol(int  intStartClearCol,  int  intEndClearCol) throws Exception {
		intStartClearColG  =  intStartClearCol ;
		intEndClearColG    =  intEndClearCol ;
	}
	
	// ���w���ɡA�B���ǤJ���|�ɡA�t�s�s�ɡC
	public void setPreView(boolean  booleanFlagValue,  String  stringPathValue) throws Exception {
		booleanFlag     =  booleanFlagValue ;
		stringFilePath  =  stringPathValue ;
	}
	
	// 
	public void doStopAction(boolean  booleanFlagValue) throws Exception {
		booleanStopG  =  booleanFlagValue ;
	}
	
	// �ȹ��� Sheet ����
	public void setFirstSheet(boolean  booleanFirstSheetFlag) throws Exception {
		booleanFirstSheetFlagG  =  booleanFirstSheetFlag ;
	}
	
	public int getStartDataRow() throws Exception {
		return  (intStartDataRowGlobal-1) ;
	}
	
	public int getPageAllRow() throws Exception {
		return  intPageAllRowGlobal ;
	}
	
	public int getPageNo() throws Exception {
		return  intPageNoGlobal ;
	}
	
	public int getPageDataRow() throws Exception {    
		return  intPageDataRowGlobal ;
	}
	
	public int doAdd1PageNo() throws Exception {     
		intPageNoGlobal  =  intPageNoGlobal  +  1 ;
		return  intPageNoGlobal ;
	}
	
	public void setVisiblePropertyOnFlow(boolean  booleanVisible,  Vector  retVector) throws Exception {
		booleanVisibleG         =  booleanVisible ;
		ActiveXComponent Excel  =  (ActiveXComponent)retVector.get(0);
		Excel.setProperty("Visible",  new Variant(booleanVisible));
	}
	
	// �]�w���� 970131 B3774
	public void setPageNo(int intPageNo) throws Exception {
		intPageNoGlobal  =  intPageNo ;
	}
	
	// �إ� Excel ����
	public Vector getExcelObject(String stringExcelName){
		Vector  retVector  =  new  Vector( ) ;
		// �إ�com����
		ActiveXComponent Excel;
		ComThread.InitSTA();
		Excel  =  ExcelVerson();
		Excel.setProperty("Visible",  new Variant(booleanVisibleG));
//		Excel.setProperty("Visible",  new Variant(false));
		Excel.setProperty("DisplayAlerts", new Variant(false));	
		Object    objectExcel        =  Excel.getObject();
		Dispatch    objectWorkbooks  =  Excel.getProperty("Workbooks").toDispatch();
		Dispatch    objectWorkbook   = null ;
		objectWorkbook  =  Dispatch.call(objectWorkbooks, "Open", stringExcelName,  new  Variant(false)).toDispatch();
		Dispatch    objectSheets  =  Dispatch.get(objectWorkbook,  "Sheets").toDispatch();
		Dispatch    objectSheet1  =  null ;
		Dispatch    objectSheet2  =  null ;
		if("".equals(stringExcelFileNameGlobal)) {
			if(!booleanType){
    				objectSheet1  =  Dispatch.call(objectSheets,  "Item",  new  Variant(1)).toDispatch() ;
    				objectSheet2  =  Dispatch.call(objectSheets,  "Item",  new  Variant(2)).toDispatch() ;  
    				if(booleanFirstSheetFlagG){
    					Dispatch.call(objectSheet2,  "Activate");
    				} else {
    					Dispatch.call(objectSheet1,  "Activate");
    				} 
			}else {
				objectSheet1  =  Dispatch.call(objectSheets,  "Item",  new  Variant(1)).toDispatch() ;  
				Dispatch.call(objectSheet1,  "Activate");
			}
		} else {
			objectSheet1  =  Dispatch.call(objectSheets,  "Item",  stringExcelFileNameGlobal).toDispatch() ;
			Dispatch.call(objectSheet1,  "Activate");
		}
		// �^�Ǹ��
		retVector.add(Excel) ;
		retVector.add(objectSheet1) ;
		retVector.add(objectSheet2) ;
		retVector.add(objectSheets) ;
		retVector.add(objectWorkbook) ;
		Excel.setProperty("Visible",  new Variant(booleanVisibleG));		
		Excel.setProperty("DisplayAlerts", new Variant(false));			
		return  retVector ;
	}
	// ��ܨ����� Excel ����
	public void getReleaseExcelObject(Vector  vectorExcelObject){
		// ��ƳB�z
		ActiveXComponent  Excel          =  (ActiveXComponent)  vectorExcelObject.get(0) ;
		Dispatch          objectSheet1   =  (Dispatch)vectorExcelObject.get(1) ;//
		Dispatch          objectSheet2   =  (Dispatch)vectorExcelObject.get(2) ;//
		Dispatch          objectSheets   =  (Dispatch)vectorExcelObject.get(4) ;//
		Dispatch          objectA1       =   null ;//
		//
		if(!"".equals(stringFilePath)) {
			Dispatch.call(objectSheets,  "SaveAs",  stringFilePath);//
		}
		// �w��             true booleanFlag
		// ���             true booleanVisibleG
		// ����̫�@�Ӱʧ@ true booleanStopG
		if(!booleanType){
			//��� Excel
			Excel.setProperty("DisplayAlerts", new Variant(true));
			if(!booleanFlag  &&  !booleanVisibleG){
				Dispatch.call(Excel, "Quit");
			} else {
				if(booleanSheetEndViewG) {
					// �̫���ܲĤ@�� Sheet
					Dispatch.call(objectSheet1,  "Activate");
					objectA1  =  Dispatch.invoke(objectSheet1,  "Range",   Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch();
					Dispatch.call(objectA1,  "Select");
					//
					if(booleanVisibleG) {
					    // ���
    					if(booleanFlag) {
    					    if(!booleanStopG) Dispatch.call(objectSheet1,  "PrintPreview");
    					} 
				  } else {
				      // �����
				      Dispatch.call(Excel, "Quit");
				  }
				} else {
					Dispatch.call(objectSheet2,  "Activate");
					objectA1  =  Dispatch.invoke(objectSheet2,  "Range",   Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch();
					Dispatch.call(objectA1,  "Select");
					//
					if(booleanVisibleG) {
					    // ���
    					if(booleanFlag) {
    					    if(!booleanStopG) Dispatch.call(objectSheet2,  "PrintPreview");
    					} 
				  } else {
				      // �����
				      Dispatch.call(Excel, "Quit");
				  }
				}
			}
		} else {
			if(!booleanFlag  &&  !booleanVisibleG){
				  Dispatch.call(Excel, "Quit");
			} else {
				  Dispatch.call(objectSheet1,  "Activate");
				  //
					if(booleanVisibleG) {
					    // ���
    					if(booleanFlag) {
    					    if(!booleanStopG) {
    					        System.out.println("1111------------------------") ;
    					        Dispatch.call(objectSheet1,  "PrintPreview");
    					    }
    					} 
				  } else {
				      // �����
				      Dispatch.call(Excel, "Quit");
				  }
			}
		}
		// ����com����
		ComThread.Release() ;
	}
	
	//��Client Excel Version �}��
   public String getExcelVerson(){
          Vector  vectorVersion  =  new  Vector() ;
          //
          ExcelVerson(vectorVersion) ;
          //
          if(vectorVersion.size()  ==  0)  return  "" ;
          //
          return ""+vectorVersion.get(0) ;
          
   }
   public ActiveXComponent ExcelVerson(){
          Vector  vectorVersion  =  new  Vector() ;
          return  ExcelVerson(vectorVersion) ;
   }
	public ActiveXComponent ExcelVerson(Vector  vectorVersion){
		ActiveXComponent Excel;
		ComThread.InitSTA();
		int intExcelVerson = 0;
		try{
			Excel = new ActiveXComponent("Excel.Application.8");//Excel 97
			System.out.println("Excel 97 is OK!");
         vectorVersion.add("97") ;
			return Excel;			 
		}catch(Exception Excel97){
			try{
				Excel = new ActiveXComponent("Excel.Application.9");//Excel 2000
				System.out.println("Excel 2000 is OK!");					 
            vectorVersion.add("2000") ;
				return Excel;
			}catch(Exception Excel2000){
				try{
					Excel = new ActiveXComponent("Excel.Application.10");//Excel 2002
					System.out.println("Excel 2002 is OK!");							 
               vectorVersion.add("2002") ;
					return Excel;
				}catch(Exception Excel2002){
					try{
						Excel = new ActiveXComponent("Excel.Application.11");//Excel 2003
						System.out.println("Excel 2003 is OK!");
                  vectorVersion.add("2003") ;							 
						return Excel;
					}catch(Exception Excel2003){
							try{
									Excel = new ActiveXComponent("Excel.Application.12");//Excel 2003
									System.out.println("Excel 2007 is OK!");	
                           vectorVersion.add("2007") ;						 
									return Excel;
							}catch(Exception Excel2010){
									try{
											Excel = new ActiveXComponent("Excel.Application.13");//Excel 2003
											System.out.println("Excel 2010 is OK!");							 
                                 vectorVersion.add("2010") ;
											return Excel;
									}catch(Exception Excel14){
											try{
													Excel = new ActiveXComponent("Excel.Application.14");//Excel 2003
													System.out.println("Excel.Application.14 is OK!");	
                                       vectorVersion.add(".14") ;
													return Excel;
											}catch(Exception Excel15){
													try{
															Excel = new ActiveXComponent("Excel.Application.15");//Excel 2003
															System.out.println("Excel.Application.15 is OK!");							 
                                             vectorVersion.add(".15") ;
															return Excel;
													}catch(Exception Excel16){
															try{
																	Excel = new ActiveXComponent("Excel.Application.16");//Excel 2003
																	System.out.println("Excel.Application.16 is OK!");							 
                                                   vectorVersion.add(".16") ;
																	return Excel;
															}catch(Exception Excel17){
																	try{
																			Excel = new ActiveXComponent("Excel.Application.17");//Excel 2003
																			System.out.println("Excel.Application.17 is OK!");							 
                                                         vectorVersion.add(".17") ;
																			return Excel;
																	}catch(Exception Excel18){
																			try{
																					Excel = new ActiveXComponent("Excel.Application.18");//Excel 2003
																					System.out.println("Excel.Application.18 is OK!");							 
                                                               vectorVersion.add(".18") ;
																					return Excel;
																			}catch(Exception Excel19){
																					try{
																							Excel = new ActiveXComponent("Excel.Application.19");//Excel 2003
																							System.out.println("Excel.Application.19 is OK!");							 
                                                                     vectorVersion.add(".19") ;
																							return Excel;
																					}catch(Exception Excel20){
																							try{
																									Excel = new ActiveXComponent("Excel.Application.20");//Excel 2003
																									System.out.println("Excel.Application.20 is OK!");							 
                                                                           vectorVersion.add(".20") ;
																									return Excel;
																							}catch(Exception ExcelError){
																									System.out.println("�Шϥ� Excel2010 �H�W����!");							 								
																		
																							}
																
																					}
														
																			}
												
																	}
										
															}
								
													}
						
											}
				
									}
		
							}

					}
				}
			}
		}
		Excel = new ActiveXComponent("Excel.Application");
		System.out.println("All is OK!");							 		 
		return Excel;
	}
	// ����
   // �o��sheet���`��
   public int getSheetCount(Dispatch objectSheets) {  
       int count = Dispatch.get(objectSheets, "count").toInt();  
       return count;  
   }  
	// ���o�S�w Sheet �� 0 �}�l
	public Dispatch  getDispatchSheet(int  intSheet,  Dispatch objectSheets) {
			try {
					Dispatch  objectSheet1  =  Dispatch.call(objectSheets,  "Item",  new  Variant(intSheet+1)).toDispatch() ;
					if(objectSheet1  !=  null)  return  objectSheet1;
			}catch(Exception  e) {
					return  null ;
			}
			return  null ;
	}
	// �� Sheet �W�� ���o�S�w Sheet 
	public Dispatch  getDispatchSheetForName(String  stringSheetName,  Dispatch objectSheets) {
			try {
               Dispatch  objectSheet1  =  Dispatch.call(objectSheets,  "Item",  stringSheetName).toDispatch() ;
					if(objectSheet1  !=  null)  return  objectSheet1;
			}catch(Exception  e) {
					return  null ;
			}
			return  null ;
	}
	// �R�� Sheet
	public void  doDeleteSheet(Dispatch  objectSheet ){
			Dispatch.call(objectSheet, "Delete") ; 
	}
	// ���� Sheet
	public void  doHideSheet(Dispatch  objectSheet ){
         Dispatch.put(objectSheet, "Visible", new Boolean(false));  
	}
	// �ƻs Sheet (�N objectSheetSource �ƻs���� objectSheetDesc ���e�C)
	public void  doCopySheet(Dispatch  objectSheetSource,  Dispatch  objectSheetDesc ){
			Dispatch.call(objectSheetSource, "Copy", objectSheetDesc);
	}
	// ���o Sheet �W��
	public String getSheetName(Dispatch objectSheet1){
		String  stringSheetName  =  Dispatch.get(objectSheet1, "Name").toString() ;
		return  stringSheetName;
	}
	public void doSetSheetName(String  stringSheetName,  Dispatch objectSheet1){
			Dispatch.put(objectSheet1, "Name", stringSheetName) ;
	}
	//Copy Sheet2 Template to Sheet1
	public void CopyPage(Dispatch objectSheet1,  Dispatch objectSheet2){
		Dispatch.call(objectSheet2,  "Activate");										 
		Dispatch objectA1  =  Dispatch.invoke(objectSheet2,  "Range", Dispatch.Get,  new Object[] {"A1"},  new int[1]).toDispatch();
		Dispatch.call(objectA1,  "Select");
		Dispatch  objectRow  =  Dispatch.invoke(objectSheet2,  "Rows",  Dispatch.Get,  new Object[] {"1:" + intPageAllRowGlobal},  new int[1]).toDispatch();	
		Dispatch.call(objectRow,  "Copy");
		//Sheet1
		Dispatch.call(objectSheet1,  "Activate");
		objectA1  =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,  new Object[] {"A" + ((intPageNoGlobal - 1) * intPageAllRowGlobal + 1)},  new int[1]).toDispatch();
		Dispatch.call(objectA1,  "Select");
		Dispatch.call(objectSheet1,  "Paste");			
	}
	
	// �����ɡA����
	public int doChangePage(int  intRecordNo,  Dispatch  objectSheet1,  Dispatch  objectSheet2){
		if(intRecordNo  >=  (intPageDataRowGlobal + intStartDataRowGlobal - 1)){
			CopyPage(objectSheet1,  objectSheet2);
			String  stringStartClearCol  =  getExcelColumnName( "A",  intStartClearColG) ;
			String  stringEndClearColG   =  getExcelColumnName( "A",  intEndClearColG) ;
			//System.out.println("�M��"+intStartClearColG+"--"+intEndClearColG+"--------------"+(""+stringStartClearCol + intStartDataRowGlobal + ":" +stringEndClearColG + (intStartDataRowGlobal + intPageDataRowGlobal -1))) ;
			doClearContents(stringStartClearCol + intStartDataRowGlobal + ":" +stringEndClearColG + (intStartDataRowGlobal + intPageDataRowGlobal -1),  objectSheet2) ;
			intRecordNo   =   intStartDataRowGlobal - 1 ;
			intPageNoGlobal++;
		}
		return  intRecordNo ;
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
		//char charColumn   = stringColumn.charAt(0);
		int    intColumn    = getExcelColumnNo(stringColumn) + 'A'  -1;
		String stringReturn = ""; 
		// < A
		if ((intColumn + intCalculate) < 65 ) {
			stringReturn = "0";
		}
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
	
	// intPosColumn  		Excel ����m(��)
	// intPosRow   			Excel ����m(�C)
	// objectSheet2   		Excel ���u�@��
	public void putDataIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutData, Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch.put(Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,  new Object[] {stringTmp+ (intPosRow+1)},  new int[1]).toDispatch(),
			     "Value",
			     stringPutData
			    );
	}
	
	// intPosColumn  		Excel ����m(��)
	// intPosRow   			Excel ����m(�C)
	// objectSheet2   		Excel ���u�@��
	public void putTypeIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutType, Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch.put(Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,  new Object[] {stringTmp+ (intPosRow+1)},  new int[1]).toDispatch(),
			     "NumberFormatLocal",
			     stringPutType
			    );
	}
	
	// intPosColumn  		Excel ����m(��)
	// intPosRow   			Excel ����m(�C)
	// objectSheet2   		Excel ���u�@��
	public String  getDataFromExcel(int  intPosColumn,  int  intPosRow,  Dispatch  objectSheet){	
		String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch cell         =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
		String  stringReturn  =  Dispatch.get(cell,"Value").toString();
		//
		return  stringReturn.trim() ;
	}
	
	public String  getDataFromExcel2(int  intPosColumn,  int  intPosRow,  Dispatch  objectSheet)throws  Throwable {		
		String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch cell         =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
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
	public String  getDataFromExcel3(int  intPosColumn,  int  intPosRow,  Dispatch  objectSheet) throws  Throwable {	
		String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch cell                =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
		String  stringReturn  =  Dispatch.get(cell,"Text").toString();
		//
		if("null".equals(stringReturn))  stringReturn  =  "" ;
		//
		if(!"".equals(stringReturn)) {
		    Farglory.util.FargloryUtil  exeUtil  =  new  Farglory.util.FargloryUtil() ;
		    if(exeUtil.isDigitNum (stringReturn)) {
		        stringReturn  =  exeUtil.doDeleteDogAfterZero (stringReturn);
		    }
		}
		return  stringReturn.trim() ;
	}
	public String  getDataFromExcel4(int  intPosColumn,  int  intPosRow,  Dispatch  objectSheet){	
		String  stringTmp     =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch cell                =  Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+ (intPosRow+1)}, new int[1]).toDispatch();
		String  stringReturn  =  Dispatch.get(cell,"Value").toString();
		//
		if("null".equals(stringReturn))  stringReturn  =  "0" ;
		return  stringReturn.trim() ;
	}
	// 20110531 B3774
	public String getDataFromExcel(int intPosColumn, int intPosRow, String stringType, Dispatch objectSheet){
		String   stringTmp    = getExcelColumnName( "A",  intPosColumn);
		Dispatch cell         = Dispatch.invoke(objectSheet, "Range", Dispatch.Get, new Object[]{stringTmp+(intPosRow+1)}, new int[1]).toDispatch();
		String   stringReturn = Dispatch.get(cell, stringType).toString();
		//
		return stringReturn.trim();
	}
	
	// �M�����A��줧������|�Q�M��
	public void  doClearContents(String  stringRange,  Dispatch  objectSheet){	
		Dispatch objectRangeClear  =  Dispatch.invoke(objectSheet,  "Range",   Dispatch.Get,  new Object[] {stringRange},  new int[1]).toDispatch();
		Dispatch.call(objectRangeClear,  "ClearContents");
	}
	public void  doClearContents(int  intColStart,  int  intRowStart,  int  intColEnd,  int  intRowEnd,  Dispatch  objectSheet){	
       for(int  intCol=intColStart  ;  intCol<=intColEnd  ;  intCol++) {
                for(int  intRow=intRowStart  ;  intRow<=intRowEnd  ;  intRow++) {
                       putDataIntoExcel(intCol,  intRow,  "", objectSheet) ;
                }
       }
	}
	
	// �ƻs�@�����
	public void  doCopyColumns(int  intPosColumn,  Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Copy");
		Dispatch.call(objectRange, "Insert");
	}
	// �ƻs�@����� intPosColumn�A�K�� intPosColumn2
	public void  doCopyColumnsSpecPos(int  intPosColumn,  int  intPosColumn2,  Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		stringTmp  =  getExcelColumnName( "A",  intPosColumn2) ;
		Dispatch  objectRange2  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange,  "Copy");
		Dispatch.call(objectRange2, "Insert");
	}
	
	// �ƻs�Y�ϰ���
	public void  doCopyColumns(int  intPosColumnS,  int  intPosColumnE,  Dispatch  objectSheet){	
		String  stringTmpS  =  getExcelColumnName( "A",  intPosColumnS) ;
		String  stringTmpE  =  getExcelColumnName( "A",  intPosColumnE) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmpS  +  ":"  +  stringTmpE}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Copy");
		Dispatch.call(objectRange, "Insert");
	}
	
	// �ƻs�@��ӦC
	public void  doCopyRow(int  intPosRow,  Dispatch  objectSheet){	
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intPosRow  +  ":"  + intPosRow}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Copy");
		Dispatch.call(objectRange, "Insert");
	}
	
	public void  doCopyRowSpecPos(int  intPosRow,  int  intPosRow2,  Dispatch  objectSheet){
		Dispatch  objectRange   =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intPosRow   +  ":"  + intPosRow}, new int[1]).toDispatch( ) ;
		Dispatch  objectRange2  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intPosRow2  +  ":"  + intPosRow2}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange,  "Copy");
		Dispatch.call(objectRange2, "Insert");
	}
	
	public void  doCopyRowSpecPos(int  intCopyRowS,  int  intCopyRowE,  int  intInsertRow,  Dispatch  objectSheet){
		Dispatch  objectRange   =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intCopyRowS  +  ":"  + intCopyRowE}, new int[1]).toDispatch( ) ;
		Dispatch  objectRange2  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intInsertRow  +  ":"  + intInsertRow}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange,  "Copy");
		Dispatch.call(objectRange2, "Insert");
	}
	
	public void  doCopyRow(int  intRowStart,  int  intRowEnd,  Dispatch  objectSheet){	
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intRowStart  +  ":"  + intRowEnd}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Copy");
		Dispatch.call(objectRange, "Insert");
	}
	
	// �R���@����
	public void  doDeleteColumns(int  intPosColumn,  Dispatch  objectSheet){	
		String    stringTmp    =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		//Dispatch.put(objectRange, "Delete",  new  varant(-4159 ));
		Dispatch.call(objectRange, "Delete");
	}
	
	public void  doDeleteColumns2(String  stringRange,  Dispatch  objectSheet){	
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringRange}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Delete");
	}
	
	// �R���@��C
	public void  doDeleteRows(int  intPosRowS,  int  intPosRowE,  Dispatch  objectSheet){	
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intPosRowS  +  ":"  +  intPosRowE}, new int[1]).toDispatch( ) ;
		//Dispatch.put(objectRange, "Delete",  new  varant(-4159));
		Dispatch.call(objectRange, "Delete");
	}
	
	// �ܧ���e
	public void  doChangeColumnsSize(int  intPosColumn,  int  intColumnSize,  Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		Dispatch.put(objectRange, "ColumnWidth", new Variant(intColumnSize));
	}
	// �ܧ�氪
   // Excel �u���\ 0 - 409
	public void  doChangeRowSize(int  intPosRow,  int  intRowSize,  Dispatch  objectSheet){	
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{intPosRow  +  ":"  +  intPosRow}, new int[1]).toDispatch( ) ;
      //
      if(intRowSize  <  0)    intRowSize  = 0 ;
      if(intRowSize  >  409)  intRowSize  = 409 ;
      //
		Dispatch.put(objectRange, "RowHeight", new Variant(intRowSize));
	}
	// �e�ؽu
	//exeExcel.doLineStyle("A1:C1",  objectSheet1) ;
	public void  doLineStyle(int intX1,  int  intY1,  int  intX2,  int  intY2,  Dispatch  objectSheet1) throws Throwable {
	    String  stringRange = "" ;
	    for(int  intNo=intY1  ;  intNo<=intY2  ;  intNo++) {
          stringRange = getExcelColumnName( "A",  intX1)+(intNo+1)+":"+
          	            getExcelColumnName( "A",  intX2)+(intNo+1) ;
	        doLineStyle(stringRange,  objectSheet1) ;
	    }
	}
	public void  doLineStyleBorder(String  stringRange,  Dispatch  objectSheet1){	
		Dispatch objectRange    =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
		Dispatch.call(objectSheet1,  "Activate");
		Dispatch.call(objectRange, "Select");
		// ��
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"7"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�W
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"8"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�U
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"9"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�k
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"10"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		// ��
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"11"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
	}
	public void  doLineStyle(String  stringRange,  Dispatch  objectSheet1){	
		Dispatch objectRange    =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
		Dispatch.call(objectSheet1,  "Activate");
		Dispatch.call(objectRange, "Select");
		// ��
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"7"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�W
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"8"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�U
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"9"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		//�k
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"10"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
		// ��
		objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"11"},  new int[1]).toDispatch();
		Dispatch.put(objectBorders,  "LineStyle",  "1") ;
	}
	
	// �e�ؽu2 970402 B3774
	public void doLineStyle2(String stringRange,      Dispatch objectSheet, 
				 String stringTopLine,    String stringTopWeight,
				 String stringBottomLine, String stringBottomWeight,
				 String stringLeftLine,   String stringLeftWeight,
				 String stringRightLine,  String stringRightWeight,
				 String stringCenterLine, String stringCenterWeight){
		Dispatch objectRange    =  Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
		Dispatch.call(objectSheet,  "Activate");
		Dispatch.call(objectRange, "Select");
		//�W
		if(stringTopLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"8"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringTopLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringTopWeight) ;
		}
		//�U
		if(stringBottomLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"9"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringBottomLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringBottomWeight) ;
		}
		//��
		if(stringLeftLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"7"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringLeftLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringLeftWeight) ;
		}
		//�k
		if(stringRightLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"10"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringRightLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringRightWeight) ;
		}
		//��
		if(stringCenterLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"11"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringCenterLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringCenterWeight) ;
		}
	}
	// �e�ؽu3 970613 B3774
	public void doLineStyle3(String stringRange,      Dispatch objectSheet, 
				 String stringTopLine,    String stringTopWeight,
				 String stringBottomLine, String stringBottomWeight,
				 String stringLeftLine,   String stringLeftWeight,
				 String stringRightLine,  String stringRightWeight,
				 String stringVertLine,   String stringVertWeight,
				 String stringHoriLine,   String stringHoriWeight){
		Dispatch objectRange    =  Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
		Dispatch.call(objectSheet,  "Activate");
		Dispatch.call(objectRange, "Select");
		//�W
		if(stringTopLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"8"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringTopLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringTopWeight) ;
		}
		//�U
		if(stringBottomLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"9"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringBottomLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringBottomWeight) ;
		}
		//��
		if(stringLeftLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"7"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringLeftLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringLeftWeight) ;
		}
		//�k
		if(stringRightLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"10"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringRightLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringRightWeight) ;
		}
		//��(����)
		if(stringVertLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"11"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringVertLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringVertWeight) ;
		}
		//��(����)
		if(stringHoriLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"12"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringHoriLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringHoriWeight) ;
		}
	}
	// �e�ؽu4 980619 B3774
	public void doLineStyle4(String stringRange,      Dispatch objectSheet, 
				 String stringTopLine,    String stringTopWeight,    String stringTopColorIndex, 
				 String stringBottomLine, String stringBottomWeight, String stringBottomColorIndex, 
				 String stringLeftLine,   String stringLeftWeight,   String stringLeftColorIndex, 
				 String stringRightLine,  String stringRightWeight,  String stringRightColorIndex, 
				 String stringVertLine,   String stringVertWeight,   String stringVertColorIndex, 
				 String stringHoriLine,   String stringHoriWeight,   String stringHoriColorIndex){
		Dispatch objectRange    =  Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
		Dispatch.call(objectSheet,  "Activate");
		Dispatch.call(objectRange, "Select");
		//�W
		if(stringTopLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"8"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringTopLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringTopWeight) ;
			if(stringTopColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringTopColorIndex) ;
			}
		}
		//�U
		if(stringBottomLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"9"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringBottomLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringBottomWeight) ;
			if(stringBottomColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringBottomColorIndex) ;
			}
		}
		//��
		if(stringLeftLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"7"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringLeftLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringLeftWeight) ;
			if(stringLeftColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringLeftColorIndex) ;
			}
		}
		//�k
		if(stringRightLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"10"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringRightLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringRightWeight) ;
			if(stringRightColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringRightColorIndex) ;
			}
		}
		//��(����)
		if(stringVertLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"11"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringVertLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringVertWeight) ;
			if(stringVertColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringVertColorIndex) ;
			}
		}
		//��(����)
		if(stringHoriLine.length()!=0){
			objectBorders  =  Dispatch.invoke(objectRange,  "Borders", Dispatch.Get,new Object[] {"12"},  new int[1]).toDispatch();
			Dispatch.put(objectBorders,  "LineStyle",  stringHoriLine) ;
			Dispatch.put(objectBorders,  "Weight",     stringHoriWeight) ;
			if(stringHoriColorIndex.length()!=0){
				Dispatch.put(objectBorders,  "ColorIndex",  stringHoriColorIndex) ;
			}
		}
	}
	// ������覡 971125 B3774
	// �m�� 3 �a�k 1 �a 2          new Variant(-4152 )
	public void setHVAlign(String stringRange, Dispatch objectSheet, String stringHAlign, String stringVAlign){
		Dispatch objectRange   = Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		//
		Dispatch.call(objectRange, "Select");
		if(stringHAlign.length() != 0){
			Dispatch.put(objectRange, "HorizontalAlignment", stringHAlign);	
		}
		if(stringVAlign.length() != 0){
			Dispatch.put(objectRange, "HorizontalAlignment", stringVAlign);	
		}
	}
	// �]�w�r���C��
	// 3   ����
	// Bold ����(Y)      Italic ����(Y)     Underline ���u(xlUnderlineStyleSingle 2) 
	//exeExcel.setFontColorRange("Bold",     "Y",  "3",  "D4",  objectSheet1) ;
	//exeExcel.setFontColorRange("Italic",   "Y",  "3",  "D5",  objectSheet1) ;
	//exeExcel.setFontColorRange("Underline","2",  "3",  "D6",  objectSheet1) ;
	public  void  setFontColorRange(String  stringFontType,  String  stringFontValue,  String stringColor,  String  stringRange,  Dispatch  objectSheet1){
    		Dispatch  objectRange     =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
    		Dispatch  objectFont      =  null ;
		    //		    
	    	//Dispatch.call(objectRange, "Select");
	    	objectFont = Dispatch.get(objectRange, "Font").toDispatch( ) ;
	    	// �r��
	    	if(!"".equals(stringFontType)) {
    			  if(!"Underline".equals(stringFontType)) {
    				    Dispatch.put(objectFont,  stringFontType,  new Variant(stringFontValue.equals("Y"))) ;
    			  } else {
    				    Dispatch.put(objectFont,  stringFontType,  stringFontValue) ;
    	    	}
		    }
	    	// �C��
	    	if(!"".equals(stringColor))Dispatch.put(objectFont,  "ColorIndex",  stringColor) ;
	}
	// �]�w���I���C��
	// 36 �L��  35 �L��   34 �L��   40 �L��
	//exeExcel.setBackgroundColorRange("40",  "E7",  objectSheet1) ;
	public  void  setBackgroundColorRange(String stringColor,  String  stringRange,  Dispatch  objectSheet1){
		Dispatch  objectRange     =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
	    	Dispatch  objectInterior  =  null ;
	    	//
	    	//Dispatch.call(objectRange, "Select");
	    	objectInterior = Dispatch.get(objectRange, "Interior").toDispatch( ) ;
	    	Dispatch.put(objectInterior,  "ColorIndex",  stringColor) ;
	    	Dispatch.put(objectInterior,  "Pattern",  "1") ;
	}
	//�X���x�s��
	public  void  doMergeCells(String  stringRange,  Dispatch  objectSheet1){
		Dispatch objectRange = Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[]{stringRange}, new int[1]).toDispatch();
		Dispatch.put(objectRange, "MergeCells", "True");
	}
	//������å\��
	public  void  doHiddenCells(String  stringRange,  Dispatch  objectSheet1){
		Dispatch objectRange  = Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[]{stringRange}, new int[1]).toDispatch();
		Dispatch entireColumn = Dispatch.get(objectRange,"EntireColumn").toDispatch();
		//Dispatch.put(entireColumn, "Hidden", new Boolean(true));
		Dispatch.put(entireColumn, "Hidden", new Boolean(true));
	}
	// ����
	// �d�ҡGsetPageTitleName(objectSheet1,  "&14���q�W��\n�q��o��\n94�~11��25��ӳ�") ;
	// �d�ҡGsetPageTitleName(objectSheet1,  "&\"�з���,����\"&2894�~11��25��ӳ�") ; 
	/*�榡�N�X �y�z 
      &L �U�@���r���a������C 
      &C �U�@���r���m���C 
      &R �U�@���r���a�k����C 
      &E �}���C�L�����u�\��C 
      &X �}���C�L�W�Цr���\��C 
      &Y �}���C�L�U�Цr���\��C 
      &B �}���C�L����r���\��C 
      &I �}���C�L����r���\��C 
      &U �}���C�L���u�\��C 
      &S �}���C�L�R���u�\��C 
      &D �C�L�ثe����C 
      &T �C�L�ثe�ɶ��C 
      &F �C�L���W�١C 
      &A �C�L����ï���ҦW�١C 
      &P �C�L���X�C 
      &P+�Ʀr �C�L���X�[�W���w�Ʀr�C 
      &P-�Ʀr �C�L���X��h���w�Ʀr�C 
      && �C�L��ӳs�r���C 
      & "fontname" �H���w�r���C�L�H��r���C�����[�W���޸��C 
      &nn �H���w�r���j�p�C�L�H��r���C�ϥΨ��ƫ��w�r�����I�Ƥj�p�C 
      &N �C�L����`���ơC 
  */
	public  void  setPageTitleName(Dispatch  objectSheet1,  String stringPageTitleName){
		setPageTitleName(objectSheet1,  "CenterHeader",  stringPageTitleName);
	}
	/*
	���� RightHeader
	���� CenterHeader
	�k�� LeftHeader
	*/
	public  void  setPageTitleName(Dispatch  objectSheet1,  String  stingPosition,  String stringPageTitleName){
		objectSheet1  =  Dispatch.get(objectSheet1,  "PageSetup").toDispatch( );
		Dispatch.put(objectSheet1,  stingPosition,  stringPageTitleName) ;
	}	

	
	//����
	public  void  setPageFooter(Dispatch  objectSheet1,  String stringPageTitleName){
		objectSheet1  =  Dispatch.get(objectSheet1,  "PageSetup").toDispatch( );
		Dispatch.put(objectSheet1,  "CenterFooter",  stringPageTitleName) ;
	}		
	
	// ���o�w�}�Ҥ� sheet �� �̤j�C��
	public  int  getExcelMaxRow(Dispatch  dispatchSheet1){
		Dispatch  dispatchUsedRange =  Dispatch.get(dispatchSheet1, "UsedRange").toDispatch();
		String[]  retArray          =  convert.StringToken(Dispatch.call(dispatchUsedRange, "Address").toString( ),  "$") ;
		if(retArray.length  !=  5)  return  1 ;
		int       intRowMax         =  doParseInteger(retArray[4].trim( )) ;
		int       intColMax         =  getExcelColumnNo(retArray[3].trim( )) ;   
		return  intRowMax ;
	}
	
	// ���o�w�}�Ҥ� sheet �� �̤j����
	public  int  getExcelMaxCol(Dispatch  dispatchSheet1){
		Dispatch  dispatchUsedRange =  Dispatch.get(dispatchSheet1, "UsedRange").toDispatch();
		String[]  retArray          =  convert.StringToken(Dispatch.call(dispatchUsedRange, "Address").toString( ),  "$") ;
		if(retArray.length  !=  5)  return  1 ;
		int       intColMax         =  getExcelColumnNo(retArray[3].trim( )) ;   
		return  intColMax ;
	}
	
	// �۰ʽվ���e
	public void doAutoAdjustColWidth(String  stringColumnName,  Dispatch  objectSheet){
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringColumnName}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange,  "AutoFit");
	}
	
	// ���� B3774
	public void setPageBreak(int  intRow,  Dispatch  objectSheet){
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Rows",  Dispatch.Get,  new Object[]{""+intRow}, new int[1]).toDispatch( ) ;
		Dispatch objHPageBreaks = Dispatch.call(objectSheet,  "HPageBreaks").toDispatch();
		Dispatch.call(objHPageBreaks, "Add", objectRange);
	}	
	
	// �䥦
	public  int  doParseInteger(String  stringNum) {
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
   //�e�����ϧ�
	//type , �Z������ت��Z�� , �Z���W��ت��Z�� , �Ϫ��e�� , �Ϫ����� , �񺡪��C�� , �u�ت��C�� , ��r
	//�d�� setRectangle(1,100,100,200,10,"150,100,50","150,100,50","test",objectSheet1);
	public  void  setRectangle(int type , int left , int top , int width, int height , String Fill_Color , String Line_Color , String Text,  Dispatch  objectSheet1){

		Dispatch shapes = Dispatch.get(objectSheet1, "Shapes").toDispatch();
		Dispatch   shape      =   Dispatch.invoke(shapes,"AddShape", 1
		                                        , new Object[]{  
							    new Integer(type)//�Ϫ����� 1
							  , new Integer(left)//�Z������ت��Z�� 10
							  , new Integer(top)//�Z���W��ت��Z�� 10 
							  , new Integer(width)//�Ϫ��e��        100
							  , new Integer(height)//�Ϫ�����       10
							  }  
							, new int[1]).toDispatch();
		
		//�񺡪��C�� �� 0,255,0 �� 150,100,50
  		//�i�Ѧ� G:\��T��\Excel\test\jacob_shape_color_list_v1.xlsx  
		System.out.println(" Fill_Color "+Fill_Color);
		Dispatch  object_Fill = Dispatch.get(shape, "Fill").toDispatch( ) ;
		Dispatch  object_Fill_ForeColor = Dispatch.get(object_Fill, "ForeColor").toDispatch( ) ;
	 	Dispatch.put(object_Fill_ForeColor,  "RGB",  Fill_Color) ;
		
		//�u�ت��C��
		Dispatch  object_Line = Dispatch.get(shape, "Line").toDispatch( ) ;
		Dispatch  object_Line_ForeColor = Dispatch.get(object_Line, "ForeColor").toDispatch( ) ;
	        Dispatch.put(object_Line_ForeColor,  "RGB",  Line_Color) ;			
		
		//��r
		Dispatch  object_TextFrame2 = Dispatch.get(shape, "TextFrame2").toDispatch( ) ;
		Dispatch  object_TextRange = Dispatch.get(object_TextFrame2, "TextRange").toDispatch( ) ;
		Dispatch  object_Characters = Dispatch.get(object_TextRange, "Characters").toDispatch( ) ;
		Dispatch.put(object_Characters,  "Text",  Text ) ;
   }	
	//type , �Z������ت��Z�� , �Z���W��ت��Z�� , �Ϫ��e�� , �Ϫ����� , �񺡪��C�� , �u�ت��C�� , ��r
	//�d�� setRectangle(1,100,100,200,10,"150,100,50","150,100,50","text",objectSheet1);
	public  void  setRectangle(int type , double left , double top , double width, double height , String Fill_Color , String Line_Color , String Text,  Dispatch  objectSheet1){

		Dispatch shapes = Dispatch.get(objectSheet1, "Shapes").toDispatch();
		Dispatch   shape      =   Dispatch.invoke(shapes,"AddShape", 1
		                                        , new Object[]{  
							    new Integer(type)//�Ϫ����� 1
							  , new Double(left)//�Z������ت��Z�� 10
							  , new Double(top)//�Z���W��ت��Z�� 10 
							  , new Double(width)//�Ϫ��e��        100
							  , new Double(height)//�Ϫ�����       10
							  }  
							, new int[1]).toDispatch();
		
		//�񺡪��C�� �� 0,255,0 �� 150,100,50
		//�i�Ѧ� G:\��T��\Excel\testjacob_shape_color_list_v1.xlsx 
		System.out.println(" Fill_Color "+Fill_Color);
		Dispatch  object_Fill = Dispatch.get(shape, "Fill").toDispatch( ) ;
		Dispatch  object_Fill_ForeColor = Dispatch.get(object_Fill, "ForeColor").toDispatch( ) ;
	 	Dispatch.put(object_Fill_ForeColor,  "RGB",  Fill_Color) ;
		
		//�u�ت��C��
		Dispatch  object_Line = Dispatch.get(shape, "Line").toDispatch( ) ;
		Dispatch  object_Line_ForeColor = Dispatch.get(object_Line, "ForeColor").toDispatch( ) ;
	    Dispatch.put(object_Line_ForeColor,  "RGB",  Line_Color) ;			
		
		//��r
		Dispatch  object_TextFrame2 = Dispatch.get(shape, "TextFrame2").toDispatch( ) ;
		Dispatch  object_TextRange = Dispatch.get(object_TextFrame2, "TextRange").toDispatch( ) ;
		Dispatch  object_Characters = Dispatch.get(object_TextRange, "Characters").toDispatch( ) ;
		Dispatch.put(object_Characters,  "Text",  Text ) ;
		
		
		
   }	
   
   
   //�e�����u
	//type , �u������X�b , �u������Y�b , �u���k��X�b , �u���k��Y�b , �u�����C��
	//�d�� setConnectorStraight(1,100,100,200,10,"150,100,50",objectSheet1);
	public  void  setConnectorStraight(int type , double leftX , double leftY , double rightX, double rightY , String Line_Color,   Dispatch  objectSheet1){

		Dispatch shapes = Dispatch.get(objectSheet1, "Shapes").toDispatch();
		Dispatch   shape      =   Dispatch.invoke(shapes,"AddConnector", 1
		                                        , new Object[]{  
							    new Integer(type)//�Ϫ����� 1
							  , new Double(leftX)//�u������X�b
							  , new Double(leftY)//�u������Y�b 
							  , new Double(rightX)//�u���k��X�b
							  , new Double(rightY)//�u���k��Y�b
							  }  
							, new int[1]).toDispatch();
		

		//�u�����C��
		Dispatch  object_Line = Dispatch.get(shape, "Line").toDispatch( ) ;
		Dispatch  object_Line_ForeColor = Dispatch.get(object_Line, "ForeColor").toDispatch( ) ;
	    Dispatch.put(object_Line_ForeColor,  "RGB",  Line_Color) ;		
		
		//Dispatch  object_Line = Dispatch.get(shape, "Line").toDispatch( ) ;
		//Dispatch  object_ Line_Weight = Dispatch.get(object_Line, "Weight").toDispatch( ) ;
	    //Dispatch.put(object_Line_Weight, "Weight" ,   Line_Weight) ;	
		
   } 
	// �O�@�u�@�� B3774
	public  void  setProtect(String  stringPassword,  Dispatch  objectSheet){
		if(stringPassword.length() == 0){
			Dispatch.invoke(objectSheet, "Protect", Dispatch.Method, new Object[] { new Variant("Farglory"), new Variant(true), new Variant(true), new Variant(true)}, new int[1]);	
		}
		else {
			Object objPassword = stringPassword;		
			Dispatch.invoke(objectSheet, "Protect", Dispatch.Method, new Object[] { new Variant(objPassword), new Variant(true), new Variant(true), new Variant(true)}, new int[1]);	
		}

	}
  	//�r�[�R���u
	//A1:A1��쪺�r�n�[�R���u
	//excel�{���d�� Boolean Boolflag = new Boolean("true");
	//                       exeExcel.setStrikethrough(Boolflag,"A1:A1",objectSheet1);
	// flag true ���R���u false �S�R���u         
	public  void  setStrikethrough( Boolean boolflag,  String  stringRange,  Dispatch  objectSheet1){
    		Dispatch  objectRange     =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
    		Dispatch  objectFont      =  null ; 
	    	objectFont = Dispatch.get(objectRange, "Font").toDispatch( ) ;
			Dispatch.put(objectFont, "Strikethrough",boolflag);
	}
}