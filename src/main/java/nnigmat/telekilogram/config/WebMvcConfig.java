package nnigmat.telekilogram.config;

import nnigmat.telekilogram.config.converters.StringToRoomTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@ComponentScan(basePackages = {"nnigmat.telekilogram"})
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private StringToRoomTOConverter stringToRoomTOConverter;

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        formatterRegistry.addConverter(stringToRoomTOConverter);
    }
}
