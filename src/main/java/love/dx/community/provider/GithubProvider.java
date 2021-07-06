package love.dx.community.provider;

import com.alibaba.fastjson.JSON;
import love.dx.community.dto.AccessTokenDTO;
import love.dx.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
@Component 作用：紧紧地把当前的类初始化到 Spring 容器的上下文
    通过 @Component 注解已将 provider 放到了 spring 容器里
 */
@Component
public class GithubProvider {
    /*
    创建getAccessToken()方法，传入AccessTokenDTO 中的参数，
    调用https://github.com/login/oauth/access_token地址，获取accesstoken
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        //使用 OkHttp 文档，做 post 请求
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();//如果取到值会返回body().string()
            //但是怀疑这里的 body().string() 不是我们想得到的那个 accesstoken，它可能是其它的一种方式
            //所以先打印一下将string拿到，拿到的string形式为：access_token=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&scope=user&token_type=bearer
            String string = response.body().string();
            //此行代码与它下面3行代码表达意思相同，为简化格式后的代码
            String token = string.split("&")[0].split("=")[1];
//            String[] split = string.split("&");//通过 & 号拆分
//            String tokenstr = split[0];//拿到左边的第0个，0个就是tokenstring
//            String token = tokenstr.split("=")[1];//将tokenstring 通过 = 号拆分拿到它的第1个就是我们需要的token
            return token;//返回 token
//            System.out.println(string);
//            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //写一个方法通过accesstoken可以获取到用户信息，一个post方法就行
    public GithubUser getUser(String accessToken){
        //构建一个httpClient
        OkHttpClient client = new OkHttpClient();
        //构建一个Request
        Request request = new Request.Builder()
                .url("https://api.github.com/user?")
                .header("Authorization","token "+accessToken)
                .build();
        try {
            //通过 client.newCall(request).execute() 就能拿到 Response
            Response response = client.newCall(request).execute();
            /*
            通过 response.body().string() 就能拿到当前这个string的对象
            但是通过浏览器的请求已经知道它就是一个json的格式，所以直接用Fastjson的包去做解析
             */
            String string = response.body().string();
            //parseObject意思是可以把string直接的自动转换成一个java的类对象
            //这一步意思是把string的json对象自动的转换解析成java的类对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //如果抛异常或者其它情况返回null
        return null;
    }
}
