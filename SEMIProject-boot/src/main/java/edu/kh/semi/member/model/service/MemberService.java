package edu.kh.semi.member.model.service;

import edu.kh.semi.member.model.dto.Member;

public interface MemberService {

	Member selectMemberByNo(int memberNo);

}
