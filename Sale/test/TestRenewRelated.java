package Sale.test;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import com.fglife.soap.client.BlackListClient;
import com.fglife.soap.cr.RenewRelatedQuery;
import com.fglife.soap.cr.RenewRelatedReply;

import Farglory.aml.AMLTools_Lyods;
import Farglory.aml.AMLyodsBean;
import Farglory.aml.RiskCustomBean;
import Farglory.util.Result;
import jcx.jform.*;

public class TestRenewRelated extends bproc{
  public String getDefaultValue(String value)throws Throwable{
    String rsMsg = "";
    Result result = null;
	
	//showDialog("LyodsProcess", "", false, false, 300, 300, 600, 400);
    
	//showDialog("LyodsProcess");
	showForm("LyodsProcess");
	setValue("LProjectId", "test111");
	
	getButton("button1").doClick();
	getButton("RenewRelated").doClick();
	
	//getButton("button2").doClick();
	
	
    return value;
  }
  public String getInformation(){
    return "---------------Test(Test).defaultValue()----------------";
  }
}
