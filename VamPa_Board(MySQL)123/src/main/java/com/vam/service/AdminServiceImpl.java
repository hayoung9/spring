package com.vam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vam.mapper.AdminMapper;
import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AdminServiceImpl implements AdminService{
	@Autowired
	private AdminMapper adminMapper;	
	
	/* ��ǰ ��� */
	@Override
	public void bookEnroll(BookVO book) {
		
		log.info("(srevice)bookEnroll........");
		
		adminMapper.bookEnroll(book);
		
	}
	
	/* ī�װ� ����Ʈ */
	@Override
	public List<CateVO> cateList() {
		
		log.info("(service)cateList........");
		
		return adminMapper.cateList();
	}
	
	/* ��ǰ ����Ʈ */
	@Override
	public List<BookVO> goodsGetList(Criteria cri) {
		log.info("goodsGetTotalList()..........");
		return adminMapper.goodsGetList(cri);
	}

	/* ��ǰ �� ���� */
	public int goodsGetTotal(Criteria cri) {
		log.info("goodsGetTotal().........");
		return adminMapper.goodsGetTotal(cri);
	}
}
