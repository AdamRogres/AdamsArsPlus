package com.adamsmods.adamsarsplus.entities.ai.pathfinding;

public interface ICustomSizeNavigator {

    boolean isSmallerThanBlock();
    float getXZNavSize();
    int getYNavSize();
}
