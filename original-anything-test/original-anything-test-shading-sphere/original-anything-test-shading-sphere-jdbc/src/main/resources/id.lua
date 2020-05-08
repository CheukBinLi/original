return os.date('%y');
local getDay=os.date('*t',os.time());
return getTime..getDay;