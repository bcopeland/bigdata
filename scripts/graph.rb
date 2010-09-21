#! /usr/bin/ruby
require 'json'
require 'net/http'

def rate_limit(remaining_hits, end_period)

    # sometimes the headers are missing
    if (end_period == 0)
        return
    end

    now = Time.now.to_i
    puts "remaining hits: #{remaining_hits}, endp: #{end_period}, now: #{now}"
    sleep_time = (end_period - now) / (remaining_hits + 1)
    puts "sleeping: #{sleep_time}s"
    sleep(sleep_time)
end

def do_request(url, fn)
    result = Net::HTTP.get_response(URI.parse(url))
    f = open(fn, 'w')
    f.puts(result.body)
    f.close()
    rate_limit(result.header["X-RateLimit-Remaining"].to_i,
               result.header["X-RateLimit-Reset"].to_i);
end

def download_friends(id)
    fn = "data/graph/" + id.to_s + ".friends"
    if !File.exists?(fn)
        url = "http://api.twitter.com/1/friends/ids.json?user_id=" +
              id.to_s
        do_request(url, fn)
    end
end

def download_followers(id)
    fn = "data/graph/" + id.to_s + ".followers"
    if !File.exists?(fn)
        url = "http://api.twitter.com/1/followers/ids.json?user_id=" +
              id.to_s
        do_request(url, fn)
    end
end

fn = ARGV[0]
f = open(fn)
while f.gets
    entry = JSON.parse($_)
    if entry["user"] != nil
        id = entry["user"]["id"]
        puts id
        download_friends(id)
        download_followers(id)
    end
end
f.close
