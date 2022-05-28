package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.Document;
import com.ptit.Elearning.Entity.Folder;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {
    public Document saveDocument(MultipartFile file, Folder folder) throws Exception;
    public Document saveDocument(MultipartFile file) throws Exception;
    public Document updateDocument(Document document);
    public Document getDocument(Long documentId);
    public boolean deleteDocument(Document document);
}
