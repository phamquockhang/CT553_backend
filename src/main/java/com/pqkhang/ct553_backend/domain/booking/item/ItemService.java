package com.pqkhang.ct553_backend.domain.booking.item;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ItemService {
    Page<ItemDTO> getItems(Map<String, String> params) throws ResourceNotFoundException;

    List<ItemDTO> getAllItems();

    ItemDTO getItemById(Integer itemId) throws ResourceNotFoundException;

    ItemDTO createItem(@Valid ItemDTO itemDTO) throws ResourceNotFoundException;

    ItemDTO updateItem(Integer itemId, @Valid ItemDTO itemDTO) throws ResourceNotFoundException;

    void deleteItem(Integer itemId) throws ResourceNotFoundException;

    boolean existsByName(String name);
}
