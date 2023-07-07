package com.example.inventory_team_manager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventory_team_manager.models.Part;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InventoryDao {
    private final InventoryDbHelper dbHelper;

    public InventoryDao(InventoryDbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public long insertPart(Part part) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        long newRowId = -1;

        db.beginTransaction();
        try{
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            ContentValues inventoryValues = new ContentValues();
            inventoryValues.put("part_id", part.getPartId());
            inventoryValues.put("initial_part_date", part.getInitialPartDate());
            inventoryValues.put("part_location", part.getPartLocation());
            inventoryValues.put("quantity", part.getQuantity());

            newRowId = db.insert("Inventory", null, inventoryValues);

            if (newRowId == -1) {
                throw new SQLException("Failed to insert row into Inventory");
            }

            ContentValues transactionsValues = new ContentValues();
            transactionsValues.put("part_id", part.getPartId());
            transactionsValues.put("transaction_date", currentDate);
            transactionsValues.put("quantity", part.getQuantity());
            transactionsValues.put("transaction_type", "insertion");

            long newTransactionRowId = db.insert("Transactions", null, transactionsValues);

            if (newTransactionRowId == -1) {
                throw new SQLException("Failed to insert row into Transactions");
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO add logging here
        } finally {
            db.endTransaction();
        }
        db.close();
        return newRowId;
    }

    public List<Part> getAllParts() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        String[] projection = {"part_id", "initial_part_date", "part_location", "quantity"};

        Cursor cursor = db.query(
                "Inventory",
                projection,
                null,
                null,
                null,
                null,
                null
        );

        List<Part> allParts = new ArrayList<>();

        while(cursor.moveToNext()) {
            String partId = cursor.getString(
                    cursor.getColumnIndexOrThrow("part_id"));
            String partDate = cursor.getString(
                    cursor.getColumnIndexOrThrow("initial_part_date"));
            String partLocation = cursor.getString(
                    cursor.getColumnIndexOrThrow("part_location"));
            int quantity = cursor.getInt(
                    cursor.getColumnIndexOrThrow("quantity"));

            allParts.add(new Part(partId, partDate, partLocation, quantity));
        }
        cursor.close();
        return allParts;
    }

    public void removeQuantity(Part part, int removalAmount) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int rowsAffected = 0;
        db.beginTransaction();
        try {
            // Basic input validation
            if (removalAmount > part.getQuantity()) {
                throw new IllegalArgumentException("Amount to remove exceeds current quantity.");
            }
            if (removalAmount == part.getQuantity()) {
                deletePart(part);
            }
            else {
                ContentValues values = new ContentValues();
                values.put("quantity", part.getQuantity() - removalAmount);

                String selection = "part_id = ?";
                String[] selectionArgs = { part.getPartId() };

                rowsAffected = db.update(
                        "Inventory",
                        values,
                        selection,
                        selectionArgs
                );

                if (rowsAffected == 0) {
                    throw new SQLException("Failed to update row in Inventory.");
                }

                // Getting current date
                String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                // Insert new row to Transactions
                ContentValues transactionValues = new ContentValues();
                transactionValues.put("part_id", part.getPartId());
                transactionValues.put("transaction_date", currentDate);
                transactionValues.put("quantity", removalAmount);
                transactionValues.put("transaction_type", "removal");

                long newTransactionId = db.insert("Transactions", null, transactionValues);

                if (newTransactionId == 0) {
                    throw new SQLException("Failed to insert row in Transactions");
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            //TODO Add logging to removal method
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public void deletePart(Part part) {
        //TODO Finish this method
    }
}

