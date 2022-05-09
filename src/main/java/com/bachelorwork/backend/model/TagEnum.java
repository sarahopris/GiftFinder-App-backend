package com.bachelorwork.backend.model;

public enum TagEnum {
    //AGE
    BABY,
    KID,
    TEEN,
    YOUNG_ADULT,
    ADULT,
    SENIOR,

    //WHAT IS TRUE ABOUT THEM
    DRIVER,
    DRINKER,
    STRESS,
    APARTMENT,
    HOUSE,
    PET,
    SPORT,
    PARENT,

    // OCCASION
    BIRTHDAY,
    WINTER_HOLLYDAY,
    ROMANTIC_OCCASION,
    PERSONAL_ACHIEVEMENT,
    NO_OCCASION,
    INVITED;

    //Relationship
    enum FEMALE {
        GIRLFRIEND,
        MOM,
        GRANDMOTHER,
        FRIEND,
        WIFE,
        SISTER,
        DAUGHTER,
        GRANDDAUGHTER,
        OTHER_RELATIVE,
        WORK_COLLEAGUE
    };
    enum MALE{
        BOYFRIEND,
        FATHER,
        GRANDFATHER,
        FRIEND,
        HUSBAND,
        BROTHER,
        SON,
        GRANDSON,
        OTHER_RELATIVE,
        WORK_COLLEAGUE
    }

}
