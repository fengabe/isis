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


package org.apache.isis.viewer.dnd.view.base;

import org.apache.isis.core.commons.exceptions.UnexpectedCallException;
import org.apache.isis.viewer.dnd.view.Axes;
import org.apache.isis.viewer.dnd.view.Content;
import org.apache.isis.viewer.dnd.view.View;
import org.apache.isis.viewer.dnd.view.ViewRequirement;
import org.apache.isis.viewer.dnd.view.ViewSpecification;


public class NonBuildingSpecification implements ViewSpecification {
    private final String name;

    public NonBuildingSpecification(final View view) {
        final String name = view.getClass().getName();
        this.name = name.substring(name.lastIndexOf('.') + 1);
    }

    public View createView(final Content content, Axes axes, int sequence) {
        throw new UnexpectedCallException();
    }

    public String getName() {
        return name;
    }

    public boolean isAligned() {
        return false;
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReplaceable() {
        return false;
    }
    
    public boolean isResizeable() {
        return false;
    }

    public boolean isSubView() {
        return false;
    }

    public boolean canDisplay(ViewRequirement requirement) {
        return false;
    }

}