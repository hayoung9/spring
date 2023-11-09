package com.vam.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vam.model.MemberVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class MemberMapperTests {
	
	@Autowired
	private MemberMapper membermapper; //MemberMapper.java �������̽� ������ ����
	
	//ȸ������ ���� �׽�Ʈ �޼���
	/*
	 * @Test public void memberJoin() throws Exception{ MemberVO member = new
	 * MemberVO();
	 * 
	 * member.setMemberId("spring_test"); //ȸ�� id member.setMemberPw("spring_test");
	 * //ȸ�� ��й�ȣ member.setMemberName("spring_test"); //ȸ�� �̸�
	 * member.setMemberMail("spring_test"); //ȸ�� ����
	 * member.setMemberAddr1("spring_test"); //ȸ�� �����ȣ
	 * member.setMemberAddr2("spring_test"); //ȸ�� �ּ�
	 * member.setMemberAddr3("spring_test"); //ȸ�� ���ּ�
	 * 
	 * membermapper.memberJoin(member); //���� �޼��� ���� }
	 */
	// ���̵� �ߺ��˻�
	
//	@Test
//public void memberIdChk() throws Exception{
//		String id = "admin"; // �����ϴ� ���̵�
//		String id2 = "test123"; // �������� �ʴ� ���̵�
//		membermapper.idCheck(id);
//		membermapper.idCheck(id2);
		
//	}
	
	// �α��� ���� mapper �޼��� �׽�Ʈ
	@Test
	public void memberLogin() throws Exception{
		
		MemberVO member = new MemberVO(); // MemberVO ���� ���� �� �ʱ�ȭ
		
		// �ùٸ� ���̵� ��� �Է°��
		member.setMemberId("t");
		member.setMemberPw("123");
		
		// �ùٸ��� ���� ���̵� ��� �Է°��
		//member.setMemberId("test1123");
		//member.setMemberPw("test13211321");
		
		membermapper.memberLogin(member);
		System.out.println("��� �� : " + membermapper.memberLogin(member));
		
	}
	
	
	
}
