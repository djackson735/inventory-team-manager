package com.example.inventory_team_manager.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventory_team_manager.database.InventoryDao;
import com.example.inventory_team_manager.database.InventoryDbHelper;
import com.example.inventory_team_manager.models.Part;

import java.util.List;

public class InventoryRepository {

    private InventoryDao inventoryDao;
    private LiveData<List<Part>> allParts;

    public InventoryRepository(Application application) {
        InventoryDbHelper db = new InventoryDbHelper(application);
        inventoryDao = new InventoryDao(db);
    }

    public LiveData<List<Part>> getAllParts() {
        return new MutableLiveData<>(inventoryDao.getAllParts());
    }
}
