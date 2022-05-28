package com.ptit.Elearning.Repository;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Timeline;
import com.ptit.Elearning.Entity.TimelineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, TimelineId> {
    public List<Timeline> findByCreditClass(CreditClass creditClass);
    public List<Timeline> findByTimelineIdCreditClassId(Long id);
    public Optional<Timeline> findByTimelineId(TimelineId timelineId);
}
