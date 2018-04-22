/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.ICA;

import ejb.session.stateless.ICAControllerLocal;
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
@Named(value = "iCAUserManagedBean")
@RequestScoped
public class ICAUserManagedBean {

    @EJB(name = "ICAControllerLocal")
    private ICAControllerLocal iCAControllerLocal;

    /**
     * Creates a new instance of ICAUserManagedBean
     */
    private String userEmail;
    private String userPassword;

    public ICAUserManagedBean() {
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

    public void login(ActionEvent event) throws IOException {

        try {

            ICAStaffEntity staffInformation = iCAControllerLocal.staffLogin(userEmail, userPassword);
            FacesContext.getCurrentInstance().getExternalContext().redirect("ICAStaffHome.xhtml");

        } catch (InvalidLoginCredentialException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid login credential: " + ex.getMessage(), null));
        }

    }

    public void logout(ActionEvent event) throws IOException {

        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("ICAStaffLogin.xhtml");
    }

}
