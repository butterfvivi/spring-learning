package org.vivi.framework.quartz.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.vivi.framework.quartz.entity.IJobLog;

@Getter
@AllArgsConstructor
public class JobLogEvent {

    private final IJobLog jobLog;
}
