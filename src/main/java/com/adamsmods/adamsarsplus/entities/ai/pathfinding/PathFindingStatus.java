package com.adamsmods.adamsarsplus.entities.ai.pathfinding;

public enum PathFindingStatus
{
    IN_PROGRESS_COMPUTING,
    IN_PROGRESS_FOLLOWING,
    CALCULATION_COMPLETE,
    // Marks the path as finished by an entity, aka it walked over it and reached the end
    COMPLETE,
    CANCELLED
}
