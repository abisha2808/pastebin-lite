	package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Paste;
import com.example.demo.Service.PasteService;

@RestController
@RequestMapping("/api")
public class PasteController {
	private final PasteService service;

    public PasteController(PasteService service) {
        this.service = service;
    }

    // Health check
    @GetMapping("/healthz")
    public Map<String, Boolean> health() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("ok", true);
        return response;
    }

    // Create paste
    @PostMapping("/pastes")
    public ResponseEntity<?> createPaste(@RequestBody Map<String, Object> body) {
        String content = (String) body.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "content is required"));
        }

        Integer ttl = body.get("ttl_seconds") != null ? (Integer) body.get("ttl_seconds") : null;
        Integer maxViews = body.get("max_views") != null ? (Integer) body.get("max_views") : null;

        Paste paste = service.createPaste(content, ttl, maxViews);

        String url = "/p/" + paste.getId();
        return ResponseEntity.ok(Map.of("id", paste.getId(), "url", url));
    }

    // Fetch paste via API
    @GetMapping("/pastes/{id}")
    public ResponseEntity<?> getPaste(@PathVariable Long id) {
        return service.getPaste(id)
                .map(p -> ResponseEntity.ok(Map.of(
                        "content", p.getContent(),
                        "remaining_views", p.getMaxViews() != null ? p.getMaxViews() - p.getViews() : null,
                        "expires_at", p.getExpiresAt()
                )))
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Paste not found")));
    }
    
    
    @GetMapping("/p/{id}")
    public ResponseEntity<String> viewPaste(@PathVariable Long id) {
        return service.getPaste(id)
                .map(p -> ResponseEntity.ok()
                        .header("Content-Type", "text/html")
                        .body("<pre>" + escapeHtml(p.getContent()) + "</pre>"))
                .orElse(ResponseEntity.status(404).body("<h1>Paste not found</h1>"));
    }

    private String escapeHtml(String s) {	
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

}
