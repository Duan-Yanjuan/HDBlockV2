/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import datamodel.ws.LandlordAsset;
import datamodel.ws.LandlordAssetUpdate;
import datamodel.ws.SignContract;
import datamodel.ws.TenancyAgreementAsset;
import datamodel.ws.TenantAsset;
import datamodel.ws.TenantAssetUpdate;
import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import util.helperclass.LandlordTenancyAgreement;
import util.helperclass.Tenant;
import util.helperclass.TenantTenancyAgreement;
import util.helperclass.TenantTenancySignature;
import util.security.CryptographicHelper;

/**
 *
 * @author David
 */
@Stateless
public class HDBlockUserEntityController implements HDBlockUserEntityControllerLocal {

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;
    private final String COMPOSER_URL = "http://172.25.96.144:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String TENANT_ASSET_ORG = "org.acme.hdb.RegisterAsTenant";
    private final String LANDLORD_ASSET_ORG = "org.acme.hdb.RegisterAsLandlord";
    private final String CREATE_TENANCY_AGREEMENT_ASSET_ORG = "org.acme.hdb.CreateTenancyAgreement";
    private final String TENANCY_SIGNATURE_ASSET_ORG = "org.acme.hdb.TenancySignature";
    private final String HOUSE_ASSET_ORG = "org.acme.hdb.House";
    private final String STAMP_CERTIFICATE_ASSET_ORG = "org.acme.hdb.StampCertificate";
    
   // private final String TENANCY_AGREEMENT_ASSET_BYID_ORG = "";
   //http://localhost:3000/api/queries/GetTenancySignatureByTenantId?tenantId="resource:org.acme.hdb.Tenant#t1"
    
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
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            
     
