package org.jboss.as.quickstarts.datagrid.jaxrs.services;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.as.quickstarts.datagrid.carmart.model.Car;

@XmlRootElement
public class QueryResponse {

	int resultCount = 0;
	ArrayList<Car> resultList = new ArrayList<Car>();
	
	public void addCar(Car foundCar){
		resultList.add(foundCar);
		resultCount++;
	}

	public int getResultCount() {
		return resultCount;
	}

	@XmlAttribute
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public ArrayList<Car> getResultList() {
		return resultList;
	}

	@XmlElement(name="Car")
	public void setResultList(ArrayList<Car> resultList) {
		this.resultList = resultList;
	}

	
	
}
