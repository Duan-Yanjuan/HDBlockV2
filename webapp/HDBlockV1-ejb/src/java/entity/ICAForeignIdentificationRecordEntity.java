/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author David
 */
@Entity
public class ICAForeignIdentificationRecordEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //this is just an incremental Id
    @Column(length = 9, nullable = false) 
    private String nric;
    @Column(length = 32, nullable = false) 
    private String fullname;
    @Temporal(TemporalType.DATE)
    @Column( nullable = false) 
    private Date issuedDate;
    @Temporal(TemporalType.DATE)
    @Column( nullable = false) 
    private Date validityPeriod;
    @Temporal(TemporalType.DATE)
    @Column( nullable = false) 
    private Date dateOfBirth;
    @Column(nullable = false) 
    private String identificationType;
    @Column(nullable = false)
    private boolean isActive;

    public ICAForeignIdentificationRecordEntity() {
        this.isActive = true; // by default when the the identifacation pass is created it will always be active.
    }

    public ICAForeignIdentificationRecordEntity(String nric, String fullname, Date issuedDate, Date validityPeriod, Date dateOfBirth, String identificationType) {
        this();
        this.nric = nric;
        this.fullname = fullname;
        this.issuedDate = issuedDate;
        this.validityPeriod = validityPeriod;
        this.dateOfBirth = dateOfBirth;
        this.identificationType = identificationType;
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
        if (!(object instanceof ICAForeignIdentificationRecordEntity)) {
            return false;
        }
        ICAForeignIdentificationRecordEntity other = (ICAForeignIdentificationRecordEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ICAForeignIdentificationRecordEntity[ id=" + id + " ]";
    }

    /**
     * @return the nric
     */
    public String getNric() {
        return nric;
    }

    /**
     * @param nric the nric to set
     */
    public void setNric(String nric) {
        this.nric = nric;
    }

    /**
     * @return the issuedDate
     */
    public Date getIssuedDate() {
        return issuedDate;
    }

    /**
     * @param issuedDate the issuedDate to set
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * @return the validityPeriod
     */
    public Date getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(Date validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    /**
     * @return the identificationType
     */
    public String getIdentificationType() {
        return identificationType;
    }

    /**
     * @param identificationType the identificationType to set
     */
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    /**
     * @return the isActive
     */
    public boolean isIsActive() {
        return isActive;
    }

    /**
     * @param isActive the isActive to set
     */
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
}
