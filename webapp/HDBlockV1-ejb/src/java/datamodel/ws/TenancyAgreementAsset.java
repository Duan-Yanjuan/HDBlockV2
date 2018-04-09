/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws;

import java.util.Date;

/**
 *
 * @author David
 */
public class TenancyAgreementAsset {
    
    private String $class;
    private String agreementId;
    private Date startDate;
    private int duration;
    private double securityDeposit;
    private double advanceRentalFee;
    private double rentalFee;
    private String[] tenants;
    private String house;

    public TenancyAgreementAsset() {
    }

    public TenancyAgreementAsset(String $class, String agreementId, Date startDate, int duration, double securityDeposit, double advanceRentalFee, double rentalFee, String[] tenants, String house) {
        this.$class = $class;
        this.agreementId = agreementId;
        this.startDate = startDate;
        this.duration = duration;
        this.securityDeposit = securityDeposit;
        this.advanceRentalFee = advanceRentalFee;
        this.rentalFee = rentalFee;
        this.tenants = tenants;
        this.house = house;
    }
    
    

    /**
     * @return the $class
     */
    public String get$class() {
        return $class;
    }

    /**
     * @param $class the $class to set
     */
    public void set$class(String $class) {
        this.$class = $class;
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
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
     * @return the tenants
     */
    public String[] getTenants() {
        return tenants;
    }

    /**
     * @param tenants the tenants to set
     */
    public void setTenants(String[] tenants) {
        this.tenants = tenants;
    }

    /**
     * @return the house
     */
    public String getHouse() {
        return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(String house) {
        this.house = house;
    }
    
    
    
}
