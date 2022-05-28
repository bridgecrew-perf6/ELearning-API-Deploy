package com.ptit.Elearning.ServiceImpl;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.TimelineId;
import com.ptit.Elearning.Exception.NotFoundException;
import com.ptit.Elearning.Repository.TimelineRepository;
import com.ptit.Elearning.Entity.Timeline;
import com.ptit.Elearning.Service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimelineServiceImpl implements TimelineService {
    @Autowired
    TimelineRepository timelineRepository;
    @Override
    public List<Timeline> getByCreditClass(CreditClass creditClass) {
        return timelineRepository.findByCreditClass(creditClass);
    }

    @Override
    public List<Timeline> getByCreditClass(Long creditClassiId) {
        return timelineRepository.findByTimelineIdCreditClassId(creditClassiId);
    }

    @Override
    public Timeline createNewTimeline(Timeline timeline) {
        return timelineRepository.save(timeline);
    }

    @Override
    public Timeline getById(TimelineId timelineId) {
        return timelineRepository.findByTimelineId(timelineId).orElseThrow(()->new NotFoundException("Could not find timeline"));
    }

    @Override
    public Timeline updateById(Timeline timelineOld, Timeline timeLineUpdate) {
        timelineRepository.delete(timelineOld);
        return timelineRepository.save(timeLineUpdate);
    }
}
