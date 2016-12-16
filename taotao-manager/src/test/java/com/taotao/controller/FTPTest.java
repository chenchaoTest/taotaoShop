package com.taotao.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

public class FTPTest {

	@Test
	public void testFtpClient(){
		try {
			//创建一个FtpClient
			FTPClient ftpClient = new FTPClient();
			//创建ftp连接
			ftpClient.connect("172.16.20.29",9021);
			//登陆ftp服务器
			ftpClient.login("fenqifukuan", "SzEib9s9");
			//上传文件
			FileInputStream fileInputStream = new FileInputStream("G://鸿冠信息//测试数据//FQFK-308010702999689_20160105.csv");
			ftpClient.changeWorkingDirectory("/");
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.storeFile("111.cvs", fileInputStream);
			//断开连接
			fileInputStream.close();
			ftpClient.logout();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		}
	}
}
