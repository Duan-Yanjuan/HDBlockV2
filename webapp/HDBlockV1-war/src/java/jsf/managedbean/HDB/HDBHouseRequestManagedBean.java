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

/**
 *
 * @author David
 */
@Named(value = "hDBHouseRequestManagedBean")
@ViewScoped
public class HDBHouseRequestManagedBean implements Serializable {

    @EJB(name = "HDBControllerLocal")
    private HDBControllerLocal hDBControllerLocal;

    /**
     * Creates a new instance of HDBHouseRequestManagedBean
     */
    
    
    
    public HDBHouseRequestManagedBean() {
    }
    
}
