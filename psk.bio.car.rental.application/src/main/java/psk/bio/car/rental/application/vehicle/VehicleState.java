package psk.bio.car.rental.application.vehicle;


/**
 * NEW -> INSURED // via employee ✔️
 * ........................
 * NOT_INSURED -> INSURED // via employee ✔️
 * ........................
 * INSURED -> IN_REPAIR // via employee ✔️
 * INSURED -> READY_TO_RENT // via employee ✔️
 * ........................
 * IN_REPAIR -> READY_TO_RENT // via employee and then check for insurance validity ✔️
 * charge company ✔️ ; charge client ✔️
 * ........................
 * READY_TO_RENT -> RENTED // via client ✔️
 * READY_TO_RENT -> NOT_INSURED // AUTOMATIC via scheduled checker ✔️
 * ........................
 * RENTED -> JUST_RETURNED // via employee ✔️
 * ............. -> READY_TO_RENT // via employee and then check for insurance validity ✔️
 * ............. -> IN_REPAIR // via employee ✔️
 */
public enum VehicleState {
    RENTED,
    JUST_RETURNED,
    READY_TO_RENT,
    IN_REPAIR,
    INSURED,
    NOT_INSURED,
    NEW
}
