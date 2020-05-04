package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Category;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ICategoryManager extends IDatabaseManager {

    Category insertCategory(Category category);

    void insertOrReplaceInTxCategory(Category... categories);
}
