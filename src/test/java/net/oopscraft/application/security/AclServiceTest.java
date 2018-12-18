package net.oopscraft.application.security;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.oopscraft.application.ApplicationTestRunnerWithSpring;
import net.oopscraft.application.core.PageInfo;
import net.oopscraft.application.core.TextTable;

public class AclServiceTest extends ApplicationTestRunnerWithSpring {
	
	@Autowired
	AclService aclService;
	
	@Test
	public void test() throws Exception {
		AclService.SearchCondition searchCondition = aclService.new SearchCondition();
		PageInfo pageInfo = new PageInfo(1,10);
		List<Acl> acls = aclService.getAcls(searchCondition, pageInfo);
		System.out.println(new TextTable(acls));
		assert(true);
	}

}
