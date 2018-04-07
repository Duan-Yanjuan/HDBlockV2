/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperclass;

import java.math.BigDecimal;

/**
 *
 * @author David
 */
public class ProductEntity {
    
     private String skuCode; 
    private String name;    
    private String description;    
    private Integer quantityOnHand;
    private Integer reorderQuantity;
    private BigDecimal unitPrice;    
    private String category;
    private Integer productRating;

    public ProductEntity() {
    }

    public ProductEntity(String skuCode, String name, String description, Integer quantityOnHand, Integer reorderQuantity, BigDecimal unitPrice, String category, Integer productRating) {
        this.skuCode = skuCode;
        this.name = name;
        this.description = description;
        this.quantityOnHand = quantityOnHand;
        this.reorderQuantity = reorderQuantity;
        this.unitPrice = unitPrice;
        this.category = category;
        this.productRating = productRating;
    }

    /**
     * @return the skuCode
     */
    
    
    
    
    public String getSkuCode() {
        return skuCode;
    }

    /**
     * @param skuCode the skuCode to set
     */
    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the quantityOnHand
     */
    public Integer getQuantityOnHand() {
        return quantityOnHand;
    }

    /**
     * @param quantityOnHand the quantityOnHand to set
     */
    public void setQuantityOnHand(Integer quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    /**
     * @return the reorderQuantity
     */
    public Integer getReorderQuantity() {
        return reorderQuantity;
    }

    /**
     * @param reorderQuantity the reorderQuantity to set
     */
    public void setReorderQuantity(Integer reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    /**
     * @return the unitPrice
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice the unitPrice to set
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the productRating
     */
    public Integer getProductRating() {
        return productRating;
    }

    /**
     * @param productRating the productRating to set
     */
    public void setProductRating(Integer productRating) {
        this.productRating = productRating;
    }
}
