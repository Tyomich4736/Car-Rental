package by.nosevich.carrental.service.accessory;

import by.nosevich.carrental.dto.AccessoryDto;
import by.nosevich.carrental.service.DaoService;

public interface AccessoryService extends DaoService<AccessoryDto> {
    void deleteByIdIfExist(int id);
    void editIfExist(int id, AccessoryDto accessory);
}
