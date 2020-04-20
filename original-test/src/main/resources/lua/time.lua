local function floorDiv(x, y)
    local r = x / y;
    if ((x ^ y) < 0 and (r * y ~= x)) then
        r = r - 1;
    end
    return math.floor(r);
end

local function floorMod(x, y)
    return x - floorDiv(x, y) * y;
end

local function getDayOfTime(time)
    return floorMod(time, 86400);
end

local function getHour(dateTime)
    return math.floor(dateTime / 3600);
end
local function getSecond(dateTime)
    return math.floor(dateTime % 3600);
end

local function getTime()
    local time = redis.call("GET", "TIME");
    if ("false" == tostring(time)) then
        redis.replicate_commands();
        time = redis.call("TIME")[1];
        local expire = 3600 - getSecond(getDayOfTime(time));
        redis.call("SETEX", "TIME", expire, time);
    end
    return time;
end

local function getKey(sequenceKey, tenantKey, appKey, module)
    return sequenceKey .. "_" .. tenantKey .. "_" .. appKey .. ":" .. module;
end

local function incr(sequenceKey, tenantKey, appKey, module, year, days, hour, quantity, elapsedTime)
    local key = getKey(sequenceKey, tenantKey, appKey, module);
    local incrKey = key .. year .. "." .. days .. "." .. hour;
    --local sequenceResult = quantity > 1 ? redis.call("INCRBY", key, quantity):redis.call("INCR", key);
    local sequenceResult;
    if (tonumber(quantity) > 1) then
        sequenceResult = redis.call("INCRBY", key, quantity);
    else
        sequenceResult = redis.call("INCR", key)
    end

    if (sequenceResult == quantity) then
        local randomSec = math.random(10, 60);
        redis.call("EXPIRE", key, 3600 - elapsedTime + randomSec);
    end
    return sequenceResult;
end

local day;
--sequence key tag
local sequenceKey = KEYS[1];
--tenant name / id
local tenantKey = ARGV[1];
--application name / database
local appKey = ARGV[2];
--application module name / table name
local module = ARGV[3];
--get generate of number
local quantity = tonumber(ARGV[4]);

--time zone
local timeZone = 8 * 3600;
--current time

--redis.replicate_commands()
--local time = tonumber(redis.call("TIME")[1]) + timeZone;
--local time =ARGV[5]
local time = getTime();
--daysz
local days = { 1, 32, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335 }
--:year-benchmarkYear
local benchmarkYear = 2000;
--24hour
local SECONDS_PER_DAY = 86400;
local DAYS_PER_CYCLE = 146097;
--local DAYS_0000_TO_1970 = (DAYS_PER_CYCLE * 5) - (30 * 365 + 7);
local DAYS_0000_TO_1970 = (730485 - 10957);

local epochDay = floorDiv(time, SECONDS_PER_DAY);

local dayOfTime = getDayOfTime(time);
local hour = getHour(dayOfTime);
local second = getSecond(dayOfTime);

local zeroDay = epochDay + DAYS_0000_TO_1970;

zeroDay = zeroDay - 60;
local adjust = 0;
if (zeroDay < 0) then
    --// adjust negative years to positive for calculation
    local adjustCycles = math.floor((zeroDay + 1) / DAYS_PER_CYCLE - 1);
    adjust = adjustCycles * 400;
    zeroDay = zeroDay + (-adjustCycles * DAYS_PER_CYCLE);
end
local yearEst = math.floor((400 * zeroDay + 591) / DAYS_PER_CYCLE);

local doyEst = math.floor(zeroDay - (365 * yearEst + yearEst / 4 - (yearEst / 100) + yearEst / 400));

if (doyEst < 0) then
    yearEst = yearEst - 1;
    doyEst = math.floor(zeroDay - (365 * yearEst + yearEst / 4 - yearEst / 100 + yearEst / 400));
end
yearEst = yearEst + adjust;
local marchDoy0 = doyEst;

local marchMonth0 = math.floor((marchDoy0 * 5 + 2) / 153);
local month = math.floor((marchMonth0 + 2) % 12 + 1);
local dom = marchDoy0 - math.floor((marchMonth0 * 306 + 5) / 10) + 1;
yearEst = yearEst + math.floor((marchMonth0 / 10));

day = days[month] + dom;

if ((((yearEst % 4) == 0) and ((yearEst % 100) ~= 0 or (yearEst % 400) == 0)) and month > 2) then
    day = day + 1;
end
yearEst = yearEst - benchmarkYear;
local result = incr(sequenceKey, tenantKey, appKey, module, yearEst, day, hour, quantity, second);

--year:2020 day:364 hour:11 sequence:1
--return:20364110000001
return string.format("%d%03d%02d%07d", yearEst, day, hour, result);
--return string.format("%d-%03d-%03d", yearEst, day, hour);