/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.datagrid.carmart.session;

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;

import org.infinispan.commons.api.BasicCacheContainer;
import org.infinispan.manager.DefaultCacheManager;

/**
 * {@link CacheContainerProvider}'s implementation creating a DefaultCacheManager 
 * which is configured programmatically. Infinispan's libraries need to be bundled 
 * with the application - this is called "library" mode.
 * 
 * The only difference against TomcatCacheContainerProvider is the transaction 
 * manager lookup class.
 * 
 * @author Martin Gencur
 * 
 */
@ApplicationScoped
public class JBossASCacheContainerProvider implements CacheContainerProvider {
    private Logger log = Logger.getLogger(this.getClass().getName());

    private BasicCacheContainer manager;

    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
           try {
					manager = new DefaultCacheManager("infinispan.xml");
				} catch (IOException e) {
					e.printStackTrace();
				}
            
			log.info("=== Using DefaultCacheManager (library mode) ===");
        }
        return manager;
    }
    
    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}
