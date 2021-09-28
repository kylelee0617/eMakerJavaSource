package Farglory.util ;
import  java.sql.* ;
import  java.util.*;
import  jcx.util.*;
import  org.sqlite.JDBC ;
import  Farglory.util.FargloryUtil ;
public class  SqliteDB {
    Connection  conn                 = null ; 
    Statement   stmt                 = null ;
    Hashtable   hashtableColumnsData =  new  Hashtable() ;
    Vector      vectorTxtType        =  new  Vector() ;
    public  SqliteDB(String  stringFileName) throws  Throwable {
        setTalk(stringFileName) ;
    }
    public  boolean  setTalk(String  stringFileName) throws  Throwable {
          closeCon( ) ;
          try {
              Class.forName("org.sqlite.JDBC");
              conn  =  DriverManager.getConnection("jdbc:sqlite:"+stringFileName);
              stmt  =  conn.createStatement();
              
          } catch (Throwable e) {
              System.out.println(""+e.toString());
              return  false ;
          }
          return  true ;
    }
    public  void  closeCon( ) throws  Throwable {
        if(stmt  !=  null)         stmt.close() ;
        if(conn  !=  null)         conn.close() ;
        stmt  =  null ;
        conn  =  null ;
    }
    public  boolean  exeFromPool(String  stringSql) throws  Throwable {
        if(stmt  ==  null)  return  false ;
        try {
            stmt.executeUpdate(stringSql);  
        } catch (Throwable e) {
              System.out.println(""+e.toString());
              return  false ;
        }
        return  true ;
    }
    public  String[][]  getColumnsData(String  stringTable,  ResultSet  rs) throws  Throwable {
        Vector  vectorTableColumns  =  new  Vector() ;  if(stmt  ==  null)  return  (new  String[0][0]) ;
        //
        if(stringTable.toUpperCase().indexOf("SELECT")==-1)  stringTable  =  " SELECT  *  FROM  "+stringTable ;
        try {
            if(rs==null) rs  = stmt.executeQuery(stringTable);  
            
            ResultSetMetaData rsmd               = rs.getMetaData();
            int               intNumberOfColumns = rsmd.getColumnCount();
            String            stringColumnType   =  "" ;
            String            stringColumnName   =  "" ;
            String[]          arrayTemp          =  null ;
            //
            for(int  intNo=0  ;  intNo<intNumberOfColumns  ;  intNo++) {
                stringColumnName   =  rsmd.getColumnName(intNo+1).trim();
                stringColumnType   =  rsmd.getColumnTypeName(intNo+1).trim();
                //
                arrayTemp    =  new  String[2] ;
                arrayTemp[0] = stringColumnName ;
                arrayTemp[1] = stringColumnType ;
                //System.out.println("stringColumnName("+stringColumnName+")stringColumnType("+stringColumnType+")---------------------------") ;
                //
                vectorTableColumns.add(arrayTemp) ;
            }
            rs  =  null ;
        } catch (Throwable e) {
              System.out.println(""+e.toString());
              return  (String[][])vectorTableColumns.toArray(new  String[0][0]) ;
        }
        return  (String[][])vectorTableColumns.toArray(new  String[0][0]) ;
    }
    public  String[][]  getQueryData(String  stringSql) throws  Throwable {
        Vector  vectorTableData  =  new  Vector() ;  if(stmt  ==  null)  return  (new  String[0][0]) ;
        try {
            ResultSet         rs                 = stmt.executeQuery(stringSql);  
            ResultSetMetaData rsmd               = rs.getMetaData();
            int               intNumberOfColumns = rsmd.getColumnCount();
            String            stringColumnName   =  "" ;
            String            stringColumnValue  =  "" ;
            String[]          arrayTemp          =  null ;
            //
            boolean  booleanFirst  =  true ;
            while (rs.next()) {
                if(booleanFirst  &&  !rs.isFirst()) return new  String[0][0] ;
                //
                arrayTemp  =  new  String[intNumberOfColumns] ;
                for(int  intNo=0  ;  intNo<intNumberOfColumns  ;  intNo++) {
                    stringColumnName   =  rsmd.getColumnName(intNo+1);
                    stringColumnValue  =  rs.getString(stringColumnName) ; 
                    //
                    arrayTemp[intNo] = stringColumnValue ;
                }
                vectorTableData.add(arrayTemp) ;
            }
            rs  =  null ;
        } catch (Throwable e) {
              System.out.println(""+e.toString());
              return  (String[][])vectorTableData.toArray(new  String[0][0]) ;
        }
        return  (String[][])vectorTableData.toArray(new  String[0][0]) ;
    }
		public  String  getNameUnion(String  stringFieldName,  String  stringTableName,  String  stringSqlAnd,  Hashtable  hashtableAnd)  throws Throwable{
				String      stringSql         =  "" ;
				String      stringName        =  "" ;
				Vector      vectorColumnName  =  new  Vector() ;
				//
				Vector  vectorTableData  =  getQueryDataHashtable(stringTableName,  hashtableAnd,  stringSqlAnd,  vectorColumnName) ;
				if(vectorTableData.size()  !=  0) {
						Hashtable  hashtableTemp  =  (Hashtable) vectorTableData.get(0) ;
						if(hashtableTemp  !=  null) {
								stringName  = (""+hashtableTemp.get(stringFieldName)).trim( ) ;
								if("null".equals(stringName))  stringName  =  "" ;
						}
				}
				return  stringName ;
		}
    public  Vector  getQuerySumData(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSelectSql,  String  stringSqlAnd) throws  Throwable {
        Vector      vectorTableData   =  new  Vector() ; 
        String      stringWhereSql    =  "" ;
        String      stringColumnName  =  "" ;
        String      stringColumnType  =  "" ;
        String      stringColumnValue =  "" ;
        String      stringTableNameT  =  convert.StringToken(stringTableName,  "%-%")[0] ; // 避免不同資料庫，相同表格名稱時，[表格]%-%[資料庫]
        String[][]  retColumnsData    =  null ;
        retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
        if(retColumnsData  ==  null) {
            retColumnsData        =  getColumnsData(stringTableNameT,  null) ;
            hashtableColumnsData.put(stringTableName,  retColumnsData) ;
        }
        // SQL 語法產生
        for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
            	stringColumnName   =  retColumnsData[intNo][0].trim() ;
            	stringColumnType   =  retColumnsData[intNo][1].trim() ;
              // AND
              stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
              //
              if(isNum(stringColumnType)  ||  "null".equals(stringColumnValue)) {
                  // 數值 
                  stringWhereSql  +=  " AND  "+stringColumnName+" =  "+stringColumnValue+" \n" ;
              } else {
                  // 文字
                  stringWhereSql  +=  " AND  "+stringColumnName+" =  '"+stringColumnValue+"' \n" ;
              }
              // 清空
              hashtableAnd.remove(stringColumnName) ;
        }
        //
        String  stringSql  =  " SELECT "+stringSelectSql  +  " \n" +
                                " FROM  "  +  stringTableNameT  +  " \n" +
                               " WHERE  1 =  1 \n" +
                               stringWhereSql +
                               stringSqlAnd ;
        System.out.println("SQL------------------------\n"+stringSql+"") ;
        // 資料取得                      
        boolean     booleanFlag  =  !"".equals(stringSelectSql) &&  
                                    !"".equals(stringTableNameT) &&  
                                    (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
        if(!booleanFlag)  return  new  Vector() ;
        // 資料整理
                  stringSelectSql  =  stringSelectSql.replaceAll(" as ",  " ") ;
                  stringSelectSql  =  stringSelectSql.replaceAll(" As ",  " ") ;
                  stringSelectSql  =  stringSelectSql.replaceAll(" aS ",  " ") ;
                  stringSelectSql  =  stringSelectSql.replaceAll(" AS ",  " ") ;
        int       intPosS          =  0 ;
        int       intPosE          =  0 ;
        String[]  arrayFieldName   =  convert.StringToken(stringSelectSql,  ",") ;
        String[]  arrayFieldNameL  =  null ;
        for(int  intNo=0  ;  intNo<arrayFieldName.length  ;  intNo++) {
            stringColumnName  =  arrayFieldName[intNo].trim() ;
            //
            arrayFieldNameL  =  convert.StringToken(stringColumnName,  " ") ;
            if(arrayFieldNameL.length  >  1) {
                 stringColumnName       =  arrayFieldNameL[arrayFieldNameL.length-1] ;
            } else {
                 intPosS  =  stringColumnName.indexOf("(") ;
                 intPosE  =  stringColumnName.indexOf(")") ;
                 if(intPosS!=-1  &&  intPosE!=-1) {
                    stringColumnName  =  doSubstring(stringColumnName,  intPosS+1,  intPosE) ;
                }
            }
            arrayFieldName[intNo]  =  stringColumnName ;
        }
        try {
            int               intCount              = 0 ;
            ResultSet         rs                    = stmt.executeQuery(stringSql);  
            ResultSetMetaData rsmd                  = rs.getMetaData();
            int               intNumberOfColumns    = rsmd.getColumnCount();
            Hashtable         hashtableTemp         =  new  Hashtable() ;
            String            stringColumnNameShow  =  "" ;
            //
            while (rs.next()) {
                //
                hashtableTemp      =  new  Hashtable() ;
                for(int  intNo=0  ;  intNo<intNumberOfColumns  ;  intNo++) {
                    if(intNo<arrayFieldName.length) {
                        stringColumnNameShow   =  arrayFieldName[intNo].trim();
                    }
                    stringColumnName   =  rsmd.getColumnName(intNo+1).trim();
                    //
                    stringColumnValue  =  rs.getString(stringColumnName) ; 
                    //
                    hashtableTemp.put(stringColumnNameShow,  stringColumnValue) ;
                }
                vectorTableData.add(hashtableTemp) ;
                intCount++ ;
            }
            rs  =  null ;
            System.out.println("筆數：("+intCount+")----------------") ;
        } catch (Throwable e) {
              System.out.println("ERROR(getQuerySumData)--\n"+e.toString());
              return  vectorTableData ;
        }
        return  vectorTableData ;
    }
    public  Vector  getQueryDataHashtable(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  Vector  vectorColumnName) throws  Throwable {
        Vector      vectorTableData   =  new  Vector() ;  if(stmt  ==  null)  return  vectorTableData ;
        String      stringTableNameT  =  convert.StringToken(stringTableName,  "%-%")[0] ;  // 避免不同資料庫，相同表格名稱時，[表格]%-%[資料庫]
        String      stringColumnName  =  "" ;
        String      stringColumnType  =  "" ;
        String      stringColumnValue =  "" ;
        String      stringSelectSql   =  "" ;
        String      stringWhereSql    =  "" ;
        String[][]  retColumnsData    =  null ;
        // SQL
        retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
        if(retColumnsData  ==  null) {
            retColumnsData        =  getColumnsData(stringTableNameT,  null) ;
            hashtableColumnsData.put(stringTableName,  retColumnsData) ;
        }
        for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
            	stringColumnName   =  retColumnsData[intNo][0].trim() ;
            	stringColumnType   =  retColumnsData[intNo][1].trim() ;
            	//
            	vectorColumnName.add(stringColumnName) ;
            	// SELECT
            	if(!"".equals(stringSelectSql))  stringSelectSql  +=  ", " ;
              stringSelectSql += stringColumnName  ;	
              // AND
              stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
              //
              if("null".equals(stringColumnValue)) {
                  //
              } else if(isNum(stringColumnType)) {
                  // 數值 
                  if(doParseDouble(stringColumnValue)  ==  0)  stringColumnValue  =  "0" ;
                  stringWhereSql  +=  " AND  "+stringColumnName+" =  "+stringColumnValue+" \n" ;
              } else {
                  // 文字
                  stringWhereSql  +=  " AND  "+stringColumnName+" =  '"+stringColumnValue+"' \n" ;
              }
              // 清空
              hashtableAnd.remove(stringColumnName) ;
        }
        String  stringSql  =  " SELECT  "  +  stringSelectSql  +  " \n" +
                                " FROM  "  +  stringTableNameT  +  " \n" +
                               " WHERE  1 =  1 \n" +
                               stringWhereSql +
                               stringSqlAnd ;
        System.out.println("SQL------------------------\n"+stringSql) ;
        boolean     booleanFlag  =  !"".equals(stringSelectSql) &&  
                                    !"".equals(stringTableNameT) &&  
                                    (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
        if(!booleanFlag)  return  new  Vector() ;
        //
        try {
            ResultSet         rs                 = stmt.executeQuery(stringSql);  
            int               intCount           =  0 ;
            int               intNumberOfColumns = retColumnsData.length;
            Hashtable         hashtableTemp      =  new  Hashtable() ;
            //
            while (rs.next()) {
                //
                hashtableTemp      =  new  Hashtable() ;
                for(int  intNo=0  ;  intNo<intNumberOfColumns  ;  intNo++) {
                    stringColumnName   =  retColumnsData[intNo][0];
                    stringColumnValue  =  rs.getString(stringColumnName) ; 
                    //
                    hashtableTemp.put(stringColumnName,  stringColumnValue) ;
                }
                vectorTableData.add(hashtableTemp) ;
                intCount++ ;
            }
            rs  =  null ;
            System.out.println("筆數：("+intCount+")----------------") ;
        } catch (Throwable e) {
              System.out.println(""+e.toString());
              return  vectorTableData ;
        }
        return  vectorTableData ;
    }
    public  String  doInsertDB(String  stringTableName,  Hashtable  hashtableData,  boolean  booleanDB) throws  Throwable {
        String   stringSql    =  "" ;
        boolean  booleanFlag  =  true ;
        if(stmt  ==  null)  return  "" ;
        //
        try {
            String      stringSelectSql       =  "" ;
            String      stringValueSql        =  "" ;
            String      stringColumnName      =  "" ;
            String      stringColumnType      =  "" ;
            String      stringColumnValue     =  "" ;
            String      stringTableNameT      =  convert.StringToken(stringTableName,  "%-%")[0] ; // 避免不同資料庫，相同表格名稱時，[表格]%-%[資料庫]
            String[][]  retColumnsData        =  null ;
            retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
            if(retColumnsData  ==  null) {
                retColumnsData        =  getColumnsData(stringTableNameT,  null) ;
                hashtableColumnsData.put(stringTableName,  retColumnsData) ;
            }
            for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                	stringColumnName   =  retColumnsData[intNo][0].trim() ;
                	stringColumnType   =  retColumnsData[intNo][1].trim() ;
                	stringColumnValue  =  ""+hashtableData.get(stringColumnName) ;  
                	//
                	if(!"".equals(stringSelectSql))  stringSelectSql  +=  ", " ;
	                stringSelectSql += stringColumnName  ;	
	                //
	                if("null".equals(stringColumnValue))  stringColumnValue  =  "" ;
	                if(isNum(stringColumnType)) {
                      // 數值 
                      if(doParseDouble(stringColumnValue)  ==  0)  stringColumnValue  =  "0" ;
                      //
                      if(!"".equals(stringValueSql))  stringValueSql  +=  ", " ;
                      stringValueSql  +=  " "+stringColumnValue+" " ;
                  } else {
                      // 文字
                      if(!"".equals(stringValueSql))  stringValueSql  +=  ", " ;
                      stringValueSql  +=  " '"+stringColumnValue+"' " ;
                  }
                  hashtableData.remove(stringColumnName) ;
            }
            stringSql    = "INSERT  INTO "  +  stringTableNameT  +  " ( "+stringSelectSql+") "+
                        	                                       " VALUES  ( "+stringValueSql+" ) " ;
            System.out.println("SQL------------------------\n"+stringSql) ;
            booleanFlag  =  booleanDB  &&  
                            !"".equals(stringTableNameT)     &&  
                            !"".equals(stringSelectSql)  &&  
                            !"".equals(stringValueSql) ;
            if(booleanFlag) {
                exeFromPool(stringSql) ;
                System.out.println("SQL 異動------------------------") ;
            }

        } catch (Throwable e) {
              return  ""+e.toString() ;
        }

