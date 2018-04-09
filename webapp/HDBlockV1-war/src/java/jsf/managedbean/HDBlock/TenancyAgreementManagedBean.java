/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBlockUserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helperclass.LandlordTenancyAgreement;

/**
 *
 * @author David
 */
@Named(value = "tenancyAgreementManagedBean")
@ViewScoped
public class TenancyAgreementManagedBean implements Serializable {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    /**
     * Creates a new instance of TenancyAgreementManagedBean
     */
    
    
    private List<LandlordTenancyAgreement> tenancyAgreements;
    private LandlordTenancyAgreement selectedTenancy;
    
    public TenancyAgreementManagedBean() {
        tenancyAgreements = new ArrayList<>();
        selectedTenancy = new LandlordTenancyAgreement();
    }
    
    @PostConstruct
    public void postConstruct(){
        
        HDBlockUserEntity userInformation = (HDBlockUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userInformation");
       tenancyAgreements = hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByLandlordId(userInformation.getIdentificationNo());
       
    }

    /**
     * @return the tenancyAgreements
     */
    public List<LandlordTenancyAgreement> getTenancyAgreements() {
        return tenancyAgreements;
    }

    /**
     * @param tenancyAgreements the tenancyAgreements to set
     */
    public void setTenancyAgreements(List<LandlordTenancyAgreement> tenancyAgreements) {
        this.tenancyAgreements = tenancyAgreements;
    }

    /**
     * @return the selectedTenancy
     */
    public LandlordTenancyAgreement getSelectedTenancy() {
        return selectedTenancy;
    }

    /**
     * @param selectedTenancy the selectedTenancy to set
     */
    public void setSelectedTenancy(LandlordTenancyAgreement selectedTenancy) {
        this.selectedTenancy = selectedTenancy;
    }
    
}
