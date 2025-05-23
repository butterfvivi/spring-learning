// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.impl;

import org.vivi.framework.dynamic.sqlbatis3.utils.UtilAll;
import org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.TypeConversionException;
import org.vivi.framework.dynamic.sqlbatis3.utils.bean.convert.TypeConvertCommon;

/**
 * Converts given object to <code>Double</code>.
 * Conversion rules:
 * <ul>
 * <li><code>null</code> value is returned as <code>null</code></li>
 * <li>object of destination type is simply casted</li>
 * <li>object is converted to string, trimmed, and then converted if
 * possible.</li>
 * </ul>
 * Number string may start with plus and minus sign.
 */
public class DoubleConverter extends TypeConvertCommon<Double> {

	public Double convert(final Object value) {
		if (value == null) {
			return null;
		}

		if (value.getClass() == Double.class) {
			return (Double) value;
		}
		if (value instanceof Number) {
			return Double.valueOf(((Number) value).doubleValue());
		}
		if (value instanceof Boolean) {
			return ((Boolean) value).booleanValue() ? Double.valueOf(1) : Double.valueOf(0);
		}

		try {
			String stringValue = value.toString().trim();
			if (UtilAll.UString.startsWithChar(stringValue, '+')) {
				stringValue = stringValue.substring(1);
			}
			return Double.valueOf(stringValue);
		} catch (NumberFormatException nfex) {
			throw new TypeConversionException(value, nfex);
		}
	}

}
