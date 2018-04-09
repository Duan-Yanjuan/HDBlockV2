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
public class Tenant {
    
    
    private String firstName;
    private String lastName;
    private String email;
    private String tenantStatus;
    private String ic;

    public Tenant() {
    }

    public Tenant(String firstName, String lastName, String email, String tenantStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tenantStatus = tenantStatus;
    }

    public Tenant(String firstName, String lastName, String email, String tenantStatus, String ic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tenantStatus = tenantStatus;
        this.ic = ic;
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
     * @return the tenantStatus
     */
    public String getTenantStatus() {
        return tenantStatus;
    }

    /**
     * @param tenantStatus the tenantStatus to set
     */
    public void setTenantStatus(String tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    /**
     * @return the ic
     */
    public String getIc() {
        return ic;
    }

    /**
     * @param ic the ic to set
     */
    public void setIc(String ic) {
        this.ic = ic;
    }

  
    
    
}
