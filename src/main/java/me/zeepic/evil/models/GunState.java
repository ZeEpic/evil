package me.zeepic.evil.models;

import lombok.Getter;

public enum GunState {

    CAN_FIRE(true), RELOADING(false), COOLDOWN(false), CAN_RELOAD(false);

    public static final int GUN_COOLDOWN = 2;
    @Getter private final boolean ableToFire;

    GunState(boolean ableToFire) {
        this.ableToFire = ableToFire;
    }
}
