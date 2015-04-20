package edu.illinois.cs.eval;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import edu.illinois.cs.index.ResultDoc;
import edu.illinois.cs.index.Searcher;

public class Evaluate {
	/**
	 * Format for judgements.txt is:
	 * 
	 * line 0: <query 1 text> line 1: <space-delimited list of relevant URLs>
	 * line 2: <query 2 text> line 3: <space-delimited list of relevant URLs>
	 * ...
	 */
	private static final String _judgeFile = "judgements.txt";
	final static String _indexPath = "lucene-mooc-index";
	static Searcher _searcher = null;

	public static void main(String[] args) throws IOException {
		_searcher = new Searcher(_indexPath);
		BufferedReader br = new BufferedReader(new FileReader(_judgeFile));
		String line = null;
		double avgPrecSum = 0.0;
		double numQueries = 0.0;
		while ((line = br.readLine()) != null) {
			avgPrecSum += eval(line, br.readLine());
			++numQueries;
		}
		br.close();

		System.out.println("\nMAP: " + avgPrecSum / numQueries);
	}

	private static double eval(String query, String docString) {
		ArrayList<ResultDoc> results = _searcher.search(query).getDocs();
		if (results.size() == 0)
			return 0;

		HashSet<String> relDocs = new HashSet<String>(Arrays.asList(docString
				.split(" ")));
		int i = 1;
		double avgp = 0.0;
		double numRel = 1;
		System.out.println("\nQuery: " + query);
		for (ResultDoc rdoc : results) {
			String urlRes = rdoc.url();
			boolean found = false;
			for (String urlRel : relDocs) {
			    String[] urlResA = urlRes.split("start=");
			    String[] urlRelA = urlRel.split("start=");
			    if (urlResA[0].equals(urlRelA[0]) && Math.abs(Integer.parseInt(urlResA[1]) - Integer.parseInt(urlRelA[1])) <= 300) { 
			     	found = true;
			    	break;
			    }
			}
			if (found) {
			    avgp += numRel / i;
				++numRel;
				System.out.print("  ");
				System.out.println(i + ". " + urlRes);
			} else {
				System.out.print("X ");
				System.out.println(i + ". " + urlRes);
			}
			++i;
		}
		System.out.println("Average Precision: " + (avgp / (i-1)));
		return avgp / (i - 1);
	}
}