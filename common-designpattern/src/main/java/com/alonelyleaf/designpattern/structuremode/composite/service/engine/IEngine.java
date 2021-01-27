package com.alonelyleaf.designpattern.structuremode.composite.service.engine;

import com.alonelyleaf.designpattern.structuremode.composite.model.aggregates.TreeRich;
import com.alonelyleaf.designpattern.structuremode.composite.model.vo.EngineResult;

import java.util.Map;

public interface IEngine {

    EngineResult process(final Long treeId, final String userId, TreeRich treeRich, final Map<String, String> decisionMatter);

}
