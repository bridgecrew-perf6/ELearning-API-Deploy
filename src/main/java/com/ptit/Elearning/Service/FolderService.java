package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Folder;

import java.util.List;

public interface FolderService {
    public Folder createNewFolder(CreditClass creditClass, String folderName, Folder parents) throws IllegalAccessException;
    public Folder createNewFolderWithoutParents(CreditClass creditClass, String folderName);
    public List<Folder> getFolderByCreditClass(CreditClass creditClass);
    public Folder getFolderById(Long folderId);

    Folder updateFolderName(Folder folder);
    public boolean deleteFolder(Folder folder);
}
