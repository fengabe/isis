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

package org.apache.isis.core.runtime.system;

import org.apache.isis.core.commons.config.ConfigurationConstants;
import org.apache.isis.core.commons.config.IsisConfiguration;
import org.apache.isis.core.metamodel.spec.SpecificationLoaderSpi;
import org.apache.isis.core.metamodel.specloader.ObjectReflectorInstaller;
import org.apache.isis.core.runtime.authentication.AuthenticationManager;
import org.apache.isis.core.runtime.authentication.AuthenticationManagerInstaller;
import org.apache.isis.core.runtime.authorization.AuthorizationManager;
import org.apache.isis.core.runtime.authorization.AuthorizationManagerInstaller;
import org.apache.isis.core.runtime.fixtures.FixturesInstaller;
import org.apache.isis.core.runtime.imageloader.TemplateImageLoaderInstaller;
import org.apache.isis.core.runtime.installerregistry.installerapi.EmbeddedWebServerInstaller;
import org.apache.isis.core.runtime.installerregistry.installerapi.IsisViewerInstaller;
import org.apache.isis.core.runtime.installerregistry.installerapi.PersistenceMechanismInstaller;
import org.apache.isis.core.runtime.services.ServicesInstaller;
import org.apache.isis.core.runtime.system.persistence.PersistenceSessionFactory;
import org.apache.isis.core.runtime.userprofile.UserProfileStore;
import org.apache.isis.core.runtime.userprofile.UserProfileStoreInstaller;
import org.apache.isis.core.runtime.viewer.IsisViewer;

public final class SystemConstants {

    /**
     * Key used to lookup {@link DeploymentType} (eg via command line) in
     * {@link IsisConfiguration}.
     * 
     * <p>
     * Use {@link DeploymentType#valueOf(String)} to decode.
     */

    public static final String DEPLOYMENT_TYPE_KEY = ConfigurationConstants.ROOT + "deploymentType";
    /**
     * Key used to lookup {@link SpecificationLoaderSpi specification Loader} in
     * {@link IsisConfiguration}, and root for any
     * {@link ObjectReflectorInstaller reflector}-specific configuration keys.
     */
    public final static String REFLECTOR_KEY = ConfigurationConstants.ROOT + ObjectReflectorInstaller.TYPE;
    
    /**
     * Default for {@link #REFLECTOR_KEY}
     */
    public static final String REFLECTOR_DEFAULT = "java";

    /**
     * Key used to lookup {@link IsisViewer viewer} in {@link IsisConfiguration}
     * , and root for any {@link IsisViewerInstaller viewer}-specific
     * configuration keys.
     */
    public final static String VIEWER_KEY = ConfigurationConstants.ROOT + IsisViewerInstaller.TYPE;
    /**
     * Default for {@link #VIEWER_KEY}.
     */
    public static final String VIEWER_DEFAULT = "dnd";

    /**
     * Key used to lookup {@link PersistenceSessionFactory persistor} in
     * {@link IsisConfiguration}, and root for any
     * {@link PersistenceMechanismInstaller persistor}-specific configuration
     * keys.
     */
    public final static String OBJECT_PERSISTOR_INSTALLER_KEY = ConfigurationConstants.ROOT + PersistenceMechanismInstaller.TYPE;

    // TODO: inline
    public static final String OBJECT_PERSISTOR_KEY = OBJECT_PERSISTOR_INSTALLER_KEY;
    // TODO: move to being a responsibility of DeploymentType instead
    public static final String OBJECT_PERSISTOR_NON_PRODUCTION_DEFAULT = "in-memory";
    // TODO: move to being a responsibility of DeploymentType instead
    public static final String OBJECT_PERSISTOR_PRODUCTION_DEFAULT = "xml";

    /**
     * Key used to lookup {@link UserProfileStore user profile store} (via
     * command line) in {@link IsisConfiguration}, and root for any
     * {@link UserProfileStoreInstaller user profile store} -specific
     * configuration keys.
     */
    public final static String PROFILE_PERSISTOR_INSTALLER_KEY = ConfigurationConstants.ROOT + UserProfileStoreInstaller.TYPE;

    // TODO: inline
    public static final String USER_PROFILE_STORE_KEY = PROFILE_PERSISTOR_INSTALLER_KEY;
    // TODO: move to being a responsibility of DeploymentType instead
    public static final String USER_PROFILE_STORE_NON_PRODUCTION_DEFAULT = "in-memory";
    // TODO: move to being a responsibility of DeploymentType instead
    public static final String USER_PROFILE_STORE_PRODUCTION_DEFAULT = "xml";

