package  Doc.TestJob;
import      javax.swing.*;
import      jcx.jform.bproc;
import      java.io.*;//
import      java.util.*;
import      jcx.util.*;
import      jcx.html.*;
import      jcx.db.*;
import      jcx.net.*;
import      jcx.net.smtp ;
import      java.text.* ;
import      java.nio.channels.*;//
import      Farglory.util.FargloryUtil ;

public class  TestJob1 {
    public  static  void  main(String  args[]) throws Throwable{
        System.out.println(">>>排程 TestJob1 啟動<<<");
        talk              dbDocCS                               =  new  talk("mssql", "172.16.14.1",    "doc",  "docfglife",  "Doc") ;    // Doc
        
        dbDocCS.close() ;
        
        System.out.println(">>>排程 Doc7B0262 完成<<<");
    }
    
    public  static  talk  getTalkCS(String  stringDBName,  FargloryUtil  exeUtil) throws  Throwable {
        talk  dbDocCS  =  null ;
        try{ 
            dbDocCS  =  new  talk("mssql", "172.16.14.1",    "invoice",  "invoicepw",   stringDBName) ;    // Doc
            dbDocCS.getColumnsFromPool("USERS") ;
        } catch (Throwable e) {
            System.out.println("人壽連線失敗") ; 
            return null ;
        } 
        //
        return  dbDocCS ;
    }
    
}

