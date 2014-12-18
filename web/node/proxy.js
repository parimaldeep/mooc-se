var proxy = require('http-proxy').createProxy();
var util = require('util')

var re = new RegExp("^\/search");
require('http').createServer(function(req, res) {
  console.log("Requested " + (req.headers.host + req.url));
  var target = 'http://0.0.0.0:3000/';
  if(re.test(req.url)) {
    target = 'http://0.0.0.0:8081/search';
  }
  var options = {
    target: target,
    protocol: 'http'
  };
  console.log(" -> routed to: " + util.inspect(options));
  proxy.web(req, res, options, function(error) { console.log(error) } );
}).listen(9001);

console.log("Started proxy server on port 9001");
