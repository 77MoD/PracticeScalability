package dev.m7mqd.practice.api.area;

import dev.m7mqd.practice.api.base.Arena;
import dev.m7mqd.practice.api.utils.Position;

import java.util.Optional;

public interface Area extends Arena {
    Position getMinPoint();
    Position getMaxPoint();
    default Optional<Position> getMainPoint(){
        return Optional.empty();
    }
}
