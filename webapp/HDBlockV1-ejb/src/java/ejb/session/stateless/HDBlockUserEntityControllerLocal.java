/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HDBHouseEntity;
import entity.HDBlockUserEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewHouseException;
import util.exception.CreateNewUserException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserNotFoundException;
import util.helperclass.TenancyAgreement;

/**
 *
 * @author David
 */
@Local
public interface HDBlockUserEntityControllerLocal {
    
    
    public HDBlockUserEntity retrieveUserByEmail(String email) throws UserNotFoundException;
    public HDBlockUserEntity userLogin(String email, String password) throws InvalidLoginCredentialException;

    public void retrieveCustomerTest();

    public List<HDBHouseEntity> retrieveAllAvailableHouse(); //for tenant

    public void addNewTenant(HDBlockUserEntity tenant, String tenancyAgreementId);

    public boolean signTenancy(String email, String tenancyAgreementId);

    public List<TenancyAgreement> retrieveTenancyAgreementByIC(String identificationNumer);

    public HDBlockUserEntity registerAccount(HDBlockUserEntity newUser) throws CreateNewUserException;

    public void testBlockChainCode();

    public List<HDBHouseEntity> retrieveLandlordHouseByEmail(String email);

    public HDBHouseEntity registerHouse(HDBHouseEntity newHouse, String landlordEmail) throws UserNotFoundException, CreateNewHouseException;

    public List<HDBlockUserEntity> retrieveAllUser();

 

}
