package com.taobao.pamirs.schedule.taskmanager;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.data.Stat;

import com.taobao.pamirs.schedule.TaskItemDefine;

/**
 * 调度配置中心客户端接口，可以有基于数据库的实现，可以有基于ConfigServer的实现
 *
 * @author xuannan
 */
public interface IScheduleDataManager
{

	/**
	 * 获取当前系统时间，以初始调度中心客户端的时候zk服务器时间为基准
	 * zkBaseTime + (sysdate - loclaBaseTime)
	 * @return
	 */
	public long getSystemTime();

	/**
	 * 重新装载当前调度服务器server需要处理的任务队列
	 * 遍历 /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem子节点
	 * 如果 /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/cur_server 等于 当前server的uuid，则表示是其需要承担的任务项
	 * @param taskType 任务类型
	 * @param uuid 当前server的UUID
	 */
	public List<TaskItemDefine> reloadDealTaskItem(String taskType, String uuid) throws Exception;

	/**
	 * 装载所有的任务队列信息
	 * 遍历 /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem 子节点
	 */
	public List<ScheduleTaskItem> loadAllTaskItem(String taskType) throws Exception;

	/**
	 * 释放自己把持，别人申请的队列
	 *
	 *  主要
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/cur_server
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/req_server
	 *
	 * 如果发生了调整，要设置的调度任务的加载标识 reload=true
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 *
	 */
	public void releaseDealTaskItem(String taskType, String uuid) throws Exception;

	/**
	 * 获取指定任务类型的处理队列数量
	 *  /rootptah/baseTaskType/<baseTaskType>/<taskType>/taskItem 子节点的数量
	 */
	public int queryTaskItemCount(String taskType) throws Exception;

	/**
	 * 从 /rootPath/baseTaskType/<baseTaskType> 中获取调度任务类型相关信息
	 */
	public ScheduleTaskType loadTaskTypeBaseInfo(String taskType) throws Exception;

	/**
	 * 清除已经过期的调度服务器信息
	 * 遍历 /rootpath/baseTaskType/<baseTasktype>/<taskType>/server 子节点
	 * 系统时间-修改时间 > 判断一个服务器死亡的周期，如过期则删除
	 */
	public int clearExpireScheduleServer(String taskType, long expireTime) throws Exception;

	/**
	 * 清除任务信息，服务器已经不存在的时候
	 * 如果 /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/cur_server的值 不在serverList中 将该path的值设置为null
	 */
	public int clearTaskItem(String taskType, List<String> serverList) throws Exception;

	/**
	 * 获取某个调度任务类型下所有的有效服务器信息
     * 遍历 /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 */
	public List<ScheduleServer> selectAllValidScheduleServer(String taskType) throws Exception;

	/**
	 *  根据任务类型 加载 该任务下所有调度服务器
	 *  /rootptah/baseTaskType/<baseTaskType>/<taskType>/server的子节点
	 */
	public List<String> loadScheduleServerNames(String taskType) throws Exception;

	/**
	 * 重新分配任务Item
	 * 遍历 /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem子节点，将所有任务项分配给调度服务器集合
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/cur_server
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/taskItem/<taskItem>/req_server
	 * 如果发生改变
	 * 则设置的调度任务的加载标识 reload=true
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 */
	public void assignTaskItem(String taskType, String currentUuid, int maxNumOfOneServer, List<String> serverList)
			throws Exception;

	/**
	 * 发送心跳信息
     * 更新/rootpath/baseTaskType/<baseTaskType>/<taskType>/server/<taskType$ip$uuid$>的心跳时间和版本号
	 */
	public boolean refreshScheduleServer(ScheduleServer server) throws Exception;

	/**
	 * 注册调度服务器
     * /rootPath/baseTaskType/<baseTaskTyp>/<taskType>/server/<taskType$ip$uuid$>
	 */
	public void registerScheduleServer(ScheduleServer server) throws Exception;

	/**
	 * 注销服务器
     * 删除/rootPath/baseTaskType/<baseTaskTyp>/<taskType>/server/<taskType$ip$uuid$>
	 */
	public void unRegisterScheduleServer(String taskType, String serverUUID) throws Exception;

	/**
	 * 清除已经过期的OWN_SIGN的自动生成的数据
	 * /rootPath/baseTaskType/<baseTaskType>
	 * @param baseTaskType 任务类型
	 * @param serverUUID 服务器
	 * @param expireDateInternal 过期时间，以天为单位
	 */
	public void clearExpireTaskTypeRunningInfo(String baseTaskType, String serverUUID, double expireDateInternal)
			throws Exception;

    /**
     * 判断当前调度服务器是否是leader
     * @param uuid
     * @param serverList
     * @return
     */
	public boolean isLeader(String uuid, List<String> serverList);

	public void pauseAllServer(String baseTaskType) throws Exception;

	public void resumeAllServer(String baseTaskType) throws Exception;

    /**
     * 获取所有的调度任务类型信息
     * 遍历/rootPath/baseTaskType的子节点
     * @return
     * @throws Exception
     */
	public List<ScheduleTaskType> getAllTaskTypeBaseInfo() throws Exception;

	/**
	 * 清除一个任务类型的运行期信息
	 * 删除/rootPath/baseTaskType/<baseTaskType>子节点
	 */
	public void clearTaskType(String baseTaskType) throws Exception;

