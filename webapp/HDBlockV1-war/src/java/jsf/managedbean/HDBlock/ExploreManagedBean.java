/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBHouseEntity;
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
@Named(value = "exploreManagedBean")
@ViewScoped
public class ExploreManagedBean implements Serializable  {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    /**
     * Creates a new instance of ExploreManagedBean
     */
    
    
    private List<HDBHouseEntity> housesForBrowsing;
    private HDBHouseEntity selectedHouse;
    
    
    public ExploreManagedBean() {
        housesForBrowsing = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct(){
        
      housesForBrowsing =  gethDBlockUserEntityControllerLocal().retrieveAllAvailableHouse();
    }

    /**
     * @return the hDBlockUserEntityControllerLocal
     */
    public HDBlockUserEntityControllerLocal gethDBlockUserEntityControllerLocal() {
        return hDBlockUserEntityControllerLocal;
    }

    /**
     * @param hDBlockUserEntityControllerLocal the hDBlockUserEntityControllerLocal to set
     */
    public void sethDBlockUserEntityControllerLocal(HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal) {
        this.hDBlockUserEntityControllerLocal = hDBlockUserEntityControllerLocal;
    }

    /**
     * @return the housesForBrowsing
     */
    public List<HDBHouseEntity> getHousesForBrowsing() {
        return housesForBrowsing;
    }

    /**
     * @param housesForBrowsing the housesForBrowsing to set
     */
    public void setHousesForBrowsing(List<HDBHouseEntity> housesForBrowsing) {
        this.housesForBrowsing = housesForBrowsing;
    }

    /**
     * @return the selectedHouse
     */
    public HDBHouseEntity getSelectedHouse() {
        return selectedHouse;
    }

    /**
     * @param selectedHouse the selectedHouse to set
     */
    public void setSelectedHouse(HDBHouseEntity selectedHouse) {
        this.selectedHouse = selectedHouse;
    }

 
    
}
