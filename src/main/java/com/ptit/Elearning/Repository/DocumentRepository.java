package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document,Long> {

    public Optional<Document> findByDocumentId(Long documentId);
}
