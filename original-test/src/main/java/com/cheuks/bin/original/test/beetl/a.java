package com.cheuks.bin.original.test.beetl;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.MapResourceLoader;

public class a {

	public static void main(String[] args) throws IOException {
		String a = "1<%if(has(id)){%>AND id=:id <%}%>\n" + "<%if(has(fundraisingId)){%>AND fundraisingId=:fundraisingId <%}%>\n" + "<%if(has(weiXinInfoId)){%>AND weiXinInfoId=:weiXinInfoId <%}%>\n" + "<%if(has(payAmount)){%>AND payAmount=:payAmount <%}%>" + "<%if(has(unifiedOrderId)){%>AND unifiedOrderId=:unifiedOrderId <%}%>\n" + "<%if(has(status)){%>AND status=:status <%}%>\n" + "<%if(has(blessingMessage)){%>AND blessingMessage=:blessingMessage <%}%>\n"
				+ "<%if(has(completeDate)){%>AND completeDate=:completeDate <%}%>\n" + "<%if (has(orderby)) {%>ORDER BY A.${orderby} \n" + "	<%if (has(sort)){%>  ${sort}" + "	<%}else{%> ASC<%}%>\n" + "<%}%> 2";

		Configuration configuration = Configuration.defaultConfiguration();
		MapResourceLoader resourceLoader = new MapResourceLoader();
		GroupTemplate groupTemplate = new GroupTemplate(resourceLoader, configuration);

		resourceLoader.put("a", a);
		Template t = groupTemplate.getTemplate("a");
		System.err.println(t.render());

	}
}
