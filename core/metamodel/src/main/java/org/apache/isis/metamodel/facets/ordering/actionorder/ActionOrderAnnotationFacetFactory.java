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


package org.apache.isis.metamodel.facets.ordering.actionorder;

import org.apache.isis.applib.annotation.ActionOrder;
import org.apache.isis.metamodel.facets.FacetHolder;
import org.apache.isis.metamodel.facets.FacetUtil;
import org.apache.isis.metamodel.facets.MethodRemover;
import org.apache.isis.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.apache.isis.metamodel.spec.feature.ObjectFeatureType;


public class ActionOrderAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract {

    public ActionOrderAnnotationFacetFactory() {
        super(ObjectFeatureType.OBJECTS_ONLY);
    }

    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder facetHolder) {
        final ActionOrder annotation = (ActionOrder) getAnnotation(cls, ActionOrder.class);
        return FacetUtil.addFacet(create(annotation, facetHolder));
    }

    private ActionOrderFacet create(final ActionOrder annotation, final FacetHolder facetHolder) {
        return annotation == null ? null : new ActionOrderFacetAnnotation(annotation.value(), facetHolder);
    }

}