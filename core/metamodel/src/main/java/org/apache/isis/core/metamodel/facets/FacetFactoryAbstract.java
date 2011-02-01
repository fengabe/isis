/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.core.metamodel.facets;


import java.util.List;

import com.google.common.collect.ImmutableList;

import org.apache.isis.core.metamodel.facetapi.FeatureType;
import org.apache.isis.core.metamodel.spec.SpecificationLookup;
import org.apache.isis.core.metamodel.spec.SpecificationLookupAware;


public abstract class FacetFactoryAbstract implements FacetFactory, SpecificationLookupAware {

    private final List<FeatureType> featureTypes;
    
    private SpecificationLookup specificationLookup;

    public FacetFactoryAbstract(final List<FeatureType> featureTypes) {
        this.featureTypes = ImmutableList.copyOf(featureTypes);
    }

    @Override
    public List<FeatureType> getFeatureTypes() {
        return featureTypes;
    }

    @Override
    public void process(ProcessClassContext processClassContaxt) {
    }

    @Override
    public void process(ProcessMethodContext processMethodContext) {
    }

    @Override
    public void processParams(ProcessParameterContext processParameterContext) {
    }

    
    //////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    //////////////////////////////////////////////////////////////////
    
    protected SpecificationLookup getSpecificationLookup() {
        return specificationLookup;
    }

    /**
     * Injected
     */
    @Override
    public void setSpecificationLookup(final SpecificationLookup specificationLookup) {
        this.specificationLookup = specificationLookup;
    }

}