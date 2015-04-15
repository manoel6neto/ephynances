package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentBean;
import br.com.physisbrasil.web.ephynances.ejb.PaymentDocumentBean;
import br.com.physisbrasil.web.ephynances.ejb.SubAgreementInstallmentBean;
import br.com.physisbrasil.web.ephynances.model.AgreementInstallment;
import br.com.physisbrasil.web.ephynances.model.Payment;
import br.com.physisbrasil.web.ephynances.model.PaymentDocument;
import br.com.physisbrasil.web.ephynances.model.SubAgreementInstallment;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
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
    private Payment payment;

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
        
        if ((Payment) getFlash("payment") != null) {
            payment = (Payment) getFlash("payment");
        } else {
            if (payment == null) {
                payment = new Payment();
                payment.setTotalValue(new BigDecimal(0));
            }
        }
    }

    public void addSubInstallmentPayment() {
        try {
            subAgreementInstallmentBean.clearCache();
            if (subAgreementInstallment != null) {
                if (subAgreementInstallment.getPayment() != null) {
                    JsfUtil.addErrorMessage("Sub Parcela já foi paga.");
                } else {
                    if (payment != null) {
                        if (subAgreementInstallment.getValue().compareTo(payment.getTotalValue()) == 0) {
                            payment.setSubAgreementInstallment(subAgreementInstallment);
                            payment.setTotalValue(payment.getTotalValue());
                            payment.setPaymentDate(new Date(System.currentTimeMillis()));
                            paymentBean.create(payment);
                            paymentBean.clearCache();

                            subAgreementInstallmentBean.clearCache();
                            subAgreementInstallment = subAgreementInstallmentBean.find(subAgreementInstallment.getId());
                            subAgreementInstallment.setPayment(payment);
                            subAgreementInstallmentBean.edit(subAgreementInstallment);
                            subAgreementInstallmentBean.clearCache();
                            
                            payment = new Payment();
                            payment.setTotalValue(new BigDecimal(0));

                            JsfUtil.addSuccessMessage("Pagamento realizado com sucesso. Sub Parcela totalmente paga.");
                        } else {
                            JsfUtil.addErrorMessage("Não é possível pagar valores superiores ou inferiores ao valor da sub parcela.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao adicionar pagamento.");
        }
    }

    public String checkStatusSubAgreementInstallment() {
        try {
            if (subAgreementInstallment != null) {
                if (subAgreementInstallment.getPayment() == null) {
                    return "Sem pagamento";
                } else {
                    return "Pagamento na data: " + formatDate(subAgreementInstallment.getPayment().getPaymentDate());
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao verificar status do pagamento da Sub Parcela");
        }

        return "Sem pagamento";
    }
    
    public void confirmPayment() {
        try {
            subAgreementInstallmentBean.clearCache();
            if (subAgreementInstallment != null) {
                if (subAgreementInstallment.getPayment() != null) {
                    Payment tempPayment = paymentBean.find(subAgreementInstallment.getPayment().getId());
                    tempPayment.setConfirmationDate(new Date(System.currentTimeMillis()));
                    paymentBean.edit(tempPayment);
                    paymentBean.clearCache();

                    //Reload and set status
                    subAgreementInstallmentBean.clearCache();
                    subAgreementInstallment = subAgreementInstallmentBean.find(subAgreementInstallment.getId());

                    JsfUtil.addSuccessMessage("Pagamento confirmado com sucesso!!");
                } else {
                    JsfUtil.addErrorMessage("Pagamento não encontrado.");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao mudar o status do pagamento!!");
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
    
    public void removeDocument(Long paymentDocumentId) {
        try {
            paymentDocumentBean.clearCache();
            PaymentDocument tempPaymentDocument = paymentDocumentBean.find(paymentDocumentId);
            if (tempPaymentDocument != null) {
                paymentDocumentBean.remove(tempPaymentDocument);
                paymentDocumentBean.clearCache();
                paymentBean.clearCache();
                subAgreementInstallmentBean.clearCache();
                
                subAgreementInstallment = subAgreementInstallmentBean.find(subAgreementInstallment.getId());
                
                putFlash("subAgreementInstallment", subAgreementInstallment);
                JsfUtil.addSuccessMessage("Documento removido com sucesso!!");
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao apagar o documento solicitado.");
        }
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
    
    public String checkIsPaid() {
        if (subAgreementInstallment != null) {
            if (subAgreementInstallment.getPayment() != null) {
                return String.valueOf(true);
            } else {
                return String.valueOf(false);
            }
        }
        
        return String.valueOf(false);
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

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String formatDate(Date dateToFormat) {
        DateFormat outputFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return outputFormatter.format(dateToFormat);
    }
}
