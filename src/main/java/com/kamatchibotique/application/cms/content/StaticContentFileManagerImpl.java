/**
 * 
 */
package com.kamatchibotique.application.cms.content;

import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.cms.impl.CMSManager;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;

public class StaticContentFileManagerImpl extends StaticContentFileManager {
	private static final long serialVersionUID = 1L;
	private FilePut uploadFile;
	private FileGet getFile;
	private FileRemove removeFile;
	private FolderRemove removeFolder;
	private FolderPut addFolder;
	private FolderList listFolder;

	@Override
	public void addFile(final String merchantStoreCode, Optional<String> path, final InputContentFile inputContentFile)
			throws ServiceException {
		uploadFile.addFile(merchantStoreCode, path, inputContentFile);

	}

	@Override
	public void addFiles(final String merchantStoreCode, Optional<String> path, final List<InputContentFile> inputStaticContentDataList)
			throws ServiceException {
		uploadFile.addFiles(merchantStoreCode, path, inputStaticContentDataList);
	}

	@Override
	public void removeFile(final String merchantStoreCode, final FileContentType staticContentType,
			final String fileName, Optional<String> path) throws ServiceException {
		removeFile.removeFile(merchantStoreCode, staticContentType, fileName, path);

	}

	@Override
	public OutputContentFile getFile(String merchantStoreCode, Optional<String> path, FileContentType fileContentType, String contentName)
			throws ServiceException {
		return getFile.getFile(merchantStoreCode, path, fileContentType, contentName);
	}

	@Override
	public List<String> getFileNames(String merchantStoreCode, Optional<String> path, FileContentType fileContentType)
			throws ServiceException {
		return getFile.getFileNames(merchantStoreCode, path, fileContentType);
	}

	@Override
	public List<OutputContentFile> getFiles(String merchantStoreCode, Optional<String> path, FileContentType fileContentType)
			throws ServiceException {
		return getFile.getFiles(merchantStoreCode, path, fileContentType);
	}

	@Override
	public void removeFiles(String merchantStoreCode, Optional<String> path) throws ServiceException {
		removeFile.removeFiles(merchantStoreCode, path);
	}

	public void setRemoveFile(FileRemove removeFile) {
		this.removeFile = removeFile;
	}

	public FileRemove getRemoveFile() {
		return removeFile;
	}

	public void setGetFile(FileGet getFile) {
		this.getFile = getFile;
	}

	public FileGet getGetFile() {
		return getFile;
	}

	public void setUploadFile(FilePut uploadFile) {
		this.uploadFile = uploadFile;
	}

	public FilePut getUploadFile() {
		return uploadFile;
	}

	@Override
	public void removeFolder(String merchantStoreCode, String folderName, Optional<String> path) throws ServiceException {
		this.removeFolder.removeFolder(merchantStoreCode, folderName, path);

	}

	@Override
	public void addFolder(String merchantStoreCode, String folderName, Optional<String> path) throws ServiceException {
		addFolder.addFolder(merchantStoreCode, folderName, path);
	}

	public FolderRemove getRemoveFolder() {
		return removeFolder;
	}

	public void setRemoveFolder(FolderRemove removeFolder) {
		this.removeFolder = removeFolder;
	}

	public FolderPut getAddFolder() {
		return addFolder;
	}

	public void setAddFolder(FolderPut addFolder) {
		this.addFolder = addFolder;
	}

	@Override
	public List<String> listFolders(String merchantStoreCode, Optional<String> path) throws ServiceException {
		return this.listFolder.listFolders(merchantStoreCode, path);
	}

	public FolderList getListFolder() {
		return listFolder;
	}

	public void setListFolder(FolderList listFolder) {
		this.listFolder = listFolder;
	}

	@Override
	public CMSManager getCmsManager() {
		return null;
	}

}
