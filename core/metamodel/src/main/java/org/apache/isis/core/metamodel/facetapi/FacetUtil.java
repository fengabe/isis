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

package org.apache.isis.core.metamodel.facetapi;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.apache.isis.applib.filter.Filter;

public final class FacetUtil {

    private FacetUtil() {
    }

    /**
     * Attaches the {@link Facet} to its {@link Facet#getFacetHolder() facet
     * holder}.
     * 
     * @return <tt>true</tt> if a non-<tt>null</tt> facet was added,
     *         <tt>false</tt> otherwise.
     */
    public static boolean addFacet(final Facet facet) {
        if (facet == null) {
            return false;
        }
        facet.getFacetHolder().addFacet(facet);
        return true;
    }

    public static boolean addFacet(final MultiTypedFacet facet) {
        if (facet == null) {
            return false;
        }
        facet.getFacetHolder().addFacet(facet);
        return true;
    }

    /**
     * Attaches each {@link Facet} to its {@link Facet#getFacetHolder() facet
     * holder}.
     * 
     * @return <tt>true</tt> if any facets were added, <tt>false</tt> otherwise.
     */
    public static boolean addFacets(final Facet[] facets) {
        boolean addedFacets = false;
        for (final Facet facet : facets) {
            addedFacets = addFacet(facet) | addedFacets;
        }
        return addedFacets;
    }

    /**
     * Attaches each {@link Facet} to its {@link Facet#getFacetHolder() facet
     * holder}.
     * 
     * @return <tt>true</tt> if any facets were added, <tt>false</tt> otherwise.
     */
    public static boolean addFacets(final List<Facet> facetList) {
        boolean addedFacets = false;
        for (final Facet facet : facetList) {
            addedFacets = addFacet(facet) | addedFacets;
        }
        return addedFacets;
    }

    /**
     * Bit nasty, for use only by {@link FacetHolder}s that index their
     * {@link Facet}s in a Map.
     * 
     * @param facetsByClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Class<? extends Facet>[] getFacetTypes(final Map<Class<? extends Facet>, Facet> facetsByClass) {
        return facetsByClass.keySet().toArray(new Class[0]);
    }

    /**
     * Bit nasty, for use only by {@link FacetHolder}s that index their
     * {@link Facet}s in a Map.
     * 
     * @param facetsByClass
     * @return
     */
    public static List<Facet> getFacets(final Map<Class<? extends Facet>, Facet> facetsByClass, final Filter<Facet> filter) {
        final List<Facet> filteredFacets = Lists.newArrayList();
        final List<Facet> allFacets = new ArrayList<Facet>(facetsByClass.values());
        for (int i = 0; i < allFacets.size(); i++) {
            final Facet facet = allFacets.get(i);
            if (filter.accept(facet)) {
                filteredFacets.add(facet);
            }
        }
        return filteredFacets;
    }

    public static void removeFacet(final Map<Class<? extends Facet>, Facet> facetsByClass, final Facet facet) {
        removeFacet(facetsByClass, facet.facetType());
    }

    public static void removeFacet(final Map<Class<? extends Facet>, Facet> facetsByClass, final Class<? extends Facet> facetType) {
        final Facet facet = facetsByClass.get(facetType);
        if (facet == null) {
            return;
        }
        facetsByClass.remove(facetType);
        facet.setFacetHolder(null);
    }

    public static void addFacet(final Map<Class<? extends Facet>, Facet> facetsByClass, final Facet facet) {
        facetsByClass.put(facet.facetType(), facet);
    }

    public static Facet[] toArray(final List<Facet> facetList) {
        if (facetList == null) {
            return new Facet[0];
        } else {
            return facetList.toArray(new Facet[] {});
        }
    }

    public static Hashtable<Class<? extends Facet>, Facet> getFacetsByType(final FacetHolder facetHolder) {
        final Hashtable<Class<? extends Facet>, Facet> facetByType = new Hashtable<Class<? extends Facet>, Facet>();
        final Class<? extends Facet>[] facetsFor = facetHolder.getFacetTypes();
        for (final Class<? extends Facet> facetType : facetsFor) {
            final Facet facet = facetHolder.getFacet(facetType);
            facetByType.put(facetType, facet);
        }
        return facetByType;
    }

}
