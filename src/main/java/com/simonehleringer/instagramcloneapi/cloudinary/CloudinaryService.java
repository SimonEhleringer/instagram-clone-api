package com.simonehleringer.instagramcloneapi.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public String uploadImage(String imageDataUri, ImageType imageType, UUID userId) {
        var params = ObjectUtils.asMap(
                "resource_type", "auto",
                "folder", "instagram-clone/" + imageType.getFolderName() + "/" + userId.toString(),
                "allowed_formats", new String[] { "jpg", "jpeg", "png" }
        );

        try {
            var result = cloudinary.uploader().upload(imageDataUri, params);

            var publicId = (String) result.get("public_id");

            return publicId;
        } catch (IOException e) {
            throw new CanNotUploadImageException();
        }
    }
}
