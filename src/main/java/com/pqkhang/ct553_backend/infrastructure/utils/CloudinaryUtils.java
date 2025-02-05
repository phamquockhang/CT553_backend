package com.pqkhang.ct553_backend.infrastructure.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CloudinaryUtils {
    private final Cloudinary cloudinary;

    private String sanitizeFileName(String fileName) {
        // Remove file extension if present
        String nameWithoutExtension = fileName.replaceAll("\\.[^/.]+$", "");

        // Replace special characters and spaces with underscores
        // giu nguyen cum " - " de phan biet cac anh cua cung 1 san pham
//        return nameWithoutExtension.replaceAll("[^a-zA-Z0-9]", "_");
        return nameWithoutExtension.replaceAll("[^a-zA-Z0-9 -]", "_");
    }

    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public String uploadImage(File file, String imageName) {
        String sanitizedFileName = sanitizeFileName(imageName);

        try {
            var uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap(
                    "folder", "/CT553/",
                    "public_id", sanitizedFileName,
                    "unique_filename", false,  // Prevent Cloudinary from adding random suffix
                    "overwrite", true
            ));
//            var uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "/CT553/"));
            return uploadResult.get("url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image");
        }
    }

    public void deleteImage(String imageUrl) {
        try {
            var publicId = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
            String publicIdFormatted = publicId.replaceAll("%20", " ");
            cloudinary.uploader().destroy("CT553/" + publicIdFormatted, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image");
        }
    }
}
