package by.nosevich.carrental.service.accessory;

import by.nosevich.carrental.dto.AccessoryDto;
import by.nosevich.carrental.model.Accessory;
import by.nosevich.carrental.repository.AccessoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JpaAccessoryService implements AccessoryService {

    private final AccessoryRepository accessoryRepository;

    @Autowired
    public JpaAccessoryService(AccessoryRepository accessoryRepository) {
        this.accessoryRepository = accessoryRepository;
    }

    @Override
    public List<AccessoryDto> getAll() {
        return accessoryRepository.findAll().stream().map(AccessoryDto::new).collect(Collectors.toList());
    }

    @Override
    public Optional<AccessoryDto> getById(int id) throws NullPointerException {
        Optional<Accessory> optionalAccessory = accessoryRepository.findById(id);
        return optionalAccessory.map(AccessoryDto::new);
    }

    @Override
    public void delete(AccessoryDto accessoryDto) {
        accessoryRepository.delete(convertDtoToEntity(accessoryDto));
    }

    @Override
    public void save(AccessoryDto accessoryDto) {
        accessoryRepository.save(convertDtoToEntity(accessoryDto));
    }

    @Override
    public void deleteByIdIfExist(int id) {
        Optional<Accessory> optionalAccessory = accessoryRepository.findById(id);
        optionalAccessory.ifPresent(accessoryRepository::delete);
    }

    @Override
    public void editIfExist(int id, AccessoryDto accessoryDto) {
        Optional<Accessory> optionalOriginal = accessoryRepository.findById(id);
        if (optionalOriginal.isPresent()) {
            Accessory original = optionalOriginal.get();
            BeanUtils.copyProperties(accessoryDto, original);
            accessoryRepository.save(original);
        }
    }

    private Accessory convertDtoToEntity(AccessoryDto dto) {
        if (dto == null) {
            return null;
        }
        if (dto.getId() != null) {
            Optional<Accessory> optionalEntity = accessoryRepository.findById(dto.getId());
            if (optionalEntity.isPresent()) {
                Accessory entity = optionalEntity.get();
                BeanUtils.copyProperties(dto, entity);
                return entity;
            }
        }
        Accessory entity = new Accessory();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
