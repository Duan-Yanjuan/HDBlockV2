/**
 * HDBlock script file
 */
/**
 * @param {org.acme.hdb.RegisterAsTenant} register as tenant
 * @transaction
 */
function registerAsTenant(tenant) {
  console.log("############################################################");
  console.log('Register as tenant');
  return getAssetRegistry('org.acme.hdb.Tenant')
    .then(function(tenantRegistry) { 
    var factory = getFactory(); 
    var newTenant = factory.newResource('org.acme.hdb', 'Tenant', tenant.id);
    newTenant.id = tenant.id;
    newTenant.email = tenant.email;
    newTenant.firstName = tenant.firstName;
    newTenant.lastName = tenant.lastName;
    newTenant.DOB = tenant.DOB;
    return tenantRegistry.add(newTenant);
  })
  .catch(function (error){
    console.log("registerAsTenant error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.RegisterAsLandlord} register as landlord
 * @transaction
 */
function registerAsLandlord(landlord) {
  console.log("############################################################");
  console.log('Register as landlord');
  return getAssetRegistry('org.acme.hdb.Landlord')
    .then(function(landlordRegistry) {
    var factory = getFactory();
    var newLandlord = factory.newResource('org.acme.hdb', 'Landlord', landlord.id);
    newLandlord.id = landlord.id;
    newLandlord.email = landlord.email;
    newLandlord.firstName = landlord.firstName;
    newLandlord.lastName = landlord.lastName;
    newLandlord.DOB = landlord.DOB;
    return landlordRegistry.add(newLandlord);
  })
    .catch(function (error){
    console.log("registerAsLandlord error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.RegisterHouse} Register House
 * @transaction
 */
function registerHouse(args) {
  console.log("############################################################");
  var landlord = args.landlord
  if (landlord.ICStatus != "Valid") {  // only valid landlords can add houses
    throw new Error("The landlord's IC is not valid!");
  }
  return getAssetRegistry('org.acme.hdb.House')
    .then(function(houseRegistry) {
    var factory = getFactory();
    var newHouse = factory.newResource('org.acme.hdb', 'House', args.houseId);
    newHouse.address = args.address;
    newHouse.type = args.type;
    newHouse.landlord = landlord;
    houseRegistry.add(newHouse);
    console.log("registerHouse: " + newHouse.address);
    console.log("registerHouse: " + newHouse.type);
    console.log("registerHouse: " + newHouse.landlord);
    return getAssetRegistry('org.acme.hdb.Landlord')
      .then(function(landlordRegistry) {
      landlord.house = newHouse;
      console.log("registerHouse: before update wordstate");
      console.log("############################################################");
      return landlordRegistry.update(landlord);
    });
  })
  .catch(function (error){
    console.log("registerHouse error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.ApproveTenantIdentity} Approve Tenant Identity
 * @transaction
 */
function approveTenantIdentity(args) {
  console.log("############################################################");
  var tenant = args.tenant
  tenant.ICStatus = "Valid";
  return getAssetRegistry('org.acme.hdb.Tenant')
    .then(function(tenantRegistry) {
    console.log("approveTenantIdentity: before update wordstate");
    console.log("############################################################");
    return tenantRegistry.update(tenant);
  })
  .catch(function (error){
    console.log("approveTenantIdentity error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.ApproveLandlordIdentity} Approve Landlord Identity
 * @transaction
 */
function approveLandlordIdentity(args) {
  console.log("############################################################");
  var landlord = args.landlord
  landlord.ICStatus = "Valid";
  return getAssetRegistry('org.acme.hdb.Landlord')
    .then(function(landlordRegistry) {
    console.log("approveLandlordIdentity: before update wordstate");
    console.log("############################################################");
    return landlordRegistry.update(landlord);
  })
  .catch(function (error){
    console.log("approveLandlordIdentity error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.ApproveHouse} Approve House
 * @transaction
 */
function approveHouse(house) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.House')
    .then(function(houseRegistry) {
      house.house.status = 'Valid';
      console.log("approveHouse: before update wordstate");
      console.log("############################################################");
      return houseRegistry.update(house.house);
  })
  .catch(function (error){
    console.log("approveHouse error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.UpdateTenantStatus} Update Tenant Status
 * @transaction
 */
function UpdateTenantStatus(args) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.Tenant')
    .then(function(tenantRegistry) {
      args.tenant.ICStatus = args.status;
      console.log("UpdateTenantStatus: before update wordstate");
      console.log("############################################################");
      return tenantRegistry.update(args.tenant);
  })
  .catch(function (error){
    console.log("UpdateTenantStatus error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.UpdateLandlordStatus} Update Landlord Status
 * @transaction
 */
function UpdateLandlordStatus(args) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.Landlord')
    .then(function(landlordRegistry) {
      args.landlord.ICStatus = args.status;
      console.log("UpdateLandlordStatus: before update wordstate");
      console.log("############################################################");
      return landlordRegistry.update(args.landlord);
  })
  .catch(function (error){
    console.log("UpdateLandlordStatus error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.UpdateHouseStatus} Update House Status
 * @transaction
 */
function UpdateHouseStatus(args) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.House')
    .then(function(houseRegistry) {
      args.house.status = args.status;
      console.log("UpdateHouseStatus: before update wordstate");
      console.log("############################################################");
      return houseRegistry.update(args.house);
  })
  .catch(function (error){
    console.log("UpdateHouseStatus error: " + error.message);
  });
}


/**
 * @param {org.acme.hdb.CreateTenancyAgreement} Create Tenancy Agreement
 * @transaction
 */
function createTenancyAgreement(args) {
  console.log("############################################################");
  if (args.house.status == "Invalid" || args.house.status == "Pending" ) {
    throw new Error("The house is not valid!");
  } else if (args.house.status != "Available") {
    throw new Error("The house is not available for sale currently!");
  }
  var landlord = args.house.landlord;
  var tenancyAgreementRegistry;
  var newAgreement;
  var index;
  var tenantList;
  var factory = getFactory();
  return getAssetRegistry('org.acme.hdb.TenancyAgreement')
    .then(function(_tenancyAgreementRegistry) {
    tenancyAgreementRegistry = _tenancyAgreementRegistry;
    newAgreement = factory.newResource('org.acme.hdb', 'TenancyAgreement', args.agreementId);
    newAgreement.dateCreated = new Date();
    newAgreement.startDate = args.startDate;
    newAgreement.duration = args.duration;
    newAgreement.securityDeposit = args.securityDeposit;
    newAgreement.advanceRentalFee = args.advanceRentalFee;
    newAgreement.rentalFee = args.rentalFee;
    newAgreement.house = args.house;
    newAgreement.landlord = args.house.landlord;
    newAgreement.numOfTenants = args.tenants.length;
    console.log("add agreement");
    console.log("createTenancyAgreement: " + newAgreement.dateCreated);
    console.log("createTenancyAgreement: " + newAgreement.startDate);
    console.log("createTenancyAgreement: " + newAgreement.duration);
    console.log("createTenancyAgreement: " + newAgreement.securityDeposit);
    console.log("createTenancyAgreement: " + newAgreement.advanceRentalFee);
    console.log("createTenancyAgreement: " + newAgreement.rentalFee);
    console.log("createTenancyAgreement: " + newAgreement.house);
    console.log("createTenancyAgreement: " + newAgreement.landlord);
    console.log("createTenancyAgreement: " + newAgreement.numOfTenants);
    tenancyAgreementRegistry.add(newAgreement);  

    return getAssetRegistry('org.acme.hdb.Tenant')}) 
      .then(function(tenantRegistry) {
      tenantList = args.tenants;
      index = 0;
      return getAssetRegistry('org.acme.hdb.TenancySignature')})
        .then(function(tenancySignatureRegistry) {
        var signatureList = [];
        while(index < tenantList.length) {
          var tenant = tenantList[index];
          var newSignature = factory.newResource('org.acme.hdb', 'TenancySignature', newAgreement.agreementId + "_" + index);
          newSignature.tenant = tenant;
          newSignature.agreement = newAgreement;
          tenancySignatureRegistry.add(newSignature);  // add a new signature with status of unsigned for each tenant
          index = index + 1;
          signatureList.push(newSignature);
          console.log("add new signature");
          console.log("createTenancyAgreement: " + newSignature.tenant);
          console.log("createTenancyAgreement: " + newSignature.agreement);
        }
        newAgreement.signatureList = signatureList; // append the signature list to tenancy agreement
      console.log("createTenancyAgreement: before update wordstate");
      console.log("############################################################");
        return tenancyAgreementRegistry.add(newAgreement);
      })
    .catch(function(error) {
    console.log("error" + error.message);
  })
  .catch(function (error){
    console.log("createTenancyAgreement error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.SignTenancyAgreement} Sign Tenancy Agreement
 * @transaction
 */
function signTenancyAgreement(args) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.TenancySignature')
    .then(function(tenancySignatureRegistry) {
      args.signature.isSigned = true;
    console.log("signTenancyAgreement: before update wordstate");
    console.log("############################################################");
    return tenancySignatureRegistry.update(args.signature);
  })
  .catch(function (error){
    console.log("signTenancyAgreement error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.UpdateTenancyAgreement} Update Tenancy Agreement
 * @transaction
 */
function updateTenancyAgreement(args) {
  console.log("############################################################");
  var signatureList = args.agreement.signatureList;
  var tenantList = [];
  return getAssetRegistry('org.acme.hdb.TenancyAgreement')
    .then(function(tenancyAgreementRegistry) {
    args.agreement.status = "Signed";
    tenancyAgreementRegistry.update(args.agreement);
    console.log('update agreement');
    console.log(args.agreement.agreementId);
    
    return getAssetRegistry('org.acme.hdb.TenancySignature')})
      .then(function(tenancySignatureRegistry) {
            console.log('list of tenants');
      for(var i = 0; i < signatureList.length; i++){
        var tenant = signatureList[i].tenant;
        tenant.agreement = args.agreement;
        tenantList.push(tenant);
      }
      
      return getAssetRegistry('org.acme.hdb.House')})
      .then(function(houseRegistry) {
        var house = args.agreement.house;
        house.tenants = tenantList;  // append the current tenant to the house
        houseRegistry.update(house);
        console.log("update house");
        console.log(house.tenants);
        console.log("update tenant");
        return getAssetRegistry('org.acme.hdb.Tenant')})
        .then(function(tenantRegistry) {
          for(var i = 0; i < tenantList.length; i++){
          var tenant = tenantList[i];
          console.log("tenant: " + tenant.id);
          tenantRegistry.update(tenant);
          }
        console.log("updateTenancyAgreement: before update wordstate");
        console.log("############################################################");
        return true;
      })
  .catch(function (error){
    console.log("updateTenancyAgreement error: " + error.message);
  });
}


/**
 * @param {org.acme.hdb.ActivateTenancyAgreement} Activate Tenancy Agreement (time-triggered event when the start date approaches
 * @transaction
 */
function ActivateTenancyAgreement(args) {
  console.log("############################################################");
  var agreement = args.agreement;
  if (agreement.status != "Valid") {
    throw new Error("The tenancy agreement is not valid!");
  } else {
    return getAssetRegistry('org.acme.hdb.TenancyAgreement')
    .then(function(agreementRegistry) {
      var agreement = args.agreement;
      agreement.status = "Active";
      return agreementRegistry.update(agreement);
    })
    .catch(function (error){
      console.log("Activate tenancy agreement error: " + error.message);
    });
  }
}

/**
 * @param {org.acme.hdb.EndTenancyAgreement} Terminate Tenancy Agreement (time-triggered event when the end date approaches
 * @transaction
 */
function EndTenancyAgreement(args) {
  console.log("############################################################");
  var agreement = args.agreement;
  if (agreement.status != "Active") {
    throw new Error("The tenancy agreement is not active!");
  } else {
    return getAssetRegistry('org.acme.hdb.TenancyAgreement')
    .then(function(agreementRegistry) {
      agreement.status = "Ended";
      return agreementRegistry.update(agreement);
    })
    .catch(function (error){
      console.log("end tenancy agreement error: " + error.message);
    });
  }
}


/**
 * @param {org.acme.hdb.PayStampDuty} Pay StampDuty
 * @transaction
 */
function payStampDuty(args) {
  console.log("############################################################");
  if (args.agreement.status != "Signed") {
    throw new Error("The tenancy agreement has not been signed");
  }
  var factory = getFactory();
  var certificate;

  return getAssetRegistry('org.acme.hdb.StampCertificate')
    .then(function(stampCertificateRegistry) {
    certificate = factory.newResource('org.acme.hdb', 'StampCertificate', args.certificateId);
    certificate.amount = args.amount;
    certificate.agreement = args.agreement;
    certificate.dateCreated = new Date();
    stampCertificateRegistry.add(certificate);
  
    return getAssetRegistry('org.acme.hdb.TenancyAgreement')})
    .then(function(agreementRegistry) {
      var agreement = args.agreement;
      agreement.isStampDutyPaid = true;
      agreement.stampCertificate = certificate;
      agreement.status = "Valid";
      console.log(agreement.agreementId);
      console.log("payStampDuty: before update wordstate");
      console.log("############################################################");
      return agreementRegistry.update(agreement);
    })
  .catch(function (error){
    console.log("payStampDuty error: " + error.message);
  });
}


/**
 * @param {org.acme.hdb.PaySecurityDeposit} Pay security deposit for the tenancy agreement. The security deposit will be returned back to tenants after end date
 * @transaction
 */
function PaySecurityDeposit(args) {
  console.log("############################################################");
  var agreement = args.agreement;
  if (agreement.status != "Signed" && agreement.status != "Valid") {  // the deposit can be paid only after all tenants sign the tenancy agreement
    throw new Error("The tenancy agreement has not been signed or is not valid!");
  } else {
    var factory = getFactory();
    var record;

    return getAssetRegistry('org.acme.hdb.DepositCertificate')
      .then(function(depositCertificateRegistry) {
      record = factory.newResource('org.acme.hdb', 'DepositCertificate', args.recordId);
      record.amount = args.amount;
      record.agreement = args.agreement;
      record.dateCreated = new Date();
      depositCertificateRegistry.add(record);

      return getAssetRegistry('org.acme.hdb.TenancyAgreement')})
      .then(function(agreementRegistry) {
        var agreement = args.agreement;
        agreement.depositCertificate = record;
        agreement.status = "Valid";
        return agreementRegistry.update(agreement);
      })
      .catch(function (error){
        console.log("paySecurityDeposit error: " + error.message);
      });
  }
}


/**
 * @param {org.acme.hdb.PayAdvanceFee} Pay security deposit for the tenancy agreement. The security deposit will be returned back to tenants after end date
 * @transaction
 */
function PayAdvanceFee(args) {
  console.log("############################################################");
  var agreement = args.agreement;
  if (agreement.status != "Signed" && agreement.status != "Valid") {  // the advance fee can be paid only after all tenants sign the tenancy agreement
    throw new Error("The tenancy agreement has not been signed or is not valid!");
  } else {
    return getAssetRegistry('org.acme.hdb.TenancyAgreement')
    .then(function(agreementRegistry) {
      agreement.advanceRentalFee = args.amount;
      return agreementRegistry.update(agreement);
    })
    .catch(function (error){
      console.log("pay security fee error: " + error.message);
    });
  }
}

