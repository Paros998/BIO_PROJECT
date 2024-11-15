package psk.bio.car.rental.application.security.errors;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SecureExceptionMapper {
//    @Override
//    public Response toResponse(final Exception e) {
//        log.error("error", e);
//        if (e instanceof WebApplicationException ex && ex.getResponse().getStatus() == BUSINESS.status()) {
//            return mapBusinessException(ex);
//        }
//        if (e instanceof WebApplicationException ex) {
//            return mapException(ex);
//        }
//        return mapException();
//    }
//
//    private Response mapBusinessException(final WebApplicationException e) {
//        final var res = ErrorResponse.builder()
//                .status(BUSINESS.status())
//                .code(BUSINESS_ERROR_CODE)
//                .businessError(e.getMessage())
//                .build();
//        return Response.status(BUSINESS.status()).entity(res).type(MediaType.APPLICATION_JSON_TYPE).build();
//    }
//
//    private Response mapException(final WebApplicationException e) {
//        final var res = ErrorResponse.builder()
//                .status(e.getResponse().getStatus())
//                .code(SERVER_ERROR_CODE)
//                .build();
//        return Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).entity(res).type(MediaType.APPLICATION_JSON_TYPE).build();
//    }
//
//    private Response mapException() {
//        final var res = ErrorResponse.builder()
//                .status(INTERNAL_SERVER_ERROR.getStatusCode())
//                .code(SERVER_ERROR_CODE)
//                .build();
//        return Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).entity(res).type(MediaType.APPLICATION_JSON_TYPE).build();
//    }
}
