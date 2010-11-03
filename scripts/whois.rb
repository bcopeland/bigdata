#! /usr/bin/ruby
require 'json'
require 'net/http'

def rate_limit(remaining_hits, end_period)

    # sometimes the headers are missing
    if (end_period == 0)
        return
    end

    now = Time.now.to_i
    # puts "remaining hits: #{remaining_hits}, endp: #{end_period}, now: #{now}"
    sleep_time = (end_period - now) / (remaining_hits + 1)
    # puts "sleeping: #{sleep_time}s"
    sleep(sleep_time)
end

def do_request(url)
    result = Net::HTTP.get_response(URI.parse(url))
    return [result.body, proc {
            rate_limit(result.header["X-RateLimit-Remaining"].to_i,
            result.header["X-RateLimit-Reset"].to_i);
        }]

end

def show(id)
    url = "http://api.twitter.com/1/users/show.json?user_id=" +
              id.to_s
    (ans, ratelimit) = do_request(url)
    value = JSON.parse(ans)
    puts value["screen_name"]
    ratelimit.call
end

while gets
    show($_)
end
