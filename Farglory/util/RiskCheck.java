package Farglory.util;

import java.text.SimpleDateFormat;
import java.util.*;
import com.fglife.risk.*;
import jcx.db.talk;
import jcx.jform.bvalidate;
import jcx.jform.sproc;


public class RiskCheck extends sproc {
  talk dbSale = null;
  talk dbEMail = null;
  talk dbBen = null;
  talk dbEIP = null;
  
  RiskCheckBean bean = null;
  boolean isTest = true;
  String stringSQL = "";
  String testFlag = "";
  String userNo = "";
  String empNo = "";
  String sysdate = "";
  String systime = "";
  String actionNo = "";
  String strHouse = "";
  String strCar = "";

  public String processRisk() throws Throwable {
    String sysType = "RYB";// ���ʲ���PB �P��C
    String[][] retCustom = this.bean.getRetCustom();
    String[][] retSBen = this.bean.getRetSBen();
    String strProjectID1 = this.bean.getProjectID1();
    String strOrderNo = this.bean.getOrderNo();
    String strOrderDate = this.bean.getOrderDate();

    System.out.println("strOrderNo=====>" + strOrderNo);
    System.out.println("strProjectID1=====>" + strProjectID1);
    System.out.println("strOrderDate=====>" + strOrderDate);

    System.out.println("�s�J���I�p����q�H���-----------------------------------S");
    for (int i = 0; i < retSBen.length; i++) {
      boolean isNew = true;
      String[][] retBen = null;
      String id = retSBen[i][4];
      System.out.println("id:" + id);
      String sqlBen = "Select SHA02 FROM PSHAPF WHERE SHA06 = '" + id + "' And SHA00 = 'RY'";
      retBen = dbBen.queryFromPool(sqlBen);
      String beforeNo = "";

      System.out.println("retBen:" + retBen.length);
      if (retBen.length > 0) {
        beforeNo = retBen[0][0];
        isNew = false;
      }

      String ono = "";    //�q��s��
      String oid = "";    //�k�HID
      String oname = "";  //�k�HNAME
      ono = retSBen[i][0];
      oid = retSBen[i][1];
      System.out.println("ono:" + ono);
      System.out.println("oid:" + oid);
      String sql91name = "SELECT CustomName FROM Sale05M091 WHERE OrderNo= '" + ono + "' AND CustomNo = '" + oid + "' AND ISNULL(StatusCd , '') = '' ";
      String[][] ret91Name = null;
      ret91Name = dbSale.queryFromPool(sql91name);
      if (ret91Name.length > 0) {
        oname = ret91Name[0][0];
      }
      System.out.println("oname:" + oname);

      String fileType = retSBen[i][7];
      String name = retSBen[i][3];
      String idType = "1";
      String bdate = retSBen[i][5].trim();
      bdate = bdate.replace("/", "");
      if ("".equals(bdate)) {
        bdate = "0";
      }
      String nation = retSBen[i][6].trim();
      String nationCode = "TWN";
      String PDCZPFSql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + nation + "'";
      String[][] retPDCZPF = null;
      retPDCZPF = dbBen.queryFromPool(PDCZPFSql);
      nationCode = retPDCZPF[0][0].trim();
      String sqlInsert = "Insert into PSHAPF (SHA00, SHA02, SHA03, SHA04, SHA05, SHA06, SHA07, SHA08 " + ",SHA97, SHA98, SHA99, SHA100, SHA101, SHA102, SHA09, SHA10, SHA11, SHA12 "
          + ") VALUES (" + "'RY','" + strOrderNo + "','" + fileType + "','" + name + "','" + idType + "','" + id + "','" + bdate + "','" + nationCode + "'" + ",'" + empNo + "',"
          + sysdate + "," + systime + ",'" + empNo + "'," + sysdate + "," + systime + ",'R','3','" + oid + "','" + oname + "' )";
      String sqlupdate = "UPDATE PSHAPF SET SHA02='" + strOrderNo + "',SHA03='" + fileType + "', SHA04='" + name + "', SHA05 = '" + idType + "', SHA06 = '" + id + "', SHA07="
          + bdate + ",SHA08='" + nationCode + "'" + ",SHA100='" + empNo + "', SHA101 = " + sysdate + ", SHA102=" + systime + ", SHA09='R', SHA10='3', SHA11='" + oid
          + "', SHA12='" + oname + "' " + " Where SHA00 = 'RY' And SHA06 = '" + id + "'";
      try {
        if (isNew) {
          dbBen.execFromPool(sqlInsert);
        } else {
          dbBen.execFromPool(sqlupdate);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    System.out.println("�s�J���I�p����q�H���-----------------------------------E");
    
    System.out.println("�s�J���I�p��Ȥ���-----------------------------------S");
    ResourceBundle resource = ResourceBundle.getBundle("sale");
    String as400ip = resource.getString("AS400.IP");
    String as400account = resource.getString("AS400.ACCOUNT");
    String as400password = resource.getString("AS400.PASSWORD");
    String as400init = resource.getString("AS400.INIT");
    String cms00c = resource.getString("CMS00C.LIB");
    String blpc00a = resource.getString("BLPC00A.LIB");
    String psri02 = resource.getString("PSRI02.LIB");
    String riskValue = "";
    String riskPoint = "";
    String sqltopmanager = "";
    RPGAS400Interface ra = null;
    RPGAS400Interface rb = null;
    RPGAS400Interface rc = null;

    ArrayList list = new ArrayList();
    try {
      ra = new RPGCMS00C(as400ip, as400account, as400password);
      rb = new RPGBLPC00A(as400ip, as400account, as400password);
      rc = new RPGPSRI02(as400ip, as400account, as400password);

      System.out.println(" retCustom.length=====> :" + retCustom.length);
      String msgboxtext = "";
      String tmpMsgText = "";
      
      for (int i = 0; i < retCustom.length; i++) {  //start �Ȥ�for
        // 20200319 kyle : �L�o�w�Q���W���Ȥ�(���ˮ֭��I��)
        if ("C".equals(retCustom[i][23].trim())) continue;

        String strpositionCd = "8";
        if (!"".equals(retCustom[i][5].trim())) {
          strpositionCd = retCustom[i][5].trim();
        }
        sqltopmanager = " SELECT TOP 1 PositionCD, PName, ChairMan From A_Position " + " WHERE PositionCD = '" + strpositionCd + "'" + " ORDER BY PositionCD DESC ";
        String retPosition2[][] = dbSale.queryFromPool(sqltopmanager);
        String isManager = "N";
        if (retPosition2.length > 0) {
          isManager = retPosition2[0][2];
        }
        String type = "N";
        String sex = "";

        if (retCustom[i][5].trim().length() == 8) {
          type = "C";// N: �ӤH C: ���q F: �~��H
        } else {
          // �~��H(�H��O�P�_)
          if (!"���إ���".equals(retCustom[i][4].trim())) {
            type = "F";
          }
          // �ʧO
          if (retCustom[i][5].charAt(1) == '1') {
            sex = "M";
          } else if (retCustom[i][5].charAt(1) == '2') {
            sex = "F";
          }
        }
        // ���y��X(�Y�Źw�]TWN)
        String strCZ09 = "";
        if ("".equals(retCustom[i][4].trim())) {
          strCZ09 = "���إ���";
        } else {
          strCZ09 = retCustom[i][4].trim();
        }
        String strSaleSql = "SELECT CZ02 FROM PDCZPF WHERE CZ01='NATIONCODE' AND CZ09='" + strCZ09 + "'";
        String retCNYCode[][] = dbBen.queryFromPool(strSaleSql);
        String cnyCode = retCNYCode[0][0].trim();
        System.out.println("cnyCode=====> :" + cnyCode);
        LinkedHashMap map = new LinkedHashMap();
        map.put("INAME", retCustom[i][6]); // �Ȥ�m�W
        map.put("IDATE", retCustom[i][8].replace("/", ""));// �ͤ�
        System.out.println("IDATE=====> :" + retCustom[i][8].replace("/", ""));
        map.put("ID", retCustom[i][5]); // �����Ҹ�
        map.put("IAD1", retCustom[i][11]);// �a�} 1
        map.put("IAD2", retCustom[i][12]);// �a�} 2
        map.put("IAD3", retCustom[i][13]);// �a�} 3
        map.put("IADD", retCustom[i][12].trim() + retCustom[i][13].trim() + retCustom[i][14].trim());// ���a�}
        map.put("IZIP", retCustom[i][11]);// �l���ϸ�
        map.put("ITELO", retCustom[i][16]);// ���q�q��
        map.put("ITELH", retCustom[i][17]);// ��a�q��
        map.put("TYPE", type);// N: �ӤH C: ���q
        map.put("SEX", sex);// �ʧO M,F
        map.put("CNY", cnyCode);// ���y
        map.put("JOB", "");// ¾�~�N�X
        map.put("VOC", "");// ��~�O
        map.put("CUST", " ");// ���@�ŧi
        map.put("IESTD", " "); // �]�w���
        map.put("IEXEC", isManager);// �����޲z�H Y/N
        map.put("CNY2", " ");// ���y 2
        map.put("CNY3", " ");// ���y 3
        map.put("ICHGD", "");// �ܧ�n�O���
        map.put("CHGNO", empNo);// ���ʤH�����s
        map.put("RTCOD", "");// �^�нX
        map.put("INSN", "");// �Ȥ�s��

        LinkedHashMap mapb = new LinkedHashMap();
        mapb.put("INSID", retCustom[i][5]); // �����Ҹ�
        mapb.put("SYSTEM", sysType); // �t�ΧO
        mapb.put("CHGNO", empNo);// ���ʤH�����s
        mapb.put("CAPTION", strOrderNo);// ����
        mapb.put("STRDATE", "0");// �_�l��
        mapb.put("ENDDATE", "0");// �פ��
        mapb.put("RTCOD", "");// �^�нX

        LinkedHashMap mapc = new LinkedHashMap();
        mapc.put("RI0201", retCustom[i][5]); // �����Ҹ�
        mapc.put("RI0202", "RY");// �t�ΧO
        mapc.put("RI0203", "Y");
        mapc.put("RIPOLN", "");
        mapc.put("RIFILE", strOrderNo);// �ӷ��׸�
        mapc.put("RI0204", "");//
        mapc.put("RI0205", "");//
        mapc.put("RI0206", "");//
        mapc.put("RI0207", "");//
        mapc.put("RI0208", "");//
        mapc.put("RI0209", "");//
        mapc.put("RO0201", "0");//
        mapc.put("RO0202", "");//
        mapc.put("RO0203", "0");//
        mapc.put("RO0204", "");//
        mapc.put("RO0205", "0");//
        mapc.put("RO0206", "");//
        mapc.put("RO0207", "0");//
        mapc.put("RO0208", "");//
        mapc.put("RO0209", "0");//
        mapc.put("RO0210", "");//
        mapc.put("RO0211", "");//
        mapc.put("RO0212", "");//
        mapc.put("RO0213", "");//
        mapc.put("RTNR02", "");// �^�нX

        boolean a = ra.invoke(as400init, cms00c, map);
        System.out.println("RTCODE:" + ra.getResult()[22]);
        boolean b = rb.invoke(as400init, blpc00a, mapb);
        System.out.println("RTCODE:" + rb.getResult()[6]);
        boolean c = rc.invoke(as400init, psri02, mapc);

        // RA ERROR MSG
        String resultMsg = "";
        if ("1".equals(ra.getResult()[22])) {
          resultMsg = "���O��N��C";
        } else if ("2".equals(ra.getResult()[22])) {
          resultMsg = "���O��N�����Ңע��ˮ֦��~�Ψ����Ҥ��i�ť�";
        } else if ("3".equals(ra.getResult()[22])) {
          resultMsg = "���O��N�ͤ餣�i��0";
        } else if ("4".equals(ra.getResult()[22])) {
          resultMsg = "���O��N����榡���~";
        } else if ("5".equals(ra.getResult()[22])) {
          resultMsg = "�m�W���i�ť�";
        } else if ("6".equals(ra.getResult()[22])) {
          resultMsg = "�a�}�ˮֶl�Ϥ��ŦX";
        } else {
          resultMsg = "" + rc.getResult()[20];
        }

        System.out.println("19�~�����I�� :" + rc.getResult()[19]);
        System.out.println("20�~�����I���� :" + rc.getResult()[20]);

        riskPoint = rc.getResult()[19].toString().trim();
        riskValue = rc.getResult()[20].toString().trim();
        System.out.println("RiskValue=====>" + riskValue);
        msgboxtext += "�Ȥ� " + retCustom[i][6] + " �~�����I���� :" + resultMsg + "\n";
        tmpMsgText += retCustom[i][6] + "  " + resultMsg.trim() + "\n";

        HashMap m = new HashMap();
        m.put("p01", strProjectID1);
        m.put("p02", strHouse.toUpperCase());
        m.put("p025", strCar.toUpperCase());
        m.put("p03", retCustom[i][6]);
        m.put("p035", retCustom[i][5]);
        m.put("p04", strOrderDate);
        m.put("p05", riskPoint);
        m.put("p06", riskValue.replace("���I", ""));
        m.put("riskValue", riskValue);
        list.add(m);
        
      } //End �Ȥ�for
      ra.disconnect();
      
      //��s�Ȥ᭷�I��
      if(this.bean.isUpdSale05M091()) {
        this.updSaleM091(list);
      }

      messagebox(msgboxtext);
      setValue("RiskCheckRS", tmpMsgText);
    } catch (Exception e) {
      e.printStackTrace();
      ra.disconnect();
    }
    System.out.println("�s�J���I�p��Ȥ���-----------------------------------E");

    //TODO: insert into Sale05M070
    this.insSale05M070(list);
    
    //TODO: �o�eMAIL
    if(bean.isSendMail()) {
      this.sendMail(list);
    }
    
    return "0";
  }
  
  //��empNo
  public void getEMPNO() throws Throwable {
    stringSQL = "SELECT EMPNO FROM FGEMPMAP where FGEMPNO ='" + this.userNo + "'";
    String[][] retEip = dbEIP.queryFromPool(stringSQL);
    if (retEip.length > 0) {
      this.empNo = retEip[0][0];
    }
    System.out.println("empNo>>>" + this.empNo);
  }

  //�t�Τ���ɶ�
  public void getDateTime() throws Throwable {
    sysdate = new SimpleDateFormat("yyyyMMdd").format(getDate());
    systime = new SimpleDateFormat("HHmmss").format(getDate());
  }
  
  //actionNo
  public void getActionNo() throws Throwable {
    String ram = "";
    Random random = new Random();
    for (int i = 0; i < 4; i++) {
      ram += String.valueOf(random.nextInt(10));
    }
    this.actionNo = this.sysdate + this.systime + ram;
    System.out.println("strActionNo>>>" + this.actionNo);
  }
  
  //�ɼӧO
  public void getHouseCar() throws Throwable {
    String sql092 = "SELECT HouseCar,Position FROM  Sale05M092 WHERE OrderNo = '" + this.bean.getOrderNo() + "' ORDER BY RecordNo";
    String[][] retPosition = dbSale.queryFromPool(sql092);
    if (retPosition.length > 0) {
      for (int a = 0; a < retPosition.length; a++) {
        if ("House".equals(retPosition[a][0].trim())) {
          if ("".equals(strHouse)) {
            strHouse = retPosition[a][1].trim();
          } else {
            strHouse = strHouse + "," + retPosition[a][1].trim();
          }
        } else {
          if ("".equals(strCar)) {
            strCar = retPosition[a][1].trim();
          } else {
            strCar = strCar + "," + retPosition[a][1].trim();
          }
        }
      }
    }
  }
  
  
  /**
   * �^�gSale05M091 �Ȥ᭷�I��
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String updSaleM091(List customList) throws Throwable {
    System.out.println("�^�g05M091���----------" + customList.size() + "------------------S");
    
    Transaction trans = new Transaction();
    for(int ii=0 ; ii<customList.size() ; ii++) {
      HashMap data = (HashMap)customList.get(ii);
      String M091Sql = "UPDATE Sale05M091 SET RiskValue = '" + data.get("riskValue").toString().trim() + "' "
          + "WHERE OrderNo = '" + this.bean.getOrderNo() + "' AND CustomNo = '" + data.get("p035").toString().trim() + "' and ISNULL(StatusCd , '') = '';  ";
      trans.append(M091Sql);
    }
    trans.close();
    dbSale.execFromPool(trans.getString());
    
    System.out.println("�^�g05M091���----------" + customList.size() + "------------------E");
    return "0";
  }
  
  /**
   * �g�JSale05M070
   * 
   * @param customList
   * @return
   * @throws Throwable
   */
  public String insSale05M070(List customList) throws Throwable {
    System.out.println("�s�J05M070���-----------------------------------S");
    
    // �Ǹ�
    int intRecordNo = 1;
    String[][] ret05M070;
    stringSQL = "SELECT MAX(RecordNo) AS MaxNo FROM Sale05M070 WHERE OrderNo ='" + this.bean.getOrderNo() + "'";
    ret05M070 = dbSale.queryFromPool(stringSQL);
    if (!"".equals(ret05M070[0][0].trim())) {
      intRecordNo = Integer.parseInt(ret05M070[0][0].trim()) + 1;
    }
    System.out.println("insSale05M070 intRecordNo >>>" + intRecordNo);
    
    Transaction trans = new Transaction();
    for(int ii=0 ; ii<customList.size() ; ii++) {
      HashMap data = (HashMap)customList.get(ii);
      String strCustomNo = data.get("p035").toString().trim();
      String strCustomName = data.get("p03").toString().trim();
      String riskValue = data.get("riskValue").toString().trim();
      stringSQL = "INSERT INTO Sale05M070 (OrderNo,ProjectID1,RecordNo,ActionNo,Func,RecordType,ActionName,RecordDesc,CustomID,CustomName,OrderDate,SHB00,SHB06A,SHB06B,SHB06,SHB97, SHB98, SHB99) VALUES ('"
          + this.bean.getOrderNo() + "','" + this.bean.getProjectID1() + "','" + intRecordNo + "','" + actionNo + "','�ʫ��ҩ���','���I�p����q�H���','" + this.bean.getActionText() + "','���I��:" + riskValue + "','" + strCustomNo
          + "','" + strCustomName + "','" + this.bean.getOrderDate() + "','RY','773','022','���I��:" + riskValue + "','" + empNo + "'," + this.sysdate + "," + this.systime + ")";
      trans.append(stringSQL);
      intRecordNo++;
    }
    trans.close();
    dbSale.execFromPool(trans.getString());

    System.out.println("�s�J05M070���-----------------------------------E");
    return "0";
  }
  
  //�oMAIL
  public String sendMail(List customList) throws Throwable {
    System.out.println("�o�eEMAIL-----------------------------------S");
    String userEmail = "";
    String userEmail2 = "";
    String DPCode = "";
    String DPManageemNo = "";
    String DPeMail = "";
    String DPeMail2 = "";
    String[][] reteMail = null;
    
    ////////////////
    stringSQL = "SELECT DP_CODE,PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPCode = reteMail[0][0];
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        userEmail = reteMail[0][1];
      }
      if (reteMail[0][2] != null && !reteMail[0][2].equals("")) {
        userEmail2 = reteMail[0][2];
      }
    }
    /////////////////////////////////////////
    System.out.println("DPCode===>" + DPCode);
    System.out.println("userEmail===>" + userEmail);
    System.out.println("userEmail2===>" + userEmail2);
    /////////////////////////////////////////////////
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + DPCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      DPManageemNo = reteMail[0][0];
    }
    /////////////////////////////////////////
    System.out.println("DPManageemNo===>" + DPManageemNo);
    /////////////////////////////////////////////////
    stringSQL = "SELECT PN_EMAIL1,PN_EMAIL2 FROM PERSONNEL WHERE PN_EMPNO='" + DPManageemNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    if (reteMail.length > 0) {
      if (reteMail[0][0] != null && !reteMail[0][0].equals("")) {
        DPeMail = reteMail[0][0];
      }
      if (reteMail[0][1] != null && !reteMail[0][1].equals("")) {
        DPeMail2 = reteMail[0][1];
      }
    }
    ////////////////////////////////////
    System.out.println("DPeMail===>" + DPeMail);
    System.out.println("DPeMail2===>" + DPeMail2);
    /////////////////////////////////////////////////////////
    String PNCode = "";
    String PNManageemNo = "";
    String PNMail = "";

    stringSQL = "SELECT PN_DEPTCODE FROM PERSONNEL WHERE PN_EMPNO='" + empNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNCode = reteMail[0][0];
    System.out.println("PNCode===>" + PNCode);
    stringSQL = "SELECT DP_MANAGEEMPNO FROM CATEGORY_DEPARTMENT WHERE DP_CODE='" + PNCode + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNManageemNo = reteMail[0][0];
    System.out.println("PNManageemNo===>" + PNManageemNo);
    stringSQL = "SELECT PN_EMAIL1 FROM PERSONNEL WHERE PN_EMPNO='" + PNManageemNo + "'";
    reteMail = dbEMail.queryFromPool(stringSQL);
    PNMail = reteMail[0][0];
    System.out.println("PNMail===>" + PNMail);
    ////////////////////////////////////////////////////////////////////////////////////////

    // send email
    boolean hilitgt = false;
    String table1 = "<table style='text-align:center;' border=1><tr><td>�קO</td><td>�ɼӧO</td><td>����O</td><td>�Ȥ�W��</td><td>�I�q���</td><td>���I��X��</td><td>�Ȥ᭷�I����</td><td>����</td></tr>";
    String tail = "</table>";
    String contextsample = "<tr><td>${p01}</td><td>${p02}</td><td>${p025}</td><td>${p03}</td><td>${p04}</td><td>${p05}</td><td>${p06}</td><td align='left' valign='center'>${p07}</td></tr>";
    String cbottom = "</body></html>";
    String context = this.bean.getProjectID1() + "��" + strHouse + "���ʲ��q�ʫȤ᭷�I���ŵ������G�q��" + testFlag + "<BR>";

    context = table1;
    for (int i = 0; i < customList.size(); i++) {
      HashMap cm = (HashMap) customList.get(i);

      String l1 = new String(contextsample);
      l1 = l1.replace("${p01}", (String) cm.get("p01"));
      l1 = l1.replace("${p02}", (String) cm.get("p02"));
      l1 = l1.replace("${p025}", (String) cm.get("p025"));
      l1 = l1.replace("${p03}", (String) cm.get("p03"));
      l1 = l1.replace("${p035}", (String) cm.get("p035"));
      l1 = l1.replace("${p04}", (String) cm.get("p04"));
      l1 = l1.replace("${p05}", (String) cm.get("p05"));
      l1 = l1.replace("${p06}", (String) cm.get("p06"));
      String tempPo6 = "" + cm.get("p06");
      tempPo6 = tempPo6.trim();
      if ("��".equals(tempPo6.trim())) {
        l1 = l1.replace("${p07}", "�~���θꮣ���I������" + tempPo6 + "���I�Ȥ�A�Ш̬~������@�~�W�w�A����[�j���ޱ����I");
        hilitgt = true;
      } else {
        l1 = l1.replace("${p07}", "�~���θꮣ���I������" + tempPo6 + "���I�Ȥ�");
      }

      context = context + l1;
    }
    context = context + tail + cbottom;

    // �H�o�q���H��
    String subject = this.bean.getProjectID1() + "��" + strHouse + "���ʲ��q�ʫȤ᭷�I���ŵ������G�q��" + testFlag;
    if (hilitgt) {
      String[] arrayUser = { "Kyle_Lee@fglife.com.tw", userEmail, DPeMail, PNMail };
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, context, null, "", "text/html");
      System.out.println("sendRS===>" + sendRS);
    } else {
      String[] arrayUser = { "Kyle_Lee@fglife.com.tw", userEmail };
      String sendRS = sendMailbcc("ex.fglife.com.tw", "Emaker-Invoice@fglife.com.tw", arrayUser, subject, context, null, "", "text/html");
      System.out.println("sendRS===>" + sendRS);
    }
    System.out.println("�o�eEMAIL-----------------------------------E");
    return "0";
  }
  
  //�O�Ʀr�^�� true�A�_�h�^�� false�C
  public boolean check(String value) throws Throwable {
    return false;
  }

  public String getDefaultValue(String value) throws Throwable {
    dbSale = getTalk("Sale");
    dbEMail = getTalk("eMail");
    dbBen = getTalk("400CRM");
    dbEIP = getTalk("EIP");
    
    String sqlCustom = 
        "select DISTINCT OrderNo,RecordNo,auditorship,Nationality,CountryName,CustomNo,CustomName,Percentage,Birthday,MajorName,PositionName,ZIP,City,Town,Address,Cellphone,Tel,Tel2,eMail,IsLinked,IsControlList,IsBlackList,TrxDate,StatusCd "
        +"from Sale05M091 sm where CustomNo In " 
        +"('A101517596','A220187841','F127714465','A129744788','L122338290','G122045000','E220217946','L221318741','F260094259','A201415482','C220278876','E122986371','L223519675','E221386968','G120260563','F223715333','A101375221','P102423728','F225591573','F127679756','F226015349','U121517026','A122373316','A123268403','F127683330','W100358879','F125849965','A130608042','F228314014','F126459072','U121231567','A203111656','A124000594','F226136610','C220238809','F227859505','E123712562','F222111760','A210218839','F227855276','D200215392','S101951726','Q222046560','A226873686','Q222984636','A120252298','A226403095','N123911839','E121300591','A121263419','N222460077','B222025032','AC01152051','F127171880','H122111215','L220268519','Z100010446','A121670245','F224152821','F228128616','A125412445','F128142650','E220588977','T220367194','A224382897','L123308867','H220931088','F221245007','E121862283','A222332497','F226909499','K222640635','E223024907','A202598622','G120195132','H220418133','N121827067','C220922940','F290159281','A127222163','L221860259','L223307424','F123659212','A126229275','A123673299','A123236643','A127862818','H223174343','A128949805','A225154093','T121248150','W200042378','R222171694','A127283237','A122734337','M221575256','N224470508','S221669230','F227079605','A103034269','K121533157','U220584052','F123852142','F220437127','G220869282','P220440587','N223726038','F224210784','N222937895','A132672166','A129214981','F125991964','E223412407','A127824907','89829333','U120603989','U220601772','K01909784','T223683659','K345858(A)','T221613453','A127242290')";
    String[][] retCustom = dbSale.queryFromPool(sqlCustom);
    
    String sqlCustomBen = 
        "select OrderNo,CustomNo,RecordNo,BenName,BCustomNo,Birthday,CountryName,HoldType,IsBlackList,IsControlList,IsLinked,TrxDate,StatusCd " + 
        "from Sale05M091Ben smb where CustomNo In " + 
        "('A101517596','A220187841','F127714465','A129744788','L122338290','G122045000','E220217946','L221318741','F260094259','A201415482','C220278876','E122986371','L223519675','E221386968','G120260563','F223715333','A101375221','P102423728','F225591573','F127679756','F226015349','U121517026','A122373316','A123268403','F127683330','W100358879','F125849965','A130608042','F228314014','F126459072','U121231567','A203111656','A124000594','F226136610','C220238809','F227859505','E123712562','F222111760','A210218839','F227855276','D200215392','S101951726','Q222046560','A226873686','Q222984636','A120252298','A226403095','N123911839','E121300591','A121263419','N222460077','B222025032','AC01152051','F127171880','H122111215','L220268519','Z100010446','A121670245','F224152821','F228128616','A125412445','F128142650','E220588977','T220367194','A224382897','L123308867','H220931088','F221245007','E121862283','A222332497','F226909499','K222640635','E223024907','A202598622','G120195132','H220418133','N121827067','C220922940','F290159281','A127222163','L221860259','L223307424','F123659212','A126229275','A123673299','A123236643','A127862818','H223174343','A128949805','A225154093','T121248150','W200042378','R222171694','A127283237','A122734337','M221575256','N224470508','S221669230','F227079605','A103034269','K121533157','U220584052','F123852142','F220437127','G220869282','P220440587','N223726038','F224210784','N222937895','A132672166','A129214981','F125991964','E223412407','A127824907','89829333','U120603989','U220601772','K01909784','T223683659','K345858(A)','T221613453','A127242290')";
    String[][] retSBen = dbSale.queryFromPool(sqlCustomBen);
    
//    String[][] retCustom = getTableData("table1");
//    String[][] retSBen = getTableData("table6");
    String strOrderNo = getValue("field3").trim();
    String strOrderDate = getValue("field2").trim();
    String strProjectID1 = getValue("field1").trim();
    String actionText = getValue("actionText").trim();
    
    RiskCheckBean bean = new RiskCheckBean();
    bean.setRetCustom(retCustom);
    bean.setRetSBen(retSBen);
    bean.setOrderNo(strOrderNo);
    bean.setProjectID1(strProjectID1);
    bean.setOrderDate(strOrderDate);
    bean.setActionText(actionText);
    this.bean = bean;
    
    String serverIP = get("serverIP").toString().trim();
    isTest = serverIP.contains("172.16.14.4")? false:true;
    if(isTest) {
      testFlag = " (����) ";
      System.out.println("����>>>" + testFlag);
    }
    
    //Emaker�u��
    this.userNo = getUser().toUpperCase().trim();
    //FG�u��
    this.getEMPNO();
    //�t�Τ���ɶ�
    this.getDateTime();
    //action
    this.getActionNo();
    //HouseCar
    this.getHouseCar();
    
    this.processRisk();
    
    return value;
  }
}
