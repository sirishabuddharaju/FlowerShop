package com.example.demo.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Flower;
import com.example.demo.repository.FlowerRepository;
import com.example.demo.service.FlowerService;

@Service
public class FlowerServiceImpl implements FlowerService {

    @Autowired
    private FlowerRepository flowerRepository;

    @Override
    public List<Flower> findAll() {
        return flowerRepository.findAll();
    }

    @Override
    public Optional<Flower> findById(Long id) {
        return flowerRepository.findById(id);
    }

    @Override
    public Flower saveFlower(Flower flower) {
        return flowerRepository.save(flower);
    }

    @Override
    public Flower updateFlower(Long id, Flower flowerDetails) {
        Flower existing = flowerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flower not found with id: " + id));

        if (flowerDetails.getName() != null && !flowerDetails.getName().isEmpty())
            existing.setName(flowerDetails.getName());
        if (flowerDetails.getColor() != null && !flowerDetails.getColor().isEmpty())
            existing.setColor(flowerDetails.getColor());
        if (flowerDetails.getDescription() != null && !flowerDetails.getDescription().isEmpty())
            existing.setDescription(flowerDetails.getDescription());
        if (flowerDetails.getPrice() > 0)
            existing.setPrice(flowerDetails.getPrice());
        if (flowerDetails.getStock() > 0)
            existing.setStock(flowerDetails.getStock());
        if (flowerDetails.getImage() != null)
            existing.setImage(flowerDetails.getImage());

        return flowerRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        flowerRepository.deleteById(id);
    }

    @Override
    public List<Flower> searchByName(String q) {
        return flowerRepository.findByNameContainingIgnoreCase(q);
    }
}
