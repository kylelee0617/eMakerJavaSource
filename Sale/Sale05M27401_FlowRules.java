// 20091023 �j�� �G��mark
package Sale;
import     jcx.jform.bRule;
import     java.util.*;

public class Sale05M27401_FlowRules extends bRule{
  public Vector getIDs(String value) throws Throwable{
    // �^�ǭȬ� Vector contails �ŦX�o���W�檺�b��
    // value �� "(Sale05M27401)���~�~�޸s��...��
    Vector id = new Vector();
    value = value.replaceAll("�s��","");
    String stringSql                 = "";
    String retData[][]               = null;
    String retTempData[][]     = null;
    String stringSubSql          = ""; // �U�g��ɧO�P�_��
    String stringContractNo   = getData("ContractNo").trim();
    String stringCompanyCd = getData("CompanyCd").trim(); // �ק���:20120517 ���u�s��:B3774
    // Start �ק���:20110610 ���u�s��:B3774
    String stringSignType    = "";
    //
    stringSql = "select SignType "+
              "from Sale05M228 "+
              "where FlowFormID='"+value.substring(1, 13)+"' "+
              "and CensorName='"+value.substring(14, value.length())+"'";
    retData = getTalk("Sale").queryFromPool(stringSql);
    if(retData.length > 0){
      stringSignType = retData[0][0];
    }
    // End �ק���:20110610 ���u�s��:B3774
    //
    if("(Sale05M27401)���~�~��".equals(value)  ||  "(Sale05M27401)��~�g��".equals(value)  ||  "(Sale05M27401)�}�o�g��".equals(value)  ||  
      "(Sale05M27401)�]�ȸg��".equals(value)  ||  "(Sale05M27401)�]�ȥN�P".equals(value)){
      stringSubSql = "and (isnull(T225.BuildingCd,'')='' or "+
                         "T225.BuildingCd in (select distinct substring((case when isnull(T276.SecPosition,'')!='' then T276.SecPosition else T275.Position end),1,1) "+
                                          "from Sale05M275_New T275 left join Sale05M276 T276 "+
                                          "on T275.ContractNo=T276.ContractNo "+
                                          // Start �ק���:20120518 ���u�s��:B3774
                                          //"where T275.ContractNo='"+stringContractNo+"')) ";
                                          "and T275.CompanyCd=T276.CompanyCd "+
                                          "where T275.ContractNo='"+stringContractNo+"' "+
                                          "and T275.CompanyCd='"+stringCompanyCd+"')) ";
                                          // End �ק���:20120518 ���u�s��:B3774
    }
    //
    // Start �ק���:20091211 ���u�s��:B3774
    /*
    stringSql = "select (case when isnull(T303.Agent,'')!='' then T303.Agent else T225.EmployeeNo end), "+
              "(case when isnull(T303.Agent,'')!='' then 'Agent' else '' end) "+
              "from Sale05M225 T225 left join (select EmpNo, Agent "+
                                          "from Sale05M303 "+
                                          "where REPLACE(convert(char(16),getdate(),120),'-','/') "+
                                          "between StartDate+' '+StartTime and EndDate+' '+EndTime) T303 "+
                                    "on T225.EmployeeNo=T303.EmpNo, Sale05M228 T228 "+
              "where T225.FlowFormID=T228.FlowFormID "+
              "and T225.CensorSeq=T228.CensorSeq "+
              "and T225.FlowFormID='"+value.substring(1, 13)+"' "+
              "and T225.ProjectID1='"+getData("ProjectID1").trim()+"' "+
              stringSubSql+
              "and convert(char(10),getdate(),111) between T225.StartDate and T225.EndDate "+
              "and T228.CensorName='"+value.substring(14, value.length())+"' "+
              "order by T225.SeqNo";
    retData = getTalk("Sale").queryFromPool(stringSql);
    for(int i=0; i<retData.length; i++){
      if("Agent".equals(retData[i][1].trim())){
        stringSql = "select PROXY_EMP_NO "+
                  "FROM VREST "+
                  "where EMP_NO='"+retData[i][0].trim()+"' "+
                  "and REPLACE(convert(char(16),getdate(),120),'-','') "+
                  "between convert(char(8),convert(int,RTRIM(DATE_F))+19110000)+' '+substring(TIME_F,1,2)+':'+substring(TIME_F,3,2) "+
                  "and convert(char(8),convert(int,RTRIM(DATE_T))+19110000)+' '+substring(TIME_T,1,2)+':'+substring(TIME_T,3,2)";
        retTempData = getTalk("FE3D").queryFromPool(stringSql);
        if(retTempData.length == 0){
          id.addElement(retData[i][0].trim());
        }
      }else{
        id.addElement(retData[i][0].trim());
      }
    }
    */
    stringSql = "select T225.EmployeeNo, (case when isnull(T303.Agent,'')!='' then T303.Agent else T225.EmployeeNo end), "+
              "(case when isnull(T303.Agent,'')!='' then 'Agent' else '' end) "+
              "from Sale05M225 T225 left join (select EmpNo, Agent "+
                                          "from Sale05M303 "+
                                          "where REPLACE(convert(char(16),getdate(),120),'-','/') "+
                                          "between StartDate+' '+StartTime and EndDate+' '+EndTime) T303 "+
                                    "on T225.EmployeeNo=T303.EmpNo, Sale05M228 T228 "+
              "where T225.FlowFormID=T228.FlowFormID "+
              "and T225.CensorSeq=T228.CensorSeq "+
              "and T225.FlowFormID='"+value.substring(1, 13)+"' "+
              "and T225.ProjectID1='"+getData("ProjectID1").trim()+"' "+
              stringSubSql+
              "and convert(char(10),getdate(),111) between T225.StartDate and T225.EndDate "+
              "and T228.CensorName='"+value.substring(14, value.length())+"' "+
              "order by T225.SeqNo";
    retData = getTalk("Sale").queryFromPool(stringSql,30);
    
    for(int i=0; i<retData.length; i++){
      stringSql = "select PROXY_EMP_NO "+
                "FROM VREST "+
                "where EMP_NO='"+retData[i][0].trim()+"' "+
                "and REPLACE(convert(char(16),getdate(),120),'-','') "+
                "between convert(char(8),convert(int,RTRIM(DATE_F))+19110000)+' '+substring(TIME_F,1,2)+':'+substring(TIME_F,3,2) "+
                "and convert(char(8),convert(int,RTRIM(DATE_T))+19110000)+' '+substring(TIME_T,1,2)+':'+substring(TIME_T,3,2)";
      retTempData = getTalk("FE3D").queryFromPool(stringSql,30);
      if(retTempData.length > 0){ // ��ܭ�ñ�֤H�а��X�t
        // Start �ק���:20110610 ���u�s��:B3774
        //if(id.indexOf(retTempData[0][0].trim()) == -1){ // �ק���:20100716 ���u�s��:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retTempData[0][0].trim()) == -1){
        // End �ק���:20110610 ���u�s��:B3774
          id.addElement(retTempData[0][0].trim());
        } // �ק���:20100716 ���u�s��:B3774
      }else if("Agent".equals(retData[i][2].trim())){ // ���]�w�N�z�H
        // Start �ק���:20110610 ���u�s��:B3774
        //if(id.indexOf(retData[i][1].trim()) == -1){ // �ק���:20100716 ���u�s��:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retData[i][1].trim()) == -1){
        // End �ק���:20110610 ���u�s��:B3774
          id.addElement(retData[i][1].trim());
        } // �ק���:20100716 ���u�s��:B3774
      }else{
        // Start �ק���:20110610 ���u�s��:B3774
        //if(id.indexOf(retData[i][0].trim()) == -1){ // �ק���:20100716 ���u�s��:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retData[i][0].trim()) == -1){
        // End �ק���:20110610 ���u�s��:B3774
          id.addElement(retData[i][0].trim());
        } // �ק���:20100716 ���u�s��:B3774
      }
    }
    // End �ק���:20091211 ���u�s��:B3774
    //
    return id;
  }

  public String getInformation(){
    return "---------------(Sale05M27401)�s��.Rule()----------------";
  }
}