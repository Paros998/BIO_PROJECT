package psk.bio.car.rental.api.common.paging;

public record PageRequest(Integer page, Integer pageLimit, String sortDir, String sortBy) {
}
