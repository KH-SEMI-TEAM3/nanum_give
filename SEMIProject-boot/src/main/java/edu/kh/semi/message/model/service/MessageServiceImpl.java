package edu.kh.semi.message.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.semi.message.model.dto.Message;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {@Override
	public int sendMessage(Message message) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Message getMessageDetail(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int deleteMessagePage(Message messages) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Message> getInboxMessages(int memberNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Message> getSentMessages(int memberNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
