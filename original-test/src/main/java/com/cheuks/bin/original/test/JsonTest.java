package com.cheuks.bin.original.test;

import java.math.BigDecimal;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class JsonTest {

	private static String a = "";
	public static void main(String[] args) {
		JsonParser jsonParser = new JsonParser();
		JsonArray array = jsonParser.parse(a).getAsJsonArray();
		float total = 0;
		float x = 0;
		float temp = 0;
		for (int i = 0, len = array.size(); i < len; i++) {
			if ((temp = array.get(i).getAsJsonObject().get("tax_amount").getAsFloat()) < 0) {
				x += temp;
			} else {
				total += temp;
			}
		}
		System.out.println(x + "  : " + (total + 273.37));
		System.err.println(BigDecimal.valueOf(0D).compareTo(BigDecimal.ZERO) < 0);
		System.err.println(BigDecimal.valueOf(11D).multiply(BigDecimal.valueOf(-1D)).intValue());

		StringBuffer sql = new StringBuffer("select m.order_type,m.order_id ,m.order_id as order_code,'SMART-BUY' source_system,");
		sql.append("t.voucher_key_id,t.voucher_id,t.voucher_type,t.voucher_return_code ,t.lock_amount,t.tax_amount ");
		sql.append("from xxxxxxxxx m ");
		sql.append("left join  sm_order_balance_payment_rel r on m.id = r.relate_id ");
		sql.append("left join  sm_order_balance_payment p on  r.payment_id = p.id ");
		sql.append("left join  sm_consumer_voucher t on  m.relate_id = t.relate_id  ");
		sql.append("where p.id = ? and m.check_status = ? and r.status=? and t.lock_amount is not null");
		System.out.println(sql.toString());
	}

}
