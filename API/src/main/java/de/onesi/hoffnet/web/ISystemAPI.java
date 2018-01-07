package de.onesi.hoffnet.web;

import de.onesi.hoffnet.web.data.Configuration;

public interface ISystemAPI {

    /**
     * ContextPath /state
     *
     * @return JSON String represents current de.onesi.hoffnet.states.OvenState
     */
    String state();

    /**
     * ContextPath /configure
     * in State OvenState.READY it can get configured
     * the workflow starts, when it got successfully configured
     *
     * @param configuration
     * @return
     */
    String configure(Configuration configuration);

    /**
     * ContextPath /temperature
     * at any time you can get the Temperature of the system
     * @return JSON String represents current Temperature Pojo de.onesi.hoffnet.web.data.Temperature
     */
    String temperature();
}
