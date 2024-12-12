package psk.bio.car.rental.infrastructure.data.common.paging;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;

import java.util.List;
import java.util.function.Function;

@UtilityClass
@SuppressWarnings("unused")
public class PageMapper {
    public PageRequest toPageRequest(final @NonNull Integer page, final @NonNull Integer pageLimit, final @NonNull String sortDir,
                                     final @NonNull String sortBy) {
        return new PageRequest(page, pageLimit, sortDir, sortBy);
    }

    public <T> PageResponse<T> toPageResponse(final @NonNull SpringPageResponse<T> response) {
        return new PageResponse<>(response.getCurrentPage(), response.getTotalPages(), response.getContent());
    }

    public <T, R> PageResponse<R> toPageResponse(final @NonNull SpringPageResponse<T> response, final @NonNull Function<T, R> mapper) {
        List<R> result = response.getContent().stream().map(mapper).toList();

        return new PageResponse<>(response.getCurrentPage(), response.getTotalPages(), result);
    }

    public SpringPageRequest toSpringPageRequest(final @NonNull PageRequest request) {
        return new SpringPageRequest(request.page(), request.pageLimit(), request.sortDir(), request.sortBy());
    }
}
