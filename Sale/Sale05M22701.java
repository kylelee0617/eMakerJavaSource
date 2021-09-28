package Sale;
import     jcx.jform.bTransaction;
import     jcx.util.*;
import     jcx.db.*;
import     javax.swing.*;

public class Sale05M22701 extends bTransaction{
		talk  dbFE3D  =  getTalk(""+get("put_dbFE3D"));
		talk  dbSale    =  getTalk(""+get("put_dbSale"));
		talk  dbFED1  =  getTalk(""+get("put_dbFED1"));
		
		public boolean action(String value) throws Throwable{
				// 回傳值為 true 表示執行接下來的資料庫異動或查詢
				// 回傳值為 false 表示接下來不執行任何指令
				// 傳入值 value 為 "新增","查詢","修改","刪除","列印","PRINT" (列印預覽的列印按鈕),"PRINTALL" (列印預覽的全部列印按鈕) 其中之一
				if(!isBatchCheckOK(value )){
						return false;
				}
				return true;
		}

		public boolean isBatchCheckOK(String stringValue) throws Throwable{
				if("新增".equals(stringValue) || "修改".equals(stringValue)){
						String LastUser         = getUser().toUpperCase();
						String LastDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
						setValue("LastUser",         LastUser);
						setValue("LastDateTime", LastDateTime);
						//
						String stringFlowFormID = getValue("FlowFormID");
						if(stringFlowFormID.length() == 0){
								message("[表單ID] 不可為空白!");
								getcLabel("FlowFormID").requestFocus();
								return false;
						}
						String stringFlowFormName = getValue("FlowFormName");
						if(stringFlowFormName.length() == 0){
								message("[表單名稱] 不可為空白!");
								getcLabel("FlowFormName").requestFocus();
								return false;
						}
						// 表格資料
						JTable jtable                      = getTable("table1");
						String  stringCensorSeq    = "";
						String  stringCensorName = "";
						String  stringDeptCd          = "";
						String  stringSignType       = ""; // 修改日期:20100422 員工編號:B3774
						//
						if(jtable.getRowCount() == 0){
								message("請輸入表格資料。");
								return false;
						}
						for(int intNo=0; intNo<jtable.getRowCount(); intNo++){
								stringCensorSeq    = (""+getValueAt("table1",  intNo,  "CensorSeq")).trim();
								stringCensorName = (""+getValueAt("table1",  intNo,  "CensorName")).trim();
								stringDeptCd          = (""+getValueAt("table1",  intNo,  "DeptCd")).trim();
								stringSignType       = (""+getValueAt("table1",  intNo,  "SignType")).trim(); // 修改日期:20100422 員工編號:B3774
								// 簽核點編號
								if(stringCensorSeq.length() == 0){
										message("第 "+(intNo+1)+" 列之 [簽核點編號] 不可為空白!");
										setFocus("table1", intNo, "CensorSeq");
										return false;
								}
								// 簽核點名稱
								if(stringCensorName.length() == 0){
										message("第 "+(intNo+1)+" 列之 [簽核點名稱] 不可為空白!");
										setFocus("table1", intNo, "CensorName");
										return false;
								}
								// 簽核部門代碼
								if(stringDeptCd.length() == 0){
										message("第 "+(intNo+1)+" 列之 [簽核部門代碼] 不可為空白!");
										setFocus("table1", intNo, "DeptCd");
										return false;
								}
								// Start 修改日期:20100422 員工編號:B3774
								// 簽核類型
								if(stringSignType.length() == 0){
										message("第 "+(intNo+1)+" 列之 [簽核類型] 不可為空白!");
										setFocus("table1", intNo, "SignType");
										return false;
								}
								// End 修改日期:20100422 員工編號:B3774
								// 塞使用者及時間
								setValueAt("table1",  LastUser,  intNo,  "LastUser");
								setValueAt("table1",  LastDateTime,  intNo,  "LastDateTime");
						}
				}
				
				return true;
		}

		public String getEmpName(String stringEmpNo) throws Throwable{
				String     stringSql            = "";
				String     stringEmpName = "";
				String[][] retFE3D05         = null;
				//
				stringSql = "SELECT EMP_NAME "+
								  "FROM FE3D05 "+
								  "WHERE EMP_NO='"+stringEmpNo+"'";
				retFE3D05 = dbFE3D.queryFromPool(stringSql);
				if(retFE3D05.length != 0){
						stringEmpName  = retFE3D05[0][0].trim( );
				}
				return stringEmpName;
		}
		
		public String getUserName(String stringID) throws Throwable{
				String     stringSql            = "";
				String     stringUserName = "";
				String[][] retUSERS          = null;
				//
				stringSql = "SELECT NAME "+
								  "FROM USERS "+
								  "WHERE ID='"+stringID+"'";
				retUSERS = dbSale.queryFromPool(stringSql);
				if(retUSERS.length != 0){
						stringUserName = retUSERS[0][0].trim();
				}
				return stringUserName;
		}
		
		public String getCompanyName(String stringCompanyCd) throws Throwable{
				String     stringSql                    = "";
				String     stringCompanyName = "";
				String[][] retFED1023               = null;
				stringSql = "SELECT COMPANY_NAME "+
								  "FROM FED1023 "+
								  "WHERE COMPANY_CD='"+stringCompanyCd+"'";
				retFED1023 = dbFED1.queryFromPool(stringSql);
				if(retFED1023.length > 0){
						stringCompanyName = retFED1023[0][0].trim();
				}
				return stringCompanyName;
		}

		public String getInformation(){
				return "---------------新增按鈕程式.preProcess()----------------";
		}
}