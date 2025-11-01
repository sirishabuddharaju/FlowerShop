package com.example.demo.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Flower;
import com.example.demo.service.FlowerService;

@Controller
@RequestMapping("/flowers")
public class FlowerController {

    @Autowired
    private FlowerService flowerService;

    @GetMapping("/home")
    public String homePage(Model model) {
        // Fetch all flowers to display on home
        model.addAttribute("flowers", flowerService.findAll());
        return "home"; // Thymeleaf template: home.html
    }

    // Display all flowers as cards
    @GetMapping("/allFlowers")
    public String viewAllFlowers(Model model) {
        List<Flower> flowers = flowerService.findAll();

        // Convert image bytes to Base64 string for HTML rendering
        List<FlowerWithImage> flowersWithImages = flowers.stream()
                .map(flower -> {
                    String base64Image = null;
                    if (flower.getImage() != null) {
                        base64Image = Base64.getEncoder().encodeToString(flower.getImage());
                    }
                    return new FlowerWithImage(flower, base64Image);
                })
                .collect(Collectors.toList());

        model.addAttribute("flowers", flowersWithImages);
        return "flowers"; // Thymeleaf template: flowers.html
    }

    // Display add flower form
    @GetMapping("/add")
    public String showAddFlowerForm(Model model) {
        model.addAttribute("flower", new Flower());
        return "add-flower"; // Thymeleaf template: add-flower.html
    }

    // Handle form submission to save flower
    @PostMapping("/add")
    public String addFlower(
            @ModelAttribute Flower flower,
            @RequestParam("imageFile") MultipartFile imageFile
    ) throws Exception {

        if (imageFile != null && !imageFile.isEmpty()) {
            flower.setImage(imageFile.getBytes());
        }

        flowerService.saveFlower(flower);

        return "redirect:/flowers"; // Redirect to all flowers page
    }

    @GetMapping("/viewFlowers")
    public String viewAdminAllFlowers(Model model) {
        List<Flower> flowers = flowerService.findAll();

        // Convert image bytes to Base64 string for HTML rendering
        List<FlowerWithImage> flowersWithImages = flowers.stream()
                .map(flower -> {
                    String base64Image = null;
                    if (flower.getImage() != null) {
                        base64Image = Base64.getEncoder().encodeToString(flower.getImage());
                    }
                    return new FlowerWithImage(flower, base64Image);
                })
                .collect(Collectors.toList());

        model.addAttribute("flowers", flowersWithImages);
        return "viewFlowers"; // Thymeleaf template: flowers.html
    }
    // Helper class to send flower + base64 image to Thymeleaf
    public static class FlowerWithImage {
        private Flower flower;
        private String base64Image;

        public FlowerWithImage(Flower flower, String base64Image) {
            this.flower = flower;
            this.base64Image = base64Image;
        }

        public Flower getFlower() { return flower; }
        public String getBase64Image() { return base64Image; }
    }
    
    @GetMapping("/edit/{id}")
    public String editFlowerForm(@PathVariable Long id, Model model) {
        Flower flower = flowerService.findById(id).orElseThrow(() -> new RuntimeException("Flower not found"));
        model.addAttribute("flower", flower);
        return "edit-flower";
    }

    // Update Flower
    @PostMapping("/update/{id}")
    public String updateFlower(@PathVariable Long id,
                               @ModelAttribute Flower flower,
                               @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Flower existing = flowerService.findById(id).orElseThrow(() -> new RuntimeException("Flower not found"));

        // Update only changed fields
        if (flower.getName() != null && !flower.getName().isEmpty()) existing.setName(flower.getName());
        if (flower.getColor() != null && !flower.getColor().isEmpty()) existing.setColor(flower.getColor());
        if (flower.getDescription() != null && !flower.getDescription().isEmpty()) existing.setDescription(flower.getDescription());
        if (flower.getPrice() > 0) existing.setPrice(flower.getPrice());
        if (flower.getStock() > 0) existing.setStock(flower.getStock());
        if (!imageFile.isEmpty()) existing.setImage(imageFile.getBytes());

        try {
            flowerService.updateFlower(id, existing);
            return "redirect:/flowers/viewFlowers?success=Flower updated successfully!";
        } catch (Exception e) {
            return "redirect:/flowers/viewFlowers?error=Failed to update flower!";
        }
    }

    // 🪻 Delete Flower
    @GetMapping("/delete/{id}")
    public String deleteFlower(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            flowerService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Flower deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete flower!");
        }
        return "redirect:/flowers/viewFlowers";
    }

   

       
}



