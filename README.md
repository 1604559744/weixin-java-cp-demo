[TOC]

# 企业微信 网页授权登入demo

### 企业微信网页授权登入

#### 1：工具包

[com.github.binarywang](https://github.com/Wechat-Group/WxJava)

#### 2：企业微信 网页授权登入

####   2.1：操作流程

1. 创建自建应用
2. 设置可信域名
3. 设置应用台主页

####   2.2：resources文件夹中的application.yml配置文件

```yml
logging:
  level:
    org.springframework.web: INFO
    com.github.binarywang.demo.wx.cp: DEBUG
    me.chanjar.weixin: DEBUG
wechat:
  cp:
    corpId: ww2bd1ee4b49axxxxx
    appConfigs:
      - agentId: 1000002
        secret: g7jrp99jdAfRlzaV-49mnhusnN4fzqyy2Bbgfxxxxxx
        token:
        aesKey:
        redirectUri: http://fcdb2z.natappfree.cc/mobile/qyweixin/oauth
```

corpId：企业ID，agentId：应用ID，secret：应用标识，redirectUri：回调地址。如果只是实现网页授权登陆token，aesKey无需填写。

####   2.3：WxOauthController

```java
@Controller
@Slf4j
public class WxOauthController {
    /**
     * 授权入口
     * @return
     * @throws Exception
     */
    @GetMapping("/mobile/qyweixin/init")
    public Object init() throws Exception{
        // agentId 填写自建应用得id
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
  wxCpService.setWxCpConfigStorage(WxCpConfiguration.getCpService(1000002).getWxCpConfigStorage());
       
        //url即回调地址
        String aNull = wxCpService.getOauth2Service().buildAuthorizationUrl(null);
        log.info("" + aNull);
        //注意这边需要重定向转发（这边重定向得地址就是上面的url的地址）
        return "redirect:" + aNull;
    }

    /**
     * 回调地址
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping("/mobile/qyweixin/oauth")
    @ResponseBody
    public Object oauth(String code) throws Exception{
        log.info(code + "code得值");
        final WxCpService wxCpService = WxCpConfiguration.getCpService(1000002);
        // 根据code获取 访问用户身份
        WxCpOauth2UserInfo userInfo = wxCpService.getOauth2Service().getUserInfo(code);
        log.info(userInfo + "用户信息");
        // 根据userId获取 访问用户的详细信息
        WxCpUser user = wxCpService.getUserService().getById(userInfo.getUserId());
        log.info(user + "用户得详细信息");
        return code;
    }
}
```

####   2.4：测试

- 百度搜索natapp,进入官网注册登陆

- 点击我的隧道

- 配置我的隧道映射的本地端口

- [查看NATAPP1分钟快速新手图文教程](https://natapp.cn/article/natapp_newbie)

- 启动 natapp 应用得到随机分配的域名，例如：http://fcdb2z.natappfree.cc ，这个就是对应本地的 locaohost:配置的映射端口

- 在步骤二配置好可新域名跟应用台主页，例如：

  > 应用台主页：http://fcdb2z.natappfree.cc/mobile/qyweixin/init
  >
  > 可信域名：fcdb2z.natappfree.cc

- 进入工作台点击创建的应用，看后台打印信息，即可知道是否登入成功

> 通过拿到的用户的信息去数据库匹配应用系统用户信息，进行后续操作

