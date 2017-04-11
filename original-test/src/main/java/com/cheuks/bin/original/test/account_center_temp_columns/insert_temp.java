package com.cheuks.bin.original.test.account_center_temp_columns;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class insert_temp {

	public static String insertTemp(Long id, String templateCode, String templateName, String templaceSheetName, String templateDesc, String tempType) {

		String sql = String.format(
				"INSERT INTO `accountcenter`.`sm_order_balance_template` (`id`, `template_code`, `template_name`, `template_gentype`, `template_path`, `template_type`, `template_fname`, `template_sheet_name`, `template_desc`, `status`, `created_name`, `created_by`, `creation_date`, `last_updated_by`, `last_updated_name`, `last_update_date`) VALUES ('%d', '%s', '%s', '1', NULL, '%s', '%s.%s', '%s', '%s', '1', '1', '1', '2017-03-24 10:29:29', '1', '1', '2017-03-24 10:29:33');",
				id, templateCode, templateName, tempType, templateName, tempType, templaceSheetName, templateDesc);
		return sql;
	}

	public static String insertTempaceForColumns(Long parentId, Long id, JsonArray jsonArray) {

		String sql = "INSERT INTO `accountcenter`.`sm_order_balance_template_col` (`id`, `template_id`, `col_name`, `col_code`, `col_type`, `col_is_formate`, `col_format`, `col_index`, `col_desc`, `created_name`, `created_by`, `creation_date`, `last_updated_by`, `last_updated_name`, `last_update_date`, `attribute1`, `attribute2`, `attribute3`, `attribute4`, `attribute5`, `status`) VALUES ('%d', '%d', '%s', '%s', '1', '0', NULL, '%s', '%s', '1', '1', '2017-03-24 09:09:33', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1');";

		StringBuilder sb = new StringBuilder();
		int index = 1;
		for (JsonElement obj : jsonArray) {
			JsonObject jsonObject = obj.getAsJsonObject();
			sb.append(String.format(sql, id++, parentId, jsonObject.get("displayName").getAsString(), jsonObject.get("field").getAsString(), index++, jsonObject.get("displayName").getAsString())).append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {

		//机票、滴滴
		System.out.println(insertTemp(100L, "JP_STATISTICS_ORDER", "统计查询机票订单导出", "机票订单", "统计查询机票订单导出", "xls"));
		System.out.println(insertTemp(101L, "CAR_STATISTICS_ORDER", "统计查询滴滴订单导出", "滴滴订单", "统计查询机滴滴单导出", "xls"));

		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray;
		long len = 170L;
		//字段
		String jp = "[{field:'order_id',displayName:'订单ID'},{field:'vendor_order_id',displayName:'商家订单ID'},{field:'issue_date',displayName:'订单日期'},{field:'order_desc',displayName:'订单描述'},{field:'order_inst_type_name',displayName:'订单实例类型'},{field:'busi_org_name',displayName:'预算部门'},{field:'company_name',displayName:'入账单位'},{field:'invoice_head',displayName:'发票抬头'},{field:'created_name',displayName:'创建人'},{field:'check_status_name',displayName:'对账状态'},{field:'created_by',displayName:'联系人'},{field:'passenger_name',displayName:'乘机人'},{field:'lock_amount',displayName:'预算锁定金额'},{field:'settle_amount',displayName:'订单金额'},{field:'ticket_fee',displayName:'机票价格'},{field:'airport_fee',displayName:'机场建设费'},{field:'oil_fee',displayName:'燃油费'},{field:'service_fee',displayName:'服务费'},{field:'change_fee',displayName:'退改费用'},{field:'insurance_fee',displayName:'保险费'},{field:'order_id',displayName:'订单ID'},{field:'order_status_name',displayName:'订单状态'},{field:'supplier_id',displayName:'供应商ID'},{field:'merchant_name',displayName:'商家'}]";
		jsonArray = jsonParser.parse(jp).getAsJsonArray();
		len += jsonArray.size();
		System.out.println(insertTempaceForColumns(100L, 170L, jsonArray));
		//滴滴
		String car = "[{field:'order_id',displayName:'订单ID'},{field:'pay_time',displayName:'商家确认时间'},{field:'busi_org_name',displayName:'预算部门'},{field:'created_name',displayName:'下单人'},{field:'passenger_phone',displayName:'乘客手机号'},{field:'reason',displayName:'用车事由'},{field:'total_amount',displayName:'订单金额'},{field:'voucher_resc',displayName:'预算券名称'},{field:'lock_amount',displayName:'预算锁定金额'},{field:'check_status_name',displayName:'对账状态'},{field:'order_status_name',displayName:'订单状态'},{field:'create_time',displayName:'下单时间'},{field:'order_type_name',displayName:'订单类型'},{field:'supplier_id',displayName:'供应商ID'},{field:'merchant_name',displayName:'商家'},{field:'departure_time',displayName:'启程时间'},{field:'start_name',displayName:'出发地'},{field:'start_address',displayName:'出发地址'},{field:'finish_time',displayName:'完成时间'},{field:'end_name',displayName:'目的地'},{field:'end_address',displayName:'目的地地址'},{field:'normal_distance',displayName:'行车距离'}]";
		jsonArray = jsonParser.parse(car).getAsJsonArray();
		System.out.println(insertTempaceForColumns(101L, Long.valueOf(len + 20), jsonArray));

	}

}
