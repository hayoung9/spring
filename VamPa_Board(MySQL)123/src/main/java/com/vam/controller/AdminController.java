package com.vam.controller;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vam.model.AuthorVO;
import com.vam.model.BookVO;
import com.vam.model.Criteria;
import com.vam.model.PageDTO;
import com.vam.service.AdminService;
import com.vam.service.AuthorService;

import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
public class AdminController {
   
   private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminController.class);

   @Autowired
   private AuthorService authorService;
   
   @Autowired
   private AdminService adminService;
   
   /*������ ���� ������ �̵�*/
   @RequestMapping(value="main", method=RequestMethod.GET)
   public void adminMainGET() throws Exception{
      logger.info("������ ������ �̵�");
   }
   
   @RequestMapping(value="authorEnroll.do",method=RequestMethod.POST)
   public String authorEnrollStringPOST(AuthorVO author, RedirectAttributes rttr) throws Exception{
      logger.info("authorEnroll : "+author);
      
      authorService.authorEnroll(author);
      
      rttr.addFlashAttribute("enroll_result",author.getAuthorName());
      
      return "redirect:/admin/authorManage";
   }
   
   /* ��ǰ ����(��ǰ���) ������ ���� */
    @RequestMapping(value = "goodsManage", method = RequestMethod.GET)
    public void goodsManageGET(Criteria cri,Model model) throws Exception{
        logger.info("��ǰ ����(��ǰ���) ������ ����999999");
        
        List list = adminService.goodsGetList(cri);
        
        if(!list.isEmpty())
        {
           model.addAttribute("list",list);
        } else {
           model.addAttribute("listCheck","empty");
           return;
        }
        
        //������ �������̽� ������
        model.addAttribute("pageMaker",new PageDTO(cri,adminService.goodsGetTotal(cri)));
       
    }
    
    /* ��ǰ ��� ������ ���� */
    @RequestMapping(value = "goodsEnroll", method = RequestMethod.GET)
    public void goodsEnrollGET(Model model) throws Exception{
        logger.info("��ǰ ��� ������ ����");
        ObjectMapper objm = new ObjectMapper();
        
        List list = adminService.cateList();
        
        String cateList = objm.writeValueAsString(list);
        
        model.addAttribute("cateList", cateList);
        
        logger.info("���� ��.........." + list);
      logger.info("���� ��.........." + cateList);
    }
    
    //��ǰ ��ȸ ������
    @GetMapping("/goodsDetail")
    public void goodsGetInfoGET(int bookId, Criteria cri, Model model)
    {
       logger.info("goodsGetInfo()...."+bookId);
       
       //��� ������ ���� ����
       model.addAttribute("cri",cri);
       
       //��ȸ ������ ����
      // model.addAttribute("goodsInfo",adminService.goodsGetDetail(bookId));
    }
  
    
    /* �۰� ��� ������ ���� */
    @RequestMapping(value = "authorEnroll", method = RequestMethod.GET)
    public void authorEnrollGET() throws Exception{
        logger.info("�۰� ��� ������ ����");
    }
    
    /* �۰� ���� ������ ���� */
    @RequestMapping(value = "authorManage", method = RequestMethod.GET)
    public void authorManageGET(Criteria cri,Model model) throws Exception{
        logger.info("�۰� ���� ������ ����........"+cri);
        
        List list = authorService.authorGetList(cri);
        
        if(!list.isEmpty()) {
         model.addAttribute("list",list);   // �۰� ���� ���
      } else {
         model.addAttribute("listCheck", "empty");   // �۰� �������� ���� ���
      }
//        model.addAttribute("list",list);
        
//        int total = authorService.authorGetTotal(cri);
//        PageDTO pageMaker = new PageDTO(cri, total);
//        
//        model.addAttribute("pageMaker",pageMaker);
        
        /* ������ �̵� �������̽� ������ */
          model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri)));
    } 
    
    @GetMapping({"/authorDetail","/authorModify"})
    public void authorGetInfoGET(int authorId, Criteria cri, Model model) throws Exception{
       logger.info("authorDetail...."+authorId);
       
       //�۰� ���� ������ ����
       model.addAttribute("cri",cri);
       
       //���� �۰� ����
       model.addAttribute("authorInfo",authorService.authorGetDetail(authorId));
    }
    
    
    //�۰� ���� ����
    @PostMapping("/authorModify")
    public String authorModifyPOST(AuthorVO author,RedirectAttributes rttr) throws Exception{
       logger.info("authorModifyPOST...."+author);
       
       int result = authorService.authorModify(author);
       
       rttr.addFlashAttribute("modify_result",result);
       return "redirect:/admin/authorManage";
    }
    
    //��ǰ���
    @PostMapping("/goodsEnroll")
    public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) {
       logger.info("goodsEnrollPOST...."+book);
       
       adminService.bookEnroll(book);
       
       rttr.addFlashAttribute("enroll_result",book.getBookName());
       
       return "redirect:/admin/goodsManage";
       
    }
    
    
    //�۰� �˻� �˾�â
    @GetMapping("/authorPop")
    public void authorPopGET(Criteria cri, Model model) throws Exception{
       logger.info("authorPopGET......");
       
       cri.setAmount(5);
       
       List list = authorService.authorGetList(cri);
       
       if(!list.isEmpty()) {
          model.addAttribute("list",list);
       } else {
          model.addAttribute("listCheck","empty");
       }
       
       model.addAttribute("pageMaker",new PageDTO(cri,authorService.authorGetTotal(cri)));
    }
}