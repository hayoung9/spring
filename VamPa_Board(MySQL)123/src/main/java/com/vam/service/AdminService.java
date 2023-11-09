package com.vam.service;

import java.util.List;

import com.vam.model.BookVO;
import com.vam.model.CateVO;
import com.vam.model.Criteria;

public interface AdminService {
	/* ��ǰ ��� */
	public void bookEnroll(BookVO book);	
	
	/* ī�װ� ����Ʈ */
	public List<CateVO> cateList();
	
	/* ��ǰ ����Ʈ */
	public List<BookVO> goodsGetList(Criteria cri);
	
	/* ��ǰ �� ���� */
	public int goodsGetTotal(Criteria cri);
}
