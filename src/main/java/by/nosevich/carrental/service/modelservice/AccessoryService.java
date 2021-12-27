package by.nosevich.carrental.service.modelservice;

import by.nosevich.carrental.entities.Accessory;

public interface AccessoryService extends DAO<Accessory> {
    Accessory getByName(String name);
}
