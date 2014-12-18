package edu.illinois.cs.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.util.ajax.JSON;

import edu.illinois.cs.index.ResultDoc;
import edu.illinois.cs.index.SearchQuery;
import edu.illinois.cs.index.SearchResult;
import edu.illinois.cs.index.Searcher;

public class SearchServlet extends HelloWorldServlet {
	private static final long serialVersionUID = -8020111169469835646L;
	private static final Searcher searcher = new Searcher("lucene-mooc-index");
	private static final int PAGE_SIZE = 10;

	public class APIException extends Exception {
		private static final long serialVersionUID = -3562991459585959513L;
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Map<String, String[]> params = request.getParameterMap();

			if (params.containsKey("query")) {
				SearchResult result = null;
				int page = 1;

				if (params.containsKey("page"))
					page = Integer.parseInt(params.get("page")[0]);

				SearchQuery query = new SearchQuery()
						.queryText(params.get("query")[0])
						.fromDoc(PAGE_SIZE * (page - 1)).numResults(PAGE_SIZE);
				result = searcher.search(query);

				ArrayList<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
				for (ResultDoc rdoc : result.getDocs()) {
					HashMap<String, Object> doc = new HashMap<String, Object>();
					doc.put("title", rdoc.title());
					doc.put("url", rdoc.url());
					doc.put("snippet", result.getSnippet(rdoc));
					results.add(doc);
				}

				Map<String, Object> responseMap = new HashMap<String, Object>();
				responseMap.put("results", results);
				responseMap.put("hits", 1);

				int totalPages = result.numHits() / PAGE_SIZE;
				if (result.numHits() % PAGE_SIZE > 0)
					totalPages += 1;

				responseMap.put("pages", totalPages);
				responseMap.put("page", page);
				responseMap.put("error", null);
				response.getWriter().println(JSON.toString(responseMap));
			} else {
				throw new APIException();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			badRequest(response);
		}
	}

	void badRequest(HttpServletResponse response) throws IOException {
		response.setContentType("application/json");
		Map<String, Object> badReqMap = new HashMap<String, Object>();
		badReqMap.put("results", null);
		badReqMap.put("error", "Bad request");
		response.getWriter().println(JSON.toString(badReqMap));
	}
}
