package com.github.nightdev.kitEngine.manager.result;

public enum KitResults implements KitResult{
    SUCCESS(KitResult.create(true, "You have successfully claimed this kit!")),

    FAIL_COOLDOWN(KitResult.create(false, "You are on cooldown!")),
    FAIL_COST(KitResult.create(false, "You cannot afford that!")),
    FAIL_PERMISSION(KitResult.create(false, "You do not have permission!")),
    FAIL_REQUIREMENTS(KitResult.create(false, "You do not meet all of the requirements!")),
    FAIL_USES(KitResult.create(false, "You have already reached the max amount of uses!")),
    FAIL_REQUIRES_EMPTY_INVENTORY(KitResult.create(false, "Your inventory is not empty!")),

    ERROR(KitResult.create(false, "An unknown error has occurred."))
    ;

    private final KitResult result;

    KitResults(KitResult result) {
        this.result = result;
    }

    @Override
    public boolean success() {
        return result.success();
    }

    @Override
    public String msg() {
        return result.msg();
    }

    public KitResult get() {
        return this.result;
    }

    public static KitResult error(String error) {
        return KitResult.create(false, error);
    }
}
