package psk.bio.car.rental.api.common.paging;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class PageResponse<T> implements Serializable {
    private final Integer currentPage;
    private final Integer totalPages;
    private final List<T> content;
}
