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
	// Connection ��ü�� ���� ������ ������ ���� ����
	// �������̽� ������ ������Ÿ���� ���ϴ� �Ӽ� ����
	private DataSource dataSource;
	
	// setter ����
	public void setDataSouce(DataSource dataSouce)
	{
		this.dataSource = dataSouce;
	}
	
	// IBoardDAO �������̽� �޼ҵ� �������̵�
	
	// �Խù� ��ȣ�� �ִ밪 ����
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

	
	// �Խù� �ۼ� -> ������ �Է�
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
	
	
	// DB �� ���ڵ��� ������ �������� �޼ҵ� ����
	// -> �˻� ����� �۾��Ͽ� �����ϰ� �� �޼ҵ�
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

	
	// Ư�� ������ �Խù� ����� �о���� �޼ҵ� ����
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

	
	// Ư�� �Խù� ��ȸ�� ���� ��ȸ Ƚ�� ���� �޼ҵ�
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

	
	// Ư���Խù�(����) �о���� �޼ҵ� ����
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
	
	
	// Ư�� �Խù� �����ϴ� �޼ҵ�
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

	
	// �ش� �Խù��� ������ �����ϴ� �޼ҵ� ����
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

	
	// ������ ������ �������� �޼ҵ� ����
	// ������ �Խù� ��ȣ ����
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

	
	// ������ �Խù� ��ȣ ����
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
