package bg.eshop.service.impl;

import bg.eshop.service.CloudinaryService;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {

        File file = File.createTempFile("t-file", multipartFile.getOriginalFilename());
        multipartFile.transferTo(file);

        return this.cloudinary.uploader().upload(file, new HashMap()).get("url").toString();
    }
}
