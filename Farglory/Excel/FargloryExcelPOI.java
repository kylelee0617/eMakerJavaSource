package Farglory.Excel ;
import  java.io.*;//
import  java.util.*;
import  com.jacob.activeX.*;
import  com.jacob.com.*;
import  jcx.util.*;
import  org.apache.poi.hssf.usermodel.*;
import  org.apache.poi.poifs.filesystem.*;
public  class  FargloryExcelPOI {
	// �����]�w
	int          intStartDataRowGlobal         =  6 ;                // �}�lExcel �C��
	int          intPageDataRowGlobal        =  20 ;               // ����Excel �C��
	int          intPageAllRowGlobal            =  28 ;               // �@�����C��
	int          intPageNoGlobal                   =  1 ;                // ��l����
	int          intStartClearColG           =  0 ;                // �}�l�M�����(�w�] 'A')
	int          intEndClearColG             =  17 ;                // �����M�����(�w�] 'R')
	boolean      booleanFlag                 =  true ;             // �w���Gtrue ��ܭn�w���C
	boolean      booleanFirstSheetFlagG      =  true ;             // �]�w�� Sheet ��Action�A�w�]�ĤG��
	boolean      booleanVisibleG             =  true ;             // �]�w�n���n��� Excel ���ϥΪ̬�
	String       stringFilePath              =  "C:\\temp.xls" ;   // ���w���ɡA�D�Ŧr��ɡA�t�s�s��
	HSSFCellStyle  cellStyleG  =  null ;								// �w�]�r��
	// �غc�l
	public FargloryExcelPOI(){	}
/*	public FargloryExcelPOI(int  intStartDataRow,  int  intPageDataRow,  int  intPageAllRow,  int  intPageNo){
       intStartDataRowGlobal  =  intStartDataRow ;
       intPageDataRowGlobal   =  intPageDataRow ;
       intPageAllRowGlobal    =  intPageAllRow ;
       intPageNoGlobal        =  intPageNo ;
	}*/
	//
	public void setVisibleProperty(boolean  booleanVisible) throws Exception {
	    booleanVisibleG  =  booleanVisible ;
	}
	//
/*	public void setClearCol(int  intStartClearCol,  int  intEndClearCol) throws Exception {
	    intStartClearColG  =  intStartClearCol ;
	    intEndClearColG    =  intEndClearCol ;
	}*/
	//
	public void setPreView(boolean  booleanFlagValue,  String  stringPathValue) throws Exception {
	    booleanVisibleG  =  booleanFlagValue ;
	    stringFilePath   =  stringPathValue ;
	}
	//
/*	public int getStartDataRow() throws Exception {
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
	}*/
	// �إ� Excel ����

