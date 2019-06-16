package com.test.sub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IBoardDAO
{
	// 게시물 번호의 최대값 얻어내기
	public int getMaxNum() throws SQLException;
	
	// 게시물 작성
	public int insertData(BoardDTO dto) throws SQLException;
	
	// DB 의 레코드의 갯수를 가져오는 메소드 정의
	// -> 검색 기능을 작업하여 수정하게 될 메소드
	public int getDataCount(String searchKey, String seachValue);
	
	// 특정 영역의 게시물 목록을 읽어오는 메소드 정의
	public List<BoardDTO> getList(int start, int end, String searchKey, String searchValue);
	
	// 특정 게시물 조회에 따른 조회 횟수 증가 메소드
	public int updateHitCount(int num);
	
	// 특정게시물(단일) 읽어오는 메소드 정의
	public BoardDTO getReadData(int num);
	
	// 특정 게시물 삭제하는 메소드
	public int deleteData(int num);
	
	// 해당 게시물의 내용을 수정하는 메소드 정의
	public int updateData(BoardDTO dto);
	
	// 이전글 다음글 가져오는 메소드 정의
	// 이전글 게시물 번호 얻어내기
	public int getBeforeNum(int num);
	
	// 다음글 게시물 번호 얻어내기
	public int getNextNum(int num);
}
