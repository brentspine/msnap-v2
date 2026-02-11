package net.msnap.contracts.datatypes;

public enum MicrosoftAccountActiveState {

    OBTAINED("We obtained the account but havent changed stuff"),
    SECURING("We are currently in the process of securing the account"),
    SECURED("The account is secured"),
    SECURING_FAILED("We failed to secure the account..."),
    LOST("We lost access to the account"),
    SOLD("Account was sold"),
    RETIRED("The account is probably going to stay unused because of various reasons, like expired licenses");

    private String description;

    MicrosoftAccountActiveState(String description) {
        this.description = description;
    }

}
