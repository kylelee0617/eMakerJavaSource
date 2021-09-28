package Farglory.util;

import org.apache.commons.lang.StringUtils;;

public class SystemConfig {
  //Server
  private String serverIp = "";
  private String serverName = "";
  private String serverType = "";
  private boolean isTest = true;
  
  //AS400 LIB
  private String GENLIB = "";
  
  //Lyods Utils
  private String lyodsSoapURL = "";

  public String getServerIp() {
    return serverIp;
  }

  public void setServerIp(String serverIp) {
    this.serverIp = serverIp.trim();
    if(StringUtils.indexOf(this.serverIp, "172.16") == 0) this.setTest(false);
  }

  public String getServerName() {
    return serverName;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public String getServerType() {
    return serverType;
  }

  public void setServerType(String serverType) {
    this.serverType = serverType;
  }

  public boolean isTest() {
    return isTest;
  }

  public void setTest(boolean isTest) {
    this.isTest = isTest;
  }

  public String getLyodsSoapURL() {
    return lyodsSoapURL;
  }

  public void setLyodsSoapURL(String lyodsSoapURL) {
    this.lyodsSoapURL = lyodsSoapURL;
  }

  public String getGENLIB() {
    return GENLIB;
  }

  public void setGENLIB(String gENLIB) {
    GENLIB = gENLIB;
  }

}
