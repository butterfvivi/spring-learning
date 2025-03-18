package org.vivi.framework.lucky.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.DbImpl;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class LevelDBTool extends DbImpl {

	public LevelDBTool(Options options, File databaseDir) throws IOException {
		super(options, databaseDir);
	}

	// 依据key，获取Value
	public byte[] get(String key) throws DBException {
		return super.get(key.getBytes(StandardCharsets.UTF_8));
	}
	
	public Object getObject(String key) {
		byte[] bytes = get(key);
		if (null == bytes)
			return null;
		try (ByteArrayInputStream in = new ByteArrayInputStream(bytes);
				ObjectInputStream objIn = new ObjectInputStream(in)) {
			return objIn.readObject();
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	// 获取所有key
	public List<String> getKeys() {
		List<String> list = new ArrayList<>();
		DBIterator iterator = null;
		try {
			iterator = super.iterator();
			while (iterator.hasNext()) {
				Map.Entry<byte[], byte[]> item = iterator.next();
				String key = new String(item.getKey(), StandardCharsets.UTF_8);
				list.add(key);
			}
		} catch (Exception e) {
			throw new DBException(e);
		} finally {
			if (iterator != null) {
				try {
					iterator.close();
				} catch (IOException e) {
					throw new DBException(e);
				}

			}
		}
		return list;
	}

	public void put(String key, byte[] value) throws DBException {
		super.put(key.getBytes(StandardCharsets.UTF_8), value);
	}

	public void put(String key, Serializable obj) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream();
				ObjectOutputStream objOut = new ObjectOutputStream(out)) {
			objOut.writeObject(obj);
			objOut.flush();
			byte[] bytes = out.toByteArray();
			put(key, bytes);
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	// 基于fastjson的对象序列化
	public byte[] serializer(Object obj) {
		byte[] jsonBytes = JSON.toJSONBytes(obj,
				new SerializerFeature[] { SerializerFeature.DisableCircularReferenceDetect });
		return jsonBytes;
	}

	// 根据key删除数据
	public void delete(String key) {
		try {
			super.delete(key.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			throw new DBException(e);
		}
	}

	// spring销毁对象前关闭
	// @PreDestroy
	public void closeDB() {
		/*if (super != null) {
			
		}*/
		try {
			super.close();
		} catch (DBException e) {
			throw new DBException(e);
		}
	}

	public static class DBException extends RuntimeException {
		public DBException(Throwable e) {
			super(e);
		}
		public DBException(String msg) {
			super(msg);
		}
	}
}
