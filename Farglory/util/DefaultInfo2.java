package Farglory.util;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class DefaultInfo2 extends jcx.jform.sproc {

  public String getDefaultValue(String value) throws Throwable {
    StringBuilder showConfig = new StringBuilder();

    InetAddress addr = InetAddress.getLocalHost();
    
    String serverName = addr.getHostName().toString(); // 獲得本機名稱
    put("serverName", serverName);
    showConfig.append("server name:").append(serverName).append("\n");
    
    String serverIP = addr.getHostAddress().toString(); // 獲得本機IP
    put("serverIP", serverIP);
    showConfig.append("server ip:").append(serverIP).append("\n");

    // =============================== Map 化 ==================================
    Map configMap = new HashMap();
    ResourceBundle resource = ResourceBundle.getBundle("configK");

    //for AS400
    String GENLIB = resource.getString("AS400.GENLIB");
    configMap.put("GENLIB", GENLIB);
    showConfig.append("GENLIB:").append(GENLIB).append("\n");
    
    put("GENLIB", GENLIB);
    put("LSPFLIB" , resource.getString("AS400.LSPFLIB"));
    put("PSLIB" , resource.getString("AS400.PSLIB"));
    put("WENLIB" , resource.getString("AS400.WENLIB"));
    put("WENLIB" , resource.getString("AS400.WENLIB"));
    
    String libType = resource.getString("AS400.LIBTYPE");
    put("LIBTYPE" , libType);
    
    // 萊斯主機路徑
    String lyodsSoapURL = resource.getString("lyodsSoapURL");
    configMap.put("lyodsSoapURL", lyodsSoapURL);
    showConfig.append("lyodsSoapURL:").append(lyodsSoapURL).append("\n");
    
    //測試機或正式
    String serverType = resource.getString("serverType");
    put("serverType", serverType);
    showConfig.append("serverType:").append(serverType).append("\n");
    
    // EMAKER主機資訊
    configMap.put("serverIP", serverIP);
    configMap.put("serverName", serverName);
    configMap.put("serverType", serverType);
    put("config", configMap);
    
    showConfig.append("LIBTYPE:").append(libType).append("\n");

    setValue("config", showConfig.toString());

    // =============================== 物件化 ==================================
    SystemConfig sysConfig = new SystemConfig();
    sysConfig.setServerIp(serverIP);
    sysConfig.setServerName(serverName);
    sysConfig.setServerType(serverType);
    sysConfig.setLyodsSoapURL(lyodsSoapURL);
    sysConfig.setGENLIB(GENLIB);
//    put("sysConfig", sysConfig);

//    Map mapSysConfig = new HashMap();
//    mapSysConfig.put("sysConfig", sysConfig);
//    put("SYSTEMCONFIG", mapSysConfig);
//
//    this.putProperty("lyodsSoapURL", lyodsSoapURL);
//    System.out.println(">>>" + this.getProperty("lyodsSoapURL"));

    return value;
  }

  public String getInformation() {
    return "---------------default(default).defaultValue()----------------";
  }
}
