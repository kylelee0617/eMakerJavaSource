 // �ק���:20150518 ���u�s��:B3774  �ܰʹL�h�A�R���Ҧ�mark
package Sale.backup;
import     jcx.jform.bTransaction;
import     java.util.*;
import     jcx.util.*;
import     jcx.html.*;
import     jcx.db.*;
import     com.jacob.activeX.*;
import     com.jacob.com.*;
import     Farglory.util.FargloryUtil;

public class Sale05R09401 extends bTransaction{
    talk  dbSale    =  getTalk(""+get("put_dbSale"));
    talk  dbFED1  =  getTalk(""+get("put_dbFED1"));

    public boolean action(String value) throws Throwable{
        // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
        // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
        // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
        if(!isBatchCheckOK()){
            return false;
        }
        Farglory.util.FargloryUtil  exeUtil  =  new  Farglory.util.FargloryUtil();
        long  longTime1  =  exeUtil.getTimeInMillis( );
        doExcel();
        long  longTime2  =  exeUtil.getTimeInMillis( );
        System.out.println("+�����w��---" + ((longTime2-longTime1)/1000) + "��---");
        return false;
    }
    
    // �ˮ�
    // �e�ݸ���ˮ֡A���T�^�� True
      public boolean isBatchCheckOK() throws Throwable{
        Farglory.util.FargloryUtil  exeFun  =  new  Farglory.util.FargloryUtil();
        String stringUser          = getUser().toUpperCase();
        String stringTrxDateS   = getValue("TrxDateS").trim();
        String stringTrxDateE   = getValue("TrxDateE").trim();
        String stringLastYMDS = getValue("LastYMDS").trim();
        String stringLastYMDE = getValue("LastYMDE").trim();
        //
        if("B3774".equals(stringUser) || "B2593".equals(stringUser) || "B3752".equals(stringUser)){
            if(!((stringTrxDateS.length() > 0  &&  stringTrxDateE.length() > 0)   ||   (stringLastYMDS.length() > 0  &&  stringLastYMDE.length() > 0))){
                message("[�h�����] �M [�h���J���]�A�Цܤֿ�J�@�դ��!");
                return false;
            }
            if((stringTrxDateS.length() > 0  &&  stringTrxDateE.length() == 0)   ||   (stringTrxDateS.length() == 0  &&  stringTrxDateE.length() > 0)){
                message("[�h�����]�A�п�J�_�����!");
                return false;
            }
            if((stringLastYMDS.length() > 0  &&  stringLastYMDE.length() == 0)   ||   (stringLastYMDS.length() == 0  &&  stringLastYMDE.length() > 0)){
                message("[�h���J���]�A�п�J�_�����!");
                return false;
            }
        }else{
            if(stringTrxDateS.length() == 0  ||  stringTrxDateE.length() == 0){
                message("�п�J [�h�����]!");
                return false;
            }
        }       
        //
        // �h�����-�_
        if(stringTrxDateS.length() > 0){
            stringTrxDateS = exeFun.getDateAC(stringTrxDateS, "�h�����-�_");
            if(stringTrxDateS.length() != 10){
                message(stringTrxDateS);
                getcLabel("TrxDateS").requestFocus();
                return false;
            }else{
                setValue("TrxDateS", stringTrxDateS);
            }
        }
        // �h�����-��
        if(stringTrxDateE.length() > 0){
            stringTrxDateE = exeFun.getDateAC(stringTrxDateE, "�h�����-��");
            if(stringTrxDateE.length() != 10){
                message(stringTrxDateE);
                getcLabel("TrxDateE").requestFocus();
                return false;
            }else{
                setValue("TrxDateE", stringTrxDateE);
            }
        }
        //
        if(stringTrxDateS.length() > 0  &&  stringTrxDateE.length() > 0){
            if(datetime.subDays1(stringTrxDateS.replaceAll("/",""),stringTrxDateE.replaceAll("/","")) > 0){
                message("[�h�����] �_���i�H�j��!");
                return false;
            }
        }
        // �h���J���-�_
        if(stringLastYMDS.length() > 0){
            stringLastYMDS = exeFun.getDateAC(stringLastYMDS, "�h���J���-�_");
            if(stringLastYMDS.length() != 10){
                message(stringLastYMDS);
                getcLabel("LastYMDS").requestFocus();
                return false;
            }else{
                setValue("LastYMDS", stringLastYMDS);
            }
        }
        // �h���J���-��
        if(stringLastYMDE.length() > 0){
            stringLastYMDE = exeFun.getDateAC(stringLastYMDE, "�h���J���-��");
            if(stringLastYMDE.length() != 10){
                message(stringLastYMDE);
                getcLabel("LastYMDE").requestFocus();
                return false;
            }else{
                setValue("LastYMDE", stringLastYMDE);
            }
        }
        //
        if(stringLastYMDS.length() > 0  &&  stringLastYMDE.length() > 0){
            if(datetime.subDays1(stringLastYMDS.replaceAll("/",""),stringLastYMDE.replaceAll("/","")) > 0){
                message("[�h���J���] �_���i�H�j��!");
                return false;
            }
        }
        
        //
        return true;
    }

