package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Branch;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IBranchManager extends IDatabaseManager {

    Branch insertBranch(Branch branch);

    void insertOrReplaceInTxBranch(Branch... branches);

    ArrayList<Branch> listBranches();
}
