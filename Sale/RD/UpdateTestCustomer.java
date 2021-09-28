package Sale.RD;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import jcx.db.talk;
import jcx.jform.bproc;

public class UpdateTestCustomer extends bproc{
  public String getDefaultValue(String value)throws Throwable{
    
    String[] namelist = {
        "�^�~�F;04322046"
        ,"�x�n�q;22099131"
        ,"�Τ@���~;73251209"
        ,"����G��;00160202"
        ,"�T������;00166402"
        ,"��������~;00168529"
        ,"�d�z�F�_����;00172002"
        ,"�F�h�����|��;00181804"
        ,"���ݭs;F221371897"
        ,"�x�ݯq;A120831833"
        ,"�����P;E123606925"
        ,"��C��;U121153808"
        ,"�W�¤���g;F220492173"
        ,"��������;Q223996005"
        ,"���ݥ�;F199927685"
        ,"�P����;R222761429"
        ,"���p�K;F126393588"
        ,"�\��;K05719268"
        ,"��~��;KJ0351263"
        ,"���ө�;K02898965"
        ,"ù���P;K02892375"
        ,"�H����;FC01389035"
        ,"���^��;B274089637"};
    
    StringBuilder inIdList = new StringBuilder();
    StringBuilder inNameList = new StringBuilder();
    for(int ii=0 ; ii<namelist.length ; ii++) {
      String[] nameID = namelist[ii].split(";");
      if(ii > 0) inNameList.append(",");
      inIdList.append("'").append(nameID[1]).append("'");
      inNameList.append("'").append(nameID[0]).append("'");
    }

    String serverIP = get("serverIP").toString().trim();
    System.out.println("serverIP>>>" + serverIP);
    if ( serverIP.contains("172.16.") ) {
      System.out.println(">>>��������<<<"); 
      return value;
    }

    talk dbSale =  getTalk("pw0d") ;
    String projectIds = getValue("ProjectIDs").trim();
    if ( "".equals(projectIds) ) {
      message("��J�קO");
      return value;
    }

    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyyMMddhhmmss");

    Vector vectorSql = new  Vector( ) ;
    String sql = "delete from query_log where project_id in ('" + projectIds.replaceAll("," , "','") + "') "
        + "and ( query_id in (" + inIdList.toString() + ") or NAME in (" + inNameList.toString() + ") ) ";
    vectorSql.add(sql) ;

    
    String[] spProjectids = projectIds.split(",");
    for (int i=0 ; i < spProjectids.length ; i++) {
      String thisPID = spProjectids[i].trim();
      Date nowDate= new Date();
      String qidL = sdFormat.format(nowDate) ;
      System.out.println(">>>qidL>>>" + qidL);

      //�k�H
      sql = "INSERT INTO query_log VALUES ( '"+ qidL + Integer.toString(i) +  "00'  ,'91','" + thisPID + "','L','1','TWN','�^�~�F','04322046','1985/05/15','M','44',1,9,' ','Y','N','Y','Y',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�x�n�q','22099131','1987/02/21','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�Τ@���~','73251209','1967/08/25','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','����G��','00160202','1985/05/15','M','44',1,9,' ','Y','Y','Y','Y',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�T������','00166402','1985/05/15','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','��������~','00168529','1985/05/15','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�d�z�F�_����','00172002','1985/05/15','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�F�h�����|��','00181804','1985/05/15','M','44',1,9,' ','N','N','N','N',' NONE ',' ','B03918','172.16.17.183','2019/04/29','10:50:54','','')";
      vectorSql.add(sql) ;

      //PEPS
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','���ݭs ','F221371897','1963/01/28','F','44',3,40,' ','N','Y','N','N','NONEHTML','','B03621','172.16.8.144','2019/04/26','17:48:17','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�x�ݯq','A120831833','1970/11/03','M','44',3,39,' ','N','Y','N','N','NONE HTML','','B03901','172.16.8.144','2019/06/23','18:00:54','','')";
      vectorSql.add(sql) ;

      //���
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�����P','E123606925','1989/12/22','M','18',3,39,' ','N','Y','N','N','NONE HTML','','B03967','172.16.8.144','2019/05/29','19:23:10','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','��C��','U121153808','1978/11/13','M','21',3,35,' ','N','Y','N','N','NONE HTML','','B03967','172.16.8.144','2019/06/21','12:49:58','','')";
      vectorSql.add(sql) ;

      //�w�f
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�W�¤���g','F220492173','1989/12/22','M','44',18,277,' ','Y','Y','Y','Y','NONE HTML','','B03554','172.16.8.144','2019/04/26','17:48:59','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','��������','Q223996005','1990/12/20','F','44',3,40,' ','Y','Y','Y','Y','  ','','B04056','172.16.8.144','2019/04/25','10:08:39','','')";
      vectorSql.add(sql) ;

      //����
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','���ݥ�','F199927685','1985/05/15','M','44',1,9,' ','N','N','N','N','  ' , '  ' ,'B03918','172.16.17.183','2019/04/29','10:51:11','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','�P����','R222761429','1982/05/25','F','44',3,45,' ','N','N','N','N','  ','','B03614','172.16.8.144','2019/04/29','16:44:49','','')";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','���p�K','F126393588','1918/01/01','M','44',1,9,' ','N','N','N','N','  ' , '  ' ,'B03918','172.16.17.183','2019/04/23','15:04:17','','')";
      vectorSql.add(sql) ;

      //�n��H
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc) ,'91','" + thisPID + "','F','1','HKG','�\��','K05719268','1967/01/07','F','51',57,3,'�d�r��43�����M�y605��','N','Y','Y','N','',null,'B03927','172.16.8.144','2019/12/14','15:31:26',null,null)";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc) ,'91','" + thisPID + "','F','1','HKG','��~��','KJ0351263','1966/05/07','M','12',57,1,'�d�r��43�����M�y605��','N','Y','N','N','  ',null,'B03927','172.16.8.144','2019/12/14','15:36:32',null,null)";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc) ,'91','" + thisPID + "','F','1','HKG','���ө�','K02898965','1964/03/07','M','99',57,1,'�E�s���w���w�|��1347��','Y','N','N','N','  ',null,'B03927','172.16.8.144','2019/12/14','15:39:32',null,null)";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc) ,'91','" + thisPID + "','F','1','HKG','ù���P','K02892375','1966/10/20','F','99',57,1,'����E�s���w���w�|��1347��','N','N','N','N','  ',null,'B03927','172.16.8.144','2019/12/14','15:42:40',null,null)";
      vectorSql.add(sql) ;
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc) ,'91','" + thisPID + "','F','1','MYS','�H����','FC01389035','1965/01/21','M','18',1,11,'��q��5��7��3��','N','N','N','N','  ',null,'B03593','172.16.8.144','2020/06/14','12:15:55',null,null)";
      vectorSql.add(sql) ;

      //����
      sql = "INSERT INTO query_log VALUES ( (select top 1 CONVERT(VARCHAR , CAST(QID AS decimal(17,0))+1) from query_log order by CAST(QID AS decimal(17,0)) desc)  ,'91','" + thisPID + "','L','1','TWN','���^��','B274089637','1956/08/31','F','43',1,9,' ','Y','Y','Y','Y','  ','','B03918','172.16.17.183','2019/04/29','11:21:47','','')";
      vectorSql.add(sql) ;
    }

    if(vectorSql.size()  >  0) {
      dbSale.execFromPool((String[])  vectorSql.toArray(new  String[0])) ;
    }

    message("����");
    return value;
  }
  public String getInformation(){
    return "---------------UpdateTestCustomer(\u628a\u90a3\u4e9b\u4eba\u7d66\u6211\u585e\u9032\u53bb!).defaultValue()----------------";
  }
}
