/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean.HDBlock;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CreateNewHouseException;
import util.exception.CreateNewUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author David
 */
@Named(value = "houseManagedBean")
@ViewScoped
public class HouseManagedBean implements Serializable {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    /**
     * Creates a new instance of HouseManagedBean
     */
    private HDBHouseEntity newHouse;
    private List<HDBHouseEntity> houses;
    private String landlordEmail;
    private String rentalStatus; //this is from blockchain

    public HouseManagedBean() {
        houses = new ArrayList<>();
        newHouse = new HDBHouseEntity();
        rentalStatus = "occupied";
    }

    @PostConstruct
    public void postConstruct() {

        HDBlockUserEntity currentUserInformation = (HDBlockUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("userInformation");

        landlordEmail = currentUserInformation.getEmail();
        System.out.println("**************** Landlord email is " + landlordEmail);
        houses = hDBlockUserEntityControllerLocal.retrieveLandlordHouseByEmail(landlordEmail);
    }

    public void registerHouse() {

        try {

            System.out.println("House is" + newHouse.getAddress());
            HDBlockUserEntity landlordInformation = hDBlockUserEntityControllerLocal.retrieveUserByEmail(landlordEmail);
            newHouse.setHouseOwner(landlordInformation);
            newHouse.setHouseImage("XXXXX");
            hDBlockUserEntityControllerLocal.registerHouse(newHouse, landlordEmail);
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "The registration is succesful. Approval notification will be send to you via email in 1 - 2 working days.)", null));
            houses = hDBlockUserEntityControllerLocal.retrieveLandlordHouseByEmail(landlordEmail);


        } catch (Exception ex) {

              FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unable to register new house. Try again later." + ex.getMessage(), null));
        }

    }

    /**
     * @return the houses
     */
    public List<HDBHouseEntity> getHouses() {
        return houses;
    }

    /**
     * @param houses the houses to set
     */
    public void setHouses(List<HDBHouseEntity> houses) {
        this.houses = houses;
    }

    /**
     * @return the newHouse
     */
    public HDBHouseEntity getNewHouse() {
        return newHouse;
    }

    /**
     * @param newHouse the newHouse to set
     */
    public void setNewHouse(HDBHouseEntity newHouse) {
        this.newHouse = newHouse;
    }

    /**
     * @return the landlordEmail
     */
    public String getLandlordEmail() {
        return landlordEmail;
    }

    /**
     * @param landlordEmail the landlordEmail to set
     */
    public void setLandlordEmail(String landlordEmail) {
        this.landlordEmail = landlordEmail;
    }

    /**
     * @return the rentalStatus
     */
    public String getRentalStatus() {
        return rentalStatus;
    }

    /**
     * @param rentalStatus the rentalStatus to set
     */
    public void setRentalStatus(String rentalStatus) {
        this.rentalStatus = rentalStatus;
    }

}
