package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.ProductDTO;
import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.SellingPrice;
import com.pqkhang.ct553_backend.domain.category.entity.Weight;
import com.pqkhang.ct553_backend.domain.category.mapper.BuyingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.mapper.ProductMapper;
import com.pqkhang.ct553_backend.domain.category.mapper.SellingPriceMapper;
import com.pqkhang.ct553_backend.domain.category.mapper.WeightMapper;
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
import java.util.function.Function;
import java.util.stream.Collectors;

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
    WeightMapper weightMapper;

    // Constants
    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_PAGE_SIZE = "10";

    // Reusable Specifications
    private Specification<Product> isItemActivated() {
        return (root, query, cb) -> cb.equal(root.get("item").get("isActivated"), true);
    }

    private Specification<Product> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec(params)).and(buildActiveSpec(params)).and(buildItemSpec(params));
    }

    private Specification<Product> buildQuerySpec(Map<String, String> params) {
        return params.containsKey("query") ? (root, query, cb) -> {
            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
            Predicate[] predicates = Arrays.stream(searchValues).map(stringUtils::normalizeString).map(value -> "%" + value.trim() + "%").map(pattern -> cb.like(cb.function("unaccent", String.class, cb.lower(root.get("productName"))), pattern)).toArray(Predicate[]::new);
            return cb.or(predicates);
        } : null;
    }

    private Specification<Product> buildActiveSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "isActivated").stream().map(criteria -> (Specification<Product>) (root, query, cb) -> cb.equal(root.get("isActivated"), Boolean.parseBoolean(criteria.getValue().toString()))).reduce(Specification::or).orElse(null);
    }

    private Specification<Product> buildItemSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "itemId").stream().map(criteria -> (Specification<Product>) (root, query, cb) -> cb.equal(root.get("item").get("itemId"), Integer.parseInt(criteria.getValue().toString()))).reduce(Specification::or).orElse(null);
    }

    // Utility methods
    ProductDTO customProductDTO(Product product) {
        ProductDTO dto = productMapper.toProductDTO(product);
        dto.setBuyingPrice(getFirstCurrent(product.getBuyingPrices(), BuyingPrice::getIsCurrent, buyingPriceMapper::toBuyingPriceDTO));
        dto.setSellingPrice(getFirstCurrent(product.getSellingPrices(), SellingPrice::getIsCurrent, sellingPriceMapper::toSellingPriceDTO));
        dto.setWeight(getFirstCurrent(product.getWeights(), Weight::getIsCurrent, weightMapper::toWeightDTO));
        return dto;
    }

    private <T, R> R getFirstCurrent(List<T> items, Function<T, Boolean> isCurrent, Function<T, R> mapper) {
        return items.stream().filter(isCurrent::apply).findFirst().map(mapper).orElse(null);
    }

    private Page<ProductDTO> buildProductPage(org.springframework.data.domain.Page<Product> productPage, Pageable pageable) throws ResourceNotFoundException {
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
                .meta(meta).data(productPage.getContent().stream().map(this::customProductDTO).collect(Collectors.toList()))
                .build();
    }

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Product.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Product findProductById(Integer productId) throws ResourceNotFoundException {
        return productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product ID " + productId + " is invalid."));
    }

    // Service methods
    @Override
    public ProductDTO getProduct(Integer productId) throws ResourceNotFoundException {
        return customProductDTO(findProductById(productId));
    }

    @Override
    public Page<ProductDTO> getProducts(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<Product> spec = buildSearchSpec(params);
        return buildProductPage(productRepository.findAll(spec, pageable), pageable);
    }

    @Override
    public Page<ProductDTO> getProductsPublicApi(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<Product> spec = buildSearchSpec(params).and(isItemActivated());
        return buildProductPage(productRepository.findAll(spec, pageable), pageable);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::customProductDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productMapper.toProduct(productDTO);
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ResourceNotFoundException {
        Product product = findProductById(id);
        productMapper.updateProductFromDTO(productDTO, product);
        product.setUpdatedAt(stringUtils.getCurrentDateTime());
        return productMapper.toProductDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void deleteProduct(Integer productId) throws ResourceNotFoundException {
        productRepository.delete(findProductById(productId));
    }
}