            if (newUser.getUserType().equals("tenant")) {
               System.out.println("*********** REGISTER Tenant RESPONSE IS ");
               //CALL COMPOSER REST SERVER TO CREATE NEW USER(TENANT).
               myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
               Date tenantDob = newUser.getDateOfBirth();
               String tenantDob_String = df.format(tenantDob);
               System.out.println("********************** tenant dob " + tenantDob_String);
               TenantAsset tenant = new TenantAsset(TENANT_ASSET_ORG, newUser.getIdentificationNo() , newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending" , "21-04-1992" );
               registerUserResponse = myResource.request().post(Entity.json(tenant));
               responseStatus = registerUserResponse.getStatus();
               System.out.println("*********** REGISTER TENANT RESPONSE IS " + responseStatus);
//               
//                  "$class": "org.acme.hdb.RegisterAsLandlord",
//        "id": "G1234567P",
//        "email": "leeYanJuan@gmail.com",
//        "firstName": "TESTTSADe",
//        "lastName": "Heng",
//        "ICStatus": "Pending" ,
//        "DOB" : "10-09-1980"
//              
                    
            }
            else if(newUser.getUserType().equals("landlord")){
                System.out.println("*********** REGISTER Landlord RESPONSE IS ");
               //CALL COMPOSER REST SERVER TO CREATE NEW USER(lANDLORD).
               myResource = CLIENT.target(COMPOSER_URL).path(LANDLORD_ASSET_ORG);
               System.out.println("******** RESOURCES IS " + myResource.getUri());
               Date landlordDob = newUser.getDateOfBirth();
               String landlordDob_String = df.format(landlordDob);
               System.out.println("********************** Lanldord dob " + landlordDob_String);
               System.out.println("********************** Lanldord dob " + newUser.getIdentificationNo() );
                   System.out.println("********************** Lanldord dob " + newUser.getEmail());
                     System.out.println("********************** Lanldord dob " +  newUser.getFirstName());
                       System.out.println("********************** Lanldord dob " + newUser.getLastName());
                         System.out.println("********************** Lanldord dob " + landlordDob_String);
               
               LandlordAsset landlord = new LandlordAsset(LANDLORD_ASSET_ORG, newUser.getIdentificationNo() , newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending" , "10-09-1980");
               registerUserResponse = myResource.request().post(Entity.json(landlord));
               responseStatus = registerUserResponse.getStatus();
               System.out.println("*********** REGISTER Landlord RESPONSE IS " + responseStatus );
               System.out.println("********************* INFOR " + registerUserResponse.getStatusInfo());
                      
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
            HDBlockUserEntity userStatus= (HDBlockUserEntity) query.getSingleResult();
            if(userStatus.isStatusIsValid())
            {
                System.out.println("Status is Valid");
                return userStatus;
            }else
                System.out.println("Status is Valid = " + userStatus.isStatusIsValid() );
                throw new UserNotFoundException("Your account is not activated.");
          //  return 
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
           // ex.printStackTrace();
            throw new InvalidLoginCredentialException(ex.getMessage());
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

   /* @Override //to be deleted
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
                       // tenants.add(new Tenant(tenantFullName, tenantEmail, tenantIc));
                        
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
             
             
        }
        return tenantTenancies;

    }*/

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
            WebTarget myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.Tenant");
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

    
   
    @Override
    public List<LandlordTenancyAgreement> retrieveTenancyAgreementByLandlordId(String landlordIC){
        
        //  http://localhost:3000/api/queries/GetTenancyAgreementByLandlordId?landlordId=resource%3Aorg.acme.hdb.Landlord%23l1
        List<LandlordTenancyAgreement> tenancyAgreements = new ArrayList<>();
        List<Tenant> tenants = new ArrayList<>();
        try {

     
            String methodName = "GetTenancyAgreementByLandlordId";
            String landlordId = "resource%3Aorg.acme.hdb.Landlord%23" + landlordIC;
            System.out.println("*** LANDLORD ID IS " + landlordId);
            WebTarget myResource = CLIENT.target(COMPOSER_URL).path("queries").path(methodName).queryParam("landlordId", landlordId);
            System.out.println("**** FULL PATH" + CLIENT.target(COMPOSER_URL).path("queries").path(methodName).queryParam("landlordId", landlordId).getUri());
            Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            int responseStatus = response.getStatus();
            int noOfTenancyAgreement = 0;

            System.out.println("*** RESPONSE IS  " + response.getStatusInfo());
            if (responseStatus == 200) {
               
                String tenancyAgreementAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(tenancyAgreementAssets));
                JsonArray tenancyAgreeemnt = reader.readArray();
                noOfTenancyAgreement = tenancyAgreeemnt.size();
                System.out.println("**** NOOFTA  " + noOfTenancyAgreement );
                for(int i=0; i<noOfTenancyAgreement; i++){
                  String tenancyAgreementId =  tenancyAgreeemnt.getJsonObject(i).getString("agreementId");
                  String dateCreated =  tenancyAgreeemnt.getJsonObject(i).get("dateCreated").toString(); //need to substring
                  String contractStartDate = tenancyAgreeemnt.getJsonObject(i).get("startDate").toString(); /// need to substring
                  double securityDeposit =  Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("securityDeposit").toString());
                  double advanceRentalFee =  Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("advanceRentalFee").toString());
                  double rentalFee = Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("rentalFee").toString());
                  int rentalDuration = tenancyAgreeemnt.getJsonObject(i).getInt("duration");
                  String contractStatus = tenancyAgreeemnt.getJsonObject(i).getString("status");
                  JsonArray listOfSignature =  tenancyAgreeemnt.getJsonObject(i).getJsonArray("signatureList");
                  String houseId = tenancyAgreeemnt.getJsonObject(i).getString("house");
                  
                  for(int j=0; j<listOfSignature.size(); j++){
                    // I NEED TO MAKE A CALL TO GET TENANT INFORMATION BY PASSING SIGNATURE ID .
                     
                   //  ONCE I get TENANT ID , I NEED TO QUERY MY DATABASE AGAIN....
                   
                    String tenancySignature = listOfSignature.getJsonString(i).toString();
                    System.out.println("** tenant signature: " + tenancySignature);
                    tenancySignature = tenancySignature.split("#")[1];
                    System.out.println("the tenant signature ID is: " + tenancySignature);   
                    //get TenantSignature 
                    
                    
                    
                    
                    WebTarget myTenantSig = CLIENT.target(COMPOSER_URL).path("/org.acme.hdb.TenancySignature/" + tenancySignature);
                    invocationBuilder = myTenantSig.request(MediaType.APPLICATION_JSON);
                    
                    String tsAsset = invocationBuilder.get(String.class);
                    reader = Json.createReader(new StringReader(tsAsset));
                    JsonObject ts = reader.readObject();
                    
                    Response response2 = invocationBuilder.get();
                    int responseStatus2 = response2.getStatus();
                    if (responseStatus2 == 200) {
                      String tenant =  ts.getString("tenant");
                      tenant = tenant.split("#")[1];
                      System.out.println("the tenant  ID is: " + tenant);  
                      
                     Query q =  em.createQuery("SELECT t FROM HDBlockUserEntity t WHERE t.identificationNo =:tenantIc");
                     q.setParameter("tenantIc", tenant);
                     HDBlockUserEntity tenantData = (HDBlockUserEntity) q.getSingleResult();
                     
                     String firstName = tenantData.getFirstName();
                     String lastName = tenantData.getLastName();
                     String email = tenantData.getEmail();
                     boolean tenantStatus = tenantData.isStatusIsValid();
                     tenants.add(new Tenant(firstName, lastName, email, tenantStatus));
                                  
                    }
                   
                  }
     
                    LandlordTenancyAgreement ta = new LandlordTenancyAgreement(tenancyAgreementId, dateCreated , contractStartDate, securityDeposit , advanceRentalFee , rentalDuration, rentalFee , tenants, contractStatus);
                    tenancyAgreements.add(ta);
                }
            }

            
        }catch(Exception ex){
            
         ex.printStackTrace();
        }
          
