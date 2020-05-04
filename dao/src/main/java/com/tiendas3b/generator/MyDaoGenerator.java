package com.tiendas3b.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * @author dfa
 */
public class MyDaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";
    private static final String EQUALS_BASE = "EqualsBase";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.tiendas3b.ticketdoctor.db.dao");
        System.out.println(OUT_DIR);
        addTables(schema);
        new DaoGenerator().generateAll(schema, OUT_DIR);
    }

    /**
     * Create tables and the relationships between them
     */
    private static void addTables(Schema schema) {
        /* entities */
        Entity ticket = addTicket(schema);
        Entity ticketStatus = addTicketStatus(schema);
        Entity action = addActions(schema);
        Entity users = addUser(schema);
        Entity category = addCategories(schema);
        Entity type = addType(schema);
        Entity projectStatus = addProjectStatus(schema);
        Entity provider = addProvider(schema);
        Entity symptom = addSymptom(schema);
        Entity diagnostic = addDiagnostic(schema);
        Entity typeSymptom = addTypeSymptom(schema);
        Entity symptomDiagnostic = addSymptomDiagnostic(schema);
        Entity impCause = addImpCause(schema);
        Entity branchType = addBranchType(schema);
        Entity branch = addBranch(schema);
        Entity standardSolution = addStandardSolution(schema);
        Entity possibleOrigin = addPossibleOrigin(schema);
        Entity profile = addProfile(schema);
        Entity access = addAccess(schema);






        Property profileIdForAccessPK = access.addLongProperty("profileId").notNull().getProperty();
        Property actionForAccessPK = access.addStringProperty("action").notNull().getProperty();
        Property nodeForAccessPK = access.addStringProperty("node").notNull().getProperty();
        Index indexUnique = new Index();
        indexUnique.addProperty(profileIdForAccessPK);
        indexUnique.addProperty(actionForAccessPK);
        indexUnique.addProperty(nodeForAccessPK);
        indexUnique.makeUnique();
        access.addIndex(indexUnique);



        Property profileIdForUser = users.addLongProperty("profileId")/*.notNull()*/.getProperty();
        users.addToOne(profile, profileIdForUser, "profile");

        Property providerIdForAction = action.addLongProperty("providerId").getProperty();
        action.addToOne(provider, providerIdForAction, "provider");

        Property technicianIdForAction = action.addLongProperty("technicianId").notNull().getProperty();
        action.addToOne(users, technicianIdForAction, "technician");



        Property applicantIdForTicket = ticket.addLongProperty("applicantId").notNull().getProperty();
        ticket.addToOne(users, applicantIdForTicket, "applicant");    // one-to-one getApplicant()

        Property keyboarderIdForTicket = ticket.addLongProperty("keyboarderId").notNull().getProperty();
        ticket.addToOne(users, keyboarderIdForTicket, "keyboarder");    // one-to-one getKeyboarder()

        Property waitingForUserIdForTicket = ticket.addLongProperty("waitingForUserId").getProperty();
        ticket.addToOne(users, waitingForUserIdForTicket, "waitingForUser");    // one-to-one getWaitingForUserId()

        Property waitingForProviderIdForTicket = ticket.addLongProperty("waitingForProviderId").getProperty();
        ticket.addToOne(provider, waitingForProviderIdForTicket, "waitingForProvider");    // one-to-one getWaitingForTicketId()

        Property technicianIdForTicket = ticket.addLongProperty("technicianId").getProperty();
        ticket.addToOne(users, technicianIdForTicket, "technician");    // one-to-one getTechnician()

        Property categoryIdForTicket = ticket.addLongProperty("categoryId").notNull().getProperty();
        ticket.addToOne(category, categoryIdForTicket, "category");    // one-to-one getCategory()

        Property projectStatusIdForTicket = ticket.addLongProperty("projectStatusId").notNull().getProperty();
        ticket.addToOne(projectStatus, projectStatusIdForTicket, "projectStatus");    // one-to-one getProjectStatus()

        Property typeIdForTicket = ticket.addLongProperty("typeId").getProperty();
        ticket.addToOne(type, typeIdForTicket, "type");    // one-to-one getType()

        Property statusIdForTicket = ticket.addLongProperty("statusId").notNull().getProperty();
        ticket.addToOne(ticketStatus, statusIdForTicket, "status");    // one-to-one getStatus()

        Property ticketIdForAction = action.addLongProperty("ticketId").notNull().getProperty();
        ToMany ticketToAction = ticket.addToMany(action, ticketIdForAction);
        ticketToAction.setName("actions"); // one-to-many (userDetails.getListOfActions)



        Property typeIdForTypeSymptom = typeSymptom.addLongProperty("typeId").notNull().getProperty();
        typeSymptom.addToOne(type, typeIdForTypeSymptom, "type");

        Property symptomIdForTypeSymptom = typeSymptom.addLongProperty("symptomId").notNull().getProperty();
        typeSymptom.addToOne(symptom, symptomIdForTypeSymptom, "symptom");


        Property diagnosticIdForSymptomDiagnostic = symptomDiagnostic.addLongProperty("diagnosticId").notNull().getProperty();
        symptomDiagnostic.addToOne(diagnostic, diagnosticIdForSymptomDiagnostic, "diagnostic");

        Property symptomIdForSymptomDiagnostic = symptomDiagnostic.addLongProperty("symptomId").notNull().getProperty();
        symptomDiagnostic.addToOne(symptom, symptomIdForSymptomDiagnostic, "symptom");



        Property symptomIdForTicket = ticket.addLongProperty("symptomId").notNull().getProperty();
        ticket.addToOne(typeSymptom, symptomIdForTicket, "typeSymptom");

        Property diagnosticIdForTicket = ticket.addLongProperty("diagnosticId").getProperty();
        ticket.addToOne(symptomDiagnostic, diagnosticIdForTicket, "symptomDiagnostic");

        Property causeIdForTicket = ticket.addLongProperty("causeId").getProperty();
        ticket.addToOne(impCause, causeIdForTicket, "cause");



        Property branchTypeIdForBranch = branch.addLongProperty("branchTypeId").notNull().getProperty();
        branch.addToOne(branchType, branchTypeIdForBranch, "branchType");

        Property branchIdForTicket = ticket.addLongProperty("branchId").notNull().getProperty();
        ticket.addToOne(branch, branchIdForTicket, "branch");


        Property categoryIdForStandardSolution = standardSolution.addLongProperty("categoryId").notNull().getProperty();
        standardSolution.addToOne(category, categoryIdForStandardSolution, "category");    // one-to-one getCategory()

        Property standardSolutionIdForTicket = ticket.addLongProperty("standardSolutionId").getProperty();
        ticket.addToOne(standardSolution, standardSolutionIdForTicket, "standardSolution");

        Property possibleOriginIdForTicket = ticket.addLongProperty("possibleOriginId").getProperty();
        ticket.addToOne(possibleOrigin, possibleOriginIdForTicket, "possibleOrigin");
    }

    private static Entity addAccess(Schema schema) {
        Entity entity = schema.addEntity("Access");
        entity.addIdProperty().primaryKey().autoincrement();
//        entity.addStringProperty("node").notNull();
//        entity.addStringProperty("action").notNull();
//        entity.addLongProperty("profileId").notNull();
        return entity;
    }

    private static Entity addProfile(Schema schema) {
        Entity entity = schema.addEntity("Profile");
        entity.addIdProperty().primaryKey().autoincrement();
        entity.addStringProperty("name").notNull();
        entity.addStringProperty("description").notNull();
        entity.addBooleanProperty("hidden");
        return entity;
    }

    private static Entity addPossibleOrigin(Schema schema) {
        Entity standardSolution = schema.addEntity("PossibleOrigin");
        standardSolution.addIdProperty().primaryKey().autoincrement();
        standardSolution.addStringProperty("name").notNull();
        standardSolution.addStringProperty("description");
        standardSolution.setSuperclass(EQUALS_BASE);
        return standardSolution;
    }

    private static Entity addStandardSolution(Schema schema) {
        Entity standardSolution = schema.addEntity("StandardSolution");
        standardSolution.addIdProperty().primaryKey().autoincrement();
        standardSolution.addStringProperty("name").notNull();
        standardSolution.addStringProperty("description").notNull();
        return standardSolution;
    }

    private static Entity addBranch(Schema schema) {
        Entity provider = schema.addEntity("Branch");
        provider.addIdProperty().primaryKey().autoincrement();
        provider.addStringProperty("number3b").notNull();
        provider.addStringProperty("name").notNull();
        provider.addStringProperty("street");
        provider.addStringProperty("number");
        provider.addStringProperty("neighborhood");
        provider.addStringProperty("township");
        provider.addStringProperty("city").notNull();
        provider.addStringProperty("postalCode");
        provider.addStringProperty("phone");
        provider.addStringProperty("cellphone");
//        provider.addLongProperty("branchTypeId").notNull();
        provider.addLongProperty("storehouseId");
        provider.addLongProperty("districtId");
        provider.addLongProperty("technicianId").notNull();
        provider.addIntProperty("status");
        provider.addLongProperty("attendantId");
        provider.addBooleanProperty("purchaseDelivery");
        provider.addStringProperty("code");
        provider.addStringProperty("emails");
        return provider;
    }

    private static Entity addBranchType(Schema schema) {
        Entity branchType = schema.addEntity("BranchType");
        branchType.addIdProperty().primaryKey().autoincrement();
        branchType.addStringProperty("name").notNull();
        branchType.addStringProperty("description").notNull();
        return branchType;
    }

    private static Entity addImpCause(Schema schema) {
        Entity typeSymptom = schema.addEntity("ImpCause");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addStringProperty("description").notNull();
        typeSymptom.addStringProperty("generalDescription").notNull();
        typeSymptom.addStringProperty("impSolution").notNull();
        typeSymptom.addStringProperty("code");
        typeSymptom.addShortProperty("status").notNull();
        return typeSymptom;
    }

    private static Entity addSymptomDiagnostic(Schema schema) {
        Entity typeSymptom = schema.addEntity("SymptomDiagnostic");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addBooleanProperty("sub").notNull();
        typeSymptom.addBooleanProperty("status").notNull();
        typeSymptom.addStringProperty("renew").notNull();
        return typeSymptom;
    }

    private static Entity addTypeSymptom(Schema schema) {
        Entity typeSymptom = schema.addEntity("TypeSymptom");
        typeSymptom.addIdProperty().primaryKey().autoincrement();
        typeSymptom.addStringProperty("renew").notNull();
        typeSymptom.addBooleanProperty("status").notNull();
        return typeSymptom;
    }

    private static Entity addDiagnostic(Schema schema) {
        Entity diagnostic = schema.addEntity("Diagnostic");
        diagnostic.addIdProperty().primaryKey().autoincrement();
        diagnostic.addStringProperty("description").notNull();
        diagnostic.addStringProperty("status").notNull();
        return diagnostic;
    }

    private static Entity addSymptom(Schema schema) {
        Entity symptom = schema.addEntity("Symptom");
        symptom.addIdProperty().primaryKey().autoincrement();
        symptom.addStringProperty("name").notNull();
        symptom.addStringProperty("sub");
        symptom.addStringProperty("description");
        symptom.addShortProperty("failure").notNull();
        symptom.addStringProperty("status").notNull();
        return symptom;
    }

    private static Entity addProvider(Schema schema) {
        Entity provider = schema.addEntity("Provider");
        provider.addIdProperty().primaryKey().autoincrement();
        provider.addStringProperty("businessName").notNull();
        provider.addStringProperty("description");
        provider.addBooleanProperty("rfc").notNull();
        provider.addStringProperty("street");
        provider.addStringProperty("number");
        provider.addStringProperty("neighborhood");
        provider.addStringProperty("township");
        provider.addBooleanProperty("city").notNull();
        provider.addStringProperty("postalCode");
        provider.addStringProperty("phone1");
        provider.addStringProperty("extension1");
        provider.addStringProperty("phone2");
        provider.addStringProperty("extension2");
        provider.addBooleanProperty("cont1");
        provider.addBooleanProperty("phoneCont1");
        provider.addStringProperty("emailCont1");
        provider.addStringProperty("cont2");
        provider.addStringProperty("phoneCont2");
        provider.addStringProperty("emailCont2");
        provider.addBooleanProperty("account").notNull();
        provider.addStringProperty("deliveryDays");
        provider.addStringProperty("payDays");
        provider.addStringProperty("status");
        provider.addStringProperty("key");
        return provider;
    }

    private static Entity addProjectStatus(Schema schema) {
        Entity projectStatus = schema.addEntity("ProjectStatus");
        projectStatus.addIdProperty().primaryKey().autoincrement();
        projectStatus.addStringProperty("name").notNull();
        projectStatus.addStringProperty("description").notNull();
        projectStatus.addBooleanProperty("status").notNull();
        projectStatus.setSuperclass(EQUALS_BASE);
        return projectStatus;
    }

    private static Entity addType(Schema schema) {
        Entity type = schema.addEntity("Type");
        type.addIdProperty().primaryKey().autoincrement();
        type.addStringProperty("name").notNull();//.unique();
        type.addStringProperty("description");
        type.addStringProperty("abbreviation");
        type.addBooleanProperty("status").notNull();
        type.setSuperclass(EQUALS_BASE);
        return type;
    }

    private static Entity addCategories(Schema schema) {
        Entity category = schema.addEntity("Category");
        category.addIdProperty().primaryKey().autoincrement();
        category.addStringProperty("name").notNull();//.unique();
        category.addStringProperty("description");
        category.setSuperclass(EQUALS_BASE);
        return category;
    }

    private static Entity addUser(Schema schema) {
        Entity user = schema.addEntity("User");
        user.addIdProperty().primaryKey().autoincrement();
        user.addStringProperty("name")/*.notNull()*/;
        user.addStringProperty("lastName")/*.notNull()*/;
        user.addStringProperty("login")/*.notNull().unique()*/;
        user.addStringProperty("password")/*.notNull()*/;
        user.addStringProperty("email")/*.notNull()*/;
        user.addStringProperty("phone")/*.notNull()*/;
        user.addStringProperty("alias");
        user.addIntProperty("departmentId")/*.notNull()*/;
        user.addIntProperty("branchId")/*.notNull()*/;
//        ticketStatus.addIntProperty("profileId").notNull();
        user.addBooleanProperty("mailSend")/*.notNull()*/;
        user.addIntProperty("spending");
        user.addIntProperty("userCreate");
        user.addDateProperty("dateCreate");
        user.addIntProperty("userModify");
        user.addDateProperty("dateModify");
        user.addIntProperty("userId")/*.notNull()*/;
        user.addBooleanProperty("status");
        user.addBooleanProperty("mailEvent")/*.notNull()*/;
        return user;
    }

    private static Entity addTicketStatus(Schema schema) {
        Entity ticketStatus = schema.addEntity("TicketStatus");
        ticketStatus.addIdProperty().primaryKey().autoincrement();
        ticketStatus.addStringProperty("name").notNull().unique();
        ticketStatus.addStringProperty("description");
        ticketStatus.addStringProperty("abbreviation");
        ticketStatus.addBooleanProperty("hidden");
        ticketStatus.setSuperclass(EQUALS_BASE);
        return ticketStatus;
    }

    private static Entity addTicket(Schema schema) {
        Entity ticket = schema.addEntity("Ticket");
        ticket.addIdProperty().primaryKey().autoincrement();
        ticket.addStringProperty("observations").notNull();
        ticket.addBooleanProperty("remoteSolution");
        ticket.addDateProperty("openingDate");
        ticket.addDateProperty("updateDate");
        ticket.addDateProperty("solutionDate");
        ticket.addDateProperty("closingDate");
//        ticket.addLongProperty("applicantId").notNull();
//        ticket.addIntProperty("branchId").notNull();
//        ticket.addIntProperty("symptomId").notNull();
//        ticket.addIntProperty("diagnosticId");
//        ticket.addIntProperty("possibleOriginId");
//        ticket.addIntProperty("standardSolutionId");
//        ticket.addIntProperty("statusId").notNull();
//        ticket.addIntProperty("technicianId");
//        ticket.addIntProperty("keyboarderId").notNull();
//        ticket.addIntProperty("type");
//        ticket.addIntProperty("waitingForUserId");
//        ticket.addIntProperty("waitingForProviderId");
//        ticket.addIntProperty("causeId");
        ticket.addIntProperty("solutionId");
        ticket.addStringProperty("file");
        ticket.addStringProperty("caption");
        ticket.addIntProperty("openDays");
//        ticket.addIntProperty("categoryId").notNull();
//        ticket.addIntProperty("projectStatusId");
        ticket.addStringProperty("lapsed");
        return ticket;
    }

    private static Entity addActions(Schema schema) {
        Entity action = schema.addEntity("Action");
        action.addIdProperty().primaryKey().autoincrement();
//        action.addStringProperty("ticketId").notNull();
        action.addDateProperty("date").notNull();
        action.addStringProperty("description").notNull();
//        action.addIntProperty("technicianId").notNull();
//        action.addIntProperty("providerId");
        return action;
    }

}
