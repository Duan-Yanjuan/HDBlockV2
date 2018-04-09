/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ICAForeignIdentificationRecordEntity;
import entity.ICAIdentificationRecordEntity;
import entity.ICAStaffEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.ICRecordNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.helperclass.PendingUser;

/**
 *
 * @author David
 */
@Local
public interface ICAControllerLocal {
 
    
     public ICAStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException;
     public ICAStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException;
    public ICAStaffEntity createNewStaff(ICAStaffEntity newStaff);

    public List<PendingUser> retrieveUserWithPendingStatus();

    public boolean processUserIdentity(PendingUser user) throws ICRecordNotFoundException;

    public List<ICAIdentificationRecordEntity> retrieveAllIdentification();

    public ICAIdentificationRecordEntity createNewIdentificationRecord(ICAIdentificationRecordEntity newRecord);

    public ICAIdentificationRecordEntity retrieveAllIdentificationById(String IC);

    public boolean finalProcessUserIdentity(String ic, String userType, String result);




     
}
