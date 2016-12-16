package com.taotao.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.taotao.service.PictureService;
import com.taotao.utils.FtpUtil;
import com.taotao.utils.IDUtils;
@Service
public class PictureServiceImpl implements PictureService {

	@Value("${FTP_ADDRESS}")
	private String FTP_ADDRESS;
	@Value("${FTP_PORT}")
	private int FTP_PORT;
	@Value("${FTP_USERNAME}")
	private String FTP_USERNAME;
	@Value("${FTP_PASSWORD}")
	private String FTP_PASSWORD;
	@Value("${FTP_BASE_PATH}")
	private String FTP_BASE_PATH;
	@Value("${IMAGE_BASE_URL}")
	private String IMAGE_BASE_URL;
	
	@Override
	public Map uploadPicture(MultipartFile uploadFile) {
		Map map = new HashMap<>();
		try {
			//生成一个新的文件名
			//取原是文件名
			System.out.println(uploadFile);
			String oldName = uploadFile.getOriginalFilename();
			//生成新的文件名
			String newName = IDUtils.genImageName();
			System.out.println(oldName);
			newName = newName + oldName.substring(oldName.lastIndexOf("."));
			String imagePath = new DateTime().toString("yyyy/MM/dd");
			//图片上传
			System.out.println(FTP_ADDRESS+"-"+FTP_PORT+"-"+FTP_USERNAME+"-"+FTP_PASSWORD+"-"+FTP_BASE_PATH+"-"+imagePath);
			boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWORD, FTP_BASE_PATH, imagePath, newName, uploadFile.getInputStream());
			System.out.println(result);
			//返回结果
			if(!result){
				map.put("error", 1);
				map.put("massage", "文件上传失败");
				return map;
			}else{
				map.put("error", 0);
				map.put("massage", "文件上传成功");
				map.put("url", IMAGE_BASE_URL + imagePath + "/" + newName);
				return map;
			}
		} catch (IOException e) {
			e.printStackTrace();
			map.put("error", 1);
			map.put("massage", "文件上传发生异常");
			return map;
		}
	}
}
