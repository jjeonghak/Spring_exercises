package spring.typeconverter.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import spring.typeconverter.type.IpPort;

public class ConversionServiceTest {

    @Test
    void conversionService() {
        DefaultConversionService cs = new DefaultConversionService();
        cs.addConverter(new StringToIntegerConverter());
        cs.addConverter(new IntegerToStringConverter());
        cs.addConverter(new StringToIpPortConverter());
        cs.addConverter(new IpPortToStringConverter());

        Assertions.assertThat(cs.convert("10", Integer.class)).isEqualTo(10);
        Assertions.assertThat(cs.convert(10, String.class)).isEqualTo("10");
        Assertions.assertThat(cs.convert("127.0.0.1:8080", IpPort.class)).isEqualTo(new IpPort("127.0.0.1", 8080));
        Assertions.assertThat(cs.convert(new IpPort("127.0.0.1", 8080), String.class)).isEqualTo("127.0.0.1:8080");
    }
}
