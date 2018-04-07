/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import datamodel.ws.LandlordAsset;
import datamodel.ws.Product;
import datamodel.ws.TenantAsset;
import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.CreateNewHouseException;
import util.exception.CreateNewUserException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserNotFoundException;
import util.helperclass.ProductEntity;
import util.helperclass.TenancyAgreement;
import util.helperclass.Tenant;
import util.security.CryptographicHelper;

/**
 *
 * @author David
 */
@Stateless
public class HDBlockUserEntityController implements HDBlockUserEntityControllerLocal {

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;
    private final String COMPOSER_URL = "http://172.25.106.191:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String TENANT_ASSET_ORG = "org.acme.hdb.RegisterAsTenant";
    private final String LANDLORD_ASSET_ORG = "org.acme.hdb.RegisterAsLandlord";
    private final String TENANCY_AGREEMENT_ASSET_ORG = "org.acme.hdb.TenancyAgreement";
    private final String TENANCY_SIGNATURE_ASSET_ORG = "org.acme.hdb.TenancySignature";
    private final String HOUSE_ASSET_ORG = "org.acme.hdb.House";
    private final String STAMP_CERTIFICATE_ASSET_ORG = "org.acme.hdb.StampCertificate";
    private final Client CLIENT = ClientBuilder.newClient(); 

    public void persist(Object object) {
        em.persist(object);
    }

   
  
