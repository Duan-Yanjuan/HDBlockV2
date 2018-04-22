/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.ICA;

import ejb.session.stateless.ICAControllerLocal;
import entity.ICAIdentificationRecordEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author David
 */
@Named(value = "iCAForeignIdentityManagedBean")
@ViewScoped
public class ICAForeignIdentityManagedBean implements Serializable{

    @EJB(name = "ICAControllerLocal")
    private ICAControllerLocal iCAControllerLocal;

    private List<ICAIdentificationRecordEntity> records;
    private ICAIdentificationRecordEntity selectedRecord;
   
    
    
    
    public ICAForeignIdentityManagedBean() {
         records = new ArrayList<>();
         selectedRecord = new ICAIdentificationRecordEntity();
    }
    
    @PostConstruct
    public void postConstruct(){
      
         records = iCAControllerLocal.retrieveAllIdentification();
        
    }

    /**
     * @return the records
     */
    public List<ICAIdentificationRecordEntity> getRecords() {
        return records;
    }

    /**
     * @param records the records to set
     */
    public void setRecords(List<ICAIdentificationRecordEntity> records) {
        this.records = records;
    }

    /**
     * @return the selectedRecord
     */
    public ICAIdentificationRecordEntity getSelectedRecord() {
        return selectedRecord;
    }

    /**
     * @param selectedRecord the selectedRecord to set
     */
    public void setSelectedRecord(ICAIdentificationRecordEntity selectedRecord) {
        this.selectedRecord = selectedRecord;
    }
    
    
    public void revokeIdentity() {
        System.out.println("********* IDNEITYT TO BE REVOKE IS " + selectedRecord.getFullname());
    }
    
    
    
    
}
