/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBlockUserEntity;
import javafx.event.ActionEvent;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import util.exception.CreateNewUserException;

/**
 *
 * @author David
 */
@Named(value = "registrationManagedBean")
@RequestScoped
public class RegistrationManagedBean {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    /**
     * Creates a new instance of RegistrationManagedBean
     */
    
    private HDBlockUserEntity newUsers;   
    
    
    public RegistrationManagedBean() {
    
        newUsers = new HDBlockUserEntity();
    }

    /**
     * @return the newUsers
     */
    public HDBlockUserEntity getNewUsers() {
        return newUsers;
    }

    /**
     * @param newUsers the newUsers to set
     */
    public void setNewUsers(HDBlockUserEntity newUsers) {
        this.newUsers = newUsers;
    }
    
    public void registerAccount(){
        
        try{
          
            hDBlockUserEntityControllerLocal.registerAccount(newUsers);
            newUsers = new HDBlockUserEntity();
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Thank You for Registering With Us. We will email you, once the account is approved.", null));
      
        }
        catch(CreateNewUserException ex){
             
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
      
        }
       
    }
        
    
}
