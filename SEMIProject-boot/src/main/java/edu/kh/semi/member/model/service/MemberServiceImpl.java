package edu.kh.semi.member.model.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.kh.semi.member.model.dto.Member;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class MemberServiceImpl implements MemberService {

	@Override
	public Member selectMemberByNo(int memberNo) {
		// TODO Auto-generated method stub
		return null;
	}

}
