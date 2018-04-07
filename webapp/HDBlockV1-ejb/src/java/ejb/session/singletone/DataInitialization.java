/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singletone;

import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import ejb.session.stateless.ICAControllerLocal;
import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import entity.ICAForeignIdentificationRecordEntity;
import entity.ICAIdentificationRecordEntity;
import entity.ICAStaffEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import util.exception.CreateNewHouseException;
import util.exception.CreateNewUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author David
 */
@Startup
@Singleton
@LocalBean
public class DataInitialization {

    @EJB(name = "ICAControllerLocal")
    private ICAControllerLocal iCAControllerLocal;

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    public DataInitialization() {
    }

    @PostConstruct
    public void postConstruct() {

        try {

            if(hDBlockUserEntityControllerLocal.retrieveAllUser().isEmpty()){
                
                System.out.println("********** EMPTY");
            initializeData();}
            
        } catch (Exception ex) {

        }

    }

    public void initializeData() throws CreateNewUserException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        try {

            Date tenantDob = new Date();
            Date landlordDob = new Date();
            Date identity1IssueDate = new Date();
            Date identity1ValidityDate = new Date();
            Date identity1BirthDate = new Date();

            Date identity2IssueDate = new Date();
            Date identity2ValidityDate = new Date();
            Date identity2BirthDate = new Date();

            try {

                tenantDob = sdf.parse("21-04-1992");
                landlordDob = sdf.parse("10-09-1980");
                
                identity1IssueDate = sdf.parse("20-05-2015");
                identity1ValidityDate = sdf.parse("20-05-2019");
                identity1BirthDate = sdf.parse("10-08-1980");

                identity2IssueDate = sdf.parse("07-11-2016");
                identity2ValidityDate = sdf.parse("10-11-2020");
                identity2BirthDate = sdf.parse("21-04-1992");

                Date c = sdf.parse("2015-05-26");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            HDBlockUserEntity landlord1 = new HDBlockUserEntity("S1234567G", "mary80@gmail.com", "Mary", "Tan", "87762123", landlordDob , "landlord", "password");
            landlord1 = hDBlockUserEntityControllerLocal.registerAccount(landlord1);

            HDBlockUserEntity tenant1 = new HDBlockUserEntity("S9876541G", "david93@gmail.com", "David", "Tan", "93219083", tenantDob, "tenant", "password");
            tenant1 = hDBlockUserEntityControllerLocal.registerAccount(tenant1);

            HDBHouseEntity house1 = new HDBHouseEntity("Hougang Avenue 1", "#11-121", "531012", "2 Room Flat", "XXXXX", 1700.00, landlord1);
            house1 = hDBlockUserEntityControllerLocal.registerHouse(house1, landlord1.getEmail());

            ICAStaffEntity staff1 = new ICAStaffEntity("detan1993", "password", "David", "Tan");
            iCAControllerLocal.createNewStaff(staff1);
            ICAIdentificationRecordEntity identity1 = new ICAIdentificationRecordEntity("S1234567G", "Mary Tan", identity1IssueDate, identity1ValidityDate, identity1BirthDate, "Permanent Resident");
            iCAControllerLocal.createNewIdentificationRecord(identity1);

            ICAIdentificationRecordEntity identity2 = new ICAIdentificationRecordEntity("S9876541G", "David Tan", identity2IssueDate, identity2ValidityDate, identity2BirthDate, "Student Pass");
            iCAControllerLocal.createNewIdentificationRecord(identity2);

        } catch (CreateNewUserException | UserNotFoundException | CreateNewHouseException ex) {
            throw new CreateNewUserException("HELLO");
        }

        
        //THIS IS TO TEST CALLING OF BLOCKCHAIN COMPOSER REST.
        //  hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByIC("TESTING");
        //   hDBlockUserEntityControllerLocal.testBlockChainCode();
        //  iCAControllerLocal.retrieveUserWithPendingStatus();
    }

}