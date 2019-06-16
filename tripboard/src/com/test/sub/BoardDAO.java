package com.test.sub;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class BoardDAO implements IBoardDAO
{
	// Connection 객체에 대한 의존성 주입을 위한 주입
	// 인터페이스 형태의 데이터타입을 취하는 속성 구성
	private DataSource dataSource;
	
	// setter 구성
	public void setDataSouce(DataSource dataSouce)
	{
		this.dataSource = dataSouce;
	}
	
	// IBoardDAO 인터페이스 메소드 오버라이딩
	
	// 게시물 번호의 최대값 얻어내기
	@Override
	public int getMaxNum() throws SQLException
	{
		int result = 0;
		
		String sql = "SELECT NVL(MAX(NUM),0)AS MAXNUM FROM TBL_BOARD";

		Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next())
		{
			result = rs.getInt("MAXNUM");
		}

		rs.close();
		pstmt.close();
		
		return result;
	}

	
	// 게시물 작성 -> 데이터 입력
	@Override
	public int insertData(BoardDTO dto) throws SQLException
	{
		int result = 0;

		String sql = "INSERT INTO TBL_BOARD(NUM,NAME,PWD,EMAIL,SUBJECT,CONTENT,IPADDR,HITCOUNT,CREATED) VALUES(?,?,?,?,?,?,?,0,sysdate)";
		
		Connection conn = dataSource.getConnection();
		PreparedStatement pstmt = conn.prepareStatement(sql);
		
		pstmt.setInt(1, dto.getNum());
		pstmt.setString(2, dto.getName());
		pstmt.setString(3, dto.getPwd());
		pstmt.setString(4, dto.getEmail());
		pstmt.setString(5, dto.getSubject());
		pstmt.setString(6, dto.getContent());
		pstmt.setString(7, dto.getIpAddr());

		result = pstmt.executeUpdate();

		pstmt.close();

		return result;
	}
	
	
	// DB 의 레코드의 갯수를 가져오는 메소드 정의
	// -> 검색 기능을 작업하여 수정하게 될 메소드
	@Override
	public int getDataCount(String searchKey, String searchValue)
	{
		int result = 0;
		String sql = "select count(*) as count from tbl_board";
		
		Connection conn;
		
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		
		try
		{
			searchValue = "%" + searchValue + "%";
			sql += " where " + searchKey + " like ?";
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				result = rs.getInt("count");
			}

			rs.close();
			pstmt.close();

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return result;
	}

	
	// 특정 영역의 게시물 목록을 읽어오는 메소드 정의
	@Override
	public List<BoardDTO> getList(int start, int end, String searchKey, String searchValue)
	{
		List<BoardDTO> result = new ArrayList<BoardDTO>();
		searchValue = "%" + searchValue + "%";
		String sql = "select NUM,NAME,SUBJECT,HITCOUNT,CREATED from ( select rownum rnum,data.* from ( select num,name,subject,hitcount,to_char(created,'yyyy-mm-dd') AS created from tbl_board where "
				+ searchKey + " like ? order by num desc) data ) where rnum>=? and rnum <=?";
		
		Connection conn;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try
		{
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			rs = pstmt.executeQuery();

			while (rs.next())
			{
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt("NUM"));
				dto.setName(rs.getString("NAME"));
				dto.setSubject(rs.getString("SUBJECT"));
				dto.setHitCount(rs.getInt("HITCOUNT"));
				dto.setCreated(rs.getString("CREATED"));

				result.add(dto);
			}

		} catch (SQLException e)
		{
			e.printStackTrace();
		} finally
		{

			try
			{
				rs.close();
				pstmt.close();

			} catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}

		return result;

	}

	
	// 특정 게시물 조회에 따른 조회 횟수 증가 메소드
	@Override
	public int updateHitCount(int num)
	{
		int result = 0;

		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn;
		
		try
		{
			sql = "UPDATE TBL_BOARD SET HITCOUNT= HITCOUNT+1 WHERE NUM=?";
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		return result;
	}

	
	// 특정게시물(단일) 읽어오는 메소드 정의
	@Override
	public BoardDTO getReadData(int num)
	{
		BoardDTO result = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "";
		Connection conn;
		
		try
		{
			sql = "SELECT NUM,NAME,PWD,EMAIL,SUBJECT,CONTENT,IPADDR,HITCOUNT,CREATED FROM TBL_BOARD WHERE NUM =?";

			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);

			rs = pstmt.executeQuery();

			while (rs.next())
			{
				result = new BoardDTO();
				result.setNum(rs.getInt("NUM"));
				result.setName(rs.getString("NAME"));
				result.setPwd(rs.getString("PWD"));
				result.setEmail(rs.getString("EMAIL"));
				result.setSubject(rs.getString("SUBJECT"));
				result.setContent(rs.getString("CONTENT"));
				result.setIpAddr(rs.getString("IPADDR"));
				result.setHitCount(rs.getInt("HITCOUNT"));
				result.setCreated(rs.getString("CREATED"));

			}

			rs.close();
			pstmt.close();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}
	
	
	// 특정 게시물 삭제하는 메소드
	@Override
	public int deleteData(int num)
	{
		int result = 0;
		PreparedStatement pstmt = null;
		String sql = "";
		Connection conn;

		try
		{
			sql = "DELETE FROM TBL_BOARD WHERE NUM=?";
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();

			pstmt.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	
	// 해당 게시물의 내용을 수정하는 메소드 정의
	@Override
	public int updateData(BoardDTO dto)
	{
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn;

		try
		{
			conn = dataSource.getConnection();
			sql = "UPDATE TBL_BOARD SET NAME =?, PWD=?,EMAIL=?,SUBJECT=?,CONTENT=? WHERE NUM=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getPwd());
			pstmt.setString(3, dto.getEmail());
			pstmt.setString(4, dto.getSubject());
			pstmt.setString(5, dto.getContent());
			pstmt.setInt(6, dto.getNum());

			result = pstmt.executeUpdate();
			pstmt.close();

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	
	// 이전글 다음글 가져오는 메소드 정의
	// 이전글 게시물 번호 얻어내기
	@Override
	public int getBeforeNum(int num)
	{
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn;
		
		try
		{
			sql = "SELECT NVL(MAX(NUM),-1) BEFORENUM FROM TBL_BOARD WHERE NUM<?";
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				result = rs.getInt("BEFORENUM");
			}
			rs.close();
			pstmt.close();
		} catch (Exception E)
		{
			E.printStackTrace();
		}
		return result;
	}

	
	// 다음글 게시물 번호 얻어내기
	@Override
	public int getNextNum(int num)
	{
		int result = 0;
		String sql = "";
		PreparedStatement pstmt = null;
		Connection conn;
		
		try
		{
			sql = "SELECT NVL(MIN(NUM),-1) NEXTNUM FROM TBL_BOARD WHERE NUM>?";
			
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				result = rs.getInt("NEXTNUM");
			}
			rs.close();
			pstmt.close();
		} catch (Exception E)
		{
			E.printStackTrace();
		}
		return result;
	}
	
}
