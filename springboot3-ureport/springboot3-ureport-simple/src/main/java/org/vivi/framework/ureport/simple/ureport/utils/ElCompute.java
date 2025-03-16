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
package org.vivi.framework.ureport.simple.ureport.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Jacky.gao
 * @since 2018年6月28日
 */
public class ElCompute {

	private static final char empty = ' ';

	public static Object doCompute(String expr) {
		return convertToSuffExper(convertExpToList(expr));
	}

	public static void main(String[] args) {
		long start1 = System.currentTimeMillis();
		System.out.println(convertToSuffExper(convertExpToList("22/2*2-(5+(1*2))-2*2")));
		long end1 = System.currentTimeMillis();
		System.out.println(end1 - start1);
	}

	private static List<String> convertExpToList(String expr) {
		List<String> list = new ArrayList<String>();
		char prevQuote = empty, prevChar = empty, prevOperator = empty;
		int start = 0;
		for (int i = 0; i < expr.length(); i++) {
			char c = expr.charAt(i);
			if (prevChar == '\\') {
				prevChar = c;
				continue;
			}
			if (prevQuote == '"') {
				if (c == '"') {
					prevQuote = empty;
					list.add(expr.substring(start + 1, i));
					start = i + 1;
				}
				prevChar = c;
				continue;
			}
			switch (c) {
			case '+':
				if (prevQuote == empty && prevOperator == empty && i > 0) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = c;
				} else {
					prevOperator = empty;
				}
				break;
			case '-':
				if (prevQuote == empty && prevOperator == empty && i > 0) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = c;
				} else {
					prevOperator = empty;
				}
				break;
			case '*':
				if (prevQuote == empty && prevOperator == empty && i > 0) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = c;
				} else {
					prevOperator = empty;
				}
				break;
			case '/':
				if (prevQuote == empty && prevOperator == empty && i > 0) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = c;
				} else {
					prevOperator = empty;
				}
				break;
			case '%':
				if (prevQuote == empty && prevOperator == empty && i > 0) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = c;
				} else {
					prevOperator = empty;
				}
				break;
			case '(':
				if (prevQuote == empty) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = '(';
				}
				break;
			case ')':
				if (prevQuote == empty) {
					if (start != i) {
						list.add(expr.substring(start, i));
					}
					list.add(String.valueOf(c));
					start = i + 1;
					prevOperator = empty;
				}
				break;
			case '"':
				if (prevQuote == '"') {
					prevQuote = empty;
					list.add(expr.substring(start + 1, i));
					start = i + 1;
				} else {
					prevQuote = '"';
				}
				prevOperator = empty;
				break;
			case empty:
				if (prevQuote == empty) {
					start = i + 1;
				}
				break;
			default:
				prevOperator = empty;

			}
			prevChar = c;
		}
		if (start < expr.length()) {
			list.add(expr.substring(start, expr.length()));
		}
		return list;
	}

	private static String convertToSuffExper(List<String> experList) {
		Stack<String> stack = new Stack<String>();
		List<String> list = new ArrayList<String>();
		for (String s : experList) {
			if ("(".equals(s)) {
				stack.push(s);
			} else if (")".equals(s)) {
				while (!stack.isEmpty()) {
					String o = stack.pop();
					if ("(".equals(o)) {
						break;
					}
					list.add(o);
				}
			} else if ("+".equals(s) || "-".equals(s)) {
				if (stack.isEmpty() || "(".equals(stack.peek())) {
					stack.push(s);
					continue;
				}
				list.add(stack.pop());
				stack.push(s);

			} else if ("*".equals(s) || "/".equals(s) || "%".equals(s)) {
				if (stack.isEmpty()) {
					stack.push(s);
					continue;
				}
				String o = stack.peek();
				if ("*".equals(o) || "/".equals(o) || "%".equals(o)) {
					list.add(stack.pop());
				}
				stack.push(s);
			} else {
				list.add(s);
			}
		}
		while (!stack.isEmpty()) {
			list.add(stack.pop());
		}
		for (String s : list) {
			String right = null;
			String left = null;
			switch (s) {
			case "+":
				right = stack.pop();
				left = stack.pop();
				if (NumberUtils.isCreatable(left) && NumberUtils.isCreatable(right)) {
					stack.push(ToolUtils.toBigDecimal(left).add(ToolUtils.toBigDecimal(right)).toString());
				} else {
					stack.push(left + right);
				}
				break;
			case "-":
				right = stack.pop();
				left = stack.pop();
				if (NumberUtils.isCreatable(left) && NumberUtils.isCreatable(right)) {
					stack.push(ToolUtils.toBigDecimal(left).subtract(ToolUtils.toBigDecimal(right)).toString());
				} else {
					stack.push(left + right);
				}
				break;
			case "*":
				right = stack.pop();
				left = stack.pop();
				if (NumberUtils.isCreatable(left) && NumberUtils.isCreatable(right)) {
					stack.push(ToolUtils.toBigDecimal(left).multiply(ToolUtils.toBigDecimal(right)).toString());
				} else {
					stack.push(left + right);
				}
				break;
			case "/":
				right = stack.pop();
				left = stack.pop();
				if (NumberUtils.isCreatable(left) && NumberUtils.isCreatable(right)) {
					BigDecimal a = ToolUtils.toBigDecimal(left).divide(ToolUtils.toBigDecimal(right), 10, RoundingMode.HALF_UP).stripTrailingZeros();
					stack.push(a.toString());
				} else {
					stack.push(left + right);
				}
				break;
			case "%":
				right = stack.pop();
				left = stack.pop();
				if (NumberUtils.isCreatable(left) && NumberUtils.isCreatable(right)) {
					BigDecimal[] b = ToolUtils.toBigDecimal(left).divideAndRemainder(ToolUtils.toBigDecimal(right));
					stack.push(b[1].toString());
				} else {
					stack.push(left + right);
				}
				break;
			default:
				stack.push(s);
			}
		}
		return stack.pop();
	}
}
