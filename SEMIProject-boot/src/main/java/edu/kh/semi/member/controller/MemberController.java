package edu.kh.semi.member.controller;

import org.springframework.beans.factory.annotation.Autowired;

import edu.kh.semi.member.model.service.MemberService;

public class MemberController {

	@Autowired
	private MemberService memberService;
}
