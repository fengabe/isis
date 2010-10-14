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


package org.apache.isis.webapp.view.field;

import java.util.HashMap;
import java.util.Map;

import org.apache.isis.metamodel.spec.feature.ObjectAssociation;


public class LinkedFieldsBlock extends InclusionList {
    private Map<String, LinkedObject> linkedFields = new HashMap<String, LinkedObject>();

    public void link(String field, String variable, String scope, String forwardView) {
        linkedFields.put(field, new LinkedObject(variable, scope, forwardView));
    }

    public LinkedObject[] linkedFields(ObjectAssociation[] fields) {
        LinkedObject[] includedFields = new LinkedObject[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String id2 = fields[i].getId();
            if (fields[i].isOneToOneAssociation() && linkedFields.containsKey(id2)) {
                includedFields[i] = linkedFields.get(id2);
            }
        }
        return includedFields;
    }

}
