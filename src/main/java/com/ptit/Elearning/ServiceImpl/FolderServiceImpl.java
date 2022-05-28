package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Folder;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.FolderRespository;
import com.ptit.Elearning.Service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    FolderRespository folderRespository;

    @Override
    public Folder createNewFolderWithoutParents(CreditClass creditClass, String folderName) {
        Folder folder = new Folder();
        folder.setCreditClass(creditClass);
        folder.setFolderName(folderName);
        folder.setUpTime(new Timestamp(System.currentTimeMillis()));

        return folderRespository.save(folder);
    }

    @Override
    public List<Folder> getFolderByCreditClass(CreditClass creditClass) {
        List<Folder> folders  = folderRespository.findByCreditClass(creditClass);
        return folders;
    }

    @Override
    public Folder getFolderById(Long folderId) {
        return folderRespository.findByFolderId(folderId).orElseThrow(()->new NotFoundException("Could not find folder with id: "+folderId));
    }

    @Override
    public Folder updateFolderName(Folder folder) {
        return folderRespository.save(folder);
    }

    @Override
    public boolean deleteFolder(Folder folder) {
        try{
            folderRespository.delete(folder);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Folder createNewFolder(CreditClass creditClass, String folderName, Folder parents) throws IllegalAccessException {
        //kiểm tra thử xem folder cha hiện tại có nằm trong lớp tín chỉ hay không?
        Folder current = parents;
        Folder folderTemp = current.getParentsFolder();
        while(folderTemp!=null){
            current = folderTemp;
            folderTemp = current.getParentsFolder();
        }
        if(current.getCreditClass().getCreditClassId()!=creditClass.getCreditClassId()){
            throw new IllegalAccessException("Folder is not in credit class");
        }
        Folder folder = new Folder();
        folder.setParentsFolder(parents);
        folder.setFolderName(folderName);
        folder.setUpTime(new Timestamp(System.currentTimeMillis()));
        folder.setCreditClass(creditClass);
        return folderRespository.save(folder);
    }
}
