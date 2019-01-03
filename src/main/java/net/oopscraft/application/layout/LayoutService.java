package net.oopscraft.application.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.oopscraft.application.layout.repository.LayoutRepository;

@Service
public class LayoutService {

	@Autowired
	LayoutRepository layoutRepository;
	
	public Layout getLayout(String id) throws Exception {
		return layoutRepository.findOne(id);
	}
	
	public Layout getAvailableLayout(String id) throws Exception {
		if(id == null) {
			Layout layout = new Layout();
			layout.setHeaderPage("/WEB-INF/view/header.jsp");
			layout.setFooterPage("/WEB-INF/view/footer.jsp");
			return layout;
		}else{
			return layoutRepository.findOne(id);
		}
	}
}
