package by.nosevich.carrental.controllers.admincontrollers;

import by.nosevich.carrental.entities.Accessory;
import by.nosevich.carrental.service.modelservice.AccessoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminAccessoryController {

    @Autowired
    private AccessoryService accessoryService;

    @GetMapping("/accessories")
    public String getAccessoryList(Model model) {
        List<Accessory> accessories = accessoryService.getAll();
        model.addAttribute("accessories", accessories);
        return "forAdmin/accessoryList";
    }

    @GetMapping("/addAccessory")
    public String addAccessoryPage() {
        return "forAdmin/addAccessory";
    }

    @PostMapping("/addAccessory")
    public String addAccessory(Accessory acc) {
        if(acc != null) accessoryService.save(acc);
        return "redirect:/admin/accessories";
    }

    @GetMapping("/deleteAccessory/{id}")
    public String deleteAccessory(@PathVariable("id") String id) {
        try {
            Accessory acc = accessoryService.getById(Integer.parseInt(id));
            accessoryService.delete(acc);
        } catch(NullPointerException e) {
            return "redirect:/admin/accessories";
        }
        return "redirect:/admin/accessories";
    }

    @GetMapping("/editAccessory/{id}")
    public String editAccessoryForm(@PathVariable("id") String id, Model model) {
        try {
            Accessory acc = accessoryService.getById(Integer.parseInt(id));
            model.addAttribute("acc", acc);
            return "forAdmin/editAccessory";
        } catch(Exception e) {
            return "redirect:/admin/accessories";
        }
    }

    @PostMapping("/editAccessory/{id}")
    public String editAccessory(@PathVariable("id") String id, Accessory editedAcc) {
        try {
            Accessory acc = accessoryService.getById(Integer.parseInt(id));
            acc.setName(editedAcc.getName());
            acc.setPrice(editedAcc.getPrice());
            accessoryService.save(acc);
            return "redirect:/admin/accessories";
        } catch(Exception e) {
            return "redirect:/admin/accessories";
        }
    }
}
