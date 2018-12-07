package net.oopscraft.application;

public class ApplicationTest {
	
	public Application application;

	public ApplicationTest() throws Exception {
		application = ApplicationContainer.getApplication();
	}
	
	public Application getApplication() {
		return application;
	}

}
