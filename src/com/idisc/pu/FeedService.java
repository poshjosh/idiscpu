package com.idisc.pu;

import com.bc.jpa.context.JpaContext;
import com.bc.jpa.dao.Criteria;
import com.idisc.pu.entities.Feed;
import com.idisc.pu.entities.Feed_;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import com.bc.jpa.dao.Select;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 31, 2016 9:31:37 AM
 */
public class FeedService extends DaoService {

    private static final Logger logger = Logger.getLogger(FeedService.class.getName());
    
    public FeedService(JpaContext jpaContext) {
        super(jpaContext);
    }
    
    public Integer getNumberOfFeedsAfter(String sitename, Date maxAge) {
        
        final JpaContext jpaContext = this.getJpaContext();
        
        final Integer siteId = this.getSiteService().getIdForSitename(sitename);
        
        try(Select<Integer> dao = jpaContext.getDaoForSelect(Feed.class, Integer.class)) {
            TypedQuery<Integer> tq = dao.count(Feed.class, Feed_.feedid)
              .where(Feed_.siteid, siteId)
              .and().where(Feed_.datecreated, Select.GT, maxAge).createQuery();
            final Integer count = tq.getSingleResult();
            return count == null ? 0 : count;
        }
    }

    public Optional<Feed> getMostRecentForSite(String site) {
        
        final JpaContext jpaContext = this.getJpaContext();
        
        final Integer siteId = this.getSiteService().getIdForSitename(site);
        
        final List<Feed> results = jpaContext.getDaoForSelect(Feed.class)
                .where(Feed_.siteid, siteId)
                .descOrder(Feed.class, Feed_.feeddate)
                .getResultsAndClose(0, 1);
        
       
        return results == null || results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<Feed> getFeeds(int offset, int limit, boolean spreadOutput) {
      
        final List<Feed> output;
    
        logger.entering(this.getClass().getName(), "#getFeeds(int, int, boolean)");
      
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

            logger.log(Level.FINE, "Loaded {0} feeds", sizeOf(output));

            printFirstDateLastDateAndFeedIds(Level.FINER, "AFTER", output);
        }

        return output;
    }
  
    public List<Feed> selectFeeds(int offset, int limit) {
        List<Feed> loadedFeeds = this.getJpaContext().getDaoForSelect(Feed.class)
                .descOrder(Feed.class, Feed_.feedid)
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
                
                logger.log(Level.WARNING, 
                "#createIfNoneExistsWithMatchingData(Collection<Feed>). Caught exception: {0}", 
                e.toString());
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

        try(Select<Integer> dao = this.getJpaContext().getDaoForSelect(Feed.class, Integer.class)) {
        
            final boolean persist;
            
            if(this.restrictSearchToDataColumns(dao, feed)) {

                Integer found;
                try{
                    found = dao.select(Feed_.feedid).createQuery().setFirstResult(0).setMaxResults(1).getSingleResult();
                }catch(NoResultException ignored) {
                    found = null;
                }
                persist = found == null;
            }else{
                persist = true;
            }

            if(persist) {

                dao.begin().persist(feed).commit();
                 
                logger.fine(() -> "Saving " + feed.getSiteid().getSite() + " title: " + feed.getTitle());
                
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
        
        try(Select<Feed> select = getJpaContext().getDaoForSelect(Feed.class)) {
        
            if(this.restrictSearchToDataColumns(select, toFind)) {

                found = select.createQuery().setFirstResult(0).setMaxResults(1).getSingleResult();
                
            }else{
                
                found = outputIfNone;
            }
            
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
    
    private boolean restrictSearchToDataColumns(Criteria criteria, Feed toFind) {
        
        criteria.from(Feed.class);

        int whereCount = 0;
        if(this.where(criteria, Feed_.title.getName(), toFind.getTitle())) {
            ++whereCount;
        }
//        this.where(criteria, Feed_.content.getName(), toFind.getContent());
        if(this.where(criteria, Feed_.description.getName(), toFind.getDescription())) {
            ++whereCount;
        }
        
        if(this.where(criteria, Feed_.siteid.getName(), toFind.getSiteid())) {
            ++whereCount;
        }
        
        return whereCount >= 2;
    }
    
    private boolean where (Criteria criteria, String key, Object val) {
        if(val != null) {
            criteria.where(key, Criteria.EQ, val);
            return true;
        }
        return false;
    }

    private void printFirstDateLastDateAndFeedIds(Level level, String key, List<Feed> feeds) {
        if (logger.isLoggable(level) && feeds != null && !feeds.isEmpty()) {
            Feed first = (Feed)feeds.get(0);
            Feed last = (Feed)feeds.get(feeds.size() - 1);
            logger.log(level, () -> MessageFormat.format(
                    "{0}. First feed, date: {1}. Last feed, date: {2}\n{3}", 
                     key, first.getFeeddate(), last.getFeeddate(), feeds));
            
        }
    }
    
    private transient SiteService _ss;
    public SiteService getSiteService() {
        if(_ss == null) {
            _ss = new SiteService(this.getJpaContext());
        }
        return _ss;
    }
}
