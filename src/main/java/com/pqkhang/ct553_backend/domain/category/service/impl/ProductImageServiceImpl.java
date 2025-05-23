package com.pqkhang.ct553_backend.domain.category.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.category.dto.ProductImageDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import com.pqkhang.ct553_backend.domain.category.entity.ProductImage;
import com.pqkhang.ct553_backend.domain.category.mapper.ProductImageMapper;
import com.pqkhang.ct553_backend.domain.category.repository.ProductImageRepository;
import com.pqkhang.ct553_backend.domain.category.repository.ProductRepository;
import com.pqkhang.ct553_backend.domain.category.service.ProductImageService;
import com.pqkhang.ct553_backend.infrastructure.utils.CloudinaryUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
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
    ProductRepository productRepository;

    private List<ProductImageDTO> convertAndUploadProductImageList(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles) {
        // convert product image files to list of files
        List<File> productImages = productImageFiles.stream().map(file -> {
            try {
                return cloudinaryUtils.convertMultipartFileToFile(file);
            } catch (Exception e) {
                throw new RuntimeException("Error converting file");
            }
        }).toList();

        // upload images to cloudinary and database
        String productName = cloudinaryUtils.sanitizeName(productRepository.findProductByProductId(productImageDTO.getProductId()).getProductName());
        int nextIndex = 1;
        String publicId = "CT553/" + productName + " - Img " + nextIndex;
        for (File image : productImages) {
            try {
                while (productImageRepository.existsByPublicId(publicId)) {
                    nextIndex++;
                    publicId = "CT553/" + productName + " - Img " + nextIndex;
                }

                // upload image to cloudinary
                String productImageUrls = cloudinaryUtils.uploadImage(image, productName + " - Img " + nextIndex);

                // save image to database
                ProductImage productImage = new ProductImage();
                productImage.setProduct(Product.builder().productId(productImageDTO.getProductId()).build());
                productImage.setImageUrl(productImageUrls);
                productImage.setPublicId(publicId);
                productImageRepository.save(productImage);

                nextIndex++;
                publicId = "CT553/" + productName + " - Img " + nextIndex;
            } catch (Exception e) {
                throw new RuntimeException("Error uploading image");
            }
        }

        return productImageRepository.findAllByProduct_ProductId(productImageDTO.getProductId()).stream()
                .map(productImageMapper::toProductImageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductImageDTO> getAllProductImagesByProductId(Integer productId) {
        return productImageRepository.findAllByProduct_ProductId(productId).stream()
                .map(productImageMapper::toProductImageDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductImageDTO> createProductImage(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles) throws ResourceNotFoundException {
        if (productRepository.findProductByProductId(productImageDTO.getProductId()) == null) {
            throw new ResourceNotFoundException("Product not found");
        }
        if (productImageFiles == null || productImageFiles.isEmpty()) {
            throw new ResourceNotFoundException("No image files");
        }

        // delete all old product images in cloudinary and database
        List<ProductImage> oldProductImages = productImageRepository.findAllByProduct_ProductId(productImageDTO.getProductId());
        oldProductImages.forEach(image -> {
            try {
                cloudinaryUtils.deleteImage(image.getImageUrl());
                deleteProductImage(image.getProductImageId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return convertAndUploadProductImageList(productImageDTO, productImageFiles);
    }

    @Override
    public List<ProductImageDTO> updateProductImageByProductId(ProductImageDTO productImageDTO, List<MultipartFile> productImageFiles, List<String> publicIdOfImageFiles) throws ResourceNotFoundException {
        if (productRepository.findProductByProductId(productImageDTO.getProductId()) == null) {
            throw new ResourceNotFoundException("Product not found");
        }

        List<ProductImage> oldProductImages = productImageRepository.findAllByProduct_ProductId(productImageDTO.getProductId());
        if (productImageFiles == null || productImageFiles.isEmpty() || productImageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            for (String publicId : publicIdOfImageFiles) {
                // check if all publicIdOfImageFiles exist in oldProductImages then skip and return old list
                if (oldProductImages.stream().noneMatch(image -> image.getPublicId().equals(publicId))) {
                    return getAllProductImagesByProductId(productImageDTO.getProductId());
                }
            }
        }

        // delete all old product images in cloudinary and database that have no public id in the public id list
        for (ProductImage image : oldProductImages) {
            if (!publicIdOfImageFiles.contains(image.getPublicId())) {
                System.out.println("Need to delete images: " + publicIdOfImageFiles);
                System.out.println("Deleting image with public id: " + image.getPublicId());
                try {
                    cloudinaryUtils.deleteImage(image.getImageUrl());
                    deleteProductImage(image.getProductImageId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (productImageFiles == null || productImageFiles.isEmpty() || productImageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            return getAllProductImagesByProductId(productImageDTO.getProductId());
        }

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

