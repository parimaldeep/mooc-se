var page = require('webpage').create();
var system = require('system');
var fs = require('fs');

if(system.args.length !== 3) {
  console.log('Usage: phantomjs text-scraper.js <url> <output file>');
  phantom.exit();
}

var url = system.args[1];
var outfile = system.args[2];

page.onConsoleMessage = function(msg) {
  console.log(msg);
};

page.open(url, function(status) {
  var output = url + '\n';
  if(status === 'success') {
    setTimeout(function() {
      var text = page.evaluate(function () {
        return document.title + '\n' + document.body.innerText;
      });
      output += text;
      fs.write(outfile, output);
    }, 1000);
    setTimeout(function () {
      phantom.exit()
    }, 1000);
  } else {
    console.log("Error!")
    phantom.exit()
  }
});
