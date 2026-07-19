package com.github.nightdev.kitEngine.manager.result;

public interface KitResult {
    boolean success();
    String msg();

    static KitResult create(boolean success, String msg){
        return new KitResult() {
            @Override
            public boolean success() {
                return success;
            }

            @Override
            public String msg() {
                return msg;
            }
        };
    }
}
