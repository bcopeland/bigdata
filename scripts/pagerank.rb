#! /usr/bin/ruby

require 'json'

# person -> pagerank
$rank = {}

def get_followers(id)
    fn = "data/graph/#{id}.followers"
    if !File.exists?(fn)
        return []
    end

    file = open(fn)
    entry = JSON.parse(file.gets)
    file.close
    return entry
end

def get_friends(id)
    fn = "data/graph/#{id}.friends"
    if !File.exists?(fn)
        return []
    end
    file = open(fn)
    entry = JSON.parse(file.gets)
    file.close
    return entry
end

def pagerank(id)
    d = 0.85

    followers = get_followers(id)
    sum_pr = 0;

    followers.each do |f|
        l = get_friends(f).length
        next if l == 0
        next if $rank[f] == nil
        sum_pr += $rank[f] / l
    end
    n = followers.length
    if (n == 0)
        n = 1
    end
    return ((1-d) / n + d * sum_pr)
end

def do_ranking
    max_user = 1000000
    for i in 0..max_user
        $rank[i] = 1.0
    end

    for i in 0..5
        rank_next = {}
        $rank.keys.each do |id|
            rank_next[id] = pagerank(id)
        end
        $rank = rank_next
    end

    $rank.keys.each do |id|
        r = $rank[id]
        puts "#{id} #{r}"
    end
end

do_ranking()

