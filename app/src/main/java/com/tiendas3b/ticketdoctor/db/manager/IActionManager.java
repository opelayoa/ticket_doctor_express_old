package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Action;

import java.util.List;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface IActionManager extends IDatabaseManager {

    Action insertAction(Action action);

    List<Action> getActionsByTicketId(Long id);

    void insertOrReplaceInTxActions(Action... actions);
}
