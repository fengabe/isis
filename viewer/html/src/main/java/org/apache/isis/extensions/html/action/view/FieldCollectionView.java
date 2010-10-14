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


package org.apache.isis.extensions.html.action.view;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.metamodel.spec.feature.OneToManyAssociation;
import org.apache.isis.extensions.html.action.view.util.TableUtil;
import org.apache.isis.extensions.html.component.Table;
import org.apache.isis.extensions.html.component.ViewPane;
import org.apache.isis.extensions.html.context.Context;
import org.apache.isis.extensions.html.request.Request;
import org.apache.isis.runtime.context.IsisContext;



public class FieldCollectionView extends ObjectViewAbstract {
    @Override
    protected void doExecute(final Context context, final ViewPane content, final ObjectAdapter object, final String field) {
        final String id = context.mapObject(object);
        final ObjectSpecification specification = object.getSpecification();

        final OneToManyAssociation collection = (OneToManyAssociation) specification.getAssociation(field);
        
        IsisContext.getPersistenceSession().resolveField(object, collection);
        
        context.addCollectionFieldCrumb(collection.getName());
        content.add(context.getComponentFactory().createHeading(collection.getName()));
        final Table table = TableUtil.createTable(context, id, object, collection);
        content.add(table);
        if (collection.isUsable(IsisContext.getAuthenticationSession(), object).isAllowed()) {
            content.add(context.getComponentFactory().createAddOption(id, collection.getId()));
        }
    }

    public String name() {
        return Request.FIELD_COLLECTION_COMMAND;
    }

}
