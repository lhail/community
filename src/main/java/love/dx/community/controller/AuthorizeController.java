package love.dx.community.controller;

import love.dx.community.dto.AccessTokenDTO;
import love.dx.community.dto.GithubUser;
import love.dx.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    //GithubProvider 封装完毕，在这里调用 Provider方法
    //在 GithubProvider 中通过 @Component 注解已将 provider 放到了 spring 容器里
    //@Autowired 自动把spring容器里面写好的一个实例化的实例加载到当前使用的上下文
    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;

    //写一个方法返回String，因为需要返回到一个页面去
    @GetMapping("callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        //调用GithubProvider中的方法，它括号中的 new AccessTokenDTO() 意思是它需要一个accesstoken对象
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取到accessToken后再调用GithubProvider
        GithubUser user = githubProvider.getUser(accessToken);//传入accessToken的时候就能拿到user
        System.out.println(user.getName());//打印看user是否是需要的(lhail)
        return "index";
    }
}
