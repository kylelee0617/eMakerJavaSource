/**
 * 2020-03-16 kyle add Enum for AML
 */
package Farglory.util ;

public enum EnumRsStatus {
	SUCCESS(0 , "success" , "���\" , "���榨�\")
	,NODATA(700 , "no data" , "�d�L���" , "�d�L���")
	,NODATA_AMLMSG(710 , "NO AMLMSG" , "�d�L�A�˻���" , "Error: �d�L���˺A����")
	,ERROR(9999 , "Error" , "����" , "�����}�p�t�S");

	private int code;
	private String ename;
	private String cname;
	private String desc;

	private EnumRsStatus(int code , String ename , String cname , String desc) {
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
