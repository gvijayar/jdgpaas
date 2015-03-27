package org.jboss.as.quickstarts.datagrid.jaxrs;

import java.util.Set;
import java.util.HashSet;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.jboss.as.quickstarts.datagrid.jaxrs.services.CacheQueryService;

@ApplicationPath("/rest")
public class RestEndpointsManager extends Application {

	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public RestEndpointsManager(){
	     singletons.add(new CacheQueryService());
	}
	
	@Override
	public Set<Class<?>> getClasses() {
	     return empty;
	}
	@Override
	public Set<Object> getSingletons() {
	     return singletons;
	}
}
