package org.vivi.framework.report.service.common.utils;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.vivi.framework.report.service.common.constants.StatusCode;
import org.vivi.framework.report.service.common.exception.BizException;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * InfluxDB数据库连接操作类
 */

public class InfluxDBConnection {

	// 用户名
	private String username;
	// 密码
	private String password;
	// 连接地址
	private String openurl;
	// 数据库
	private String database;
	// 保留策略
	private String retentionPolicy;

	private InfluxDB influxDB;

	public InfluxDBConnection(String username, String password, String openurl, String database,
                              String retentionPolicy) {
		this.username = username;
		this.password = password;
		this.openurl = openurl;
		this.database = database;
		this.retentionPolicy = retentionPolicy == null || retentionPolicy.equals("") ? "autogen" : retentionPolicy;
		influxDbBuild();
	}

	/**
	 * 测试连接是否正常
	 *
	 * @return true 正常
	 */
	public boolean ping() {
		boolean isConnected = false;
		Pong pong;
		try {
			pong = influxDB.ping();
			if (pong != null) {
				isConnected = true;
			}
		} catch (Exception e) {
			throw new BizException(StatusCode.FAILURE, "influxdb连接测试失败，失败原因："+e.getMessage());
		}
		return isConnected;
	}

	/**
	 * 连接时序数据库 ，若不存在则创建
	 *
	 * @return
	 */
	public InfluxDB influxDbBuild() {
		if (influxDB == null) {
			influxDB = InfluxDBFactory.connect(openurl, username, password);
		}
		try {
			// if (!influxDB.databaseExists(database)) {
			// influxDB.createDatabase(database);
			// }
		} catch (Exception e) {
			// 该数据库可能设置动态代理，不支持创建数据库
			// e.printStackTrace();
		} finally {
			influxDB.setRetentionPolicy(retentionPolicy);
		}
		influxDB.setLogLevel(InfluxDB.LogLevel.NONE);
		return influxDB;
	}

	/**
	 * 查询
	 *
	 * @param command 查询语句
	 * @return
	 */
	public QueryResult query(String command) {
		return influxDB.query(new Query(command, database));
	}

	/**
	 * 插入
	 *
	 * @param measurement 表
	 * @param tags        标签
	 * @param fields      字段
	 */
//	public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields, long time,
//			TimeUnit timeUnit) {
//		Builder builder = Point.measurement(measurement);
//		builder.tag(tags);
//		builder.fields(fields);
//		if (0 != time) {
//			builder.time(time, timeUnit);
//		}
//		influxDB.write(database, retentionPolicy, builder.build());
//	}

	/**
	 * 批量写入测点
	 *
	 * @param batchPoints
	 */
//	public void batchInsert(BatchPoints batchPoints) {
//		influxDB.write(batchPoints);
//		// influxDB.enableGzip();
//		// influxDB.enableBatch(2000,100,TimeUnit.MILLISECONDS);
//		// influxDB.disableGzip();
//		// influxDB.disableBatch();
//	}

	/**
	 * 批量写入数据
	 *
	 * @param database        数据库
	 * @param retentionPolicy 保存策略
	 * @param consistency     一致性
	 * @param records         要保存的数据（调用BatchPoints.lineProtocol()可得到一条record）
	 */
//	public void batchInsert(final String database, final String retentionPolicy, final ConsistencyLevel consistency,
//			final List<String> records) {
//		influxDB.write(database, retentionPolicy, consistency, records);
//	}

	/**
	 * 删除
	 *
	 * @param command 删除语句
	 * @return 返回错误信息
	 */
//	public String deleteMeasurementData(String command) {
//		QueryResult result = influxDB.query(new Query(command, database));
//		return result.getError();
//	}

	/**
	 * 关闭数据库
	 */
	public void close() {
		influxDB.close();
	}

	/**
	 * 构建Point
	 *
	 * @param measurement
	 * @param time
	 * @param fields
	 * @return
	 */
	public Point pointBuilder(String measurement, long time, Map<String, String> tags, Map<String, Object> fields) {
		Point point = Point.measurement(measurement).time(time, TimeUnit.MILLISECONDS).tag(tags).fields(fields).build();
		return point;
	}
	
	public static void main(String[] args) {
		InfluxDBConnection influxDBConnection = new InfluxDBConnection("root", "1qaz@WSX", "http://127.0.0.1:8086", "_internal", null);
		QueryResult queryResult = influxDBConnection.query("SELECT * FROM \"database\" WHERE time > now() - 5m");
		queryResult.getResults();
		queryResult.getResults().get(0).getSeries().get(0).getColumns();
		queryResult.getResults().get(0).getSeries().get(0).getValues();
		System.out.println();
	}

}
