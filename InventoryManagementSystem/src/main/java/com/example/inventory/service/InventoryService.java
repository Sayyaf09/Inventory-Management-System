package com.example.inventory.service;

import com.example.inventory.exception.ItemNotFoundException;
import com.example.inventory.model.InventoryItem;
import com.example.inventory.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    public InventoryItem addItem(InventoryItem item) {
        return repository.save(item);
    }

    public void removeItem(Long id) {
        if (!repository.existsById(id)) {
            throw new ItemNotFoundException("Item not found with ID: " + id);
        }
        repository.deleteById(id);
    }

    public List<InventoryItem> listItems() {
        return repository.findAll();
    }

    public List<InventoryItem> searchItems(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public String exportToCsv() throws IOException {
        List<InventoryItem> items = listItems();
        String fileName = "inventory.csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("ID,Name,Quantity,Price\n");
            for (InventoryItem item : items) {
                writer.append(item.getId() + "," + item.getName() + "," + item.getQuantity() + "," + item.getPrice() + "\n");
            }
        }
        return fileName;
    }
}