package com.simonehleringer.instagramcloneapi.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;

    // TODO: Add validation
    public String uploadImage(String imageBase64UrlEncoded) {
        var params = ObjectUtils.asMap("resource_type", "auto");

        try {
            var result = cloudinary.uploader().upload(imageBase64UrlEncoded, params);

            var publicId = (String) result.get("public_id");

            return publicId;
        } catch (IOException e) {
            throw new CanNotUploadImageException();
        }
    }
}
