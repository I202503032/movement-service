package pe.edu.cibertec.movementservice.service;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pe.edu.cibertec.movementservice.restclient.inventory.iclient.InventoryClient;
import pe.edu.cibertec.movementservice.restclient.inventory.model.MovimientoResponse;
import pe.edu.cibertec.movementservice.restclient.inventory.model.ProductoResponse;

import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ReporteService {
    private static final int STOCK_MINIMO_POR_DEFECTO = 5;

    private final InventoryClient inventoryClient;

    public List<ProductoResponse> getProductos() {
        return llamarInventario(inventoryClient::getProductos);
    }

    public List<ProductoResponse> getStockCritico() {
        return getProductos()
                .stream()
                .filter(p -> p.getCantidad() < stockMinimo(p))
                .toList();
    }

    public List<MovimientoResponse> getMovimientos(Integer productoId) {
        return llamarInventario(() -> inventoryClient.getMovimientos(productoId));
    }

    public int stockMinimo(ProductoResponse producto) {
        return producto.getStockMinimo() != null ? producto.getStockMinimo() : STOCK_MINIMO_POR_DEFECTO;
    }

    private <T> T llamarInventario(Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (RetryableException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "inventory-service no disponible");
        }
    }
}
