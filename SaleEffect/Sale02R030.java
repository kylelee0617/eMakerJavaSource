package SaleEffect;

import jcx.jform.bTransaction;
import java.io.*;
import java.util.*;
import jcx.util.*;
import jcx.html.*;
import jcx.db.*;
import com.jacob.activeX.*;
import com.jacob.com.*;

public class Sale02R030 extends bTransaction {
  double double�W�Ƥp�p11Global = 0;
  double double�q���p�pGlobal = 0;
  double double����p�pGlobal = 0;
  double double�ذe�p�pGlobal = 0;
  double double�����p�pGlobal = 0;
  double double��������p�pGlobal = 0; // 2015-11-18 B3018
  double double�Q���p�pGlobal = 0; // 2017-05-02 B4474
  double double�b��p�pGlobal = 0;
  double double�����p�pGlobal = 0;
  double double�W�C���p�pGlobal = 0;
  talk dbSale = getTalk((String) get("put_dbSale"));// SQL2000

  public boolean action(String value) throws Throwable {
    // �^�ǭȬ� true ��ܰ��汵�U�Ӫ���Ʈw���ʩάd��
    // �^�ǭȬ� false ��ܱ��U�Ӥ����������O
    // �ǤJ�� value �� "�s�W","�d��","�ק�","�R��","�C�L","PRINT" (�C�L�w�����C�L���s),"PRINTALL"
    // (�C�L�w���������C�L���s) �䤤���@
    // �N�e�ݪ���Ƨ@�B�z
    if (!isBatchCheckOK()) return false;
    // �� Excel �@�B�z
    doExcel();
    return false;
  }

  // ����ˮ�
  public boolean isBatchCheckOK() throws Throwable {
    // �קO
    if ("".equals(getValue("ProjectID").trim())) {
      message("�קO���i���ťաC");
      getcLabel("ProjectID").requestFocus();
      return false;
    }
    String[][] retAProject = getAProject(getValue("ProjectID").trim());
    if (retAProject.length == 0) {
      message("�L���קO�s�b�C");
      getcLabel("ProjectID").requestFocus();
      return false;
    }
    // �קO����
    String[][] retSale02M020 = getSale02M020();
    if (retSale02M020.length > 0) {
      String[][] retSale02M020Count = getSale02M020(getValue("ProjectID").trim());
      if (retSale02M020Count.length == 0) {
        message("�L�ϥΦ��קO�v���A�Ь���ޫǥD�޶}��!");
        getcLabel("ProjectID").requestFocus();
        return false;
      }
    }

    // �I�q���ñ���饲���n���@flag
    int countOC = 0;

    // �I�q���
    String retDate = "";
    int countDate = 0;
    String stringOrderDateStart = getValue("OrderDateStart").trim();
    if (!"".equals(stringOrderDateStart)) {
      retDate = getDateAC(stringOrderDateStart, "�I�q���(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("OrderDateStart").requestFocus();
        return false;
      }
      setValue("OrderDateStart", retDate);
      countDate++;
      countOC++;
    }
    String stringOrderDateEnd = getValue("OrderDateEnd").trim();
    if (!"".equals(stringOrderDateEnd)) {
      retDate = getDateAC(stringOrderDateEnd, "�I�q���(��)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("OrderDateEnd").requestFocus();
        return false;
      }
      setValue("OrderDateEnd", retDate);
      countDate++;
    }
    if (countDate == 1) {
      message("[�I�q���(�_)(��)] ���P�ɭ���C");
      return false;
    }

    // �ɨ����
    countDate = 0;
    String stringEnougDateStart = getValue("EnougDateStart").trim();
    if (!"".equals(stringEnougDateStart)) {
      retDate = getDateAC(stringEnougDateStart, "�ɨ����(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("EnougDateStart").requestFocus();
        return false;
      }
      setValue("EnougDateStart", retDate);
      countDate++;
    }
    String stringEnougDateEnd = getValue("EnougDateEnd").trim();
    if (!"".equals(stringEnougDateEnd)) {
      retDate = getDateAC(stringEnougDateEnd, "�ɨ����(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("EnougDateEnd").requestFocus();
        return false;
      }
      setValue("EnougDateEnd", retDate);
      countDate++;
    }
    if (countDate == 1) {
      message("[�ɨ����(�_)(��)] ���P�ɭ���C");
      return false;
    }

    // ñ�����
    countDate = 0;
    String stringContrDateStart = getValue("ContrDateStart").trim();
    if (!"".equals(stringContrDateStart)) {
      retDate = getDateAC(stringContrDateStart, "ñ�����(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("ContrDateStart").requestFocus();
        return false;
      }
      setValue("ContrDateStart", retDate);
      countDate++;
      countOC++;
    }
    String stringContrDateEnd = getValue("ContrDateEnd").trim();
    if (!"".equals(stringContrDateEnd)) {
      retDate = getDateAC(stringContrDateEnd, "ñ�����(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("ContrDateEnd").requestFocus();
        return false;
      }
      setValue("ContrDateEnd", retDate);
      countDate++;
    }
    if (countDate == 1) {
      message("[ñ�����(�_)(��)] ���P�ɭ���C");
      return false;
    }

    // �X���|�f
    countDate = 0;
    String stringDateCheckStart = getValue("DateCheckStart").trim();
    String stringDateCheckEnd = getValue("DateCheckEnd").trim();
    if (!"".equals(stringDateCheckStart)) {
      retDate = getDateAC(stringDateCheckStart, "�X���|�f(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("DateCheckStart").requestFocus();
        return false;
      } else {
        setValue("DateCheckStart", retDate);
      }
      countDate++;
    }
    if (!"".equals(stringDateCheckEnd)) {
      retDate = getDateAC(stringDateCheckEnd, "�X���|�f(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("DateCheckEnd").requestFocus();
        return false;
      } else {
        setValue("DateCheckEnd", retDate);
      }
      countDate++;
    }
    if (countDate == 1) {
      message("[�X���|�f���(�_)(��)] ���P�ɭ���C");
      return false;
    }

    // ñ�������
    countDate = 0;
    String stringDateRangeStart = getValue("DateRangeStart").trim();
    if (!"".equals(stringDateRangeStart)) {
      retDate = getDateAC(stringDateRangeStart, "ñ�������(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("DateRangeStart").requestFocus();
        return false;
      } else {
        setValue("DateRangeStart", retDate);
      }
      countDate++;
    }
    String stringDateRangeEnd = getValue("DateRangeEnd").trim();
    if (!"".equals(stringDateRangeEnd)) {
      retDate = getDateAC(stringDateRangeEnd, "ñ�������(�_)");
      if (retDate.length() != 10) {
        message(retDate);
        getcLabel("DateRangeEnd").requestFocus();
        return false;
      } else {
        setValue("DateRangeEnd", retDate);
      }
      countDate++;
    }
    if (countDate == 1) {
      message("[ñ���������(�_)(��)] ���P�ɭ���C");
      return false;
    }

    if (countOC == 0) {
      message("[�I�q���] �P [ñ�����] �����ܤ@�C");
      return false;
    }

    // Check File �O�_�s�b
    message("");
    return true;
  }
  // ����ˮ� END

