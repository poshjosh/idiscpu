package com.idisc.pu;

import com.bc.jpa.dao.Criteria;
import com.bc.jpa.dao.JpaObjectFactory;
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
import com.idisc.pu.entities.Site;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

/**
 * @author Chinomso Bassey Ikwuagwu on Aug 31, 2016 9:31:37 AM
 */
public class FeedDao extends DaoBase {

    private transient static final Logger LOG = Logger.getLogger(FeedDao.class.getName());
    
    public FeedDao(JpaObjectFactory jpaContext) {
        super(jpaContext);
    }

    public Optional<Feed> getMostRecentForSite(String site) {
        
        final Feed feed = this.getMostRecentForSite(site, null);
        
        return feed == null ? Optional.empty() : Optional.of(feed);
    }

    public Feed getMostRecentForSite(String site, Feed outputIfNone) {
        
        final JpaObjectFactory jpaContext = this.getJpaContext();
        
        final Integer siteId = this.getSiteService().getIdForSitename(site, 0);
        
        Feed output;
        
        if(siteId == 0) {
            output = null;
        }else{
            try{

                final List<Feed> results = jpaContext.getDaoForSelect(Feed.class)
                        .where(Feed_.siteid, siteId)
                        .descOrder(Feed_.feeddate)
                        .getResultsAndClose(0, 1);

                output = results == null || results.isEmpty() ? null : results.get(0);

            }catch(NoResultException e) {
                output = null;
            }
        }
        
        return output == null ? outputIfNone : output;
    }

    public List<Feed> getFeeds(int offset, int searchLimit, int resultLimit) {
      
        final boolean spreadOutput = searchLimit > resultLimit;
        
        final List<Feed> loadedFeeds = this.selectFeeds(offset, searchLimit);
        
        final List<Feed> output = this.limitFeeds(loadedFeeds, resultLimit, spreadOutput);

        return output;
    }
  
    public List<Feed> limitFeeds(List<Feed> feeds, int limit, boolean spreadOutput) {
      
        final List<Feed> output;
    
        LOG.entering(this.getClass().getName(), "#getFeeds(int, int, boolean)");
      
        if (feeds == null || feeds.isEmpty()) {

          output = Collections.EMPTY_LIST;

        }else if(feeds.size() <= limit) {

              output = Collections.unmodifiableList(feeds);  

        }else{

            printFirstDateLastDateAndFeedIds(Level.FINER, "BEFORE", feeds);

            final List<Feed> list;
            if (spreadOutput) {
                list = new FeedsSpreader().spread(feeds, limit);
            }else{
                list = truncate(feeds, limit);
            }
            output = Collections.unmodifiableList(list);

            LOG.log(Level.FINE, "Loaded {0} feeds", sizeOf(output));

            printFirstDateLastDateAndFeedIds(Level.FINER, "AFTER", output);
        }

        return output;
    }

    public List<Feed> selectFeeds(int offset, int limit) {
        List<Feed> loadedFeeds = this.getJpaContext().getDaoForSelect(Feed.class)
                .descOrder(Feed.class, Feed_.feeddate)
                .getResultsAndClose(offset, limit);
        return loadedFeeds;
    }
  
    public List<Feed> spreadOutput(List<Feed> feeds, int outputSize) {
        return new FeedsSpreader().spread(feeds, outputSize);      
    }
  
    protected List<Feed> truncate(List<Feed> feeds, int limit) {
        return feeds.size() <= limit ? feeds : feeds.subList(0, limit);  
    }
  
    protected int sizeOf(Collection<Feed> feeds) {
        return feeds == null ? 0 : feeds.size();
    }
  
