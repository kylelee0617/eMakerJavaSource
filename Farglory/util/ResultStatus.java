/**
 * 2020-03-16 kyle add Enum for AML
 */
package Farglory.util ;

public class ResultStatus {
	public static String[] SUCCESS =             {"0" , "success" , "���\" , "���榨�\"};
	public static String[] SUCCESSBUTSOMEERROR = {"1" , "successButSomeError" , "��������" , "���榨�\�A���������o�Ͱ��D�C"};
	public static String[] NODATA =              {"700" , "no data" , "�d�L���" , "�d�L���"};
	public static String[] NODATA_AMLMSG =       {"710" , "NO AMLMSG" , "�d�L�A�˻���" , "Error: �d�L���˺A����"};
	public static String[] SENDEMAILERROR =      {"93" , "sendEMailError" , "�o�eEmail����" , "�o�eEMail���ѡC"};
	public static String[] ERROR =               {"9999" , "Error" , "����" , "�����}�p�t�S"};
	
}
