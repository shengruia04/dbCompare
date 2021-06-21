package com.db.mongo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PojoEntity<PK, S, T, V> extends AbstractEntity<PK> {

    protected S sss;

    protected T ttt;

    protected T[] tArray;

    protected T[][] tArrayArray;

    protected List<T[]> tArrayList;

    protected Map<Integer, List<T[]>> tArrayListMap;

    protected List<V> vList = new ArrayList<>();
}
