package com.video.vip.util.token;

import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.service.RedisService;
import com.video.vip.basics.util.basics.DateUtil;
import com.video.vip.basics.util.basics.MyApplicationContextUtil;
import com.video.vip.basics.util.basics.StringUtil;
import com.video.vip.basics.util.pwd.PassportConstantUtil;
import com.video.vip.basics.util.pwd.PhoneUtil;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Iterator;

/**
 * 生成token工具类
 *
 * @author wxn
 */
@Slf4j
public class JavaWebTokenUtil {
    private final static String TOKEN_REDIS = "token_";

    /**
     * 密钥KEY
     */
    private final static String BASE64_SECRETT = "ofSDnwDGMO:e_2Q0Nj1IIEPOUjsdWES#MjYyN2I0ZjY=";

    /**
     * 解析token
     * @param jsonWebToken
     * @return
     */
    public static String parseJWT(String jsonWebToken) {
        try {
            log.debug("开始解析token:{}", jsonWebToken);
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(BASE64_SECRETT))
                    .parseClaimsJws(jsonWebToken).getBody();
            //如果token不为空，则将手机号解密
            if (null != claims) {
                log.info("解析后的token:{}", claims.toString());
                Iterator iterator = claims.keySet().iterator();
                JSONObject jsonObjToken = new JSONObject();
                while (iterator.hasNext()) {
                    String strKey = iterator.next().toString();
                    //如果是手机号
                    if ("phone".toUpperCase().equals(strKey.toUpperCase())) {
                        String phone = claims.get("phone").toString();
                        phone = PhoneUtil.decrypt(phone);
                        jsonObjToken.put("phone", phone);
                    } else {
                        jsonObjToken.put(strKey, claims.get(strKey));
                    }
                }
                //获取redis操作类
                RedisService redisService = (RedisService) MyApplicationContextUtil.getContext().getBean("redisService");
                String redisKeyStr = TOKEN_REDIS + jsonObjToken.get("p_id");
                if(StringUtils.isNotBlank(jsonObjToken.getString("platform_id"))){
                    redisKeyStr += "_"+jsonObjToken.getString("platform_id");
                }
                log.info("token校验redis key:{}", redisKeyStr);
                Object objTokenLoginDate = redisService.get(redisKeyStr);
                log.info("token校验redis存储的时间{}", objTokenLoginDate);
                if (null == objTokenLoginDate) {
                    log.warn("redisToken不存在过期或已登出：jsonToken:{},redisToken:{}"
                            , jsonObjToken.get("login_date").toString(), "0");
                    return null;
                } else {
                    log.info("token中存储的登录时间{}", jsonObjToken.get("login_date").toString());
                    if (!jsonObjToken.get(PassportConstantUtil.TOKEN_LOGIN_DATE_INDEX).toString().equals(objTokenLoginDate.toString())) {
                        log.warn("token过期或已登出:jsonToken:{},redisToken:{}"
                                , jsonObjToken.get("login_date").toString(), objTokenLoginDate.toString());
                        return null;
                    }
                }
                return jsonObjToken.toJSONString();
            } else {
                return null;
            }
        } catch (SignatureException | ExpiredJwtException e) {
            log.warn("解析token失败（过期）：jsonWebToken:{},e:{}",jsonWebToken, e.getClass());
            return null;
        } catch (Exception ex) {
            log.warn("解析token发生异常：jsonWebToken:{},e:{}",jsonWebToken, ex.getClass(), ex);
            return null;
        }
    }

    /**
     * 登出
     *
     * @param token
     * @return
     */
    public static boolean logoutByToken(@NonNull String token) {
        String jsonToken = JavaWebTokenUtil.parseJWT(token);
        if (null == jsonToken) {
            return true;
        } else {
            JSONObject jsonObjToken = JSONObject.parseObject(jsonToken);
            if (jsonObjToken == null|| jsonObjToken.get("p_id") == null|| StringUtils.isBlank(jsonObjToken.get("p_id").toString())) {
                log.warn("token中没有找到pid:jsonObjToken:{}", jsonObjToken);
                return false;
            } else {
                return logoutByPid(Long.parseLong(jsonObjToken.get("p_id").toString()));
            }
        }
    }

    /**
     * 根据pid,redis登出
     *
     * @param : pid
     * @author : wxn
     * @date : 2019/11/28 16:51
     */
    private static boolean logoutByPid(@NonNull Long pid) {
        //获取redis操作类
        RedisService redisService = (RedisService) MyApplicationContextUtil.getContext().getBean("redisService");
        redisService.remove(TOKEN_REDIS + pid);
        return true;
    }

    /**
     * 生成token
     *
     * @param pId                用户账号id
     * @param roles              app角色列表，json格式appId:角色,appId:角色
     * @param phone              手机号
     * @param tokenExpiresMs token过期时间（单位:毫秒）
     * @return 返回token
     */
    public static String createJWT(Long pId, String roles, String phone, Long tokenExpiresMs) {
        //手机号加密
        if(StringUtils.isEmpty(phone)){
            phone = PhoneUtil.encryption(phone);
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        String strNowDate = DateUtil.getDateTime(now, "yyyyMMddHHmmssSSS");

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BASE64_SECRETT);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("p_id", pId)
                .claim("roles", roles)
                .claim("phone", phone)
                .claim("login_date", strNowDate)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (tokenExpiresMs >= 0) {
            long expMillis = nowMillis + tokenExpiresMs;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        //获取redis操作类
        RedisService redisService =
                (RedisService) MyApplicationContextUtil.getContext().getBean("redisService");
        log.info("添加login_date缓存----token_"+pId.toString()+",strNowDate="+strNowDate);
        //默认30天过期30*24*60*60L
        redisService.set(TOKEN_REDIS + pId.toString(), strNowDate, 30 * 24 * 60 * 60L);
        //生成JWT
        return builder.compact();
    }
}
