package Sale.AML;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import jcx.db.talk;
import jcx.jform.bproc;

public class CheckAML_Si extends bproc{
	public String getDefaultValue(String value)throws Throwable{
		//20191107 �~���θꮣ���I�޲z�F���B�z�{�ǧ@�~+����~���Υ����ꮣ���I�����B�z�{�ǧ@�~+�~���θꮣ�W����B�z�{�ǧ@�~
		System.out.println("===========AML============S");
		talk  dbSale =  getTalk("Sale") ;
		talk  db400CRM =  getTalk("400CRM") ;
		talk  dbPW0D =  getTalk("pw0d") ;
		talk  dbJGENLIB  =  getTalk("JGENLIB") ;
		talk  dbEIP  =  getTalk("EIP") ;
		String strSaleSql = "";
		String str400CRMSql = "";
		String strPW0DSql = "";
		String strJGENLIBSql = "";
		String strEIPSql = "";
		String strBDaysql = "";
		String str400sql = "";
		String stringSQL = "";
		String strPW0Dsql = "";
		String[][]   ret080Table;//�{��
		String[][]   ret083Table;//�H�Υd
		String[][]   ret328Table;//�Ȧ�
		String[][]   ret082Table;//����
		String[][]  ret070Table;
		String[][] retPDCZPFTable;
		String[][] retQueryLog;
		String[][] retCList;
		boolean Pattern1Show = false;
		boolean Pattern2Show = false;
		boolean Pattern3Show = false;
		boolean Pattern4Show = false;
		//���e����
		String strActionName =  getValue("actionName").trim() ;//�@�ʦW��
		String strCreditCardMoney  =  getValue("CreditCardMoney").trim() ;//�H�Υd
		String strCashMoney  =  getValue("CashMoney").trim() ;//�{��
		String strBankMoney  =  getValue("BankMoney").trim() ;//�Ȧ�
		String strCheckMoney  =  getValue("CheckMoney").trim() ;//����
		String strReceiveMoney = getValue("ReceiveMoney").trim() ;//�����`�B
		String strProjectID1 =  getValue("field2").trim() ;//�קO�N�X
		String strEDate =  getValue("field3").trim() ;//���ڤ��
		String strDocNo =  getValue("field4").trim() ;//�s��
		if("".equals(strCreditCardMoney)){
			strCreditCardMoney = "0";
		}
		if("".equals(strCashMoney)||"0.0".equals(strCashMoney)){
			strCashMoney = "0";
		}
		if("".equals(strBankMoney)||"0.0".equals(strBankMoney)){
			strBankMoney = "0";
		}
		if("".equals(strCheckMoney)){
			strCheckMoney = "0";
		}
		//�Nú�H����
		String strDeputy=getValue("PaymentDeputy").trim();
		String strDeputyName = getValue("DeputyName").trim();
		String strDeputyID=getValue("DeputyID").trim();
		String strDeputyRelationship = getValue("DeputyRelationship").trim();
		String bStatus=getValue("B_STATUS").trim();
		String cStatus=getValue("C_STATUS").trim();
		String rStatus=getValue("R_STATUS").trim();
		//�ʶR�H�m�W
		String allOrderID = "";
		String allOrderName = "";
		String[][] orderCustomTable =  getTableData("table3");
		for (int g = 0; g < orderCustomTable.length; g++) {
			if("".equals(allOrderName)){
				allOrderID =  orderCustomTable[g][3].trim();
				allOrderName =  orderCustomTable[g][4].trim();
			}else{
				allOrderID = allOrderID+"�B"+ orderCustomTable[g][3].trim();
				allOrderName = allOrderName+"�B"+ orderCustomTable[g][4].trim();
			}
		}
		//13,14
		String rule13=getValue("Rule13").trim();
		String rule14=getValue("Rule14").trim();
		//�@��
		String errMsg="";
		String allCustomName = allOrderName;
		String allCustomID = allOrderID;
		//���ڤ������榡
		String[] tempEDate = strEDate.split("/");
		String rocDate = "";
		String year = tempEDate[0];
		int intYear = Integer.parseInt(year) - 1911;
		rocDate = Integer.toString(intYear)+ tempEDate[1]+ tempEDate[2];
		//LOG NOW DATE
		Date now = new Date();
		SimpleDateFormat nowsdf = new SimpleDateFormat("yyyyMMdd");
		String strNowDate = nowsdf.format(now);
		String tempROCYear=""+(Integer.parseInt(strNowDate.substring(0,strNowDate.length()-4))-1911);
		String RocNowDate = tempROCYear+strNowDate.substring(strNowDate.length()-4,strNowDate.length());
		SimpleDateFormat nowTimeSdf = new SimpleDateFormat("HHmmss");
		String strNowTime = nowTimeSdf.format(now);
		SimpleDateFormat nowTimestampSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strNowTimestamp =  nowTimestampSdf.format(now);
		//���s
		String userNo = getUser().toUpperCase().trim();
		String empNo="";
		String [][] retEip=null;
		strEIPSql="SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + userNo + "'" ;
		retEip = dbEIP.queryFromPool(strEIPSql);
		if(retEip.length>0){
			empNo=retEip[0][0] ;
		}
		//�ʪ��ҩ��渹
		String strOrderNo = "";
		String[][] orderNoTable =  getTableData("table4");
		strOrderNo=orderNoTable[0][2].trim();
		//�~���l�ܬy����
		int intRecordNo =1;
		strSaleSql = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='"+strOrderNo+"'";
		ret070Table = dbSale.queryFromPool(strSaleSql);
		if(!"".equals(ret070Table[0][0].trim())){
			intRecordNo = Integer.parseInt(ret070Table[0][0].trim())+1;
		}
		//actionNo
		String ram = "";
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			ram += String.valueOf(random.nextInt(10));
		}
		String actionNo =strNowDate+ strNowTime+ram;
		//Pattern �˺A
		//1�P�@�Ȥ�P�@��~�餺2��(�t)�H�W�]�t�{���B�״ڡB�H�Υd�B�䲼����A�B�C���Ҥ���s�x��450,000~499,999���A�t���ˮֹwĵ�C
		//3�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C
		//2�P�@�Ȥ�3����~�餺�A��2��H�{���ζ״ڹF450,000~499,999��, �t���ˮִ��ܳq���C
		//4�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Set amlMsg = new TreeSet();
		//���i���ڳ檺�Ҧ��Ȥ�
		String[][] orderTable3 =  getTableData("table3");
		for (int g = 0; g < orderTable3.length; g++) {//���檺�Ҧ��H
			String strOrderID= orderTable3[g][3].trim();//������
			String strOrderName= orderTable3[g][4].trim();//�m�W
			System.out.println("ID=====>"+strOrderID) ;
			System.out.println("�m�W=====>"+strOrderName) ;
			//�@�P
			//�Ʀr�榡
			DecimalFormat decimalFormat = new DecimalFormat("##.00");//�|�B���J�p�ƫ���
			//���檺���ڤ�(���@�w�O���骺���)
			String sameDay = strDocNo.substring(strDocNo.length()-10, strDocNo.length()-3);
			//�p���L��
			SimpleDateFormat EDateSDF = new SimpleDateFormat("yyyy/MM/dd");
			Calendar calar1 = Calendar.getInstance();
			Calendar calar2 = Calendar.getInstance();
			Date eDate = EDateSDF.parse(strEDate);
			calar1.setTime(eDate);
			calar2.setTime(eDate);
			calar1.add(Calendar.DAY_OF_MONTH, -1); 
			calar2.add(Calendar.DAY_OF_MONTH, -2); 
			String strcalar1 =  new SimpleDateFormat("yyyyMMdd").format(calar1.getTime());
			String strcalar2 =  new SimpleDateFormat("yyyyMMdd").format(calar2.getTime());
			//ROC FORMAT
			strcalar1 = Integer.toString( (Integer.parseInt(strcalar1.substring(0,4)) -1911)) + strcalar1.substring(4,8) ;
			strcalar2 =  Integer.toString( (Integer.parseInt(strcalar2.substring(0,4)) -1911))+ strcalar2.substring(4,8) ;
		    
			//���H����Ҧ���
			//�H��p��
			int p1AllCount = 0;//����s�x��450,000~499,999������
			double p3AllAddup = 0;//���B�[�`
			double m328AllAddup = 0;//�״ڥ[�`
			boolean p2AllToday = false; //�T�餺������O�_����45~50����
			
			
			//�H�H�p��
			int p1Count = 0;//����s�x��450,000~499,999������(�ӤH)
			double p3Addup = 0;//���B�[�`(�ӤH)
		    double m328Addup = 0;//�״ڥ[�`(�ӤH)
			boolean p2Today = false;// //�T�餺������O�_����45~50����(�ӤH)
			
			
			
			String[][] retSameDay =null;
			String sqlSameDay = "SELECT DocNo,Percentage FROM Sale05M084 WHERE CUSTOMNO = '"+strOrderID+"' AND DOCNO LIKE '%"+sameDay+"%'";
			retSameDay = dbSale.queryFromPool(sqlSameDay);
			System.out.println("���Ѧ�"+retSameDay.length+"�i��") ;
			for (int m = 0; m < retSameDay.length; m++) {//���Ѫ��C�i���ڳ�
				String strSSDocNo = retSameDay[m][0].trim();     //  ���ڳ渹
				String strSSPercent =  retSameDay[m][1].trim();    // �ʤ���
				System.out.println("�渹=====>"+strSSDocNo) ;
				System.out.println("�ʤ���=====>"+strSSPercent) ;
				//�h�i�ʫ��ҩ���n�ӤH�ƺ�
				String[][] retCountOrder =null;
				String sqlCountOrder  = "SELECT * FROM Sale05M086 WHERE DOCNO = '"+strSSDocNo+"'";
				retCountOrder = dbSale.queryFromPool(sqlCountOrder);
				int OrderBase = retCountOrder.length ;
				
				//�H�Υd 083
				String[][] retCCM = null;
				String sqlCCM= "SELECT CreditCardMoney FROM Sale05M083 where DocNo = '"+strSSDocNo+"'";
				retCCM = dbSale.queryFromPool(sqlCCM);
				for (int n = 0; n < retCCM.length; n++) {
					double origCCM = Double.parseDouble(retCCM[n][0].trim());     //�H�Υd���B
					origCCM = Double.parseDouble(decimalFormat.format( origCCM/OrderBase)); //�ӤH��           
					double dubCCMP  =  origCCM*(Double.parseDouble(strSSPercent)/100)  ;   //�Ӧʤ���
					if(origCCM>449999 && origCCM<500000){
						p1AllCount++;
					}
					if(dubCCMP>449999 && dubCCMP<500000){
						p1Count++;
					}
				}
				//�{�� 080
				String[][] ret80CM = null;
				String sql80CM= "SELECT CashMoney FROM Sale05M080 where DocNo = '"+strSSDocNo+"'";
				ret80CM = dbSale.queryFromPool(sql80CM);
				for (int o = 0; o < ret80CM.length; o++) {
					double origCM = Double.parseDouble(ret80CM[o][0].trim()); //�{�����B
					origCM = Double.parseDouble(decimalFormat.format( origCM/OrderBase));//�ӤH��
					double dub80CMP  = origCM*(Double.parseDouble(strSSPercent)/100);//�Ӧʤ���
					if(origCM>449999 && origCM<500000){
						p1AllCount++;
					}
					if(dub80CMP>449999 && dub80CMP<500000){
						p1Count++;
					}
					//�{���֥[
					p3AllAddup+=origCM;
					p3Addup+=dub80CMP;
				}
				//�Ȧ� 328
				String[][] ret328BM = null;
				String sql328BM= "SELECT BankMoney FROM Sale05M328 where DocNo = '"+strSSDocNo+"'";
				ret328BM = dbSale.queryFromPool(sql328BM);
				for (int p = 0; p < ret328BM.length; p++) {
					double origBM = Double.parseDouble(ret328BM[p][0].trim());
					origBM = Double.parseDouble(decimalFormat.format( origBM/OrderBase));
					double dub328BMP  = origBM*(Double.parseDouble(strSSPercent)/100)  ;
					if(origBM>449999 && origBM<500000){
						p1AllCount++;
					}
					if(dub328BMP>449999 && dub328BMP<500000){
						p1Count++;
					}  
					//�״ڲ֥[
					m328AllAddup+=origBM;
					m328Addup+=dub328BMP;
				}
				//���� 082
				String[][] ret082CM = null;
				String sql82CM= "SELECT CheckMoney FROM Sale05M082 where DocNo = '"+strSSDocNo+"'";
				ret082CM = dbSale.queryFromPool(sql82CM);
				for (int q = 0; q < ret082CM.length; q++) {
					double origCM = Double.parseDouble(ret082CM[q][0].trim());
					origCM = Double.parseDouble(decimalFormat.format( origCM/OrderBase));
					double dub82CMP  =  origCM*(Double.parseDouble(strSSPercent)/100)  ;
					if(origCM>449999 && origCM<500000){
						p1AllCount++;
					}
					if(dub82CMP>449999 && dub82CMP<500000){
						p1Count++;
					} 
				}
			} //���Ѫ��C�i���ڳ� 
			//TEST MSG
			System.out.println("���H����֭p") ;
			System.out.println("����45��50�U����=====>"+p1AllCount) ;
			System.out.println("����45��50�U����(�ӤH)=====>"+p1Count) ;
			System.out.println("�{���[�`=====>"+p3AllAddup) ;
			System.out.println("�{���[�`(�ӤH)=====>"+p3Addup) ;
			System.out.println("�״ڥ[�`=====>"+m328AllAddup) ;
			System.out.println("�״ڥ[�`(�ӤH)=====>"+m328Addup) ;
			
			
			//����O�_����45�U��50�U����
			//�{��
			if(p3AllAddup>449999 && p3AllAddup<500000){
				p2AllToday =true;
			}
			if(p3Addup>449999 && p3Addup<500000){
				p2Today =true;
			}
			//�״�
			if(m328AllAddup>449999 && m328AllAddup<500000){
				p2AllToday =true;
			}
			if(m328Addup>449999 && m328Addup<500000){
				p2Today =true;
			}
			System.out.println("���馬�ڳ�{���ζ״ڲ֭p�F45~50����===>>"+p2AllToday);
			System.out.println("���馬�ڳ�{���ζ״ڲ֭p�F45~50����(�ӤH)===>>"+p2Today);
			//�e�@��
			System.out.println("�U�Ȥ�e�@��======="+strcalar1+"=================");
			//���H�e�@��Ҧ���
			//�H��p��
			double previousDayAllAddup = 0;//�e�@��{���[�`
			double previousDaym328AllAddup = 0;//�e�@��״ڥ[�`
			boolean p2AllPreviousDay = false; //�T�餺���e�@��O�_����45~50����
			//�H�H�p��
			double previousDayAddup = 0;//�e�@��{���[�`(�ӤH)
		    double previousDaym328Addup = 0;//�e�@��״ڥ[�`(�ӤH)
			boolean p2PreviousDay = false;// //�T�餺������e�@�餶��45~50����(�ӤH)
			
			String[][] retADayBefore84Tab = null;
			String sqlADayBefore = "SELECT  DocNo,Percentage FROM Sale05M084 WHERE CUSTOMNO = '"+strOrderID+"'  AND DOCNO LIKE '%"+strcalar1+"%'";
			retADayBefore84Tab = dbSale.queryFromPool(sqlADayBefore);
			System.out.println("�e�@�馳"+retADayBefore84Tab.length+"�i��") ;
			for (int r = 0; r < retADayBefore84Tab.length; r++) { //�e�@�骺�C�i���ڳ� 
				String strADayBeforeDocNo = retADayBefore84Tab[r][0].trim();
				String strADayBeforePercent =  retADayBefore84Tab[r][1].trim();
				System.out.println("�e�@��渹=====>"+strADayBeforeDocNo) ;
				System.out.println("�e�@��ʤ���=====>"+strADayBeforePercent) ;
				//�h�i�ʫ��ҩ���n�ӤH�ƺ�
				String[][] retCountOrder =null;
				String sqlCountOrder  = "SELECT * FROM Sale05M086 WHERE DOCNO = '"+strADayBeforeDocNo+"'";
				retCountOrder = dbSale.queryFromPool(sqlCountOrder);
				int OrderBase =retCountOrder.length ;
				//�{��
				String [][] retADB80Tab = null;
				String sqlADB80 = "SELECT CashMoney FROM Sale05M080 WHERE DocNo = '"+strADayBeforeDocNo+"'";
				retADB80Tab  =  dbSale.queryFromPool(sqlADB80);
				for (int ra = 0; ra < retADB80Tab.length; ra++) {
					double tempCashMoney = Double.parseDouble(retADB80Tab[ra][0].trim());
					tempCashMoney = Double.parseDouble(decimalFormat.format( tempCashMoney/OrderBase));
					double dubADBCMP  =  tempCashMoney*(Double.parseDouble(strADayBeforePercent)/100)  ;
					previousDayAllAddup+=tempCashMoney;
					previousDayAddup+=dubADBCMP;
				}
				//�Ȧ�(�|���h��)
				String [][] retADB328Tab = null;
				String sqlADB328 = "SELECT SUM(BankMoney) FROM Sale05M328 WHERE DocNo = '"+strADayBeforeDocNo+"'";
				retADB328Tab  =  dbSale.queryFromPool(sqlADB328);
				for (int rb = 0; rb < retADB328Tab.length; rb++) {
					String bankMoney = retADB328Tab[rb][0].trim(); 
					if("".equals(bankMoney)){
						bankMoney = "0";
					}
					double tempBankMoney = Double.parseDouble(bankMoney);
					tempBankMoney = Double.parseDouble(decimalFormat.format( tempBankMoney/OrderBase));
					double dubADBBMP  =  tempBankMoney*(Double.parseDouble(strADayBeforePercent)/100)  ;
					previousDaym328AllAddup+=tempBankMoney;
					previousDaym328Addup+=dubADBBMP;
				}
			}//�e�@�骺�C�i���ڳ� 
			System.out.println("���H�e�@��֭p") ;
			System.out.println("�{���[�`=====>"+previousDayAllAddup) ;
			System.out.println("�{���[�`(�ӤH)=====>"+previousDayAddup) ;
			System.out.println("�״ڥ[�`=====>"+previousDaym328AllAddup) ;
			System.out.println("�״ڥ[�`(�ӤH)=====>"+previousDaym328Addup) ;
			//�e�@��O�_����45�U��50�U����
			//�{��
			if(previousDayAllAddup>449999 && previousDayAllAddup<500000){
				p2AllPreviousDay =true;
			}
			if(previousDayAddup>449999 && previousDayAddup<500000){
				p2PreviousDay =true;
			}
			//�״�
			if(previousDaym328AllAddup>449999 && previousDaym328AllAddup<500000){
				p2AllPreviousDay =true;
			}
			if(previousDaym328Addup>449999 && previousDaym328Addup<500000){
				p2PreviousDay =true;
			}
			System.out.println("�e�@�馬�ڳ�{���ζ״ڲ֭p�F45~50����===>>"+p2AllPreviousDay);
			System.out.println("�e�@�馬�ڳ�{���ζ״ڲ֭p�F45~50����(�ӤH)===>>"+p2PreviousDay);
			//�e�G��
			System.out.println("�U�Ȥ�e�G��======="+strcalar2+"=================");
			//���H�e�G��Ҧ���
			//�H��p��twoDaysAgo
			double twoDaysAgoAllAddup = 0;//�e�G��{���[�`
			double twoDaysAgom328AllAddup = 0;//�e�G��״ڥ[�`
			boolean p2AlltwoDaysAgo = false; //�T�餺���e�G��O�_����45~50����
			//�H�H�p��
			double twoDaysAgoAddup = 0;//�e�G��{���[�`(�ӤH)
		    double twoDaysAgom328Addup = 0;//�e�G��״ڥ[�`(�ӤH)
			boolean p2twoDaysAgo = false;// //�T�餺������e�G�餶��45~50����(�ӤH)
			
			String[][] retTwoDayBefore84Tab = null;
			String sqlTwoDayBefore = "SELECT  DocNo,Percentage FROM Sale05M084 WHERE CUSTOMNO = '"+strOrderID+"'  AND DOCNO LIKE '%"+strcalar2+"%'";
			retTwoDayBefore84Tab = dbSale.queryFromPool(sqlTwoDayBefore);
			System.out.println("�e�G�馳"+retTwoDayBefore84Tab.length+"�i��") ;
			for (int s = 0; s < retTwoDayBefore84Tab.length; s++) {//�e�G�骺�C�i���ڳ� 
				String strTwoDayBeforeDocNo = retTwoDayBefore84Tab[s][0].trim();
				String strTwoDayBeforePercent =  retTwoDayBefore84Tab[s][1].trim();
				System.out.println("�e�G��渹=====>"+strTwoDayBeforeDocNo) ;
				System.out.println("�e�G��ʤ���=====>"+strTwoDayBeforePercent) ;
				//�h�i�ʫ��ҩ���n�ӤH�ƺ�
				String[][] retCountOrder =null;
				String sqlCountOrder  = "SELECT * FROM Sale05M086 WHERE DOCNO = '"+strTwoDayBeforeDocNo+"'";
				retCountOrder = dbSale.queryFromPool(sqlCountOrder);
				int OrderBase =retCountOrder.length ;
				//System.out.println("�X�i�e�G���ʫ��ҩ���====>"+OrderBase) ;
				//�{��
				String [][] retTDB80Tab = null;
				String sqlTDB80 = "SELECT CashMoney FROM Sale05M080 WHERE DocNo = '"+strTwoDayBeforeDocNo+"'";
				retTDB80Tab  =  dbSale.queryFromPool(sqlTDB80);
				for (int sa = 0; sa < retTDB80Tab.length; sa++) {
					String cashMoney = retTDB80Tab[sa][0].trim();
					double tempCashMoney = Double.parseDouble(cashMoney);
					tempCashMoney = Double.parseDouble(decimalFormat.format( tempCashMoney/OrderBase));
					double dubTDBCMP  =  tempCashMoney*(Double.parseDouble(strTwoDayBeforePercent)/100)  ;
					twoDaysAgoAllAddup +=tempCashMoney;
					twoDaysAgoAddup +=dubTDBCMP;
				}
				//�Ȧ�
				String [][] retTDB328Tab = null;
				String sqlTDB328 = "SELECT SUM(BankMoney) FROM Sale05M328 WHERE DocNo = '"+strTwoDayBeforeDocNo+"'";
				retTDB328Tab  =  dbSale.queryFromPool(sqlTDB328);
				for (int sb = 0; sb < retTDB328Tab.length; sb++) {
					String bankMoney = retTDB328Tab[sb][0].trim(); 
					if("".equals(bankMoney)){
						bankMoney = "0";
					}
					double tempBankMoney = Double.parseDouble(bankMoney);
					tempBankMoney = Double.parseDouble(decimalFormat.format( tempBankMoney/OrderBase));
					double dubTDBBMP  =  tempBankMoney*(Double.parseDouble(strTwoDayBeforePercent)/100)  ;
					twoDaysAgom328AllAddup+=tempBankMoney;
					twoDaysAgom328Addup+=dubTDBBMP;
				}
			}//�e�G�骺�C�i���ڳ� 
			System.out.println("���H�e�G��֭p") ;
			System.out.println("�{���[�`=====>"+twoDaysAgoAllAddup) ;
			System.out.println("�{���[�`(�ӤH)=====>"+twoDaysAgoAddup) ;
			System.out.println("�״ڥ[�`=====>"+twoDaysAgom328AllAddup) ;
			System.out.println("�״ڥ[�`(�ӤH)=====>"+twoDaysAgom328Addup) ;
			//�e�G��O�_����45�U��50�U����
			//�{��
			if(twoDaysAgoAllAddup>449999 && twoDaysAgoAllAddup<500000){
				p2AlltwoDaysAgo =true;
			}
			if(twoDaysAgoAddup>449999 && twoDaysAgoAddup<500000){
				p2twoDaysAgo =true;
			}
			//�״�
			if(twoDaysAgom328AllAddup>449999 && twoDaysAgom328AllAddup<500000){
				p2AlltwoDaysAgo =true;
			}
			if(twoDaysAgom328Addup>449999 && twoDaysAgom328Addup<500000){
				p2twoDaysAgo =true;
			}
			System.out.println("�e�G�馬�ڳ�{���ζ״ڲ֭p�F45~50����===>>"+p2AlltwoDaysAgo);
			System.out.println("�e�G�馬�ڳ�{���ζ״ڲ֭p�F45~50����(�ӤH)===>>"+p2twoDaysAgo);
			//�Ӱ��޿�
			//�YAML003�A�˲ŦX�B�wĵ��A�M��AML004���֭p�Ѽ�
			int p2AllCount = 0;
			double p4AllAddup = 0;
			int p2Count = 0;
			double p4Addup = 0;
			
			//�e�G��
			if(twoDaysAgoAllAddup>=500000){
				p2AllCount = 0;
				p4AllAddup = 0;
				System.out.println("���H�e�G��{���֭p�W�L���Q�U�A�p���k�s�G"+twoDaysAgoAllAddup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}else{
				if(p2AlltwoDaysAgo){
					p2AllCount++;
				}
				p4AllAddup+=twoDaysAgoAllAddup;
				System.out.println("���H�e�G��{���֭p���W�L���Q�U") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}
			if(twoDaysAgoAddup>=500000){
				p2Count = 0;
				p4Addup = 0;
				System.out.println("���H�e�G��{���֭p�W�L���Q�U�A�p���k�s(�ӤH)�G"+twoDaysAgoAddup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}else{
				if(p2twoDaysAgo){
					p2Count++;
				}
				p4Addup+=twoDaysAgoAddup;
				System.out.println("���H�e�G��{���֭p���W�L���Q�U(�ӤH)") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}
			//�e�@��
			if(previousDayAllAddup>=500000){
				p2AllCount = 0;
				p4AllAddup = 0;
				System.out.println("���H�e�@��{���֭p�W�L���Q�U�A�p���k�s:"+previousDayAllAddup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}else{
				if(p2AllPreviousDay){
					p2AllCount++;
				}
				p4AllAddup+=previousDayAllAddup;
				System.out.println("���H�e�@��{���֭p���W�L���Q�U") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}
			//�T��֭p�W�L���Q�]�n�k�s
			if(p4AllAddup>=500000){
				p2AllCount = 0;
				p4AllAddup = 0;
			}
			
			if(previousDayAddup>=500000){
				p2Count = 0;
				p4Addup = 0;
				System.out.println("���H�e�@��{���֭p�W�L���Q�U�A�p���k�s(�ӤH):"+previousDayAddup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}else{
				if(p2PreviousDay){
					p2Count++;
				}
				p4Addup+=previousDayAddup;
				System.out.println("���H�e�@��{���֭p���W�L���Q�U(�ӤH)") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}
			//�T��֭p�W�L���Q�]�n�k�s(�ӤH)
			if(p4Addup>=500000){
				p2Count = 0;
				p4Addup = 0;
			}
			
			//����
			if(p3AllAddup>=500000){
				p2AllCount = 0;
				p4AllAddup = 0;	
				System.out.println("���H����{���֭p�W�L���Q�U�A�p���k�s:"+p3AllAddup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}else{
				if(p2AllToday){
					p2AllCount++;
				}
				p4AllAddup+=p3AllAddup;
				System.out.println("���H����{���֭p���W�L���Q�U") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50�������ơG"+p2AllCount) ;
				System.out.println("�T�餺�{���֭p�G"+p4AllAddup) ;
			}
			//����L�{���T���ˮ֤��θ�
			if(p3AllAddup == 0){
				p4AllAddup = 0;	
			}
			//����{���״ڵL45~50�������θ�
			if(!p2AllToday){
				p2AllCount = 0;
			}
			
			if(p3Addup>=500000){
				p2Count = 0;
				p4Addup = 0;
				System.out.println("���H����{���֭p�W�L���Q�U�A�p���k�s(�ӤH):"+p3Addup) ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}else{
				if(p2Today){
					p2Count++;
				}
				p4Addup+=p3Addup;//�e�G���k�s�u�⤵��
				System.out.println("���H����{���֭p���W�L���Q�U(�ӤH)") ;
				System.out.println("�T�餺�{���B�״ڲ֭p����45~50��������(�ӤH)�G"+p2Count) ;
				System.out.println("�T�餺�{���֭p(�ӤH)�G"+p4Addup) ;
			}
			//����L�{���T���ˮ֤��θ�
			if(p3Addup == 0){
				p4Addup = 0;	
			}
			//����{���״ڵL45~50�������θ�
			if(!p2Today){
				p2Count = 0;
			}
			
			//ALL
			//�˺A1
			if(p1AllCount>=2){
				//Sale05M070
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','001','�Ȥ�"+allOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//AS400
				strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '001', '�Ȥ�"+allOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbJGENLIB.execFromPool(strJGENLIBSql);
				amlMsg.add("�Ȥ�"+allOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C");
			}else{
				//���ŦX
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','001','�Ȥ�"+allOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			}
			//�˺A3
			if(p3AllAddup>499999){
				//Sale05M070
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','003','�Ȥ�"+allOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//AS400
				strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '003', '�Ȥ�"+allOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbJGENLIB.execFromPool(strJGENLIBSql);
				amlMsg.add("�Ȥ�"+allOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C");
			}else{
				//���ŦX
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','003','�Ȥ�"+allOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			}
			//�ӤH
			if(p1Count>=2){
				//Sale05M070
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+strOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','001','�Ȥ�"+strOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//AS400
				strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '001', '�Ȥ�"+strOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbJGENLIB.execFromPool(strJGENLIBSql);
				amlMsg.add("�Ȥ�"+strOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C");
			}else{
				//���ŦX
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','001','�Ȥ�"+strOrderName+"������I2���H�W����ڶ��A�B�C������s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			}
			if(p3Addup>499999){
				//Sale05M070
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+strOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','003','�Ȥ�"+strOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//AS400
				strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '003', '�Ȥ�"+strOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbJGENLIB.execFromPool(strJGENLIBSql);
				amlMsg.add("�Ȥ�"+strOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C");
			}else{
				//���ŦX
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','003','�Ȥ�"+strOrderName+"���{��ú�ǹF�s�x��50�U���H�W�A�Хӳ��j�B�q�f����è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			}
			///20200828+�Y�e��饼���ڡA����ܤT��֭p
			if(retADayBefore84Tab.length != 0 || retTwoDayBefore84Tab.length != 0){
				//�˺A2
				if(p2AllCount>=2){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','002','�Ȥ�"+allOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '002', '�Ȥ�"+allOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					amlMsg.add("�Ȥ�"+allOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C");
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','002','�Ȥ�"+allOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
				//�˺A4
				if(p4AllAddup>499999){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','004','�Ȥ�"+allOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '004', '�Ȥ�"+allOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					amlMsg.add("�Ȥ�"+allOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C");
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','004','�Ȥ�"+allOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
				if(p2Count>=2){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+strOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','002','�Ȥ�"+strOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '002', '�Ȥ�"+strOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					amlMsg.add("�Ȥ�"+strOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C");
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','002','�Ȥ�"+strOrderName+"3����~�餺�A��2��H�{���ζ״ڤ���s�x��45�U�B���F50�U���d��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
				if(p4Addup>499999){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+strOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','004','�Ȥ�"+strOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strOrderID+"', '"+strOrderName+"', '773', '004', '�Ȥ�"+strOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					amlMsg.add("�Ȥ�"+strOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C");
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allOrderID+"','"+allOrderName+"','"+strEDate+"','RY','773','004','�Ȥ�"+strOrderName+"3����~�餺�A�֭pú��{���W�L50�U���C�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
			}
		}//���檺�Ҧ��H
		//AML MSG �B�z
		Iterator  it = amlMsg.iterator();
		while(it.hasNext()){
			String tempMsg =(String)it.next();
			System.out.println(tempMsg);
			errMsg =errMsg+tempMsg+"\n";
		}
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//Pattern5,8,9,10,11,17~20
		//�H�Υd
		ret083Table  =  getTableData("table5");
		if(ret083Table.length > 0) {
			for(int e=0;e<ret083Table.length;e++){
				String str083Deputy = ret083Table[e][7].trim();//���Hú��
				String str083DeputyName=ret083Table[e][8].trim();//�m�W
				String str083DeputyId=ret083Table[e][9].trim();//�����Ҹ�
				String str083Rlatsh=ret083Table[e][10].trim();//���Y
				String str083Bstatus=ret083Table[e][12].trim();//�¦W��
				String str083Cstatus=ret083Table[e][13].trim();//���ަW��
				String str083Rstatus=ret083Table[e][14].trim();//�Q���H
		System.out.println("str083Deputy=====>"+str083Deputy);
		System.out.println("str083DeputyName=====>"+str083DeputyName);
		System.out.println("str083DeputyId=====>"+str083DeputyId);
		System.out.println("str083Rlatsh=====>"+str083Rlatsh);
		System.out.println("str083Bstatus=====>"+str083Bstatus);
		System.out.println("str083Cstatus=====>"+str083Cstatus);
		System.out.println("str083Rstatus=====>"+str083Rstatus);
				//���A��LOG_2,3,4,6,7,9,10,11,12,15,16
				//2
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','002','�P�@�Ȥ�3����~�餺�A��2��H�{���ζ״ڹF450,000~499,999��, �t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//3
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','003','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//4
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','004','�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//6
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','006','�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶR�A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//7
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','007','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//9
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','009','�Ȥ�Y�ӦۥD�޾����Ҥ��i����~���P�����ꮣ���Y���ʥ�����a�Φa�ϡA�Ψ�L����`�Υ��R����`����a�Φa�ϡA���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//10
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','010','�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//11
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','011','����̲ר��q�H�Υ���H���D�޾������i�����Ƥ��l�ι���F�ΰ�ڻ{�w�ΰl�d�����Ʋ�´�F�Υ������æ��P���Ʋ�´�����p�̡A���̸ꮣ����k�i������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//12
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','012','�Ȥ�n�D�N���ʲ��v�Q�n�O���ĤT�H�A���ണ�X�������p�Ωڵ����������`���p�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//15
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','015','�n�D���q�}�ߨ����T��I�������䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//16
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','016','�n�D���q�}�ߺM�P����u(�������u)�䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				
				if("Y".equals(str083Deputy)){//���Nú�H
					//18����W��
					//Query_Log ���ͤ�
					strPW0Dsql = "SELECT BIRTHDAY FROM QUERY_LOG WHERE PROJECT_ID = '"+strProjectID1+"' AND QUERY_ID = '"+str083DeputyId+"' AND NAME = '"+str083DeputyName+"'";
					retQueryLog = dbPW0D.queryFromPool(strPW0Dsql);
					if(retQueryLog.length > 0) {
						System.out.println("BIRTHDAY====>"+retQueryLog[0][0].trim().replace("/","-")) ;
						strBDaysql = "AND ( CUSTOMERNAME='"+str083DeputyName+"' AND BIRTHDAY = '"+retQueryLog[0][0].trim().replace("/","-")+"' )";
					}else{
						strBDaysql = "AND CUSTOMERNAME='"+str083DeputyName+"'";
					}
					System.out.println("strBDaysql====>"+strBDaysql) ;
					//AS400
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X181' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str083DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '018', '�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}else{
							errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}
						*/
						errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���ŦX','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str083DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//X171
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X171' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str083DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '021', '�ӫȤ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str083DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���ŦX','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str083DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
					if("�B��".equals(str083Rlatsh) || "��L".equals(str083Rlatsh)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���ŦX','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str083DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','008','�Nú�ڤH"+str083DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}else{
						errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}
					*/
					errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";
					//�Ȥᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C
					//�Ȥᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C
					if("Y".equals(str083Bstatus) || "Y".equals(str083Cstatus)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName ,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '020', '�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}else{
							errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}
						*/
						errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���ŦX','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str083DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//�Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
					if("Y".equals(str083Rstatus)){
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str083DeputyId+"', '"+str083DeputyName+"', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�H�Υd�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}else{
							errMsg =errMsg+"\n�H�Υd�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}
						*/
						errMsg =errMsg+"�H�Υd�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�H�Υd���','"+strActionName+"', '���ŦX','"+str083DeputyId+"','"+str083DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str083DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
				}else{
					//���Hú��(���A��5,8,17,19,20)
					//5
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','005','�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//8
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','008','���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//17
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','017','�ӫȤᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//17
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','018','�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//19
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','019','�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//20
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�H�Υd���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','020','�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}		
			}
		}
		//�{��(�u���@��)
		System.out.println("strDeputy=====>"+strDeputy);
		System.out.println("strDeputyName=====>"+strDeputyName);
		System.out.println("strDeputyID=====>"+strDeputyID);
		System.out.println("strDeputyRelationship=====>"+strDeputyRelationship);
		System.out.println("bStatus=====>"+bStatus);
		System.out.println("cStatus=====>"+cStatus);
		System.out.println("rStatus=====>"+rStatus);
		System.out.println("strCashMoney=====>"+strCashMoney);
		//���A��LOG_6,9,10,11,12,15,16
		//6
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','006','�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶR�A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//9
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','009','�Ȥ�Y�ӦۥD�޾����Ҥ��i����~���P�����ꮣ���Y���ʥ�����a�Φa�ϡA�Ψ�L����`�Υ��R����`����a�Φa�ϡA���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//10
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','010','�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//11
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','011','����̲ר��q�H�Υ���H���D�޾������i�����Ƥ��l�ι���F�ΰ�ڻ{�w�ΰl�d�����Ʋ�´�F�Υ������æ��P���Ʋ�´�����p�̡A���̸ꮣ����k�i������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//12
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','012','�Ȥ�n�D�N���ʲ��v�Q�n�O���ĤT�H�A���ണ�X�������p�Ωڵ����������`���p�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//15
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','015','�n�D���q�}�ߨ����T��I�������䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//16
		strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','016','�n�D���q�}�ߺM�P����u(�������u)�䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
		dbSale.execFromPool(strSaleSql);
		intRecordNo++;
		//5, 8, 17,18,19,20,21
		if(Double.parseDouble(strCashMoney) != 0){//���{��ú�O
			if("Y".equals(strDeputy)){//���Nú�H
				//18����W��
				//Query_Log ���ͤ�
				strPW0DSql = "SELECT BIRTHDAY FROM QUERY_LOG WHERE PROJECT_ID = '"+strProjectID1+"' AND QUERY_ID = '"+strDeputyID+"' AND NAME = '"+strDeputyName+"'";
				retQueryLog = dbPW0D.queryFromPool(strPW0DSql);
				if(retQueryLog.length > 0) {
					System.out.println("BIRTHDAY====>"+retQueryLog[0][0].trim().replace("/","-")) ;
					strBDaysql = "AND ( CUSTOMERNAME='"+strDeputyName+"' AND BIRTHDAY = '"+retQueryLog[0][0].trim().replace("/","-")+"' )";
				}else{
					strBDaysql = "AND CUSTOMERNAME='"+strDeputyName+"'";
				}
				System.out.println("strBDaysql====>"+strBDaysql) ;
				//AS400
				str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X181' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+strDeputyID+"' "+strBDaysql ;
				retCList = db400CRM.queryFromPool(str400sql);
				if(retCList.length > 0) {
					//400 LOG
					stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '018', '�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(stringSQL);	
					//SALE LOG
					stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "+
									" VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '�Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(stringSQL);
					intRecordNo++;
					/*
					if("".equals(errMsg)){
						errMsg ="�{���Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
					}else{
						errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
					}
					*/
					errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC\n";
				}else{
					//���ŦX
					stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "+
									" VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���ŦX','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+strDeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(stringSQL);
					intRecordNo++;
				}
				//X171
				str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X171' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+strDeputyID+"' "+strBDaysql ;
				retCList = db400CRM.queryFromPool(str400sql);
				if(retCList.length > 0) {
					//400 LOG
					stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '021', '�Ȥ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(stringSQL);	
					//SALE LOG
					stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "+
									" VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '�Nú�ڤH"+strDeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+strDeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(stringSQL);
					intRecordNo++;
					/*
					if("".equals(errMsg)){
						errMsg ="�{���Nú�ڤH"+strDeputyName+"�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
					}else{
						errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
					}
					*/
					errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C\n";
				}else{
					//���ŦX
					stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) "+
									" VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���ŦX','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+strDeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(stringSQL);
					intRecordNo++;
				}
				//�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
				if("�B��".equals(strDeputyRelationship) || "��L".equals(strDeputyRelationship)){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '�Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="�{���Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
					}else{
						errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
					}
					*/
					errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���ŦX','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+strDeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
				//���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
				//Sale05M070
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"','�Nú�ڤH"+strDeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','008','�Nú�ڤH"+strDeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//AS400
				strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbJGENLIB.execFromPool(strJGENLIBSql);
				/*
				if("".equals(errMsg)){
					errMsg ="�{���Nú�ڤH"+strDeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
				}else{
					errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
				}
				*/
				errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";
				//�Ȥᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C
				//�Ȥᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C
				if("Y".equals(bStatus) || "Y".equals(cStatus)){
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '�Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '020', '�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="�{���Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
					}else{
						errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
					}
					*/
					errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '���ŦX','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+strDeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
				//�Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
				if("Y".equals(rStatus)){
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '�Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="�{���Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
					}else{
						errMsg =errMsg+"\n�{���Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
					}
					*/
					errMsg =errMsg+"�{���Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
				}else{
					//���ŦX
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '���ŦX','"+strDeputyID+"','"+strDeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+strDeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}
			}else{//�{�����Hú��
				//���A��5,8,17,18,19,20
				//5
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo, Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','005','�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//8
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','008','���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//17
				strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','017','�ӫȤᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//18
				strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','018','�Ȥᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//19
				strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','019','�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//20
				strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�{�����','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','020','�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			}
		}
		//�Ȧ�
		ret328Table  =  getTableData("table9");
		if(ret328Table.length > 0) {
			for(int f=0;f<ret328Table.length;f++){
				String str328Deputy = ret328Table[f][5].trim();//���Hú��
				String str328DeputyName=ret328Table[f][6].trim();//�m�W
				String str328DeputyId=ret328Table[f][7].trim();
				String str328ExPlace=ret328Table[f][8].trim();
				String str328Rlatsh=ret328Table[f][9].trim();
				String str328bStatus=ret328Table[f][11].trim();
				String str328cStatus=ret328Table[f][12].trim();
				String str328rStatus=ret328Table[f][13].trim();		
		System.out.println("str328Deputy=====>"+str328Deputy);
		System.out.println("str328DeputyName=====>"+str328DeputyName);
		System.out.println("str328DeputyId=====>"+str328DeputyId);
		System.out.println("str328ExPlace=====>"+str328ExPlace);
		System.out.println("str328Rlatsh=====>"+str328Rlatsh);
		System.out.println("str328bStatus=====>"+str328bStatus);
		System.out.println("str328cStatus=====>"+str328cStatus);
		System.out.println("str328rStatus=====>"+str328rStatus);
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','007','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//9
				//���A��3,4,6,7,9,11,12,15,16
				//3
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','003','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//4
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','004','�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//6
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','006','�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶR�A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//7
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','009','�Ȥ�Y�ӦۥD�޾����Ҥ��i����~���P�����ꮣ���Y���ʥ�����a�Φa�ϡA�Ψ�L����`�Υ��R����`����a�Φa�ϡA���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//11
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','011','����̲ר��q�H�Υ���H���D�޾������i�����Ƥ��l�ι���F�ΰ�ڻ{�w�ΰl�d�����Ʋ�´�F�Υ������æ��P���Ʋ�´�����p�̡A���̸ꮣ����k�i������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//12
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','012','�Ȥ�n�D�N���ʲ��v�Q�n�O���ĤT�H�A���ണ�X�������p�Ωڵ����������`���p�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//15
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','015','�n�D���q�}�ߨ����T��I�������䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//16
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','016','�n�D���q�}�ߺM�P����u(�������u)�䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//�۪��ĺʷ��޲z�e���|�����ڨ���~����´�Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`��ڨ���~����´��ĳ����a�Φa�϶פJ������ڶ��C
				strJGENLIBSql =  "SELECT CZ07 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09 = '" + str328ExPlace + "'";
				retPDCZPFTable = dbJGENLIB.queryFromPool(strJGENLIBSql);
				if(retPDCZPFTable.length > 0){
					String strCZ07 =retPDCZPFTable[0][0].trim();
					if("�u���k��".equals(strCZ07)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '�Nú�ڤH"+str328DeputyName+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','010','�Nú�ڤH"+str328DeputyName+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '010', '�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						
						String strTempMsg = "";
						if("Y".equals(str328Deputy)){
							strTempMsg = "�Ȧ�Nú�ڤH"+str328DeputyName;
						}else{
							strTempMsg = "�Ȥ�"+allOrderName;
						}
						/*			
						if("".equals(errMsg)){
							errMsg =strTempMsg+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n"+strTempMsg+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+strTempMsg+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���ŦX','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','010','�Nú�ڤH"+str328DeputyName+"�Y�Ӧ۬~���θꮣ����Y���ʥ��B����`�Υ��R����`����a�Φa�϶פJ���ڶ��A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
				}
				if("Y".equals(str328Deputy)){//���Nú�H
					//����W��
					System.out.println("�Ȧ����W��====>") ;
					//Query_Log ���ͤ�
					strPW0Dsql = "SELECT BIRTHDAY FROM QUERY_LOG WHERE PROJECT_ID = '"+strProjectID1+"' AND QUERY_ID = '"+str328DeputyId+"' AND NAME = '"+str328DeputyName+"'";
					retQueryLog = dbPW0D.queryFromPool(strPW0Dsql);
					if(retQueryLog.length > 0) {
						System.out.println("BIRTHDAY====>"+retQueryLog[0][0].trim().replace("/","-")) ;
						strBDaysql = "AND ( CUSTOMERNAME='"+str328DeputyName+"' AND BIRTHDAY = '"+retQueryLog[0][0].trim().replace("/","-")+"' )";
					}else{
						strBDaysql = "AND CUSTOMERNAME='"+str328DeputyName+"'";
					}
					System.out.println("strBDaysql====>"+strBDaysql) ;
					//AS400
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X181' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str328DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '018', '�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}else{
							errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}
						*/
						errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���ŦX','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str328DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//X171
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X171' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str328DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '021', '�Ȥ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','�Nú�ڤH"+str328DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str328DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName,RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���ŦX','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str328DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
					if("�B��".equals(str328Rlatsh) || "��L".equals(str328Rlatsh)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName, RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName, RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���ŦX','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str328DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','�Nú�ڤH"+str328DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','008','�Nú�ڤH"+str328DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}else{
						errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}
					*/
					errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";
					//�Ȥᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C
					//�Ȥᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C
					if("Y".equals(str328bStatus) || "Y".equals(str328cStatus)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '020', '�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}else{
							errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}
						*/
						errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���ŦX','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str328DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//�Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
					if("Y".equals(str328rStatus)){
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"', '�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str328DeputyId+"', '"+str328DeputyName+"', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="�Ȧ�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}else{
							errMsg =errMsg+"\n�Ȧ�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}
						*/
						errMsg =errMsg+"�Ȧ�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"', '���ŦX','"+str328DeputyId+"','"+str328DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str328DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
				}else{
					//���Hú��(���A��)10,5,8,17,19,20
					//5
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName, RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','005','�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//8
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','008','���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//10
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','010','�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//17
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','017','�ӫȤᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//18
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','018','�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//19
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"', '���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','019','�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//20
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','020','�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//21
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType,ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȧ���','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','021','�Ȥ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}	
			}
		}
		//����
		ret082Table  =  getTableData("table2");
		if(ret082Table.length > 0) {
			for(int g=0;g<ret082Table.length;g++){
				String str082Deputy = ret082Table[g][8].trim();//���Hú��
				String str082DeputyName=ret082Table[g][9].trim();//�m�W
				String str082DeputyId=ret082Table[g][10].trim();//�����Ҹ�
				String str082Rlatsh=ret082Table[g][11].trim();//���Y
				String str082Bstatus=ret082Table[g][13].trim();//�¦W��
				String str082Cstatus=ret082Table[g][14].trim();//���ަW��
				String str082Rstatus=ret082Table[g][15].trim();//�Q���H
		System.out.println("str082Deputy=====>"+str082Deputy);
		System.out.println("str082DeputyName=====>"+str082DeputyName);
		System.out.println("str082DeputyId=====>"+str082DeputyId);
		System.out.println("str082Rlatsh=====>"+str082Rlatsh);
		System.out.println("str082Bstatus=====>"+str082Bstatus);
		System.out.println("str082Cstatus=====>"+str082Cstatus);
		System.out.println("str082Rstatus=====>"+str082Rstatus);
				//���A��2,3,4,6,7,9,10,11,12,15,16
				//2
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','002','�P�@�Ȥ�3����~�餺�A��2��H�{���ζ״ڹF450,000~499,999��, �t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//3
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','003','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//4
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','004','�P�@�Ȥ�3����~�餺�A�֭pú��{���W�L50�U��, �t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//6
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','006','�P�@�Ȥᤣ�ʲ��R��Añ���e�h�q�����ʶR�A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//7
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','007','�P�@�Ȥ�P�@��~��{��ú�ǲ֭p�F50�U��(�t)�H�W�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//9
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','009','�Ȥ�Y�ӦۥD�޾����Ҥ��i����~���P�����ꮣ���Y���ʥ�����a�Φa�ϡA�Ψ�L����`�Υ��R����`����a�Φa�ϡA���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//10
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','010','�ۥD�޾����Ҥ��i����~���P������U���ƥ��l���Y���ʥ�����a�Φa�ϡB�Ψ�L����`�Υ��R����`����a�Φa�϶פJ������ڶ��A���ˮ֨�X�z�ʡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//11
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','011','����̲ר��q�H�Υ���H���D�޾������i�����Ƥ��l�ι���F�ΰ�ڻ{�w�ΰl�d�����Ʋ�´�F�Υ������æ��P���Ʋ�´�����p�̡A���̸ꮣ����k�i������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//12
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','012','�Ȥ�n�D�N���ʲ��v�Q�n�O���ĤT�H�A���ണ�X�������p�Ωڵ����������`���p�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//15
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','015','�n�D���q�}�ߨ����T��I�������䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
				//16
				strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','016','�n�D���q�}�ߺM�P����u(�������u)�䲼�@�����I�覡�A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
				dbSale.execFromPool(strSaleSql);
				intRecordNo++;
			
				if("Y".equals(str082Deputy)){//���Nú�H
					System.out.println("���ڨ���W��====>") ;
					//����W��
					//Query_Log ���ͤ�
					strPW0Dsql = "SELECT BIRTHDAY FROM QUERY_LOG WHERE PROJECT_ID = '"+strProjectID1+"' AND QUERY_ID = '"+str082DeputyId+"' AND NAME = '"+str082DeputyName+"'";
					retQueryLog = dbPW0D.queryFromPool(strPW0Dsql);
					if(retQueryLog.length > 0) {
						System.out.println("BIRTHDAY====>"+retQueryLog[0][0].trim().replace("/","-")) ;
						strBDaysql = "AND ( CUSTOMERNAME='"+str082DeputyName+"' AND BIRTHDAY = '"+retQueryLog[0][0].trim().replace("/","-")+"' )";
					}else{
						strBDaysql = "AND CUSTOMERNAME='"+str082DeputyName+"'";
					}
					System.out.println("strBDaysql====>"+strBDaysql) ;
					//AS400
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X181' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str082DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '018', '�ӫȤᬰ���ަW���H������W��A�T�����ýШ̨���~�����q���@�~�|��k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"', '�Nú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="���ڥNú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}else{
							errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC";
						}
						*/
						errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"', '���ŦX','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','018','�Nú�ڤH"+str082DeputyName+"�����ޤ�����W���H�A�иT�����A�è̬~��������q���@�~�e�e�k��ǡC','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//X171
					str400sql = "SELECT * FROM CRCLNAPF WHERE CONTROLLISTNAMECODE IN (SELECT DISTINCT C.CONTROLLISTNAMECODE FROM CRCLNCPF C,CRCLCLPF L WHERE C.CONTROLCLASSIFICATIONCODE=L.CONTROLCLASSIFICATIONCODE AND L.CONTROLCLASSIFICATIONCODE ='X171' AND C.REMOVEDDATE >= '"+strNowTimestamp+"' ) AND ISREMOVE = 'N'  AND CUSTOMERID = '"+str082DeputyId+"' "+strBDaysql ;
					retCList = db400CRM.queryFromPool(str400sql);
					if(retCList.length > 0) {
						//400 LOG
						stringSQL = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '021', '�Ȥ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(stringSQL);	
						//SALE LOG
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"', '�Nú�ڤH"+str082DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str082DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
						/*
						if("".equals(errMsg)){
							errMsg ="���ڥNú�ڤH"+str082DeputyName+"�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						stringSQL = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"', '���ŦX','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','021','�Nú�ڤH"+str082DeputyName+"�B�a�x�����Φ��K�����Y���H�A�����n�F�v��¾�ȤH�h�A�Х[�j�Ȥ��¾�լd�A�è̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(stringSQL);
						intRecordNo++;
					}
					//�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡC�Ш̬~������@�~��z
					if("�B��".equals(str082Rlatsh) || "��L".equals(str082Rlatsh)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','�Nú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '005', '�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="���ڥNú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}else{
							errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C";
						}
						*/
						errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���ŦX','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','005','�Nú�ڤH"+str082DeputyName+"�P�Ȥ�"+allOrderName+"�D�G�˵����������Y�A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C
					//Sale05M070
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','�Nú�ڤH"+str082DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','008','�Nú�ڤH"+str082DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//AS400
					strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '008', '���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbJGENLIB.execFromPool(strJGENLIBSql);
					/*
					if("".equals(errMsg)){
						errMsg ="���ڥNú�ڤH"+str082DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}else{
						errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C";
					}
					*/
					errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"�N����z���ʲ�����A�Ш̬~���θꮣ����@�~��z�C\n";
					//�Ȥᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C
					//�Ȥᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C
					if("Y".equals(str082Bstatus) || "Y".equals(str082Cstatus)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','�Nú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '020', '�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="���ڥNú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}else{
							errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C";
						}
						*/
						errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���ŦX','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','020','�Nú�ڤH"+str082DeputyName+"���æ��¦W���H�A���ЮֽT�{��A�A�i������������@�~�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
					//�Ȥᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C
					if("Y".equals(str082Rstatus)){
						//Sale05M070
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','�Nú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
						//AS400
						strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+str082DeputyId+"', '"+str082DeputyName+"', '773', '019', '�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbJGENLIB.execFromPool(strJGENLIBSql);
						/*
						if("".equals(errMsg)){
							errMsg ="���ڥNú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}else{
							errMsg =errMsg+"\n���ڥNú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C";
						}
						*/
						errMsg =errMsg+"���ڥNú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C\n";
					}else{
						//���ŦX
						strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���ŦX','"+str082DeputyId+"','"+str082DeputyName+"','"+strEDate+"','RY','773','019','�Nú�ڤH"+str082DeputyName+"�����q�Q�`���t�H�A�Ш̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
						dbSale.execFromPool(strSaleSql);
						intRecordNo++;
					}
				}else{//���Hú��
					//���A��5,8,17,19,20
					//5
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','005','�Nú�ڤH�P�ʶR�H���Y���D�G���ˤ���/�ÿˡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//8
					strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','008','���ʲ��P��ѲĤT��N�z��ú�ڡA�t���ˮִ��ܳq���C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//17
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo, Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"', '���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','017','�ӫȤᬰ���ަW���H�A�а���[�j���Ȥ��¾�f�d�ę̀���~�������q���@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//19
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo, ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','019','�ӫȤᬰ���q�Q�`���t�H�A�ݨ̫O�I�~�P�Q�`���Y�H�q�Ʃ�ڥH�~����L����޲z��k����C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//20
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','020','�ӫȤᬰ�æ��¦W���H�A���ЮֽT�{��A�A�i��������C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
					//21
					strSaleSql = "INSERT INTO Sale05M070 (DocNo, OrderNo, ProjectID1, RecordNo,ActionNo,  Func, RecordType, ActionName, RecordDesc, CustomID, CustomName, EDate, SHB00, SHB06A, SHB06B, SHB06,SHB97,SHB98,SHB99)  VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','���ڸ��','"+strActionName+"','���A��','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','021','�Ȥ�Ψ���q�H�B�a�x�����Φ��K�����Y���H�A���{���B�����ꤺ�~�F���ΰ�ڲ�´���n�F�v��¾�ȡA�Х[�j�Ȥ��¾�լd�A�Ш̬~������@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
					dbSale.execFromPool(strSaleSql);
					intRecordNo++;
				}		
			}
		}
		//13.�Ȥ��I���ʲ�������ڶ��A�H�{�r��I�q���H�~�U�����ڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C
		if("Y".equals(rule13)){
			//Sale05M070
			strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','013','�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbSale.execFromPool(strSaleSql);
			intRecordNo++;
			//AS400
			strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '013', '�Ȥ��I���ʲ�������ڶ��A�H�{�r��I�q���H�~�U�����ڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbJGENLIB.execFromPool(strJGENLIBSql);
			/*
			if("".equals(errMsg)){
				errMsg ="�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C";
			}else{
				errMsg =errMsg+"\n�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C";
			}
			*/
			errMsg =errMsg+"�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C\n";
		}else{
			//���ŦX
			strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','013','�Ȥ�"+allCustomName+"�H�{�r��I�q���H�~�U�����ʲ�������ڡA�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbSale.execFromPool(strSaleSql);
			intRecordNo++;
		}
		//14.�Ȥ��ñ���e���e�I�M�۳ƴڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C
		if("Y".equals(rule14)){
			//Sale05M070
			strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','014','�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbSale.execFromPool(strSaleSql);
			intRecordNo++;
			//AS400
			strJGENLIBSql = "INSERT INTO PSHBPF (SHB00, SHB01, SHB03, SHB04, SHB05, SHB06A, SHB06B, SHB06, SHB97, SHB98, SHB99) VALUES ('RY', '"+strDocNo+"', '"+RocNowDate+"', '"+strDeputyID+"', '"+strDeputyName+"', '773', '014', '�Ȥ��ñ���e���e�I�M�۳ƴڡA�B�L�X�z��������ӷ��A���ˮ֬O�_�ŦX�æ��~�������x�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbJGENLIB.execFromPool(strJGENLIBSql);
			/*
			if("".equals(errMsg)){
				errMsg ="�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C";
			}else{
				errMsg =errMsg+"\n�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C";
			}
			*/
			errMsg =errMsg+"�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C\n";
		}else{
			//���ŦX
			strSaleSql = "INSERT INTO Sale05M070 (DocNo,OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,EDate,SHB00,SHB06A,SHB06B,SHB06,SHB97,SHB98,SHB99) VALUES ('"+strDocNo+"','"+strOrderNo+"','"+strProjectID1+"','"+intRecordNo+"','"+actionNo+"','���ڳ�','�Ȥ���','"+strActionName+"','���ŦX','"+allCustomID+"','"+allCustomName+"','"+strEDate+"','RY','773','014','�Ȥ�"+allCustomName+"ñ���e(�t���)���e�I�M�۳ƴڡA�Ш̬~���θꮣ����@�~��z�C','"+empNo+"','"+RocNowDate+"','"+strNowTime+"')";
			dbSale.execFromPool(strSaleSql);
			intRecordNo++;
		}
		if(!"".equals(errMsg)){
			setValue("errMsgBoxText",errMsg);	
			getButton("errMsgBoxBtn").doClick();
			getButton("sendMail").doClick();
		}
		System.out.println("value=====>"+value);
		System.out.println("===========AML============E");
		return value;
	}
	public String getInformation(){
		return "---------------AML(\u6d17\u9322\u9632\u5236).defaultValue()----------------";
	}
}
