package com.taobao.pamirs.schedule.strategy;

import com.taobao.pamirs.schedule.strategy.ScheduleStrategy.Kind;

public class ScheduleStrategyRunntime
{

	/**
	 * 策略名称
	 */
	String strategyName;

	/**
	 * uuid
	 */
	String uuid;

	/**
	 * ip
	 */
	String ip;

	/**
	 * 任务类型,可选类型：Schedule,Java,Bean 大小写敏感
	 */
	private Kind kind;

	/**
	 * 任务名称：与任务类型匹配的名称例如：1、任务管理中配置的任务名称(对应Schedule) 2、Class名称(对应java) 3、Bean的名称(对应Bean)
	 */
	private String taskName;

	/**
	 * 任务参数:逗号分隔的Key-Value。 对任务类型为Schedule的无效，需要通过任务管理来配置的
	 */
	private String taskParameter;

	/**
	 * 需要的任务数量
	 */
	int requestNum;
	/**
	 * 当前的任务数量
	 */
	int currentNum;

	String message;

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public String getStrategyName()
	{
		return strategyName;
	}

	public void setStrategyName(String strategyName)
	{
		this.strategyName = strategyName;
	}

	public Kind getKind()
	{
		return kind;
	}

	public void setKind(Kind kind)
	{
		this.kind = kind;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public String getTaskParameter()
	{
		return taskParameter;
	}

	public void setTaskParameter(String taskParameter)
	{
		this.taskParameter = taskParameter;
	}

	public int getRequestNum()
	{
		return requestNum;
	}

	public void setRequestNum(int requestNum)
	{
		this.requestNum = requestNum;
	}

	public int getCurrentNum()
	{
		return currentNum;
	}

	public void setCurrentNum(int currentNum)
	{
		this.currentNum = currentNum;
	}

	@Override
	public String toString()
	{
		return "ScheduleStrategyRunntime [strategyName=" + strategyName + ", uuid=" + uuid + ", ip=" + ip + ", kind="
				+ kind + ", taskName=" + taskName + ", taskParameter=" + taskParameter + ", requestNum=" + requestNum
				+ ", currentNum=" + currentNum + ", message=" + message + "]";
	}

}
