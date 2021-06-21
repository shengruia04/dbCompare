package com.db.hibernate.entity;

import com.db.base.model.Bean1;
import com.db.hibernate.orm.JsonUtility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
/**
 * @author linjianghua
 * @date 2021/06/13 16:07
 * @desc
 */
@Setter
@Getter
@MappedSuperclass
public class AbstractEntity implements Serializable {

    @Id
    private String id;

    @Column(columnDefinition = "")
    private int version;

    @Column(columnDefinition = "")
    private int updateTime;

    @Column(columnDefinition = "")
    private boolean deleted = false;

    @Lob
    @Column(columnDefinition = "")
    private byte[] data;

    private transient Bean1 bean1;

    public void doSerialize() {
        this.data = JsonUtility.toCompressBytes(bean1);
    }

    public void doDeserialize() {
        this.bean1 = JsonUtility.toObjectWithCompress(data, Bean1.class);
        this.data = null;
    }
}
