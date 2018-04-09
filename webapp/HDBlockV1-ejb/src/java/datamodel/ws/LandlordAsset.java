/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author David
 */

@XmlRootElement
@XmlType(name = "landlordAsset", propOrder = {
    "$class",
    "id",
    "email",
    "firstName",
    "lastName",
    "ICStatus",
    "DOB"
})
public class LandlordAsset {
    private String $class;
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String ICStatus;
    private String DOB;
    
    
    public LandlordAsset() {
    }

    public LandlordAsset(String $class, String id, String email, String firstName, String lastName, String ICStatus , String DOB) {
        this.$class = $class;
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ICStatus = ICStatus;
        this.DOB = DOB;
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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
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
     * @return the ICStatus
     */
    public String getICStatus() {
        return ICStatus;
    }

    /**
     * @param ICStatus the ICStatus to set
     */
    public void setICStatus(String ICStatus) {
        this.ICStatus = ICStatus;
    }

    /**
     * @return the DOB
     */
    public String getDOB() {
        return DOB;
    }

    /**
     * @param DOB the DOB to set
     */
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

   


}
