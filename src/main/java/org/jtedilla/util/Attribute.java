/*
 * chombo: Hadoop Map Reduce utility
 * Author: Pranab Ghosh
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package org.jtedilla.util;

import java.util.List;

/**
 * Base class for attribute. Used in schema definition. Initialized based on schema definition
 * JSON file
 * @author pranab
 *
 */
public class Attribute {
	protected String name;
	protected int ordinal = -1;
	protected boolean id;
	protected String dataType;
	protected List<String> cardinality;
	protected double  min;
	protected boolean minDefined;
	protected double  max;
	protected boolean maxDefined;
	protected double mean;
	protected boolean meanDefined;
	protected double variance;
	protected double stdDev;
	protected boolean stdDevDefined;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}
	public boolean isId() {
		return id;
	}
	public void setId(boolean id) {
		this.id = id;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
		minDefined = true;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
		maxDefined = true;
	}
	public List<String> getCardinality() {
		return cardinality;
	}
	public void setCardinality(List<String> cardinality) {
		this.cardinality = cardinality;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
		meanDefined = true;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public double getStdDev() {
		return stdDev;
	}
	public void setStdDev(double stdDev) {
		this.stdDev = stdDev;
		stdDevDefined = true;
	}
	public boolean isCategorical() {
		return dataType.equals("categorical");
	}

	public boolean isInteger() {
		return dataType.equals("int");
	}

	public boolean isDouble() {
		return dataType.equals("double");
	}

	public boolean isText() {
		return dataType.equals("text");
	}
	
	public int cardinalityIndex(String value) {
		return cardinality.indexOf(value);
	}
}
