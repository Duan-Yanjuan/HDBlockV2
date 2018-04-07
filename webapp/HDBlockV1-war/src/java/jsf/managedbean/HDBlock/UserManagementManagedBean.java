/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBlockUserEntity;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author David
 */
@Named(value = "userManagementManagedBean")
@ViewScoped
public class UserManagementManagedBean implements Serializable {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    
    private String userEmail;
    private String userPassword;
    
    /**
     * Creates a new instance of UserManagementManagedBean
     */
    public UserManagementManagedBean() {
    }
    
     public void login(ActionEvent event) throws IOException
    {
        try
        {
            HDBlockUserEntity currentUserInformation = hDBlockUserEntityControllerLocal.userLogin(userEmail, userPassword);
            
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userIsLogin", true);
            System.err.println("*********user type is " + currentUserInformation.getUserType());
            
            if(currentUserInformation.getUserType().equals("tenant")){
               FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isTenant", true);
            }else if(currentUserInformation.getUserType().equals("landlord")){
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLandlord", true);
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userInformation", currentUserInformation);
            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");            
        }
        catch(InvalidLoginCredentialException ex)
        {
          //  ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }
    }
     
     public void logout(ActionEvent event) throws IOException{
        
         ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("Login.xhtml");
     }

    /**
     * @return the userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * @param userEmail the userEmail to set
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword the userPassword to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    
}
