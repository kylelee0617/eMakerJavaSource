/**
 * 2020-03-16 kyle add Enum for AML
 */
package Sale.Enum ;

public enum EnumAMLStatus {
	SUCCESS(1 , "success" , "成功完成" , "desc");

	private int code;
	private String ename;
	private String cname;
	private String desc;

	private EnumAMLStatus(int code , String ename , String cname , String desc) {
		this.code = code;
		this.ename = ename;
		this.cname = cname;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	public String getCname()
	{
		return cname;
	}

	public void setCname(String cname)
	{
		this.cname = cname;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
