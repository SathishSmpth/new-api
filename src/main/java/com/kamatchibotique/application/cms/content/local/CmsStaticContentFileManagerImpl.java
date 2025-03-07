/**
 * 
 */
package com.kamatchibotique.application.cms.content.local;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.kamatchibotique.application.cms.content.ContentAssetsManager;
import com.kamatchibotique.application.cms.impl.CMSManager;
import com.kamatchibotique.application.cms.impl.LocalCacheManagerImpl;
import com.kamatchibotique.application.enums.FileContentType;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kamatchibotique.application.constants.Constants;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;

public class CmsStaticContentFileManagerImpl implements ContentAssetsManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CmsStaticContentFileManagerImpl.class);
	private static CmsStaticContentFileManagerImpl fileManager = null;
	private static final String ROOT_NAME = "static";

	private static final String ROOT_CONTAINER = "files";

	private String rootName = ROOT_NAME;

	private LocalCacheManagerImpl cacheManager;

	@PostConstruct
	void init() {

		this.rootName = ((CMSManager) cacheManager).getRootName();
		LOGGER.info("init " + getClass().getName() + " setting root" + this.rootName);

	}

	public static CmsStaticContentFileManagerImpl getInstance() {

		if (fileManager == null) {
			fileManager = new CmsStaticContentFileManagerImpl();
		}

		return fileManager;

	}

	@Override
	public void addFile(final String merchantStoreCode, Optional<String> folderPath,
			final InputContentFile inputStaticContentData) throws ServiceException {
		try {

			// base path
			String rootPath = this.buildRootPath();
			Path confDir = Paths.get(rootPath);
			this.createDirectoryIfNorExist(confDir);

			// node path
			StringBuilder nodePath = new StringBuilder();
			nodePath.append(rootPath).append(merchantStoreCode);
			Path merchantPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(merchantPath);

			// file path
			nodePath.append(Constants.SLASH).append(inputStaticContentData.getFileContentType())
					.append(Constants.SLASH);
			Path dirPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(dirPath);
			nodePath.append(inputStaticContentData.getFileName());

			Path path = Paths.get(nodePath.toString());

			Files.copy(inputStaticContentData.getFile(), path, StandardCopyOption.REPLACE_EXISTING);

			LOGGER.info("Content data added successfully.");
		} catch (final Exception e) {
			LOGGER.error("Error while saving static content data", e);
			throw new ServiceException(e);

		}

	}

	@Override
	public void addFiles(final String merchantStoreCode, Optional<String> folderPath,
			final List<InputContentFile> inputStaticContentDataList) throws ServiceException {
		try {

			// base path
			String rootPath = this.buildRootPath();
			Path confDir = Paths.get(rootPath);
			this.createDirectoryIfNorExist(confDir);

			// node path
			StringBuilder nodePath = new StringBuilder();
			nodePath.append(rootPath).append(merchantStoreCode);
			Path merchantPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(merchantPath);

			for (final InputContentFile inputStaticContentData : inputStaticContentDataList) {

				// file path
				nodePath.append(Constants.SLASH).append(inputStaticContentData.getFileContentType())
						.append(Constants.SLASH);
				Path dirPath = Paths.get(nodePath.toString());
				this.createDirectoryIfNorExist(dirPath);

				// file creation
				nodePath.append(Constants.SLASH).append(inputStaticContentData.getFileName());

				Path path = Paths.get(nodePath.toString());

				Files.copy(inputStaticContentData.getFile(), path, StandardCopyOption.REPLACE_EXISTING);

			}

			LOGGER.info("Total {} files added successfully.", inputStaticContentDataList.size());

		} catch (final Exception e) {
			LOGGER.error("Error while saving content image", e);
			throw new ServiceException(e);

		}
	}

	@Override
	public OutputContentFile getFile(final String merchantStoreCode, Optional<String> folderPath,
									 final FileContentType fileContentType, final String contentFileName) throws ServiceException {

		throw new ServiceException("Not implemented for httpd image manager");

	}

	@Override
	public List<OutputContentFile> getFiles(final String merchantStoreCode, Optional<String> folderPath,
			final FileContentType staticContentType) throws ServiceException {

		throw new ServiceException("Not implemented for httpd image manager");

	}

	@Override
	public void removeFile(final String merchantStoreCode, final FileContentType staticContentType,
			final String fileName, Optional<String> folderPath) throws ServiceException {

		try {

			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append(buildRootPath()).append(Constants.SLASH).append(merchantStoreCode)
					.append(Constants.SLASH).append(staticContentType).append(Constants.SLASH).append(fileName);

			Path path = Paths.get(merchantPath.toString());

			Files.deleteIfExists(path);

		} catch (final Exception e) {
			LOGGER.error("Error while deleting files for {} merchant ", merchantStoreCode);
			throw new ServiceException(e);
		}

	}

	/**
	 * Removes the data in a given merchant node
	 */
	@Override
	public void removeFiles(final String merchantStoreCode, Optional<String> folderPath) throws ServiceException {

		LOGGER.debug("Removing all images for {} merchant ", merchantStoreCode);

		try {

			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append(buildRootPath()).append(Constants.SLASH).append(merchantStoreCode);

			Path path = Paths.get(merchantPath.toString());

			Files.deleteIfExists(path);

		} catch (final Exception e) {
			LOGGER.error("Error while deleting content image for {} merchant ", merchantStoreCode);
			throw new ServiceException(e);
		}

	}

	/**
	 * Queries the CMS to retrieve all static content files. Only the name of
	 * the file will be returned to the client
	 * 
	 * @param merchantStoreCode
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<String> getFileNames(final String merchantStoreCode, Optional<String> folderPath,
			final FileContentType staticContentType) throws ServiceException {

		try {

			StringBuilder merchantPath = new StringBuilder();
			merchantPath.append(buildRootPath()).append(merchantStoreCode).append(Constants.SLASH)
					.append(staticContentType);

			Path path = Paths.get(merchantPath.toString());

			List<String> fileNames = null;

			if (Files.exists(path)) {

				fileNames = new ArrayList<String>();
				try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)) {
					for (Path dirPath : directoryStream) {

						String fileName = dirPath.getFileName().toString();

						if (staticContentType.name().equals(FileContentType.IMAGE.name())) {
							// File f = new File(fileName);
							String mimetype = URLConnection.guessContentTypeFromName(fileName);
							// String mimetype= new
							// MimetypesFileTypeMap().getContentType(f);
							if (!StringUtils.isBlank(mimetype)) {
								String type = mimetype.split("/")[0];
								if (type.equals("image")) {
									fileNames.add(fileName);
								}
							}
							// fileNames.add(fileName);

						} else {
							fileNames.add(fileName);
						}

					}
				}

				return fileNames;
			}
		} catch (final Exception e) {
			LOGGER.error("Error while fetching file for {} merchant ", merchantStoreCode);
			throw new ServiceException(e);
		}
		return new ArrayList<>();
	}

	public void setRootName(String rootName) {
		this.rootName = rootName;
	}

	public String getRootName() {
		return rootName;
	}

	private String buildRootPath() {
		return new StringBuilder().append(getRootName()).append(Constants.SLASH).append(ROOT_CONTAINER)
				.append(Constants.SLASH).toString();

	}

	private void createDirectoryIfNorExist(Path path) throws IOException {

		if (Files.notExists(path)) {
			Files.createDirectory(path);
		}
	}

	public LocalCacheManagerImpl getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(LocalCacheManagerImpl cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public void addFolder(String merchantStoreCode, String folderName, Optional<String> folderPath)
			throws ServiceException {


		try {

			Path merchantPath = this.buildMerchantPath(merchantStoreCode);
			
			StringBuilder nodePath = new StringBuilder();
			if(folderPath.isPresent()) {
				nodePath
				.append(merchantPath.toString())
				.append(Constants.SLASH).append(folderPath.get()).append(Constants.SLASH);
			}
			// add folder
			nodePath.append(folderName);
			
			Path dirPath = Paths.get(nodePath.toString());
			this.createDirectoryIfNorExist(dirPath);

		} catch (IOException e) {
			LOGGER.error("Error while creating fiolder for {} merchant ", merchantStoreCode);
			throw new ServiceException(e);
		}

	}
	
	private Path buildMerchantPath(String merchantCode) throws IOException {
		
		String rootPath = this.buildRootPath();
		Path confDir = Paths.get(rootPath);
		
		// node path
		StringBuilder nodePath = new StringBuilder();
		nodePath
		.append(confDir.toString())
		.append(rootPath).append(merchantCode);
		Path merchantPath = Paths.get(nodePath.toString());
		this.createDirectoryIfNorExist(merchantPath);
		
		return merchantPath;
		
	}

	@Override
	public void removeFolder(String merchantStoreCode, String folderName, Optional<String> folderPath)
			throws ServiceException {

		//String rootPath = this.buildRootPath();
		try {
			
			Path merchantPath = this.buildMerchantPath(merchantStoreCode);
			StringBuilder nodePath = new StringBuilder();
			nodePath.append(merchantPath.toString()).append(Constants.SLASH);
			if(folderPath.isPresent()) {
				nodePath.append(folderPath.get()).append(Constants.SLASH);
			}
			
			nodePath.append(folderName);
			
			Path longPath = Paths.get(nodePath.toString());
			
			if (Files.exists(longPath)) {
				Files.delete(longPath);
			}
			
		} catch (IOException e) {
			LOGGER.error("Error while creating fiolder for {} merchant ", merchantStoreCode);
			throw new ServiceException(e);
		}

	}

	@Override
	public List<String> listFolders(String merchantStoreCode, Optional<String> folderPath) throws ServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CMSManager getCmsManager() {
	  return null;
	}

}
