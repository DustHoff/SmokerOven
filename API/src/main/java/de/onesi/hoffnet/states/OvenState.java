package de.onesi.hoffnet.states;

import java.util.EnumSet;

public enum OvenState {

    /**
     * Booting up the application.
     * Next will be {@link #READY}
     */
    INITIALIZE {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(READY, FAILED);
        }
    },

    /**
     * Application and oven is ready and waiting for
     * {@link de.onesi.hoffnet.web.data.Configuration} input
     */
    READY {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(PREPAIRE, FAILED);
        }
    },

    /**
     * Something went wrong. Re-initialization will be performed automatically.
     * Next will be {@link #INITIALIZE}
     */
    FAILED {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(INITIALIZE);
        }
    },
    /**
     * parent State
     */
    PREPAIRE {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(PREPAIRE_NOTHING, PREPAIRE_WAIT);
        }
    },
    
    /**
     * Internal State
     * Placebo State. Nothing have to prepaired
     */
    PREPAIRE_NOTHING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(START);
        }
    },

    /**
     * Internal State
     * Timebased Release
     */
    PREPAIRE_WAIT {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(START);
        }
    },

    /**
     * Fork State
     * 2 working Threads
     */
    START {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY);
        }
    },

    /**
     * parent State for the worker threads
     * 1 Thread starts with HEATING (BASED on Temperature)
     * 1 Thread starts with SMOKE
     * Busy
     */
    BUSY {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(HEATING, COOLING, SMOKE, AIR, FAIlED);
        }
    },

    /**
     * Internal State
     * Busy heating
     */
    HEATING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(COOLING, FINISHED, FAILED);
        }
    },

    /**
     * Internal State
     * Busy cooling
     */
    COOLING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(HEATING, FINISHED, FAILED);
        }
    },

    /**
     * Internal State
     * Busy smoking
     */
    SMOKE {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(AIR, FINISHED, FAILED);
        }
    },

    /**
     * Internal State
     * Busy
     */
    AIR {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(SMOKE, FINISHED, FAILED);
        }
    },

    /**
     * Join State 
     * 1 Thread Busy (HEATING,COOLING)
     * 1 Thread Busy (SMOKE, AIR)
     * We're done.
     */
    FINISHED {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(READY);
        }
    };

    /**
     * @return the next possible states after this
     */
    abstract public EnumSet<OvenState> getNext();

    @Override
    public String toString() {
        return name();
    }
}
