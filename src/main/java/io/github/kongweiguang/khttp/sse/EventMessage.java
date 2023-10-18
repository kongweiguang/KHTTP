package io.github.kongweiguang.khttp.sse;

import java.util.StringJoiner;

/**
 * sse消息
 *
 * @author kongweiguang
 * @since 6.0.0
 */
public class EventMessage {
	/**
	 * 间隔时间重试
	 */
	private final int retry;
	/**
	 * id
	 */
	private final String id;
	/**
	 * 时间的类型
	 */
	private final String event;
	/**
	 * 数据
	 */
	private final String data;


	public int retry() {
		return retry;
	}

	public String id() {
		return id;
	}

	public String event() {
		return event;
	}

	public String data() {
		return data;
	}

	public EventMessage(final String id, final String event, final String data) {
		this(1000, id, event, data);
	}

	public EventMessage(final int retry, final String id, final String event, final String data) {
		this.retry = retry;
		this.id = id;
		this.event = event;
		this.data = data;
	}

	@Override
	public String toString() {
		return new StringJoiner("\n")
			.add("retry: " + retry)
			.add("id: " + id)
			.add("event: " + event)
			.add("data: " + data)
			.add("\n")
			.toString();
	}
}
