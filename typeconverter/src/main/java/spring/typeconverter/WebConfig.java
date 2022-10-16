package spring.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spring.typeconverter.converter.IntegerToStringConverter;
import spring.typeconverter.converter.IpPortToStringConverter;
import spring.typeconverter.converter.StringToIpPortConverter;
import spring.typeconverter.formatter.MyNumberFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        //우선순위
        //registry.addConverter(new StringToIpPortConverter());
        //registry.addConverter(new IntegerToStringConverter());

        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
    }
}
