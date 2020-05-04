package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.Symptom;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ISymptomManager extends IDatabaseManager {

    Symptom insertSymptom(Symptom symptom);

    void insertOrReplaceInTxSymptom(Symptom... symptoms);

    ArrayList<Symptom> listSymptoms(long typeId);
}
