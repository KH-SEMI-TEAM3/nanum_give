package edu.kh.semi.message.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.kh.semi.member.model.dto.Member;
import edu.kh.semi.member.model.service.MemberService;
import edu.kh.semi.message.model.dto.Message;
import edu.kh.semi.message.model.service.MessageService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MemberService memberService; // 주고 받는 사람 조회용

    @Autowired
    private MessageService messageService; // 쪽지 처리용

    /** 쪽지 작성 폼으로 이동
     *  URL: /message/send/{memberNo}
     */
    @GetMapping("/send/{memberNo}")
    public String sendMessagePage(@PathVariable("memberNo") int memberNo,
                                   Model model, @RequestParam(value = "boardNo", required = false) Integer boardNo,
                                   @RequestParam(value = "boardCode", defaultValue = "1") int boardCode, // 게시판 코드 (기본값 1)
                                   @RequestParam(value = "cp", defaultValue = "1") int cp                       
    		) {

        // 받는 사람 정보 조회
        Member recipient = memberService.selectMemberByNo(memberNo);

        if (recipient == null) {
            model.addAttribute("errorMessage", "존재하지 않는 회원입니다.");
            return "redirect:/";
        }

        model.addAttribute("recipient", recipient);
        model.addAttribute("receiverNo", memberNo); // 받는 사람 회원 번호도 모델에 담아 폼 hidden 필드에 사용
        model.addAttribute("boardNo", boardNo); // 받은 boardNo 값을 모델에 담아 View로 전달 (hidden 필드에 사용)
        model.addAttribute("boardCode", boardCode);
        model.addAttribute("cp", cp);
        return "message/send"; // templates/message/send.html
    }

    /** 쪽지 전송 처리 (POST)
     *  URL: /message/send
     */
    @PostMapping("/send")
    public String sendMessage(@ModelAttribute Message message,
                              HttpSession session,
                              Model model, RedirectAttributes ra,
                              @RequestParam(required = false, value = "boardCode", defaultValue = "1") int boardCode,
                              @RequestParam(required = false, value = "cp", defaultValue = "1") int cp) {

        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        message.setSenderNo(loginMember.getMemberNo());

        int result = messageService.sendMessage(message);

        if (result > 0) {
            //  성공 시 원래 글 상세 페이지로 이동
        	  return "redirect:/message/detail/"+message.getMessageNo();        } else {
            //  실패 시 다시 쪽지 작성 페이지로 이동 (boardNo, boardCode, cp 유지)
            ra.addFlashAttribute("message", "쪽지 전송 실패.");

            String redirectPath = "redirect:/message/detail/" + message.getMessageNo();
            String queryParams = "?";

            if (message.getBoardNo() != 0) {
                queryParams += "boardNo=" + message.getBoardNo();
            }

            queryParams += "&boardCode=" + boardCode + "&cp=" + cp;

            //return "redirect:/message/outbox";
             return redirectPath; //+ queryParams;
            //  /message/detail/37
        }
    }

    
    /** 받은 쪽지함으로 가기
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/inbox")
    public String viewInbox(HttpSession session, Model model) {

        // 로그인 여부 확인
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        // 받은 쪽지 목록 조회
        List<Message> messageList = messageService.getInboxMessages(loginMember.getMemberNo());

        model.addAttribute("messageList", messageList);
        return "message/messageReceivedList"; // templates/message/messageReceivedList.html
    }
    
    /** 받은 쪽지 상세 조회
     *  URL: /message/detail/{messageNo}
     */
    @GetMapping("/inboxDetail/{messageNo:[0-9]+}")
    public String viewMessageInboxDetail(@PathVariable("messageNo") int messageNo,
                                     HttpSession session,
                                     Model model
                                    ) {

        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
           return "redirect:/member/login";
        }

        // Map으로 파라미터 묶기
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("messageNo", messageNo);
        paramMap.put("memberNo", loginMember.getMemberNo());

        Message message = messageService.getMessageDetail(paramMap);

        if (message == null) {
            model.addAttribute("errorMessage", "쪽지를 찾을 수 없습니다.");
            return "redirect:/message/inbox";
        }

        model.addAttribute("message", message);
        return "message/inboxDetail";
    
    }
    
    
    /** 보낸 쪽지 상세 조회
     *  URL: /message/detail/{messageNo}
     */
    @GetMapping("/outboxDetail/{messageNo:[0-9]+}")
    public String viewMessageOutboxDetail(@PathVariable("messageNo") int messageNo,
                                     HttpSession session,
                                     Model model) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
           return "redirect:/member/login";
        }

        // Map으로 파라미터 묶기
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("messageNo", messageNo);
        paramMap.put("memberNo", loginMember.getMemberNo());

        Message message = messageService.getMessageDetail(paramMap);

        if (message == null) {
            model.addAttribute("errorMessage", "쪽지를 찾을 수 없습니다.");
            return "redirect:/message/outbox";
        }

        model.addAttribute("message", message);
        return "message/outBoxDetail";
    
    }
    
    
    /** 보낸 쪽지함으로 이동 */
    @GetMapping("/outbox")
    public String outbox(HttpSession session, Model model) {

        Member loginMember = (Member) session.getAttribute("loginMember");

        if (loginMember == null) {
            model.addAttribute("errorMessage", "로그인이 필요합니다.");
            return "redirect:/member/login";
        }

        List<Message> sentList = messageService.getSentMessages(loginMember.getMemberNo());
        model.addAttribute("sentList", sentList);

        return "message/outbox"; // templates/message/outbox.html
    }
    
    
    
    
    /** 받은 쪽지 삭제
     * @param boardNo
     * @param model
     * @param memberNo
     * @param messageNo
     * @param messages
     * @return
     */
    @GetMapping("/inboxDelete/{memberNo}/{messageNo}")
    public String deleteMessageIn (@RequestParam (required = false, value = "boardNo", defaultValue = "1") int boardNo, Model model
    		, @PathVariable("memberNo") int memberNo , @PathVariable("messageNo") int messageNo, @ModelAttribute Message messages
    		){
    	
   
    	messages.setBoardNo(boardNo);
    	messages.setMessageNo(messageNo);
    	
    	int result = 0;
    	String message= null;
    	
    	
    	
		result = messageService.deleteMessagePage(messages);

		
    	if(result==0) {
    		
    		message = "삭제 실패!";
    		model.addAttribute("message", message);
    		
    		return "redirect:/message/inboxDetail";
    	}
    	
    	else {
    		message = "삭제 성공!";
    		model.addAttribute("message", message);
        	return "redirect:/message/inbox";

    	}
    	
    	
    }
    
    
    /** 나가는 쪽지 삭제
     * @param boardNo
     * @param model
     * @param memberNo
     * @param messageNo
     * @param messages
     * @return
     */
    @GetMapping("/outboxDelete/{memberNo}/{messageNo}")
    public String deleteMessageOut (@RequestParam (required = false, value = "boardNo", defaultValue = "1") int boardNo, Model model
    		, @PathVariable("memberNo") int memberNo , @PathVariable("messageNo") int messageNo, @ModelAttribute Message messages
    		){
    	
   
    	messages.setBoardNo(boardNo);
    	messages.setMessageNo(messageNo);
    	
    	int result = 0;
    	String message= null;
    	
    	
    	
		result = messageService.deleteMessagePage(messages);

		
    	if(result==0) {
    		
    		message = "삭제 실패!";
    		model.addAttribute("message", message);
    		
    		return "redirect:/message/outboxDetail";
    	}
    	
    	else {
    		message = "삭제 성공!";
    		model.addAttribute("message", message);
        	return "redirect:/message/outbox";

    	}
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
