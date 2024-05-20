package com.kamatchibotique.application.model.content;

import com.kamatchibotique.application.enums.FileContentType;

public abstract class StaticContentFile extends ContentFile {
    private FileContentType fileContentType;

    public FileContentType getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(FileContentType fileContentType) {
        this.fileContentType = fileContentType;
    }
}
