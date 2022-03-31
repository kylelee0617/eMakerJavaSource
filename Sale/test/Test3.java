package Sale.test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test3 extends jcx.jform.bproc {
  public String getDefaultValue(String value) throws Throwable {

    String str = "有其父必有犬子"; // 要加密的字串
    System.out.println(this.getMD5Str(str).toUpperCase());
    
    System.out.println(get("NINJACODE2").toString());

    return value;
  }

  public String getMD5Str(String str) {
    byte[] digest = null;
    try {
      MessageDigest md5 = MessageDigest.getInstance("md5");
      digest = md5.digest(str.getBytes("utf-8"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    // 16是表示转换为16进制数
    String md5Str = new BigInteger(1, digest).toString(16);
    return md5Str;
  }

  public String getInformation() {
    return "---------------Test2(Test2).defaultValue()----------------";
  }
}
