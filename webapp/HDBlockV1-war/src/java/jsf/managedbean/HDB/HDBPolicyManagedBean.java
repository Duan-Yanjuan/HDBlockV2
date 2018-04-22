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
import util.exception.CreateNewPolicyException;

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
    private HDBRentingPolicyEntity newPolicy;   
    
    public HDBPolicyManagedBean() {
        rentingPolicies = new ArrayList<>();
        newPolicy = new HDBRentingPolicyEntity();
    }
    
    @PostConstruct
    public void postConstruct(){    
        rentingPolicies = hDBControllerLocal.retrieveHDBRentingPolicies();
        selectedPolicy = rentingPolicies.get(0);
    }
    
        /**
     * @return the newPolicy
     */
    public HDBRentingPolicyEntity getNewPolicy() {
        return newPolicy;
    }

    /**
     * @param newPolicy the newPolicy to set
     */
    public void setNewPolicy(HDBRentingPolicyEntity newPolicy) {
        this.newPolicy = newPolicy;
    }
    
    public List<HDBRentingPolicyEntity> getRentingPolicies() {
        return rentingPolicies;
    }
    
    public void setRentingPolicies(List<HDBRentingPolicyEntity> rentingPolicies) {
        this.rentingPolicies = rentingPolicies;
    }
    
    
    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Updated Policy ID: ", ((HDBRentingPolicyEntity) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        HDBRentingPolicyEntity editedPolicy = (HDBRentingPolicyEntity) event.getObject();
        this.updatePolicy(editedPolicy);     
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
    
        for (HDBRentingPolicyEntity h : rentingPolicies) {
            if (policyCode.equals(h.getPolicyCode())) {
                result.add(h);
            }
        }
        return result;
    }

    public void createNewPolicy(){
       
        try{
          
            hDBControllerLocal.createNewPolicy(newPolicy);
            newPolicy = new HDBRentingPolicyEntity();
            
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The new policy is being created.", null));
      
        }
        catch(CreateNewPolicyException ex){
             
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, ex.getMessage(), null));
      
        }
       
    }
       
     public void updatePolicy(HDBRentingPolicyEntity editedPolicy){
         hDBControllerLocal.updatePolicy(editedPolicy);
     }  
    
}
