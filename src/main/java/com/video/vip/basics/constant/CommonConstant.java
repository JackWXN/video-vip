package com.video.vip.basics.constant;

/**
 * 通用常量类
 * @author wxn
 */
public class CommonConstant {

    /**
     * 时间格式
     */
    public static final String DATE_FORMAT_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 用户登录后解析的token key.在request的attribute中获取
     */
    public final static String USER_TOKEN_KEY = "USER_TOKEN";

    /**
     * 本地缺省配置文件路径
     */
    public final static String LOCAT_DEFAULT_CONFIG_PATH = "/app/video/sysConfig/";

    /**
     * 文件服务器基本路径
     */
    public final static String LOCAT_PUBLIC_PATH = "/app/mount/itfin";
//    public final static String LOCAT_PUBLIC_PATH = "c:/app/mount/itfin";

    /**
     * 公用配置文件路径
     */
    public final static String LOCAT_PUBLIC_CONFIG_PATH = LOCAT_PUBLIC_PATH+"/fileData/config/";

    /**
     * 开发本地引用配置路径
     */
    public final static String LOCAT_DEV_CONFIG_PATH = "C:/video/sysConfig/";

    /**
     * 项目名称
     */
    public static String PROJECT_NAME = "";

    /**
     * 项目路径
     */
    public static String PROJECT_PATCH = "";

    /**
     * 操作系统类型
     */
    public static String OPERATING_SYSTEM = "";

    /**
     * 服务器ip
     */
    public static String SERVER_IP = "";

    /**
     * 人员管理token中的id名称
     */
    public static final String TOKEN_PM_ID_NAME = "pmid";

    /**
     * 用户中心token中的id名称
     */
    public static final String TOKEN_USERCENTER_ID_NAME = "userId";

    /**
     * 执行开启
     */
    public static final String DTO_EXEC_OPEN = "Y";

}
