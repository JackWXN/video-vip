package com.video.vip.basics.util.basics;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ip工具类
 * Created by charlin on 2017/9/9.
 */
@Slf4j
public class IPUtil {

    /**
     * 获取本地ip 适合windows与linux
     * @return
     */
    public static String getLocalIP() {
        String ip = "";
        try{
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost().getHostAddress();
            } else {
                ip = getLinuxLocalIp();
            }
        }catch (Exception ee){
            log.error("获取本地ip出现异常！ip={}",ip,ee);
        }
        return ip;
    }

    /**
     * 检查IP是否合法
     * @param ip
     * @return
     */
    public static boolean ipValid(String ip) {
        String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
        String regex1 = "1\\d{2}";
        String regex2 = "[1-9]\\d";
        String regex3 = "\\d";
        String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
        regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex  + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本地Host名称
     */
    public static String getLocalHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取Linux下的IP地址
     *
     * @return IP地址
     * @throws SocketException
     */
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                                System.out.println(ipaddress);
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            System.out.println("获取ip地址异常");
            ip = "127.0.0.1";
            ex.printStackTrace();
        }
        System.out.println("IP:"+ip);
        return ip;
    }


    /**
     * 获取客户机的ip地址
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }


    /**
     * 把ip转化为整数
     * @param ip
     * @return
     */
    public static long translateIP2Int(String ip){
        String[] intArr = ip.split("\\.");
        int[] ipInt = new int[intArr.length];
        for (int i = 0; i <intArr.length ; i++) {
            ipInt[i] = new Integer(intArr[i]).intValue();
        }
        return ipInt[0] * 256 * 256 * 256 + + ipInt[1] * 256 * 256 + ipInt[2] * 256 + ipInt[3];
    }

    public static void main(String[] args) {
        System.out.println(getLocalIP());
    }
}
