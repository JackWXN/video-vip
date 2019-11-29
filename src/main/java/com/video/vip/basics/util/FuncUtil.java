package com.video.vip.basics.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.video.vip.basics.constant.CommonConstant;
import com.video.vip.basics.dto.Result;
import com.video.vip.basics.dto.UserTokenDTO;
import com.video.vip.basics.util.enums.ResultEnum;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * 融捷信通用工具类(业务相关)
 * @author wxn
 */
@Slf4j
public class FuncUtil {

    /**
     * 启动时判断，如果没有配置文件，则自动加载local目录下的配置文件
     */
    public static void selectedConfig(){
        try {
            InputStream input = null;
            input = FuncUtil.class.getClassLoader().getResourceAsStream("application.yml");
            if(input==null){
                log.warn("！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
                log.warn("注意：！！启动时未找到主配置文件，默认加载local文件夹的配置文件的");
                log.warn("！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！");
                String strProjectPatch = java.net.URLDecoder.decode(FuncUtil.class.getResource("/").getPath(), "gbk");
                System.out.println(strProjectPatch);
                FileUtils.copyDirectory(
                        new File(strProjectPatch+"../../src/main/conf/local")
                        ,new File(strProjectPatch));
            }else{
                log.info("启动时找到主配置文件，继续执行。");
            }
        } catch (Exception e) {
            log.error("启动时判断配置文件发生异常！！");
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换Result中的data
     * @param result 需要转换data的redult参数
     * @param c 需要转换data的class
     * @return
     */
    public static Result getResultParseData(@NonNull Result result, @NonNull Class c){
        if(result.getData()!=null){
            try {
                result.setData(JSON.toJavaObject((JSONObject)result.getData(),c));
            } catch (Exception e) {
                log.error("返回值转换异常异常:data:{}",result.getData(),e);
                result = Result.newResult(ResultEnum.EXCEPTION,"");
            }
        }
        return result;
    }

    /**
     * 转换类
     * @param c 需要转换data的class
     * @return
     */
    public static Result getJSONParseData(@NonNull Object obj, @NonNull Class c){
        Result result = Result.newSuccess();
        try {
            if(obj instanceof JSONObject){
                result.setData(JSON.toJavaObject((JSONObject)obj,c));
            }else if(obj instanceof String){
                result.setData(JSON.toJavaObject(JSONObject.parseObject((String)obj),c));
            }else if(obj instanceof List){
                List listRetu = new ArrayList();
                for(Object value : (List)obj){
                    if(value instanceof JSONObject){
                        listRetu.add(JSON.toJavaObject((JSONObject)obj,c));
                    }else if(value instanceof String){
                        listRetu.add(JSON.toJavaObject(JSONObject.parseObject((String)obj),c));
                    }else{
                        result = Result.newResult(ResultEnum.FAIL,"转换类型错误");
                        break;
                    }
                }
                result.setData(listRetu);
            }else{
                result = Result.newResult(ResultEnum.FAIL,"转换类型错误");
            }
        } catch (Exception e) {
            log.error("返回值转换异常异常:c = {}",c,e);
            result = Result.newResult(ResultEnum.EXCEPTION,"");
        }
        return result;
    }

    /**
     * 转换string为JSONObject对象
     * @param objData
     * @return
     */
    public static Object getApiRequest(Object objData){
        try {
            if(objData != null){
                if(objData instanceof String){
                    objData = JSONObject.parseObject(objData.toString());
                }
            }
        }catch (Exception ee){
            log.error("类型转换失败！！，返回原始值:objData:{}",objData,ee);
        }
        return objData;
    }

    /**
     * 将token转换成UserTokenDTO
     * @param jsonToken 明文token
     * @param oldToken 密文token
     * @return
     */
    public static UserTokenDTO getUserTokenDTOToken(
            @NonNull JSONObject jsonToken, @NonNull String oldToken){
        UserTokenDTO tUserTokenDTO = new UserTokenDTO();
        tUserTokenDTO.setToken(oldToken);
        tUserTokenDTO.setPid(jsonToken.getLong("p_id"));
        tUserTokenDTO.setPhone(jsonToken.getString("phone"));
        tUserTokenDTO.setAppId(jsonToken.getString("app_id"));
        tUserTokenDTO.setRoles(jsonToken.get("roles")==null
                ?new JSONObject(): JSONObject.parseObject(jsonToken.get("roles").toString()));
        return tUserTokenDTO;
    }

    /**
     * 设置项目名称常量
     * @return
     */
    public static String setProjectName() {
        if (StringUtils.isNotBlank(CommonConstant.PROJECT_NAME)){
            return CommonConstant.PROJECT_NAME;
        }
        try {
            String strProjectName = java.net.URLDecoder.decode(FuncUtil.class.getResource("/").getPath(), "gbk");
            int intFor = 0;
            //判断循环几次
            if(StringUtils.isNotBlank(strProjectName)){
                if("/".equals(strProjectName.substring(strProjectName.length()-1))){
                    intFor = 3;
                }else{
                    intFor = 2;
                }
            }
            for(int i=0;i<intFor;i++){
                strProjectName = strProjectName.substring(0,strProjectName.lastIndexOf("/"));
            }
            CommonConstant.OPERATING_SYSTEM = System.getProperty("os.name")
                    .toUpperCase();
            log.info("==> 操作系统类型为：" + CommonConstant.OPERATING_SYSTEM);
            CommonConstant.PROJECT_NAME = strProjectName.substring(strProjectName.lastIndexOf("/")+1);
            log.info("==> 获得项目名称为：" + CommonConstant.PROJECT_NAME);
            CommonConstant.PROJECT_PATCH = strProjectName;
            //如果操作系统是WINDOWS则去掉第一位/
            if(CommonConstant.OPERATING_SYSTEM.indexOf("WINDOWS")>-1){
                if(StringUtils.isNotBlank(CommonConstant.PROJECT_PATCH)
                        &&CommonConstant.PROJECT_PATCH.charAt(0)=='/'){
                    CommonConstant.PROJECT_PATCH = CommonConstant.PROJECT_PATCH.substring(1);
                }
            }
            log.info("==> 获得项目部署绝对目录为：" + CommonConstant.PROJECT_PATCH);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("设置项目名称错误", e);
        }
        return CommonConstant.PROJECT_NAME;
    }

    /**
     * 获取操作系统类型
     * @return
     */
    private static String getOSName() {
        return System.getProperty("os.name").toLowerCase();
    }

    /**
     * 设置项目部署绝对路径
     * @return
     */
    public static String setMRealPath() {
        setProjectName();
        return CommonConstant.PROJECT_PATCH;
    }
    /**
     * 根据页数和每页条数计算mysql limit起始条数
     * @param pageNum 页数
     * @param num 每页读取条数
     * @return
     */
    public static Integer getMysqlLimitBenin(Integer pageNum,Integer num){
        if(pageNum!=null&&num!=null){
            return (pageNum-1)*num;
        }else{
            return 0;
        }
    }

    /**
     * 去掉like查询特殊字符
     * @param param
     * @return
     */
    public static String getMysqlLikeKeyword(String param){
        StringBuffer strbRetu = new StringBuffer();
        if(StringUtils.isNotBlank(param)){
            for(int i=0;i<param.length();i++){
                strbRetu.append("\\"+param.charAt(i));
            }
            return strbRetu.toString();
        }else{
            return param;
        }
    }

    /**
     * orc识别身份证格式转换
     * 20060216-20160216 /  2006.02.16-2016.02.16
     * @param param
     * @return
     */
    public static String idCardFormat(String param){
        String result = "";
        if(StringUtils.isBlank(param)){
            return result;
        }
        String[] format = param.split("-");

        if(format != null && format.length > 0){
            for(int i=0;i<format.length;i++){
                String str = format[i];
                result += str.substring(0,4) + "." + str.substring(4,6) + "." + str.substring(6,str.length()) + " - ";
            }
        }
        return  result.substring(0,result.length()-3);
    }

    /**
     * output输出到页面
     * @param str 要输出的内容
     * @param response
     * @author wxn
     * @date 2015年12月4日, PM 05:05:52
     * @remark 产品批量新增：新增
     */
    public static void outPrint(String str, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            out = response.getWriter();
            out.print(str);
        } catch (Exception e) {
            log.error("输出页面时发生异常：{},{}" ,e.getClass(),e.getMessage(), e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }

}
