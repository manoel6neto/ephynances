package br.com.physisbrasil.web.ephynances.jsf;

import br.com.physisbrasil.web.ephynances.ejb.AgreementBean;
import br.com.physisbrasil.web.ephynances.ejb.AgreementDocumentBean;
import br.com.physisbrasil.web.ephynances.model.Agreement;
import br.com.physisbrasil.web.ephynances.model.AgreementDocument;
import br.com.physisbrasil.web.ephynances.util.JsfUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
public class AgreementDocumentController extends BaseController {

    @EJB
    private AgreementDocumentBean agreementDocumentBean;
    private AgreementDocument agreementDocument;

    @EJB
    private AgreementBean agreementBean;
    private Agreement agreement;

    private UploadedFile file;

    @PostConstruct
    public void init() {

        if ((Agreement) getFlash("agreement") != null) {
            agreement = (Agreement) getFlash("agreement");
        } else {
            if (agreement == null) {
                agreement = new Agreement();
            }
        }

        if ((AgreementDocument) getFlash("agreementDocument") != null) {
            agreementDocument = (AgreementDocument) getFlash("agreementDocument");
        } else {
            if (agreementDocument == null) {
                agreementDocument = new AgreementDocument();
            }
        }
    }

    public String startAgreementDocuments(Long agreementId) {
        try {
            agreementBean.clearCache();
            agreement = agreementBean.find(agreementId);
            if (agreement != null) {

                putFlash("agreement", agreement);
                return "/agreementDocument/documents";
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao carregar os documentos para o contrato.");
        }

        return "/agreement/list";
    }

    public void removeDocument(Long agreementDocumentId) {
        try {
            agreementDocumentBean.clearCache();
            AgreementDocument tempAgreementDocument = agreementDocumentBean.find(agreementDocumentId);
            if (tempAgreementDocument != null) {
                agreementDocumentBean.remove(tempAgreementDocument);
                agreementDocumentBean.clearCache();
                agreementBean.clearCache();
                agreement = agreementBean.find(agreement.getId());

                putFlash("agreement", agreement);
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
                        if (agreementDocument.getName() != null && agreementDocument.getDescription() != null) {
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
                    agreementDocument.setExtension(file.getFileName().trim().split(Pattern.quote("."))[1]);
                    agreementDocument.setMime(file.getContentType());
                    agreementDocument.setSize(file.getSize());
                    agreementDocument.setFile(IOUtils.toByteArray(file.getInputstream()));
                    agreementDocument.setAgreement(agreement);

                    agreementDocumentBean.create(agreementDocument);
                    agreementDocumentBean.clearCache();
                    agreementBean.clearCache();
                    agreement = agreementBean.find(agreement.getId());
                    
                    agreementDocument = new AgreementDocument();

                    putFlash("agreement", agreement);
                    JsfUtil.addSuccessMessage("Upload do documento realizado com sucesso!!");
                } else {
                    throw new Exception("Falha ao ler o arquivo");
                }
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao fazer o upload do arquivo.");
        }
    }

    public StreamedContent downloadDocument(Long agreementDocumentId) {
        try {
            agreementDocumentBean.clearCache();
            AgreementDocument tempAgreementDocument = agreementDocumentBean.find(agreementDocumentId);
            if (tempAgreementDocument != null) {
                InputStream stream = new ByteArrayInputStream(tempAgreementDocument.getFile());
                return new DefaultStreamedContent(stream, tempAgreementDocument.getMime(), tempAgreementDocument.getName() + "." + tempAgreementDocument.getExtension());
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, "Falha ao preparar download do documento solicitado.");
        }

        return null;
    }

    public Agreement getAgreement() {
        return agreement;
    }

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
    }

    public AgreementDocument getAgreementDocument() {
        return agreementDocument;
    }

    public void setAgreementDocument(AgreementDocument agreementDocument) {
        this.agreementDocument = agreementDocument;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
