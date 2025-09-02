package com.busreservation.controller;

import com.busreservation.model.Ticket;
import com.busreservation.repository.TicketRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketRepository ticketRepository;

    public TicketController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/{ticketId}/download")
    public ResponseEntity<Resource> downloadTicket(@PathVariable Long ticketId) throws IOException {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        Path path = Paths.get(ticket.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path.getFileName().toString());
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(path))
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
}
