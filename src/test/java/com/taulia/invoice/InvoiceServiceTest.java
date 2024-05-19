package com.taulia.invoice;

import static com.taulia.invoice.InvoiceApplication.MAPPER;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.taulia.invoice.dto.request.InvoiceItemRequestDto;
import com.taulia.invoice.dto.request.InvoiceRequestDto;
import com.taulia.invoice.exception.BuyerNotFoundException;
import com.taulia.invoice.exception.InvoiceNotFoundException;
import com.taulia.invoice.exception.SupplierNotFoundException;
import com.taulia.invoice.persistence.entity.Buyer;
import com.taulia.invoice.persistence.entity.Invoice;
import com.taulia.invoice.persistence.entity.InvoiceItem;
import com.taulia.invoice.persistence.entity.Supplier;
import com.taulia.invoice.persistence.repository.BuyerRepository;
import com.taulia.invoice.persistence.repository.InvoiceRepository;
import com.taulia.invoice.persistence.repository.SupplierRepository;
import com.taulia.invoice.service.InvoiceService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

class InvoiceServiceTest {

  @Mock
  private InvoiceRepository invoiceRepository;

  @Mock
  private BuyerRepository buyerRepository;

  @Mock
  private SupplierRepository supplierRepository;

  @InjectMocks
  private InvoiceService invoiceService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreateInvoice() {
    InvoiceRequestDto request = createInvoiceRequest();

    Buyer buyer = Buyer.create(UUID.fromString(request.buyerId()), "BuyerName");
    Supplier supplier = Supplier.create(UUID.fromString(request.supplierId()), "SupplierName");

    List<InvoiceItem> invoiceItems = Arrays.asList(
        InvoiceItem.create(null, "Item 1", 2L, BigDecimal.TEN),
        InvoiceItem.create(null, "Item 2", 3L, BigDecimal.valueOf(15.0))
    );

    when(buyerRepository.findById(any(UUID.class))).thenReturn(Optional.of(buyer));
    when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));
    when(invoiceRepository.save(any(Invoice.class)))
        .thenReturn(Invoice.create(null, 123L, buyer, supplier, invoiceItems,
        LocalDate.now(), LocalDate.now()));

    Invoice result = invoiceService.create(request);

    assertNotNull(result);
    assertEquals(request.invoiceNumber(), result.getInvoiceNumber());
    assertEquals(buyer, result.getBuyer());
    assertEquals(supplier, result.getSupplier());
    assertEquals(invoiceItems.size(), result.getItems().size());
  }

  private static InvoiceRequestDto createInvoiceRequest() {
    List<InvoiceItemRequestDto> itemDtos = Arrays.asList(
        new InvoiceItemRequestDto("Item 1", 2L, BigDecimal.TEN),
        new InvoiceItemRequestDto("Item 2", 3L, BigDecimal.valueOf(15.0))
    );

    InvoiceRequestDto request = new InvoiceRequestDto(123L, UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), LocalDate.now(), itemDtos);
    return request;
  }

  @Test
  public void testCreateInvoice_BuyerNotFoundException() {
    InvoiceRequestDto request = new InvoiceRequestDto(123L, UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), LocalDate.now(), new ArrayList<>());

    when(buyerRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(BuyerNotFoundException.class, () -> {
      invoiceService.create(request);
    });
  }

  @Test
  public void testCreateInvoice_SupplierNotFoundException() {
    InvoiceRequestDto request = new InvoiceRequestDto(123L, UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), LocalDate.now(), new ArrayList<>());

    Buyer buyer = Buyer.create(UUID.randomUUID(), "Alex");

    when(buyerRepository.findById(any(UUID.class))).thenReturn(Optional.of(buyer));
    when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(SupplierNotFoundException.class, () -> {
      invoiceService.create(request);
    });
  }

  @Test
  public void testCreateInvoice_DataIntegrityViolationException() {
    InvoiceRequestDto request = new InvoiceRequestDto(123L, UUID.randomUUID().toString(),
        UUID.randomUUID().toString(), LocalDate.now(), new ArrayList<>());

    Buyer buyer = Buyer.create(UUID.randomUUID(), "Alex");
    Supplier supplier = Supplier.create(UUID.randomUUID(), "Tehnopolis");

    when(buyerRepository.findById(any(UUID.class))).thenReturn(Optional.of(buyer));
    when(supplierRepository.findById(any(UUID.class))).thenReturn(Optional.of(supplier));
    when(invoiceRepository.save(any(Invoice.class))).thenThrow(DataIntegrityViolationException.class);

    assertThrows(DataIntegrityViolationException.class, () -> {
      invoiceService.create(request);
    });
  }

  @Test
  public void testDeleteInvoice_Success() {
    UUID invoiceId = UUID.randomUUID();
    when(invoiceRepository.existsById(invoiceId)).thenReturn(true);

    invoiceService.delete(invoiceId);

    verify(invoiceRepository, times(1)).deleteById(invoiceId);
  }

  @Test
  public void testDeleteInvoice_InvoiceNotFoundException() {
    UUID invoiceId = UUID.randomUUID();
    when(invoiceRepository.existsById(invoiceId)).thenReturn(false);

    assertThrows(InvoiceNotFoundException.class, () -> {
      invoiceService.delete(invoiceId);
    });
  }

  @Test
  public void testUpdateInvoice_Success() throws JsonProcessingException {
    InvoiceRequestDto request = createInvoiceRequest();

    Buyer buyer = Buyer.create(UUID.fromString(request.buyerId()), "BuyerName");
    Supplier supplier = Supplier.create(UUID.fromString(request.supplierId()), "SupplierName");

    List<InvoiceItem> invoiceItems = Arrays.asList(
        InvoiceItem.create(null, "Item 1", 2L, BigDecimal.TEN),
        InvoiceItem.create(null, "Item 2", 3L, BigDecimal.valueOf(15.0))
    );
    Invoice invoice = Invoice.create(null, 123L, buyer, supplier,
        invoiceItems, LocalDate.now(), LocalDate.now());

    String patchString = "[\n" +
        "  { \"op\": \"replace\", \"path\": \"/dueDate\", \"value\": \"2023-05-12\" }\n" +
        "]";

    JsonNode patch = MAPPER.readTree(patchString);

    Invoice updatedInvoice = Invoice.create(null, 123L, buyer, supplier,
        invoiceItems, LocalDate.now(), LocalDate.parse("2023-05-12"));
    when(invoiceRepository.findById(invoice.getId())).thenReturn(Optional.of(invoice));
    when(invoiceRepository.save(any(Invoice.class))).thenReturn(updatedInvoice);

    Invoice returnedInvoice = invoiceService.update(invoice.getId(), patch);

    assertNotNull(returnedInvoice);
    assertEquals(LocalDate.parse("2023-05-12"), returnedInvoice.getDueDate());
  }


  @Test
  public void testUpdateInvoice_InvoiceNotFoundException() {
    UUID invoiceId = UUID.randomUUID();
    JsonNode patch = null;

    when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

    assertThrows(InvoiceNotFoundException.class, () -> {
      invoiceService.update(invoiceId, patch);
    });
  }
}
