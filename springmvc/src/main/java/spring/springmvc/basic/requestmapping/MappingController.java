package spring.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/basic", method = RequestMethod.GET)
    public String basic() {
        log.info("basic");
        return "ok";
    }

    @GetMapping("/mapping/{data}")
    public String mappingPath(@PathVariable("data") String data) {
        log.info("mappingPath data={}", data);
        return "ok";
    }

    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String param() {
        log.info("mappingParam");
        return "ok";
    }

    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String headers() {
        log.info("mappingHeaders");
        return "ok";
    }

    @PostMapping(value = "/mapping-consumes", consumes = "application/json")
    public String consumes() {
        log.info("mappingConsumes");
        return "ok";
    }
}
