/*
 * Copyright 2018 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.idisc.pu.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Chinomso Bassey Ikwuagwu on Nov 3, 2018 1:26:37 PM
 */
@Entity
@Table(name = "feed")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feed.findAll", query = "SELECT f FROM Feed f")})
public class Feed implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "feedid")
    private Integer feedid;
    @Size(max = 100)
    @Column(name = "rawid")
    private String rawid;
    @Size(max = 1000)
    @Column(name = "url")
    private String url;
    @Size(max = 1000)
    @Column(name = "imageurl")
    private String imageurl;
    @Size(max = 100)
    @Column(name = "author")
    private String author;
    @Size(max = 500)
    @Column(name = "title")
    private String title;
    @Size(max = 1000)
    @Column(name = "keywords")
    private String keywords;
    @Size(max = 1000)
    @Column(name = "categories")
    private String categories;
    @Size(max = 1000)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Column(name = "feeddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date feeddate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "datecreated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecreated;
    @Basic(optional = false)
    @NotNull
    @Column(name = "timemodified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timemodified;
    @Size(max = 500)
    @Column(name = "extradetails")
    private String extradetails;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feedid", fetch = FetchType.LAZY)
    private List<Bookmarkfeed> bookmarkfeedList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feedid", fetch = FetchType.LAZY)
    private List<Favoritefeed> favoritefeedList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feedid", fetch = FetchType.LAZY)
    private List<Feedhit> feedhitList;
    @JoinColumn(name = "siteid", referencedColumnName = "siteid")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Site siteid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "feedid", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    public Feed() {
    }

    public Feed(Integer feedid) {
        this.feedid = feedid;
    }

    public Feed(Integer feedid, String content, Date feeddate, Date datecreated, Date timemodified) {
        this.feedid = feedid;
        this.content = content;
        this.feeddate = feeddate;
        this.datecreated = datecreated;
        this.timemodified = timemodified;
    }

    public Integer getFeedid() {
        return feedid;
    }

    public void setFeedid(Integer feedid) {
        this.feedid = feedid;
    }

    public String getRawid() {
        return rawid;
    }

    public void setRawid(String rawid) {
        this.rawid = rawid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFeeddate() {
        return feeddate;
    }

    public void setFeeddate(Date feeddate) {
        this.feeddate = feeddate;
    }

    public Date getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(Date datecreated) {
        this.datecreated = datecreated;
    }

    public Date getTimemodified() {
        return timemodified;
    }

    public void setTimemodified(Date timemodified) {
        this.timemodified = timemodified;
    }

    public String getExtradetails() {
        return extradetails;
    }

    public void setExtradetails(String extradetails) {
        this.extradetails = extradetails;
    }

    @XmlTransient
    public List<Bookmarkfeed> getBookmarkfeedList() {
        return bookmarkfeedList;
    }

    public void setBookmarkfeedList(List<Bookmarkfeed> bookmarkfeedList) {
        this.bookmarkfeedList = bookmarkfeedList;
    }

    @XmlTransient
    public List<Favoritefeed> getFavoritefeedList() {
        return favoritefeedList;
    }

    public void setFavoritefeedList(List<Favoritefeed> favoritefeedList) {
        this.favoritefeedList = favoritefeedList;
    }

    @XmlTransient
    public List<Feedhit> getFeedhitList() {
        return feedhitList;
    }

    public void setFeedhitList(List<Feedhit> feedhitList) {
        this.feedhitList = feedhitList;
    }

    public Site getSiteid() {
        return siteid;
    }

    public void setSiteid(Site siteid) {
        this.siteid = siteid;
    }

    @XmlTransient
    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feedid != null ? feedid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Feed)) {
            return false;
        }
        Feed other = (Feed) object;
        if ((this.feedid == null && other.feedid != null) || (this.feedid != null && !this.feedid.equals(other.feedid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.idisc.pu.entities.Feed[ feedid=" + feedid + " ]";
    }

}
