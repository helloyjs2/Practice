package com.test.mvc;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.test.sub.BoardDTO;
import com.test.sub.IBoardDAO;
import com.test.util.MyUtil;

public class ListController implements Controller
{
	private IBoardDAO dao;
	
	public void setDAO(IBoardDAO dao)
	{
		this.dao = dao;
	}

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ModelAndView mav = new ModelAndView();
		MyUtil myUtil = new MyUtil();
		String cp = request.getContextPath();
	
		request.setCharacterEncoding("UTF-8");
		
		/* �˻� Ű�� �˻� �� ���� */
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if (searchKey != null)
		{// �˻� ����� ���� �������� ��û�Ǿ��� ���
			/* �Ѿ�� ���� get����̶��...-> get�� �ѱ� ���ڿ��� ���ڵ��ؼ� ������ ������..  */
			if (request.getMethod().equalsIgnoreCase("GET"))
			{
				//���ڵ� ó��
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		} else
		{ // �⺻���� ������ ��û�� �̷������ ��� 
			searchKey = "subject";
			searchValue = "";
		}
		
		// �Ѿ�� ������ ��ȣ Ȯ��
		String pageNum = request.getParameter("pageNum");
		int current_page = 1; // ���� ǥ�õǾ�� �ϴ� ������
		if (pageNum != null)
			current_page = Integer.parseInt(pageNum);
		
		/* ��ü ������ ���� ���ϱ� */
		int dataCount = dao.getDataCount(searchKey, searchValue);
		
		/* ��ü �������� �������� �� ������ �� ��� */
		int numPerPage = 10; // �� �������� ǥ���� ������ ����
		int total_page = myUtil.getPageCount(numPerPage, dataCount);
		
		/* �����ͺ��̽����� ������ ���۰� �� ��ġ  */
		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;
		
		/* ����Ʈ �������� */
		List<BoardDTO> lists = dao.getList(start, end, searchKey, searchValue);
		
		/* �� ���뺸�� �ּ�  */
		String articleUrl = cp + "/Article.jsp";
		
		mav.setViewName("/WEB-INF/views/List.jsp");
		mav.addObject("cp", cp);
		mav.addObject("lists", lists);
		mav.addObject("articleUrl", articleUrl);
		mav.addObject("dataCount", dataCount);
		mav.addObject("cp", cp);
		mav.addObject("cp", cp);
		
		return mav;
	}

}
