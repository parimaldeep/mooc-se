package edu.illinois.cs.index;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Searcher
{
    private IndexSearcher indexSearcher;
    private IndexReader indexReader;
    private SpecialAnalyzer analyzer;
    private static SimpleHTMLFormatter formatter;
    private static final int numFragments = 4;
    private static final String defaultField = "content";

    /**
     * Sets up the Lucene index Searcher with the specified index.
     *
     * @param indexPath
     *            The path to the desired Lucene index.
     */
    public Searcher(String indexPath)
    {
        try
        {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexPath)));
            indexSearcher = new IndexSearcher(reader);
            //indexSearcher.setSimilarity(new BM25Similarity());     // BM25Similarity exists in Lucene
            //indexSearcher.setSimilarity(new TFIDFSimilarity());  // this is our custom similarity function
            indexSearcher.setSimilarity(new LMDirichletSimilarity());
            //indexSearcher.setSimilarity(new SimpleSimilarity()); // simple boolean occurrence example function
            analyzer = new SpecialAnalyzer();
            formatter = new SimpleHTMLFormatter("<strong>", "</strong>");
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * The main search function.
     * @param searchQuery Set this object's attributes as needed.
     * @return
     */
    public SearchResult search(SearchQuery searchQuery)
    {
        BooleanQuery combinedQuery = new BooleanQuery();
        for(String field: searchQuery.fields())
        {
            QueryParser parser = new QueryParser(Version.LUCENE_46, field, analyzer);
            try
            {
                Query textQuery = parser.parse(searchQuery.queryText());
                combinedQuery.add(textQuery, BooleanClause.Occur.MUST);
            }
            catch(ParseException exception)
            {
                exception.printStackTrace();
            }
        }

        return runSearch(combinedQuery, searchQuery);
    }
    /*
    public SearchResult searchFeedback(String query, SearchQuery searchQuery)
    {
  //Get the original results
    	TopDocs docs = indexSearcher.search(searchQuery,25);
    	HashMap<String,ScorePair> map = new HashMap<String,ScorePair>();
    	for (int i = 0; i < docs.scoreDocs.length; i++) {
        //Iterate fields for each result
    		Iterator<String> fields = indexReader.getTermVectors(docs.scoreDocs[i].doc).iterator();
    		String fieldname;
    		while (fields.hasNext()) {
            //For each field, iterate it's terms
    			Fields field = indexReader.getTermVectors(docs.scoreDocs[i].doc);
    			fieldname = fields.next();
    			Terms terms = field.terms(fieldname);
    			
                //and store it
    			putTermInMap(fieldname, terms.toString(), terms.getSumDocFreq(), map);
    		}
    	}

    	List<ScorePair> byScore = new ArrayList<ScorePair>(map.values());
    	Collections.sort(byScore);

    	BooleanQuery bq = new BooleanQuery();
    //Perhaps we want to give the original query a bit of a boost
    	//query.setBoost(5);
    	bq.add(searchQuery,BooleanClause.Occur.SHOULD);
    	for (int i = 0; i < 25; i++) {
        //Add all our found terms to the final query
    		ScorePair pair = byScore.get(i);
    		bq.add(new TermQuery(new Term(pair.field,pair.term)),BooleanClause.Occur.SHOULD);
    	}
    	
    	BooleanQuery combinedQuery = bq;
        for(String field: searchQuery.fields())
        {
            QueryParser parser = new QueryParser(Version.LUCENE_46, field, analyzer);
            try
            {
                Query textQuery = parser.parse(searchQuery.queryText());
                combinedQuery.add(textQuery, BooleanClause.Occur.MUST);
            }
            catch(ParseException exception)
            {
                exception.printStackTrace();
            }
        }

        return runSearch(combinedQuery, searchQuery);
    }

    //Say, we want to score based on tf/idf
    void putTermInMap(String field, String term, long freq, Map<String,ScorePair> map) {
        String key = field + ":" + term;
        if (map.containsKey(key))
            map.get(key).increment();
        else
            map.put(key,new ScorePair((int)freq,field,term));
    }
    */
    /**
     * The simplest search function. Searches the abstract field and returns a
     * the default number of results.
     *
     * @param queryText
     *            The text to search
     * @return the SearchResult
     */
    public SearchResult search(String queryText)
    {
        return search(new SearchQuery(queryText, defaultField));
    	//Query q = new Query();
        //return searchFeedback(queryText, new SearchQuery(queryText, defaultField));
    }

    /**
     * Performs the actual Lucene search.
     *
     * @param luceneQuery
     * @param numResults
     * @return the SearchResult
     */
    private SearchResult runSearch(Query luceneQuery, SearchQuery searchQuery)
    {
        try
        {
            TopDocs docs = indexSearcher.search(luceneQuery, searchQuery.fromDoc() + searchQuery.numResults());
            ScoreDoc[] hits = docs.scoreDocs;
            String field = searchQuery.fields().get(0);

            SearchResult searchResult = new SearchResult(searchQuery, docs.totalHits);
            for(ScoreDoc hit : hits)
            {
                Document doc = indexSearcher.doc(hit.doc);
                ResultDoc rdoc = new ResultDoc(hit.doc);

                String highlighted = null;
                try
                {
                    Highlighter highlighter = new Highlighter(formatter, new QueryScorer(luceneQuery));
                    String title = doc.getField("title").stringValue();
                    rdoc.title(title);
                    String contents = doc.getField(field).stringValue();
                    rdoc.content(contents);
                    String url = doc.getField("url").stringValue();
                    rdoc.url(url);
                    String[] snippets = highlighter.getBestFragments(analyzer, field, contents, numFragments);
                    highlighted = createOneSnippet(snippets);
                }
                catch(InvalidTokenOffsetsException exception)
                {
                    exception.printStackTrace();
                    highlighted = "(no snippets yet)";
                }

                searchResult.addResult(rdoc);
                searchResult.setSnippet(rdoc, highlighted);
            }

            searchResult.trimResults(searchQuery.fromDoc());
            return searchResult;
        }
        catch(IOException exception)
        {
            exception.printStackTrace();
        }
        return new SearchResult(searchQuery);
    }

    /**
     * Create one string of all the extracted snippets from the highlighter
     * @param snippets
     * @return
     */
    private String createOneSnippet(String[] snippets)
    {
        String result = " ... ";
        for(String s: snippets)
            result += s + " ... ";
        return result;
    }
}
/*

class ScorePair implements Comparable{
    int count = 0;
    double idf;
    String field;
    String term;

    ScorePair(int docfreq, String field, String term) {
        count++;
        //Standard Lucene idf calculation.  This is calculated once per field:term
        idf = (1 + Math.log(indexReader.numDocs()/((double)docfreq + 1))) ^ 2;
        this.field = field;
        this.term = term;
    }

    void increment() { count++; }

    double score() {
        return Math.sqrt(count) * idf;
    }

    //Standard Lucene TF/IDF calculation, if I'm not mistaken about it.
    public int compareTo(Object pair) {
        if (this.score() < ((ScorePair)pair).score()) return -1;
        else return 1;
    }
	
}*/
