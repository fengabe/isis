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


package org.apache.isis.remoting.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.isis.metamodel.adapter.oid.Oid;
import org.apache.isis.metamodel.adapter.version.Version;
import org.apache.isis.metamodel.authentication.AuthenticationSession;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.metamodel.spec.feature.ObjectActionType;
import org.apache.isis.metamodel.spec.feature.ObjectAssociation;
import org.apache.isis.remoting.client.transaction.ClientTransactionEvent;
import org.apache.isis.remoting.data.Data;
import org.apache.isis.remoting.data.common.CollectionData;
import org.apache.isis.remoting.data.common.EncodableObjectData;
import org.apache.isis.remoting.data.common.IdentityData;
import org.apache.isis.remoting.data.common.NullData;
import org.apache.isis.remoting.data.common.ObjectData;
import org.apache.isis.remoting.data.common.ReferenceData;
import org.apache.isis.remoting.data.query.PersistenceQueryData;
import org.apache.isis.remoting.exchange.AuthorizationRequestUsability;
import org.apache.isis.remoting.exchange.AuthorizationRequestVisibility;
import org.apache.isis.remoting.exchange.ClearAssociationRequest;
import org.apache.isis.remoting.exchange.ClearValueRequest;
import org.apache.isis.remoting.exchange.CloseSessionRequest;
import org.apache.isis.remoting.exchange.ExecuteClientActionRequest;
import org.apache.isis.remoting.exchange.ExecuteServerActionRequest;
import org.apache.isis.remoting.exchange.FindInstancesRequest;
import org.apache.isis.remoting.exchange.GetObjectRequest;
import org.apache.isis.remoting.exchange.GetPropertiesRequest;
import org.apache.isis.remoting.exchange.GetPropertiesResponse;
import org.apache.isis.remoting.exchange.HasInstancesRequest;
import org.apache.isis.remoting.exchange.OidForServiceRequest;
import org.apache.isis.remoting.exchange.OpenSessionRequest;
import org.apache.isis.remoting.exchange.OpenSessionResponse;
import org.apache.isis.remoting.exchange.AuthorizationResponse;
import org.apache.isis.remoting.exchange.ClearAssociationResponse;
import org.apache.isis.remoting.exchange.ClearValueResponse;
import org.apache.isis.remoting.exchange.CloseSessionResponse;
import org.apache.isis.remoting.exchange.ExecuteClientActionResponse;
import org.apache.isis.remoting.exchange.ExecuteServerActionResponse;
import org.apache.isis.remoting.exchange.FindInstancesResponse;
import org.apache.isis.remoting.exchange.GetObjectResponse;
import org.apache.isis.remoting.exchange.HasInstancesResponse;
import org.apache.isis.remoting.exchange.OidForServiceResponse;
import org.apache.isis.remoting.exchange.ResolveFieldRequest;
import org.apache.isis.remoting.exchange.ResolveFieldResponse;
import org.apache.isis.remoting.exchange.ResolveObjectRequest;
import org.apache.isis.remoting.exchange.ResolveObjectResponse;
import org.apache.isis.remoting.exchange.SetAssociationRequest;
import org.apache.isis.remoting.exchange.SetAssociationResponse;
import org.apache.isis.remoting.exchange.SetValueRequest;
import org.apache.isis.remoting.exchange.SetValueResponse;
import org.apache.isis.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.apache.isis.runtime.context.IsisContext;
import org.apache.isis.runtime.logging.Logger;

import com.google.inject.internal.Lists;


/**
 * previously called <tt>DistributionLogger</tt>.
 */
public class ServerFacadeLogger extends Logger implements ServerFacade {

    private static String PADDING = "      ";

    private final ObjectEncoderDecoder encoder;
    private final ServerFacade decorated;

    public ServerFacadeLogger(final ObjectEncoderDecoder encoder, final ServerFacade decorated, final String level) {
        super(level);
        this.encoder = encoder;
        this.decorated = decorated;
    }

