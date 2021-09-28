package Sale.Sale05M090.Form;

import java.util.Vector;

import jcx.db.talk;
import jcx.jform.bproc;
import jcx.util.convert;

public class UpdBen extends bproc {

  public String getDefaultValue(String value) throws Throwable {
    System.out.println(">>>更新受益人表格<<<");

    Farglory.util.FargloryUtil exeFun = new Farglory.util.FargloryUtil();
    talk dbSale = getTalk("Sale");
    Vector vectorSql = new Vector();

    String orderNo = getValue("OrderNo").toString().trim();
    String[][] tableBen = getTableData("table6");

    /*
     * // test for (int i=0; i<tableBen.length; i++) { String[] ben1 = tableBen[i];
     * for (int j=0; j<ben1.length; j++) { System.out.println(">>>" + ben1[j]);
     * 
     * if (j == ben1.length-1){ System.out.println("===="); } } } // test end
     */
    boolean confirm2ExcuteSQL = true;
    String delSql = "delete from Sale05M091Ben where orderNo = '" + orderNo + "' ";
    vectorSql.add(delSql);

    String insSql = "";
    for (int i = 0; i < tableBen.length; i++) {
      String[] ben1 = tableBen[i];
      String birthDay = exeFun.getDateAC(ben1[5].trim(), "");

      if ("".equals(ben1[6].trim())) {
        messagebox("請指定實質受益人 [" + ben1[3].trim() + "]  之國別。");
        confirm2ExcuteSQL = false;
        break;
      }
      if ("".equals(ben1[7].trim())) {
        messagebox("請指定實質受益人 [" + ben1[3].trim() + "]  之對象別。");
        confirm2ExcuteSQL = false;
        break;
      }
      if (birthDay.length() != 10) {
        messagebox("實質受益人日期錯誤");
        confirm2ExcuteSQL = false;
        break;
      }

      insSql = "insert into Sale05M091Ben (OrderNo, CustomNo, RecordNo, BenName, BCustomNo, Birthday, CountryName, HoldType, IsBlackList, IsControlList, IsLinked, TrxDate, StatusCd) "
          + " values ( " + "N'" + ben1[0].trim() + "' " + ",N'" + ben1[1].trim() + "' " + "," + ben1[2].trim() + " " + ",N'" + ben1[3].trim() + "' " + ",N'" + ben1[4].trim() + "' "
          + ",N'" + convert.replace(birthDay, "/", "") + "' " + ",N'" + ben1[6].trim() + "' " + ",N'" + ben1[7].trim() + "' " + ",N'" + ben1[8].trim() + "' " + ",N'"
          + ben1[9].trim() + "' " + ",N'" + ben1[10].trim() + "' " + ",N'" + ben1[11].trim() + "' " + ",N'" + ben1[12].trim() + "' " + ") ";
      vectorSql.add(insSql);
    }

    if (confirm2ExcuteSQL) {
      put("UpdBen_RS", "Y");
      dbSale.execFromPool((String[]) vectorSql.toArray(new String[0]));
    }else {
      put("UpdBen_RS", "N");
    }
    
    System.out.println(">>>更新受益人表格 END<<<");

    return value;
  }

  public String getInformation() {
    return "---------------updateBen(\u66f4\u65b0\u53d7\u76ca\u4eba).defaultValue()----------------";
  }
}
