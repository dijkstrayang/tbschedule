package com.taobao.pamirs.schedule;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 调度处理工具类
 *
 * @author xuannan
 */
public class ScheduleUtil {

    public static String OWN_SIGN_BASE = "BASE";

    public static String getLocalHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            return "";
        }
    }

    public static int getFreeSocketPort() {
        try {
            ServerSocket ss = new ServerSocket(0);
            int freePort = ss.getLocalPort();
            ss.close();
            return freePort;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return "";
        }
    }

    public static String transferDataToString(Date d) {
        SimpleDateFormat DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return DATA_FORMAT_yyyyMMddHHmmss.format(d);
    }

    public static Date transferStringToDate(String d) throws ParseException {
        SimpleDateFormat DATA_FORMAT_yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return DATA_FORMAT_yyyyMMddHHmmss.parse(d);
    }

    public static Date transferStringToDate(String d, String formate) throws ParseException {
        SimpleDateFormat FORMAT = new SimpleDateFormat(formate);
        return FORMAT.parse(d);
    }

    public static String getTaskTypeByBaseAndOwnSign(String baseType, String ownSign) {
        if (ownSign.equals(OWN_SIGN_BASE) == true) {
            return baseType;
        }
        return baseType + "$" + ownSign;
    }

    public static String splitBaseTaskTypeFromTaskType(String taskType) {
        if (taskType.indexOf("$") >= 0) {
            return taskType.substring(0, taskType.indexOf("$"));
        } else {
            return taskType;
        }

    }

    public static String splitOwnsignFromTaskType(String taskType) {
        if (taskType.indexOf("$") >= 0) {
            return taskType.substring(taskType.indexOf("$") + 1);
        } else {
            return OWN_SIGN_BASE;
        }
    }

    /**
     * 分配任务数量 即根据Strategy的配置为所有ManagerFactory，指定需要创建的线程组个数
     *
     * @param serverNum 总的服务器数量 可以分配的ManagerFactory总数
     * @param taskItemNum 任务项数量，配置线程组总数
     * @param maxNumOfOneServer 每个server最大任务项数目 单jvm最大线程组数量
     * @return int[] 每个调度服务器上 用于的线程组的数量
     */
    public static int[] assignTaskNumber(int serverNum, int taskItemNum, int maxNumOfOneServer) {
        int[] taskNums = new int[serverNum];
        //线程组总数 / 单jvm最大线程组数量   = 每个jvm基数
        int numOfSingle = taskItemNum / serverNum;
        //线程组总数 % 单jvm最大线程组数量 = 待分配的任务数
        int otherNum = taskItemNum % serverNum;
        // 20150323 删除, 任务分片保证分配到所有的线程组数上。 开始
        // if (maxNumOfOneServer >0 && numOfSingle >= maxNumOfOneServer) {
        // numOfSingle = maxNumOfOneServer;
        // otherNum = 0;
        // }
        // 20150323 删除, 任务分片保证分配到所有的线程组数上。 结束
        for (int i = 0; i < taskNums.length; i++) {
            if (i < otherNum) {
                taskNums[i] = numOfSingle + 1;
            } else {
                taskNums[i] = numOfSingle;
            }
        }
        return taskNums;
    }
}
