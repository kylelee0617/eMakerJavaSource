package Sale.Sale05M090;

import javax.swing.JButton;

import jcx.db.talk;
import jcx.jform.bproc;

public class FormLoad extends bproc{
	public String getDefaultValue(String value)throws Throwable{

        /**
         * 20200617 Kyle : 
         * 1. 有收款單禁止刪除。
         * 2. 原建設有發票禁止修改客戶之姓名，增加禁止修改ID 、比例、禁止新增刪除
         * 3. 修正 : 客戶資料被禁改後，重新進入新增模式時會有同樣筆數無法編輯的問題。
         */


		//20191022
		setValue("NonAgent","1");
		getButton("ButtonSaleWay").doClick() ;  // 2012/09/17 B3018 新增
		getButton("button5").doClick() ;
		getButton("ButtonTable12ComNo").doClick() ; 	// table12 之業主別下拉式選單設定
		// Start 20080623 JackLee
		/*if("會計審核".equals(getValue("FlowStatus").trim())){
			setEditable("table3", false);
		}else{
			setEditable("table3", true);
		}*/
		if("購屋證明單 - 審核(Sale05M09002)".equals(getFunctionName())){
			String stringFlowStatus = getValue("FlowStatus");
			if("經辦".equals(stringFlowStatus)){
				setVisible("OK", true);
				setVisible("Reject", true);
				getButton("buttonMove").doClick();
			}else{
				setVisible("OK", false);
				setVisible("Reject", false);
			}
			// Form上欄位表格按鈕隱藏、顯示或禁改
			setEditable("field1", false);
			setEditable("field2", false);
			setEditable("field3", false);
			setEditable("field5", false);
			setVisible("FlowStatus", true);
			setEditable("FlowStatus", false);
			setEditable("LimitFlag", false);
			setEditable("table1", false);
			getTableButton("table1", 0).setEnabled(false);
			getTableButton("table1", 1).setEnabled(false);
			getTableButton("table1", 2).setEnabled(false);
			setEditable("table2", false);
			getTableButton("table2", 0).setEnabled(false);
			getTableButton("table2", 1).setEnabled(false);
			getTableButton("table2", 2).setEnabled(false);
			for(int i=1; i<=5; i++){
				setEditable("SaleID"+i, false);
				setEditable("SaleName"+i, false);
			}
			setEditable("MediaID", false);
			setEditable("MajorID", false);
			setEditable("ZoneID", false);
			setEditable("Remark", false);
			setEditable("MediaName", false);
			setEditable("MajorName", false);
			setEditable("ZoneName", false);
			setEditable("UseType", false);
			setEditable("table3", false);
			setEditable("table4", false);
			setVisible("button4", false);
			setVisible("button6", false);
			// ToolBar的要隱藏
			setVisible("DEL", false);
			setVisible("button3", false);
			setVisible("button1", false);
			setVisible("button2", false);
			return value;
		}
		// End
		setEditable("field3",false);
		setEditable("FlowStatus",false);
		setEditable("table2","HouseCar",false);
		setEditable("button2Count",false);

		//[棟樓資料之售價不能修改] 檢核
		// [刪除按鈕不能執行] 檢核
		talk            dbSale               =  getTalk(""  +  get("put_dbSale")) ;
		String      stringProjectID    =  getValue("field1").trim( ) ;
		String      stringPosition      =  "" ;
		String      stringSql             =  "" ;
		String      stringSqlDelete   =  "" ;
		String       stringOrderNo   =  getValue("field3").trim( ) ;
		String[][]  retTable             =  getTableData("table2") ;
		String[][]  retSale05M061  =  null ;
		String[][]  retSale05M087  =  null ;
		String[][]  retSale05M086  =  null ;
		boolean  booleanEnable  =  true ;
		for(int  intRow=0  ;  intRow<retTable.length  ;  intRow++) {
				//棟樓別
				//setEditable("table2",  intRow,  3,  false) ;
				//System.out.println("stringPosition----"+retTable[intRow][3]) ;
				stringPosition  =  retTable[intRow][3].trim( ) ;
				stringSqlDelete  =  "SELECT  ISNULL(SUM(H_ReceiveMoney  +  H_MomentaryMoney  +  L_ReceiveMoney   +  L_MomentaryMoney),0) ,ISNULL(SUM(H_MONEY+L_MONEY),0) "  +
									            " FROM  Sale05M061 " +
								              " WHERE  ProjectID1  =  '"  +  stringProjectID  +  "' "  +
								                   " AND  Position  =  '"  +  stringPosition  +  "' "  ;
				stringSql  = 	stringSqlDelete  +  " AND  ORDER_NO  =  -1 " ;
				retSale05M061  =  dbSale.queryFromPool(stringSql) ;
				System.out.println(intRow  +  "-----------"  +  stringPosition  +  "-----"  +  retSale05M061[0][0].trim( )) ;
				System.out.println(intRow  +  "-----------"  +  stringPosition  +  "-----"  +  retSale05M061[0][1].trim( )) ;
				if(Double.parseDouble(retSale05M061[0][1].trim( ))  ==  0) {
						setEditable("table2",  intRow,  6,  true) ;
				} else if (Double.parseDouble(retSale05M061[0][0].trim( )) < Double.parseDouble(retSale05M061[0][1].trim( ))){
					setEditable("table2",  intRow,  6,  true) ;
				}else{
					setEditable("table2",  intRow,  6,  false) ;
				}
				/*950515
				if(retSale05M061.length == 0  ||  "".equals(retSale05M061[0][0].trim( ))  ||  Double.parseDouble(retSale05M061[0][0].trim( )) == 0) {
						setEditable("table2",  intRow,  6,  true) ;
						//System.out.println(intRow  +  "-----------"  +  stringPosition  +  "-----"  +  retSale05M061[0][0].trim( )+"-----------true") ;
				} else {
						setEditable("table2",  intRow,  6,  false) ;
						//System.out.println(intRow  +  "-----------"  +  stringPosition  +  "-----"  +  retSale05M061[0][0].trim( )+"-----------false") ;
				}*/
				// 刪除
				/*if(booleanEnable) {
						retSale05M061  =  dbSale.queryFromPool(stringSqlDelete) ;
						if(retSale05M061.length  !=  0)
						if(retSale05M061.length != 0  &&  !"".equals(retSale05M061[0][0].trim( ))  &&  Double.parseDouble(retSale05M061[0][0].trim( )) > 0) {
								booleanEnable =  false ;
								System.out.println("刪除------"+  intRow  +  "-----------"  +  stringPosition  +  "-----"  +  retSale05M061[0][0].trim( )+"-----------false") ;
						}
				}*/
		}
		stringSql  =  "SELECT  DocNo  "  +
										" FROM  Sale05M086 " +							  
									  " WHERE  OrderNo  =  '"  +  stringOrderNo  +  "' "  ;				
		retSale05M086  =  dbSale.queryFromPool(stringSql) ;
		if(retSale05M086.length > 0) {
			booleanEnable =  false ;
			System.out.println("OrderNo已有收款:"+stringOrderNo);
		}
		//
        if(POSITION  !=  1) getButton(4).setEnabled(booleanEnable) ;
        
		// [客戶資料之 名字 ID 比例 不能修改] 檢核
		boolean booCustomEditable = true;
		String strCustomEditable = "1";
		String strCustomEditableMsg = "";
		if (POSITION == 4) {
			//查詢才需要處理
			//1. 客戶允許修改否?
			stringSql            =  "SELECT  * "  +
										" FROM  Sale05M087 "  +
										" WHERE  DOCNO  IN  (SELECT  DocNo  FROM  Sale05M086  WHERE  OrderNo  =  '"  +  stringOrderNo  +  "') " ;
			retSale05M087   =  dbSale.queryFromPool(stringSql) ;
			if(retSale05M087.length  >  0) {
				//已有發票則禁止修改
				booCustomEditable  =  false ;
                strCustomEditable = "0";
				strCustomEditableMsg = "已有開立發票禁止異動客戶";
			}

			//2. 定審
			JButton buyedInfo = getButton("BuyedInfo");
			buyedInfo.doClick();
		}
		System.out.println(">>>能否修改客戶資料>>>" + booCustomEditable);
		//統編、姓名、比例 的修改狀態
		setEditable("table1" , "CustomNo",booCustomEditable);
		setEditable("table1" , "CustomName",booCustomEditable);
		setEditable("table1" , "Percentage",booCustomEditable);
		getTableButton("table1" , 0).setEnabled(booCustomEditable) ;
		getTableButton("table1" , 1).setEnabled(booCustomEditable) ;
		getTableButton("table1" , 2).setEnabled(booCustomEditable) ;
		setValue("CustomID_NAME_PER_Editable" , strCustomEditable);
		setValue("CustomID_NAME_PER_EditableMsg" , strCustomEditableMsg);
		
		// 行銷策略 預設值
		String  stringSaleWay  =  getValue("SaleWay").trim() ;
		//
		put("Sale05M090Gift_OrderNo",   stringOrderNo) ;
		put("Sale05M090Gift_ProjectID",  stringProjectID) ; 
		// 頁籤載入
		getButton("ButtonTable8").doClick() ;     // 銷獎專用
		getButton("ButtonTable11").doClick() ;   // 面積
		getButton("ButtonTable9Old").doClick() ;   // 售出人
		//getButton("ButtonTable6").doClick() ;   // 受益人
		getButton("ButtonTable10").doClick() ;   // 代理人
		System.out.println("ButtonTable9Old------------------------------E") ;
		(new  Farglory.util.FargloryUtil()).doChangeTableField(getTable("table2"),  "底價",  0) ;
		//
		setVisible("ButtonTable11",                "B3018".equals(getUser())) ;
		setVisible("ButtonTrustAccountNo",  "B3018".equals(getUser())) ;
		//
		return value;
	}
	public String getInformation(){
		return "---------------().defaultValue()----------------";
	}
}
