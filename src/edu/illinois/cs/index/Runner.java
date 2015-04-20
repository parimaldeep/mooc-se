package edu.illinois.cs.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Runner {
	final static String _dataset = "mooc";
	final static String _indexPath = "lucene-mooc-index";
//	final static String _prefix = "crawl/";
//	final static String _fileList = "courses.txt";
	final static String _prefix = "ocr/videos/";
	final static String _fileList = "videos.txt";

	public static void main(String[] args) throws IOException {
		if (args.length == 1 && args[0].equalsIgnoreCase("--index"))
			Indexer.index(_indexPath, _prefix, _fileList);
		else if (args.length == 1 && args[0].equalsIgnoreCase("--search"))
			interactiveSearch();
		else
			System.out.println("Usage: --index to index or --search to search an index");
	}

	/**
	 * Feel free to modify this function!
	 * 
	 * @throws IOException
	 */
	private static void interactiveSearch() throws IOException {
		Searcher searcher = new Searcher(_indexPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Type text to search, blank to quit.");
		System.out.print("> ");
		String input;
		while ((input = br.readLine()) != null && !input.equals("")) {
			SearchResult result = searcher.search(input);
			ArrayList<ResultDoc> results = result.getDocs();
			int rank = 1;
			if (results.size() == 0)
				System.out.println("No results found!");
			for (ResultDoc rdoc : results) {
				System.out.println("\n------------------------------------------------------");
				System.out.println(rank + ". " + rdoc.title());
				System.out.println(rdoc.url());
				System.out.println("------------------------------------------------------");
				System.out.println(result.getSnippet(rdoc)
						.replaceAll("\n", " "));
				++rank;
			}
			System.out.print("> ");
		}
	}
}