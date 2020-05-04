package com.tiendas3b.ticketdoctor.db.manager;

import com.tiendas3b.ticketdoctor.db.dao.Provider;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IProviderManager extends IDatabaseManager {

    Provider insertProvider(Provider provider);

    void insertOrReplaceInTxProvider(Provider... providers);

    ArrayList<Provider> listProviders();
}