    public void doExcel() throws Throwable{
        Farglory.util.FargloryUtil  exeUtil  =  new  Farglory.util.FargloryUtil();
        long  longTime1  =  exeUtil.getTimeInMillis( );
        String stringProjectID1                  = getValue("ProjectID1").trim();
        String stringTrxDateS                    = getValue("TrxDateS").trim();
        String stringTrxDateE                    = getValue("TrxDateE").trim();
        String stringIsNoContainChange   = getValue("IsNoContainChange").trim();
        String stringLastYMDS                  = getValue("LastYMDS").trim();
        String stringLastYMDE                  = getValue("LastYMDE").trim();
        String stringSql                              = "";
        String stringProjectID1Sql              = "";
        String stringTrxDateSql                  = "";
        String stringLastYMDSql                = "";
        String stringNoContainChangeSql = "";
        String retData[][]                            = null;
        String stringQryCondition              = "";
        //
        if(stringProjectID1.length() > 0){
            stringProjectID1Sql = "and T94.ProjectID1='"+stringProjectID1+"' ";
            stringQryCondition = "����:�קO";
        }
        if(stringTrxDateS.length() > 0){
            stringTrxDateSql    = "AND T94.TrxDate BETWEEN '"+stringTrxDateS+"' AND '"+stringTrxDateE+"' ";
            stringQryCondition = stringQryCondition.length()==0?"����:�h�����":stringQryCondition+"/�h�����";
        }
        if(stringLastYMDS.length() > 0){
            stringLastYMDSql  = "AND (CASE WHEN T94.LastYMD='' THEN T94.TrxDate ELSE T94.LastYMD END) BETWEEN '"+stringLastYMDS+"' AND '"+stringLastYMDE+"' ";
            stringQryCondition = stringQryCondition.length()==0?"����:�h���J���":stringQryCondition+"/�h���J���";;
        }
        if("Y".equals(stringIsNoContainChange)){
            stringNoContainChangeSql = "and RTRIM(T233.BItemCd)+RTRIM(T233.MItemCd)+RTRIM(T233.SItemCd)+RTRIM(T233.DItemCd)  not IN ('03030103',  '03030104') ";
            stringQryCondition = stringQryCondition.length()==0?"����:���t���W����":stringQryCondition+"/���t���W����";
        }
        // 2 ORDERNO
        stringSql = "SELECT T94.ProjectID1, T94.TrxDate, T94.ORDERNO, T95.HouseCar, T95.Position, "+
                  "'' CustomName, T90.OrderDate, T92.PingSu, T92.ListPrice, T92.DealMoney, "+
                  "T92.GiftMoney, T92.CommMoney, T92.ViMoney, T40.FloorPrice, "+
                  "SaleName1, SaleName2, SaleName3, SaleName4, SaleName5, "+
                  "T94.BItemCd, BItemName, T94.MItemCd, MItemName, T94.SItemCd, SItemName, "+
                  "T94.DItemCd, DItemName, "+
                  "(case when T95.HouseCar='House' "+
                            "then (select distinct "+
                                        "(case when convert(char(10),ContrDate,111)='1900/01/01' "+
                                                  "then '' "+
                                             "else convert(char(10),ContrDate,111) end) "+
                                   "from A_Sale1 "+
                                   "where ProjectID1=T94.ProjectID1 "+
                                   "and (case when convert(char(10),ContrDate,111)='1900/01/01' "+
                                                "then '' "+
                                           "else convert(char(10),ContrDate,111) end)!='' "+
                                   "and HouseCar='Position' "+
                                   "and Position=T95.Position "+
                                   "and convert(char(10),Deldate,111)=T94.TrxDate "+
                                   "and ((isnull(OrderNo,'')='' and convert(char(10),OrderDate,111)=T90.OrderDate) or "+
                                      "(isnull(OrderNo,'')!='' and OrderNo=T94.OrderNo))) "+
                       "when T95.HouseCar='Car' "+
                            "then (select distinct "+
                                        "(case when convert(char(10),ContrDate,111)='1900/01/01' "+
                                                  "then '' "+
                                             "else convert(char(10),ContrDate,111) end) "+
                                   "from A_Sale1 "+
                                   "where ProjectID1=T94.ProjectID1 "+
                                   "and (case when convert(char(10),ContrDate,111)='1900/01/01' "+
                                                "then '' "+
                                           "else convert(char(10),ContrDate,111) end)!='' "+
                                   "and HouseCar='Car' "+
                                   "and Car=T95.Position "+
                                   "and convert(char(10),Deldate,111)=T94.TrxDate "+
                                   "and ((isnull(OrderNo,'')='' and convert(char(10),OrderDate,111)=T90.OrderDate) or "+
                                      "(isnull(OrderNo,'')!='' and OrderNo=T94.OrderNo))) "+
                       "else '' end) ContrDate, "+
                  "(SELECT TOP 1 M031.TypeName+'('+convert(nvarchar,M031.RangeStart)+'~'+convert(nvarchar,M031.RangeEnd)+')' "+
                  "FROM Sale09M030 as M030, Sale09M031 as M031 "+
                  "WHERE M030.AreaGUID=M031.AreaGUID "+
                  "AND M030.ProjectID=T40.ProjectID1 "+
                  "AND T40.PingSu BETWEEN RangeStart AND RangeEnd "+
                  "ORDER BY M031.OrderByNo) HouseType, "+
                  "(CASE WHEN T94.LastYMD='' THEN T94.TrxDate ELSE T94.LastYMD END) LastYMD "+
                  "FROM SALE05M094 T94, SALE05M230 T230, SALE05M231 T231, SALE05M233 T233, "+
                  "SALE05M095 T95, SALE05M092 T92, SALE05M040 T40, "+
                  "SALE05M090 T90 "+
                  "WHERE ISNULL(T94.BItemCd,'')<>'' "+
                  "AND T94.BItemCd=T230.BItemCd "+
                  "AND T94.BItemCd=T231.BItemCd "+
                  "AND T94.MItemCd=T231.MItemCd "+
                  "AND T94.BItemCd=T233.BItemCd "+
                  "AND T94.MItemCd=T233.MItemCd "+
                  "AND T94.SItemCd=T233.SItemCd "+
                  "AND T94.DItemCd=T233.DItemCd "+
                  "AND T95.ProjectID1=T94.ProjectID1 "+
                  "AND T95.OrderNo=T94.OrderNo "+
                  "AND T95.TrxDate=T94.TrxDate "+
                  "AND T92.ORDERNO=T94.ORDERNO "+
                  "AND T92.Position=T95.Position "+
                  "AND T40.ProjectID1=T95.ProjectID1 "+
                  "AND T40.Position=T95.Position "+
                  "AND T40.Position=T92.Position "+
                  "AND T90.ORDERNO=T94.ORDERNO "+
                  stringProjectID1Sql+
                  stringNoContainChangeSql+
                  stringTrxDateSql+
                  stringLastYMDSql+
                  "AND NOT (T90.ProjectID1='H32A' AND T92.Position='S10F01') "+
                  "ORDER BY T94.ProjectID1, T94.ORDERNO";
        retData = dbSale.queryFromPool(stringSql);
        if(retData.length == 0){
            message("�d�L���!");
            return;
        }
        //
        Farglory.Excel.FargloryExcel  exeExcel  =  new  Farglory.Excel.FargloryExcel();
        //Vector     retVector                 = exeExcel.getExcelObject("http://emaker.farglory.com.tw:8080/servlet/baServer3?step=6?filename=C:/emaker/batch/EXCEL/Sale/Sale05R09401.xlsx");        
        //Vector     retVector                 = exeExcel.getExcelObject("G:\\��T��\\Excel\\Sale\\Sale05R09401.xlsx");
        Vector     retVector                 = exeExcel.getExcelObject("G:\\��T��\\Excel\\Sale\\Sale05R09401t.xltx");
        Dispatch objectSheet1       = (Dispatch)retVector.get(1);
        int          intStartDataRow  = 4;
        int          intInsertDataRow = intStartDataRow;
        String    stringTemp           = "";
        // �h������B�h���J���
        if(stringTrxDateS.length() > 0){
            stringTemp = "�h������G"+stringTrxDateS+"-"+stringTrxDateE;
        }
        if(stringLastYMDS.length() > 0){
            stringTemp = stringTemp.length()==0?"�h���J����G"+stringLastYMDS+"-"+stringLastYMDE:stringTemp+"�B�h���J����G"+stringLastYMDS+"-"+stringLastYMDE;
        }
        exeExcel.putDataIntoExcel(  0,  1,  stringTemp,  objectSheet1);
        // ����
        exeExcel.putDataIntoExcel(  0,  2,  stringQryCondition,                                           objectSheet1);
        
        // �����
        stringTemp = "";
        for(int intRow=0; intRow<retData.length; intRow++){
            for(int intCol=0; intCol<retData[intRow].length; intCol++){
                stringTemp = retData[intRow][intCol].trim();
                //
                if(intCol == 2){
                    stringTemp = exeUtil.doSubstring(stringTemp,2,stringTemp.length());
                }else if(intCol == 5){
                    stringTemp = getCustomNames(retData[intRow][2]);
                }
                //
                exeExcel.putDataIntoExcel(intCol,  intInsertDataRow,  stringTemp,  objectSheet1);
            }
            intInsertDataRow++;
        }
        // �ؽu
        if(retData.length == 1){
            exeExcel.doLineStyle2("AC"+(intStartDataRow+1)+":AC"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "3");
            exeExcel.doLineStyle2("AD"+(intStartDataRow+1)+":AD"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "3");
            exeExcel.doLineStyle2("A"+(intStartDataRow+1)+":AB"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "2");
        }else{
            exeExcel.doLineStyle3("AC"+(intStartDataRow+1)+":AC"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "2", "1", "2");
            exeExcel.doLineStyle3("AD"+(intStartDataRow+1)+":AD"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "2", "1", "2");
            exeExcel.doLineStyle3("A"+(intStartDataRow+1)+":AB"+(intStartDataRow+retData.length), objectSheet1, "1", "2", "1", "3", "1", "3", "1", "3", "1", "2", "1", "2");
        }
        
        long  longTime2  =  exeUtil.getTimeInMillis( );
        System.out.println("���---" + ((longTime2-longTime1)/1000) + "��---");

        // ���� Excel ����
        if(exeExcel != null){
            exeExcel.getReleaseExcelObject(retVector);
        }
    }
    
    public String getCustomNames(String stringOrderNo) throws Throwable{
        talk  dbSale  =  getTalk(""+get("put_dbSale"));
        String stringSql                     = "";
        String stringCustomNames = "";
        String retData[][]                   = null;
        //        
        stringSql = "SELECT CustomName "+
                   "FROM Sale05M091 "+
                   "WHERE OrderNo='"+stringOrderNo+"' "+
                   "AND ISNULL(StatusCd,'')=''";
        retData = dbSale.queryFromPool(stringSql);
        for(int intNo=0; intNo<retData.length; intNo++){
            stringCustomNames = stringCustomNames.length()==0?
                                retData[intNo][0]:
                                stringCustomNames+","+retData[intNo][0];
        }
        return stringCustomNames;
    }


    public String getInformation(){
        return "---------------�h��M�U(Sale05R09401)----------------";
    }
}