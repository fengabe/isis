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


package org.apache.isis.extensions.dnd.view.collection;

import org.apache.isis.commons.debug.DebugString;
import org.apache.isis.commons.exceptions.IsisException;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.consent.Consent;
import org.apache.isis.metamodel.consent.Veto;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.extensions.dnd.drawing.Image;
import org.apache.isis.extensions.dnd.drawing.ImageFactory;
import org.apache.isis.extensions.dnd.view.Content;


public class RootCollection extends AbstractCollectionContent {
    private final ObjectAdapter collection;

    public RootCollection(final ObjectAdapter collection) {
        this.collection = collection;
    }

    @Override
    public void debugDetails(final DebugString debug) {
        debug.appendln("collection", collection);
        super.debugDetails(debug);
    }

    @Override
    public ObjectAdapter getCollection() {
        return collection;
    }

    public String getHelp() {
        return "No help for this collection";
    }

    public String getIconName() {
        return null;
    }

    public String getId() {
        return "";
    }

    public ObjectAdapter getAdapter() {
        return collection;
    }

    public ObjectSpecification getSpecification() {
        return collection.getSpecification();
    }

    public boolean isTransient() {
        return collection != null;
    }

    public void setObject(final ObjectAdapter object) {
        throw new IsisException("Invalid call");
    }

    public String title() {
        return collection.titleString();
    }

    @Override
    public String windowTitle() {
        return collection.titleString();
    }

    @Override
    public String toString() {
        return "Root Collection: " + collection;
    }

    public ObjectAdapter drop(final Content sourceContent) {
        return null;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    @Override
    public Image getIconPicture(final int iconHeight) {
        // return ImageFactory.getInstance().loadObjectIcon(getSpecification(), "", iconHeight);
        return ImageFactory.getInstance().loadIcon("root-collection", iconHeight, null);
    }
}