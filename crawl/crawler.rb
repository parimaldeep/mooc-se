course_list = File.open("courses.txt", 'w')
for line in File.open("urls.txt", 'r').readlines
  docid = line.split(" ")[0]
  url = line.split(" ")[1].chomp
  puts " #{docid} -> #{url}"
  `phantomjs text-scraper.js #{url} courses/#{docid}.txt`
  course_list << "courses/#{docid}.txt\n"
end
