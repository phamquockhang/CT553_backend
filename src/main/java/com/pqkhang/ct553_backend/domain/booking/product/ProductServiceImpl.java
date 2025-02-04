package com.pqkhang.ct553_backend.domain.booking.product;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    StringUtils stringUtils;
    RequestParamUtils requestParamUtils;
    ProductRepository productRepository;
    ProductMapper productMapper;

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
                .data(productPage.getContent().stream()
                        .map(productMapper::toProductDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductDTO)
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
        if (productRepository.existsByName((productDTO.getName()))) {
            throw new ResourceNotFoundException("Tên sản phẩm đã tồn tại");
        } else {
            Product product = productMapper.toProduct(productDTO);
            productRepository.save(product);
            return productMapper.toProductDTO(product);
        }
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) throws ResourceNotFoundException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product ID " + id + " is invalid."));

        if (productDTO.getName().equals(product.getName()) && productDTO.getDescription().equals(product.getDescription())) {
            throw new ResourceNotFoundException("Không có thông tin sản phẩm cần cập nhật");
        }

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
        return productRepository.existsByName(name);
    }

}

