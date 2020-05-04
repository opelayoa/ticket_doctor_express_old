package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.StandardSolution;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IStandardSolutionManager extends IDatabaseManager {

    StandardSolution insertStandardSolution(StandardSolution standardSolution);

    void insertOrReplaceInTxStandardSolution(StandardSolution... standardSolutions);
}
