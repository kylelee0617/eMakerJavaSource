package Const_Ask.Report;

import javax.swing.*;
import jcx.jform.bproc;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;

import com.jacob.activeX.*;
import com.jacob.com.*;

public class Report333412Print12 extends bproc {
  public String getDefaultValue(String value) throws Throwable {

    Farglory.util.FargloryUtil exeUtil = new Farglory.util.FargloryUtil();

    String stringFile[] = { "表8-請款申請書(工程類).xlt", "表8-請款申請書(工程類)D3.xls", "askrpt_master_new_6.xlt" };
    for (int i = 0; i < stringFile.length; i++) {
      String stringServerPath = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\" + stringFile[i];
      String stringClientPath = "C:\\Emaker_Print\\";
      exeUtil.doBatchMkDir(stringClientPath);
      System.out.println(stringClientPath + "--------------------------");
      boolean booleanExist = exeUtil.doSaveFile(stringServerPath, stringClientPath + stringFile[i], "Y");
    }

    String stringSavePath = "C:\\Emaker_Print";
    (new File(stringSavePath)).mkdir();
    String stringMessage = "";
    String stringMessageT = "";
    String ret[][] = getTableData("table1");
    String user = getUser();

    for (int i = 0; i < ret.length; i++) {

      if ("1".equals(ret[i][0])) {

        // -------------20151110 奕銘加 有代工超用金額 且 請款超過80% 不可列印----------------------//
        talk db = getTalk("Const_Ask");
        String sql = "";
        String str[][] = {};
        String mcase_pubser = getValue("field24").trim();
        String casecode = getValue("field1").trim();
        String prdocode = ret[i][15].trim();
        String sum_point_wast = "0";
        String str_group[][] = {};
        String str_group_1[][] = {};
        String str_group_2[][] = {};
        String stringgcasecode = casecode.trim();
        String stringgcasecode_1 = casecode.trim();
        String stringgcasecode_2 = casecode.trim();

        if (Integer.parseInt(mcase_pubser) >= 3) {

          // 查詢 該案是否 群組化
          // 點工
          sql = " select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b  where a.serial = b.serial and b.gcasecode = '" + casecode
              + "' and a.type = 0 ";
          str_group = db.queryFromPool(sql);
          if (str_group.length > 0) {
            stringgcasecode = str_group[0][0];
          }

          // 廢棄物
          sql = " select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b  where a.serial = b.serial and b.gcasecode = '" + casecode
              + "' and a.type = 1 ";
          str_group_1 = db.queryFromPool(sql);
          if (str_group_1.length > 0) {
            stringgcasecode_1 = str_group_1[0][0];
          }

          // 民生
          sql = " select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b  where a.serial = b.serial and b.gcasecode = '" + casecode
              + "' and a.type = 2 ";
          str_group_2 = db.queryFromPool(sql);
          if (str_group_2.length > 0) {
            stringgcasecode_2 = str_group_2[0][0];
          }

          sql = " select  isnull(  round( sum(A1.ask_tot * POWP1.purchase /100 )  , 0 ) , 0 ) "
              + " from    ask_mey as A1 , contract as C1 , prdt as P1 , point_of_work_proportion as POWP1 , point_of_work_correspond as POWC1 ";
          if (str_group.length > 0) {
            sql += " where C1.casecode in (  select b.gcasecode   from point_of_work_gmaster a , point_of_work_gdetail b  "
                + " where a.serial = b.serial  and a.casecode in (  select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b "
                + " where a.serial = b.serial and b.gcasecode = '" + casecode + "'  ) )";
          } else {
            sql += " where C1.casecode = '" + casecode + "'";
          }
          sql += "  and     P1.prdocode = '" + prdocode + "'   and     A1.casecode = C1.casecode and A1.contract_id = C1.contract_id   and     P1.prdocode = C1.prdocode "
              + "  and     POWC1.buycode = C1.buycode   and     POWC1.stemid = POWP1.stemid ";

          // 20120410 因 採購室 金惇 需求 巨蛋類的案別 A51 扣 0.1% 設定 ( 原 A51 扣 0.3% )
          // casecode like 234D1xxx A51 使用 type D , 營造 C
          if (casecode.indexOf("234D1") == 0) {
            sql += " and  POWC1.type = 'D' ";
          } else {
            sql += " and  POWC1.type = 'C' ";
          }

          String str_point[][] = db.queryFromPool(sql);
          // 點工
          sql = " select isnull( sum(CI2.imfprice * CD2.purchase_num) , 0 )  from   prdt as P2 , check_detail as CD2 , contract_item as CI2 , check_master as CM2 ";
          if (str_group.length > 0) {
            sql += " where CD2.casecode in (  select b.gcasecode  from point_of_work_gmaster a , point_of_work_gdetail b "
                + " where a.serial = b.serial and a.casecode = '" + stringgcasecode + "' and a.type = 0 )  ";
          } else {
            sql += " where CD2.casecode = '" + casecode + "'";
          }
          sql += "  and    P2.prdocode = '" + prdocode + "'   and    CM2.check_type = 5   and    P2.prdocode = CD2.prdocode "
              + "  and    CI2.casecode = CD2.casecode and CI2.budflr = CD2.budflr    and    CI2.stemcode = CD2.stemcode and CI2.budget_ser = CD2.budget_ser  "
              + "  and    CI2.stemcode_ser = CD2.stemcode_ser   and    CD2.casecode = CM2.casecode and CD2.check_id = CM2.check_id  "
              + "  and    CM2.casecode = CI2.casecode and CM2.contract_id = CI2.contract_id ";
          String str_sumpoint[][] = db.queryFromPool(sql);
          sum_point_wast = operation.floatAdd(sum_point_wast, str_sumpoint[0][0], 0);
          System.out.println(" point " + str_sumpoint[0][0]);

          // 廢棄物
          sql = " select isnull( sum(500 * CD2.purchase_num) , 0 )  from   prdt as P2 , check_detail as CD2 , contract_item as CI2 , check_master as CM2 ";
          if (str_group_1.length > 0) {

            String sql_w = " select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b  where a.serial = b.serial and b.gcasecode = '" + casecode
                + "'  and a.type = 1 ";
            String str_type[][] = db.queryFromPool(sql_w);

            sql += " where CD2.stemcode in ('A0107','A0108') and CD2.casecode in (  select b.gcasecode  from point_of_work_gmaster a , point_of_work_gdetail b "
                + " where a.serial = b.serial and a.casecode = '" + str_type[0][0] + "' and a.type = 1  )  ";
          } else {
            sql += " where CD2.stemcode in ('A0107','A0108') and CD2.casecode = '" + casecode + "'";
          }
          sql += "  and    P2.prdocode = '" + prdocode + "'   and    CM2.check_type = 6   and    P2.prdocode = CD2.prdocode "
              + "  and    CI2.casecode = CD2.casecode and CI2.budflr = CD2.budflr    and    CI2.stemcode = CD2.stemcode and CI2.budget_ser = CD2.budget_ser  "
              + "  and    CI2.stemcode_ser = CD2.stemcode_ser   and    CD2.casecode = CM2.casecode and CD2.check_id = CM2.check_id  "
              + "  and    CM2.casecode = CI2.casecode and CM2.contract_id = CI2.contract_id ";
          str_sumpoint = db.queryFromPool(sql);
          System.out.println(" wast " + str_sumpoint[0][0]);
          sum_point_wast = operation.floatAdd(sum_point_wast, str_sumpoint[0][0], 0);

          // 累計使用民生垃圾
          sql = " select isnull( sum(CI2.imfprice *  CD2.purchase_num) , 0 )  from   prdt as P2 , check_detail as CD2 , contract_item as CI2 , check_master as CM2 ";
          // " where CD2.casecode = '"+casecode+"' "+
          if (str_group_2.length > 0) {

            String sql_w = " select a.casecode  from point_of_work_gmaster a , point_of_work_gdetail b  where a.serial = b.serial and b.gcasecode = '" + casecode
                + "'  and a.type = 2 ";
            String str_type[][] = db.queryFromPool(sql_w);

            sql += " where CD2.stemcode in ('T0008') and CD2.casecode in (  select b.gcasecode  from point_of_work_gmaster a , point_of_work_gdetail b "
                + " where a.serial = b.serial and a.casecode = '" + str_type[0][0] + "' and a.type = 2  )  ";
          } else {
            sql += " where CD2.stemcode in ('T0008') and CD2.casecode = '" + casecode + "'";
          }
          sql += "  and    P2.prdocode = '" + prdocode + "'   and    CM2.check_type = 6   and    P2.prdocode = CD2.prdocode "
              + "  and    CI2.casecode = CD2.casecode and CI2.budflr = CD2.budflr    and    CI2.stemcode = CD2.stemcode and CI2.budget_ser = CD2.budget_ser  "
              + "  and    CI2.stemcode_ser = CD2.stemcode_ser   and    CD2.casecode = CM2.casecode and CD2.check_id = CM2.check_id  "
              + "  and    CM2.casecode = CI2.casecode and CM2.contract_id = CI2.contract_id ";
          str_sumpoint = db.queryFromPool(sql);
          System.out.println(" 民生 " + str_sumpoint[0][0]);
          sum_point_wast = operation.floatAdd(sum_point_wast, str_sumpoint[0][0], 0);

          // 其他收入
          String sql_dis = " select isnull( sum(other_income_dis) ,0)  from ask_mey a , contract b  where a.casecode = '" + casecode + "'  and b.prdocode = '"
              + prdocode + "'  and a.other_income_dis > 0  and a.casecode = b.casecode  and a.contract_id = b.contract_id ";
          String str_dis[][] = db.queryFromPool(sql_dis);

          sum_point_wast = operation.floatSubtract(sum_point_wast, str_dis[0][0], 0);
          System.out.println(" 可用 " + str_point[0][0] + " 累計 " + sum_point_wast);
          String over_amt = operation.floatSubtract(str_point[0][0], sum_point_wast, 0);
          over_amt = operation.floatSubtract("0", over_amt, 0);

          // 20151110 奕銘 請款比例超過80% 提醒 不可列印

          String sql_rate = " select case sub_cont when '0' then '0' else isnull( round( ( ask_tot / sub_cont ) * 100 , 4 ) , 0 ) end , ask_tot , sub_cont  from contract "
              + " where casecode = '" + casecode + "' and contract_id = " + ret[i][9] + " ";
          String str_rate[][] = db.queryFromPool(sql_rate);

          if ("DBAX".equals(user)) {
            if (Double.parseDouble(sum_point_wast) != 0) {
              if ((Double.parseDouble(str_point[0][0]) < Double.parseDouble(sum_point_wast)) && Double.parseDouble(str_rate[0][0]) > 80.0) {
                // messagebox("本廠商點工與廢棄物累計使用金額已超過可用金額的100%");
                // messagebox("「"+ret[i][4]+"」點工與廢棄物累計使用金額已超過100%，超過可使用金額
                // "+format.format(over_amt,"999,999,999,999,999,999,999").trim()+"。\n請款單"+ret[i][1]+"請款比例"+format.format(str_rate[0][0],"999,999,999,999,999,999,999.9999").trim()+"已超過80%，若未回補沖帳金額將無法列印請款單!!");
                // break;
              }
            }
          }

        }

        // 20151124負值 沒有申請收款單，不可列印
        if (Double.parseDouble(ret[i][7]) < 0.0) {
          System.out.println("--收款單列印--");
          String sql_rece = " select *  from receive_const_master  where casecode = '" + getValue("field1").trim() + "' and ask_mey_id = '" + ret[i][10]
              + "' and permit <> -99 ";
          String str_rece[][] = db.queryFromPool(sql_rece);
          if (!"DBAX".equals(user)) {
            if (str_rece.length == 0) {
              messagebox("請款金額為負值者，請先建立收款單才可列印");
              break;
            }
          }
        }

        // 若ok 執行 Excel
        doExcel(i, ret, stringSavePath);
      }
    }

    Runtime.getRuntime().exec("cmd /c DEL " + stringSavePath + "\\*.xls");
    return value;
  }

