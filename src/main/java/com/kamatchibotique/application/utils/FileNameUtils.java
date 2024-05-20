package com.kamatchibotique.application.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FileNameUtils {

	public boolean validFileName(String fileName) {
		// Check if the file name or its extension is empty
		if (StringUtils.isEmpty(FilenameUtils.getExtension(fileName)) || StringUtils.isEmpty(FilenameUtils.getBaseName(fileName))) {
			return false;
		}
		return true;
	}
}
