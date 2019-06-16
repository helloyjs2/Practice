package com.test.util;
/*
 * =========================
 * 	MyUtil.java
 *  - �Խ��� ����¡ ó��
 *  ======================== 
 */

//���� ���� Ȯ���غ����� �ϴ� ����¡ ó�� ����� �پ��� ������� �Ѱ���(�׳��� ������� ���...)�̴�.
// �н��� ��ģ ����.. �� �߰����� ������ �����ϰ� Ȯ���Ű��, �ٸ� ����鵵 ã�ƺ��� �����ؾ� �Ѵ�.
public class MyUtil
{
//	�� ��ü ������ ���� ���ϴ� �޼ҵ�
//	numPerPage : �� �������� ǥ���� ������(�Խù�)�� ��
//	dataCount : ��ü ������(�Խù�) ��
	public int getPageCount(int numPerPage, int dataCount)
	{
		int pageCount = 0;

		pageCount = dataCount / numPerPage;

		if (dataCount % numPerPage != 0)
		{
			pageCount++;
		}

		return pageCount;

//	�� �������� 10���� �Խù��� ����� �� �� 32���� �Խù��� �������� �����ϱ� ���ؼ��� [32/10]�� ������ ������ ����� 3�� ���� �� �ִ�.
//	�׷��� �̶�, ������ 2���� �Խù��� ������ֱ� ���ؼ��� ������ �ϳ��� �� �ʿ��ϴ�.

	}

//	�� ����¡ ó�� ����� �޼ҵ�	
	// current_page : ���� ǥ���� ������
	// total_page : ��ü ������ ��
	// list_url : ��ũ�� ������ url
	public String pageIdnexList(int current_page, int total_page, String list_url)
	{
		// ���� ����¡�� ������ StringBuffer����
		StringBuffer strList = new StringBuffer();
		
		//����¡ ó�� �� �Խù� ����Ʈ �ϴ��� ���ڸ� 10���� �����ְڴ�.
		int numPerBlock = 10;
		//���� ������(�� �������� �������� ���ڰ� �޶����� �ϱ� ����...)
		int currentPageSetup;
		int page;
		// ���� ������ ���� ���� ó������ �̵��ϱ� ���� ����(�󸶸�ŭ �̵��ؾ� �ϴ���...)
		int n;
		
		// ����¡ ó���� ������ �ʿ����� ���� ���... (�����Ͱ� ���������ʰų� �������� ���� 1�������� ��ä��� ���)
		if (current_page == 0)
			return "";
		
		// ������ ��û�� ó���ϴ� �������� url����� ���Ͽ� ���� ó��		
		/*
		 *  - List.jsp + [?]
		 *  
		 *  - List.jsp?Ű1=��1 + [&]
		 */
		//��ũ url�� [?] ���� 
		// ������
		if (list_url.indexOf("?") != -1)
			list_url = list_url + "&";
		else
		// ������
			list_url = list_url + "?";
		// �˻��� ���� �����ϸ� �̹� request���� searchKey���� serachValue���� ����ִ� ��Ȳ�̹Ƿ�
		// [&]�� �ٿ��� �߰��� �־�� �Ѵ�.
		
		//currentPageSetup = ǥ���� ù ������ -1
		// ���� ���� �������� 5��������(currentPage = 5)
		// ����Ʈ �ϴܿ� ������ ������ ������ 10�̸� (numPerBlock = 10)
		// [5/10 = 0]... ���⿡ [*10] �ص� 0�̴�.
		// ������.. ���� �������� 11�������� (current_page=11)
		// [11/10 =1] �̸� ���⿡ [*10]�� �ϸ� 10�̴�.		
		// �׷��� currentPageSetup �� 10�� �Ǵ� ���̴�.
		currentPageSetup = (current_page / numPerBlock) * numPerBlock;
		
	
		// -- ���� �� ó������ (���� 72)
		// ���� �������� 20���������ٸ�(current_page=20)
		// [20/10==2]�̸�.. ���⿡ [*10]
		// �̷���Ȳ�̶�� �ٽ� 10�� ���� 10���� ����� �ش�.		
		if (current_page % numPerBlock == 0)
			currentPageSetup = currentPageSetup - numPerBlock;
		
		
		// �������� 11�̻��� �� ó��
		
		// 1������
		if ((total_page > numPerBlock) && (currentPageSetup > 0))
			strList.append("<a href='" + list_url + "pageNum=1'>1</a>");
		// --list_url�� ��ó���� �̹� ���� ��Ȳ�̱� ������...
		// [...?] ���� ���� [....?...&]�� �����̴�.
		// �̷� ���� ����� [...?pageNum=1] �̰ų�
		// [..&pageNum=1]�� �Ǵ� ��Ȳ�̴�.

		//prev
		n = current_page - numPerBlock;	// n : �ش� ��������ŭ ������ ���� ���� ���� 
		if ((total_page > numPerBlock) && (currentPageSetup > 0))
			strList.append(" <a href='" + list_url + "pageNum=" + n + "'>Prev</a>");
		//currentPageSetup �� 0���� ū ���� �̹� �������� 10�̻��̶�� �ǹ��̸� �� ��, ���� ������(current_page)�� 11 ������ �̻��� ���
		//Prev�� ���̱� ���� ����
		//prev�� Ŭ�� �� ���
		// n ���� �������� �̵��ϴµ� 12���� prev�� ��� 2�������� �ǰ�, 22���� Prev�� ��� 12 �������� �� �� �ֵ��� ó���ϴ� ����̴�.
		
		
		// �ٷΰ���
		page = currentPageSetup + 1;
		// [+1]�� �ϴ� ������ �տ��� currentPageSetup ���� 10�� �����Դٸ� 10���� ���ڰ��� ���� �ƴ϶�
		// �ٷΰ��� �������� 11���� �����ؾ� �ϱ� �����̴�.

		while ((page <= total_page) && (page <= currentPageSetup + numPerBlock))
		{// ���� ������ ��Ÿ�� ���̱�
			if (page == current_page)
			{// �ӹ��� �ִ� �������� �����۸�ũ�� �ɷ����� �ʴ�.
				strList.append("<span style='color:orage; font-weight:bold;'>" + page + "</span>");
			} else
				strList.append(" <a href='" + list_url + "pageNum=" + page + "'>" + page + "</a>");

			page++;
		}
		
		//Next
		n = current_page + numPerBlock;
		
		if ((total_page - currentPageSetup) > numPerBlock)
		{
			strList.append(" <a href='" + list_url + "pageNum=" + n + "'>Next</a>");
		}
		
		// ������ ������
		if ((total_page > numPerBlock) && (currentPageSetup + numPerBlock) < total_page)
		{
			strList.append(" <a href='" + list_url + "pageNum=" + total_page +"'>"+total_page+ "</a>");

		}

		return strList.toString();

	}
}
