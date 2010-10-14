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


package org.apache.isis.extensions.dnd.tree;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.SpecificationFacets;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.metamodel.spec.feature.ObjectAssociationFilters;
import org.apache.isis.extensions.dnd.view.Axes;
import org.apache.isis.extensions.dnd.view.Content;
import org.apache.isis.extensions.dnd.view.ObjectContent;
import org.apache.isis.extensions.dnd.view.SubviewDecorator;
import org.apache.isis.extensions.dnd.view.View;
import org.apache.isis.extensions.dnd.view.ViewRequirement;
import org.apache.isis.extensions.dnd.view.border.SelectObjectBorder;
import org.apache.isis.runtime.context.IsisContext;


/**
 * Specification for a tree node that will display a closed object as a root node or within an object. This
 * will indicate that the created view can be opened if: one of it fields is a collection; it is set up to
 * show objects within objects and one of the fields is an object but it is not a lookup.
 * 
 * @see org.apache.isis.extensions.dnd.tree.OpenObjectNodeSpecification for displaying an open collection as
 *      part of an object.
 */
class ClosedObjectNodeSpecification extends NodeSpecification {
    private final boolean showObjectContents;
    private SubviewDecorator decorator = new SelectObjectBorder.Factory();

    public ClosedObjectNodeSpecification(final boolean showObjectContents) {
        this.showObjectContents = showObjectContents;
    }

    public boolean canDisplay(ViewRequirement requirement) {
        return requirement.isObject() && requirement.hasReference();
    }

    @Override
    public int canOpen(final Content content) {
        final ObjectAdapter object = ((ObjectContent) content).getObject();
        final ObjectAssociation[] fields = object.getSpecification().getAssociations(
                ObjectAssociationFilters.dynamicallyVisible(IsisContext.getAuthenticationSession(), object));
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isOneToManyAssociation()) {
                return CAN_OPEN;
            }

            if (  showObjectContents && fields[i].isOneToOneAssociation() &&
                !(SpecificationFacets.isBoundedSet(object.getSpecification()))) {
                return CAN_OPEN;
            }
        }
        return CANT_OPEN;
    }

    @Override
    protected View createNodeView(final Content content, Axes axes) {
        View treeLeafNode = new LeafNodeView(content, this);
        treeLeafNode = decorator.decorate(axes, treeLeafNode);
        return treeLeafNode;
    }

    public String getName() {
        return "Object tree node - closed";
    }
}