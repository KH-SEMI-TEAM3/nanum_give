package edu.kh.semi.mypage.model.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import edu.kh.semi.member.model.dto.Member;

public class MyPageServiceImpl implements MyPageService {

	@Override
	public int updateInfo(Member inputMember, String[] memberAddress) {
		
		return 0;
	}

	@Override
	public int changePw(Map<String, String> paramMap, int memberNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int secession(String memberPw, int memberNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int profile(MultipartFile profileImg, Member loginMember) {
		// TODO Auto-generated method stub
		return 0;
	}

}
