package net.oopscraft.application.page;

import org.springframework.stereotype.Service;

@Service
public class PageService {
	
	public Page getPage(String id) throws Exception {
		Page page = new Page();
		page.setId(id);
		page.setName("page name");
		page.setType(Page.Type.IFRAME);
		page.setValue("http://www.google.co.kr");
		return page;
	}

}