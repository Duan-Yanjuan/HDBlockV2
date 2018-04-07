/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamodel.ws;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import util.helperclass.ProductEntity;

/**
 *
 * @author David
 */

@XmlRootElement
@XmlType(name = "newProduct", propOrder = {
    "username",
    "password",
    "newProductEntity"
})
public class Product {
    
     private String username;
    private String password;
    private ProductEntity newProductEntity;

    public Product() {
    }

    public Product(String username, String password, ProductEntity newProductEntity) {
        this.username = username;
        this.password = password;
        this.newProductEntity = newProductEntity;
    }
    
    

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the newProductEntity
     */
    public ProductEntity getNewProductEntity() {
        return newProductEntity;
    }

    /**
     * @param newProductEntity the newProductEntity to set
     */
    public void setNewProductEntity(ProductEntity newProductEntity) {
        this.newProductEntity = newProductEntity;
    }
    
}
