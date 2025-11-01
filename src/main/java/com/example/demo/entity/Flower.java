package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flowers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flower name is required")
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank(message = "Flower color is required")
    @Column(name = "color", nullable = false)
    private String color;

    @NotBlank(message = "Description cannot be empty")
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull(message = "Stock count is required")
    @Positive(message = "Stock must be greater than 0")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Lob
    @NotNull(message = "Image must be uploaded")
    @Column(name = "image", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] image;
}
