package com.example.catalog.controller;

import com.example.catalog.dto.ProductOfferingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productOfferings")
public class ProductOfferingController {
    @GetMapping("/{id}")
    public ResponseEntity<ProductOfferingResponse> getById(@PathVariable String id) { return ResponseEntity.ok(new ProductOfferingResponse()); }
}