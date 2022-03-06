package by.nosevich.carrental.controllers.admincontrollers;

import by.nosevich.carrental.dto.AccessoryDto;
import by.nosevich.carrental.service.accessory.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminAccessoryController {

    public static final String ACCESSORIES_PAGE = "admin/accessoryList";
    public static final String REDIRECT_ON_ACCESSORIES_PAGE = "redirect:/admin/accessories";
    public static final String ADD_ACCESSORY_PAGE = "admin/addAccessory";
    public static final String EDIT_ACCESSORY_PAGE = "admin/editAccessory";
    public static final String ACCESSORY_ATTRIBUTE = "accessory";
    public static final String ACCESSORIES_ATTRIBUTE = "accessories";

    private final AccessoryService accessoryService;

    @Autowired
    public AdminAccessoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

    @GetMapping("/accessories")
    public String getAccessoryList(Model model) {
        List<AccessoryDto> accessories = accessoryService.getAll();
        model.addAttribute(ACCESSORIES_ATTRIBUTE, accessories);
        return ACCESSORIES_PAGE;
    }

    @GetMapping("/addAccessory")
    public String addAccessoryPage() {
        return ADD_ACCESSORY_PAGE;
    }

    @PostMapping("/addAccessory")
    public String addAccessory(AccessoryDto accessory) {
        if (accessory != null) {
            accessoryService.save(accessory);
        }
        return REDIRECT_ON_ACCESSORIES_PAGE;
    }

    @GetMapping("/deleteAccessory/{id}")
    public String deleteAccessory(@PathVariable("id") Integer id) {
        accessoryService.deleteByIdIfExist(id);
        return REDIRECT_ON_ACCESSORIES_PAGE;
    }

    @GetMapping("/editAccessory/{id}")
    public String getEditAccessoryForm(@PathVariable("id") Integer id, Model model) {
        Optional<AccessoryDto> optionalAccessory = accessoryService.getById(id);
        if (optionalAccessory.isPresent()) {
            model.addAttribute(ACCESSORY_ATTRIBUTE, optionalAccessory.get());
            return EDIT_ACCESSORY_PAGE;
        }
        return REDIRECT_ON_ACCESSORIES_PAGE;
    }

    @PostMapping("/editAccessory/{id}")
    public String editAccessory(@PathVariable("id") Integer id, AccessoryDto editedAccessory) {
        accessoryService.editIfExist(id, editedAccessory);
        return REDIRECT_ON_ACCESSORIES_PAGE;
    }
}
