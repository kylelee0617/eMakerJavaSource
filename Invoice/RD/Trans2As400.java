package Invoice.RD;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import Farglory.util.*;

public class Trans2As400 extends bproc {

  KUtils util = new KUtils();

  public String getDefaultValue(String value) throws Throwable {
    System.out.println("�ɵo�����>>>>>>>>>> Start :" + this.getClass());
    talk dbInvoice = getTalk("" + get("put_dbInvoice"));
    talk dbAs400 = getTalk("AS400");
    String GENLIB = ((Map) get("config")).get("GENLIB").toString().trim();

    String projectId = this.getValue("ProjectNo").trim();
    String invoiceDate = getValue("InvoiceDate").trim();
    StringBuilder sql = null;

    // TODO: �̷ӱ����M030�o��
    sql = new StringBuilder();
    sql.append("SELECT * FROM INVOM030 a ");
    sql.append("WHERE 1=1 ");
    sql.append("AND invoiceDate = '" + invoiceDate + "' ");
    sql.append("AND projectNo = '" + projectId + "' ");
    String[][] retM030 = dbInvoice.queryFromPool(sql.toString());

    if (retM030.length == 0) {
      messagebox(this.getClass() + ": �d�L�o�����");
      return value;
    }

    // TODO: �g�JAS400 A �ɡA�òզ��Ȥ�in�d�߱���
    String[] arrCustomNos = new String[retM030.length];
    for (int idx = 0; idx < retM030.length; idx++) {
      String transfer = retM030[idx][23].toString().trim();
      if (transfer.length() >= 4) transfer = transfer.substring(0, 2);

      sql = new StringBuilder();
      sql.append("insert into " + GENLIB + ".GLEAPFUF ");
      sql.append("(EA01U, EA02U, EA03U, EA04U, EA05U, EA06U, EA07U, EA08U, EA09U, EA10U, EA11U, EA12U, EA13U, EA14U, EA15U, EA16U, EA17U, EA18U, EA19U, EA20U, EA21U, EA22U) ");
      sql.append("values ");
      sql.append("(");
      sql.append("'").append(retM030[idx][0].toString().trim()).append("', "); // �o�����X
      sql.append("'").append(retM030[idx][1].toString().trim()).append("', "); // �o�����
      sql.append("'").append(retM030[idx][3].toString().trim()).append("', "); // �o���p��
      sql.append("'").append(retM030[idx][4].toString().trim()).append("', "); // ���q�N�X
      sql.append("'").append(retM030[idx][5].toString().trim()).append("', "); // �����N�X
      sql.append("'").append(retM030[idx][6].toString().trim()).append("', "); // �קO�N�X
      sql.append("'").append(retM030[idx][7].toString().trim()).append("', "); // Invoice Way
      sql.append("'").append(retM030[idx][8].toString().trim()).append("', "); // ��O�N��
      sql.append("'").append(retM030[idx][9].toString().trim()).append("', "); // �Ȥ�N��
      sql.append("'").append(retM030[idx][11].toString().trim()).append("', "); // �K�n
      sql.append("").append(retM030[idx][12].toString().trim()).append(", "); // ���|
      sql.append("").append(retM030[idx][13].toString().trim()).append(", "); // �|�B
      sql.append("").append(retM030[idx][14].toString().trim()).append(", "); // �t�|
      sql.append("'").append(retM030[idx][15].toString().trim()).append("', "); // �|�O
      sql.append("").append(retM030[idx][16].toString().trim()).append(", "); // �w�������B
      sql.append("").append(retM030[idx][17].toString().trim()).append(", "); // �w��������
      sql.append("'").append(retM030[idx][18].toString().trim()).append("', "); // �w�C�LYN
      sql.append("").append(retM030[idx][19].toString().trim()).append(", "); // �ɦL����
      sql.append("'").append(retM030[idx][20].toString().trim()).append("', "); // �@�oYN
      sql.append("'").append(retM030[idx][21].toString().trim()).append("', "); // �J�bYN
      sql.append("'").append(retM030[idx][22].toString().trim()).append("', "); // �o���B�z�覡
      sql.append("'").append(transfer).append("' "); // ����/�ȪA
      sql.append(") ");
      dbAs400.execFromPool(sql.toString());

      arrCustomNos[idx] = retM030[idx][9].toString().trim();
    }

    // TODO: �̷ӵo����M0C0�Ȥ�
    sql = new StringBuilder();
    sql.append("SELECT DISTINCT CustomNo , CustomName from InvoM0C0 ");
    sql.append("WHERE 1=1 ");
    sql.append("AND CustomNo in (" + util.genQueryInString(arrCustomNos) + ") ");
    String[][] retM0C0 = dbInvoice.queryFromPool(sql.toString());

    // TODO: �g�JAS400 E��
    for (int idx = 0; idx < retM0C0.length; idx++) {
      sql = new StringBuilder();
      sql.append("insert into " + GENLIB + ".GLEDPFUF ");
      sql.append("(ED01U, ED02U) ");
      sql.append("values ");
      sql.append("(");
      sql.append("'").append(retM0C0[idx][0].toString().trim()).append("', ");
      sql.append("'").append(retM0C0[idx][1].toString().trim()).append("' ");
      sql.append(") ");
      dbAs400.execFromPool(sql.toString());
    }

    message("�@���ɵo��" + retM030.length + "���A�Ȥ�" + retM0C0.length + "��");

    System.err.println("�ɵo�����>>>>>>>>>> End");
    return value;
  }

  public String getInformation() {
    return "---------------BUFAPIO(GO!!!).defaultValue()----------------";
  }
}
