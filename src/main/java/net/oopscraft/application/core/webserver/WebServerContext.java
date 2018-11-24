package net.oopscraft.application.core.webserver;

import java.util.LinkedHashMap;
import java.util.Map;

public class WebServerContext {
	
	String contextPath = null;
	String resourceBase = null;
	String descriptor = null;
	Map<String, String> parameter = new LinkedHashMap<String, String>();

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

	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(Map<String, String> parameter) {
		this.parameter = parameter;
	}

	public void setParameter(String name, String value) {
		this.parameter.put(name, value);
	}

	public String getParameter(String name) {
		return this.parameter.get(name);
	}
}
