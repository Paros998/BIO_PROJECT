package psk.bio.car.rental.infrastructure.data.common.paging;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

@Data
public class SpringPageRequest {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_PAGE_SIZE = 20;

    private static final String SORT_ASC = "asc";
    private static final String SORT_DESC = "desc";
    private static final String DEFAULT_SORT = SORT_ASC;

    private Integer page;
    private Integer pageLimit;
    private String sortDir;
    private String sortBy;

    @SuppressWarnings("checkstyle:HiddenField")
    public SpringPageRequest(final Integer page, final Integer pageLimit, final String sortDir, final String sortBy) {

        this.page = DEFAULT_PAGE;
        this.pageLimit = DEFAULT_PAGE_SIZE;
        this.sortDir = DEFAULT_SORT;

        if (page != null) {
            this.page = page;
        }

        if (pageLimit != null) {
            this.pageLimit = pageLimit;
        }

        if (sortDir != null) {
            this.sortDir = sortDir.toLowerCase();
        }

        if (sortBy != null) {
            this.sortBy = sortBy;
        }
    }

    public <T> PageRequest getRequest(final Class<T> cls) {
        List<Field> fields = Arrays.asList(cls.getDeclaredFields());
        Class<?> superclass = cls.getSuperclass();

        List<Field> superFields = new ArrayList<>();

        if (superclass != null) {
            superFields = Arrays.asList(superclass.getDeclaredFields());
        }

        Predicate<Field> fieldPredicate = field -> field.getName().equals(sortBy);

        if (fields.stream().noneMatch(fieldPredicate) && superFields.stream().noneMatch(fieldPredicate)) {

            String[] clazzAndMember = sortBy.split("\\.", 2);

            Class<?> clazz = fields.stream()
                    .filter(field -> field.getName().equals(clazzAndMember[0]))
                    .findFirst()
                    .orElseThrow(
                            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No member with class %s exists on this entity",
                                    clazzAndMember[0]))
                    )
                    .getType();

            AtomicBoolean fieldMatches = new AtomicBoolean(false);

            if (Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> field.getName().equals(clazzAndMember[1]))) {
                fieldMatches.set(true);
            }

            if (!fieldMatches.get()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("No field %s exists on this member class",
                        clazzAndMember[1]));
            }

        }

        if (!sortDir.equals(SORT_ASC) && !sortDir.equals(SORT_DESC)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sort Direction must be ASC or DESC");
        }

        if (page < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page Number cannot be less than 1");
        }

        if (pageLimit < -1 || pageLimit == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page Size cannot be less than -1 or equal 0");
        }

        if (pageLimit == -1) {
            return PageRequest.of(page - 1, Integer.MAX_VALUE, Sort.Direction.valueOf(sortDir.toUpperCase()), sortBy);
        }

        return PageRequest.of(page - 1, pageLimit, Sort.Direction.valueOf(sortDir.toUpperCase()), sortBy);
    }

}
