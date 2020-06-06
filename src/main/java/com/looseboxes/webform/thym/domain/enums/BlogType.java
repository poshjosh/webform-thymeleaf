package com.looseboxes.webform.thym.domain.enums;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author hp
 */
// This is not an entity, no Id declared etc, 
// Hibernate (h2) complained that no identifier declared
@Entity
@Table(name = "blog_type")
public enum BlogType {
    FASHION, MOVIES, GOSSIP, SPORTS;
    
    @Id
    @Basic(optional = false)
    private Short id;
    
    @Size(max = 32)
    @Basic(optional = false)
    private String name;
    
    private BlogType() {
        this.id = (short)(this.ordinal());
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "com.looseboxes.webform.thym.domain.enums.BlogType[ id=" + id + " ]";
    }
}
