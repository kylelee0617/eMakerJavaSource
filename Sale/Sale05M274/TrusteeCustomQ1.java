package Sale.Sale05M274;

import jcx.jform.bQuery;

public class TrusteeCustomQ1 extends bQuery {
  public String getFilter() throws Throwable {
    // �^�ǭȬ��۩w�d�߱���
    // �^�ǭȥ����O�ťթΥH and �}�l,�p "and FIELD1='ABC'"
    // �]�i�H�^�ǧ��㪺 SQL �y�k���N��]�w���� �p select distinct display_field,data_field from table1
    // where type=100
    String orderNo = this.getTableData("table1")[0][5].trim();

    return "select customName, customName from Sale05m091 where 1=1 and orderNo = '" + orderNo + "' and ISNULL(StatusCd, '') != 'C' ";
  }

  public String getInformation() {
    return "---------------null(null).setf_defined_where_clause()----------------";
  }
}
