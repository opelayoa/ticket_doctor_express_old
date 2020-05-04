package com.tiendas3b.ticketdoctor.db.manager;


import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;

import java.util.ArrayList;

/**
 * Interface that provides methods for managing the database inside the Application.
 *
 * @author dfa
 */
public interface ITypeSymptomManager extends IDatabaseManager {

    TypeSymptom insertTypeSymptom(TypeSymptom typeSymptom);

    void insertOrReplaceInTxTypeSymptom(TypeSymptom... typeSymptoms);

    ArrayList<TypeSymptom> listTypeSymptoms(long typeId);

    long getTypeSymptomId(long typeId, long symptomId);
}
