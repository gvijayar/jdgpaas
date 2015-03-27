package org.jboss.as.quickstarts.datagrid.jaxrs.services;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.query.CacheQuery;
import org.infinispan.query.Search;
import org.jboss.as.quickstarts.datagrid.carmart.model.Car;
import org.jboss.as.quickstarts.datagrid.carmart.session.CacheContainerProvider;
import org.jboss.as.quickstarts.datagrid.carmart.session.CarManager;
import org.jboss.as.quickstarts.datagrid.jaxrs.ServicesLookupManager;

@Path("/query")
public class CacheQueryService{

	@Inject
	private CacheContainerProvider provider;

	@GET
	@Path("/or")
	@Produces({ "application/xml" })
	public String searchEntity(@QueryParam("brand") String brand, @QueryParam("color") String color){

		Cache<Object, Object> carCache = getCarCache();
		QueryBuilder qb = Search.getSearchManager(carCache).buildQueryBuilderForClass(Car.class).get();
		QueryResponse response = new QueryResponse();
		
		Query query1 = qb.keyword().onFields("brand").matching(brand).createQuery();
		Query query2 = qb.keyword().onFields("color").matching(color).createQuery();		
		Query query = qb.bool().should(query1).should(query2).createQuery();

		CacheQuery cacheQuery = Search.getSearchManager(carCache).getQuery(query);
		
		@SuppressWarnings("rawtypes")
		List list = cacheQuery.list();
		@SuppressWarnings("unchecked")
		Iterator<Object> iter = list.iterator();
		while (iter.hasNext()){
			response.addCar((Car)iter.next());
		}
		
		return marshallResponse(response).toString();
	}

	@GET
	@Path("/and")
	@Produces({ "application/xml" })
	public String searchEntityNotRestrictive(@QueryParam("brand") String brand, @QueryParam("color") String color){
		
		Cache<Object, Object> carCache = getCarCache();
		QueryBuilder qb = Search.getSearchManager(carCache).buildQueryBuilderForClass(Car.class).get();
		QueryResponse response = new QueryResponse();
		
		Query query1 = qb.keyword().onFields("brand").matching(brand).createQuery();
		Query query2 = qb.keyword().onFields("color").matching(color).createQuery();		
		Query query = qb.bool().must(query1).must(query2).createQuery();
		
		CacheQuery cacheQuery = Search.getSearchManager(carCache).getQuery(query);
		
		@SuppressWarnings("rawtypes")
		List list = cacheQuery.list();
		@SuppressWarnings("unchecked")
		Iterator<Object> iter = list.iterator();
		while (iter.hasNext()){
			response.addCar((Car)iter.next());
		}
		
		return marshallResponse(response).toString();
	}

	
	private Cache<Object, Object> getCarCache(){
		provider = ServicesLookupManager.getInstance().getContextualInstance(ServicesLookupManager.getInstance().getBeanManagerFromJNDI(), CacheContainerProvider.class);
		Cache<Object, Object> carCache = ((DefaultCacheManager) provider.getCacheContainer()).getCache(CarManager.CACHE_NAME);
		return carCache;
	}
	
	private StringWriter marshallResponse (QueryResponse response){
		StringWriter outputWriter = new StringWriter();
		try{
			JAXBContext jaxbContext = JAXBContext.newInstance(QueryResponse.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
 
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			jaxbMarshaller.marshal(response, outputWriter);
		}catch(Exception e){
			outputWriter.write("<xml><result>" + "OPPS! HOUSTON WE GOT A PROBLEM !!" + "</result></xml>");
		}	
		return outputWriter;
	}
	
}
