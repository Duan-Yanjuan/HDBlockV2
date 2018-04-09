/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;

/**
 *
 * @author David
 */
public class TenantTenancyAgreement {
    
    private boolean tenantHasSigned;
    private String tenantId;
    private String tenancyAgreementId;
//    private String signatureId;
    private int rentalDuration;
    private double securityDeposit;
    private double advanceRentalFee;
    private double rentalFee;
    private String startDate;
    private String dateCreated;
    private String landlordName;
    private String houseInformation;

    /**
     * @return the tenantHasSigned
     */
    public boolean isTenantHasSigned() {
        return tenantHasSigned;
    }

    /**
     * @param tenantHasSigned the tenantHasSigned to set
     */
    public void setTenantHasSigned(boolean tenantHasSigned) {
        this.tenantHasSigned = tenantHasSigned;
    }

    /**
     * @return the tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * @param tenantId the tenantId to set
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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
     * @return the landlordName
     */
    public String getLandlordName() {
        return landlordName;
    }

    /**
     * @param landlordName the landlordName to set
     */
    public void setLandlordName(String landlordName) {
        this.landlordName = landlordName;
    }

    /**
     * @return the houseInformation
     */
    public String getHouseInformation() {
        return houseInformation;
    }

    /**
     * @param houseInformation the houseInformation to set
     */
    public void setHouseInformation(String houseInformation) {
        this.houseInformation = houseInformation;
    }
    
    
    
    
}
