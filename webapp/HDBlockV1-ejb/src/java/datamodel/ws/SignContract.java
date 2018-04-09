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
@XmlType(name = "signContract", propOrder = {
    "$class",
    "signature",
})

public class SignContract {
    
    private String $class;
    private String signature;

    public SignContract() {
    }

    public SignContract(String $class, String signature) {
        this.$class = $class;
        this.signature = signature;
    }
    
//    
//            "$class": "org.acme.hdb.SignTenancyAgreement",
//  "signature": "53101211-12109042018_0"

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
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
}
