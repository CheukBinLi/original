package com.cheuks.bin.original.common.util;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/***
 * * *
 * 
 * @CREATE ON 2018年08月20日 下午4:41:01
 * @EMAIL:20796698@QQ.COM
 *
 * @author CHEUK.BIN.LI
 * @see cron 表达式转换工具
 */
public class CronBuilder implements Serializable {

	private static final long serialVersionUID = 2382561748780978569L;
	//	　　（1） Seconds Minutes Hours DayofMonth Month DayofWeek Year
	//
	//	　　（2）Seconds Minutes Hours DayofMonth Month DayofWeek

	List<String> year;
	List<String> month;//1-12
	List<String> dayofMonth;//1-31
	List<String> dayofWeek;//周1-7
	List<String> hours;
	List<String> minutes;
	List<String> seconds;

	/***
	 * * *
	 * 
	 * @CREATE ON 2018年08月21日 上午11:07:54
	 * @EMAIL:20796698@QQ.COM
	 *
	 * @author CHEUK.BIN.LI
	 * @see cron表达式
	 */
	public static class Cron {
		private final String cron;

		private Cron(String cron) {
			super();
			this.cron = cron;
		}

		public String getCron() {
			return cron;
		}
	}

	public static final DateFormat defaultFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
	public static final DateFormat defaultDateFormat = new SimpleDateFormat("ss mm HH dd MM yyyy");

	/***
	 * * *
	 * 
	 * @CREATE ON 2018年08月20日 上午9:57:43
	 * @EMAIL:20796698@QQ.COM
	 *
	 * @author CHEUK.BIN.LI
	 * @see 周日-周六排列
	 */
	public interface DayOfWeek {
		int SUN = 1, MON = 2, TUE = 3, WED = 4, THU = 5, FRI = 6, SAT = 7;
	}

	/***
	 * * *
	 * 
	 * @CREATE ON 2018年08月20日 上午9:58:19
	 * @EMAIL:20796698@QQ.COM
	 *
	 * @author CHEUK.BIN.LI
	 * @see 月份排列
	 */
	public interface Month {
		int JAN = 0, FEB = 1, MAR = 2, APR = 3, MAY = 4, JUN = 5, JUL = 6, AUG = 7, SEP = 8, OCT = 9, NOV = 10, DEC = 11;
	}

	public static enum Operation {
		VALUE("V"), OR(","), TO("-"), All("*"), ALL_MOTH("1-12"), INTERVAL("/"), LAST("L"), WORK_DAY("W"), IN("#"), PLACEHOLDER("?");

		String value;

		private Operation(String value) {
			this.value = value;
		}
	}

	public static CronBuilder builder() {
		return new CronBuilder();
	}

	public Cron build() {
		//		Seconds Minutes Hours DayofMonth Month DayofWeek Year
		//		if (null == dayofMonth && null == dayofWeek || dayofMonth.isEmpty() && dayofWeek.isEmpty() || null == dayofMonth && dayofWeek.isEmpty() || dayofMonth.isEmpty() && null == dayofWeek)
		boolean hasDay = false;
		String dayofMonth = getValue(this.dayofMonth, "*");
		if (!"*".contains(dayofMonth) && !"?".contains(dayofMonth)) {
			hasDay = true;
		}
		String year = getValue(this.year);
		if ("*".contains(year)) {
			return new Cron(String.format("%s %s %s %s %s %s", getValue(this.seconds, "0"), getValue(this.minutes), getValue(this.hours), dayofMonth = getValue(this.dayofMonth, "*"), getValue(this.month), getValue(hasDay ? null : this.dayofWeek, "?")));
		}
		return new Cron(String.format("%s %s %s %s %s %s %s", getValue(this.seconds, "0"), getValue(this.minutes), getValue(this.hours), dayofMonth = getValue(this.dayofMonth, "*"), getValue(this.month), getValue(hasDay ? null : this.dayofWeek, "?"), year));
	}

	public static Cron buildCronByString(String cron) {
		if (null == cron)
			return new Cron("* * * * * ?");
		//		Seconds Minutes Hours DayofMonth Month DayofWeek Year
		return new Cron(cron);
	}

