/**
 * HDBlock script file
 */
/**
 * @param {org.acme.hdb.RegisterAsTenant} register as tenant
 * @transaction
 */
function registerAsTenant(tenant) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.Tenant')
    .then(function(tenantRegistry) { 
    var factory = getFactory(); 
    var newTenant = factory.newResource('org.acme.hdb', 'Tenant', tenant.id);
    newTenant.id = tenant.id;
    newTenant.email = tenant.email;
    newTenant.firstName = tenant.firstName;
    newTenant.lastName = tenant.lastName;
    newTenant.DOB = tenant.DOB;
    console.log("############################################################");
    return tenantRegistry.add(newTenant);
  })
  .catch(function (errir){
    concole.log("registerAsTenant error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.RegisterAsLandlord} register as landlord
 * @transaction
 */
function registerAsLandlord(landlord) {
  console.log("############################################################");
  return getAssetRegistry('org.acme.hdb.Landlord')
    .then(function(landlordRegistry) {
    console.log('testskflasjfd');
    var factory = getFactory();
    var newLandlord = factory.newResource('org.acme.hdb', 'Landlord', landlord.id);
    newLandlord.id = landlord.id;
    newLandlord.email = landlord.email;
    newLandlord.firstName = landlord.firstName;
    newLandlord.lastName = landlord.lastName;
    newLandlord.DOB = landlord.DOB;
    console.log("############################################################");
    return landlordRegistry.add(newLandlord);
  })
    .catch(function (errir){
    concole.log("registerAsLandlord error: " + error.message);
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
  .catch(function (errir){
    concole.log("approveTenantIdentity error: " + error.message);
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
  .catch(function (errir){
    concole.log("approveLandlordIdentity error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.RegisterHouse} Register House
 * @transaction
 */
function registerHouse(args) {
  console.log("############################################################");
  var landlord = args.landlord
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
  .catch(function (errir){
    concole.log("registerHouse error: " + error.message);
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
      house.house.status = 'Available';
      console.log("approveHouse: before update wordstate");
      console.log("############################################################");
      return houseRegistry.update(house.house);
  })
  .catch(function (errir){
    concole.log("approveHouse error: " + error.message);
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
  .catch(function (errir){
    concole.log("UpdateTenantStatus error: " + error.message);
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
  .catch(function (errir){
    concole.log("UpdateLandlordStatus error: " + error.message);
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
  .catch(function (errir){
    concole.log("UpdateHouseStatus error: " + error.message);
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
  .catch(function (errir){
    concole.log("signTenancyAgreement error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.CreateTenancyAgreement} Create Tenancy Agreement
 * @transaction
 */
function createTenancyAgreement(args) {
  console.log("############################################################");
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

    return getAssetRegistry('org.acme.hdb.Tenant')}) // 
      .then(function(tenantRegistry) {
      tenantList = args.tenants;
      index = 0;
      return getAssetRegistry('org.acme.hdb.TenancySignature')}) //
        .then(function(tenancySignatureRegistry) {
        var signatureList = [];
        while(index < tenantList.length) {
          var tenant = tenantList[index];
          var newSignature = factory.newResource('org.acme.hdb', 'TenancySignature', newAgreement.agreementId + "_" + index);
          newSignature.tenant = tenant;
          newSignature.agreement = newAgreement;
          tenancySignatureRegistry.add(newSignature); 
          index = index + 1;
          signatureList.push(newSignature);
          console.log("add new signature");
          console.log("createTenancyAgreement: " + newSignature.tenant);
          console.log("createTenancyAgreement: " + newSignature.agreement);
        }
        newAgreement.signatureList = signatureList;
      console.log("createTenancyAgreement: before update wordstate");
      console.log("############################################################");
        return tenancyAgreementRegistry.add(newAgreement);
    // ????????????????? ask prof why its add
      })
    .catch(function(error) {
    console.log("error" + error.message);
  })
  .catch(function (errir){
    concole.log("createTenancyAgreement error: " + error.message);
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
        console.log('tenant: ' + tenant.id);
      }
      
      return getAssetRegistry('org.acme.hdb.House')})
      .then(function(houseRegistry) {
        var house = args.agreement.house;
        house.tenants = tenantList;
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
  .catch(function (errir){
    concole.log("updateTenancyAgreement error: " + error.message);
  });
}

/**
 * @param {org.acme.hdb.PayStampDuty} Pay StampDuty
 * @transaction
 */
function payStampDuty(args) {
  console.log("############################################################");
  var factory = getFactory();
  var certificate;

  return getAssetRegistry('org.acme.hdb.StampCertificate')
    .then(function(stampCertificateRegistry) {
    certificate = factory.newResource('org.acme.hdb', 'StampCertificate', args.certificateId);
    certificate.amount = args.amount;
    certificate.agreement = args.agreement;
    certificate.dateCreated = new Date();
    console.log(certificate.certificateId);
    stampCertificateRegistry.add(certificate);
  
    return getAssetRegistry('org.acme.hdb.TenancyAgreement')})
    .then(function(agreementRegistry) {
      var agreement = args.agreement;
      agreement.isStampDutyPaid = true;
      agreement.stampCertificate = certificate;
      console.log(agreement.agreementId);
      console.log("payStampDuty: before update wordstate");
      console.log("############################################################");
      return agreementRegistry.update(agreement);
    })
  .catch(function (errir){
    concole.log("payStampDuty error: " + error.message);
  });
}
















/**
 * @param {org.acme.hdb.RenewTenancyAgreement} Renew Tenancy Agreement
 * @transaction
 */
function renewTenancyAgreement() {

}

/**
 * @param {org.acme.hdb.PayDeposit} Pay Deposit
 * @transaction
 */
function payDeposit() {

}


/**
 * @param {org.acme.hdb.GetAllPendingTenants} get all pending tenants (for ICA to use)
 * @transaction
 */
/*
function getAllPendingTenants() {
  return getAssetRegistry('org.acme.hdb.Tenant')
  .then(function(tenantRegistry) {
    var allTenants = tenantRegistry.getAll();
    console.log(allTenants);
    var allTenants1 = tenantRegistry.resolveAll();
    console.log(allTenants1);
    console.log('get all');
    console.log(allTenants[Object.keys(allTenants)[0]]);
    
    console.log(typeof allTenants);
    for (var i = 0; i < allTenants.length; i++) {
      //console.log(allTenants[i].id);
      console.log('kakaka');
    }
    console.log('end');
    return allTenants;
  }); 
}
*/