package com.internal.playment.common.inner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UUID {

    private static Map<String, Integer> IDENTITES = new HashMap<>();// 自增集合

    private static final int SHORT_KEY_MAX = 999;

    private static final int KEY_MAX = 9999;

    private static String IP;

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmss");

    private static SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyMMdd");

    static {
        IP = IpUtils.getIp();
        IP = IP.substring(IP.lastIndexOf(".") + 1);
        switch (IP.length()) {
            case 1:
                IP = "00" + IP;
                break;
            case 2:
                IP = "0" + IP;
                break;
            default:
                break;
        }
    }

    /**
     * 获取序列号
     *
     * @param table  表名
     * @param keyMax key最大值
     * @return key
     */
    private static synchronized int sequence(String table, int keyMax) {
        Integer identity = IDENTITES.get(table);
        if (null == identity) {
            identity = 0;
        }
        if (identity >= keyMax) {
            identity = 0;
        }
        IDENTITES.put(table, ++identity);
        return identity;
    }

    private static synchronized int sequence(int lastKey) {
        int identity = ++lastKey;
        if (identity >= UUID.SHORT_KEY_MAX) {
            identity = 0;
        }
        return identity;
    }

    /**
     * 获取 long 主键(12位)
     *
     * @param table 表名
     * @return 主键
     */
    public static synchronized long createShortKey(String table) {
        return Long.valueOf(TIME_FORMAT.format(new Date()) + IP + String.format("%03d", sequence(table, SHORT_KEY_MAX)));
    }

    /**
     * 获取 int 主键（7位yyMMdd000）
     *
     * @param lastId 最后的ID
     * @return 主键
     */
    public static synchronized int createIntegerKey(String lastId) {
        int newIdDate = Integer.parseInt(TIME_FORMAT.format(new Date()));
        if (lastId==null||lastId.length() < 6) {
            return Integer.parseInt(newIdDate + String.format("%03d", sequence(0)));
        }
        int lastIdDate = Integer.parseInt(lastId.substring(0, 6));
        int lastKey = Integer.parseInt(lastId.substring(6));
        if (newIdDate > lastIdDate) {
            return Integer.parseInt(newIdDate + String.format("%03d", sequence(0)));
        }
        return Integer.parseInt(newIdDate + String.format("%03d", sequence(lastKey)));
    }

    /**
     * 获取 long 主键(19位：yyMMddHHmmss + 0000)
     *
     * @param table 表名
     * @return 主键
     */
    public static synchronized long createKey(String table) {
        return Long.valueOf(DATE_FORMAT.format(new Date()) + IP + String.format("%04d", sequence(table, KEY_MAX)));
    }

    /**
     * 获取带前缀的主键(19位+前缀长度)
     *
     * @param table  表名
     * @param prefix 前缀名
     * @return 主键
     */
    public static synchronized String createKey(String table, String prefix) {
        return prefix + DATE_FORMAT.format(new Date()) + IP + String.format("%04d", sequence(table, KEY_MAX));
    }

    /**
     * 获取带前缀的主键
     *
     * @param prefix 前缀名
     * @return 主键
     */
    public static synchronized String createKeyRandom(String prefix) {
        Random random = new Random();
        return prefix + DATE_FORMAT.format(new Date()) + String.format("%04d", random.nextInt(SHORT_KEY_MAX));
    }


    /**
     * 获取
     * 商户号
     * 代理商户号
     * 通道ID
     * 机构ID
     * @param prefix 前缀名
     * @return 主键
     */

    public static  synchronized String createNumber( String prefix,String lastId) {
        String idNumber="";
        switch (prefix){
            case "M":
                if (("").equals(lastId) || lastId==null){
                    idNumber= "M"+"201001";
                }else {
                    int mLastId=Integer.parseInt(lastId.substring(1));
                    idNumber="M"+(mLastId+1+"");
                }
                return idNumber;
            case "A":
                if (("").equals(lastId) || lastId==null){
                    idNumber= "A"+"301001";
                }else {
                    int aLastId = Integer.parseInt(lastId.substring(1));
                    idNumber="A"+(aLastId+1+"");
                }
                return idNumber;
            case "CH":
                if (("").equals(lastId) || lastId==null){
                    idNumber= "CH"+"101001";
                }else {
                    int chLastId=Integer.parseInt(lastId.substring(2));
                    idNumber="CH"+(chLastId+1+"");
                }
                return idNumber;
            case "ORG":
                if (("").equals(lastId) || lastId==null){
                    idNumber= "ORG"+"1001";
                }else {
                    int oLastId=Integer.parseInt(lastId.substring(3));
                    idNumber="ORG"+(oLastId+1+"");
                }
                return idNumber;
            case "EX":
                if (("").equals(lastId) || lastId==null){
                    idNumber= "EX"+"1001";
                }else {
                    int chLastId=Integer.parseInt(lastId.substring(2));
                    idNumber="EX"+(chLastId+1+"");
                }
                return idNumber;
            default: return "";

        }

//    return  null;
    }

public static String createId(){
    //格式化当前时间
    SimpleDateFormat sfDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    String strDate = sfDate.format(new Date());
    //得到17位时间如：20170411094039080
//    System.out.println("时间17位：" + strDate);
    //为了防止高并发重复,再获取3个随机数
    String random = getRandom620(3);
    //最后得到20位订单编号。
    return strDate + random;
}
    /**
     * 获取6-10 的随机位数数字
     * @param length	想要生成的长度
     * @return result
     */
    public static String getRandom620(Integer length) {
        String result = "";
        Random rand = new Random();
        int n = 20;
        if (null != length && length > 0) {
            n = length;
        }
        int randInt = 0;
        for (int i = 0; i < n; i++) {
            randInt = rand.nextInt(10);
            result += randInt;
        }
        return result;
    }


    /**
     * 获取提现流水号（11位yyMMdd10001）
     * @param lastId 最后的ID
     * @return 主键
     */
    public static  synchronized String createDrawalNumber( String lastId) {
        if (("").equals(lastId) || lastId==null){
            return  TIME_FORMAT.format(new Date())+"10001";
        }else {
            return  TIME_FORMAT.format(new Date())+(Integer.parseInt(lastId.substring(6))+1)+"";

        }

    }

    /**
     * 获取16位随机字符串 * @return String
     */
    public static String getUUID() {
        String uuid = java.util.UUID.randomUUID().toString();
        char[] cs = new char[32];
        char c = 0;
        for (int i = uuid.length() / 2, j = 1; i-- > 0; ) {
            if ((c = uuid.charAt(i)) != '-') {
                cs[j++] = c;
            }
        }
        String uid = String.valueOf(cs);
        return uid;
    }
}
