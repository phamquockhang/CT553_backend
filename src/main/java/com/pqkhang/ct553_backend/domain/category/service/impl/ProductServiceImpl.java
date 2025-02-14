package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.ProductDTO;
import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.SellingPrice;
import com.pqkhang.ct553_backend.domain.category.mapper.BuyingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.mapper.ProductMapper;
import com.pqkhang.ct553_backend.domain.category.mapper.SellingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.repository.ProductRepository;
import com.pqkhang.ct553_backend.domain.category.service.ProductService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    StringUtils stringUtils;
    RequestParamUtils requestParamUtils;
    ProductRepository productRepository;
    ProductMapper productMapper;
    BuyingPriceMapper buyingPriceMapper;
    SellingPriceMapper sellingPriceMapper;

    public ProductDTO customProductPriceDTO(Product product) {
        ProductDTO productDTO = productMapper.toProductDTO(product);
        productDTO.setBuyingPrice(product.getBuyingPrices().stream()
                .filter(BuyingPrice::getIsCurrent)
                .map(buyingPriceMapper::toBuyingPriceDTO)
                .findFirst()
                .orElse(null));
        productDTO.setSellingPrice(product.getSellingPrices().stream()
                .filter(SellingPrice::getIsCurrent)
                .map(sellingPriceMapper::toSellingPriceDTO)
                .findFirst()
                .orElse(null));
        return productDTO;
    }

    private Specification<Product> getProductSpec(Map<String, String> params) {
        Specification<Product> spec = Specification.where(null);
        if (params.containsKey("query")) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    Arrays.stream(searchValues)
                            .map(stringUtils::normalizeString)
                            .map(value -> "%" + value.trim().toLowerCase() + "%")
                            .map(likePattern -> criteriaBuilder.or(
                                    criteriaBuilder.like(
                                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("name"))),
                                            likePattern)
                            ))
                            .toArray(Predicate[]::new)
            ));
        }
        return spec;
    }

    @Override
    public Page<ProductDTO> getProducts(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Product> spec = getProductSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Product.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Product> productPage = productRepository.findAll(spec, pageable);

        if (productPage.isEmpty()) {
            throw new ResourceNotFoundException("No product found!");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(productPage.getTotalPages())
                .total(productPage.getTotalElements())
                .build();

        return Page.<ProductDTO>builder()
                .meta(meta)
                .data(productPage.getContent().stream().map(this::customProductPriceDTO).toList())
                .build();
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::customProductPriceDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product ID " + id + " is invalid."));
        return productMapper.toProductDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) throws ResourceNotFoundException {
//        if (productRepository.existsByProductName((productDTO.getProductName()))) {
//            throw new ResourceNotFoundException("Tên sản phẩm đã tồn tại");
//        } else {
        Product product = productMapper.toProduct(productDTO);
        productRepository.save(product);
        return productMapper.toProductDTO(product);
//        }
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product ID " + id + " is invalid."));

//        if (productDTO.getProductName().equals(product.getProductName()) && productDTO.getDescription().equals(product.getDescription())) {
//            throw new ResourceNotFoundException("Không có thông tin sản phẩm cần cập nhật");
//        }

        productMapper.updateProductFromDTO(productDTO, product);
        productRepository.save(product);
        return productMapper.toProductDTO(product);
    }

    @Override
    public void deleteProduct(Integer productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product ID " + productId + " is invalid."));
        productRepository.delete(product);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByProductName(name);
    }

}

