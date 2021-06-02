package com.simonehleringer.instagramcloneapi.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryServiceTest {
    @InjectMocks
    private CloudinaryService underTest;

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @BeforeEach
    void setUp() {
        given(cloudinary.uploader()).willAnswer((Answer<Uploader>) invocationOnMock -> uploader);
    }

    @Test
    void uploadImage_givenValidParameters_shouldReturnGeneratedPublicImageId() throws Exception {
        // Arrange
        var imageDataUri = "imageDataUri";
        var imageType = ImageType.POST;
        var userId = UUID.fromString("11111111-1111-1111-1111-111111111111");

        var publicId = "publicId";

        var result = new HashMap<String, String>();
        result.put("public_id", publicId);

        given(uploader.upload(eq(imageDataUri), any())).willReturn(result);

        // Act
        var publicImageId = underTest.uploadImage(imageDataUri, imageType, userId);

        // Assert
        assertThat(publicImageId).isEqualTo(publicId);

        verify(uploader).upload(eq(imageDataUri), any());
    }

    @Test
    void uploadImage_givenInvalidParameters_shouldThrow() throws Exception {
        // Arrange
        given(uploader.upload(anyString(), any())).willThrow(IOException.class);

        // Act
        // Assert
        assertThatThrownBy(() ->
                underTest.uploadImage("imageDataUri", ImageType.POST, UUID.fromString("11111111-1111-1111-1111-111111111111")));
    }
}