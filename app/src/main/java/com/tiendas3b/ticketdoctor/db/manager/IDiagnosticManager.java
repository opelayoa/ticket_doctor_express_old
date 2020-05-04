package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Diagnostic;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IDiagnosticManager extends IDatabaseManager {

    Diagnostic insertDiagnostic(Diagnostic diagnostic);

    void insertOrReplaceInTxDiagnostic(Diagnostic... diagnostics);

    ArrayList<Diagnostic> listDiagnostics(long symptomId);
}
