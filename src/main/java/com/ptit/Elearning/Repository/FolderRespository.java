package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRespository extends JpaRepository<Folder,Long> {
        public List<Folder> findByCreditClass(CreditClass creditClass);
        public Optional<Folder> findByFolderId(Long folderId);
}
