local lock=KEYS[1]
local accessToken=KEYS[2]
local token=ARGV[1]
local timeOut=ARGV[2]

redis.call("DEL", lock);
return redis.call("SETEX", accessToken,timeOut,token);