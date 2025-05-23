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
package org.vivi.framework.ureport.demo.core.chart.dataset.impl.category;


import org.vivi.framework.ureport.demo.core.build.Context;
import org.vivi.framework.ureport.demo.core.model.Cell;


public class RadarDataset extends CategoryDataset {
    private boolean fill = true;
    private double lineTension = 0.1;

    @Override
    public String buildDataJson(Context context, Cell cell) {
        String props = "\"fill\":" + fill + ",\"lineTension\":" + lineTension;
        String datasetJson = buildDatasetJson(context, cell, props);
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String labels = getLabels();
        sb.append("\"labels\":" + labels + ",");
        sb.append("\"datasets\":[" + datasetJson + "]");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String getType() {
        return "radar";
    }

    public boolean isFill() {
        return fill;
    }

    public void setFill(boolean fill) {
        this.fill = fill;
    }

    public double getLineTension() {
        return lineTension;
    }

    public void setLineTension(double lineTension) {
        this.lineTension = lineTension;
    }
}
