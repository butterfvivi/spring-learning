package org.vivi.framework.quartz.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Trigger;
import org.vivi.framework.quartz.entity.IJob;

@Getter
@AllArgsConstructor
public class IJobEvent {

    private final IJob job;

    private final Trigger trigger;

}
