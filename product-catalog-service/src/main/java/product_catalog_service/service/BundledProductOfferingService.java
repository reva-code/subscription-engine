package product_catalog_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_catalog_service.entity.BundledProductOffering;
import product_catalog_service.entity.ProductOffering;
import product_catalog_service.repository.BundledProductOfferingRepository;
import product_catalog_service.repository.ProductOfferingRepository;

import java.util.List;

@Service
public class BundledProductOfferingService {

    @Autowired
    private BundledProductOfferingRepository repository;

    @Autowired
    private ProductOfferingRepository offeringRepository;

    public BundledProductOffering create(BundledProductOffering bundle) {

        ProductOffering offering = offeringRepository
                .findById(bundle.getProductOffering().getId())
                .orElseThrow();

        bundle.setProductOffering(offering);

        return repository.save(bundle);
    }

    public List<BundledProductOffering> getAll() {
        return repository.findAll();
    }

    public BundledProductOffering getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public BundledProductOffering update(Long id, BundledProductOffering bundle) {

        BundledProductOffering existing = repository.findById(id).orElseThrow();

        existing.setBundleName(bundle.getBundleName());
        existing.setDescription(bundle.getDescription());

        ProductOffering offering = offeringRepository
                .findById(bundle.getProductOffering().getId())
                .orElseThrow();

        existing.setProductOffering(offering);

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}