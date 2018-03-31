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

    PREPAIRE {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(PREPAIRE_NOTHING, PREPAIRE_WAIT, START, FAILED);
        }
    },

    PREPAIRE_NOTHING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(PREPAIRE_WAIT, START, FAILED);
        }
    },

    PREPAIRE_WAIT {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(PREPAIRE_NOTHING, START, FAILED);
        }
    },

    START {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY, FAILED);
        }
    },

    /**
     * Busy
     */
    BUSY {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(HEATING, COOLING, SMOKE, AIR, FINISHED, FAILED);
        }
    },

    /**
     * Busy heating
     */
    HEATING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY, COOLING, SMOKE, AIR, FINISHED, FAILED);
        }
    },

    /**
     * Busy cooling
     */
    COOLING {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY, HEATING, SMOKE, AIR, FINISHED, FAILED);
        }
    },

    /**
     * Busy smoking
     */
    SMOKE {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY, HEATING, COOLING, AIR, FINISHED, FAILED);
        }
    },

    /**
     * Busy
     */
    AIR {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.of(BUSY, HEATING, COOLING, SMOKE, FINISHED, FAILED);
        }
    },

    /**
     * We're done.
     */
    FINISHED {
        @Override
        public EnumSet<OvenState> getNext() {
            return EnumSet.noneOf(OvenState.class);
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
