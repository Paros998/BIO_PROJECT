package psk.bio.car.rental.infrastructure.data.common.paging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class SpringPageResponse<T> {
    private final Integer currentPage;
    private final Integer totalPages;
    private final List<T> content;

    public SpringPageResponse(final Page<T> page) {
        content = page.getContent();
        currentPage = page.getNumber() + 1;
        totalPages = page.getTotalPages();
    }
}
