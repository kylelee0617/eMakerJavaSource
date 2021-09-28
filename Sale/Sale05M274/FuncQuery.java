package Sale.Sale05M274;
import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

public class FuncQuery extends bTransaction{
  public boolean action(String value)throws Throwable{
    // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
    // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
    // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL" (�C�L�w���������C�L���s) �䤤���@
    String stringDate = getQueryValue("ContractDate").trim();
    // Start �ק���:20100409 ���u�s��:B3774
    String retDate[]    = convert.StringToken(stringDate, "and");
    /*
    if(stringDate.length() > 0){
      if(stringDate.replaceAll("/","").length() != 8){
        message("[ñ�����] �榡���~(YYYY/MM/DD)!");
        return false;
      }
      Farglory.util.FargloryUtil  exeFun  =  new  Farglory.util.FargloryUtil();
      stringDate = exeFun.getDateAC(stringDate, "ñ�����");
      if(stringDate.length() != 10){
        message(stringDate);
        return false;
      }
    */  
    if(!"and".equals(stringDate)  &&  retDate.length == 2){
      Farglory.util.FargloryUtil  exeFun  =  new  Farglory.util.FargloryUtil();
      String stringDateS = retDate[0].trim().replaceAll("/","");
      String stringDateE = retDate[1].trim().replaceAll("/","");
      //
      stringDateS = exeFun.getDateAC(stringDateS, "ñ�����-�_");
      if(stringDateS.length() != 10){
        message(stringDateS);
        return false;
      }
      stringDateE = exeFun.getDateAC(stringDateE, "ñ�����-��");
      if(stringDateE.length() != 10){
        message(stringDateE);
        return false;
      }
      //
      if(stringDateS.compareTo(stringDateE) > 0){
        message("[ñ�����] �_�����~!");
        return false;
      }
      stringDate = stringDateS + " and " + stringDateE;
    // End �ק���:20100409 ���u�s��:B3774 
      setQueryValue("ContractDate", stringDate);
    }
    //
    // Start �ק���:20100120 ���u�s��:B3774
    // Start �ק���:20100409 ���u�s��:B3774
    /*
    String stringF_INP_TIME = getQueryValue("table19.F_INP_TIME").trim();
    if(stringF_INP_TIME.length() > 0){
      if(!check.isACDay(stringF_INP_TIME)){
        message("[ñ�֤��] �榡���~(YYYYMMDD)!");
        return false;
      }
    */
    stringDate = getQueryValue("table19.F_INP_TIME").trim();
    retDate     = convert.StringToken(stringDate, "and");
    if(!"and".equals(stringDate)  &&  retDate.length == 2){
      Farglory.util.FargloryUtil  exeFun  =  new  Farglory.util.FargloryUtil();
      String stringDateS = retDate[0].trim().replaceAll("/","");
      String stringDateE = retDate[1].trim().replaceAll("/","");
      //
      if(stringDateS.length() != 8  &&  stringDateS.length() != 17){
        message("[ñ�֤��]-�_ �榡���~!");
        return false;
      }
      if(stringDateS.length() == 8){
        stringDateS = stringDateS + " 00:00:00";
      }
      if(stringDateE.length() != 8  &&  stringDateE.length() != 17){
        message("[ñ�֤��]-�� �榡���~!");
        return false;
      }
      if(stringDateE.length() == 8){
        stringDateE = stringDateE + " 23:59:59";
      }
      //
      if(stringDateS.compareTo(stringDateE) > 0){
        message("[ñ�֤��] �_�����~!");
        return false;
      }
      stringDate = stringDateS + " and " + stringDateE;
      setQueryValue("table19.F_INP_TIME", stringDate);
    // End �ק���:20100409 ���u�s��:B3774 
    }
    // End �ק���:20100120 ���u�s��:B3774
    return true;
  }
  public String getInformation(){
    return "---------------\u67e5\u8a62\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
