package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentDocumentBean;
import br.com.physisbrasil.web.ephynances.ejb.SubAgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.PaymentDocument;
import br.com.physisbrasil.web.ephynances.model.SubAgreementInstallment;
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
public class SubAgreementInstallmentController extends BaseController {

    @EJB
    private SubAgreementInstallmentBean subAgreementInstallmentBean;
    private SubAgreementInstallment subAgreementInstallment;

    @EJB
    private PaymentBean paymentBean;

    @EJB
    private PaymentDocumentBean paymentDocumentBean;
    private PaymentDocument paymentDocument;

    @EJB
    private AgreementInstallmentBean agreementInstallmentBean;
    private AgreementInstallment agreementInstallment;

    private UploadedFile file;

    @PostConstruct
    public void init() {

        if ((SubAgreementInstallment) getFlash("subAgreementInstallment") != null) {
            subAgreementInstallment = (SubAgreementInstallment) getFlash("subAgreementInstallment");
        } else {
            if (subAgreementInstallment == null) {
                subAgreementInstallment = new SubAgreementInstallment();
            }
        }

        if ((PaymentDocument) getFlash("paymentDocument") != null) {
            paymentDocument = (PaymentDocument) getFlash("paymentDocument");
        } else {
            if (paymentDocument == null) {
                paymentDocument = new PaymentDocument();
            }
        }

        if ((AgreementInstallment) getFlash("agreementInstallment") != null) {
            agreementInstallment = (AgreementInstallment) getFlash("agreementInstallment");
        } else {
            if (agreementInstallment == null) {
                agreementInstallment = new AgreementInstallment();
            }
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
                    paymentDocument.setPayment(subAgreementInstallment.getPayment());

                    paymentDocumentBean.create(paymentDocument);
                    paymentDocumentBean.clearCache();
                    paymentBean.clearCache();
                    subAgreementInstallmentBean.clearCache();
                    
                    subAgreementInstallment = subAgreementInstallmentBean.find(subAgreementInstallment.getId());

                    paymentDocument = new PaymentDocument();

                    putFlash("subAgreementInstallment", subAgreementInstallment);
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

    public String startSubAgreementInstallmentActivity(Long agreementInstallmentId) {
        try {
            agreementInstallmentBean.clearCache();
            subAgreementInstallmentBean.clearCache();

            agreementInstallment = agreementInstallmentBean.find(agreementInstallmentId);
            if (agreementInstallment != null) {
                if (agreementInstallment.getSubAgreementInstallment() != null) {
                    subAgreementInstallment = subAgreementInstallmentBean.find(agreementInstallment.getSubAgreementInstallment().getId());

                    putFlash("subAgreementInstallment", subAgreementInstallment);
                    putFlash("agreementInstallment", agreementInstallment);
                    return "/subAgreementInstallment/subAgreementDetails";
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar os detalhes da subparcela e do pagamento.");
        }

        return "/agreementInstallment/installments";
    }

    public SubAgreementInstallment getSubAgreementInstallment() {
        return subAgreementInstallment;
    }

    public void setSubAgreementInstallment(SubAgreementInstallment subAgreementInstallment) {
        this.subAgreementInstallment = subAgreementInstallment;
    }

    public PaymentDocument getPaymentDocument() {
        return paymentDocument;
    }

    public void setPaymentDocument(PaymentDocument paymentDocument) {
        this.paymentDocument = paymentDocument;
    }

    public AgreementInstallment getAgreementInstallment() {
        return agreementInstallment;
    }

    public void setAgreementInstallment(AgreementInstallment agreementInstallment) {
        this.agreementInstallment = agreementInstallment;
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
