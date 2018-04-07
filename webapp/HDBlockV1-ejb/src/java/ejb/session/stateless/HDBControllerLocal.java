/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HDBStaffEntity;
import entity.HDBRentingPolicyEntity;
import javax.ejb.Local;
import java.util.List;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;

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
    
}
