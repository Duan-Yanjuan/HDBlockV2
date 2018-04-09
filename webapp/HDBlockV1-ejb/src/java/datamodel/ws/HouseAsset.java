/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws;
import entity.ICAIdentificationRecordEntity;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;


/**
 *
 * @author steph
 */

@XmlRootElement
@XmlType(name = "houseAsset", propOrder = {
    "$class",
    "id",
    "type",
    "status",
    "landlord"
})
public class HouseAsset {
    private String $class;
    private String id;
    private String type;
    private String status;
    private String landlord;


    
    public HouseAsset() {
    }

    public HouseAsset(String $class, String id, String type, String status, String landlord) {
        this.$class = $class;
        this.id = id;
        this.type = type;
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
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
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
