// 20091023 大改 故不mark
package Sale;
import     jcx.jform.bRule;
import     java.util.*;

public class Sale05M27401_FlowRules extends bRule{
  public Vector getIDs(String value) throws Throwable{
    // 回傳值為 Vector contails 符合這條規格的帳號
    // value 為 "(Sale05M27401)內業業管群組...等
    Vector id = new Vector();
    value = value.replaceAll("群組","");
    String stringSql                 = "";
    String retData[][]               = null;
    String retTempData[][]     = null;
    String stringSubSql          = ""; // 各經辦棟別判斷用
    String stringContractNo   = getData("ContractNo").trim();
    String stringCompanyCd = getData("CompanyCd").trim(); // 修改日期:20120517 員工編號:B3774
    // Start 修改日期:20110610 員工編號:B3774
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
    // End 修改日期:20110610 員工編號:B3774
    //
    if("(Sale05M27401)內業業管".equals(value)  ||  "(Sale05M27401)營業經辦".equals(value)  ||  "(Sale05M27401)開發經辦".equals(value)  ||  
      "(Sale05M27401)財務經辦".equals(value)  ||  "(Sale05M27401)財務代銷".equals(value)){
      stringSubSql = "and (isnull(T225.BuildingCd,'')='' or "+
                         "T225.BuildingCd in (select distinct substring((case when isnull(T276.SecPosition,'')!='' then T276.SecPosition else T275.Position end),1,1) "+
                                          "from Sale05M275_New T275 left join Sale05M276 T276 "+
                                          "on T275.ContractNo=T276.ContractNo "+
                                          // Start 修改日期:20120518 員工編號:B3774
                                          //"where T275.ContractNo='"+stringContractNo+"')) ";
                                          "and T275.CompanyCd=T276.CompanyCd "+
                                          "where T275.ContractNo='"+stringContractNo+"' "+
                                          "and T275.CompanyCd='"+stringCompanyCd+"')) ";
                                          // End 修改日期:20120518 員工編號:B3774
    }
    //
    // Start 修改日期:20091211 員工編號:B3774
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
      if(retTempData.length > 0){ // 表示原簽核人請假出差
        // Start 修改日期:20110610 員工編號:B3774
        //if(id.indexOf(retTempData[0][0].trim()) == -1){ // 修改日期:20100716 員工編號:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retTempData[0][0].trim()) == -1){
        // End 修改日期:20110610 員工編號:B3774
          id.addElement(retTempData[0][0].trim());
        } // 修改日期:20100716 員工編號:B3774
      }else if("Agent".equals(retData[i][2].trim())){ // 有設定代理人
        // Start 修改日期:20110610 員工編號:B3774
        //if(id.indexOf(retData[i][1].trim()) == -1){ // 修改日期:20100716 員工編號:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retData[i][1].trim()) == -1){
        // End 修改日期:20110610 員工編號:B3774
          id.addElement(retData[i][1].trim());
        } // 修改日期:20100716 員工編號:B3774
      }else{
        // Start 修改日期:20110610 員工編號:B3774
        //if(id.indexOf(retData[i][0].trim()) == -1){ // 修改日期:20100716 員工編號:B3774
        if("StringSign".equals(stringSignType)  ||  id.indexOf(retData[i][0].trim()) == -1){
        // End 修改日期:20110610 員工編號:B3774
          id.addElement(retData[i][0].trim());
        } // 修改日期:20100716 員工編號:B3774
      }
    }
    // End 修改日期:20091211 員工編號:B3774
    //
    return id;
  }

  public String getInformation(){
    return "---------------(Sale05M27401)群組.Rule()----------------";
  }
}