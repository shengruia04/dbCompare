package com.db.mongo.entity.equip;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SkillData {

    private Map<Integer, Integer> skillMap = new HashMap<>();

}
