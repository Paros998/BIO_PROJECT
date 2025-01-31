package psk.bio.car.rental.infrastructure.data.pagini;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class PageMapperTest {

    @Test
    void shouldCreatePageRequestWithValidPositiveIntegerInputs() {
        // given
        Integer page = 1;
        Integer pageLimit = 10;
        String sortDir = "ASC";
        String sortBy = "id";

        // when
        PageRequest result = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);

        // then
        assertNotNull(result);
        assertEquals(page, result.page());
        assertEquals(pageLimit, result.pageLimit());
        assertEquals(sortDir, result.sortDir());
        assertEquals(sortBy, result.sortBy());
    }
    
    @Test
void shouldCreatePageRequestWithNonEmptyStringForSortBy() {
    // given
    Integer page = 2;
    Integer pageLimit = 20;
    String sortDir = "DESC";
    String sortBy = "name";

    // when
    PageRequest result = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);

    // then
    assertNotNull(result);
    assertEquals(page, result.page());
    assertEquals(pageLimit, result.pageLimit());
    assertEquals(sortDir, result.sortDir());
    assertEquals(sortBy, result.sortBy());
    assertFalse(result.sortBy().isEmpty());
    
}
    @Test
void shouldHandleMaximumIntegerValuesForPageAndPageLimit() {
    // given
    Integer page = Integer.MAX_VALUE;
    Integer pageLimit = Integer.MAX_VALUE;
    String sortDir = "ASC";
    String sortBy = "id";

    // when
    PageRequest result = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);

    // then
    assertNotNull(result);
    assertEquals(page, result.page());
    assertEquals(pageLimit, result.pageLimit());
    assertEquals(sortDir, result.sortDir());
    assertEquals(sortBy, result.sortBy());
}
  @Test
void shouldCreatePageRequestWithEmptyStringForSortBy() {
    // given
    Integer page = 1;
    Integer pageLimit = 10;
    String sortDir = "ASC";
    String sortBy = "";

    // when
    PageRequest result = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);

    // then
    assertNotNull(result);
    assertEquals(page, result.page());
    assertEquals(pageLimit, result.pageLimit());
    assertEquals(sortDir, result.sortDir());
    assertEquals(sortBy, result.sortBy());
    assertTrue(result.sortBy().isEmpty());
}  

}
