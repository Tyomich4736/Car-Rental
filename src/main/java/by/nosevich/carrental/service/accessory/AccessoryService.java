package by.nosevich.carrental.service.accessory;

import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.service.DaoService;

public interface AccessoryService extends DaoService<Accessory> {
    Accessory getByName(String name);
}
