package com.tiendas3b.ticketdoctor.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dfa on 24/02/2016.
 */
public class TicketDTO implements Serializable{

    private int id;
    @SerializedName("observaciones")
    private String observations;
    //solucionremota
    private short remoteSolution;
    //fechaapertura
    private Date openingDate;
    //fechaactualizacion
    private Date dateUpdate;
    //fechasolucion
    private Date dateSolution;
    //fechacierre
    private Date closingDate;
    //solicitante_id
    private int applicantId;
    //lugar_id
    private int branchId;
    //sintoma_id
    private int symptomId;
    //diagnostico_id
    private Integer diagnosticId;
    //posibleorigen_id
    private Integer possibleOriginId;
    //solucionestandard_id
    private Integer standardSolutionId;
    //estado_id
    private int statusId;
    //tecnico_id
    private Integer technicianId;
    //capturista_id
    private int capturistaId;
    //tipo
    private Integer type;
    //espera_usuario_id
    private Integer userWaiting;//TODO userWaitingID
    //espera_proveedor_id
    private Integer providerWaiting;//TODO userWaitingID
    //imp_motivo_id
    private Integer causeId;
    //imp_solucion_id
    private Integer solutionId;
    //archivo
    private String file;
    //leyenda
    private String caption;
    //dias_abierto
    private Integer openDays;
    //categoria
    private int category;//TODO userWaitingID
    //statusp_id
    private Integer projectStatusId;
    //vencido
    private String lapsed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public short getRemoteSolution() {
        return remoteSolution;
    }

    public void setRemoteSolution(short remoteSolution) {
        this.remoteSolution = remoteSolution;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(Date dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public Date getDateSolution() {
        return dateSolution;
    }

    public void setDateSolution(Date dateSolution) {
        this.dateSolution = dateSolution;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getSymptomId() {
        return symptomId;
    }

    public void setSymptomId(int symptomId) {
        this.symptomId = symptomId;
    }

    public Integer getDiagnosticId() {
        return diagnosticId;
    }

    public void setDiagnosticId(Integer diagnosticId) {
        this.diagnosticId = diagnosticId;
    }

    public Integer getPossibleOriginId() {
        return possibleOriginId;
    }

    public void setPossibleOriginId(Integer possibleOriginId) {
        this.possibleOriginId = possibleOriginId;
    }

    public Integer getStandardSolutionId() {
        return standardSolutionId;
    }

    public void setStandardSolutionId(Integer standardSolutionId) {
        this.standardSolutionId = standardSolutionId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public Integer getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(Integer technicianId) {
        this.technicianId = technicianId;
    }

    public int getCapturistaId() {
        return capturistaId;
    }

    public void setCapturistaId(int capturistaId) {
        this.capturistaId = capturistaId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserWaiting() {
        return userWaiting;
    }

    public void setUserWaiting(Integer userWaiting) {
        this.userWaiting = userWaiting;
    }

    public Integer getProviderWaiting() {
        return providerWaiting;
    }

    public void setProviderWaiting(Integer providerWaiting) {
        this.providerWaiting = providerWaiting;
    }

    public Integer getCauseId() {
        return causeId;
    }

    public void setCauseId(Integer causeId) {
        this.causeId = causeId;
    }

    public Integer getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(Integer solutionId) {
        this.solutionId = solutionId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Integer getOpenDays() {
        return openDays;
    }

    public void setOpenDays(Integer openDays) {
        this.openDays = openDays;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Integer getProjectStatusId() {
        return projectStatusId;
    }

    public void setProjectStatusId(Integer projectStatusId) {
        this.projectStatusId = projectStatusId;
    }

    public String getLapsed() {
        return lapsed;
    }

    public void setLapsed(String lapsed) {
        this.lapsed = lapsed;
    }
}
