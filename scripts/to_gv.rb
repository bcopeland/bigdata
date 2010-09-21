#! /usr/bin/ruby

require 'json'

def link_all(id, fn, isfwd)
    f = open(fn)
    entry = JSON.parse(f.gets)
    f.close

    arrow = (isfwd) ? "->" : "<-"

    entry.each do |x|
        puts "  #{id} -> #{x};" 
    end
end

print <<EOH
digraph G {
  size="8,10.5"
  node[shape=circle,width=.04,height=.04,fixedsize=true,label=""]
  edge[arrowhead=none]
EOH

#files = Dir.glob("data/*friends")
#files.each do |fn|
#    id = fn.gsub(/\..*/,"").gsub(/data\//,"")
#    link_all(id, fn, true)
#end

files = Dir.glob("data/*followers")
files.each do |fn|
    id = fn.gsub(/\..*/,"").gsub(/data\//,"")
    link_all(id, fn, false)
end
puts '}'
