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

import com.tiendas3b.ticketdoctor.db.dao.Branch;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BRANCH".
*/
public class BranchDao extends AbstractDao<Branch, Long> {

    public static final String TABLENAME = "BRANCH";

    /**
     * Properties of entity Branch.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Number3b = new Property(1, String.class, "number3b", false, "NUMBER3B");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Street = new Property(3, String.class, "street", false, "STREET");
        public final static Property Number = new Property(4, String.class, "number", false, "NUMBER");
        public final static Property Neighborhood = new Property(5, String.class, "neighborhood", false, "NEIGHBORHOOD");
        public final static Property Township = new Property(6, String.class, "township", false, "TOWNSHIP");
        public final static Property City = new Property(7, String.class, "city", false, "CITY");
        public final static Property PostalCode = new Property(8, String.class, "postalCode", false, "POSTAL_CODE");
        public final static Property Phone = new Property(9, String.class, "phone", false, "PHONE");
        public final static Property Cellphone = new Property(10, String.class, "cellphone", false, "CELLPHONE");
        public final static Property StorehouseId = new Property(11, Long.class, "storehouseId", false, "STOREHOUSE_ID");
        public final static Property DistrictId = new Property(12, Long.class, "districtId", false, "DISTRICT_ID");
        public final static Property TechnicianId = new Property(13, long.class, "technicianId", false, "TECHNICIAN_ID");
        public final static Property Status = new Property(14, Integer.class, "status", false, "STATUS");
        public final static Property AttendantId = new Property(15, Long.class, "attendantId", false, "ATTENDANT_ID");
        public final static Property PurchaseDelivery = new Property(16, Boolean.class, "purchaseDelivery", false, "PURCHASE_DELIVERY");
        public final static Property Code = new Property(17, String.class, "code", false, "CODE");
        public final static Property Emails = new Property(18, String.class, "emails", false, "EMAILS");
        public final static Property BranchTypeId = new Property(19, long.class, "branchTypeId", false, "BRANCH_TYPE_ID");
    };

    private DaoSession daoSession;


    public BranchDao(DaoConfig config) {
        super(config);
    }
    
    public BranchDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BRANCH\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"NUMBER3B\" TEXT NOT NULL ," + // 1: number3b
                "\"NAME\" TEXT NOT NULL ," + // 2: name
                "\"STREET\" TEXT," + // 3: street
                "\"NUMBER\" TEXT," + // 4: number
                "\"NEIGHBORHOOD\" TEXT," + // 5: neighborhood
                "\"TOWNSHIP\" TEXT," + // 6: township
                "\"CITY\" TEXT NOT NULL ," + // 7: city
                "\"POSTAL_CODE\" TEXT," + // 8: postalCode
                "\"PHONE\" TEXT," + // 9: phone
                "\"CELLPHONE\" TEXT," + // 10: cellphone
                "\"STOREHOUSE_ID\" INTEGER," + // 11: storehouseId
                "\"DISTRICT_ID\" INTEGER," + // 12: districtId
                "\"TECHNICIAN_ID\" INTEGER NOT NULL ," + // 13: technicianId
                "\"STATUS\" INTEGER," + // 14: status
                "\"ATTENDANT_ID\" INTEGER," + // 15: attendantId
                "\"PURCHASE_DELIVERY\" INTEGER," + // 16: purchaseDelivery
                "\"CODE\" TEXT," + // 17: code
                "\"EMAILS\" TEXT," + // 18: emails
                "\"BRANCH_TYPE_ID\" INTEGER NOT NULL );"); // 19: branchTypeId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BRANCH\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Branch entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getNumber3b());
        stmt.bindString(3, entity.getName());
 
        String street = entity.getStreet();
        if (street != null) {
            stmt.bindString(4, street);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(5, number);
        }
 
        String neighborhood = entity.getNeighborhood();
        if (neighborhood != null) {
            stmt.bindString(6, neighborhood);
        }
 
        String township = entity.getTownship();
        if (township != null) {
            stmt.bindString(7, township);
        }
        stmt.bindString(8, entity.getCity());
 
        String postalCode = entity.getPostalCode();
        if (postalCode != null) {
            stmt.bindString(9, postalCode);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(10, phone);
        }
 
        String cellphone = entity.getCellphone();
        if (cellphone != null) {
            stmt.bindString(11, cellphone);
        }
 
        Long storehouseId = entity.getStorehouseId();
        if (storehouseId != null) {
            stmt.bindLong(12, storehouseId);
        }
 
        Long districtId = entity.getDistrictId();
        if (districtId != null) {
            stmt.bindLong(13, districtId);
        }
        stmt.bindLong(14, entity.getTechnicianId());
 
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(15, status);
        }
 
        Long attendantId = entity.getAttendantId();
        if (attendantId != null) {
            stmt.bindLong(16, attendantId);
        }
 
        Boolean purchaseDelivery = entity.getPurchaseDelivery();
        if (purchaseDelivery != null) {
            stmt.bindLong(17, purchaseDelivery ? 1L: 0L);
        }
 
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(18, code);
        }
 
        String emails = entity.getEmails();
        if (emails != null) {
            stmt.bindString(19, emails);
        }
        stmt.bindLong(20, entity.getBranchTypeId());
    }

    @Override
    protected void attachEntity(Branch entity) {
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
    public Branch readEntity(Cursor cursor, int offset) {
        Branch entity = new Branch( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // number3b
            cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // street
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // number
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // neighborhood
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // township
            cursor.getString(offset + 7), // city
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // postalCode
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // phone
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // cellphone
            cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11), // storehouseId
            cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12), // districtId
            cursor.getLong(offset + 13), // technicianId
            cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14), // status
            cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15), // attendantId
            cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0, // purchaseDelivery
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // code
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // emails
            cursor.getLong(offset + 19) // branchTypeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Branch entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNumber3b(cursor.getString(offset + 1));
        entity.setName(cursor.getString(offset + 2));
        entity.setStreet(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setNumber(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNeighborhood(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTownship(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCity(cursor.getString(offset + 7));
        entity.setPostalCode(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPhone(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setCellphone(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setStorehouseId(cursor.isNull(offset + 11) ? null : cursor.getLong(offset + 11));
        entity.setDistrictId(cursor.isNull(offset + 12) ? null : cursor.getLong(offset + 12));
        entity.setTechnicianId(cursor.getLong(offset + 13));
        entity.setStatus(cursor.isNull(offset + 14) ? null : cursor.getInt(offset + 14));
        entity.setAttendantId(cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15));
        entity.setPurchaseDelivery(cursor.isNull(offset + 16) ? null : cursor.getShort(offset + 16) != 0);
        entity.setCode(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setEmails(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setBranchTypeId(cursor.getLong(offset + 19));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Branch entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Branch entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getBranchTypeDao().getAllColumns());
            builder.append(" FROM BRANCH T");
            builder.append(" LEFT JOIN BRANCH_TYPE T0 ON T.\"BRANCH_TYPE_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected Branch loadCurrentDeep(Cursor cursor, boolean lock) {
        Branch entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        BranchType branchType = loadCurrentOther(daoSession.getBranchTypeDao(), cursor, offset);
         if(branchType != null) {
            entity.setBranchType(branchType);
        }

        return entity;    
    }

    public Branch loadDeep(Long key) {
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
    public List<Branch> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<Branch> list = new ArrayList<Branch>(count);
        
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
    
    protected List<Branch> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<Branch> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
