/*******************************************************************************
 * Copyright 2017 vanilladb.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.vanilladb.core.query.algebra;

import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.sql.VarcharConstant;

/**
 * The scan class corresponding to the <em>product</em> relational algebra
 * operator.
 */
public class ExplainScan implements Scan {
	
	private Scan s;
	private Schema sch;
	private String record;
	private int numRecs;
	private boolean isFirst;

	/**
	 * Creates a product scan having the two underlying scans.
	 * 
	 * @param s1
	 *            the LHS scan
	 * @param s2
	 *            the RHS scan
	 */
	public ExplainScan (Scan s, String record, Schema sch) {
		this.s = s;
		this.sch = sch;
		this.record = "\n" + record;
		s.beforeFirst();
		while(s.next()) {
			numRecs++;
		}
		this.record += "\nActual #recs: " + numRecs;
	}

	/**
	 * Positions the scan before its first record. In other words, the LHS scan
	 * is positioned at its first record, and the RHS scan is positioned before
	 * its first record.
	 * 
	 * @see Scan#beforeFirst()
	 */
	@Override
	public void beforeFirst() {
		isFirst = true;
	}

	/**
	 * Moves the scan to the next record. The method moves to the next RHS
	 * record, if possible. Otherwise, it moves to the next LHS record and the
	 * first RHS record. If there are no more LHS records, the method returns
	 * false.
	 * 
	 * @see Scan#next()
	 */
	@Override
	public boolean next() {
		if (isFirst) {
			isFirst = false;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Closes both underlying scans.
	 * 
	 * @see Scan#close()
	 */
	@Override
	public void close() {
		s.close();
	}

	/**
	 * Returns true if the specified field is in either of the underlying scans.
	 * 
	 * @see Scan#hasField(java.lang.String)
	 */
	@Override
	public boolean hasField(String fldName) {
		return sch.hasField(fldName);
	}
	
	@Override
	public Constant getVal(String fldName) {
		if (fldName.equals("query-plan"))
			return new VarcharConstant(record);
		else 
			throw new RuntimeException("field " + fldName + " not found.");
	}
	
	
	
	
}
