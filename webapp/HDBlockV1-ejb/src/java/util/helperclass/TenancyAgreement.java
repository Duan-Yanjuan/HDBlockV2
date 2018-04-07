/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;

import java.util.Date;
import java.util.List;

/**
 *
 * @author David
 */
public class TenancyAgreement {
    
    
    private String agreementId;
    private Date dateCreated;
    private Date rentalStartDate;
    private Double rentalFee;
    private Double advanceRentalFeePaid;
    private Double securityDepositPaid;
    private String tenancyStatus; //Pending, Active, signed , invalid,  expired
   /* private Landlord landlord;
    private List<Tenant> tenants;
    private House houseRented;*/
    private String landlordEmail;
    private String  houseId;
    private List<Tenant> tenants;

    public TenancyAgreement() {
    }

    public TenancyAgreement(String agreementId, Date dateCreated, Date rentalStartDate, Double rentalFee, Double advanceRentalFeePaid, Double securityDepositPaid, String tenancyStatus, String landlordEmail, String houseId, List<Tenant> tenants) {
        this.agreementId = agreementId;
        this.dateCreated = dateCreated;
        this.rentalStartDate = rentalStartDate;
        this.rentalFee = rentalFee;
        this.advanceRentalFeePaid = advanceRentalFeePaid;
        this.securityDepositPaid = securityDepositPaid;
        this.tenancyStatus = tenancyStatus;
        this.landlordEmail = landlordEmail;
        this.houseId = houseId;
        this.tenants = tenants;
    }
   
    /**
     * @return the agreementId
     */
    public String getAgreementId() {
        return agreementId;
    }

    /**
     * @param agreementId the agreementId to set
     */
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the rentalStartDate
     */
    public Date getRentalStartDate() {
        return rentalStartDate;
    }

    /**
     * @param rentalStartDate the rentalStartDate to set
     */
    public void setRentalStartDate(Date rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    /**
     * @return the rentalFee
     */
    public Double getRentalFee() {
        return rentalFee;
    }

    /**
     * @param rentalFee the rentalFee to set
     */
    public void setRentalFee(Double rentalFee) {
        this.rentalFee = rentalFee;
    }

    /**
     * @return the advanceRentalFeePaid
     */
    public Double getAdvanceRentalFeePaid() {
        return advanceRentalFeePaid;
    }

    /**
     * @param advanceRentalFeePaid the advanceRentalFeePaid to set
     */
    public void setAdvanceRentalFeePaid(Double advanceRentalFeePaid) {
        this.advanceRentalFeePaid = advanceRentalFeePaid;
    }

    /**
     * @return the securityDepositPaid
     */
    public Double getSecurityDepositPaid() {
        return securityDepositPaid;
    }

    /**
     * @param securityDepositPaid the securityDepositPaid to set
     */
    public void setSecurityDepositPaid(Double securityDepositPaid) {
        this.securityDepositPaid = securityDepositPaid;
    }

    /**
     * @return the tenancyStatus
     */
    public String getTenancyStatus() {
        return tenancyStatus;
    }

    /**
     * @param tenancyStatus the tenancyStatus to set
     */
    public void setTenancyStatus(String tenancyStatus) {
        this.tenancyStatus = tenancyStatus;
    }

    /**
     * @return the landlordEmail
     */
    public String getLandlordEmail() {
        return landlordEmail;
    }

    /**
     * @param landlordEmail the landlordEmail to set
     */
    public void setLandlordEmail(String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }

    /**
     * @return the houseId
     */
    public String getHouseId() {
        return houseId;
    }

    /**
     * @param houseId the houseId to set
     */
    public void setHouseId(String houseId) {
        this.houseId = houseId;
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
    
    
    
    
    
    
}
