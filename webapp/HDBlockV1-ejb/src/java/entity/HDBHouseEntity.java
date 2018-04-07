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
import javax.persistence.OneToOne;

/**
 *
 * @author David
 */
@Entity
public class HDBHouseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseId; //This is just an incremental Id
    @Column(length = 64, nullable = false)
    private String address;
    @Column(length = 8, nullable = false)
    private String unitNumber;
    @Column(length = 6, nullable = false)
    private String postalCode;
    @Column(nullable = false)
    private String flatType; //example 2 room flat or 3 room flat
    @Column(nullable = false)
    private String houseImage;
    @Column(nullable = false)
    private double rentalPrice;
    @Column(nullable = false)
    private boolean statusIsValid;
    
    @OneToOne
    private HDBlockUserEntity houseOwner;

    public HDBHouseEntity() {
        this.statusIsValid = false;
    }

    public HDBHouseEntity(String address, String unitNumber, String postalCode, String flatType, String houseImage, double rentalPrice, HDBlockUserEntity houseOwner) {
        this();      
        this.address = address;
        this.unitNumber = unitNumber;
        this.postalCode = postalCode;
        this.flatType = flatType;
        this.houseImage = houseImage;
        this.rentalPrice = rentalPrice;
        this.houseOwner = houseOwner;
    }
    
    public Long getId() {
        return houseId;
    }

    public void setId(Long houseId) {
        this.houseId = houseId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (houseId != null ? houseId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HDBHouseEntity)) {
            return false;
        }
        HDBHouseEntity other = (HDBHouseEntity) object;
        if ((this.houseId == null && other.houseId != null) || (this.houseId != null && !this.houseId.equals(other.houseId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.HDBHouseEntity[ id=" + houseId + " ]";
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
     * @return the unitNumber
     */
    public String getUnitNumber() {
        return unitNumber;
    }

    /**
     * @param unitNumber the unitNumber to set
     */
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
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
     * @return the houseImage
     */
    public String getHouseImage() {
        return houseImage;
    }

    /**
     * @param houseImage the houseImage to set
     */
    public void setHouseImage(String houseImage) {
        this.houseImage = houseImage;
    }

    /**
     * @return the rentalPrice
     */
    public double getRentalPrice() {
        return rentalPrice;
    }

    /**
     * @param rentalPrice the rentalPrice to set
     */
    public void setRentalPrice(double rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    /**
     * @return the statusIsValid
     */
    public boolean isStatusIsValid() {
        return statusIsValid;
    }

    /**
     * @param statusIsValid the statusIsValid to set
     */
    public void setStatusIsValid(boolean statusIsValid) {
        this.statusIsValid = statusIsValid;
    }

    /**
     * @return the houseOwner
     */
    public HDBlockUserEntity getHouseOwner() {
        return houseOwner;
    }

    /**
     * @param houseOwner the houseOwner to set
     */
    public void setHouseOwner(HDBlockUserEntity houseOwner) {
        this.houseOwner = houseOwner;
    }

}
