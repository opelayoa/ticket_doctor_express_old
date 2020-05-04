package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.SymptomDiagnostic;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ISymptomDiagnosticManager extends IDatabaseManager {

    SymptomDiagnostic insertSymptomDiagnostic(SymptomDiagnostic symptomDiagnostic);

    void insertOrReplaceInTxSymptomDiagnostic(SymptomDiagnostic... symptomDiagnostics);

    Long getSymptomDiagnostic(long symptomId, long diagnosticId);
}
