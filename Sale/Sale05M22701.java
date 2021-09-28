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
				// �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
				// �^�ǭȬ� false ��ܱ��U�Ӥ����������O
				// �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
				if(!isBatchCheckOK(value )){
						return false;
				}
				return true;
		}

		public boolean isBatchCheckOK(String stringValue) throws Throwable{
				if("�s�W".equals(stringValue) || "�ק�".equals(stringValue)){
						String LastUser         = getUser().toUpperCase();
						String LastDateTime = datetime.getTime("YYYY/mm/dd h:m:s");
						setValue("LastUser",         LastUser);
						setValue("LastDateTime", LastDateTime);
						//
						String stringFlowFormID = getValue("FlowFormID");
						if(stringFlowFormID.length() == 0){
								message("[���ID] ���i���ť�!");
								getcLabel("FlowFormID").requestFocus();
								return false;
						}
						String stringFlowFormName = getValue("FlowFormName");
						if(stringFlowFormName.length() == 0){
								message("[���W��] ���i���ť�!");
								getcLabel("FlowFormName").requestFocus();
								return false;
						}
						// �����
						JTable jtable                      = getTable("table1");
						String  stringCensorSeq    = "";
						String  stringCensorName = "";
						String  stringDeptCd          = "";
						String  stringSignType       = ""; // �ק���:20100422 ���u�s��:B3774
						//
						if(jtable.getRowCount() == 0){
								message("�п�J����ơC");
								return false;
						}
						for(int intNo=0; intNo<jtable.getRowCount(); intNo++){
								stringCensorSeq    = (""+getValueAt("table1",  intNo,  "CensorSeq")).trim();
								stringCensorName = (""+getValueAt("table1",  intNo,  "CensorName")).trim();
								stringDeptCd          = (""+getValueAt("table1",  intNo,  "DeptCd")).trim();
								stringSignType       = (""+getValueAt("table1",  intNo,  "SignType")).trim(); // �ק���:20100422 ���u�s��:B3774
								// ñ���I�s��
								if(stringCensorSeq.length() == 0){
										message("�� "+(intNo+1)+" �C�� [ñ���I�s��] ���i���ť�!");
										setFocus("table1", intNo, "CensorSeq");
										return false;
								}
								// ñ���I�W��
								if(stringCensorName.length() == 0){
										message("�� "+(intNo+1)+" �C�� [ñ���I�W��] ���i���ť�!");
										setFocus("table1", intNo, "CensorName");
										return false;
								}
								// ñ�ֳ����N�X
								if(stringDeptCd.length() == 0){
										message("�� "+(intNo+1)+" �C�� [ñ�ֳ����N�X] ���i���ť�!");
										setFocus("table1", intNo, "DeptCd");
										return false;
								}
								// Start �ק���:20100422 ���u�s��:B3774
								// ñ������
								if(stringSignType.length() == 0){
										message("�� "+(intNo+1)+" �C�� [ñ������] ���i���ť�!");
										setFocus("table1", intNo, "SignType");
										return false;
								}
								// End �ק���:20100422 ���u�s��:B3774
								// ��ϥΪ̤ήɶ�
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
				return "---------------�s�W���s�{��.preProcess()----------------";
		}
}