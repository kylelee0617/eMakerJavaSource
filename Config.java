import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Config extends jcx.jform.sproc {
  public String getDefaultValue(String value) throws Throwable {
    StringBuilder showConfig = new StringBuilder();
    // ���A����T
    InetAddress addr = InetAddress.getLocalHost();
    String ip = addr.getHostAddress().toString(); // ��o����IP
    String address = addr.getHostName().toString(); // ��o�����W��
    put("serverIP", ip);
    System.out.println("addr=:" + String.valueOf(addr));
    System.out.println("ip:" + ip + "/ name:" + address);
    showConfig.append("ip:").append(ip).append("\n");
    showConfig.append("name:").append(address).append("\n");
    
    // �]�w��
    Map configMap = new HashMap();
    ResourceBundle resource = ResourceBundle.getBundle("configK");
    String GENLIB = resource.getString("AS400.GENLIB");
    showConfig.append("GENLIB:").append(GENLIB).append("\n");
    configMap.put("GENLIB", GENLIB);
    
    String PRINTURL = resource.getString("SYSTEM.PRINTURL");
    System.out.println("PRINTURL>>>" + PRINTURL);
    showConfig.append("PRINTURL:").append(PRINTURL).append("\n");
    configMap.put("PRINTURL", PRINTURL);
    put("config", configMap);
    setValue("config", showConfig.toString());
    return value;
  }
}