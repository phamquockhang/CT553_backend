package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.product.Product;
import com.pqkhang.ct553_backend.domain.booking.product.ProductRepository;
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
        String productName = productRepository.findProductByProductId(productImageDTO.getProductId()).getProductName();
//        List<String> productImageUrls = new java.util.ArrayList<>(List.of());
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
        if (productImageFiles == null || productImageFiles.isEmpty() || productImageFiles.stream().allMatch(MultipartFile::isEmpty)) {
            throw new ResourceNotFoundException("No image files");
        }
        if (publicIdOfImageFiles == null || publicIdOfImageFiles.isEmpty()) {
            throw new ResourceNotFoundException("No public id of image files");
        }

        // delete all old product images in cloudinary and database that have no public id in the public id list
        List<ProductImage> oldProductImages = productImageRepository.findAllByProduct_ProductId(productImageDTO.getProductId());
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

