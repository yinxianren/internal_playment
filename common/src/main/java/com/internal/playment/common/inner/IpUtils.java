package com.internal.playment.common.inner;


import javax.servlet.http.HttpServletRequest;
import java.net.*;
import java.util.Enumeration;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2017/12/13
 * Time: 15:34
 * Project: portal-parent
 * Package: com.rxh.utils
 */
public class IpUtils {

    // private final static String[] HEADERS = {"Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};

    public final static String X_FORWARDED_FOR = "X-Forwarded-For";
    public final static String X_REAL_IP = "X-Real-IP";

    private final static String LOOPBACK_ADDRESS = "127.0.0.1";

    /**
     * 获取本机IP
     *
     * @return IP
     */
    public static String getIp() {
        String linuxIp = getLinuxLocalIp();
        String winIp = getWindowsLocalIp();
        if (StringUtils.isNotBlank(linuxIp) && !LOOPBACK_ADDRESS.equals(linuxIp)) {
            return linuxIp;
        } else if (StringUtils.isNotBlank(winIp) && !LOOPBACK_ADDRESS.equals(winIp)) {
            return winIp;
        } else {
            return LOOPBACK_ADDRESS;
        }
    }

    /**
     * Linux获取获取本机IP
     *
     * @return IP
     */
    public static String getLinuxLocalIp() {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                while (ips.hasMoreElements()) {
                    ip = ips.nextElement();
                    if (ip instanceof Inet4Address && ip.getHostAddress().contains(".")) {
                        // 1.获取符合条件的ip
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Windows获取获取本机IP
     *
     * @return IP
     */
    public static String getWindowsLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取请求的IP
     *
     * @param request 请求
     * @return IP
     */
    public static String getReallyIpForRequest(HttpServletRequest request) {
        if (request != null) {
            String remoteAddr = request.getRemoteAddr();
            String forwarded = request.getHeader(X_FORWARDED_FOR);
            String realIp = request.getHeader(X_REAL_IP);
            if (StringUtils.isBlank(realIp)) {
                return StringUtils.isBlank(forwarded) ? remoteAddr : StringUtils.split(forwarded, ",")[0].trim();
            } else {
                return StringUtils.isBlank(forwarded) ? realIp : StringUtils.split(forwarded, ",")[0].trim();
            }
        }
        return null;
    }
}
