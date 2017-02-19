package com.taotao.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.common.utils.JsonUtils;
import com.taotao.portal.pojo.TbItem;
import com.taotao.portal.pojo.TbItemDesc;
import com.taotao.portal.pojo.TbItemParamItem;
import com.taotao.portal.service.ItemService;
/**
 * 商品信息管理
 * @author chao
 *
 */
@Service
public class ItemServiceImpl implements ItemService {

	@Value("${ITEM_INFO_URL}")
	private String ITEM_INFO_URL;
	@Value("${REST_BASE_URL}")
	private String REST_BASE_URL;
	@Value("${ITEM_DESC_URL}")
	private String ITEM_DESC_URL;
	@Value("${ITEM_PARAM_URL}")
	private String ITEM_PARAM_URL;
	
	@Override
	public TbItem getItemById(Long itemId) {
		try {
			//通过http调用taotao-rest服务
			String json = HttpClientUtil.doGet(REST_BASE_URL+ITEM_INFO_URL+itemId);
			if(!StringUtils.isBlank(json)){
				TaotaoResult result = TaotaoResult.formatToPojo(json, TbItem.class);
				if(result.getStatus()==200){
					TbItem tbItem = (TbItem) result.getData();
					return tbItem;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 商品描述
	 */
	@Override
	public String getItemDescById(Long itemId) {
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL+ITEM_DESC_URL+itemId);
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemDesc.class);
			if(result.getStatus()==200){
				TbItemDesc itemDesc = (TbItemDesc) result.getData();
				return itemDesc.getItemDesc();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 商品规格
	 */
	@Override
	public String getItemParam(Long itemId) {
		try {
			String json = HttpClientUtil.doGet(REST_BASE_URL+ITEM_PARAM_URL+itemId);
			TaotaoResult result = TaotaoResult.formatToPojo(json, TbItemParamItem.class);
			if(result.getStatus()==200){
				TbItemParamItem tbItemParamItem = (TbItemParamItem) result.getData();
				String paramData = tbItemParamItem.getParamData();
				//生成html
				//把规格参数json数据转换成java对象
				List<Map> jsonList = JsonUtils.jsonToList(paramData, Map.class);
				StringBuffer sb = new StringBuffer();
				sb.append("<table cellpadding=\"0\" cellspacing=\"1\" width=\"100%\" border=\"0\" class=\"Ptable\">\n");
				sb.append("    <tbody>\n");
				for(Map m1:jsonList) {
					sb.append("        <tr>\n");
					sb.append("            <th class=\"tdTitle\" colspan=\"2\">"+m1.get("group")+"</th>\n");
					sb.append("        </tr>\n");
					List<Map> list2 = (List<Map>) m1.get("params");
					for(Map m2:list2) {
						sb.append("        <tr>\n");
						sb.append("            <td class=\"tdTitle\">"+m2.get("k")+"</td>\n");
						sb.append("            <td>"+m2.get("v")+"</td>\n");
						sb.append("        </tr>\n");
					}
				}
				sb.append("    </tbody>\n");
				sb.append("</table>");
				//返回html片段
				return sb.toString();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
