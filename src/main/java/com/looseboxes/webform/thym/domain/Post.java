package com.looseboxes.webform.thym.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author hp
 */
@Entity
@Table(name = "post")
@XmlRootElement
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    
    @Size(max = 64)
    @Basic(optional = false)
    private String title;

    @Size(max = 10240)
    private String content;
    
    @Basic(optional = false)
    //@NotNull
    @Temporal(TemporalType.TIMESTAMP)
//    @javax.persistence.Convert(converter=com.bc.jpa.dateconverter.DateConverterJpa.class)
    @Column(name = "time_created")
    private Date timeCreated;
    
    @Basic(optional = false)
    //@NotNull
    @Temporal(TemporalType.TIMESTAMP)
//    @javax.persistence.Convert(converter=com.bc.jpa.dateconverter.DateConverterJpa.class)
    @Column(name = "time_modified")
    private Date timeModified;
    
    @Size(max = 255)
    @Column(name = "image", length = 255)
    private String image;
    
    @JoinColumn(name = "blog", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Blog blog;

    @JoinTable(name = "tag_post", joinColumns = {
        @JoinColumn(name = "post", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "tag", referencedColumnName = "id")})
    @ManyToMany
    private List<Tag> tagList;
    
    public Post() {
    }

    public Post(Integer id) {
        this.id = id;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeModified() {
        return timeModified;
    }

    public void setTimeModified(Date timeModified) {
        this.timeModified = timeModified;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
    
    @XmlTransient
    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Post)) {
            return false;
        }
        Post other = (Post) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title=" + title + ", content=" + content + ", timeCreated=" + timeCreated + ", timeModified=" + timeModified + ", image=" + image + ", blog=" + (blog==null?null:blog.getHandle()) + '}';
    }
}
