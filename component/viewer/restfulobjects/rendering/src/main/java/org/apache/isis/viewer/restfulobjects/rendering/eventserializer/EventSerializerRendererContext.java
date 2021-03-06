package org.apache.isis.viewer.restfulobjects.rendering.eventserializer;

import java.util.Collections;
import java.util.List;

import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.profiles.Localization;
import org.apache.isis.core.commons.authentication.AuthenticationSession;
import org.apache.isis.core.metamodel.adapter.mgr.AdapterManager;
import org.apache.isis.core.runtime.system.context.IsisContext;
import org.apache.isis.core.runtime.system.persistence.PersistenceSession;
import org.apache.isis.viewer.restfulobjects.rendering.RendererContext;

public class EventSerializerRendererContext implements RendererContext {

    private final String baseUrl;
    private final Where where;
    
    public EventSerializerRendererContext(String baseUrl, Where where) {
        this.baseUrl = baseUrl;
        this.where = where;
    }

    @Override
    public String urlFor(String url) {
        return baseUrl + url;
    }

    @Override
    public AuthenticationSession getAuthenticationSession() {
        return IsisContext.getAuthenticationSession();
    }

    @Override
    public PersistenceSession getPersistenceSession() {
        return IsisContext.getPersistenceSession();
    }

    @Override
    public AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

    @Override
    public List<List<String>> getFollowLinks() {
        return Collections.emptyList();
    }

    @Override
    public Where getWhere() {
        return where;
    }

    @Override
    public Localization getLocalization() {
        return IsisContext.getLocalization();
    }


}
