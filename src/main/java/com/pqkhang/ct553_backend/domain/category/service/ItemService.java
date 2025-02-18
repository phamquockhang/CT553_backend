package com.pqkhang.ct553_backend.domain.category.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.GeneralizedItemDTO;
import com.pqkhang.ct553_backend.domain.category.dto.ItemDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ItemService {
    ItemDTO getItem(Integer itemId) throws ResourceNotFoundException;

    Page<GeneralizedItemDTO> getItems(Map<String, String> params) throws ResourceNotFoundException;

    List<GeneralizedItemDTO> getAllItems();

    ItemDTO createItem(@Valid ItemDTO itemDTO) throws ResourceNotFoundException;

    ItemDTO updateItem(Integer itemId, @Valid ItemDTO itemDTO) throws ResourceNotFoundException;

    void deleteItem(Integer itemId) throws ResourceNotFoundException;
}
