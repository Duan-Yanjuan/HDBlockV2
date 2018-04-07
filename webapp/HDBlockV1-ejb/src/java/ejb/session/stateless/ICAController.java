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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;
    
    private final String COMPOSER_URL = "http://172.25.105.61:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String TENANT_ASSET_ORG = "org.acme.hdb.Tenant";
    private final String LANDLORD_ASSET_ORG = "org.acme.hdb.Landlord";
     private final Client CLIENT = ClientBuilder.newClient(); 
    

    public void persist(Object object) {
        em.persist(object);
    }
    
    @Override
    public ICAStaffEntity createNewStaff(ICAStaffEntity newStaff){
        
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);
        
        return newStaff;
        
    }
    
   
    @Override
    public ICAIdentificationRecordEntity createNewIdentificationRecord(ICAIdentificationRecordEntity newRecord){
        
        em.persist(newRecord);
        em.flush();
        em.refresh(newRecord);
        
        return newRecord;
    }

       
    @Override
    public ICAStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException
    {
        //ProductResource_JerseyClient client = new ProductResource_JerseyClient();
        
        Query query = em.createQuery("SELECT i FROM ICAStaffEntity i WHERE i.userName =:username");
        query.setParameter("username", username);
        
         try
        {
           return (ICAStaffEntity)query.getSingleResult(); 
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            System.out.println("Exception");
            throw new StaffNotFoundException("Staff username " + username + " does not exist!");
        }
        
        
        
    }
    
    @Override
    public ICAStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException{  //simple login logic
        
          try
        {
            ICAStaffEntity staff = retrieveUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staff.getSalt()));
            
            if(staff.getPassword().equals(passwordHash))
            {
                return staff;
            }
            else
            {
                throw new InvalidLoginCredentialException("username does not exist or invalid password!");
            }
        }
        catch(StaffNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("username does not exist or invalid password!");
        }
        
    }
    
    
   
    @Override
    public List<ICAIdentificationRecordEntity> retrieveAllIdentification()
    {
        Query query = em.createQuery("SELECT ic FROM ICAIdentificationRecordEntity ic");
        return query.getResultList();
        
    }
    

    @Override
    public boolean processUserIdentity(PendingUser user) throws ICRecordNotFoundException{
        
        /**call composer rest to read the list of landlord and tenant asset whose IC status is pending. 
        we will obtain the response in json format and we only need to get the DOB and IC and name
        */
 
        String ic = "";
        Date dob = new Date();
        String firstName  ="";
        String lastName = "";
        String email = "";
        String userType ="";
        String userIdentitification = "";
        
        try{
            
        System.out.println("************* processUserIdentity " + user.getUserType());
        ic = user.getIdentificationNo();  //change to value return in json format later on
        dob = new Date(); //change to json value
        firstName = user.getFirstName();
        lastName = user.getLastName(); //change to json value
        email = user.getEmail();
        userType = user.getUserType();
        userIdentitification = user.getIdentificationNo();
      
        boolean identityIsValid = checkIdentificationValidity(ic,dob, firstName, lastName);       

        if(identityIsValid)
        {
            //send the update request to composer res to update the ICStatus 
         
             if(userType.equals("Tenant")){
                
                 updateUserAsset(userType, TENANT_ASSET_ORG ,"Valid" , userIdentitification);
                
            }
            else if(userType.equals("Landlord")){
                 updateUserAsset(userType, LANDLORD_ASSET_ORG ,"Valid", userIdentitification);
            }
            
            return true;
        }

        }catch(ICRecordNotFoundException ex){
            
            System.out.println("************* INVALID");
            if(userType.equals("Tenant")){
                
                 updateUserAsset(userType, TENANT_ASSET_ORG ,"Invalid" , user.getIdentificationNo());
                
            }
            else if(userType.equals("Landlord")){
                 updateUserAsset(userType, LANDLORD_ASSET_ORG ,"Invalid" , user.getIdentificationNo());
            }
           
           
            throw new ICRecordNotFoundException(ex.getMessage());
            
        }      
              return false;
    } 
    
    
    private void updateUserAsset(String userType, String path, String status , String ic){
        
               
        WebTarget myResource; 
        Response registerUserResponse;
        
         myResource = CLIENT.target(COMPOSER_URL).path(path);
         TenantAssetUpdate tenant;
         LandlordAssetUpdate landlord;
         
         if(userType.equals("Tenant")){
            tenant = new TenantAssetUpdate(path, ic, status);
            registerUserResponse = myResource.request().put(Entity.json(tenant));
            System.out.println("************* Response " + registerUserResponse.getStatus());
         }else if(userType.equals("Landlord")){
             landlord = new LandlordAssetUpdate(path, ic, status);
             registerUserResponse = myResource.request().put(Entity.json(landlord));
             System.out.println("************* Response " + registerUserResponse.getStatus());
         }
        
    }
    
    
    @Override
    public List<PendingUser> retrieveUserWithPendingStatus(){
        
       
        List<PendingUser> usersWithPendingStatus = new ArrayList<>();
        
        try{
            
              WebTarget myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
         Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
         Response response = invocationBuilder.get();
         int responseStatus = response.getStatus();
         int noOfTenant = 0;
         int noOfLandlord = 0;
         String userIdentificationNo = "";
         String userEmail ="";
         String firstName = "";
         String lastName  = "";
         String icaStatus = "";
         Date dob = new Date();
            
         //GETTING TENANT VALUE
            if (responseStatus == 200) {
                String tenantAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(tenantAssets));
                JsonArray tenants = reader.readArray();
                noOfTenant = tenants.size();
                for(int i=0; i<noOfTenant; i++){
                    
                    userIdentificationNo = tenants.getJsonObject(i).getString("id");
                    userEmail = tenants.getJsonObject(i).getString("email");
                    firstName =  tenants.getJsonObject(i).getString("firstName");
                    lastName =  tenants.getJsonObject(i).getString("lastName");
                    icaStatus =  tenants.getJsonObject(i).getString("ICStatus");
                    //dob = tenants.getJsonObject(i).get("lastName");
                    
                    if(icaStatus.equals("Pending")){  
                         usersWithPendingStatus.add(new PendingUser(i+1, userIdentificationNo, userEmail, firstName, lastName, dob, "Tenant"));
                    }
                   
            
                    System.out.println("**************** tenant ID[" + i + "] is " + userIdentificationNo);
                    System.out.println("**************** tenant email[" + i + "] is " + userEmail );
                    System.out.println("**************** tenant firstName[" + i + "] is " + firstName);
                    System.out.println("**************** tenant  lastName[" + i + "] is " + lastName);
                    System.out.println("**************** tenant ICStatus[" + i + "] is " + icaStatus );


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
                
                for(int i=0; i< noOfLandlord; i++){
                    
                    userIdentificationNo = landlords.getJsonObject(i).getString("id");
                    userEmail = landlords.getJsonObject(i).getString("email");
                    firstName =  landlords.getJsonObject(i).getString("firstName");
                    lastName =  landlords.getJsonObject(i).getString("lastName");
                    icaStatus =  landlords.getJsonObject(i).getString("ICStatus");
                    //dob = tenants.getJsonObject(i).get("lastName");
                    
                    if(icaStatus.equals("Pending")){  
                         usersWithPendingStatus.add(new PendingUser( (noOfTenant + i + 1), userIdentificationNo, userEmail, firstName, lastName, dob, "Landlord"));
                    }
                   
            
                    System.out.println("**************** Landlord ID is " + userIdentificationNo);
                    System.out.println("**************** Landlord email is " + userEmail );
                    System.out.println("**************** Landlord firstName is " + firstName);
                    System.out.println("**************** Landloerd lastName is " + lastName);
                    System.out.println("**************** Landlord ICStatus is " + icaStatus );


                }
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
       
                
          return usersWithPendingStatus;
        
    }
    

    
    private boolean checkIdentificationValidity(String identificationNumber, Date dateOfBirth, String firstName, String lastName) throws ICRecordNotFoundException
    {
       
        
            Query query = em.createQuery("SELECT i FROM ICAIdentificationRecordEntity i WHERE i.nric = :ic");
            query.setParameter("ic", identificationNumber);
            List<ICAIdentificationRecordEntity> record = query.getResultList();
            
            
            if(record.isEmpty()){
                throw new ICRecordNotFoundException("No Such Identity");
            }
            else
            {
               if(record.get(0).getFullname().equals(firstName + " " + lastName) && record.get(0).getDateOfBirth() == dateOfBirth) //must put and DOB
                {
                    return true;
                }
                else
                {
                    throw new ICRecordNotFoundException("Applicant information does not match the existing record.");
                }
                
                
            }
               
       }
    
    
    
    
    
   /* public ICAForeignIdentificationRecordEntity updateIdentification(ICAForeignIdentificationRecordEntity identity){
        
        if(identity.getId() != null)
        {
            ICAForeignIdentificationRecordEntity identityToUpdate = retrieveStaffByStaffId(staffEntity.getStaffId());
            if(staffEntityToUpdate.getUsername().equals(staffEntity.getUsername()))
            {
                staffEntityToUpdate.setFirstName(staffEntity.getFirstName());
                staffEntityToUpdate.setLastName(staffEntity.getLastName());
                staffEntityToUpdate.setAccessRightEnum(staffEntity.getAccessRightEnum());
                staffEntityToUpdate.setUsername(staffEntity.getUsername());
            }
        }
        else
        {
            throw new StaffNotFoundException("Staff ID not provided for staff to be updated");
        }
    }*/
    
    
}
