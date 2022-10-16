package spring.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import spring.springmvc.basic.DataDto;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        DataDto dataDto = objectMapper.readValue(messageBody, DataDto.class);
        log.info("username={}, age={}", dataDto.getUsername(), dataDto.getAge());
        response.getWriter().write("ok");
    }

    @ResponseBody
    @RequestMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        DataDto dataDto = objectMapper.readValue(messageBody, DataDto.class);
        log.info("username={}, age={}", dataDto.getUsername(), dataDto.getAge());
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody DataDto dataDto) {
        log.info("username={}, age={}", dataDto.getUsername(), dataDto.getAge());
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<DataDto> httpEntity) {
        DataDto dataDto = httpEntity.getBody();
        log.info("username={}, age={}", dataDto.getUsername(), dataDto.getAge());
        return "ok";
    }

    @ResponseBody
    @RequestMapping("/request-body-json-v5")
    public DataDto requestBodyJsonV5(@RequestBody DataDto dataDto) {
        log.info("username={}, age={}", dataDto.getUsername(), dataDto.getAge());
        return dataDto;
    }
}
