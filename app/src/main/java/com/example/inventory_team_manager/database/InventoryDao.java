package com.example.inventory_team_manager.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.inventory_team_manager.models.Part;

import java.text.SimpleDateFormat;
import java.util.Date;
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
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            ContentValues inventoryValues = new ContentValues();
            inventoryValues.put("part_id", part.getPartId());
            inventoryValues.put("date", part.getDate());
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
            transactionsValues.put("transaction_type", "insert");

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
}

