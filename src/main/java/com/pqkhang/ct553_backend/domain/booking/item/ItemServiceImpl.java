package com.pqkhang.ct553_backend.domain.booking.item;

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
public class ItemServiceImpl implements ItemService {

    StringUtils stringUtils;
    RequestParamUtils requestParamUtils;
    ItemRepository itemRepository;
    ItemMapper itemMapper;

    private Specification<Item> getItemSpec(Map<String, String> params) {
        Specification<Item> spec = Specification.where(null);
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
    public Page<ItemDTO> getItems(Map<String, String> params) throws ResourceNotFoundException {
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
        return Page.<ItemDTO>builder()
                .meta(meta)
                .data(itemPage.getContent().stream()
                        .map(itemMapper::toItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll().stream()
                .map(itemMapper::toItemDTO)
                .toList();
    }

    @Override
    public ItemDTO getItemById(Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Item ID " + id + " is invalid."));
        return itemMapper.toItemDTO(item);
    }

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) throws ResourceNotFoundException {
        if (itemRepository.existsByName((itemDTO.getName()))) {
            throw new ResourceNotFoundException("Tên sản phẩm đã tồn tại");
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

        if (itemDTO.getName().equals(item.getName())) {
            throw new ResourceNotFoundException("Không có thông tin sản phẩm cần cập nhật");
        }

        itemMapper.updateItemFromDTO(itemDTO, item);
        itemRepository.save(item);
        return itemMapper.toItemDTO(item);
    }

    @Override
    public void deleteItem(Integer itemId) throws ResourceNotFoundException {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item ID " + itemId + " is invalid."));
        itemRepository.delete(item);
    }

    @Override
    public boolean existsByName(String name) {
        return itemRepository.existsByName(name);
    }

}

