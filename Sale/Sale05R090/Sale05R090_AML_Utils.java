//20200107 Kyle add
package Sale.Sale05R090;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jcx.db.talk;
import jcx.jform.bproc;

public class Sale05R090_AML_Utils extends bproc{
     //DB connection
     talk dbSale = getTalk("Sale");

  public String getDefaultValue(String value)throws Throwable{
        message("Utils Go~");
    return value;
    }

    //查詢匯出地
    public Map getExportingPlaces(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select distinct T328.docNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + RTRIM(b.ExportingPlace) FROM Sale05M328 b WHERE T328.DocNo = b.docNo FOR XML PATH('')), 1, 3, '') AS ExportingPlace ");
        sbSQL.append("from Sale05M328 T328 , Sale05M086 T086 ");
        sbSQL.append("where T328.docNo = T086.docNo ");
        sbSQL.append("and LEN(ExportingPlace) > 0 ");
        sbSQL.append("and T086.orderNo in ("+ orderNos +") ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        //格式: Map(orderNo , Map(key , value))
        for (int x=0; x<ret.length; x++) {
            String[] thisArr = ret[x];
            String thisRsMapKey = thisArr[0].trim();
            Map mapLv2 = new HashMap();
            mapLv2.put("ExportingPlace" , thisArr[1].trim().replaceAll("Q_Q" , "\n"));
            rsMap.put(thisRsMapKey , mapLv2);
        }
        return rsMap;
    }

    //查詢收款期別
    public Map getPositionOrder_No(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select distinct T081.DocNo ");
        sbSQL.append(",REPLACE(REPLACE(STUFF((SELECT 'Q_Q' + b.position , ' : ', (SELECT TOP 1 RTRIM(ITEMLS_CHINESE) FROM Sale05M052 T52 WHERE T52.ITEMLS_CD = b.ITEMLS_CD) FROM Sale05M081 b WHERE T081.docNo = b.docNo order by b.HouseCar desc FOR XML PATH('')), 1, 3, ''), '<order_no>', ''), '</order_no>', '') AS order_no ");
        sbSQL.append("from Sale05M081 T081 , Sale05M086 T086  ");
        sbSQL.append("where T081.docNo = T086.docNo ");
        sbSQL.append("and T086.orderNo in ("+ orderNos +") ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        //格式: Map(orderNo , Map(key , value))
        for (int x=0; x<ret.length; x++) {
            String[] thisArr = ret[x];
            String thisRsMapKey = thisArr[0].trim();
            Map mapLv2 = new HashMap();
            mapLv2.put("QQ" , thisArr[1].trim().replaceAll("Q_Q" , "\n"));
            rsMap.put(thisRsMapKey , mapLv2);
        }
        return rsMap;
    }

    //查詢洗錢樣態
    public Map getSale05M070_Rpt3(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select distinct a.orderNO , a.DocNo , SHB06 ");
        sbSQL.append("from Sale05M070 a where 1=1 ");
        sbSQL.append("and orderNo in ("+ orderNos +") and RecordDesc not in ('不符合', '不適用') and RecordDesc not like '風險值%' ");
        sbSQL.append("order by orderNo desc , docNO desc ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        //格式: Map(orderNo , "String")
        StringBuilder sbRsMapValue = new StringBuilder();
        String lastKey = "";
        for (int x=0; x<ret.length; x++) {
            String[] thisArr = ret[x];
            String orderNo = thisArr[0].trim();
            String docNo = thisArr[1].trim();
            String thisRsMapKey = orderNo + docNo;
            boolean isFirst = true;
            if ( thisRsMapKey.equals(lastKey) ) {
                isFirst = false;
                sbRsMapValue.append("\n");
            } else {
                rsMap.put(thisRsMapKey , sbRsMapValue.toString() );    
                sbRsMapValue.delete(0, sbRsMapValue.length() );
            }
            sbRsMapValue.append( thisArr[2].trim() );

            lastKey = thisRsMapKey;
        }
        
        return rsMap;
    }

    //查詢指定第三人
    public Map getSale05M356_Rpt3(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select distinct T275.orderNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.DesignatedName FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS CustomName ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.DesignatedId FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS CustomNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.Relation FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS Relation ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.ExportingPlace FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS ExportingPlace ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.Controllist FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS Controllist ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.Blacklist FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS Blacklist ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.Stakeholder FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS Stakeholder ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T356B.Reason FROM Sale05M356 T356B WHERE T356.contractNo = T356B.contractNo FOR XML PATH('')), 1, 3, '') AS Reason ");
        sbSQL.append("from Sale05M356 T356 , Sale05M274 T274 , Sale05M275_new T275 ");
        sbSQL.append("where T356.contractNO = T274.contractNo and T356.contractNO = T275.contractNo and T274.contractNo = T275.contractNo and T274.isVoid = 'N' and LEN(DesignatedName) >0 ");
        sbSQL.append("and T275.orderNo in ("+ orderNos +") ");
        sbSQL.append("order by T275.orderNo DESC ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        //格式: Map(orderNo , Map(key , value))
        for (int x=0; x<ret.length; x++) {
            String[] thisArr = ret[x];
            String thisRsMapKey = thisArr[0].trim();
            Map mapLv2 = new HashMap();
            mapLv2.put("CustomName" , thisArr[1].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("CustomNo" , thisArr[2].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("Relation" , thisArr[3].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("ExportingPlace" , thisArr[4].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsControlList" , thisArr[5].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsBlackList" , thisArr[6].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsLinked" , thisArr[7].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("Reason" , thisArr[8].trim().replaceAll("Q_Q" , "\n"));
            rsMap.put(thisRsMapKey , mapLv2);
        }
        
        return rsMap;
    }

    //查詢代理人2
    public Map getAgent2_Rpt3(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("SELECT distinct orderNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.AgentName FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS AgentNamers ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.ACustomNo FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS ACustomNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.AgentRel FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS Relation ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.IsControlList FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS IsControlList ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.IsBlackList FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS IsBlackList ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.IsLinked FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS IsLinked ");
        sbSQL.append(",'Y' as type1 ");
        sbSQL.append(",'-' as type2 ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + agB.AgentReason FROM Sale05M091Agent agB WHERE agB.orderNo = agA.orderNo FOR XML PATH('')), 1, 3, '') AS Reason ");
        sbSQL.append("FROM Sale05M091Agent agA ");
        sbSQL.append("union ");
        sbSQL.append("select distinct T275.orderNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.TrusteeName FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS AgentNamers ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.TrusteeId FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS ACustomNo ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.Relation FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS Relation ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.Controllist FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS IsControlList ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.Blacklist FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS IsBlackList ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.Stakeholder FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS IsLinked ");
        sbSQL.append(",'-' as type1 ");
        sbSQL.append(",'Y' as type2 ");
        sbSQL.append(",STUFF((SELECT 'Q_Q' + T355B.Reason FROM Sale05M355 T355B WHERE T355.contractNo = T355B.contractNo FOR XML PATH('')), 1, 3, '') AS Reason ");
        sbSQL.append("from Sale05M355 T355 , Sale05M274 T274 , Sale05M275_new T275 where T355.contractNO = T274.contractNo and T355.contractNO = T275.contractNo and T274.contractNo = T275.contractNo and T274.isVoid = 'N' and LEN(TrusteeName) >0 ");
        sbSQL.append("order by orderNo desc ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        //格式: Map(orderNo , Map(key , value))
        for (int x=0; x<ret.length; x++) {
            String[] thisArr = ret[x];
            String thisRsMapKey = thisArr[0].trim();
            Map mapLv2 = new HashMap();
            mapLv2.put("CustomName" , thisArr[1].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("CustomNo" , thisArr[2].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("Relation" , thisArr[3].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsControlList" , thisArr[4].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsBlackList" , thisArr[5].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("IsLinked" , thisArr[6].trim().replaceAll("Q_Q" , "\n"));
            mapLv2.put("type1" , thisArr[7].trim());
            mapLv2.put("type2" , thisArr[8].trim());
            mapLv2.put("Reason" , thisArr[9].trim().replaceAll("Q_Q" , "\n"));
            rsMap.put(thisRsMapKey , mapLv2);
        }
        
        return rsMap;
    }

    //查詢代繳人
    public Map getDeputy(String orderNos) throws Throwable {
        String sql = "" 
        +"select T86.orderNo, T80.docNo, DeputyName , DeputyID , DeputyRelationship, Reason, C_STATUS, B_STATUS, R_STATUS, T80.CashMoney from Sale05M080 T80 , Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo in ("+ orderNos +") and PaymentDeputy = 'Y' and LEN(DeputyName)>0 and LEN(DeputyID)>0 and LEN(DeputyRelationship)>0 "
        + "union "
        + "select T86.orderNo, T80.docNo, DeputyName , DeputyID , DeputyRelationship, Reason, C_STATUS, B_STATUS, R_STATUS, T80.CheckMoney from Sale05M082 T80 , Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo in ("+ orderNos +") and PaymentDeputy = 'Y' and LEN(DeputyName)>0 and LEN(DeputyID)>0 and LEN(DeputyRelationship)>0  "
        + "union "
        + "select T86.orderNo, T80.docNo, DeputyName , DeputyID , DeputyRelationship, Reason, C_STATUS, B_STATUS, R_STATUS, T80.CreditCardMoney from Sale05M083 T80 , Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo in ("+ orderNos +") and PaymentDeputy = 'Y' and LEN(DeputyName)>0 and LEN(DeputyID)>0 and LEN(DeputyRelationship)>0 "
        + "union "
        + "select T86.orderNo, T80.docNo, DeputyName , DeputyID , DeputyRelationship, Reason, C_STATUS, B_STATUS, R_STATUS, T80.BankMoney from Sale05M328 T80 , Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo in ("+ orderNos +") and PaymentDeputy = 'Y' and LEN(DeputyName)>0 and LEN(DeputyID)>0 and LEN(DeputyRelationship)>0 ";
        String[][] ret = dbSale.queryFromPool(sql);
        Map rsMap = new HashMap();

        // 格式:  Map( docNo , Map() )
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];  //DB來源 本筆array
            String thisNo = thisArr[1].trim(); 

            String[] tmpArrayLv2 = new String[thisArr.length];
            if ( !rsMap.containsKey( thisNo ) ) {
                tmpArrayLv2 = thisArr;
            } else {
                Map tmpMap2Arr = (Map)rsMap.get( thisNo );
                tmpArrayLv2[2] = tmpMap2Arr.get("DeputyName") + "\n" + thisArr[2].trim();
                tmpArrayLv2[3] = tmpMap2Arr.get("DeputyID") + "\n" + thisArr[3].trim();
                tmpArrayLv2[4] = tmpMap2Arr.get("DeputyRelationship") + "\n" + thisArr[4].trim();
                tmpArrayLv2[5] = tmpMap2Arr.get("Reason") + "\n" + thisArr[5].trim();
                tmpArrayLv2[6] = tmpMap2Arr.get("C_STATUS") + "\n" + thisArr[6].trim();
                tmpArrayLv2[7] = tmpMap2Arr.get("B_STATUS") + "\n" + thisArr[7].trim();
                tmpArrayLv2[8] = tmpMap2Arr.get("R_STATUS") + "\n" + thisArr[8].trim();
                tmpArrayLv2[9] = tmpMap2Arr.get("Money") + "\n" + thisArr[9].trim();
            }
            Map mapLv2 = new HashMap();
            mapLv2.put( "DeputyName" , tmpArrayLv2[2].trim() );
            mapLv2.put( "DeputyID" , tmpArrayLv2[3].trim() );
            mapLv2.put( "DeputyRelationship" , tmpArrayLv2[4].trim() );
            mapLv2.put( "Reason" , tmpArrayLv2[5].trim() );
            mapLv2.put( "C_STATUS" , tmpArrayLv2[6].trim() );
            mapLv2.put( "B_STATUS" , tmpArrayLv2[7].trim() );
            mapLv2.put( "R_STATUS" , tmpArrayLv2[8].trim() );
            mapLv2.put( "Money" , tmpArrayLv2[9].trim() );
            rsMap.put( thisNo , mapLv2 );
        }
        return rsMap;
    }

    public Map getSale05M080_Rpt3(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select T86.orderNo, T80.DocNo, T80.EDate, ReceiveMoney ");
        sbSQL.append(", ISNULL(CashMoney, '0') , ISNULL(CreditCardMoney, '0') ,ISNULL(CheckMoney, '0'), ISNULL(BankMoney, '0') ");
        sbSQL.append("from Sale05M080 T80, Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo  in ("+ orderNos +") ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();

        // 格式:  Map( orderNo , List[Map(一筆收款)] )
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];  //DB來源 本筆array
            String thisNo = thisArr[0].trim(); //本訂單編號

            List tmpRsList = new ArrayList();   //用List為同一訂單收款單集合
            if ( rsMap.containsKey( thisNo ) ) {
                tmpRsList = (List)rsMap.get( thisNo );
            }
            Map inListMap = new HashMap();
            inListMap.put( "DocNo" , thisArr[1].trim() );
            inListMap.put( "EDate" , thisArr[2].trim() );
            inListMap.put( "ReceiveMoney" , thisArr[3].trim() );
            inListMap.put( "CashMoney" , thisArr[4].trim() );
            inListMap.put( "CreditCardMoney" , thisArr[5].trim() );
            inListMap.put( "CheckMoney" , thisArr[6].trim() );
            inListMap.put( "BankMoney" , thisArr[7].trim() );
            tmpRsList.add(inListMap);
            rsMap.put( thisNo , tmpRsList );
        }
        return rsMap;
    }

    public Map getSale05M070(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select orderNo , ");
        sbSQL.append("Count(case when (SHB06B='021') then 1 else null end) as C021");
        sbSQL.append(", Count(case when (SHB06B='018') then 1 else null end) as C018 ");
        sbSQL.append(", Count(case when (SHB06B='017') then 1 else null end) as C017 ");
        sbSQL.append(", Count(case when (SHB06B='009') then 1 else null end) as C009 ");
        sbSQL.append(", Count(case when (SHB06B='011') then 1 else null end) as C011 ");
        sbSQL.append(", Count(case when (SHB06B='008') then 1 else null end) as C008 ");
        sbSQL.append(", Count(case when (SHB06B='004') then 1 else null end) as C004 ");
        sbSQL.append(", Count(case when (SHB06B='002') then 1 else null end) as C002 ");
        sbSQL.append(", Count(case when (SHB06B='013') then 1 else null end) as C013 ");
        sbSQL.append(", Count(case when (SHB06B='010') then 1 else null end) as C010 ");
        sbSQL.append(", Count(case when (SHB06B='012') then 1 else null end) as C012 ");
        sbSQL.append(", Count(case when (SHB06B='006') then 1 else null end) as C006 ");
        sbSQL.append(", Count(case when (SHB06B='015') then 1 else null end) as C015 ");
        sbSQL.append(", Count(case when (SHB06B='016') then 1 else null end) as C016 ");
        sbSQL.append("from Sale05M070 where orderNo in ("+ orderNos +") and RecordDesc not in ('不符合', '不適用') ");
        sbSQL.append("group by orderNo ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();
        int docCount = 0;
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];  //DB來 本筆ARR
            String thisOrderNo = thisArr[0].trim(); //本訂單編號
            String[] tmpRsArr = new String[thisArr.length]; //新
            if ( !rsMap.containsKey( thisOrderNo ) ) {
                tmpRsArr = thisArr;
            }
            Map mapVal = new HashMap();
            mapVal.put( "C021" , tmpRsArr[1].trim() );
            mapVal.put( "C018" ,  tmpRsArr[2].trim() );
            mapVal.put( "C017" , tmpRsArr[3].trim() );
            mapVal.put( "C009" , tmpRsArr[4].trim() );
            mapVal.put( "C011" , tmpRsArr[5].trim() );
            mapVal.put( "C008" , tmpRsArr[6].trim() );
            mapVal.put( "C004" , tmpRsArr[7].trim() );
            mapVal.put( "C002" , tmpRsArr[8].trim() );
            mapVal.put( "C013" , tmpRsArr[9].trim() );
            mapVal.put( "C010" , tmpRsArr[10].trim() );
            mapVal.put( "C012" , tmpRsArr[11].trim() );
            mapVal.put( "C006" , tmpRsArr[12].trim() );
            mapVal.put( "C015" , tmpRsArr[13].trim() );
            mapVal.put( "C016" , tmpRsArr[14].trim() );
            rsMap.put( thisOrderNo , mapVal );
        }
        return rsMap;
    }

    public Map getSale05M080(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select ");
        sbSQL.append("T86.orderNo, ISNULL((select sum(CheckMoney) from Sale05M082 T82 where T82.DocNo = T80.DocNo) , '0') as billMoney ");
        sbSQL.append(", ISNULL(CashMoney, '0') , ISNULL(CreditCardMoney, '0') , ISNULL(BankMoney, '0') ");
        sbSQL.append(",RTRIM(ISNULL((select top 1 T328.ExportingPlace from Sale05M328 T328 where T328.DocNo = T80.DocNo), '')) as BankExportingPlace ");
        sbSQL.append("from Sale05M080 T80, Sale05M086 T86 where T80.DocNo = T86.DocNo and T86.orderNo  in ("+ orderNos +") ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();
        int docCount = 0;
        for (int x = 0 ; x < ret.length ; x++) {
            docCount++;
            String[] thisArr = ret[x];  //DB來 本筆ARR
            String thisOrderNo = thisArr[0].trim(); //本訂單編號
            
            String[] tmpRsArr = new String[thisArr.length];
            if ( !rsMap.containsKey( thisOrderNo ) ) {
                tmpRsArr = thisArr;
            } else {
                Map mapTmpVal = (Map)rsMap.get( thisOrderNo );  //組成後MAP，本筆暫存
                tmpRsArr[0] = thisOrderNo;
                tmpRsArr[1] = Double.toString(Double.parseDouble( mapTmpVal.get("billMoney").toString() ) + Double.parseDouble( thisArr[1].trim() ));
                tmpRsArr[2] = Double.toString(Double.parseDouble( mapTmpVal.get("cashMoney").toString() ) + Double.parseDouble( thisArr[2].trim() ));
                tmpRsArr[3] = Double.toString(Double.parseDouble( mapTmpVal.get("creditCardMoney").toString() ) + Double.parseDouble( thisArr[3].trim() ));
                tmpRsArr[4] = Double.toString(Double.parseDouble( mapTmpVal.get("bankMoney").toString() ) + Double.parseDouble( thisArr[4].trim() ));
                tmpRsArr[5] = mapTmpVal.get("bankExportingPlace") + "\n" + thisArr[5].trim();
            }
            Map mapVal = new HashMap();
            mapVal.put( "orderNo" , tmpRsArr[0].trim() );
            mapVal.put( "billMoney" ,  tmpRsArr[1].trim() );
            mapVal.put( "cashMoney" , tmpRsArr[2].trim() );
            mapVal.put( "creditCardMoney" , tmpRsArr[3].trim() );
            mapVal.put( "bankMoney" , tmpRsArr[4].trim() );
            mapVal.put( "bankExportingPlace" , tmpRsArr[5].trim() );
            mapVal.put( "docCount" , Integer.toString(docCount) );
            rsMap.put( thisOrderNo , mapVal );
        }
        return rsMap;
    }

    //取得合約棟樓資料
    public Map getSale05M278(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select ");
        sbSQL.append("T278.orderno, T274.contractDate , sum(T278.PureMoney) , sum(T278.BalaMoney) ");
        sbSQL.append("from Sale05M278 T278 , Sale05M274 T274 where T274.contractNo = T278.contractNo and T274.isVoid = 'N' ");
        sbSQL.append("and T278.orderNo in ("+ orderNos +") ");
        sbSQL.append("group by T278.orderno, T274.contractDate ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];
            String thisOrderNo = thisArr[0].trim();
            Map mapLv2 = new HashMap();
            mapLv2.put("ContractDate" , thisArr[1].trim());
            mapLv2.put("PureMoney" , thisArr[2].trim());
            mapLv2.put("BalaMoney" , thisArr[3].trim());
            rsMap.put( thisOrderNo , mapLv2 );
        }
        return rsMap;
    }

    public Map getSale05M091(String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select ");
        sbSQL.append("T091.orderNo , T091.customName , T091.customNo , RTRIM(T091.CountryName) ");
        sbSQL.append(", T091.City  , T091.Town , T091.Birthday , T091.MajorName , T091.PositionName , RTRIM(T091.riskValue) ");
        sbSQL.append(", T091.IsControlList , T091.IsBlackList , T091.IsLinked ");
        sbSQL.append("from Sale05M091 T091 where 1=1 ");
        sbSQL.append("and T091.orderNo in ("+ orderNos +") ");
        sbSQL.append("and ( T091.StatusCd != 'C' or T091.StatusCd is null )  ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];
            String thisOrderNo = thisArr[0].trim();
            String[] mapArr = (String[])rsMap.get( thisOrderNo );
            
            if ( !rsMap.containsKey( thisOrderNo ) ) {
                rsMap.put( thisOrderNo , thisArr );
            } else {
                String[] val = new String[thisArr.length];
                val[0] = thisOrderNo;   //單號不變
                val[1] = mapArr[1].trim() + "\n" + thisArr[1].trim();
                val[2] = mapArr[2].trim() + "\n" + thisArr[2].trim();
                val[3] = mapArr[3].trim() + "\n" + thisArr[3].trim();
                val[4] = mapArr[4].trim() + "\n" + thisArr[4].trim();
                val[5] = mapArr[5].trim() + "\n" + thisArr[5].trim();
                val[6] = mapArr[6].trim() + "\n" + thisArr[6].trim();
                val[7] = mapArr[7].trim() + "\n" + thisArr[7].trim();
                val[8] = mapArr[8].trim() + "\n" + thisArr[8].trim();
                val[9] = mapArr[9].trim() + "\n" + thisArr[9].trim();
                val[10] = mapArr[10].trim() + "\n" + thisArr[10].trim();
                val[11] = mapArr[11].trim() + "\n" + thisArr[11].trim();
                val[12] = mapArr[12].trim() + "\n" + thisArr[12].trim();
                rsMap.put( thisOrderNo , val );
            }
        }
        return rsMap;
    }

    public Map getSale05M091Ben (String orderNos) throws Throwable {
        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select ");
        sbSQL.append("T091B.orderNo , (select BName from A_holding Thold where Thold.BType = T091B.HoldType) , T091B.BenName , T091B.BCustomNo , T091B.CountryName , T091B.Birthday ");
        sbSQL.append(", T091B.IsControlList , T091B.IsBlackList , T091B.IsLinked ");
        sbSQL.append("from Sale05M091Ben T091B where 1=1 ");
        sbSQL.append("and T091B.orderNo in ("+ orderNos +") ");
        String[][] ret = dbSale.queryFromPool( sbSQL.toString() );
        Map rsMap = new HashMap();
        for (int x = 0 ; x < ret.length ; x++) {
            String[] thisArr = ret[x];
            String thisOrderNo = thisArr[0].trim();
            String[] mapArr = (String[])rsMap.get( thisOrderNo );
            
            if ( !rsMap.containsKey( thisOrderNo ) ) {
                rsMap.put( thisOrderNo , thisArr );
            } else {
                String[] val = new String[thisArr.length];
                val[0] = thisOrderNo;   //單號不變
                val[1] = mapArr[1].trim() + "\n" + thisArr[1].trim();
                val[2] = mapArr[2].trim() + "\n" + thisArr[2].trim();
                val[3] = mapArr[3].trim() + "\n" + thisArr[3].trim();
                val[4] = mapArr[4].trim() + "\n" + thisArr[4].trim();
                val[5] = mapArr[5].trim() + "\n" + thisArr[5].trim();
                val[6] = mapArr[6].trim() + "\n" + thisArr[6].trim();
                val[7] = mapArr[7].trim() + "\n" + thisArr[7].trim();
                val[8] = mapArr[8].trim() + "\n" + thisArr[8].trim();
                rsMap.put( thisOrderNo , val );
            }
        }
        return rsMap;
    }

  public String getInformation(){
    return "---------------button4(queryUtil).defaultValue()----------------";
  }
}
