package Farglory.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DefaultInfo extends jcx.jform.sproc {

  public String getDefaultValue(String value) throws Throwable {
    StringBuilder showConfig = new StringBuilder();

    InetAddress addr = InetAddress.getLocalHost();
    
    String serverName = addr.getHostName().toString(); // ��o�����W��
    put("serverName", serverName);
    showConfig.append("server name:").append(serverName).append("\n");
    
    String serverIP = addr.getHostAddress().toString(); // ��o����IP
    put("serverIP", serverIP);
    showConfig.append("server ip:").append(serverIP).append("\n");

    // =============================== Map �� ==================================
    Map configMap = new HashMap();
    ResourceBundle resource = ResourceBundle.getBundle("configK");

    // GENLIB for AS400
    String GENLIB = resource.getString("AS400.GENLIB");
    configMap.put("GENLIB", GENLIB);
    showConfig.append("GENLIB:").append(GENLIB).append("\n");

    // �ܴ��D�����|
    String lyodsSoapURL = resource.getString("lyodsSoapURL");
    configMap.put("lyodsSoapURL", lyodsSoapURL);
    showConfig.append("lyodsSoapURL:").append(lyodsSoapURL).append("\n");
    
    //���վ��Υ���
    String serverType = resource.getString("serverType");
    put("serverType", serverType);
    showConfig.append("serverType:").append(serverType).append("\n");
    
    // EMAKER�D����T
    configMap.put("serverIP", serverIP);
    configMap.put("serverName", serverName);
    configMap.put("serverType", serverType);
    
    //???
    String ninjaCode = resource.getString("NINJACODE");
    put("ninjaCode", ninjaCode);
    
    
    //��J����MAP
    put("config", configMap);
    setValue("config", showConfig.toString());

    return value;
  }

  public String getInformation() {
    return "---------------default(default).defaultValue()----------------";
  }
}
