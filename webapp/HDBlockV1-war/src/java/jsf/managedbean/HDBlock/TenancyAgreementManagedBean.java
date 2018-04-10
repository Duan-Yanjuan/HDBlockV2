/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import datamodel.ws.TenancyAgreementAsset;
import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helperclass.LandlordTenancyAgreement;
import util.helperclass.Tenant;
import util.helperclass.TenantTenancyAgreement;
import util.helperclass.TenantTenancySignature;

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
    private String newTenant;
    private List<Tenant> tenants;
    private TenancyAgreementAsset taAsset;
    private HDBHouseEntity landlordHouseInformation;
    private boolean isLandlord;
    private boolean isTenant;

    //for retrieval
    private List<LandlordTenancyAgreement> landlordTenancyAgreements;
   // private List<TenantTenancyAgreement> tenantTenancyAgreements;
    private List<TenantTenancySignature> tenantSignature;
    private LandlordTenancyAgreement selectedTenancy;
    private TenantTenancySignature selectedTenancySign;

    public TenancyAgreementManagedBean() {
        landlordTenancyAgreements = new ArrayList<>();
        tenantSignature = new ArrayList<>();
        selectedTenancy = new LandlordTenancyAgreement();
        taAsset = new TenancyAgreementAsset();
        tenants = new ArrayList<>();
        landlordHouseInformation = new HDBHouseEntity();
        selectedTenancySign = new TenantTenancySignature();
    }

    @PostConstruct
    public void postConstruct() {

        Object landlordSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isLandlord");

        isLandlord = false;
        Object tenantSession = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isTenant");
        isTenant = false;
        if (landlordSession != null) {
            isLandlord = (Boolean) landlordSession;
        } else if (tenantSession != null) {
            isTenant = (Boolean) tenantSession;
        }

        HDBlockUserEntity userInfo = (HDBlockUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userInformation");
        //  FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("isTenant");

        if (isLandlord) {
            landlordTenancyAgreements = hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByLandlordId(userInfo.getIdentificationNo());
//            System.out.println("**********  landlordTenancyAgreements " + landlordTenancyAgreements.get(0).getTenancyAgreementId());

            System.out.println("******** EMAIL IS " + userInfo.getEmail());
            landlordHouseInformation = hDBlockUserEntityControllerLocal.retrieveLandlordHouseByEmail(userInfo.getEmail()).get(0);
            landlordTenancyAgreements = hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByLandlordId(userInfo.getIdentificationNo());
            System.out.println("******** House IS " + landlordHouseInformation.getPostalCode());
            System.out.println("******** House IS " + landlordHouseInformation.getAddress());
        } else if (isTenant) {
            System.out.println("******** inside uisTenant");
            tenantSignature =  hDBlockUserEntityControllerLocal.retrieveTenancySignature(userInfo.getIdentificationNo());
        }

        //  HDBlockUserEntity userInformation = (HDBlockUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userInformation");
        //   tenancyAgreements = hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByLandlordId(userInformation.getIdentificationNo());
    }

    public void add_tenant(ActionEvent event) {
        tenants.add(new Tenant());
        System.out.println("********** TENANT SIZE " + tenants.size() + " tenant is " + tenants.get(0));
    }

    
    public void signContract(ActionEvent event){
        System.out.println("********** SIGNING CONTRACT SELECTED SINGATURE  " + selectedTenancySign.getSignatureId());
        if(hDBlockUserEntityControllerLocal.SignContract(selectedTenancySign.getSignatureId() , selectedTenancySign.getAgreement())){
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Contract Signed Succesfully", null));
              HDBlockUserEntity userInfo = (HDBlockUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userInformation");
             tenantSignature =  hDBlockUserEntityControllerLocal.retrieveTenancySignature(userInfo.getIdentificationNo());
        }else
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail to Sign.", null));
 
    }
  

    
    
    public void submitTa() {

        // 53070909108_01012018)
        SimpleDateFormat sdfRental = new SimpleDateFormat("yyyy-MM-dd");
        Date rentalDate = taAsset.getStartDate();

        Date todayDate = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String todayDateString = df.format(todayDate);
        System.out.println("*************** todatDate " + todayDateString);
        String[] todayDateFormated = todayDateString.split("/");

        String[] tenantsId = new String[tenants.size()];
        for (int i = 0; i < tenants.size(); i++) {
            System.out.println(tenants.get(i));
            tenantsId[i] = tenants.get(i).getIc();
            System.out.println("YENANT ID IS " + tenants.get(i).getIc());
        }
        System.out.println("********** RENTAL DATE " + rentalDate);
        String tenancyId = landlordHouseInformation.getPostalCode() + landlordHouseInformation.getUnitNumber().substring(1) + todayDateFormated[0] + todayDateFormated[1] + todayDateFormated[2];
        String houseId = landlordHouseInformation.getPostalCode() + landlordHouseInformation.getUnitNumber().substring(1);
        System.out.println("Tenancy ID " + tenancyId);
        System.out.println("********** SUBMIT" + tenants.size() + taAsset.getRentalFee());
        boolean createResult = hDBlockUserEntityControllerLocal.createNewTenancyAgreement(rentalDate, taAsset.getDuration(), taAsset.getSecurityDeposit(), taAsset.getAdvanceRentalFee(), taAsset.getRentalFee(), tenantsId, tenancyId , houseId);
        if (createResult) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Tenancy Agreement is created succesfully", null));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to Create tenancy agreement. Contact Admin", null));
        }

        setLandlordTenancyAgreements(new ArrayList<>());
        selectedTenancy = new LandlordTenancyAgreement();
        taAsset = new TenancyAgreementAsset();
        tenants = new ArrayList<>();

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
        System.out.println("*********************SELECTED TENANCY " + selectedTenancy.getTenancyAgreementId());
        System.out.println("*********************SELECTED TENANCY " + selectedTenancy.getDateCreated());
        System.out.println("*********************SELECTED TENANCY " + selectedTenancy.getAdvanceRentalFee());
        System.out.println("*********************SELECTED TENANCY " + selectedTenancy.getRentalFee());
        this.selectedTenancy = selectedTenancy;
    }

    /**
     * @return the taAsset
     */
    public TenancyAgreementAsset getTaAsset() {
        return taAsset;
    }

    /**
     * @param taAsset the taAsset to set
     */
    public void setTaAsset(TenancyAgreementAsset taAsset) {
        this.taAsset = taAsset;
    }

    /**
     * @return the newTenant
     */
    public String getNewTenant() {
        return newTenant;
    }

    /**
     * @param newTenant the newTenant to set
     */
    public void setNewTenant(String newTenant) {
        this.newTenant = newTenant;
    }

    public List<Tenant> getTenants() {
        return tenants;
    }

    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    /**
     * @return the landlordHouseInformation
     */
    public HDBHouseEntity getLandlordHouseInformation() {
        return landlordHouseInformation;
    }

    /**
     * @param landlordHouseInformation the landlordHouseInformation to set
     */
    public void setLandlordHouseInformation(HDBHouseEntity landlordHouseInformation) {
        this.landlordHouseInformation = landlordHouseInformation;
    }

    /**
     * @return the landlordTenancyAgreements
     */
    public List<LandlordTenancyAgreement> getLandlordTenancyAgreements() {
        return landlordTenancyAgreements;
    }

    /**
     * @param landlordTenancyAgreements the landlordTenancyAgreements to set
     */
    public void setLandlordTenancyAgreements(List<LandlordTenancyAgreement> landlordTenancyAgreements) {
        this.landlordTenancyAgreements = landlordTenancyAgreements;
    }



    /**
     * @return the isLandlord
     */
    public boolean isIsLandlord() {
        return isLandlord;
    }

    /**
     * @param isLandlord the isLandlord to set
     */
    public void setIsLandlord(boolean isLandlord) {
        this.isLandlord = isLandlord;
    }

    /**
     * @return the isTenant
     */
    public boolean isIsTenant() {
        return isTenant;
    }

    /**
     * @param isTenant the isTenant to set
     */
    public void setIsTenant(boolean isTenant) {
        this.isTenant = isTenant;
    }

    /**
     * @return the tenantSignature
     */
    public List<TenantTenancySignature> getTenantSignature() {
        return tenantSignature;
    }

    /**
     * @param tenantSignature the tenantSignature to set
     */
    public void setTenantSignature(List<TenantTenancySignature> tenantSignature) {
        this.tenantSignature = tenantSignature;
    }

    /**
     * @return the selectedTenancySign
     */
    public TenantTenancySignature getSelectedTenancySign() {
        return selectedTenancySign;
    }

    /**
     * @param selectedTenancySign the selectedTenancySign to set
     */
    public void setSelectedTenancySign(TenantTenancySignature selectedTenancySign) {
        this.selectedTenancySign = selectedTenancySign;
    }

}
