/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import datamodel.ws.HouseAsset;
import datamodel.ws.LandlordAsset;
import datamodel.ws.SignContract;
import datamodel.ws.TenancyAgreementAsset;
import datamodel.ws.TenantAsset;
import datamodel.ws.UpdateTenancyAgreement;
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
    private final String COMPOSER_URL = "http://172.25.107.104:3000/api"; // PLEASE AMEND TO YOUR OWN URL.
    private final String TENANT_ASSET_ORG = "org.acme.hdb.RegisterAsTenant";
    private final String LANDLORD_ASSET_ORG = "org.acme.hdb.RegisterAsLandlord";
    private final String CREATE_TENANCY_AGREEMENT_ASSET_ORG = "org.acme.hdb.CreateTenancyAgreement";
    private final String TENANCY_SIGNATURE_ASSET_ORG = "org.acme.hdb.TenancySignature";
    private final String HOUSE_ASSET_ORG = "org.acme.hdb.House";
    private final String STAMP_CERTIFICATE_ASSET_ORG = "org.acme.hdb.StampCertificate";

    private final Client CLIENT = ClientBuilder.newClient();

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public HDBlockUserEntity registerAccount(HDBlockUserEntity newUser) throws CreateNewUserException {

        try {
            em.persist(newUser);
            em.flush();
            em.refresh(newUser);

            WebTarget myResource;
            Response registerUserResponse;
            int responseStatus = 0;
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            if (newUser.getUserType().equals("tenant")) {
                System.out.println("*********** REGISTER Tenant RESPONSE IS ");
                myResource = CLIENT.target(COMPOSER_URL).path(TENANT_ASSET_ORG);
                Date tenantDob = newUser.getDateOfBirth();
                String tenantDob_String = df.format(tenantDob);
                System.out.println("********************** tenant dob " + tenantDob_String);
                TenantAsset tenant = new TenantAsset(TENANT_ASSET_ORG, newUser.getIdentificationNo(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending", tenantDob_String);
                registerUserResponse = myResource.request().post(Entity.json(tenant));
                responseStatus = registerUserResponse.getStatus();
                System.out.println("*********** REGISTER TENANT RESPONSE IS " + responseStatus);

            } else if (newUser.getUserType().equals("landlord")) {
                System.out.println("*********** REGISTER Landlord RESPONSE IS ");
                //CALL COMPOSER REST SERVER TO CREATE NEW USER(lANDLORD).
                myResource = CLIENT.target(COMPOSER_URL).path(LANDLORD_ASSET_ORG);
                System.out.println("******** RESOURCES IS " + myResource.getUri());
                Date landlordDob = newUser.getDateOfBirth();
                String landlordDob_String = df.format(landlordDob);
                System.out.println("********************** Lanldord dob " + landlordDob_String);
                System.out.println("********************** Lanldord dob " + newUser.getIdentificationNo());
                System.out.println("********************** Lanldord dob " + newUser.getEmail());
                System.out.println("********************** Lanldord dob " + newUser.getFirstName());
                System.out.println("********************** Lanldord dob " + newUser.getLastName());
                System.out.println("********************** Lanldord dob " + landlordDob_String);

                LandlordAsset landlord = new LandlordAsset(LANDLORD_ASSET_ORG, newUser.getIdentificationNo(), newUser.getEmail(), newUser.getFirstName(), newUser.getLastName(), "Pending", landlordDob_String);
                registerUserResponse = myResource.request().post(Entity.json(landlord));
                responseStatus = registerUserResponse.getStatus();
                System.out.println("*********** REGISTER Landlord RESPONSE IS " + responseStatus);
                System.out.println("********************* INFOR " + registerUserResponse.getStatusInfo());

            }

            return newUser;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null
                    && ex.getCause().getCause() != null
                    && ex.getCause().getCause().getClass().getSimpleName().equals("MySQLIntegrityConstraintViolationException")) {
                throw new CreateNewUserException("Email or IC already exist");
            } else {
                throw new CreateNewUserException("An unexpected error has occurred: " + ex.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CreateNewUserException("An unexpected error has occurred: " + ex.getMessage());
        }

    }

    @Override
    public HDBHouseEntity registerHouse(HDBHouseEntity newHouse, String landlordEmail) throws UserNotFoundException, CreateNewHouseException {

        try {

            em.persist(newHouse);
            em.flush();
            em.refresh(newHouse);
            HDBlockUserEntity landlordInformation = retrieveUserByEmail(landlordEmail);
            System.out.println("*********** REGISTER House RESPONSE IS ");
            String landlordIC = landlordInformation.getIdentificationNo();
            WebTarget myResource;
            Response registerHouseResponse;
            int responseStatus = 0;
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

            System.out.println("*********** REGISTER House RESPONSE IS ");
            //CALL COMPOSER REST SERVER TO CREATE NEW USER(TENANT).
            myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.RegisterHouse");
            System.out.println("*********** PATH IS  " + myResource.getUri());
            String houseId = newHouse.getPostalCode() + "_" + newHouse.getUnitNumber().substring(1);
            HouseAsset ha = new HouseAsset("org.acme.hdb.RegisterHouse", houseId, newHouse.getAddress(), newHouse.getFlatType(), landlordIC);
            System.out.println("********************* HOUSE ID IS " + houseId);
            registerHouseResponse = myResource.request().post(Entity.json(ha));
            responseStatus = registerHouseResponse.getStatus();
            System.out.println("*********** REGISTER House RESPONSE IS " + responseStatus);

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
            HDBlockUserEntity userStatus = (HDBlockUserEntity) query.getSingleResult();
            if (userStatus.isStatusIsValid()) {
                System.out.println("Status is Valid");
                return userStatus;
            }

            return userStatus;
        } catch (NoResultException | NonUniqueResultException ex) {
            System.out.println("Exception");
            throw new UserNotFoundException("User email " + email + " does not exist!");
        }

    }

    @Override
    public List<HDBlockUserEntity> retrieveAllUser() {

        Query query = em.createQuery("SELECT u FROM HDBlockUserEntity u");
        System.out.println("********** RETRIEVE USER " + query.getResultList().size());
        return query.getResultList();
    }

    @Override
    public HDBlockUserEntity userLogin(String email, String password) throws InvalidLoginCredentialException {  //simple login logic

        try {
            HDBlockUserEntity user = retrieveUserByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + user.getSalt()));

            if (user.getPassword().equals(passwordHash) && user.isStatusIsValid()) {
                return user;
            } else if (user.getPassword().equals(passwordHash) && !user.isStatusIsValid()) {
                throw new InvalidLoginCredentialException("Account has not been activated!");
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
    public List<HDBHouseEntity> retrieveLandlordHouseByEmail(String email) {

        Query getHouses = em.createQuery("SELECT h FROM HDBHouseEntity h WHERE h.houseOwner.email = :uEmail");
        getHouses.setParameter("uEmail", email);

        return getHouses.getResultList();
    }

    @Override
    public void addNewTenant(HDBlockUserEntity tenant, String tenancyAgreementId) {
        String tenantIc = tenant.getIdentificationNo();

    }

    @Override
    public boolean signTenancy(String email, String tenancyAgreementId) {

        String tenantIc = getUsersIdentificationNumber(email);

        return true;

    }

    public boolean payStampDuty(Double dutyAmount, String tenancyAgreementId) {

        return true;

    }

    private String getTenantFullName(String email) {

        HDBlockUserEntity userName = (HDBlockUserEntity) em.createQuery("SELECT c FROM HDBlockUserEntity c WHERE c.email = :cEmail").setParameter("cEmail", email).getSingleResult();
        return userName.getFirstName() + " " + userName.getLastName();
    }

    private String getUsersIdentificationNumber(String email) {

        HDBlockUserEntity userName = (HDBlockUserEntity) em.createQuery("SELECT c FROM HDBlockUserEntity c WHERE c.email = :cEmail").setParameter("cEmail", email).getSingleResult();
        if (userName != null) {
            return userName.getIdentificationNo();
        }

        return null;
    }

    @Override
    public void retrieveCustomerTest() {

        System.out.println("****************** TESTING CLIENT");
        try {

            Client client = ClientBuilder.newClient();

            WebTarget myResource = client.target("http://localhost:3446/FoodEmblemV1-war/Resources").path("Customer").path("login").path("Jy@gmail.com").path("12345678");
            Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            String mesg = invocationBuilder.get(String.class);

            JsonReader reader = Json.createReader(new StringReader(mesg));
            JsonStructure jsonst = reader.read();
            JsonObject object = (JsonObject) jsonst;

            System.out.println("*************** response " + response.getStatus());
            System.out.println("*************** msg " + mesg);

            boolean a = Boolean.parseBoolean(object.get("loginSuccess").toString());
            System.out.println("*****************" + a + " ");

        } catch (NullPointerException | IllegalArgumentException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void testBlockChainCode() {

        System.out.println("*****************inside testblockchiancode");

        try {

            WebTarget myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.Tenant");
            Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.get();
            int resultFromChainCode = 0;

            if (response.getStatus() == 200) {
                String mesg = invocationBuilder.get(String.class);
                JsonReader reader = Json.createReader(new StringReader(mesg));
                JsonArray noOfTenant = reader.readArray();

                for (int i = 0; i < noOfTenant.size(); i++) {

                    System.out.println("**************** ID[" + i + "] is " + noOfTenant.getJsonObject(i).getString("id"));
                    System.out.println("**************** email[" + i + "] is " + noOfTenant.getJsonObject(i).getString("email"));
                    System.out.println("**************** firstName[" + i + "] is " + noOfTenant.getJsonObject(i).getString("firstName"));
                    System.out.println("**************** lastName[" + i + "] is " + noOfTenant.getJsonObject(i).getString("lastName"));
                    System.out.println("**************** ICStatus[" + i + "] is " + noOfTenant.getJsonObject(i).getString("ICStatus"));

                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<LandlordTenancyAgreement> retrieveTenancyAgreementByLandlordId(String landlordIC) {

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
                System.out.println("**** NOOFTA  " + noOfTenancyAgreement);
                for (int i = 0; i < noOfTenancyAgreement; i++) {
                    String tenancyAgreementId = tenancyAgreeemnt.getJsonObject(i).getString("agreementId");
                    String dateCreated = tenancyAgreeemnt.getJsonObject(i).get("dateCreated").toString(); //need to substring
                    String contractStartDate = tenancyAgreeemnt.getJsonObject(i).get("startDate").toString(); /// need to substring
                    double securityDeposit = Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("securityDeposit").toString());
                    double advanceRentalFee = Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("advanceRentalFee").toString());
                    double rentalFee = Double.parseDouble(tenancyAgreeemnt.getJsonObject(i).get("rentalFee").toString());
                    int rentalDuration = tenancyAgreeemnt.getJsonObject(i).getInt("duration");
                    String contractStatus = tenancyAgreeemnt.getJsonObject(i).getString("status");
                    JsonArray listOfSignature = tenancyAgreeemnt.getJsonObject(i).getJsonArray("signatureList");
                    String houseId = tenancyAgreeemnt.getJsonObject(i).getString("house");

                    for (int j = 0; j < listOfSignature.size(); j++) {
                        String tenancySignature = listOfSignature.getJsonString(j).toString();
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
                            String tenant = ts.getString("tenant");
                            tenant = tenant.split("#")[1];
                            System.out.println("the tenant  ID is: " + tenant);

                            Query q = em.createQuery("SELECT t FROM HDBlockUserEntity t WHERE t.identificationNo =:tenantIc");
                            q.setParameter("tenantIc", tenant);
                            HDBlockUserEntity tenantData = (HDBlockUserEntity) q.getSingleResult();

                            String firstName = tenantData.getFirstName();
                            String lastName = tenantData.getLastName();
                            String email = tenantData.getEmail();
                            boolean tenantStatus = tenantData.isStatusIsValid();
                            tenants.add(new Tenant(firstName, lastName, email, tenantStatus));

                        }

                    }

                    LandlordTenancyAgreement ta = new LandlordTenancyAgreement(tenancyAgreementId, dateCreated, contractStartDate, securityDeposit, advanceRentalFee, rentalDuration, rentalFee, tenants, contractStatus);
                    tenancyAgreements.add(ta);
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return tenancyAgreements;

    }

    @Override
    public void updateUserIdentity(String userIdentificationNo) {

        try {

            HDBlockUserEntity userToBeUpdated = em.find(HDBlockUserEntity.class, userIdentificationNo);

            if (userToBeUpdated != null) {

                userToBeUpdated.setStatusIsValid(true);
                em.flush();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public List<TenantTenancySignature> retrieveTenancySignature(String tenantIc) {

        List<TenantTenancySignature> ts = new ArrayList<>();

        try {

            String tenantFullIC_Path = "resource%3Aorg.acme.hdb.Tenant%23" + tenantIc;
            System.out.println("************* TENANT ID IS " + tenantFullIC_Path);
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

                for (int i = 0; i < noOfTs; i++) {
                    // String sigId = tenancySignature.getJsonObject(i).getString("signatureId").split("_")[0];
                    boolean isSigned = tenancySignature.getJsonObject(i).getBoolean("isSigned");
                    String tenantId = tenancySignature.getJsonObject(i).getString("tenant").split("#")[1];
                    String agreemetId = tenancySignature.getJsonObject(i).getString("agreement").split("#")[1];
                    String signatureId = tenancySignature.getJsonObject(i).getString("signatureId");

                    TenantTenancySignature tts = new TenantTenancySignature(signatureId, isSigned, tenantId, agreemetId);
                    System.out.println("SINFOR  " + isSigned + " " + tenantId + " " + agreemetId);
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
                        double securityDeposit = Double.parseDouble(ta.get("securityDeposit").toString());
                        double advanceRentalFee = Double.parseDouble(ta.get("advanceRentalFee").toString());
                        double rentalFee = Double.parseDouble(ta.get("rentalFee").toString());
                        int rentalDuration = ta.getInt("duration");
                        String contractStatus = ta.getString("status");
                        String landlordId = ta.getString("landlord").split("#")[1];

                        Query q = em.createQuery("SELECT l FROM HDBlockUserEntity l WHERE l.identificationNo =:landlordIc");
                        q.setParameter("landlordIc", landlordId);
                        HDBlockUserEntity landlordData = (HDBlockUserEntity) q.getSingleResult();
                        String landlordName = landlordData.getFirstName() + " " + landlordData.getLastName();
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ts;
        //return null;

    }

    private boolean checkSignatureCompleteness(String tenancyId) {

        System.out.println("*** TENANCY  ID IS " + tenancyId);
        WebTarget myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.TenancyAgreement").path(tenancyId);
        System.out.println("**** FULL PATH" + CLIENT.target(COMPOSER_URL).path("org.acme.hdb.TenancyAgreement").path(tenancyId).getUri());
        Invocation.Builder invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        int responseStatus = response.getStatus();
        int noOfTenancyAgreement = 0;
        int noOfTenantSignature = 0;
        int signatureList = 0;

        System.out.println("*** RESPONSE IS  " + response.getStatusInfo());
        if (responseStatus == 200) {

            String tenancyAgreementAssets = invocationBuilder.get(String.class);
            JsonReader reader = Json.createReader(new StringReader(tenancyAgreementAssets));
            //JsonArray tenancyAgreeemnt = reader.readArray();
            JsonObject tenancyAgreement = reader.readObject();

            String tenancyAgreementId = tenancyAgreement.getString("agreementId");
            JsonArray listOfSignature = tenancyAgreement.getJsonArray("signatureList");
            signatureList = listOfSignature.size();
            System.out.println("************ No of signatue required = " + signatureList);
            for (int i = 0; i < listOfSignature.size(); i++) {
                //Format of Singature ID is resource:org.acme.hdb.TenancySignature#53101211-12122042018_0
                //   String signatureID = listOfSignature.getString(i).substring(39);
                String signatureID = tenancyAgreementId + "_" + i;
                System.out.println("singature ID is " + signatureID);
                myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.TenancySignature").path(signatureID);
                invocationBuilder = myResource.request(MediaType.APPLICATION_JSON);
                response = invocationBuilder.get();
                String tenancySignatureAssets = invocationBuilder.get(String.class);
                reader = Json.createReader(new StringReader(tenancySignatureAssets));
                JsonObject tenancySignature = reader.readObject();
                System.out.println("Is Signed " + tenancySignature.getBoolean("isSigned"));
                if (response.getStatus() == 200 && tenancySignature.getBoolean("isSigned")) {
                    noOfTenantSignature++;
                }
            }

            //this means all the tenants in this contract has signed the agreement
            if (noOfTenantSignature == signatureList) {
                return true;
            }

        }

        return false;

    }

    @Override
    public boolean SignContract(String signatureId, String agreementId) {

        WebTarget myResource;
        Response signContractResponse;

        myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.SignTenancyAgreement");
        System.out.println("*full path " + myResource.getUri());
        SignContract signContact = new SignContract("org.acme.hdb.SignTenancyAgreement", signatureId);
        signContractResponse = myResource.request().post(Entity.json(signContact));

        //getting tenancy agreement.
        System.out.println("************* Response " + signContractResponse.getStatusInfo());
        boolean signatureIsComplete = checkSignatureCompleteness(agreementId);
        if (signContractResponse.getStatus() == 200 && signatureIsComplete) {

            myResource = CLIENT.target(COMPOSER_URL).path("org.acme.hdb.UpdateTenancyAgreement");
            System.out.println("*full path of updating tenancy agreement " + myResource.getUri() + " agreemennt ID " + agreementId);
            UpdateTenancyAgreement updateTa = new UpdateTenancyAgreement("org.acme.hdb.UpdateTenancyAgreement", agreementId);
            signContractResponse = myResource.request().post(Entity.json(updateTa));
            System.out.println("************* Response update tenancy agreement is COMPLETE");

            return true;

        } else if (signContractResponse.getStatus() == 200 && !signatureIsComplete) {
            return true;
        }

        return false;
    }

    @Override
    public boolean createNewTenancyAgreement(Date rentalStartDate, int rentalDuration, double securityDeposit, double advanceRentalFee, double rentalFee, String[] tenantsId, String tenancyId, String houseId) {

        String houseID_Formatted = houseId;
        System.out.println("*********** Creating Tenancy Agreement");
        WebTarget myResource;
        Response createTenancyAgreementResponse;
        int responseStatus = 0;
        myResource = CLIENT.target(COMPOSER_URL).path(CREATE_TENANCY_AGREEMENT_ASSET_ORG);
        System.out.println("PATH IS : " + CREATE_TENANCY_AGREEMENT_ASSET_ORG);
        System.out.println("TA ID : " + tenancyId);
        System.out.println("Rental Start Date : " + rentalStartDate);
        System.out.println("Rental Duration ID : " + rentalDuration);
        System.out.println("Security Deposit: " + securityDeposit);
        System.out.println("advanceRentalFee : " + advanceRentalFee);
        System.out.println("rental fee : " + rentalFee);
        System.out.println("tenants ID : " + tenantsId[0]);
        System.out.println("houseID_Formatted : " + houseID_Formatted);

        TenancyAgreementAsset taAsset = new TenancyAgreementAsset(CREATE_TENANCY_AGREEMENT_ASSET_ORG, tenancyId, rentalStartDate, rentalDuration, securityDeposit, advanceRentalFee, rentalFee, tenantsId, houseID_Formatted);

        createTenancyAgreementResponse = myResource.request().post(Entity.json(taAsset));
        responseStatus = createTenancyAgreementResponse.getStatus();
        System.out.println("*********** CREATE TENACNY AGREEMENT  RESPONSE IS " + responseStatus);

        if (responseStatus == 200) {
            return true;
        }

        return false;

    }
}
