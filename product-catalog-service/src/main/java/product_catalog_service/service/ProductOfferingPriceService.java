package product_catalog_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_catalog_service.entity.ProductOffering;
import product_catalog_service.entity.ProductOfferingPrice;
import product_catalog_service.repository.ProductOfferingPriceRepository;
import product_catalog_service.repository.ProductOfferingRepository;

import java.util.List;

@Service
public class ProductOfferingPriceService {

    @Autowired
    private ProductOfferingPriceRepository priceRepository;

    @Autowired
    private ProductOfferingRepository offeringRepository;

    // CREATE
    public ProductOfferingPrice create(ProductOfferingPrice price) {

        ProductOffering offering =
                offeringRepository.findById(
                        price.getProductOffering().getId())
                        .orElseThrow();

        price.setProductOffering(offering);

        return priceRepository.save(price);
    }

    // GET ALL
    public List<ProductOfferingPrice> getAll() {
        return priceRepository.findAll();
    }

    // GET BY ID
    public ProductOfferingPrice getById(Long id) {
        return priceRepository.findById(id).orElse(null);
    }

    // UPDATE
    public ProductOfferingPrice update(Long id,
                                       ProductOfferingPrice price) {

        ProductOfferingPrice existing =
                priceRepository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(price.getName());
            existing.setPrice(price.getPrice());
            existing.setCurrency(price.getCurrency());
            existing.setRecurringChargePeriod(
                    price.getRecurringChargePeriod());

            ProductOffering offering =
                    offeringRepository.findById(
                            price.getProductOffering().getId())
                            .orElseThrow();

            existing.setProductOffering(offering);

            return priceRepository.save(existing);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        priceRepository.deleteById(id);
    }
}