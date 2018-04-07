/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.ICA;

import ejb.session.stateless.ICAControllerLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.helperclass.PendingUser;

/**
 *
 * @author David
 */
@Named(value = "iCAIdentityManagedBean")
@ViewScoped
public class ICAIdentityManagedBean implements Serializable {

    @EJB(name = "ICAControllerLocal")
    private ICAControllerLocal iCAControllerLocal;

    /**
     * Creates a new instance of ICAIdentityManagedBean
     */
    
    private List<PendingUser> userRequest;
    private PendingUser selectedUser;
    private String identityMessage;
    
    public ICAIdentityManagedBean() {
        userRequest = new ArrayList<>();
        selectedUser = new PendingUser();
    }
    
    @PostConstruct
    public void postConstruct(){
        
        userRequest = iCAControllerLocal.retrieveUserWithPendingStatus();
        
    }

    /**
     * @return the userRequest
     */
    public List<PendingUser> getUserRequest() {
        return userRequest;
    }

    /**
     * @param userRequest the userRequest to set
     */
    public void setUserRequest(List<PendingUser> userRequest) {
        this.userRequest = userRequest;
    }

    /**
     * @return the selectedUser
     */
    public PendingUser getSelectedUser() {
        return selectedUser;
    }

    /**
     * @param selectedUser the selectedUser to set
     */
    public void setSelectedUser(PendingUser selectedUser) {
        System.out.println("************ selected user is" + selectedUser.getEmail());
        this.selectedUser = selectedUser;
        processIdentity();
    }
    
    public void processIdentity(){
        
        try{
            
           boolean statusIsApproved = iCAControllerLocal.processUserIdentity(selectedUser);
          
           if(statusIsApproved){
               identityMessage = "User Identity " + selectedUser.getIdentificationNo() + " Is VALID and Has Been ENDORSED.";
           }
            
        }catch(Exception ex){
            identityMessage = "User Identity " + selectedUser.getIdentificationNo() + " Has Been REJECTED Due to " + ex.getMessage();
         
        }
        
    }
    
   /* public void processIdentity(ActionEvent event) throws IOException{
        
     /*   try{
            
           boolean statusIsApproved = iCAControllerLocal.processUserIdentity(selectedUser);
          
           if(statusIsApproved){
               identityMessage = "Identity has been approved.";
           }
            
        }catch(Exception ex){
            identityMessage = "Identity has been rejected due to " + ex.getMessage();
          // FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Identity has been rejected" + ex.getMessage(), null));
        }
     
     System.out.println("************ YOUR SELECTED VLAUYE IS " + selectedUser.getIdentificationNo());
        
        
    }*/

    /**
     * @return the identityMessage
     */
    public String getIdentityMessage() {
        return identityMessage;
    }

    /**
     * @param identityMessage the identityMessage to set
     */
    public void setIdentityMessage(String identityMessage) {
        this.identityMessage = identityMessage;
    }
    
}
