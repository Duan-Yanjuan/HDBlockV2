/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import datamodel.ws.HouseAssetUpdate;
import entity.HDBStaffEntity;
import entity.HDBRentingPolicyEntity;
import entity.HDBHouseEntity;
import entity.HDBHouseOwnerRecordEntity;
import entity.HDBlockUserEntity;
import entity.ICAIdentificationRecordEntity;
import util.helperclass.PendingHouse;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.security.CryptographicHelper;
import util.exception.CreateNewPolicyException;
import util.exception.HDBRecordNotFoundException;
import util.exception.ICRecordNotFoundException;



/**
 *
 * @author Steph
 */
@Stateless
public class HDBController implements HDBControllerLocal {

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;

    
    private final String COMPOSER_URL = "http://172.25.107.104:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String HOUSE_ASSET_ORG = "org.acme.hdb.House";
    private final Client CLIENT = ClientBuilder.newClient(); 
     
    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public HDBStaffEntity createNewStaff(HDBStaffEntity newStaff){
        
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);
        
        return newStaff;   
    }
    
        
    @Override
    public HDBStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException{  //simple login logic
        
          try
        {
            HDBStaffEntity staff = retrieveUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staff.getSalt()));
            
            if(staff.getPassword().equals(passwordHash))
            {
                return staff;
            }
            else
            {
                throw new InvalidLoginCredentialException(passwordHash);
            }
        }
        catch(StaffNotFoundException ex)
        {
            String msg = "username does not exist or invalid password!";
            throw new InvalidLoginCredentialException(ex.getMessage());
        }
        
    }
    
    
        
    @Override
    public HDBRentingPolicyEntity createNewPolicy(HDBRentingPolicyEntity newPolicy) throws CreateNewPolicyException{
        
        em.persist(newPolicy);
        em.flush();
        em.refresh(newPolicy);
        
        return newPolicy;   
    }
    
    @Override
    public HDBRentingPolicyEntity updatePolicy(HDBRentingPolicyEntity editedPolicy){
         
        HDBRentingPolicyEntity policyRecord = em.find(HDBRentingPolicyEntity.class, editedPolicy.getId());

        policyRecord.setPolicyCode(editedPolicy.getPolicyCode());
        policyRecord.setFlatType(editedPolicy.getFlatType());
        policyRecord.setRentingType(editedPolicy.getRentingType());
        policyRecord.setMaxNumofTenantsAllowed(editedPolicy.getMaxNumofTenantsAllowed());
        em.flush();
        em.refresh(policyRecord);
        
        return policyRecord;
    }
    
    @Override
    public HDBStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException
    {
        //ProductResource_JerseyClient client = new ProductResource_JerseyClient();
        
        Query query = em.createQuery("SELECT i FROM HDBStaffEntity i WHERE i.userName =:username");
        query.setParameter("username", username);
        
        try
        {
           return (HDBStaffEntity)query.getSingleResult(); 
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            System.out.println("Exception");
            throw new StaffNotFoundException("Staff username " + username + " does not exist!");
        }
             
    }
    
    @Override
    public List<HDBRentingPolicyEntity> retrieveHDBRentingPolicies() {
        TypedQuery<HDBRentingPolicyEntity> query = em.createQuery("SELECT i FROM HDBRentingPolicyEntity i", HDBRentingPolicyEntity.class);
        return query.getResultList();
    }
    

    @Override
    public List<PendingHouse> retrieveHouseWithPendingStatus(){
        
       
        List<PendingHouse> housesWithPendingStatus = new ArrayList<>();
        
        try{
         WebTarget myResource = CLIENT.target(COMPOSER_URL).path(HOUSE_ASSET_ORG);
         Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
         Response response = invocationBuilder.get();
         int responseStatus = response.getStatus();
         int noOfHouses = 0;
         String houseId = "";
         String type = "";
         String address = "";
         String status = "";
         String landlord = "";
         String landlordICValidityPeriod = "";
         boolean isHouseOwnerValid = false;  
            
         //GETTING HOUSE VALUE
            if (responseStatus == 200) {
                String houseAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(houseAssets));
                JsonArray houses = reader.readArray();
                noOfHouses = houses.size();
                
                for(int i=0; i<noOfHouses; i++){
                    
                    houseId = houses.getJsonObject(i).getString("houseId");
                    type = houses.getJsonObject(i).getString("type");
                    address = houses.getJsonObject(i).getString("address");
                    status =  houses.getJsonObject(i).getString("status");
                    landlord =  houses.getJsonObject(i).getString("landlord").substring(31);
                    landlordICValidityPeriod = checkIdentificationValidityPeriod(landlord);
                    isHouseOwnerValid = checkHouseValidity(houseId,landlord);    
                    
                    if(status.equals("Pending")){  
                         housesWithPendingStatus.add(new PendingHouse(i+1, houseId, type, address, landlord, landlordICValidityPeriod, isHouseOwnerValid));
                    }
                
                    System.out.println("**************** House ID[" + i + "] is " + houseId );
                    System.out.println("**************** Flat Type[" + i + "] is " + type);
                    System.out.println("**************** Address[" + i + "] is " + address);
                    System.out.println("**************** Status[" + i + "] is " + status);
                    System.out.println("**************** Landlord[" + i + "] is " + landlord);
                    System.out.println("**************** landlordICValidityPeriod[" + i + "] is " + landlordICValidityPeriod);
                    System.out.println("**************** isHouseOwnerValid[" + i + "] is " + isHouseOwnerValid);

                }
            }
            
       
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
       
                
          return housesWithPendingStatus;
        
    }
    
    @Override
    public List<HDBHouseEntity> retrieveAllHouses()
    {
        Query query = em.createQuery("SELECT i FROM HDBHouseEntity i");
        return query.getResultList();
    }
    
     @Override
    public boolean processHouseValidity(PendingHouse house) throws HDBRecordNotFoundException{
        
        /**call composer rest to read the list of house asset whose status is pending. 
        we will obtain the response in json format and we only need to get the DOB and IC and name
        */
 
        String houseId = "";
        String landlordNRIC = "";
        
        try{
            
        System.out.println("************* processHouseValidity " + house.getIdentificationNo());
        houseId = house.getIdentificationNo();  //change to value return in json format later on
        landlordNRIC = house.getLandlord();
     
      
        boolean houseIsValid = checkHouseValidity(houseId,landlordNRIC);     

        if(houseIsValid)
        {
            //send the update request to composer res to update the House Status 
            updateHouseAsset(houseId, HOUSE_ASSET_ORG ,"Valid" );
                
            return true;
        }

        }catch(HDBRecordNotFoundException ex){
            
            System.out.println("************* INVALID");
            
            updateHouseAsset(houseId, HOUSE_ASSET_ORG ,"Invalid");
        
                  
            throw new HDBRecordNotFoundException(ex.getMessage());
            
        }      
              return false;
    } 
    
    
    
    private void updateHouseAsset(String id, String path, String status ){
        
               
        WebTarget myResource; 
        Response registerUserResponse;
        
         myResource = CLIENT.target(COMPOSER_URL).path(path);
         HouseAssetUpdate house;
        
         
         if(status.equals("Pending")){
            house = new HouseAssetUpdate(path, id, status);
            registerUserResponse = myResource.request().put(Entity.json(house));
            System.out.println("************* Response " + registerUserResponse.getStatus());
         }
        
    }
    
    
       private boolean checkHouseValidity(String houseId, String landlordNRIC) throws HDBRecordNotFoundException
    {
       
        
            Query query = em.createQuery("SELECT i FROM HDBHouseOwnerRecordEntity i WHERE i.Nric = :landlordNRIC");
            query.setParameter("landlordNRIC", landlordNRIC);
            List<HDBHouseOwnerRecordEntity> record = query.getResultList();
            String recordNric = record.get(0).getNRIC();
            String recordHouseId = record.get(0).getPostalCode() + "_" + record.get(0).getUnitNo().substring(1);
            System.out.println(recordHouseId);
            
            
            if(record.isEmpty()){
                throw new HDBRecordNotFoundException("No Such House");
            }
            else
            {
               if(recordNric.equals(landlordNRIC) && recordHouseId.equals(houseId)) 
                {
                    return true;
                }
                else
                {
                    throw new HDBRecordNotFoundException("House information does not match the existing record.");
                }
                              
            }
               
       }
       
       
       
       @Override
       public HDBHouseOwnerRecordEntity createNewOwner(HDBHouseOwnerRecordEntity newOwner){
           
           em.persist(newOwner);
           em.flush();
           em.refresh(newOwner);
           return newOwner;
       }    
    
       

    public String checkIdentificationValidityPeriod(String identificationNumber) throws ICRecordNotFoundException {

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Query query = em.createQuery("SELECT i FROM ICAIdentificationRecordEntity i WHERE i.nric = :ic");
        query.setParameter("ic", identificationNumber);
        List<ICAIdentificationRecordEntity> record = query.getResultList();

        if (record.isEmpty()) {
            throw new ICRecordNotFoundException("Non-Existence of Such Identity Number");
        } else {
            System.out.println("***************** IC STATUS FOUND");
            String validityPeriod_String = df.format(record.get(0).getValidityPeriod());
            System.out.println("***************** VALIDITY PERIOD IN DATA BASE " + validityPeriod_String);
            
           
            if (record.get(0).getNric().equals(identificationNumber))
            {
                return validityPeriod_String;
            } else {
                throw new ICRecordNotFoundException("Mismatch of Information with His/Her Identity Numebr");
            }

        }

    }
       
       
    
    
}
