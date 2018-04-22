/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import datamodel.ws.LandlordAssetUpdate;
import datamodel.ws.TenantAssetUpdate;
import entity.ICAIdentificationRecordEntity;
import entity.ICAStaffEntity;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.ICRecordNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.helperclass.PendingUser;
import util.security.CryptographicHelper;

/**
 *
 * @author David
 */
@Stateless
public class ICAController implements ICAControllerLocal {

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;

    private final String COMPOSER_URL = "http://172.25.107.104:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String TENANT_ASSET_ORG = "org.acme.hdb.Tenant";
    private final String LANDLORD_ASSET_ORG = "org.acme.hdb.Landlord";
    private final String TENANT_ASSET_UPDATE_STATUS_ORG = "org.acme.hdb.UpdateTenantStatus";
    private final String LANDLORD_ASSET_UPDATE_STATUS_ORG = "org.acme.hdb.UpdateLandlordStatus";
    private final Client CLIENT = ClientBuilder.newClient();

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public ICAStaffEntity createNewStaff(ICAStaffEntity newStaff) {

        System.out.println("** crating staff");
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);

        return newStaff;

    }

    @Override
    public ICAIdentificationRecordEntity createNewIdentificationRecord(ICAIdentificationRecordEntity newRecord) {

        em.persist(newRecord);
        em.flush();
        em.refresh(newRecord);

        return newRecord;
    }

    @Override
    public ICAStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException {
        //ProductResource_JerseyClient client = new ProductResource_JerseyClient();

        Query query = em.createQuery("SELECT i FROM ICAStaffEntity i WHERE i.userName =:username");
        query.setParameter("username", username);

        try {
            return (ICAStaffEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            System.out.println("Exception");
            throw new StaffNotFoundException("Staff username " + username + " does not exist!");
        }

    }

    @Override
    public ICAStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException {  //simple login logic

        try {
            ICAStaffEntity staff = retrieveUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staff.getSalt()));

            if (staff.getPassword().equals(passwordHash)) {
                return staff;
            } else {
                throw new InvalidLoginCredentialException("username does not exist or invalid password!");
            }
        } catch (StaffNotFoundException ex) {
            throw new InvalidLoginCredentialException("username does not exist or invalid password!");
        }

    }

    @Override
    public List<ICAIdentificationRecordEntity> retrieveAllIdentification() {
        Query query = em.createQuery("SELECT ic FROM ICAIdentificationRecordEntity ic WHERE ic.validityPeriod != null");
        return query.getResultList();

    }
    
    @Override
       public ICAIdentificationRecordEntity retrieveAllIdentificationById(String IC) {
        Query query = em.createQuery("SELECT ic FROM ICAIdentificationRecordEntity ic WHERE ic.nric =:userIc");
        query.setParameter("userIc", IC);
        ICAIdentificationRecordEntity information = (ICAIdentificationRecordEntity) query.getSingleResult();
        return information;

    }

   
    @Override
    public boolean finalProcessUserIdentity(String ic,String userType , String result ){  //this is triggered when ICA staff manually choose to reject or approve.
        
        String status = "Invalid";
               
        if(result.equals("Approve")){
          status = "Valid";
          hDBlockUserEntityControllerLocal.updateUserIdentity(ic); // change user status to isValid.
            
        }
        
        System.out.println("************* Update Chain code with preocessing result = " + result + " for user type " + userType + " status is " + status);
        if (userType.equals("Tenant"))
           return updateUserAsset(userType, TENANT_ASSET_UPDATE_STATUS_ORG, status, ic);
        else if(userType.equals("Landlord")) 
          return updateUserAsset(userType, LANDLORD_ASSET_UPDATE_STATUS_ORG, status, ic);
        
            return false;

    }   
       
       
       
       
       
    @Override
    public boolean processUserIdentity(PendingUser user) throws ICRecordNotFoundException {
        /**
         * call composer rest to read the list of landlord and tenant asset
         * whose IC status is pending. we will obtain the response in json
         * format and we only need to get the DOB and IC and name
         */
        String ic = "";
        String dob = "";
        String firstName = "";
        String lastName = "";
        String email = "";
        String userType = "";
        String userIdentitification = "";

        try {

            System.out.println("************* processUserIdentity " + user.getUserType() +  " " + user.getDob());
            ic = user.getIdentificationNo();  //change to value return in json format later on
            dob = user.getDob(); //change to json value
            firstName = user.getFirstName();
            lastName = user.getLastName(); //change to json value
            email = user.getEmail();
            userType = user.getUserType();
            userIdentitification = user.getIdentificationNo();

            boolean identityIsValid = checkIdentificationValidity(ic, dob, firstName, lastName);

            if (identityIsValid) {
                //send the update request to composer res to update the ICStatus       
                if (userType.equals("Tenant")) {
                    System.out.println("************* TENANT IC IS VALID");
                    updateUserAsset(userType, TENANT_ASSET_UPDATE_STATUS_ORG, "Valid", userIdentitification);
                    hDBlockUserEntityControllerLocal.updateUserIdentity(userIdentitification); //set the identity to true
                } else if (userType.equals("Landlord")) {
                    System.out.println("************* LANDLORD IC IS VALID");
                    updateUserAsset(userType, LANDLORD_ASSET_UPDATE_STATUS_ORG, "Valid", userIdentitification);
                    hDBlockUserEntityControllerLocal.updateUserIdentity(userIdentitification); ////set the identity to true
                }

                return true;
            }

        } catch (ICRecordNotFoundException ex) {

            System.out.println("************* INVALID");
            if (userType.equals("Tenant")) {
                System.out.println("************* TENANT IC NOT VALid. ABOUT TO UPDATE THE STATUS");
                updateUserAsset(userType, TENANT_ASSET_UPDATE_STATUS_ORG, "Invalid", user.getIdentificationNo());
                

            } else if (userType.equals("Landlord")) {
                System.out.println("************* LADNLORD IC NOT VALid. ABOUT TO UPDATE THE STATUS");
                updateUserAsset(userType, LANDLORD_ASSET_UPDATE_STATUS_ORG, "Invalid", user.getIdentificationNo());
            }

            throw new ICRecordNotFoundException(ex.getMessage());

        }
        return false;
    }

    private boolean updateUserAsset(String userType, String path, String status, String ic) {

        WebTarget myResource;
        Response registerUserResponse;

        myResource = CLIENT.target(COMPOSER_URL).path(path);
        System.out.println("*full path " + myResource.getUri());
        TenantAssetUpdate tenant;
        LandlordAssetUpdate landlord;

        if (userType.equals("Tenant")) {
            System.out.println("************* TENTANT CALLIONG COMPOSER. PATH IS " + path + " Status is " + status + " IC is " + ic);
            tenant = new TenantAssetUpdate(path, status, ic);
            registerUserResponse = myResource.request().post(Entity.json(tenant));
           
            System.out.println("************* Response " + registerUserResponse.getStatusInfo());
             if(registerUserResponse.getStatus() == 200)
                return true;

        } else if (userType.equals("Landlord")) {
            System.out.println("************* LANDLORD CALLIONG COMPOSER IC is " + ic + "path is " + path);
            landlord = new LandlordAssetUpdate(path, status, ic);
            registerUserResponse = myResource.request().post(Entity.json(landlord));
            System.out.println("************* Response " + registerUserResponse.getStatus());
             if(registerUserResponse.getStatus() == 200)
                return true;
        }
        
        return false;
    }

    
    /*
        This method make a restful call to composer server to retrieve the list of landlord and tenants asset whose identity status is pending.
    */
    @Override
    public List<PendingUser> retrieveUserWithPendingStatus() {

        List<PendingUser> usersWithPendingStatus = new ArrayList<>();

        try {

            WebTarget myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
            Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            int responseStatus = response.getStatus();
            int noOfTenant = 0;
            int noOfLandlord = 0;
            String userIdentificationNo = "";
            String userEmail = "";
            String firstName = "";
            String lastName = "";
            String icaStatus = "";
            String dob = "";

            //GETTING TENANT VALUE
            if (responseStatus == 200) {
                String tenantAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(tenantAssets));
                JsonArray tenants = reader.readArray();
                noOfTenant = tenants.size();
                for (int i = 0; i < noOfTenant; i++) {

                    userIdentificationNo = tenants.getJsonObject(i).getString("id");
                    userEmail = tenants.getJsonObject(i).getString("email");
                    firstName = tenants.getJsonObject(i).getString("firstName");
                    lastName = tenants.getJsonObject(i).getString("lastName");
                    icaStatus = tenants.getJsonObject(i).getString("ICStatus");
                    dob = tenants.getJsonObject(i).getString("DOB");
                    //dob = tenants.getJsonObject(i).get("lastName");

                    if (icaStatus.equals("Pending")) {
                        usersWithPendingStatus.add(new PendingUser(i + 1, userIdentificationNo, userEmail, firstName, lastName, dob, "Tenant"));
                    }

                    System.out.println("**************** tenant ID[" + i + "] is " + userIdentificationNo);
                    System.out.println("**************** tenant email[" + i + "] is " + userEmail);
                    System.out.println("**************** tenant firstName[" + i + "] is " + firstName);
                    System.out.println("**************** tenant  lastName[" + i + "] is " + lastName);
                    System.out.println("**************** tenant ICStatus[" + i + "] is " + icaStatus);
                    System.out.println("**************** tenant DOB[" + i + "] is " + dob);

                }
            }

            //GETTING LANDLORD VALUE
            myResource = CLIENT.target(COMPOSER_URL).path(LANDLORD_ASSET_ORG);
            invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            response = invocationBuilder.get();
            responseStatus = response.getStatus();

            if (responseStatus == 200) {
                String landlordAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(landlordAssets));
                JsonArray landlords = reader.readArray();
                noOfLandlord = landlords.size();

                for (int i = 0; i < noOfLandlord; i++) {

                    userIdentificationNo = landlords.getJsonObject(i).getString("id");
                    userEmail = landlords.getJsonObject(i).getString("email");
                    firstName = landlords.getJsonObject(i).getString("firstName");
                    lastName = landlords.getJsonObject(i).getString("lastName");
                    icaStatus = landlords.getJsonObject(i).getString("ICStatus");
                    dob = landlords.getJsonObject(i).getString("DOB");
                    //dob = tenants.getJsonObject(i).get("lastName");

                    if (icaStatus.equals("Pending")) {
                        usersWithPendingStatus.add(new PendingUser((noOfLandlord + i + 1), userIdentificationNo, userEmail, firstName, lastName, dob, "Landlord"));
                    }

                    System.out.println("**************** Landlord ID is " + userIdentificationNo);
                    System.out.println("**************** Landlord email is " + userEmail);
                    System.out.println("**************** Landlord firstName is " + firstName);
                    System.out.println("**************** Landloerd lastName is " + lastName);
                    System.out.println("**************** Landlord ICStatus is " + icaStatus);

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return usersWithPendingStatus;

    }

    private boolean checkIdentificationValidity(String identificationNumber, String dateOfBirth, String firstName, String lastName) throws ICRecordNotFoundException {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Query query = em.createQuery("SELECT i FROM ICAIdentificationRecordEntity i WHERE i.nric = :ic");
        query.setParameter("ic", identificationNumber);
        List<ICAIdentificationRecordEntity> record = query.getResultList();

        if (record.isEmpty()) {
            throw new ICRecordNotFoundException("Non-Existence of Such Identity Number");
        } else {
            System.out.println("***************** IC STATUS FOUND");
            String dateOfBirth_String = df.format(record.get(0).getDateOfBirth());
            System.out.println("***************** DOB DATABASE " + dateOfBirth_String + " DOB from BC " + dateOfBirth);
            System.out.println("***************** NAME IN DATA BASE " + record.get(0).getFullname() + " Name from BC " + firstName + " " + lastName);
            if (record.get(0).getFullname().equals(firstName + " " + lastName) && dateOfBirth_String.equals(dateOfBirth)) //must put and DOB
            {
                return true;
            } else {
                throw new ICRecordNotFoundException("Mismatch of Application Information with His/Her Identity Numebr");
            }

        }

    }

    
    @Override
    public boolean revokeIdentity(ICAIdentificationRecordEntity identity){
        
        String icNo = identity.getNric();
        Query q = em.createQuery("SELECT ic FROM ICAIdentificationRecordEntity ic WHERE ic.nric =:userIc");
        q.setParameter("userIc", icNo);
        
        ICAIdentificationRecordEntity userIdentity  = (ICAIdentificationRecordEntity) q.getSingleResult();
        
        userIdentity.setIsActive(false);
        em.flush();
        
        
        //call composer restful service start here.
           
        
        return true;
        
    }
}
