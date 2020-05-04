package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.BranchType;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IBranchTypeManager extends IDatabaseManager {

    BranchType insertBranchType(BranchType branchType);

    void insertOrReplaceInTxBranchType(BranchType... branchTypes);
}
