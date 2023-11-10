package com.vam.controller;

import java.io.File;
import java.nio.file.FileSystem;
import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vam.model.MemberVO;
import com.vam.service.MemberService;



@Controller
@RequestMapping(value = "/member")
public class MemberController {
   
   private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
   
   @Autowired
   private MemberService memberservice;
   
   @Autowired
   private JavaMailSender mailSender;
   
   @Autowired
   private BCryptPasswordEncoder pwEncoder;
   
   //회원가입 페이지 이동
   @RequestMapping(value = "join", method = RequestMethod.GET)
   public void loginGET() {
      
      logger.info("회원가입 페이지 진입");
      
   }
   
   //회원가입
      @RequestMapping(value="/join",method=RequestMethod.POST)
      public String joinPOST(MemberVO member) throws Exception{
         
        // logger.info("join 진입");
         
        // memberservice.memberJoin(member);
         
        // logger.info("join Service 성공");
    	  
    	  String rawPw = "";	// 인코딩 전 비밀번호
    	  String encodePw = "";	// 인코딩 후 비밀번호
    	  
    	  rawPw = member.getMemberPw();	// 비밀번호 데이터 얻음 
    	  encodePw = pwEncoder.encode(rawPw); // 비밀번호 인코딩
    	  member.setMemberPw(encodePw); // 인코딩된 비밀번호 member객체에 다시 저장
    	  
    	  memberservice.memberJoin(member);
         
         return "redirect:/main";
      }
   
      
   //로그인 페이지 이동
   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public void joinGET() {
      
      logger.info("로그인 페이지 진입");
      
   }
   
   //아이디 중복 검사
   @RequestMapping(value = "/memberIdChk",method=RequestMethod.POST)
   @ResponseBody
   public String memberIdChkPOST(String memberId) throws Exception{
      logger.info("memberIdChk() 진입");
      System.out.println("gd");
      int result = memberservice.idCheck(memberId);
      
      logger.info("결과 값 = "+result);
      
      if(result != 0) {
         return "fail";
      } else {
         return "success";
      }
   }//memberIdChkPOST()종료
   

   @RequestMapping(value="/mailCheck",method=RequestMethod.GET)
   @ResponseBody
   public String mailCheckGET(String email) throws Exception{
      logger.info("이메일 데이터 전송 확인");
      logger.info("인증번호 : "+email);
      
      Random random = new Random();
      int checkNum = random.nextInt(888888)+111111;
      logger.info("인증번호"+checkNum);
      
      /*이메일 보내기*/
      String setFrom = "cjycocoho@naver.com";
      String toMail = email;
      String title="회원가입 인증 이메일입니다.";
      String content=
            "홈페이지를 방문해주셔서 감사합니다."+"<br><br>"+
            "인증번호는 "+checkNum+"입니다."+
            "<br>"+"해당 인증번호를 인증번호 확인란에 기입하여 주세요.";      
      
      try {
         
         MimeMessage message = mailSender.createMimeMessage();
         MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
         helper.setFrom(setFrom);
         helper.setTo(toMail);
         helper.setSubject(title);
         helper.setText(content,true);
         mailSender.send(message);
      } catch(Exception e)
      {
         e.printStackTrace();
      }
      
      String num = Integer.toString(checkNum);
      
      return num;
   }
   
   /* 로그인 */
   @RequestMapping(value="login.do", method=RequestMethod.POST)
   public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{
	   
	   HttpSession session = request.getSession();
	   String rawPw = "";
	   String encodePw = "";
	   
	   MemberVO lvo = memberservice.memberLogin(member); // 제출한아이디와 일치하는 아이디 있는지
	   
	   if(lvo != null) {	//일치하는 아이디 존재시
		   
		   rawPw = member.getMemberPw(); // 사용자가 제출한 비밀번호
		   encodePw = lvo.getMemberPw(); // 데이터베이스에 저장한 인코딩된 비밀번호
		   
		   if(true == pwEncoder.matches(rawPw, encodePw)) { // 비밀번호 일치여부 판단 
			   
			   lvo.setMemberPw("");	// 인코딩된 비밀번호 정보 지움
			   session.setAttribute("member", lvo);	//session에 사용자의 정보 저장
			   return "redirect:/main"; //메인페이지 이동
			   
			   
		   } else {
			   
			   rttr.addFlashAttribute("result", 0);
			   return "redirect:/member/login"; //로그인 페이지로 이동
			   
		   }
		   
		   
	   } else { //일치하는 아이디가 존재하지 않을 시 (로그인 실패)
		   
		   rttr.addFlashAttribute("result", 0);
		   return "redirect:/member/login"; // 로그인 페이지로 이동
		   
	   }
	   
   }

   /* 메인페이지 로그아웃 */
   @RequestMapping(value="logout.do", method=RequestMethod.GET)
   public String logoutMainGET(HttpServletRequest request) throws Exception{
	   logger.info("logoutMainGET메서드 진입");
       
       HttpSession session = request.getSession();
       
       session.invalidate();
       
       return "redirect:/main";   
   }

   
   /* 비동기방식 로그아웃 메서드 */
   @RequestMapping(value="logout.do", method=RequestMethod.POST)
   @ResponseBody
   public void logoutPOST(HttpServletRequest request) throws Exception{
       
       logger.info("비동기 로그아웃 메서드 진입");
       
       HttpSession session = request.getSession();
       
       session.invalidate();
       
   }
   
   //이메일 전송
   /*
   @RequestMapping(value="/sendMail", method= RequestMethod.GET)
   public void sendMailTest() throws Exception{
      String subject = "test메일";
      String content = "메일 테스트 내용" + "<img src=\"이미지 경로\">";
      String from ="anszl123@naver.com";
      String to = "anszl789@naver.com";
      
      try {
         MimeMessage mail = mailSender.createMimeMessage();
         MimeMessageHelper mailHelper = new MimeMessageHelper(mail,true,"UTF-8");
         
         mailHelper.setFrom(from);
         
         mailHelper.setTo(to);
         mailHelper.setSubject(subject);
         mailHelper.setText(content,true);
         
         FileSystemResource file =new FileSystemResource(new File("D:\\test.txt"));
         mailHelper.addAttachment("업로드파일.형식", file);
         
         
         mailSender.send(mail);
      } catch(Exception e)
      {
         e.printStackTrace();
      }
   }
   
   @RequestMapping(value="/sendMail",method=RequestMethod.GET)
   public void sendMailTest2() throws Exception {
      String subject = "test메일";
      String content = "스프링 프젝 이메일 전송";
      String from ="anszl123@naver.com";
      String to = "anszl789@naver.com";
      
      final MimeMessagePreparator preparator = new MimeMessagePreparator() {
         
         @Override
         public void prepare(MimeMessage mimeMessage) throws Exception {
            // TODO Auto-generated method stub
            final MimeMessageHelper mailHelper = new MimeMessageHelper(mimeMessage,true,"UTF-8");
            
            mailHelper.setFrom(from);
            mailHelper.setTo(to);
            mailHelper.setSubject(subject);
            mailHelper.setText(content,true);
         }
      };
      
      try {
         mailSender.send(preparator);
      } catch(Exception e)
      {
         e.printStackTrace();
      }
      
   }
   */
   
}