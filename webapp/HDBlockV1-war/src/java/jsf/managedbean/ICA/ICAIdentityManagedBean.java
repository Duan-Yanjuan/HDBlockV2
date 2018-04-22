/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.ICA;

import ejb.session.stateless.ICAControllerLocal;
import entity.ICAIdentificationRecordEntity;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private boolean userStatusIsValid;
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
           System.out.println("************ selected user is" + selectedUser.getEmail());
           boolean statusIsApproved = iCAControllerLocal.processUserIdentity(selectedUser);
           DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
           if(statusIsApproved){
               
               userStatusIsValid = true;
      
                identityMessage = "User Identity " + selectedUser.getIdentificationNo() + " Has Been Approved and Endorsed";
                userRequest = iCAControllerLocal.retrieveUserWithPendingStatus();
                         System.out.println("*************** userstratus is valid" +  identityMessage);
               //ICAIdentificationRecordEntity identityHolder = iCAControllerLocal.retrieveAllIdentificationById(selectedUser.getIdentificationNo());
              // selectedUser.setIdentityValidityPeriod(df.format(identityHolder.getValidityPeriod()));
              // selectedUser.setPassType(identityHolder.getIdentificationType());            
               //make another call
               //identityMessage = "User Identity " + selectedUser.getIdentificationNo() + " Is VALID and Has Been ENDORSED.";
           }
            
        }catch(Exception ex){
            userStatusIsValid = false;
            identityMessage = "User Identity " + selectedUser.getIdentificationNo() + " Has Been REJECTED Due to " + ex.getMessage();
         
        }
        
    }
    
    public void finalProcessIdentity(String result){
        
        System.out.println("*********** RESULT " + result + selectedUser.getEmail());
         FacesContext context = FacesContext.getCurrentInstance();
        
          boolean processHasSucceeded = iCAControllerLocal.finalProcessUserIdentity(selectedUser.getIdentificationNo(), selectedUser.getUserType(), result);
        if(processHasSucceeded && result.equals("Approve")){
             userRequest = iCAControllerLocal.retrieveUserWithPendingStatus();
         context.addMessage(null, new FacesMessage("Successful",   "User Identity " + selectedUser.getIdentificationNo() + " Has Been ENDORSED."));
        }else if(processHasSucceeded && result.equals("Reject")){
             userRequest = iCAControllerLocal.retrieveUserWithPendingStatus();
          context.addMessage(null, new FacesMessage("Successful",   "Applicant Has Been Rejected"));
          
          
        }else{
           context.addMessage(null, new FacesMessage("ERROR",   "Error in Endorsing Identity. Please contact Admin"));
        }
               //make another call
            
          
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


    /**
     * @return the userStatusIsValid
     */
    public boolean isUserStatusIsValid() {
        return userStatusIsValid;
    }

    /**
     * @param userStatusIsValid the userStatusIsValid to set
     */
    public void setUserStatusIsValid(boolean userStatusIsValid) {
        this.userStatusIsValid = userStatusIsValid;
    }
    
}
