package com.idisc.pu;

import com.bc.jpa.JpaContext;
import com.bc.jpa.dao.BuilderForSelect;
import com.bc.jpa.dao.Criteria;
import com.bc.util.XLogger;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.NoResultException;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 31, 2016 9:31:37 AM
 */
public class FeedService extends DaoService {
    
    public FeedService(JpaContext jpaContext) {
        super(jpaContext);
    }

    public List<Feed> getFeeds(int offset, int limit, boolean spreadOutput) {
      
        final List<Feed> output;
    
XLogger.getInstance().entering(this.getClass(), "#getFeeds(int, boolean)", "");
      
        List<Feed> loadedFeeds = this.selectFeeds(offset, limit);

        if (loadedFeeds == null || loadedFeeds.isEmpty()) {

          output = Collections.EMPTY_LIST;

        }else if(loadedFeeds.size() <= limit) {

              output = Collections.unmodifiableList(loadedFeeds);  

        }else{

            printFirstDateLastDateAndFeedIds(Level.FINER, "BEFORE", loadedFeeds);

            if (spreadOutput) {
                  output = Collections.unmodifiableList(spreadOutput(loadedFeeds, limit));  
            }else{
                  output = Collections.unmodifiableList(truncate(loadedFeeds, limit));  
            }

            XLogger.getInstance().log(Level.FINE, "Loaded {0} feeds", getClass(), sizeOf(output));

            printFirstDateLastDateAndFeedIds(Level.FINER, "AFTER", output);
        }

        return output;
    }
  
    public List<Feed> selectFeeds(int offset, int limit) {
        List<Feed> loadedFeeds = this.getJpaContext().getBuilderForSelect(Feed.class)
                .descOrder(Feed.class, Feed_.feedid.getName())
                .getResultsAndClose(offset, limit);
        return loadedFeeds;
    }
  
    public List<Feed> spreadOutput(List<Feed> feeds, int outputSize) {
        return new SpreadBySite().spread(feeds, outputSize);      
    }
  
    protected List<Feed> truncate(List<Feed> feeds, int limit) {
        return feeds.size() <= limit ? feeds : feeds.subList(0, limit);  
    }
  
    protected int sizeOf(Collection<Feed> feeds) {
        return feeds == null ? 0 : feeds.size();
    }
  
    /**
     * @param feeds The collection of feeds to create if none exists with matching data.
     * @return The collection of feeds (a subset of the input collection) which were
     * not created. An empty collection is returned if all were successfully created.
     */
    public Collection<Feed> createIfNoneExistsWithMatchingData(Collection<Feed> feeds) {
     
        Collection<Feed> failed = null;
        
        Date date = new Date();
        
        for(Feed feed:feeds) {
            
            if(feed.getDatecreated() == null) {
                feed.setDatecreated(date);
            }

            boolean created;
            try{
                
                created = this.createIfNoneExistsWithMatchingData(feed);
                
            }catch(Exception e) {
                
                created = false;
                
                XLogger.getInstance().log(Level.WARNING, 
                "#createIfNoneExistsWithMatchingData(Collection<Feed>). Caught exception: {0}", 
                this.getClass(), e.toString());
            }
            
            if(!created) {
                if(failed == null) {
                    failed = new LinkedList();
                }
                failed.add(feed);
            }
        }
        
        return failed == null ? Collections.EMPTY_LIST : failed;
    }
    
    public boolean createIfNoneExistsWithMatchingData(Feed feed) {
        
        boolean created = false;
        
        try(BuilderForSelect<Feed> dao = this.getJpaContext().getBuilderForSelect(Feed.class)) {
        
            this.restrictSearchToDataColumns(dao, feed);

            Feed found;
            try{
                found = dao.createQuery().setFirstResult(0).setMaxResults(1).getSingleResult();
            }catch(NoResultException e) {
                found = null;
            }

            if(found == null) {

                dao.begin().persist(feed).commit();
                 
                XLogger.getInstance().log(Level.FINER, "Persisted feed: {0}", this.getClass(), feed);
                
                created = true;
            }    
        }
        
        return created;
    }
    
    public boolean isExisting(Feed toFind) {
        
        return this.selectFirstWithMatchingData(toFind, null) != null;
    }
    
    public Feed selectFirstWithMatchingData(Feed toFind, Feed outputIfNone) {
        
        Feed found;
        
        try(BuilderForSelect<Feed> select = getJpaContext().getBuilderForSelect(Feed.class)) {
        
            this.restrictSearchToDataColumns(select, toFind);

            found = select.createQuery().setFirstResult(0).setMaxResults(1).getSingleResult();
            
        }catch(NoResultException ignored) {
         
            found = outputIfNone;
        }
        
        return found;
    }
    
    public Date getEarliestDate(Collection<Map> feeds, String dateColumnName) {
        Date earliestDate = null;
        for (Map feed : feeds) {
            Date date = (Date)feed.get(dateColumnName);
            if (earliestDate == null) {
                earliestDate = date;
            } else if (date.before(earliestDate)) {
                earliestDate = date;
            }
        }
    
        return earliestDate;
    }
  
    public <E extends Feed> Date getEarliestDate(Collection<E> feeds) {
        
        Date earliestDate = null;
        for (Feed feed : feeds) {
            Date date = feed.getFeeddate() == null ? feed.getDatecreated() : feed.getFeeddate();
            if (earliestDate == null) {
                earliestDate = date;
            } else if (date.before(earliestDate)) {
                earliestDate = date;
            }
        }
    
        return earliestDate;
    }
    
    public void appendStringRepresentation(StringBuilder appendTo, Feed feed) {
        appendTo.append("ID: ").append(feed.getFeedid());
        appendTo.append(", site: ").append(feed.getSiteid().getSite());
        appendTo.append(", author: ").append(feed.getAuthor());
        appendTo.append(", date: ").append(feed.getFeeddate());
        appendTo.append(", link: ").append(feed.getUrl());
        appendTo.append(", imageUrl: ").append(feed.getImageurl());
    }
    
    private void restrictSearchToDataColumns(Criteria criteria, Feed toFind) {
        
        criteria.from(Feed.class);

        this.where(criteria, Feed_.title.getName(), toFind.getTitle());
        this.where(criteria, Feed_.content.getName(), toFind.getContent());
        this.where(criteria, Feed_.description.getName(), toFind.getDescription());
        this.where(criteria, Feed_.siteid.getName(), toFind.getSiteid());
    }
    
    private void where (Criteria criteria, String key, Object val) {
        if(val != null) {
            criteria.where(key, Criteria.EQ, val);
        }
    }

    private void printFirstDateLastDateAndFeedIds(Level level, String key, List<Feed> feeds) {
        if (XLogger.getInstance().isLoggable(level, this.getClass()) && feeds != null && !feeds.isEmpty()) {
            Feed first = (Feed)feeds.get(0);
            Feed last = (Feed)feeds.get(feeds.size() - 1);
            XLogger.getInstance().log(level, "{0}. First feed, date: {1}. Last feed, date: {2}\n{3}", 
                    this.getClass(), key, first.getFeeddate(), last.getFeeddate(), feeds);
        }
    }
}
