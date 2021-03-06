package Invoice.utils;

import Invoice.vo.GLEAPFUFBean;
import Invoice.vo.GLEBPFUFBean;
import Invoice.vo.GLECPFUFBean;
import jcx.db.talk;
import jcx.jform.bproc;

/**
 * 寫入AS400 元件
 * 
 * @author B04391
 *
 */

public class AS400SQL extends bproc {
  talk dbInvoice2 = null;
  talk dbInvoice = null;
  talk as400 = null;
  
  /**
   * 寫入AS400 發票主檔
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
    sbSQL.append("'").append(aBean.getEA01U()).append("', ");     //發票號碼
    sbSQL.append("'").append(aBean.getEA02U()).append("', ");     //發票日期
    sbSQL.append("'").append(aBean.getEA03U()).append("', ");     //發票聯式
    sbSQL.append("'").append(aBean.getEA04U()).append("', ");     //公司代碼
    sbSQL.append("'").append(aBean.getEA05U()).append("', ");     //部門代碼
    sbSQL.append("'").append(aBean.getEA06U()).append("', ");     //案別代碼
    sbSQL.append("'").append(aBean.getEA07U()).append("', ");     //Invoice Way
    sbSQL.append("'").append(aBean.getEA08U()).append("', ");     //戶別代號
    sbSQL.append("'").append(aBean.getEA09U()).append("', ");     //客戶代號
    sbSQL.append("'").append(aBean.getEA10U()).append("', ");     //摘要
    sbSQL.append("").append(aBean.getEA11U()).append(", ");       //未稅
    sbSQL.append("").append(aBean.getEA12U()).append(", ");       //稅額
    sbSQL.append("").append(aBean.getEA13U()).append(", ");       //含稅
    sbSQL.append("'").append(aBean.getEA14U()).append("', ");     //稅別
    sbSQL.append("").append(aBean.getEA15U()).append(", ");                      //已折讓金額
    sbSQL.append("").append(aBean.getEA16U()).append(", ");                      //已折讓次數
    sbSQL.append("'").append(aBean.getEA17U()).append("', ");     //已列印YN
    sbSQL.append("").append(aBean.getEA18U()).append(", ");             //補印次數
    sbSQL.append("'").append(aBean.getEA19U()).append("', ");     //作廢YN
    sbSQL.append("'").append(aBean.getEA20U()).append("', ");     //入帳YN
    sbSQL.append("'").append(aBean.getEA21U()).append("', ");                   //發票處理方式
    sbSQL.append("'").append(aBean.getEA22U().replace("發票", "")).append("' ");      //收款/客服
    sbSQL.append(") ");
    
    return as400.execFromPool(sbSQL.toString());
  }
  
  
  public String getDefaultValue(String value) throws Throwable {
    return value;
  }
}
