package com.taobao.pamirs.schedule.strategy;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ScheduleStrategy
{

	public enum Kind
	{
		Schedule, Java, Bean
	}

	/**
	 * 策略名称
	 */
	private String strategyName;

	/**
	 * IP地址(逗号分隔)：127.0.0.1或者localhost会在所有机器上运行
	 */
	private String[] IPList;

	/**
	 * 单JVM最大线程组数量:单JVM最大线程组数量，如果是0，则表示没有限制.每个服务器运行的线程组数量 =总量/机器数
	 */
	private int numOfSingleServer;
	/**
	 * 需要执行调度的最大线程组数量，所有服务器总共运行的线程组的最大数量，也可以理解为调度服务器的数量
	 */
	private int assignNum;

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
	 * 服务状态: pause,resume
	 */
	private String sts = STS_RESUME;

	public static String STS_PAUSE = "pause";
	public static String STS_RESUME = "resume";

	@Override
	public String toString()
	{
		return ToStringBuilder.reflectionToString(this);
	}

	public String getStrategyName()
	{
		return strategyName;
	}

	public void setStrategyName(String strategyName)
	{
		this.strategyName = strategyName;
	}

	public int getAssignNum()
	{
		return assignNum;
	}

	public void setAssignNum(int assignNum)
	{
		this.assignNum = assignNum;
	}

	public String[] getIPList()
	{
		return IPList;
	}

	public void setIPList(String[] iPList)
	{
		IPList = iPList;
	}

	public void setNumOfSingleServer(int numOfSingleServer)
	{
		this.numOfSingleServer = numOfSingleServer;
	}

	public int getNumOfSingleServer()
	{
		return numOfSingleServer;
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

	public String getSts()
	{
		return sts;
	}

	public void setSts(String sts)
	{
		this.sts = sts;
	}
}
