package com.ybsx.controller.tool;

import java.io.File;
import java.nio.file.Files;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ybsx.base.ResultBody;
import com.ybsx.base.yml.YmlConfig;
import com.ybsx.util.SeaweedUtil;

/**
 * 文件控制器
 * @author zhouKai
 * @createDate 2017年12月4日 下午5:43:57
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {


	@Autowired
	private YmlConfig ymlConfig;
	
	/**
	 * 上传文件
	 * @param multipartFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResultBody<?> upload(HttpServletRequest request) throws Exception {
		File dir = new File(ymlConfig.tempDir);
		if (!dir.exists()) {
			dir.mkdir();
		}
		Part part = request.getPart("file");
		File tempFile = new File(dir, System.currentTimeMillis() + "_" + part.getSubmittedFileName());
		Files.copy(part.getInputStream(), tempFile.toPath());
		String fid = SeaweedUtil.saveFile(tempFile);
		tempFile.delete();
		return new ResultBody<>(ymlConfig.seaweed.publicUrl + "/" + fid);
	}
	

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResultBody<?> delete(String fid) throws Exception {
		SeaweedUtil.deleteByFid(fid);
		return new ResultBody<>();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
