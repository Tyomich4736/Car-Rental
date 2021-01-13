package by.nosevich.carrental.model.service.entityservice;

import by.nosevich.carrental.model.entities.Accessory;

public interface AccessoryService extends DAO<Accessory>{
	Accessory getByName(String name);
}
