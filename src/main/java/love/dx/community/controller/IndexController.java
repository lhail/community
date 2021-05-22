package love.dx.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 写一个注解 @Controller 这时它会自动识别扫描当前的类，把它作为spring的一个bean去管理
 * 同时也识别它为一个Controller，意思是允许这个类去接收前端的一个请求;
 */
@Controller
public class IndexController {
    @GetMapping("/")    //设置一个根目录
    public String index(){
        return "index";
    }
}
