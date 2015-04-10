package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentDocumentBean;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.PaymentDocument;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Thomas
 */
@ManagedBean
@ViewScoped
public class PaymentDocumentController extends BaseController {

    @EJB
    private PaymentDocumentBean paymentDocumentBean;
    private PaymentDocument paymentDocument;

    @EJB
    private PaymentBean paymentBean;
    private Payment payment;

    private UploadedFile file;

    @PostConstruct
    public void init() {

        if ((Payment) getFlash("payment") != null) {
            payment = (Payment) getFlash("payment");
        } else {
            if (payment == null) {
                payment = new Payment();
            }
        }

        if ((PaymentDocument) getFlash("paymentDocument") != null) {
            paymentDocument = (PaymentDocument) getFlash("paymentDocument");
        } else {
            if (paymentDocument == null) {
                paymentDocument = new PaymentDocument();
            }
        }
    }

    public String startPaymentDocuments(Long paymentId) {
        try {
            paymentBean.clearCache();
            payment = paymentBean.find(paymentId);
            if (payment != null) {

                putFlash("payment", payment);
                return "/paymentDocument/documents";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar os documentos para o pagamento.");
        }

        return "/payment/list";
    }

    public void removeDocument(Long paymentDocumentId) {
        try {
            paymentDocumentBean.clearCache();
            PaymentDocument tempPaymentDocument = paymentDocumentBean.find(paymentDocumentId);
            if (tempPaymentDocument != null) {
                paymentDocumentBean.remove(tempPaymentDocument);
                paymentDocumentBean.clearCache();
                paymentBean.clearCache();
                payment = paymentBean.find(payment.getId());

                putFlash("payment", payment);
                JsfUtil.addSuccessMessage("Documento removido com sucesso!!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar o documento solicitado.");
        }
    }

    public String checkFile() {
        try {
            if (file != null) {
                if (file.getFileName() != null) {
                    if (!file.getFileName().equalsIgnoreCase("")) {
                        if (paymentDocument.getName() != null && paymentDocument.getDescription() != null) {
                            return String.valueOf(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return String.valueOf(true);
        }

        return String.valueOf(true);
    }

    public void upload() {
        try {
            if (!Boolean.parseBoolean(checkFile())) {
                if (file.getInputstream() != null) {
                    paymentDocument.setExtension(file.getFileName().trim().split(Pattern.quote("."))[1]);
                    paymentDocument.setMime(file.getContentType());
                    paymentDocument.setSize(file.getSize());
                    paymentDocument.setFile(IOUtils.toByteArray(file.getInputstream()));
                    paymentDocument.setPayment(payment);

                    paymentDocumentBean.create(paymentDocument);
                    paymentDocumentBean.clearCache();
                    paymentBean.clearCache();
                    payment = paymentBean.find(payment.getId());

                    paymentDocument = new PaymentDocument();

                    putFlash("payment", payment);
                    JsfUtil.addSuccessMessage("Upload do documento realizado com sucesso!!");
                } else {
                    throw new Exception("Falha ao ler o arquivo");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao fazer o upload do arquivo.");
        }
    }

    public StreamedContent downloadDocument(Long paymentDocumentId) {
        try {
            paymentDocumentBean.clearCache();
            PaymentDocument tempPaymentDocument = paymentDocumentBean.find(paymentDocumentId);
            if (tempPaymentDocument != null) {
                InputStream stream = new ByteArrayInputStream(tempPaymentDocument.getFile());
                return new DefaultStreamedContent(stream, tempPaymentDocument.getMime(), tempPaymentDocument.getName() + "." + tempPaymentDocument.getExtension());
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao preparar download do documento solicitado.");
        }

        return null;
    }

    public PaymentDocument getPaymentDocument() {
        return paymentDocument;
    }

    public void setPaymentDocument(PaymentDocument paymentDocument) {
        this.paymentDocument = paymentDocument;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
}