	/**
	 * 创建一个新的任务类型
	 * 创建/rootPath/baseTaskType/<baseTaskType>
	 * value：ScheduleTaskType（调度任务类型）
	 */
	public void createBaseTaskType(ScheduleTaskType baseTaskType) throws Exception;

    /**
     * 修改一个任务类型
	 * 修改 /rootPath/baseTaskType/<baseTaskType>
     * @param baseTaskType
     * @throws Exception
     */
	public void updateBaseTaskType(ScheduleTaskType baseTaskType) throws Exception;

	/**
	 * 根据baseTaskType 查询所有调度任务运行时信息
	 * 遍历/rootPath/baseTaskType/<baseTaskType>
	 * @param baseTaskType
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleTaskTypeRunningInfo> getAllTaskTypeRunningInfo(String baseTaskType) throws Exception;

	/**
	 * 删除一个任务类型
	 * 删除删除/rootPath/baseTaskType/<baseTaskType>节点
	 */
	public void deleteTaskType(String baseTaskType) throws Exception;

	/**
	 * 根据条件查询 有效的服务器信息
	 * baseTaskType 原始任务类型 可为空
	 * ownSign 环境信息 可为空
	 *
	 * baseTaskType、ownSign都不为空的时候, 查询 查询 /rootPath/baseTaskType/<baseTaskType>/<baseTaskType$ownSign>/server下的调度服务器
	 *
	 * baseTaskType不为空、ownSign为空的时候  /rootPath/baseTaskType/<baseTaskType>/<baseTaskType$*>/server下所有调度服务器
	 *
	 * baseTaskType为空、ownSign不为空的时候 /rootPath/baseTaskType/<*>/<*$ownSign>/server
	 *
	 * 两者都为空的时候 /rootPath/baseTaskType/<*>/<*>/server
	 *
	 * 调用selectAllValidScheduleServer(String taskType) 遍历 /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 */
	public List<ScheduleServer> selectScheduleServer(String baseTaskType, String ownSign, String ip, String orderStr)
			throws Exception;

	/**
	 * 查询调度服务的历史记录
	 */
	public List<ScheduleServer> selectHistoryScheduleServer(String baseTaskType, String ownSign, String ip,
			String orderStr) throws Exception;

	/**
	 * 根据调度管理器（可以理解Wie机器？？？）查询运行于其上所有调度服务器
	 * @param factoryUUID
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleServer> selectScheduleServerByManagerFactoryUUID(String factoryUUID) throws Exception;

	/**
	 * 创建任务项。注意其中的 CurrentSever和RequestServer不会起作用
	 */
	public void createScheduleTaskItem(ScheduleTaskItem[] taskItems) throws Exception;

	/**
	 * 更新任务的状态和处理信息
	 */
	public void updateScheduleTaskItemStatus(String taskType, String taskItem, ScheduleTaskItem.TaskItemSts sts,
			String message) throws Exception;

	/**
	 * 删除任务项 /rootpath/baseTaskType/<baseTaskType>/<taskType>/tasktItem/<taskItem>
	 */
	public void deleteScheduleTaskItem(String taskType, String taskItem) throws Exception;

	/**
	 * 初始化任务调度的域信息和静态任务信息
	 */
	public void initialRunningInfo4Static(String baseTaskType, String ownSign, String uuid) throws Exception;

	public void initialRunningInfo4Dynamic(String baseTaskType, String ownSign) throws Exception;

	/**
	 * 运行期信息是否初始化成功
	 * 判断/rootptah/baseTaskType/<baseTaskType>/<taskType>/taskItem写入的数据是否和当前调度任务的leader服务器相同
	 */
	public boolean isInitialRunningInfoSucuss(String baseTaskType, String ownSign) throws Exception;

	/**
	 * 设置 运行期信息初始化成功
	 * /rootptah/baseTaskType/<baseTaskType>/<taskType>/taskItem 写入leader的uuid
	 * @param baseTaskType 原始任务类型
	 * @param taskType 任务类型
	 * @param uuid leader uuid
	 * @throws Exception
	 */
	public void setInitialRunningInfoSucuss(String baseTaskType, String taskType, String uuid) throws Exception;

	/**
	 * 从当前调度服务器中获取leader
	 * 调度服务器的唯一标识格式taskType1$ip$uuid$，其中uuid最小的是leader
	 * @param serverList
	 * @return
	 */
	public String getLeader(List<String> serverList);

	/**
	 * 设置的调度任务的加载标识 reload=true
	 * /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 * @param taskType
	 * @return long：version
	 * @throws Exception
	 */
	public long updateReloadTaskItemFlag(String taskType) throws Exception;

	/**
	 *  获取调度任务的版本号
	 *  /rootPath/baseTaskType/<baseTaskType>/<taskType>/server
	 * @param taskType
	 * @return long：version
	 * @throws Exception
	 */
	public long getReloadTaskItemFlag(String taskType) throws Exception;

	/**
	 * 通过taskType获取当前运行的serverList信息。
	 * 遍历/rootpath/baseTaskType/baseTaskType1/taskType1/server子节点
	 */
	public Map<String, Stat> getCurrentServerStatList(String taskType) throws Exception;

}
