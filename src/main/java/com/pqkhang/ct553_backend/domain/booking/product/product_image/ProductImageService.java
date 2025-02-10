package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface ProductImageService {

    List<ProductImageDTO> getAllProductImagesByProductId(Integer productId);

    List<ProductImageDTO> createProductImage(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles) throws ResourceNotFoundException;

    List<ProductImageDTO> updateProductImageByProductId(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles, List<String> publicIdOfImageFiles) throws ResourceNotFoundException;

    void deleteProductImage(Integer productImageId) throws ResourceNotFoundException;
}
