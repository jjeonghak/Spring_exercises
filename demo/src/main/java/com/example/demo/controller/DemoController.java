package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DemoController {

    //어플리케이션에서 get 방식으로 접근할때
    //localhost:8080/demo
    @GetMapping("demo")
    public String demo(Model model) {
        //key-value 값
        //뷰에서 data 자리에 value 값 삽입
        model.addAttribute("data", "demo");
        //resources/templates에서 demo.html 랜더링하게 demo 리턴
        return "demo";
    }

    @GetMapping("demo-mvc")
    public String demoMVC(@RequestParam(value = "name", required = false) String name, Model model) {
        model.addAttribute("name", name);
        return "demo-mvc";
    }

    @GetMapping("demo-string")
    @ResponseBody //html 직접 값 대입, html 소스코드
    public String demoString(@RequestParam("name") String name) {
        return "demo " + name;
    }

    //객체를 반환하면서 ResponsBody 어노테이션 상속시 json 방식으로 반환하는 것이 기본
    @GetMapping("demo-api")
    @ResponseBody
    public Demo demoApi(@RequestParam("name") String name) {
        Demo demo = new Demo();
        demo.setName(name);
        //key-value 구조"의 json
        //{"key":"value"}
        return demo;
    }
    static class Demo {
        private String name;

        //property 접근 방식
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
