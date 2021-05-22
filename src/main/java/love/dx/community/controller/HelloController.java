package love.dx.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 写一个注解 @Controller 这时它会自动识别扫描当前的类，把它作为spring的一个bean去管理
 * 同时也识别它为一个Controller，意思是允许这个类去接收前端的一个请求;
 */
@Controller
public class HelloController {
    @GetMapping("/hello")
    public String hello(@RequestParam(name = "name") String name, Model model){   //@RequestParam():请求的参数
        model.addAttribute("name",name);//把浏览器传过来的值放到model里面去
        return "hello";//返回一个hello，这时会自动去模板目录(templates)找hello这个模板
    }
}
