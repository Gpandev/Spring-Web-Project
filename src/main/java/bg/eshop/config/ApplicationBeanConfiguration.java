package bg.eshop.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeanConfiguration {

    static ModelMapper modelMapper;

    static  {
        modelMapper = new ModelMapper();
    }
}