        return  stringSql ;
    }
    // 刪除
    public  String  doDeleteDB(String  stringTableName,  Hashtable  hashtableAnd,  String  stringSqlAnd,  boolean  booleanDB) throws  Throwable {
        String      stringWhereSql    =  "" ;
        String      stringColumnName  =  "" ;
        String      stringColumnType  =  "" ;
        String      stringColumnValue =  "" ;
        String      stringTableNameT  =  convert.StringToken(stringTableName,  "%-%")[0] ; // 避免不同資料庫，相同表格名稱時，[表格]%-%[資料庫]
        String[][]  retColumnsData    =  null ;
        //
        retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
        if(retColumnsData  ==  null) {
            retColumnsData        =  getColumnsData(stringTableNameT,  null) ;
            hashtableColumnsData.put(stringTableName,  retColumnsData) ;
        }
        // SQL 語法產生
        for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
            	stringColumnName   =  retColumnsData[intNo][0].trim() ;
            	stringColumnType   =  retColumnsData[intNo][1].trim() ;
              // AND
              stringColumnValue  =  (String) hashtableAnd.get(stringColumnName) ;  if(stringColumnValue  ==  null)  continue ;
              if(isNum(stringColumnType)  ||  "null".equals(stringColumnValue)) {
                  // 數值 
                  stringWhereSql  +=  "AND  "+stringColumnName+" =  "+stringColumnValue+" \n" ;
              } else {
                  // 文字
                  stringWhereSql  +=  "AND  "+stringColumnName+" =  '"+stringColumnValue+"' \n" ;
              }
              // 清除
              hashtableAnd.remove(stringColumnName) ;
        }
        //
        String  stringSql  =  "DELETE  FROM  "  +  stringTableNameT  +  " \n" +
                               "WHERE  1 =  1 \n" +
                               stringWhereSql +
                               stringSqlAnd ;
        System.out.println("SQL------------------------\n"+stringSql) ;
        //           
        boolean     booleanFlag  =  booleanDB  &&  
                                    !"".equals(stringTableNameT)      &&  
                                    (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;         
        if(booleanFlag) {
           exeFromPool(stringSql) ;
           System.out.println("SQL 異動------------------------") ;
        }
        return  stringSql ;
    }
    // 更新
    public  String  doUpdateDB(String  stringTableName,  String  stringSqlAnd,  Hashtable  hashtableData,  Hashtable  hashtableAnd,  boolean  booleanDB) throws  Throwable {
        if(stmt  ==  null)  return  "" ;
        //
        Vector      vectorTableData       =  new  Vector() ; 
        String      stringSql             =  "" ;
        String      stringUpdateDataSql   =  "" ;
        String      stringWhereSql        =  "" ;
        String      stringColumnName      =  "" ;
        String      stringColumnType      =  "" ;
        String      stringColumnValue1    =  "" ;
        String      stringColumnValue2    =  "" ;
        String      stringTableNameT      =  convert.StringToken(stringTableName,  "%-%")[0] ; // 避免不同資料庫，相同表格名稱時，[表格]%-%[資料庫]
        String[][]  retColumnsData        =  null ; 
        String      stringSign            =  "" ;
        boolean     booleanNum            =  false ;
        //
        retColumnsData  =  (String[][]) hashtableColumnsData.get(stringTableName) ;
        if(retColumnsData  ==  null) {
            retColumnsData        =  getColumnsData(stringTableNameT,  null) ;
            hashtableColumnsData.put(stringTableName,  retColumnsData) ;
        }
        try {
            // SQL 語法產生
            for(int  intNo=0  ;  intNo<retColumnsData.length  ;  intNo++) {
                	stringColumnName    =  retColumnsData[intNo][0].trim() ;
                	stringColumnType    =  retColumnsData[intNo][1].trim() ;
                	stringColumnValue1  =  (String) hashtableData.get(stringColumnName) ; 
                	stringColumnValue2  =  (String) hashtableAnd.get(stringColumnName) ; 
                	booleanNum          =  isNum(stringColumnType);
                	// 更新資料
                	if(stringColumnValue1  !=  null) {
                    	if(!"".equals(stringUpdateDataSql))  stringUpdateDataSql  +=  ", \n" ;
                      if(booleanNum  ||  "null".equals(stringColumnValue1)) {
                          // 數值
                          if(doParseDouble(stringColumnValue1)  <=  0)  stringColumnValue1 =  "0" ;
                          stringUpdateDataSql  +=  " "+ stringColumnName+"  =  "+stringColumnValue1+" " ;
                      } else {
                          if(stringColumnType.toUpperCase().startsWith("N")) {
                              stringSign  =  "N" ;
                          } else {
                              stringSign  =  "" ;
                          }
                          // 文字
                          stringUpdateDataSql  +=  " "+ stringColumnName+"  =  "+stringSign+"'"+stringColumnValue1+"' " ;
                      }
                   }
                  // 限制條件
                  if(stringColumnValue2  !=  null) {
                      if(booleanNum  ||  "null".equals(stringColumnValue2)) {
                          // 數值 
                          stringWhereSql  +=  " AND "  + stringColumnName + "  =  "  +stringColumnValue2  +  " \n" ;
                      } else {
                          // 文字
                          stringWhereSql  +=  " AND "  + stringColumnName + "  =  '"  +stringColumnValue2  +  "' \n" ;
                      }
                  }
                	// 清空
                	hashtableData.remove(stringColumnName) ;
                	hashtableAnd.remove(stringColumnName) ;
            }
            //
            stringSql  =  " UPDATE  "  +  stringTableNameT+" \n"  +
                             " SET  "  +  stringUpdateDataSql  +  " \n" +
                           " WHERE  1=1 \n"  +  stringWhereSql +  ""+
                             stringSqlAnd ;
            boolean     booleanFlag  =  booleanDB  &&  
                                        !"".equals(stringTableNameT)      &&  
                                        !"".equals(stringUpdateDataSql)  &&  
                                        (!"".equals(stringWhereSql)  ||  !"".equals(stringSqlAnd)) ;
            if(booleanFlag)  exeFromPool(stringSql) ;
        } catch (Throwable e) {
              return  ""+e.toString() ;
        }
        return  stringSql ;
    }
    public  boolean  isNum(String  stringType) throws  Throwable {
        if(vectorTxtType.size()  ==  0) {
            vectorTxtType.add("text".toUpperCase()) ;
            vectorTxtType.add("VARCHAR".toUpperCase()) ;
            vectorTxtType.add("CHAR".toUpperCase()) ;
        }
        if("null".equals(stringType))  return  false ;
        //
        boolean   booleanFlag  =  (vectorTxtType.indexOf(stringType.toUpperCase())==-1) ;
        System.out.println("型態判斷："+booleanFlag+"--"+stringType) ;
        return  booleanFlag ;
    }
		public  double  doParseDouble(String  stringNum) throws Exception {
				// 
				double  doubleNum  =  0 ;
				//
			 if(stringNum==null  ||"".equals(stringNum)  ||  "null".equals(stringNum))  return  0;
				//
				stringNum  =  stringNum.trim();
				//
				try{
						doubleNum  =  Double.parseDouble(stringNum) ;
				} catch(Exception e) {
				        System.out.println("無法剖析["  +  stringNum  +  "]，回傳 0。") ;
						return  0 ;
				}
				return  doubleNum ;
		}
		public  String  doSubstring(String  stringObject,  int  intStart,  int  intEnd) throws Exception {
				String  stringRet  =  "" ;
				// intStart
				if(stringObject.length( )  <  intStart)  return  stringRet ;
				intStart  =  (intStart  <  0) ?  0 :  intStart ;
				// intEnd
				if(intEnd  <  0)  return  stringRet ;
				intEnd  =  (stringObject.length( )  <  intEnd) ?  stringObject.length( ) :  intEnd ;
				stringRet  =  stringObject.substring(intStart,  intEnd) ;
				return  stringRet ;
		}
}


