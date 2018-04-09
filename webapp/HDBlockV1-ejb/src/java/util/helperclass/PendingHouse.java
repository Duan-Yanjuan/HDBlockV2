/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;
import util.helperclass.Tenant;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author steph
 */
public class PendingHouse {
    private int requestId;
    private String identificationNo;
    private String type;
    private String landlord;
    
    
    public PendingHouse() {
    }

    public PendingHouse(int requestId, String identificationNo, String type, String landlord) {
        this.requestId = requestId;
        this.identificationNo = identificationNo;
        this.type = type;
        this.landlord = landlord;
    }
    
        /**
     * @return the requestId
     */
    public int getRequestId() {
        return requestId;
    }

    /**
     * @param requestId the requestId to set
     */
    public void setRequestId(int requestId) {
        this.requestId = requestId;
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
     * @return the requestId
     */
    public String getType() {
        return type;
    }

    /**
     * @param requestId the requestId to set
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
