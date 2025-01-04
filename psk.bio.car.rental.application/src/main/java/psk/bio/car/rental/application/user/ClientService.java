package psk.bio.car.rental.application.user;

import lombok.NonNull;
import psk.bio.car.rental.api.clients.ClientModel;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.security.FinishRegisterRequest;

import java.util.UUID;

public interface ClientService {
    UUID registerNewClient(@NonNull String email, @NonNull String password);

    void finishRegistration(@NonNull UUID userId, @NonNull FinishRegisterRequest request);

    PageResponse<ClientModel> fetchClients(@NonNull PageRequest pageRequest);

    void setClientActiveState(UUID clientId, boolean newState);
}
