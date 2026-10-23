package pe.edu.cibertec.movementservice.restclient.errorhandler;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());

        return switch (status) {
            case NOT_FOUND -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "El recurso no fue encontrado");
            case BAD_REQUEST -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Solicitud Incorrecta");
            case UNAUTHORIZED, FORBIDDEN -> new ResponseStatusException(
                    status, "Sin permiso en el servicio remoto");
            case SERVICE_UNAVAILABLE -> new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE, "Servicio remoto no disponible");
            default -> new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Error en el servicio remoto " + status);
        };

    }

}