	/***
	 * 
	 * @param date
	 * @param periodType
	 *            0不重复 1每半小时 2每小时 3每天 4每周 5每月 6每年 <br>
	 *            【不重复=0】【每半小时=1】【每小时=2】【每天=3】【工作天=4(X)】【每周=5】【每月=6】【每年=7】
	 * @return
	 */
	public static Cron buildSimpleCron(Date date, int periodType) {
		return buildSimpleCron(date, periodType, 0);
	}

	@SuppressWarnings("deprecation")
	public static Cron buildSimpleCron(Date date, int periodType, int offset) {
		//		int second = offset > IntervalType.SECONDS.lessThan ? offset % 60 : offset;
		long nextDate = date.getTime() - (offset * 1000);
		date = System.currentTimeMillis() >= nextDate ? date : new Date(date.getTime() - (offset * 1000));
		Cron cron;
		switch (periodType) {
		case 1:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.HOURS, Operation.All, 0, 0), new IntervalTypeProvider(IntervalType.MINUTES, Operation.INTERVAL, date.getMinutes() == 30 ? 0 : date.getMinutes(), 30), new IntervalTypeProvider(IntervalType.SECONDS, Operation.VALUE, 0, 0));
			break;
		case 2:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.HOURS, Operation.INTERVAL, 0, 1), new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0), new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0));
			break;
		case 3:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.DAY_OF_WEEK, Operation.PLACEHOLDER, 0, 0), new IntervalTypeProvider(IntervalType.DAY_OF_MONTH, Operation.All, 0, 0), new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0));
			break;
		case 4:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.DAY_OF_WEEK, Operation.VALUE, date.getDay(), 1), new IntervalTypeProvider(IntervalType.DAY_OF_MONTH, Operation.PLACEHOLDER, 0, 0), new IntervalTypeProvider(IntervalType.MONTH, Operation.ALL_MOTH, 0, 0), new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0));
			break;
		case 5:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.MONTH, Operation.All, 0, 0), new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0));
			break;
		case 6:
			cron = CronBuilder.build(date, new IntervalTypeProvider(IntervalType.YEAR, Operation.All, 0, 0));
			//			cron = new CronBuilder().add(IntervalType.DAY_OF_MONTH, date.getDate()).add(IntervalType.SECONDS, 0).add(IntervalType.MINUTES, date.getMinutes()).add(IntervalType.HOURS, date.getHours()).build();
			break;
		//		case 7:
		//			cron = new CronBuilder().add(IntervalType.DAY_OF_MONTH, date.getDate()).add(IntervalType.DAY_OF_MONTH, 0).add(IntervalType.SECONDS, date.getSeconds()).add(IntervalType.MINUTES, date.getMinutes()).add(IntervalType.HOURS, date.getHours()).build();
		//			break;

		default:
			cron = CronBuilder.build(date);
			break;
		}
		return cron;
	}

	/***
	 * * *
	 * 
	 * @CREATE ON 2018年08月20日 上午9:58:34
	 * @EMAIL:20796698@QQ.COM
	 *
	 * @author CHEUK.BIN.LI
	 * @see 位置类型
	 */
	static enum IntervalType {
		YEAR(1970, 2099), MONTH(1, 12), DAY_OF_MONTH(1, 31), DAY_OF_WEEK(1, 7), HOURS(0, 23), MINUTES(0, 59), SECONDS(0, 59);

		int greaterThan;
		int lessThan;

		boolean check(int value) {
			return (value > greaterThan && value < lessThan);
		}

		boolean check(int... values) {
			for (int i : values) {
				if (i < greaterThan || i > lessThan)
					return false;
			}
			return true;
		}

		IntervalType(int greaterThan, int lessThan) {
			this.greaterThan = greaterThan;
			this.lessThan = lessThan;
		}

		public int getGreaterThan() {
			return greaterThan;
		}

		public int getLessThan() {
			return lessThan;
		}
	}

	/***
	 * 例:如果在day使用5L,意味着在最后的一个星期四触发
	 * 
	 * @param day
	 * @return
	 */
	public CronBuilder setLastDayOfWeek(int day) {
		if (day < 1 || day > 7)
			return this;
		(this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, true)).add(day + Operation.LAST.value);
		return this;
	}

	/***
	 * 当月最后一天
	 * 
	 * @return
	 */
	public CronBuilder setLastDayOfMonth() {
		checkedAndClearOrCreateList(this.dayofMonth, true).add(Operation.LAST.value);
		return this;
	}

	public CronBuilder setWorkingDay() {
		checkedAndClearOrCreateList(this.dayofWeek, true).add(Operation.WORK_DAY.value);
		return this;
	}

	/***
	 * 设置指定时间 从开始基数起(start)，每隔多少基数(interval)，触发
	 * 
	 * @param start
	 * @param interval
	 * @param intervalType
	 * @return
	 */
	public final CronBuilder setIntervalOf(int start, int interval, IntervalType intervalType) {
		List<String> result;
		switch (intervalType) {
		case YEAR:
			result = this.year = checkedAndClearOrCreateList(this.year, true);
			break;
		case MONTH:
			result = this.month = checkedAndClearOrCreateList(this.month, true);
			break;
		case DAY_OF_MONTH:
			result = this.dayofMonth = checkedAndClearOrCreateList(this.dayofMonth, true);
			break;
		case DAY_OF_WEEK:
			result = this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, true);
			break;
		case HOURS:
			result = this.hours = checkedAndClearOrCreateList(this.hours, true);
			break;
		case MINUTES:
			result = this.minutes = checkedAndClearOrCreateList(this.minutes, true);
			break;
		case SECONDS:
			result = this.seconds = checkedAndClearOrCreateList(this.seconds, true);
			break;
		default:
			return this;
		}
		result.add(start + Operation.INTERVAL.value + interval);

		return this;
	}

	/***
	 * 数量A至数量B
	 * 
	 * @param a
	 * @param b
	 * @param intervalType
	 * @return
	 */
	public CronBuilder addAToB(int a, int b, IntervalType intervalType) {
		if (intervalType.check(a, b))
			return this;
		List<String> result;
		switch (intervalType) {
		case YEAR:
			result = this.year = checkedAndClearOrCreateList(this.year, false);
			break;
		case MONTH:
			result = this.month = checkedAndClearOrCreateList(this.month, false);
			break;
		case DAY_OF_MONTH:
			result = this.dayofMonth = checkedAndClearOrCreateList(this.dayofMonth, false);
			break;
		case DAY_OF_WEEK:
			result = this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, false);
			break;
		case HOURS:
			result = this.hours = checkedAndClearOrCreateList(this.hours, false);
			break;
		case MINUTES:
			result = checkedAndClearOrCreateList(this.minutes, false);
			break;
		case SECONDS:
			result = this.seconds = checkedAndClearOrCreateList(this.seconds, false);
			break;
		default:
			return this;
		}
		result.add(a + Operation.TO.value + b);
		return this;
	}

	/***
	 * 添加数位数据
	 * 
	 * @param i
	 *            数量
	 * @param intervalType
	 *            数位
	 * @return
	 */
	public CronBuilder add(IntervalType intervalType, int... i) {
		//		List<String> result;
		switch (intervalType) {
		case YEAR:
			checkedCriticalPointAndAdd(1970, 2099, this.year = checkedAndClearOrCreateList(this.year, false), i);
			break;
		case MONTH:
			checkedCriticalPointAndAdd(0, 11, this.month = checkedAndClearOrCreateList(this.month, false), i);
			//			result = this.month = checkedAndClearOrCreateList(this.month, false);
			break;
		case DAY_OF_MONTH:
			checkedCriticalPointAndAdd(0, 31, this.dayofMonth = checkedAndClearOrCreateList(this.dayofMonth, false), i);
			//			result = this.dayofMonth = checkedAndClearOrCreateList(this.dayofMonth, false);
			break;
		case DAY_OF_WEEK:
			checkedCriticalPointAndAdd(1, 7, this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, false), i);
			//			result = this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, false);
			break;
		case HOURS:
			checkedCriticalPointAndAdd(0, 23, this.hours = checkedAndClearOrCreateList(this.hours, false), i);
			//			result = this.hours = checkedAndClearOrCreateList(this.hours, false);
			break;
		case MINUTES:
			checkedCriticalPointAndAdd(0, 59, this.minutes = checkedAndClearOrCreateList(this.minutes, false), i);
			//			result = this.minutes = checkedAndClearOrCreateList(this.minutes, false);
			break;
		case SECONDS:
			checkedCriticalPointAndAdd(0, 59, this.seconds = checkedAndClearOrCreateList(this.seconds, false), i);
			//			result = this.seconds = checkedAndClearOrCreateList(this.seconds, false);
			break;
		}
		return this;
	}

	protected List<String> checkedCriticalPointAndAdd(IntervalType intervalType, Operation operation, List<String> list, int a, int b) {
		if (null == list)
			return list;
		switch (operation) {
			case VALUE:
				if (intervalType.check(a)) {
					list.clear();
					list.add(Integer.toString(a));
				}
				break;
			case All:
				list.clear();
				break;
			case INTERVAL:
				if (intervalType.check(a, b)) {
					//				list.add((IntervalType.DAY_OF_WEEK == intervalType || IntervalType.DAY_OF_MONTH == intervalType) ? "?" : "*");
					list.clear();
					list.add(a + operation.value + b);
				}
				break;
			case OR:
				if (intervalType.check(a)) {
					list.add(Integer.toString(a));
				}
				break;
			case TO:
				if (intervalType.check(a, b)) {
					list.add(a + operation.value + b);
				}
				break;
			case IN:
				if (IntervalType.DAY_OF_WEEK == intervalType && intervalType.check(a, b)) {//最多6个星期
					list.add(a + operation.value + b);
				}
				break;
			case LAST:
				if ((IntervalType.DAY_OF_WEEK == intervalType || IntervalType.DAY_OF_MONTH == intervalType) && intervalType.check(a)) {
					list.add(a + operation.value + b);
				}
				break;
			case WORK_DAY:
				if (IntervalType.DAY_OF_WEEK == intervalType && intervalType.check(a)) {
					list.add(operation.value);
				}
				break;
			case PLACEHOLDER:
				if ((IntervalType.DAY_OF_WEEK == intervalType || IntervalType.DAY_OF_MONTH == intervalType)) {
					list.clear();
					list.add(operation.value);
				}
				break;
			default :
				list.clear();
				list.add(operation.ALL_MOTH.value);
				break;
		}
		return list;
	}

	protected List<String> checkedCriticalPointAndAdd(int greaterThan, int lessThan, List<String> list, int... values) {
		if (null == list || null == values)
			return list;
		for (int i : values) {
			if (i > lessThan || i < greaterThan)
				continue;
			list.add(Integer.toString(i));
		}
		return list;
	}

	String getValue(List<String> list, String defaultValue) {
		if (null == list || list.isEmpty()) {
			return null == defaultValue ? "*" : defaultValue;
		}
		StringBuilder result = new StringBuilder();
		list.forEach((item) -> {
			result.append(item).append(",");
		});
		return result.substring(0, result.length() - 1);
	}

	String getValue(List<String> list) {
		return getValue(list, null);
	}

	List<String> checkedAndClearOrCreateListAndAddDefaultValue(final List<String> list, boolean isClear, String... defaultValues) {
		List<String> result = list;
		if (null == result) {
			result = new LinkedList<String>();
		}
		if (isClear)
			result.clear();
		if ((list == null || list.isEmpty()) && null != defaultValues) {
			for (String item : defaultValues)
				result.add(item);
		}
		return result;
	}

	List<String> checkedAndClearOrCreateList(final List<String> list, boolean isClear) {
		return checkedAndClearOrCreateListAndAddDefaultValue(list, isClear);
	}

	public static class IntervalTypeProvider {
		private IntervalType type;
		private Operation operation;
		private int value1;
		private int value2;

		public IntervalTypeProvider setType(IntervalType type) {
			this.type = type;
			return this;
		}

		public IntervalTypeProvider setOperation(Operation operation) {
			this.operation = operation;
			return this;
		}

		public IntervalTypeProvider setValue1(int value) {
			this.value1 = value;
			return this;
		}

		public IntervalTypeProvider setValue2(int value) {
			this.value2 = value;
			return this;
		}

		public IntervalTypeProvider(IntervalType type, Operation operation, int value1, int value2) {
			this.type = type;
			this.operation = operation;
			this.value1 = value1;
			this.value2 = value2;
		}

		public IntervalTypeProvider() {
		}

		public IntervalType getType() {
			return type;
		}

		public Operation getOperation() {
			return operation;
		}

		public int getValue1() {
			return value1;
		}

		public int getValue2() {
			return value2;
		}
	}

	public static Cron build(Date date, IntervalTypeProvider... provider) {
		if (null == provider && null != date) {
			return new Cron(defaultFormat.format(date));
		}
		return new CronBuilder().fillValue(date, provider).build();

	}

	@SuppressWarnings("deprecation")
	CronBuilder fillValue(Date date, IntervalTypeProvider... provider) {
		if (null != date) {
			this.year = checkedAndClearOrCreateListAndAddDefaultValue(this.year, false, Integer.toString(date.getYear() + 1900));
			this.month = checkedAndClearOrCreateListAndAddDefaultValue(this.month, false, Integer.toString(date.getMonth() + 1));
			this.dayofMonth = checkedAndClearOrCreateListAndAddDefaultValue(this.dayofMonth, false, Integer.toString(date.getDate()));
			this.hours = checkedAndClearOrCreateListAndAddDefaultValue(this.hours, false, Integer.toString(date.getHours()));
			this.minutes = checkedAndClearOrCreateListAndAddDefaultValue(this.minutes, false, Integer.toString(date.getMinutes()));
			this.seconds = checkedAndClearOrCreateListAndAddDefaultValue(this.seconds, false, Integer.toString(date.getSeconds()));
			this.dayofWeek = checkedAndClearOrCreateListAndAddDefaultValue(this.dayofWeek, false, Integer.toString(date.getDay()));
		}
		for (IntervalTypeProvider item : provider) {
			switch (item.getType()) {
			case YEAR:
				checkedCriticalPointAndAdd(item.type, item.operation, this.year = checkedAndClearOrCreateList(this.year, false), item.value1, item.value2);
				break;
			case MONTH:
				checkedCriticalPointAndAdd(item.type, item.operation, this.month = checkedAndClearOrCreateList(this.month, false), item.value1, item.value2);
				break;
			case DAY_OF_MONTH:
				checkedCriticalPointAndAdd(item.type, item.operation, this.dayofMonth = checkedAndClearOrCreateList(this.dayofMonth, false), item.value1, item.value2);
				break;
			case DAY_OF_WEEK:
				checkedCriticalPointAndAdd(item.type, item.operation, this.dayofWeek = checkedAndClearOrCreateList(this.dayofWeek, false), item.value1, item.value2);
				break;
			case HOURS:
				checkedCriticalPointAndAdd(item.type, item.operation, this.hours = checkedAndClearOrCreateList(this.hours, false), item.value1, item.value2);
				break;
			case MINUTES:
				checkedCriticalPointAndAdd(item.type, item.operation, this.minutes = checkedAndClearOrCreateList(this.minutes, false), item.value1, item.value2);
				break;
			case SECONDS:
				checkedCriticalPointAndAdd(item.type, item.operation, this.seconds = checkedAndClearOrCreateList(this.seconds, false), item.value1, item.value2);
				break;
			}
		}
		return this;
	}

	/***
	 * 校验日期循环 是否是循环任务
	 * 
	 * @param cron
	 * @return ，循环返回值为Long.MAX_VALUE; 否即返回当任务最后一次执行时间
	 * @throws ParseException
	 */
	public static long checkCronLoop(String cron) throws ParseException {
		String[] crons = cron.split(" ");
		if (crons.length < 6)
			return System.currentTimeMillis();
		// 每年运行
		if (crons.length == 6)
			return Long.MAX_VALUE;

		String item;
		String[] subItems;
		LinkedList<Integer> sort = null;
		String splitChar;
		StringBuilder date = new StringBuilder();
		LinkedList<String> cronItem = new LinkedList<>();
		for (int i = 0, len = crons.length; i < len; i++) {
			item = crons[i];
			//			（1） Seconds Minutes Hours DayofMonth Month DayofWeek Year
			if (item.contains("*") || item.contains("W") || item.contains("/") || item.contains("?")) {
				switch (i) {
				case 0:
					cronItem.add(Integer.toString(IntervalType.SECONDS.lessThan));
					continue;
				case 1:
					cronItem.add(Integer.toString(IntervalType.MINUTES.lessThan));
					continue;
				case 2:
					cronItem.add(Integer.toString(IntervalType.HOURS.lessThan));
					continue;
				case 3:
					cronItem.add(Integer.toString(IntervalType.DAY_OF_MONTH.greaterThan));
					continue;
				case 4:
					cronItem.add(Integer.toString(IntervalType.MONTH.lessThan));
					continue;

				case 6:
					return Long.MAX_VALUE;

				default:
					break;
				}
			}
			if (item.contains((splitChar = "-")) || item.contains((splitChar = ","))) {
				subItems = item.split(splitChar);
				if (null == sort) {
					sort = new LinkedList<>();
				} else {
					sort.clear();
				}
				for (String subItem : subItems) {
					sort.add(Integer.valueOf(subItem));
				}
				Collections.sort(sort);
				cronItem.add(Integer.toString(sort.getLast()));
			} else {
				cronItem.add(item);
			}
		}
		for (int i = 0, len = cronItem.size(); i < len; i++) {
			if (i == 5)
				continue;
			date.append(cronItem.get(i)).append(" ");
		}
		if (cronItem.get(3).equals(Integer.toString(IntervalType.DAY_OF_MONTH.greaterThan))) {
			Calendar ca = Calendar.getInstance();
			ca.setTime(defaultDateFormat.parse(date.substring(0, date.length() - 1)));
			ca.add(Calendar.MONTH, 1);
			ca.add(Calendar.DATE, -1);
			//			ca.add(Calendar.DATE, -1);
			return ca.getTimeInMillis();
		} else
			return defaultDateFormat.parse(date.substring(0, date.length() - 1)).getTime();

	}

	public static void main(String[] args) throws ParseException {

//		Cron cron =CronBuilder.buildCronByString("0 00 9 ? * MON");
//		CronBuilder.checkCronLoop("0 00 9 ? * MON");
//		System.out.println(CronBuilder.checkCronLoop("0 00 9 ? * MON 2020"));
//				new Cron().converty(new Date(), 0);
//				CronBuilder c = new CronBuilder();
//				c.setLastDayOfWeek(4).add(IntervalType.MONTH, 31, 24, 11, 12, 10).add(IntervalType.DAY_OF_WEEK, DayOfWeek.MON, DayOfWeek.THU).addAToB(11, 23, IntervalType.HOURS);
		//		System.err.println(c.build());
		//		System.err.println(CronBuilder.build(new Date(), new IntervalTypeProvider(IntervalType.MONTH, Operation.OR, 12, 13), new IntervalTypeProvider(IntervalType.MONTH, Operation.OR, 5, 12)));
		//		System.out.println(defaultFormat.format(new Date(CronBuilder.checkCronLoop("0 0/30 23 31 * ? *"))));
		//
		//		System.out.println(defaultDateFormat.format(new Date(CronBuilder.checkCronLoop("0 24,54 * 5 9 ? 2018"))));
		//		System.out.println(new Date(CronBuilder.checkCronLoop("0 24,54 * 5 9 ? 2018")).getTime());
		//				Calendar c = Calendar.getInstance();
		//				c.add(Calendar.DATE, 3);
		//				System.out.println(new Date(CronBuilder.checkCronLoop("0 0/1 * * * ?")).getTime());
//						System.out.println(CronBuilder.buildSimpleCron(new Date(), 0).getCron());
//						System.out.println(CronBuilder.buildSimpleCron(new Date(), 1).getCron());
//						System.out.println(CronBuilder.buildSimpleCron(new Date(), 2).getCron());
//						System.out.println(CronBuilder.buildSimpleCron(new Date(), 3).getCron());
//						System.out.println(CronBuilder.buildSimpleCron(new Date(), 4).getCron());
		//				System.out.println(CronBuilder.buildSimpleCron(new Date(), 5).getCron());
		//				System.out.println(CronBuilder.buildSimpleCron(new Date(), 6).getCron());
		//				System.out.println(CronBuilder.buildSimpleCron(new Date(), 7).getCron());

	}
}
