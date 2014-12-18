$ ->
  new Searcher()

class Searcher
  constructor: () ->
    @startHashChangeListener()
    @startSearchFormListener()
    @search()
    _this = @

  startHashChangeListener: () ->
    self = @
    $(window).hashchange () ->
      self.search()

  search: () ->
    params = window.location.hash.replace(/[\#\?]/g, '').split('&')
    hash = {}
    for param in params
      split = param.split '='
      hash[split[0]] = split[1]
    return unless hash.q?
    if hash.q.length == 0
      $('#searchResults').html ''
      $('#searchPagination').html ''
      return
    @doSearch(decodeURIComponent(hash.q), hash.p)

  startSearchFormListener: () ->
    $('#searchForm').submit () ->
      query = $(this).find('input').val()
      window.location.hash = "?q=#{encodeURIComponent query}&p=#{1}"
      return false

  doSearch: (query, page) ->
    page = 1 unless page
    $.getJSON "#{window.location.href}".replace(/#.*$/,"search"), query: query, page: page, (data) ->
    #$.getJSON "#{window.location.href}/search", query: query, page: page, (data) ->
      if data.error
        alert "Something horrible happened: \n\n#{data.error}"
      else
        hashbase = "?q=#{query}"
        if data.results.length == 0
          $('#searchResults').html '<li>No results found...</li>'
          $('#searchPagination').html ''
        else
          $('#searchResults').html ich.search_results(data, true)
          prevpage = Math.max data.page - 1, 1
          nextpage = Math.min data.page + 1, data.pages
          pagedata =
            hashbase: hashbase
            prevpage: prevpage
            prevenabled: prevpage != data.page
            nextpage: nextpage
            nextenabled: nextpage != data.page
            pages: []

          range = []
          unless data.page - 5 > 0
            range = (i for i in [1..Math.min(data.pages, 10)])
          else unless data.page + 4 > data.pages
            range = (i for i in [data.page-5..data.page+4])
          else
            range = (i for i in [Math.max(1, data.pages-10)..data.pages])

          for i in range
            pagedata.pages[i] =
              idx: i
              active: i == data.page

          $('#searchPagination').html ich.pagination(pagedata, true)
          document.body.scrollTop = document.documentElement.scrollTop = 0
    false