    public ServerFacadeLogger(final ObjectEncoderDecoder encoder, final ServerFacade decorated) {
        this(encoder, decorated, null);
    }



    @Override
    protected Class getDecoratedClass() {
        return decorated.getClass();
    }

    //////////////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////////////

    public void init() {
        decorated.init();
    }

    public void shutdown() {
        decorated.shutdown();
    }



    //////////////////////////////////////////////////////////////////
    // authentication, authorization
    //////////////////////////////////////////////////////////////////

    public OpenSessionResponse openSession(OpenSessionRequest request) {
        log("authenticate");
        return decorated.openSession(request);
    }

    public AuthorizationResponse authorizeUsability(AuthorizationRequestUsability request) {
        log("authoriseUsability");
        return decorated.authorizeUsability(request);
    }

    public AuthorizationResponse authorizeVisibility(AuthorizationRequestVisibility request) {
        log("authoriseVisibility");
        return decorated.authorizeVisibility(request);
    }

    //////////////////////////////////////////////////////////////////
    // session
    //////////////////////////////////////////////////////////////////

    public CloseSessionResponse closeSession(CloseSessionRequest request) {
    	AuthenticationSession session = request.getSession();
        log("close session " + session);
        CloseSessionResponse response = decorated.closeSession(request);
        return response;
    }


    //////////////////////////////////////////////////////////////////
    // setAssociation, setValue, clearAssociation, clearValue
    //////////////////////////////////////////////////////////////////

    public SetAssociationResponse setAssociation(
            SetAssociationRequest request) {

    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetData = request.getTarget();
    	IdentityData associateData = request.getAssociate();

        log("set association " + fieldIdentifier + indentedNewLine() + "target: " + dump(targetData) + indentedNewLine()
                + "associate: " + dump(associateData));
        SetAssociationResponse response = decorated.setAssociation(request);
		final ObjectData[] changes = response.getUpdates();
        log("  <-- changes: " + dump(changes));
        return response;
    }

