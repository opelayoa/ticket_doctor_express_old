package com.tiendas3b.ticketdoctor.db.manager;

import com.tiendas3b.ticketdoctor.db.dao.Ticket;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ITicketManager extends IDatabaseManager{


    /**
     * Insert a ticket into the DB
     *
     * @param ticket to be inserted
     */
    Ticket insertTicket(Ticket ticket);

    void insertOrReplaceInTxTickets(Ticket... tickets);

    /**
     * List all the tickets from the DB
     *
     * @return list of tickets
     */
    ArrayList<Ticket> listTickets();

    /**
     * Update a ticket from the DB
     *
     * @param ticket to be updated
     */
    void updateTicket(Ticket ticket);

    /**
     * Delete all tickets with a certain email from the DB
     *
     * @param ticket to be deleted
     */
    void deleteTicket(Ticket ticket);

    /**
     * Delete a ticket with a certain id from the DB
     *
     * @param ticketId of tickets to be deleted
     */
    boolean deleteTicketById(Long ticketId);

    /**
     * @param ticketId - of the ticket we want to fetch
     * @return Return a ticket by its id
     */
    Ticket getTicketById(Long ticketId);

    /**
     * Delete all the tickets from the DB
     */
    void deleteTickets();

//
//    /**
//     * Insert or update a phoneNumber object into the DB
//     *
//     * @param phoneNumber to be inserted/updated
//     */
//    void insertOrUpdatePhoneNumber(DBPhoneNumber phoneNumber);
//
//    /**
//     * Insert or update a list of phoneNumbers into the DB
//     *
//     * @param phoneNumbers - list of objects
//     */
//    void bulkInsertPhoneNumbers(Set<DBPhoneNumber> phoneNumbers);


}