  // Excel 報表列印
  public void doExcel(int i, String ret[][], String stringSavePath) throws Throwable {
    // Farglory.util.FargloryUtil exeFun = new Farglory.util.FargloryUtil() ;
//        Farglory.Excel.FargloryExcel  exeFun  =  new  Farglory.Excel.FargloryExcel(0,0,0,0) ;
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel();
    String user = getUser();
    if (!"DBAX".equals(user)) {
      exeFun.setVisibleProperty(false); // 設定不顯示excel
    }
    // 資料處理
    message("");

    String DBname = (String) get("Const_Ask");
    talk db = getTalk(DBname);
    String sql = "";
    String str[][] = {};
    String casecode = getValue("field1").trim();
    String contract_id = ret[i][9].trim();
    // int dis = 3 ;//Excel上下偏移量
    int dis = 0;// Excel上下偏移量
    String patch = "";

    int ondate = Integer.parseInt(getValue("text8"));

    // 用日期來區分新與舊
    /*
     * if( "034H108".equals( casecode ) ){ patch =
     * "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_6.xlt"; }else{
     */
    if (Integer.parseInt(ret[i][3]) >= ondate) {
      // System.out.println("ret = "+ret[i][3]);
//          patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_b1.xlt";
      // patch =
      // "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_b23.xls";//設成1頁寬
      // 20170803奕銘小數點3位
      if (("034H96".equals(casecode) && "107".equals(contract_id)) || ("034H932".equals(casecode) && "2".equals(contract_id))
          || ("034H932".equals(casecode) && "34".equals(contract_id)) || ("034H98".equals(casecode) && "38".equals(contract_id))
          || ("034H805".equals(casecode) && "40".equals(contract_id)) || ("034H601".equals(casecode) && "297".equals(contract_id))
          || ("034H113".equals(casecode) && "25".equals(contract_id)) || ("034H90".equals(casecode) && "112".equals(contract_id))) {
        // patch = "C:\\Emaker_Print\\表8-請款申請書(工程類)D3.xls";//設成1頁寬
        patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\表8-請款申請書(工程類)D3.xls";// 設成1頁寬
      } else {
        // patch = "C:\\Emaker_Print\\表8-請款申請書(工程類).xls";//設成1頁寬
        patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\表8-請款申請書(工程類).xlt";
      }
    } else {
//          patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_5.xlt";
      // patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_6.xlt";
      // patch = "C:\\Emaker_Print\\askrpt_master_new_6.xlt";//設成1頁寬
      patch = "G:\\資訊室\\Excel\\Const_Ask\\請款後明細列印\\askrpt_master_new_6.xlt";// 設成1頁寬
    }
    // }
    System.out.println("path " + patch);

    // ------------------------------------------------------------------------------------請款書正副表---------------------------------------------------------------------------------//

    Vector retVector = exeFun.getExcelObject(patch);

    Dispatch objectSheet1 = (Dispatch) retVector.get(1);

    // ---------------------------------------------------------------------------主表---------------------------------------------------------------------------//

    // ---------公司名-------------//
    String sql_m = "SELECT casecode,insur_comp_name,firm_no FROM mcase WHERE casecode = '" + casecode + "' ";
    String comp_n[][] = db.queryFromPool(sql_m);
//        if( "05159128".equals( comp_n[0][2].trim()) || "04673318".equals( comp_n[0][2].trim()) ){
//          exeFun.setPageTitleName(objectSheet1,  "&C&\"標楷體\"&12"+comp_n[0][1]+"\n 請  款  申  請  書") ;
//        }else{
    exeFun.setPageTitleName(objectSheet1, "&C&\"標楷體\"&20\n 請  款  申  請  書");
    exeFun.putDataIntoExcel(0, 3 + dis, comp_n[0][1], objectSheet1);
//        }           
    // 1 2 3 4 5 6
    sql = "SELECT a.ask_mey_subj , c.prdinit , a.ask_date , a.ask_period , a.ask_time , rtrim(b.casecode)+'-'+b.cid_ym+'-'+ltrim(str(b.cid_show))+'-'+ltrim(str(b.cid_serial))" +
    // 7
        ",b.sub_cont " +
        // 8 9 10 11 12 13
        ", isnull( a.ask_tot_for , 0 ) , isnull( a.ask_tot , 0 ) , isnull( a.ask_tot_acl , 0 ), a.keep_dis_for , a.keep_dis , a.keep_dis_acl" +
        // 14 15 16
        ",a.keep_pay_for,a.keep_pay,a.keep_pay_acl " +
        // 17 18 19 20 21 22
        ",a.down_pay_for,a.down_pay,a.down_pay_acl,a.down_dis_for,a.down_dis,a.down_dis_acl" +
        // 23 24 25 26 27 28
        ",a.lend_pay_for,a.lend_pay,a.lend_pay_acl,a.lend_dis_for,a.lend_dis,a.lend_dis_for " +
        // 29 30 31
        ",a.cons_dis_for,a.cons_dis,a.cons_dis_acl" +
        // 32 33 34 35 36 37
        ", isnull( a.repl_pay_for , 0 ) , isnull( a.repl_pay , 0 ) , isnull( a.repl_pay_acl , 0 ) , a.repl_dis_for , a.repl_dis , a.repl_dis_acl " +
        // 38 39 40
        ",a.ask_net_for , a.ask_net , a.ask_net_acl " +
        // 41 42 43 44
        ",a.aid_ym , a.aid_show , c.social , b.permit_emaker " +
        // 45 46 47 48
        ",a.remark_1 , a.remark_2 , b.permit , a.barcode " +
        // 49
        ", case b.sub_cont when 0 then 0 else ( a.ask_tot_for ) / b.sub_cont * 100 end " +
        // 50
        ", case b.sub_cont when 0 then 0 else ( a.ask_tot ) / b.sub_cont * 100 end " +
        // 51
        ", case b.sub_cont when 0 then 0 else( a.ask_tot_acl ) / b.sub_cont * 100 end " +
        // 52
        ", c.prdocode FROM ask_mey a , contract b , prdt c WHERE a.casecode = '" + casecode + "' and a.contract_id = '" + ret[i][9] + "' and a.ask_mey_id = '"
        + ret[i][10] + "' and a.ask_time = '" + ret[i][11] + "' and a.casecode = b.casecode and a.contract_id = b.contract_id and b.prdocode = c.prdocode ";
    String str_mey[][] = db.queryFromPool(sql);

    // 20110413 剩餘保留款 設定
    String keep_dis = str_mey[0][12];
    String keep_pay = str_mey[0][14];
    String ask_tot_acl = str_mey[0][9];
    String keep_dis_acl = str_mey[0][12];
    String keep_pay_acl = str_mey[0][15];
    String repl_pay_acl = str_mey[0][33];
    // ask_tot_acl = operation.floatAdd( ask_tot_acl , repl_pay_acl , 0
    // );//20150416金花加 估驗+代工請款
    String persent = operation.floatMultiply(operation.floatDivide(operation.floatSubtract(keep_dis_acl, keep_pay_acl, 0), ask_tot_acl, 6), "100", 2);

    System.out.println(" keep_dis_acl  " + keep_dis_acl + " keep_pay_acl " + keep_pay_acl + " ask_tot_acl " + ask_tot_acl + " 剩餘保留款 " + persent);
    // if( Double.parseDouble( keep_pay ) > 0 ){
    // 20121212 智哥改
    if (Double.parseDouble(keep_dis) > 0.0 && Double.parseDouble(ask_tot_acl) > 0.0) {
      exeFun.putDataIntoExcel(29, 13, "剩餘保留款：" + persent + "%", objectSheet1);
    }

    // 20130114 怡珠加 請款比例 ( 估驗金額 - 代工請款 ) / 合約金額 * 100
    String fin_cal_for = "";
    String fin_cal = "";
    String fin_cal_acl = "";
    if (Double.parseDouble(str_mey[0][6]) > 0.0) {
      // 前期
      fin_cal_for = operation.floatAdd(str_mey[0][48], "0", 1);

      // 本期
      fin_cal = operation.floatAdd(str_mey[0][49], "0", 1);

      // 累計
      fin_cal_acl = operation.floatAdd(fin_cal_for.trim(), fin_cal.trim(), 1);

      // 20130617 阿智 誤差計算 超過100 只能 100
      String sub_amt = "0";
      String sum_amt = operation.floatAdd(fin_cal_for, fin_cal, 1);
      System.out.println(" fin_cal_for " + fin_cal_for + " fin_cal " + fin_cal + " sum_amt " + sum_amt);
      if (Double.parseDouble(sum_amt) > 100.0) {
        sub_amt = operation.floatSubtract("100", sum_amt, 1);
        System.out.println(" fin_cal " + fin_cal + " sub_amt " + sub_amt);
        fin_cal = operation.floatAdd(fin_cal, sub_amt, 1);
        fin_cal_acl = operation.floatAdd(fin_cal_for, fin_cal, 1);
      }

      if (fin_cal_for.length() == 4) {
        fin_cal_for = " " + fin_cal_for;
      }
      if (fin_cal_for.length() == 3) {
        fin_cal_for = "  " + fin_cal_for;
      }
      if (fin_cal.length() == 4) {
        fin_cal = " " + fin_cal;
      }
      if (fin_cal.length() == 3) {
        fin_cal = "  " + fin_cal;
      }
      if (fin_cal_acl.length() == 4) {
        fin_cal_acl = " " + fin_cal_acl;
      }
      if (fin_cal_acl.length() == 3) {
        fin_cal_acl = "  " + fin_cal_acl;
      }
      // 20160121 金花姐加 若累計是100了，前期直接設定100 本期 0
      if ("100.0".equals(fin_cal_acl) && (Integer.parseInt(str_mey[0][10]) > Integer.parseInt(str_mey[0][6]))) {
        exeFun.putDataIntoExcel(3, 4, "100.0%", objectSheet1);// 前期
        exeFun.putDataIntoExcel(3, 5, "0.0%", objectSheet1);// 本期
      } else {
        exeFun.putDataIntoExcel(3, 4, "" + fin_cal_for + "%", objectSheet1);//
        exeFun.putDataIntoExcel(3, 5, "" + fin_cal + "%", objectSheet1);//
      }
      exeFun.putDataIntoExcel(3, 6, "" + fin_cal_acl + "%", objectSheet1);// 累計
    }

    // 左上
    exeFun.putDataIntoExcel(9, 5 + dis, casecode, objectSheet1);
    ret[i][6] = ret[i][6].trim();
    if (ret[i][6].length() > 7) {
      exeFun.putDataIntoExcel(34, 8 + dis, ret[i][6].substring(0, 7).trim(), objectSheet1);// 工程名稱
    } else {
      exeFun.putDataIntoExcel(34, 8 + dis, ret[i][6], objectSheet1);// 工程名稱
    }
    // exeFun.putDataIntoExcel(12,5+dis,"廠商名稱："+str_mey[0][1]+"("+str_mey[0][51].trim()+")"
    // , objectSheet1) ; //廠商
    if (str_mey[0][1].length() >= 4) {
      exeFun.putDataIntoExcel(12, 5 + dis, "廠商名稱：" + str_mey[0][1].substring(0, 4), objectSheet1); // 廠商
    } else {
      exeFun.putDataIntoExcel(12, 5 + dis, "廠商名稱：" + str_mey[0][1], objectSheet1); // 廠商
    }
    exeFun.putDataIntoExcel(24, 6 + dis, str_mey[0][2], objectSheet1); // 請款日
    exeFun.putDataIntoExcel(17, 6 + dis, str_mey[0][3], objectSheet1); // 期別
    exeFun.putDataIntoExcel(31, 6 + dis, str_mey[0][4], objectSheet1); // 次數
    exeFun.putDataIntoExcel(24, 5 + dis, str_mey[0][5], objectSheet1); // 編號
    exeFun.putDataIntoExcel(9, 6 + dis, str_mey[0][6], objectSheet1); // 合約金額 b.sub_cont

    // 主表
    exeFun.putDataIntoExcel(2, 10 + dis, operation.floatAdd(str_mey[0][7], str_mey[0][31], 6), objectSheet1); // ask_tot+repl_pay
    exeFun.putDataIntoExcel(2, 12 + dis, operation.floatAdd(str_mey[0][8], str_mey[0][32], 6), objectSheet1);
    exeFun.putDataIntoExcel(2, 14 + dis, operation.floatAdd(str_mey[0][9], str_mey[0][33], 6), objectSheet1);
    exeFun.putDataIntoExcel(5, 10 + dis, str_mey[0][10], objectSheet1); // keep_dis
    exeFun.putDataIntoExcel(5, 12 + dis, str_mey[0][11], objectSheet1);
    exeFun.putDataIntoExcel(5, 14 + dis, str_mey[0][12], objectSheet1);
    exeFun.putDataIntoExcel(8, 10 + dis, str_mey[0][13], objectSheet1); // keep_pay
    exeFun.putDataIntoExcel(8, 12 + dis, str_mey[0][14], objectSheet1);
    exeFun.putDataIntoExcel(8, 14 + dis, str_mey[0][15], objectSheet1);
    exeFun.putDataIntoExcel(11, 10 + dis, str_mey[0][16], objectSheet1); // down_pay
    exeFun.putDataIntoExcel(11, 12 + dis, str_mey[0][17], objectSheet1);
    exeFun.putDataIntoExcel(11, 14 + dis, str_mey[0][18], objectSheet1);
    exeFun.putDataIntoExcel(15, 10 + dis, str_mey[0][19], objectSheet1); // down_dis
    exeFun.putDataIntoExcel(15, 12 + dis, str_mey[0][20], objectSheet1);
    exeFun.putDataIntoExcel(15, 14 + dis, str_mey[0][21], objectSheet1);
    /*
     * exeFun.putDataIntoExcel(18,10+dis, str_mey[0][22] , objectSheet1) ;
     * //lend_pay exeFun.putDataIntoExcel(18,12+dis, str_mey[0][23] , objectSheet1)
     * ; exeFun.putDataIntoExcel(18,14+dis, str_mey[0][24] , objectSheet1) ;
     * exeFun.putDataIntoExcel(18,10+dis, str_mey[0][25] , objectSheet1) ;
     * //lend_dis exeFun.putDataIntoExcel(18,12+dis, str_mey[0][26] , objectSheet1)
     * ; exeFun.putDataIntoExcel(18,14+dis, str_mey[0][27] , objectSheet1) ;
     */
    exeFun.putDataIntoExcel(18, 10 + dis, str_mey[0][28], objectSheet1); // cons_dis
    exeFun.putDataIntoExcel(18, 12 + dis, str_mey[0][29], objectSheet1);
    exeFun.putDataIntoExcel(18, 14 + dis, str_mey[0][30], objectSheet1);
    exeFun.putDataIntoExcel(21, 10 + dis, str_mey[0][34], objectSheet1); // repl_dis
    exeFun.putDataIntoExcel(21, 12 + dis, str_mey[0][35], objectSheet1);
    exeFun.putDataIntoExcel(21, 14 + dis, str_mey[0][36], objectSheet1);
    exeFun.putDataIntoExcel(25, 10 + dis, str_mey[0][37], objectSheet1); // ask_net
    exeFun.putDataIntoExcel(25, 12 + dis, str_mey[0][38], objectSheet1);
    exeFun.putDataIntoExcel(25, 14 + dis, str_mey[0][39], objectSheet1);
    // exeFun.putDataIntoExcel(15,20+dis, "營造："+str_mey[0][44] , objectSheet1) ;
    // //remark_
    // exeFun.putDataIntoExcel(15,22+dis, "成控："+str_mey[0][45] , objectSheet1) ;
    exeFun.putDataIntoExcel(4, 35 + dis, str_mey[0][44], objectSheet1); // remark_
    exeFun.putDataIntoExcel(4, 36 + dis, str_mey[0][45], objectSheet1);

    // barcode
    // 用日期來區分新與舊
    if (Integer.parseInt(ret[i][3]) >= ondate) {
      exeFun.putDataIntoExcel(28, 1, "*" + str_mey[0][47] + "*", objectSheet1);
    }

    sql = "SELECT aid_ym,aid_show FROM ask_mey WHERE casecode = '" + casecode + "' and contract_id = '" + ret[i][9] + "' and ask_time = '"
        + operation.floatSubtract(ret[i][11], "1", 0) + "' ";
    String code[][] = db.queryFromPool(sql);

    // 右上*
    if ("1".equals(ret[i][11])) {
      System.out.println(" 統一編號 1 " + str_mey[0][42]);
      // exeFun.putDataIntoExcel(30,1+dis, casecode , objectSheet1) ;
      // exeFun.putDataIntoExcel(34,9+dis,
      // casecode+"-"+str_mey[0][40]+"-"+str_mey[0][41] , objectSheet1) ;//請款編號
      exeFun.putDataIntoExcel(34, 9 + dis, str_mey[0][40] + "-" + str_mey[0][41], objectSheet1);// 請款編號
      exeFun.putDataIntoExcel(34, 12 + dis, str_mey[0][42].trim(), objectSheet1); // 統一編號
      // exeFun.putDataIntoExcel(30,6+dis, str_mey[0][38].trim() , objectSheet1) ;
      // //實附金額
    } else {
      System.out.println(" 統一編號 2 " + str_mey[0][42]);
      // exeFun.putDataIntoExcel(30,1+dis, casecode , objectSheet1) ;
      // exeFun.putDataIntoExcel(34,9+dis,
      // casecode+"-"+str_mey[0][40]+"-"+str_mey[0][41] , objectSheet1) ;//請款編號
      // exeFun.putDataIntoExcel(34,10+dis, casecode+"-"+code[0][0]+"-"+code[0][1] ,
      // objectSheet1) ;//前期請款編號
      exeFun.putDataIntoExcel(34, 9 + dis, str_mey[0][40] + "-" + str_mey[0][41], objectSheet1);// 請款編號
      exeFun.putDataIntoExcel(34, 10 + dis, code[0][0] + "-" + code[0][1], objectSheet1);// 前期請款編號
      exeFun.putDataIntoExcel(34, 12 + dis, str_mey[0][42].trim(), objectSheet1); // 統一編號
      // exeFun.putDataIntoExcel(30,6+dis, str_mey[0][38].trim() , objectSheet1) ;
      // //實附金額
    }

    // ------------------財務審核 檢核--------------------//
    System.out.println("permit = " + str_mey[0][46] + "permit_e = " + str_mey[0][43]);
    // >300w
    String range = "0";
    if (Double.parseDouble(str_mey[0][6]) >= 3000000) {
      if (!"8".equals(str_mey[0][43].trim()) || !"1".equals(str_mey[0][46].trim())) {
        range = operation.floatMultiply(str_mey[0][6], "0.3", 6);
        System.out.println("range " + range + " ask_tot " + str_mey[0][9]);
        if (Double.parseDouble(range) < Double.parseDouble(str_mey[0][9])) {
          exeFun.putDataIntoExcel(29, 15 + dis, "合約未製作完成", objectSheet1);
        }
      }
      if ("8".equals(str_mey[0][43].trim())) exeFun.putDataIntoExcel(29, 15 + dis, "合約製作完成", objectSheet1);
    }

    // 50~300w
    range = "0";
    if (Double.parseDouble(str_mey[0][6]) < 3000000 && Double.parseDouble(str_mey[0][6]) >= 500000) {

      if (!"8".equals(str_mey[0][43].trim()) || !"1".equals(str_mey[0][46].trim())) {
        range = operation.floatMultiply(str_mey[0][6], "0.3", 6);
        System.out.println("range " + range + " ask_tot " + str_mey[0][9]);
        if (Double.parseDouble(range) < Double.parseDouble(str_mey[0][9])) {
          exeFun.putDataIntoExcel(29, 15 + dis, "合約未製作完成", objectSheet1);
        }
      }
      if ("8".equals(str_mey[0][43].trim())) exeFun.putDataIntoExcel(29, 15 + dis, "合約製作完成", objectSheet1);

    }

    // -------------------20140904 加入追加減訊息----------------//
    String sql_add = "";
    String str_add[][] = {};
    String sql_add_permit = "";
    String str_add_permit[][] = {};
    String sum_add = "0";
    sql_add = " select isnull( cadd_time , 0 ) , sum(sub_cont) , sum(acl_ask)  from contract_item  where casecode = '" + casecode + "' and contract_id = '" + ret[i][9]
        + "'  group by cadd_time  order by cadd_time ";
    str_add = db.queryFromPool(sql_add);

    String message_add = "";
    for (int j = 0; j < str_add.length; j++) {

      if ("0".equals(str_add[j][0])) {
        message_add += " 原：" + str_add[j][1];
      } else {
        // 合約製作完成
        sql_add_permit = " select permit  from contract_add_master  where casecode = '" + casecode + "' and contract_id = '" + ret[i][9] + "' and add_time = '"
            + str_add[j][0] + "' ";
        str_add_permit = db.queryFromPool(sql_add_permit);

        // >300w
        if (str_add_permit.length > 0) {
          System.out.println(" 追 " + str_add[j][0] + " permit " + str_add_permit[0][0]);
          range = "0";
          String remark = "";
          if (Double.parseDouble(str_add[j][1]) >= 3000000) {
            if (!"99".equals(str_add_permit[0][0].trim())) {
              range = operation.floatMultiply(str_add[j][1], "0.3", 6);
              System.out.println("追加減 >300 range " + range + " acl_ask " + str_add[j][2]);
              if (Double.parseDouble(range) < Double.parseDouble(str_add[j][2])) {
                remark = "(合約未製作完成)";
              }
            }
            if ("99".equals(str_add_permit[0][0].trim())) remark = "(合約製作完成)";
          }

          // 50~300w
          range = "0";
          if (Double.parseDouble(str_add[j][1]) < 3000000 && Double.parseDouble(str_add[j][1]) >= 500000) {
            if (!"99".equals(str_add_permit[0][0].trim())) {
              range = operation.floatMultiply(str_add[j][1], "0.3", 6);
              System.out.println("50~300 range " + range + " acl_ask " + str_add[j][2]);
              if (Double.parseDouble(range) < Double.parseDouble(str_add[j][2])) {
                remark = "(合約未製作完成)";
              }
            }
            if ("99".equals(str_add_permit[0][0].trim())) remark = "(合約製作完成)";
          }

          // 負值
          range = "0";
          if (Double.parseDouble(str_add[j][1]) < 0) {
            if (!"99".equals(str_add_permit[0][0].trim())) {
              System.out.println(" < 0 range " + range + "");
              remark = "(合約未製作完成)";
            }
            if ("99".equals(str_add_permit[0][0].trim())) remark = "(合約製作完成)";
          }
          message_add += " 第" + str_add[j][0] + "次：" + str_add[j][1] + remark;
        }
      }
      sum_add = operation.floatAdd(sum_add, str_add[j][1], 0);
    }
    message_add += " 累計：" + sum_add;
    exeFun.putDataIntoExcel(2, 30 + dis, message_add, objectSheet1); // 追加減

    // -------------------------------------------------------------------------副表------------------------------------------------------------------------------//

    int item_count = 0;
    sql = "SELECT a.budflr,b.stemname+a.imfmrk+a.spemrk,c.imfnum,a.connum,c.addnum,a.stemunit,a.conprc,a.fornum,a.for_ask,a.imfnum,a.sub_ask,a.aclnum,a.acl_ask "
        + "FROM ask_item a , costsec b , budcost c WHERE a.casecode = '" + casecode + "' and a.ask_mey_id = '" + ret[i][10] + "' and a.stemcode = b.stemcode "
        + "and a.casecode = c.casecode and a.budflr = c.budflr and a.stemcode = c.stemcode and a.budget_ser = c.budget_ser";
    String mey_item[][] = db.queryFromPool(sql);

    if (mey_item.length < 5 && (Double.parseDouble(str_mey[0][31]) > 0 || Double.parseDouble(str_mey[0][32]) > 0)) {
      // 4內筆 + 代工請款
      for (int j = 0; j < mey_item.length; j++) {
        String stemname = mey_item[j][1];
        if (stemname.length() > 15) {
          stemname = stemname.substring(0, 15);
        }
        exeFun.putDataIntoExcel(0, 19 + item_count + dis, mey_item[j][0], objectSheet1);// budflr
        exeFun.putDataIntoExcel(1, 19 + item_count + dis, stemname, objectSheet1);// stemname
        // exeFun.putDataIntoExcel(6,11+j+dis, mey_item[j][2] , objectSheet1) ;//imfnum
        exeFun.putDataIntoExcel(6, 19 + item_count + dis, mey_item[j][3], objectSheet1);// connum
        // exeFun.putDataIntoExcel(10,11+j+dis, mey_item[j][4] , objectSheet1) ;//addnum
        exeFun.putDataIntoExcel(4, 19 + item_count + dis, mey_item[j][5].trim(), objectSheet1);// stemunit
        exeFun.putDataIntoExcel(6, 20 + item_count + dis, mey_item[j][6], objectSheet1);// conprc
        exeFun.putDataIntoExcel(11, 19 + item_count + dis, mey_item[j][7], objectSheet1);// fornum
        exeFun.putDataIntoExcel(11, 20 + item_count + dis, mey_item[j][8], objectSheet1);// for_ask
        exeFun.putDataIntoExcel(19, 19 + item_count + dis, mey_item[j][9], objectSheet1);// imfnum
        exeFun.putDataIntoExcel(19, 20 + item_count + dis, mey_item[j][10], objectSheet1);// sub_ask
        exeFun.putDataIntoExcel(29, 19 + item_count + dis, mey_item[j][11], objectSheet1);// aclnum
        exeFun.putDataIntoExcel(29, 20 + item_count + dis, mey_item[j][12], objectSheet1);// acl_ask
        item_count += 2;
      }
      // there
      // 代工請款
      if (Double.parseDouble(str_mey[0][31]) > 0 || Double.parseDouble(str_mey[0][32]) > 0) {
        exeFun.putDataIntoExcel(1, 19 + item_count + dis, "代工點工(料)", objectSheet1);
        if (Double.parseDouble(str_mey[0][31]) == 0) {
          exeFun.putDataIntoExcel(11, 19 + item_count + dis, "0", objectSheet1);
        } else {
          exeFun.putDataIntoExcel(11, 19 + item_count + dis, "1", objectSheet1);
        }
        if (Double.parseDouble(str_mey[0][32]) == 0) {
          exeFun.putDataIntoExcel(19, 19 + item_count + dis, "0", objectSheet1);
        } else {
          exeFun.putDataIntoExcel(19, 19 + item_count + dis, "1", objectSheet1);
        }
        if (Double.parseDouble(str_mey[0][33]) == 0) {
          exeFun.putDataIntoExcel(29, 19 + item_count + dis, "0", objectSheet1);
        } else {
          exeFun.putDataIntoExcel(29, 19 + item_count + dis, "1", objectSheet1);
        }
        exeFun.putDataIntoExcel(4, 19 + item_count + dis, "式", objectSheet1);// stemunit
        exeFun.putDataIntoExcel(11, 20 + item_count + dis, str_mey[0][31], objectSheet1);
        exeFun.putDataIntoExcel(19, 20 + item_count + dis, str_mey[0][32], objectSheet1);
        exeFun.putDataIntoExcel(29, 20 + item_count + dis, str_mey[0][33], objectSheet1);
      }
      /*
       * Dispatch.call(objectSheet1, "Activate"); exeFun.doStopAction(true) ;
       * exeFun.setVisiblePropertyOnFlow(true, retVector) ; // 控制顯不顯示 Excel
       * exeFun.setSheetEndView(false) ;
       */
    } else if (mey_item.length >= 5 && (Double.parseDouble(str_mey[0][31]) > 0 || Double.parseDouble(str_mey[0][32]) > 0)) {

      // 大於5筆 + 代工請款

      // --------------主表----------------//
      exeFun.putDataIntoExcel(1, 19 + dis, "本工程款", objectSheet1);// stemname
      exeFun.putDataIntoExcel(4, 19 + item_count + dis, "式", objectSheet1);// stemunit

      if (Double.parseDouble(str_mey[0][7]) == 0) {
        exeFun.putDataIntoExcel(11, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(11, 19 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][8]) == 0) {
        exeFun.putDataIntoExcel(19, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(19, 19 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][9]) == 0) {
        exeFun.putDataIntoExcel(29, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(29, 19 + dis, "1", objectSheet1);
      }
      exeFun.putDataIntoExcel(11, 20 + item_count + dis, str_mey[0][7], objectSheet1);
      exeFun.putDataIntoExcel(19, 20 + item_count + dis, str_mey[0][8], objectSheet1);
      exeFun.putDataIntoExcel(29, 20 + item_count + dis, str_mey[0][9], objectSheet1);

      // 代工請款
      exeFun.putDataIntoExcel(1, 21 + dis, "代工點工(料)", objectSheet1);
      if (Double.parseDouble(str_mey[0][31]) == 0) {
        exeFun.putDataIntoExcel(11, 21 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(11, 21 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][32]) == 0) {
        exeFun.putDataIntoExcel(19, 21 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(19, 21 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][33]) == 0) {
        exeFun.putDataIntoExcel(29, 21 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(29, 21 + dis, "1", objectSheet1);
      }
      exeFun.putDataIntoExcel(4, 21 + dis, "式", objectSheet1);// stemunit
      exeFun.putDataIntoExcel(11, 22 + dis, str_mey[0][31], objectSheet1);
      exeFun.putDataIntoExcel(19, 22 + dis, str_mey[0][32], objectSheet1);
      exeFun.putDataIntoExcel(29, 22 + dis, str_mey[0][33], objectSheet1);

    } else if (mey_item.length > 5) {
      System.out.println("大於5筆");
      // 大於5筆

      // --------------主表----------------//
      exeFun.putDataIntoExcel(1, 19 + dis, "本工程款", objectSheet1);// stemname
      exeFun.putDataIntoExcel(4, 19 + item_count + dis, "式", objectSheet1);// stemunit

      if (Double.parseDouble(str_mey[0][7]) == 0) {
        exeFun.putDataIntoExcel(11, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(11, 19 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][8]) == 0) {
        exeFun.putDataIntoExcel(19, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(19, 19 + dis, "1", objectSheet1);
      }
      if (Double.parseDouble(str_mey[0][9]) == 0) {
        exeFun.putDataIntoExcel(29, 19 + dis, "0", objectSheet1);
      } else {
        exeFun.putDataIntoExcel(29, 19 + dis, "1", objectSheet1);
      }

      exeFun.putDataIntoExcel(11, 20 + item_count + dis, str_mey[0][7], objectSheet1);
      exeFun.putDataIntoExcel(19, 20 + item_count + dis, str_mey[0][8], objectSheet1);
      exeFun.putDataIntoExcel(29, 20 + item_count + dis, str_mey[0][9], objectSheet1);

    } else if (mey_item.length <= 5) {
      // 5筆以內 無代工請款
      for (int j = 0; j < mey_item.length; j++) {
        String stemname = mey_item[j][1];
        if (stemname.length() > 15) {
          stemname = stemname.substring(0, 15);
        }
        exeFun.putDataIntoExcel(0, 19 + item_count + dis, mey_item[j][0], objectSheet1);// budflr
        exeFun.putDataIntoExcel(1, 19 + item_count + dis, stemname, objectSheet1);// stemname
        // exeFun.putDataIntoExcel(6,11+j+dis, mey_item[j][2] , objectSheet1) ;//imfnum
        exeFun.putDataIntoExcel(6, 19 + item_count + dis, mey_item[j][3], objectSheet1);// connum
        // exeFun.putDataIntoExcel(10,11+j+dis, mey_item[j][4] , objectSheet1) ;//addnum
        exeFun.putDataIntoExcel(4, 19 + item_count + dis, mey_item[j][5].trim(), objectSheet1);// stemunit
        exeFun.putDataIntoExcel(6, 20 + item_count + dis, mey_item[j][6], objectSheet1);// conprc
        exeFun.putDataIntoExcel(11, 19 + item_count + dis, mey_item[j][7], objectSheet1);// fornum
        exeFun.putDataIntoExcel(11, 20 + item_count + dis, mey_item[j][8], objectSheet1);// for_ask
        exeFun.putDataIntoExcel(19, 19 + item_count + dis, mey_item[j][9], objectSheet1);// imfnum
        exeFun.putDataIntoExcel(19, 20 + item_count + dis, mey_item[j][10], objectSheet1);// sub_ask
        exeFun.putDataIntoExcel(29, 19 + item_count + dis, mey_item[j][11], objectSheet1);// aclnum
        exeFun.putDataIntoExcel(29, 20 + item_count + dis, mey_item[j][12], objectSheet1);// acl_ask
        item_count += 2;
      }
      /*
       * Dispatch.call(objectSheet1, "Activate"); exeFun.doStopAction(true) ;
       * exeFun.setVisiblePropertyOnFlow(true, retVector) ; // 控制顯不顯示 Excel
       * exeFun.setSheetEndView(false) ;
       */
      // Dispatch.call(objectSheet1, "PrintOut"); //<=5設定直接列印

    }

//------------------------------------------------------------------------------------代工扣款--------------------------------------------------------------------------------------//

    sql = "select ask_mey_id,work_ser from ask_repl where casecode ='" + casecode + "' and ask_mey_id_d = '" + ret[i][10] + "' ";
    String tmp_mey_id[][] = db.queryFromPool(sql);

    String mcase_pubser = getValue("field24").trim();
    String itemtot = "0";
    String repl_dis = "0";
    String repl_dis_d = "0";
    int count = 0;
    if (tmp_mey_id.length > 0) {
      if (tmp_mey_id.length % 15 == 0) {
        count = tmp_mey_id.length / 15;
      } else {
        count = tmp_mey_id.length / 15 + 1;
      }
    }

    String tmp[][] = new String[tmp_mey_id.length][15];

    for (int j = 0; j < tmp_mey_id.length; j++) {
      // 查詢可扣款之值並加入vec1
      if (Integer.parseInt(mcase_pubser) >= 2) {
        sql = "SELECT rtrim(c.casecode)+'-'+c.aid_ym+'-'+ltrim(str(c.aid_show)),c.ask_date,rtrim(c.casecode)+'-'+d.cid_ym+'-'+ltrim(str(d.cid_show)),"
            + "g.prdinit,b.stemunit,b.imfnum,b.imfprice,b.imfnum*b.imfprice,b.imfmrk,b.itemtot,f.repl_dis,'0',b.itemno,g.prdocode,c.ask_mey_id  "
            + "FROM check_master a , ask_work b , ask_mey c , contract d , ask_repl f  , prdt g WHERE a.casecode = '" + casecode + "' and b.prdocode = '" + ret[i][15]
            + "' and b.ask_mey_id = '" + tmp_mey_id[j][0] + "' and b.work_ser = '" + tmp_mey_id[j][1] + "' and b.ask_mey_id <> 0 and f.ask_mey_id_d = '" + ret[i][10]
            + "' and b.itemtot = b.repl_dis and a.casecode = b.casecode and a.check_id = b.check_id and a.casecode = c.casecode and b.ask_mey_id = c.ask_mey_id "
            + "and c.casecode = d.casecode and c.contract_id = d.contract_id and c.casecode = f.casecode and c.ask_mey_id = f.ask_mey_id and d.prdocode  = g.prdocode "
            + "and b.work_ser = f.work_ser ";
      } else {
        sql = "SELECT rtrim(c.casecode)+'-'+c.aid_ym+'-'+ltrim(str(c.aid_show)),c.ask_date,rtrim(c.casecode)+'-'+d.cid_ym+'-'+ltrim(str(d.cid_show)),"
            + "g.prdinit,b.stemunit,b.imfnum,b.imfprice,b.imfnum*b.imfprice,b.imfmrk,b.itemtot,f.repl_dis,'0',b.itemno,g.prdocode,c.ask_mey_id  "
            + "FROM  ask_work b , ask_mey c , contract d , ask_repl f  , prdt g WHERE b.casecode = '" + casecode + "' and b.prdocode = '" + ret[i][15] + "' "
            + "and b.ask_mey_id = '" + tmp_mey_id[j][0] + "' and b.work_ser = '" + tmp_mey_id[j][1] + "' and b.ask_mey_id <> 0 and f.ask_mey_id_d = '" + ret[i][10] + "' "
            + "and b.itemtot = b.repl_dis and b.ask_mey_id = c.ask_mey_id and c.casecode = d.casecode and c.contract_id = d.contract_id "
            + "and c.casecode = f.casecode and c.ask_mey_id = f.ask_mey_id and d.prdocode  = g.prdocode and b.work_ser = f.work_ser ";
      }
      String repl_prdt[][] = db.queryFromPool(sql);
      if (repl_prdt.length > 0) {
        tmp[j] = repl_prdt[0];
      }
      if ("1".equals(tmp[j][12])) {
        tmp[j][12] = "垃圾";
      }
      if ("2".equals(tmp[j][12])) {
        tmp[j][12] = "進度";
      }
      if ("3".equals(tmp[j][12])) {
        tmp[j][12] = "品質";
      }
      if ("4".equals(tmp[j][12])) {
        tmp[j][12] = "外勞";
      }
    }

    // --------------------------------主表(代工記錄)----------------------------------//

    String repl_ms1 = "";
    String repl_ms2 = "";
    if (tmp.length > 8) {
      for (int k = 0; k < 2; k++) {
        if (k == 0) {
          for (int j = 0; j < 4; j++) {
            repl_ms1 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
        if (k == 1) {
          for (int j = 4; j < 8; j++) {
            repl_ms2 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
      }
      exeFun.putDataIntoExcel(2, 31 + dis, repl_ms1, objectSheet1);
      exeFun.putDataIntoExcel(2, 32 + dis, repl_ms2 + "篇幅有限,請參閱代工資料表", objectSheet1);
    } else if (tmp.length == 8) {
      for (int k = 0; k < 2; k++) {
        if (k == 0) {
          for (int j = 0; j < 4; j++) {
            repl_ms1 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
        if (k == 1) {
          for (int j = 4; j < 8; j++) {
            repl_ms2 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
      }
      exeFun.putDataIntoExcel(2, 31 + dis, repl_ms1, objectSheet1);
      exeFun.putDataIntoExcel(2, 32 + dis, repl_ms2, objectSheet1);
    } else if (tmp.length < 8 && tmp.length > 4) {
      for (int k = 0; k < 2; k++) {
        if (k == 0) {
          for (int j = 0; j < 4; j++) {
            repl_ms1 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
        if (k == 1) {
          for (int j = 4; j < tmp.length; j++) {
            repl_ms2 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
          }
        }
      }
      exeFun.putDataIntoExcel(2, 31 + dis, repl_ms1, objectSheet1);
      exeFun.putDataIntoExcel(2, 32 + dis, repl_ms2, objectSheet1);
    } else if (tmp.length <= 4) {
      for (int j = 0; j < tmp.length; j++) {
//            if( !"".equals(tmp[j][3].trim()) ) 
        repl_ms1 += "(" + tmp[j][3] + "-" + tmp[j][14] + "=" + tmp[j][10] + ")";
        System.out.println("repl_ms1 = " + repl_ms1);
      }
      // exeFun.putDataIntoExcel(3,17+dis, repl_ms1 , objectSheet1) ;
      exeFun.putDataIntoExcel(2, 31 + dis, repl_ms1, objectSheet1);
    }

    System.out.println("20+dis" + (20 + dis));

    if (!"DBAX".equals(user)) {
      Dispatch.call(objectSheet1, "PrintOut"); // <=5設定直接列印
      
//      Dispatch.call(objectSheet1, "Activate");
//      exeFun.doStopAction(true);
//      exeFun.setVisiblePropertyOnFlow(true, retVector); // 控制顯不顯示 Excel
//      exeFun.setSheetEndView(false);
    } else {
      Dispatch.call(objectSheet1, "Activate");
      exeFun.doStopAction(true);
      exeFun.setVisiblePropertyOnFlow(true, retVector); // 控制顯不顯示 Excel
      exeFun.setSheetEndView(false);
    }
    // 釋放 Excel 物件
    exeFun.setPreView(false, stringSavePath + "\\" + datetime.getTime("yymmdd hms") + "Temp.xls");

    exeFun.getReleaseExcelObject(retVector);

    // 20151124有收款單 自動印收款單
    System.out.println("--收款單列印--");
    String sql_rece = " select *  from receive_const_master  where casecode = '" + casecode + "' and ask_mey_id = '" + ret[i][10] + "' and permit <> -99 ";
    String str_rece[][] = db.queryFromPool(sql_rece);
    if (str_rece.length > 0) {
      getButton("button_rece_M").doClick();
    }

    // 有借款沖帳明細 列印
    sql = " select a.casecode , c.ask_mey_id , a.lend_mey_id , a.invoice_serial   from lend_invoice a ,  ask_lend_detail c  where c.casecode = '" + casecode
        + "' and c.ask_mey_id = '" + ret[i][10] + "'            and a.casecode = c.casecode and a.lend_mey_id = c.lend_mey_id "
        + "           and a.invoice_serial in( select b.invoice_serial  from ask_lend_rec_item b  where a.casecode = b.casecode and a.lend_mey_id = b.lend_mey_id "
        + "         and a.invoice_serial = b.invoice_serial  )  ";
    str = db.queryFromPool(sql);
    if (str.length > 0) {
      getButton("lend_rec_v2").doClick();
    }

  }

  public String getInformation() {
    return "---------------button24(button24).defaultValue()----------------";
  }
}
