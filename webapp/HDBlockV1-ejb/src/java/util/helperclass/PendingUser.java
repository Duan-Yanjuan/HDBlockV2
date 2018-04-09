/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;

import java.util.Date;

/**
 *
 * @author David
 */
public class PendingUser {
    
    private int requestId;
    private String identificationNo;
    private String email;
    private String firstName;
    private String lastName;
    private String dob;
    private String userType;
    private String identityValidityPeriod;
    private String passType;

    public PendingUser() {
    }

    public PendingUser(int requestId, String identificationNo, String email, String firstName, String lastName, String dob, String userType) {
        this.requestId = requestId;
        this.identificationNo = identificationNo;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.userType = userType;
    }

    
    /**
     * @return the identificationNo
     */
    public String getIdentificationNo() {
        return identificationNo;
    }

    /**
     * @param identificationNo the identificationNo to set
     */
    public void setIdentificationNo(String identificationNo) {
        this.identificationNo = identificationNo;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the requestId
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return the identityValidityPeriod
     */
    public String getIdentityValidityPeriod() {
        return identityValidityPeriod;
    }

    /**
     * @param identityValidityPeriod the identityValidityPeriod to set
     */
    public void setIdentityValidityPeriod(String identityValidityPeriod) {
        this.identityValidityPeriod = identityValidityPeriod;
    }

    /**
     * @return the passType
     */
    public String getPassType() {
        return passType;
    }

    /**
     * @param passType the passType to set
     */
    public void setPassType(String passType) {
        this.passType = passType;
    }

 
 
    
}
