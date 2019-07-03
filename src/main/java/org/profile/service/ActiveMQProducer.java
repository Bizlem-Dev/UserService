package org.profile.service;

import java.util.Map;

public interface ActiveMQProducer {

	public void producerCall(String topicId, Map<String, String> property,
	            String messageBody);
}