    /**
     * Key used to lookup {@link AuthenticationManager authentication manager}
     * in {@link IsisConfiguration}, and root for any
     * {@link AuthenticationManagerInstaller authentication manager}-specific
     * configuration keys.
     */
    public static final String AUTHENTICATION_INSTALLER_KEY = ConfigurationConstants.ROOT + AuthenticationManagerInstaller.TYPE;
    /**
     * Default for {@link #AUTHENTICATION_INSTALLER_KEY} if not exploring.
     */
    public static final String AUTHENTICATION_DEFAULT = "file";
    /**
     * Default for {@link #AUTHENTICATION_INSTALLER_KEY} if exploring.
     */
    public static final String AUTHENTICATION_EXPLORATION_DEFAULT = "bypass";

    /**
     * Key used to lookup {@link AuthorizationManager authorization manager} in
     * {@link IsisConfiguration}, and root for any
     * {@link AuthorizationManagerInstaller authorization manager}-specific
     * configuration keys.
     */
    public static final String AUTHORIZATION_INSTALLER_KEY = ConfigurationConstants.ROOT + AuthorizationManagerInstaller.TYPE;
    /**
     * Default for {@link #AUTHORIZATION_DEFAULT} if production.
     */
    public static final String AUTHORIZATION_DEFAULT = "file";

    /**
     * Default for {@link #AUTHORIZATION_DEFAULT} if not production.
     */
    public static final String AUTHORIZATION_NON_PRODUCTION_DEFAULT = "bypass";

    /**
     * Key used to lookup {@link ServicesInstaller services installer} in
     * {@link IsisConfiguration}, and root for any {@link ServicesInstaller
     * services installer}-specific configuration keys.
     */
    public static final String SERVICES_INSTALLER_KEY = ConfigurationConstants.ROOT + ServicesInstaller.TYPE;
    /**
     * Default for {@link #SERVICES_INSTALLER_KEY}
     */
    public static final String SERVICES_INSTALLER_DEFAULT = "configuration";

    /**
     * Key used to lookup {@link FixturesInstaller fixtures installer} in
     * {@link IsisConfiguration}, and root for any {@link FixturesInstaller
     * fixtures installer}-specific configuration keys.
     */
    public static final String FIXTURES_INSTALLER_KEY = ConfigurationConstants.ROOT + FixturesInstaller.TYPE;
    /**
     * Default for {@link #FIXTURES_INSTALLER_KEY}
     */
    public static final String FIXTURES_INSTALLER_DEFAULT = "configuration";

    /**
     * Key used to lookup {@link TemplateImageLoaderInstaller template image
     * loader} in {@link IsisConfiguration}, and root for any
     * {@link TemplateImageLoaderInstaller template image loader}-specific
     * configuration keys.
     */
    public static final String IMAGE_LOADER_KEY = ConfigurationConstants.ROOT + TemplateImageLoaderInstaller.TYPE;
    /**
     * Default for {@link #IMAGE_LOADER_KEY}
     */
    public static final String IMAGE_LOADER_DEFAULT = "awt";


    /**
     * Key used to lookup {@link EmbeddedWebServerInstaller embedded web
     * installer} in {@link IsisConfiguration}, and root for any
     * {@link EmbeddedWebServerInstaller embedded web server installer}-specific
     * configuration keys.
     */
    public static final String WEBSERVER_KEY = ConfigurationConstants.ROOT + EmbeddedWebServerInstaller.TYPE;
    /**
     * Default for {@link #WEBSERVER_KEY}
     */
    public static final String WEBSERVER_DEFAULT = "jetty";

    /**
     * Key by which requested fixture (eg via command line) is made available in
     * {@link IsisConfiguration}.
     */
    public final static String FIXTURE_KEY = ConfigurationConstants.ROOT + "fixture";

    /**
     * Key by which requested user (eg via command line) is made available in
     * {@link IsisConfiguration} .
     */
    public final static String USER_KEY = ConfigurationConstants.ROOT + "user";

    /**
     * Key by which requested password (eg via command line) is made available
     * in {@link IsisConfiguration}.
     */
    public final static String PASSWORD_KEY = ConfigurationConstants.ROOT + "password";

    /**
     * Key as to whether to show splash (eg via command line) is made available
     * in {@link IsisConfiguration}.
     * 
     * <p>
     * Use {@link Splash#valueOf(String)} to decode.
     */
    public static final String NOSPLASH_KEY = ConfigurationConstants.ROOT + "nosplash";
    public static final boolean NOSPLASH_DEFAULT = false;

    public static final String LOCALE_KEY = ConfigurationConstants.ROOT + "locale";

    private SystemConstants() {
    }

}
