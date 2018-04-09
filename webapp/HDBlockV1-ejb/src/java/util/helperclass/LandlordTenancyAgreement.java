/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;

import java.util.List;

/**
 *
 * @author David
 */
public class LandlordTenancyAgreement {
    
    
    private String tenancyAgreementId;
    private String dateCreated;
    private String startDate;
    private double securityDeposit;
    private double advanceRentalFee;
    private int rentalDuration;
    private double rentalFee;
    private List<Tenant> tenants;
    private String contractStatus;

    public LandlordTenancyAgreement() {
    }

    public LandlordTenancyAgreement(String tenancyAgreementId, String dateCreated, String startDate, double securityDeposit, double advanceRentalFee, int rentalDuration, double rentalFee, List<Tenant> tenants, String contractStatus) {
        this.tenancyAgreementId = tenancyAgreementId;
        this.dateCreated = dateCreated;
        this.startDate = startDate;
        this.securityDeposit = securityDeposit;
        this.advanceRentalFee = advanceRentalFee;
        this.rentalDuration = rentalDuration;
        this.rentalFee = rentalFee;
        this.tenants = tenants;
        this.contractStatus = contractStatus;
    }
    
   
    /**
     * @return the tenancyAgreementId
     */
    public String getTenancyAgreementId() {
        return tenancyAgreementId;
    }

    /**
     * @param tenancyAgreementId the tenancyAgreementId to set
     */
    public void setTenancyAgreementId(String tenancyAgreementId) {
        this.tenancyAgreementId = tenancyAgreementId;
    }

    /**
     * @return the dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the securityDeposit
     */
    public double getSecurityDeposit() {
        return securityDeposit;
    }

    /**
     * @param securityDeposit the securityDeposit to set
     */
    public void setSecurityDeposit(double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    /**
     * @return the advanceRentalFee
     */
    public double getAdvanceRentalFee() {
        return advanceRentalFee;
    }

    /**
     * @param advanceRentalFee the advanceRentalFee to set
     */
    public void setAdvanceRentalFee(double advanceRentalFee) {
        this.advanceRentalFee = advanceRentalFee;
    }

    /**
     * @return the rentalDuration
     */
    public int getRentalDuration() {
        return rentalDuration;
    }

    /**
     * @param rentalDuration the rentalDuration to set
     */
    public void setRentalDuration(int rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    /**
     * @return the tenants
     */
    public List<Tenant> getTenants() {
        return tenants;
    }

    /**
     * @param tenants the tenants to set
     */
    public void setTenants(List<Tenant> tenants) {
        this.tenants = tenants;
    }

    /**
     * @return the contractStatus
     */
    public String getContractStatus() {
        return contractStatus;
    }

    /**
     * @param contractStatus the contractStatus to set
     */
    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    /**
     * @return the rentalFee
     */
    public double getRentalFee() {
        return rentalFee;
    }

    /**
     * @param rentalFee the rentalFee to set
     */
    public void setRentalFee(double rentalFee) {
        this.rentalFee = rentalFee;
    }
    
    
    
    
    
}
