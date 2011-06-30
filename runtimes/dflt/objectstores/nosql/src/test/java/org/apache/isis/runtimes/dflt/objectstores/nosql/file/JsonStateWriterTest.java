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
package org.apache.isis.runtimes.dflt.objectstores.nosql.file;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.runtimes.dflt.objectstores.nosql.StateWriter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class JsonStateWriterTest {

    private JsonStateWriter writer;

    @Before
    public void setup() {
        writer = new JsonStateWriter(null, "spec-name");
    }

    @Test
    public void noData() throws Exception {
        assertEquals("{}", writer.getData());
    }

    @Test
    public void basicData() throws Exception {
        writer.writeId("#1");
        writer.writeTime("ddmmyy");
        writer.writeType("com.planchase.ClassName");
        writer.writeVersion("1", "2");
        writer.writeUser("fred");
        assertEquals("{\n    \"_id\": \"#1\",\n    \"_time\": \"ddmmyy\",\n    \"_type\": \"com.planchase.ClassName\",\n    \"_user\": \"fred\",\n    \"_version\": \"2\"\n}", writer.getData());
    }

    @Test
    public void encrytionVersion() throws Exception {
        writer.writeEncryptionType("etc1");
        assertEquals("{\"_encrypt\": \"etc1\"}", writer.getData());
    }

    @Test
    public void numberData() throws Exception {
        writer.writeField("number", 1239912);
        assertEquals("{\"number\": \"1239912\"}", writer.getData());
    }

    @Test
    public void stringData() throws Exception {
        writer.writeField("number", "string-data");
        assertEquals("{\"number\": \"string-data\"}", writer.getData());
    }

    @Test
    public void nullData() throws Exception {
        writer.writeField("number", null);
        assertEquals("{\"number\": null}", writer.getData());
    }

    @Test
    public void addAggregate() throws Exception {
        StateWriter aggregate = writer.addAggregate("#4");
        aggregate.writeField("number", "string-data");
        assertEquals("{\"#4\": {\"number\": \"string-data\"}}", writer.getData());
    }

    @Test
    public void elementData() throws Exception {
        List<StateWriter> elements = new ArrayList<StateWriter>();
        StateWriter elementWriter1 = writer.createElementWriter();
        elementWriter1.writeField("number", "1");
        elements.add(elementWriter1);
        StateWriter elementWriter2 = writer.createElementWriter();
        elementWriter2.writeField("number", "4");
        elements.add(elementWriter2);
        
        writer.writeCollection("coll", elements);
        
        assertEquals("{\"coll\": [\n    {\"number\": \"1\"},\n    {\"number\": \"4\"}\n]}", writer.getData());
    }
    
    @Test
    public void requestData() throws Exception {
        writer.writeType("com.planchase.ClassName");
        writer.writeId("#8");
        writer.writeVersion("1", "2");
        assertEquals("com.planchase.ClassName #8 1 2", writer.getRequest());
    }

}