/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author David
 */
@Entity
public class HDBHouseOwnerRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String unitNo;
    private String postalCode;
    private String flatType;
    private String flatOwner;
    private String NRIC;

    public HDBHouseOwnerRecordEntity() {
    }

    public HDBHouseOwnerRecordEntity(String address, String unitNo, String postalCode, String flatType, String flatOwner, String NRIC) {
        this.address = address;
        this.unitNo = unitNo;
        this.postalCode = postalCode;
        this.flatType = flatType;
        this.flatOwner = flatOwner;
        this.NRIC = NRIC;
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
        if (!(object instanceof HDBHouseOwnerRecordEntity)) {
            return false;
        }
        HDBHouseOwnerRecordEntity other = (HDBHouseOwnerRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.HDBHouseOwnerRecordEntity[ id=" + id + " ]";
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the unitNo
     */
    public String getUnitNo() {
        return unitNo;
    }

    /**
     * @param unitNo the unitNo to set
     */
    public void setUnitNo(String unitNo) {
        this.unitNo = unitNo;
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
     * @return the flatOwner
     */
    public String getFlatOwner() {
        return flatOwner;
    }

    /**
     * @param flatOwner the flatOwner to set
     */
    public void setFlatOwner(String flatOwner) {
        this.flatOwner = flatOwner;
    }

    /**
     * @return the NRIC
     */
    public String getNRIC() {
        return NRIC;
    }

    /**
     * @param NRIC the NRIC to set
     */
    public void setNRIC(String NRIC) {
        this.NRIC = NRIC;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
}
