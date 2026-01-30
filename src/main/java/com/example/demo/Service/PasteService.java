package com.example.demo.Service;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.Paste;
import com.example.demo.Repository.PasteRepository;

@Service	
public class PasteService {
	private final PasteRepository repository;

    public PasteService(PasteRepository repository) {
        this.repository = repository;
    }

    public Paste createPaste(String content, Integer ttlSeconds, Integer maxViews) {
        Paste paste = new Paste();
        paste.setContent(content);

        if (ttlSeconds != null && ttlSeconds >= 1) {
            paste.setExpiresAt(Instant.now().plusSeconds(ttlSeconds));
        }

        if (maxViews != null && maxViews >= 1) {
            paste.setMaxViews(maxViews);
        }

        return repository.save(paste);
    }

    public Optional<Paste> getPaste(Long id) {
        Optional<Paste> optPaste = repository.findById(id);

        if (optPaste.isEmpty()) return Optional.empty();

        Paste paste = optPaste.get();

        // check TTL
        if (paste.getExpiresAt() != null && Instant.now().isAfter(paste.getExpiresAt())) {
            repository.delete(paste);
            return Optional.empty();
        }

        // check views
        if (paste.getMaxViews() != null && paste.getViews() >= paste.getMaxViews()) {
            repository.delete(paste);
            return Optional.empty();
        }

        // increment views
        paste.setViews(paste.getViews() + 1);
        repository.save(paste);

        return Optional.of(paste);
    }
    public Optional<Paste> getPasteForView(Long id) {
        return repository.findById(id);
    }

}
