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
   
   //ȸ������ ������ �̵�
   @RequestMapping(value = "join", method = RequestMethod.GET)
   public void loginGET() {
      
      logger.info("ȸ������ ������ ����");
      
   }
   
   //ȸ������
      @RequestMapping(value="/join",method=RequestMethod.POST)
      public String joinPOST(MemberVO member) throws Exception{
         
        // logger.info("join ����");
         
        // memberservice.memberJoin(member);
         
        // logger.info("join Service ����");
    	  
    	  String rawPw = "";	// ���ڵ� �� ��й�ȣ
    	  String encodePw = "";	// ���ڵ� �� ��й�ȣ
    	  
    	  rawPw = member.getMemberPw();	// ��й�ȣ ������ ���� 
    	  encodePw = pwEncoder.encode(rawPw); // ��й�ȣ ���ڵ�
    	  member.setMemberPw(encodePw); // ���ڵ��� ��й�ȣ member��ü�� �ٽ� ����
    	  
    	  memberservice.memberJoin(member);
         
         return "redirect:/main";
      }
   
      
   //�α��� ������ �̵�
   @RequestMapping(value = "/login", method = RequestMethod.GET)
   public void joinGET() {
      
      logger.info("�α��� ������ ����");
      
   }
   
   //���̵� �ߺ� �˻�
   @RequestMapping(value = "/memberIdChk",method=RequestMethod.POST)
   @ResponseBody
   public String memberIdChkPOST(String memberId) throws Exception{
      logger.info("memberIdChk() ����");
      System.out.println("gd");
      int result = memberservice.idCheck(memberId);
      
      logger.info("��� �� = "+result);
      
      if(result != 0) {
         return "fail";
      } else {
         return "success";
      }
   }//memberIdChkPOST()����
   

   @RequestMapping(value="/mailCheck",method=RequestMethod.GET)
   @ResponseBody
   public String mailCheckGET(String email) throws Exception{
      logger.info("�̸��� ������ ���� Ȯ��");
      logger.info("������ȣ : "+email);
      
      Random random = new Random();
      int checkNum = random.nextInt(888888)+111111;
      logger.info("������ȣ"+checkNum);
      
      /*�̸��� ������*/
      String setFrom = "cjycocoho@naver.com";
      String toMail = email;
      String title="ȸ������ ���� �̸����Դϴ�.";
      String content=
            "Ȩ�������� �湮���ּż� �����մϴ�."+"<br><br>"+
            "������ȣ�� "+checkNum+"�Դϴ�."+
            "<br>"+"�ش� ������ȣ�� ������ȣ Ȯ�ζ��� �����Ͽ� �ּ���.";      
      
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
   
   /* �α��� */
   @RequestMapping(value="login.do", method=RequestMethod.POST)
   public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{
	   
	   HttpSession session = request.getSession();
	   String rawPw = "";
	   String encodePw = "";
	   
	   MemberVO lvo = memberservice.memberLogin(member); // �����Ѿ��̵�� ��ġ�ϴ� ���̵� �ִ���
	   
	   if(lvo != null) {	//��ġ�ϴ� ���̵� �����
		   
		   rawPw = member.getMemberPw(); // ����ڰ� ������ ��й�ȣ
		   encodePw = lvo.getMemberPw(); // �����ͺ��̽��� ������ ���ڵ��� ��й�ȣ
		   
		   if(true == pwEncoder.matches(rawPw, encodePw)) { // ��й�ȣ ��ġ���� �Ǵ� 
			   
			   lvo.setMemberPw("");	// ���ڵ��� ��й�ȣ ���� ����
			   session.setAttribute("member", lvo);	//session�� ������� ���� ����
			   return "redirect:/main"; //���������� �̵�
			   
			   
		   } else {
			   
			   rttr.addFlashAttribute("result", 0);
			   return "redirect:/member/login"; //�α��� �������� �̵�
			   
		   }
		   
		   
	   } else { //��ġ�ϴ� ���̵� �������� ���� �� (�α��� ����)
		   
		   rttr.addFlashAttribute("result", 0);
		   return "redirect:/member/login"; // �α��� �������� �̵�
		   
	   }
	   
   }

   /* ���������� �α׾ƿ� */
   @RequestMapping(value="logout.do", method=RequestMethod.GET)
   public String logoutMainGET(HttpServletRequest request) throws Exception{
	   logger.info("logoutMainGET�޼��� ����");
       
       HttpSession session = request.getSession();
       
       session.invalidate();
       
       return "redirect:/main";   
   }

   
   /* �񵿱��� �α׾ƿ� �޼��� */
   @RequestMapping(value="logout.do", method=RequestMethod.POST)
   @ResponseBody
   public void logoutPOST(HttpServletRequest request) throws Exception{
       
       logger.info("�񵿱� �α׾ƿ� �޼��� ����");
       
       HttpSession session = request.getSession();
       
       session.invalidate();
       
   }
   
   //�̸��� ����
   /*
   @RequestMapping(value="/sendMail", method= RequestMethod.GET)
   public void sendMailTest() throws Exception{
      String subject = "test����";
      String content = "���� �׽�Ʈ ����" + "<img src=\"�̹��� ���\">";
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
         mailHelper.addAttachment("���ε�����.����", file);
         
         
         mailSender.send(mail);
      } catch(Exception e)
      {
         e.printStackTrace();
      }
   }
   
   @RequestMapping(value="/sendMail",method=RequestMethod.GET)
   public void sendMailTest2() throws Exception {
      String subject = "test����";
      String content = "������ ���� �̸��� ����";
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