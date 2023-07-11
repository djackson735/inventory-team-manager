package com.example.inventory_team_manager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventory_team_manager.models.Part;
import com.example.inventory_team_manager.util.ExceptionMessages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InventoryDao {
    public static final String INSERTION = "insertion";
    public static final String ADDITION = "addition";
    public static final String REDUCTION = "reduction";
    public static final String DELETION = "deletion";
    public static final String INVENTORY = "Inventory";
    public static final String TRANSACTIONS = "Transactions";
    public static final String PART_ID = "part_id";
    public static final String INITIAL_PART_DATE = "initial_part_date";
    public static final String PART_LOCATION = "part_location";
    public static final String PART_QUANTITY = "part_quantity";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TRANSACTION_TYPE = "transaction_type";

    private final InventoryDbHelper dbHelper;

    public InventoryDao(InventoryDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertPart(Part part) throws SQLException {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        long newRowId = -1;

        db.beginTransaction();
        try {
            ContentValues inventoryValues = new ContentValues();
            inventoryValues.put(PART_ID, part.getPartId());
            inventoryValues.put(INITIAL_PART_DATE, part.getInitialPartDate());
            inventoryValues.put(PART_LOCATION, part.getPartLocation());
            inventoryValues.put(PART_QUANTITY, part.getPartQuantity());

            newRowId = db.insert(INVENTORY, null, inventoryValues);

            if (newRowId == -1) {
                throw new SQLException(ExceptionMessages.FAILED_TO_INSERT_INVENTORY);
            }
            updateTransactions(db, part, part.getPartQuantity(), INSERTION);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return newRowId;
    }

    public List<Part> getAllParts() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        String[] projection = {PART_ID, INITIAL_PART_DATE, PART_LOCATION, PART_QUANTITY};

        Cursor cursor = db.query(
                INVENTORY,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Part> allParts = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                //TODO: getColumnIndexOrThrow is a last line of defense. Ensure the front end handles column names
                String partId = cursor.getString(
                        cursor.getColumnIndexOrThrow(PART_ID));
                String partDate = cursor.getString(
                        cursor.getColumnIndexOrThrow(INITIAL_PART_DATE));
                String partLocation = cursor.getString(
                        cursor.getColumnIndexOrThrow(PART_LOCATION));
                int quantity = cursor.getInt(
                        cursor.getColumnIndexOrThrow(PART_QUANTITY));

                allParts.add(new Part(partId, partDate, partLocation, quantity));
            }
        } finally {
            cursor.close();
            db.close();
        }
        return allParts;
    }

    public void increaseQuantity(Part part, int additionAmount) throws SQLException {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(PART_QUANTITY, part.getPartQuantity() + additionAmount);

            String selection = "part_id = ?";
            String[] selectionArgs = {part.getPartId()};

            int rowsAffected = db.update(
                    INVENTORY,
                    values,
                    selection,
                    selectionArgs
            );
            if (rowsAffected == 0) {
                throw new SQLException(ExceptionMessages.FAILED_TO_UPDATE_INVENTORY);
            }
            updateTransactions(db, part, additionAmount, ADDITION);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void removeQuantity(Part part, int reductionAmount) throws SQLException {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            // Basic input validation
            if (reductionAmount > part.getPartQuantity()) {
                throw new IllegalArgumentException("Amount to remove exceeds current quantity.");
            } else if (reductionAmount == part.getPartQuantity()) {
                deletePart(part);
                updateTransactions(db, part, reductionAmount, DELETION);
            } else {
                ContentValues values = new ContentValues();
                values.put(PART_QUANTITY, part.getPartQuantity() - reductionAmount);

                String selection = "part_id = ?";
                String[] selectionArgs = {part.getPartId()};

                int rowsAffected = db.update(
                        INVENTORY,
                        values,
                        selection,
                        selectionArgs
                );
                if (rowsAffected == 0) {
                    throw new SQLException(ExceptionMessages.FAILED_TO_UPDATE_INVENTORY);
                }
                updateTransactions(db, part, reductionAmount, REDUCTION);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void deletePart(Part part) throws SQLException {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        db.beginTransaction();
        String selection = PART_ID + " LIKE ?";
        String[] selectionArgs = {part.getPartId()};
        try {
            int rowsDeleted = db.delete(
                    INVENTORY,
                    selection,
                    selectionArgs
            );
            if (rowsDeleted == 0) {
                throw new SQLException(ExceptionMessages.FAILED_TO_DELETE_INVENTORY);
            }
            updateTransactions(db, part, part.getPartQuantity(), DELETION);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Called on successful CRUD operations, uses the existing db instance
    private long updateTransactions(SQLiteDatabase db, Part part, int quantityChange, String transactionType) throws SQLException {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        ContentValues transactionValues = new ContentValues();

        transactionValues.put(PART_ID, part.getPartId());
        transactionValues.put(TRANSACTION_DATE, currentDate);
        transactionValues.put(PART_QUANTITY, quantityChange);
        transactionValues.put(TRANSACTION_TYPE, transactionType);

        long newTransactionId = db.insert(TRANSACTIONS, null, transactionValues);
        if (newTransactionId == -1) {
            throw new SQLException(ExceptionMessages.FAILED_TO_INSERT_TRANSACTIONS);
        }

        return newTransactionId;
    }

}

