package com.db.mongo.entity;

import com.db.base.model.Bean1;
import com.db.mongo.pojo.core.anno.Id;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
public class AbstractEntity<PK> {

    @Id
    private PK id;

    private int version;

    private boolean deleted = false;

    public int updateTime;

    public void setBean1(Bean1 bean1){
    }

    public Bean1 getBean1(){
        return null;
    }

    public PK getId() {
        return id;
    }

    public void setId(PK id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
