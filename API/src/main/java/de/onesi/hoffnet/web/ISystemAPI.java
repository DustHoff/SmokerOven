package de.onesi.hoffnet.web;

import de.onesi.hoffnet.web.data.Configuration;

public interface ISystemAPI {

    /**
     * ContextPath /state
     *
     * @return JSON String represents current OvenState
     */
    String state();

    /**
     * ContextPath /configure
     *
     * @param configuration
     * @return
     */
    String configure(Configuration configuration);

    /**
     * ContextPath /temperature
     *
     * @return JSON String represents current Temperature Pojo de.onesi.hoffnet.web.data.Temperature
     */
    String temperature();
}
