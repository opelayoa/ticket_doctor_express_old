package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Type;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ITypeManager extends IDatabaseManager {

    Type insertType(Type type);

    void insertOrReplaceInTxType(Type... types);

    ArrayList<Type> listTypes();

    ArrayList<Type> listTypesWithNone(String message);
}
