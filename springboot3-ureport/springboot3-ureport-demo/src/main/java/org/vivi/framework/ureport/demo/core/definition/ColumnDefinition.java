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
package org.vivi.framework.ureport.demo.core.definition;

import org.vivi.framework.ureport.demo.core.model.Column;

import java.io.Serializable;
import java.util.List;


public class ColumnDefinition implements Comparable<ColumnDefinition>, Serializable {

    private static final long serialVersionUID = 1L;

    private int columnNumber;
    private int width;
    private boolean hide;

    protected Column newColumn(List<Column> columns) {
        Column col = new Column(columns);
        col.setWidth(width);
        return col;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    @Override
    public int compareTo(ColumnDefinition o) {
        return columnNumber - o.getColumnNumber();
    }
}
