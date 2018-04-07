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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import util.security.CryptographicHelper;

/**
 *
 * @author David
 */
@Entity
public class HDBlockUserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(length = 9)
    private String identificationNo;
    @Column(unique = true)
    private String email;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String phoneNo;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Column(nullable =false)     
    private String userType;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String password;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    @Column (nullable = false)
    private boolean statusIsValid;
    
    @OneToOne(mappedBy = "houseOwner")
    private HDBHouseEntity house;

    public HDBlockUserEntity() {
           
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
        this.statusIsValid = false; //by default false until the authority accepted the application. 
    }

    public HDBlockUserEntity(String identificationNo, String email, String firstName, String lastName, Date dateOfBirth, String userType, String password) {
        this();
        this.identificationNo = identificationNo;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.userType = userType;
        this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
    }

    public HDBlockUserEntity(String identificationNo, String email, String firstName, String lastName, String phoneNo, Date dateOfBirth, String userType, String password) {
        this();
        this.identificationNo = identificationNo;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNo = phoneNo;
        this.dateOfBirth = dateOfBirth;
        this.userType = userType;
        this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
    }
    
    
    
    

    @Override
    public String toString() {
        return "entity.HDBlockUserEntity[ id=" + getEmail() + " ]";
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        if(password != null)
        {
            this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        }
        else
        {
            this.password = null;
        }
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
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
     * @return the house
     */
    public HDBHouseEntity getHouse() {
        return house;
    }

    /**
     * @param house the house to set
     */
    public void setHouse(HDBHouseEntity house) {
        this.house = house;
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
     * @return the phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     * @param phoneNo the phoneNo to set
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    
}
