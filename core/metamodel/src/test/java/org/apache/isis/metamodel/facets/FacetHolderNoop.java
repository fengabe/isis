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


package org.apache.isis.metamodel.facets;

import org.apache.isis.applib.Identifier;
import org.apache.isis.commons.filters.Filter;


/**
 * Has no functionality but makes it easier to write tests that require an instance of an {@link Identifier}.
 */
public class FacetHolderNoop implements FacetHolder {

    public void addFacet(final Facet facet) {}

    public void addFacet(final MultiTypedFacet facet) {}

    public boolean containsFacet(final Class<? extends Facet> facetType) {
        return false;
    }

    public <T extends Facet> T getFacet(final Class<T> cls) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Facet>[] getFacetTypes() {
        return new Class[0];
    }

    public Facet[] getFacets(final Filter<Facet> filter) {
        return new Facet[0];
    }

    public Identifier getIdentifier() {
        return null;
    }

    public void removeFacet(final Facet facet) {}

    public void removeFacet(final Class<? extends Facet> facetType) {}

}
