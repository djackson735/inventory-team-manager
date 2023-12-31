package com.example.inventory_team_manager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDbHelper extends SQLiteOpenHelper{
    public InventoryDbHelper(Context context) {
        super(context, "inventory.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE Inventory ("
                + "part_id TEXT PRIMARY KEY, "
                + "initial_part_date TEXT, "
                + "part_location TEXT, "
                + "part_quantity INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);

        String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE Transactions ("
                + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "part_id TEXT, "
                + "transaction_date TEXT, "
                + "quantity INTEGER NOT NULL, "
                + "transaction_type TEXT NOT NULL, "
                + "FOREIGN KEY(part_id) REFERENCES Inventory(part_id));";
    }

    //TODO: This upgrade method completely destroys the old db. Change it so it keeps old records and updates the incoming db.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DROP_INVENTORY_TABLE = "DROP TABLE IF EXISTS Inventory";
        db.execSQL(SQL_DROP_INVENTORY_TABLE);
        onCreate(db);
    }
}


