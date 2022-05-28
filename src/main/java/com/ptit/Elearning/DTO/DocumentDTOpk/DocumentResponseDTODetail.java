package com.ptit.Elearning.DTO.DocumentDTOpk;

import java.sql.Timestamp;

public class DocumentResponseDTODetail {
    private Long documentId;
    private String documentName;
    private String fileType;
    private Timestamp createAt;

    public DocumentResponseDTODetail() {
    }

    public DocumentResponseDTODetail(Long documentId, String documentName, String fileType, Timestamp createAt) {
        this.documentId = documentId;
        this.documentName = documentName;
        this.fileType = fileType;
        this.createAt = createAt;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
