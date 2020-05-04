package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.PossibleOrigin;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IPossibleOriginManager extends IDatabaseManager {

    PossibleOrigin insertPossibleOrigin(PossibleOrigin possibleOrigin);

    void insertOrReplaceInTxPossibleOrigin(PossibleOrigin... possibleOrigins);

    ArrayList<PossibleOrigin> listPossibleOrigins();

    ArrayList<PossibleOrigin> listPossibleOriginsWithNone(String name);
}
