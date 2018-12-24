package com.monitor.humiture.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.alibaba.fastjson.JSON;
import com.monitor.humiture.annotation.Login;
import com.monitor.humiture.annotation.LoginUser;
import com.monitor.humiture.dto.LoginDTO;
import com.monitor.humiture.dto.TokenDTO;
import com.monitor.humiture.entity.User;
import com.monitor.humiture.entity.UserDevice;
import com.monitor.humiture.interceptor.AuthorizationInterceptor;
import com.monitor.humiture.service.UserDeviceService;
import com.monitor.humiture.service.UserService;
import com.monitor.humiture.utils.JwtUtils;
import com.monitor.humiture.vo.ResultVO;
import jdk.nashorn.internal.parser.Token;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/wx/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private JwtUtils jwtUtils;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVO<String> login(@RequestBody LoginDTO loginDTO){

        log.info("\n小程序用户登录，参数信息为:\n{}",loginDTO);
        WxMaJscode2SessionResult session = null;
        try {

            session = wxMaService.getUserService().getSessionInfo(loginDTO.getCode());
            log.info("session_key为{}",session.getSessionKey());
            log.info("openid为{}",session.getOpenid());

            if (!wxMaService.getUserService().checkUserInfo(session.getSessionKey(), loginDTO.getRawData(), loginDTO.getSignature())) {
                return ResultVO.ResultBuilder.getError("user check failed");
            }

            User temp = null;
            //首次登录，则注册
            if( (temp = userService.getUserByOpenId(session.getOpenid())) ==null){
                User user = JSON.parseObject(loginDTO.getRawData(), User.class);
                user.setOpenId(session.getOpenid());
                user.setGender("0".equals(user.getGender())?"男":"女");
                userService.save(user);
                temp = userService.getUserByOpenId(session.getOpenid());
            }

            Integer userId = temp.getId();
            String token = jwtUtils.generateToken(userId);

            //生成token，用于自定义登录态
            TokenDTO data = new TokenDTO(token,jwtUtils.getExpire());
            log.info("\n生成的token为：{}",data);

            return ResultVO.ResultBuilder.getOK(data);


        } catch (WxErrorException e) {
            e.printStackTrace();
        }

        return ResultVO.ResultBuilder.getError(null);
    }


    /**
     * 修改设备名
     * @param deviceId
     * @return
     */
    @Login
    @RequestMapping("/device/updateName")
    public ResultVO<String> updateDeViceName(@LoginUser User user, String name,String deviceId){

        log.info("\n用户:{}将修改id为{}的设备名为{}",user.getNickName(),deviceId,name);
        try{
            userDeviceService.updateDeviceName(name,deviceId);
            return ResultVO.ResultBuilder.getOK();
        }catch (Exception e){
            return ResultVO.ResultBuilder.getError("更新名字失败");
        }

    }




}
