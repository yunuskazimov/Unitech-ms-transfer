package az.unibank.unitechmstransfer.config;

import az.unibank.unitechmstransfer.mapper.TransferMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public TransferMapper TransferMapper() {
        return TransferMapper.INSTANCE;
    }

}
