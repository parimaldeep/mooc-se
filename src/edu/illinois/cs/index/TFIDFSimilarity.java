package edu.illinois.cs.index;

import org.apache.lucene.search.similarities.BasicStats;
import org.apache.lucene.search.similarities.SimilarityBase;

public class TFIDFSimilarity extends SimilarityBase {
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
		float IDF = 1 + (float) Math.log(stats.getNumberOfDocuments() / stats.getDocFreq());
		return termFreq * IDF;
	}

	@Override
	public String toString() {
		return "TF-IDF Similarity";
	}
}
