package bg.eshop.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class CloudConfiguration {

    @Value("dbvfrgvj9")
    private String cloudName;

    @Value("244737738683887")
    private String cloudKey;

    @Value("uuN8jJSBEiq2teNMtJQwc1mlDGg")
    private String cloudSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(new HashMap<String, Object>(){{
            put("cloud_name", cloudName);
            put("api_key", cloudKey);
            put("api_secret", cloudSecret);
        }});
    }
}
