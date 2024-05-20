package com.kamatchibotique.application.service.services.content;

import com.kamatchibotique.application.enums.FileContentType;
import com.kamatchibotique.application.enums.content.ContentType;
import com.kamatchibotique.application.exception.ServiceException;
import com.kamatchibotique.application.model.content.Content;
import com.kamatchibotique.application.model.content.ContentDescription;
import com.kamatchibotique.application.model.content.InputContentFile;
import com.kamatchibotique.application.model.content.OutputContentFile;
import com.kamatchibotique.application.model.merchant.MerchantStore;
import com.kamatchibotique.application.model.reference.language.Language;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ContentService {

    List<Content> listByType(ContentType contentType, MerchantStore store, Language language)
            throws ServiceException;

    List<Content> listByType(List<ContentType> contentType, MerchantStore store, Language language)
            throws ServiceException;

    Content getByCode(String code, MerchantStore store)
            throws ServiceException;

    void saveOrUpdate(Content content)
            throws ServiceException;

    boolean exists(String code, ContentType type, MerchantStore store);

    Content getByCode(String code, MerchantStore store, Language language)
            throws ServiceException;

    Content getById(Long id, MerchantStore store, Language language)
            throws ServiceException;

    Content getById(Long id, MerchantStore store) throws ServiceException;

    Content getById(Long id) throws ServiceException;

    void delete(Content content) throws ServiceException;

    void addContentFile(String merchantStoreCode, InputContentFile contentFile)
            throws ServiceException;

    void addContentFiles(String merchantStoreCode, List<InputContentFile> contentFilesList) throws ServiceException;

    void removeFile(String merchantStoreCode, FileContentType fileContentType, String fileName) throws ServiceException;

    void removeFile(String storeCode, String filename) throws ServiceException;

    void removeFiles(String merchantStoreCode) throws ServiceException;

    void renameFile(String merchantStoreCode, FileContentType fileContentType, Optional<String> path, String originalName, String newName) throws ServiceException;

    OutputContentFile getContentFile(String merchantStoreCode, FileContentType fileContentType, String fileName)
            throws ServiceException;

    List<OutputContentFile> getContentFiles(String merchantStoreCode, FileContentType fileContentType)
            throws ServiceException;


    List<String> getContentFilesNames(String merchantStoreCode,
                                      FileContentType fileContentType) throws ServiceException;

    void addLogo(String merchantStoreCode, InputContentFile cmsContentImage)
            throws ServiceException;

    void addOptionImage(String merchantStoreCode, InputContentFile cmsContentImage)
            throws ServiceException;


    List<Content> listByType(List<ContentType> contentType, MerchantStore store)
            throws ServiceException;

    Page<Content> listByType(ContentType contentType, MerchantStore store, int page, int count)
            throws ServiceException;

    Page<Content> listByType(ContentType contentType, MerchantStore store, Language language, int page, int count)
            throws ServiceException;

    List<ContentDescription> listNameByType(List<ContentType> contentType,
                                            MerchantStore store, Language language) throws ServiceException;

    Content getByLanguage(Long id, Language language) throws ServiceException;

    ContentDescription getBySeUrl(MerchantStore store, String seUrl);

    List<Content> getByCodeLike(ContentType type, String codeLike, MerchantStore store, Language language);

    void addFolder(MerchantStore store, Optional<String> path, String folderName) throws ServiceException;

    List<String> listFolders(MerchantStore store, Optional<String> path) throws ServiceException;

    void removeFolder(MerchantStore store, Optional<String> path, String folderName) throws ServiceException;
}
