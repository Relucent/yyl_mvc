package yyl.mvc.test.plug;

import java.util.Arrays;

import yyl.mvc.core.plug.mybatis.PageList;
import yyl.mvc.core.util.json.JsonUtil;

public class PageListTest {
	public static void main(String[] args) {
		PageList<Integer> page = new PageList<>();
		page.setStart(0);
		page.setLimit(10);
		page.setTotal(20);
		page.setRecords(Arrays.asList(0, 1, 2, 3, 4, 6, 7, 8, 9));

		String json = JsonUtil.encode(page);
		System.out.println(json);
	}
}
