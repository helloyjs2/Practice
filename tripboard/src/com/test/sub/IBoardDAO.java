package com.test.sub;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IBoardDAO
{
	// �Խù� ��ȣ�� �ִ밪 ����
	public int getMaxNum() throws SQLException;
	
	// �Խù� �ۼ�
	public int insertData(BoardDTO dto) throws SQLException;
	
	// DB �� ���ڵ��� ������ �������� �޼ҵ� ����
	// -> �˻� ����� �۾��Ͽ� �����ϰ� �� �޼ҵ�
	public int getDataCount(String searchKey, String seachValue);
	
	// Ư�� ������ �Խù� ����� �о���� �޼ҵ� ����
	public List<BoardDTO> getList(int start, int end, String searchKey, String searchValue);
	
	// Ư�� �Խù� ��ȸ�� ���� ��ȸ Ƚ�� ���� �޼ҵ�
	public int updateHitCount(int num);
	
	// Ư���Խù�(����) �о���� �޼ҵ� ����
	public BoardDTO getReadData(int num);
	
	// Ư�� �Խù� �����ϴ� �޼ҵ�
	public int deleteData(int num);
	
	// �ش� �Խù��� ������ �����ϴ� �޼ҵ� ����
	public int updateData(BoardDTO dto);
	
	// ������ ������ �������� �޼ҵ� ����
	// ������ �Խù� ��ȣ ����
	public int getBeforeNum(int num);
	
	// ������ �Խù� ��ȣ ����
	public int getNextNum(int num);
}
