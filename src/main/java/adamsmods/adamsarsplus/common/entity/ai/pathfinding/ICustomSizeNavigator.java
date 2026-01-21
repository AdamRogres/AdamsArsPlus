package adamsmods.adamsarsplus.common.entity.ai.pathfinding;

public interface ICustomSizeNavigator {

    boolean isSmallerThanBlock();
    float getXZNavSize();
    int getYNavSize();
}
