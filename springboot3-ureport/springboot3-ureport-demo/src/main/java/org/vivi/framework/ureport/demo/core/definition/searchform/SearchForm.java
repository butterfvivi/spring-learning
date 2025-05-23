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
package org.vivi.framework.ureport.demo.core.definition.searchform;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年10月23日
 */
public class SearchForm {
    private List<Component> components;
    private FormPosition formPosition;

    public String toHtml(RenderContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("<form id='search-form' style='margin-top:10px;margin-bottom:10px' onkeypress='event.keyCode!=13;' onkeydown='if(event.keyCode==13){return false}'>");
        for (Component component : components) {
            sb.append(component.toHtml(context));
        }
        sb.append("</form>");
        return sb.toString();
    }

    public String toJs(RenderContext context) {
        StringBuilder sb = new StringBuilder();
        for (Component component : components) {
            sb.append(component.initJs(context));
        }
        return sb.toString();
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public FormPosition getFormPosition() {
        return formPosition;
    }

    public void setFormPosition(FormPosition formPosition) {
        this.formPosition = formPosition;
    }
}
