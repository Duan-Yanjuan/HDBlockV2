/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDB;

import ejb.session.stateless.HDBControllerLocal;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.helperclass.PendingHouse;

/**
 *
 * @author Steph
 */
@Named(value = "hDBHouseRequestManagedBean")
@ViewScoped
public class HDBHouseRequestManagedBean implements Serializable {

    @EJB(name = "HDBControllerLocal")
    private HDBControllerLocal hDBControllerLocal;

    /**
     * Creates a new instance of HDBHouseRequestManagedBean
     */
    
    private List<PendingHouse> houseRequest;
    private PendingHouse selectedHouse;
    private String validityMessage;
  
    
    
    public HDBHouseRequestManagedBean() {
        houseRequest = new ArrayList<>();
        selectedHouse = new PendingHouse();
    }
    
        
    @PostConstruct
    public void postConstruct(){
        
        houseRequest = hDBControllerLocal.retrieveHouseWithPendingStatus();
        
    }
    
      /**
     * @return the houseRequest
     */
    public List<PendingHouse> getHouseRequest() {
        return houseRequest;
    }

    /**
     * @param houseRequest the houseRequest to set
     */
    public void setHouseRequest(List<PendingHouse> houseRequest) {
        this.houseRequest = houseRequest;
    }

    /**
     * @return the selectedUser
     */
    public PendingHouse getSelectedHouse() {
        return selectedHouse;
    }

    /**
     * @param selectedUser the selectedUser to set
     */
    public void setSelectedHouse(PendingHouse selectedHouse) {
        System.out.println("************ selected house is" + selectedHouse.getIdentificationNo());
        this.selectedHouse = selectedHouse;
        processValidity();
    }
    
    /**
     * @return the validityMessage
     */
    public String getValidityMessage() {
        return validityMessage;
    }

    /**
     * @param validityMessage the validityMessage to set
     */
    public void setValidityMessage(String identityMessage) {
        this.validityMessage = validityMessage;
    }
    
       public void processValidity(){
        
        try{
            
           boolean statusIsApproved = hDBControllerLocal.processHouseValidity(selectedHouse);
          
           if(statusIsApproved){
               validityMessage = "Validity has been approved.";
               houseRequest = hDBControllerLocal.retrieveHouseWithPendingStatus();
           }
            
        }catch(Exception ex){
            validityMessage = "Validity has been rejected due to " + ex.getMessage();
         
        }
        
    }
       
       
           
    
}
