package Invoice.utils;

import Invoice.vo.GLEAPFUFBean;
import Invoice.vo.GLEBPFUFBean;
import Invoice.vo.GLECPFUFBean;
import jcx.db.talk;
import jcx.jform.bproc;

/**
 * �g�JAS400 ����
 * 
 * @author B04391
 *
 */

public class AS400SQL extends bproc {
  talk dbInvoice2 = null;
  talk dbInvoice = null;
  talk as400 = null;
  
  /**
   * �g�JAS400 �o���D��
   * @param aBean
   * @return
   * @throws Throwable
   */
  public String insGLEAPFUF(GLEAPFUFBean aBean) throws Throwable {
    as400 = getTalk("AS400");
    
    StringBuilder sbSQL = new StringBuilder();
    sbSQL.append("insert into GLEAPFUF ");
    sbSQL.append("(EA01U, EA02U, EA03U, EA04U, EA05U, EA06U, EA07U, EA08U, EA09U, EA10U, EA11U, EA12U, EA13U, EA14U, EA15U, EA16U, EA17U, EA18U, EA19U, EA20U, EA21U, EA22U) ");
    sbSQL.append("values ");
    sbSQL.append("(");
    sbSQL.append("'").append(aBean.getEA01U()).append("', ");     //�o�����X
    sbSQL.append("'").append(aBean.getEA02U()).append("', ");     //�o�����
    sbSQL.append("'").append(aBean.getEA03U()).append("', ");     //�o���p��
    sbSQL.append("'").append(aBean.getEA04U()).append("', ");     //���q�N�X
    sbSQL.append("'").append(aBean.getEA05U()).append("', ");     //�����N�X
    sbSQL.append("'").append(aBean.getEA06U()).append("', ");     //�קO�N�X
    sbSQL.append("'").append(aBean.getEA07U()).append("', ");     //Invoice Way
    sbSQL.append("'").append(aBean.getEA08U()).append("', ");     //��O�N��
    sbSQL.append("'").append(aBean.getEA09U()).append("', ");     //�Ȥ�N��
    sbSQL.append("'").append(aBean.getEA10U()).append("', ");     //�K�n
    sbSQL.append("").append(aBean.getEA11U()).append(", ");       //���|
    sbSQL.append("").append(aBean.getEA12U()).append(", ");       //�|�B
    sbSQL.append("").append(aBean.getEA13U()).append(", ");       //�t�|
    sbSQL.append("'").append(aBean.getEA14U()).append("', ");     //�|�O
    sbSQL.append("").append(aBean.getEA15U()).append(", ");                      //�w�������B
    sbSQL.append("").append(aBean.getEA16U()).append(", ");                      //�w��������
    sbSQL.append("'").append(aBean.getEA17U()).append("', ");     //�w�C�LYN
    sbSQL.append("").append(aBean.getEA18U()).append(", ");             //�ɦL����
    sbSQL.append("'").append(aBean.getEA19U()).append("', ");     //�@�oYN
    sbSQL.append("'").append(aBean.getEA20U()).append("', ");     //�J�bYN
    sbSQL.append("'").append(aBean.getEA21U()).append("', ");                   //�o���B�z�覡
    sbSQL.append("'").append(aBean.getEA22U().replace("�o��", "")).append("' ");      //����/�ȪA
    sbSQL.append(") ");
    
    return as400.execFromPool(sbSQL.toString());
  }
  
  
  public String getDefaultValue(String value) throws Throwable {
    return value;
  }
}
