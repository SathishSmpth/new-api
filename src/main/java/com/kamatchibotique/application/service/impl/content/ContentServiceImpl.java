package com.kamatchibotique.application.service.impl.content;

import com.kamatchibotique.application.cms.content.StaticContentFileManager;
import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.enums.content.ContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.Content;
import com.kamatchibotique.application.model.content.ContentDescription;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import com.kamatchibotique.application.repository.content.ContentRepository;
import com.kamatchibotique.application.repository.content.PageContentRepository;
import com.kamatchibotique.application.service.services.content.ContentService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

@AllArgsConstructor
@Service("contentService")
public class ContentServiceImpl implements ContentService {

    private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

    private final ContentRepository contentRepository;

    @Autowired
    private PageContentRepository pageContentRepository;


    @Autowired
    StaticContentFileManager contentFileManager;

    @Override
    public List<Content> listByType(ContentType contentType, MerchantStore store, Language language)
            throws ServiceException {

        return contentRepository.findByType(contentType, store.getId(), language.getId());
    }

    @Override
    public void delete(Content content) throws ServiceException {
        Content c = this.getById(content.getId());
        contentRepository.delete(c);
    }

    @Override
    public Content getByLanguage(Long id, Language language) throws ServiceException {
        return contentRepository.findByIdAndLanguage(id, language.getId());
    }

    @Override
    public List<Content> listByType(List<ContentType> contentType, MerchantStore store, Language language)
            throws ServiceException {
        return contentRepository.findByTypes(contentType, store.getId(), language.getId());
    }

    @Override
    public List<ContentDescription> listNameByType(List<ContentType> contentType, MerchantStore store,
                                                   Language language) throws ServiceException {

        return contentRepository.listNameByType(contentType, store, language);
    }

    @Override
    public List<Content> listByType(List<ContentType> contentType, MerchantStore store) throws ServiceException {

        return contentRepository.findByTypes(contentType, store.getId());
    }

    @Override
    public Content getByCode(String code, MerchantStore store) throws ServiceException {

        return contentRepository.findByCode(code, store.getId());

    }

    @Override
    public Content getById(Long id) {
        return contentRepository.findOne(id);
    }

    @Override
    public void saveOrUpdate(final Content content) throws ServiceException {
        if (content.getId() != null && content.getId() > 0) {
            contentRepository.save(content);
        } else {
            contentRepository.save(content);
        }
    }

    @Override
    public Content getByCode(String code, MerchantStore store, Language language) throws ServiceException {
        return contentRepository.findByCode(code, store.getId(), language.getId());
    }

    @Override
    public void addContentFile(String merchantStoreCode, InputContentFile contentFile) throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
        Assert.notNull(contentFile, "InputContentFile image can not be null");
        Assert.notNull(contentFile.getFileName(), "InputContentFile.fileName can not be null");
        Assert.notNull(contentFile.getFileContentType(), "InputContentFile.fileContentType can not be null");

        String mimeType = URLConnection.guessContentTypeFromName(contentFile.getFileName());
        contentFile.setMimeType(mimeType);

