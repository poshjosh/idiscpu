package com.idisc.pu;

import com.bc.jpa.EntityManagerFactoryCreatorImpl;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SynchronizationType;

/**
 * @author USER
 */
public class EntityManagerFactoryCreatorDevmode extends EntityManagerFactoryCreatorImpl{
    
    private transient static final Logger LOG = Logger.getLogger(EntityManagerFactoryCreatorDevmode.class.getName());

    public static class EntityManagerFactoryDevmode extends EntityManagerFactoryDecorator {
        
        private final AtomicInteger entityManagerCount = new AtomicInteger();

        private final long LOG_INTERVAL_MILLIS = 30_000;

        private long lastLogTimeMillis;

        private final List<EntityManagerStats> statsCache = new ArrayList<>();

        public static final class EntityManagerStats {
            private final EntityManager entityManager;
            private final String threadName;
            private final StackTraceElement [] stackTrace;
            public EntityManagerStats(EntityManager entityManager) {
                this(entityManager, Thread.currentThread().getName(), Thread.currentThread().getStackTrace());
            }
            public EntityManagerStats(EntityManager entityManager, String threadName, StackTraceElement[] stackTrace) {
                this.entityManager = entityManager;
                this.threadName = threadName;
                this.stackTrace = stackTrace;
            }
            public EntityManager getEntityManager() {
                return entityManager;
            }
            public String getThreadName() {
                return threadName;
            }
            public StackTraceElement[] getStackTrace() {
                return stackTrace;
            }
            @Override
            public String toString() {
                final StringBuilder b = new StringBuilder();
                b.append("Thread name: ").append(threadName).append(". Stack trace = ");
                b.append('{');
                for(StackTraceElement ste : this.stackTrace) {
                    b.append('\n').append(ste);
                }
                b.append('}');
                return b.toString();
            }
        }
        
        public class EntityManagerImpl extends EntityManagerDecorator{
            private final EntityManagerStats stats;
            public EntityManagerImpl(EntityManager delegate) {
                super(delegate);
                entityManagerCount.incrementAndGet();
                synchronized(entityManagerCount) {
                    stats = new EntityManagerStats(delegate);
                    statsCache.add(stats);
                }
            }
            @Override
            public void close() {
                try{
                    super.close(); 
                }finally{
                    entityManagerCount.decrementAndGet();
                    synchronized(entityManagerCount) {
                        statsCache.remove(stats);
                    }
                    if(System.currentTimeMillis() - lastLogTimeMillis >= LOG_INTERVAL_MILLIS) {
                        LOG.log(Level.INFO, "ACTIVE EntityManagers: {0}. Stats cache size: {1}", 
                                new Object[]{entityManagerCount.get(), statsCache.size()});
                        lastLogTimeMillis = System.currentTimeMillis();
                    }
                }
            }
        }
        public EntityManagerFactoryDevmode(EntityManagerFactory delegate) {
            super(delegate);
            Runtime.getRuntime().addShutdownHook(new Thread(){
                @Override
                public void run() {
                    printStats();
                }
            });
        }
        @Override
        public void close() {
            try{
                super.close();
            }finally{
                this.printStats();
            }
        }
        private boolean printStatsAttempted;
        private void printStats() {
            if(!printStatsAttempted) {
                printStatsAttempted = true;
                for(EntityManagerStats stats : statsCache) {
                    System.out.println();
                    System.out.println(stats);
                }
            }
        }
        @Override
        public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
            return new EntityManagerImpl(super.createEntityManager(synchronizationType, map));
        }
        @Override
        public EntityManager createEntityManager(SynchronizationType synchronizationType) {
            return new EntityManagerImpl(super.createEntityManager(synchronizationType));
        }
        @Override
        public EntityManager createEntityManager(Map map) {
            return new EntityManagerImpl(super.createEntityManager(map));
        }
        @Override
        public EntityManager createEntityManager() {
            return new EntityManagerImpl(super.createEntityManager());
        }
        public int getEntityManagerCount() {
            return entityManagerCount.get();
        }
        public List<EntityManagerStats> getStatsCache() {
            return Collections.unmodifiableList(statsCache);
        }
    }
    
    public static EntityManagerFactoryDevmode entityManagerFactory;

    public EntityManagerFactoryCreatorDevmode() { }

    public EntityManagerFactoryCreatorDevmode(ClassLoader classLoader) {
        super(classLoader);
    }

    public EntityManagerFactoryCreatorDevmode(Function<String, Properties> propertiesProvider) {
        super(propertiesProvider);
    }

    public EntityManagerFactoryCreatorDevmode(URI uri, Function<String, Properties> propertiesProvider) {
        super(uri, propertiesProvider);
    }

    public EntityManagerFactoryCreatorDevmode(ClassLoader classLoader, 
            Function<String, Properties> propertiesProvider) {
        super(classLoader, propertiesProvider);
    }

    @Override
    public EntityManagerFactory newInstance(String persistenceUnit) {

        if(entityManagerFactory != null) {
            throw new IllegalStateException("DO NOT CREATE MULTIPLE INSTANCES OF " + EntityManagerFactory.class);
        }

        final EntityManagerFactory created = super.newInstance(persistenceUnit);

        entityManagerFactory = new EntityManagerFactoryDevmode(created);

        return entityManagerFactory;
    }
}
