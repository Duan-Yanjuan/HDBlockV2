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
    "houseId",
    "address",
    "type",
    "landlord",
})
public class HouseAsset {
    private String $class;
    private String houseId;
    private String address;
    private String type;
    private String landlord;
    
    public HouseAsset() {
    }

    public HouseAsset(String $class, String houseId, String address, String type, String landlord) {
        this.$class = $class;
        this.houseId = houseId;
        this.address = address;
        this.type = type;
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
     * @return the houseId
     */
    public String getHouseId() {
        return houseId;
    }

    /**
     * @param houseId the houseId to set
     */
    public void setHouseId(String houseId) {
        this.houseId = houseId;
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