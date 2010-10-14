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


package org.apache.isis.metamodel.specloader.internal;

import org.apache.isis.metamodel.adapter.Instance;
import org.apache.isis.metamodel.adapter.MutableProposedHolder;
import org.apache.isis.metamodel.adapter.ObjectAdapter;
import org.apache.isis.metamodel.consent.InteractionInvocationMethod;
import org.apache.isis.metamodel.consent.InteractionResultSet;
import org.apache.isis.metamodel.facets.object.parseable.ParseableFacet;
import org.apache.isis.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.apache.isis.metamodel.facets.propparam.typicallength.TypicalLengthFacet;
import org.apache.isis.metamodel.facets.propparam.validate.maxlength.MaxLengthFacet;
import org.apache.isis.metamodel.interactions.InteractionUtils;
import org.apache.isis.metamodel.interactions.ValidityContext;
import org.apache.isis.metamodel.spec.ObjectSpecification;
import org.apache.isis.metamodel.spec.feature.ObjectActionParameter;
import org.apache.isis.metamodel.spec.feature.OneToOneActionParameter;
import org.apache.isis.metamodel.spec.feature.ParseableEntryActionParameter;
import org.apache.isis.metamodel.specloader.internal.peer.ObjectActionParamPeer;


public class ObjectActionParameterParseable extends ObjectActionParameterAbstract implements
        ParseableEntryActionParameter {

    public ObjectActionParameterParseable(
            final int index,
            final ObjectActionImpl action,
            final ObjectActionParamPeer peer) {
        super(index, action, peer);
    }

    public int getNoLines() {
        final MultiLineFacet facet = getFacet(MultiLineFacet.class);
        return facet.numberOfLines();
    }

    public boolean canWrap() {
        final MultiLineFacet facet = getFacet(MultiLineFacet.class);
        return !facet.preventWrapping();
    }

    public int getMaximumLength() {
        final MaxLengthFacet facet = getFacet(MaxLengthFacet.class);
        return facet.value();
    }

    public int getTypicalLineLength() {
        final TypicalLengthFacet facet = getFacet(TypicalLengthFacet.class);
        return facet.value();
    }

    /**
     * Invoked when tab away, disables the OK button.
     * 
     * <p>
     * Assumed to be invoked {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public String isValid(final ObjectAdapter adapter, final Object proposedValue) {

        if (!(proposedValue instanceof String)) {
            return null;
        }
        final String proposedString = (String) proposedValue;

        final ObjectActionParameter objectActionParameter = getAction().getParameters()[getNumber()];
        if (!(objectActionParameter instanceof ParseableEntryActionParameter)) {
            return null;
        }
        final ParseableEntryActionParameter parameter = (ParseableEntryActionParameter) objectActionParameter;

        final ObjectSpecification parameterSpecification = parameter.getSpecification();
        final ParseableFacet p = parameterSpecification.getFacet(ParseableFacet.class);
        final ObjectAdapter newValue = p.parseTextEntry(null, proposedString);

        final ValidityContext<?> ic = parameter.createProposedArgumentInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, adapter, arguments(newValue), getNumber());

        final InteractionResultSet buf = new InteractionResultSet();
        InteractionUtils.isValidResultSet(parameter, ic, buf);
        if (buf.isVetoed()) {
            return buf.getInteractionResult().getReason();
        }
        return null;

    }

    /**
     * TODO: this is not ideal, because we can only populate the array for single argument, rather than the
     * entire argument set. Instead, we ought to do this in two passes, one to build up the argument set as a
     * single unit, and then validate each in turn.
     * 
     * @param proposedValue
     * @return
     */
    private ObjectAdapter[] arguments(final ObjectAdapter proposedValue) {
        final int parameterCount = getAction().getParameterCount();
        final ObjectAdapter[] arguments = new ObjectAdapter[parameterCount];
        arguments[getNumber()] = proposedValue;
        return arguments;
    }


    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(ObjectAdapter adapter) {
        OneToOneActionParameter specification = this;
        return adapter.getInstance(specification);
    }


    
    // //////////////////////////////////////////////////////////////////////
    // get, set
    // //////////////////////////////////////////////////////////////////////

    /**
     * Gets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, wrapping the proposed value into a {@link ObjectAdapter}.
     */
    public ObjectAdapter get(ObjectAdapter owner) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object proposed = proposedHolder.getProposed();
        return getRuntimeContext().adapterFor(proposed);
    }

    /**
     * Sets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, unwrapped the proposed value from a {@link ObjectAdapter}.
     */
    public void set(ObjectAdapter owner, ObjectAdapter newValue) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object newValuePojo = newValue.getObject();
        proposedHolder.setProposed(newValuePojo);
    }

    private MutableProposedHolder getProposedHolder(ObjectAdapter owner) {
        Instance instance = getInstance(owner);
        if(!(instance instanceof MutableProposedHolder)) {
            throw new IllegalArgumentException("Instance should implement MutableProposedHolder");
        }
        MutableProposedHolder proposedHolder = (MutableProposedHolder) instance;
        return proposedHolder;
    }


}