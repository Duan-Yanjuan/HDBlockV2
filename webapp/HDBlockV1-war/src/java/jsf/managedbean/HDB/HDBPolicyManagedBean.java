/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDB;

import ejb.session.stateless.HDBControllerLocal;
import entity.HDBRentingPolicyEntity;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author Steph
 */
@Named(value = "hDBPolicyManagedBean")
@ViewScoped
public class HDBPolicyManagedBean implements Serializable {
    @EJB(name = "HDBControllerLocal")
    private HDBControllerLocal hDBControllerLocal;
    /**
     * Creates a new instance of HDBPolicyManagedBean
     */
    
    private List<HDBRentingPolicyEntity> rentingPolicies;
    private HDBRentingPolicyEntity selectedPolicy;
    
    public HDBPolicyManagedBean() {
        rentingPolicies = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){    
        rentingPolicies = hDBControllerLocal.retrieveHDBRentingPolicies();
        selectedPolicy = rentingPolicies.get(0);
    }
    
    public List<HDBRentingPolicyEntity> getRentingPolicies() {
        return rentingPolicies;
    }
    
    public void setRentingPolicies(List<HDBRentingPolicyEntity> rentingPolicies) {
        this.rentingPolicies = rentingPolicies;
    }
    
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Policy Edited", ((HDBRentingPolicyEntity) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((HDBRentingPolicyEntity) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
     
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    
    /**
     * @param policyCode
     * @return the policyCode
     */
    public ArrayList<HDBRentingPolicyEntity> getPoliciesByPolicyCode(String policyCode) {
        // Store all results
        ArrayList<HDBRentingPolicyEntity> result = new ArrayList<HDBRentingPolicyEntity>();
        // Check through each existing policy
        for (HDBRentingPolicyEntity h : rentingPolicies) {
            // If the policy contains the code we want, store as a result
            if (policyCode.equals(h.getPolicyCode())) {
                result.add(h);
            }
        }
        return result;
    }


    
}
