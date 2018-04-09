/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author David
 */

@XmlRootElement
@XmlType(name = "landlordAssetUpdate", propOrder = {
    "$class",
    "status",
    "landlord",

})
public class LandlordAssetUpdate {
    
     private String $class;
     private String status;
     private String landlord;

    public LandlordAssetUpdate() {
    }

    public LandlordAssetUpdate(String $class, String status, String landlord) {
        this.$class = $class;
        this.status = status;
        this.landlord = landlord;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the landlord
     */
    public String getLandlord() {
        return landlord;
    }

    /**
     * @param landlord the landlord to set
     */
    public void setLandlord(String landlord) {
        this.landlord = landlord;
    }
     
     

    
}
