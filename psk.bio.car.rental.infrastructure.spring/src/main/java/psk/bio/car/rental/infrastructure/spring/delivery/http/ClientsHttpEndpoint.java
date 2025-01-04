package psk.bio.car.rental.infrastructure.spring.delivery.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psk.bio.car.rental.api.clients.ClientModel;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.application.user.ClientService;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/clients")
@RestController
public class ClientsHttpEndpoint {
    private final ClientService clientService;

    @GetMapping
    public PageResponse<ClientModel> fetchClients(final @RequestParam(required = false, defaultValue = "1") Integer page,
                                                    final @RequestParam(required = false, defaultValue = "10") Integer pageLimit,
                                                    final @RequestParam(required = false, defaultValue = "desc") String sortDir,
                                                    final @RequestParam(required = false, defaultValue = "firstName") String sortBy) {
        final PageRequest pageRequest = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);
        return clientService.fetchClients(pageRequest);
    }

    @PostMapping("/{userId}/activate")
    public void setClientActiveState(final @PathVariable("userId") UUID clientId, final @RequestParam Boolean setActive) {
        clientService.setClientActiveState(clientId, setActive);
    }
}
