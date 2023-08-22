package com.example.inventory_team_manager.viewmodels;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.inventory_team_manager.models.Part;
import com.example.inventory_team_manager.repositories.InventoryRepository;

import java.util.List;

public class InventoryViewModel extends ViewModel {

    private InventoryRepository repository;
    private LiveData<List<Part>> allParts;

    public InventoryViewModel(Application application) {
        repository = new InventoryRepository(application);
        allParts = repository.getAllParts();
    }

    public LiveData<List<Part>> getAllParts() {
        return allParts;
    }
}
