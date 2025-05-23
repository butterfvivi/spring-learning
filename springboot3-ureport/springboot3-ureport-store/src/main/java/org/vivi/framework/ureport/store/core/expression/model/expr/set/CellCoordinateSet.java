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
package org.vivi.framework.ureport.store.core.expression.model.expr.set;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年4月2日
 */
public class CellCoordinateSet {
	private List<CellCoordinate> cellCoordinates;
	
	public CellCoordinateSet(List<CellCoordinate> cellCoordinates) {
		this.cellCoordinates = cellCoordinates;
	}
	public List<CellCoordinate> getCellCoordinates() {
		return cellCoordinates;
	}
}
