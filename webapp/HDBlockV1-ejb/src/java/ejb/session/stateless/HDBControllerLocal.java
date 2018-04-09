/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HDBStaffEntity;
import entity.HDBRentingPolicyEntity;
import entity.HDBHouseEntity;
import entity.HDBHouseOwnerRecordEntity;
import entity.HDBlockUserEntity;
import javax.ejb.Local;
import java.util.List;
import util.exception.CreateNewUserException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.CreateNewPolicyException;
import util.exception.HDBRecordNotFoundException;
import util.helperclass.PendingHouse;


/**
 *
 * @author Steph
 */
@Local
public interface HDBControllerLocal {
    
    public HDBStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException;
    public HDBStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;
    public HDBStaffEntity createNewStaff(HDBStaffEntity newStaff);
    public List<HDBRentingPolicyEntity> retrieveHDBRentingPolicies();
    public HDBRentingPolicyEntity createNewPolicy(HDBRentingPolicyEntity newPolicy) throws CreateNewPolicyException;
    public HDBRentingPolicyEntity updatePolicy(HDBRentingPolicyEntity editedPolicy);
    public List<PendingHouse> retrieveHouseWithPendingStatus();
    public List<HDBHouseEntity> retrieveAllHouses();
    public boolean processHouseValidity(PendingHouse house) throws HDBRecordNotFoundException;

    public HDBHouseOwnerRecordEntity createNewOwner(HDBHouseOwnerRecordEntity newOwner);
    
    
    
}
