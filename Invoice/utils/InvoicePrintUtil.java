package Invoice.utils;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import Invoice.vo.InvoicePrintVo;

public class InvoicePrintUtil{
  protected Call call = null;

  public InvoicePrintUtil() {
  }

  public InvoicePrintUtil(String url) throws ServiceException {
    System.out.println("===================Start to init InvoicePrintUtil====================");
    
    Service printService = new Service();
    call = (Call) printService.createCall();
    call.setTargetEndpointAddress(url + "/InvoicePrintServiceImpl?wsdl"); // invoiceEndpoint
    call.setOperationName("print");
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "sendPost"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN); // �H��H�l���ϸ�
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "sendAddr"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN); // �H��H�a�}
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "sendCompany"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �H��H���q
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "sendName"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �H��H�W��
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "recipientPost"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ����H�l���ϸ�
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "recipientAddr"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ����H�a�}
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "recipientCompany"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ����H���q
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "recipientName"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ����H�m�W
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "title"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o�����Y
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "invoiceDate"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o�����ͤ��
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "invoiceNumber"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o���s��
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "printDate"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o���C�L���
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "randomCode"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �H���X
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "total"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �`�p
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "buyerId"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �R��
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "sellerId"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ���
    // call.addParameter(new QName("http://impl.service.invoice.fglife.com", "barcode"),
    // org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ���X
    // call.addParameter(new QName("http://impl.service.invoice.fglife.com", "qrcode1"),
    // org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// QR CODE1
    // call.addParameter(new QName("http://impl.service.invoice.fglife.com", "qrcode2"),
    // org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// QR CODE2
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "mark1"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o���U����O1
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "mark2"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �o���U����O 2
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "detail"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// ���ӲӶ��H,�@�����j��� ;�����j����
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "printCount"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �C�L����;�j��1���|�ܸɦL
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "deptId"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �C�L�̩��ݳ��
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "saleAmount"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �P���B
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "buyerName"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �R���H
    call.addParameter(new QName("http://impl.service.invoice.fglife.com", "tax"),
        org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);// �|�B
    call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
    
    System.out.println("===================init InvoicePrintUtil Complete====================");
  }
  
  public String doPrint(InvoicePrintVo vo) {
    String title = vo.getTitle();
    String sendPost = vo.getSendPost();
    String sendAddr = vo.getSendAddr();
    String sendCompany = vo.getSendCompany();
    String sendName = vo.getSendName();
    String recipientPost = vo.getRecipientPost();
    String recipientAddr = vo.getRecipientAddr();
    String recipientCompany = vo.getRecipientCompany();
    String recipientName = vo.getRecipientName();
    String invoiceDate = vo.getInvoiceDate();
    String invoiceNumber = vo.getInvoiceNumber();
    String printDate = vo.getPrintDate();
    String randomCode = vo.getRandomCode();
    String saleAmount = vo.getSaleAmount();
    String total = vo.getTotal();
    String buyerId = vo.getBuyerId();
    String buyerName = vo.getBuyerName();
    String sellerId = vo.getSellerId();
    String mark1 = vo.getMark1();
    String mark2 = vo.getMark2();
    String detail = vo.getDetail();
    String printCount = vo.getPrintCount();
    String deptId = vo.getDeptId();
    String tax = vo.getTax();
    
    String result = "";
    try {
      result = (String) call.invoke(new Object[] { sendPost, sendAddr, sendCompany, sendName, recipientPost,
          recipientAddr, recipientCompany, recipientName, title, invoiceDate, invoiceNumber, printDate, randomCode,
          total, buyerId, sellerId, mark1, mark2, detail, printCount, deptId, saleAmount, buyerName, tax });
      
      System.out.println(call.getMessageContext().getRequestMessage().getSOAPPartAsString());
    }catch(Exception ex) {
      result = ex.toString();
    }
    
    return result;
  }
  
}
