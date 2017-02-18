package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchResult;
import com.taotao.search.service.SearchService;
@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;
	
	@Override
	public SearchResult search(String queryString, int page, int rows)
			throws Exception {
		//������ѯ����
		SolrQuery query = new SolrQuery();
		//���ò�ѯ����
		query.setQuery(queryString);
		//���÷�ҳ
		query.setStart((page - 1) * rows);
		query.setRows(rows);
		//����Ĭ��������
		query.set("df", "item_keywords");
		//���ø�����ʾ
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style=\"color:red\">");
		query.setHighlightSimplePost("</em>");
		//ִ�в�ѯ
		SearchResult searchResult = searchDao.search(query);
		//�����ѯ�����ҳ��
		long recordCount = searchResult.getRecordCount();
		long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}
		searchResult.setPageCount(pageCount);
		searchResult.setCurPage(page);
		
		return searchResult;
	}

}
