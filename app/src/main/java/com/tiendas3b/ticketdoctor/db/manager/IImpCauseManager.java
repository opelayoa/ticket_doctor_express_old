package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.ImpCause;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IImpCauseManager extends IDatabaseManager {

    ImpCause insertImpCause(ImpCause impCause);

    void insertOrReplaceInTxImpCause(ImpCause... impCauses);

    ArrayList<ImpCause> listCauses();
}
