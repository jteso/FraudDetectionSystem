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

import java.util.ArrayList;
import java.util.List;

/**
 * @author pranab
 *
 */
public class FeatureCount  {
	private int ordinal;
	private String type;
	private List<BinCount> counts = new ArrayList<BinCount>();
	private double laplaceProb;
	
	public FeatureCount( int ordinal, String type) {
		super();
		this.ordinal = ordinal;
		this.type = type;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	

	public List<BinCount> getCounts() {
		return counts;
	}

	public void setCounts(List<BinCount> counts) {
		this.counts = counts;
	}

	public void addBinCount(BinCount binCount) {
		boolean added = false;
		for (BinCount thisBinCount : counts) {
			if (thisBinCount.getBin().equals(binCount.getBin())){
				thisBinCount.addCount(binCount.getCount());
				added = true;
			}
		}
		
		if (!added){
			counts.add(binCount);
		}
	}
	
	public void normalize(int total) {
		for (BinCount binCount : counts) {
			binCount.normalize(total);
		}
		laplaceProb = 1.0 / (1 + total);
	}	
	
	public double getProb(String bin) {
		double prob = laplaceProb;
		for (BinCount binCount : counts) {
			if (binCount.getBin().equals(bin)) {
				prob = binCount.getProb();
				break;
			}
		}		
		return prob;
	}
}
