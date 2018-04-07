/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDB;

import ejb.session.stateless.HDBControllerLocal;
import entity.ICAStaffEntity;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author David
 */
@Named(value = "hDBUserManagedBean")
@RequestScoped
public class HDBUserManagedBean {

    @EJB(name = "HDBControllerLocal")
    private HDBControllerLocal hDBControllerLocal;

    
    
    
    
    /**
     * Creates a new instance of HDBUserManagedBean
     */
    public HDBUserManagedBean() {
    }
    
     public void login (ActionEvent event) throws IOException    {
        
     
          
         
       
    }
    
     public void logout(ActionEvent event) throws IOException{
        
         ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ICAStaffLogin.xhtml");
     }

    
}
