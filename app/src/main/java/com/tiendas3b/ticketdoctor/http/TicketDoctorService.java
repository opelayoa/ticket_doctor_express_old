package com.tiendas3b.ticketdoctor.http;

import com.tiendas3b.ticketdoctor.db.dao.Access;
import com.tiendas3b.ticketdoctor.db.dao.Action;
import com.tiendas3b.ticketdoctor.db.dao.Branch;
import com.tiendas3b.ticketdoctor.db.dao.BranchType;
import com.tiendas3b.ticketdoctor.db.dao.Category;
import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;
import com.tiendas3b.ticketdoctor.db.dao.ImpCause;
import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;
import com.tiendas3b.ticketdoctor.db.dao.Profile;
import com.tiendas3b.ticketdoctor.db.dao.ProjectStatus;
import com.tiendas3b.ticketdoctor.db.dao.Provider;
import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;
import com.tiendas3b.ticketdoctor.db.dao.Symptom;
import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;
import com.tiendas3b.ticketdoctor.db.dao.Ticket;
import com.tiendas3b.ticketdoctor.db.dao.TicketStatus;
import com.tiendas3b.ticketdoctor.db.dao.Type;
import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;
import com.tiendas3b.ticketdoctor.db.dao.User;
import com.tiendas3b.ticketdoctor.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by dfa on 23/02/2016.
 */
public interface TicketDoctorService {

    @POST("user/login")
    Call<UserDTO> login();

    @FormUrlEncoded
    @POST("ticket/tickets")
    Call<List<Ticket>> getTickets(@Field("o") int offset);

    @FormUrlEncoded
    @POST("ticket/tickets/closed")
    Call<List<Ticket>> getClosedTickets(@Field("o") int offset);

    @POST("ticket/insert")
    Call<GeneralResponse> insertTicket(@Body Ticket newTicket);

    @FormUrlEncoded
    @POST("ticket/actions")
    Call<List<Action>> getActions(@Field("t") Long ticketId);

    @POST("sync/users")
    Call<List<User>> getUsersCatalog();

    @POST("sync/categories")
    Call<List<Category>> getCategoriesCatalog();

    @POST("sync/types")
    Call<List<Type>> getTypesCatalog();

    @POST("sync/projectStatus")
    Call<List<ProjectStatus>> getProjectStatusCatalog();

    @POST("sync/symptoms")
    Call<List<Symptom>> getSymptomCatalog();

    @POST("sync/diagnostics")
    Call<List<Diagnostic>> getDiagnosticCatalog();

    @POST("sync/typeSymptoms")
    Call<List<TypeSymptom>> getTypeSymptomCatalog();

    @POST("sync/symptomDiagnostics")
    Call<List<SymptomDiagnostic>> getSymptomDiagnosticCatalog();

    @POST("sync/impCauses")
    Call<List<ImpCause>> getImpCauseCatalog();

    @POST("sync/branchTypes")
    Call<List<BranchType>> getBranchTypeCatalog();

    @POST("sync/branches")
    Call<List<Branch>> getBranchCatalog();

    @POST("sync/standardSolutions")
    Call<List<StandardSolution>> getStandardSolutionCatalog();

    @POST("sync/possibleOrigins")
    Call<List<PossibleOrigin>> getPossibleOriginCatalog();

    @POST("sync/ticketStatus")
    Call<List<TicketStatus>> getTicketStatusCatalog();

    @POST("sync/providers")
    Call<List<Provider>> getProviderCatalog();

    @POST("sync/profiles")
    Call<List<Profile>> getProfileCatalog();

    @POST("sync/accesses")
    Call<List<Access>> getAccessCatalog();







//    Call<User> getTickets(@Body String name, @Body String email);






}
