package com.busreservation.service.implementations;

import com.busreservation.dto.CancelRequest;
import com.busreservation.model.Booking;
import com.busreservation.model.Cancellation;
import com.busreservation.model.enums.BookingStatus;
import com.busreservation.repository.BookingRepository;
import com.busreservation.repository.CancellationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancellationServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CancellationRepository cancellationRepository;

    @InjectMocks
    private CancellationServiceImpl cancellationService;

    private Booking mockBooking;
    private CancelRequest validRequest;

    @BeforeEach
    void setUp() {
        mockBooking = new Booking();
        mockBooking.setId(1L);
        mockBooking.setStatus(BookingStatus.CONFIRMED);

        validRequest = new CancelRequest(1L, "Change of plans");
    }

    @Test
    void testCancelBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(mockBooking));

        Booking result = cancellationService.cancelBooking(validRequest);

        // Verify that the booking status and cancelledAt were updated
        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        assertNotNull(result.getCancelledAt());

        // Verify booking save is called
        verify(bookingRepository, times(1)).save(mockBooking);

        // Capture and verify cancellation entity
        ArgumentCaptor<Cancellation> cancellationCaptor = ArgumentCaptor.forClass(Cancellation.class);
        verify(cancellationRepository, times(1)).save(cancellationCaptor.capture());

        Cancellation capturedCancellation = cancellationCaptor.getValue();
        assertEquals(mockBooking, capturedCancellation.getBooking());
        assertEquals("Change of plans", capturedCancellation.getReason());
        assertNotNull(capturedCancellation.getCancellationTime());
    }

    @Test
    void testCancelBooking_BookingNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> cancellationService.cancelBooking(validRequest));

        // Verify that neither save method is called
        verify(bookingRepository, never()).save(any(Booking.class));
        verify(cancellationRepository, never()).save(any(Cancellation.class));
    }
}