        return tenancyAgreements;
        
    }

    
    @Override
    public void updateUserIdentity(String userIdentificationNo){
        
        try{
            
                  HDBlockUserEntity userToBeUpdated = em.find(HDBlockUserEntity.class, userIdentificationNo );
            
            if(userToBeUpdated != null)
            {
                
                userToBeUpdated.setStatusIsValid(true);
                em.flush();
                
            }
        }
        catch(Exception ex){
         ex.printStackTrace();
        }

      
            
        
        
    }
    
    
    @Override
     public List<TenantTenancySignature> retrieveTenancySignature(String tenantIc){
        
         List<TenantTenancySignature> ts = new ArrayList<>();


        //tenantId=resource%3Aorg.acme.hdb.Tenant%23S9876541G
        
        //return list of signature  each signature (status )
       // http://localhost:3000/api/queries/GetTenancySignatureByTenantId?tenantId="resource:org.acme.hdb.Tenant#t1\
       
         String tenantFullIC_Path = "resource%3Aorg.acme.hdb.Tenant%23" + tenantIc;
         System.out.println("************* TENANT ID IS " + tenantFullIC_Path );
         WebTarget myResource = CLIENT.target(COMPOSER_URL).path("queries").path("GetTenancySignatureByTenantId").queryParam("tenantId", tenantFullIC_Path);
         System.out.println("************************** FULL PATH" + CLIENT.target(COMPOSER_URL).path("queries").path("GetTenancySignatureByTenantId").queryParam("tenantId", tenantIc).getUri());
         Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
         Response response = invocationBuilder.get();
         int responseStatus = response.getStatus();
   
            System.out.println("***************** Response IS  " + response.getStatusInfo());
         //GETTING TENANT VALUE
            if (responseStatus == 200) {
                 int noOfTs = 0;
                String tenancySignatureAssets = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(tenancySignatureAssets));
                JsonArray tenancySignature = reader.readArray();
                noOfTs = tenancySignature.size();
                
                System.out.println("************* TENANCY SIGNATURE " + noOfTs);
                
                
                  for(int i=0; i<noOfTs; i++){
                     // String sigId = tenancySignature.getJsonObject(i).getString("signatureId").split("_")[0];
                      boolean isSigned = tenancySignature.getJsonObject(i).getBoolean("isSigned");
                      String tenantId = tenancySignature.getJsonObject(i).getString("tenant").split("#")[1];
                      String agreemetId = tenancySignature.getJsonObject(i).getString("agreement").split("#")[1];
                      String signatureId = tenancySignature.getJsonObject(i).getString("signatureId");
                      
                      
                    TenantTenancySignature tts = new TenantTenancySignature(signatureId, isSigned, tenantId, agreemetId);
                    System.out.println("SINFOR  " + isSigned +  " " + tenantId  + " " + agreemetId  );
                    System.out.println("is Singed " + tenancySignature.getJsonObject(i).get("isSigned"));
                    
                    
                    WebTarget myTenantAgreement = CLIENT.target(COMPOSER_URL).path("/org.acme.hdb.TenancyAgreement/" + agreemetId);
                    
                    System.out.println("********* FULL PATH : " + CLIENT.target(COMPOSER_URL).path("/org.acme.hdb.TenancyAgreement/" + agreemetId).getUri());
                    invocationBuilder = myTenantAgreement.request(MediaType.APPLICATION_JSON);
                    
                    String taAsset = invocationBuilder.get(String.class);
                    reader = Json.createReader(new StringReader(taAsset));
                    JsonObject ta = reader.readObject();
                    
                    Response response2 = invocationBuilder.get();
                    int responseStatus2 = response2.getStatus();
                    if (responseStatus2 == 200) {
                      String dateCreated = ta.get("dateCreated").toString();
                     String contractStartDate = ta.get("startDate").toString();/// need to substring
                     double securityDeposit =  Double.parseDouble(ta.get("securityDeposit").toString());
                  double advanceRentalFee =  Double.parseDouble(ta.get("advanceRentalFee").toString());
                  double rentalFee = Double.parseDouble(ta.get("rentalFee").toString());
                  int rentalDuration = ta.getInt("duration");
                  String contractStatus = ta.getString("status");
                   String landlordId = ta.getString("landlord").split("#")[1];
                  
                   Query q =  em.createQuery("SELECT l FROM HDBlockUserEntity l WHERE l.identificationNo =:landlordIc");
                   q.setParameter("landlordIc", landlordId);
                   HDBlockUserEntity landlordData = (HDBlockUserEntity) q.getSingleResult();
                   String landlordName =  landlordData.getFirstName() + " " + landlordData.getLastName();
                   String contactNo = landlordData.getPhoneNo();
                   HDBHouseEntity landlordHouse = landlordData.getHouse();
                   String houseAddress = landlordHouse.getAddress() + " " + landlordHouse.getUnitNumber() + " " + landlordHouse.getPostalCode();
                    //  System.out.println("start Date " + tenancyAgreeemnt.getJsonObject(i).get("startDate"));
                    
                    TenantTenancyAgreement taInfor = new TenantTenancyAgreement(rentalDuration, securityDeposit, advanceRentalFee, rentalFee, contractStartDate, dateCreated, landlordName, contactNo, houseAddress, contractStatus);
                    tts.setTenantTa(taInfor);
                    ts.add(tts);
                                       
                    }
                  }
            }
                   
        return ts;
            //return null;
        
     }
     
     
     
    @Override
     public boolean SignContract(String signatureId){
         
        WebTarget myResource;
        Response signContractResponse;

        myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.SignTenancyAgreement");
        System.out.println("*full path " + myResource.getUri());
        SignContract signContact = new SignContract("org.acme.hdb.SignTenancyAgreement", signatureId);
        signContractResponse = myResource.request().post(Entity.json(signContact));
           
            System.out.println("************* Response " + signContractResponse.getStatusInfo());
             if(signContractResponse.getStatus() == 200){
                 return true;
                 
             }
        
         return false;
     }
       


    
    
    
    @Override
    public boolean createNewTenancyAgreement(Date rentalStartDate, int rentalDuration , double securityDeposit , double advanceRentalFee, double rentalFee, String[] tenantsId , String tenancyId ){
        
        String houseID_Formatted = "house1234" ;
        System.out.println("*********** Creating Tenancy Agreement");
        WebTarget myResource;
        Response createTenancyAgreementResponse;
        int responseStatus = 0;
        myResource = CLIENT.target(COMPOSER_URL).path(CREATE_TENANCY_AGREEMENT_ASSET_ORG);
        TenancyAgreementAsset taAsset = new TenancyAgreementAsset(CREATE_TENANCY_AGREEMENT_ASSET_ORG, tenancyId, rentalStartDate, rentalDuration, securityDeposit, advanceRentalFee, rentalFee, tenantsId, houseID_Formatted);

        createTenancyAgreementResponse = myResource.request().post(Entity.json(taAsset));
        responseStatus = createTenancyAgreementResponse.getStatus();
        System.out.println("*********** CREATE TENACNY AGREEMENT  RESPONSE IS " + responseStatus);
        
        if(responseStatus == 200)
            return true;
        
        return false;
//               


        
        /*{
  "$class": "org.acme.hdb.CreateTenancyAgreement",
  "agreementId": "a2",
  "startDate": "2018-04-07T12:24:51.792Z",
  "duration": 0,
  "securityDeposit": 0,
  "advanceRentalFee": 0,
  "rentalFee": 0,
  "tenants": ["t1","t2"],
  "house": "h1"*/

        
        //http://localhost:3000/api/org.acme.hdb.CreateTenancyAgreement
        
        
    }
}