    @Override
    public HDBlockUserEntity registerAccount(HDBlockUserEntity newUser) throws CreateNewUserException{
        
        try
        {
            
             em.persist(newUser);
             em.flush();
             em.refresh(newUser);
               
            WebTarget myResource; 
            Response registerUserResponse;
            int responseStatus = 0 ;
            
     
            if (newUser.getUserType().equals("tenant")) {
               System.out.println("*********** REGISTER Tenant RESPONSE IS ");
               //CALL COMPOSER REST SERVER TO CREATE NEW USER(TENANT).
               myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
               TenantAsset tenant = new TenantAsset(TENANT_ASSET_ORG, newUser.getIdentificationNo() , newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending");
               registerUserResponse = myResource.request().post(Entity.json(tenant));
               responseStatus = registerUserResponse.getStatus();
               System.out.println("*********** REGISTER TENANT RESPONSE IS " + responseStatus);
               
              
                    
            }
            else if(newUser.getUserType().equals("landlord")){
                System.out.println("*********** REGISTER Landlord RESPONSE IS ");
               //CALL COMPOSER REST SERVER TO CREATE NEW USER(lANDLORD).
               myResource = CLIENT.target(COMPOSER_URL).path(LANDLORD_ASSET_ORG);
               System.out.println("******** RESOURCES IS " + myResource.getUri());
               LandlordAsset landlord = new LandlordAsset(LANDLORD_ASSET_ORG, newUser.getIdentificationNo() , newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending" , newUser.getDateOfBirth());
               registerUserResponse = myResource.request().post(Entity.json(landlord));
               responseStatus = registerUserResponse.getStatus();
               System.out.println("*********** REGISTER Landlord RESPONSE IS " + responseStatus );
                      
            }
            
            //COMPOSER REST MIGHT RETURN UNSUCCESFUL RESPONSE
           /* if(responseStatus != 200)
                   throw new CreateNewUserException("Fail to Register Account in Block Chain Network");*/
                
             return newUser;
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && 
                    ex.getCause().getCause() != null &&
                    ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException"))
            {
                throw new CreateNewUserException("Email or IC already exist");
            }
            else
            {
                throw new CreateNewUserException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new CreateNewUserException("An unexpected error has occurred: " + ex.getMessage());
        }
   
    }
    
     @Override
    public HDBHouseEntity registerHouse(HDBHouseEntity newHouse, String landlordEmail) throws UserNotFoundException , CreateNewHouseException {

        try {
            //add to database first
           //do checking of duplciate here            
            
            em.persist(newHouse);
            em.flush();
            em.refresh(newHouse);
            HDBlockUserEntity landlordInformation = retrieveUserByEmail(landlordEmail);
            String landlordIC = landlordInformation.getIdentificationNo();
            //pass chain code with landlord ID 

            //registerHouse call another chain code
            // HDB table call occupant table is needed.
            return newHouse;

        } catch (UserNotFoundException ex) {
            throw new UserNotFoundException("User email " + landlordEmail + " does not exist!");
        }

    }

    @Override
    public HDBlockUserEntity retrieveUserByEmail(String email) throws UserNotFoundException {

        Query query = em.createQuery("SELECT u FROM HDBlockUserEntity u WHERE u.email =:userEmail");
        query.setParameter("userEmail", email);

        try {
            return (HDBlockUserEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            System.out.println("Exception");
            throw new UserNotFoundException("User email " + email + " does not exist!");
        }

    }
    
    @Override
    public List<HDBlockUserEntity> retrieveAllUser(){
        
        Query query = em.createQuery("SELECT u FROM HDBlockUserEntity u");
        System.out.println("********** RETRIEVE USER " + query.getResultList().size());
        return query.getResultList();
    }

    @Override
    public HDBlockUserEntity userLogin(String email, String password) throws InvalidLoginCredentialException {  //simple login logic

        try {
            HDBlockUserEntity user = retrieveUserByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + user.getSalt()));

            if (user.getPassword().equals(passwordHash)) {
                return user;
            } else {
                throw new InvalidLoginCredentialException("email does not exist or invalid password!");
            }
        } catch (UserNotFoundException ex) {
            throw new InvalidLoginCredentialException("email does not exist or invalid password!");
        }

    }

    @Override
    public List<HDBHouseEntity> retrieveAllAvailableHouse() {

    //    Query getAllHouseQuery = em.createQuery("SELECT hdb FROM HDBHouseEntity hdb WHERE hdb.statusIsValid = true");
        Query getAllHouseQuery = em.createQuery("SELECT hdb FROM HDBHouseEntity hdb");
        return getAllHouseQuery.getResultList();
        

    }
    
    @Override
    public List<HDBHouseEntity> retrieveLandlordHouseByEmail(String email){
        
        Query getHouses = em.createQuery("SELECT h FROM HDBHouseEntity h WHERE h.houseOwner.email = :uEmail");
        getHouses.setParameter("uEmail", email);
        
        return getHouses.getResultList();
    }

    @Override
    public List<TenancyAgreement> retrieveTenancyAgreementByIC(String identificationNumer) {  

        //make a call to block chain by passing tenant email and get all the value
        List<TenancyAgreement> tenantTenancies = new ArrayList<>();
        String taId;
        Date dateCreated;
        Date rentalStart;
        Double rentalFee;
        Double advanceRentalFee;
        Double securityDeposit;
        String tenancyStatus;
        String landlordEmail;
        String houseId;
        List<Tenant> tenants;

        try {
            Client client = ClientBuilder.newClient(); //Customer/login/"
         //   WebTarget myResource = client.target("http://localhost:3446/FoodEmblemV1-war/Resources").path("RestaurantDish").path("1");
              WebTarget myResource = client.target("http://localhost:3446/FoodEmblemV1-war/Resources").path("Restaurant");
              Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            int resultFromChainCode = 0;
            
            if (response.getStatus() == 200) {
                String mesg = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(mesg));
                JsonStructure jsonst = reader.read();
                JsonObject object = (JsonObject) jsonst;
                JsonArray jsonArray = object.getJsonArray("restaurants");
                resultFromChainCode = jsonArray.size();

                for (int noOfAgreement = 0; noOfAgreement < resultFromChainCode; noOfAgreement++) {

                    taId = "";
                    dateCreated = new Date();
                    rentalStart = new Date();
                    rentalFee = 0.0;
                    advanceRentalFee = 0.0;
                    securityDeposit = 0.0;
                    tenancyStatus = "";
                    landlordEmail = "";
                    houseId = "";
                    tenants = new ArrayList<>();
                    JsonArray tenantResultFromChainCode = jsonArray.getJsonObject(noOfAgreement).getJsonArray("dishes");
                    
                    System.out.println("Value [i] = " + noOfAgreement + " address is " + jsonArray.getJsonObject(noOfAgreement).getString("address"));
                    System.out.println("Value [i] = " + noOfAgreement + " apiKey is " + jsonArray.getJsonObject(noOfAgreement).getString("apiKey"));
                    System.out.println("Value [i] = " + noOfAgreement + " contactNo " + jsonArray.getJsonObject(noOfAgreement).getString("contactNo"));
                    System.out.println("Value [i] = " + noOfAgreement + " email " + jsonArray.getJsonObject(noOfAgreement).get("email"));

                    for (int noOfTenant = 0; noOfTenant < tenantResultFromChainCode.size(); noOfTenant++) {
                        String tenantEmail = tenantResultFromChainCode.getJsonObject(noOfTenant).getString("category");   ; //get from cc
                        String tenantFullName = tenantResultFromChainCode.getJsonObject(noOfTenant).getString("name"); //getTenantFullName(tenantEmail);
                        String tenantIc = tenantResultFromChainCode.getJsonObject(noOfTenant).get("id").toString(); //get from CC\
                        
                        System.out.println("************ TENANT INFORMATION " + tenantEmail + "_ " + tenantFullName + "_ " + tenantIc );
                        tenants.add(new Tenant(tenantFullName, tenantEmail, tenantIc));
                        
                    }

                    tenantTenancies.add(new TenancyAgreement(taId, dateCreated, rentalStart, rentalFee, advanceRentalFee, securityDeposit, tenancyStatus, landlordEmail, houseId, tenants));
                }
            }
            
            
            Client client2 = ClientBuilder.newClient(); //Customer/login/"
          
            
    //        Client client = ClientBuilder.newClient();
    

            WebTarget target = client2.target("http://localhost:3446/PointOfSaleSystemV6-war/Resources/Product");
            //WebTarget target = client2.target("http://localhost:3446/FoodEmblemV1-war/Resources/Sensor/updateRestaurantFridgeTemp");
            ProductEntity pe = new ProductEntity("PROD023" , "PRODUCTXYZ3" , "PRODUCTXYZ2" , 5 , 10 , new BigDecimal(10.50) , "CategoryXYZ" , 3);
            Product a = new Product("manager" , "password" , pe);
            Response response3 = target.request().put(Entity.json(a));
                    
            System.out.println("**************** create product " + response3.getStatus());
                    

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /* int resultFromChainCode = 10; //to be changed
        
       
        
        for(int noOfAgreement = 0; noOfAgreement<resultFromChainCode; noOfAgreement++)
        {
             taId = "";
             dateCreated =new Date();
             rentalStart = new Date();
             rentalFee = 0.0 ;
             advanceRentalFee = 0.0;
             securityDeposit = 0.0;
             tenancyStatus = "";
             landlordEmail = "";
             houseId = "";
             tenants = new ArrayList<>();
             int tenantResultFromChainCode =4; //chnage to size
             for(int noOfTenant=0; noOfTenant<tenantResultFromChainCode; noOfTenant++)
             {
                 String tenantEmail = "" ; //get from cc
                 String tenantFullName = getTenantFullName(tenantEmail);
                 String tenantIc = "" ; //get from CC
                 tenants.add(new Tenant(tenantFullName, tenantEmail, tenantIc));
             }
             
             tenantTenancies.add(new TenancyAgreement(taId, dateCreated, rentalStart, rentalFee, advanceRentalFee,securityDeposit, tenancyStatus, landlordEmail, houseId,tenants));
             
             
        }*/
        return tenantTenancies;

    }

    @Override
    public void addNewTenant(HDBlockUserEntity tenant, String tenancyAgreementId) {
        String tenantIc = tenant.getIdentificationNo();
        //call get method to chain code

        //Json Object here. 
        //call a Post request to udpate the tenancy agreement contract by adding new tenant
    }

    @Override
    public boolean signTenancy(String email, String tenancyAgreementId) {

        String tenantIc = getUsersIdentificationNumber(email);

        return true;

        //call update request to update TENANT Signature asset
        // javascript side  the tenant.tenantIC check with my path parameter. 
        //  javascript side  will check tenant.tenancyAgreement.get(i) with my tenancyAgreementId 
    }

    public boolean payStampDuty(Double dutyAmount, String tenancyAgreementId) {

        //retrieve tenancyAgreement by Id from blockchain;
        //create new Stamp Certificate class.    //becareful with Json manipuoatting
        return true;

    }

    private String getTenantFullName(String email) {

        HDBlockUserEntity userName = (HDBlockUserEntity) em.createQuery("SELECT c FROM HDBlockUserEntity c WHERE c.email = :cEmail").setParameter("cEmail", email).getSingleResult();
        return userName.getFirstName() + " " + userName.getLastName();
    }

    private String getUsersIdentificationNumber(String email) {

        HDBlockUserEntity userName = (HDBlockUserEntity) em.createQuery("SELECT c FROM HDBlockUserEntity c WHERE c.email = :cEmail").setParameter("cEmail", email).getSingleResult();
        if(userName != null)
            return userName.getIdentificationNo();
        
        return null;
    }
    
    @Override
    public void retrieveCustomerTest() {

        System.out.println("****************** TESTING CLIENT");

        //  http://localhost:3446/FoodEmblemV1-war/Resources/Customer/login/Jy@gmail.com/a
        try {

            Client client = ClientBuilder.newClient(); //Customer/login/"
            //  WebTarget myResource = client.target("http://services.groupkt.com/country/get/all");

            WebTarget myResource = client.target("http://localhost:3446/FoodEmblemV1-war/Resources").path("Customer").path("login").path("Jy@gmail.com").path("12345678");
            Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            String mesg = invocationBuilder.get(String.class);

            JsonReader reader = Json.createReader(new StringReader(mesg));
            JsonStructure jsonst = reader.read();
            JsonObject object = (JsonObject) jsonst;
            // JsonString status = (JsonString) object.get("loginSuccess");

            //JsonReader jsonReader = Json.createReader(new StringReader(mesg));
            //JsonObject object = jsonReader.readObject();
            //String msg = object.getString("loginSuccess");
            //    response.get
//        Response response = 
            System.out.println("*************** response " + response.getStatus());
            System.out.println("*************** msg " + mesg);

            boolean a = Boolean.parseBoolean(object.get("loginSuccess").toString());
            System.out.println("*****************" + a + " ");

        } catch (NullPointerException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        /*   WebTarget myResource2 = client.target("http://example.com/webapi/read").path("{userName}").resolveTemplate("userName", "janedoe").queryParam("chapter", "1");
        // http://example.com/webapi/read/janedoe?chapter=1
        Response response2 = myResource2.request().get();
        System.out.println(response2.getStatus());*/
        //ProductResource_JerseyClient client = new ProductResource_JerseyClient();
    }
    
    
    
    @Override
    public void testBlockChainCode(){
        
               System.out.println("*****************inside testblockchiancode");    
       
        try {
          
         //   WebTarget myResource = client.target("http://localhost:3446/FoodEmblemV1-war/Resources").path("RestaurantDish").path("1");
            WebTarget myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
              Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            int resultFromChainCode = 0;
            
            if (response.getStatus() == 200) {
                String mesg = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(mesg));
                JsonArray noOfTenant = reader.readArray();
                
                for(int i=0; i<noOfTenant.size(); i++){
                    
                    System.out.println("**************** ID[" + i + "] is " + noOfTenant.getJsonObject(i).getString("id"));
                    System.out.println("**************** email[" + i + "] is " + noOfTenant.getJsonObject(i).getString("email"));
                    System.out.println("**************** firstName[" + i + "] is " + noOfTenant.getJsonObject(i).getString("firstName"));
                    System.out.println("**************** lastName[" + i + "] is " + noOfTenant.getJsonObject(i).getString("lastName"));
                    System.out.println("**************** ICStatus[" + i + "] is " + noOfTenant.getJsonObject(i).getString("ICStatus"));


                }
                
                
    //                JsonArray a = new JsonArray();
               // JsonStructure jsonst = reader.read();
              //  JsonObject object = (JsonObject) jsonst;
               
                // JsonArray jsonArray = object.getJsonArray("");
                //JsonArray jsonArray = object.get
//                resultFromChainCode = jsonArray.size()

            

              // System.out.println("***************** GESG BLOCK CHAIN  messg " + a.size());    
               

            }

            

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
