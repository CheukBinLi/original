local lock=KEYS[1]
if redis.call("EXISTS", lock) == 0 then
	return 0;
end
return redis.call("SETEX", lock,5000,0); --return OK