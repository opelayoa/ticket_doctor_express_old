package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Access;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IAccessManager extends IDatabaseManager {

    int getAccessByProfileId(Long id, String nodeAdmin);

    void insertOrReplaceInTxAccesses(Access... actions);
}
