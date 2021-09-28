package Sale.LyodsProcess;
import jcx.jform.bproc;

public class FormLoad extends bproc{
	public String getDefaultValue(String value)throws Throwable{
		System.out.println(">>>LyodsProcess Init Done");
		
		getButton("DoCheck").doClick();
		
		return value;
	}
	public String getInformation(){
		return "---------------FormLoda(FormLoda).defaultValue()----------------";
	}
}
