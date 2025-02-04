package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.product.Product;
import com.pqkhang.ct553_backend.infrastructure.utils.CloudinaryUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class ProductImageServiceImpl implements ProductImageService {

    ProductImageRepository productImageRepository;
    ProductImageMapper productImageMapper;
    CloudinaryUtils cloudinaryUtils;

    private ProductImageDTO convertAndUploadProductImageList(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles) {
        // convert product image files to list of files
        List<File> productImages = productImageFiles.stream().map(file -> {
            try {
                return cloudinaryUtils.convertMultipartFileToFile(file);
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image");
            }
        }).toList();

        // upload images to cloudinary
        List<String> productImageUrls = productImages.stream().map(cloudinaryUtils::uploadImage).toList();

        // save product images to database
        List<ProductImage> productImageList = productImageUrls.stream().map(url -> {
            ProductImage productImage = new ProductImage();
            productImage.setProduct(Product.builder().productId(productImageDTO.getProductId()).build());
            productImage.setImageUrl(url);
            return productImage;
        }).toList();
        productImageRepository.saveAll(productImageList);
        return productImageDTO;
    }

    @Override
    public List<ProductImageDTO> getAllProductImagesByProductId(Integer productId) {
        return productImageRepository.findAllByProduct_ProductId(productId).stream()
                .map(productImageMapper::toProductImageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageDTO createProductImage(@RequestPart("productImageDTO") ProductImageDTO productImageDTO, @RequestPart("imageUrl") List<MultipartFile> productImageFiles) {
        return convertAndUploadProductImageList(productImageDTO, productImageFiles);
    }

    @Override
    public ProductImageDTO updateAllProductImageByProductId(@RequestPart("productImageDTO") ProductImageDTO productImageDTO, @RequestPart("imageUrl") List<MultipartFile> productImageFiles) throws ResourceNotFoundException {

        // delete all old product images
        List<ProductImage> oldProductImages = productImageRepository.findAllByProduct_ProductId(productImageDTO.getProductId());
        oldProductImages.forEach(image -> {
            try {
                deleteProductImage(image.getProductImageId());
            } catch (ResourceNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        // convert and upload new product images
        return convertAndUploadProductImageList(productImageDTO, productImageFiles);
    }

    @Override
    public void deleteProductImage(Integer productImageId) throws ResourceNotFoundException {
        ProductImage productImage = productImageRepository.findById(productImageId)
                .orElseThrow(() -> new ResourceNotFoundException("Product image not found"));
        productImageRepository.delete(productImage);
    }

}

