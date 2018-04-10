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
@XmlType(name = "houseAsset", propOrder = {
    "$class",
    "agreement"
    
   
})
public class UpdateTenancyAgreement {
    
    private String $class;
    private String agreement;

    public UpdateTenancyAgreement() {
    }

    
    public UpdateTenancyAgreement(String $class, String agreement) {
        this.$class = $class;
        this.agreement = agreement;
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
}
