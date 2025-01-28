package psk.bio.car.rental.application.vehicle;


/**
 * NEW -> NOT_INSURED // via employee
 * NEW -> IN_REPAIR // via employee
 * ........................
 * NOT_INSURED -> IN_REPAIR // via employee
 * NOT_INSURED -> READY_TO_RENT // via employee
 * ........................
 * IN_REPAIR -> READY_TO_RENT // via employee and the check for insurance validity
 * ........................
 * READY_TO_RENT -> RENTED // via client
 * READY_TO_RENT -> NOT_INSURED // AUTOMATIC via scheduled checker
 * ........................
 * RENTED -> JUST_RETURNED // via employee
 * ........................
 * JUST_RETURNED -> READY_TO_RENT // via employee
 * JUST_RETURNED -> IN_REPAIR // via employee
 * JUST_RETURNED -> NOT_INSURED // via employee
 */
public enum VehicleState {
    RENTED,
    JUST_RETURNED,
    READY_TO_RENT,
    IN_REPAIR,
    NOT_INSURED,
    NEW
}
