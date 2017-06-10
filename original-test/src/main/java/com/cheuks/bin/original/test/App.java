package com.cheuks.bin.original.test;

import java.net.URLEncoder;

import org.junit.Test;

/**
 * Hello world!
 *
 */
public class App {
	// public static void main(String[] args) {
	// ApplicationContext ac = new ClassPathXmlApplicationContext("application-config.xml");
	// DoThing doThing = (DoThing) ac.getBean("doThing");
	// doThing.MM();
	// }

	@Test
	public void httpsEncode() {
		String str = "https://www.baidu.com/";
		System.out.println(URLEncoder.encode(str));
	}
	
	@Test
	public void x() {
		StringBuffer sql = new StringBuffer("select m.order_type,m.original_order_id order_id ,m.order_code,'MALL' s ource_system,");
        sql.append("t.voucher_key_id,t.voucher_id,t.voucher_type,t.voucher_return_code ,t.lock_amount,t.tax_amount ");
        sql.append("from " + "###" + " m ");
        sql.append("left join  sm_order_balance_payment_rel r on m.id = r.relate_id ");
        sql.append("left join  sm_order_balance_payment p on  r.payment_id = p.id " );
        sql.append("left join  sm_consumer_voucher t on  m.relate_id = t.relate_id ");
        sql.append("left join  sm_after_sale_order so on  m.order_id = so.order_id and t.voucher_key_id = so.voucher_key_id and so.status = 2 ");
        sql.append("where p.id = ? and m.check_status = ? and r.status=? and t.lock_amount is not null");
		System.out.println(sql);
	}

}
