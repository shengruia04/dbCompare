package com.db.hibernate.orm;

import com.db.hibernate.entity.AbstractEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JdbcAccessor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public AbstractEntity load(Object id, Class<? extends AbstractEntity> entityClass) {
        StringBuilder sqlBuilder = new StringBuilder();
        String entityName = entityClass.getAnnotation(Entity.class).name();
        sqlBuilder.append("select id, version, update_time, data, deleted from ")
                .append(entityName).append(" where id = ").append(id);

        List<AbstractEntity> list = jdbcTemplate.query(sqlBuilder.toString(), new RowMapper<AbstractEntity>(){
            @SneakyThrows
            @Override
            public AbstractEntity mapRow(ResultSet rs, int rowNum) {
                AbstractEntity entity = entityClass.newInstance();
                entity.setId(rs.getString("id"));
                entity.setData(rs.getBytes("data"));
                entity.setDeleted(rs.getBoolean("deleted"));
                entity.setUpdateTime(rs.getInt("update_time"));
                entity.setVersion(rs.getInt("version"));
                return entity;
            }
        });

        AbstractEntity abstractEntity = list.get(0);
        abstractEntity.doDeserialize();
        return abstractEntity;
    }

}
