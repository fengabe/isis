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


package org.apache.isis.webapp.view.action;

import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.runtimecontext.spec.feature.ObjectActionSet;
import org.apache.isis.metamodel.spec.feature.ObjectAction;
import org.apache.isis.metamodel.spec.feature.ObjectActionType;
import org.apache.isis.runtime.context.IsisContext;
import org.apache.isis.webapp.AbstractElementProcessor;
import org.apache.isis.webapp.Dispatcher;
import org.apache.isis.webapp.context.RequestContext.Scope;
import org.apache.isis.webapp.processor.Request;
import org.apache.isis.webapp.util.MethodsUtils;
import org.apache.isis.webapp.view.field.InclusionList;


public class Methods extends AbstractElementProcessor {

    public void process(Request request) {
        String objectId = request.getOptionalProperty(OBJECT);
        boolean showForms = request.isRequested(FORMS, false);
        ObjectAdapter object = MethodsUtils.findObject(request.getContext(), objectId);
        if (objectId == null) {
            objectId = request.getContext().mapObject(object, null);
        }
        
        InclusionList inclusionList = new InclusionList();
        request.setBlockContent(inclusionList);
        request.processUtilCloseTag();
        
        request.appendHtml("<div class=\"actions\">");
        request.appendHtml("<div class=\"action\">");
        if (inclusionList.includes("edit") && !object.getSpecification().isService()) {
            request.appendHtml("<a href=\"_generic_edit." + Dispatcher.EXTENSION + "?_result=" + objectId + "\">Edit</a>");
        }
        request.appendHtml("</div>");
        writeMethods(request, objectId, object, showForms, inclusionList);
        request.popBlockContent();
        request.appendHtml("</div>");
    }

    public static void writeMethods(Request request, String objectId, ObjectAdapter adapter, boolean showForms, InclusionList inclusionList) {
        ObjectAction[] actions = adapter.getSpecification().getObjectActions(ObjectActionType.USER);
        writeMethods(request, adapter, actions, objectId, showForms, inclusionList);
        // TODO determine if system is set up to display exploration methods
        if (true) {
            actions = adapter.getSpecification().getObjectActions(ObjectActionType.EXPLORATION);
            writeMethods(request, adapter, actions, objectId, showForms, inclusionList);
        }
        // TODO determine if system is set up to display debug methods
        if (true) {
            actions = adapter.getSpecification().getObjectActions(ObjectActionType.DEBUG);
            writeMethods(request, adapter, actions, objectId, showForms, inclusionList);
        }
    }

    private static void writeMethods(Request request, ObjectAdapter adapter, ObjectAction[] actions, String objectId, boolean showForms, InclusionList inclusionList) {
        actions = inclusionList.includedActions(actions);
        for (int j = 0; j < actions.length; j++) {
            ObjectAction action = actions[j];
            if (action instanceof ObjectActionSet) {
                request.appendHtml("<div class=\"actions\">");
                writeMethods(request, adapter, action.getActions(), objectId, showForms, inclusionList);
                request.appendHtml("</div>");
            } else if (action.isContributed() && action.getParameterCount() == 1
                    && adapter.getSpecification().isOfType(action.getParameters()[0].getSpecification())) {
                if (objectId != null) {
                    ObjectAdapter target = (ObjectAdapter) request.getContext().getMappedObject(objectId);
                    ObjectAdapter realTarget = action.realTarget(target);
                    String realTargetId = request.getContext().mapObject(realTarget, Scope.INTERACTION);
                    writeMethod(request, adapter, new String[] { objectId }, action, realTargetId, showForms);
                } else {
                    request.appendHtml("<div class=\"action\">" + action.getName() + "???</div>");
                }
            } else {
                writeMethod(request, adapter, new String[0], action, objectId, showForms);
            }
        }
    }

    private static void writeMethod(
            Request request,
            ObjectAdapter adapter,
            String[] parameters,
            ObjectAction action,
            String objectId,
            boolean showForms) {
//        if (action.isVisible(IsisContext.getSession(), null) && action.isVisible(IsisContext.getSession(), adapter)) {
        if (action.isVisible(IsisContext.getAuthenticationSession(), adapter).isAllowed()) {
            request.appendHtml("<div class=\"action\">");
            if (IsisContext.getSession() == null) {
                request.appendHtml("<span class=\"disabled\" title=\"no user logged in\">");
                request.appendHtml(action.getName());
                request.appendHtml("</span>");
/*            } else if (action.isUsable(IsisContext.getSession(), null).isVetoed()) {
                request.appendHtml("<span class=\"disabled\" title=\"" + action.isUsable(IsisContext.getSession(), null).getReason() + "\">");
                request.appendHtml(action.getName());
                request.appendHtml("</span>");
   */         } else if (action.isUsable(IsisContext.getAuthenticationSession(), adapter).isVetoed()) {
                request.appendHtml("<span class=\"disabled\" title=\"" + action.isUsable(IsisContext.getAuthenticationSession(), adapter).getReason() + "\">");
                request.appendHtml(action.getName());
                request.appendHtml("</span>");
            } else {
                String version = request.getContext().mapVersion(adapter);
                if (action.getParameterCount() == 0) {
                    ActionButton.write(request, adapter, action, parameters, objectId, version, "_generic." + Dispatcher.EXTENSION, null,
                            null, null, null, null, null, null);
                } else if (showForms) {
                    CreateFormParameter params = new CreateFormParameter();
                    params.objectId = objectId;
                    params.methodName = action.getId();
                    params.forwardResultTo = "_generic." + Dispatcher.EXTENSION;
                    params.buttonTitle = "OK";
                    params.legend = action.getName();
                  //  parameters.resultName = request.getOptionalProperty(RESULT_NAME);
                   // parameters.resultOverride = request.getOptionalProperty(RESULT_OVERRIDE);
                  //  parameters.scope = request.getOptionalProperty(SCOPE);
                  //  parameters.className = request.getOptionalProperty(CLASS, "action");
                  //  parameters.id = request.getOptionalProperty(ID);
                    ActionForm.createForm(request, params, true);
                } else {
                    request.appendHtml("<a href=\"_generic_action." + Dispatcher.EXTENSION + "?_result=" + objectId + "&" + VERSION + "=" + version + "&method="
                            + action.getId() + "\" title=" + action.getDescription() + ">" + action.getName() + "</a>");
                }
            }
            request.appendHtml("</div>");
        }
    }

    public String getName() {
        return "methods";
    }

}
