package com.video.vip.controller.login;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.video.vip.basics.service.RedisService;
import com.video.vip.util.login.CommonCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 验证码-图片验证码
 * @author wxn
 */
@CrossOrigin()
@RestController
@RequestMapping(value="/img/vcode/")
@Slf4j
@Api(value = "ImgVcodeController", tags = "图片验证码控制类")
public class ImgVcodeController {
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private RedisService redisService;

    @ApiOperation(value="生成图片验证码", notes="根据onlyKey生成图片验证码")
    @ApiImplicitParams({@ApiImplicitParam(name = "onlyKey", value = "唯一key", required = true, dataType = "string")})
    @RequestMapping(value = "/get/{onlyKey:.+}", method= RequestMethod.GET)
    public void send(@NonNull @PathVariable String onlyKey) {
        log.info("生成图片验证码:onlyKey:{}", onlyKey);
        byte[] captchaChallengeAsJpeg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到redis中
            String createText = defaultKaptcha.createText();
            log.info("生成图片验证码结果：{}",createText);
            boolean bool = redisService.set(CommonCodeUtils.REDIS_IMG_VCODE_KEY+onlyKey,createText,CommonCodeUtils.REDIS_IMG_VCODE_KEY_OVERTIME);
            if(bool){
                log.debug("redis保存图片验证码成功");
                //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
                BufferedImage challenge = defaultKaptcha.createImage(createText);
                ImageIO.write(challenge, "jpg", jpegOutputStream);

                //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
                captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
                httpServletResponse.setHeader("Cache-Control", "no-store");
                httpServletResponse.setHeader("Pragma", "no-cache");
                httpServletResponse.setDateHeader("Expires", 0);
                httpServletResponse.setContentType("image/jpeg");
                ServletOutputStream responseOutputStream = null;
                try{
                    responseOutputStream = httpServletResponse.getOutputStream();
                    responseOutputStream.write(captchaChallengeAsJpeg);
                }catch (Exception e){
                    log.error("生成图片验证码异常",e);
                }finally {
                    if(responseOutputStream!=null){
                        try {
                            responseOutputStream.flush();
                            responseOutputStream.close();
                        } catch (IOException e) {
                            log.error("生成图片验证码异常",e);
                        }
                    }
                }
            }else{
                log.warn("redis保存图片验证码失败");
            }
        }catch (IOException e) {
            log.error("生成图片验证码异常",e);
        }
    }

}
