package net.oopscraft.application.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebServerContext {
	
	String contextPath = null;
	String resourceBase = null;
	String descriptor = null;
	Map<String, String> parameters = new LinkedHashMap<String, String>();

	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}

	public String getResourceBase() {
		return resourceBase;
	}

	public void setResourceBase(String resourceBase) {
		this.resourceBase = resourceBase;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> initParameters) {
		this.parameters = initParameters;
	}

	public void addParameter(String name, String value) {
		this.parameters.put(name, value);
	}

	public String getParameter(String name) {
		return this.parameters.get(name);
	}
}
