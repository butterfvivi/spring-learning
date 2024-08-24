package org.vivi.framework.quartz.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.quartz.Trigger;

@Getter
@AllArgsConstructor
public class JobEvent {

    private final Job job;

    private final Trigger trigger;

}
