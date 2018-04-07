/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.HDBStaffEntity;
import entity.HDBRentingPolicyEntity;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.ArrayList;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author Steph
 */
@Stateless
public class HDBController implements HDBControllerLocal {

    @PersistenceContext(unitName = "HDBlockV1-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public HDBStaffEntity createNewStaff(HDBStaffEntity newStaff){
        
        em.persist(newStaff);
        em.flush();
        em.refresh(newStaff);
        
        return newStaff;   
    }
    
    
    @Override
    public HDBStaffEntity retrieveUserByUsername(String username) throws StaffNotFoundException
    {
        //ProductResource_JerseyClient client = new ProductResource_JerseyClient();
        
        Query query = em.createQuery("SELECT i FROM HDBStaffEntity i WHERE i.userName =:username");
        query.setParameter("username", username);
        
        try
        {
           return (HDBStaffEntity)query.getSingleResult(); 
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            System.out.println("Exception");
            throw new StaffNotFoundException("Staff username " + username + " does not exist!");
        }
             
    }
    
    @Override
    public List<HDBRentingPolicyEntity> retrieveHDBRentingPolicies() {
        TypedQuery<HDBRentingPolicyEntity> query = em.createQuery("SELECT i FROM HDBRentingPolicyEntity i", HDBRentingPolicyEntity.class);
        return query.getResultList();
    }
    
    
    @Override
    public HDBStaffEntity staffLogin(String username, String password) throws InvalidLoginCredentialException{  //simple login logic
        
          try
        {
            HDBStaffEntity staff = retrieveUserByUsername(username);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + staff.getSalt()));
            
            if(staff.getPassword().equals(passwordHash))
            {
                return staff;
            }
            else
            {
                throw new InvalidLoginCredentialException(passwordHash);
            }
        }
        catch(StaffNotFoundException ex)
        {
            String msg = "username does not exist or invalid password!";
            throw new InvalidLoginCredentialException(ex.getMessage());
        }
        
    }
    
    
    
    
    
    
    
}
