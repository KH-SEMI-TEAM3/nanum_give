package edu.kh.semi.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.kh.semi.member.model.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
}
