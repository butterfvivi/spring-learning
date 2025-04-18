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
package org.vivi.framework.ureport.store.core;

import java.io.Serializable;

/**
 * @author Jacky.gao
 * @since 2017年2月24日
 */
public class Range implements Serializable{
	private static final long serialVersionUID = -4547468301777433024L;
	private int start=-1;
	private int end;
	public Range() {
	}
	public Range(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	public void setEnd(int end) {
		this.end = end;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStart() {
		return start;
	}
	public int getEnd() {
		return end;
	}
}
