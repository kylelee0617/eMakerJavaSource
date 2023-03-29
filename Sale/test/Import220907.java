package Sale.test;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.apache.commons.lang.StringUtils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import Farglory.util.KSqlUtils;
import Farglory.util.KUtils;
import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.check;

/**
 * �פJ���wEXCEL
 * 
 * @author B04391
 *
 */
public class Import220907 extends bproc {
  String �v�jDate;
  String �w�oDate;
  KSqlUtils ksUtil;
  KUtils kUtil;
  talk dbSale;
  talk dbAS400;
  talk dbFE5D = null;
  String serverType;
  boolean isTest = true;

  public String getDefaultValue(String value) throws Throwable {
    ksUtil = new KSqlUtils();
    kUtil = new KUtils();
    dbSale = ksUtil.getTBean().getDbSale();
    dbAS400 = ksUtil.getTBean().getDb400CRM();
    dbFE5D = this.getTalk("FE5D");
    serverType = get("serverType").toString().trim();

    // �]�w����
    isTest = "PROD".equals(serverType) ? false : true;
    System.out.println(">>>Data220627 ReadExcel  ����:" + serverType);

    this.����();

    return value;
  }

  private void ����() throws Throwable {
    /**
     * 1. �פJexcle
     * 2. post to viewTable
     * 3. doRisk
     * 4. ���G�g�JviewTable
     * 5. 
     */
    
    // �إ�com����
    String filePath = "G:\\kyleTest\\Excel\\Test\\customerOrderDetail.xlsx";
    ActiveXComponent Excel;
    ComThread.InitSTA();
    Excel = new ActiveXComponent("Excel.Application");
    Object objectExcel = Excel.getObject();
    Excel.setProperty("Visible", new Variant(false));
    Object objectWorkbooks = Dispatch.get(objectExcel, "Workbooks").toDispatch();
    Object objectWorkbook = Dispatch.call(objectWorkbooks, "Open", filePath).toDispatch();
    Object objectSheet1 = Dispatch.get(objectWorkbook, "ActiveSheet").toDispatch();
    Dispatch.call(objectSheet1, "Activate");
    
    String[] title = { "�q��s��", "�קO", "�ɼӧO" , "����O" ,"�Ȥ�m�W" ,"�Ȥ�ID" ,"�q����" ,"�ؼЭ��I��" ,"�ؼЭ��I����" ,"��~" ,"�O�_����" ,"���y" 
        ,"�I�����I��" ,"�I������" ,"���~���I��" ,"���~����" ,"�q�����I��" ,"�q������" ,"���y���I��" ,"���y����"};
    this.setTableHeader("ResultTable", title);
    
    int intRow = 3;
    List listNoProcess = new ArrayList();
    List listImport = new ArrayList();
    while(true) {
      String[] arrNoProcess = new String[6];
      String orderNo = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "A" + intRow }, new int[1]).toDispatch(), "Value").toString()).trim(); 
      if(StringUtils.equals(orderNo.toUpperCase(), ""))  continue;
      if(StringUtils.equals(orderNo.toUpperCase(), "END"))  break;
      
      String projectId = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "B" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String house = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "C" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String car = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "D" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String custName = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "E" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String custId = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "F" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String orderDate = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "G" + intRow }, new int[1]).toDispatch(), "Value").toString());
      String targetRiskName = isNull(Dispatch.get(Dispatch.invoke(objectSheet1, "Range", Dispatch.Get, new Object[] { "Y" + intRow }, new int[1]).toDispatch(), "Value").toString());
      
      //�d�ߦ�~�O �޲z�H ���y
      String sql = "SELECT MajorName ,PositionName ,CountryName "
                 + "FROM Sale05M091 b "
                 + "WHERE b.OrderNo = '"+ orderNo +"' AND CustomNo = '"+ custId +"' ";
      String[][] arrT091 = dbSale.queryFromPool(sql);
      
      String indCode = ksUtil.getIndustryCodeByMajorName(arrT091[0][0].trim());
      String managerFlag = ksUtil.isManager(arrT091[0][1].trim());
      String nationName = arrT091[0][2].trim();
      
      /*
      //��s��~�O
      String sql2 = "UPDATE PLSPFLIB.CMSCLNTM SET CVOCAT = '"+ indCode +"' ,CMEXEC = '"+ managerFlag +"' "
                  + "WHERE CMCLTN = ( "
                  + "select CMCLTN " 
                  + "from PLSPFLIB.CMSCLNTM " 
                  + "where CMTIDF = '"+ custId +"' " 
                  + "order by CMLUPY desc , CMLUPM desc , CMLUPD desc , CMCLTN desc "
                  + "FETCH FIRST 1 ROWS ONLY "
                  + ") ";
      String updCMSCLNTM = dbAS400.execFromPool(sql2);
      if(StringUtils.equals(updCMSCLNTM, "0")) {
        arrNoProcess[0] = orderNo;
        arrNoProcess[1] = projectId;
        arrNoProcess[2] = house;
        arrNoProcess[3] = custId;
        arrNoProcess[4] = custMame;
        arrNoProcess[5] = "�L��~�O�i��s";
        listNoProcess.add(arrNoProcess);
      }
      */
      
      //����O���I��
      String sql3 = "SELECT CZ02 ,CZ04 ,CZ07 ,CZ08 FROM PWENLIB.PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='"+ nationName +"' FETCH FIRST 1 ROWS ONLY ";
      String[][] retNationRisk = dbAS400.queryFromPool(sql3);
      String nationRiskVal = retNationRisk[0][1].replaceAll("�@", "").replaceAll(" ", ""); 
      String nationRiskName = retNationRisk[0][2].replaceAll("�@", "").replaceAll(" ", "");
      String nationCode = retNationRisk[0][0].replaceAll("�@", "").replaceAll(" ", "");
      
      //�p�⭷�I��
      double vRO0203 = 7.5; //�T�w���~�����I
      double vRO0205 = 2;   //�T�w�q���C���I
      double targetRiskVal = 0;
      if(StringUtils.equals(targetRiskName, "��")) {
        targetRiskVal = 30;
      }else if(StringUtils.equals(targetRiskName, "��")) {
        targetRiskVal = 25;
      }else {
        targetRiskVal = 16;
      }
      
      //�w�T�{���I��
      double vRolast3 = vRO0203 + vRO0205 + Integer.parseInt(nationRiskVal)*0.15;
      
      //�^���Ȥ�I�����I��
      double bakuRiskVal = 0;
      String bakuRiskName = "�C���I";
      if(StringUtils.equals(targetRiskName, "��")) {
        bakuRiskVal = ( targetRiskVal - vRolast3 )/0.4;
      } else if(StringUtils.equals(targetRiskName, "��")) {
        bakuRiskVal = ( targetRiskVal - vRolast3 )/0.4;
      }else if(StringUtils.equals(targetRiskName, "�C")) {
        bakuRiskVal = ( targetRiskVal - vRolast3 )/0.4;
      }
      
      if(bakuRiskVal < 10) {
        bakuRiskVal = 10;
      }else if(bakuRiskVal>10 && bakuRiskVal<20) {
        bakuRiskVal = 10;
      }else if(bakuRiskVal>20 && bakuRiskVal<30) {
        bakuRiskVal = 20;
        bakuRiskName = "�����I";
      }else if(bakuRiskVal>30) {
        bakuRiskVal = 30;
        bakuRiskName = "�����I";
      }
      //=====================
      
      /*
      //�g�J�@���s�����I
      String sql4 = "INSERT INTO PSRI02PF "
                  + "(RI01, RI02, RI03, RI04, RI05, RIPOLN, RIFILE, RI0204, RI0205, RI0206, RI0207, RI0208, RI0209 "
                  + ", RO0201, RO0202, RO0203, RO0204, RO0205, RO0206, RO0207, RO0208, RO0209, RO0210, RO0211, RO0212, RO0213, RTNR02) " 
                  + "VALUES "
                  + "('"+ custId +"', 1090917, 150410, 'RY', 'QUSER', '', '"+ orderNo +"', '15', 'RYB', '1', '"+ nationCode +"', '', '' "
                  + ", 15, '�����I', 30, '�����I', 10, '�C���I', "+ nationRiskVal +", '"+ nationRiskName +"', 16, '"+ targetRiskName +"', 'A221331647', 'CS0331H111', 'CS0331H111', '') ";
      String updPSRI02 = dbAS400.execFromPool(sql4);
      if(StringUtils.equals(updPSRI02, "0")) {
        arrNoProcess[0] = orderNo;
        arrNoProcess[1] = projectId;
        arrNoProcess[2] = house;
        arrNoProcess[3] = custId;
        arrNoProcess[4] = custMame;
        arrNoProcess[5] = "�]�G���g�JPSRI02PF";
        listNoProcess.add(arrNoProcess);
      }
      */
      
      //����X��USER�T�{      
      String[] tempArr = new String[20];
      tempArr[0] = orderNo;
      tempArr[1] = projectId;
      tempArr[2] = house;
      tempArr[3] = car;
      tempArr[4] = custName;
      tempArr[5] = custId;
      tempArr[6] = orderDate;
      tempArr[7] = Double.toString(targetRiskVal);
      tempArr[8] = targetRiskName;
      tempArr[9] = indCode + "/" + arrT091[0][0].trim();
      tempArr[10] = managerFlag + "/" + arrT091[0][1].trim();
      tempArr[11] = nationName;
      tempArr[12] = Double.toString(bakuRiskVal);
      tempArr[13] = bakuRiskName;
      tempArr[14] = "30";
      tempArr[15] = "�����I";
      tempArr[16] = "10";
      tempArr[17] = "�C���I";
      tempArr[18] = nationRiskVal;
      tempArr[19] = nationRiskName;
      listImport.add(tempArr);
      
      intRow++;
    }
    
    //��Xto�e��
    String[][] arrRS = new String[listImport.size()][];
    arrRS = (String[][]) listImport.toArray(arrRS);
    this.setTableData("ResultTable", arrRS);
    this.setValue("TableCount", Integer.toString(arrRS.length));

    //����Excel
    Excel.setProperty("DisplayAlerts", new Variant(false));
    Excel.invoke("Quit", new Variant[] {});
    ComThread.Release();
  }
  
  private String isNull(String text) {
    if (StringUtils.isBlank(text) || StringUtils.equals(text.toLowerCase(), "null")) return "";
    
    return text.trim();
  }

  private boolean isBlank(String test) {
    boolean rs = false;
    if (StringUtils.isBlank(test) || StringUtils.equals(test, "null")) rs = true;
    return rs;
  }

  public boolean ����ˮ�() throws Throwable {
    �v�jDate = this.getValue("StartDate").trim();
    �w�oDate = this.getValue("EndDate").trim();
    if (StringUtils.isBlank(�v�jDate) || StringUtils.isBlank(�w�oDate)) {
      message("���!");
      return false;
    }

    if (�v�jDate.length() == 8) {
      if (!check.isACDay(�v�jDate)) return false;
      �v�jDate = kUtil.formatACDate(�v�jDate, "/");
    } else if (�v�jDate.length() == 10 && StringUtils.contains(�v�jDate, "/")) {

    } else {
      message("�v�jDate����榡���~");
      return false;
    }

    if (�w�oDate.length() == 8) {
      if (!check.isACDay(�w�oDate)) return false;
      �w�oDate = kUtil.formatACDate(�w�oDate, "/");
    } else if (�w�oDate.length() == 10 && StringUtils.contains(�w�oDate, "/")) {

    } else {
      message("�w�oDate����榡���~");
      return false;
    }

    return true;
  }

  public String getInformation() {
    return "---------------button1(\u67e5\u8a62\u7b2c\u4e00\u7b46\u6c92\u884c\u696d\u5225\u4e3b\u8981\u5ba2\u6236\u4f5c\u696d).defaultValue()----------------";
  }
}
