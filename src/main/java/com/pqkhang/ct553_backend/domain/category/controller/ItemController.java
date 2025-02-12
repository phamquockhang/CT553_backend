package com.pqkhang.ct553_backend.domain.category.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.category.dto.ItemDTO;
import com.pqkhang.ct553_backend.domain.category.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ApiResponse<Page<ItemDTO>> getItems(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<ItemDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(itemService.getItems(params))
                .message("Lấy tất cả mặt hàng thành công")
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ItemDTO>> getAllItems() {
        return ApiResponse.<List<ItemDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(itemService.getAllItems())
                .message("Lấy tất cả mặt hàng thành công")
                .build();
    }

    @PostMapping
    public ApiResponse<ItemDTO> createItem(@RequestBody ItemDTO itemDTO) throws ResourceNotFoundException {
        return ApiResponse.<ItemDTO>builder()
                .status(HttpStatus.CREATED.value())
                .success(true)
                .payload(itemService.createItem(itemDTO))
                .message("Tạo mặt hàng thành công")
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ItemDTO> updateItem(@PathVariable("id") Integer itemId, @RequestBody ItemDTO itemDTO) throws ResourceNotFoundException {
        return ApiResponse.<ItemDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(itemService.updateItem(itemId, itemDTO))
                .message("Cập nhật mặt hàng thành công")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteItem(@PathVariable("id") Integer itemId) throws ResourceNotFoundException {
        itemService.deleteItem(itemId);
        return ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Xóa mặt hàng thành công")
                .build();
    }

}