    public SetValueResponse setValue(
            final SetValueRequest request) {

    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();
    	EncodableObjectData value = request.getValue();

        log("set value " + fieldIdentifier + indentedNewLine() + "target: " + dump(target) + indentedNewLine() + "value: "
                + value);
        SetValueResponse response = decorated.setValue(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    public ClearAssociationResponse clearAssociation(
            final ClearAssociationRequest request) {
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();
    	IdentityData associate = request.getAssociate();

        log("clear association " + fieldIdentifier + indentedNewLine() + "target: " + dump(target) + indentedNewLine()
                + "associate: " + dump(associate));
        ClearAssociationResponse response = decorated.clearAssociation(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    public ClearValueResponse clearValue(
    		final ClearValueRequest request) {

    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();

        log("clear value " + fieldIdentifier + indentedNewLine() + "target: " + dump(target));
        ClearValueResponse response = decorated.clearValue(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // executeClientAction, executeServerAction
    //////////////////////////////////////////////////////////////////

    public ExecuteServerActionResponse executeServerAction(
            final ExecuteServerActionRequest request) {

    	ObjectActionType actionType = request.getActionType();
    	String actionIdentifier = request.getActionIdentifier();
    	ReferenceData target = request.getTarget();
    	Data[] parameters = request.getParameters();

        log("execute action " + actionIdentifier + "/" + actionType + indentedNewLine() + "target: " + dump(target)
                + indentedNewLine() + "parameters: " + dump(parameters));
        ExecuteServerActionResponse result;
        try {
            result = decorated.executeServerAction(request);
            log("  <-- returns: " + dump(result.getReturn()));
            log("  <-- persisted target: " + dump(result.getPersistedTarget()));
            log("  <-- persisted parameters: " + dump(result.getPersistedParameters()));
            log("  <-- updates: " + dump(result.getUpdates()));
            log("  <-- disposed: " + dump(result.getDisposed()));
        } catch (final RuntimeException e) {
            log("  <-- exception: " + e.getClass().getName() + " " + e.getMessage());
            throw e;
        }
        return result;
    }

    public ExecuteClientActionResponse executeClientAction(
    		ExecuteClientActionRequest request) {

    	ReferenceData[] data = request.getData();
    	int[] types = request.getTypes();

        List<Data> complete = Lists.newArrayList();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            str.append(indentedNewLine());
            str.append("[");
            str.append(i + 1);
            str.append("] ");
            switch (types[i]) {
            case ClientTransactionEvent.ADD:
                str.append("persisted: ");
                break;
            case ClientTransactionEvent.CHANGE:
                str.append("changed: ");
                break;
            case ClientTransactionEvent.DELETE:
                str.append("deleted: ");
                break;
            }
            dump(str, data[i], 3, complete);
        }
        log("execute client action " + str);

        final ExecuteClientActionResponse results = decorated.executeClientAction(request);

        complete = new Vector();
        str = new StringBuilder();
        final ReferenceData[] persistedUpdates = results.getPersisted();
        final Version[] changedVersions = results.getChanged();
        for (int i = 0; i < persistedUpdates.length; i++) {
            str.append(indentedNewLine());
            str.append("[");
            str.append(i + 1);
            str.append("] ");
            switch (types[i]) {
            case ClientTransactionEvent.ADD:
                str.append("persisted: ");
                dump(str, persistedUpdates[i], 3, complete);
                break;
            case ClientTransactionEvent.CHANGE:
                str.append("changed: ");
                str.append(changedVersions[i]);
                break;
            }
        }
        log(" <--- execute client action results" + str);
        /*
         * log(" <-- persisted: " + dump(results.getPersisted())); log(" <-- changed: " +
         * dump(results.getChanged()));
         */
        return results;
    }


    //////////////////////////////////////////////////////////////////
    // getObject, resolveXxx
    //////////////////////////////////////////////////////////////////

    public GetObjectResponse getObject(
    		GetObjectRequest request) {

    	Oid oid = request.getOid();

        log("get object " + oid);
        GetObjectResponse response = decorated.getObject(request);
		final ObjectData data = response.getObjectData();
        log(" <-- data: " + data);
        return response;
    }

    public ResolveFieldResponse resolveField(
    		ResolveFieldRequest request) {

    	IdentityData target = request.getTarget();
    	String fieldIdentifier = request.getFieldIdentifier();

        log("resolve field " + fieldIdentifier + " - " + dump(target));
        ResolveFieldResponse response = decorated.resolveField(request);
		final Data result = response.getData();
        log(" <-- data: " + dump(result));
        return response;
    }

    public ResolveObjectResponse resolveImmediately(
    		ResolveObjectRequest request) {

    	IdentityData target = request.getTarget();

        log("resolve immediately" + dump(target));
        ResolveObjectResponse response = decorated.resolveImmediately(request);
		final ObjectData objectData = response.getObjectData();
        log("  <-- data: " + dump(objectData));
        return response;
    }


    //////////////////////////////////////////////////////////////////
    // findInstances, hasInstances
    //////////////////////////////////////////////////////////////////

    public FindInstancesResponse findInstances(
    		FindInstancesRequest request) {

    	PersistenceQueryData criteria = request.getCriteria();

        log("find instances " + criteria);
        FindInstancesResponse response = decorated.findInstances(request);
		final ObjectData[] instances = response.getInstances();
        log(" <-- instances: " + dump(instances));
        return response;
    }

    public HasInstancesResponse hasInstances(
    		HasInstancesRequest request) {

    	String specificationName = request.getSpecificationName();

        log("has instances " + specificationName);
        HasInstancesResponse response = decorated.hasInstances(request);
		final boolean hasInstances = response.hasInstances();
        log(" <-- instances: " + (hasInstances ? "yes" : "no"));
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // getProperties
    //////////////////////////////////////////////////////////////////

    public GetPropertiesResponse getProperties(GetPropertiesRequest request) {
        log("get properties");
        GetPropertiesResponse response = decorated.getProperties(request);
		final Properties properties = response.getProperties();
        log(" <-- data: " + properties);
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // services
    //////////////////////////////////////////////////////////////////

    public OidForServiceResponse oidForService(
    		OidForServiceRequest request) {

    	String serviceId = request.getServiceId();

        log("oid for resource " + serviceId);

        OidForServiceResponse response = decorated.oidForService(request);
		final IdentityData oidData = response.getOidData();
        log(" <-- data: " + dump(oidData));
        return response;
    }


    //////////////////////////////////////////////////////////////////
    // Helpers
    //////////////////////////////////////////////////////////////////

    private String dump(final Data data) {
        final StringBuilder str = new StringBuilder();
        dump(str, data, 1, new ArrayList<Data>());
        return str.toString();
    }

    private String dump(final Data[] data) {
        final StringBuilder str = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            str.append("\n    [");
            str.append(i + 1);
            str.append("] ");
            dump(str, data[i], 3, new ArrayList<Data>());
        }
        return str.toString();
    }

    private void dump(final StringBuilder str, final Data data, final int indent, final List<Data> complete) {
        if (data == null) {
            str.append("null");
        } else if (data instanceof NullData) {
            str.append("NULL (NullData object)");
        } else if (data instanceof EncodableObjectData) {
            final EncodableObjectData encodeableObjectData = ((EncodableObjectData) data);
            str.append("ValueData@" + Integer.toHexString(encodeableObjectData.hashCode()) + " " + encodeableObjectData.getType()
                    + ":" + encodeableObjectData.getEncodedObjectData());
        } else if (data instanceof IdentityData) {
            final IdentityData referenceData = (IdentityData) data;
            str.append("ReferenceData@" + Integer.toHexString(referenceData.hashCode()) + " " + referenceData.getType() + ":"
                    + referenceData.getOid() + ":" + referenceData.getVersion());
        } else if (data instanceof ObjectData) {
            dumpObjectData(str, data, indent, complete);
        } else if (data instanceof CollectionData) {
            dumpCollectionData(str, data, indent, complete);
        } else {
            str.append("Unknown: " + data);
        }
    }

    private void dumpCollectionData(final StringBuilder str, final Data data, final int indent, final List<Data> complete) {
        final CollectionData objectData = ((CollectionData) data);
        str.append("CollectionData@" + Integer.toHexString(objectData.hashCode()) + " " + objectData.getType() + ":"
                + objectData.getOid() + ":" + (objectData.hasAllElements() ? "A" : "-") + ":" + objectData.getVersion());
        final Object[] elements = objectData.getElements();
        for (int i = 0; elements != null && i < elements.length; i++) {
            str.append("\n");
            str.append(padding(indent));
            str.append(i + 1);
            str.append(") ");
            dump(str, (Data) elements[i], indent + 1, complete);
        }
    }

    private void dumpObjectData(final StringBuilder str, final Data data, final int indent, final List<Data> complete) {
        final ObjectData objectData = ((ObjectData) data);
        str.append("ObjectData@" + Integer.toHexString(objectData.hashCode()) + " " + objectData.getType() + ":"
                + objectData.getOid() + ":" + (objectData.hasCompleteData() ? "C" : "-") + ":" + objectData.getVersion());

        if (complete.contains(objectData)) {
            str.append(" (already detailed)");
            return;
        }

        complete.add(objectData);
        final ObjectSpecification spec = IsisContext.getSpecificationLoader().loadSpecification(data.getType());
        final ObjectAssociation[] fs = encoder.getFieldOrder(spec);
        final Object[] fields = objectData.getFieldContent();
        for (int i = 0; fields != null && i < fields.length; i++) {
            str.append("\n");
            str.append(padding(indent));
            str.append(i + 1);
            str.append(") ");
            str.append(fs[i].getId());
            str.append(": ");
            dump(str, (Data) fields[i], indent + 1, complete);
        }
    }

    private String indentedNewLine() {
        return "\n" + padding(2);
    }

    private String padding(final int indent) {
        final int length = indent * 3;
        while (length > PADDING.length()) {
            PADDING += PADDING;
        }
        return PADDING.substring(0, length);
    }


}