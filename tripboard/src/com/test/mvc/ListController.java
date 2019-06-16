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
		
		/* 검색 키와 검색 값 수신 */
		String searchKey = request.getParameter("searchKey");
		String searchValue = request.getParameter("searchValue");
		
		if (searchKey != null)
		{// 검색 기능을 통해 페이지가 요청되었을 경우
			/* 넘어온 값이 get방식이라면...-> get은 한글 문자열을 인코딩해서 보내기 때문에..  */
			if (request.getMethod().equalsIgnoreCase("GET"))
			{
				//디코딩 처리
				searchValue = URLDecoder.decode(searchValue, "UTF-8");
			}
		} else
		{ // 기본적인 페이지 요청이 이루어졌을 경우 
			searchKey = "subject";
			searchValue = "";
		}
		
		// 넘어온 페이지 번호 확인
		String pageNum = request.getParameter("pageNum");
		int current_page = 1; // 현재 표시되어야 하는 페이지
		if (pageNum != null)
			current_page = Integer.parseInt(pageNum);
		
		/* 전체 데이터 갯수 구하기 */
		int dataCount = dao.getDataCount(searchKey, searchValue);
		
		/* 전체 페이지를 기준으로 총 페이지 수 계산 */
		int numPerPage = 10; // 한 페이지에 표시할 데이터 갯수
		int total_page = myUtil.getPageCount(numPerPage, dataCount);
		
		/* 데이터베이스에서 가져올 시작과 끝 위치  */
		int start = (current_page - 1) * numPerPage + 1;
		int end = current_page * numPerPage;
		
		/* 리스트 가져오기 */
		List<BoardDTO> lists = dao.getList(start, end, searchKey, searchValue);
		
		/* 글 내용보기 주소  */
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
