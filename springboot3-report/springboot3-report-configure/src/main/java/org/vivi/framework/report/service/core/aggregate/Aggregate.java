package org.vivi.framework.report.service.core.aggregate;


/**  
 * @ClassName: Aggregate
 * @Description: 数据聚合
*/
public abstract class Aggregate<T,S,M> {

	/**
	*<p>Title: aggregate</p>
	*<p>Description: 数据聚合</p>
	*/
	public abstract S aggregate(T t,S s,M m);
}