        if (contentFile.getFileContentType().name().equals(FileContentType.IMAGE.name())
                || contentFile.getFileContentType().name().equals(FileContentType.STATIC_FILE.name())) {
            addFile(merchantStoreCode, contentFile);
        } else if (contentFile.getFileContentType().name().equals(FileContentType.API_IMAGE.name())) {
            contentFile.setFileContentType(FileContentType.IMAGE);
            addImage(merchantStoreCode, contentFile);
        } else if (contentFile.getFileContentType().name().equals(FileContentType.API_FILE.name())) {
            contentFile.setFileContentType(FileContentType.STATIC_FILE);
            addFile(merchantStoreCode, contentFile);
        } else {
            addImage(merchantStoreCode, contentFile);
        }

    }

    @Override
    public void addLogo(String merchantStoreCode, InputContentFile cmsContentImage) throws ServiceException {

        Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
        Assert.notNull(cmsContentImage, "CMSContent image can not be null");

        cmsContentImage.setFileContentType(FileContentType.LOGO);
        addImage(merchantStoreCode, cmsContentImage);

    }

    @Override
    public void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage) throws ServiceException {

        Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
        Assert.notNull(cmsContentImage, "CMSContent image can not be null");
        cmsContentImage.setFileContentType(FileContentType.PROPERTY);
        addImage(merchantStoreCode, cmsContentImage);

    }

    private void addImage(String merchantStoreCode, InputContentFile contentImage) throws ServiceException {

        try {
            LOG.info("Adding content image for merchant id {}", merchantStoreCode);

            String p = contentImage.getPath();
            Optional<String> path = Optional.ofNullable(p);
            contentFileManager.addFile(merchantStoreCode, path, contentImage);

        } catch (Exception e) {
            LOG.error("Error while trying to convert input stream to buffered image", e);
            throw new ServiceException(e);

        } finally {

            try {
                if (contentImage.getFile() != null) {
                    contentImage.getFile().close();
                }
            } catch (Exception ignore) {
            }

        }

    }

    private void addFile(final String merchantStoreCode, InputContentFile contentImage) throws ServiceException {

        try {
            LOG.info("Adding content file for merchant id {}", merchantStoreCode);
            // staticContentFileManager.addFile(merchantStoreCode,
            // contentImage);

            String p = null;
            Optional<String> path = Optional.ofNullable(p);

            contentFileManager.addFile(merchantStoreCode, path, contentImage);

        } catch (Exception e) {
            LOG.error("Error while trying to convert input stream to buffered image", e);
            throw new ServiceException(e);

        } finally {

            try {
                if (contentImage.getFile() != null) {
                    contentImage.getFile().close();
                }
            } catch (Exception ignore) {
            }
        }

    }

    @Override
    public void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList)
            throws ServiceException {

        Assert.notNull(merchantStoreCode, "Merchant store ID can not be null");
        Assert.notEmpty(contentFilesList, "File list can not be empty");
        LOG.info("Adding total {} images for given merchant", contentFilesList.size());

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        LOG.info("Adding content images for merchant....");
        contentFileManager.addFiles(merchantStoreCode, path, contentFilesList);

        try {
            for (InputContentFile file : contentFilesList) {
                if (file.getFile() != null) {
                    file.getFile().close();
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
            throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant Store Id can not be null");
        Assert.notNull(fileContentType, "Content file type can not be null");
        Assert.notNull(fileName, "Content Image type can not be null");

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        contentFileManager.removeFile(merchantStoreCode, fileContentType, fileName, path);
    }

    @Override
    public void removeFile(String storeCode, String fileName) throws ServiceException {

        String fileType = "IMAGE";
        String mimetype = URLConnection.guessContentTypeFromName(fileName);
        String type = mimetype.split("/")[0];
        if (!type.equals("image"))
            fileType = "STATIC_FILE";

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        contentFileManager.removeFile(storeCode, FileContentType.valueOf(fileType), fileName, path);

    }

    @Override
    public void removeFiles(String merchantStoreCode) throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant Store Id can not be null");

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        contentFileManager.removeFiles(merchantStoreCode, path);
    }

    @Override
    public OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
            throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant store ID can not be null");
        Assert.notNull(fileName, "File name can not be null");

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        return contentFileManager.getFile(merchantStoreCode, path, fileContentType, fileName);

    }

    @Override
    public List<OutputContentFile> getContentFiles(String merchantStoreCode, FileContentType fileContentType)
            throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");
        // return staticContentFileManager.getFiles(merchantStoreCode,
        // fileContentType);
        String p = null;
        Optional<String> path = Optional.ofNullable(p);
        return contentFileManager.getFiles(merchantStoreCode, path, fileContentType);
    }

    @Override
    public List<String> getContentFilesNames(String merchantStoreCode, FileContentType fileContentType)
            throws ServiceException {
        Assert.notNull(merchantStoreCode, "Merchant store Id can not be null");

        String p = null;
        Optional<String> path = Optional.ofNullable(p);

        return contentFileManager.getFileNames(merchantStoreCode, path, fileContentType);
    }

    @Override
    public ContentDescription getBySeUrl(MerchantStore store, String seUrl) {
        return contentRepository.getBySeUrl(store, seUrl);
    }

    @Override
    public List<Content> getByCodeLike(ContentType type, String codeLike, MerchantStore store, Language language) {
        return contentRepository.findByCodeLike(type, '%' + codeLike + '%', store.getId(), language.getId());
    }

    @Override
    public Content getById(Long id, MerchantStore store, Language language) throws ServiceException {

        Content content = contentRepository.findOne(id);

        if (content != null) {
            if (content.getMerchantStore().getId().intValue() != store.getId().intValue()) {
                return null;
            }
        }

        return content;
    }

    public Content getById(Long id, MerchantStore store) throws ServiceException {

        Content content = contentRepository.findOne(id);

        if (content != null) {
            if (content.getMerchantStore().getId().intValue() != store.getId().intValue()) {
                return null;
            }
        }

        return content;
    }

    @Override
    public void addFolder(MerchantStore store, Optional<String> path, String folderName) throws ServiceException {
       Objects.requireNonNull(store, "MerchantStore cannot be null");
       Objects.requireNonNull(folderName, "Folder name cannot be null");

        if (path.isPresent()) {
            if (!this.isValidLinuxDirectory(path.get())) {
                throw new ServiceException("Path format [" + path.get() + "] not a valid directory format");
            }
        }
        contentFileManager.addFolder(store.getCode(), folderName, path);


    }

    @Override
    public List<String> listFolders(MerchantStore store, Optional<String> path) throws ServiceException {
       Objects.requireNonNull(store, "MerchantStore cannot be null");

        return contentFileManager.listFolders(store.getCode(), path);
    }

    @Override
    public void removeFolder(MerchantStore store, Optional<String> path, String folderName) throws ServiceException {
       Objects.requireNonNull(store, "MerchantStore cannot be null");
       Objects.requireNonNull(folderName, "Folder name cannot be null");

        contentFileManager.removeFolder(store.getCode(), folderName, path);

    }

    public boolean isValidLinuxDirectory(String path) {
        Pattern linuxDirectoryPattern = Pattern.compile("^/|(/[a-zA-Z0-9_-]+)+$");
        return path != null && !path.trim().isEmpty() && linuxDirectoryPattern.matcher(path).matches();
    }

    @Override
    public void renameFile(String merchantStoreCode, FileContentType fileContentType, Optional<String> path,
                           String originalName, String newName) throws ServiceException {

        OutputContentFile file = contentFileManager.getFile(merchantStoreCode, path, fileContentType, originalName);

        if (file == null) {
            throw new ServiceException("File name [" + originalName + "] not found for merchant [" + merchantStoreCode + "]");
        }

        ByteArrayOutputStream os = file.getFile();
        InputStream is = new ByteArrayInputStream(os.toByteArray());

        //remove file
        contentFileManager.removeFile(merchantStoreCode, fileContentType, originalName, path);

        //recreate file
        InputContentFile inputFile = new InputContentFile();
        inputFile.setFileContentType(fileContentType);
        inputFile.setFileName(newName);
        inputFile.setMimeType(file.getMimeType());
        inputFile.setFile(is);

        contentFileManager.addFile(merchantStoreCode, path, inputFile);

    }

    @Override
    public Page<Content> listByType(ContentType contentType, MerchantStore store, int page, int count)
            throws ServiceException {
        Pageable pageRequest = PageRequest.of(page, count);
        return pageContentRepository.findByContentType(contentType, store.getId(), pageRequest);
    }

    @Override
    public Page<Content> listByType(ContentType contentType, MerchantStore store, Language language, int page,
                                    int count) throws ServiceException {
        Pageable pageRequest = PageRequest.of(page, count);
        return pageContentRepository.findByContentType(contentType, store.getId(), language.getId(), pageRequest);
    }

    @Override
    public boolean exists(String code, ContentType type, MerchantStore store) {
        Content c = contentRepository.findByCodeAndType(code, type, store.getId());
        return c != null ? true : false;
    }

}
