package edu.illinois.cs.index;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class SimpleSimilarity extends SimilarityBase {

	/**
	 * Returns a score for a single term in the document.
	 * 
	 * @param stats
	 *            Provides access to corpus-level statistics
	 * @param termFreq
	 * @param docLength
	 *            The length of the current document
	 */
	@Override
	protected float score(BasicStats stats, float termFreq, float docLength) {
		// if the term appears in the query and this document, add 1 to the final score
		return 1;
	}

	@Override
	public String toString() {
		return "Simple Similarity";
	}

}
