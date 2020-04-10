package com.cheuks.bin.original.rmi;

import com.cheuks.bin.original.rmi.t.TestModel;
import com.cheuks.bin.original.rmi.t.test2I;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RmiServer {

    @Autowired
    private test2I test2i;

    public static void main(String[] args) throws InterruptedException, IOException {

        // PropertyConfigurator.configure("log4j.properties");
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-config.xml");
        applicationContext.start();

        // SimpleRmiService simpleRmiService = applicationContext.getBean(SimpleRmiService.class);
        //
        // SimpleRmiBeanFactory factory = applicationContext.getBean(SimpleRmiBeanFactory.class);
        //
        // Thread.sleep(5000);
        // // test2I t = (test2I) applicationContext.getBean("CCTV2");
        // // System.err.println(t.a3("xxx", 10));
        //

        final RmiServer rmiServer = applicationContext.getBean(RmiServer.class);
        boolean isClient = args.length > 1;
        // if (!applicationContext.containsBean(RmiContant.BEAN_RMI_NETWORK_SERVER) && isClient) {
        // System.err.println(rmiServer.test2i.a4("xxxxxxxxxxxxx"));
        // NettyRmiInvokeClientImpl r=applicationContext.getBean(NettyRmiInvokeClientImpl.class);
        // System.err.println(r);

        final String l4k = "{\"ecOrderH\":{\"fee_reim_return_code\":\"HHT183377465450692608\",\"company_id\":\"11006\",\"bank_name\":\"招商银行股份有限公司广州淘金支行\",\"receiver\":\"广州市中航服商务管理有限公司\",\"remark\":\"慧通:HHT183377465450692608付款申请:350.00\",\"apply_amount\":350.00,\"tenant_id\":\"81920358843678720\",\"sensitive_info\":null,\"user_email\":\"\",\"ecOrderLs\":[{\"amount\":100.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"18906913\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000351\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000351\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":300.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10081\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":350.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10082\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":270.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10083\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null}],\"source_order_return_code\":\"HHT183377465450692608\",\"bank_return_code\":null,\"ecOrderAddLs\":[{\"amount\":-350.000,\"voucher_type\":null,\"voucher_id\":\"10082\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"},{\"amount\":-300.000,\"voucher_type\":null,\"voucher_id\":\"10081\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"},{\"amount\":-20.000,\"voucher_type\":null,\"voucher_id\":\"10083\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"}],\"user_id\":\"11146956\",\"bank_account\":\"201181289110001\",\"source_system\":\"SMART-BUY\",\"ecTaxLs\":[],\"source_order_url\":\"https://jiebaoyun.midea.com/jiebao-plus/#/smart_buy_account/statements/search/183377465450692608\",\"vendor_id\":\"3850\",\"is_month_pay\":\"Y\",\"reim_data\":\"2017-09-11\",\"currency_return_code\":\"CNY\",\"source_order_id\":\"183377465450692608\",\"vendor_site_id\":\"1928726\",\"source_order_type\":\"YJ-HOTEL\",\"order_type\":\"TY_PUB\"},\"requestBody\":\" {\"ecOrderH\":{\"fee_reim_return_code\":\"EC1231231\",\"tenant_id\":\"8758756\",\"user_id\":\"13630189348\",\"user_email\":\"553290835@qq.com\",\"reim_data\":\"2016-01-01\",\"company_id\":\"12131\",\"currency_return_code\":\"CNY\",\"apply_amount\":12,\"sensitive_info\":\"wwww\",\"remark\":\"wwww\",\"receiver\":\"1111\",\"bank_name\":\"2222\",\"bank_account\":\"qwqw\",\"bank_return_code\":\"qwe2\",\"order_type\":\"TY_PUB\",\"vendor_id\":\"1212\",\"vendor_site_id\":\"324324\",\"is_month_pay\":\"Y\",\"source_system\":\"SMART-BUY\",\"source_order_type\":\"xxx\",\"source_order_id\":\"1212\",\"source_order_return_code\":\"2123\",\"source_order_url\":\"http://2121\",\"ecOrderLs\":[{\"voucher_type\":\"EA\",\"voucher_id\":\"2131\",\"order_return_code\":\"21323\",\"order_type\":\"1234324\",\"source_system\":\"SMART-BUY\",\"lock_return_code\":\"xxxxx\",\"amount\":2,\"tax_amount\":1.2,\"invoice_maker\":\"广东爱米高家具有限公司\",\"invoice_num\":\"988888\",\"tax_rate\":\"8%\",\"invoice_date\":\"2015-10-11\"}],\"ecOrderAddLs\":[{\"voucher_type\":\"EA\",\"voucher_id\":\"2131\",\"order_return_code\":\"21323\",\"order_type\":\"1234324\",\"source_system\":\"SMART-BUY\",\"amount\":2}],\"ecTaxLs\":[{\"tax_amount\":1.2,\"invoice_maker\":\"广东爱米高家具有限公司\",\"invoice_num\":\"988888\",\"tax_rate\":\"8%\",\"invoice_date\":\"2015-10-11\",\"tax_fee_type_id\":\"1212121\"}]}}\",\"apiMethodName\":\"midea.ExpenseBuy007Request.POST.request\",\"responseClass\":\"com.midea.api.response.expenseBuy007.ExpenseBuy007Response\",\"textParams\":{}}";

        // Thread.sleep(10000);
        // Long now = System.currentTimeMillis();
        // System.err.println(rmiServer.test2i.a4("{\"ecOrderH\":{\"fee_reim_return_code\":\"HHT183377465450692608\",\"company_id\":\"11006\",\"bank_name\":\"招商银行股份有限公司广州淘金支行\",\"receiver\":\"广州市中航服商务管理有限公司\",\"remark\":\"慧通:HHT183377465450692608付款申请:350.00\",\"apply_amount\":350.00,\"tenant_id\":\"81920358843678720\",\"sensitive_info\":null,\"user_email\":\"\",\"ecOrderLs\":[{\"amount\":100.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"18906913\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000351\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000351\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":300.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10081\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":350.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10082\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null},{\"amount\":270.00,\"voucher_type\":\"BUDGET\",\"voucher_id\":\"10083\",\"invoice_maker\":null,\"tax_amount\":0.00,\"lock_return_code\":\"ff8080815dadfaa4015e2912cddf16b8|HHT20170822000352\",\"invoice_date\":null,\"order_return_code\":\"HHT20170822000352\",\"invoice_num\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\",\"tax_rate\":null}],\"source_order_return_code\":\"HHT183377465450692608\",\"bank_return_code\":null,\"ecOrderAddLs\":[{\"amount\":-350.000,\"voucher_type\":null,\"voucher_id\":\"10082\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"},{\"amount\":-300.000,\"voucher_type\":null,\"voucher_id\":\"10081\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"},{\"amount\":-20.000,\"voucher_type\":null,\"voucher_id\":\"10083\",\"order_return_code\":null,\"order_type\":\"HOTEL\",\"source_system\":\"SMART-BUY\"}],\"user_id\":\"11146956\",\"bank_account\":\"201181289110001\",\"source_system\":\"SMART-BUY\",\"ecTaxLs\":[],\"source_order_url\":\"https://jiebaoyun.midea.com/jiebao-plus/#/smart_buy_account/statements/search/183377465450692608\",\"vendor_id\":\"3850\",\"is_month_pay\":\"Y\",\"reim_data\":\"2017-09-11\",\"currency_return_code\":\"CNY\",\"source_order_id\":\"183377465450692608\",\"vendor_site_id\":\"1928726\",\"source_order_type\":\"YJ-HOTEL\",\"order_type\":\"TY_PUB\"},\"requestBody\":\"
        // {\"ecOrderH\":{\"fee_reim_return_code\":\"EC1231231\",\"tenant_id\":\"8758756\",\"user_id\":\"13630189348\",\"user_email\":\"553290835@qq.com\",\"reim_data\":\"2016-01-01\",\"company_id\":\"12131\",\"currency_return_code\":\"CNY\",\"apply_amount\":12,\"sensitive_info\":\"wwww\",\"remark\":\"wwww\",\"receiver\":\"1111\",\"bank_name\":\"2222\",\"bank_account\":\"qwqw\",\"bank_return_code\":\"qwe2\",\"order_type\":\"TY_PUB\",\"vendor_id\":\"1212\",\"vendor_site_id\":\"324324\",\"is_month_pay\":\"Y\",\"source_system\":\"SMART-BUY\",\"source_order_type\":\"xxx\",\"source_order_id\":\"1212\",\"source_order_return_code\":\"2123\",\"source_order_url\":\"http://2121\",\"ecOrderLs\":[{\"voucher_type\":\"EA\",\"voucher_id\":\"2131\",\"order_return_code\":\"21323\",\"order_type\":\"1234324\",\"source_system\":\"SMART-BUY\",\"lock_return_code\":\"xxxxx\",\"amount\":2,\"tax_amount\":1.2,\"invoice_maker\":\"广东爱米高家具有限公司\",\"invoice_num\":\"988888\",\"tax_rate\":\"8%\",\"invoice_date\":\"2015-10-11\"}],\"ecOrderAddLs\":[{\"voucher_type\":\"EA\",\"voucher_id\":\"2131\",\"order_return_code\":\"21323\",\"order_type\":\"1234324\",\"source_system\":\"SMART-BUY\",\"amount\":2}],\"ecTaxLs\":[{\"tax_amount\":1.2,\"invoice_maker\":\"广东爱米高家具有限公司\",\"invoice_num\":\"988888\",\"tax_rate\":\"8%\",\"invoice_date\":\"2015-10-11\",\"tax_fee_type_id\":\"1212121\"}]}}\",\"apiMethodName\":\"midea.ExpenseBuy007Request.POST.request\",\"responseClass\":\"com.midea.api.response.expenseBuy007.ExpenseBuy007Response\",\"textParams\":{}}"));
        // System.out.println((System.currentTimeMillis() - now) + "ms");
        System.err.println("准备");
        for (int i = 0; i < 3; i++) {
            System.err.println(i + 1);
            Thread.sleep(1000);
        }
        System.out.println("开始");
        final ExecutorService e = Executors.newCachedThreadPool();

        int count = 10;
        int weight = 1;
        if (null != args && args.length > 0) {
            weight = Integer.valueOf(args[0]);
            count = Integer.valueOf(args[1]);
        }
        final CountDownLatch countDownLatch = new CountDownLatch(weight);
        System.out.println("count:" + count + " weight:" + weight);
        Long now = System.currentTimeMillis();
        // 1M文件
        // InputStreamReader in = new InputStreamReader(RmiServer.class.getClassLoader().getSystemResourceAsStream("a.txt"));
        // int code;
        // StringBuilder sb = new StringBuilder();
        // while (-1 != (code = in.read())) {
        // sb.append(code);
        // }
        // String fileA = sb.toString();
        final TestModel testModel = new TestModel(10088, "叼拿升", "普通实体类测试", l4k, Arrays.asList("小明", "小红", "小蓝", "小黄", "小青", "小紫"));

        for (int i = 0; i < weight; i++) {
            rmiServer.runX(e, countDownLatch, count, rmiServer, testModel);
        }
        countDownLatch.await();
        System.out.println((System.currentTimeMillis() - now) + "ms");
        // }
    }

    private void runX(ExecutorService e, final CountDownLatch countDownLatch, final int count, final RmiServer rmiServer, final Object... param) {
        e.execute(new Runnable() {
            public void run() {
                for (int i = 0; i < count; i++) {
                    // if (null == rmiServer.test2i.a4(l4k)) {
                    // System.err.println("null");
                    // }
                    rmiServer.test2i.getTestModel((TestModel) param[0]);
                }
                System.out.println(count);
                countDownLatch.countDown();
            }
        });
    }

}