    /**
     * @param feeds The collection of feeds to create if none exists with matching data.
     * @param ifNoneExistsWithMatchingData
     * @return The collection of feeds (a subset of the input collection) which were
     * not created. An empty collection is returned if all were successfully created.
     */
    public Collection<Feed> create(Collection<Feed> feeds, boolean ifNoneExistsWithMatchingData) {
     
        LOG.log(Level.FINE, "Feeds: {0}", (feeds == null ? null : feeds.size()));
        
        if(feeds == null || feeds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        Collection<Feed> failed = null;
        
        final Date date = new Date();
        
        final EntityManager em = this.getJpaContext().getEntityManager();
        
        try{
            
            em.getTransaction().begin();
            
            for(Feed feed : feeds) {

                if(feed.getDatecreated() == null) {
                    feed.setDatecreated(date);
                }

                final boolean created = this.create(em, feed, ifNoneExistsWithMatchingData);

                if(!created) {
                    if(failed == null) {
                        failed = new LinkedList();
                    }
                    failed.add(feed);
                }            
            }
            
            this.getJpaContext().commit(em.getTransaction());
            
        }finally{
            if(em.isOpen()) {
                em.close();
            }
        }
        
        return failed == null ? Collections.EMPTY_LIST : failed;
    }

    public boolean create(Feed feed, boolean onlyIfNoneExistsWithMatchingData) {
        
        final EntityManager em = this.getJpaContext().getEntityManager();

        final boolean created;
        try{
            
            em.getTransaction().begin();
            
            created = this.create(em, feed, onlyIfNoneExistsWithMatchingData);
            
            this.getJpaContext().commit(em.getTransaction());
            
        }finally{
            if(em.isOpen()) {
                em.close(); 
            }
        }
        
        return created;
    }

    public boolean create(EntityManager em, Feed feed, boolean onlyIfNoneExistsWithMatchingData) {
        
        final Integer foundFeedid;
        if(onlyIfNoneExistsWithMatchingData) {
//            final List<Integer> matching = this.selectMatchingFeedids(feed, 0, 1);
            final List<Integer> matching = this.selectMatching(em, feed, Integer.class, Feed_.feedid, 0, 1);
            foundFeedid = matching.isEmpty() ? null : matching.get(0);
        }else{
            foundFeedid = null;
        }

        final String siteName = feed.getSiteid() == null ? null : feed.getSiteid().getSite();

        LOG.finer(() -> "Saving " + siteName + " feed titled: " + feed.getTitle());

        final boolean created;
        
        if(foundFeedid == null) {

            em.persist(feed);

            LOG.fine(() -> "Saved " + siteName + " feed titled: " + feed.getTitle());

            created = true;

        }else{

            LOG.fine(() -> "Already exisits " + siteName + " feed with ID " + foundFeedid + 
                    ", title: " + feed.getTitle() + ", description " + feed.getDescription());
            
            created = false;
        }    
        
        return created;
    }

    public boolean isExisting(Feed toFind) {
        
        final List<Integer> matchingList = this.selectMatchingFeedids(toFind, 0, 1);
        
        return matchingList != null && !matchingList.isEmpty();
    }

    public List<Feed> selectMatchingFeeds(Feed toFind, int offset, int limit) {
        return this.selectMatching(toFind, Feed.class, null, offset, limit);
    }
    
    public List<Integer> selectMatchingFeedids(Feed toFind, int offset, int limit) {
        return this.selectMatching(toFind, Integer.class, Feed_.feedid, offset, limit);
    }

    public <T> List<T> selectMatching(
            Feed toFind, Class<T> resultType, 
            SingularAttribute<Feed, T> resultColumn, int offset, int limit) {
        
        List<T> found; 

        final EntityManager em = this.getJpaContext().getEntityManager();
        
        try{
            found = this.selectMatching(em, toFind, resultType, resultColumn, offset, limit);
        }finally{
            if(em.isOpen()) {
                em.close();
            }
        }
        
        return found;
    }
    
    public <T> List<T> selectMatching(
            EntityManager em, Feed toFind, Class<T> resultType, 
            SingularAttribute<Feed, T> resultColumn, int offset, int limit) {
        
        List<T> found; 

        final TypedQuery<T> tq = this.selectMatching(em, toFind, resultType, resultColumn, null);

        if(tq == null) {
            found = Collections.EMPTY_LIST;
        }else{

            tq.setFirstResult(offset).setMaxResults(limit);

            if(limit == 1) {
                try{
                    final T result = tq.getSingleResult();
                    found = Collections.singletonList(result);
                }catch(NoResultException e) {
                    found = Collections.EMPTY_LIST;
                }
            }else{
                found = tq.getResultList();
            }
        }
        
        return found;
    }

    public <T> TypedQuery<T> selectMatching(
            EntityManager em, Feed toFind, Class<T> resultType, 
            SingularAttribute<Feed, T> resultColumn, TypedQuery<T> outputIfNone) {
        
        final TypedQuery<T> output;
        
        final String url = toFind.getUrl();
        final Site site = toFind.getSiteid();
        
        if((url == null || url.isEmpty()) && site == null) {
            
            output = outputIfNone;
            
        }else{
            
            final CriteriaBuilder cb = em.getCriteriaBuilder();
            final CriteriaQuery<T> cq = cb.createQuery(resultType);

            final Root<Feed> root = cq.from(Feed.class);

            if(resultColumn != null) {
                cq.select(root.get(resultColumn));
            }

            final List<Predicate> predicateList = new ArrayList<>(4);

            final Predicate urlEquals = url == null ? null : cb.equal(root.get(Feed_.url), url);
            if(urlEquals != null) {
                predicateList.add(urlEquals);
            }
            if(site != null) {
                final Predicate siteEquals = this.notNullAndEquals(cb, root, Feed_.siteid, site);
                final String title = toFind.getTitle();
                if(title != null) {
                    final Predicate titleEquals = this.notNullAndEquals(cb, root, Feed_.title, title);
                    predicateList.add(cb.and(siteEquals, titleEquals));
                }
                final String description = toFind.getDescription();
                if(description != null) {
                    final Predicate descriptionEquals = this.notNullAndEquals(cb, root, Feed_.description, description);
                    predicateList.add(cb.and(siteEquals, descriptionEquals));
                }
                final String content = toFind.getContent();
                if(content != null) {
                    final Predicate contentEquals = this.notNullAndEquals(cb, root, Feed_.content, content);
                    predicateList.add(cb.and(siteEquals, contentEquals));
                }
            }

            if(predicateList.isEmpty()) {
                output = outputIfNone;
            }else{
                if(predicateList.size() == 1) {
                    cq.where(predicateList.get(0));
                }else{
                    cq.where(cb.or(predicateList.toArray(new Predicate[0])));
                }

                output = em.createQuery(cq);
            }
        }
        
        return output;
    }

    private Predicate notNullAndEquals(CriteriaBuilder cb, Root<Feed> root, SingularAttribute<Feed, ?> a, Object val) {
        final Path<?> path = root.get(a);
        return cb.and(cb.isNotNull(path), cb.equal(path, val));
    }

    /**
     * @deprecated 
     * @param toFind
     * @param outputIfNone
     * @return 
     */
    @Deprecated
    public Feed selectFirstWithMatchingData(Feed toFind, Feed outputIfNone) {
        
        Feed found;
        
        try(Select<Feed> select = getJpaContext().getDaoForSelect(Feed.class)) {
        
            if(this.restrictSearchToDataColumns(select, toFind)) {

                found = select.createQuery().setFirstResult(0).setMaxResults(1).getSingleResult();
                
            }else{
                
                found = null;
            }
            
        }catch(NoResultException ignored) {
         
            found = null;
        }
        
        return found == null ? outputIfNone : found;
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

    public Long countFeedsBefore(Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsBefore(null, maxAge, outputIfNone);
    }
    
    public Long countFeedsBefore(String sitename, Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsBefore(sitename, maxAge, outputIfNone);
    }

    public Long getNumberOfFeedsBefore(Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsBefore(null, maxAge, outputIfNone);
    }

    public Long getNumberOfFeedsBefore(String sitename, Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeeds(sitename, maxAge, Select.LT, outputIfNone);
    }
    
    public Long countFeedsAfter(Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsAfter(null, maxAge, outputIfNone);
    }
    
    public Long countFeedsAfter(String sitename, Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsAfter(sitename, maxAge, outputIfNone);
    }

    public Long getNumberOfFeedsAfter(Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeedsAfter(null, maxAge, outputIfNone);
    }

    public Long getNumberOfFeedsAfter(String sitename, Date maxAge, Long outputIfNone) {
        return this.getNumberOfFeeds(sitename, maxAge, Select.GT, outputIfNone);
    }
    
    public Long getNumberOfFeeds(String sitename, Date maxAge, 
            Criteria.ComparisonOperator comparisonOperator, Long outputIfNone) {
        
        final JpaObjectFactory jpaContext = this.getJpaContext();
        
        final Integer siteId = sitename == null ? null : this.getSiteService().getIdForSitename(sitename, null);
        
        if(sitename != null && siteId == null) {
            throw new IllegalArgumentException("Unexpected sitename: " + sitename);
        }
        
        try(Select<Long> select = jpaContext.getDaoForSelect(Long.class)) {
            select.from(Feed.class).count(Feed_.feedid);
            if(siteId != null) {
                select.where(Feed_.siteid, siteId);
                select.and();
            }
            final TypedQuery<Long> tq = select.where(Feed_.feeddate, comparisonOperator, maxAge).createQuery();
            final Long count = tq.getSingleResult();
            return count == null ? outputIfNone : count;
        }
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

        if(this.where(criteria, Feed_.title.getName(), Criteria.EQUALS, toFind.getTitle())) {
            ++whereCount;
        }
//        this.where(criteria, Feed_.content.getName(), toFind.getContent());
        if(this.where(criteria, Feed_.description.getName(), Criteria.EQUALS, toFind.getDescription())) {
            ++whereCount;
        }
        
        if(this.where(criteria, Feed_.siteid.getName(), Criteria.EQUALS, toFind.getSiteid())) {
            ++whereCount;
        }
        
        return whereCount >= 2;
    }
    
    private boolean where (Criteria criteria, String key, Criteria.ComparisonOperator  optr, Object val) {
        if(val != null) {
            criteria.where(key, optr, val);
            return true;
        }
        return false;
    }

    private void printFirstDateLastDateAndFeedIds(Level level, String key, List<Feed> feeds) {
        if (LOG.isLoggable(level) && feeds != null && !feeds.isEmpty()) {
            Feed first = (Feed)feeds.get(0);
            Feed last = (Feed)feeds.get(feeds.size() - 1);
            LOG.log(level, () -> MessageFormat.format(
                    "{0}. First feed, date: {1}. Last feed, date: {2}\n{3}", 
                     key, first.getFeeddate(), last.getFeeddate(), feeds));
            
        }
    }
    
    private transient SiteDao _ss;
    public SiteDao getSiteService() {
        if(_ss == null) {
            _ss = new SiteDao(this.getJpaContext());
        }
        return _ss;
    }
}
/**
 * 
    public boolean create(Feed feed, boolean onlyIfNoneExistsWithMatchingData) {
        
        boolean created = false;

        try(Select<Integer> dao = this.getJpaContext().getDaoForSelect(Integer.class)) {
        
            Integer found;
            
            if(onlyIfNoneExistsWithMatchingData && this.restrictSearchToDataColumns(dao, feed)) {

                try{
                    found = dao.from(Feed.class)
                            .select(Feed_.feedid)
                            .createQuery()
                            .setFirstResult(0).setMaxResults(1)
                            .getSingleResult();
                }catch(NoResultException ignored) {
                    found = null;
                }
            }else{
                found = null;
            }
            
            final String siteName = feed.getSiteid() == null ? null : feed.getSiteid().getSite();
            
            if(found == null) {

                LOG.finer(() -> "Saving " + siteName + " feed titled: " + feed.getTitle());

                dao.begin().persist(feed).commit();
                 
                LOG.fine(() -> "Saved " + siteName + " feed titled: " + feed.getTitle());
                
                created = true;
                
            }else{
                
                final Integer feedid = found;
                LOG.fine(() -> "Already exisits " + siteName + " feed with ID " + feedid + 
                        ", title: " + feed.getTitle() + ", description " + feed.getDescription());
            }    
        }
        
        return created;
    }

    public List<Feed> selectMatching_old(Feed toFind, int offset, int limit) {
        
        List<Feed> found;
        
        final String url = toFind.getUrl();
        final Site site = toFind.getSiteid();
        
        if((url == null || url.isEmpty()) && site == null) {
            
            found = Collections.EMPTY_LIST;
            
        }else{
            
            final EntityManager em = this.getJpaContext().getEntityManager();
            
            try{
                
                final CriteriaBuilder cb = em.getCriteriaBuilder();
                final CriteriaQuery<Feed> cq = cb.createQuery(Feed.class);
                
                final Root<Feed> root = cq.from(Feed.class);

                final List<Predicate> predicateList = new ArrayList<>(4);

                final Predicate urlEquals = url == null ? null : cb.equal(root.get(Feed_.url), url);
                if(urlEquals != null) {
                    predicateList.add(urlEquals);
                }
                if(site != null) {
                    final Predicate siteEquals = this.notNullAndEquals(cb, root, Feed_.siteid, site);
                    final String title = toFind.getTitle();
                    if(title != null) {
                        final Predicate titleEquals = this.notNullAndEquals(cb, root, Feed_.title, title);
                        predicateList.add(cb.and(siteEquals, titleEquals));
                    }
                    final String description = toFind.getDescription();
                    if(description != null) {
                        final Predicate descriptionEquals = this.notNullAndEquals(cb, root, Feed_.description, description);
                        predicateList.add(cb.and(siteEquals, descriptionEquals));
                    }
                    final String content = toFind.getContent();
                    if(content != null) {
                        final Predicate contentEquals = this.notNullAndEquals(cb, root, Feed_.content, content);
                        predicateList.add(cb.and(siteEquals, contentEquals));
                    }
                }

                if(predicateList.isEmpty()) {
                    found = Collections.EMPTY_LIST;
                }else{
                    if(predicateList.size() == 1) {
                        cq.where(predicateList.get(0));
                    }else{
                        cq.where(cb.or(predicateList.toArray(new Predicate[0])));
                    }
                    
                    final TypedQuery<Feed> tq = em.createQuery(cq);
                    tq.setFirstResult(offset).setMaxResults(limit);

                    if(limit == 1) {
                        try{
                            final Feed result = tq.getSingleResult();
                            found = Collections.singletonList(result);
                        }catch(NoResultException e) {
                            found = Collections.EMPTY_LIST;
                        }
                    }else{
                        found = tq.getResultList();
                    }
                }
            }finally{
                if(em.isOpen()) {
                    em.close();
                }
            }
        }
        
        return found == null ? Collections.EMPTY_LIST : found;
    }
 */