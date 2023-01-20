package dev.m7mqd.practice.api.utils;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data()
@Builder(setterPrefix = "set")
public class Position {
    private long x, y, z;
    public Position(){

    }
    public Position(long x, long y, long z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public static Position of(long x, long y, long z){
        return new Position(x, y, z);
    }
}
