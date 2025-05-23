/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package org.vivi.framework.ureport.demo.core.chart.option.impl;


import org.vivi.framework.ureport.demo.core.chart.option.Labels;
import org.vivi.framework.ureport.demo.core.chart.option.Option;
import org.vivi.framework.ureport.demo.core.chart.option.Position;


public class LegendOption implements Option {
    private boolean display = true;
    private Position position = Position.top;
    private Labels labels;

    @Override
    public String buildOptionJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"legend\":{");
        sb.append("\"display\":" + display + ",");
        sb.append("\"position\":\"" + position + "\"");
        if (labels != null) {
            sb.append("\"labels\":" + labels.toJson());
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String getType() {
        return "legend";
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Labels getLabels() {
        return labels;
    }

    public void setLabels(Labels labels) {
        this.labels = labels;
    }
}