  public void doExcel() throws Throwable {
    // �� Function ��� Excel ��m�A�ð���ӵ{��
    String stringExcelName = "";
    String stringFunction = getValue("Function").trim();
    String stringDisplay = "";
    System.out.println("stringFunction>>>" + stringFunction);

    switch (Integer.parseInt(stringFunction)) {
    case 0:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailSale1.XLS";// �X��Ы�²��
      } else {
        stringExcelName = "SaleOutDetailSale.XLS";// �X��ЫΫD²��
      }
      doSaleOutDetailSale(stringExcelName, "A_Sale"); // 20090603 �ץ�
      break;
    case 3:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailSale1.XLS";// �X�⨮��²��
      } else {
        stringExcelName = "SaleOutDetailSale.XLS";// �X�⨮��D²��
      }
      doSaleOutDetailSale(stringExcelName, "A_Sale"); // 20090603 �ץ�
      break;
    case 1:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailRent1.XLS";// �X���Ы�²��
      } else {
        stringExcelName = "SaleOutDetailRent.XLS";// �X���ЫΫD²��
      }
      doSaleOutDetailRent(stringExcelName, "A_Sale");// 20090603 �ץ�
      break;
    case 4:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailRent1.XLS";// �X������²��
      } else {
        stringExcelName = "SaleOutDetailRent.XLS";// �X������D²��
      }
      doSaleOutDetailRent(stringExcelName, "A_Sale");// 20090603 �ץ�
      break;
    case 2:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailDelete1.XLS"; // �h��Ы�²��
      } else {
        stringExcelName = "SaleOutDetailDelete.XLS"; // �h��ЫΫD²��
      }
      doSaleOutDetailDelete(stringExcelName, "A_Sale1");
      break;
    case 5:
      stringDisplay = getValue("Display").trim();
      if ("1".equals(stringDisplay)) {
        stringExcelName = "SaleOutDetailDelete1.XLS";// �h�ᨮ��²��
      } else {
        stringExcelName = "SaleOutDetailDelete.XLS";// �h�ᨮ��D²��
      }
      doSaleOutDetailDelete(stringExcelName, "A_Sale1");
      break;
    }
  }

  public boolean doSaleOutDetailDelete(String stringExcelName, String stringTableSelect) throws Throwable {
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(6, 20, 33, 1);
    // ��ƳB�z
    String stringHComLocal = "";
    String stringLComLocal = "";
    String stringSql = "";
    String stringProjectID = getValue("ProjectID").trim();
    String stringOrderDateStart = getValue("OrderDateStart").trim();
    String stringOrderDateEnd = getValue("OrderDateEnd").trim();
    String stringSqlAnd = getSqlAnd() + getSqlAnd2();
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    String stringHouseCar = "";
    String stringFunction = getValue("Function").trim();
    String stringThisType = "";
    boolean booleanHouse = true;
    boolean booleanLand = true;

    if ("".equals(stringOrderDateStart)) {
      message("[�I�q/�h����]�A����");
      return false;
    }

    //
    switch (Integer.parseInt(stringFunction)) {
    case 0:
    case 1:
    case 2:
      stringThisType = "�Фl"; // �Фl����
      break;
    case 3:
    case 4:
    case 5:
      stringThisType = "����"; // �Фl����
      break;
    }
    switch (Integer.parseInt(stringHouseLand)) {
    case 0:
      stringHouseCar = "";
      break;
    case 1:
      stringHouseCar = "H_";
      break;
    case 2:
      stringHouseCar = "L_";
      break;
    }
    /*
     * 0.Position 1.PositionRent 2.Car 3.CarRent 4.Custom 5.OrderDate 6.OrderMon1
     * 7.OrderMon2 8.OrderMon3 9.EnougDate 10.EnougMon1 11.EnougMon2 12.EnougMon3
     * 13.ContrDate 14.ContrMon1 15.ContrMon2 16.ContrMon3 17.Deldate 18.PingSu
     * 19.PreMoney1 20.PreMoney2 21.PreMoney3 22.DealMoney1 23.DealMoney2
     * 24.DealMoney3 25.GiftMoney1 26.GiftMoney2 27.GiftMoney3 28.CommMoney1
     * 29.CommMoney2 30.CommMoney3 31.PureMoney1 32.PureMoney2 33.PureMoney3
     * 34.LastMoney1 35. LastMoney2 36.LastMoney3 37.BalaMoney1 38.BalaMoney2
     * 39.BalaMoney3 40.SaleName1 41.SaleName2 42.SaleName3 43.SaleName4
     * 44.SaleName5 45.MediaName 46.ZoneName 47.MajorName 48.UseType 49.Remark
     * 50.DateRange 51.DateCheck 52.DateFile 53.DateBonus 54.H_COM 55.L_COM
     * 56.SaleName6 57.SaleName7 58.SaleName8 59.SaleName9 60.SaleName10
     * 61.CommMoney11 62.CommMoney12 63.CommMoney13
     */
    String[][] retASale = getDeleteForASale1(stringProjectID, stringOrderDateStart, stringOrderDateEnd, stringThisType, stringHouseCar, stringSqlAnd);
    // ���L����ˬd
    if (retASale.length == 0) {
      message("�d�L��ơC");
      return false;
    }
    // ���o Exce ����
    Vector retVector = exeFun.getExcelObject("G:\\��T��\\Excel\\SaleEffect\\" + stringExcelName);
    System.out.println("-------" + "G:\\��T��\\Excel\\SaleEffect\\" + stringExcelName + "------");
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    Dispatch objectSheet2 = (Dispatch) retVector.get(2);
    Dispatch objectClick = null;
    exeFun.setClearCol(0, 39);
    // �@�P�ʦC�L
    // ����ܧ�
    if ("�Фl".equals(stringThisType)) {
      exeFun.putDataIntoExcel(1, 4, "�ɼӧO", objectSheet2);
    } else if ("����".equals(stringThisType)) {
      exeFun.putDataIntoExcel(1, 4, "����O", objectSheet2);
    }
    // ���q�W
    exeFun.putDataIntoExcel(0, 1, getACom(stringDistributeNo), objectSheet2);
    // �קO
    exeFun.putDataIntoExcel(0, 3, "�קO�G" + stringProjectID, objectSheet2);
    // ���
    exeFun.putDataIntoExcel(4, 3, "����G" + getConertFormatDate(stringOrderDateStart) + "~" + getConertFormatDate(stringOrderDateEnd), objectSheet2);
    // ����SQL
    String stringConditionDescription = getConditionDescription();
    String stringConditionDescription2 = getConditionDescription2();
    exeFun.putDataIntoExcel(19, 3, stringConditionDescription + stringConditionDescription2, objectSheet2);
    /*
     * 0.H_Com 1.L_Com 2.OpenDate 3.PositionMoneyS1 4.PositionMoneyS2
     * 5.PositionMoneyS3 6.CarMoneyS1 7.CarMoneyS2 8.CarMoneyS3
     */
    String[][] retAProjectLocal = getDeleteForAProject(stringProjectID, stringHouseCar);
    // retAProjectLocal START
    // ���}��� OpenDate
    exeFun.putDataIntoExcel(30, 3, "���}����G" + getConertFormatDate(convert.replace(retAProjectLocal[0][2].trim(), "-", "").substring(0, 8)), objectSheet2);
    // �i����B
    double double�i����B = 0;
    for (int intNo = 0; intNo < retAProjectLocal.length; intNo++) {
      stringHComLocal = retAProjectLocal[intNo][0].trim();
      stringLComLocal = retAProjectLocal[intNo][1].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      //
      if ("�Фl".equals(stringThisType)) {
        if (booleanHouse) double�i����B += doParseDouble(retAProjectLocal[0][4].trim(), "1-1");// PositionMoneyS2
        if (booleanLand) double�i����B += doParseDouble(retAProjectLocal[0][5].trim(), "1-2");// PositionMoneyS3
      } else if ("����".equals(stringThisType)) {
        if (booleanHouse) double�i����B += doParseDouble(retAProjectLocal[0][7].trim(), "1-4");// CarMoneyS2
        if (booleanLand) double�i����B += doParseDouble(retAProjectLocal[0][8].trim(), "1-5");// CarMoneyS3
      }
    }
    exeFun.putDataIntoExcel(2, 26, "" + double�i����B, objectSheet2);
    // retAProjectLocal END
    // �֭p
    // retASale�֭p START
    /*
     * 0.SumPingSu 1.SumPreMoney 2.SumPreMoney2 3.SumPreMoney3 4.SumDealMoney
     * 5.SumDealMoney2 6.SumDealMoney3 7.SumGiftMoney 8.SumGiftMoney2
     * 9.SumGiftMoney3 10.SumCommMoney 11.SumCommMoney2 12.SumCommMoney3
     * 13.SumPureMoney 14.SumPureMoney2 15.SumPureMoney3 16.SumLastMoney
     * 17.SumLastMoney2 18.SumLastMoney3 19.SumBalaMoney 20.SumBalaMoney2
     * 21.SumBalaMoney3 22. H_COM 23. L_COM 24.SumCommMoney11 25.SumCommMoney12 26
     * SumCommMoney13
     */
    String[][] retASale�֭p = getSumDeleteForASale1(stringHouseCar, stringProjectID, stringOrderDateEnd, stringThisType, getSqlAnd2());
    double double�W�Ʋ֭p = 0;
    double double�q���֭p = 0;
    double double����֭p = 0;
    double double�ذe�֭p = 0;
    double double�����֭p = 0;
    double double��������֭p = 0; // 2015-11-18 B3018
    double double�Q���֭p = 0; // 2017-05-02 B4474
    double double�b��֭p = 0;
    double double�����֭p = 0;
    double double�W�C���֭p = 0;
    for (int intNo = 0; intNo < retASale�֭p.length; intNo++) {
      // �W�Ʋ֭p
      if ("Z".equals(stringProjectID)) { // M^2 Convert �W
        // SumPingSu
        double�W�Ʋ֭p += doParseDouble(retASale�֭p[intNo][0].trim(), "1-7") * 0.3025;
      } else {
        // SumPingSu
        double�W�Ʋ֭p += doParseDouble(retASale�֭p[intNo][0].trim(), "1-8");
      }
      //
      stringHComLocal = retASale�֭p[intNo][22].trim();
      stringLComLocal = retASale�֭p[intNo][23].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      //
      if (booleanHouse) {
        // SumPreMoney2
        double�q���֭p += doParseDouble(retASale�֭p[intNo][2].trim(), "1-9");
        // SumDealMoney2
        double����֭p += doParseDouble(retASale�֭p[intNo][5].trim(), "1-10");
        // SumGiftMoney2
        double�ذe�֭p += doParseDouble(retASale�֭p[intNo][8].trim(), "1-11");
        // SumCommMoney2
        double�����֭p += doParseDouble(retASale�֭p[intNo][11].trim(), "1-12");
        double��������֭p += doParseDouble(retASale�֭p[intNo][25].trim(), "1-12"); // 2015-11-18 B3018
        // SumPureMoney2
        double�b��֭p += doParseDouble(retASale�֭p[intNo][14].trim(), "1-13");
        // SumLastMoney2
        double�����֭p += doParseDouble(retASale�֭p[intNo][17].trim(), "1-14");
        // SumBalaMoney2
        double�W�C���֭p += doParseDouble(retASale�֭p[intNo][20].trim(), "1-15");
        //
        double�Q���֭p += doParseDouble(retASale�֭p[intNo][28].trim(), "�Q��"); // 2017-05-02 B4474
      }
      if (booleanLand) {
        // SumPreMoney3
        double�q���֭p += doParseDouble(retASale�֭p[intNo][3].trim(), "1-16");
        // SumDealMoney3
        double����֭p += doParseDouble(retASale�֭p[intNo][6].trim(), "1-17");
        // SumGiftMoney3
        double�ذe�֭p += doParseDouble(retASale�֭p[intNo][9].trim(), "1-18");
        // SumCommMoney3
        double�����֭p += doParseDouble(retASale�֭p[intNo][12].trim(), "1-19");
        double��������֭p += doParseDouble(retASale�֭p[intNo][26].trim(), "1-19"); // 2015-11-18 B3018
        // SumPureMoney3
        double�b��֭p += doParseDouble(retASale�֭p[intNo][15].trim(), "1-20");
        // SumLastMoney3
        double�����֭p += doParseDouble(retASale�֭p[intNo][18].trim(), "1-21");
        // SumBalaMoney3
        double�W�C���֭p += doParseDouble(retASale�֭p[intNo][21].trim(), "1-22");
        //
        double�Q���֭p += doParseDouble(retASale�֭p[intNo][29].trim(), "�Q��"); // 2017-05-02 B4474
      }
    }
    exeFun.putDataIntoExcel(10, 26, "" + double�W�Ʋ֭p, objectSheet2);
    exeFun.putDataIntoExcel(11, 26, "" + double�q���֭p, objectSheet2);
    exeFun.putDataIntoExcel(12, 26, "" + double����֭p, objectSheet2);
    exeFun.putDataIntoExcel(13, 26, "" + double�ذe�֭p, objectSheet2);
    exeFun.putDataIntoExcel(14, 26, "" + double�����֭p, objectSheet2);
    exeFun.putDataIntoExcel(15, 26, "" + double��������֭p, objectSheet2); // 2015-11-18 B3018
    exeFun.putDataIntoExcel(16, 26, "" + double�Q���֭p, objectSheet2); // 2017-05-02 B4474
    exeFun.putDataIntoExcel(17, 26, "" + double�b��֭p, objectSheet2);
    exeFun.putDataIntoExcel(18, 26, "" + double�����֭p, objectSheet2);
    exeFun.putDataIntoExcel(19, 26, "" + double�W�C���֭p, objectSheet2);
    // retASale�֭p END
    // �h��v
    double double�h��v = 0;
    if (double�i����B != 0) double�h��v = double����֭p / double�i����B;
    exeFun.putDataIntoExcel(35, 26, "" + double�h��v, objectSheet2);
    // ���_�ʦC�L
    double double�W�Ƥp�p = 0;
    double double�q���p�p = 0;
    double double����p�p = 0;
    double double�ذe�p�p = 0;
    double double�����p�p = 0;
    double double��������p�p = 0; // 2015-11-18 B3018
    double double�Q���p�p = 0; // 2017-05-02 B4474
    double double�b��p�p = 0;
    double double�����p�p = 0;
    double double�W�C���p�p = 0;

    double double�W��11 = 0;
    double double�I�q���B5 = 0;
    double double�ɨ����B7 = 0;
    double doubleñ�����B9 = 0;
    double double�q��12 = 0;
    double double���13 = 0;
    double double�ذe14 = 0;
    double double����15 = 0;
    double double������� = 0; // 2015-11-18 B3018
    double double�Q�� = 0; // 2017-05-02 B4474
    double double�b��16 = 0;
    double double����17 = 0;
    double double�W�C��18 = 0;
    int intStartDataRow = exeFun.getStartDataRow();
    int intPageDataRow = exeFun.getPageDataRow();
    int intRecordNo = intStartDataRow;
    for (int intRetASale = 0; intRetASale < retASale.length; intRetASale++) {
      // No
      exeFun.putDataIntoExcel(0, intRecordNo, Integer.toString(intRetASale + 1), objectSheet2);
      //
      if ("�Фl".equals(stringThisType)) {
        if (!"".equals(retASale[intRetASale][0].trim())) { // Position
          exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][0].trim(), objectSheet2);// Position
        } else {
          exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][1].trim(), objectSheet2);// PositionRent
        }
      } else if ("����".equals(stringThisType)) {
        if (!"".equals(retASale[intRetASale][2].trim())) { // Car
          exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][2].trim(), objectSheet2);// Car
        } else {
          exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][3].trim(), objectSheet2);// CarRent
        }
      }
      // �Ȥ�m�W Custom
      exeFun.putDataIntoExcel(2, intRecordNo, retASale[intRetASale][4].trim(), objectSheet2);
      // �I�q��� OrderDate
      exeFun.putDataIntoExcel(3, intRecordNo, retASale[intRetASale][5].trim(), objectSheet2);
      // �ɨ���� EnougDate
      exeFun.putDataIntoExcel(5, intRecordNo, retASale[intRetASale][9].trim(), objectSheet2);
      // ñ����� ContrDate
      exeFun.putDataIntoExcel(7, intRecordNo, retASale[intRetASale][13].trim(), objectSheet2);
      // �h���� Deldate
      exeFun.putDataIntoExcel(9, intRecordNo, retASale[intRetASale][17].trim(), objectSheet2);
      // �W�� PingSu
      double�W��11 = doParseDouble(retASale[intRetASale][18].trim(), "1-30");
      exeFun.putDataIntoExcel(10, intRecordNo, "" + double�W��11, objectSheet2);
      //
      stringHComLocal = retASale[intRetASale][54].trim();
      stringLComLocal = retASale[intRetASale][55].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      //
      double�I�q���B5 = 0;
      double�ɨ����B7 = 0;
      doubleñ�����B9 = 0;
      double�q��12 = 0;
      double���13 = 0;
      double�ذe14 = 0;
      double����15 = 0;
      double������� = 0; // 2015-11-18 B3018
      double�Q�� = 0; // 2017-05-02 B4474
      double�b��16 = 0;
      double����17 = 0;
      double�W�C��18 = 0;
      if (booleanHouse) {
        // �I�q���B5 OrderMon2
        double�I�q���B5 += doParseDouble(retASale[intRetASale][7].trim(), "1-31");
        // �ɨ����B7 EnougMon2
        double�ɨ����B7 += doParseDouble(retASale[intRetASale][11].trim(), "1-32");
        // ñ�����B9 ContrMon2
        doubleñ�����B9 += doParseDouble(retASale[intRetASale][15].trim(), "1-33");
        // PreMoney2
        double�q��12 += doParseDouble(retASale[intRetASale][20].trim(), "1-34");
        // ���13 DealMoney2
        double���13 += doParseDouble(retASale[intRetASale][23].trim(), "1-35");
        // �ذe14 GiftMoney2
        double�ذe14 += doParseDouble(retASale[intRetASale][26].trim(), "1-36");
        // ����15 CommMoney2
        double����15 += doParseDouble(retASale[intRetASale][29].trim(), "1-37");
        double������� += doParseDouble(retASale[intRetASale][62].trim(), "1-37"); // 2015-11-18 B3018
        // �b�� PureMoney2
        double�b��16 += doParseDouble(retASale[intRetASale][32].trim(), "1-38");
        // ���� LastMoney2
        double����17 += doParseDouble(retASale[intRetASale][35].trim(), "1-39");
        // BalaMoney2
        double�W�C��18 += doParseDouble(retASale[intRetASale][38].trim(), "1-40");
        //
        double�Q�� += doParseDouble(retASale[intRetASale][65].trim(), "�Q��"); // 2017-05-02 B4474
      }
      if (booleanLand) {
        // �I�q���B5 OrderMon3
        double�I�q���B5 += doParseDouble(retASale[intRetASale][8].trim(), "1-41");
        // �ɨ����B7 EnougMon3
        double�ɨ����B7 += doParseDouble(retASale[intRetASale][12].trim(), "1-42");
        // ñ�����B9 ContrMon3
        doubleñ�����B9 += doParseDouble(retASale[intRetASale][16].trim(), "1-43");
        // �q�� PreMoney3
        double�q��12 += doParseDouble(retASale[intRetASale][21].trim(), "1-44");
        // ���13 DealMoney3
        double���13 += doParseDouble(retASale[intRetASale][24].trim(), "1-45");
        // �ذe14 GiftMoney3
        double�ذe14 += doParseDouble(retASale[intRetASale][27].trim(), "1-46");
        // ����15 CommMoney3
        double����15 += doParseDouble(retASale[intRetASale][30].trim(), "1-47");
        double������� += doParseDouble(retASale[intRetASale][63].trim(), "1-47"); // 2015-11-18 B3018
        // �b�� PureMoney3
        double�b��16 += doParseDouble(retASale[intRetASale][33].trim(), "1-48");
        // ���� LastMoney3
        double����17 += doParseDouble(retASale[intRetASale][36].trim(), "1-49");
        // BalaMoney3
        double�W�C��18 += doParseDouble(retASale[intRetASale][39].trim(), "1-50");
        //
        double�Q�� += doParseDouble(retASale[intRetASale][66].trim(), "�Q��"); // 2017-05-02 B4474
      }
      exeFun.putDataIntoExcel(4, intRecordNo, "" + double�I�q���B5, objectSheet2);
      exeFun.putDataIntoExcel(6, intRecordNo, "" + double�ɨ����B7, objectSheet2);
      exeFun.putDataIntoExcel(8, intRecordNo, "" + doubleñ�����B9, objectSheet2);
      exeFun.putDataIntoExcel(11, intRecordNo, "" + double�q��12, objectSheet2);
      exeFun.putDataIntoExcel(12, intRecordNo, "" + double���13, objectSheet2);
      exeFun.putDataIntoExcel(13, intRecordNo, "" + double�ذe14, objectSheet2);
      exeFun.putDataIntoExcel(14, intRecordNo, "" + double����15, objectSheet2);
      exeFun.putDataIntoExcel(15, intRecordNo, "" + double�������, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(16, intRecordNo, "" + double�Q��, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(17, intRecordNo, "" + double�b��16, objectSheet2);
      exeFun.putDataIntoExcel(18, intRecordNo, "" + double����17, objectSheet2);
      exeFun.putDataIntoExcel(19, intRecordNo, "" + double�W�C��18, objectSheet2);
      // ��X�H1 SaleName1
      exeFun.putDataIntoExcel(20, intRecordNo, retASale[intRetASale][40].trim(), objectSheet2);
      // ��X�H2 SaleName2
      exeFun.putDataIntoExcel(21, intRecordNo, retASale[intRetASale][41].trim(), objectSheet2);
      // ��X�H3 SaleName3
      exeFun.putDataIntoExcel(22, intRecordNo, retASale[intRetASale][42].trim(), objectSheet2);
      // ��X�H4 SaleName4
      exeFun.putDataIntoExcel(23, intRecordNo, retASale[intRetASale][43].trim(), objectSheet2);
      // ��X�H5 SaleName5
      exeFun.putDataIntoExcel(24, intRecordNo, retASale[intRetASale][44].trim(), objectSheet2);
      exeFun.putDataIntoExcel(25, intRecordNo, retASale[intRetASale][56].trim(), objectSheet2);// ��X�H6 SaleName6
      exeFun.putDataIntoExcel(26, intRecordNo, retASale[intRetASale][57].trim(), objectSheet2);// ��X�H7 SaleName7
      exeFun.putDataIntoExcel(27, intRecordNo, retASale[intRetASale][58].trim(), objectSheet2);// ��X�H8 SaleName8
      exeFun.putDataIntoExcel(28, intRecordNo, retASale[intRetASale][59].trim(), objectSheet2);// ��X�H9 SaleName9
      exeFun.putDataIntoExcel(29, intRecordNo, retASale[intRetASale][60].trim(), objectSheet2);// ��X�H10 SaleName10

      // �C�� MediaName
      exeFun.putDataIntoExcel(30, intRecordNo, retASale[intRetASale][45].trim(), objectSheet2);
      // �ϰ� ZoneName
      exeFun.putDataIntoExcel(31, intRecordNo, retASale[intRetASale][46].trim(), objectSheet2);
      // �~�O MajorName
      exeFun.putDataIntoExcel(32, intRecordNo, retASale[intRetASale][47].trim(), objectSheet2);
      // �γ~ UseType
      exeFun.putDataIntoExcel(33, intRecordNo, retASale[intRetASale][48].trim(), objectSheet2);
      // �Ƶ� Remark
      exeFun.putDataIntoExcel(34, intRecordNo, retASale[intRetASale][49].trim(), objectSheet2);
      // ���� DateRange
      exeFun.putDataIntoExcel(35, intRecordNo, retASale[intRetASale][50].trim(), objectSheet2);
      // �X���|�f DateCheck
      exeFun.putDataIntoExcel(36, intRecordNo, retASale[intRetASale][51].trim(), objectSheet2);
      // �X���k�� DateFile
      exeFun.putDataIntoExcel(37, intRecordNo, retASale[intRetASale][52].trim(), objectSheet2);
      // �����л� DateBonus
      exeFun.putDataIntoExcel(38, intRecordNo, retASale[intRetASale][53].trim(), objectSheet2);
      // �p�p
      double�W�Ƥp�p += double�W��11;
      double�q���p�p += double�q��12;
      double����p�p += double���13;
      double�ذe�p�p += double�ذe14;
      double�����p�p += double����15;
      double��������p�p += double�������; // 2015-11-18 B3018
      double�Q���p�p += double�Q��; // 2017-05-02 B4474
      double�b��p�p += double�b��16;
      double�����p�p += double����17;
      double�W�C���p�p += double�W�C��18;
      // �U�@��
      intRecordNo++;
      // �����ɥ����NSheet2 Copy Sheet1
      // if(intRecordNo == intStartDataRow){
      if (intRecordNo >= (intPageDataRow + intStartDataRow)) {
        // �p�p�C�L
        // if(intRecordNo == retASale.length) {
        exeFun.putDataIntoExcel(10, 25, "" + double�W�Ƥp�p, objectSheet2);
        exeFun.putDataIntoExcel(11, 25, "" + double�q���p�p, objectSheet2);
        exeFun.putDataIntoExcel(12, 25, "" + double����p�p, objectSheet2);
        exeFun.putDataIntoExcel(13, 25, "" + double�ذe�p�p, objectSheet2);
        exeFun.putDataIntoExcel(14, 25, "" + double�����p�p, objectSheet2);
        exeFun.putDataIntoExcel(15, 25, "" + double��������p�p, objectSheet2); // 2015-11-18 B3018
        exeFun.putDataIntoExcel(16, 25, "" + double�Q���p�p, objectSheet2); // 2017-05-02 B4474
        exeFun.putDataIntoExcel(17, 25, "" + double�b��p�p, objectSheet2);
        exeFun.putDataIntoExcel(18, 25, "" + double�����p�p, objectSheet2);
        exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�p, objectSheet2);
        // }
      }
      intRecordNo = exeFun.doChangePage(intRecordNo, objectSheet1, objectSheet2);
    } // For LOOP END
      // �ƻs������
    if (intRecordNo != intStartDataRow) {
      // �p�p
      exeFun.putDataIntoExcel(10, 25, "" + double�W�Ƥp�p, objectSheet2);
      exeFun.putDataIntoExcel(11, 25, "" + double�q���p�p, objectSheet2);
      exeFun.putDataIntoExcel(12, 25, "" + double����p�p, objectSheet2);
      exeFun.putDataIntoExcel(13, 25, "" + double�ذe�p�p, objectSheet2);
      exeFun.putDataIntoExcel(14, 25, "" + double�����p�p, objectSheet2);
      exeFun.putDataIntoExcel(15, 25, "" + double��������p�p, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(16, 25, "" + double�Q���p�p, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(17, 25, "" + double�b��p�p, objectSheet2);
      exeFun.putDataIntoExcel(18, 25, "" + double�����p�p, objectSheet2);
      exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�p, objectSheet2);
      // �ƻs
      exeFun.CopyPage(objectSheet1, objectSheet2);
      exeFun.doClearContents("A" + (intStartDataRow + 1) + ":AL" + (intStartDataRow + intPageDataRow), objectSheet2);
      exeFun.doAdd1PageNo();
    }
    // �����C�L
    // ���� Excel ����
    exeFun.getReleaseExcelObject(retVector);
    return true;
  }

  public boolean doSaleOutDetailRent(String stringExcelName, String stringTableSelect) throws Throwable {
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(6, 20, 34, 1);
    // ��ƳB�z
    String stringSql = "";
    String stringHComLocal = "";
    String stringLComLocal = "";
    String stringProjectID = getValue("ProjectID").trim();
    String stringOrderDateStart = getValue("OrderDateStart").trim();
    String stringOrderDateEnd = getValue("OrderDateEnd").trim();
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    String stringHouseCar = "";
    String stringFunction = getValue("Function").trim();
    String stringThisType = "";
//      String     stringOrderForm         =  getValue("OrderForm").trim( ) ;
//      String     stringOrderBy             =  "" ;
    //
    switch (Integer.parseInt(stringFunction)) {
    case 1:
      stringThisType = "�Фl"; // �Фl����
      break;
    case 4:
      stringThisType = "����"; // �Фl����
      break;
    }

    switch (Integer.parseInt(stringHouseLand)) {
    case 0:
      stringHouseCar = "";
      break;
    case 1:
      stringHouseCar = "H_";
      break;
    case 2:
      stringHouseCar = "L_";
      break;
    }
    // ���o���
    /*
     * 0.PositionRent 1.CarRent 2.Custom 3.OrderDate 4.OrderMon1 5.EnougDate
     * 6.EnougMon1 7.ContrDate 8.ContrMon1 9.Deldate 10.PingSu 11.RentRange
     * 12.RentLast 13.PingRent 14.Rent 15.RentFree 16.Guranteer 17.PreMoney1
     * 18.DealMoney1 19.GiftMoney1 20.CommMoney1 21.PureMoney1 22.LastMoney1
     * 23.BalaMoney1 24.SaleName1 25.SaleName2 26.SaleName3 27.SaleName4
     * 28.SaleName5 29.MediaName 30.ZoneName 31.MajorName 32.UseType 33.Remark
     * 34.DateRange 35.DateCheck 36.DateFile 37.DateBonus 38.SaleName6 39.SaleName7
     * 40.SaleName8 41.SaleName9 42.SaleName10 43 H_Com 44.L_Com 45.DealMoney
     * 46.H_DealMoney 47.L_DealMoney 48 CommMoney1 49 H_LastMoney 50 L_LastMoney 51
     * H_GiftMoney 52 L_GiftMoney 53 H_CommMoney 54 L_CommMoney 55 H_CommMoney1 56
     * L_CommMoney1 57 H_BalaMoney 58 L_BalaMoney
     */
    String[][] retASale = getRentForASale(stringHouseCar, stringProjectID, stringOrderDateStart, stringOrderDateEnd, stringThisType);
    // ���L����ˬd
    if (retASale.length == 0) {
      message("�d�L��ơC");
      return false;
    }
    // ���o Exce ����
    Vector retVector = exeFun.getExcelObject("G:\\��T��\\Excel\\SaleEffect\\" + stringExcelName);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    Dispatch objectSheet2 = (Dispatch) retVector.get(2);
    Dispatch objectClick = null;
    exeFun.setClearCol(0, 39);
    //
    // �@�P�ʦC�L
    // ����ܧ�
    if ("�Фl".equals(stringThisType)) {
      exeFun.putDataIntoExcel(0, 3, "�ɼӥX��", objectSheet2);
    } else if ("����".equals(stringThisType)) {
      exeFun.putDataIntoExcel(0, 3, "����X��", objectSheet2);
    }
    // ���q�W
    exeFun.putDataIntoExcel(0, 1, getACom(stringDistributeNo), objectSheet2);
    // �קO
    exeFun.putDataIntoExcel(0, 3, "�קO�G" + stringProjectID, objectSheet2);

    // ���
    // EMK20210115004 Kyle Mod
    String excelOrderDate = ("-1".equals(getConertFormatDate(stringOrderDateStart)) ? "" : getConertFormatDate(stringOrderDateStart)) + "~"
        + ("-1".equals(getConertFormatDate(stringOrderDateEnd)) ? "" : getConertFormatDate(stringOrderDateEnd));
    exeFun.putDataIntoExcel(5, 3, "�I�q����G" + (excelOrderDate.length() > 1 ? excelOrderDate : "-"), objectSheet2);

    // ����SQL
    String stringConditionDescription = getConditionDescription();
    String stringConditionDescription2 = getConditionDescription2();
    exeFun.putDataIntoExcel(21, 3, stringConditionDescription + stringConditionDescription2, objectSheet2);
    // retAProjectLocal START
    // 0 H_Com 1 L_Com 2 OpenDate 3 PositionMoneyR1 4 CarMoneyR1
    String[][] retAProjectLocal = getRentForAProject(stringProjectID, stringHouseCar);
    stringHComLocal = retAProjectLocal[0][0];
    stringLComLocal = retAProjectLocal[0][1];

    // ���}���
    exeFun.putDataIntoExcel(34, 3, "���}����G" + getConertFormatDate(convert.replace(retAProjectLocal[0][2].trim(), "-", "").substring(0, 8)), objectSheet2);
    // �i����B
    if ("�Фl".equals(stringThisType)) {
      exeFun.putDataIntoExcel(2, 25, retAProjectLocal[0][3], objectSheet2); // PositionMoneyR1
    } else if ("�Фl".equals(stringThisType)) {
      exeFun.putDataIntoExcel(2, 25, retAProjectLocal[0][4], objectSheet2); // CarMoneyR1
    }
    // retAProjectLocal END
    // ���_-----retASale
    double double�W�Ƥp�p = 0;
    double double������p�p = 0;
    double double�����W�p�p = 0;
    double double������p�p = 0;
    double double�`�����p�p = 0;
    double double�ذe�p�p = 0;
    double double�����p�p = 0;
    double double��������p�p = 0; // 2015-11-18 B3018
    double double�Q���p�p = 0; // 2017-05-02 B4474
    double double�`�����p�p = 0;
    double double�W�C���p�p = 0;
    double double�K�����p�p = 0;
    double double�W��9 = 0;
    double double����������11 = 0;
    double double������10 = 0;
    double double�����W12 = 0;
    double double������13 = 0;
    double double�`����14 = 0;
    double double�ذe15 = 0;
    double double����16 = 0;
    double double������� = 0; // 2015-11-18 B3018
    double double�Q�� = 0; // 2017-05-02 B4474
    double double�`����17 = 0;
    double double�W�C��18 = 0;
    double double�K����19 = 0;
    int intStartDataRow = exeFun.getStartDataRow();
    int intPageDataRow = exeFun.getPageDataRow();
    int intRecordNo = intStartDataRow;
    boolean booleanHouse = false;
    boolean booleanLand = false;
    for (int intRetASale = 0; intRetASale < retASale.length; intRetASale++) {
      double�W��9 = 0;
      double������10 = 0;
      double����������11 = 0;
      double�����W12 = 0;
      double������13 = 0;
      double�`����14 = 0;
      double�ذe15 = 0;
      double����16 = 0;
      double������� = 0; // 2015-11-18 B3018
      double�Q�� = 0; // 2017-05-02 B4474
      double�`����17 = 0;
      double�W�C��18 = 0;
      double�K����19 = 0;
      // No
      exeFun.putDataIntoExcel(0, intRecordNo, "" + (intRetASale + 1), objectSheet2);
      //
      if ("�Фl".equals(stringThisType)) {
        exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][0].trim(), objectSheet2);// PositionRent
      } else if ("����".equals(stringThisType)) {
        exeFun.putDataIntoExcel(1, intRecordNo, retASale[intRetASale][1].trim(), objectSheet2);// CarRent
      }
      // �Ȥ�m�W Custom
      exeFun.putDataIntoExcel(2, intRecordNo, retASale[intRetASale][2].trim(), objectSheet2);
      // �I�q��� OrderDate
      exeFun.putDataIntoExcel(3, intRecordNo, retASale[intRetASale][3].trim(), objectSheet2);
      // �I�q���B OrderMon1
      exeFun.putDataIntoExcel(4, intRecordNo, retASale[intRetASale][4].trim(), objectSheet2);
      // ñ����� ContrDate
      exeFun.putDataIntoExcel(5, intRecordNo, retASale[intRetASale][7].trim(), objectSheet2);
      // ñ�����B ContrMon1
      exeFun.putDataIntoExcel(6, intRecordNo, retASale[intRetASale][8].trim(), objectSheet2);
      // �h���� Deldate
      exeFun.putDataIntoExcel(7, intRecordNo, retASale[intRetASale][9].trim(), objectSheet2);
      // �W�� PingSu
      double�W��9 = doParseDouble(retASale[intRetASale][10].trim(), "1");
      exeFun.putDataIntoExcel(8, intRecordNo, "" + double�W��9, objectSheet2);
      // ����_�� RentRange
      double������10 = doParseDouble(retASale[intRetASale][11].trim(), "2");
      exeFun.putDataIntoExcel(9, intRecordNo, "" + double������10, objectSheet2);
      // ��������(��) RentLast (����)
      double����������11 = doParseDouble(retASale[intRetASale][12].trim(), "3");
      exeFun.putDataIntoExcel(10, intRecordNo, "" + double����������11, objectSheet2);
      // ����(�W) PingRent
      double�����W12 = doParseDouble(retASale[intRetASale][13].trim(), "4");
      exeFun.putDataIntoExcel(11, intRecordNo, "" + double�����W12, objectSheet2);
      // ����(��) Rent
      double������13 = doParseDouble(retASale[intRetASale][14].trim(), "5");
      exeFun.putDataIntoExcel(12, intRecordNo, "" + double������13, objectSheet2);
      // �`����
      // double�`����14 = double������10 * double������13 ;
      /*
       * if("0".equals(stringHouseLand)) { // ���� //double�`����14 =
       * doParseDouble(retASale[intRetASale][45].trim( ), "10-1") ; // double�`����14 = 0
       * ; if("".equals(stringDistributeNo) ||
       * stringDistributeNo.equals(retASale[intRetASale][43].trim( )))double�`����14 +=
       * doParseDouble(retASale[intRetASale][46].trim( ), "10-1") ;
       * if("".equals(stringDistributeNo) ||
       * stringDistributeNo.equals(retASale[intRetASale][44].trim( )))double�`����14 +=
       * doParseDouble(retASale[intRetASale][47].trim( ), "10-2") ; } else
       * if("1".equals(stringHouseLand)) { // �Ы� double�`����14 =
       * doParseDouble(retASale[intRetASale][46].trim( ), "10-3") ; //
       * if(!"".equals(stringDistributeNo) &&
       * !stringDistributeNo.equals(retASale[intRetASale][43].trim( )))double�`����14 = 0
       * ; } else if("2".equals(stringHouseLand)) { // �g�a double�`����14 =
       * doParseDouble(retASale[intRetASale][47].trim( ), "10-4") ; //
       * if(!"".equals(stringDistributeNo) &&
       * !stringDistributeNo.equals(retASale[intRetASale][44].trim( )))double�`����14 = 0
       * ; } else { double�`����14 = 0 ; } //
       * 
       * exeFun.putDataIntoExcel(13, intRecordNo, ""+double�`����14, objectSheet2) ;
       */
      // �ذe GiftMoney1
      // double�ذe15 = doParseDouble(retASale[intRetASale][19].trim( ), "6") ;
      // exeFun.putDataIntoExcel(14, intRecordNo, ""+double�ذe15, objectSheet2) ;
      // ���� CommMoney1
      // double����16 = doParseDouble(retASale[intRetASale][20].trim( ), "7") ;
      // exeFun.putDataIntoExcel(15, intRecordNo, ""+double����16, objectSheet2) ;
      // ������� CommMoney1
      // double������� = doParseDouble(retASale[intRetASale][48].trim( ), "7") ; //
      // 2015-11-18 B3018
      // exeFun.putDataIntoExcel(16, intRecordNo, ""+double�������, objectSheet2) ; //
      // 2015-11-18 B3018
      // �`����
      // double�`����17 = double������10 * double����������11 ;
      // if("B3018".equals(getUser())) messagebox(convert.FourToFive(""+double������10,
      // 0)+" * "+convert.FourToFive(""+double����������11, 0)+" =
      // "+convert.FourToFive(""+double�`����17, 0)) ;
      // exeFun.putDataIntoExcel(17, intRecordNo, ""+double�`����17, objectSheet2) ;
      // �W�C�� BalaMoney1
      // double�W�C��18 = doParseDouble(retASale[intRetASale][23].trim( ), "8") ;
      // exeFun.putDataIntoExcel(18, intRecordNo, ""+double�W�C��18, objectSheet2) ;

      /** 2017/08/02 B4474 �ק� �NEXCEL ������@�� EX �K���� 20 �� 19 **/

      // �K���� RentFree
      double�K����19 = doParseDouble(retASale[intRetASale][15].trim(), "9");
      exeFun.putDataIntoExcel(20, intRecordNo, "" + double�K����19, objectSheet2);
      // �O�Ҫ� Guranteer
      exeFun.putDataIntoExcel(21, intRecordNo, retASale[intRetASale][16].trim(), objectSheet2);
      // ��X�H1 SaleName1
      exeFun.putDataIntoExcel(22, intRecordNo, retASale[intRetASale][24].trim(), objectSheet2);
      // ��X�H2 SaleName2
      exeFun.putDataIntoExcel(23, intRecordNo, retASale[intRetASale][25].trim(), objectSheet2);
      // ��X�H3 SaleName3
      exeFun.putDataIntoExcel(24, intRecordNo, retASale[intRetASale][26].trim(), objectSheet2);
      // ��X�H4 SaleName4
      exeFun.putDataIntoExcel(25, intRecordNo, retASale[intRetASale][27].trim(), objectSheet2);
      // ��X�H5 SaleName5
      exeFun.putDataIntoExcel(26, intRecordNo, retASale[intRetASale][28].trim(), objectSheet2);
      exeFun.putDataIntoExcel(27, intRecordNo, retASale[intRetASale][38].trim(), objectSheet2);// ��X�H6 SaleName6
      exeFun.putDataIntoExcel(28, intRecordNo, retASale[intRetASale][39].trim(), objectSheet2);// ��X�H7 SaleName7
      exeFun.putDataIntoExcel(29, intRecordNo, retASale[intRetASale][40].trim(), objectSheet2);// ��X�H8 SaleName8
      exeFun.putDataIntoExcel(30, intRecordNo, retASale[intRetASale][41].trim(), objectSheet2);// ��X�H9 SaleName9
      exeFun.putDataIntoExcel(31, intRecordNo, retASale[intRetASale][42].trim(), objectSheet2);// ��X�H10 SaleName10

      // �C�� MediaName
      exeFun.putDataIntoExcel(32, intRecordNo, retASale[intRetASale][29].trim(), objectSheet2);
      // �ϰ� ZoneName
      exeFun.putDataIntoExcel(33, intRecordNo, retASale[intRetASale][30].trim(), objectSheet2);
      // �~�O MajorName
      exeFun.putDataIntoExcel(34, intRecordNo, retASale[intRetASale][31].trim(), objectSheet2);
      // �γ~ UseType
      exeFun.putDataIntoExcel(35, intRecordNo, retASale[intRetASale][32].trim(), objectSheet2);
      // �Ƶ� Remark
      exeFun.putDataIntoExcel(36, intRecordNo, retASale[intRetASale][33].trim(), objectSheet2);
      // ���� DateRange
      exeFun.putDataIntoExcel(37, intRecordNo, retASale[intRetASale][34].trim(), objectSheet2);
      // �X���|�f DateCheck
      exeFun.putDataIntoExcel(38, intRecordNo, retASale[intRetASale][35].trim(), objectSheet2);
      // �X���k�� DateFile
      exeFun.putDataIntoExcel(39, intRecordNo, retASale[intRetASale][36].trim(), objectSheet2);
      // �����л� DateBonus
      exeFun.putDataIntoExcel(40, intRecordNo, retASale[intRetASale][37].trim(), objectSheet2);
      // �p�p
      stringHComLocal = retASale[intRetASale][43].trim();
      stringLComLocal = retASale[intRetASale][44].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg oce
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      double�`����14 = 0;
      double�ذe15 = 0;
      double����16 = 0;
      double������� = 0;
      double�Q�� = 0;
      double�`����17 = 0;
      double�W�C��18 = 0;
      if (booleanHouse) { // H_Com
        double�`����14 += doParseDouble(retASale[intRetASale][46].trim(), "10-3");
        double�ذe15 += doParseDouble(retASale[intRetASale][51].trim(), "10-3");
        double����16 += doParseDouble(retASale[intRetASale][53].trim(), "10-3");
        double������� += doParseDouble(retASale[intRetASale][55].trim(), "10-3");
        double�Q�� += doParseDouble(retASale[intRetASale][59].trim(), "�Q��");
        double�`����17 += doParseDouble(retASale[intRetASale][49].trim(), "17-1");
        double�W�C��18 += doParseDouble(retASale[intRetASale][57].trim(), "17-1");
      }
      if (booleanLand) { // L_Com
        double�`����14 += doParseDouble(retASale[intRetASale][47].trim(), "10-4");
        double�ذe15 += doParseDouble(retASale[intRetASale][52].trim(), "10-4");
        double����16 += doParseDouble(retASale[intRetASale][54].trim(), "10-4");
        double������� += doParseDouble(retASale[intRetASale][56].trim(), "10-4");
        double�Q�� += doParseDouble(retASale[intRetASale][60].trim(), "�Q��");
        double�`����17 += doParseDouble(retASale[intRetASale][50].trim(), "17-1");
        double�W�C��18 += doParseDouble(retASale[intRetASale][58].trim(), "17-1");
      }
      exeFun.putDataIntoExcel(13, intRecordNo, "" + double�`����14, objectSheet2);
      exeFun.putDataIntoExcel(14, intRecordNo, "" + double�ذe15, objectSheet2);
      exeFun.putDataIntoExcel(15, intRecordNo, "" + double����16, objectSheet2);
      exeFun.putDataIntoExcel(16, intRecordNo, "" + double�������, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(17, intRecordNo, "" + double�Q��, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(18, intRecordNo, "" + double�`����17, objectSheet2);
      exeFun.putDataIntoExcel(19, intRecordNo, "" + double�W�C��18, objectSheet2);

      //
      double�W�Ƥp�p += double�W��9;
      double������p�p += double������10;
      double�����W�p�p += double�����W12;
      double������p�p += double������13;
      double�`�����p�p += double�`����14;
      double�ذe�p�p += double�ذe15;
      double�����p�p += double����16;
      double��������p�p += double�������; // 2015-11-18 B3018
      double�Q���p�p += double�Q��; // 2017-05-02 B4474
      double�`�����p�p += double�`����17;
      double�W�C���p�p += double�W�C��18;
      double�K�����p�p += double�K����19;
      intRecordNo++;
      // �����ɥ����NSheet2 Copy Sheet1
      if (intRecordNo >= (intPageDataRow + intStartDataRow)) {
        // �p�p�C�L
        // if(intRetASale == retASale.length) {
        exeFun.putDataIntoExcel(8, 25, "" + double�W�Ƥp�p, objectSheet2);
        exeFun.putDataIntoExcel(9, 25, "" + double������p�p, objectSheet2);
        exeFun.putDataIntoExcel(11, 25, "" + double�����W�p�p, objectSheet2);
        exeFun.putDataIntoExcel(12, 25, "" + double������p�p, objectSheet2);
        exeFun.putDataIntoExcel(13, 25, "" + double�`�����p�p, objectSheet2);
        exeFun.putDataIntoExcel(14, 25, "" + double�ذe�p�p, objectSheet2);
        exeFun.putDataIntoExcel(15, 25, "" + double�����p�p, objectSheet2);
        exeFun.putDataIntoExcel(16, 25, "" + double��������p�p, objectSheet2); // 2015-11-18 B3018
        exeFun.putDataIntoExcel(17, 25, "" + double�Q���p�p, objectSheet2); // 2017-05-02 B4474
        exeFun.putDataIntoExcel(18, 25, "" + double�`�����p�p, objectSheet2);
        exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�p, objectSheet2);
        exeFun.putDataIntoExcel(20, 25, "" + double�K�����p�p, objectSheet2);
        // }
      }
      intRecordNo = exeFun.doChangePage(intRecordNo, objectSheet1, objectSheet2);
      /*
       * if(intRecordNo == intStartDataRow){ //�p�p�C�L if(intRetASale == retASale.length)
       * { exeFun.putDataIntoExcel(8, 25, ""+double�W�Ƥp�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(9, 25, ""+double������p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(11, 25, ""+double�����W�p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(12, 25, ""+double������p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(13, 25, ""+double�`�����p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(14, 25, ""+double�ذe�p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(15, 25, ""+double�����p�p, objectSheet2) ;
       * exeFun.putDataIntoExcel(16, 25, ""+double��������p�p, objectSheet2) ; //
       * 2015-11-18 B3018 exeFun.putDataIntoExcel(17, 25, ""+double�`�����p�p,
       * objectSheet2) ; exeFun.putDataIntoExcel(18, 25, ""+double�W�C���p�p, objectSheet2)
       * ; exeFun.putDataIntoExcel(19, 25, ""+double�K�����p�p, objectSheet2) ; } }
       */
    } // For LOOP END
      // �ƻs������
    if (intRecordNo != intStartDataRow) {
      // �p�p
      exeFun.putDataIntoExcel(8, 25, "" + double�W�Ƥp�p, objectSheet2);
      exeFun.putDataIntoExcel(9, 25, "" + double������p�p, objectSheet2);
      exeFun.putDataIntoExcel(11, 25, "" + double�����W�p�p, objectSheet2);
      exeFun.putDataIntoExcel(12, 25, "" + double������p�p, objectSheet2);
      exeFun.putDataIntoExcel(13, 25, "" + double�`�����p�p, objectSheet2);
      exeFun.putDataIntoExcel(14, 25, "" + double�ذe�p�p, objectSheet2);
      exeFun.putDataIntoExcel(15, 25, "" + double�����p�p, objectSheet2);
      exeFun.putDataIntoExcel(16, 25, "" + double��������p�p, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(17, 25, "" + double�Q���p�p, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(18, 25, "" + double�`�����p�p, objectSheet2);
      exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�p, objectSheet2);
      exeFun.putDataIntoExcel(20, 25, "" + double�K�����p�p, objectSheet2);
      // �ƻs
      exeFun.CopyPage(objectSheet1, objectSheet2);
      exeFun.doClearContents("A" + (intStartDataRow + 1) + ":AN" + (intStartDataRow + intPageDataRow), objectSheet2);
      exeFun.doAdd1PageNo();
    }
    // �����C�L
    // ���� Excel ����
    exeFun.getReleaseExcelObject(retVector);
    return true;
  }

  public boolean doSaleOutDetailSale(String stringExcelName, String stringTableSelect) throws Throwable {
//    System.out.println("doSaleOutDetailSale>>>");
    Farglory.Excel.FargloryExcel exeFun = new Farglory.Excel.FargloryExcel(6, 20, 28, 1);
    //
    String stringTmp = "";
    String stringTmp2 = "";
    String stringSql = "";
    String stringOrderDateStart = getValue("OrderDateStart").trim();
    String stringOrderDateEnd = getValue("OrderDateEnd").trim();
    String stringProjectID = getValue("ProjectID").trim();
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    String stringHouseCar = "";
    String stringFunction = getValue("Function").trim();
    String stringThisType = "";
    String stringOrderForm = getValue("OrderForm").trim();
    String stringOrderBy = "";
    String stringLComLocal = "";
    String stringHComLocal = "";
    String[][] retASale2 = null;
    double double���릨���� = 0;
    boolean booleanHouse = true;
    boolean booleanLand = true;
    // retAProjectLocal START
    String[][] retAProjectLocal = getSaleForAProject(stringProjectID);
    stringHComLocal = retAProjectLocal[0][0];
    stringLComLocal = retAProjectLocal[0][1];
    // retAProjectLocal END

    switch (Integer.parseInt(stringFunction)) {
    case 0:
      stringThisType = "�Фl"; // �Фl����
      stringOrderBy = "Position"; // �Ƨ�: �̴ɼӧO(�w�])
      break;
    case 3:
      stringThisType = "����"; // �Фl����
      stringOrderBy = "Car"; // �Ƨ�: �̴ɼӧO(�w�])
      break;
    }
    if ("2".equals(stringOrderForm)) {
      stringOrderBy = "ContrDate"; // �Ƨ�: ��ñ��
    } else if ("1".equals(stringOrderForm)) {
      stringOrderBy = "OrderDate"; // �Ƨ�: �̪��w��
    }
//    System.out.println("stringOrderBy>>>" + stringOrderBy);
//    System.out.println("stringThisType>>>" + stringThisType);

    switch (Integer.parseInt(stringHouseLand)) {
    case 0:
      stringHouseCar = "";
      break;
    case 1:
      stringHouseCar = "H_";
      break;
    case 2:
      stringHouseCar = "L_";
      break;
    }

    // �D��
    /*
     * OrderMon2 0 _ H_Com 1 _ L_Com 2 _ Position 3 _ Car 4 _ Custom 5 _ OrderDate 6
     * _ OrderMon1 7 _ OrderMon2 8 _ OrderMon3 9 _EnougDate 10 _ EnougMon1 11 _
     * EnougMon2 12 _ EnougMon3 13 _ ContrDate 14 _ ContrMon1 15 _ ContrMon2 16 _
     * ContrMon3 16 _ Deldate 18 _ PingSu 19 _ PreMoney1 20 _ PreMoney2 21 _
     * PreMoney3 22 _ DealMoney1 23 _ DealMoney2 24 _ DealMoney3 25 _ GiftMoney1 26
     * _ GiftMoney2 27 _ GiftMoney3 28 _ CommMoney1 29 _ CommMoney2 30 _ CommMoney3
     * 31 _ PureMoney1 32 _ PureMoney2 33 _ PureMoney3 34 _ LastMoney1 35 _
     * LastMoney2 36 _ LastMoney3 37 _ BalaMoney1 38 _ BalaMoney2 39 _ BalaMoney3 40
     * _ SaleName1 41 _ SaleName2 42 _ SaleName3 43 _ SaleName4 44 _ SaleName5 45 _
     * MediaName 46 _ ZoneName 47 _ MajorName 48 _ UseType 49 _ Remark 50 _
     * DateRange 51 _ DateCheck 52 _ DateFile 53 _ DateBonus 54 _ SaleName6 55 _
     * SaleName7 56 _ SaleName8 57 _ SaleName9 58 _ SaleName10 59 _ CommMoney1 60 _
     * CommMoney2 61 _ CommMoney1 62 ViMoney 63 64
     */
    String[][] retASale = getSaleForASale(stringHouseCar, stringProjectID, stringOrderDateStart, stringOrderDateEnd, stringThisType, stringOrderBy, getSqlAnd(), getSqlAnd2());
    if (retASale.length == 0) {
      message("�d�L��ơC");
      return false;
    }
    // ���o Excel ����
    Vector retVector = exeFun.getExcelObject("G:\\��T��\\Excel\\SaleEffect\\" + stringExcelName);
    Dispatch objectSheet1 = (Dispatch) retVector.get(1);
    Dispatch objectSheet2 = (Dispatch) retVector.get(2);
    Dispatch objectClick = null;
    exeFun.setClearCol(0, 37);
    // ����
    // ������W��
    if ("�Фl".equals(stringThisType)) {
      stringTmp2 = "�ɼӧO";
    } else if ("����".equals(stringThisType)) {
      stringTmp2 = "����O";
    }
    exeFun.putDataIntoExcel(1, 4, stringTmp2, objectSheet2);
    // ���q�W
    exeFun.putDataIntoExcel(0, 1, getACom(stringDistributeNo), objectSheet2);
    // �קO
    exeFun.putDataIntoExcel(0, 3, "�קO�G" + stringProjectID, objectSheet2);

    // ���
    // ENK20210115004 Kyle Mod
    String excelOrderDate = ("-1".equals(getConertFormatDate(stringOrderDateStart)) ? "" : getConertFormatDate(stringOrderDateStart)) + "~"
        + ("-1".equals(getConertFormatDate(stringOrderDateEnd)) ? "" : getConertFormatDate(stringOrderDateEnd));
    exeFun.putDataIntoExcel(4, 3, "�I�q����G" + (excelOrderDate.length() > 1 ? excelOrderDate : "-"), objectSheet2);

    // ����SQL
    String stringConditionDescription = getConditionDescription();
    String stringConditionDescription2 = getConditionDescription2();
    exeFun.putDataIntoExcel(19, 3, stringConditionDescription + stringConditionDescription2, objectSheet2);
    // ���}���
    exeFun.putDataIntoExcel(30, 3, "���}����G" + getConertFormatDate(convert.replace(retAProjectLocal[0][2].trim(), "-", "").substring(0, 8)), objectSheet2);
    // �֭p
    // �֭p���
    // String[][] retTotRecForASale = getTotRecForASale(stringProjectID,
    // getSqlAnd(), getSqlAnd2(), stringThisType) ;
    // exeFun.putDataIntoExcel(2, 26, retTotRecForASale[0][0].trim(), objectSheet2)
    // ;//MajorName
    // �i����B
    // retAProject START
    // 0 H_Com 1 L_Com
    // 2. PositionMoneyS1 3. PositionMoneyS2 4. PositionMoneyS3
    // 5. CarMoneyS1 6. CarMoneyS2 7. CarMoneyS3
    // 8. OpenDate
    String[][] retAProject = getSaleForAProject(stringProjectID, stringHouseCar);
    double double�i����B3 = 0;
    for (int intNo = 0; intNo < retAProject.length; intNo++) {
      booleanHouse = false;
      booleanLand = false;
      stringHComLocal = retAProject[intNo][0].trim();
      stringLComLocal = retAProject[intNo][1].trim();
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      if ("�Фl".equals(stringThisType)) {
        if (booleanHouse) double�i����B3 += doParseDouble(retAProject[intNo][3].trim(), "2"); // PositionMoneyS2
        if (booleanLand) double�i����B3 += doParseDouble(retAProject[intNo][4].trim(), "3"); // PositionMoneyS3
      } else if ("����".equals(stringThisType)) {
        if (booleanHouse) double�i����B3 += doParseDouble(retAProject[intNo][6].trim(), "5"); // CarMoneyS2
        if (booleanLand) double�i����B3 += doParseDouble(retAProject[intNo][7].trim(), "6"); // CarMoneyS3
      }
    }
    exeFun.putDataIntoExcel(2, 25, "" + double�i����B3, objectSheet2);// MajorName
    // retAProject END
    // �֭p
    double double�W�Ʋ֭p11 = 0;
    double double�q���֭p12 = 0;
    double double����֭p13 = 0;
    double double�ذe�֭p14 = 0;
    double double�����֭p15 = 0;
    double double��������֭p = 0; // 2015-11-18 B3018
    double double�Q���֭p = 0; // 2017-05-02 B4474
    double double�b��֭p16 = 0;
    double double�����֭p17 = 0;
    double double�W�C���֭p18 = 0;
    /*
     * 0 H_Com 1 L_Com 2. SumPingSu 3. SumPreMoney 4. SumPreMoney2 5. SumPreMoney3
     * 6. SumDealMoney 7. SumDealMoney2 8. SumDealMoney3 9. SumGiftMoney 10.
     * SumGiftMoney2 11. SumGiftMoney3 12.SumCommMoney 13.SumCommMoney2 14.
     * SumCommMoney3 15. SumPureMoney 16. SumPureMoney2 17. SumPureMoney3 18.
     * SumLastMoney 19. SumLastMoney2 20. SumLastMoney3 21.SumBalaMoney 22.
     * SumBalaMoney2 23. SumBalaMoney3
     */
    // System.out.println("-----------------------------------------�֭p") ;
    retASale2 = getSumSaleForASale(stringHouseCar, stringProjectID, stringOrderDateEnd, stringThisType, getSqlAnd2());
    //
    double�W�Ʋ֭p11 = 0;
    double�q���֭p12 = 0;
    double����֭p13 = 0;
    double�ذe�֭p14 = 0;
    double�����֭p15 = 0;
    double��������֭p = 0; // 2015-11-18 B3018
    double�Q���֭p = 0; // 2017-05-02 B4474
    double�b��֭p16 = 0;
    double�����֭p17 = 0;
    double�W�C���֭p18 = 0;
    for (int intNo = 0; intNo < retASale2.length; intNo++) {
      if ("Z".equals(stringProjectID.trim())) {// ** M^2 Convert �W
        double�W�Ʋ֭p11 += doParseDouble(retASale2[intNo][2].trim(), "8") * 0.3025; // SumPingSu
      } else {
        double�W�Ʋ֭p11 += doParseDouble(retASale2[intNo][2].trim(), "9");
      }
      booleanHouse = false;
      booleanLand = false;
      stringHComLocal = retASale2[intNo][0].trim();
      stringLComLocal = retASale2[intNo][1].trim();
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      if (booleanHouse) {
        double�q���֭p12 += doParseDouble(retASale2[intNo][4].trim(), "10"); // SumPreMoney2
        double����֭p13 += doParseDouble(retASale2[intNo][7].trim(), "11");// SumDealMoney2
        double�ذe�֭p14 += doParseDouble(retASale2[intNo][10].trim(), "12");// SumGiftMoney2
        double�����֭p15 += doParseDouble(retASale2[intNo][13].trim(), "13");// SumCommMoney2
        double��������֭p += doParseDouble(retASale2[intNo][25].trim(), "13");// SumCommMoney12 // 2015-11-18 B3018
        double�Q���֭p += doParseDouble(retASale2[intNo][28].trim(), "13");// SumCommMoney12 // 2017-05-02 B4474
        double�b��֭p16 += doParseDouble(retASale2[intNo][16].trim(), "14");// SumPureMoney2
        double�����֭p17 += doParseDouble(retASale2[intNo][19].trim(), "15");// SumLastMoney2
        double�W�C���֭p18 += doParseDouble(retASale2[intNo][22].trim(), "16");// SumBalaMoney2
      }
      if (booleanLand) {
        double�q���֭p12 += doParseDouble(retASale2[intNo][5].trim(), "17"); // .SumPreMoney3
        double����֭p13 += doParseDouble(retASale2[intNo][8].trim(), "18"); // SumDealMoney3
        double�ذe�֭p14 += doParseDouble(retASale2[intNo][11].trim(), "19"); // SumGiftMoney3
        double�����֭p15 += doParseDouble(retASale2[intNo][14].trim(), "20"); // SumCommMoney3
        double��������֭p += doParseDouble(retASale2[intNo][26].trim(), "20"); // SumCommMoney13 // 2015-11-18 B3018
        double�Q���֭p += doParseDouble(retASale2[intNo][29].trim(), "20"); // SumCommMoney13 // 2017-05-02 B4474
        double�b��֭p16 += doParseDouble(retASale2[intNo][17].trim(), "21"); // SumPureMoney3
        double�����֭p17 += doParseDouble(retASale2[intNo][20].trim(), "22"); // SumLastMoney3
        double�W�C���֭p18 += doParseDouble(retASale2[intNo][23].trim(), "23"); // SumBalaMoney3
      }
    }
    exeFun.putDataIntoExcel(10, 26, "" + double�W�Ʋ֭p11, objectSheet2);
    // (intPageNoGlobal-2)*intPageAllRowGlobal+26
    exeFun.putDataIntoExcel(11, 26, "" + double�q���֭p12, objectSheet2);
    exeFun.putDataIntoExcel(12, 26, "" + double����֭p13, objectSheet2);//
    exeFun.putDataIntoExcel(13, 26, "" + double�ذe�֭p14, objectSheet2);
    exeFun.putDataIntoExcel(14, 26, "" + double�����֭p15, objectSheet2);
    exeFun.putDataIntoExcel(15, 26, "" + double��������֭p, objectSheet2); // 2015-11-18 B3018
    exeFun.putDataIntoExcel(16, 26, "" + double�Q���֭p, objectSheet2); // 2017-05-02 B4474
    exeFun.putDataIntoExcel(17, 26, "" + double�b��֭p16, objectSheet2);
    exeFun.putDataIntoExcel(18, 26, "" + double�����֭p17, objectSheet2);
    exeFun.putDataIntoExcel(19, 26, "" + double�W�C���֭p18, objectSheet2);
    // '** �֭p������
    // retSumDealMoneyForASale START
    // 0 H_COM 1 L_COM
    // 2 SumDealMoney 3 SumDealMoney2 4 SumDealMoney3 5 SumPingSu
    String[][] retSumDealMoneyForASale = getSumDealMoneyForASale(stringHouseCar, stringProjectID, stringOrderDateEnd, getSqlAnd2(), stringThisType);
    double double�֭p������ = 0; // Cells(25, 31)
    double double�W�� = 0;
    for (int intNo = 0; intNo < retSumDealMoneyForASale.length; intNo++) {
      // SumPingSu
      double�W�� += doParseDouble(retSumDealMoneyForASale[intNo][5].trim(), "42");
      stringHComLocal = retSumDealMoneyForASale[intNo][0].trim();
      stringLComLocal = retSumDealMoneyForASale[intNo][1].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      if (booleanHouse) double�֭p������ += doParseDouble(retSumDealMoneyForASale[intNo][3], "43"); // SumDealMoney2
      if (booleanLand) double�֭p������ += doParseDouble(retSumDealMoneyForASale[intNo][4], "44"); // SumDealMoney3
    }
    if (double�W�� == 0) {
      exeFun.putDataIntoExcel(31, 26, "0", objectSheet2);// ���
    } else {
      if ("Z".equals(stringProjectID)) { // ** M^2 Convert �W
        double�֭p������ = double�֭p������ / (double�W�� * 0.3025);
      } else {
        double�֭p������ = double�֭p������ / double�W��;
      }
      exeFun.putDataIntoExcel(36, 26, "" + double�֭p������, objectSheet2);// ���
    }
    // retSumDealMoneyForASale END
    //
    int intStartDataRow = exeFun.getStartDataRow();
    int intPageDataRow = exeFun.getPageDataRow();
    int intRecordNo = intStartDataRow;
    for (int intRetASale = 0; intRetASale < retASale.length; intRetASale++) {
      // No
      exeFun.putDataIntoExcel(0, intRecordNo, "" + (intRetASale + 1), objectSheet2);
      // �ɼӧO or ����O
      if ("�Фl".equals(stringThisType)) {
        stringTmp = retASale[intRetASale][2]; // Position
      } else if ("����".equals(stringThisType)) {
        stringTmp = retASale[intRetASale][3]; // Car
      }
      exeFun.putDataIntoExcel(1, intRecordNo, stringTmp, objectSheet2);
      // Custom�Ȥ�m�W
      exeFun.putDataIntoExcel(2, intRecordNo, retASale[intRetASale][4].trim(), objectSheet2);
      // OrderDate �I�q���
      exeFun.putDataIntoExcel(3, intRecordNo, retASale[intRetASale][5].trim(), objectSheet2);
      // EnougDate �ɨ����
      exeFun.putDataIntoExcel(5, intRecordNo, retASale[intRetASale][9].trim(), objectSheet2);
      // ContrDateñ�����
      exeFun.putDataIntoExcel(7, intRecordNo, retASale[intRetASale][13].trim(), objectSheet2);
      // Deldate
      exeFun.putDataIntoExcel(9, intRecordNo, retASale[intRetASale][17].trim(), objectSheet2);
      //
      stringHComLocal = retASale[intRetASale][0].trim();
      stringLComLocal = retASale[intRetASale][1].trim();
      booleanHouse = false;
      booleanLand = false;
      if ("".equals(stringHouseCar)) {
        // �Фg
        if (!"".equals(stringDistributeNo)) {
          if (stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
          if (stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
        } else {
          booleanHouse = true; // �Фl�֥[
          booleanLand = true; // �g�a�֥[
        }
      } else {
        // ��
        if ("H_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringHComLocal.equals(stringDistributeNo)) booleanHouse = true; // �Фl�֥[
        // �g
        if ("L_".equals(stringHouseCar)) if ("".equals(stringDistributeNo) || stringLComLocal.equals(stringDistributeNo)) booleanLand = true; // �g�a�֥[
      }
      //
      double double�I�q���B5 = 0;
      double double�ɨ����B7 = 0;
      double doubleñ�����B9 = 0;
      double double�W��11 = 0;
      double double�q��12 = 0;
      double double���13 = 0;
      double double�ذe14 = 0;
      double double����15 = 0;
      double double�b��16 = 0;
      double double����17 = 0;
      double double�W�C��18 = 0;
      double double������� = 0; // 2015-11-18 B3018
      double double�Q�� = 0; // 2017-05-02 B4474
      // PingSu
      if ("Z".equals(stringProjectID)) { // '** M^2 Convert �W
        double�W��11 = doParseDouble(retASale[intRetASale][18], "49") * 0.3025;// PingSu
      } else {
        double�W��11 = doParseDouble(retASale[intRetASale][18], "50");
      }
      exeFun.putDataIntoExcel(10, intRecordNo, "" + double�W��11, objectSheet2);
      //
      if (booleanHouse) { // H_Com
        double�I�q���B5 += doParseDouble(retASale[intRetASale][7], "51"); // OrderMon2
        double�ɨ����B7 += doParseDouble(retASale[intRetASale][11], "52");// EnougMon2
        doubleñ�����B9 += doParseDouble(retASale[intRetASale][15], "53"); // ContrMon2
        double�q��12 += doParseDouble(retASale[intRetASale][20], "54");// PreMoney2
        double���13 += doParseDouble(retASale[intRetASale][23], "55");// DealMoney2
        double�ذe14 += doParseDouble(retASale[intRetASale][26], "56");// GiftMoney2
        double����15 += doParseDouble(retASale[intRetASale][29], "57");// CommMoney2
        double�b��16 += doParseDouble(retASale[intRetASale][32], "58");// PureMoney2
        double����17 += doParseDouble(retASale[intRetASale][35], "59");// LastMoney2
        double�W�C��18 += doParseDouble(retASale[intRetASale][38], "60");// BalaMoney2
        double������� += doParseDouble(retASale[intRetASale][60], "71");// CommMoney12 // 2015-11-18 B3018
        double�Q�� += doParseDouble(retASale[intRetASale][63], "71");// CommMoney12 // 2017-05-02 B4474
      }
      if (booleanLand) { // L_Com
        double�I�q���B5 += doParseDouble(retASale[intRetASale][8], "61"); // OrderMon3
        double�ɨ����B7 += doParseDouble(retASale[intRetASale][12], "62");// EnougMon3
        doubleñ�����B9 += doParseDouble(retASale[intRetASale][16], "63");// ContrMon3
        double�q��12 += doParseDouble(retASale[intRetASale][21], "64");// PreMoney3
        double���13 += doParseDouble(retASale[intRetASale][24], "65");// DealMoney3
        double�ذe14 += doParseDouble(retASale[intRetASale][27], "66");// GiftMoney3
        double����15 += doParseDouble(retASale[intRetASale][30], "67");// CommMoney3
        double�b��16 += doParseDouble(retASale[intRetASale][33], "68");// PureMoney3
        double����17 += doParseDouble(retASale[intRetASale][36], "69");// LastMoney3
        double�W�C��18 += doParseDouble(retASale[intRetASale][39], "70");// BalaMoney3
        double������� += doParseDouble(retASale[intRetASale][61], "72");// CommMoney13 // 2015-11-18 B3018
        double�Q�� += doParseDouble(retASale[intRetASale][64], "72");// CommMoney13 // 2017-05-02 B4474
      }
      exeFun.putDataIntoExcel(4, intRecordNo, "" + double�I�q���B5, objectSheet2);
      exeFun.putDataIntoExcel(6, intRecordNo, "" + double�ɨ����B7, objectSheet2);
      exeFun.putDataIntoExcel(8, intRecordNo, "" + doubleñ�����B9, objectSheet2);
      exeFun.putDataIntoExcel(11, intRecordNo, "" + double�q��12, objectSheet2);
      exeFun.putDataIntoExcel(12, intRecordNo, "" + double���13, objectSheet2);
      exeFun.putDataIntoExcel(13, intRecordNo, "" + double�ذe14, objectSheet2);
      exeFun.putDataIntoExcel(14, intRecordNo, "" + double����15, objectSheet2);
      exeFun.putDataIntoExcel(15, intRecordNo, "" + double�������, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(16, intRecordNo, "" + double�Q��, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(17, intRecordNo, "" + double�b��16, objectSheet2);
      exeFun.putDataIntoExcel(18, intRecordNo, "" + double����17, objectSheet2);
      exeFun.putDataIntoExcel(19, intRecordNo, "" + double�W�C��18, objectSheet2);
      //
      exeFun.putDataIntoExcel(20, intRecordNo, retASale[intRetASale][40].trim() + (intRetASale + 1), objectSheet2);// SaleName1
      exeFun.putDataIntoExcel(21, intRecordNo, retASale[intRetASale][41].trim(), objectSheet2);// SaleName2
      exeFun.putDataIntoExcel(22, intRecordNo, retASale[intRetASale][42].trim(), objectSheet2);// SaleName3
      exeFun.putDataIntoExcel(23, intRecordNo, retASale[intRetASale][43].trim(), objectSheet2);// SaleName4
      exeFun.putDataIntoExcel(24, intRecordNo, retASale[intRetASale][44].trim(), objectSheet2);// SaleName5
      exeFun.putDataIntoExcel(25, intRecordNo, retASale[intRetASale][54].trim(), objectSheet2);// SaleName6
      exeFun.putDataIntoExcel(26, intRecordNo, retASale[intRetASale][55].trim(), objectSheet2);// SaleName7
      exeFun.putDataIntoExcel(27, intRecordNo, retASale[intRetASale][56].trim(), objectSheet2);// SaleName8

      exeFun.putDataIntoExcel(28, intRecordNo, retASale[intRetASale][57].trim(), objectSheet2);// SaleName9
      exeFun.putDataIntoExcel(29, intRecordNo, retASale[intRetASale][58].trim(), objectSheet2);// SaleName10
      exeFun.putDataIntoExcel(30, intRecordNo, retASale[intRetASale][45].trim(), objectSheet2);// MediaName
      exeFun.putDataIntoExcel(31, intRecordNo, retASale[intRetASale][46].trim(), objectSheet2);// ZoneName
      exeFun.putDataIntoExcel(32, intRecordNo, retASale[intRetASale][47].trim(), objectSheet2);// MajorName
      exeFun.putDataIntoExcel(33, intRecordNo, retASale[intRetASale][48].trim(), objectSheet2);// UseType
      exeFun.putDataIntoExcel(34, intRecordNo, retASale[intRetASale][49].trim(), objectSheet2);// Remark
      exeFun.putDataIntoExcel(35, intRecordNo, retASale[intRetASale][50].trim(), objectSheet2);// DateRange
      exeFun.putDataIntoExcel(36, intRecordNo, retASale[intRetASale][51].trim(), objectSheet2);// DateCheck
      exeFun.putDataIntoExcel(37, intRecordNo, retASale[intRetASale][52].trim(), objectSheet2);// DateFile
      exeFun.putDataIntoExcel(38, intRecordNo, retASale[intRetASale][53].trim(), objectSheet2);// DateBonus

      // �p�p
      if (!"".equals(retASale[intRetASale][18].trim())) { // PingSu
        double�W�Ƥp�p11Global += double�W��11;
      }
      // PreMoney1
      if (!"".equals(retASale[intRetASale][19].trim())) {
        double�q���p�pGlobal += double�q��12;
      }
      // DealMoney1
      if (!"".equals(retASale[intRetASale][22].trim())) {
        double����p�pGlobal += double���13;
      }
      // GiftMoney1
      if (!"".equals(retASale[intRetASale][25].trim())) {
        double�ذe�p�pGlobal += double�ذe14;
      }
      // CommMoney1
      if (!"".equals(retASale[intRetASale][28].trim())) {
        double�����p�pGlobal += double����15;
      }
      if (!"".equals(retASale[intRetASale][29].trim())) {
        double��������p�pGlobal += double�������; // 2015-11-18 B3018
      }
      if (!"".equals(retASale[intRetASale][64].trim())) {
        double�Q���p�pGlobal += double�Q��; // 2017-05-02 B4474
      }
      // PureMoney1
      if (!"".equals(retASale[intRetASale][31].trim())) {
        double�b��p�pGlobal += double�b��16;
      }
      // LastMoney1
      if (!"".equals(retASale[intRetASale][34].trim())) {
        double�����p�pGlobal += double����17;
      }
      // BalaMoney1
      if (!"".equals(retASale[intRetASale][37].trim())) {
        double�W�C���p�pGlobal += double�W�C��18;
      }
      intRecordNo++;
      // �����ɥ����NSheet2 Copy Sheet1
      if (intRecordNo >= (intPageDataRow + intStartDataRow)) {
        // ���릨����
        if (double�W�Ƥp�p11Global == 0) {
          double���릨���� = 0;
        } else {
          double���릨���� = double����p�pGlobal / double�W�Ƥp�p11Global;
        }
        exeFun.putDataIntoExcel(35, 25, "" + double���릨����, objectSheet2);
        exeFun.putDataIntoExcel(10, 25, "" + double�W�Ƥp�p11Global, objectSheet2);
        exeFun.putDataIntoExcel(11, 25, "" + double�q���p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(12, 25, "" + double����p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(13, 25, "" + double�ذe�p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(14, 25, "" + double�����p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(15, 25, "" + double��������p�pGlobal, objectSheet2); // 2015-11-18 B3018
        exeFun.putDataIntoExcel(16, 25, "" + double�Q���p�pGlobal, objectSheet2); // 2017-05-02 B4474
        exeFun.putDataIntoExcel(17, 25, "" + double�b��p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(18, 25, "" + double�����p�pGlobal, objectSheet2);
        exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�pGlobal, objectSheet2);
      }
      intRecordNo = exeFun.doChangePage(intRecordNo, objectSheet1, objectSheet2);
    } // For LOOP END
      // �ƻs������
    if (intRecordNo != intStartDataRow) {
      // ���릨����
      if (double�W�Ƥp�p11Global == 0) {
        double���릨���� = 0;
      } else {
        double���릨���� = double����p�pGlobal / double�W�Ƥp�p11Global;
      }
      exeFun.putDataIntoExcel(35, 25, "" + double���릨����, objectSheet2);
      exeFun.putDataIntoExcel(10, 25, "" + double�W�Ƥp�p11Global, objectSheet2);
      exeFun.putDataIntoExcel(11, 25, "" + double�q���p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(12, 25, "" + double����p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(13, 25, "" + double�ذe�p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(14, 25, "" + double�����p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(15, 25, "" + double��������p�pGlobal, objectSheet2); // 2015-11-18 B3018
      exeFun.putDataIntoExcel(16, 25, "" + double�Q���p�pGlobal, objectSheet2); // 2017-05-02 B4474
      exeFun.putDataIntoExcel(17, 25, "" + double�b��p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(18, 25, "" + double�����p�pGlobal, objectSheet2);
      exeFun.putDataIntoExcel(19, 25, "" + double�W�C���p�pGlobal, objectSheet2);

      exeFun.CopyPage(objectSheet1, objectSheet2);
      exeFun.doClearContents("A" + (intStartDataRow + 1) + ":AG" + (intStartDataRow + intPageDataRow), objectSheet2);
      //
      exeFun.doAdd1PageNo();
    }
    // ���� Excel ����
    exeFun.getReleaseExcelObject(retVector);
    return true;
  }

  // ����
  public String getSqlAnd() throws Exception {
    String stringSqlAnd = "";
    // �I�q���
    String stringOrderDateStart = getValue("OrderDateStart").trim();
    if (!"".equals(stringOrderDateStart)) stringSqlAnd += " AND  OrderDate  >=  '" + stringOrderDateStart + "'";
    String stringOrderDateEnd = getValue("OrderDateEnd").trim();
    if (!"".equals(stringOrderDateEnd)) stringSqlAnd += " AND  OrderDate  <=  '" + stringOrderDateEnd + "'";
    // �ɨ����
    String stringEnougDateStart = getValue("EnougDateStart").trim();
    if (!"".equals(stringEnougDateStart)) stringSqlAnd += " AND  EnougDate  >=  '" + stringEnougDateStart + "'";
    String stringEnougDateEnd = getValue("EnougDateEnd").trim();
    if (!"".equals(stringEnougDateEnd)) stringSqlAnd += " AND  EnougDate  <=  '" + stringEnougDateEnd + "' ";
    // ñ�����
    String stringContrDateStart = getValue("ContrDateStart").trim();
    if (!"".equals(stringContrDateStart)) stringSqlAnd += " AND  ContrDate  >=  '" + stringContrDateStart + "' ";
    String stringContrDateEnd = getValue("ContrDateEnd").trim();
    if (!"".equals(stringContrDateEnd)) stringSqlAnd += " AND  ContrDate  <= '" + stringContrDateEnd + "' ";
    // �X���|�f
    String stringDateCheckStart = getValue("DateCheckStart").trim();
    if (!"".equals(stringDateCheckStart)) stringSqlAnd += " AND  DateCheck  >=  '" + stringDateCheckStart + "' ";
    String stringDateCheckEnd = getValue("DateCheckEnd").trim();
    if (!"".equals(stringDateCheckEnd)) stringSqlAnd += " AND  DateCheck  <=  '" + stringDateCheckEnd + "'";
    // ñ�������
    String stringDateRangeStart = getValue("DateRangeStart").trim();
    if (!"".equals(stringDateRangeStart)) stringSqlAnd += " AND  DateRange  >=  '" + stringDateRangeStart + "' ";
    String stringDateRangeEnd = getValue("DateRangeEnd").trim();
    if (!"".equals(stringDateRangeEnd)) stringSqlAnd += " AND  DateRange  <=  '" + stringDateRangeEnd + "' ";
    return stringSqlAnd;
  }

  public String getSqlAnd2() throws Exception {
    String stringSqlAnd = "";
    // �ϧO
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    String stringFunction = getValue("Function").trim();
    if (!"".equals(stringDistributeNo)) {
      switch (Integer.parseInt(stringHouseLand)) {
      case 0: // ����
        stringSqlAnd = " AND  (H_Com  =  '" + stringDistributeNo + "'   OR  " + " L_Com  =  '" + stringDistributeNo + "') ";
        break;
      case 1: // �Ы�
        stringSqlAnd = " AND  H_Com  = '" + stringDistributeNo + "' ";
        break;
      case 2: // �g�a
        stringSqlAnd = " AND L_Com  = '" + stringDistributeNo + "' ";
        break;
      }
      switch (Integer.parseInt(stringFunction)) {
      case 0:
        // stringSqlAnd += " AND SUBSTRING(Position,1,1) NOT IN ('+','-') " ;
        break;
      case 1:
        // stringSqlAnd += " AND SUBSTRING(PositionRent,1,1) NOT IN ('+','-') " ;
        break;
      case 2:
        // stringSqlAnd += " AND (SUBSTRING(Position,1,1) NOT IN ('+','-') OR" +
        // " SUBSTRING(PositionRent,1,1) NOT IN ('+','-')) " ;
        break;
      case 3:
        // stringSqlAnd += " AND SUBSTRING(Car,1,1) NOT IN ('+','-') " ;
        break;
      case 4:
        // stringSqlAnd += " AND SUBSTRING(CarRent,1,1) NOT IN ('+','-') " ;
        break;
      case 5:
        // stringSqlAnd += " AND (SUBSTRING(Car,1,1) NOT IN ('+','-') OR" +
        // " SUBSTRING(CarRent,1,1) NOT IN ('+','-')) " ;
        break;
      }
    }
    // �a�D
    String stringLandOwner = getValue("LandOwner").trim();
    switch (Integer.parseInt(stringLandOwner)) {
    case 0:
      break;
    case 1:
      stringSqlAnd += " AND  LandOwner  =  'N' ";
      break;
    case 2:
      stringSqlAnd += " AND  LandOwner  =  'Y' ";
      break;
    }
    return stringSqlAnd;
  }

  public String getSqlAnd3() throws Exception {
    String stringSqlAnd = "";
    // �ϧO
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    String stringFunction = getValue("Function").trim();
    if (!"".equals(stringDistributeNo)) {
      switch (Integer.parseInt(stringFunction)) {
      case 0:
        // stringSqlAnd += " AND SUBSTRING(Position,1,1) NOT IN ('+','-') " ;
        break;
      case 1:
        // stringSqlAnd += " AND SUBSTRING(PositionRent,1,1) NOT IN ('+','-') " ;
        break;
      case 2:
        // stringSqlAnd += " AND (SUBSTRING(Position,1,1) NOT IN ('+','-') OR" +
        // " SUBSTRING(PositionRent,1,1) NOT IN ('+','-')) " ;
        break;
      case 3:
        // stringSqlAnd += " AND SUBSTRING(Car,1,1) NOT IN ('+','-') " ;
        break;
      case 4:
        // stringSqlAnd += " AND SUBSTRING(CarRent,1,1) NOT IN ('+','-') " ;
        break;
      case 5:
        // stringSqlAnd += " AND (SUBSTRING(Car,1,1) NOT IN ('+','-') OR" +
        // " SUBSTRING(CarRent,1,1) NOT IN ('+','-')) " ;
        break;
      }
    }
    // �a�D
    String stringLandOwner = getValue("LandOwner").trim();
    switch (Integer.parseInt(stringLandOwner)) {
    case 0:
      break;
    case 1:
      stringSqlAnd += " AND  LandOwner  =  'N' ";
      break;
    case 2:
      stringSqlAnd += " AND  LandOwner  =  'Y' ";
      break;
    }
    return stringSqlAnd;
  }

  public String[] getSqlAnd4() throws Exception {
    String stringSqlAnd = "";
    // �ϧO
    String stringDistributeNo = getValue("Distribute").trim();
    String stringHouseLand = getValue("HouseLand").trim();
    Vector vectorSql = new Vector();
    if (!"".equals(stringDistributeNo)) {
      switch (Integer.parseInt(stringHouseLand)) {
      case 0: // ����
        stringSqlAnd = " AND  H_Com  = '" + stringDistributeNo + "' ";
        vectorSql.add(stringSqlAnd);
        stringSqlAnd = " AND L_Com  = '" + stringDistributeNo + "' ";
        vectorSql.add(stringSqlAnd);
        break;
      case 1: // �Ы�
        stringSqlAnd = " AND  H_Com  = '" + stringDistributeNo + "' ";
        vectorSql.add(stringSqlAnd);
        break;
      case 2: // �g�a
        stringSqlAnd = " AND L_Com  = '" + stringDistributeNo + "' ";
        vectorSql.add(stringSqlAnd);
        break;
      }
    } else {
      vectorSql.add("");
    }
    return (String[]) vectorSql.toArray(new String[0]);
  }

  public String getConditionDescription() throws Exception {
    String stringConditionDescription = "";
    // �ϧO
    String stringDistribute = getValue("Distribute").trim();
    if (stringDistribute.length() > 0) {
      stringConditionDescription += "�ϧO:" + stringDistribute + ";";
    } else {
      stringConditionDescription += "�ϧO:�L" + ";";
    }
    // �ЫΡB�g�a
    String stringHouseLand = getValue("HouseLand").trim();
    switch (Integer.parseInt(stringHouseLand)) {
    case 0:
      stringConditionDescription += "�Фg:����;";
      break;
    case 1:
      stringConditionDescription += "�Фg:�Ы�;";
      break;
    case 2:
      stringConditionDescription += "�Фg:�g�a;";
      break;
    }
    // �a�D
    String stringLandOwner = getValue("LandOwner").trim();
    switch (Integer.parseInt(stringLandOwner)) {
    case 0:
      stringConditionDescription += "�a�D:����;";
      break;
    case 1:
      stringConditionDescription += "�a�D:���t�a�D;";
      break;
    case 2:
      stringConditionDescription += "�a�D:�a�D;";
      break;
    }
    // �ɨ����
    String stringEnougDateStart = convert.replace(getValue("EnougDateStart").trim(), "/", "");
    String stringEnougDateEnd = convert.replace(getValue("EnougDateEnd").trim(), "/", "");
    if (!"".equals(stringEnougDateStart) && !"".equals(stringEnougDateEnd)) {
      stringConditionDescription += "�ɨ�:" + stringEnougDateStart + "��" + stringEnougDateEnd + ";";
    }
    // ñ�����
    String stringContrDateStart = convert.replace(getValue("ContrDateStart").trim(), "/", "");
    String stringContrDateEnd = convert.replace(getValue("ContrDateEnd").trim(), "/", "");
    if (!"".equals(stringContrDateStart) && !"".equals(stringContrDateEnd)) {
      stringConditionDescription += "ñ��:" + stringContrDateStart + "��" + stringContrDateEnd + ";";
    }
    return stringConditionDescription;
  }

  public String getDateAC(String value, String stringErrorFieldName) throws Throwable {
    int intIndexStart = 0;
    int intIndexEnd = value.indexOf("/");
    String stringTmp = "";
    Vector retVector = new Vector();
    int i = 0;
    while (intIndexEnd != -1) {
      stringTmp = value.substring(intIndexStart, intIndexEnd);
      if (!"".equals(stringTmp)) retVector.add(stringTmp.trim());
      intIndexStart = intIndexEnd + "/".length();
      intIndexEnd = value.indexOf("/", intIndexStart);
      i++;
    }
    stringTmp = value.substring(intIndexStart, value.length());
    if (!"".equals(stringTmp)) retVector.add(stringTmp.trim());
    boolean booleanFlow = (retVector.size() != 3);
    booleanFlow = booleanFlow && ((retVector.size() == 1 && value.length() != 8) || (retVector.size() != 1));
    if (booleanFlow) {
      return "[" + stringErrorFieldName + "] ����榡���~(YYYY/MM/DD)�C";
    }
    stringTmp = "";
    booleanFlow = true;
    if (((String) retVector.get(0)).length() < 4) stringTmp = "0" + (String) retVector.get(0);
    else
      stringTmp = (String) retVector.get(0);
    for (int intRetVector = 1; intRetVector < retVector.size(); intRetVector++) {
      if (((String) retVector.get(intRetVector)).length() == 1) {
        stringTmp += "/0" + (String) retVector.get(intRetVector);
      } else {
        stringTmp += "/" + (String) retVector.get(intRetVector);
      }
    }
    if (stringTmp.length() == 8) {
      stringTmp = stringTmp.substring(0, 4) + "/" + stringTmp.substring(4, 6) + "/" + stringTmp.substring(6, 8);
    }
    String retDate = stringTmp;
    stringTmp = stringTmp.substring(0, 4) + stringTmp.substring(5, 7) + stringTmp.substring(8, 10);
    if (!check.isACDay(stringTmp)) {
      return "[" + stringErrorFieldName + "] ����榡���~(YYYY/MM/DD)�C";
    }
    return retDate;
  }

  public String getConditionDescription2() throws Exception {
    String stringConditionDescription = "";
    // �X���|�f*
    String stringDateCheckStart = convert.replace(getValue("DateCheckStart").trim(), "/", "");
    String stringDateCheckEnd = convert.replace(getValue("DateCheckEnd").trim(), "/", "");
    if (!"".equals(stringDateCheckStart) && !"".equals(stringDateCheckEnd)) {
      stringConditionDescription += "�X���|�f:" + stringDateCheckStart + "��" + stringDateCheckEnd + ";";
    }
    // ñ�������*
    String stringDateRangeStart = convert.replace(getValue("DateRangeStart").trim(), "/", "");
    String stringDateRangeEnd = convert.replace(getValue("DateRangeEnd").trim(), "/", "");
    if (!"".equals(stringDateRangeStart) && !"".equals(stringDateRangeEnd)) {
      stringConditionDescription += "ñ�������:" + stringDateRangeStart + "��" + stringDateRangeEnd + ";";
    }
    return stringConditionDescription;
  }

  // ����榡�ഫ
  public String getConertFormatDate(String stringDate) {
    String stringTmp = convert.ac2roc(convert.replace(stringDate, "/", ""));
    String retDate = convert.FormatedDate(stringTmp, "/");
    return retDate;
  }

  public double doParseDouble(String stringNum, String stringPosition) throws Exception {
    //
    double doubleNum = 0;
    if ("".equals(stringNum) || "null".equals(stringNum)) {
      System.out.println("[" + stringNum + "]-----------" + stringPosition + "�L�k��R[" + stringNum + "]�A�^�� 0�C");
      return 0;
    }
    try {
      doubleNum = Double.parseDouble(stringNum);
    } catch (Exception e) {
      System.out.println(stringPosition + "�L�k��R[" + stringNum + "]�A�^�� 0�C");
      return 0;
    }
    return doubleNum;
  }

  // ��Ʈw
  public String getACom(String stringDistributeNo) throws Exception {
    String stringSql = "";
    String stringACom = "";
    String[][] retACom = null;
    //
    stringSql = "SELECT  Com_Name  FROM  A_Com  WHERE  Com_No  ='" + stringDistributeNo + "'";
    retACom = dbSale.queryFromPool(stringSql);
    if (retACom.length != 0) stringACom = retACom[0][0].trim();
    return stringACom;
  }

  // �ˮ�
  public String[][] getAProject(String stringProjectID) throws Exception {
    String stringSql = "";
    String[][] retAProject = null;
    //
    stringSql = "SELECT  PROJECTID,  PROJECTNAME " + " FROM  A_PROJECT " + " WHERE  PROJECTID  =  '" + stringProjectID + "' ";
    retAProject = dbSale.queryFromPool(stringSql);
    return retAProject;
  }

  public String[][] getDeleteForAProject(String stringProjectID, String stringHouseCar) throws Exception {
    String stringSql = "";
    String[][] retAProject = null;
    //
    stringSql = "SELECT  H_Com,                                              L_Com,                      OpenDate,  " + stringHouseCar
        + "PositionMoneyS,  H_PositionMoneyS,  L_PositionMoneyS,  " + stringHouseCar + "CarMoneyS,          H_CarMoneyS,          L_CarMoneyS " + " FROM  A_Project "
        + " WHERE  ProjectID  =  '" + stringProjectID + "'";
    retAProject = dbSale.queryFromPool(stringSql);
    return retAProject;
  }

  public String[][] getSaleForAProject(String stringProjectID, String stringHouseCar) throws Exception {
    String stringSql = "";
    String[][] retAProject = null;
    //
    stringSql = "SELECT      H_Com,  L_Com, " + stringHouseCar + "PositionMoneyS,  H_PositionMoneyS,   L_PositionMoneyS,  " + stringHouseCar
        + "CarMoneyS,          H_CarMoneyS,           L_CarMoneyS, " + " OpenDate " + " FROM  A_Project " + " WHERE  ProjectID  =  '" + stringProjectID + "'";
    retAProject = dbSale.queryFromPool(stringSql);
    return retAProject;
  }

  public String[][] getRentForAProject(String stringProjectID, String stringHouseCar) throws Exception {
    String stringSql = "";
    String[][] retAProject = null;
    // 0 H_Com 1 L_Com 2 OpenDate 3 PositionMoneyR1 4 CarMoneyR1
    stringSql = "SELECT  H_Com,                                                   L_Com,                                            OpenDate,  " + stringHouseCar
        + "PositionMoneyR,  " + stringHouseCar + "CarMoneyR " + " FROM  A_Project " + " WHERE  ProjectID  =  '" + stringProjectID + "'";
    retAProject = dbSale.queryFromPool(stringSql);
    return retAProject;
  }

  public String[][] getSaleForAProject(String stringProjectID) throws Exception {
    String stringSql = "";
    String[][] retAProject = null;
    //
    stringSql = "SELECT  H_Com,   L_Com,  OpenDate  " + " FROM  A_Project " + " WHERE  ProjectID  =  '" + stringProjectID + "'";
    retAProject = dbSale.queryFromPool(stringSql);
    return retAProject;
  }

  public String[][] getTotRecForASale(String stringProjectID, String stringSqlAnd, String stringSqlAnd2, String stringThisType) throws Exception {
    String stringSql = "";
    String[][] retASale = null;
    //
    stringSql = "SELECT  COUNT(*)  " + " FROM  A_Sale " + " WHERE  ProjectID1  =  '" + stringProjectID + "' " + stringSqlAnd2 + stringSqlAnd + " AND  Dealmoney > 0 ";
    // " AND Not IsDate(DelDate) "
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND LEN(Position) > 0 ";
      // " AND SUBSTRING(Position,1,1) NOT IN ('+','-') ";
    } else if ("����".equals(stringThisType)) {
      stringSql += " AND LEN(Car) > 0 ";
      // " AND SUBSTRING(Car,1,1) NOT IN ('+','-') ";
    }
    retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getSumDealMoneyForASale(String stringHouseCar, String stringProjectID, String stringOrderDateEnd, String stringSqlAnd2, String stringThisType)
      throws Exception {
    String stringSql = "";
    String[][] retASale = null;
    // 0 H_COM 1 L_COM
    // 2 SumDealMoney 3 SumDealMoney2 4 SumDealMoney3 5 SumPingSu
    stringSql = "SELECT  H_COM,                                                     L_COM,  " + " SUM(" + stringHouseCar + "DealMoney),   SUM(H_DealMoney),  SUM(L_DealMoney), "
        + " SUM(PingSu) " + " FROM  A_SALE " + " WHERE  ProjectID1 = '" + stringProjectID + "' " + " AND  OrderDate  <=  '" + stringOrderDateEnd + "'  " + stringSqlAnd2;
    // " AND Not IsDate(DelDate) " +
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND LEN(Position)  >  0 ";
    } else if ("����".equals(stringThisType)) {
      stringSql += " AND LEN(Car)  >  0 ";
    }
    stringSql += " GROUP BY  H_COM,  L_COM ";
    retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getRentForASale(String stringHouseCar, String stringProjectID, String stringOrderDateStart, String stringOrderDateEnd, String stringThisType) throws Exception {
    String stringSql = "";
    String stringSql�e�ݵe�� = getSqlAnd2() + getSqlAnd();
    // String[] arraySql = getSqlAnd4() ;
    String[][] retASale = null;
    /*
     * 0.PositionRent 1.CarRent 2.Custom 3.OrderDate 4.OrderMon1 5.EnougDate
     * 6.EnougMon1 7.ContrDate 8.ContrMon1 9.Deldate 10.PingSu 11.RentRange
     * 12.RentLast 13.PingRent 14.Rent 15.RentFree 16.Guranteer 17.PreMoney1
     * 18.DealMoney1 19.GiftMoney1 20.CommMoney1 21.PureMoney1 22.LastMoney1
     * 23.BalaMoney1 24.SaleName1 25.SaleName2 26.SaleName3 27.SaleName4
     * 28.SaleName5 29.MediaName 30.ZoneName 31.MajorName 32.UseType 33.Remark
     * 34.DateRange 35.DateCheck 36.DateFile 37.DateBonus 38.SaleName6 39.SaleName7
     * 40.SaleName8 41.SaleName9 42.SaleName10 43 H_Com 44.L_Com 45.DealMoney
     * 46.H_DealMoney 47.L_DealMoney 48 CommMoney1 49 H_LastMoney 50 L_LastMoney 51
     * H_GiftMoney 52 L_GiftMoney 53 H_CommMoney 54 L_CommMoney 55 H_CommMoney1 56
     * L_CommMoney1 57 H_BalaMoney 58 L_BalaMoney 59 H_ViMoney 60 L_ViMoney
     */
    stringSql = "SELECT  PositionRent,                                 CarRent,                                         Custom,                                          OrderDate,  "
        + stringHouseCar + "OrderMon,          EnougDate,  " + stringHouseCar + "EnougMon,      ContrDate," + stringHouseCar
        + "ContrMon,           Deldate,                                          PingSu,                                           RentRange,  "
        + "RentLast,                                       PingRent,                                        Rent,                                               RentFree,  "
        + "Guranteer,  " + stringHouseCar + "PreMoney,  " + stringHouseCar + "DealMoney,  " + stringHouseCar + "GiftMoney,  " + stringHouseCar + "CommMoney,  " + stringHouseCar
        + "PureMoney,  " + stringHouseCar + "LastMoney,  " + stringHouseCar + "BalaMoney,  "
        + "SaleName1,                                    SaleName2,                                     SaleName3,                                    SaleName4,  "
        + "SaleName5,                                    MediaName,                                    ZoneName,                                    MajorName, "
        + "UseType,                                        Remark,                                            DateRange,                                    DateCheck,  "
        + "DateFile,                                        DateBonus,                                      SaleName6,                                     SaleName7, "
        + "SaleName8,                                   SaleName9,                                      SaleName10,                                   H_Com, "
        + " L_Com,                                          DealMoney,                                      H_DealMoney,                                L_DealMoney,"
        + stringHouseCar + "CommMoney1,    H_LastMoney,                                  L_LastMoney,                                 H_GiftMoney, \n"
        + " L_GiftMoney,                                H_CommMoney,                             L_CommMoney,                              H_CommMoney1,   \n"
        + " L_CommMoney1,                         H_BalaMoney,                                 L_BalaMoney,                         H_ViMoney,                                 L_ViMoney "
        + " FROM  A_SALE " + " WHERE  ProjectID1  =  '" + stringProjectID + "' "
//        + " AND  OrderDate  Between  '" + stringOrderDateStart + "'   AND  '" + stringOrderDateEnd + "' " 
        + stringSql�e�ݵe��;
    // " AND Not IsDate(DelDate) " ;
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND  LEN(PositionRent)  >  0  ORDER BY  OrderDate,  Custom ";
    } else if ("����".equals(stringThisType)) {
      stringSql += " AND  LEN(CarRent)  >  0  ORDER BY  OrderDate,  Custom ";
    }
    retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getSaleForASale(String stringHouseCar, String stringProjectID, String stringOrderDateStart, String stringOrderDateEnd, String stringThisType,
      String stringOrderBy, String stringSqlAnd, String stringSqlAnd2) throws Exception {
    String stringSql = "";
    /*
     * OrderMon2 0 _ H_Com 1 _ L_Com 2 _ Position 3 _ Car 4 _ Custom 5 _ OrderDate 6
     * _ OrderMon1 7 _ OrderMon2 8 _ OrderMon3 9 _EnougDate 10 _ EnougMon1 11 _
     * EnougMon2 12 _ EnougMon3 13 _ ContrDate 14 _ ContrMon1 15 _ ContrMon2 16 _
     * ContrMon3 16 _ Deldate 18 _ PingSu 19 _ PreMoney1 20 _ PreMoney2 21 _
     * PreMoney3 22 _ DealMoney1 23 _ DealMoney2 24 _ DealMoney3 25 _ GiftMoney1 26
     * _ GiftMoney2 27 _ GiftMoney3 28 _ CommMoney1 29 _ CommMoney2 30 _ CommMoney3
     * 31 _ PureMoney1 32 _ PureMoney2 33 _ PureMoney3 34 _ LastMoney1 35 _
     * LastMoney2 36 _ LastMoney3 37 _ BalaMoney1 38 _ BalaMoney2 39 _ BalaMoney3 40
     * _ SaleName1 41 _ SaleName2 42 _ SaleName3 43 _ SaleName4 44 _ SaleName5 45 _
     * MediaName 46 _ ZoneName 47 _ MajorName 48 _ UseType 49 _ Remark 50 _
     * DateRange 51 _ DateCheck 52 _ DateFile 53 _ DateBonus 54 _ SaleName6 55 _
     * SaleName7 56 _ SaleName8 57 _ SaleName9 58 _ SaleName10 59 _ CommMoney1 60 _
     * CommMoney2 61 _ CommMoney1 62_ViMoney 63 64
     */
    stringSql = "SELECT  H_Com,                                        L_Com,                                        Position,                                      Car, "
        + " Custom,                                      OrderDate,  " + stringHouseCar + "OrderMon,    H_OrderMon, " + " L_OrderMon,                              EnougDate,  "
        + stringHouseCar + "EnougMon,    H_EnougMon, " + " L_EnougMon,                             ContrDate,  " + stringHouseCar + "ContrMon,     H_ContrMon, "
        + " L_ContrMon,                              Deldate,                                       PingSu,  " + stringHouseCar + "PreMoney, "
        + " H_PreMoney,                              L_PreMoney,  " + stringHouseCar + "DealMoney,   H_DealMoney, " + " L_DealMoney,  " + stringHouseCar
        + "GiftMoney,    H_GiftMoney,                              L_GiftMoney,  " + stringHouseCar + "CommMoney,    H_CommMoney,                          L_CommMoney,  "
        + stringHouseCar + "PureMoney, " + " H_PureMoney,                            L_PureMoney,  " + stringHouseCar + "LastMoney,    H_LastMoney, " + " L_LastMoney,  "
        + stringHouseCar + "BalaMoney,    H_BalaMoney,                              L_BalaMoney, "
        + " SaleName1,                                  SaleName2,                                SaleName3,                                  SaleName4, "
        + " SaleName5,                                  MediaName,                               ZoneName,                                   MajorName, "
        + " UseType,                                      Remark,                                       DateRange,                                  DateCheck, "
        + " DateFile,                                       DateBonus,                                SaleName6,                                  SaleName7, "
        + " SaleName8,                                  SaleName9,                                SaleName10, " + stringHouseCar + "CommMoney1, "
        + " H_CommMoney1,                         L_CommMoney1 , " + stringHouseCar + "ViMoney, " + " H_ViMoney,                                   L_ViMoney " + " FROM  A_Sale "
        + " WHERE  ProjectID1  =  '" + stringProjectID + "' "
        // + " AND OrderDate Between '" + stringOrderDateStart + "' AND '" +
        // stringOrderDateEnd + "' "
        + stringSqlAnd2 + stringSqlAnd;
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND  LEN(Position)  >  0  ORDER BY  " + stringOrderBy;
    } else if ("����".equals(stringThisType)) {
      stringSql += " AND  LEN(Car)  >  0  ORDER BY  " + stringOrderBy;
    }
    String[][] retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getSumSaleForASale(String stringHouseCar, String stringProjectID, String stringOrderDateEnd, String stringThisType, String stringSqlAnd2) throws Exception {
    String stringSql = "";
    String stringContrDateEnd = getValue("ContrDateEnd").trim();
    String stringEnougDateEnd = getValue("EnougDateEnd").trim();
    String stringDateCheckEnd = getValue("DateCheckEnd").trim();
    String stringDateRangeEnd = getValue("DateRangeEnd").trim();

    /*
     * 0 H_Com 1 L_Com 2. SumPingSu 3. SumPreMoney 4. SumPreMoney2 5. SumPreMoney3
     * 6. SumDealMoney 7. SumDealMoney2 8. SumDealMoney3 9. SumGiftMoney 10.
     * SumGiftMoney2 11. SumGiftMoney3 12.SumCommMoney 13.SumCommMoney2 14.
     * SumCommMoney3 15. SumPureMoney 16. SumPureMoney2 17. SumPureMoney3 18.
     * SumLastMoney 19. SumLastMoney2 20. SumLastMoney3 21.SumBalaMoney 22.
     * SumBalaMoney2 23. SumBalaMoney3 24 SumCommMoney11 25.SumCommMoney12 26
     * SumCommMoney13 27 ViMoney1 28 ViMoney2 29 ViMoney3
     */
    stringSql = "SELECT H_Com,                           L_Com,  " + " SUM(PingSu),                SUM(" + stringHouseCar + "PreMoney),       SUM(H_PreMoney), "
        + " SUM(L_PreMoney),       SUM(" + stringHouseCar + "DealMoney),     SUM(H_DealMoney),  " + " SUM(L_DealMoney),     SUM(" + stringHouseCar
        + "GiftMoney),       SUM(H_GiftMoney), " + " SUM(L_GiftMoney),      SUM(" + stringHouseCar + "CommMoney),   SUM(H_CommMoney),  " + " SUM(L_CommMoney),  SUM("
        + stringHouseCar + "PureMoney),     SUM(H_PureMoney), " + " SUM(L_PureMoney),    SUM(" + stringHouseCar + "LastMoney),       SUM(H_LastMoney),  "
        + " SUM(L_LastMoney),     SUM(" + stringHouseCar + "BalaMoney),       SUM(H_BalaMoney), " + " SUM(L_BalaMoney),     SUM(" + stringHouseCar
        + "CommMoney1),  SUM(H_CommMoney1),  " + " SUM(L_CommMoney1),     SUM(" + stringHouseCar + "ViMoney),  SUM(H_ViMoney),  " + " SUM(L_ViMoney)" + " FROM  A_SALE "
        + " WHERE  ProjectID1 = '" + stringProjectID + "'" + " AND DEalmoney <> 0 " + stringSqlAnd2;
    if (!"".equals(stringOrderDateEnd)) stringSql += " AND  OrderDate <= '" + stringOrderDateEnd + "' ";
    if (!"".equals(stringContrDateEnd)) stringSql += " AND  ContrDate <= '" + stringContrDateEnd + "' ";
    if (!"".equals(stringEnougDateEnd)) stringSql += " AND  EnougDate <= '" + stringEnougDateEnd + "' ";
    if (!"".equals(stringDateCheckEnd)) stringSql += " AND  DateCheck <= '" + stringDateCheckEnd + "' ";
    if (!"".equals(stringDateRangeEnd)) stringSql += " AND  DateRange <= '" + stringDateRangeEnd + "' ";

    // " AND Not IsDate(DelDate) " ;
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND LEN(Position) > 0 ";
    } else if ("����".equals(stringThisType)) {
      stringSql += " AND LEN(Car) > 0 ";
    }
    stringSql += "GROUP BY H_Com,  L_Com ";
    System.out.println(stringSql);
    String[][] retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getDeleteForASale1(String stringProjectID, String stringOrderDateStart, String stringOrderDateEnd, String stringTypeC, String stringType, String stringSqlAnd)
      throws Exception {
    String stringSql = "";
    String stringOrderDateCondition = getValue("OrderDateCondition").trim();

    /*
     * 0.Position 1.PositionRent 2.Car 3.CarRent 4.Custom 5.OrderDate 6.OrderMon1
     * 7.OrderMon2 8.OrderMon3 9.EnougDate 10.EnougMon1 11.EnougMon2 12.EnougMon3
     * 13.ContrDate 14.ContrMon1 15.ContrMon2 16.ContrMon3 17.Deldate 18.PingSu
     * 19.PreMoney1 20.PreMoney2 21.PreMoney3 22.DealMoney1 23.DealMoney2
     * 24.DealMoney3 25.GiftMoney1 26.GiftMoney2 27.GiftMoney3 28.CommMoney1
     * 29.CommMoney2 30.CommMoney3 31.PureMoney1 32.PureMoney2 33.PureMoney3
     * 34.LastMoney1 35. LastMoney2 36.LastMoney3 37.BalaMoney1 38.BalaMoney2
     * 39.BalaMoney3 40.SaleName1 41.SaleName2 42.SaleName3 43.SaleName4
     * 44.SaleName5 45.MediaName 46.ZoneName 47.MajorName 48.UseType 49.Remark
     * 50.DateRange 51.DateCheck 52.DateFile 53.DateBonus 54.H_COM 55.L_COM
     * 56.SaleName6 57.SaleName7 58.SaleName8 59.SaleName9 60.SaleName10
     * 61.CommMoney11 62.CommMoney12 63.CommMoney13 64.ViMoney1 65.ViMoney2
     * 66.ViMoney3
     */
    stringSql = "SELECT  Position,                         PositionRent,                      Car,                                     CarRent,  "
        + " Custom,                           OrderDate,  " + stringType + "OrderMon ,  H_OrderMon,  " + " L_OrderMon,                   EnougDate,  " + stringType
        + "EnougMon,   H_EnougMon,  " + " L_EnougMon,                  ContrDate,  " + stringType + "ContrMon,    H_ContrMon,  "
        + " L_ContrMon ,                   Deldate,                               PingSu,  " + stringType + "PreMoney,  " + " H_PreMoney ,                   L_PreMoney,  "
        + stringType + "DealMoney,  H_DealMoney,  " + " L_DealMoney ,  " + stringType + "GiftMoney,   H_GiftMoney,                      L_GiftMoney,  " + stringType
        + "CommMoney,  H_CommMoney,                 L_CommMoney,  " + stringType + "PureMoney,  " + " H_PureMoney,                  L_PureMoney,  " + stringType
        + "LastMoney,  H_LastMoney,  " + " L_LastMoney ,  " + stringType + "BalaMoney,  H_BalaMoney,                    L_BalaMoney,  "
        + " SaleName1,                       SaleName2,                         SaleName3,                        SaleName4,  "
        + " SaleName5,                       MediaName,                        ZoneName,                         MajorName,  "
        + " UseType,                            Remark,                               DateRange,                        DateCheck,  "
        + " DateFile,                            DateBonus,                         H_COM,                              L_COM, "
        + " SaleName6,                       SaleName7,                          SaleName8,                       SaleName9, " + " SaleName10, " + stringType
        + "CommMoney1,  H_CommMoney1,           L_CommMoney1 ," + " " + stringType + "ViMoney,  H_ViMoney,           L_ViMoney " + " FROM A_Sale1 " + " WHERE  ProjectID1  =  '"
        + stringProjectID + "' " + " AND  DelDate  Between  '" + stringOrderDateStart + "'  AND  '" + stringOrderDateEnd + "' " + stringSqlAnd;
    // " AND IsDate(DelDate) "
    if ("Y".equals(stringOrderDateCondition)) {
      stringSql += " AND  OrderDate  NOT  BETWEEN  '" + stringOrderDateStart + "'  AND  '" + stringOrderDateEnd + "' ";
    }
    if ("�Фl".equals(stringTypeC)) {
      stringSql += " AND  ((Position           IS  NOT  NULL  AND  Position          <>  '')  OR" + " (PositionRent  IS  NOT  NULL  AND  PositionRent  <>  ''))  "
          + "  ORDER BY  Position,  PositionRent ";
    } else if ("����".equals(stringTypeC)) {
      stringSql += " AND  ((Car           IS  NOT  NULL  AND  Car         <>  '')  OR" + " (CarRent  IS  NOT  NULL  AND  CarRent  <>  ''))  " + "  ORDER BY  Car,  CarRent ";
    }
    String[][] retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getSumDeleteForASale1(String stringHouseCar, String stringProjectID, String stringOrderDateEnd, String stringThisType, String stringSqlAnd) throws Exception {
    String stringSql = "";
    String stringOrderDateCondition = getValue("OrderDateCondition").trim();
    /*
     * 0.SumPingSu 1.SumPreMoney 2.SumPreMoney2 3.SumPreMoney3 4.SumDealMoney
     * 5.SumDealMoney2 6.SumDealMoney3 7.SumGiftMoney 8.SumGiftMoney2
     * 9.SumGiftMoney3 10.SumCommMoney 11.SumCommMoney2 12.SumCommMoney3
     * 13.SumPureMoney 14.SumPureMoney2 15.SumPureMoney3 16.SumLastMoney
     * 17.SumLastMoney2 18.SumLastMoney3 19.SumBalaMoney 20.SumBalaMoney2
     * 21.SumBalaMoney3 22.H_COM 23.L_COM 24.SumCommMoney11 25.SumCommMoney12 26
     * SumCommMoney13 27.Sum(ViMoney) 28.Sum(H_ViMoney) 29.Sum(L_ViMoney)
     */
    stringSql = "SELECT  SUM(PingSu),                SUM(" + stringHouseCar + "PreMoney),      SUM(H_PreMoney),  " + " SUM(L_PreMoney),       SUM(" + stringHouseCar
        + "DealMoney),    SUM(H_DealMoney),  " + " SUM(L_DealMoney),     SUM(" + stringHouseCar + "GiftMoney),      SUM(H_GiftMoney),  " + " SUM(L_GiftMoney),      SUM("
        + stringHouseCar + "CommMoney),  SUM(H_CommMoney),  " + " SUM(L_CommMoney),  SUM(" + stringHouseCar + "PureMoney),    SUM(H_PureMoney),  " + " SUM(L_PureMoney),  SUM("
        + stringHouseCar + "LastMoney),     SUM(H_LastMoney),  " + " SUM(L_LastMoney),      SUM(" + stringHouseCar + "BalaMoney),   SUM(H_BalaMoney), "
        + " SUM(L_BalaMoney),      H_COM,                                                     L_COM,  " + " SUM(" + stringHouseCar
        + "CommMoney1),  SUM(H_CommMoney1), SUM(L_CommMoney), " + " SUM(" + stringHouseCar + "ViMoney),    SUM(H_ViMoney) ,  SUM(L_ViMoney) " + " FROM  A_Sale1 "
        + " WHERE  ProjectID1  =  '" + stringProjectID + "' " + " AND  DelDate  <=  '" + stringOrderDateEnd + "' " + stringSqlAnd;
    // " AND IsDate(DelDate) "
    if ("Y".equals(stringOrderDateCondition)) {
      String stringOrderDateStart = getValue("OrderDateStart").trim();
      stringSql += " AND  OrderDate  NOT  BETWEEN  '" + stringOrderDateStart + "'  AND  '" + stringOrderDateEnd + "' ";
    }
    if ("�Фl".equals(stringThisType)) {
      stringSql += " AND  LEN(Position)  >  0 ";
    } else {
      stringSql += " AND  LEN(Car)  >  0 ";
    }
    stringSql += " GROUP BY  H_COM,  L_COM ";
    String[][] retASale = dbSale.queryFromPool(stringSql);
    return retASale;
  }

  public String[][] getSale02M020() throws Exception {
    String stringSql = "";
    String[][] retSale02M020 = null;
    //
    stringSql = "SELECT  ProjectID1  " + " FROM  Sale02M020 " + " WHERE  EMP_NO  =  '" + getUser() + "' ";
    retSale02M020 = dbSale.queryFromPool(stringSql);
    return retSale02M020;
  }

  public String[][] getSale02M020(String stringProjectID1) throws Exception {
    String stringSql = "";
    String[][] retSale02M020 = null;
    //
    stringSql = "SELECT  ProjectID1  " + " FROM  Sale02M020 " + " WHERE  EMP_NO  =  '" + getUser() + "' " + " AND  ProjectID1  = '" + stringProjectID1 + "' ";
    retSale02M020 = dbSale.queryFromPool(stringSql);
    return retSale02M020;
  }

  public String getInformation() {
    return "---------------\u5217\u5370\u6309\u9215\u7a0b\u5f0f.preProcess()----------------";
  }
}