	public HSSFWorkbook getExcelObject(String stringExcelName){
		Vector        retVector  =  new  Vector() ;
		HSSFWorkbook  workBook  =  null ;
		//
		try {
				FileInputStream   fis             =  new  FileInputStream(stringExcelName);
				POIFSFileSystem   fs              =  new  POIFSFileSystem( fis );
				                              workBook  =  new  HSSFWorkbook( fs );   
				//
				setDefaultCellStyle(HSSFFont.COLOR_NORMAL,  HSSFFont.BOLDWEIGHT_NORMAL,  HSSFCellStyle.ALIGN_LEFT,  HSSFCellStyle.BORDER_THIN,  workBook) ;

	  } catch(Exception e) {}
		return  workBook ;
	}
	// ��ܨ����� Excel ����
	public void getReleaseExcelObject(HSSFWorkbook  workBook){
     try {
          FileOutputStream  fout  =  new  FileOutputStream(stringFilePath);
          workBook.write( fout );
          fout.close();
					//��� Excel
			    if(booleanVisibleG){
								Farglory.Excel.FargloryExcel  exeFun        =  new  Farglory.Excel.FargloryExcel() ;
								Vector                        retVector     =  exeFun.getExcelObject(stringFilePath) ;
								Dispatch                      objectSheet1  =  (Dispatch)retVector.get(1) ;
								Dispatch                      objectClick   =  null ;
								// ���� Excel ����
		  					exeFun.getReleaseExcelObject(retVector) ;
			    }
	    } catch(Exception e) {}
	    workBook  =  null ;
	}
	// ����
	//Copy Sheet2 Template to Sheet1
/*	public void CopyPage(Dispatch objectSheet1,  Dispatch objectSheet2){
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
	}*/
	// shortColor
	// HSSFFont��COLOR_RED�BCOLOR_NORMAL
	// shortFont
	//HSSFFont �� BOLDWEIGHT_BOLD�BBOLDWEIGHT_NORMAL
	// shortAlign
	// HSSFCellStyle �� ALIGN_CENTER�BALIGN_LEFT    �BALIGN_GENERAL�BALIGN_RIGHT�B
	//                             ALIGN_FILL�B       ALIGN_JUSTIFY�BALIGN_CENTER_SELECTION
	// shortBorder
	// HSSFCellStyle �� BORDER_NONE�BBORDER_DOTTED�BBORDER_THIN�BBORDER_MEDIUM�BBORDER_DASHED�BBORDER_DOUBLE
	public void setDefaultCellStyle(short  shortColor,  short  shortFont,  short  shortAlign,  short  shortBorder,  HSSFWorkbook  workbook){	
				// 1�B�Ыئr��A�]�m�䬰����B����G 
				HSSFFont  font  =  workbook.createFont( )  ; 
				font.setColor(shortColor) ; 
				font.setBoldweight(shortFont) ; 
				// 2�B�Ыخ榡 
				HSSFCellStyle  cellStyle  =  workbook.createCellStyle( ) ; 
				cellStyle.setFont(font) ; 
				cellStyle.setAlignment(shortAlign) ;
				// ��u
        cellStyle.setBorderBottom(shortBorder); //�U���    
        cellStyle.setBorderLeft(shortBorder);   //�����    
        cellStyle.setBorderRight(shortBorder);  //�k���    
        cellStyle.setBorderTop(shortBorder);    //�W���    
        //
        cellStyleG  =  cellStyle ;
	}
	public HSSFCellStyle setCellStyle(short  shortColor,  short  shortFont,  short  shortAlign,  short  shortBorder,  HSSFWorkbook  workbook){	
				// 1�B�Ыئr��A�]�m�䬰����B����G 
				HSSFFont  font  =  workbook.createFont( )  ; 
				font.setColor(shortColor) ; 
				font.setBoldweight(shortFont) ; 
				// 2�B�Ыخ榡 
				HSSFCellStyle  cellStyle  =  workbook.createCellStyle( ) ; 
				cellStyle.setFont(font) ; 
				cellStyle.setAlignment(shortAlign) ;
				// ��u
        cellStyle.setBorderBottom(shortBorder); //�U���    
        cellStyle.setBorderLeft(shortBorder);   //�����    
        cellStyle.setBorderRight(shortBorder);  //�k���    
        cellStyle.setBorderTop(shortBorder);    //�W���    
        //
        return  cellStyle ;
	}
	public void setDefaultBorder(short  shortBorder){	
				// ��u
        cellStyleG.setBorderBottom(shortBorder); //�U���    
        cellStyleG.setBorderLeft(shortBorder);   //�����    
        cellStyleG.setBorderRight(shortBorder);  //�k���    
        cellStyleG.setBorderTop(shortBorder);    //�W���    
	}
	public void setDefaultCellStyle(short  shortAlign,  HSSFCellStyle  cellStyle){	
				cellStyle.setAlignment(shortAlign) ;
				cellStyleG  =  cellStyle ;
	}
	public void setDefaultCellStyle(HSSFCellStyle  cellStyle){	
				cellStyleG  =  cellStyle ;
	}
	// intPosColumn  			Excel ����m(��)
	// intPosRow   			    Excel ����m(�C)
	//  objectSheet2   			Excel ���u�@��
	public void putDataIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutData, HSSFSheet  sheet){	
				HSSFCell         cell            =  null ;
				HSSFRow        row            =  null ;
				try {
						// ���
						row   =  sheet.createRow(intPosRow) ;
						cell  =  row.createCell((short)  intPosColumn) ;
						//
						cell.setEncoding( HSSFCell.ENCODING_UTF_16 );
						cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
						if(cellStyleG  !=  null)			cell.setCellStyle(cellStyleG); 
						//
						cell.setCellValue((String)   stringPutData );
				} catch (Exception  e) {
						System.out.println("---------------------"+e.toString()) ;
				}
	}
	// ���òĤ@�C���e�Q�ӧ@���榡�]�w
	public void putDataIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutData, String  stringField,  HSSFSheet  sheet){	
				try {
						// ���o���A
						HSSFCellStyle cellStyle     =  null ;
						HSSFCell         cell            =  null ;
						HSSFRow        row            =  null ;
						if(!"".equals(stringField)  &&  stringField.length()  ==  1) {
								int                   intFieldPos  =  (int)stringField.charAt(0)  -  65 ;
								if(intFieldPos  >=  0  &&  intFieldPos  <=  9)   {
										 row              =  sheet.getRow(0) ;
										 cell              =  row.getCell((short) intFieldPos) ;
										 cellStyle      =  cell.getCellStyle(); 
								} else {
										cellStyle  =  cellStyleG ;
								}
						} else {
								cellStyle  =  cellStyleG ;
								System.out.println("---------------------Default") ;
						}
						// ���
						row   =  sheet.createRow(intPosRow) ;
						cell  =  row.createCell((short)  intPosColumn) ;
						//
						cell.setEncoding( HSSFCell.ENCODING_UTF_16 );
						cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
						if(cellStyle  !=  null)			cell.setCellStyle(cellStyle); 
						//
						cell.setCellValue((String)   stringPutData );
				} catch (Exception  e) {
						System.out.println("---------------------"+e.toString()) ;
				}
	}
	public void putDataIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutData, HSSFCellStyle cellStyle,  HSSFSheet  sheet){	
				try {
						// ���o���A
						HSSFCell         cell            =  null ;
						HSSFRow        row            =  null ;
						// ���
						row   =  sheet.createRow(intPosRow) ;
						cell  =  row.createCell((short)  intPosColumn) ;
						//
						cell.setEncoding( HSSFCell.ENCODING_UTF_16 );
						cell.setCellType(HSSFCell.CELL_TYPE_STRING); 
						if(cellStyle  !=  null)			cell.setCellStyle(cellStyle); 
						//
						cell.setCellValue((String)   stringPutData );
				} catch (Exception  e) {
						System.out.println("---------------------"+e.toString()) ;
				}
	}
	 // intPosColumn  				Excel ����m(��)
	 // intPosRow   				Excel ����m(�C)
	 //  objectSheet2   			Excel ���u�@��
