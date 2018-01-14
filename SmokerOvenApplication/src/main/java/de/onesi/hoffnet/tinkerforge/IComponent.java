package de.onesi.hoffnet.tinkerforge;

public interface IComponent {
    public String getUuid();

    public short identfier();
    public void initialize() throws Exception;
}
