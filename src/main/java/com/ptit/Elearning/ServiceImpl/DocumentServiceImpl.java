package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.Document;
import com.ptit.Elearning.Entity.Folder;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.DocumentRepository;
import com.ptit.Elearning.Repository.FolderRespository;
import com.ptit.Elearning.Service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.sql.Timestamp;
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    FolderRespository folderRespository;

    @Override
    public Document saveDocument(MultipartFile file, Folder folder) throws Exception {
        Document document = null;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){//any invalid character: throw exception!
                throw new Exception("File name contains invalid path sequence "+fileName);
            }
            if (isSupportedContentType(getFileExtension(fileName))==false) {
                throw new IllegalStateException("File must be an Image with type .jpg, .jpeg, .png .docx .xls .ppt");
            }
            document = new Document(fileName,file.getContentType(),file.getBytes(),new Timestamp(System.currentTimeMillis()));
            document.setFolder(folder);
            return documentRepository.save(document);
        }catch (Exception e){
            throw new Exception("Could not save file");
        }
    }

    @Override
    public Document saveDocument(MultipartFile file) throws Exception {
        Document document = null;
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try{
            if(fileName.contains("..")){//any invalid character: throw exception!
                throw new Exception("File name contains invalid path sequence "+fileName);
            }
            if (isSupportedContentType(getFileExtension(fileName))==false) {
                throw new IllegalStateException("File must be an Image with type .jpg, .jpeg, .png .docx .xls .ppt");
            }
            document = new Document(fileName,file.getContentType(),file.getBytes(),new Timestamp(System.currentTimeMillis()));
            return documentRepository.save(document);
        }catch (Exception e){
            throw new Exception("Could not save file");
        }
    }

    @Override
    public Document updateDocument(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document getDocument(Long documentId) {
        Document document = documentRepository.findByDocumentId(documentId)
                .orElseThrow(()-> new NotFoundException("Could not found with id: "+documentId));

        return document;
    }

    @Override
    @Transactional
    public boolean deleteDocument(Document document) {
        try{
            //ngay tại đây không thể dùng câu xóa bình thường, trường hợp này cực kỳ dặc biệt do ta dùng bidirectional
            Folder folder = document.getFolder();
            if(folder!=null)
            folder.getDocuments().removeIf(d -> d.getDocumentId() == document.getDocumentId());
            folderRespository.save(folder);

            //câu lệnh xóa bên dưới không thể xóa dưới database:
//            documentRepository.delete(document);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    private boolean isSupportedContentType(String contentType) {
        return (contentType.equals("png")
                || contentType.equals("jpg")
                || contentType.equals("jpeg")|| contentType.equals("doc")
                || contentType.equals("docx")
                || contentType.equals("xls") || contentType.equals("xlsx")
                || contentType.equals("ppt")|| contentType.equals("pptx")|| contentType.equals("pdf"));
    }
    private static String getFileExtension(String fullName) {

        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

}