/*	public void putTypeIntoExcel(int  intPosColumn,  int  intPosRow,  String  stringPutType, Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch.put(Dispatch.invoke(objectSheet,  "Range",  Dispatch.Get,  new Object[] {stringTmp+ (intPosRow+1)},  new int[1]).toDispatch(),
					"NumberFormatLocal",
					stringPutType
					);
	}*/
	// intPosColumn  			Excel ����m(��)
	// intPosRow   			    Excel ����m(�C)
	//  objectSheet2   			Excel ���u�@��
	public String  getDataFromExcel(int  intPosColumn,  int  intPosRow,  HSSFSheet  sheet){	
			String  stringReturn  =  "" ;
			try {
					HSSFRow    row           =  sheet.getRow(intPosRow) ;
					HSSFCell   cell          =  row.getCell((short)  intPosColumn) ;
					           stringReturn  =  cell.getStringCellValue() ;
			} catch (Exception  e) {
					System.out.println("ERROR------------------"+e.toString());
					return  stringReturn ;		
			}
			if("null".equals(stringReturn))  stringReturn  =  "" ;
			return  stringReturn ;
	}
	// �M�����A��줧������|�Q�M��
/*	public void  doClearContents(String  stringRange,  Dispatch  objectSheet){	
		Dispatch objectRangeClear  =  Dispatch.invoke(objectSheet,  "Range",   Dispatch.Get,  new Object[] {stringRange},  new int[1]).toDispatch();
		Dispatch.call(objectRangeClear,  "ClearContents");
	}
	// �ƻs�@�����
	public void  doCopyColumns(int  intPosColumn,  Dispatch  objectSheet){	
		String  stringTmp  =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		Dispatch.call(objectRange, "Copy");
		Dispatch.call(objectRange, "Insert");
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
	// �R���@����
	public void  doDeleteColumns(int  intPosColumn,  Dispatch  objectSheet){	
		String    stringTmp    =  getExcelColumnName( "A",  intPosColumn) ;
		Dispatch  objectRange  =  Dispatch.invoke(objectSheet,  "Columns",  Dispatch.Get,  new Object[]{stringTmp  +  ":"  +  stringTmp}, new int[1]).toDispatch( ) ;
		//Dispatch.put(objectRange, "Delete",  new  varant(-4159));
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
	// �e�ؽu
	public void  doLineStyle(String  stringRange,  Dispatch  objectSheet1){	
		Dispatch objectRange    =  Dispatch.invoke(objectSheet1,  "Range",  Dispatch.Get,new Object[]{stringRange},  new int[1]).toDispatch( ) ;
		Dispatch objectBorders  =  null ;
		//
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
	// ����
	// �d�ҡGsetPageTitleName(objectSheet1,  "&14���q�W��\n�q��o��\n94�~11��25��ӳ�") ;
	public  void  setPageTitleName(Dispatch  objectSheet1,  String stringPageTitleName){
		objectSheet1  =  Dispatch.get(objectSheet1,  "PageSetup").toDispatch( );
		Dispatch.put(objectSheet1,  "CenterHeader",  stringPageTitleName) ;
	}
	// ���o�w�}�Ҥ� sheet �� �̤j�C��
	public  int  getExcelMaxRow(Dispatch  dispatchSheet1){
    Dispatch  dispatchUsedRange =  Dispatch.get(dispatchSheet1, "UsedRange").toDispatch();
		String[]  retArray          =  convert.StringToken(Dispatch.call(dispatchUsedRange, "Address").toString( ),  "$") ;
		int       intRowMax         =  doParseInteger(retArray[4].trim( )) ;
		int       intColMax         =  getExcelColumnNo(retArray[3].trim( )) ;   
		return  intRowMax ;
	}
	// ���o�w�}�Ҥ� sheet �� �̤j����
	public  int  getExcelMaxCol(Dispatch  dispatchSheet1){
    	Dispatch  dispatchUsedRange =  Dispatch.get(dispatchSheet1, "UsedRange").toDispatch();
		String[]  retArray          =  convert.StringToken(Dispatch.call(dispatchUsedRange, "Address").toString( ),  "$") ;
		int       intColMax         =  getExcelColumnNo(retArray[3].trim( )) ;   
		return  intColMax ;
	}*/
}
