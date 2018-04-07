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
import util.security.CryptographicHelper;

/**
 *
 * @author David
 */
@Entity
public class HDBStaffEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String userName;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String password;
    @Column(columnDefinition = "CHAR(32) NOT NULL")
    private String salt;
    @Column(length = 32, nullable = false)
    private String firstName;
    @Column(length = 32, nullable = false)
    private String lastName;

    public HDBStaffEntity() {
        this.salt = CryptographicHelper.getInstance().generateRandomString(32);
    }

    public HDBStaffEntity(String userName, String password, String firstName, String lastName) {
        this();
        this.userName = userName;
        this.password = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + this.salt));
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "entity.HDBStaffEntity[ id=" + getUserName() + " ]";
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
        this.password = password;
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

}
