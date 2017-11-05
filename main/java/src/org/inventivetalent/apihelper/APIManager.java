package org.inventivetalent.apihelper;

import org.bukkit.plugin.*;
import java.util.logging.*;
import org.bukkit.event.*;
import org.bukkit.*;
import javax.annotation.*;
import org.inventivetalent.apihelper.exception.*;
import java.util.*;

public class APIManager
{
    private static final Map<API, RegisteredAPI> HOST_MAP;
    private static final Map<Class<? extends API>, Set<Plugin>> PENDING_API_CLASSES;
    private static final Logger LOGGER;
    
    public static RegisteredAPI registerAPI(final API api) throws APIRegistrationException {
        if (APIManager.HOST_MAP.containsKey(api)) {
            throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is already registered");
        }
        final RegisteredAPI registeredAPI = new RegisteredAPI(api);
        APIManager.HOST_MAP.put(api, registeredAPI);
        api.load();
        APIManager.LOGGER.fine("'" + api.getClass().getName() + "' registered as new API");
        return registeredAPI;
    }
    
    public static RegisteredAPI registerAPI(final API api, final Plugin host) throws IllegalArgumentException, APIRegistrationException {
        validatePlugin(host);
        registerAPI(api);
        return registerAPIHost(api, host);
    }
    
    public static API registerEvents(final API api, final Listener listener) throws APIRegistrationException {
        if (!APIManager.HOST_MAP.containsKey(api)) {
            throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
        }
        final RegisteredAPI registeredAPI = APIManager.HOST_MAP.get(api);
        if (registeredAPI.eventsRegistered) {
            return api;
        }
        Bukkit.getPluginManager().registerEvents(listener, registeredAPI.getNextHost());
        registeredAPI.eventsRegistered = true;
        return api;
    }
    
    private static void initAPI(final API api) throws APIRegistrationException {
        if (!APIManager.HOST_MAP.containsKey(api)) {
            throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
        }
        final RegisteredAPI registeredAPI = APIManager.HOST_MAP.get(api);
        registeredAPI.init();
    }
    
    public static void initAPI(final Class<? extends API> clazz) throws APIRegistrationException {
        API clazzAPI = null;
        for (final API api : APIManager.HOST_MAP.keySet()) {
            if (api.getClass().equals(clazz)) {
                clazzAPI = api;
                break;
            }
        }
        if (clazzAPI == null) {
            if (!APIManager.PENDING_API_CLASSES.containsKey(clazz)) {
                throw new APIRegistrationException("API for class '" + clazz.getName() + "' is not registered");
            }
            APIManager.LOGGER.info("API class '" + clazz.getName() + "' is not yet initialized. Creating new instance.");
            try {
                clazzAPI = (API)clazz.newInstance();
                registerAPI(clazzAPI);
                for (final Plugin plugin : APIManager.PENDING_API_CLASSES.get(clazz)) {
                    if (plugin != null) {
                        registerAPIHost(clazzAPI, plugin);
                    }
                }
            }
            catch (ReflectiveOperationException e) {
                APIManager.LOGGER.warning("API class '" + clazz.getName() + "' is missing valid constructor");
            }
            APIManager.PENDING_API_CLASSES.remove(clazz);
        }
        initAPI(clazzAPI);
    }
    
    private static void disableAPI(final API api) {
        if (!APIManager.HOST_MAP.containsKey(api)) {
            return;
        }
        final RegisteredAPI registeredAPI = APIManager.HOST_MAP.get(api);
        registeredAPI.disable();
        APIManager.HOST_MAP.remove(api);
    }
    
    public static void disableAPI(final Class<? extends API> clazz) {
        API clazzAPI = null;
        for (final API api : APIManager.HOST_MAP.keySet()) {
            if (api.getClass().equals(clazz)) {
                clazzAPI = api;
                break;
            }
        }
        disableAPI(clazzAPI);
    }
    
    public static void require(final Class<? extends API> clazz, @Nullable final Plugin host) {
        try {
            if (host == null) {
                throw new APIRegistrationException();
            }
            registerAPIHost(clazz, host);
        }
        catch (APIRegistrationException e) {
            if (APIManager.PENDING_API_CLASSES.containsKey(clazz)) {
                APIManager.PENDING_API_CLASSES.get(clazz).add(host);
            }
            else {
                final Set<Plugin> hosts = new HashSet<Plugin>();
                hosts.add(host);
                APIManager.PENDING_API_CLASSES.put(clazz, hosts);
            }
        }
    }
    
    private static RegisteredAPI registerAPIHost(final API api, final Plugin host) throws APIRegistrationException {
        validatePlugin(host);
        if (!APIManager.HOST_MAP.containsKey(api)) {
            throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
        }
        final RegisteredAPI registeredAPI = APIManager.HOST_MAP.get(api);
        registeredAPI.registerHost(host);
        APIManager.LOGGER.fine("'" + host.getName() + "' registered as Host for '" + api + "'");
        return registeredAPI;
    }
    
    public static RegisteredAPI registerAPIHost(final Class<? extends API> clazz, final Plugin host) throws APIRegistrationException {
        validatePlugin(host);
        API clazzAPI = null;
        for (final API api : APIManager.HOST_MAP.keySet()) {
            if (api.getClass().equals(clazz)) {
                clazzAPI = api;
                break;
            }
        }
        if (clazzAPI == null) {
            throw new APIRegistrationException("API for class '" + clazz.getName() + "' is not registered");
        }
        return registerAPIHost(clazzAPI, host);
    }
    
    public static Plugin getAPIHost(final API api) throws APIRegistrationException, MissingHostException {
        if (!APIManager.HOST_MAP.containsKey(api)) {
            throw new APIRegistrationException("API for '" + api.getClass().getName() + "' is not registered");
        }
        return APIManager.HOST_MAP.get(api).getNextHost();
    }
    
    private static void validatePlugin(final Plugin plugin) {
        if (plugin instanceof API) {
            throw new IllegalArgumentException("Plugin must not implement API");
        }
    }
    
    static {
        HOST_MAP = new HashMap<API, RegisteredAPI>();
        PENDING_API_CLASSES = new HashMap<Class<? extends API>, Set<Plugin>>();
        LOGGER = Logger.getLogger("APIManager");
    }
}
