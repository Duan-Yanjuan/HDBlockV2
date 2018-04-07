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
@XmlType(name = "tenantAssetUpdate", propOrder = {
    "$class",
    "id",
    "ICStatus",

})
public class TenantAssetUpdate {
    
    private String $class;
    private String id;
    private String ICStatus;

    public TenantAssetUpdate() {
    }

    public TenantAssetUpdate(String $class, String id, String ICStatus) {
        this.$class = $class;
        this.id = id;
        this.ICStatus = ICStatus;
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
    
    
}
