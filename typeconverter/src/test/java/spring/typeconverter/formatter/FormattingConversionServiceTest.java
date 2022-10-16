package spring.typeconverter.formatter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import spring.typeconverter.converter.IpPortToStringConverter;
import spring.typeconverter.converter.StringToIpPortConverter;
import spring.typeconverter.type.IpPort;

public class FormattingConversionServiceTest {
    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService cs = new DefaultFormattingConversionService();
        cs.addConverter(new StringToIpPortConverter());
        cs.addConverter(new IpPortToStringConverter());
        cs.addFormatter(new MyNumberFormatter());

        IpPort ipPort = cs.convert("127.0.0.1:8080", IpPort.class);
        Assertions.assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        Assertions.assertThat(cs.convert(1000, String.class)).isEqualTo("1,000");
        Assertions.assertThat(cs.convert("1,000", Long.class)).isEqualTo(1000);
    }
}
