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
public class TenantTenancySignature {
    
 
    private String signatureId;
    private boolean isSigned;
    private String tenant;
    private String agreement;
    private TenantTenancyAgreement tenantTa;

    public TenantTenancySignature() {
    }

    public TenantTenancySignature( String signatureId, boolean isSigned, String tenant, String agreement) {
   
        this.signatureId = signatureId;
        this.isSigned = isSigned;
        this.tenant = tenant;
        this.agreement = agreement;
    }

    public TenantTenancySignature(boolean isSigned, String tenant, String agreement) {
        this.isSigned = isSigned;
        this.tenant = tenant;
        this.agreement = agreement;
    }
    
    
    
    
  //  "$class": "org.acme.hdb.TenancySignature",
//        "signatureId": "53101211-12109042018_0",
//        "isSigned": false,
//        "tenant": "resource:org.acme.hdb.Tenant#S9876541G",
//        "agreement": "resource:org.acme.hdb.TenancyAgreement#53101211-12109042018"


    /**
     * @return the signatureId
     */
    public String getSignatureId() {
        return signatureId;
    }

    /**
     * @param signatureId the signatureId to set
     */
    public void setSignatureId(String signatureId) {
        this.signatureId = signatureId;
    }

    /**
     * @return the isSigned
     */
    public boolean isIsSigned() {
        return isSigned;
    }

    /**
     * @param isSigned the isSigned to set
     */
    public void setIsSigned(boolean isSigned) {
        this.isSigned = isSigned;
    }

    /**
     * @return the tenant
     */
    public String getTenant() {
        return tenant;
    }

    /**
     * @param tenant the tenant to set
     */
    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    /**
     * @return the agreement
     */
    public String getAgreement() {
        return agreement;
    }

    /**
     * @param agreement the agreement to set
     */
    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }

    /**
     * @return the tenantTa
     */
    public TenantTenancyAgreement getTenantTa() {
        return tenantTa;
    }

    /**
     * @param tenantTa the tenantTa to set
     */
    public void setTenantTa(TenantTenancyAgreement tenantTa) {
        this.tenantTa = tenantTa;
    }
}
