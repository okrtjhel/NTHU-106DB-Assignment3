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

import java.util.HashSet;
import java.util.Set;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;
import org.vanilladb.core.storage.metadata.statistics.Bucket;
import org.vanilladb.core.storage.metadata.statistics.Histogram;

/**
 * The {@link Plan} class corresponding to the <em>product</em> relational
 * algebra operator.
 */
public class ExplainPlan implements Plan {
	
	/**
	 *  Default 
	 */
	
	private Plan p;
	
	
	public ExplainPlan (Plan p) {
		this.p = p;
	}

	/**
	 * Creates a explain scan for this query.
	 * 
	 * @see Plan#open()
	 */
	@Override
	public Scan open() {
		Scan s = p.open();
		return new ExplainScan(s, p.toString(0), schema());
	}

	/**
	 * Estimates the number of block accesses in the product. The formula is:
	 * 
	 * <pre>
	 * B(product(p1, p2)) = B(p1) + R(p1) * B(p2)
	 * </pre>
	 * 
	 * @see Plan#blocksAccessed()
	 */
	@Override
	public long blocksAccessed() {
		return p.blocksAccessed();
	}

	/**
	 * Returns the schema of the product, which is the union of the schemas of
	 * the underlying queries.
	 * 
	 * @see Plan#schema()
	 */
	@Override
	public Schema schema() {
		Schema sch = new Schema();
		sch.addField("query-plan", Type.VARCHAR(500));
		return sch;
	}

	/**
	 * Returns the histogram that approximates the join distribution of the
	 * field values of query results.
	 * 
	 * @see Plan#histogram()
	 */
	@Override
	public Histogram histogram() {
		return p.histogram();
	}

	/**
	 * Returns an estimate of the number of records in the query's output table.
	 * 
	 * @see Plan#recordsOutput()
	 */
	@Override
	public long recordsOutput() {
		long recordsOutput = 1;
		return recordsOutput;
	}
	
	@Override 
	public String toString(int depth) {
		return "";
	}
	
}
