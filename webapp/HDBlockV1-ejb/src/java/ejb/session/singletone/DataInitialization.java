/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singletone;

import ejb.session.stateless.HDBControllerLocal;
import ejb.session.stateless.HDBlockUserEntityControllerLocal;
import ejb.session.stateless.ICAControllerLocal;
import entity.HDBHouseEntity;
import entity.HDBHouseOwnerRecordEntity;
import entity.HDBlockUserEntity;
import entity.ICAIdentificationRecordEntity;
import entity.ICAStaffEntity;
import entity.HDBStaffEntity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @EJB(name = "HDBControllerLocal")
    private HDBControllerLocal hDBControllerLocal;

    @EJB(name = "ICAControllerLocal")
    private ICAControllerLocal iCAControllerLocal;

    @EJB(name = "HDBlockUserEntityControllerLocal")
    private HDBlockUserEntityControllerLocal hDBlockUserEntityControllerLocal;

    public DataInitialization() {
    }

    @PostConstruct
    public void postConstruct() {

        try {
//       
//       
//            SimpleDateFormat sdfRental = new SimpleDateFormat("yyyy-MM-dd");
//            Date rentalDate = sdfRental.parse("2018-04-30");
//        
//              Date todayDate = new Date();
//              DateFormat df = new SimpleDateFormat("dd/MM/yyyy");      
//             String todayDateString = df.format(todayDate);
//             String [] todayDateFormated = todayDateString.split("/");
//             
//              String [] tenantsId = {"S9876541G"};
//              String houseId = "53101211-12107042018";
//              System.out.println("HOUSE ID " + houseId);
//              hDBlockUserEntityControllerLocal.createNewTenancyAgreement(rentalDate, 2, 3000, 1500, 1500, tenantsId, houseId);
//       
//      
            //  hDBlockUserEntityControllerLocal.retrieveTenancyAgreementByLandlordId("S1234567P");
            //   hDBlockUserEntityControllerLocal.retrieveTenancySinatureByTenantId("S9876541G");

            HDBHouseOwnerRecordEntity owner1 = new HDBHouseOwnerRecordEntity("Hougang Avenue 1", "#11-121", "531012", "2 Room Flat", "Mary Tan", "S1234567P");
            hDBControllerLocal.createNewOwner(owner1);

            if (hDBlockUserEntityControllerLocal.retrieveAllUser().isEmpty()) {

                System.out.println("********** EMPTY");
                initializeData();
            }

        } catch (Exception ex) {

        }

    }

    public void initializeData() throws CreateNewUserException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdfRental = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date rentalDate = new Date();
            Date tenantDob = new Date();
            Date landlordDob = new Date();
            Date identity1IssueDate = new Date();
            Date identity1ValidityDate = new Date();
            Date identity1BirthDate = new Date();

            Date identity2IssueDate = new Date();
            Date identity2ValidityDate = new Date();
            Date identity2BirthDate = new Date();

            Date identity3IssueDate = new Date();
            Date identity3ValidityDate = new Date();
            Date identity3BirthDate = new Date();

            Date identity4IssueDate = new Date();
            Date identity4ValidityDate = new Date();
            Date identity4BirthDate = new Date();

            try {
                rentalDate = sdfRental.parse("2018-04-30");
                tenantDob = sdf.parse("21-04-1992");
                landlordDob = sdf.parse("10-09-1980");

                identity1IssueDate = sdf.parse("20-05-2015");
                identity1ValidityDate = sdf.parse("20-05-2019");
                identity1BirthDate = sdf.parse("10-09-1980");

                identity2IssueDate = sdf.parse("07-11-2016");
                identity2ValidityDate = sdf.parse("10-11-2020");
                identity2BirthDate = sdf.parse("21-04-1992");

                identity3IssueDate = sdf.parse("07-11-1985");
                identity3BirthDate = sdf.parse("21-04-1970");

                identity4IssueDate = sdf.parse("07-11-2017");
                identity4ValidityDate = sdf.parse("10-11-2021");
                identity4BirthDate = sdf.parse("21-04-1993");

                Date c = sdf.parse("2015-05-26");
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            HDBlockUserEntity landlord1 = new HDBlockUserEntity("S1234566P", "mary80@gmail.com", "Mary", "Tan", "87762123", landlordDob, "landlord", "password");
            landlord1 = hDBlockUserEntityControllerLocal.registerAccount(landlord1);

            HDBlockUserEntity tenant1 = new HDBlockUserEntity("S9876542G", "david93@gmail.com", "David", "Tan", "93219083", tenantDob, "tenant", "password");
            tenant1 = hDBlockUserEntityControllerLocal.registerAccount(tenant1);

            HDBHouseEntity house1 = new HDBHouseEntity("Hougang Avenue 1", "#11-121", "531012", "2 Room Flat", "XXXXX", 1700.00, landlord1);
            house1 = hDBlockUserEntityControllerLocal.registerHouse(house1, landlord1.getEmail());

            ICAStaffEntity staff1 = new ICAStaffEntity("detan1993", "password", "David", "Tan");
            iCAControllerLocal.createNewStaff(staff1);

            ICAIdentificationRecordEntity identity1 = new ICAIdentificationRecordEntity("S1234566P", "Mary Tan", identity1IssueDate, identity1ValidityDate, identity1BirthDate, "Permanent Resident");
            iCAControllerLocal.createNewIdentificationRecord(identity1);

            ICAIdentificationRecordEntity identity2 = new ICAIdentificationRecordEntity("S9876542G", "David Tan", identity2IssueDate, identity2ValidityDate, identity2BirthDate, "Student Pass");
            iCAControllerLocal.createNewIdentificationRecord(identity2);

            ICAIdentificationRecordEntity identity3 = new ICAIdentificationRecordEntity("S932190G", "Larry Lam", identity3IssueDate, null, identity3BirthDate, "Citizen");
            iCAControllerLocal.createNewIdentificationRecord(identity3);

            ICAIdentificationRecordEntity identity4 = new ICAIdentificationRecordEntity("G1111111P", "Darren Tan", identity4IssueDate, identity4ValidityDate, identity4BirthDate, "Student Pass");
            iCAControllerLocal.createNewIdentificationRecord(identity4);
            
            HDBStaffEntity hdbStaff1 = new HDBStaffEntity("stephanie", "password", "Stephanie", "Huang");
            hDBControllerLocal.createNewStaff(hdbStaff1);

            Date todayDate = new Date();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String todayDateString = df.format(todayDate);
            String[] todayDateFormated = todayDateString.split("/");

            String[] tenantsId = {"S9876541G"};
            String houseId = house1.getPostalCode() + house1.getUnitNumber().substring(1) + todayDateFormated[0] + todayDateFormated[1] + todayDateFormated[2];
            System.out.println("HOUSE ID " + houseId);

        } catch (CreateNewUserException | UserNotFoundException | CreateNewHouseException ex) {
            throw new CreateNewUserException("HELLO");
        }

    }

}
