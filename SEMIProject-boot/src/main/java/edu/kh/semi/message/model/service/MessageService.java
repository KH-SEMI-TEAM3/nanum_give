package edu.kh.semi.message.model.service;

import java.util.List;
import java.util.Map;


import edu.kh.semi.message.model.dto.Message;


public interface MessageService {

	int sendMessage(Message message);

	Message getMessageDetail(Map<String, Object> paramMap);

	int deleteMessagePage(Message messages);

	List<Message> getInboxMessages(int memberNo);

	List<Message> getSentMessages(int memberNo);


}
