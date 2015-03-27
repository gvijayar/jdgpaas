package org.jboss.as.quickstarts.datagrid.jaxrs;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

public class ServicesLookupManager {

	private static ServicesLookupManager iCommons;
	
	private ServicesLookupManager(){
		
	}
	
	static public ServicesLookupManager getInstance(){
		if (iCommons == null){
			iCommons = new ServicesLookupManager();
		}
		return iCommons;
	}

    public BeanManager getBeanManagerFromJNDI() {
        // BeanManager is only available when CDI is available. This is achieved by the presence of beans.xml file
        InitialContext context;
        Object result;
        try {
            context = new InitialContext();
            result = context.lookup("java:comp/BeanManager"); // lookup in JBossAS
        } catch (NamingException e) {
            throw new RuntimeException("BeanManager could not be found in JNDI", e);
        }
        return (BeanManager) result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getContextualInstance(final BeanManager manager, final Class<T> type) {
        T result = null;
        Bean<T> bean = (Bean<T>) manager.resolve(manager.getBeans(type));
        if (bean != null) {
            CreationalContext<T> context = manager.createCreationalContext(bean);
            if (context != null) {
                result = (T) manager.getReference(bean, type, context);
            }
        }
        return result;
    }
    
    public UserTransaction getUserTransactionFromJNDI() {
        InitialContext context;
        Object result;
        try {
            context = new InitialContext();
            result = context.lookup("java:comp/UserTransaction"); // lookup in JBossAS
        } catch (NamingException ex) {
            throw new RuntimeException("UserTransaction could not be found in JNDI", ex);
        }
        return (UserTransaction) result;
    }
    
	
}
