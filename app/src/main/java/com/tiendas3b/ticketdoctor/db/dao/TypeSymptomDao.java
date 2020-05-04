package com.tiendas3b.ticketdoctor.db.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.tiendas3b.ticketdoctor.db.dao.TypeSymptom;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TYPE_SYMPTOM".
*/
public class TypeSymptomDao extends AbstractDao<TypeSymptom, Long> {

    public static final String TABLENAME = "TYPE_SYMPTOM";

    /**
     * Properties of entity TypeSymptom.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Renew = new Property(1, String.class, "renew", false, "RENEW");
        public final static Property Status = new Property(2, boolean.class, "status", false, "STATUS");
        public final static Property TypeId = new Property(3, long.class, "typeId", false, "TYPE_ID");
        public final static Property SymptomId = new Property(4, long.class, "symptomId", false, "SYMPTOM_ID");
    };

    private DaoSession daoSession;


    public TypeSymptomDao(DaoConfig config) {
        super(config);
    }
    
    public TypeSymptomDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TYPE_SYMPTOM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"RENEW\" TEXT NOT NULL ," + // 1: renew
                "\"STATUS\" INTEGER NOT NULL ," + // 2: status
                "\"TYPE_ID\" INTEGER NOT NULL ," + // 3: typeId
                "\"SYMPTOM_ID\" INTEGER NOT NULL );"); // 4: symptomId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TYPE_SYMPTOM\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TypeSymptom entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getRenew());
        stmt.bindLong(3, entity.getStatus() ? 1L: 0L);
        stmt.bindLong(4, entity.getTypeId());
        stmt.bindLong(5, entity.getSymptomId());
    }

    @Override
    protected void attachEntity(TypeSymptom entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TypeSymptom readEntity(Cursor cursor, int offset) {
        TypeSymptom entity = new TypeSymptom( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // renew
            cursor.getShort(offset + 2) != 0, // status
            cursor.getLong(offset + 3), // typeId
            cursor.getLong(offset + 4) // symptomId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TypeSymptom entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRenew(cursor.getString(offset + 1));
        entity.setStatus(cursor.getShort(offset + 2) != 0);
        entity.setTypeId(cursor.getLong(offset + 3));
        entity.setSymptomId(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TypeSymptom entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TypeSymptom entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getTypeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getSymptomDao().getAllColumns());
            builder.append(" FROM TYPE_SYMPTOM T");
            builder.append(" LEFT JOIN TYPE T0 ON T.\"TYPE_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN SYMPTOM T1 ON T.\"SYMPTOM_ID\"=T1.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected TypeSymptom loadCurrentDeep(Cursor cursor, boolean lock) {
        TypeSymptom entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Type type = loadCurrentOther(daoSession.getTypeDao(), cursor, offset);
         if(type != null) {
            entity.setType(type);
        }
        offset += daoSession.getTypeDao().getAllColumns().length;

        Symptom symptom = loadCurrentOther(daoSession.getSymptomDao(), cursor, offset);
         if(symptom != null) {
            entity.setSymptom(symptom);
        }

        return entity;    
    }

    public TypeSymptom loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<TypeSymptom> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<TypeSymptom> list = new ArrayList<TypeSymptom>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<TypeSymptom> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<TypeSymptom> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
