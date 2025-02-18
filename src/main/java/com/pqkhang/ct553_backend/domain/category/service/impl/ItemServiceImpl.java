package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.GeneralizedItemDTO;
import com.pqkhang.ct553_backend.domain.category.dto.ItemDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Item;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.mapper.ItemMapper;
import com.pqkhang.ct553_backend.domain.category.repository.ItemRepository;
import com.pqkhang.ct553_backend.domain.category.repository.ProductRepository;
import com.pqkhang.ct553_backend.domain.category.service.ItemService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
public class ItemServiceImpl implements ItemService {

    StringUtils stringUtils;
    RequestParamUtils requestParamUtils;
    ItemRepository itemRepository;
    ItemMapper itemMapper;
    ProductServiceImpl productService;
    ProductRepository productRepository;

    private ItemDTO customItemDTOMapper(Item item) {
        ItemDTO itemDTO = itemMapper.toItemDTO(item);
        itemDTO.setProducts(item.getProducts().stream()
                .map(productService::customProductDTO)
                .collect(Collectors.toList()));
        return itemDTO;
    }

    private Specification<Item> getItemSpec(Map<String, String> params) {
        Specification<Item> spec = Specification.where(null);
        if (params.containsKey("query") && !params.get("query").isBlank()) {
            String searchValue = params.get("query").trim().toLowerCase();
            String[] searchValues = searchValue.split(",");
            spec = spec.or((root, query, criteriaBuilder) -> {
                Join<Item, Product> productJoin = root.join("products", JoinType.LEFT);
                return criteriaBuilder.or(
                    Arrays.stream(searchValues)
                            .map(stringUtils::normalizeString)
                            .map(value -> "%" + value.trim().toLowerCase() + "%")
                            .map(likePattern -> criteriaBuilder.or(
                                    criteriaBuilder.like(
                                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("itemName"))),
                                            likePattern),
                                    criteriaBuilder.like(
                                            criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(productJoin.get("productName"))),
                                            likePattern)
                            ))
                            .toArray(Predicate[]::new)
                );
            });
        }
        return spec;
    }

    @Override
    public ItemDTO getItem(Integer itemId) throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item ID " + itemId + " is invalid."));
        return customItemDTOMapper(item);
    }

    @Override
    public Page<GeneralizedItemDTO> getItems(Map<String, String> params) throws ResourceNotFoundException {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<Item> spec = getItemSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Item.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<Item> itemPage = itemRepository.findAll(spec, pageable);

        if (itemPage.isEmpty()) {
            throw new ResourceNotFoundException("No item found!");
        }

        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(itemPage.getTotalPages())
                .total(itemPage.getTotalElements())
                .build();
        return Page.<GeneralizedItemDTO>builder()
                .meta(meta)
                .data(itemPage.getContent().stream()
                        .map(itemMapper::toGeneralizedItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<GeneralizedItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toGeneralizedItemDTO)
                .toList();
    }

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) throws ResourceNotFoundException {
        Item oldItem = itemRepository.findByItemName(itemDTO.getItemName());
        if (oldItem != null) {
            return itemMapper.toItemDTO(oldItem);
        } else {
            Item item = itemMapper.toItem(itemDTO);
            itemRepository.save(item);
            return itemMapper.toItemDTO(item);
        }
    }

    @Override
    @Transactional
    public ItemDTO updateItem(Integer id, ItemDTO itemDTO) throws ResourceNotFoundException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item ID " + id + " is invalid."));
//        if (itemRepository.existsByItemName(itemDTO.getItemName()) && !itemDTO.getItemName().equals(item.getItemName())) {
//            throw new ResourceNotFoundException("Tên mặt hàng đã tồn tại");
//        }

//        itemMapper.updateItemFromDTO(itemDTO, item);
//        itemRepository.save(item);
//        return itemMapper.toItemDTO(item);

        Item existingItem = itemRepository.findByItemName(itemDTO.getItemName());

        if (existingItem != null && !existingItem.getItemId().equals(item.getItemId())) {
            // Chuyển các sản phẩm của mặt hàng hiện tại sang mặt hàng đã tồn tại
            item.getProducts().forEach(product -> {
                product.setItem(existingItem);
                productRepository.save(product);
            });
            // Xóa mặt hàng hiện tại
            itemRepository.delete(item);
            return itemMapper.toItemDTO(existingItem);
        } else {
            itemMapper.updateItemFromDTO(itemDTO, item);
            itemRepository.save(item);
            return itemMapper.toItemDTO(item);
        }
    }

    @Override
    public void deleteItem(Integer itemId) throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item ID " + itemId + " is invalid."));
        itemRepository.delete(item);
    }
}

