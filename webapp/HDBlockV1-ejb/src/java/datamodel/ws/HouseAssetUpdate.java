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
 * @author steph
 */
@XmlRootElement
@XmlType(name = "HouseAssetUpdate", propOrder = {
    "$class",
    "id",
    "status",

})
public class HouseAssetUpdate {
    
    private String $class;
    private String id;
    private String status;
    
    public HouseAssetUpdate() {
    }

    public HouseAssetUpdate(String $class, String id, String status) {
        this.$class = $class;
        this.id = id;
        this.status = status;
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
    
    
    
}
