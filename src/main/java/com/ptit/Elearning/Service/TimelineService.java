package com.ptit.Elearning.Service;

import com.ptit.Elearning.Entity.CreditClass;
import com.ptit.Elearning.Entity.Timeline;
import com.ptit.Elearning.Entity.TimelineId;

import java.util.List;

public interface TimelineService {
    public List<Timeline> getByCreditClass(CreditClass creditClass);
    public List<Timeline> getByCreditClass(Long creditClassiId);
    public Timeline createNewTimeline(Timeline timeline);
    public Timeline getById(TimelineId timelineId);
    public Timeline updateById(Timeline timelineOld, Timeline timeLineUpdate);
}
