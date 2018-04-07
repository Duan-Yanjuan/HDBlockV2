/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author David
 */
@Entity
public class HDBRentingPolicyEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //this is just an incremental ID 
    @Column(length = 8, nullable = false)
    private String policyCode;
    @Column(nullable = false)
    private String flatType;
    @Column(nullable = false)
    private String rentingType; //full house or bedroom
    @Column(nullable = false)
    private int maxNumofTenantsAllowed;

    public HDBRentingPolicyEntity() {
    }

    public HDBRentingPolicyEntity(String policyCode, String flatType, String rentingType, int maxNumofTenantsAllowed) {
        this.policyCode = policyCode;
        this.flatType = flatType;
        this.rentingType = rentingType;
        this.maxNumofTenantsAllowed = maxNumofTenantsAllowed;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HDBRentingPolicyEntity)) {
            return false;
        }
        HDBRentingPolicyEntity other = (HDBRentingPolicyEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.HDBRentingPolicyEntity[ id=" + id + " ]";
    }

    /**
     * @return the policyCode
     */
    public String getPolicyCode() {
        return policyCode;
    }

    /**
     * @param policyCode the policyCode to set
     */
    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }

    /**
     * @return the flatType
     */
    public String getFlatType() {
        return flatType;
    }

    /**
     * @param flatType the flatType to set
     */
    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    /**
     * @return the rentingType
     */
    public String getRentingType() {
        return rentingType;
    }

    /**
     * @param rentingType the rentingType to set
     */
    public void setRentingType(String rentingType) {
        this.rentingType = rentingType;
    }

    /**
     * @return the maxNumofTenantsAllowed
     */
    public int getMaxNumofTenantsAllowed() {
        return maxNumofTenantsAllowed;
    }

    /**
     * @param maxNumofTenantsAllowed the maxNumofTenantsAllowed to set
     */
    public void setMaxNumofTenantsAllowed(int maxNumofTenantsAllowed) {
        this.maxNumofTenantsAllowed = maxNumofTenantsAllowed;
    }
    
}
