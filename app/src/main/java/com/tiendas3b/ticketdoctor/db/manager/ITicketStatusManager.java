package com.tiendas3b.ticketdoctor.db.manager;

import com.tiendas3b.ticketdoctor.db.dao.TicketStatus;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ITicketStatusManager extends IDatabaseManager{

    TicketStatus insertTicketStatus(TicketStatus ticketStatus);

    void insertOrReplaceInTxTicketStatus(TicketStatus... ticketStatus);

    ArrayList<TicketStatus> listTicketStatus();

    void updateTicketStatus(TicketStatus ticket);

    void deleteTicketStatus(TicketStatus ticket);

    boolean deleteTicketStatusById(Long ticketId);

    TicketStatus getTicketStatusById(Long ticketId);

    void deleteTicketStatus();

}
