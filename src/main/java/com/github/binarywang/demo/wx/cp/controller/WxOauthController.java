package com.github.binarywang.demo.wx.cp.controller;

import com.github.binarywang.demo.wx.cp.config.WxCpConfiguration;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpOauth2UserInfo;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: WxOauthController
 * @description:
 * @author: longj
 **/
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
        WxCpServiceImpl wxCpService = new WxCpServiceImpl();
        // agentId 填写自建应用得id
        wxCpService.setWxCpConfigStorage(WxCpConfiguration.getCpService(1000002).getWxCpConfigStorage());
        String redirectUrl = wxCpService.getOauth2Service().buildAuthorizationUrl(null);
        log.info("" + redirectUrl);
        //注意这边需要重定向转发（这边重定向得地址就是回调的url的地址）
        return "redirect:" + redirectUrl;
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
