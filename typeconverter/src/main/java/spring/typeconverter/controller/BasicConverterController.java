package spring.typeconverter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.typeconverter.type.IpPort;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BasicConverterController {

    @GetMapping("/basic-v1")
    public String basicV1(HttpServletRequest request) {
        String data = request.getParameter("data");
        Integer intData = Integer.valueOf(data);
        System.out.println("intData = " + intData);
        return "ok";
    }

    @GetMapping("/basic-v2")
    public String basicV2(@RequestParam Integer data) {
        System.out.println("data = " + data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        System.out.println("ipPort.getIp() = " + ipPort.getIp());
        System.out.println("ipPort.getPort() = " + ipPort.getPort());
        return "ok";
    }
}
